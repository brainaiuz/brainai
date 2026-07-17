package com.edatasite.workforce.core.kafka.producer;

import com.edatasite.workforce.core.kafka.util.KafkaConstants;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFODataMQ;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component("kafkaEventProducer")
public class KafkaEventProducer extends BaseKafkaProducer<FIFODataMQ> {

    @Override
    public void sendMessage(FIFODataMQ data, String sessionID, String companyId, String clusterType) {

    }

    @Override
    public ListenableFuture<SendResult<String, String>> sendMessage(FIFODataMQ data, String key) {
        String topic = null;
        switch (data.getEntityType()) {
            case SALES_INVOICE ->
                    topic = data.isRemoving() ? KafkaConstants.Topic.salesInvoiceDeletedTopic : KafkaConstants.Topic.salesInvoiceApprovedTopic;
            case PURCHASE_INVOICE ->
                    topic = data.isRemoving() ? KafkaConstants.Topic.purchaseInvoiceDeletedTopic : KafkaConstants.Topic.purchaseInvoiceApprovedTopic;
            case GDN ->
                    topic = data.isRemoving() ? KafkaConstants.Topic.goodsDeliveryNoteDeletedTopic : KafkaConstants.Topic.goodsDeliveryNoteApprovedTopic;
            case GRN ->
                    topic = data.isRemoving() ? KafkaConstants.Topic.goodsReceivedNoteDeletedTopic : KafkaConstants.Topic.goodsReceivedNoteApprovedTopic;
            case CUSTOMER_CREDIT_NOTE ->
                    topic = data.isRemoving() ? KafkaConstants.Topic.removeCustomerCreditNoteTransactionTopic : KafkaConstants.Topic.customerCreditNoteTransactionTopic;
            case SUPPLIER_CREDIT_NOTE ->
                    topic = data.isRemoving() ? KafkaConstants.Topic.removeSupplierCreditNoteTransactionTopic : KafkaConstants.Topic.supplierCreditNoteTransactionTopic;
            case STOCK_ADJUSTMENT_IN ->
                    topic = data.isRemoving() ? KafkaConstants.Topic.inStockAdjustmentRemovedTopic : KafkaConstants.Topic.inStockAdjustmentApprovedTopic;
            case STOCK_ADJUSTMENT_OUT ->
                    topic = data.isRemoving() ? KafkaConstants.Topic.outStockAdjustmentRemovedTopic : KafkaConstants.Topic.outStockAdjustmentApprovedTopic;
            case STOCK_TRANSFER_IN ->
                    topic = data.isRemoving() ? KafkaConstants.Topic.inStockTransferRemovedTopic : KafkaConstants.Topic.inStockTransferApprovedTopic;
            case STOCK_TRANSFER_OUT ->
                    topic = data.isRemoving() ? KafkaConstants.Topic.outStockTransferRemovedTopic : KafkaConstants.Topic.outStockTransferApprovedTopic;
        }
        if (topic == null) return null;
        return send(topic, data, key);
    }
}
