package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductSolrItem implements IsSerializable {

    private Integer objectId;
    private Integer productParentId;
    private String productNumber;
    private String partNumber;
    private String skuNumber;
    private String upsNumber;
    private String subsidiaryProductUniqNum;
    private String productName;
    private String barcode;
    private Boolean productActive;
    private Boolean productStorefrontEnable;
    private Boolean inventoryTrackingEnabled;
    private Boolean trackBatchesEnabled;
    private SelectItem productType;
    private SelectItem account;
    private SelectItem cogsAccount;
    private SelectItem assetAccount;
    private String description;
    private SelectItem taxRate;
    private Double taxEffectiveRate;
    private String vendor;
    private String manufacturer;
    private SelectItem category;
    private SelectItem parentCategory;
    private Double unitPrice;
    private Double costPrice;
    private String averageCost;
    private Double quantityOnHand;
    private List<SelectItem> multiSupplier = new ArrayList<>();
    private List<SelectItem> multiLocation = new ArrayList<>();
    private SelectItem unitMeasurement;
    private Date createdDate;
    private Date updatedDate;
    private SelectItem brand;
    private SelectItem warehouse;
    private Double warehouseStock;
    private SelectItem creator;
    private SelectItem updater;
    private SelectItem productDiscountType;
    private Double productDiscountAmount;
    private ArrayList<NewProductCustomDescription> customDescriptionData;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getProductParentId() {
        return productParentId;
    }

    public void setProductParentId(Integer productParentId) {
        this.productParentId = productParentId;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(String skuNumber) {
        this.skuNumber = skuNumber;
    }

    public String getUpsNumber() {
        return upsNumber;
    }

    public void setUpsNumber(String upsNumber) {
        this.upsNumber = upsNumber;
    }

    public String getSubsidiaryProductUniqNum() {
        return subsidiaryProductUniqNum;
    }

    public void setSubsidiaryProductUniqNum(String subsidiaryProductUniqNum) {
        this.subsidiaryProductUniqNum = subsidiaryProductUniqNum;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Boolean getProductActive() {
        return productActive;
    }

    public void setProductActive(Boolean productActive) {
        this.productActive = productActive;
    }

    public Boolean getProductStorefrontEnable() {
        return productStorefrontEnable;
    }

    public void setProductStorefrontEnable(Boolean productStorefrontEnable) {
        this.productStorefrontEnable = productStorefrontEnable;
    }

    public Boolean getInventoryTrackingEnabled() {
        return inventoryTrackingEnabled;
    }

    public void setInventoryTrackingEnabled(Boolean inventoryTrackingEnabled) {
        this.inventoryTrackingEnabled = inventoryTrackingEnabled;
    }

    public Boolean getTrackBatchesEnabled() {
        return trackBatchesEnabled;
    }

    public void setTrackBatchesEnabled(Boolean trackBatchesEnabled) {
        this.trackBatchesEnabled = trackBatchesEnabled;
    }

    public SelectItem getProductType() {
        return productType;
    }

    public void setProductType(SelectItem productType) {
        this.productType = productType;
    }

    public SelectItem getAccount() {
        return account;
    }

    public void setAccount(SelectItem account) {
        this.account = account;
    }

    public SelectItem getCogsAccount() {
        return cogsAccount;
    }

    public void setCogsAccount(SelectItem cogsAccount) {
        this.cogsAccount = cogsAccount;
    }

    public SelectItem getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(SelectItem assetAccount) {
        this.assetAccount = assetAccount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SelectItem getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(SelectItem taxRate) {
        this.taxRate = taxRate;
    }

    public Double getTaxEffectiveRate() {
        return taxEffectiveRate;
    }

    public void setTaxEffectiveRate(Double taxEffectiveRate) {
        this.taxEffectiveRate = taxEffectiveRate;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public SelectItem getCategory() {
        return category;
    }

    public void setCategory(SelectItem category) {
        this.category = category;
    }

    public SelectItem getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(SelectItem parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public String getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(String averageCost) {
        this.averageCost = averageCost;
    }

    public Double getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(Double quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public List<SelectItem> getMultiSupplier() {
        return multiSupplier;
    }

    public void setMultiSupplier(List<SelectItem> multiSupplier) {
        this.multiSupplier = multiSupplier;
    }

    public List<SelectItem> getMultiLocation() {
        return multiLocation;
    }

    public void setMultiLocation(List<SelectItem> multiLocation) {
        this.multiLocation = multiLocation;
    }

    public SelectItem getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(SelectItem unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public SelectItem getBrand() {
        return brand;
    }

    public void setBrand(SelectItem brand) {
        this.brand = brand;
    }

    public SelectItem getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(SelectItem warehouse) {
        this.warehouse = warehouse;
    }

    public Double getWarehouseStock() {
        return warehouseStock;
    }

    public void setWarehouseStock(Double warehouseStock) {
        this.warehouseStock = warehouseStock;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getUpdater() {
        return updater;
    }

    public void setUpdater(SelectItem updater) {
        this.updater = updater;
    }

    public SelectItem getProductDiscountType() {
        return productDiscountType;
    }

    public void setProductDiscountType(SelectItem productDiscountType) {
        this.productDiscountType = productDiscountType;
    }

    public Double getProductDiscountAmount() {
        return productDiscountAmount;
    }

    public void setProductDiscountAmount(Double productDiscountAmount) {
        this.productDiscountAmount = productDiscountAmount;
    }

    public ArrayList<NewProductCustomDescription> getCustomDescriptionData() {
        return customDescriptionData;
    }

    public void setCustomDescriptionData(ArrayList<NewProductCustomDescription> customDescriptionData) {
        this.customDescriptionData = customDescriptionData;
    }
}
