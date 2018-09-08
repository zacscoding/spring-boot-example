package demo.okhttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Interceptor;
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
public class PostRequestWithHeaderTest {

    @LocalServerPort
    private int port;

    @Test
    public void whenSetHeaderThenCorrect() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
            .url("http://localhost:" + port + "/okhttp/defaultHeader")
            .addHeader("Content-Type", "application/json")
            .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        displayResponse(response);
    }

    @Test
    public void whenSetDefaultHeaderThenCorrect() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new DefaultContentTypeInterceptor("application/json")).build();

        Request request = new Request.Builder()
            .url("http://localhost:" + port + "/okhttp/defaultHeader")
            .addHeader("Content-Type", "application/json")
            .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        displayResponse(response);
    }

    private static class DefaultContentTypeInterceptor implements Interceptor {

        private String contentType;

        private DefaultContentTypeInterceptor(String contentType) {
            this.contentType = contentType;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request requestWIthUserAgent = originalRequest.newBuilder().header("Content-Type", contentType).build();
            return chain.proceed(requestWIthUserAgent);
        }
    }

    private void displayResponse(Response response) throws IOException {
        System.out.println(">> Display response");
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));
        System.out.println(">> body : " + response.body().string());
    }
}