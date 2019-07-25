package demo.rest;

import demo.aop.annotation.Loggings;
import demo.entity.PersonEntity;
import demo.repository.PersonRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonRepository personRepository;

    @Loggings
    @GetMapping("/{id}")
    public ResponseEntity<PersonEntity> getPerson(@PathVariable("id") Long id) {
        logger.info("call get person");
        Optional<PersonEntity> personOptional = personRepository.findById(id);

        if (!personOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(personOptional.get());
    }
}
