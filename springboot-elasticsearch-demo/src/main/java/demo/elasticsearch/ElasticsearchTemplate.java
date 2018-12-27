package demo.elasticsearch;

import demo.configuration.properties.ElasticProperties;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.util.StringUtils;

/**
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class ElasticsearchTemplate {

    private ElasticProperties elasticProperties;
    private RestHighLevelClient client;

    // persons
    public String personsIndex;

    public ElasticsearchTemplate(ElasticProperties elasticProperties, RestHighLevelClient client) {
        this.elasticProperties = elasticProperties;
        this.client = client;

        // documents
        this.personsIndex = elasticProperties.getPersonsDocument().getIndex();
    }

    public String getPersonsIndex() {
        return personsIndex;
    }

    public String createIndexAndMapping(Document document) throws IOException {
        if (existIndices(document.getIndex())) {
            log.debug("Already exist index : {}", document.getIndex());
            return document.getIndex();
        }

        CreateIndexRequest indexRequest = new CreateIndexRequest();

        indexRequest.index(document.getIndex());

        if (StringUtils.hasText(document.getSettings())) {

        }

        return client.indices().create(indexRequest).index();
    }

    public boolean existIndices(String... indices) throws IOException {
        GetIndexRequest request = new GetIndexRequest();

        request.indices(indices);

        return client.indices().exists(request);
    }
}