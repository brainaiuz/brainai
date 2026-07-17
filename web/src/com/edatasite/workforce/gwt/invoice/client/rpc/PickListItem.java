package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 23, 2010
 * Time: 2:59:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class PickListItem implements IsSerializable {

    private Integer objectID;
    private Integer pickListID;
//    private String skuNumber;
//    private String pickingNumber;
    private Integer itemID;
    private String itemName;
    private String itemNumber;
    private SelectItem warehouse;
    private BigDecimal qty;
    private String box;
    private Integer carrierID;
    private String carrierName;
    private String carrierAccount;
    private BigDecimal picked;
    private BigDecimal packed;
    private BigDecimal shipped;
    private BigDecimal shippedQty;
    private DateNonConvertable shippedDate;
    private DateNonConvertable expectedDate;
    private String description;
    private String reference;
    private BigDecimal numberOfPacks;
    private BigDecimal qtyPerPack;
    private Integer itemType;
    private ArrayList<String> serials;
    private Boolean inventoryTrackingEnabled;
    private Boolean batchTrackingEnabled;
    private Boolean trackBatchesEnabled;
    private ArrayList<ProductTrackBatchItem> assignedBatchItems;
    private CompanyCustomFieldItem articleNumberCF;
    private BigDecimal readyToShip;
    private BigDecimal bookReserve;

    public PickListItem() {
    }

    public Integer getPickListID() {
        return pickListID;
    }

    public void setPickListID(Integer pickListID) {
        this.pickListID = pickListID;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public SelectItem getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(SelectItem warehouse) {
        this.warehouse = warehouse;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public Integer getCarrierID() {
        return carrierID;
    }

    public void setCarrierID(Integer carrierID) {
        this.carrierID = carrierID;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarrierAccount() {
        return carrierAccount;
    }

    public void setCarrierAccount(String carrierAccount) {
        this.carrierAccount = carrierAccount;
    }

    public BigDecimal getPacked() {
        return packed;
    }

    public void setPacked(BigDecimal packed) {
        this.packed = packed;
    }

    public BigDecimal getShipped() {
        return shipped;
    }

    public void setShipped(BigDecimal shipped) {
        this.shipped = shipped;
    }

    public DateNonConvertable getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(DateNonConvertable shippedDate) {
        this.shippedDate = shippedDate;
    }

    public BigDecimal getPicked() {
        return picked;
    }

    public void setPicked(BigDecimal picked) {
        this.picked = picked;
    }

    public DateNonConvertable getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(DateNonConvertable expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getNumberOfPacks() {
        return numberOfPacks;
    }

    public void setNumberOfPacks(BigDecimal numberOfPacks) {
        this.numberOfPacks = numberOfPacks;
    }

    public BigDecimal getQtyPerPack() {
        return qtyPerPack;
    }

    public void setQtyPerPack(BigDecimal qtyPerPack) {
        this.qtyPerPack = qtyPerPack;
    }

    public BigDecimal getShippedQty() {
        return shippedQty != null ? shippedQty : BigDecimal.ZERO;
    }

    public void setShippedQty(BigDecimal shippedQty) {
        this.shippedQty = shippedQty;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getItemType() {
        return itemType;
    }

    public ArrayList<String> getSerials() {
        return serials;
    }

    public void setSerials(ArrayList<String> serials) {
        this.serials = serials;
    }

    public Boolean getInventoryTrackingEnabled() {
        return inventoryTrackingEnabled != null ? inventoryTrackingEnabled : false;
    }

    public void setInventoryTrackingEnabled(Boolean inventoryTrackingEnabled) {
        this.inventoryTrackingEnabled = inventoryTrackingEnabled;
    }

    public Boolean getBatchTrackingEnabled() {
        return batchTrackingEnabled != null ? batchTrackingEnabled : false;
    }

    public void setBatchTrackingEnabled(Boolean batchTrackingEnabled) {
        this.batchTrackingEnabled = batchTrackingEnabled;
    }

    public Boolean getTrackBatchesEnabled() {
        return trackBatchesEnabled != null ? trackBatchesEnabled : false;
    }

    public void setTrackBatchesEnabled(Boolean trackBatchesEnabled) {
        this.trackBatchesEnabled = trackBatchesEnabled;
    }

    public ArrayList<ProductTrackBatchItem> getAssignedBatchItems() {
        return assignedBatchItems;
    }

    public void setAssignedBatchItems(ArrayList<ProductTrackBatchItem> assignedBatchItems) {
        this.assignedBatchItems = assignedBatchItems;
    }

    public CompanyCustomFieldItem getArticleNumberCF() {
        return articleNumberCF;
    }

    public void setArticleNumberCF(CompanyCustomFieldItem articleNumberCF) {
        this.articleNumberCF = articleNumberCF;
    }

    public BigDecimal getReadyToShip() {
        return readyToShip;
    }

    public void setReadyToShip(BigDecimal readyToShip) {
        this.readyToShip = readyToShip;
    }

    public BigDecimal getBookReserve() {
        return this.bookReserve;
    }

    public void setBookReserve(final BigDecimal bookReserve) {
        this.bookReserve = bookReserve;
    }
}
