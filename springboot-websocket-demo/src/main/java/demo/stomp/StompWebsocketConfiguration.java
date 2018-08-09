package demo.stomp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * https://spring.io/guides/gs/messaging-stomp-websocket/
 * @author zacconding
 * @Date 2018-08-09
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Profile("stomp")
@Configuration
@EnableWebSocketMessageBroker // enables WebSocket message handling, backed by a message broker
public class StompWebsocketConfiguration implements WebSocketMessageBrokerConfigurer {

    // configure the message broker
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // to enable a simple memory-based message broker to carry the
        // greeting messages back to the client on destinations prefixed with "/topic".
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }
}
