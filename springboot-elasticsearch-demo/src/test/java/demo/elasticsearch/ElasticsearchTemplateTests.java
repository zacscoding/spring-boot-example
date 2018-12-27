package demo.elasticsearch;

import static org.assertj.core.api.Assertions.assertThat;

import demo.common.EmbeddedElasticsearchTestRunner;
import demo.configuration.properties.ElasticProperties;
import demo.configuration.properties.ElasticProperties.PersonsDocument;
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
    public void createIndex() throws Exception {
        // Given
        Document document = new Document() {
            @Override
            public String getIndex() {
                return "persons";
            }

            @Override
            public String getSettings() {
                return null;
            }

            @Override
            public String getMappings() {
                return null;
            }
        };

        // When
        String createdIndex = elasticsearchTemplate.createIndexAndMapping(document);

        // Then
        assertThat(createdIndex).isEqualTo(document.getIndex());
    }
}