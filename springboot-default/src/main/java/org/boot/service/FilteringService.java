package org.boot.service;

import java.util.Collection;
import org.boot.entity.Book;
import org.boot.entity.Person;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author zacconding
 * @Date 2018-04-27
 * @GitHub : https://github.com/zacscoding
 */

@Service
public class FilteringService {

    public void filterPerson(Person person) {
        if(person != null) {
            if(person.getName().equals("name2")) {
                person.setName("## Secret ##");
                person.setAge(0);
            }
        }
    }

    public void filterPersons(Collection<Person> persons) {
        if(!CollectionUtils.isEmpty(persons)) {
            for(Person person : persons) {
                filterPerson(person);
            }
        }
    }

    public void filterBook(Book book) {
        if(book != null) {
            if("Book2".equals(book.getBookName())) {
                book.setBookName("## Secret ##");
                book.setPrice(0);
            }
        }
    }

    public void filterBooks(Collection<Book> books) {
        if(!CollectionUtils.isEmpty(books)) {
            for(Book book : books) {
                filterBook(book);
            }
        }
    }
}
