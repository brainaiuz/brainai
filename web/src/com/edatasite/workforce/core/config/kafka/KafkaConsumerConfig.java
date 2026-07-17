package com.edatasite.workforce.core.config.kafka;

import com.edatasite.workforce.core.config.kafka.exceptions.NonRetryableException;
import com.edatasite.workforce.core.config.kafka.exceptions.RetryableException;
import com.edatasite.workforce.core.kafka.data.KafkaData;
import com.edatasite.workforce.core.kafka.util.KafkaConstants;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFODataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.FifoFailureService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${kafka.producer.bootstrap-servers}")
    private String kafkaUrl;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private FifoFailureService failureMarkerService;
    @Autowired
    private CompanyManager companyManager;

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    private static final Gson GSON = new Gson();


    public ConsumerFactory<String, String> consumerFactory(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "20971520");
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "20971520");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "10000");
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "3600000");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    public ConcurrentKafkaListenerContainerFactory<String, String> defaultKafkaListenerContainerFactory(String groupId, DeadLetterPublishingRecoverer deadLetterPublishingRecoverer) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(deadLetterPublishingRecoverer, new FixedBackOff(1000, 5));
        errorHandler.addRetryableExceptions(RetryableException.class);
        errorHandler.addNotRetryableExceptions(NonRetryableException.class, DeserializationException.class);

        factory.setConsumerFactory(consumerFactory(groupId));
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    public RecordInterceptor<String, String> tenantContextInterceptor() {
        return new RecordInterceptor<>() {

            @Override
            public ConsumerRecord<String, String> intercept(ConsumerRecord<String, String> rec) {
                log.error("---------------- THE FIRST INTERCEPTOR ----------------");
                try {
                    String payloadStr = null;
                    Object v = rec.value();
                    if (v instanceof String s) {
                        payloadStr = s;
                    } else if (v instanceof byte[] bytes) {
                        payloadStr = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
                    }

                    if (payloadStr != null && !payloadStr.isBlank()) {
                        KafkaData<FIFODataMQ> message = GSON.fromJson(payloadStr, new TypeToken<KafkaData<FIFODataMQ>>() {
                        }.getType());

                        String currentDatabase = message.getClusterType();
                        String currentCompanyId = message.getCompanyId();
                        String sessionID = message.getSessionID();

                        SecurityContext.getInstance().setDatabase(currentDatabase);

                        if (companyManager.schemaExists(currentCompanyId)) {
                            SecurityContext.getInstance().setCompanyId(currentCompanyId);
                            if (sessionID != null && !sessionID.isEmpty()) {
                                SecurityContext.getInstance().setSessionId(sessionID);
                            }
                        }
                    }
                } catch (Exception ex) {
                    log.error("ERROR OCCURRED WHILE RECEIVING MESSAGE", ex);
                }
                return rec;
            }

            @Override
            public ConsumerRecord<String, String> intercept(ConsumerRecord<String, String> rec, Consumer<String, String> consumer) {
                log.error("---------------- THE SECOND INTERCEPTOR ----------------");
                try {
                    String payloadStr = null;
                    Object v = rec.value();
                    if (v instanceof String s) {
                        payloadStr = s;
                    } else if (v instanceof byte[] bytes) {
                        payloadStr = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
                    }

                    if (payloadStr != null && !payloadStr.isBlank()) {
                        KafkaData<FIFODataMQ> message = GSON.fromJson(payloadStr, new TypeToken<KafkaData<FIFODataMQ>>() {
                        }.getType());

                        String currentDatabase = message.getClusterType();
                        String currentCompanyId = message.getCompanyId();
                        String sessionID = message.getSessionID();

                        SecurityContext.getInstance().setDatabase(currentDatabase);

                        if (companyManager.schemaExists(currentCompanyId)) {
                            SecurityContext.getInstance().setCompanyId(currentCompanyId);
                            if (sessionID != null && !sessionID.isEmpty()) {
                                SecurityContext.getInstance().setSessionId(sessionID);
                            }
                        }
                    }
                } catch (Exception ex) {
                    log.error("ERROR OCCURRED WHILE RECEIVING MESSAGE", ex);
                }
                return rec;
            }

            @Override
            public void afterRecord(ConsumerRecord<String, String> record, Consumer<String, String> consumer) {
                RecordInterceptor.super.afterRecord(record, consumer);
                SecurityContext.setServerSecurityContext(null);
            }
        };
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaEventListenerContainerFactory(DeadLetterPublishingRecoverer deadLetterPublishingRecoverer) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = defaultKafkaListenerContainerFactory(KafkaConstants.Group.defaultGroup, deadLetterPublishingRecoverer);
        factory.setConcurrency(3);
        factory.setRecordInterceptor(tenantContextInterceptor());
        factory.getContainerProperties()
                .setAckMode(ContainerProperties.AckMode.RECORD);
        return factory;
    }

    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer() {
        var dest = (BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition>)
                (rec, ex) -> new TopicPartition(rec.topic() + ".DLT", rec.partition());

        return new DeadLetterPublishingRecoverer(kafkaTemplate, (record, ex) -> {
            try {
                String payloadStr = null;
                Object v = record.value();
                Object key = record.key();
                if (v instanceof String s) {
                    payloadStr = s;
                } else if (v instanceof byte[] bytes) {
                    payloadStr = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
                }

                if (payloadStr != null && !payloadStr.isBlank()) {
                    KafkaData<FIFODataMQ> event = GSON.fromJson(payloadStr, new TypeToken<KafkaData<FIFODataMQ>>() {
                    }.getType());
                    String reason = ex.getClass().getSimpleName() + ": " + ex.getMessage();
                    failureMarkerService.trackFailur(event.getDataMQ(), (String) key, reason);
                } else {
                    // ToDo
                }
            } catch (Exception markEx) {
                log.error("Failed to mark entity as FAILED before sending to DLT", markEx);
            }
            return dest.apply(record, ex);
        });
    }
}
