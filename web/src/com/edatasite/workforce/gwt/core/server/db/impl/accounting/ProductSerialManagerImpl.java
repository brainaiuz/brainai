package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsProductSerial;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductSerialManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/8/12
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("productSerialManager")
public class ProductSerialManagerImpl extends BaseManager<EdsProductSerial> implements ProductSerialManager{

    public ProductSerialManagerImpl() {
        super(EdsProductSerial.class);
    }

    public List<EdsProductSerial> getProductSerials(ListingFilterParameter filterParametrs) {
        if(Constants.SALE_INVOICE.equals(filterParametrs.getViewType())){
            return (List<EdsProductSerial>) find("select ps from EdsProductSerial ps where  ps.invoiceItemID is null  and ps.itemID=?", filterParametrs.getItemId());
        }
        if (filterParametrs.getProjectId() != null) {
            if (filterParametrs.isValidSearchKey()) {
                return (List<EdsProductSerial>) find("select ps from EdsProductSerial ps, EdsQuoteItem qi where qi.objectID=ps.orderItemID and ps.invoiceItemID is null and qi.quote.relatedProject.objectID = ? " +
                        " and qi.item.objectID = ? and lower(ps.serial) like ?", filterParametrs.getProjectId(), filterParametrs.getItemId(), filterParametrs.getSqlSearchKey());
            } else {
                return (List<EdsProductSerial>) find("select ps from EdsProductSerial ps, EdsQuoteItem qi where qi.objectID=ps.orderItemID and ps.invoiceItemID is null " +
                        " and qi.quote.relatedProject.objectID = ? and qi.item.objectID = ? ", filterParametrs.getProjectId(), filterParametrs.getItemId());
            }
        } else {
            if (filterParametrs.isValidSearchKey()) {
                return (List<EdsProductSerial>) find("select ps from EdsProductSerial ps, EdsQuoteItem qi where qi.objectID=ps.orderItemID and ps.invoiceItemID is null and qi.item.objectID = ? and lower(ps.serial) like ?",
                        filterParametrs.getItemId(), filterParametrs.getSqlSearchKey());
            } else {
                return (List<EdsProductSerial>) find("select ps from EdsProductSerial ps, EdsQuoteItem qi where qi.objectID=ps.orderItemID and ps.invoiceItemID is null and qi.item.objectID = ? ", filterParametrs.getItemId());
            }
        }
    }

    @Override
    public List<Integer> getProductSerialsByPurchaseOrderItems(List<Integer> orderItems) {
        if (orderItems != null && orderItems.size() > 0) {
            StringBuilder idsAsString = new StringBuilder();
            int i = 0;
            for (Integer id : orderItems) {
                if (i != 0)
                    idsAsString.append(", ");
                idsAsString.append(id.toString());
                i++;
            }
            return (List<Integer>) find("select ps.objectID from EdsProductSerial ps where ps.orderItemID in (" + idsAsString + ")");
        }
        return new LinkedList<>();
    }

    @Override
    public List<Integer> getProductSerialsBySalesInvoiceItems(List<Integer> invoiceItemsDeleted) {
        StringBuilder idsAsString = new StringBuilder();
        int i=0;
        for (Integer id : invoiceItemsDeleted) {
            if (i != 0)
                idsAsString.append(", ");
            idsAsString.append(id.toString());
            i++;
        }
        if (idsAsString.toString().isEmpty()) {
            return null;
        }

        return (List<Integer>) find("select ps.objectID from EdsProductSerial ps where ps.invoiceItemID in (" + idsAsString + ")");
    }

    @Override
    public ProductSerialItem[] getOrderItemSerialsAsSelectItem(Integer orderItemID) {
        List<EdsProductSerial> psList = (List<EdsProductSerial>)find("select ps from EdsProductSerial ps where ps.orderItemID = ? order by ps.objectID", orderItemID);
        if (psList.size() > 0) {
            ProductSerialItem[] psItems = new ProductSerialItem[psList.size()];
            int i = 0;
            for (EdsProductSerial ps : psList) {
                psItems[i++] = new ProductSerialItem(ps.getObjectID(), ps.getSerial(), ps.getExpirationDate(), ps.getLotNumber(), ps.getRefNumber());
            }
            return psItems;
        }
        return null;
    }

    @Override
    public List<Integer> getProductSerialsByGDN(List<Integer> gdnIds) {
        StringBuilder idsAsString = new StringBuilder();
        int i = 0;
        for (Integer id : gdnIds) {
            if (id != null) {
                if (i != 0)
                    idsAsString.append(", ");
                idsAsString.append(id);
                i++;
            }
        }
        if (idsAsString.toString().isEmpty()) {
            return new ArrayList<>();
        }

        return (List<Integer>) find("select ps.objectID from EdsProductSerial ps where ps.gdnid in (" + idsAsString + ")");
    }

    @Override
    public ProductSerialItem[] getInvoiceItemSerialsAsSelectItem(Integer invoiceItemID) {
        List<EdsProductSerial> psList = (List<EdsProductSerial>)find("select ps from EdsProductSerial ps where ps.invoiceItemID = ? order by ps.objectID", invoiceItemID);
        if (psList.size() > 0) {
            ProductSerialItem[] psItems = new ProductSerialItem[psList.size()];
            int i = 0;
            for (EdsProductSerial ps : psList) {
                psItems[i++] = new ProductSerialItem(ps.getObjectID(), ps.getSerial(), ps.getExpirationDate(), ps.getLotNumber(), ps.getRefNumber());
            }
            return psItems;
        }
        return null;
    }

    @Override
    public List<ProductSerialItem> getInvoiceItemBatchSerials(Integer lineItemID, Integer productId, boolean fromGdn) {
        List<Object[]> psList = new ArrayList<>();
        if (fromGdn) {
            psList = (List<Object[]>) find(
                    "select expirationDate, lotNumber, serial, count(id) \n" +
                            "from  EdsProductSerial ps where ps.gdnid = ? " +
                            "and ps.itemID = ? " +
                            "group by expirationDate, lotNumber, serial ", lineItemID, productId);
        } else {
            psList = (List<Object[]>)find(
                    "select expirationDate, lotNumber, serial, count(id) \n" +
                            "from  EdsProductSerial ps where ps.invoiceItemID = ? " +
                            "and ps.itemID = ? " +
                            "group by expirationDate, lotNumber, serial ", lineItemID, productId);
        }
        if(psList.size()>0){
            List<ProductSerialItem> productSerialItemList = new ArrayList<>(psList.size());
            int i = 0;
            for (Object[] objects : psList) {
                ProductSerialItem serialItem = new ProductSerialItem();
                serialItem.setObjectID(null);
                serialItem.setExpirationDate((Date) objects[0]);
                serialItem.setLotNumber((String) objects[1]);
                serialItem.setSerial((String) objects[2]);

                serialItem.setQty(BigDecimal.valueOf(Double.parseDouble(((Long) objects[3]).toString())));
                productSerialItemList.add(serialItem);
                i++;
            }
            return productSerialItemList;
        }
        return null;
    }

    @Override
    public void removeSalesInvoiceFromProductSerials(Integer salesInvoiceID) {
        List<EdsProductSerial> psList = (List<EdsProductSerial>) find("select ps from EdsProductSerial ps, EdsInvoiceItem ii where ii.objectID=ps.invoiceItemID and ii.invoice.objectID = ?", salesInvoiceID);
        for (EdsProductSerial ps : psList) {
            ps.setInvoiceItemID(null);
            update(ps);
        }
    }

    @Override
    public ArrayList<ProductSerialItem> getProductSerialsByItemID(Integer itemID){
        List<EdsProductSerial> psList = (List<EdsProductSerial>)find("select ps from EdsProductSerial ps where ps.itemID = ? order by ps.objectID ", itemID);
        if(psList.size()>0){
            ArrayList<ProductSerialItem> productSerialItemList = new ArrayList<>(psList.size());
            for(EdsProductSerial productSerial: psList){
                ProductSerialItem serialItem = new ProductSerialItem();
                serialItem.setObjectID(productSerial.getObjectID());
                serialItem.setItemID(productSerial.getItemID());
                serialItem.setInvoiceID(productSerial.getInvoiceItemID());
                serialItem.setSerial(productSerial.getSerial());
                serialItem.setLotNumber(productSerial.getLotNumber());
                serialItem.setRefNumber(productSerial.getRefNumber());
                serialItem.setExpirationDate(productSerial.getExpirationDate());
                productSerialItemList.add(serialItem);
            }
            return productSerialItemList;
        }
        return null;
    }

    @Override
    public ArrayList<ProductSerialItem> getProductSerialsWithQtyByItemID(Integer itemID){
        List<Object[]> psList = (List<Object[]>)find(
                "select distinct itemID, serial, invoiceItemID, lotNumber, refNumber, expirationDate, count(id) \n" +
                        "from  EdsProductSerial ps where ps.itemID = ? group by itemID, serial, invoiceItemID, lotNumber, refNumber, expirationDate ", itemID);
        if(psList.size()>0){
            ArrayList<ProductSerialItem> productSerialItemList = new ArrayList<>(psList.size());
            for (Object[] anInResult : psList) {
                ProductSerialItem serialItem = new ProductSerialItem();
                serialItem.setObjectID(null);
                serialItem.setItemID((Integer)anInResult[0]);
                serialItem.setSerial((String) anInResult[1]);
                serialItem.setInvoiceID((Integer) anInResult[2] );
                serialItem.setLotNumber((String) anInResult[3]);
                serialItem.setRefNumber((String) anInResult[4]);
                serialItem.setExpirationDate((Date) anInResult[5]);
                serialItem.setQty(BigDecimal.valueOf(Double.parseDouble(((Long) anInResult[6]).toString())));
                productSerialItemList.add(serialItem);
            }
            return productSerialItemList;
        }
        return null;
    }

    @Override
    public Integer getProductSerialsQty(Integer itemID, String serialNumber, Date expirationDate){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(ps.objectID) from EdsProductSerial ps ");
        sql.append("WHERE ps.invoiceItemID is null ");
        sql.append("AND ps.itemID=" + itemID + " ");
        sql.append("AND ps.serial='" + serialNumber.split(" ")[0] + "' ");
        sql.append("AND ps.expirationDate='" + expirationDate + "' ");

        return ((Long) findSingle(sql.toString())).intValue();
    }

    @Override
    public List<EdsProductSerial> getProductSerialsByCount(Integer itemID, EdsProductSerial edsProductSerial, Integer count){
        final String sql = "SELECT * FROM " + getCompanyId() + ".productserial ps " +
                " WHERE ps.invoiceItemID is null " +
                " and ps.itemID = :itemID" +
                " and ps.serial = :serial" +
                " and ps.expirationDate = :expirationdate" +
                " ORDER BY ps.expirationDate asc" ;

        return this.slaveEntityManager.createNativeQuery(sql, EdsProductSerial.class)
                .setParameter("itemID", itemID)
                .setParameter("serial", edsProductSerial.getSerial())
                .setParameter("expirationdate", edsProductSerial.getExpirationDate())
                .setMaxResults(count)
                .getResultList();
    }

    @Override
    public ProductSerialItem getFirstProductSerialByIds(Integer orderItemId, Integer itemId, String type) {
        if ((type == null || type.isEmpty()) || orderItemId == null || itemId == null) {
            return null;
        }
        String sql = getProductSerialByIdsQuery(type);

        EdsProductSerial edsSerial = (EdsProductSerial) findSingle(sql, itemId, orderItemId);
        if (edsSerial != null) {
            return edsSerial.getAsRPC();
        }
        return null;
    }

    @Override
    public List<ProductSerialItem> getProductSerialByIds(Integer orderItemId, Integer itemId, String type) {
        if ((type == null || type.isEmpty()) || orderItemId == null || itemId == null) {
            return null;
        }
        String sql = getProductSerialByIdsQuery(type);

        List<EdsProductSerial> edsSerials = (List<EdsProductSerial>) find(sql, itemId, orderItemId);
        if (edsSerials != null && edsSerials.size() > 0) {
            List<ProductSerialItem> result = new ArrayList<>();
            for(EdsProductSerial productSerial: edsSerials){
                ProductSerialItem serialItem = new ProductSerialItem();
                serialItem.setObjectID(productSerial.getObjectID());
                serialItem.setItemID(productSerial.getItemID());
                serialItem.setInvoiceID(productSerial.getInvoiceItemID());
                serialItem.setSerial(productSerial.getSerial());
                serialItem.setLotNumber(productSerial.getLotNumber());
                serialItem.setRefNumber(productSerial.getRefNumber());
                serialItem.setExpirationDate(productSerial.getExpirationDate());
                result.add(serialItem);
            }
            return result;
        }
        return null;
    }

    private String getProductSerialByIdsQuery(String type) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT ps FROM EdsProductSerial ps ");
        sqlQuery.append(" WHERE ps.itemID = ? AND ");
        if (Constants.PURCHASE_ORDER.equals(type)) {
            sqlQuery.append(" ps.orderItemID = ? ");
        } else {
            sqlQuery.append(" ps.invoiceItemID = ? ");
        }
        sqlQuery.append(" ORDER BY ps.objectID ");

        return sqlQuery.toString();
    }

    @Override
    public void removePurchaseOrderProductSerials(Integer purchaseOrderId) {
        if (purchaseOrderId == null) {
            return;
        }
        final String sql = "DELETE FROM " + getCompanyId() + ".productserial ps " +
                           "    WHERE ps.id IN (" +
                           "                SELECT DISTINCT ps.id FROM " + getCompanyId() + ".purchaseorder po " +
                           "                  JOIN " + getCompanyId() + ".quote q ON po.id = q.id " +
                           "                  JOIN " + getCompanyId() + ".quoteitem qi ON q.id = qi.quote_id" +
                           "                  JOIN " + getCompanyId() + ".productserial ps ON ps.orderitemid = qi.id " +
                           "                                                AND ps.itemid = qi.item_id " +
                           "                    WHERE (q.deleted IS NULL OR q.deleted = FALSE) " +
                           "                        AND (qi.deleted IS NULL OR qi.deleted = FALSE) " +
                           "                        AND po.id = :purchaseOrderId)";

        this.masterEntityManager.createNativeQuery(sql)
                          .setParameter("purchaseOrderId", purchaseOrderId)
                          .executeUpdate();
    }

    @Override
    public void removeGrnSerialNumbers(Integer grnId) {
        if (grnId == null) {
            return;
        }
        final String sql = "DELETE FROM " + getCompanyId() + ".productserial ps " +
                           " WHERE ps.grnid = :grnId " +
                           " AND (ps.invoiceitemid IS NULL OR ps.gdnid IS NULL)";

        this.masterEntityManager.createNativeQuery(sql)
                          .setParameter("grnId", grnId)
                          .executeUpdate();
    }

    /**
     * Batch version of getFirstProductSerialByIds.
     * Returns the first serial per (invoiceItemId/orderItemId, itemId) pair.
     * Key: invoiceItem.id (line item id)
     */
    @Override
    public Map<Integer, ProductSerialItem> getFirstSerialsByInvoiceItemIds(
            Set<Integer> invoiceItemIds,
            Set<Integer> itemIds,
            String type) {

        if (invoiceItemIds == null || invoiceItemIds.isEmpty()
                || itemIds == null || itemIds.isEmpty()
                || type == null || type.isEmpty()) {
            return Collections.emptyMap();
        }

        String idColumn = Constants.PURCHASE_ORDER.equals(type) ? "ps.orderitemid" : "ps.invoiceitemid";

        String sql = """
                SELECT DISTINCT ON (%1$s) 
                    ps.id, ps.orderitemid, ps.invoiceitemid, ps.itemid,
                    ps.serial, ps.lotnumber, ps.refnumber, ps.expirationdate
                FROM %2$s.productserial ps
                WHERE %1$s IN (%3$s)
                  AND ps.itemid IN (%4$s)
                ORDER BY %1$s, ps.id ASC
                """.formatted(
                idColumn,
                getCompanyId(),
                invoiceItemIds.stream().map(String::valueOf).collect(Collectors.joining(",")),
                itemIds.stream().map(String::valueOf).collect(Collectors.joining(","))
        );

        List<Object[]> rows = findNative(sql);

        Map<Integer, ProductSerialItem> result = new HashMap<>();
        if (rows != null) {
            for (Object[] row : rows) {
                Integer lineItemId = (Integer) row[Constants.PURCHASE_ORDER.equals(type) ? 1 : 2];
                ProductSerialItem serialItem = new ProductSerialItem();
                serialItem.setObjectID((Integer) row[0]);
                String serial = (String) row[4];
                Date expDate = (Date) row[7];
                serialItem.setSerial(serial + " (" + expDate + ")");
                serialItem.setExpirationDate(expDate);
                serialItem.setLotNumber((String) row[5]);
                serialItem.setRefNumber((String) row[6]);
                result.put(lineItemId, serialItem);
            }
        }
        return result;
    }

    /**
     * Batch version of getInvoiceItemBatchSerials.
     * When fromGdn=true (shippingData present): groups by gdnid + itemId.
     * When fromGdn=false: groups by invoiceItemId + itemId.
     * Key: invoiceItem.id (line item id)
     */
    @Override
    public Map<Integer, List<ProductSerialItem>> getBatchSerialsByInvoiceItemIds(
            Set<Integer> invoiceItemIds,
            Set<Integer> itemIds,
            Set<EdsShippingData> convertedShippingData) {

        if (invoiceItemIds == null || invoiceItemIds.isEmpty()
                || itemIds == null || itemIds.isEmpty()) {
            return Collections.emptyMap();
        }

        boolean fromGdn = convertedShippingData != null && !convertedShippingData.isEmpty();
        String sql;

        if (fromGdn) {
            List<Integer> shippingDataIds = convertedShippingData.stream()
                    .filter(shd -> !shd.isDeleted())
                    .map(EdsShippingData::getObjectID)
                    .collect(Collectors.toList());

            if (shippingDataIds.isEmpty()) {
                return Collections.emptyMap();
            }

            sql = """
                    SELECT ps.gdnid, ps.expirationdate, ps.lotnumber, ps.serial, count(ps.id)
                    FROM %1$s.productserial ps
                    WHERE ps.gdnid IN (%2$s)
                      AND ps.itemid IN (%3$s)
                    GROUP BY ps.gdnid, ps.expirationdate, ps.lotnumber, ps.serial
                    ORDER BY ps.gdnid
                    """.formatted(
                    getCompanyId(),
                    shippingDataIds.stream().map(String::valueOf).collect(Collectors.joining(",")),
                    itemIds.stream().map(String::valueOf).collect(Collectors.joining(","))
            );
        } else {
            sql = """
                    SELECT ps.invoiceitemid, ps.expirationdate, ps.lotnumber, ps.serial, count(ps.id)
                    FROM %1$s.productserial ps
                    WHERE ps.invoiceitemid IN (%2$s)
                      AND ps.itemid IN (%3$s)
                    GROUP BY ps.invoiceitemid, ps.expirationdate, ps.lotnumber, ps.serial
                    ORDER BY ps.invoiceitemid
                    """.formatted(
                    getCompanyId(),
                    invoiceItemIds.stream().map(String::valueOf).collect(Collectors.joining(",")),
                    itemIds.stream().map(String::valueOf).collect(Collectors.joining(","))
            );
        }

        List<Object[]> rows = findNative(sql);
        Map<Integer, List<ProductSerialItem>> result = new HashMap<>();

        if (rows != null) {
            for (Object[] row : rows) {
                Integer lineItemId = (Integer) row[0]; // gdnid or invoiceitemid
                ProductSerialItem serialItem = new ProductSerialItem();
                serialItem.setObjectID(null);
                serialItem.setExpirationDate((Date) row[1]);
                serialItem.setLotNumber((String) row[2]);
                serialItem.setSerial((String) row[3]);
                serialItem.setQty(BigDecimal.valueOf(
                        Double.parseDouble(((Long) row[4]).toString())
                ));
                result.computeIfAbsent(lineItemId, k -> new ArrayList<>()).add(serialItem);
            }
        }
        return result;
    }
}
