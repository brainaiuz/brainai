package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.core.domain.EdsAdjustmentItem;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.solr.component.ProductsServicesSolrComponent;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FifoItem;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFOItemMQ;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class BatchFifoOutListener extends FifoListener {

    @Autowired
    private ProductsServicesSolrComponent productsServicesSolrComponent;

    @Override
    protected void doAction(FIFOItemMQ item, EdsTransaction transaction) {
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DISABLE_FIFO)) {
            return;
        }
        FifoItem fifoItem = item.convertToFifoItem();
        fifoItem.setTransactionId(transaction.getObjectID());
        BigDecimal COGS = cogsServices.getService().createCogsTransaction(fifoItem);

        if (transaction instanceof EdsStockTransferTransaction) {
            correctTransferInStock(fifoItem, (EdsStockTransferTransaction) transaction, COGS);
        }


        /**
         * re-generate COGS for the solt items after the processing transaction
         */
        reCreateCogsTransactions(item, transaction);
    }

    protected void reCreateCogsTransactions(FIFOItemMQ item, EdsTransaction transaction) {
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DISABLE_FIFO)) {
            return;
        }
        int start = 0;
        List<FifoItem> outStocks = null;

        do {
            outStocks = stockCalcManager.getOutStocksAfterTransaction(item.getProductId(), item.getWarehouserId(), transaction, start, PAGE_SIZE);
            if (CollectionUtils.isNotEmpty(outStocks)) {
                log.info("-------------------regeneration is started----------------------");
                reCreateGocsTransactions(outStocks, transaction);
            }
            start += PAGE_SIZE;
        } while (outStocks.size() == PAGE_SIZE);

        try {
            productsServicesSolrComponent.index(itemManager.get(item.getProductId()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String getTransactionType() {
        return "OUT TRANSACTION";
    }

    @Override
    boolean isValid(FIFOItemMQ item, EdsTransaction transaction) {
        if (transaction == null) {
            return false;
        }
        EdsItem product = itemManager.get(item.getProductId());
        if (product == null) {
            return false;
        }

        if (item.getTransactionItemId() != null) {
            if (transaction instanceof EdsInvoiceTransaction) {
                EdsInvoiceItem invoiceItem = invoiceItemManager.get(item.getTransactionItemId());
                if (invoiceItem == null) {
                    return false;
                }
            } else if (transaction instanceof EdsStockAdjustmentTransaction || transaction instanceof EdsStockTransferTransaction) {
                EdsAdjustmentItem adjustmentItem = stockAdjustmentItemManager.get(item.getTransactionItemId());
                if (adjustmentItem == null) {
                    return false;
                }
            }
        }

        return true;
    }
}