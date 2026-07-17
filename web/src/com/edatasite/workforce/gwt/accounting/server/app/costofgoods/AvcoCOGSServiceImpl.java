package com.edatasite.workforce.gwt.accounting.server.app.costofgoods;

import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.EdsItemStock;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Priority;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Priority(Ordered.LOWEST_PRECEDENCE)
public class AvcoCOGSServiceImpl extends COGSServiceImpl {

    @Override
    public BigDecimal getCOGSValue(EdsItem product, BigDecimal itemQty, EdsTransaction transaction, Integer warehouseId, Integer transactionItemId) {
        log.info("----------------AVCO CALCULATION-------------------------");
        log.info("COMPANY_ID: {}, TRANSACTION_ID: {}, TRANSACTION_DATE: {}, PRODUCT_ID: {}, WHAREHOUSE_ID: {}", ServerSecurityContext.getInstance().getCompanyId(), transaction.getObjectID(), transaction.getJournalDate(), product.getObjectID(), warehouseId);

        AvcoResult avcoResult = new AvcoResult(itemQty, BigDecimal.ZERO);
        StockItem lastInStock = null;
        BigDecimal availableQty = stockCalcManager.getAvailableStockByDate(product.getObjectID(), warehouseId, transaction);

        if (availableQty.compareTo(BigDecimal.ZERO) > 0) {
            List<StockItem> list = stockCalcManager.getItemStocksForSale(product.getObjectID(), warehouseId, transaction);
            avcoResult = getCOGSValue(list, product, transaction, avcoResult);
            lastInStock = list.get(list.size() - 1);

            if (avcoResult.getRequestedQty().compareTo(BigDecimal.ZERO) > 0) {
                List<EdsItemStock> inList = stockCalcManager.getAllInStocksAfterTransaction(product.getObjectID(), warehouseId, transaction);
                list = inList.stream().map(EdsItemStock::toDto).collect(Collectors.toCollection(LinkedList::new));
                avcoResult = getCOGSValueAterTransaction(list, product, transaction, avcoResult);
                lastInStock = !list.isEmpty() ? ((LinkedList<StockItem>) list).getLast() : null;
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
                avcoResult = getCOGSValueAterTransaction(list, product, transaction, avcoResult);
                lastInStock = !list.isEmpty() ? list.getLast() : null;
            } else if (!CollectionUtils.isEmpty(list)) {
                lastInStock = !list.isEmpty() ? list.getLast() : null;
            }
        }

        if (avcoResult.getRequestedQty().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal cost = lastInStock != null ? lastInStock.getPrice() : product.getUnitPrice() != null ? product.getUnitPrice() : BigDecimal.ZERO;
            StockItem stockItem = new StockItem(lastInStock != null ? lastInStock.getId() : null, warehouseId,
                    lastInStock != null ? lastInStock.getDate() : transaction.getJournalDate(),
                    avcoResult.getRequestedQty(),
                    cost, lastInStock != null ? lastInStock.getOrder() : 0);
            avcoResult = getCOGSValueAterTransaction(List.of(stockItem), product, transaction, avcoResult);
        }

        BigDecimal averageCost = avcoResult.getCOGS().divide(itemQty, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        EdsItemStock itemStock = wrapSellingItemStock(averageCost, product, transaction, warehouseId);
        itemStock.setQuantity(itemQty);
        itemStock.setTranValue(itemQty.multiply(averageCost));
        itemStock.setTransactionItemId(transactionItemId);
        itemStockManager.createOrUpdate(itemStock);
        return avcoResult.getCOGS();
    }

    private AvcoResult getCOGSValueAterTransaction(List<StockItem> availableStocks, EdsItem product, EdsTransaction transaction, AvcoResult result) {
        BigDecimal requestedQty = result.getRequestedQty();
        BigDecimal COGS = result.getCOGS();

        for (StockItem stock : availableStocks) {

            if (requestedQty.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
            if (stock.getQuantity() != null && stock.getQuantity().doubleValue() > 0) {
                BigDecimal costValue;

                if (requestedQty.compareTo(stock.getQuantity()) >= 0) {
                    costValue = stock.getQuantity().multiply(stock.getPrice());
                    requestedQty = requestedQty.subtract(stock.getQuantity());
                } else {
                    costValue = requestedQty.multiply(stock.getPrice());
                    requestedQty = BigDecimal.ZERO;
                }
                COGS = COGS.add(costValue);
            }
        }

        result.setRequestedQty(requestedQty);
        result.setCOGS(COGS);
        return result;
    }


    private AvcoResult getCOGSValue(List<StockItem> availableStocks, EdsItem product, EdsTransaction transaction, AvcoResult result) {
        BigDecimal requestedQty = result.getRequestedQty();
        BigDecimal COGS = result.getCOGS();

        BigDecimal availableQty = availableStocks.stream().map(StockItem::getQuantity).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        BigDecimal availableCost = availableStocks.stream().reduce(BigDecimal.ZERO, (total, stock) -> {
            total = total.add(stock.getQuantity().multiply(stock.getPrice()));
            return total;
        }, BigDecimal::add);
        BigDecimal avaragePrice = availableCost.divide(availableQty, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);

        for (StockItem stock : availableStocks) {

            if (requestedQty.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
            if (stock.getQuantity() != null && stock.getQuantity().doubleValue() > 0) {
                BigDecimal costValue;

                if (requestedQty.compareTo(stock.getQuantity()) >= 0) {
                    costValue = stock.getQuantity().multiply(avaragePrice);
                    requestedQty = requestedQty.subtract(stock.getQuantity());
                } else {
                    costValue = requestedQty.multiply(avaragePrice);
                    requestedQty = BigDecimal.ZERO;
                }
                COGS = COGS.add(costValue);
            }
        }

        result.setRequestedQty(requestedQty);
        result.setCOGS(COGS);
        return result;
    }

    private EdsItemStock wrapSellingItemStock(BigDecimal averageCost, EdsItem item, EdsTransaction transaction, Integer warehouseID) {
        EdsItemStock itemStock = new EdsItemStock();
        itemStock.setItemId(item.getObjectID());
        itemStock.setTranCode(Constants.TC_OUT);
        itemStock.setPrice(averageCost);
        //itemStock.setOrder(itemStockManager.getNextOrder(item.getObjectID()));
        itemStock.setTransaction(transaction);
        itemStock.setDate(transaction.getJournalDate());
        itemStock.setTranDate(transaction.getJournalDate());

        if (warehouseID != null) {
            itemStock.setWarehouse(warehouseManager.get(warehouseID));
        }
        return itemStock;
    }

    @Override
    public boolean supports(String type) {
        return COGSType.AVCO.equals(type);
    }

    static class AvcoResult implements Serializable {
        private BigDecimal requestedQty;
        private BigDecimal COGS;

        public AvcoResult() {
        }

        public AvcoResult(BigDecimal requestedQty, BigDecimal COGS) {
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
