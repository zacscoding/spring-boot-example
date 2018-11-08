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
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * @author zacconding
 * @Date 2018-09-04
 * @GitHub : https://github.com/zacscoding
 */
@Profile("rpc2")
@Configuration
public class RpcKafkaConfiguration2 {

    @Value("${rpc.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${rpc.kafka.topic.request-topic}")
    private String requestTopic;

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
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // props.put(ConsumerConfig.GROUP_ID_CONFIG, "helloworld");
        return props;
    }

    /**
     * Rpc request consumer
     */
    @Bean
    public ConsumerFactory<String, RpcRequest> requestConsumerFactory() {
        Map<String, Object> configs = generateDefaultConfigs();
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "helloworld1");
        return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new JsonDeserializer<>(RpcRequest.class));
    }

    /**
     * Rpc response consumer
     */
    @Bean
    public ConsumerFactory<String, RpcResponse> responseConsumerFactory() {
        Map<String, Object> configs = generateDefaultConfigs();
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "helloworld2");
        return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new JsonDeserializer<>(RpcResponse.class));
    }

    /**
     * Rpc request producer factory for ReplyingKafkaTemplate
     */
    @Bean
    public ProducerFactory<String, RpcRequest> rpcRequestProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * Rpc response producer
     */
    @Bean
    public ProducerFactory<String, RpcResponse> rpcResponseProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /*@Bean
    public KafkaMessageListenerContainer<String, RpcRequest> requestContainer(RpcReplyingKafkaConsumer2 consumer) {
        ContainerProperties containerProperties = new ContainerProperties(requestTopic);
        containerProperties.setMessageListener(consumer);
        return new KafkaMessageListenerContainer<>(requestConsumerFactory(), containerProperties);
    }

    *//**
     * Listener Container to be set up in ReplyingKafkaTemplate
     *//*
    @Bean
    public KafkaMessageListenerContainer<String, RpcResponse> replyContainer() {
        ContainerProperties containerProperties = new ContainerProperties(requestReplyTopic);
        return new KafkaMessageListenerContainer<>(responseConsumerFactory(), containerProperties);
    }*/

    @Bean
    public ConcurrentMessageListenerContainer<String, RpcRequest> requestContainer(RpcReplyingKafkaConsumer2 consumer) {
        ContainerProperties containerProperties = new ContainerProperties(requestTopic);
        containerProperties.setMessageListener(consumer);

        ConcurrentMessageListenerContainer<String, RpcRequest> container = new ConcurrentMessageListenerContainer<>(requestConsumerFactory(), containerProperties);
        container.setConcurrency(Integer.valueOf(5));

        return container;
    }


    @Bean
    public ConcurrentMessageListenerContainer<String, RpcResponse> replyContainer() {
        ContainerProperties containerProperties = new ContainerProperties(requestReplyTopic);

        ConcurrentMessageListenerContainer<String, RpcResponse> container = new ConcurrentMessageListenerContainer<>(responseConsumerFactory(), containerProperties);
        container.setConcurrency(Integer.valueOf(5));

        return container;
    }

    /**
     * ReplyKafkaTemplate
     */
    /*@Bean
    public ReplyingKafkaTemplate<String, RpcRequest, RpcResponse> replyKafkaTemplate(ConcurrentKafkaListenerContainerFactory<String, RpcResponse> replyContainer) {
        ReplyingKafkaTemplate<String, RpcRequest, RpcResponse> template = new ReplyingKafkaTemplate<>(rpcRequestProducerFactory(), replyContainer);
        template.setReplyTimeout(10000L);
        return template;
    }*/

    @Bean
    public ReplyingKafkaTemplate<String, RpcRequest, RpcResponse> replyKafkaTemplate(ConcurrentMessageListenerContainer<String, RpcResponse> replyContainer) {
        ReplyingKafkaTemplate<String, RpcRequest, RpcResponse> template = new ReplyingKafkaTemplate<>(rpcRequestProducerFactory(), replyContainer);
        template.setReplyTimeout(10000L);
        return template;
    }

    // Concurrent Listner container factory
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, RpcResponse>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RpcResponse> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(responseConsumerFactory());
        factory.setConcurrency(Integer.valueOf(100));
        // NOTE - set up of reply template
        factory.setReplyTemplate(responseKafkaTemplate());

        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, RpcRequest>> requestListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RpcRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(requestConsumerFactory());
        factory.setConcurrency(Integer.valueOf(100));
        // NOTE - set up of reply template
        factory.setReplyTemplate(requestKafkaTemplate());
        return factory;
    }

    // Standard KafkaTemplate
    @Bean
    public KafkaTemplate<String, RpcResponse> responseKafkaTemplate() {
        KafkaTemplate<String, RpcResponse> template = new KafkaTemplate<>(rpcResponseProducerFactory(), false);
        template.setDefaultTopic(requestReplyTopic);
        return template;
    }

    @Bean
    public KafkaTemplate<String, RpcRequest> requestKafkaTemplate() {
        KafkaTemplate<String, RpcRequest> template = new KafkaTemplate<>(rpcRequestProducerFactory(), false);
        template.setDefaultTopic(requestTopic);
        return template;
    }

    private Map<String, Object> generateDefaultConfigs() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return props;
    }
}