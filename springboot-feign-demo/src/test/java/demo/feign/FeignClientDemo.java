package demo.feign;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import feign.Feign;
import feign.Retryer;
import feign.slf4j.Slf4jLogger;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

class FeignClientDemo {

    @Test
    public void runTests() throws Exception {
        String host = "http://localhost:8080";
        okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient()
                .newBuilder()
                .connectTimeout(1500, TimeUnit.MILLISECONDS)
                .readTimeout(1500, TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool(5, 500, TimeUnit.MILLISECONDS))
                .build();

        EchoHandler handler = Feign.builder()
                                   .logger(new Slf4jLogger())
                                   .client(new OkHttpClient(okHttpClient))
                                   .retryer(new Retryer.Default(500, TimeUnit.SECONDS.toMillis(1L), 10))
                                   .encoder(new GsonEncoder())
                                   .decoder(new GsonDecoder())
                                   //.errorDecoder(AnnotationErrorDecoder.builderFor(RpcHandler.class).build())
                                   .target(EchoHandler.class, host);

        runTests(new OkHttpClient(okHttpClient), host);
    }
}
