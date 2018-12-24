package demo.person;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

/**
 * @author zacconding
 * @Date 2018-12-24
 * @GitHub : https://github.com/zacscoding
 */
public class PersonResourceV1 extends Resource<Person> {

    public PersonResourceV1(Person person, Link... links) {
        super(person, links);
    }
}
