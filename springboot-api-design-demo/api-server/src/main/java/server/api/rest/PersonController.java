package server.api.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.api.ApiStatus;
import server.api.Person;
import server.api.ResponseDTO;
import server.api.exception.DuplicateValueException;
import server.api.service.PersonService;
import server.aspect.annotation.AuthTokenPreFilter;

/**
 * @author zacconding
 * @Date 2018-12-03
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RestController
@RequestMapping("/person/v1/**")
public class PersonController {

    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }


    @AuthTokenPreFilter
    @ApiOperation(value = "ID값으로 Person 정보 가져오기")
    @GetMapping(value = "{id}")
    public ResponseDTO<Person> findPersonById(@ApiParam(name = "id", value = "person`s id") @PathVariable("id") String id) {
        log.info("find person by id. {}", id);
        Person person = personService.findOneById(id);

        if (person == null) {
            return ResponseDTO.createException(ApiStatus.BAD_REQUEST);
        }

        return ResponseDTO.createOK(person);
    }

    @AuthTokenPreFilter
    @ApiOperation(value = "Person 정보 저장")
    @PostMapping(value = "/save")
    public ResponseDTO<String> savePerson(@RequestBody Person person) {
        log.info("save person : {}", person);

        try {
            String id = personService.save(person);

            return ResponseDTO.createOK(id);
        } catch (DuplicateValueException e) {
            return ResponseDTO.createException(ApiStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @AuthTokenPreFilter
    @ApiOperation(value = "Person list 정보 가져오기")
    @GetMapping(value = "/all")
    public ResponseDTO<List<Person>> findAll() {
        return ResponseDTO.createOK(personService.findAll());
    }

    @AuthTokenPreFilter
    @ApiOperation(value = "Person id로 삭제")
    @DeleteMapping(value="/{id}")
    public ResponseDTO<Integer> deleteById(@ApiParam(name = "person id") @PathVariable("id") String id) {
        int result = personService.deleteById(id);

        if (result < 1) {
            return ResponseDTO.createException(ApiStatus.BAD_REQUEST, "Not exist id : " + id);
        }

        return ResponseDTO.createOK(result);
    }
}
