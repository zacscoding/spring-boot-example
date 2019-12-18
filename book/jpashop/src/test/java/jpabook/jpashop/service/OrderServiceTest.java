package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember("회원1");
        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order find = orderRepository.findOne(orderId);
        assertThat(find.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(find.getOrderItems().size()).isEqualTo(1);
        assertThat(find.getTotalPrice()).isEqualTo(book.getPrice() * orderCount);
        assertThat(book.getStockQuantity()).isEqualTo(10 - orderCount);
    }

    // Item에서 removeStock의 단위테스트가 존재하는게 더 적절
    @Test
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember("회원1");
        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 11;

        // when then
        assertThatThrownBy(() -> orderService.order(member.getId(), book.getId(), orderCount))
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage("need more stock");
    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember("회원1");
        Book book = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancel(orderId);

        // then
        Order find = orderRepository.findOne(orderId);
        assertThat(find.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).isEqualTo(10);
    }

    private Member createMember(String name) {
        Member member = new Member();

        member.setName(name);
        member.setAddress(new Address("서울", "강가", "123-123"));

        entityManager.persist(member);

        return member;
    }

    private Book createBook(String Name, int price, int stockQuantity) {
        Book book = new Book();

        book.setName(Name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);

        entityManager.persist(book);

        return book;
    }
}
