package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.core.domain.accounting.EdsItemStock;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.core.solr.component.ProductsServicesSolrComponent;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FifoItem;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFOItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BatchFifoInDeleteListener extends FifoListener {

    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private ProductsServicesSolrComponent productsServicesSolrComponent;

    @Override
    protected void doAction(FIFOItemMQ fifoItem, EdsTransaction transaction) {
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DISABLE_FIFO)) {
            return;
        }
        EdsItemStock firstInBeforeTransaction = stockCalcManager.getFirstInStockBeforeTransaction(fifoItem.getProductId(), fifoItem.getWarehouserId(), transactionManager.get(transaction.getObjectID()));

        if (firstInBeforeTransaction != null) {
            rabbitMQService.inItemByFifoMQ(new FifoItem(firstInBeforeTransaction.getTransaction().getObjectID(), fifoItem.getProductId(), fifoItem.getQuantity(), fifoItem.getWarehouserId()));
        } else {
            List<FifoItem> outStocks = stockCalcManager.getAllOutStocks(fifoItem.getProductId(), fifoItem.getWarehouserId());
            reCreateGocsTransactions(outStocks, transaction);

            try {
                productsServicesSolrComponent.index(itemManager.get(fifoItem.getProductId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String getTransactionType() {
        return "DELETE IN TRANSACTION";
    }
}
