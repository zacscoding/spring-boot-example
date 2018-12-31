package demo.elasticsearch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import demo.common.EmbeddedElasticsearchTestRunner;
import demo.configuration.properties.ElasticProperties;
import demo.configuration.properties.ElasticProperties.PersonsDocument;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-12-28
 * @GitHub : https://github.com/zacscoding
 */
public class ElasticsearchTemplateTests extends EmbeddedElasticsearchTestRunner {

    ElasticsearchTemplate elasticsearchTemplate;

    @Before
    public void setUp() throws Exception {
        ElasticProperties properties = new ElasticProperties();

        properties.setPersonsDocument(new PersonsDocument());
        properties.getPersonsDocument().setIndex("persons");

        elasticsearchTemplate = new ElasticsearchTemplate(properties, restHighLevelClient);
    }

    @After
    public void tearDown() throws Exception {
        // nothing
    }

    @Test
    public void createIndexAndMapping() throws Exception {
        // Given
        Document document = new Document() {
            @Override
            public String getIndex() {
                return "persons";
            }

            @Override
            public String getSettings() {
                return "settings.json";
            }

            @Override
            public String getMappings() {
                return "mappings.json";
            }
        };

        // When
        String createdIndex = elasticsearchTemplate.createIndexAndMapping(document);

        // Then
        assertThat(createdIndex).isEqualTo(document.getIndex());

        Response res = restHighLevelClient.getLowLevelClient()
            .performRequest("GET", "/" + document.getIndex());
        String responseBody = EntityUtils.toString(res.getEntity());
        ReadContext responseCtx = JsonPath.parse(responseBody);

        assertNotNull(responseCtx.read("persons.mappings._doc"));
        String numOfShards = responseCtx.read("persons.settings.index.number_of_shards");
        assertThat(numOfShards).isEqualTo("3");
    }
}
