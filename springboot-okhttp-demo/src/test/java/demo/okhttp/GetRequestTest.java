package demo.okhttp;

import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * https://www.baeldung.com/guide-to-okhttp
 *
 * @author zacconding
 * @Date 2018-09-05
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GetRequestTest {

    @LocalServerPort
    private int port;
    private OkHttpClient client;

    @Before
    public void setUp() {
        this.client = new OkHttpClient();
    }

    // Synchronous GET
    @Test
    public void whenGetRequestThenCorrect() throws IOException {
        Request request = new Request.Builder().url("http://localhost:" + port + "/okhttp/date").build();
        Call call = client.newCall(request);
        Response response = call.execute();
        displayResponse(response);
    }

    // Asynchronous GET
    @Test
    public void whenAsynchronousGetRequestThenCorrect() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Request request = new Request.Builder().url("http://localhost:" + port + "/okhttp/async/date").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response) throws IOException {
                displayResponse(response);
                countDownLatch.countDown();
            }

            public void onFailure(Call call, IOException e) {
                System.out.println("FAIL!");
                fail();
            }
        });

        countDownLatch.await();
    }

    // GET with Query Parameters
    @Test
    public void whenGetRequestWithQueryParameterThenCorrect() throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://localhost:" + port + "/okhttp/ex/bars").newBuilder();
        urlBuilder.addQueryParameter("id", "1");
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        Response response = call.execute();
        displayResponse(response);
    }

    private void displayResponse(Response response) throws IOException {
        System.out.println(">> Display response");
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }
}