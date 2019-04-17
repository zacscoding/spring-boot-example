package demo.person;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonRepository personRepository;

    // default crud api
    @GetMapping("/{id}")
    public ResponseEntity getPerson(@PathVariable Long id) {
        logger.info("request getPerson(id:{})", id);
        Optional<Person> personOptional = personRepository.findById(id);

        if (!personOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(personOptional.get());
    }

    @GetMapping
    public ResponseEntity getPersons(Pageable pageable) {
        logger.info("request getPersons(pageNumber:{},pageSize:{})"
            , pageable.getPageNumber(), pageable.getPageSize());

        return ResponseEntity.ok(personRepository.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity savePerson(@RequestBody Person person) {
        logger.info("request savePerson({})", person);

        if (!StringUtils.hasText(person.getName())) {
            return ResponseEntity.badRequest().build();
        }

        Person saved = personRepository.save(person);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePerson(@PathVariable Long id, @RequestBody Person person) {
        logger.info("request updatePerson(id:{}, person:{})", id, person);

        Optional<Person> personOptional = personRepository.findById(id);

        if (!personOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Person updated = personRepository.save(person);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePerson(@PathVariable Long id) {
        logger.info("request deletePerson(id:{})", id);

        Optional<Person> personOptional = personRepository.findById(id);

        if (!personOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        personRepository.deleteById(id);
        return ResponseEntity.ok(personOptional.get());
    }
}
