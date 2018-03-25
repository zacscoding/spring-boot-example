package org.batch.process;

import org.batch.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * This makes it easy to wire the code into a batch job that you define further down in this guide
 *
 * @author zacconding
 * @Date 2018-03-25
 * @GitHub : https://github.com/zacscoding
 */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger logger = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public Person process(Person person) throws Exception {
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();

        final Person transformedPerson = new Person(firstName, lastName);

        logger.info("@@ Converting {} into {}", person, transformedPerson);

        return transformedPerson;
    }
}
