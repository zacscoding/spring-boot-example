package server.api.v1.rest;

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
import server.api.v1.ApiStatusCodeV1;
import server.api.Person;
import server.api.v1.ResponseDTOV1;
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
public class PersonControllerV1 {

    private PersonService personService;

    @Autowired
    public PersonControllerV1(PersonService personService) {
        this.personService = personService;
    }


    @AuthTokenPreFilter
    @ApiOperation(value = "ID값으로 Person 정보 가져오기")
    @GetMapping(value = "{id}")
    public ResponseDTOV1<Person> findPersonById(@ApiParam(name = "id", value = "person`s id") @PathVariable("id") String id) {
        log.info("find person by id. {}", id);
        Person person = personService.findOneById(id);

        if (person == null) {
            return ResponseDTOV1.createException(ApiStatusCodeV1.BAD_REQUEST, String.format("Not found person id : %s", id));
        }

        return ResponseDTOV1.createOK(person);
    }

    @AuthTokenPreFilter
    @ApiOperation(value = "Person 정보 저장")
    @PostMapping(value = "/save")
    public ResponseDTOV1<String> savePerson(@RequestBody Person person) {
        log.info("save person : {}", person);

        try {
            String id = personService.save(person);
            log.info("Success to save person. id : {}", id);
            return ResponseDTOV1.createOK(id);
        } catch (DuplicateValueException e) {
            log.error("Failed to save person", e);
            return ResponseDTOV1.createException(ApiStatusCodeV1.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Exception occur while saving person", e);
            return ResponseDTOV1.createException(ApiStatusCodeV1.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @AuthTokenPreFilter
    @ApiOperation(value = "Person list 정보 가져오기")
    @GetMapping(value = "/all")
    public ResponseDTOV1<List<Person>> findAll() {
        return ResponseDTOV1.createOK(personService.findAll());
    }

    @AuthTokenPreFilter
    @ApiOperation(value = "Person id로 삭제")
    @DeleteMapping(value="/{id}")
    public ResponseDTOV1<Integer> deleteById(@ApiParam(name = "person id") @PathVariable("id") String id) {
        int result = personService.deleteById(id);

        if (result < 1) {
            return ResponseDTOV1.createException(ApiStatusCodeV1.BAD_REQUEST, "Not exist id : " + id);
        }

        return ResponseDTOV1.createOK(result);
    }
}
