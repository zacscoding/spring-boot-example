package org.zerock.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.domain.Member;

/**
 * @author zacconding
 * @Date 2017-12-24
 * @GitHub : https://github.com/zacscoding
 */
public interface MemberRepository extends JpaRepository<Member, String> {

}
