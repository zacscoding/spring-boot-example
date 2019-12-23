package jpabook.jpashop.repository.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

/**
 *
 */
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    // === v6 ===
    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)"
                + " from Order o"
                + " join o.member m"
                + " join o.delivery d"
                + " join o.orderItems oi"
                + " join oi.item i", OrderFlatDto.class)
                 .getResultList();
    }

    // === v5 ===

    public List<OrderQueryDto> findAllByDto_optimization() {
        final List<OrderQueryDto> orders = findOrders();

        final Map<Long, List<OrderItemQueryDto>> orderItemsMap = findOrderItemMap(toOrderIds(orders));

        orders.forEach(o -> o.setOrderItems(orderItemsMap.get(o.getOrderId())));

        return orders;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> orderQueryDtos) {
        return orderQueryDtos.stream()
                             .map(OrderQueryDto::getOrderId)
                             .collect(Collectors.toList());
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)"
                + " from OrderItem oi"
                + " join oi.item i"
                + " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                 .setParameter("orderIds", orderIds)
                 .getResultStream()
                 .collect(Collectors.groupingBy(
                         OrderItemQueryDto::getOrderId));
    }

    // === v4 ===

    public List<OrderQueryDto> findOrderQueryDto() {
        final List<OrderQueryDto> orders = findOrders(); // query 1번 -> 2개

        orders.forEach(o -> {
            final List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return orders;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)"
                + " from OrderItem oi"
                + " join oi.item i"
                + " where oi.order.id = :orderId", OrderItemQueryDto.class)
                 .setParameter("orderId", orderId)
                 .getResultList();
    }

    public List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.query.OrderQueryDto(o.id, m.name,"
                + "o.orderDate, o.status, o.delivery.address) from Order o"
                + " join o.member m"
                + " join o.delivery d", OrderQueryDto.class)
                 .getResultList();
    }
}
