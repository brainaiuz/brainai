package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsItemStock;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockItem;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFODataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFOItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FifoItem;

import java.math.BigDecimal;
import java.util.List;

public interface StockCalcManager extends Manager<EdsItemStock> {

    BigDecimal getAvailableStockByDate(Integer itemId, Integer warehouseId, EdsTransaction transaction);

    List<StockItem> getItemStocksForSale(Integer itemId, Integer warehouseId, EdsTransaction transaction);

    EdsItemStock getFirstInStockBeforeTransaction(Integer itemId, Integer warehouseId, EdsTransaction transaction);

    List<EdsItemStock> getAllInStocksAfterTransaction(Integer itemId, Integer warehouseId, EdsTransaction transaction);

    List<FifoItem> getOutStocksBeforeTransaction(Integer itemId, Integer warehouseId, EdsTransaction transaction);

    List<FifoItem> getOutStocksAfterTransaction(Integer itemId, Integer warehouseId, EdsTransaction transaction, int start, int size);

    List<FIFODataMQ> getOutStockListAfterTransaction(Integer itemId, Integer warehouseId, EdsTransaction transaction, int start, int size);

    List<FifoItem> getAllOutStocks(Integer itemId, Integer warehouseId);
}
