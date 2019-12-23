//package demo.batch.transaction;
//
//import java.util.List;
//
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.NonTransientResourceException;
//import org.springframework.batch.item.ParseException;
//import org.springframework.batch.item.UnexpectedInputException;
//import org.springframework.batch.item.database.JpaPagingItemReader;
//import org.springframework.stereotype.Component;
//
//import demo.transaction.domain.Transaction;
//import demo.transaction.domain.TransactionState;
//import demo.transaction.repository.TransactionRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
///**
// *
// */
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class TransactionReader extends JpaPagingItemReader<Transaction> {
//
//    private final TransactionRepository transactionRepository;
//
//    @Override
//    protected void doReadPage() {
//        super.doReadPage();
//    }
//
//    //    @Override
////    public List<Transaction> read() throws Exception, UnexpectedInputException,
////                                           ParseException, NonTransientResourceException {
////
////        List<Transaction> transactions = transactionRepository.findAllByStateNot(TransactionState.MINED);
////
////        logger.info("read not mined transaction. size : {}", transactions.size());
////
////        return transactions;
////    }
//}
