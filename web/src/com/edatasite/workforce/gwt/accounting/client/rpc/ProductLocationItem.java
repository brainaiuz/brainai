package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 15, 2010
 * Time: 2:06:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProductLocationItem implements Serializable {
    public static final String MINQTY = "minqty";
    public static final String QTY = "qty";
    public static final String LOCATION = "location";
    public static final String NAME = "name";
    public static final String NUMBER = "number";
    public static final String ACTION = "action";
    public static final String AVERAGE_COST = "averagecost";
    public static final String TOTAL = "total";
    private Integer objectID;
    private BigDecimal qty = BigDecimal.ZERO;
    private String pickingNumber;
    private BigDecimal minReorderQty = new BigDecimal(1);
    private BigDecimal minReorderPoint = new BigDecimal(1);
    private Integer warehouseID;
    private String warehouseName;
    private Integer productLocationID;
    private String productLocationName;
    private String productLocationDescription;
    private Integer productID;
    private String productName;
    private Date shipDate;
    private BigDecimal averageCost;
    private ArrayList<String> serials;
    private ArrayList<ProductTrackBatchItem> trackBatchItems;
    private String product_number;
    public ProductLocationItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getPickingNumber() {
        return pickingNumber;
    }

    public void setPickingNumber(String pickingNumber) {
        this.pickingNumber = pickingNumber;
    }

    public BigDecimal getMinReorderQty() {
        return minReorderQty;
    }

    public void setMinReorderQty(BigDecimal minReorderQty) {
        this.minReorderQty = minReorderQty;
    }

    public BigDecimal getMinReorderPoint() {
        return minReorderPoint;
    }

    public void setMinReorderPoint(BigDecimal minReorderPoint) {
        this.minReorderPoint = minReorderPoint;
    }

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Integer warehouseID) {
        this.warehouseID = warehouseID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Integer getProductLocationID() {
        return productLocationID;
    }

    public void setProductLocationID(Integer productLocationID) {
        this.productLocationID = productLocationID;
    }

    public String getProductLocationName() {
        return productLocationName;
    }

    public void setProductLocationName(String productLocationName) {
        this.productLocationName = productLocationName;
    }

    public String getProductLocationDescription() {
        return productLocationDescription;
    }

    public void setProductLocationDescription(String productLocationDescription) {
        this.productLocationDescription = productLocationDescription;
    }

    public BigDecimal getTotal() {
        if (averageCost != null && qty != null) {
            return qty.multiply(averageCost);
        }
        return null;
    }

    public BigDecimal getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost;
    }

    public ArrayList<String> getSerials() {
        return serials;
    }

    public void setSerials(ArrayList<String> serials) {
        this.serials = serials;
    }

    public ArrayList<ProductTrackBatchItem> getTrackBatchItems() {
        return trackBatchItems;
    }

    public void setTrackBatchItems(ArrayList<ProductTrackBatchItem> trackBatchItems) {
        this.trackBatchItems = trackBatchItems;
    }

    public String getProduct_number() {
        return product_number;
    }

    public void setProduct_number(String product_number) {
        this.product_number = product_number;
    }
}
