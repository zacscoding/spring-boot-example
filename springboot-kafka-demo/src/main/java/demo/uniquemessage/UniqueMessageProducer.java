package demo.uniquemessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author zacconding
 * @Date 2018-12-19
 * @GitHub : https://github.com/zacscoding
 */
@Profile("uniquemessage")
@Slf4j(topic = "[PRODUCER]")
@Component
public class UniqueMessageProducer {

    @Autowired
    private KafkaTemplate<String, UniqueMessage> kafkaTemplate;
    private final String topic = "unique-message";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Random random = new Random();

    @PostConstruct
    public void setUp() {
        Thread produceTask = new Thread(() -> {
            log.info("## Start producer task");
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    UniqueMessage message = new UniqueMessage(generateRandomId(), geneateMessage());

                    ProducerRecord<String, UniqueMessage> record = new ProducerRecord<>(
                        topic, message);
                    record.headers().add(KafkaHeaders.MESSAGE_KEY, message.getId().getBytes());
                    ListenableFuture<SendResult<String, UniqueMessage>> result = kafkaTemplate
                        .send(record);

                    result.addCallback(new ListenableFutureCallback<SendResult<String, UniqueMessage>>() {
                        @Override
                        public void onFailure(Throwable error) {
                            log.info("## Failed to send {}. > {}", message, error.getMessage());
                        }

                        @Override
                        public void onSuccess(SendResult<String, UniqueMessage> result) {
                            log.info("## Success to send {}", result.toString());
                        }
                    });
                    TimeUnit.SECONDS.sleep(3L);
                }
            } catch (Exception e) {
                log.error("## Exception occur while producing message", e);
            }
        });

        produceTask.setDaemon(true);
        produceTask.start();
    }

    private String generateRandomId() {
        return String.valueOf(
            random.nextInt(5)
        );
    }

    private String geneateMessage() {
        return UUID.randomUUID().toString();
    }
}
