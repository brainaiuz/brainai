package com.edatasite.workforce.core.kafka.producer;

import com.edatasite.workforce.core.kafka.data.SOLRDataMQ;
import com.edatasite.workforce.core.kafka.util.KafkaConstants;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
public class SolrEventProducer extends BaseKafkaProducer<SOLRDataMQ> {

    @Override
    public void sendMessage(SOLRDataMQ data, String sessionID, String companyId, String clusterType) {

    }

    @Override
    public ListenableFuture<SendResult<String, String>> sendMessage(SOLRDataMQ data, String key) {
        return send(KafkaConstants.Topic.indexInvoiceSolrEntityTopic, data, key);
    }
}
