package demo.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * @author zacconding
 * @Date 2018-12-04
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class DefaultWebSocketConnectionMaintainer implements WebSocketConnectionMaintainer {

    private Map<String, CustomWebSocketClient> clients;
    private Random random;

    public DefaultWebSocketConnectionMaintainer() {
        this.clients = new ConcurrentHashMap<>();
        this.random = new Random();
    }

    @Override
    public boolean register(String url, WebSocketListener webSocketListener) {
        if (clients.containsKey(url)) {
            log.warn("Already exist url : {}", url);
            return false;
        }

        CustomWebSocketClient webSocketClient = new CustomWebSocketClient(URI.create(url), webSocketListener);
        clients.put(url, webSocketClient);
        webSocketClient.startConnectTask();

        return true;
    }

    @Override
    public void remove(String url) {
        CustomWebSocketClient client = clients.remove(url);
        if (client != null) {
            client.closeConnection();
        }
    }

    @Override
    public WebSocketClient getActiveClient() {
        if (clients.isEmpty()) {
            return null;
        }

        List<String> urls = new ArrayList<>(clients.keySet());
        int[] indices = getRandomIndices(urls.size());

        for (int i = 0; i < indices.length; i++) {
            String url = urls.get(indices[i]);
            CustomWebSocketClient client = clients.get(url);
            if (client.isOpen()) {
                return client;
            }
        }

        return null;
    }

    /**
     * Generate shuffle indices from 0 to size -1
     */
    private int[] getRandomIndices(int size) {
        int[] indices = new int[size];
        for (int i = 0; i < size; i++) {
            indices[i] = i;
        }

        for (int i = size; i > 1; i--) {
            int idx1 = i - 1;
            int idx2 = random.nextInt(i);

            // swap
            int helper = indices[idx1];
            indices[idx1] = indices[idx2];
            indices[idx2] = helper;
        }

        return indices;
    }

    private static class CustomWebSocketClient extends WebSocketClient {

        private WebSocketListener webSocketListener;
        private boolean isFirstTry;
        private boolean dispose;

        public CustomWebSocketClient(URI serverUri, WebSocketListener webSocketListener) {
            super(serverUri);
            this.webSocketListener = webSocketListener;
            this.isFirstTry = true;
        }

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
            if (!dispose) {
                startConnectTask();
            }
        }

        @Override
        public void onError(Exception ex) {
            webSocketListener.onError(ex);
        }

        private void startConnectTask() {
            Thread task = new Thread(() -> {
                try {
                    log.info("Start connect task. url : {}", getURI());
                    int tryCount = 0;
                    while (true) {
                        boolean connect = false;
                        if (isFirstTry) {
                            connect = connectBlocking();
                            isFirstTry = false;
                        } else {
                            connect = reconnectBlocking();
                        }

                        if (connect) {
                            log.info("Success to connect {}", getURI());
                            break;
                        }

                        log.info("Failed to connect {}", getURI());
                        long sleep = Math.min(5000L, tryCount * 300);
                        TimeUnit.MILLISECONDS.sleep(sleep);
                        tryCount++;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            task.setDaemon(true);
            task.start();
        }

        private void closeConnection() {
            this.dispose = true;
            close();
        }
    }
}