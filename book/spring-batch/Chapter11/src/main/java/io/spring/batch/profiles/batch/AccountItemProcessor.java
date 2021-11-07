package io.spring.batch.profiles.batch;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import io.spring.batch.profiles.configuration.properties.BatchProperties;
import io.spring.batch.profiles.domain.Account;
import io.spring.batch.profiles.domain.Statement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountItemProcessor implements ItemProcessor<Statement, Statement> {

    private final JdbcTemplate jdbcTemplate;
    private final BatchProperties properties;

    @Override
    public Statement process(Statement item) throws Exception {
        int count = 0;
        if (properties.getProfiles().isCpuEnabled()) {
            count++;
        }
        if (properties.getProfiles().isMemoryEnabled()) {
            count++;
        }

        CountDownLatch countDownLatch = new CountDownLatch(count);
        if (properties.getProfiles().isCpuEnabled()) {
            runForCpuProfiles(countDownLatch);
        }
        if (properties.getProfiles().isMemoryEnabled()) {
            runForMemoryProfiles(countDownLatch);
        }

        final List<Account> accounts = jdbcTemplate.query(
                "select a.account_id," +
                "       a.balance," +
                "       a.last_statement_date," +
                "       t.transaction_id," +
                "       t.description," +
                "       t.credit," +
                "       t.debit," +
                "       t.timestamp " +
                "from account a left join " +
                "    transaction t on a.account_id = t.account_account_id "
                +
                "where a.account_id in " +
                "	(select account_account_id " +
                "	from customer_account " +
                "	where customer_customer_id = ?) " +
                "order by t.timestamp",
                new Object[] { item.getCustomer().getId() },
                new AccountResultSetExtractor());

        item.setAccounts(accounts);

        return item;
    }

    private void runForCpuProfiles(CountDownLatch countDownLatch) {
        Thread t = new Thread(() -> {
            try {
                final int threadCount = 10;
                final CountDownLatch doneSignal = new CountDownLatch(threadCount);

                for (int i = 0; i < threadCount; i++) {
                    final int idx = i;
                    final Thread thread = new Thread(() -> {
                        try {
                            logger.info("Start isProbablePrime loop in thread-{}", idx);
                            for (int j = 0; j < 1000000; j++) {
                                new BigInteger(String.valueOf(j)).isProbablePrime(0);
                            }
                        } finally {
                            doneSignal.countDown();
                        }
                    });
                    thread.setDaemon(true);
                    thread.start();
                }
                doneSignal.await();
            } catch (Exception e) {
                logger.error("Exception occur while waiting count down latch", e);
            } finally {
                countDownLatch.countDown();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void runForMemoryProfiles(CountDownLatch countDownLatch) {
        Thread t = new Thread(() -> {
            try {
                String memoryBuster = "memoryBuster";

                for (int i = 0; i < 200; i++) {
                    memoryBuster += memoryBuster;
                }
            } catch (Exception e) {
                logger.error("Exception occur while waiting count down latch", e);
            } finally {
                countDownLatch.countDown();
            }
        });
        t.setDaemon(true);
        t.start();
    }
}
