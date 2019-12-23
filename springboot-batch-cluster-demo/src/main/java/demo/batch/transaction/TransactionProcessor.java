package demo.batch.transaction;

import java.util.Random;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import demo.transaction.domain.Transaction;
import demo.transaction.domain.TransactionState;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@Component
public class TransactionProcessor implements ItemProcessor<Transaction, Transaction> {

    @Override
    public Transaction process(Transaction transaction) throws Exception {
        TransactionState nextState = null;

        switch (transaction.getState()) {
            case INIT:
                if (new Random().nextInt(3) == 0) {
                    logger.info("Will return null {}", transaction);
                    return null;
                }
                nextState = TransactionState.REQUESTED;
                break;
            case REQUESTED:
                if (new Random().nextInt(3) == 0) {
                    logger.info("Will return null {}", transaction);
                    return null;
                }
                nextState = TransactionState.MINED;
                break;
            case MINED:
                logger.info("Will return null {}", transaction);
                return null;
        }

        transaction.setState(nextState);

        return transaction;
    }
}
