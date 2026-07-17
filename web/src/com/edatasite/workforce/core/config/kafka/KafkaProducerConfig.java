package com.edatasite.workforce.core.config.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class KafkaProducerConfig {

    @Value(value = "${kafka.producer.bootstrap-servers}")
    private String kafkaUrl;
    @Value(value = "${kafka.producer.properties.enable.idempotence}")
    private Boolean idempotence;
    @Value(value = "${kafka.producer.properties.max.in.flight.request}")
    private Integer maxInFlightRequests;
    @Value(value = "${kafka.producer.acks}")
    private String acks;
    @Value(value = "${kafka.producer.properties.delivery.timeout}")
    private Integer deliveryTimeout;
    @Value(value = "${kafka.producer.properties.request.timeout}")
    private Integer requestTimeout;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "20971520");
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotence);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequests);
        configProps.put(ProducerConfig.ACKS_CONFIG, acks);
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeout);
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
        DefaultKafkaProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(configProps);
//        pf.setTransactionIdPrefix(buildTxId() + "-");
        return pf;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

//    @Bean
//    public KafkaTransactionManager<String, String> kafkaTransactionManager(ProducerFactory<String, String> producerFactory) {
//        return new KafkaTransactionManager<>(producerFactory);
//    }

    static String buildTxId() {
        String host;
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        String instance = System.getenv().getOrDefault("INSTANCE_ID", UUID.randomUUID().toString());
        return "kpi-service" + "-" + host + "-" + instance;
    }

}
