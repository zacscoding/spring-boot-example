package demo.dev;

import demo.common.EmbeddedElasticsearchTestRunner;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Before;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class SaveMultipleDocumentsTest extends EmbeddedElasticsearchTestRunner {

    String indexName;

    @Before
    public void setUp() throws Exception {
        indexName = "test-index";
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.settings(
            Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
                .put("index.refresh_interval", "2s")
        );

        CreateIndexResponse response = restHighLevelClient.indices().create(request);
        System.out.println("Index create result :: " + response.index());
    }

    @Test
    public void tryToSaveMultipleDocument() throws Exception {
        int docRequestCount = 10;
        int tryCount = 3;

        for (int i = 0; i < tryCount; i++) {
            String id = "id" + i;

            Thread[] saveTask = new Thread[docRequestCount];

            // try to save document with same indexName / type / id
            for (int j = 0; j < docRequestCount; j++) {
                IndexRequest indexRequest = new IndexRequest();
                indexRequest.index(indexName);
                indexRequest.type("_doc");
                indexRequest.id(id);
                Map<String, Object> source = new HashMap<>();
                source.put("name", "name" + i);
                source.put("age", i);
                source.put("diff", j);
                indexRequest.source(source);
                // saveTask[i] = new SaveTask(indexRequest, 0);
                saveTask[i] = new SaveTask(indexRequest, j);
                saveTask[i].start();
            }

            for (int j = 0; j < docRequestCount; j++) {
                saveTask[i].join();
            }
            System.out.println("-------------------------------------------------------------------");
        }

        TimeUnit.SECONDS.sleep(2L);
        displaySearchResult(indexName);
    }

    private static class SaveTask extends Thread {

        private IndexRequest indexRequest;
        private int order;

        public SaveTask(IndexRequest indexRequest, int order) {
            this.indexRequest = indexRequest;
            this.order = order;
            this.setDaemon(true);
        }

        @Override
        public void run() {
            try {
                if (order != 0) {
                    TimeUnit.MILLISECONDS.sleep(100 * order);
                }
                GetRequest getRequest = new GetRequest();
                getRequest.index(indexRequest.index());
                getRequest.type("_doc");
                getRequest.id(indexRequest.id());

                if (restHighLevelClient.exists(getRequest)) {
                    System.out.println("Skip save " + indexRequest.id());
                } else {
                    System.out.println("Try to save " + indexRequest.id());
                    IndexResponse response = restHighLevelClient.index(indexRequest);
                    System.out.println("> response : " + response.status());
                    if (response.status() == RestStatus.CREATED) {
                        displaySearchResult(indexRequest.index());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }

    private static void displaySearchResult(String indexName) throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);

        SearchResponse result = restHighLevelClient.search(searchRequest);

        SearchHits hits = result.getHits();
        System.out.println("total hits : " + hits.totalHits);
        for (SearchHit searchHit : hits.getHits()) {
            System.out.println(searchHit.getSourceAsString());
        }
    }
}