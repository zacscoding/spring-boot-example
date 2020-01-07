package datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import datajpa.dto.MemberDto;
import datajpa.entity.Member;
import datajpa.entity.Team;

/**
 */
@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository repository;
    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void testSave() {
        // given
        Member member = new Member("userA");

        // when
        Member savedMember = repository.save(member);

        // then
        Optional<Member> findOptional = repository.findById(member.getId());
        assertThat(findOptional.isPresent()).isTrue();
        Member find = findOptional.get();
        assertThat(find.getId()).isEqualTo(savedMember.getId());
        assertThat(find.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(find).isEqualTo(savedMember);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        repository.save(member1);
        repository.save(member2);

        // 단건 조회
        Member find1 = repository.findById(member1.getId()).orElseThrow(() -> new RuntimeException(""));
        Member find2 = repository.findById(member2.getId()).orElseThrow(() -> new RuntimeException(""));

        assertThat(find1).isEqualTo(member1);
        assertThat(find2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = repository.findAll();
        assertThat(all.size()).isEqualTo(2);
        assertThat(all.contains(member1)).isTrue();
        assertThat(all.contains(member2)).isTrue();

        // 카운트 검증
        assertThat(repository.count()).isEqualTo(2);

        // 삭제 검증
        repository.delete(member1);
        repository.delete(member2);

        assertThat(repository.count()).isEqualTo(0);
    }

    @Test
    public void testFindByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        Member m3 = new Member("BBB", 20);

        repository.save(m1);
        repository.save(m2);
        repository.save(m3);

        List<Member> finds = repository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(finds.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        Member m3 = new Member("BBB", 20);

        repository.save(m1);
        repository.save(m2);
        repository.save(m3);

        List<Member> finds = repository.findWithNamedQuery("BBB");
        assertThat(finds.size()).isEqualTo(1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        Member m3 = new Member("BBB", 20);

        repository.save(m1);
        repository.save(m2);
        repository.save(m3);

        List<Member> finds = repository.findUser("AAA", 10);
        assertThat(finds.size()).isEqualTo(1);
        assertThat(finds.get(0)).isEqualTo(m1);
    }

    @Test
    public void testFindUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        Member m3 = new Member("BBB", 20);

        repository.save(m1);
        repository.save(m2);
        repository.save(m3);

        List<String> usernameList = repository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void testFindMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        repository.save(m1);

        List<MemberDto> dataList = repository.findMemberDto();
        for (MemberDto memberDto : dataList) {
            System.out.println("MembetDto ==> " + memberDto);
        }
    }

    @Test
    public void testFindByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        Member m3 = new Member("BBB", 20);
        Member m4 = new Member("CCC", 20);

        repository.saveAll(Arrays.asList(m1, m2, m3, m4));

        List<Member> members = repository.findByNames(Arrays.asList("AAA", "CCC"));
        for (Member member : members) {
            System.out.println("Member ==> " + member);
        }
    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        repository.save(m1);
        repository.save(m2);

        // NoResultException
        Member findMember = repository.findMemberByUsername("AAA");
        repository.findOptionalMemberByUsername("AAA");

        Member ccc = repository.findMemberByUsername("CCC");
        assertThat(ccc).isNull();
        System.out.println("## findMemberByUsername ==> " + findMember);

        List<Member> result = repository.findListByUsername("AAA");
        System.out.println("## findListByUsername ==> " + result.size());
    }

    @Test
    public void paging() {
        // given
        repository.save(new Member("member1", 10));
        repository.save(new Member("member2", 10));
        repository.save(new Member("member3", 10));
        repository.save(new Member("member4", 10));
        repository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));

        // when
        // Page<Member> page = repository.findByAge(age, pageRequest);
        //Slice<Member> page = repository.findByAge(age, pageRequest);
        Page<Member> page = repository.findCustomCountQueryByAge(age, pageRequest);
        Page<MemberDto> dtoPage = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        // then
        assertThat(page.getContent().size()).isEqualTo(pageRequest.getPageSize());
        assertThat(page.getTotalElements()).isEqualTo(5L);
        assertThat(page.getNumber()).isEqualTo(pageRequest.getPageNumber());
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
        assertThat(page.hasNext()).isTrue();

    }
}
