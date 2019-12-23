package demo.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.transaction.domain.Transaction;
import demo.transaction.domain.TransactionState;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByStateNot(TransactionState status);
}
