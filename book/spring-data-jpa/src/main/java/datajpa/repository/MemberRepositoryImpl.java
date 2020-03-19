package datajpa.repository;

import java.util.List;

import javax.persistence.EntityManager;

import datajpa.entity.Member;
import lombok.RequiredArgsConstructor;

// 네이밍은 해당 "repository name + Impl"
// (2.x 부터는 사용자 정의 인터페이스 + Impl 방식도 지원 <== 권장)
// 변경 원하면 @EnableJpaRepositories에서 postfix 변경
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class)
                 .getResultList();
    }
}
