package demo.person;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Person repository
 *
 * @GitHub : https://github.com/zacscoding
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

}
