package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.antkorwin.xsync.XSync;
import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsAdjustmentItem;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.kafka.producer.KafkaEventProducer;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.costofgoods.COGSService;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.StockCalcManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFODataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFOItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.*;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.FifoFailureService;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Transactional
public class TransactionCustomEventListenerImpl extends CustomBusinessEventListenerAdapter implements AccountingConstants {
    private static final Logger log = LoggerFactory.getLogger(TransactionCustomEventListenerImpl.class);
    public static WfmType<EdsObject> TYPE = new WfmType<>(EventTypes.saleInvoiceTransactionCustomEventListener);

    public static final String EVENT_SALES_INVOICE_TRANSACTION = "EVENT_SALES_INVOICE_TRANSACTION";
    public static final String EVENT_REMOVE_SALES_INVOICE_TRANSACTION = "EVENT_REMOVE_SALES_INVOICE_TRANSACTION";
    public static final String EVENT_PURCHASE_INVOICE_TRANSACTION = "EVENT_PURCHASE_INVOICE_TRANSACTION";
    public static final String EVENT_REMOVE_PURCHASE_INVOICE_TRANSACTION = "EVENT_REMOVE_PURCHASE_INVOICE_TRANSACTION";
    public static final String EVENT_GOODS_DELIVIRY_NOTE_TRANSACTION = "EVENT_GOODS_DELIVIRY_NOTE_TRANSACTION";
    public static final String EVENT_REMOVE_GOODS_DELIVIRY_NOTE_TRANSACTION = "EVENT_REMOVE_GOODS_DELIVIRY_NOTE_TRANSACTION";
    public static final String EVENT_GOODS_RECEIVED_NOTE_TRANSACTION = "EVENT_GOODS_RECEIVED_NOTE_TRANSACTION";
    public static final String EVENT_REMOVE_GOODS_RECEIVED_NOTE_TRANSACTION = "EVENT_REMOVE_GOODS_RECEIVED_NOTE_TRANSACTION";
    public static final String EVENT_CUSTOMER_CREDIT_NOTE_TRANSACTION = "EVENT_CUSTOMER_CREDIT_NOTE_TRANSACTION";
    public static final String EVENT_REMOVE_CUSTOMER_CREDIT_NOTE_TRANSACTION = "EVENT_REMOVE_CUSTOMER_CREDIT_NOTE_TRANSACTION";
    public static final String EVENT_SUPPLIER_CREDIT_NOTE_TRANSACTION = "EVENT_SUPPLIER_CREDIT_NOTE_TRANSACTION";
    public static final String EVENT_REMOVE_SUPPLIER_CREDIT_NOTE_TRANSACTION = "EVENT_REMOVE_SUPPLIER_CREDIT_NOTE_TRANSACTION";
    public static final String EVENT_STOCK_ADJUSTMENT_TRANSACTION = "EVENT_STOCK_ADJUSTMENT_TRANSACTION";
    public static final String EVENT_REMOVE_STOCK_ADJUSTMENT_TRANSACTION = "EVENT_REMOVE_STOCK_ADJUSTMENT_TRANSACTION";
    public static final String EVENT_STOCK_TRANSFER_TRANSACTION = "EVENT_STOCK_TRANSFER_TRANSACTION";
    public static final String EVENT_REMOVE_STOCK_TRANSFER_TRANSACTION = "EVENT_REMOVE_STOCK_TRANSFER_TRANSACTION";
    public static final String EVENT_STOCK_TRANSFER_IN_TRANSACTION = "EVENT_STOCK_TRANSFER_IN_TRANSACTION";
    public static final String EVENT_CORRECTION_ASSEMBLY_BUILD_TRANSACTION = "EVENT_CORRECTION_ASSEMBLY_BUILD_TRANSACTION";

    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private ShippingDataManager shippingDataManager;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private WarehouseManager warehouseManager;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private StockAdjustmentManager stockAdjustmentManager;
    @Autowired
    private StockAdjustmentItemManager stockAdjustmentItemManager;
    @Autowired
    private StockTransferManager stockTransferManager;
    @Autowired
    protected XSync<String> stringXSync;
    @Autowired
    private AccountingServiceLocal accountingService;
    @Autowired
    private StockCalcManager stockCalcManager;
    @Autowired
    private FifoFailureService fifoFailureService;
    @Autowired
    private KafkaEventProducer kafkaEventProducer;
    @Autowired
    private GenericSettingsManager genericSettingsManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        System.out.println("TransactionCustomEventListenerImpl - " + event.getEventType());
        if (EVENT_SALES_INVOICE_TRANSACTION.equals(event.getEventType())) {
            onSaleInvoiceTransaction(event, false);
        } else if (EVENT_REMOVE_SALES_INVOICE_TRANSACTION.equals(event.getEventType())) {
            onSaleInvoiceTransaction(event, true);
        } else if (EVENT_PURCHASE_INVOICE_TRANSACTION.equals(event.getEventType())) {
            onPurchaseInvoiceTransaction(event, false);
        } else if (EVENT_REMOVE_PURCHASE_INVOICE_TRANSACTION.equals(event.getEventType())) {
            onPurchaseInvoiceTransaction(event, true);
        } else if (EVENT_GOODS_DELIVIRY_NOTE_TRANSACTION.equals(event.getEventType())) {
            onGoodsDeliviryNotesTransactions(event, false);
        } else if (EVENT_REMOVE_GOODS_DELIVIRY_NOTE_TRANSACTION.equals(event.getEventType())) {
            onGoodsDeliviryNotesTransactions(event, true);
        } else if (EVENT_GOODS_RECEIVED_NOTE_TRANSACTION.equals(event.getEventType())) {
            onGoodsReceivedNotesTransactions(event, false);
        } else if (EVENT_REMOVE_GOODS_RECEIVED_NOTE_TRANSACTION.equals(event.getEventType())) {
            onGoodsReceivedNotesTransactions(event, true);
        } else if (EVENT_CUSTOMER_CREDIT_NOTE_TRANSACTION.equals(event.getEventType())) {
            onCustomerCreditNoteTransaction(event, false);
        } else if (EVENT_REMOVE_CUSTOMER_CREDIT_NOTE_TRANSACTION.equals(event.getEventType())) {
            onCustomerCreditNoteTransaction(event, true);
        } else if (EVENT_SUPPLIER_CREDIT_NOTE_TRANSACTION.equals(event.getEventType())) {
            onSupplierCreditNoteTransaction(event, false);
        } else if (EVENT_REMOVE_SUPPLIER_CREDIT_NOTE_TRANSACTION.equals(event.getEventType())) {
            onSupplierCreditNoteTransaction(event, true);
        } else if (EVENT_STOCK_ADJUSTMENT_TRANSACTION.equals(event.getEventType())) {
            onStockAdjustmentTransaction(event, false);
        } else if (EVENT_REMOVE_STOCK_ADJUSTMENT_TRANSACTION.equals(event.getEventType())) {
            onStockAdjustmentTransaction(event, true);
        } else if (EVENT_STOCK_TRANSFER_TRANSACTION.equals(event.getEventType())) {
            onStockTransferTransaction(event, false);
        } else if (EVENT_REMOVE_STOCK_TRANSFER_TRANSACTION.equals(event.getEventType())) {
            onStockTransferTransaction(event, true);
        } else if (EVENT_STOCK_TRANSFER_IN_TRANSACTION.equals(event.getEventType())) {
            onStockTransferInTransaction(event);
        } else if (EVENT_CORRECTION_ASSEMBLY_BUILD_TRANSACTION.equals(event.getEventType())) {
            eventCorrectionAssemblyBuildTransaction(event);
        }
        event.setStatus(EventStatus.COMPLETED.name());
    }

    @Transactional
    public void onSaleInvoiceTransaction(EdsBusinessEvent event, boolean removing) {
        EdsSaleInvoice invoice = (EdsSaleInvoice) invoiceManager.get(event.getEntityID());

        if (!CollectionUtils.isEmpty(invoice.getConvertedShippingData())) {
            return;
        }
        EdsTransaction transaction = transactionManager.getTransactionByInvoice(invoice, removing);

        FIFODataMQ fifoDataMQ = new FIFODataMQ();
        fifoDataMQ.setEntityId(invoice.getObjectID());
        fifoDataMQ.setTransactionId(transaction.getObjectID());
        fifoDataMQ.setEntityType(EntityType.SALES_INVOICE);

        List<FIFOItemMQ> itemList = new ArrayList<>();
        for (EdsBaseInvoiceItem invoiceItem : invoice.getInvoiceItems()) {
            HashBasedTable<Integer, Integer, BigDecimal> itemsMap = HashBasedTable.create();
            Integer warehouseId = invoiceItem.getWarehouse() != null ? invoiceItem.getWarehouse().getObjectID() : warehouseManager.getDefaultWarehouse().getObjectID();
            COGSService.mapRequestedItems(invoiceItem.getItem(), invoiceItem.getQty(), warehouseId, itemsMap);

            for (Table.Cell<Integer, Integer, BigDecimal> cell : itemsMap.cellSet()) {
                FIFOItemMQ fifoItem;
                if (removing) {
                    fifoItem = new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey());
                } else {
                    fifoItem = new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey(), invoiceItem.getObjectID());
                }
                itemList.add(fifoItem);
            }
        }
        if (!itemList.isEmpty()) {
            fifoDataMQ.setFifoItems(itemList);
            fifoDataMQ.setRemoving(removing);
//            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_SEND_BY_KAFKA)) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        sendEvent(fifoDataMQ, invoice.getNumber());
                    }
                });
//            } else {
//                registerOutEvent(fifoDataMQ, invoice.getNumber());
//            }
        }
    }

    @Transactional
    public void onGoodsDeliviryNotesTransactions(EdsBusinessEvent event, boolean removing) {
        EdsShippingData goodsDelivered = shippingDataManager.get(event.getEntityID());
        EdsTransaction transaction = transactionManager.getGoodsDeliverdTransactionsByShippingData(goodsDelivered, removing).get(0);

        FIFODataMQ fifoDataMQ = new FIFODataMQ();
        fifoDataMQ.setEntityId(goodsDelivered.getObjectID());
        fifoDataMQ.setTransactionId(transaction.getObjectID());
        fifoDataMQ.setEntityType(EntityType.GDN);

        List<FIFOItemMQ> itemList = new ArrayList<>();
        for (EdsShippingDataItem item : goodsDelivered.getItems()) {
            if (item.getReceivedQty() == null || item.getReceivedQty().compareTo(BigDecimal.ZERO) <= 0 || item.getQuoteItemId() == null) {
                continue;
            }
            HashBasedTable<Integer, Integer, BigDecimal> itemsMap = HashBasedTable.create();
            Integer warehouseId = item.getWarehouse() != null ? item.getWarehouse().getObjectID() : warehouseManager.getDefaultWarehouse().getObjectID();
            COGSService.mapRequestedItems(item.getQuoteItem().getItem(), item.getReceivedQty(), warehouseId, itemsMap);

            for (Table.Cell<Integer, Integer, BigDecimal> cell : itemsMap.cellSet()) {
                FIFOItemMQ fifoItem;
                if (removing) {
                    fifoItem = new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey());
                } else {
                    fifoItem = new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey(), item.getObjectID());
                }
                itemList.add(fifoItem);
            }
        }
        if (!itemList.isEmpty()) {
            fifoDataMQ.setFifoItems(itemList);
            fifoDataMQ.setRemoving(removing);
//            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_SEND_BY_KAFKA)) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        sendEvent(fifoDataMQ, goodsDelivered.getNumber());
                    }
                });
//            } else {
//                registerOutEvent(fifoDataMQ, goodsDelivered.getNumber());
//            }
        }
    }

    @Transactional
    public void onPurchaseInvoiceTransaction(EdsBusinessEvent event, boolean removing) {
        EdsInvoice invoice = invoiceManager.get(event.getEntityID());

        if (!CollectionUtils.isEmpty(invoice.getConvertedShippingData())) {
            return;
        }
        HashBasedTable<Integer, Integer, BigDecimal> itemsMap = HashBasedTable.create();
        EdsTransaction transaction = transactionManager.getTransactionByInvoice(invoice, removing);

        FIFODataMQ fifoDataMQ = new FIFODataMQ();
        fifoDataMQ.setEntityId(invoice.getObjectID());
        fifoDataMQ.setTransactionId(transaction.getObjectID());
        fifoDataMQ.setEntityType(EntityType.PURCHASE_INVOICE);

        List<FIFOItemMQ> itemList = new ArrayList<>();
        for (EdsInvoiceItem invoiceItem : invoice.getInvoiceItems()) {
            Integer warehouseId = invoiceItem.getWarehouse() != null ? invoiceItem.getWarehouse().getObjectID() : warehouseManager.getDefaultWarehouse().getObjectID();
            COGSService.mapRequestedItems(invoiceItem.getItem(), invoiceItem.getQty(), warehouseId, itemsMap);

            for (Table.Cell<Integer, Integer, BigDecimal> cell : itemsMap.cellSet()) {
                itemList.add(new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey()));
            }
        }

        if (!itemList.isEmpty()) {
            fifoDataMQ.setFifoItems(itemList);
            fifoDataMQ.setRemoving(removing);
//            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_SEND_BY_KAFKA)) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        sendEvent(fifoDataMQ, invoice.getNumber());
                    }
                });
//            } else {
//                registerInEvent(fifoDataMQ, invoice.getNumber());
//            }
        }
    }

    @Transactional
    public void onGoodsReceivedNotesTransactions(EdsBusinessEvent event, boolean removing) {
        EdsShippingData goodsDelivered = shippingDataManager.get(event.getEntityID());
        EdsTransaction transaction = transactionManager.getTransactionsByShippingData(goodsDelivered, removing)
                .stream().filter(t -> t.getExpenseAllocation() == null || !t.getExpenseAllocation().booleanValue())
                .findFirst().get();

        HashBasedTable<Integer, Integer, BigDecimal> itemsMap = HashBasedTable.create();

        FIFODataMQ fifoDataMQ = new FIFODataMQ();
        fifoDataMQ.setEntityId(goodsDelivered.getObjectID());
        fifoDataMQ.setTransactionId(transaction.getObjectID());
        fifoDataMQ.setEntityType(EntityType.GRN);

        List<FIFOItemMQ> itemList = new ArrayList<>();
        for (EdsShippingDataItem item : goodsDelivered.getItems()) {
            if (item.getReceivedQty() == null || item.getReceivedQty().compareTo(BigDecimal.ZERO) <= 0 || item.getQuoteItemId() == null) {
                continue;
            }
            Integer warehouseId = item.getWarehouse() != null ? item.getWarehouse().getObjectID() : warehouseManager.getDefaultWarehouse().getObjectID();
            COGSService.mapRequestedItems(item.getQuoteItem().getItem(), item.getReceivedQty(), warehouseId, itemsMap);

            for (Table.Cell<Integer, Integer, BigDecimal> cell : itemsMap.cellSet()) {
                itemList.add(new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey()));
            }
        }
        if (!itemList.isEmpty()) {
            fifoDataMQ.setFifoItems(itemList);
            fifoDataMQ.setRemoving(removing);
//            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_SEND_BY_KAFKA)) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        sendEvent(fifoDataMQ, goodsDelivered.getNumber());
                    }
                });
//            } else {
//                registerInEvent(fifoDataMQ, goodsDelivered.getNumber());
//            }
        }
    }

    @Transactional
    public void onCustomerCreditNoteTransaction(EdsBusinessEvent event, boolean removing) {
        EdsInvoice invoice = invoiceManager.get(event.getEntityID());
        HashBasedTable<Integer, Integer, BigDecimal> itemsMap = HashBasedTable.create();
        EdsTransaction transaction = transactionManager.getTransactionByInvoice(invoice, removing);

        FIFODataMQ fifoDataMQ = new FIFODataMQ();
        fifoDataMQ.setEntityId(invoice.getObjectID());
        fifoDataMQ.setTransactionId(transaction.getObjectID());
        fifoDataMQ.setEntityType(EntityType.CUSTOMER_CREDIT_NOTE);

        List<FIFOItemMQ> itemList = new ArrayList<>();
        for (EdsInvoiceItem invoiceItem : invoice.getInvoiceItems()) {
            Integer warehouseId = invoiceItem.getWarehouse() != null ? invoiceItem.getWarehouse().getObjectID() : warehouseManager.getDefaultWarehouse().getObjectID();
            COGSService.mapRequestedItems(invoiceItem.getItem(), invoiceItem.getQty(), warehouseId, itemsMap);

            for (Table.Cell<Integer, Integer, BigDecimal> cell : itemsMap.cellSet()) {
                itemList.add(new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey()));
            }
        }

        if (!itemList.isEmpty()) {
            fifoDataMQ.setFifoItems(itemList);
            fifoDataMQ.setRemoving(removing);
//            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_SEND_BY_KAFKA)) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        sendEvent(fifoDataMQ, invoice.getNumber());
                    }
                });
//            } else {
//                registerInEvent(fifoDataMQ, invoice.getNumber());
//            }
        }
    }

    @Transactional
    public void onSupplierCreditNoteTransaction(EdsBusinessEvent event, boolean removing) {
        EdsInvoice invoice = invoiceManager.get(event.getEntityID());
        EdsTransaction transaction = transactionManager.getTransactionByInvoice(invoice, removing);

        FIFODataMQ fifoDataMQ = new FIFODataMQ();
        fifoDataMQ.setEntityId(invoice.getObjectID());
        fifoDataMQ.setTransactionId(transaction.getObjectID());
        fifoDataMQ.setEntityType(EntityType.SUPPLIER_CREDIT_NOTE);

        List<FIFOItemMQ> itemList = new ArrayList<>();
        for (EdsInvoiceItem invoiceItem : invoice.getInvoiceItems()) {
            HashBasedTable<Integer, Integer, BigDecimal> itemsMap = HashBasedTable.create();
            Integer warehouseId = invoiceItem.getWarehouse() != null ? invoiceItem.getWarehouse().getObjectID() : warehouseManager.getDefaultWarehouse().getObjectID();
            COGSService.mapRequestedItems(invoiceItem.getItem(), invoiceItem.getQty(), warehouseId, itemsMap);

            for (Table.Cell<Integer, Integer, BigDecimal> cell : itemsMap.cellSet()) {
                FIFOItemMQ fifoItem;
                if (removing) {
                    fifoItem = new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey());
                } else {
                    fifoItem = new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey(), invoiceItem.getObjectID());
                }
                itemList.add(fifoItem);
            }

            if (!itemList.isEmpty()) {
                fifoDataMQ.setFifoItems(itemList);
                fifoDataMQ.setRemoving(removing);
//                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_SEND_BY_KAFKA)) {
                    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            sendEvent(fifoDataMQ, invoice.getNumber());
                        }
                    });
//                } else {
//                    registerOutEvent(fifoDataMQ, invoice.getNumber());
//                }
            }
        }
    }

    @Transactional
    public void onStockAdjustmentTransaction(EdsBusinessEvent event, boolean removing) {
        EdsStockAdjustment stockAdjustment = stockAdjustmentManager.get(event.getEntityID());
        EdsTransaction transaction = transactionManager.getTransactionByStockAdjustment(stockAdjustment);

        List<FIFOItemMQ> inItemList = new ArrayList<>();
        List<FIFOItemMQ> outItemList = new ArrayList<>();
        for (EdsAdjustmentItem adjustmentItem : stockAdjustment.getAdjustmentItemList()) {

            /**
             * Out transaction
             */
            if (adjustmentItem.getUsedQty() != null && adjustmentItem.getUsedQty().compareTo(BigDecimal.ZERO) > 0) {
                HashBasedTable<Integer, Integer, BigDecimal> itemsMap = HashBasedTable.create();
                Integer warehouseId = adjustmentItem.getWarehouse() != null ? adjustmentItem.getWarehouse().getObjectID() : warehouseManager.getDefaultWarehouse().getObjectID();
                COGSService.mapRequestedItems(adjustmentItem.getItem(), adjustmentItem.getUsedQty(), warehouseId, itemsMap);

                for (Table.Cell<Integer, Integer, BigDecimal> cell : itemsMap.cellSet()) {
                    FIFOItemMQ fifoItem;
                    if (removing) {
                        fifoItem = new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey());
                    } else {
                        fifoItem = new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey(), adjustmentItem.getObjectID());
                    }
                    outItemList.add(fifoItem);
                }
            }

            /**
             * In transaction
             */
            if (adjustmentItem.getNewQty() != null && adjustmentItem.getNewQty().compareTo(BigDecimal.ZERO) > 0) {
                HashBasedTable<Integer, Integer, BigDecimal> itemsMap = HashBasedTable.create();
                Integer warehouseId = adjustmentItem.getWarehouse() != null ? adjustmentItem.getWarehouse().getObjectID() : warehouseManager.getDefaultWarehouse().getObjectID();
                COGSService.mapRequestedItems(adjustmentItem.getItem(), adjustmentItem.getNewQty(), warehouseId, itemsMap);

                for (Table.Cell<Integer, Integer, BigDecimal> cell : itemsMap.cellSet()) {
                    inItemList.add(new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey()));
                }
            }
        }

        if (!outItemList.isEmpty()) {
            FIFODataMQ fifoDataMQ = new FIFODataMQ();
            fifoDataMQ.setEntityId(stockAdjustment.getObjectID());
            fifoDataMQ.setTransactionId(transaction.getObjectID());
            fifoDataMQ.setEntityType(EntityType.STOCK_ADJUSTMENT_OUT);
            fifoDataMQ.setFifoItems(outItemList);
//            fifoDataMQ.setRemoving(removing);
//            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_SEND_BY_KAFKA)) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        sendEvent(fifoDataMQ, stockAdjustment.getNumber() + "_OUT");
                    }
                });
//            } else {
//                registerOutEvent(fifoDataMQ, stockAdjustment.getNumber() + "_OUT");
//            }
        }
        if (!inItemList.isEmpty()) {
            FIFODataMQ fifoDataMQ = new FIFODataMQ();
            fifoDataMQ.setEntityId(stockAdjustment.getObjectID());
            fifoDataMQ.setTransactionId(transaction.getObjectID());
            fifoDataMQ.setEntityType(EntityType.STOCK_ADJUSTMENT_IN);
            fifoDataMQ.setFifoItems(inItemList);
            fifoDataMQ.setRemoving(removing);
//            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_SEND_BY_KAFKA)) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        sendEvent(fifoDataMQ, stockAdjustment.getNumber() + "_IN");
                    }
                });
//            } else {
//                registerInEvent(fifoDataMQ, stockAdjustment.getNumber() + "_IN");
//            }
        }
    }

    @Transactional
    public void onStockTransferTransaction(EdsBusinessEvent event, boolean removing) {
        EdsStockTransfer stockTransfer = stockTransferManager.get(event.getEntityID());
        EdsTransaction transaction = transactionManager.getTransactionByStockTransfer(stockTransfer);
        List<EdsStockAdjustment> stockAdjustments = removing ? stockAdjustmentManager.getStockAdjustmentsByStockTransfer(stockTransfer.getObjectID(), true) : stockTransfer.getItems();

        List<FIFOItemMQ> inItemList = new ArrayList<>();
        List<FIFOItemMQ> outItemList = new ArrayList<>();
        for (EdsStockAdjustment stockAdjustment : stockAdjustments) {
            /**
             * Out transaction
             */
            {
                EdsAdjustmentItem fromWarehouse = stockAdjustment.getAdjustmentItemList().get(0);
                HashBasedTable<Integer, Integer, BigDecimal> itemsMap = HashBasedTable.create();
                Integer warehouseId = fromWarehouse.getWarehouse() != null ? fromWarehouse.getWarehouse().getObjectID() : warehouseManager.getDefaultWarehouse().getObjectID();
                COGSService.mapRequestedItems(fromWarehouse.getItem(), fromWarehouse.getUsedQty(), warehouseId, itemsMap);

                if (!removing) {
                    for (Table.Cell<Integer, Integer, BigDecimal> cell : itemsMap.cellSet()) {
                        List<FIFODataMQ> outStocksAfterTransaction = stockCalcManager.getOutStockListAfterTransaction(cell.getRowKey(), cell.getColumnKey(), transaction, 0, 1);
                        outStocksAfterTransaction.forEach(fi -> {
//                            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_SEND_BY_KAFKA)) {
                                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                                    @Override
                                    public void afterCommit() {
                                        sendEvent(fi, stockAdjustment.getNumber() + "_OUT");
                                    }
                                });
//                            } else {
//                                registerOutEvent(fi, stockAdjustment.getNumber() + "_OUT");
//                            }
                        });
                    }
                } else {
                    for (Table.Cell<Integer, Integer, BigDecimal> cell : itemsMap.cellSet()) {
                        FIFOItemMQ fifoItem;
                        if (removing) {
                            fifoItem = new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey());
                        } else {
                            fifoItem = new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey(), stockTransfer.getObjectID());
                        }
                        outItemList.add(fifoItem);
                    }
                }
            }

            /**
             * In transaction
             */
            {
                EdsAdjustmentItem toWarehouse = stockAdjustment.getAdjustmentItemList().get(1);
                HashBasedTable<Integer, Integer, BigDecimal> itemsMap = HashBasedTable.create();
                Integer warehouseId = toWarehouse.getWarehouse() != null ? toWarehouse.getWarehouse().getObjectID() : warehouseManager.getDefaultWarehouse().getObjectID();
                COGSService.mapRequestedItems(toWarehouse.getItem(), toWarehouse.getNewQty(), warehouseId, itemsMap);

                for (Table.Cell<Integer, Integer, BigDecimal> cell : itemsMap.cellSet()) {
                    inItemList.add(new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey()));
                }
            }
        }

        if (!outItemList.isEmpty()) {
            FIFODataMQ fifoDataMQ = new FIFODataMQ();
            fifoDataMQ.setEntityId(stockTransfer.getObjectID());
            fifoDataMQ.setTransactionId(transaction.getObjectID());
            fifoDataMQ.setEntityType(EntityType.STOCK_TRANSFER_OUT);
            fifoDataMQ.setFifoItems(outItemList);
            fifoDataMQ.setRemoving(removing);
//            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_SEND_BY_KAFKA)) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        sendEvent(fifoDataMQ, stockTransfer.getNumber() + "_OUT");
                    }
                });
//            } else {
//                registerOutEvent(fifoDataMQ, stockTransfer.getNumber() + "_OUT");
//            }
        }

        if (!inItemList.isEmpty()) {
            FIFODataMQ fifoDataMQ = new FIFODataMQ();
            fifoDataMQ.setEntityId(stockTransfer.getObjectID());
            fifoDataMQ.setTransactionId(transaction.getObjectID());
            fifoDataMQ.setEntityType(EntityType.STOCK_TRANSFER_IN);
            fifoDataMQ.setFifoItems(inItemList);
            fifoDataMQ.setRemoving(removing);
//            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_SEND_BY_KAFKA)) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        sendEvent(fifoDataMQ, stockTransfer.getNumber() + "_IN");
                    }
                });
//            } else {
//                registerInEvent(fifoDataMQ, stockTransfer.getNumber() + "_IN");
//            }
        }
    }

    @Transactional
    public void onStockTransferInTransaction(EdsBusinessEvent event) {
        EdsAdjustmentItem toWarehouse = stockAdjustmentItemManager.get(event.getEntityID());
        EdsTransaction transaction = transactionManager.getTransactionByStockTransfer(toWarehouse.getAdjustment().getStockTrans());

        List<FIFOItemMQ> inItemList = new ArrayList<>();
        HashBasedTable<Integer, Integer, BigDecimal> itemsMap = HashBasedTable.create();
        Integer warehouseId = toWarehouse.getWarehouse() != null ? toWarehouse.getWarehouse().getObjectID() : warehouseManager.getDefaultWarehouse().getObjectID();
        COGSService.mapRequestedItems(toWarehouse.getItem(), toWarehouse.getNewQty(), warehouseId, itemsMap);

        FIFODataMQ fifoDataMQ = new FIFODataMQ();
        fifoDataMQ.setEntityId(toWarehouse.getObjectID());
        fifoDataMQ.setTransactionId(transaction.getObjectID());
        fifoDataMQ.setEntityType(EntityType.STOCK_TRANSFER_IN);

        for (Table.Cell<Integer, Integer, BigDecimal> cell : itemsMap.cellSet()) {
            inItemList.add(new FIFOItemMQ(cell.getRowKey(), cell.getValue(), cell.getColumnKey()));
        }
        if (!inItemList.isEmpty()) {
            fifoDataMQ.setFifoItems(inItemList);
            fifoDataMQ.setRemoving(false);
//            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_SEND_BY_KAFKA)) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        sendEvent(fifoDataMQ, toWarehouse.getAdjustment().getNumber());
                    }
                });
//            } else {
//                registerInEvent(fifoDataMQ, toWarehouse.getAdjustment().getNumber());
//            }
        }
    }

    private void eventCorrectionAssemblyBuildTransaction(EdsBusinessEvent event) {
        String synchronizedKey = ServerSecurityContext.getInstance().getCompanyId() + "_" + event.getEntityID();
        stringXSync.execute(synchronizedKey, () -> accountingService.eventCorrectionAssemblyBuildTransaction(event.getEntityID()));
    }

    private void registerOutEvent(FIFODataMQ data, String key) {
        boolean sent = false;
        int attempts = 0;
        String failMessage = null;
        while (!sent && attempts < 3) {
            try {
                data.setCompanyId(Integer.valueOf(SecurityContext.getInstance().getCompanyId()));
                if (data.isRemoving()) {
                    rabbitMQService.sendToQueue(data, InOutQueue.BATCH_OUT_DELETE_QUEUE, QueueListener.BATCH_OUT_DELETE_LISTENER);
                } else {
                    rabbitMQService.sendToQueue(data, InOutQueue.BATCH_OUT_QUEUE, QueueListener.BATCH_OUT_LISTENER);
                }
                sent = true;
                log.info("TransactionCustomEventListenerImpl - registerInvoiceOutMQ - sent to queue: {}", data.isRemoving() ? InOutQueue.BATCH_OUT_DELETE_QUEUE : InOutQueue.BATCH_OUT_QUEUE);
            } catch (Exception e) {
                attempts++;
                try {
                    Thread.sleep(500L * attempts);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    log.error("Thread interrupted during retry", ex);
                    failMessage = ex.getMessage();
                    break;
                }
            }
        }

        if (!sent) {
            log.error("FILED TO SEND FIFO ITEM AFTER {} ATTEMPTS: {}", 3, data);
            handleFailure(data, key, failMessage);
        }
    }

    private void registerInEvent(FIFODataMQ data, String key) {
        boolean sent = false;
        int attempts = 0;
        String failMessage = null;
        while (!sent && attempts < 3) {
            try {
                data.setCompanyId(Integer.valueOf(SecurityContext.getInstance().getCompanyId()));
                if (data.isRemoving()) {
                    rabbitMQService.sendToQueue(data, InOutQueue.BATCH_IN_DELETE_QUEUE, QueueListener.BATCH_IN_DELETE_LISTENER);
                } else {
                    rabbitMQService.sendToQueue(data, InOutQueue.BATCH_IN_QUEUE, QueueListener.BATCH_IN_LISTENER);
                }
                sent = true;
                log.info("TransactionCustomEventListenerImpl - registerInvoiceInMQ - sent to queue: {}", data.isRemoving() ? InOutQueue.BATCH_IN_DELETE_QUEUE : InOutQueue.BATCH_IN_QUEUE);
            } catch (Exception e) {
                attempts++;
                try {
                    Thread.sleep(500L * attempts);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    log.error("Thread interrupted during retry", ex);
                    failMessage = ex.getMessage();
                    break;
                }
            }
        }

        if (!sent) {
            log.error("FILED TO SEND FIFO ITEM AFTER {} ATTEMPTS: {}", 3, data);
            handleFailure(data, key, failMessage);
        }
    }

    private void sendEvent(FIFODataMQ fifoDataMQ, String key) {
        fifoDataMQ.setCompanyId(Integer.valueOf(SecurityContext.getInstance().getCompanyId()));
        String uniqueKey = getEncodedKey(key);

        try {
            kafkaEventProducer.sendMessage(fifoDataMQ, uniqueKey).get();
        } catch (Exception e) {
            log.error("====================== FAILED TO SEND THE EVENT ======================", e);
            handleFailure(fifoDataMQ, uniqueKey, e.getMessage());
        }
    }

    private String getEncodedKey(String key) {
        return SecurityContext.getCompanyID() + "_" + key;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleFailure(FIFODataMQ fifoDataMQ, String key, String failMessage) {
        fifoDataMQ.setTarget(FailTarget.SENDING);
        fifoFailureService.trackFailur(fifoDataMQ, key, failMessage);
    }
}
