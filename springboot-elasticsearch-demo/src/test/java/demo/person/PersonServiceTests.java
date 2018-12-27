//package demo.person;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import demo.elasticsearch.EmbeddedElasticsearchTestRunner;
//import java.util.Arrays;
//import org.junit.Before;
//import org.junit.Test;
//
///**
// * @author zacconding
// * @Date 2018-12-27
// * @GitHub : https://github.com/zacscoding
// */
//public class PersonServiceTests extends EmbeddedElasticsearchTestRunner {
//
//    private PersonService personService;
//
//    @Before
//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
//        this.personService = new PersonService(restHighLevelClient);
//    }
//
//    @Test
//    public void save() {
//        // Given
//        Person person = Person.builder()
//            .name("person1")
//            .hobbies(Arrays.asList("coding", "movie"))
//            .build();
//
//        // When
//        Person savedPerson = personService.save(person);
//
//        // Then
//        assertThat(savedPerson).isNotNull();
//        assertThat(savedPerson.getId()).isNotNull();
//    }
//}