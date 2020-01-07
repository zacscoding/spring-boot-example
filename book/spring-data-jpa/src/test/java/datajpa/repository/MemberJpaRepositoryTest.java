package datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import datajpa.entity.Member;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository repository;

    @Test
    public void testSave() {
        // given
        Member member = new Member("userA");

        // when
        repository.save(member);

        // then
        Member find = repository.findById(member.getId()).get();
        assertThat(find.getId()).isEqualTo(member.getId());
        assertThat(find.getUsername()).isEqualTo(member.getUsername());
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
    public void paging() {
        // given
        repository.save(new Member("member1", 10));
        repository.save(new Member("member2", 10));
        repository.save(new Member("member3", 10));
        repository.save(new Member("member4", 10));
        repository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        // when
        List<Member> members = repository.findByPage(age, offset, limit);
        long totalCount = repository.totalCount(age);

        // then
        assertThat(members.size()).isEqualTo(limit);
        assertThat(totalCount).isEqualTo(5L);
    }
}