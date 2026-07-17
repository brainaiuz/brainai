package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/12/12
 * Time: 6:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductSelectItem extends SelectItem {
    private Integer productType;
    private Boolean isPurchasedFromSupplier;
    private Boolean hasInventoryInProductKit;
    private Integer brandId;
    private String brandName;
    private BigDecimal originalPrice;
    private BigDecimal qtyOnHand;
    private Boolean inventoryTrackingEnabled;
    private Boolean batchTrackingEnabled;
    private Integer expItemId;
    private BigDecimal discountAmount;
    private Integer discountType;
    private ArrayList<Integer> locationIds;
    private boolean active;
    private Integer warehouseId;

    public ProductSelectItem() {
    }

    public ProductSelectItem(Integer id, String name) {
        super(id, name);
    }

    public ProductSelectItem(Integer id, String name, Integer productType, Boolean purchasedFromSupplier) {
        super(id, name);
        this.productType = productType;
        isPurchasedFromSupplier = purchasedFromSupplier;
    }

    public ProductSelectItem(Integer id, String name, Integer productType, Boolean purchasedFromSupplier, Integer brandID, String brandName, BigDecimal originalPrice) {
        super(id, name);
        this.productType = productType;
        isPurchasedFromSupplier = purchasedFromSupplier;
        this.brandId = brandID;
        this.brandName = brandName;
        this.originalPrice = originalPrice;
    }

    public ProductSelectItem(Integer id, String name, String description, Integer productType, Boolean purchasedFromSupplier) {
        super(id, name, description);
        this.productType = productType;
        isPurchasedFromSupplier = purchasedFromSupplier;
    }

    public ProductSelectItem(Integer id, String name,String description, Integer productType, Boolean purchasedFromSupplier, Integer brandID, String brandName, BigDecimal originalPrice) {
        super(id, name, description);
        this.productType = productType;
        isPurchasedFromSupplier = purchasedFromSupplier;
        this.brandId = brandID;
        this.brandName = brandName;
        this.originalPrice = originalPrice;
    }

    public ProductSelectItem(Integer id, String name, Integer productType, Boolean purchasedFromSupplier, Boolean hasInventoryInProductKit) {
        super(id, name);
        this.productType = productType;
        this.hasInventoryInProductKit = hasInventoryInProductKit;
        isPurchasedFromSupplier = purchasedFromSupplier;
    }


    public ProductSelectItem(Integer id, String name, Integer productType, Boolean purchasedFromSupplier, Boolean hasInventoryInProductKit, Integer brandID, String brandName, BigDecimal originalPrice) {
        super(id, name);
        this.productType = productType;
        this.hasInventoryInProductKit = hasInventoryInProductKit;
        isPurchasedFromSupplier = purchasedFromSupplier;
        this.brandId = brandID;
        this.brandName = brandName;
        this.originalPrice = originalPrice;
    }

    public BigDecimal getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(BigDecimal qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Boolean isPurchasedFromSupplier() {
        return isPurchasedFromSupplier != null ? isPurchasedFromSupplier : false;
    }

    public void setPurchasedFromSupplier(Boolean purchasedFromSupplier) {
        isPurchasedFromSupplier = purchasedFromSupplier;
    }

    public Boolean isHasInventoryInProductKit() {
        return hasInventoryInProductKit != null ? hasInventoryInProductKit : false;
    }

    public void setHasInventoryInProductKit(Boolean hasInventoryInProductKit) {
        this.hasInventoryInProductKit = hasInventoryInProductKit;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }


    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public Integer getExpItemId() {
        return expItemId;
    }

    public void setExpItemId(Integer expItemId) {
        this.expItemId = expItemId;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount != null ? discountAmount : BigDecimal.ZERO;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public ArrayList<Integer> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(ArrayList<Integer> locationIds) {
        this.locationIds = locationIds;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }
}
