package demo.client;

import demo.util.RandomExtractor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * @author zacconding
 * @Date 2018-12-05
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j(topic = "[WS-MAINTAINER]")
public class DefaultWebSocketConnectionMaintainer2 implements WebSocketConnectionMaintainer {

    private Thread connectTask;
    private Map<String, WebSocketClient> clientMap;

    public DefaultWebSocketConnectionMaintainer2() {
        this.clientMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean register(String url, WebSocketListener webSocketListener) {
        if (clientMap.containsKey(url)) {
            return false;
        }

        WebSocketClient client = createWebSocketClient(URI.create(url), webSocketListener);
        client.connect();

        clientMap.put(url, client);

        return true;
    }

    @Override
    public void remove(String url) {
        WebSocketClient client = clientMap.remove(url);
        if (client == null) {
            return;
        }

        client.close();
    }

    @Override
    public WebSocketClient getActiveClient() throws NoAvailableClientException {
        int size = clientMap.size();

        List<String> urls = new ArrayList<>(clientMap.keySet());
        RandomExtractor<Integer> randomExtractor = RandomExtractor.createIntegerItems(size);

        while (randomExtractor.isRemain()) {
            String url = urls.get(randomExtractor.nextItem());
            WebSocketClient client = clientMap.get(url);

            if (client.isOpen()) {
                return client;
            }
        }

        throw new NoAvailableClientException();
    }

    @Override
    public int getActiveClientCount() {
        int activeCount = 0;

        for (Entry<String, WebSocketClient> entry : clientMap.entrySet()) {
            if (entry.getValue().isOpen()) {
                activeCount++;
            }
        }

        return activeCount;
    }

    private synchronized void startConnectTask() {
        if (connectTask != null && connectTask.isAlive()) {
            return;
        }

        connectTask = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    boolean existNotConnected = false;

                    for (Entry<String, WebSocketClient> entry : clientMap.entrySet()) {
                        WebSocketClient client = entry.getValue();

                        if (client.isOpen()) {
                            continue;
                        }

                        existNotConnected = true;
                        client.reconnect();
                    }

                    log.info(">>> Check websocket connection : " + existNotConnected);

                    if (!existNotConnected) {
                        Thread.currentThread().interrupt();
                    }

                    TimeUnit.MILLISECONDS.sleep(100L);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        connectTask.setName("[WS-MAINTAINER-TASK]");
        connectTask.setDaemon(true);
        connectTask.start();
    }

    private WebSocketClient createWebSocketClient(URI serverUri, WebSocketListener webSocketListener) {
        return new WebSocketClient(serverUri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                webSocketListener.onOpen(serverHandshake);
            }

            @Override
            public void onMessage(String message) {
                webSocketListener.onMessage(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                webSocketListener.onClose(code, reason, remote);
                startConnectTask();
            }

            @Override
            public void onError(Exception ex) {
                webSocketListener.onError(ex);
            }
        };
    }
}
