package demo.person;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zacconding
 * @Date 2018-12-24
 * @GitHub : https://github.com/zacscoding
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findOneById(Long id);
}