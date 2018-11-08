package demo.rpc;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.validation.constraints.Null;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-10-02
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Profile("rpc2")
@Component
public class RpcReplyingKafkaConsumer2 implements MessageListener<String, RpcRequest> {

    // @Value("${rpc.kafka.topic.requestreply-topic}")
    // private String replyTopic;

    @Autowired
    private KafkaTemplate<String, RpcResponse> kafkaTemplate;
    private ThreadPoolTaskExecutor taskExecutor;

    @PostConstruct
    private void setUp() {
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(200);
        taskExecutor.setThreadNamePrefix("rpc-server");
        taskExecutor.initialize();
        taskExecutor.setDaemon(true);
    }

    /*public RpcResponse listen(RpcRequest request) {
        RpcResponse response = new RpcResponse(request.getNum1() + request.getNum2());
        // log.info("[Consumer] consume rpc request : {} - res : {}", request, response);
        return response;
    }*/

    @Override
    public void onMessage(ConsumerRecord<String, RpcRequest> record) {
        System.out.println("## Check out thread at server " + Thread.currentThread().getName() + "-" + Thread.currentThread().getId());
        final Runnable runnable = () -> {
            System.out.println("## Check in thread at server " + Thread.currentThread().getName() + "-" + Thread.currentThread().getId());
            RpcRequest request = record.value();
            RpcResponse response = new RpcResponse(request.getNum1() + request.getNum2());

            // temp for dev
            // displayResult(record, response);

            Header replyTopicHeader = extractHeader(record.headers(), KafkaHeaders.REPLY_TOPIC);
            if (replyTopicHeader == null) {
                log.warn("Not exist reply topic at header");
                return;
            }

            Header correlationId = extractHeader(record.headers(), KafkaHeaders.CORRELATION_ID);
            if (correlationId == null) {
                log.warn("Not exist correlation id at header");
                return;
            }

            MessageBuilder<RpcResponse> builder = MessageBuilder.withPayload(response)
                                                                .setHeader(KafkaHeaders.TOPIC, replyTopicHeader.value())
                                                                .setHeader(KafkaHeaders.CORRELATION_ID, correlationId.value());

            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            kafkaTemplate.send(builder.build());
        };

        boolean executor = true;
        if (executor) {
            taskExecutor.execute(runnable);
        } else {
            runnable.run();
        }
    }

    private void displayResult(ConsumerRecord<String, RpcRequest> record, RpcResponse response) {
        RpcRequest request = record.value();

        StringBuilder sb = new StringBuilder("//=============================================\n");
        sb.append("<<<<<< Rpc Server >>>>>>\n")
          .append("> Thread : ").append(Thread.currentThread().getName()).append("-").append(Thread.currentThread().getId()).append("\n")
          .append("> request : ").append(request).append("\n")
          .append("> response : ").append(response).append("\n");

        Header[] headers = record.headers().toArray();
        if (headers == null || headers.length == 0) {
            sb.append("> request header is empty\n");
        } else {
            for (Header header : headers) {
                sb.append("key : ").append(header.key()).append(", value : ").append(new String(header.value())).append("\n");
            }
        }
        sb.append("===============================================//");
        log.info("\n" + sb.toString());
    }

    private Header extractHeader(Headers headers, String key) {
        Iterator<Header> itr = headers.headers(key).iterator();
        return itr.hasNext() ? itr.next() : null;
    }
}
