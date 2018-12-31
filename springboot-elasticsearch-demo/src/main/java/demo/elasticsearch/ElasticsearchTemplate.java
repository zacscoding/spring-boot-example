package demo.elasticsearch;

import demo.configuration.properties.ElasticProperties;
import demo.util.GsonUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

/**
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class ElasticsearchTemplate {

    private final String defaultType = "_doc";

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

    public String getPersonsType() {
        return defaultType;
    }

    public String createIndexAndMapping(Document document) throws IOException {
        if (existIndices(document.getIndex())) {
            log.debug("Already exist index : {}", document.getIndex());
            return document.getIndex();
        }

        CreateIndexRequest indexRequest = new CreateIndexRequest();

        indexRequest.index(document.getIndex());

        String settingsPath = document.getSettings();

        if (StringUtils.hasText(settingsPath)) {
            indexRequest.settings(readFile(settingsPath), XContentType.JSON);
        }

        String mappingsPath = document.getMappings();

        if (StringUtils.hasText(mappingsPath)) {
            indexRequest.mapping("_doc", readFile(mappingsPath), XContentType.JSON);
        }

        return client.indices().create(indexRequest).index();
    }

    public boolean existIndices(String... indices) throws IOException {
        GetIndexRequest request = new GetIndexRequest();

        request.indices(indices);

        return client.indices().exists(request);
    }

    private String readFile(String pathVal) throws IOException {
        Path path = null;

        if (pathVal.startsWith(File.separator)) {
            path = new File(pathVal).toPath();
        } else {
            path = new ClassPathResource(pathVal).getFile().toPath();
        }

        return Files.readAllLines(path).stream().collect(Collectors.joining(""));
    }
}
