package com.edatasite.workforce.gwt.invoice.client.rpc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProductTrackBatchItem implements Serializable {

    private static final long serialVersionUID = -23125231231L;

    public static final String NUMBER = "number";
    public static final String EXPIRY_DATE = "expiryDate";
    public static final String QTY = "qty";
    public static final String TYPE = "type";
    public static final String RELATED = "related";
    public static final String RELATED_TO = "relatedTo";
    public static final String WAREHOUSE = "warehouse";
    private Integer objectID;
    private String serial;
    private Date expirationDate;
    private BigDecimal balanceInbatch;
    private BigDecimal qty;
    private BigDecimal cost;
    private Integer itemID;
    private String batchType;
    private Integer entityId;
    private String entityType;
    private String relatedTo;
    private String link;
    private Integer warehouseId;
    private String warehouseName;

    public ProductTrackBatchItem() {
    }


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BigDecimal getBalanceInbatch() {
        return balanceInbatch;
    }

    public void setBalanceInbatch(BigDecimal balanceInbatch) {
        this.balanceInbatch = balanceInbatch;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(String relatedTo) {
        this.relatedTo = relatedTo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
