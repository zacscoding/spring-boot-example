package demo.batch.transaction;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import demo.transaction.domain.Transaction;
import demo.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionWriter implements ItemWriter<Transaction> {

    private final TransactionRepository transactionRepository;

    @Override
    public void write(List<? extends Transaction> items) throws Exception {
        logger.info("## Will update {}", items.size());
        for (Transaction item : items) {
            logger.info(">> " + item);
        }
    }
}
