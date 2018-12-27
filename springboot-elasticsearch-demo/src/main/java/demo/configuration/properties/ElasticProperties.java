package demo.configuration.properties;

import demo.elasticsearch.Document;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Elastic search configuration
 *
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@ToString
@Slf4j
@Component
@ConfigurationProperties(prefix = "elastic")
public class ElasticProperties {

    private Cluster cluster;
    private PersonsDocument personsDocument;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Cluster {

        private String hosts;
        private int port;
        private String schema;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class PersonsDocument implements Document {

        private String index;
        private String mappings;
        private String settings;
    }
}