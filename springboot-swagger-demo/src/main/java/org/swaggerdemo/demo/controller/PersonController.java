package org.swaggerdemo.demo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swaggerdemo.demo.entity.Person;

/**
 * @author zacconding
 * @Date 2018-01-22
 * @GitHub : https://github.com/zacscoding
 */
@RestController
@RequestMapping(value = "/api/persons", produces = "application/json")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private List<Person> persons;
    private int seq = 0;
    private Gson gson;

    @PostConstruct
    public void setUp() {
        persons = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            Person p = new Person();
            p.setId(seq++);
            p.setName("name" + i);
            p.setAge(i);
            p.setHobby("hobby" + i);
            persons.add(p);
        }
        gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    }


    @GetMapping
    @ApiOperation(value = "Get person all")
    @ApiResponses({
        @ApiResponse(code = 200, message = "", response = String.class)
    })
    public String getAll() {
        logger.info("## request person all");
        return gson.toJson(persons);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get one person by id")
    public String getPersonById(@PathVariable("id") @ApiParam(name = "id", value = "person id") Integer id) {
        for (Person p : persons) {
            if (p.getId().equals(id)) {
                return gson.toJson(p);
            }
        }
        return "NULL";
    }

    @PostMapping()
    @ApiOperation(value = "Add person")
    public String registPerson(Person person) {
        person.setId(seq++);
        persons.add(person);
        return gson.toJson(person);
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "Modify person info")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Success to modify"),
        @ApiResponse(code = 400, message = "Not exist id")
    })
    public ResponseEntity<Void> modifyPerson(@PathVariable("id") @ApiParam(name = "id", value = "person id") Integer id,
        @RequestBody Person person) {

        person.setId(id);
        int idx = persons.indexOf(person);
        if (idx < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Person prev = persons.get(idx);
        prev.setName(person.getName());
        prev.setAge(person.getAge());
        prev.setHobby(person.getHobby());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
