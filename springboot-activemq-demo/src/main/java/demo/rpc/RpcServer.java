package demo.rpc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author zacconding
 * @Date 2018-09-12
 * @GitHub : https://github.com/zacscoding
 */
@Profile("rpc")
@Slf4j(topic = "consumer")
@Component
public class RpcServer {

    private ExecutorService executor = Executors.newFixedThreadPool(500);
    @Autowired
    private JmsTemplate jmsTemplate;

    /*@JmsListener(destination = "rpc.queue", containerFactory = "listenerContainerFactory")
    public void receiveMessage(JsonRpcRequest rpcRequest) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        // System.out.println(System.currentTimeMillis());
        // log.info("[receive {}] request : {}", this, rpcRequest);
        if (rpcRequest.getElapsed() > 0L) {
            TimeUnit.SECONDS.sleep(rpcRequest.getElapsed());
        }
        jmsTemplate.convertAndSend("rpc.queue", new JsonRpcResponse(rpcRequest.getId(), (StringUtils.hasLength(rpcRequest.getRequestBody()) ? rpcRequest.getRequestBody().toUpperCase() : "")));
    }*/


    @JmsListener(destination = "rpc.queue", containerFactory = "listenerContainerFactory")
    public JsonRpcResponse receiveMessage(JsonRpcRequest rpcRequest) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        // System.out.println(System.currentTimeMillis());
        // log.info("[receive {}] request : {}", this, rpcRequest);
        if (rpcRequest.getElapsed() > 0L) {
            TimeUnit.SECONDS.sleep(rpcRequest.getElapsed());
        }
        return new JsonRpcResponse(rpcRequest.getId(), (StringUtils.hasLength(rpcRequest.getRequestBody()) ? rpcRequest.getRequestBody().toUpperCase() : ""));
    }
}