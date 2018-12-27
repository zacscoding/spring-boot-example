package demo.common;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

/**
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
public class EmbeddedElasticsearch {

    public static EmbeddedElasticsearch INSTANCE = new EmbeddedElasticsearch();

    private RestHighLevelClient client;
    private EmbeddedElastic embeddedElastic;
    private boolean isStarted;

    private EmbeddedElasticsearch() {
        setUp();
    }

    public synchronized void start() {
        try {
            embeddedElastic.start();
            isStarted = true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to start elastic search");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void stop() {
        embeddedElastic.stop();
        isStarted = false;
    }

    public synchronized RestHighLevelClient getRestHighLevelClient() {
        if (!isStarted) {
            throw new IllegalArgumentException("Must be called start() before getting client");
        }

        return client;
    }

    private void setUp() {
        this.embeddedElastic = EmbeddedElastic.builder()
            .withElasticVersion("6.3.0")
            .withSetting(PopularProperties.TRANSPORT_TCP_PORT, 9300)
            .withSetting(PopularProperties.HTTP_PORT, 9200)
            .withEsJavaOpts("-Xms128m -Xmx512m")
            .withStartTimeout(1L, TimeUnit.MINUTES)
            .withInstallationDirectory(
                new File(System.getProperty("java.io.tmpdir"), "embedded-elasticsearch-temp-dir")
            )
            .build();

        this.client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"))
        );
    }
}
