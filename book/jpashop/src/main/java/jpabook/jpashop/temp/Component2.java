package jpabook.jpashop.temp;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class Component2 {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void callSecond() {
        JpaTransactionManager

    }
}
