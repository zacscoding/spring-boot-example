package demo.rpc;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-04
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Profile("rpc")
@Component
public class RpcRequestGenerator {

    private Random random = new Random();
    @Value("${rpc.kafka.topic.request-topic}")
    private String requestTopic;
    @Value("${rpc.kafka.topic.requestreply-topic}")
    private String replyTopic;

    @Autowired
    private ReplyingKafkaTemplate<String, RpcRequest, RpcResponse> kafkaTemplate;

    @Scheduled(fixedRate = 5000L, initialDelay = 1000L)
    public void generate() {
        int randomThread = random.nextInt(50) + 1;
        log.info("## Random thread count : {}", randomThread);
        for (int i = 0; i < randomThread; i++) {
            Thread t = new Thread(generateRunnable(i));
            t.setDaemon(true);
            t.start();
        }
    }

    public Runnable generateRunnable(final int index) {
        return () -> {
            try {
                int num1 = random.nextInt(1000);
                int num2 = random.nextInt(1000);
                RpcRequest request = new RpcRequest(num1, num2);
                ProducerRecord<String, RpcRequest> requestProducerRecord = new ProducerRecord<>(requestTopic, request);
                // set reply topic in header
                requestProducerRecord.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));
                log.info("[Producer-{}] generated request : {}", index, request);

                RequestReplyFuture<String, RpcRequest, RpcResponse> sendAndReceive = kafkaTemplate.sendAndReceive(requestProducerRecord);
                ConsumerRecord<String, RpcResponse> consumerRecord = sendAndReceive.get();

                log.info("## Response-{} >> req : {} | offset : {} | topic : {} | res : {}", index, consumerRecord.offset(), consumerRecord.topic(), consumerRecord.value());
            } catch (Exception e) {
                log.warn("Exception while producing", e);
            }
        };
    }
}