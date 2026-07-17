package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.core.domain.fifo.EdsProcessedEvent;
import com.edatasite.workforce.core.kafka.data.SOLRDataMQ;
import com.edatasite.workforce.core.solr.component.ProductsServicesSolrComponent;
import com.edatasite.workforce.core.solr.component.PurchaseInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.ShippingDataSolrComponent;
import com.edatasite.workforce.gwt.accounting.server.app.costofgoods.COGSServices;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.StockCalcManager;
import com.edatasite.workforce.gwt.core.server.db.fifo.ProcessedEventManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EntityType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.FifoFailureService;
import com.edatasite.workforce.gwt.core.server.utils.EventHandler;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventHandlerService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ItemManager itemManager;
    @Autowired
    private InvoiceItemManager invoiceItemManager;
    @Autowired
    private FifoFailureService fifoFailureService;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private COGSServices cogsServices;
    @Autowired
    private StockCalcManager stockCalcManager;
    @Autowired
    private AccountingServiceLocal accountingService;
    @Autowired
    private StockAdjustmentItemManager stockAdjustmentItemManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private SaleInvoiceSolrComponent invoiceSolrComponent;
    @Autowired
    private PurchaseInvoiceSolrComponent purchaseInvoiceSolrComponent;
    @Autowired
    private ProductsServicesSolrComponent productsSolrComponent;
    @Autowired
    private ShippingDataSolrComponent shippingDataSolrComponent;
    @Autowired
    private ShippingDataManager shippingDataManager;
    @Autowired
    private StockAdjustmentManager stockAdjustmentManager;
    @Autowired
    private StockTransferManager stockTransferManager;
    @Autowired
    protected ProcessedEventManager processedEventRepository;


    @Transactional
    public void reIndexSolrEntity(SOLRDataMQ data) throws Exception {
        var entityType = data.getEntityType();
        var entityId = data.getEntityId();
        var status = data.getStatus();

        switch (entityType) {
            case SALES_INVOICE, PURCHASE_INVOICE -> indexInvoiceData(status, entityId, entityType);
            case GRN, GDN -> indexNoteData(status, entityId, entityType);
            case STOCK_ADJUSTMENT_IN, STOCK_ADJUSTMENT_OUT ->
                    notifyStockAdjustmentStatusUpdate(entityId, status, entityType);
            case STOCK_TRANSFER_IN, STOCK_TRANSFER_OUT -> notifyStockTransferStatusUpdate(entityId, status, entityType);
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public void reIndexSolrProducts(List<Integer> entityIds) throws Exception {
        if (entityIds != null && !entityIds.isEmpty()) {
            List<EdsItem> items = itemManager.get(entityIds);
            try {
                productsSolrComponent.indexes(items);
                log.info(" ============================== Products are added to the solr after consuming FIFO items: ================================================== ");
                EventHandler.fireEvent(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, "Products are indexed after consuming FIFO items");
            } catch (Exception e) {
                throw e;
            }
        }
    }

    @Transactional(readOnly = true)
    public boolean eventExists(String eventId) {
        var event = Optional.ofNullable(processedEventRepository.findByEventID(eventId));
        return event.isPresent();
    }

    @Transactional
    public EdsProcessedEvent generateEventProcessor(String eventId, Integer entityId, EntityType entityType) throws DataIntegrityViolationException {
        EdsProcessedEvent event = new EdsProcessedEvent();
        event.setEventId(eventId);
        event.setEntityId(entityId);
        event.setEntityType(entityType);
        event.setStatus(EventStatus.PENDING);
        event.setCreatedDate(new Date());
        event.setUpdatedDate(new Date());
        processedEventRepository.create(event);
        return event;
    }

    @Transactional
    public EdsProcessedEvent updateEventStatus(Integer eventId, EventStatus status) {
        EdsProcessedEvent event = processedEventRepository.get(eventId);
        event.setStatus(status);
        event.setUpdatedDate(new Date());
        processedEventRepository.update(event);
        return event;
    }

    private EdsInvoice indexInvoiceData(EventStatus status, Integer invoiceId, EntityType entityType) throws Exception {
        EdsInvoice invoice = invoiceManager.get(invoiceId);
        indexInvoice(invoice);

        String message = "INVOICE WITH NUMBER: " + invoice.getNumber() + " IS " + status;
        if (EntityType.SALES_INVOICE.equals(entityType)) {
            EventHandler.fireEvent(WfmUiEventType.ON_SALES_INVOICE_APPROVAL, message);
        } else if (EntityType.PURCHASE_INVOICE.equals(entityType)) {
            EventHandler.fireEvent(WfmUiEventType.ON_PURCHASE_INVOICE_APPROVAL, message);
        }
        EventHandler.fireEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, null);
        return invoice;
    }

    private void indexNoteData(EventStatus status, Integer noteId, EntityType entityType) throws InterruptedException {
        EdsShippingData edsShippingData = shippingDataManager.get(noteId);

        try {
            shippingDataSolrComponent.index(edsShippingData);
        } catch (InterruptedException e) {
            throw e;
        }
        String message = entityType.name() + " WITH NUMBER: " + edsShippingData.getNumber() + " IS " + status;
        EventHandler.fireEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, message);
    }

    private void notifyStockAdjustmentStatusUpdate(Integer objectId, EventStatus status, EntityType entityType) {
        EdsStockAdjustment stockAdjustment = stockAdjustmentManager.get(objectId);

        String message = entityType.name() + " WITH NUMBER: " + stockAdjustment.getNumber() + " IS " + status;
        EventHandler.fireEvent(WfmUiEventType.ON_STOCK_ADJUSTMENT_SAVED, message);
    }

    private void notifyStockTransferStatusUpdate(Integer objectId, EventStatus status, EntityType entityType) {
        EdsStockTransfer stockTransfer = stockTransferManager.get(objectId);

        String message = entityType.name() + " WITH NUMBER: " + stockTransfer.getNumber() + " IS " + status;
        EventHandler.fireEvent(WfmUiEventType.ON_STOCK_TRANSFER_SAVED, message);
    }

    private void indexInvoice(EdsInvoice invoice) throws Exception {
        try {
            if (invoice.getType().equals("RECEIVABLE")) {
                invoiceSolrComponent.index(invoiceManager.getSaleInvoice(invoice.getObjectID()));
            } else if (invoice.getType().equals("PAYABLE")) {
                purchaseInvoiceSolrComponent.index(invoiceManager.getPurchaseInvoice(invoice.getObjectID()));
            }
            log.info(" ============================== Invoice is added to the solr after consuming FIFO items: ================================================== ");

            List<EdsShippingData> grnGdnList = shippingDataManager.getGrnGdnsByInvoiceId(invoice.getObjectID());
            if (grnGdnList.isEmpty()) {
                try {
                    shippingDataSolrComponent.indexes(grnGdnList);
                } catch (InterruptedException e) {
                    throw e;
                }
            }
        } catch (IOException | SolrServerException | InterruptedException e) {
            throw e;
        }
    }
}
