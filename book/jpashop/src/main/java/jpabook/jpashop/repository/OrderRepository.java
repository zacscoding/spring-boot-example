package jpabook.jpashop.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;

@Repository
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public OrderRepository(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        // return findAllByString(orderSearch);
        return findAllByCriteria(orderSearch);

//        final QOrder order = QOrder.order;
//        final QMember member = QMember.member;
//
//        return queryFactory.select(order)
//                           .from(order)
//                           .join(order.member, member)
//                           .where(
//                                   statusEq(orderSearch.getOrderStatus()),
//                                   nameLike(orderSearch.getMemberName())
//                           )
//                           .limit(1000)
//                           .fetch();
    }

    private BooleanExpression statusEq(OrderStatus statusCondition) {
        if (statusCondition == null) {
            return null;
        }

        return QOrder.order.status.eq(statusCondition);
    }

    private BooleanExpression nameLike(String memberName) {
        if (!StringUtils.hasText(memberName)) {
            return null;
        }

        return QMember.member.name.like(memberName);
    }

    // jpa criteria
    // 유지보수성 낮음 => 코드 작성 + 읽기 자체가 어려움
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        final Root<Order> o = cq.from(Order.class);
        final Join<Object, Object> m = o.join("member", JoinType.INNER);

        final List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            criteria.add(cb.equal(o.get("status"), orderSearch.getOrderStatus()));
        }

        // 멤버 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            criteria.add(cb.like(m.get("name"), '%' + orderSearch.getMemberName() + '%'));
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));

        return em.createQuery(cq)
                 .setMaxResults(1000)
                 .getResultList();
    }

    // jpql 문자열 조합
    public List<Order> findAllByString(OrderSearch orderSearch) {
        String qlString = "select o from Order o join o.member m";

        final boolean hasOrderStatus = orderSearch.getOrderStatus() != null;
        final boolean hasMemberName = StringUtils.hasText(orderSearch.getMemberName());

        boolean isFirstCondition = true;

        if (hasOrderStatus) {
            if (isFirstCondition) {
                qlString += " where";
                isFirstCondition = false;
            } else {
                qlString += " and";
            }

            qlString += " m.status = :status";
        }

        if (hasMemberName) {
            if (isFirstCondition) {
                qlString += " where";
                isFirstCondition = false;
            } else {
                qlString += " and";
            }

            qlString += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(qlString, Order.class).setMaxResults(1000);

        if (hasOrderStatus) {
            query.setParameter("status", orderSearch.getOrderStatus());
        }

        if (hasMemberName) {
            query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        // fetch를 100% 이해 해야 함
        return em.createQuery(
                "select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class
        ).getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        // fetch를 100% 이해 해야 함
        return em.createQuery(
                "select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class)
                 .setFirstResult(offset)
                 .setMaxResults(limit)
                 .getResultList();
    }

    public List<Order> findAllWithItems() {
        return em.createQuery(
                "select distinct o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d" +
                " join fetch o.orderItems oi" +
                " join fetch oi.item i", Order.class)
                 // HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
                 //.setFirstResult(1)
                 //.setMaxResults(100)
                 .getResultList();
    }
}
