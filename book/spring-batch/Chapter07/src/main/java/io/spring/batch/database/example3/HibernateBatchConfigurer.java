package io.spring.batch.database.example3;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * spring-boot-starter를 사용하는 경우에는 스프링 부트에서 하이버네이트 버전과 유사한 {@link JpaTransactionManager}를 구성해준다.
 */
@Component
public class HibernateBatchConfigurer extends DefaultBatchConfigurer {

    private final PlatformTransactionManager transactionManager;

    @Autowired
    public HibernateBatchConfigurer(DataSource dataSource,
                                    EntityManagerFactory entityManagerFactory) {
        super(dataSource);
        this.transactionManager = new HibernateTransactionManager(entityManagerFactory.unwrap(SessionFactory.class));
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        // 4.2 이전 버전에서는 로그 메시지로 DataSourceTransactionManager 로 출력되는 버그 있음
        return this.transactionManager;
    }
}
