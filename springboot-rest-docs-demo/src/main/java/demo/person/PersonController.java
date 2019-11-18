package demo.person;

import java.util.Optional;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zacconding
 * @Date 2019-01-05
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RestController
@RequestMapping("/api/person")
public class PersonController {

    private PersonRepository personRepository;

    @Autowired
    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // default crud api
    @GetMapping("/{id}")
    public ResponseEntity getPerson(@PathVariable Long id) {
        Optional<Person> personOptional = personRepository.findById(id);

        if (!personOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(personOptional.get());
    }

    // q=name+name1&page=0&size=3&sort=id,DESC
    @GetMapping
    public ResponseEntity getPersons(@RequestParam(value = "q", required = false) String query,
                                     Pageable pageable) {
        // has no query
        if (!StringUtils.hasText(query)) {
            return ResponseEntity.ok(personRepository.findAll(pageable));
        }

        // has query
        StringTokenizer tokenizer = new StringTokenizer(query, " ");
        if (tokenizer.countTokens() != 2) {
            logger.warn("invalid query {}. expected pair but {}", query, tokenizer.countTokens());
            return ResponseEntity.badRequest().build();
        }

        String field = tokenizer.nextToken();

        if ("name".equals(field)) {
            return ResponseEntity.ok(personRepository.findAllByName(tokenizer.nextToken(), pageable));
        }

        if ("age".equals(field)) {
            try {
                int age = Integer.parseInt(tokenizer.nextToken());
                return ResponseEntity.ok(personRepository.findAllByAge(age, pageable));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        logger.warn("invalid query {}. has no matching field name.", query);
        return ResponseEntity.badRequest().build();
    }

    @PostMapping
    public ResponseEntity savePerson(@RequestBody Person person) {
        if (!StringUtils.hasText(person.getName())) {
            return ResponseEntity.badRequest().build();
        }

        Person saved = personRepository.save(person);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePerson(@PathVariable Long id, @RequestBody Person person) {
        Optional<Person> personOptional = personRepository.findById(id);

        if (!personOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Person updated = personRepository.save(person);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePerson(@PathVariable Long id) {
        Optional<Person> personOptional = personRepository.findById(id);

        if (!personOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        personRepository.deleteById(id);
        return ResponseEntity.ok(personOptional.get());
    }
}
