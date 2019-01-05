package demo.person;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zacconding
 * @Date 2019-01-05
 * @GitHub : https://github.com/zacscoding
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

}
