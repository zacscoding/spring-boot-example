package org.swaggerdemo.demo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swaggerdemo.demo.domain.Person;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zacconding
 * @Date 2018-01-22
 * @GitHub : https://github.com/zacscoding
 */
@RestController
@RequestMapping("/person/**")
public class PersonController {
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private List<Person> persons;
    private Gson gson;
    public int lastId = 0;
    @PostConstruct
    public void setUp() {
        persons = new ArrayList<>();
        for(int i=1; i<10; i++) {
            Person p = new Person();
            p.setId(lastId++);
            p.setName("name" + i);
            p.setAge(i);
            p.setHobby("hobby"+i);
            persons.add(p);
        }

        gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    }

    @GetMapping("/all")
    public String getAll() {
        logger.info("## request person all");
        return gson.toJson(persons);
    }

    @GetMapping("/{id}")
    public String getPersonByName(@PathVariable("id") Integer id) {
        for(Person p : persons) {
            if(p.getId().equals(id)) {
                return gson.toJson(p);
            }
        }
        return "NULL";
    }

    @PostMapping("/add")
    public String registPerson(Person person) {
        person.setId(lastId++);
        persons.add(person);
        return gson.toJson(person);
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<Void> modifyPerson(@PathVariable("id") Integer id, Person person) {
        person.setId(id);
        int idx = persons.indexOf(person);
        if(idx < 0) {
            return null;
        }

        Person prev = persons.get(idx);
        prev.setName(person.getName());
        prev.setAge(person.getAge());
        prev.setHobby(person.getHobby());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
