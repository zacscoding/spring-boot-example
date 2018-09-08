package demo.okhttp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zacconding
 * @Date 2018-09-09
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EtcRequestTest {

    @LocalServerPort
    private int port;

    @Test
    public void whenSetFollowRedirectsThenNotRedirected() throws IOException {
        System.out.println("## >> redirect false");
        OkHttpClient client = new OkHttpClient().newBuilder().followRedirects(false).build();
        Request request = new Request.Builder().url("http://t.co/I5YYd9tddw").build();
        Call call = client.newCall(request);
        Response response = call.execute();
        System.out.println(">> Response code :: " + response.code());
        assertTrue(response.code() == 301);

        System.out.println("## >> redirect true");
        OkHttpClient client2 = new OkHttpClient().newBuilder().followRedirects(true).build();
        call = client2.newCall(request);
        response = call.execute();
        assertTrue(response.code() == 200);
        System.out.println(">> Response code :: " + response.code());
    }

    @Test(expected = SocketTimeoutException.class)
    public void whenSetRequestTimeoutThenFail() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(1, TimeUnit.SECONDS).build();

        Request request = new Request.Builder().url("http://localhost:" + port + "/okhttp/delay/3").build();
        Call call = client.newCall(request);
        Response response = call.execute();

        fail();
    }

    @Test(expected = IOException.class)
    public void whenCancelRequestThenCorrect() throws IOException {
        OkHttpClient client = new OkHttpClient();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Request request = new Request.Builder().url("http://localhost:" + port + "/okhttp/delay/3").build();

        int seconds = 1;

        Call call = client.newCall(request);

        executor.schedule(() -> {
            System.out.println(">> Canceling call..");
            call.cancel();
            System.out.println(">> Canceled call..");

        }, seconds, TimeUnit.SECONDS);

        Response response = call.execute();

        fail();
    }

    @Test
    public void whenSetResponseCacheThenCorrect() throws IOException {
        int cacheSize = 10 * 1024 * 1024;

        File cacheDir = new File("src/test/resources/cache");
        Cache cache = new Cache(cacheDir, cacheSize);

        OkHttpClient client = new OkHttpClient.Builder().cache(cache).build();
        Request request = new Request.Builder().url("http://publicobject.com/helloworld.txt").build();

        Response response1 = client.newCall(request).execute();
        displayResponse(response1);

        Response response2 = client.newCall(request).execute();
        displayResponse(response2);
    }

    private void displayResponse(Response response) throws IOException {
        System.out.println(">> Display response");
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));
        System.out.println(">> body : " + response.body().string());
    }
}
