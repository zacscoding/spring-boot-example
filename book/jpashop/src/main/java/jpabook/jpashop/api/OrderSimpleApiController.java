package jpabook.jpashop.api;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * xToOne (i.e ManyToOne, OneToOne)
 *  Order
 *  Order -> Member
 *  Order -> Delivery
 *
 * Response에 리스트 반환X
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> getOrdersV1() {
        // 무한루프
        // 객체 사이클이 존재하는 부분에 @JsonIgnore
        final List<Order> orders = orderRepository.findAll(new OrderSearch());

        // Lazy 강제 초기화
        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getStatus();
        }

        return orders;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> getOrdersV2() {
        // ORDER -> SQL 1번 -> 결과 주문수 2개
        // for 2번
        //  -> SQL 2번 (Member, Delivery)

        // N+1 문제 => 1 (order) + N 회원 + N 배송 => 1 + 2 + 2
        return orderRepository.findAll(new OrderSearch())
                              .stream()
                              .map(SimpleOrderDto::new)
                              .collect(toList());
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> getOrdersV3() {
        return orderRepository.findAllWithMemberDelivery()
                              .stream()
                              .map(SimpleOrderDto::new)
                              .collect(toList());
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> getOrdersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // Lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // Lazy 초기화
        }
    }
}
