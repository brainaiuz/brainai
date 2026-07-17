package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.client.server.app.ClientServiceImpl.ZERO;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Mar 3, 2011
 * Time: 12:25:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("itemStockManager")
public class ItemStockManagerImpl extends BaseManager<EdsItemStock> implements ItemStockManager, Constants {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private WarehouseManager warehouseManager;
    public ItemStockManagerImpl() {
        super(EdsItemStock.class);
    }

    @Override
    public boolean createOrUpdate(EdsItemStock obj) {
        boolean isCreate = super.createOrUpdate(obj);
        registerCustomEvent(obj);
        return isCreate;
    }

    @Override
    public void create(EdsItemStock obj) {
        super.create(obj);
        registerCustomEvent(obj);
    }

    @Override
    public void update(EdsItemStock obj) {
        super.update(obj);
        registerCustomEvent(obj);
    }

    @Override
    public void delete(EdsItemStock obj) {
        registerCustomEvent(obj);
        super.delete(obj);
    }

    @Override
    public List<EdsItemStock> getItemStockByTransaction(Integer transactionID, Integer itemID) {
        return getItemStockByTransaction(transactionID, itemID, null, null);
    }

    @Override
    public List<EdsItemStock> getItemStockByTransaction(Integer transactionID, Integer itemID, EdsWarehouse warehouse, Integer productIdentifier) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT stock FROM EdsItemStock stock ");
        sql.append("join stock.transaction transaction ");
        sql.append("join stock.item item ");

        sql.append("WHERE transaction.objectID = ? ");
        sql.append("AND item.objectID = ? ");

        if (warehouse != null && warehouse.getObjectID() != null) {
            sql.append("AND stock.warehouse.objectID = '" + warehouse.getObjectID() + "' ");
        }
        if (productIdentifier != null) {
            sql.append("AND stock.productIdentifier='" + productIdentifier + "' ");
        }

        sql.append("ORDER BY stock.date DESC, stock.order DESC ");

        return find(sql.toString(), transactionID, itemID);
    }

    @Override
    public List<EdsItemStock> getItemStockListByProductAndWarehouse(Integer productID, Integer warehouseID) {
        return find("select s from EdsItemStock s where s.item.objectID = ? AND s.warehouse.objectID = ?", productID, warehouseID);
    }

    @Override
    public List<StockItem> getItemStocksByWarehouse(Integer itemID, Integer warehouseID) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT new com.edatasite.workforce.gwt.accounting.client.rpc.product.StockItem(w.objectID, s.date, SUM(CASE WHEN s.tranCode = '" + TC_OUT + "' THEN (0 - s.quantity) ELSE s.quantity END), s.price, s.order)");
        sql.append("FROM EdsItemStock s ");
        sql.append("join s.item i ");
        sql.append("join s.warehouse w ");
        sql.append("WHERE i.objectID = ? ");
        if (warehouseID != null) {
            sql.append(" AND w.objectID = '").append(warehouseID).append("' ");
        }
        sql.append("GROUP BY s.date, w.objectID, s.price, s.order ");
        sql.append("ORDER BY s.date, s.order ");

        return find(sql.toString(), itemID);
    }

    /**
     * Only for Magento Sync
     * */
    @Override
    public Map<Integer, BigDecimal> getItemStocksForSync() {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT i.objectID, SUM(CASE WHEN s.tranCode = '" + TC_OUT + "' THEN (0 - s.quantity) ELSE s.quantity END) ");
        sql.append("FROM EdsItemStock s ");
        sql.append("join s.item i ");
        sql.append("WHERE i.magentoEntityID is not null ");
        sql.append("and i.stockChanged = true ");
        sql.append("AND i.type = " + AccountingConstants.INVENTORY_ITEM + " ");
        sql.append("GROUP BY i.objectID ");
        sql.append("ORDER BY i.objectID ");
        List<Object[]> list = find(sql.toString());

        Map<Integer, BigDecimal> map = new HashMap<>();
        if (list != null && !list.isEmpty()) {
            for (Object[] objects : list) {
                if (objects[0] != null && objects[1] != null) {
                    map.put((Integer)objects[0], (BigDecimal) objects[1]);
                }
            }
            return map;
        }
        return null;
    }

    @Override
    public List<StockItem> getItemStocks(Integer transactionID, Integer itemID, Integer warehouseID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT new com.edatasite.workforce.gwt.accounting.client.rpc.product.StockItem(w.objectID, s.date, s.quantity, s.price, s.order)");
        sql.append("FROM EdsItemStock s ");
        sql.append("join s.transaction t ");
        sql.append("join s.item i ");
        sql.append("join s.warehouse w ");
        sql.append("WHERE i.objectID = ? AND t.objectID = ? ");

        if (warehouseID != null) {
            sql.append(" AND w.objectID = " + warehouseID);
        }

        sql.append("ORDER BY s.date DESC, s.order DESC ");//do not change please(here was normurod)

        return find(sql.toString(), itemID, transactionID);
    }

    @Override
    public BigDecimal getOutItemQtyFromStock(Integer transactionID, Integer itemID, Integer warehouseID, Integer productIdentifier) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(stock.quantity) FROM EdsItemStock stock ");
        sql.append("join stock.transaction t ");
        sql.append("join stock.item i WHERE t.objectID = ? ");
        if (itemID != null) {
            sql.append("AND i.objectID =").append(itemID);
        }
        if (warehouseID != null) {
            sql.append(" AND stock.warehouse.objectID=" + warehouseID);
        }
        if (productIdentifier != null) {
            sql.append(" AND stock.productIdentifier=" + productIdentifier);
        }
        return (BigDecimal) findSingle(sql.toString(), transactionID);
    }

    public Object getInventoryTransactionBalanceToDate(Integer objectID, Date toDate, Integer warehouseID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("SUM(CASE WHEN s.tranCode = '" + TC_OUT + "' THEN (0 - s.quantity) ELSE s.quantity END) AS qty, ");
        sql.append("SUM(CASE WHEN s.tranCode = '" + TC_OUT + "' THEN (0 - s.tranValue) ELSE s.tranValue END) AS value ");
        sql.append("FROM EdsItemStock s ");
        sql.append("WHERE s.transaction.deleted <> true and s.item.objectID = '" + objectID + "' ");
        if (toDate != null) {
            sql.append("AND s.tranDate < '" + toDate + "' ");
        }
        if (warehouseID != null) {
            sql.append("AND s.warehouse.objectID = '" + warehouseID + "' ");
        }
        return findSingle(sql.toString());
    }

    public HashMap<Integer, BigDecimal> getInventoryTransactionBalance(List<Integer> itemIds, Integer warehouseID) {
        HashMap<Integer, BigDecimal> result = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.item.objectID, ");
        sql.append("SUM(CASE WHEN s.tranCode = '" + TC_OUT + "' THEN (0 - s.quantity) ELSE s.quantity END) AS qty, ");
        sql.append("SUM(CASE WHEN s.tranCode = '" + TC_OUT + "' THEN (0 - s.tranValue) ELSE s.tranValue END) AS value ");
        sql.append("FROM EdsItemStock s ");
        sql.append("WHERE s.item.objectID  in (").append(ServerUtils.getAsCommoDelimited(itemIds, "0")).append(") ");
        if (warehouseID != null) {
            sql.append("AND s.warehouse.objectID = '" + warehouseID + "' ");
        }
        sql.append(" group by s.item.objectID ");
        List<Object[]> resp = find(sql.toString());

        Integer calculationScale = financialSettingsManager.getFinancialSettings().getAccountingCalculationScale();

        for (Object[] values : resp) {
            Integer id = (Integer) values[0];
            BigDecimal bBalance = null;
            BigDecimal bQty = null;
            BigDecimal bResult = null;
            if (values != null) {
                BigDecimal qty = (BigDecimal)((Object[]) values)[1];
                bQty = (qty != null && !qty.equals(ZERO)) ? qty : null;
                BigDecimal value = (BigDecimal)((Object[]) values)[2];
                bBalance = (value != null && !value.equals(ZERO)) ? (BigDecimal) value : null;
            }

            if (bBalance != null && bQty != null && bQty.compareTo(BigDecimal.ZERO) != 0) {
                bResult = bBalance.divide(bQty, calculationScale, RoundingMode.HALF_UP);
                result.put(id, bResult);
            }
        }
        return result;
    }

    @Override
    public LinkedHashMap<Integer, List<StockItem>> getInventoryTransaction(ListingFilterParameter fp) {

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            if (fp.getStartDate().compareTo(fp.getEndDate()) > 0) {
                return null;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("scale", ServerUtils.getCalculationScale());

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.id transactionid, i.id itemid, SUM(s.quantity) quantity, SUM(s.transaction_value) transactionValue , s.transaction_code transactionCode, ");
        sql.append(" array_to_string(array_agg(ROUND(s.quantity::numeric, :scale)::text||' × '||ROUND(s.price::numeric, :scale)::text ),'<br>') quantityPerPriceList, ");
        sql.append(" array_to_string(array_agg(s.quantity||' × '||s.price ),'<br>') priceListWithoutScaling ");
        sql.append(getStockTransactionBase(fp, map));
        List<StockItem> items = jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), map, BeanPropertyRowMapper.newInstance(StockItem.class));
        LinkedHashMap<Integer, List<StockItem>> itemStocksMap = new LinkedHashMap<>();
        for (StockItem item : items) {
            if (itemStocksMap.get(item.getItemID()) != null) {
                itemStocksMap.get(item.getItemID()).add(item);
            } else {
                List<StockItem> stockItems = new ArrayList<>();
                stockItems.add(item);
                itemStocksMap.put(item.getItemID(), stockItems);
            }

        }
        return itemStocksMap;
    }

    @Override
    public LinkedHashMap<Integer, EdsTransaction> getInventoryTransactionMap(ListingFilterParameter fp) {
        LinkedHashMap<Integer, EdsTransaction> inventoryTransactionMap = new LinkedHashMap<>();
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            if (fp.getStartDate().compareTo(fp.getEndDate()) > 0) {
                return inventoryTransactionMap;
            }
        }
        Map<String, Object> map = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct t.* ");
        sql.append(getStockTransactionBase(fp, map));

        List<EdsTransaction> transactions = findNative(sql.toString(), EdsTransaction.class);
        for (EdsTransaction transaction : transactions) {
            inventoryTransactionMap.put(transaction.getObjectID(), transaction);
        }
        return inventoryTransactionMap;
    }

    private String getStockTransactionBase(ListingFilterParameter fp, Map<String, Object> map) {
        StringBuilder sql = new StringBuilder();
        sql.append(" FROM " + getCompanyId() + ".item_stock s ");
        sql.append(" join " + getCompanyId() + ".transaction t on s.transactionid=t.id ");
        sql.append(" left join " + getCompanyId() + ".item i on s.item_id=i.id ");
        sql.append(" left join " + getCompanyId() + ".warehouse w on s.warehouseid=w.id ");
        sql.append("WHERE " + ServerUtils.checkForDeleted("t.deleted") + " and  s.quantity > 0 ");

        if (fp.getCaseID() != null) {
            sql.append("AND i.id = '" + fp.getCaseID() + "' ");
        }

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append("AND t.journalDate between '" + fp.getStartDate() + "' AND '" + fp.getEndDate() + "' ");
        }

        if (fp.getWarehouseID() != null) {
            sql.append(" AND w.id = '" + fp.getWarehouseID() + "' ");
        }

        sql.append("GROUP BY t.journalDate,i.id, t.id, s.transaction_code ");
        sql.append("ORDER BY t.journalDate,t.id ");
        return sql.toString();
    }

    @Override
    public void deleteItemStocksByTransaction(Integer transactionID) {
        update("DELETE FROM EdsItemStock WHERE transaction.objectID = ?", transactionID);
    }

    @Override
    public void deleteItemOutStocksByTransaction(Integer transactionID) {
        update("DELETE FROM EdsItemStock WHERE tranCode = 'OUT' AND transaction.objectID = ?", transactionID);
    }

    @Override
    public void deleteItemStocksByTransaction(Integer transactionID, String transactionCode, Integer itemId, Integer warehouseId, Integer transactionItemId) {
        update("DELETE FROM EdsItemStock WHERE tranCode = '" + (StringUtils.isNotBlank(transactionCode) ? transactionCode : "OUT") + "' AND transaction.objectID = ? AND item.objectID = ? AND warehouse.objectID = ? AND " + (transactionItemId != null ? "(transactionItemId IS NULL OR transactionItemId = " + transactionItemId + ")" : "transactionItemId IS NULL"), transactionID, itemId, warehouseId);
    }

    @Override
    public void deleteItemStocksByProduct(Integer productID) {
        update("DELETE FROM EdsItemStock stock WHERE stock.item.objectID = ? ", productID);
    }

    @Override
    public boolean isUsedInTransactions(Integer itemId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(id) from ").append(getCompanyId()).append(".item_stock ");
        sql.append("where item_id=" + itemId + " and " +
                " transactionid not in (select distinct t.id from ").append(getCompanyId()).append(".transaction t ")
                .append("inner join ").append(getCompanyId()).append(".transactionitem ti on ti.transactionid = t.id \n")
                .append("inner join ").append(getCompanyId()).append(".account a on a.id = ti.accountid \n")
                .append("where t.deleted is not true and a.key = " + EdsAccount.OPENING_BALANCE + " and t.inventory_id = " + itemId + ") ");
        int count = ((BigInteger) findNativeSingle(sql.toString())).intValue();
        return count > 0;
    }

    @Override
    public void unBuildAssemblyItemStocks(Integer productID) {
        List<EdsInventoryTransaction> transactions = find("select it from EdsInventoryTransaction it where it.inventory.objectID = ?", productID);
        for (EdsInventoryTransaction it : transactions) {
            deleteItemStocksByTransaction(it.getObjectID());
        }
    }

    @Override
    public void deleteByTransaction(Integer transactionID, Integer itemID) {
        List<EdsItemStock> itemStocks = find("SELECT istock FROM EdsItemStock istock WHERE istock.transaction.objectID = ? AND istock.item.objectID = ?", transactionID, itemID);
        for (EdsItemStock itemStock : itemStocks) {
            delete(itemStock);
        }
    }

    @Override
    public void deleteByID(Integer objectID) {
        List<EdsItemStock> itemStocks = find("SELECT istock FROM EdsItemStock istock WHERE istock.objectID = ? ", objectID);
        for (EdsItemStock itemStock : itemStocks) {
            delete(itemStock);
        }
    }

    @Override
    public Integer getNextOrder(Integer objectID) {
        Integer maxOrder = (Integer) findSingle("SELECT s.order FROM EdsItemStock s WHERE s.item.objectID = '" + objectID + "' AND s.tranCode = '" + TC_IN + "' ORDER BY s.order DESC ");

        if (maxOrder == null) {
            return 0;
        }

        return maxOrder + 1;
    }

    @Override
    public BigDecimal getItemQtyInStockByWarehouse(Integer itemID, Integer warehouseID, Date date) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT coalesce((select SUM(coalesce(s.quantity, 0)) ")
                .append("        FROM \"").append(schema).append("\".item_stock s ")
                .append("        WHERE s.item_id = ").append(itemID)
                .append("              AND s.warehouseid = ").append(warehouseID)
                .append("              AND s.transaction_code = 'IN' ");

        if (date != null) {
            sql.append(" AND s.transaction_date <= '" + ServerUtils.dateFormat(date, "yyyy-MM-dd 23:59:59")).append("'");
        }
        sql.append("), 0) ");
        sql.append("       -")
                .append("       coalesce((select SUM(coalesce(s.quantity, 0)) ")
                .append("        FROM \"").append(schema).append("\".item_stock s ")
                .append("        WHERE s.item_id = ").append(itemID)
                .append("              AND s.warehouseid = ").append(warehouseID)
                .append("              AND s.transaction_code = 'OUT'), 0)");

        BigDecimal itemQtyInStock = (BigDecimal) findNativeSingle(sql.toString());
        return itemQtyInStock != null ? itemQtyInStock : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getItemLastInStockPrice(Integer itemID, Date date) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.price ");
        sql.append("FROM EdsItemStock s ");
        sql.append("join s.item i ");
        sql.append("WHERE i.objectID = ? ");
        sql.append("AND s.tranDate <= ? ");
        sql.append("AND s.tranCode = 'IN' ");
        sql.append("ORDER BY s.tranDate DESC");

        return (BigDecimal) findSingle(sql.toString(), itemID, date);
    }

    @Override
    public BigDecimal getItemLastInStockTranValue(Integer itemID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.price ");
        sql.append("FROM EdsItemStock s ");
        sql.append("join s.item i ");
        sql.append("WHERE i.objectID = ? ");
        sql.append("AND s.tranCode = 'IN' ");
        sql.append("ORDER BY s.tranDate DESC");
        return (BigDecimal) findSingle(sql.toString(), itemID);
    }

    @Override
    public List<Integer> getItemsInWarehouse(Integer warehouseID) {
        return find("SELECT DISTINCT s.item.objectID from EdsItemStock s where s.warehouse.objectID = ? and s.quantity > 0", warehouseID);
    }

    @Override
    public List<Object> getProductsStock(Integer companyID, String tranCode) {
        String schema = companyID.toString();
        StringBuilder sql = new StringBuilder();
        sql.append("select max(i.id),sum(its.quantity) qty, max(w.name) warhouse, max(pwl.minreorderpoint) minreorderpoint, max(i.name) product, max(i.description) description, max(i.product_number) number from \"" + schema + "\".warehouse w ");
        sql.append("left join \"" + schema + "\".item_stock its on w.id=its.warehouseid ");
        sql.append("left join \"" + schema + "\".item i on its.item_id=i.id ");
        sql.append("left join \"" + schema + "\".transaction t on its.transactionid=t.id ");
        sql.append("left join \"" + schema + "\".ProductWarehouseLocation pwl on its.item_id=pwl.productid and pwl.warehouseid=w.id ");
        sql.append("where " + ServerUtils.checkForDeleted("t.deleted") + " and " + ServerUtils.checkForDeleted("i.deleted") + " and its.transaction_code='" + tranCode + "' ");
        sql.append("group by its.item_id");

        return (List<Object>) findNative(sql.toString());

    }

    @Override
    public BigDecimal getTransactionValueByTransactionIdAndItemId(Integer transactionID, Integer itemID) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select max( Case When  transaction_code='" + TC_OUT + "' then transaction_value else -1*transaction_value end) ");
        sql.append(" from \"" + schema + "\".item_stock where transactionid = " + transactionID);
        sql.append(" and item_id = " + itemID);
        BigDecimal amount = (BigDecimal) findNativeSingleFromSlave(sql.toString());
        return amount != null ? amount : BigDecimal.ZERO;
    }

    @Override
    public List<Integer> getItemsByUpsNumber(String productUpcNumber) {
        return findNative("SELECT DISTINCT item.id from " + getCompanyId() + ".item item where item.upcNumber like '%" + productUpcNumber + "%' and item.deleted is not true");
    }

    @Override
    public boolean hasOutTransactionsOfItemWithChosenIn(List<Integer> transactionIds) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("transactionIds", transactionIds);

        StringBuilder sql = new StringBuilder("SELECT SUM(COALESCE(os.quantity, 0)) FROM ").append(getCompanyId()).append(".item_stock s \n");
        sql.append("INNER JOIN (SELECT * FROM ").append(getCompanyId()).append(".item_stock WHERE transaction_code = 'OUT') os ")
                .append(" ON os.item_id = s.item_id and os.date = s.date and os.sorder = s.sorder and os.warehouseid = s.warehouseid \n");
        sql.append("WHERE s.transaction_code = 'IN' \n");
        sql.append("AND s.transactionid IN :transactionIds \n");

        BigDecimal outQty = (BigDecimal)findNativeSingleByNamedParams(sql.toString(), map);
        return outQty != null && outQty.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public List<StockItem> getRemainedQuantitiesOfItemWithChosenIn(Integer transactionId) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.item_id as itemID, s.warehouseid as warehouseID, s.transactionid as transactionID, s.date, s.sorder as order, sum(coalesce(os.quantity, 0)) as quantity FROM ").append(getCompanyId()).append(".item_stock s \n");
        sql.append("INNER JOIN (SELECT * FROM ").append(getCompanyId()).append(".item_stock WHERE transaction_code = 'OUT') os ")
                .append(" ON os.item_id = s.item_id and os.date = s.date and os.sorder = s.sorder and os.warehouseid = s.warehouseid \n");
        sql.append("WHERE s.transaction_code = 'IN' \n");
        sql.append("AND s.transactionid = ").append(transactionId).append(" \n");
        sql.append("GROUP BY s.id, s.item_id, s.warehouseid, s.transactionid, s.date, s.quantity, s.sorder \n");
        sql.append("ORDER BY s.sorder \n");

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(StockItem.class));
    }

    private void registerCustomEvent(EdsItemStock obj) {
//        EdsItem item = obj.getItem();
//        item.setStockChanged(true);
//        itemManager.update(item);
    }

    @Override
    public BigDecimal getAvailableStock(Integer itemId, Integer warehouseId, List<Integer> excludedTransactionIds) {
        return getAvailableStock(itemId, warehouseId, excludedTransactionIds, null);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public BigDecimal getAvailableStock(Integer itemId, Integer warehouseId, List<Integer> excludedTransactionIds, DateNonConvertable tillDate) {
        var financialSettings = financialSettingsManager.getFinancialSettings();
        boolean isMultiWarehouseEnabled = financialSettings != null && financialSettings.getEnableMultiWarehouse();
        if (!isMultiWarehouseEnabled) {
            var defaultWarehouse = warehouseManager.getDefaultWarehouse();
            if (defaultWarehouse != null) {
                warehouseId = defaultWarehouse.getObjectID();
            }
        }
        StringBuilder q = new StringBuilder();
        q.append("SELECT SUM(CASE WHEN s.transaction_code = '").append(TC_OUT).append("'").append(" THEN (0 - s.quantity) ELSE s.quantity END) ");
        q.append(" FROM ").append(getCompanyId()).append(".item_stock s ");
        q.append(" join ").append(getCompanyId()).append(".transaction t on s.transactionid = t.id ");
        q.append(" WHERE t.deleted is not true and s.item_id = ").append(itemId);
        if (tillDate != null) {
            q.append(" and s.date <= '").append(ServerUtils.dateFormat(tillDate.getNonConvertedDate(), "yyyy-MM-dd")).append("' ");
        }
        if (warehouseId != null) {
            q.append(" AND s.warehouseid = " + warehouseId);
        }
        if (excludedTransactionIds != null && !excludedTransactionIds.isEmpty()) {
            q.append(" and s.transactionid not in (" + StringUtils.join(excludedTransactionIds, ",") + ")");
        }
        BigDecimal itemQtyInStock = (BigDecimal) findNativeSingleFromSlave(q.toString());
        return itemQtyInStock != null ? itemQtyInStock : BigDecimal.ZERO;
    }

    @Override
    public Map<Integer, BigDecimal> getAvailableStockAtWarehouse(String itemIds, Integer warehouseId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.item_id, SUM(CASE WHEN s.transaction_code = 'OUT' THEN (0 - s.quantity) ELSE s.quantity END)  ")
                .append(" FROM ").append(getCompanyId()).append(".item_stock s ")
                .append(" join ").append(getCompanyId()).append(".transaction t on s.transactionid = t.id   ")
                .append(" WHERE t.deleted is not true and s.item_id in (").append(itemIds).append(")  ");
        if (warehouseId != null) {
            sql.append(" and warehouseid = ").append(warehouseId);
        }
        sql.append(" group by s.item_id ");
        List<Object[]> list = findNativeFromSlave(sql.toString());
        Map<Integer, BigDecimal> results = new HashMap<>();
        list.forEach(objects -> results.put((Integer) objects[0], (BigDecimal) objects[1]));
        return results;
    }

    @Override
    public void deleteItemStocksByTransactionIds(List<Integer> transactionIds) {
        if (transactionIds == null || transactionIds.isEmpty()) {
            return;
        }
        String sql = "delete from EdsItemStock where transaction.objectID in (:transactionIds)";

        masterEntityManager.createQuery(sql)
                .setParameter("transactionIds", transactionIds)
                .executeUpdate();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public List<StockItem> getWarehouseStocks(Integer itemId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.warehouseid, w.name, SUM(CASE WHEN s.transaction_code = '" + TC_OUT + "'" +
                " THEN (0 - s.quantity) ELSE s.quantity END) qty FROM ").append(getCompanyId()).append(".item_stock s ")
                .append("JOIN ").append(getCompanyId()).append(".transaction t ON t.id = s.transactionid ")
                .append("JOIN ").append(getCompanyId()).append(".Warehouse w ON w.id = s.warehouseid ");
        sql.append("WHERE t.deleted is not true").append(" AND s.item_id = " + itemId + " ");
        sql.append("GROUP BY s.warehouseid, w.name");

        List<Object[]> list = findNativeFromSlave(sql.toString());
        if (!CollectionUtils.isEmpty(list)) {
            List<StockItem> stocks = new ArrayList<>();
            list.forEach(objs -> {
                StockItem stockItem = new StockItem();
                stockItem.setWarehouseID((Integer) objs[0]);
                stockItem.setWarehouseName((String) objs[1]);
                stockItem.setQuantity((BigDecimal) objs[2]);
                stocks.add(stockItem);
            });

            return stocks;
        }

        return null;
    }

    @Override
    public LinkedHashMap<Integer, List<StockItem>> getWarehouseStocksMap(String itemIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.item_id,s.warehouseid, w.name, SUM(CASE WHEN s.transaction_code = '" + TC_OUT + "'" +
                        " THEN (0 - s.quantity) ELSE s.quantity END) qty FROM ").append(getCompanyId()).append(".item_stock s ")
                .append("JOIN ").append(getCompanyId()).append(".transaction t ON t.id = s.transactionid ")
                .append("JOIN ").append(getCompanyId()).append(".Warehouse w ON w.id = s.warehouseid ");
        sql.append("WHERE t.deleted is not true").append(" AND s.item_id in ( " + itemIds + ") ");
        sql.append("GROUP BY s.item_id,s.warehouseid, w.name");

        List<Object[]> list = findNativeFromSlave(sql.toString());
        LinkedHashMap<Integer, List<StockItem>> stocksMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(list)) {
            List<StockItem> stocks = new ArrayList<>();
            final Integer[] itemId = new Integer[1];
            list.forEach(objs -> {
                itemId[0] = (Integer) objs[0];
                StockItem stockItem = new StockItem();
                stockItem.setWarehouseID((Integer) objs[1]);
                stockItem.setWarehouseName((String) objs[2]);
                stockItem.setQuantity((BigDecimal) objs[3]);
                stocks.add(stockItem);
                stocksMap.put(itemId[0], stocks);
            });
            return stocksMap;
        }

        return null;
    }

    @Override
    public List<QuantityItem> getInStocksByTransactions(List<Integer> transactionIds) {
        if (CollectionUtils.isEmpty(transactionIds)) {
            return new ArrayList<>();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.item_id, s.warehouseid, SUM(s.quantity) qty FROM ").append(getCompanyId()).append(".item_stock s \n")
                .append("WHERE s.transactionid in (").append(StringUtils.join(transactionIds, ",")).append(") \n")
                .append("AND s.transaction_code = '").append(TC_IN).append("' \n")
                .append("GROUP BY s.item_id, s.warehouseid ");

        List<Object[]> list = findNativeFromSlave(sql.toString());

        return list.stream().map(objs -> {
            QuantityItem item = new QuantityItem();
            item.setId((Integer) objs[0]);
            item.setWarehouseID((Integer) objs[1]);
            item.setQuantity((BigDecimal) objs[2]);
            return item;
        }).collect(Collectors.toList());
    }
}
