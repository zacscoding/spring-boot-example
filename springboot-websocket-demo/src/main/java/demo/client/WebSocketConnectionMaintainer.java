package demo.client;

import org.java_websocket.client.WebSocketClient;

/**
 * @author zacconding
 * @Date 2018-12-05
 * @GitHub : https://github.com/zacscoding
 */
public interface WebSocketConnectionMaintainer {

    boolean register(String url, WebSocketListener webSocketListener) throws InterruptedException;

    void remove(String url);

    WebSocketClient getActiveClient() throws NoAvailableClientException;

    int getActiveClientCount();
}
