package datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
