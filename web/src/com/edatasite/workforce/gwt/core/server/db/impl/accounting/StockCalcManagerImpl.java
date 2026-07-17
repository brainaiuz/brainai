package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsItemStock;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.StockCalcManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFODataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFOItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FifoItem;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EntityType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.FailTarget;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class StockCalcManagerImpl extends BaseManager<EdsItemStock> implements StockCalcManager, Constants {

    public StockCalcManagerImpl() {
        super(EdsItemStock.class);
    }

    @Override
    public BigDecimal getAvailableStockByDate(Integer itemId, Integer warehouseId, EdsTransaction transaction) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(CASE WHEN stock.transaction_code = '" + TC_OUT + "' THEN (0 - stock.quantity) ELSE stock.quantity END) FROM ")
                .append(getCompanyId()).append(".item_stock stock \n")
                .append(" LEFT JOIN ").append(getCompanyId()).append(".transaction t on t.id = stock.transactionid \n")
                .append(" WHERE stock.item_id = ").append(itemId).append(" \n")
                .append(" and t.deleted <> true \n");

        if (warehouseId != null) {
            sql.append(" AND stock.warehouseid = ").append(warehouseId).append(" \n");
        }
        if (transaction != null) {
            sql.append(" AND (to_char(stock.transaction_date, 'yyyy-MM-dd') < '").append(ServerUtils.dateFormat(transaction.getJournalDate(), "yyyy-MM-dd")).append("' \n")
                    .append(" OR (to_char(stock.transaction_date,'yyyy-MM-dd') = '").append(ServerUtils.dateFormat(transaction.getJournalDate(), "yyyy-MM-dd")).append("' ")
                    .append(" AND stock.transactionid < ").append(transaction.getObjectID()).append(")) \n");
        }

        BigDecimal availableQty = (BigDecimal) findNativeSingle(sql.toString());
        return availableQty != null ? availableQty : BigDecimal.ZERO;
    }

    @Override
    public List<StockItem> getItemStocksForSale(Integer itemId, Integer warehouseId, EdsTransaction transaction) {
        StringBuilder sql = new StringBuilder("SELECT SUM(stock.quantity) ");
        sql.append(createItemStockBaseQuery(itemId, warehouseId, transaction, TC_OUT));
        BigDecimal outQty = (BigDecimal) findSingle(sql.toString());
        outQty = outQty != null ? outQty : BigDecimal.ZERO;

        sql = new StringBuilder("SELECT stock ");
        sql.append(createItemStockBaseQuery(itemId, warehouseId, transaction, TC_IN));
        sql.append("ORDER BY stock.date, stock.order, stock.objectID");

        List<EdsItemStock> inStocks = find(sql.toString());

        List<StockItem> itemsForSale = new ArrayList<>();

        for (EdsItemStock stock : inStocks) {

            if (BigDecimal.ZERO.compareTo(outQty) == 0) {
                itemsForSale.add(EdsItemStock.toDto(stock));
            } else if (outQty.compareTo(stock.getQuantity()) > 0) {
                outQty = outQty.subtract(stock.getQuantity());
            } else {
                BigDecimal qty = stock.getQuantity().subtract(outQty);
                outQty = BigDecimal.ZERO;

                if (qty.compareTo(BigDecimal.ZERO) > 0) {
                    StockItem stockItem = EdsItemStock.toDto(stock);
                    stockItem.setQuantity(qty);
                    itemsForSale.add(stockItem);
                }
            }
        }
        return itemsForSale;
    }

    private StringBuilder createItemStockBaseQuery(Integer itemId, Integer warehouseId, EdsTransaction transaction, String trType) {
        StringBuilder sql = new StringBuilder(" FROM EdsItemStock stock ")
                .append(" WHERE stock.tranCode = '").append(trType).append("' ")
                .append(" AND stock.transaction.deleted <> true ")
                .append(" AND stock.item.objectID = ").append(itemId)
                .append(" AND stock.warehouse.objectID = ").append(warehouseId);
        if (transaction != null) {
            sql.append(" AND (to_char(stock.tranDate, 'yyyy-MM-dd') < '").append(ServerUtils.dateFormat(transaction.getJournalDate(), "yyyy-MM-dd")).append("' \n")
                    .append(" OR (to_char(stock.tranDate,'yyyy-MM-dd') = '").append(ServerUtils.dateFormat(transaction.getJournalDate(), "yyyy-MM-dd")).append("' ")
                    .append(" AND stock.transaction.objectID < ").append(transaction.getObjectID()).append(")) \n");
        }
        return sql;
    }

    @Override
    public EdsItemStock getFirstInStockBeforeTransaction(Integer itemId, Integer warehouseId, EdsTransaction transaction) {
        StringBuilder sql = new StringBuilder("SELECT stock FROM EdsItemStock stock ")
                .append("WHERE stock.tranCode = '").append(TC_IN).append("' AND stock.item.objectID = ? AND stock.warehouse.objectID = ? ")
                .append("AND (to_char(stock.tranDate, 'yyyy-MM-dd') < '").append(ServerUtils.dateFormat(transaction.getJournalDate(), "yyyy-MM-dd")).append("' ")
                .append("OR (to_char(stock.tranDate, 'yyyy-MM-dd') = '").append(ServerUtils.dateFormat(transaction.getJournalDate(), "yyyy-MM-dd")).append("' ")
                .append("       AND stock.transaction.objectID < ").append(transaction.getObjectID()).append(")) ")
                .append("ORDER BY stock.date DESC, stock.order DESC ");

        List<EdsItemStock> list = find(sql.toString(), itemId, warehouseId);
        return !CollectionUtils.isEmpty(list) ? list.get(0) : null;
    }

    @Override
    public List<EdsItemStock> getAllInStocksAfterTransaction(Integer itemId, Integer warehouseId, EdsTransaction transaction) {
        StringBuilder sql = new StringBuilder("SELECT stock FROM EdsItemStock stock ")
                .append("WHERE stock.tranCode = '").append(TC_IN).append("' AND stock.item.objectID = ? AND stock.warehouse.objectID = ? ")
                .append("AND (to_char(stock.tranDate, 'yyyy-MM-dd') > '").append(ServerUtils.dateFormat(transaction.getJournalDate(), "yyyy-MM-dd")).append("' ")
                .append("OR (to_char(stock.tranDate, 'yyyy-MM-dd') = '").append(ServerUtils.dateFormat(transaction.getJournalDate(), "yyyy-MM-dd")).append("' ")
                .append("       AND stock.transaction.objectID > ").append(transaction.getObjectID()).append(")) ")
                .append("ORDER BY stock.date, stock.order ");

        return find(sql.toString(), itemId, warehouseId);
    }

    @Override
    public List<FifoItem> getOutStocksBeforeTransaction(Integer itemId, Integer warehouseId, EdsTransaction transaction) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT stock.transactionid, SUM(stock.quantity) qty, stock.transaction_item_id FROM ").append(getCompanyId()).append(".item_stock stock \n")
                .append("WHERE stock.transaction_code = '").append(TC_OUT).append("' \n")
                .append("AND stock.item_id = ").append(itemId).append(" \n")
                .append("AND stock.warehouseid = ").append(warehouseId).append(" \n");

        sql.append("AND (to_char(stock.transaction_date,'yyyy-MM-dd') < '").append(ServerUtils.dateFormat(transaction.getJournalDate(), "yyyy-MM-dd")).append("' \n")
                .append("OR (to_char(stock.transaction_date,'yyyy-MM-dd') = '").append(ServerUtils.dateFormat(transaction.getJournalDate(), "yyyy-MM-dd")).append("' AND stock.transactionid < ").append(transaction.getObjectID()).append(")) \n");

        sql.append("GROUP BY stock.transactionid, stock.transaction_date, stock.transaction_item_id \n")
                .append("ORDER BY stock.transaction_date DESC, stock.transactionid DESC \n");

        LinkedList<FifoItem> items = new LinkedList<>();
        List<Object[]> list = findNative(sql.toString());

        for (Object[] objects : list) {
            items.add(new FifoItem((Integer) objects[0], itemId, (BigDecimal) objects[1], warehouseId, (Integer) objects[2]));
        }

        return items;
    }

    @Override
    public List<FifoItem> getOutStocksAfterTransaction(Integer itemId, Integer warehouseId, EdsTransaction transaction, int start, int size) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT stock.transactionid, SUM(stock.quantity) qty, stock.transaction_item_id FROM ").append(getCompanyId()).append(".item_stock stock \n")
                .append("WHERE stock.transaction_code = '").append(TC_OUT).append("' \n")
                .append("AND stock.item_id = ").append(itemId).append(" \n")
                .append("AND stock.warehouseid = ").append(warehouseId).append(" \n")
                .append("AND (to_char(stock.transaction_date,'yyyy-MM-dd') > '").append(ServerUtils.dateFormat(transaction.getJournalDate(), "yyyy-MM-dd")).append("' \n")
                .append("OR (to_char(stock.transaction_date,'yyyy-MM-dd') = '").append(ServerUtils.dateFormat(transaction.getJournalDate(), "yyyy-MM-dd")).append("' AND stock.transactionid > ").append(transaction.getObjectID()).append(")) \n")
                .append("GROUP BY stock.transactionid, stock.transaction_date, stock.transaction_item_id \n")
                .append("ORDER BY stock.transaction_date, stock.transactionid \n");

        if (size > 0) {
            sql.append("OFFSET ").append(start).append(" LIMIT ").append(size);
        }
        LinkedList<FifoItem> items = new LinkedList<>();
        List<Object[]> list = findNative(sql.toString());

        for (Object[] objects : list) {
            items.add(new FifoItem((Integer) objects[0], itemId, (BigDecimal) objects[1], warehouseId, (Integer) objects[2]));
        }

        return items;
    }

    @Override
    public List<FIFODataMQ> getOutStockListAfterTransaction(Integer itemId, Integer warehouseId, EdsTransaction transaction, int start, int size) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT st.id entityId, stock.transactionid, SUM(stock.quantity) qty, stock.transaction_item_id FROM ")
                .append(getCompanyId()).append(".item_stock stock \n")
                .append("join ").append(getCompanyId()).append(".transaction tr on stock.transactionid = tr.id \n")
                .append("join ").append(getCompanyId()).append(".stockTransfer st on tr.stockTransferId = st.id \n")
                .append("WHERE stock.transaction_code = '").append(TC_OUT).append("' \n")
                .append("AND stock.item_id = ").append(itemId).append(" \n")
                .append("AND stock.warehouseid = ").append(warehouseId).append(" \n")
                .append("AND (to_char(stock.transaction_date,'yyyy-MM-dd') > '").append(ServerUtils.dateFormat(transaction.getJournalDate(), "yyyy-MM-dd")).append("' \n")
                .append("OR (to_char(stock.transaction_date,'yyyy-MM-dd') = '").append(ServerUtils.dateFormat(transaction.getJournalDate(), "yyyy-MM-dd")).append("' AND stock.transactionid > ").append(transaction.getObjectID()).append(")) \n")
                .append("GROUP BY st.id, stock.transactionid, stock.transaction_date, stock.transaction_item_id \n")
                .append("ORDER BY stock.transaction_date, stock.transactionid \n");

        if (size > 0) {
            sql.append("OFFSET ").append(start).append(" LIMIT ").append(size);
        }
        Map<Integer, FIFODataMQ> itemsMap = new HashMap<>();
        List<Object[]> list = findNative(sql.toString());

        for (Object[] objects : list) {
            if (!itemsMap.containsKey((Integer) objects[0])) {
                FIFODataMQ fifoDataMQ = new FIFODataMQ();
                fifoDataMQ.setEntityId((Integer) objects[0]);
                fifoDataMQ.setTransactionId((Integer) objects[1]);
                fifoDataMQ.setRemoving(false);
                fifoDataMQ.setTarget(FailTarget.SENDING);
                fifoDataMQ.setEntityType(EntityType.STOCK_TRANSFER_OUT);
                fifoDataMQ.getFifoItems().add(new FIFOItemMQ(itemId, (BigDecimal) objects[2], warehouseId, (Integer) objects[3]));
                itemsMap.put((Integer) objects[0], fifoDataMQ);
            } else {
                itemsMap.get((Integer) objects[0]).getFifoItems().add(new FIFOItemMQ(itemId, (BigDecimal) objects[2], warehouseId, (Integer) objects[3]));
            }
        }

        return itemsMap.values().stream().toList();
    }

    @Override
    public List<FifoItem> getAllOutStocks(Integer itemId, Integer warehouseId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT stock.transactionid, SUM(stock.quantity) qty, stock.transaction_item_id FROM ").append(getCompanyId()).append(".item_stock stock \n")
                .append("WHERE stock.transaction_code = '").append(TC_OUT).append("' \n")
                .append("AND stock.item_id = ").append(itemId).append(" \n")
                .append("AND stock.warehouseid = ").append(warehouseId).append(" \n")
                .append("GROUP BY stock.transactionid, stock.transaction_date, stock.transaction_item_id \n")
                .append("ORDER BY stock.transaction_date, stock.transactionid \n");

        LinkedList<FifoItem> items = new LinkedList<>();
        List<Object[]> list = findNative(sql.toString());

        for (Object[] objects : list) {
            items.add(new FifoItem((Integer) objects[0], itemId, (BigDecimal) objects[1], warehouseId, (Integer) objects[2]));
        }

        return items;
    }
}
