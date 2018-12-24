package demo.person;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

/**
 * @author zacconding
 * @Date 2018-12-24
 * @GitHub : https://github.com/zacscoding
 */
public class PersonResourceV1 extends Resource<Person> {

    public PersonResourceV1(Person person, Link... links) {
        super(person, links);
        add(linkTo(PersonControllerV1.class).slash(person.getId()).withSelfRel());
    }
}
