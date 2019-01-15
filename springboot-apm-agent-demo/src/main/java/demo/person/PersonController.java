package demo.person;

import java.util.Optional;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zacconding
 * @Date 2019-01-15
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RestController
@RequestMapping("/person/**")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @PostConstruct
    private void setUp() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            personRepository.save(
                Person.builder()
                    .name("hiva" + i)
                    .hobbies(Person.randomHobbies())
                    .build()
            );
        });
    }

    @GetMapping("/{id}")
    public ResponseEntity getPerson(@PathVariable Long id) {
        Optional<Person> personOptional = personRepository.findById(id);

        if (!personOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(personOptional.get());
    }

    @GetMapping
    public ResponseEntity getPersons(Pageable pageable) {
        return ResponseEntity.ok(personRepository.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity savePerson(@RequestBody Person person) {
        return ResponseEntity.ok(personRepository.save(person));
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePerson(@PathVariable Long id, Person person) {
        Optional<Person> personOptional = personRepository.findById(id);

        if (!personOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        person.setId(id);
        return ResponseEntity.ok(personRepository.save(person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePerson(@PathVariable Long id) {
        Optional<Person> personOptional = personRepository.findById(id);

        if (!personOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        personRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
