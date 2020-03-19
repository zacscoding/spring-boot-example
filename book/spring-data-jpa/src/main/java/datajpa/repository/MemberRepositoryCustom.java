package datajpa.repository;

import java.util.List;

import datajpa.entity.Member;

public interface MemberRepositoryCustom {

    List<Member> findMemberCustom();
}
