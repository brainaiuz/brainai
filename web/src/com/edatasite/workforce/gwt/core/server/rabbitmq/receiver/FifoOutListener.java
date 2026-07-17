package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.core.domain.EdsAdjustmentItem;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.solr.component.ProductsServicesSolrComponent;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FifoItem;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class FifoOutListener extends FifoBaseListener {

    @Autowired
    private ProductsServicesSolrComponent productsServicesSolrCompoent;

    @Override
    protected void doAction(FifoItem fifoItem) {
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DISABLE_FIFO)) {
            return;
        }
        BigDecimal COGS = cogsServices.getService().createCogsTransaction(fifoItem);

        EdsTransaction transaction = transactionManager.get(fifoItem.getTransactionId());

        if (transaction instanceof EdsStockTransferTransaction) {
            correctTransferInStock(fifoItem, (EdsStockTransferTransaction) transaction, COGS);
        }

        /**
         * re-generate COGS for the solt items after the processing transaction
         */
        reCreateCogsTransactions(fifoItem);
    }

    protected void reCreateCogsTransactions(FifoItem fifoItem) {
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DISABLE_FIFO)) {
            return;
        }
        int start = 0;
        List<FifoItem> outStocks = null;
        EdsTransaction transaction = transactionManager.get(fifoItem.getTransactionId());

        do {
//            outStocks = stockCalcManager.getOutStocksAfterTransaction(fifoItem.getProductId(), fifoItem.getWarehouserId(), transaction, start, PAGE_SIZE);
            if (CollectionUtils.isNotEmpty(outStocks)) {
                log.info("-------------------regeneration is started----------------------");
                reCreateGocsTransactions(outStocks);
            }
            start += PAGE_SIZE;
        } while (outStocks.size() == PAGE_SIZE);

        try {
            productsServicesSolrCompoent.index(itemManager.get(fifoItem.getProductId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getTransactionType() {
        return "OUT TRANSACTION";
    }

    @Override
    boolean isValid(FifoItem fifoItem) {
        EdsTransaction transaction = transactionManager.get(fifoItem.getTransactionId());
        if (transaction == null) {
            return false;
        }

        EdsItem product = itemManager.get(fifoItem.getProductId());
        if (product == null) {
            return false;
        }

        if (fifoItem.getTransactionItemId() != null) {
            if (transaction instanceof EdsInvoiceTransaction) {
                EdsInvoiceItem invoiceItem = invoiceItemManager.get(fifoItem.getTransactionItemId());
                if (invoiceItem == null) {
                    return false;
                }
            } else if (transaction instanceof EdsStockAdjustmentTransaction || transaction instanceof EdsStockTransferTransaction) {
                EdsAdjustmentItem adjustmentItem = stockAdjustmentItemManager.get(fifoItem.getTransactionItemId());
                if (adjustmentItem == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
