package demo.rpc;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * @author zacconding
 * @Date 2018-09-04
 * @GitHub : https://github.com/zacscoding
 */
@Profile("rpc")
@EnableKafka // enable detection of @KafkaListener
@Configuration
public class RpcKafkaConfiguration {

    @Value("${rpc.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${rpc.kafka.topic.requestreply-topic}")
    private String requestReplyTopic;

    @Value("${rpc.kafka.consumergroup}")
    private String consumerGroup;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "helloworld");
        return props;
    }

    @Bean
    public ConsumerFactory<String, RpcResponse> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new JsonDeserializer<>(RpcResponse.class));
    }

    @Bean
    public ProducerFactory<String, RpcRequest> rpcRequestProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public ProducerFactory<String, RpcResponse> rpcResponseProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaMessageListenerContainer<String, RpcResponse> replyContainer() {
        ContainerProperties containerProperties = new ContainerProperties(requestReplyTopic);
        return new KafkaMessageListenerContainer<>(consumerFactory(), containerProperties);
    }

    @Bean
    public ReplyingKafkaTemplate<String, RpcRequest, RpcResponse> replyKafkaTemplate() {
        return new ReplyingKafkaTemplate<>(rpcRequestProducerFactory(), replyContainer());
    }

    // Concurrent Listner container factory
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, RpcResponse>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RpcResponse> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // NOTE - set up of reply template
        factory.setConcurrency(Integer.valueOf(100));
        factory.setReplyTemplate(kafkaTemplate());
        return factory;
    }

    // Standard KafkaTemplate
    @Bean
    public KafkaTemplate<String, RpcResponse> kafkaTemplate() {
        return new KafkaTemplate<>(rpcResponseProducerFactory());
    }
}