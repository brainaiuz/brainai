package com.edatasite.workforce.core.kafka.listener;

import com.edatasite.workforce.core.kafka.data.KafkaData;
import com.edatasite.workforce.core.kafka.data.ProductDataMQ;
import com.edatasite.workforce.core.kafka.util.KafkaConstants;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EntityType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("productSolrEventListener")
public class ProductEventListener extends BaseKAFKAListener<ProductDataMQ> {

    @Override
    protected KafkaData<ProductDataMQ> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<KafkaData<ProductDataMQ>>() {
        }.getType());
    }

    @Override
    @KafkaListener(topics = {
            KafkaConstants.Topic.indexProductSolrTopic
    }, groupId = KafkaConstants.Group.indexProduct,
            concurrency = "3",
            containerFactory = "kafkaEventListenerContainerFactory")
    public void receiveMessage(@Payload String data, @Header("eventID") String eventID, @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        KafkaData<ProductDataMQ> message = convertMessage(data);

        if (message.getClusterType() == null || message.getClusterType().isEmpty() || message.getCompanyId() == null) {
            System.out.println("Incorrect data ");
            return;
        }

        ProductDataMQ dataMQ = message.getDataMQ();

        if (dataMQ.getProductList().isEmpty()) {
            log.warn("No valid product IDs in message: {}", dataMQ);
            return;
        }

        if (handlerService.eventExists(eventID)) {
            log.info("Event with ID: {} exists", eventID);
            return;
        }

        sync.execute(getSyncKey(message.getCompanyId(), key), () -> {
            var edsProcessedEvent = handlerService.generateEventProcessor(eventID, dataMQ.getProductList().get(0), EntityType.PRODUCT);
            try {
                handlerService.reIndexSolrProducts(dataMQ.getProductList());
                handlerService.updateEventStatus(edsProcessedEvent.getObjectID(), EventStatus.COMPLETED);
            } catch (Exception e) {
                log.error("Error occurred while processing event", e);
                handlerService.updateEventStatus(edsProcessedEvent.getObjectID(), EventStatus.FAILED);
            }
        });
    }

    private String getSyncKey(String companyId, String key) {
        return companyId + "_" + key;
    }
}
