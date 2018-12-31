package demo.person;

import static org.assertj.core.api.Assertions.assertThat;

import demo.common.EmbeddedElasticsearchTestRunner;
import demo.configuration.properties.ElasticProperties;
import demo.configuration.properties.ElasticProperties.PersonsDocument;
import demo.elasticsearch.ElasticsearchTemplate;
import java.util.Arrays;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
public class PersonServiceTests extends EmbeddedElasticsearchTestRunner {

    PersonService personService;
    ElasticsearchTemplate elasticsearchTemplate;

    @BeforeClass
    @Override
    public void setUp() throws Exception {
        super.setUp();
        ElasticProperties properties = new ElasticProperties();

        properties.setPersonsDocument(new PersonsDocument());
        properties.getPersonsDocument().setIndex("persons");
        properties.getPersonsDocument().setSettings("settings.json");
        properties.getPersonsDocument().setMappings("mappings.json");

        elasticsearchTemplate = new ElasticsearchTemplate(properties, restHighLevelClient);

        // check person index
        GetIndexRequest request = new GetIndexRequest();
        request.indices(properties.getPersonsDocument().getIndex());

        if (!restHighLevelClient.indices().exists(request)) {
            elasticsearchTemplate.createIndexAndMapping(properties.getPersonsDocument());
        }

        this.personService = new PersonService(restHighLevelClient, elasticsearchTemplate);
    }

    @Test
    public void save() {
        // Given
        Person person = Person.builder()
            .name("person1")
            .hobbies(Arrays.asList("coding", "movie"))
            .build();

        // When
        Person savedPerson = personService.save(person);

        // Then
        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getId()).isNotNull();
        assertThat(savedPerson.getName()).isEqualTo(person.getName());
    }
}
