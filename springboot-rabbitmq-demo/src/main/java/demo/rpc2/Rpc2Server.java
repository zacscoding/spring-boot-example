package demo.rpc2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author zacconding
 * @Date 2018-10-01
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class Rpc2Server implements MessageListener {

    private RabbitTemplate rabbitTemplate;
    private MessageConverter messageConverter;
    private ThreadPoolTaskExecutor taskExecutor;

    public Rpc2Server(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageConverter = rabbitTemplate.getMessageConverter();
        this.taskExecutor = new ThreadPoolTaskExecutor();
        this.taskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        this.taskExecutor.setMaxPoolSize(100);
        this.taskExecutor.initialize();
        this.taskExecutor.setDaemon(true);
    }

    @Override
    public void onMessage(Message message) {
        if (message == null) {
            return;
        }

        log.info("# onMessage external of excutors : {}({})", Thread.currentThread().getName(), Thread.currentThread().getId());

        taskExecutor.execute(() -> {
            log.info("# onMessage internal of excutors : {}({})", Thread.currentThread().getName(), Thread.currentThread().getId());
            final Rpc2Request req = (Rpc2Request) messageConverter.fromMessage(message);
            final Rpc2Response res = new Rpc2Response();
            res.setRequestId(req.getRequestId());
            res.setRawResponse(req.getRawRequest().toUpperCase());

            MessageProperties properties = message.getMessageProperties();
            rabbitTemplate.convertAndSend(properties.getReplyTo(), res, new CorrelationData(message.getMessageProperties().getCorrelationId()));
        });
    }
}