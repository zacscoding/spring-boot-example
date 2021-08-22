package jpabook.jpashop.temp;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class Component1 {

    private Component2 component2;
    private final PlatformTransactionManager transactionManager;

    @Transactional(propagation = Propagation.MANDATORY)
    public void callFirst() {
        TransactionStatus status = this.transactionManager.getTransaction(
                new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_MANDATORY));
        try {
            component2.callSecond();
            this.transactionManager.commit(status);
        } catch (Exception e) {
            this.transactionManager.rollback(status);
        }
    }
}
