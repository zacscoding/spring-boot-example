package datajpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import datajpa.dto.MemberDto;
import datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // 어노테이션이 없어도 됨
    @Query(name = "Member.findWithNamedQuery")
    List<Member> findWithNamedQuery(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    // ===================== 반환 타입 체크
    List<Member> findListByUsername(String username);

    Member findMemberByUsername(String username);

    Optional<Member> findOptionalMemberByUsername(String username);

    // ===================== 페이징과 정렬
    Page<Member> findByAge(int age, Pageable pageable);
    // Slice<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findCustomCountQueryByAge(int age, Pageable pageable);

    // ===================== 벌크
    // @Modifying(clearAutomatically = true) // 영속성 컨텍스트 초기화
    @Modifying
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // ===================== fetch join

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    // 공통 메소드 오버라이드
    @Override
    @EntityGraph(attributePaths = { "team" })
    List<Member> findAll();

    @EntityGraph(attributePaths = { "team" })
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = { "team" })
    List<Member> findByUsername(@Param("username") String username);

    @EntityGraph("Member.all")
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph2();

    // ===================== JPA Hint & Lock
    // read only를 모든 메소드에 넣는거 보다, 성능 테스트 후 얻는 이점이 있어야..
    @QueryHints(value = {
            @QueryHint(name = "org.hibernate.readOnly", value = "true")
    })
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}