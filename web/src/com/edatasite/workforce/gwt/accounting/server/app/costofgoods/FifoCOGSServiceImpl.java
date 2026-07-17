package com.edatasite.workforce.gwt.accounting.server.app.costofgoods;

import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.EdsItemStock;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Priority;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service("fifoCOGSService")
@Priority(Ordered.HIGHEST_PRECEDENCE)
public class FifoCOGSServiceImpl extends COGSServiceImpl implements AccountingConstants {

    @Override
    public BigDecimal getCOGSValue(EdsItem product, BigDecimal itemQty, EdsTransaction transaction, Integer warehouseId, Integer transactionItemId) {
        log.info("----------------FIFO CALCULATION-------------------------");
        log.info("COMPANY_ID: {}, TRANSACTION_ID: {}, TRANSACTION_DATE: {}, PRODUCT_ID: {}, WHAREHOUSE_ID: {}", ServerSecurityContext.getInstance().getCompanyId(), transaction.getObjectID(), transaction.getJournalDate(), product.getObjectID(), warehouseId);

        FifoResult fifoResult = new FifoResult(itemQty, BigDecimal.ZERO);
        StockItem lastInStock = null;
        BigDecimal availableQty = stockCalcManager.getAvailableStockByDate(product.getObjectID(), warehouseId, transaction);

        if (availableQty.compareTo(BigDecimal.ZERO) > 0) {
            List<StockItem> list = stockCalcManager.getItemStocksForSale(product.getObjectID(), warehouseId, transaction);
            fifoResult = getCOGSValue(list, product, transaction, fifoResult, transactionItemId);
            lastInStock = !list.isEmpty() ? list.get(list.size() - 1) : null;

            if (fifoResult.getRequestedQty().compareTo(BigDecimal.ZERO) > 0) {
                List<EdsItemStock> inList = stockCalcManager.getAllInStocksAfterTransaction(product.getObjectID(), warehouseId, transaction);
                list = inList.stream().map(EdsItemStock::toDto).collect(Collectors.toCollection(LinkedList::new));
                fifoResult = getCOGSValue(list, product, transaction, fifoResult, transactionItemId);

                if (!list.isEmpty()) {
                    lastInStock = ((LinkedList<StockItem>) list).getLast();
                }
            }
        } else {
            List<EdsItemStock> inList = stockCalcManager.getAllInStocksAfterTransaction(product.getObjectID(), warehouseId, transaction);
            LinkedList<StockItem> list = inList.stream().map(EdsItemStock::toDto).collect(Collectors.toCollection(LinkedList::new));
            for (StockItem item : list) {

                if (item.getQuantity().add(availableQty).compareTo(BigDecimal.ZERO) > 0) {
                    item.setQuantity(item.getQuantity().add(availableQty));
                    availableQty = BigDecimal.ZERO;
                    break;
                } else {
                    availableQty = item.getQuantity().add(availableQty);
                    item.setExclude(true);
                }
            }

            if (availableQty.compareTo(BigDecimal.ZERO) == 0) {
                list = list.stream().filter(i -> !i.isExclude()).sorted(Comparator.comparing(StockItem::getDate)).collect(Collectors.toCollection(LinkedList::new));
                fifoResult = getCOGSValue(list, product, transaction, fifoResult, transactionItemId);
                lastInStock = !list.isEmpty() ? list.getLast() : null;
            } else if (!CollectionUtils.isEmpty(list)) {
                lastInStock = !list.isEmpty() ? list.getLast() : null;
            }
        }

        if (fifoResult.getRequestedQty().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal cost;
            if (lastInStock != null) {
                cost = lastInStock.getPrice();
            } else if (product.getUnitPrice() != null) {
                cost = product.getUnitPrice();
            } else {
                cost = BigDecimal.ZERO;
            }
            StockItem stockItem = new StockItem(lastInStock != null ? lastInStock.getId() : null, warehouseId,
                    lastInStock != null ? lastInStock.getDate() : transaction.getJournalDate(),
                    fifoResult.getRequestedQty(),
                    cost, lastInStock != null ? lastInStock.getOrder() : 0);
            fifoResult = getCOGSValue(List.of(stockItem), product, transaction, fifoResult, transactionItemId);
        }

        return fifoResult.getCOGS();
    }

    private FifoResult getCOGSValue(List<StockItem> availableStocks, EdsItem product, EdsTransaction transaction, FifoResult result, Integer transactionItemId) {
        BigDecimal requestedQty = result.getRequestedQty();
        BigDecimal COGS = result.getCOGS();

        for (StockItem stock : availableStocks) {

            if (requestedQty.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
            if (stock.getQuantity() != null && stock.getQuantity().doubleValue() > 0) {
                EdsItemStock itemStock = wrapSellingItemStock(stock, product, transaction);
                BigDecimal costValue;

                if (requestedQty.compareTo(stock.getQuantity()) >= 0) {
                    costValue = stock.getQuantity().multiply(stock.getPrice());
                    requestedQty = requestedQty.subtract(stock.getQuantity());
                    itemStock.setQuantity(stock.getQuantity());
                } else {
                    costValue = requestedQty.multiply(stock.getPrice());
                    itemStock.setQuantity(requestedQty);
                    requestedQty = BigDecimal.ZERO;
                }
                COGS = COGS.add(costValue);
                itemStock.setTranValue(costValue);
                itemStock.setTransaction(transaction);
                itemStock.setTransactionItemId(transactionItemId);
                itemStockManager.createOrUpdate(itemStock);
            }
        }

        result.setRequestedQty(requestedQty);
        result.setCOGS(COGS);
        return result;
    }

    private EdsItemStock wrapSellingItemStock(StockItem stock, EdsItem item, EdsTransaction transaction) {
        EdsItemStock itemStock = new EdsItemStock();
        itemStock.setFrom_stock_id(stock.getId());
        itemStock.setItemId(item.getObjectID());
        itemStock.setTranCode(Constants.TC_OUT);
        itemStock.setPrice(stock.getPrice());
        itemStock.setDate(stock.getDate());
        itemStock.setTranDate(transaction.getJournalDate());

        if (stock.getWarehouseID() != null) {
            itemStock.setWarehouse(warehouseManager.get(stock.getWarehouseID()));
        }
        return itemStock;
    }

    @Override
    public boolean supports(String type) {
        return true;
    }

    static class FifoResult implements Serializable {
        private BigDecimal requestedQty;
        private BigDecimal COGS;

        public FifoResult() {
        }

        public FifoResult(BigDecimal requestedQty, BigDecimal COGS) {
            this.requestedQty = requestedQty;
            this.COGS = COGS;
        }

        public BigDecimal getRequestedQty() {
            return requestedQty;
        }

        public void setRequestedQty(BigDecimal requestedQty) {
            this.requestedQty = requestedQty;
        }

        public BigDecimal getCOGS() {
            return COGS;
        }

        public void setCOGS(BigDecimal COGS) {
            this.COGS = COGS;
        }
    }
}
