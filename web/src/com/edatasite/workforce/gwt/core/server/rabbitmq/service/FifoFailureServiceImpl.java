package com.edatasite.workforce.gwt.core.server.rabbitmq.service;

import com.edatasite.workforce.core.domain.fifo.EdsFifoFailure;
import com.edatasite.workforce.core.domain.fifo.EdsFifoItem;
import com.edatasite.workforce.core.kafka.data.SOLRDataMQ;
import com.edatasite.workforce.core.kafka.producer.SolrEventProducer;
import com.edatasite.workforce.core.solr.component.PurchaseInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.ShippingDataSolrComponent;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.fifo.FifoFailureManager;
import com.edatasite.workforce.gwt.core.server.db.fifo.FifoManualReviewManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFODataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFOItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("fifoFailureService")
public class FifoFailureServiceImpl implements FifoFailureService {

    private static final int MAX_RETRIES = 5;
    private static final Logger log = LoggerFactory.getLogger(FifoFailureServiceImpl.class);

    @Autowired
    private FifoFailureManager fifoFailureManager;
    @Autowired
    private FifoManualReviewManager fifoManualReviewManager;
    @Autowired
    protected SaleInvoiceSolrComponent invoiceSolrComponent;
    @Autowired
    protected PurchaseInvoiceSolrComponent purchaseInvoiceSolrComponent;
    @Autowired
    protected InvoiceManager invoiceManager;
    @Autowired
    protected ReferenceManager referenceManager;
    @Autowired
    protected ShippingDataManager shippingDataManager;
    @Autowired
    protected ShippingDataSolrComponent shippingDataSolrComponent;
    @Autowired
    private StockAdjustmentManager stockAdjustmentManager;
    @Autowired
    private StockTransferManager stockTransferManager;
    @Autowired
    private SolrEventProducer solrEventProducer;

    @Override
    @Transactional
    public List<FIFODataMQ> getPendingFailures() {
        return fifoFailureManager.getPendingFailures();
    }

    @Override
    @Transactional
    public void deleteFailure(FIFODataMQ fifoItem) {
        if (fifoItem.getObjectId() != null) {
            fifoFailureManager.deleteFifoFailure(fifoItem.getObjectId());
        }
    }

    @Override
    @Transactional
    public void updateFifoFailure(Integer failureId) {
        fifoFailureManager.updateFifoFailure(failureId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void trackFailur(FIFODataMQ item, String key, String failMessage) {
        EdsFifoFailure failure;
        if (item.getObjectId() != null) {
            failure = fifoFailureManager.get(item.getObjectId());
        } else {
            failure = fifoFailureManager.getByEntityId(item.getEntityId(), item.getEntityType(), ServerSecurityContext.getInstance().getCompanyId());
        }

        if (failure == null) {
            failure = new EdsFifoFailure();
            failure.setCompanyId(item.getCompanyId());
            failure.setEntityId(item.getEntityId());
            failure.setTransactionId(item.getTransactionId());
            failure.setRetries(1);
            failure.setRemoving(item.isRemoving());
            failure.setType(item.getEntityType());

            List<EdsFifoItem> itemList = new ArrayList();

            for (FIFOItemMQ fifoItem : item.getFifoItems()) {
                EdsFifoItem fItem = new EdsFifoItem();
                fItem.setItemId(fifoItem.getProductId());
                fItem.setTransactionItemId(fifoItem.getTransactionItemId());
                fItem.setQuantity(fifoItem.getQuantity());
                fItem.setWarehouseId(fifoItem.getWarehouserId());
                fItem.setFailure(failure);
                itemList.add(fItem);
            }
            failure.setItems(itemList);
            failure.setLastAttemptAt(new Date());
            failure.setTarget(item.getTarget());
            failure.setFailReason(failMessage);
        } else {
            failure.setRetries(failure.getRetries() + 1);
            failure.setOnQue(false);
            failure.setLastAttemptAt(new Date());
            failure.setRemoving(item.isRemoving());
            failure.setFailReason(failMessage);

            if (failure.getRetries() >= MAX_RETRIES) {
                failure.setDeleted(true);
                try {
                    ListenableFuture<SendResult<String, String>> result = sendToSolrTopic(item, EventStatus.FAILED, key);
                    result.addCallback(new ListenableFutureCallback<>() {
                        @Override
                        public void onFailure(Throwable ex) {
                            log.error(ex.getMessage(), ex);
                        }

                        @Override
                        public void onSuccess(SendResult<String, String> result) {
                            log.info("Successfully send to solr topic");
                        }
                    });
                } catch (Exception ex) {
                    log.error("FAILED TO SEND EVENT TO THE KAFKA: ", ex);
                }
                log.error("FIFO item permanently failed after {} retries. Moved to manual review: {}", MAX_RETRIES, failure);
            }
        }
        fifoFailureManager.createOrUpdate(failure);
    }

    protected ListenableFuture<SendResult<String, String>> sendToSolrTopic(FIFODataMQ item, EventStatus status, String key) {
        SOLRDataMQ solrDataMQ = new SOLRDataMQ();
        solrDataMQ.setEntityId(item.getEntityId());
        solrDataMQ.setEntityType(item.getEntityType());
        solrDataMQ.setStatus(status);

        return solrEventProducer.sendMessage(solrDataMQ, key);
    }
}
