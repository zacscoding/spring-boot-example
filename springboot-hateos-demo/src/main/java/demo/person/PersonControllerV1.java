package demo.person;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zacconding
 * @Date 2018-12-24
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/person")
public class PersonControllerV1 {

    private PersonRepository personRepository;

    @Autowired
    public PersonControllerV1(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @PostConstruct
    private void setUp() {
        saveDummy();
    }

    @GetMapping("/{id}")
    public ResponseEntity getPerson(@PathVariable("id") Long id) {
        Person person = personRepository.findOneById(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }

        PersonResourceV1 resource = new PersonResourceV1(person);
        // adds links
        resource.add(linkTo(methodOn(PersonControllerV1.class).getPerson(person.getId())).withSelfRel());
        resource.add(linkTo(methodOn(PersonControllerV1.class).updatePerson(person.getId(), person)).withRel("update"));
        resource.add(linkTo(methodOn(PersonControllerV1.class).deletePerson(person.getId())).withRel("delete"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping("/all")
    public ResponseEntity getPersons() {
        return null;
    }

    @PostMapping
    public ResponseEntity savePerson(@RequestBody Person person) {
        return null;
    }

    @PostMapping(value = "/update/{id}")
    public ResponseEntity updatePerson(@PathVariable("id") Long id, @RequestBody Person person) {
        return null;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deletePerson(@PathVariable("id") Long id) {
        return null;
    }

    ////////////////////// for dev
    private void saveDummy() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Person person = Person.builder()
                .name("Hiva" + i)
                .age(i * 10 + 5)
                .hobbies(randomHobies())
                .build();

            Person savedPerson = personRepository.save(person);
            log.info("[Created dummy person] " + savedPerson);
        });
    }

    private List<String> randomHobies() {
        String[] defaultHobies = {"coding", "book", "movie", "math"};

        List<String> hobbies = new ArrayList<>(4);
        Random random = new Random();

        for (String defaultHobby : defaultHobies) {
            if (random.nextInt() % 4 == 0) {
                hobbies.add(defaultHobby);
            }
        }

        return hobbies;
    }
}
