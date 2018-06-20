package org.boot.aop;

import org.boot.entity.Book;
import org.boot.entity.Person;
import org.boot.service.PersonBookService;
import org.boot.util.SimpleLogger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zacconding
 * @Date 2018-04-26
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FilterAdviceTest {

    @Autowired
    private PersonBookService personBookService;

    /**
     * Check by console
     */
    @Test
    public void filter() throws Exception {
        // list
        SimpleLogger.println("## Check Persons");
        SimpleLogger.printJSONPretty(personBookService.findPersons());

        SimpleLogger.println("## Check Books");
        SimpleLogger.printJSONPretty(personBookService.findBooks());

        // object
        Book book = Book.builder().bookName("Book1").price(1000).build();
        SimpleLogger.println("## Check not filtering Book");
        SimpleLogger.printJSONPretty(personBookService.getBook(book));

        SimpleLogger.println("## Check filtering Book");
        book.setBookName("Book2");
        SimpleLogger.printJSONPretty(personBookService.getBook(book));

        Person person = Person.builder().name("name1").age(10).build();
        SimpleLogger.println("## Check not filtering Person");
        SimpleLogger.printJSONPretty(personBookService.getPerson(person));

        person.setName("name2");
        SimpleLogger.println("## Check filtering Person");
        SimpleLogger.printJSONPretty(personBookService.getPerson(person));

        // map
        SimpleLogger.println("## Check filtering Map");
        SimpleLogger.printJSONPretty(personBookService.getPersonMap());
        SimpleLogger.printJSONPretty(personBookService.getBookMap());
    }
}
