package demo.accounts;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * https://github.com/keesun/study/tree/master/rest-api-with-spring
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String username);
}
