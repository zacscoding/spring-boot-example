package server.api.service;

import java.util.List;
import server.api.Person;

/**
 * @author zacconding
 * @Date 2018-12-03
 * @GitHub : https://github.com/zacscoding
 */
public interface PersonService {

    String save(Person person);

    Person findOneById(String id);

    List<Person> findAll();

    int deleteById(String id);
}
