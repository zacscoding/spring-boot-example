package demo.client;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-12-05
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class WSConnectionMaintainerTest {

    WebSocketConnectionMaintainer maintainer;

    @Before
    public void setUp() {
        // maintainer = new DefaultWebSocketConnectionMaintainer();
        maintainer = new DefaultWebSocketConnectionMaintainer2();
    }

    @Test
    public void connect() throws InterruptedException {
        List<String> urls = Arrays.asList(
            "ws://192.168.5.78:9540"
            ,"ws://192.168.5.78:9541"
        );

        for (String url : urls) {
            boolean result = maintainer.register(url, new WebSocketListener() {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    //log.info("onOpen..");
                }

                @Override
                public void onMessage(String message) {
                    //log.info("onMessage");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    //log.info("onClose.. code : {}, reason : {}, remote : {}", code, reason, remote);
                }

                @Override
                public void onError(Exception ex) {
                    //log.info("onError : " + ex.getMessage());
                }
            });
            log.info("regist result : " + result);
        }

        Thread activeClientCheck = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    WebSocketClient client = null;
                    try {
                        client = maintainer.getActiveClient();
                    } catch (NoAvailableClientException e) {
                    }
                    log.info("##[Check active client] client : {} | active count : {}"
                        , (client == null ? "null" : client.getURI()), maintainer.getActiveClientCount());
                    TimeUnit.SECONDS.sleep(1L);
                }
            } catch(Exception e) {
                Thread.currentThread().interrupt();
            }
        });
        activeClientCheck.setDaemon(true);
        activeClientCheck.start();

        TimeUnit.SECONDS.sleep(10L);

        maintainer.remove(urls.get(0));

        TimeUnit.MINUTES.sleep(10L);

        urls.forEach(url -> maintainer.remove(url));
    }
}
