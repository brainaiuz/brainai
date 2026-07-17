package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.core.domain.accounting.EdsStockTransferTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.core.solr.component.ProductsServicesSolrComponent;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FifoItem;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFOItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class BatchFifoInListener extends FifoListener {

    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private ProductsServicesSolrComponent productsServicesSolrComponent;

    @Override
    protected void doAction(FIFOItemMQ fifoItem, EdsTransaction transaction) {
        long start = System.currentTimeMillis();
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DISABLE_FIFO)) {
            return;
        }
        runOutTransactionsByFIFO(fifoItem, transaction);
        log.info("FIFO IN LISTENER finished in {} ms", (System.currentTimeMillis() - start));
    }

    @Override
    protected String getTransactionType() {
        return "IN TRANSACTION";
    }

    void runOutTransactionsByFIFO(FIFOItemMQ fifoItem, EdsTransaction transaction) {
        BigDecimal availableQty = stockCalcManager.getAvailableStockByDate(fifoItem.getProductId(), fifoItem.getWarehouserId(), transaction);

        if (availableQty.compareTo(BigDecimal.ZERO) < 0) {
            List<FifoItem> outItemsBefore = stockCalcManager.getOutStocksBeforeTransaction(fifoItem.getProductId(), fifoItem.getWarehouserId(), transaction);
            if (outItemsBefore.isEmpty()) {
                System.out.println("==============FIFO IN EXCEPTION: IndexOutOfBoundsException: Index: -1, Size: 0, Cluster: "
                        + ServerSecurityContext.getInstance().getDatabase()
                        + ", CompanyId: " + ServerSecurityContext.getInstance().getCompanyId()
                        + ", TransactionId: " + transaction.getObjectID()
                        + ", productId: " + fifoItem.getProductId());
                return;
            }
            FifoItem reCalcItem = outItemsBefore.get(outItemsBefore.size() - 1);

            for (FifoItem outItem : outItemsBefore) {

                if (outItem.getQuantity().add(availableQty).compareTo(BigDecimal.ZERO) <= 0) {
                    availableQty = availableQty.add(outItem.getQuantity());
                } else {
                    reCalcItem = outItem;
                    break;
                }
            }
            runOutTransaction(reCalcItem);
        } else {
            int start = 0;
            FifoItem reCalcItem = null;
            List<FifoItem> outItemsAfter = null;

            do {
                outItemsAfter = stockCalcManager.getOutStocksAfterTransaction(fifoItem.getProductId(), fifoItem.getWarehouserId(), transaction, start, PAGE_SIZE);
                for (FifoItem outItem : outItemsAfter) {
                    if (availableQty.subtract(outItem.getQuantity()).compareTo(BigDecimal.ZERO) >= 0) {
                        availableQty = availableQty.add(outItem.getQuantity());
                    } else {
                        reCalcItem = outItem;
                        break;
                    }
                }
                start += PAGE_SIZE;
            } while (reCalcItem == null && outItemsAfter.size() == PAGE_SIZE);

            if (reCalcItem != null) {
                runOutTransaction(reCalcItem);
            } else {
                try {
                    productsServicesSolrComponent.index(itemManager.get(fifoItem.getProductId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void runOutTransaction(FifoItem fifoItem) {
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DISABLE_FIFO)) {
            return;
        }
        if (fifoItem.getTransactionItemId() == null) {
            EdsTransaction trx = transactionManager.get(fifoItem.getTransactionId());

            if (trx instanceof EdsStockTransferTransaction) {
                ServerSecurityContext.getInstance().setStaticUserID(trx.getPostedBy() != null ? trx.getPostedBy().getObjectID() : null);
                accountingServiceLocal.createTransactionForStockTransfer(((EdsStockTransferTransaction) trx).getStockTransfer());
                ServerSecurityContext.getInstance().setStaticUserID(null);
            } else {
                rabbitMQService.outItemByFifoMQ(fifoItem);
            }
        } else {
            rabbitMQService.outItemByFifoMQ(fifoItem);
        }
    }

}