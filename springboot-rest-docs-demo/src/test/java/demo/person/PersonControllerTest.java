package demo.person;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.common.RestDocsConfiguration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author zacconding
 * @Date 2019-01-05
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class PersonControllerTest {

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
            .andDo(
                document("save-person",
                    requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                    ),
                    requestFields(
                        fieldWithPath("name").description("Name of a person"),
                        fieldWithPath("age").description("Age of a person"),
                        fieldWithPath("hobbies").description("Hobbies of a person")
                    ),
                    responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                    ),
                    responseFields(
                        personFieldDescriptor()
                    )
                )
            );
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
            .andDo(
                document("get-person",
                    pathParameters(
                        parameterWithName("id").description("id of person")
                    ),
                    responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                    ),
                    responseFields(
                        personFieldDescriptor()
                    )
                )
            );
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
            .andDo(
                document("get-persons",
                    requestParameters(
                        parameterWithName("page").description("page of search"),
                        parameterWithName("size").description("size of search"),
                        parameterWithName("sort").description("sort of search")
                    ),
                    responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                    ),
                    responseFields(
                        fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("searched content"),
                        fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("Id of a person"),
                        fieldWithPath("content[].name").type(JsonFieldType.STRING).description("name of a person"),
                        fieldWithPath("content[].age").type(JsonFieldType.NUMBER).description("age of a person"),
                        fieldWithPath("content[].hobbies").type(JsonFieldType.ARRAY).description("hobbies of a person"),

                        fieldWithPath("pageable.sort.sorted").description("sorted or not"),
                        fieldWithPath("pageable.sort.unsorted").description("unsorted or not"),
                        fieldWithPath("pageable.sort.empty").description("empty or not"),

                        fieldWithPath("pageable.offset").description("offset of search results"),
                        fieldWithPath("pageable.pageSize").description("page size of search results"),
                        fieldWithPath("pageable.pageNumber").description("page number of search results"),
                        fieldWithPath("pageable.paged").description("paged or not"),
                        fieldWithPath("pageable.unpaged").description("unpaged or not"),

                        fieldWithPath("totalPages").description("total pages of search results"),
                        fieldWithPath("totalElements").description("total number of elts"),
                        fieldWithPath("last").description("last or not"),
                        fieldWithPath("number").description("page number"),
                        fieldWithPath("size").description("elts size"),
                        fieldWithPath("sort.sorted").description("sorted or not"),
                        fieldWithPath("sort.unsorted").description("unsorted or not"),
                        fieldWithPath("sort.empty").description("empty or not"),
                        fieldWithPath("numberOfElements").description("elts number"),
                        fieldWithPath("first").description("first page or not"),
                        fieldWithPath("empty").description("empty or not")
                    )
                )
            );
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
            .andDo(
                document("update-person",
                    pathParameters(
                        parameterWithName("id").description("id of person")
                    ),
                    requestFields(
                        fieldWithPath("name").description("Name of a person"),
                        fieldWithPath("age").description("Age of a person"),
                        fieldWithPath("hobbies").description("Hobbies of a person")
                    ),
                    responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                    ),
                    responseFields(
                        personFieldDescriptor()
                    )
                )
            );
    }

    @Test
    public void test_deletePerson() throws Exception {
        // given
        Person person = savePersons(1);

        // when then
        mockMvc.perform(delete("/api/person/{id}", person.getId()))
            .andDo(print())
            .andDo(
                document("delete-person",
                    pathParameters(
                        parameterWithName("id").description("id of person")
                    ),
                    responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                    ),
                    responseFields(
                        personFieldDescriptor()
                    )
                )
            );
    }

    private FieldDescriptor[] personFieldDescriptor() {
        return new FieldDescriptor[]{
            fieldWithPath("id").description("id of a person"),
            fieldWithPath("name").description("name of a person"),
            fieldWithPath("age").description("age of a person"),
            fieldWithPath("hobbies").description("hobbies of a person")
        };
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
