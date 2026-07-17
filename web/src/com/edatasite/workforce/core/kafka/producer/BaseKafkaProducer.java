package com.edatasite.workforce.core.kafka.producer;

import com.edatasite.workforce.core.kafka.data.KafkaData;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gson.Gson;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public abstract class BaseKafkaProducer<T> {

    protected static final Logger log = LoggerFactory.getLogger(BaseKafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    protected void send(String topicName, T data, String sessionID, String companyId, String clusterType) {
        KafkaData<T> kafkaData = new KafkaData<>(data, sessionID, companyId, clusterType);
        String message = new Gson().toJson(kafkaData);
        kafkaTemplate.send(topicName, message);
    }

    protected ListenableFuture<SendResult<String, String>> send(String topicName, T data, String key) {
        KafkaData<T> kafkaData = new KafkaData<>(data, SecurityContext.getInstance().getSessionId(), SecurityContext.getInstance().getCompanyId(), SecurityContext.getInstance().getDatabase());
        String message = new Gson().toJson(kafkaData);

        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, message);
        record.headers().add("eventID", generateEventId());

        return kafkaTemplate.send(record);
    }

    public abstract void sendMessage(T data, String sessionID, String companyId, String clusterType);

    public abstract ListenableFuture<SendResult<String, String>> sendMessage(T data, String key);

    private byte[] generateEventId() {
        return UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
    }
}
