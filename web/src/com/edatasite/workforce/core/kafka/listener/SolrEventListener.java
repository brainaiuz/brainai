package com.edatasite.workforce.core.kafka.listener;

import com.edatasite.workforce.core.kafka.data.KafkaData;
import com.edatasite.workforce.core.kafka.data.SOLRDataMQ;
import com.edatasite.workforce.core.kafka.util.KafkaConstants;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component("solrEventListener")
public class SolrEventListener extends BaseKAFKAListener<SOLRDataMQ> {

    @Override
    protected KafkaData<SOLRDataMQ> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<KafkaData<SOLRDataMQ>>() {
        }.getType());
    }

    @Override
    @KafkaListener(topics = {
            KafkaConstants.Topic.indexInvoiceSolrEntityTopic
    }, groupId = KafkaConstants.Group.indexInvoice,
            concurrency = "3",
            containerFactory = "kafkaEventListenerContainerFactory")
    public void receiveMessage(@Payload String data, @Header("eventID") String eventID, @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        KafkaData<SOLRDataMQ> message = convertMessage(data);
        log.info("\n INVOICE SOLR EVENT TO THE KAFKA: {}", data);
        if (message.getClusterType() == null || message.getClusterType().isEmpty() || message.getCompanyId() == null) {
            System.out.println("Incorrect data ");
            return;
        }
        if (handlerService.eventExists(eventID)) {
            log.info("Event with ID: {} exists", eventID);
            return;
        }
        SOLRDataMQ dataMQ = message.getDataMQ();
        String currentCompanyId = message.getCompanyId();

        sync.execute(getSyncKey(dataMQ, currentCompanyId), () -> {
            var edsProcessedEvent = handlerService.generateEventProcessor(eventID, dataMQ.getEntityId(), dataMQ.getEntityType());
            try {
                handlerService.reIndexSolrEntity(dataMQ);
                handlerService.updateEventStatus(edsProcessedEvent.getObjectID(), EventStatus.COMPLETED);
            } catch (Exception e) {
                log.error("Error occurred while processing event", e);
                handlerService.updateEventStatus(edsProcessedEvent.getObjectID(), EventStatus.FAILED);
            }
        });
    }

    private String getSyncKey(SOLRDataMQ item, String companyId) {
        StringBuilder key = new StringBuilder();
        key.append(companyId + "_" + item.getEntityId());
        if (item.getEntityType() != null) {
            key.append("_" + item.getEntityType().name());
        }
        return key.toString();
    }
}
