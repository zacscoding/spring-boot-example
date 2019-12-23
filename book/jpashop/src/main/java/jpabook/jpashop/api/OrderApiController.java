package jpabook.jpashop.api;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.query.OrderFlatDto;
import jpabook.jpashop.repository.query.OrderItemQueryDto;
import jpabook.jpashop.repository.query.OrderQueryDto;
import jpabook.jpashop.repository.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> getOrdersV1() {
        final List<Order> orders = orderRepository.findAll(new OrderSearch());

        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getStatus();
            order.getOrderItems().forEach(o -> o.getItem().getName());
        }

        return orders;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> getOrdersV2() {
        final List<Order> orders = orderRepository.findAll(new OrderSearch());

        return orders.stream()
                     .map(OrderDto::new)
                     .collect(toList());
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> getOrdersV3() {

        final List<Order> orders = orderRepository.findAllWithItems();

        return orders.stream()
                     .map(OrderDto::new)
                     .collect(toList());

    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> getOrdersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                           @RequestParam(value = "limit", defaultValue = "100") int limit) {
        final List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        return orders.stream()
                     .map(OrderDto::new)
                     .collect(toList());
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> getOrdersV4() {
        return orderQueryRepository.findOrderQueryDto();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> getOrdersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();
        return flats.stream()
                    .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(),
                                                               o.getName(), o.getOrderDate(),
                                                               o.getOrderStatus(), o.getAddress()),
                                        mapping(o -> new OrderItemQueryDto(o.getOrderId(),
                                                                           o.getItemName(), o.getOrderPrice(),
                                                                           o.getCount()), toList())
                    )).entrySet().stream()
                    .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
                                                e.getKey().getName(), e.getKey().getOrderDate(),
                                                e.getKey().getOrderStatus(),
                                                e.getKey().getAddress(), e.getValue()))
                    .collect(Collectors.toList());
    }

    @Data
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems()
                              .stream()
                              .map(OrderItemDto::new)
                              .collect(toList());
        }
    }

    @Data
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
