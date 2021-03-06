package org.boot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.boot.configuration.aop.annotation.PostFilter;
import org.boot.configuration.aop.enums.EntityType;
import org.boot.entity.Book;
import org.boot.entity.Person;
import org.springframework.stereotype.Service;

/**
 * @author zacconding
 * @Date 2018-04-26
 * @GitHub : https://github.com/zacscoding
 */
@Service
public class PersonBookService {

    @PostFilter(entityType = EntityType.PERSON)
    public List<Person> findPersons() {
        int size = 5;
        List<Person> persons = new ArrayList<>(size);
        for (int i = 1; i < size; i++) {
            persons.add(Person.builder().name("name" + i).age(i).build());
        }

        return persons;
    }

    @PostFilter(entityType = EntityType.BOOk)
    public List<Book> findBooks() {
        int size = 5;
        List<Book> books = new ArrayList<>(size);
        for (int i = 1; i < size; i++) {
            books.add(Book.builder().bookName("Book" + i).price(5000 * i).build());
        }

        return books;
    }

    @PostFilter(entityType = EntityType.PERSON)
    public Person getPerson(Person person) {
        return person;
    }

    @PostFilter(entityType = EntityType.BOOk)
    public Book getBook(Book book) {
        return book;
    }

    @PostFilter(entityType = EntityType.PERSON, mapsKey = "DATA")
    public Map<String, Object> getPersonMap() {
        Map<String, Object> ret = new HashMap<>();

        int size = 5;
        List<Person> persons = new ArrayList<>(size);
        for (int i = 1; i < size; i++) {
            persons.add(Person.builder().name("name" + i).age(i).build());
        }
        ret.put("DATA", persons);
        return ret;
    }

    @PostFilter(entityType = EntityType.BOOk, mapsKey = "DATA")
    public Map<String, Object> getBookMap() {
        Map<String, Object> ret = new HashMap<>();

        int size = 5;
        List<Book> books = new ArrayList<>(size);
        for (int i = 1; i < size; i++) {
            books.add(Book.builder().bookName("Book" + i).price(5000 * i).build());
        }

        ret.put("DATA", books);
        return ret;
    }
}
