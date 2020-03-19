package datajpa.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import datajpa.repository.MemberRepository;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 15, teamA);
        Member member3 = new Member("member3", 20, teamB);
        Member member4 = new Member("member4", 25, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 초기화
        em.flush();
        em.clear();

        // 확인
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                                 .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("-> member.team = " + member.getTeam());
        }
    }

    @Test
    public void testJpaEventBaseEntity() throws Exception {
        // given
        Member member = new Member("member1");
        memberRepository.save(member);

        Thread.sleep(100L);
        member.setUsername("member2");

        em.flush(); // @PreUpdate
        em.clear();

        // when
        Optional<Member> findOptional = memberRepository.findById(member.getId());

        // then
        assertThat(findOptional.isPresent()).isTrue();
        Member find = findOptional.get();
        assertThat(find.getCreatedDate()).isNotNull();
        // assertThat(find.getUpdatedDate()).isNotNull();
        assertThat(find.getLastModifiedDate()).isNotNull();
        assertThat(find.getCreatedBy()).isNotNull();
        assertThat(find.getLastModifiedBy()).isNotNull();
    }
}
