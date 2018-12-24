package demo.person;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author zacconding
 * @Date 2018-12-25
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@WebMvcTest
public class PersonControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PersonRepository personRepository;

    @Test
    public void getPerson_then_not_found() throws Exception {
        when(personRepository.findOneById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/person/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void getPerson() throws Exception {
        Person expectedPerson = Person.builder()
            .id(1L)
            .name("Hiva1")
            .age(11)
            .hobbies(Arrays.asList("coding", "movie"))
            .build();
        when(personRepository.findOneById(1L)).thenReturn(expectedPerson);

        mockMvc.perform(get("/api/v1/person/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(expectedPerson.getId()))
            .andExpect(jsonPath("name").value(expectedPerson.getName()))
            .andExpect(jsonPath("age").value(expectedPerson.getAge()))
            .andExpect(jsonPath("hobbies[0]").value(expectedPerson.getHobbies().get(0)))
            .andExpect(jsonPath("hobbies[1]").value(expectedPerson.getHobbies().get(1)))
            .andExpect(jsonPath("_links.self").exists())
            .andExpect(jsonPath("_links.update").exists())
            .andExpect(jsonPath("_links.delete").exists());
    }
}
