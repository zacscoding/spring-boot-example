package org.batch.process;

import org.batch.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-03-25
 * @GitHub : https://github.com/zacscoding
 */
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger logger = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        logger.info("@@ after job is called..  status : {}", jobExecution.getStatus());
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("@@ Job finished ! Time to verity the results");
            jdbcTemplate.query("SELECT first_name, last_name FROM people", (rs, row) -> new Person(rs.getString(1), rs.getString(2)))
                        .forEach(person -> logger.info("Found <{}> in the database", person));
        }
    }

}
