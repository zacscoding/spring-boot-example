package demo.client;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-12-05
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class WSClientTest {

    @Test
    public void connect() throws Exception {
        String url = "ws://192.168.5.78:9540";
        WebSocketClient webSocketClient = new WebSocketClient(URI.create(url)) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                log.info("onOpen()");
            }

            @Override
            public void onMessage(String s) {
                log.info("onMessage() : {}", s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                log.info("onClose(). code : {}, reason : {}, remote : {}", i, s, b);
            }

            @Override
            public void onError(Exception e) {
                log.warn("onError()", e);
            }
        };
        boolean result = webSocketClient.connectBlocking();
        log.info("result : " + result);

        // TimeUnit.SECONDS.sleep(30L);
        // result = webSocketClient.reconnectBlocking();
        // log.info("again result : " + result);

        TimeUnit.SECONDS.sleep(5L);
    }
}
