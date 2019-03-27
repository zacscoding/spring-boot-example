package demo.web;

import demo.mapper.PersonMapper;
import demo.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonMapper personMapper;

    @GetMapping("/{id}")
    public ResponseEntity getPerson(@PathVariable("id") Integer id) {
        Person person = personMapper.findById(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(person);
    }

    @GetMapping
    public ResponseEntity getPersons() {
        return ResponseEntity.ok(personMapper.findAll());
    }

    @PostMapping
    public ResponseEntity savePerson(@RequestBody Person person) {
        if (person.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (personMapper.findById(person.getId()) != null) {
            return ResponseEntity.badRequest().build();
        }

        int updated = personMapper.save(person);
        if (updated != 0) {
            return ResponseEntity.ok(person);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
