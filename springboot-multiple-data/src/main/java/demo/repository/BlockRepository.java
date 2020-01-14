package demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import demo.domain.Block;

/**
 * Abstract transaction dao
 */
@NoRepositoryBean
public interface BlockRepository<T extends Block> extends CrudRepository<T, Long> {
}
