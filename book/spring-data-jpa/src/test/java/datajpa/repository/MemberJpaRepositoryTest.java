package datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
}