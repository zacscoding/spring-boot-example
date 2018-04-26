package org.boot.aop;

import org.boot.entity.Person;
import org.boot.service.PersonBookService;
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

    @Test
    public void filter() {
        personBookService.findPersons();
        personBookService.findBooks();

        personBookService.getBook(null);
        personBookService.getBook(null);

        personBookService.getPersonMap();
        personBookService.getBookMap();
    }
}
