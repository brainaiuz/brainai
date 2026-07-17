package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.core.domain.accounting.EdsStockTransferTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.core.solr.component.PurchaseInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.ShippingDataSolrComponent;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.costofgoods.COGSServices;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataStatus;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.StockCalcManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFODataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FifoItem;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFOItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EntityType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.FailTarget;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.FifoFailureService;
import com.edatasite.workforce.gwt.core.server.utils.EventHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public abstract class FifoListener extends BaseAmqpListener<FIFODataMQ> {

    protected static final Logger log = LoggerFactory.getLogger(FifoListener.class);
    @Autowired
    protected COGSServices cogsServices;
    @Autowired
    protected StockCalcManager stockCalcManager;
    @Autowired
    protected TransactionManager transactionManager;
    @Autowired
    protected ItemManager itemManager;
    @Autowired
    protected StockAdjustmentItemManager stockAdjustmentItemManager;
    @Autowired
    protected AccountingServiceLocal accountingServiceLocal;
    @Autowired
    protected XSync<String> stringXSync;
    @Autowired
    protected GenericSettingsManager genericSettingsManager;
    @Autowired
    protected InvoiceItemManager invoiceItemManager;
    @Autowired
    protected InvoiceManager invoiceManager;
    @Autowired
    protected ShippingDataManager shippingDataManager;
    @Autowired
    protected SolrManager solrManager;
    @Autowired
    protected SaleInvoiceSolrComponent invoiceSolrComponent;
    @Autowired
    protected PurchaseInvoiceSolrComponent purchaseInvoiceSolrComponent;
    @Autowired
    protected ShippingDataSolrComponent shippingDataSolrComponent;
    @Autowired
    protected ReferenceManager referenceManager;
    @Autowired
    private StockAdjustmentManager stockAdjustmentManager;
    @Autowired
    private StockTransferManager stockTransferManager;
    @Autowired
    private FifoFailureService fifoFailureService;

    protected static final int PAGE_SIZE = 50;

    @Override
    protected DataMQ<FIFODataMQ> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<FIFODataMQ>>() {
        }.getType());
    }

    @Override
    protected void receiveMessage(FIFODataMQ fifoItem) {
        stringXSync.execute(getSynchronizedKey(fifoItem), () -> {
            for (FIFOItemMQ item : fifoItem.getFifoItems()) {
                boolean success = false;
                int attempts = 0;
                while (!success && attempts < 3) {

                    try {
                        log.info("============================FIFO Rabbit Consumer: {}", this.getTransactionType());
                        log.info("COMPANY_ID: {}, TRANSACTION_ID: {}, INVOICE_ID: {}", ServerSecurityContext.getInstance().getCompanyId(), fifoItem.getTransactionId(), fifoItem.getEntityId());

                        EdsTransaction transaction = transactionManager.get(fifoItem.getTransactionId());
                        if (isValid(item, transaction)) {
                            doAction(item, transaction);
                            success = true;
                        } else {
                            attempts++;
                            item.setHasError(true);
                        }
                    } catch (Exception e) {
                        attempts++;
                        log.warn("EXCEPTION ITEM: {}", fifoItem);
                        log.info("FIFO EXCEPTION: {}", e.getMessage());
                        item.setHasError(true);
                        try {
                            Thread.sleep((long) Math.pow(2, attempts) * 1000);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }
            if (fifoItem.isRemoving()) {
                fifoFailureService.deleteFailure(fifoItem);
            } else {
                if (isValid(fifoItem)) {
                    updateStatus(fifoItem.getEntityId(), fifoItem.getEntityType(), Constants.APPROVE);
                    fifoFailureService.deleteFailure(fifoItem);
                } else {
                    updateStatus(fifoItem.getEntityId(), fifoItem.getEntityType(), Constants.FAILED);
                    handleFailure(fifoItem, new Exception("Not Valid. Check for Transaction or Product"));
                }
            }
        });
    }

    private boolean isValid(FIFODataMQ fifoItem) {
        for (FIFOItemMQ item : fifoItem.getFifoItems()) {
            if (Boolean.TRUE.equals(item.isHasError())) {
                return false;
            }
        }
        return true;
    }

    private void handleFailure(FIFODataMQ item, Exception e) {
        log.error("Some items permanently failed after 3 retries");
        item.setTarget(FailTarget.RECEIVING);
        fifoFailureService.trackFailur(item, item.getCompanyId() + "_" + item.getEntityId() + "_" + item.getTransactionId(), e.getMessage());
    }

    private void updateStatus(Integer entityId, EntityType entityType, String status) {
        switch (entityType) {
            case SALES_INVOICE, PURCHASE_INVOICE -> updateInvoiceStatus(status, entityId, entityType);
            case GRN, GDN ->
                    updateNoteStatus(Constants.APPROVE.equals(status) ? "SUCCESSFUL" : status, entityId, entityType);
            case STOCK_ADJUSTMENT_IN, STOCK_ADJUSTMENT_OUT ->
                    updateStockAdjustmentStatus(entityId, Constants.APPROVE.equals(status) ? Constants.STOCK_ADJUSTMENT_APPROVED : status, entityType);
            case STOCK_TRANSFER_IN, STOCK_TRANSFER_OUT ->
                    updateStockTransferStatus(entityId, Constants.APPROVE.equals(status) ? Constants.STOCK_TRANSFER_APPROVED : status, entityType);
        }
    }

    private void indexInvoice(EdsInvoice invoice) {
        try {
            if (invoice.getType().equals("RECEIVABLE")) {
                invoiceSolrComponent.index(invoiceManager.getSaleInvoice(invoice.getObjectID()));
            } else if (invoice.getType().equals("PAYABLE")) {
                purchaseInvoiceSolrComponent.index(invoiceManager.getPurchaseInvoice(invoice.getObjectID()));
            }
            log.info(" ============================== Invoice is added to the solr after consuming FIFO items: ================================================== ");
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private EdsInvoice updateInvoiceStatus(String status, Integer invoiceId, EntityType entityType) {
        EdsInvoice invoice = invoiceManager.get(invoiceId);
        EdsReference reference = referenceManager.findReference(Constants.INVOICE_STATUS, status);
        invoice.setEntityStatus(reference);
        invoiceManager.update(invoice);
        indexInvoice(invoice);
        String message = "INVOICE WITH NUMBER: " + invoice.getNumber() + " IS " + status;
        if (EntityType.SALES_INVOICE.equals(entityType)) {
            EventHandler.fireEvent(WfmUiEventType.ON_SALES_INVOICE_APPROVAL, message);
        } else if (EntityType.PURCHASE_INVOICE.equals(entityType)) {
            EventHandler.fireEvent(WfmUiEventType.ON_PURCHASE_INVOICE_APPROVAL, message);
        }
        EventHandler.fireEvent(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, message);
        return invoice;
    }

    private void updateNoteStatus(String status, Integer noteId, EntityType entityType) {
        EdsShippingData edsShippingData = shippingDataManager.get(noteId);
        edsShippingData.setStatus(ShippingDataStatus.valueOf(status));
        shippingDataManager.update(edsShippingData);

        try {
            shippingDataSolrComponent.index(edsShippingData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String message = entityType.name() + " WITH NUMBER: " + edsShippingData.getNumber() + " IS " + status;
        EventHandler.fireEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, message);
    }

    private void updateStockAdjustmentStatus(Integer objectId, String status, EntityType entityType) {
        EdsStockAdjustment stockAdjustment = stockAdjustmentManager.get(objectId);
        EdsReference reference = referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, status);
        stockAdjustment.setEntityStatus(reference);
        stockAdjustmentManager.update(stockAdjustment);

        String message = entityType.name() + " WITH NUMBER: " + stockAdjustment.getNumber() + " IS " + status;
        EventHandler.fireEvent(WfmUiEventType.ON_STOCK_ADJUSTMENT_SAVED, message);
    }

    private void updateStockTransferStatus(Integer objectId, String status, EntityType entityType) {
        log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        log.info("UPDATING THE STOCK: \n");
        log.info("EXPECTED STOCK TRANSFER ID IS: {}, AND STATUS IS: {} , THE COMPANY ID IS: {} \n", objectId, status, ServerSecurityContext.getInstance().getCompanyId());
        EdsStockTransfer stockTransfer = stockTransferManager.get(objectId);
        log.info("STOCK TRANSFER WITH ID IS: {} \n", stockTransfer != null ? "FOUND" : "NOT FOUND");
        log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        EdsReference reference = referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, status);
        stockTransfer.setEntityStatus(reference);
        stockTransferManager.update(stockTransfer);

        String message = entityType.name() + " WITH NUMBER: " + stockTransfer.getNumber() + " IS " + status;
        EventHandler.fireEvent(WfmUiEventType.ON_STOCK_TRANSFER_SAVED, message);
    }

    protected abstract void doAction(FIFOItemMQ fifoItem, EdsTransaction transaction);

    protected abstract String getTransactionType();

    protected void reCreateGocsTransactions(List<FifoItem> outStocks, EdsTransaction transaction) {
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DISABLE_FIFO)) {
            return;
        }
        if (!CollectionUtils.isEmpty(outStocks)) {
            for (FifoItem item : outStocks) {
                BigDecimal COGS = cogsServices.getService().createCogsTransaction(item);

                if (transaction instanceof EdsStockTransferTransaction) {
                    correctTransferInStock(item, (EdsStockTransferTransaction) transaction, COGS);
                }
            }
        }
    }

    protected void correctTransferInStock(FifoItem item, EdsStockTransferTransaction transaction, BigDecimal COGS) {
        if (item.getTransactionItemId() == null) {
            ServerSecurityContext.getInstance().setStaticUserID(transaction.getPostedBy() != null ? transaction.getPostedBy().getObjectID() : null);
            accountingServiceLocal.createTransactionForStockTransfer(((EdsStockTransferTransaction) transaction).getStockTransfer());
            ServerSecurityContext.getInstance().setStaticUserID(null);
        } else {
            EdsAdjustmentItem fromItem = stockAdjustmentItemManager.get(item.getTransactionItemId());
            EdsAdjustmentItem toItem = fromItem.getAdjustment().getAdjustmentItemList().get(1);

            EdsItem product = itemManager.get(item.getProductId());
            cogsServices.getService().applyStockTransferToItemStock(product, COGS, toItem.getNewQty(), transaction, toItem.getWarehouse().getObjectID(), toItem.getObjectID(), true);
        }
    }

    protected String getSynchronizedKey(FIFODataMQ fifoItem) {
        String key = ServerSecurityContext.getInstance().getCompanyId() + "_" + fifoItem.getEntityId();

        if (fifoItem.getTransactionId() != null) {
            key += "_" + fifoItem.getTransactionId();
        }
        return key;
    }

    boolean isValid(FIFOItemMQ fifoItem, EdsTransaction transaction) {
        return true;
    }
}