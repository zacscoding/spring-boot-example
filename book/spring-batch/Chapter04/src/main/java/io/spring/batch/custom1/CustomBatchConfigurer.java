package io.spring.batch.custom1;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("customize-configurer")
@Configuration
public class CustomBatchConfigurer extends DefaultBatchConfigurer {

    // ----------------------------------------------
    // JobRepository
    // ----------------------------------------------

    @Autowired
    @Qualifier("repositoryDataSource")
    private DataSource dataSource;

    @Override
    protected JobRepository createJobRepository() throws Exception {
        final JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();

        factoryBean.setDatabaseType(DatabaseType.MYSQL.getProductName());
        factoryBean.setTablePrefix("FOO_");
        factoryBean.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
        factoryBean.setDataSource(dataSource);

        // BatchConfigurer의 메소드는 스프링에서 직접 노출되지 않으므로
        // InitilizerBean.afterPropertiesSet() 및 FactoryBean.getObject() 메소드를 호출해야한다.
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }

    // ----------------------------------------------
    // TransactionManager
    // ----------------------------------------------
    @Autowired
    @Qualifier("batchTransactionManager")
    private PlatformTransactionManager transactionManager;

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    // ----------------------------------------------
    // JobExplorer
    // ----------------------------------------------

    @Override
    protected JobExplorer createJobExplorer() throws Exception {
        final JobExplorerFactoryBean factoryBean = new JobExplorerFactoryBean();

        factoryBean.setDataSource(dataSource);
        factoryBean.setTablePrefix("FOO_");

        // BatchConfigurer의 메소드는 스프링에서 직접 노출되지 않으므로
        // InitilizerBean.afterPropertiesSet() 및 FactoryBean.getObject() 메소드를 호출해야한다.
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }
}
