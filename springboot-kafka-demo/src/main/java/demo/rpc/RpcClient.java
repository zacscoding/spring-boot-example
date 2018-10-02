package demo.rpc;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
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
@Profile({"rpc", "rpc2"})
@Component
public class RpcClient {

    private Random random = new Random();
    @Value("${rpc.kafka.topic.request-topic}")
    private String requestTopic;
    @Value("${rpc.kafka.topic.requestreply-topic}")
    private String replyTopic;

    private long request = 0L;
    private long requestFail = 0L;
    private long totalElapsed = 0L;

    @Autowired
    private ReplyingKafkaTemplate<String, RpcRequest, RpcResponse> kafkaTemplate;

    @Scheduled(fixedRate = 5000L, initialDelay = 5000L)
    public void generate() throws Exception {
        // int randomThread = random.nextInt(100) + 1;
        // int randomThread = 1;
        int randomThread = 3;
        log.info("\n >> Start client. threads : {}", randomThread);
        Thread[] threads = new Thread[randomThread];
        for (int i = 0; i < randomThread; i++) {
            threads[i] = new Thread(generateRunnable(i));
            threads[i].setDaemon(true);
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < randomThread; i++) {
            threads[i].start();
        }

        for (int i = 0; i < randomThread; i++) {
            threads[i].join();
        }
        long elapsed = System.currentTimeMillis() - start;

        log.info("## Result of execute. task : {} | time : {}[MS] ==> average : {} [MS]", randomThread, elapsed, (double) elapsed / randomThread);

        request += randomThread;
        totalElapsed += elapsed;
        log.info("## Total result : {} [MS] >> Total Request : {}, Fail : {}", (double) totalElapsed / request, request, requestFail);
        log.info("-----------------------------------------------------------------------");
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

                long start = System.currentTimeMillis();
                log.info("## Before request : {}", start);
                RequestReplyFuture<String, RpcRequest, RpcResponse> sendAndReceive = kafkaTemplate.sendAndReceive(requestProducerRecord);
                ConsumerRecord<String, RpcResponse> consumerRecord = sendAndReceive.get();
                long end = System.currentTimeMillis();
                log.info("## After request : {} => {}", start, (end - start));
                StringBuilder sb = new StringBuilder("//=============================================\n");
                sb.append("<<<<<< Rpc Client >>>>>>\n")
                  .append("> request count : ").append(index).append("\n")
                  .append("> request : ").append(request).append("\n")
                  .append("> offset : ").append(consumerRecord.offset()).append("\n")
                  .append("> topic : ").append(consumerRecord.topic()).append("\n")
                  .append("===============================================//");

                //log.info("\n" + sb.toString());

                if (request.getNum1() + request.getNum2() != consumerRecord.value().getSum()) {
                    log.warn("Find invalied request response !!");
                }


            } catch (Exception e) {
                log.warn("Exception while producing", e);
                synchronized (this) {
                    requestFail += 1L;
                }
            }
        };
    }
}