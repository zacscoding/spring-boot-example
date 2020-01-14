package demo.repository.jpa;

import org.springframework.stereotype.Repository;

import demo.domain.jpa.JpaBlock;
import demo.repository.BlockRepository;

/**
 * Implementation of {@link BlockRepository} based on jpa
 */
@Repository
public interface JpaBlockRepository extends BlockRepository<JpaBlock> {
}
