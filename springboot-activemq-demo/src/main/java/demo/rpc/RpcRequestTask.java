package demo.rpc;

import java.util.Random;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-12
 * @GitHub : https://github.com/zacscoding
 */
@Profile("rpc")
@Slf4j(topic = "Request-Task")
//@Component
public class RpcRequestTask {

    private RpcClient client;
    private long requestId;

    @Autowired
    private RpcRequestTask(RpcClient client) {
        this.client = client;
        this.requestId = 0L;
    }

    @Scheduled(fixedRate = 5000L, initialDelay = 1000L)
    public void generateRpcRequest() {
        int sleepTime = new Random().nextInt(5);
        String uuid = UUID.randomUUID().toString();
        JsonRpcRequest request = new JsonRpcRequest(String.valueOf(requestId), sleepTime, uuid);
        JsonRpcResponse response = client.processRequest(request);
        log.info("\nrequest : {} ==> response : {}", request, response);
        requestId += 1L;
    }
}