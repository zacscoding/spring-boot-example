package demo.client;

import org.java_websocket.handshake.ServerHandshake;

/**
 * @author zacconding
 * @Date 2018-12-05
 * @GitHub : https://github.com/zacscoding
 */
public interface WebSocketListener {

    void onOpen(ServerHandshake serverHandshake);

    void onMessage(String message);

    void onClose(int code, String reason, boolean remote);

    void onError(Exception ex);
}
