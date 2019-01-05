package demo.configuration;

import demo.ContextListener;
import demo.configuration.properties.ElasticProperties;
import demo.elasticsearch.ElasticsearchTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Configuration
public class ElasticsearchConfiguration implements ContextListener {

    private ElasticProperties elasticProperties;

    @Autowired
    public ElasticsearchConfiguration(ElasticProperties elasticProperties) {
        this.elasticProperties = elasticProperties;
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() {
        ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate(elasticProperties,
            restHighLevelClient());
        System.out.println("elasticsearchTemplate() is called :: " + elasticsearchTemplate);
        return elasticsearchTemplate;
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
            RestClient.builder(
                new HttpHost(elasticProperties.getCluster().getHosts(),
                    elasticProperties.getCluster().getPort(),
                    elasticProperties.getCluster().getSchema()
                )
            )
        );
    }

    @Override
    public void afterContextLoaded() throws Exception {
        ElasticsearchTemplate elasticsearchTemplate = elasticsearchTemplate();
        elasticsearchTemplate.createIndexAndMapping(elasticProperties.getPersonsDocument());
    }

//    @Override
//    public void onApplicationEvent(ApplicationEvent event) {
//        System.out.println("## onApplicationEvent.. " + event);
//        System.out.println(event.getSource());
//    }
}