package demo.person.second;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import demo.person.Person;
import demo.person.PersonRepository;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Person integration tests with docker-compose-rule-junit4
 *
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PersonControllerTest {

    @ClassRule
    public static DockerComposeRule DOCKER_COMPOSE_RULE = DockerComposeRule.builder()
        .file("src/test/resources/compose/postgresql-compose.yaml")
        .waitingForService("postgres_test_db", HealthChecks.toHaveAllPortsOpen())
        .build();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ObjectMapper objectMapper;
    Random random = new Random();

    @Before
    public void setUp() {
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        personRepository.deleteAll();
    }

    @Test
    public void test_savePerson() throws Exception {
        Person person = Person.builder()
            .name("zaccoding")
            .age(20)
            .hobbies(Arrays.asList("coding", "math"))
            .build();

        mockMvc.perform(post("/api/person")
            .content(objectMapper.writeValueAsString(person))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
        // TODO : assertion of response
        ;
    }

    @Test
    public void test_getPerson() throws Exception {
        // given
        Person person = Person.builder()
            .name("zaccoding")
            .age(20)
            .hobbies(Arrays.asList("coding", "math"))
            .build();
        Person saved = personRepository.save(person);

        // when then
        mockMvc.perform(get("/api/person/{id}", saved.getId()))
            .andDo(print())
        // TODO : assertion of response
        ;
    }

    @Test
    public void test_getPersons() throws Exception {
        // given
        IntStream.rangeClosed(1, 30).forEach(this::savePersons);

        // when then
        mockMvc.perform(get("/api/person")
            .param("page", "1")
            .param("size", "5")
            .param("sort", "name,DESC"))
            .andDo(print())
        // TODO : assertion of response
        ;
    }

    @Test
    public void test_updatePerson() throws Exception {
        // given
        Person person = savePersons(1);
        Person updateRequestPerson = Person.builder()
            .name("UpdatePerson")
            .age(1)
            .hobbies(person.getHobbies())
            .build();

        // when then
        mockMvc.perform(put("/api/person/{id}", person.getId())
            .content(objectMapper.writeValueAsString(updateRequestPerson))
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
        // TODO : assertion of response
        ;
    }

    @Test
    public void test_deletePerson() throws Exception {
        // given
        Person person = savePersons(1);

        // when then
        mockMvc.perform(delete("/api/person/{id}", person.getId()))
            .andDo(print())
        // TODO : assertion of response
        ;
    }

    private Person savePersons(int index) {
        Person person = Person.builder()
            .name("Person" + index)
            .age(10 * index + 5)
            .hobbies(randomHobbies())
            .build();

        return personRepository.save(person);
    }

    private List<String> randomHobbies() {
        String[][] hobbies = new String[][]{
            {"coding", "movie", "math"},
            {"book", "music"},
            {"football", "tennis"}
        };

        return Arrays.asList(hobbies[random.nextInt(hobbies.length)]);
    }
}
