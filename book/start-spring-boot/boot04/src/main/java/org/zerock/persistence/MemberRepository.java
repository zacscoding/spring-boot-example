package org.zerock.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.Member;

import java.util.List;

/**
 * @author zacconding
 * @Date 2017-12-17
 * @GitHub : https://github.com/zacscoding
 */
public interface MemberRepository extends CrudRepository<Member, String> {

    /**
     * 멤버 아이디와 Profile 수를 구하는 메소드
     * " uid가 uid인 회원의 정보 + 회원의 프로필 사진 수를 알고 싶을 때 "
     * => @Query안의 JPQL은 SQL과 유사하지만, 테이블 대신 엔티티를 사용
     * => Table을 Entity Member, Profile 사용 && ON 보면 m.uid = p.member
     */
    @Query("SELECT m.uid, count(p) FROM Member m LEFT OUTER JOIN Profile p " + " ON m.uid = p.member WHERE m.uid = ?1 GROUP BY m")
    public List<Object[]> getMemberWithProfileCount(String uid);

    /**
     * 멤버 아이디와 현재 사용중인 profile 을 구하는 메소드
     * "회원 정보와 현재 사용중인 프로필에 대한 정보를 알고 싶을 때"
     */
    @Query("SELECT m,p FROM Member m LEFT OUTER JOIN Profile p " + " ON m.uid = p.member WHERE m.uid = ?1 AND p.current = true")
    public List<Object[]> getMemberWithProfiles(String uid);


}
