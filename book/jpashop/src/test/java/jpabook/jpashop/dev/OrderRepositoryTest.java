package jpabook.jpashop.dev;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(false)
public class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void testFindAllWithMemberDelivery() {
        orderRepository.findAllWithMemberDelivery();
        System.out.println("## ======================== ##");
        orderRepository.findAllWithMemberDelivery2();
    }

    @Test
    public void testFindAllWithItems() {
        orderRepository.findAllWithItems();
        System.out.println("## ======================== ##");
        orderRepository.findAllWithItems2();
    }
}
