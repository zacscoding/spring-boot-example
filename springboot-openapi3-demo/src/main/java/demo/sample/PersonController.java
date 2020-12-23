package demo.sample;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/person")
@Tag(name = "person", description = "the person API")
@Slf4j
public class PersonController {

    private Map<Long, Person> personRepository = new ConcurrentHashMap<>();
    private AtomicLong idGenerator = new AtomicLong(1L);

    @Operation(summary = "Get a person by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success to find",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = {
                            @Content(mediaType = "application/json")
                    })
    })
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(
            @Parameter(description = "id of a person") @PathVariable("id") Long id) {

        Person p = personRepository.get(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }

    @Operation(summary = "Save a new person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success to save",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class))
                    })
    })
    @PostMapping
    public ResponseEntity<Person> savePerson(@Valid @RequestBody Person person) {
        person.setId(idGenerator.getAndIncrement());
        personRepository.put(person.getId(), person);
        return ResponseEntity.ok(person);
    }
}
