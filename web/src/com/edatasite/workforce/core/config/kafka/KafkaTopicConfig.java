package com.edatasite.workforce.core.config.kafka;

import com.edatasite.workforce.core.kafka.util.KafkaConstants;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${kafka.producer.bootstrap-servers}")
    private String kafkaUrl;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic genericEventPostProcessorTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.genericEventPostProcessorTopic)
                .partitions(50)
                .replicas(1)
                .config("max.message.bytes", "20971520")
                .build();
    }
    @Bean
    public NewTopic taskEventPostProcessorTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.taskEventPostProcessorTopic)
                .partitions(20)
                .replicas(1)
                .config("max.message.bytes", "20971520")
                .build();
    }
    @Bean
    public NewTopic workflowEventPostProcessorTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.workflowEventPostProcessorTopic)
                .partitions(30)
                .replicas(1)
                .config("max.message.bytes", "20971520")
                .build();
    }

    @Bean
    public NewTopic salesInvoiceApprovedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.salesInvoiceApprovedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic salesInvoiceDeletedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.salesInvoiceDeletedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic purchaseInvoiceApprovedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.purchaseInvoiceApprovedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic purchaseInvoiceDeletedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.purchaseInvoiceDeletedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic goodsDeliveryNoteApprovedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.goodsDeliveryNoteApprovedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic goodsDeliveryNoteDeletedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.goodsDeliveryNoteDeletedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic goodsReceivedNoteApprovedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.goodsReceivedNoteApprovedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic goodsReceivedNoteDeletedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.goodsReceivedNoteDeletedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic customerCreditNoteTransactionTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.customerCreditNoteTransactionTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic removeCustomerCreditNoteTransactionTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.removeCustomerCreditNoteTransactionTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic supplierCreditNoteTransactionTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.supplierCreditNoteTransactionTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic removeSupplierCreditNoteTransactionTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.removeSupplierCreditNoteTransactionTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic inStockAdjustmentApprovedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.inStockAdjustmentApprovedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic inStockAdjustmentRemovedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.inStockAdjustmentRemovedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic outStockAdjustmentApprovedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.outStockAdjustmentApprovedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic outStockAdjustmentRemovedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.outStockAdjustmentRemovedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic inStockTransferApprovedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.inStockTransferApprovedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic inStockTransferRemovedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.inStockTransferRemovedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic outStockTransferApprovedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.outStockTransferApprovedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic outStockTransferRemovedTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.outStockTransferRemovedTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic updateSolrEntityTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.indexInvoiceSolrEntityTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic updateProductSolrEntityTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.indexProductSolrTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }

    @Bean
    public NewTopic runOutTransactionTopic() {
        return TopicBuilder.name(KafkaConstants.Topic.runOutTransactionTopic)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("max.message.bytes", "20971520", "min.insync.replicas", "2"))
                .build();
    }
}
