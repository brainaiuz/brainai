package com.edatasite.workforce.core.kafka.util;

public interface KafkaConstants {

    interface Group {
        String defaultGroup = "kpi";
        String indexInvoice = "index-invoice-group";
        String indexProduct = "index-product-group";
    }

    interface Topic {
        String salesInvoiceApprovedTopic = "sales-invoice-approved-topic";
        String salesInvoiceDeletedTopic = "sales-invoice-deleted-topic";
        String purchaseInvoiceApprovedTopic = "purchase-invoice-approved-topic";
        String purchaseInvoiceDeletedTopic = "purchase-invoice-deleted-topic";
        String goodsDeliveryNoteApprovedTopic = "goods-delivery-note-approved-topic";
        String goodsDeliveryNoteDeletedTopic = "goods-delivery-note-deleted-topic";
        String goodsReceivedNoteApprovedTopic = "goods-received-note-approved-topic";
        String goodsReceivedNoteDeletedTopic = "goods-received-note-deleted-topic";
        String customerCreditNoteTransactionTopic = "customer-credit-note-transaction-topic";
        String removeCustomerCreditNoteTransactionTopic = "remove-customer-credit-note-transaction-topic";
        String supplierCreditNoteTransactionTopic = "supplier-credit-note-transaction-topic";
        String removeSupplierCreditNoteTransactionTopic = "remove-supplier-credit-note-transaction-topic";
        String inStockAdjustmentApprovedTopic = "in-stock-adjustment-approved-topic";
        String inStockAdjustmentRemovedTopic = "in-stock-adjustment-removed-topic";
        String outStockAdjustmentApprovedTopic = "out-stock-adjustment-approved-topic";
        String outStockAdjustmentRemovedTopic = "out-stock-adjustment-removed-topic";
        String inStockTransferApprovedTopic = "in-stock-transfer-approved-topic";
        String inStockTransferRemovedTopic = "in-stock-transfer-removed-topic";
        String outStockTransferApprovedTopic = "out-stock-transfer-approved-topic";
        String outStockTransferRemovedTopic = "out-stock-transfer-removed-topic";
        String runOutTransactionTopic = "run-out-transaction-topic";

        String indexInvoiceSolrEntityTopic = "index-invoice-solr-topic";
        String indexProductSolrTopic = "index-product-solr-topic";

        String genericEventPostProcessorTopic = "event-post-processor-topic";
        String taskEventPostProcessorTopic = "task-event-post-processor-topic";
        String taskDocumentsPostProcessorTopic = "task-documents-post-processor-topic";
        String workflowEventPostProcessorTopic = "workflow-event-post-processor-topic";
        String importFileEventPostProcessorTopic = "import-file-event-post-processor-topic";
        String customTransactionEventPostProcessorTopic = "custom-transaction-event-post-processor-topic";
        String salesInvoiceEventPostProcessorTopic = "sale-invoice-event-post-processor-topic";
        String salesQuoteEventPostProcessorTopic = "sale-quote-event-post-processor-topic";
        String salesOrderEventPostProcessorTopic = "sale-order-event-post-processor-topic";

    }
}
