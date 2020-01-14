package demo.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import demo.domain.jpa.JpaBlock;
import demo.domain.jpa.JpaTransaction;
import demo.repository.jpa.JpaBlockRepository;
import demo.repository.jpa.JpaTransactionRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableJpaRepositories("demo.repository.jpa")
@RequiredArgsConstructor
public class JpaConfiguration {

    private final JpaInitService initService;

    @PostConstruct
    public void setUp() {
        initService.init();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    public static class JpaInitService {

        private final JpaBlockRepository blockRepository;
        private final JpaTransactionRepository transactionRepository;

        protected void init() {
            blockRepository.save(createBlock(0L, 3));
            blockRepository.save(createBlock(1L, 2));
            blockRepository.save(createBlock(2L, 5));
            blockRepository.save(createBlock(3L, 2));
        }

        private JpaBlock createBlock(Long blockNumber, int txCount) {
            List<JpaTransaction> transactions = new ArrayList<>();

            for (int i = 0; i < txCount; i++) {
                transactions.add(JpaTransaction.create("txhash0" + i, i));
            }

            return JpaBlock.create(blockNumber, "block" + blockNumber, transactions);
        }

    }
}
