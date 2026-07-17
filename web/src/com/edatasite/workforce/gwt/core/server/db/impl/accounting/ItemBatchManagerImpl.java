package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsItemBatch;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataType;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemBatchManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.ItemSerialEntityType;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Repository("ItemBatchManager")
public class ItemBatchManagerImpl extends BaseManager<EdsItemBatch> implements ItemBatchManager {
    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    private WarehouseManager warehouseManager;

    public ItemBatchManagerImpl() {
        super(EdsItemBatch.class);
    }

    @Override
    public EdsItemBatch getBatch(Integer itemID, String serial) {
        return (EdsItemBatch) findSingle("SELECT s FROM EdsItemBatch s WHERE s.item.objectID = ? AND s.serial = ?", itemID, serial);
    }

    @Override
    public void deleteBatches(Integer itemID) {
        update("DELETE FROM EdsItemBatch b where b.item.objectID = ?", itemID);
    }

    @Override
    public void deleteBatchesByEntity(Integer entityId, Integer itemId, String entityType) {
        update("DELETE FROM EdsItemBatch b where b.entityId = ? and b.item.objectID = ? and b.entityType = ?", entityId, itemId, entityType);
    }

    @Override
    public void deleteBatchesByEntityAndType(Integer entityId, String entityType) {
        update("DELETE FROM EdsItemBatch b where b.entityId = ? and b.entityType = ?", entityId, entityType);
    }

    @Override
    public List<EdsItemBatch> getBatches(Integer lineItemId, Integer itemId, Integer entityId, String entityType) {
        StringBuilder sql = new StringBuilder("SELECT b.serial, b.expiry_date, sum(b.qty) ");
        sql.append(" FROM ").append(getCompanyId()).append(".item_batch b");
        sql.append(" WHERE b.line_item_id =").append(lineItemId);
        sql.append(" AND b.entity_id =").append(entityId);
        sql.append(" AND b.entity_type ='").append(entityType).append("' ");
        sql.append(" AND b.item_id =").append(itemId);
        sql.append(" GROUP BY b.serial, b.expiry_date");

        List<Object> list = findNative(sql.toString());

        List<EdsItemBatch> edsItemBatches = Lists.newArrayList();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Object[] it = (Object[]) list.get(i);
                String serial = (String) it[0];
                Date expiryDate = (Date) it[1];
                BigDecimal qyt = (BigDecimal) it[2];

                EdsItemBatch edsItemBatch = new EdsItemBatch();
                edsItemBatch.setObjectID(lineItemId);
                edsItemBatch.setSerial(serial);
                edsItemBatch.setExpiryDate(expiryDate);
                edsItemBatch.setQty(qyt);
                edsItemBatches.add(edsItemBatch);
            }
        }
        return edsItemBatches;
    }

    @Override
    public void deleteOldBatches(Integer lineItemId, Integer itemId, Integer entityId, String entityType) {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(getCompanyId()).append(".item_batch b");
        sql.append(" WHERE b.line_item_id =").append(lineItemId);
        sql.append(" AND b.entity_id =").append(entityId);
        sql.append(" AND b.entity_type ='").append(entityType).append("' ");
        sql.append(" AND b.item_id =").append(itemId);
        updateNative(sql.toString());
    }

    @Override
    public List<Object> getBatchesOnHand(ListingFilterParameter fp) {
        String companyID = getCompanyId();

        StringBuilder sql = new StringBuilder("SELECT bt.serial, bt.expiry_date, sum(case when bt.batch_type='")
                .append(ShippingDataType.IN.name()).append("' then bt.qty else (-1)* bt.qty end) ");
        sql.append(" FROM ").append(companyID).append(".item_batch bt \n");
        sql.append(" WHERE bt.item_id = ").append(fp.getItemId());
        sql.append(" AND bt.warehouse_id = ").append(fp.getWarehouseID()).append("\n");
        sql.append(" and (bt.status = '").append(ItemSerialEntityType.APPROVED.name()).append("' OR bt.status IS NULL)\n");
        sql.append(" AND bt.converted is not true ").append("\n");
        if (fp.getSearchKey() != null) {
            sql.append(" AND lower(bt.serial) like lower('%").append(fp.getSearchKey()).append("%') \n");
        }
        sql.append("group by bt.serial, bt.expiry_date having sum(case when bt.batch_type='").append(ShippingDataType.IN.name()).append("' then bt.qty else (-1)* bt.qty end)>0");

        return findNative(sql.toString());
    }


    @Override
    public ArrayList<ProductTrackBatchItem> getBatchesOnHandByItemId(Integer itemID) {
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder("SELECT bt.serial, bt.expiry_date, sum(case when bt.batch_type='")
                .append(ShippingDataType.IN.name()).append("' then bt.qty else (-1)* bt.qty end), bt.warehouse_id, w.name \n");
        sql.append(" FROM ").append(companyID).append(".item_batch bt LEFT JOIN ").append(companyID).append(".warehouse w ON w.id=bt.warehouse_id \n");
        sql.append(" WHERE bt.item_id = ").append(itemID);
        sql.append(" and (bt.status = '").append(ItemSerialEntityType.APPROVED.name()).append("' OR bt.status IS NULL)\n");
        sql.append(" and bt.converted is not true \n");
        sql.append(" group by bt.serial, bt.expiry_date, w.name, bt.warehouse_id having sum(case when bt.batch_type='").append(ShippingDataType.IN.name()).append("' then bt.qty else (-1)* bt.qty end)>0");

        List<Object> list = findNative(sql.toString());
        ArrayList<ProductTrackBatchItem> resultList = new ArrayList<>(list.size());
        if (list != null && !list.isEmpty()) {
            int i = 0;
            for (Object item : list) {
                i++;
                Object[] it = (Object[]) item;
                String serial = (String) it[0];
                Date expiryDate = (Date) it[1];
                BigDecimal onHand = (BigDecimal) it[2];
                Integer warehouseId = (Integer) it[3];
                String warehouseName = (String) it[4];

                ProductTrackBatchItem batchItem = new ProductTrackBatchItem();
                batchItem.setObjectID(i);
                batchItem.setSerial(serial);
                batchItem.setExpirationDate(expiryDate);
                batchItem.setBalanceInbatch(onHand);
                batchItem.setWarehouseId(warehouseId);
                batchItem.setWarehouseName(warehouseName);
                resultList.add(batchItem);
            }
        }
        return resultList;
    }

    @Override
    public List<EdsItemBatch> getList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder("SELECT  b.serial, b.expiry_date, b.entity_type, b.batch_type, b.entity_id, b.warehouse_id, sum(b.qty) " +
                "from " + getCompanyId() + ".item_batch b ");
        sql.append(" WHERE b.item_id = ").append(fp.getProductId());
        sql.append(" and b.converted is not true");
        sql.append(" and (b.status = '").append(ItemSerialEntityType.APPROVED.name()).append("' OR b.status IS NULL) \n");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (lower(b.serial) LIKE '").append(fp.getSqlSearchKey() + "')");
        }
        if (fp.getSerialNumber() != null && !fp.getSerialNumber().equals("All")) {
            sql.append(" AND (lower(b.serial) = '" + fp.getSerialNumber().toLowerCase() + "')");
        }
        if (fp.getBatchHistoryType() != null && !fp.getBatchHistoryType().equals("All")) {
            sql.append(" AND (lower(b.batch_type) = '" + fp.getBatchHistoryType().toLowerCase() + "')");
        }
        if (fp.getWarehouseId() != null) {
            sql.append(" AND b.warehouse_id = " + fp.getWarehouseId());
        }
        if (fp.getFromExpiryDate() != null) {
            sql.append(" AND b.expiry_date >= '" + fp.getFromExpiryDate() + "'");
        }
        if (fp.getToExpiryDate() != null) {
            sql.append(" AND b.expiry_date <= '" + fp.getToExpiryDate() + "'");
        }
        sql.append(" group by b.serial, b.expiry_date, b.entity_type, b.batch_type, b.entity_id, b.warehouse_id \n");
        if (fp.getSortField() != null) {
            sql.append(" ORDER BY ");
            if ("number".equals(fp.getSortField())) {
                sql.append(" b.serial ");
            } else if ("expiryDate".equals(fp.getSortField())) {
                sql.append(" b.expiry_date ");
            }
            if (fp.getSortDir() != null && fp.getSortDir() == 2) {
                sql.append(" DESC ");
            }
        } else {
            sql.append(" ORDER BY b.serial DESC ");
        }

        if (fp.getLimit() > 0) {
            sql.append(" OFFSET " + fp.getStart() + " LIMIT " + fp.getLimit() + " ");
        }
        List<Object> list = findNative(sql.toString());

        List<EdsItemBatch> edsItemBatches = Lists.newArrayList();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Object[] it = (Object[]) list.get(i);
                String serial = (String) it[0];
                Date expiryDate = (Date) it[1];
                String entityType = (String) it[2];
                String batchType = (String) it[3];
                Integer entityId = (Integer) it[4];
                Integer warehouseId = (Integer) it[5];
                BigDecimal qyt = (BigDecimal) it[6];

                EdsItemBatch edsItemBatch = new EdsItemBatch();
                edsItemBatch.setSerial(serial);
                edsItemBatch.setExpiryDate(expiryDate);
                edsItemBatch.setEntityType(entityType);
                edsItemBatch.setBatchType(batchType);
                edsItemBatch.setEntityId(entityId);
                EdsWarehouse warehouse = warehouseManager.getDefaultWarehouse();
                if (warehouseId != null) {
                    warehouse = warehouseManager.get(warehouseId);
                }
                edsItemBatch.setWarehouse(warehouse);
                edsItemBatch.setQty(qyt);

                edsItemBatches.add(edsItemBatch);
            }
        }
        return edsItemBatches;
    }

    @Override
    public void updateStockTransferBatchItemsStatus(Integer objectId) {

        updateNative("update " + getCompanyId() + ".item_batch " +
                " set status='" + ItemSerialEntityType.APPROVED.name() + "' " +
                " where entity_id = '" + objectId + "' " +
                " and (entity_type='" + ItemSerialEntityType.STOCK_TRANSFER_IN.name() + "' or entity_type='" + ItemSerialEntityType.STOCK_TRANSFER_OUT.name() + "')");
    }

    @Override
    public List<ProductTrackBatchItem> getBatchesForOut(Integer entityId, String entityType) {
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder("SELECT bt.serial, bt.expiry_date, bt.warehouse_id, bt.item_id, sum(bt.qty) as qty");
        sql.append(" FROM ").append(companyID).append(".item_batch bt \n");
        sql.append(" WHERE bt.entity_id = ").append(entityId);
        sql.append(" and bt.entity_type='").append(entityType).append("' ");
        sql.append(" group by bt.serial, bt.expiry_date, bt.warehouse_id, bt.item_id");

        List<Object> list = findNative(sql.toString());
        List<ProductTrackBatchItem> resultList = new ArrayList<>(list.size());
        if (list != null && !list.isEmpty()) {
            int i = 0;
            for (Object item : list) {
                i++;
                Object[] it = (Object[]) item;
                String serial = (String) it[0];
                Date expiryDate = (Date) it[1];
                Integer warehouseId = (Integer) it[2];
                Integer itemId = (Integer) it[3];
                BigDecimal qyt = (BigDecimal) it[4];

                ProductTrackBatchItem batchItem = new ProductTrackBatchItem();
                batchItem.setObjectID(i);
                batchItem.setSerial(serial);
                batchItem.setExpirationDate(expiryDate);
                batchItem.setWarehouseId(warehouseId);
                batchItem.setItemID(itemId);
                batchItem.setQty(qyt);
                resultList.add(batchItem);
            }
        }
        return resultList;
    }

    @Override
    public BigDecimal getOnHandQtyByBatchItem(ProductTrackBatchItem batchItem) {
        StringBuilder sql = new StringBuilder(" SELECT sum(case when bt.batch_type='IN' then bt.qty else (-1)* bt.qty end) ");
        sql.append(" FROM ").append(getCompanyId()).append(".item_batch bt \n");
        sql.append(" WHERE bt.serial = '").append(batchItem.getSerial()).append("' \n");
        if (batchItem.getExpirationDate() != null) {
            sql.append(" and bt.expiry_date='").append(batchItem.getExpirationDate()).append("' ");
        } else {
            sql.append(" and bt.expiry_date is null");
        }
        sql.append(" and bt.warehouse_id=").append(batchItem.getWarehouseId());
        sql.append(" and bt.item_id=").append(batchItem.getItemID());
        sql.append(" and (bt.status = '").append(ItemSerialEntityType.APPROVED.name()).append("' OR bt.status IS NULL)\n");
        sql.append(" AND bt.converted is not true ").append("\n");
        BigDecimal qty = (BigDecimal) findNativeSingle(sql.toString());
        qty = qty != null ? qty : BigDecimal.ZERO;
        return qty;
    }

    @Override
    public List<EdsItemBatch> getSerialsSeparated(ProductTrackBatchItem batchItem, Integer warehouseId, Integer itemId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM (");
        sql.append("SELECT bt.id, bt.qty - SUM(COALESCE(b.qty,0)) qty FROM " + getCompanyId() + ".item_batch bt \n");
        sql.append("LEFT JOIN (SELECT related_id, COALESCE(sum(qty),0) qty FROM ")
                .append(getCompanyId()).append(".item_batch")
                .append(" WHERE serial = '").append(batchItem.getSerial()).append("' \n")
                .append(" AND warehouse_id = ").append(warehouseId)
                .append(" AND item_id = ").append(itemId)
                .append(" and batch_type='").append(ShippingDataType.OUT.name()).append("' \n")
                .append(" and (status = '").append(ItemSerialEntityType.APPROVED.name()).append("' OR status IS NULL)\n")
                .append(batchItem.getExpirationDate() != null ? " and expiry_date='" + batchItem.getExpirationDate() + "'" : " and expiry_date is null").append("\n")
                .append(" group by related_id) b on b.related_id = bt.id \n");
        sql.append(" WHERE bt.serial = '").append(batchItem.getSerial()).append("' \n");
        sql.append(" AND bt.warehouse_id = ").append(warehouseId);
        sql.append(" AND bt.item_id = ").append(itemId);
        sql.append(" and bt.batch_type='").append(ShippingDataType.IN.name()).append("' \n");
        sql.append(" and (bt.status = '").append(ItemSerialEntityType.APPROVED.name()).append("' OR bt.status IS NULL)\n");
        if (batchItem.getExpirationDate() != null) {
            sql.append(" and bt.expiry_date='").append(batchItem.getExpirationDate()).append("' \n");
        } else {
            sql.append(" and bt.expiry_date is null \n");
        }
        sql.append(" AND bt.converted is not true \n");
        sql.append(" group by bt.id ");
        sql.append(" order by bt.id) t where  qty > 0 ");
        List<Object> list = findNative(sql.toString());

        List<EdsItemBatch> resultList = new ArrayList<>(list.size());
        if (list != null && !list.isEmpty()) {
            int i = 0;
            for (Object item : list) {
                i++;
                Object[] it = (Object[]) item;
                Integer id = (Integer) it[0];
                BigDecimal qyt = (BigDecimal) it[1];

                EdsItemBatch edsItemBatch = new EdsItemBatch();
                edsItemBatch.setObjectID(id);
                edsItemBatch.setQty(qyt);
                resultList.add(edsItemBatch);
            }
        }
        return resultList;
    }

    /**
     * Batch version of getBatchItems / getBatches.
     * Key: lineItemId (invoiceItem.id)
     */
    @Override
    public Map<Integer, List<ProductTrackBatchItem>> getBatchItemsByInvoiceItemIds(
            Set<Integer> invoiceItemIds,
            Set<Integer> itemIds,
            Integer entityId,
            String entityType) {

        if (invoiceItemIds == null || invoiceItemIds.isEmpty()
                || itemIds == null || itemIds.isEmpty()
                || entityId == null || entityType == null) {
            return Collections.emptyMap();
        }

        String sql = """
                SELECT b.line_item_id, b.item_id, b.serial, b.expiry_date, sum(b.qty)
                FROM %1$s.item_batch b
                WHERE b.line_item_id IN (%2$s)
                  AND b.item_id IN (%3$s)
                  AND b.entity_id = %4$d
                  AND b.entity_type = '%5$s'
                GROUP BY b.line_item_id, b.item_id, b.serial, b.expiry_date
                ORDER BY b.line_item_id
                """.formatted(
                getCompanyId(),
                invoiceItemIds.stream().map(String::valueOf).collect(Collectors.joining(",")),
                itemIds.stream().map(String::valueOf).collect(Collectors.joining(",")),
                entityId,
                entityType
        );

        List<Object[]> rows = findNative(sql);
        Map<Integer, List<ProductTrackBatchItem>> result = new HashMap<>();

        if (rows != null) {
            for (Object[] row : rows) {
                Integer lineItemId = (Integer) row[0];
                Integer itemId = (Integer) row[1];

                ProductTrackBatchItem item = new ProductTrackBatchItem();
                item.setObjectID(lineItemId);
                item.setSerial((String) row[2]);
                item.setExpirationDate((Date) row[3]);
                item.setQty((BigDecimal) row[4]);
                item.setItemID(itemId);

                result.computeIfAbsent(lineItemId, k -> new ArrayList<>()).add(item);
            }
        }
        return result;
    }
}
