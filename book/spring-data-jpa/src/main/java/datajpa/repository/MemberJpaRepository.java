package datajpa.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import datajpa.entity.Member;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                 .getSingleResult();
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                 .getResultList();
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age",
                              Member.class)
                 .setParameter("username", username)
                 .setParameter("age", age)
                 .getResultList();
    }

    // paging query
    public List<Member> findWithNamedQuery(String username) {
        return em.createNamedQuery("Member.findWithNamedQuery", Member.class)
                 .setParameter("username", username)
                 .getResultList();
    }

    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc",
                              Member.class)
                 .setParameter("age", age)
                 .setFirstResult(offset)
                 .setMaxResults(limit)
                 .getResultList();
    }

    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                 .setParameter("age", age)
                 .getSingleResult();
    }

    // bulk query
    public int bulkAgePlus(int age) {
        return em.createQuery("update Member m set m.age = m.age+1 where m.age >= :age")
                 .setParameter("age", age)
                 .executeUpdate();
    }
}
