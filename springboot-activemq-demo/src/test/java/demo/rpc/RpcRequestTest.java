package demo.rpc;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zacconding
 * @Date 2018-09-12
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RpcRequestTest {

    @Autowired
    private RpcClient client;

    @Test
    public void requestAndResponse() {
        JsonRpcRequest request = new JsonRpcRequest("1", 0L, "a");
        JsonRpcResponse response = client.processRequest(request);

        assertThat(request.getId(), is(response.getId()));
        assertThat(response.getResponseBody(), is("A"));
        System.out.println(response);
    }

    @Test
    public void timeout() {
        JsonRpcRequest request = new JsonRpcRequest("1", 7L, "a");
        JsonRpcResponse response = client.processRequest(request);
        assertNull(response);
    }

    // NOT WORKING!!!
    /*
    >> Final success :: 10 (294 MS)
    >> Final success :: 100 (1671 MS)
    >> Final success :: 1000 (15722 MS)
     */
    @Test
    public void concurrent() throws InterruptedException {
        long start = System.currentTimeMillis();
        final int threadCount = 1000;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new TestThread(client, String.valueOf(i));
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }
        long elapsed = System.currentTimeMillis() - start;

        assertTrue(TestThread.SUCCESS.get() == threadCount);
        System.out.println(">> Final success :: " + TestThread.SUCCESS + " (" + elapsed + " MS)");
    }

    private static class TestThread extends Thread {

        public static AtomicInteger SUCCESS = new AtomicInteger(0);
        private static final Random RANDOM = new Random();

        private JsonRpcRequest request;
        private RpcClient client;

        public TestThread(RpcClient client, String id) {
            this.client = client;
            // this.request = new JsonRpcRequest(id, RANDOM.nextInt(2), UUID.randomUUID().toString());
            this.request = new JsonRpcRequest(id, 0, UUID.randomUUID().toString());
            this.setDaemon(true);
        }

        public void run() {
            JsonRpcResponse response = client.processRequest(request);
            try {
                if(response == null
                    || !response.getId().equals(request.getId())
                    || !response.getResponseBody().equals(request.getRequestBody().toUpperCase())) {
                    throw new Exception();
                }
                SUCCESS.incrementAndGet();
            } catch (Exception e) {

            }

            /*assertNotNull(response);
            assertThat(response.getId(), is(request.getId()));
            assertThat(response.getResponseBody(), is(request.getRequestBody().toUpperCase()));*/
        }
    }
}