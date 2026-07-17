package com.edatasite.workforce.core.kafka.producer;

import com.edatasite.workforce.core.kafka.data.EventData;
import com.edatasite.workforce.core.kafka.util.KafkaConstants;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component("eventPostProcessorProducer")
public class EventPostProcessorProducer extends BaseKafkaProducer<EventData> {

    @Override
    public void sendMessage(EventData event, String sessionID, String companyId, String clusterType) {
        String topic;
        switch (event.getType()) {
            case EventTypes.taskEventListener -> topic = KafkaConstants.Topic.taskEventPostProcessorTopic;
            case EventTypes.workflowActionDetectedEventListener -> topic = KafkaConstants.Topic.workflowEventPostProcessorTopic;
            case EventTypes.taskDocumentsReIndexEventListener -> topic = KafkaConstants.Topic.taskDocumentsPostProcessorTopic;
            case EventTypes.importFileCustomEventListener -> topic = KafkaConstants.Topic.importFileEventPostProcessorTopic;
            case EventTypes.saleInvoiceTransactionCustomEventListener -> topic = KafkaConstants.Topic.customTransactionEventPostProcessorTopic;
            case EventTypes.saleInvoiceCustomEventListener -> topic = KafkaConstants.Topic.salesInvoiceEventPostProcessorTopic;
            case EventTypes.salesQuoteEventListener -> topic = KafkaConstants.Topic.salesQuoteEventPostProcessorTopic;
            case EventTypes.salesOrderEventListener -> topic = KafkaConstants.Topic.salesOrderEventPostProcessorTopic;
            default -> topic = KafkaConstants.Topic.genericEventPostProcessorTopic;
        }
        send(topic, event, sessionID, companyId, clusterType);
    }

    @Override
    public ListenableFuture<SendResult<String, String>> sendMessage(EventData data, String key) {
        return null;
    }
}
