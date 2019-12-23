package demo.config;

import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import demo.transaction.domain.Transaction;
import demo.transaction.domain.TransactionState;
import demo.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

/**
 *
 */
@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

    private final InitService initService;

    @PostConstruct
    private void setUp() {
        initService.initDb();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final TransactionRepository transactionRepository;

        public void initDb() {
            int size = 'Z' - 'A' + 1;

            for (int i = 0; i < 10; i++) {
                Random random = new Random();
                int fromIdx = random.nextInt(size);
                int toIdx = random.nextInt(size);
                int statusIdx = random.nextInt(3);
                TransactionState status = null;

                switch (statusIdx) {
                    case 0:
                        status = TransactionState.INIT;
                        break;
                    case 1:
                        status = TransactionState.REQUESTED;
                        break;
                    default:
                        status = TransactionState.MINED;
                        break;
                }

                Transaction tx = Transaction.builder()
                                            .fromAddress("" + (char) ('A' + fromIdx))
                                            .toAddress("" + (char) ('A' + toIdx))
                                            .state(status)
                                            .build();
                tx.setFromHashCode(tx.getFromAddress().hashCode());

                transactionRepository.save(tx);
                System.out.println("Success to save transaction : " + tx);
            }
        }
    }
}
