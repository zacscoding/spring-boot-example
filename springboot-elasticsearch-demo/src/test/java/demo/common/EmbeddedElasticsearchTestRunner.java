package demo.common;

import demo.common.EmbeddedElasticsearch;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author zacconding
 * @Date 2018-12-27
 * @GitHub : https://github.com/zacscoding
 */
public class EmbeddedElasticsearchTestRunner {

    protected static RestHighLevelClient restHighLevelClient;

    @BeforeClass
    public static void classSetUp() throws Exception {
        EmbeddedElasticsearch.INSTANCE.start();
        restHighLevelClient = EmbeddedElasticsearch.INSTANCE.getRestHighLevelClient();
    }

    @AfterClass
    public static void classTearDown() throws Exception {
        EmbeddedElasticsearch.INSTANCE.stop();
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("## BEFORE");
        DeleteIndexRequest request = new DeleteIndexRequest("_all");
        restHighLevelClient.indices().delete(request);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("## AFTER");
        DeleteIndexRequest request = new DeleteIndexRequest("_all");
        restHighLevelClient.indices().delete(request);
    }
}
