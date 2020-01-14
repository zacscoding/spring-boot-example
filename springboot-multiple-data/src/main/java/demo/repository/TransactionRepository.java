package demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import demo.domain.Transaction;

/**
 * Abstract transaction dao
 */
@NoRepositoryBean
public interface TransactionRepository<T extends Transaction> extends CrudRepository<T, Long> {
}
