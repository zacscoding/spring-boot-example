package demo.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import demo.domain.jpa.JpaTransaction;
import demo.repository.TransactionRepository;

/**
 * Implementation of {@link TransactionRepository} based on jpa
 */
@Repository
public interface JpaTransactionRepository extends TransactionRepository<JpaTransaction> {

    @EntityGraph("block")
    @Override
    Optional<JpaTransaction> findById(Long id);

    @EntityGraph("block")
    @Override
    Iterable<JpaTransaction> findAll();
}
