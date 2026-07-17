package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Aug 13, 2009
 * Time: 6:18:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductItem implements IsSerializable, ListingCustomFields {

    public static final String ACTION = "action";
    public static final String PRODUCT_NUMBER = "productNumber";
    public static final String NAME = "name";
    public static final String DISCRIPTION = "discription";
    public static final String STATUS = "status";
    public static final String COST_PRICE = "costPrice";
    public static final String SELING_PRICE = "sellingPrice";
    public static final String TYPE = "type";
    public static final String INVENTORY_TYPE = "inventoryType";
    public static final String INCOME_ACCOUND = "incomeAccound";
    public static final String COGS_ACCOUND = "cogsAccound";
    public static final String ASSET_ACCOUND = "assetAccount";
    public static final String ACCOUND = "accound";
    public static final String ITEMS_IN_STOCK = "itemsInStock";
    public static final String ON_PURCHASE_ORDER = "onPurchaseOrder";
    public static final String TAX_RATE = "taxRate";
    public static final String OPENING_BALANCE = "openingBalance";
    public static final String AS_OF = "asOf";
    public static final String SCUNUMBER = "skuNuber";
    public static final String WEREHOUSE = "werehouse";
    public static final String LOCATION = "location";
    public static final String UNITPRICE = "unitPrice";
    public static final String SALE_ORDER_QTY = "saleorderqty";
    public static final String AVAILABLE_STOCK = "available_stock";
    public static final String Vendor = "vendor";
    public static final String Category = "category";
    public static final String IN_BOX = "inbox";
    public static final String TOTAL_SUB_QTY = "totalsubqty";
    public static final String PICTURE = "picture";
    public static final String STOREFRONT = "storefront";
    public static final String PART_NUMBER = "partNumber";
    public static final String TAX_AMOUNT = "taxAmount";
    public static final String AVERAGE_COST = "averageCost";
    public static final String UNIT_MEASUREMENT_NAME = "unitMeasurement";
    public static final String BARCODE = "barcode";
    public static final String BRAND = "brand";
    public static final String CREATOR = "creator";
    public static final String UPDATER = "updater";
    public static final String CREATED_DATE = "createdAt";
    public static final String UPDATED_DATE = "updatedAt";
    public static final String DISCOUNT_TYPE = "discountType";
    public static final String DISCOUNT_AMOUNT = "discountAmount";
    public static final String RENT_STATUS = "rentStatus";

    private Integer objectId;
    private String productNumber;
    private String name;
    private String description;
    private BigDecimal unitpPrice;
    private String unitPrice;
    private BigDecimal costPrice;
    private Integer type;
    private String typeName;
    private BigDecimal itemsInStock;
    private BigDecimal itemsInWarehouse;
    private BigDecimal onSaleOrderQty;
    //    private BigDecimal saleOrderRamaingQty;
    private Integer accountID;
    private String account;
    private String cogsAccount;
    private String assetAccount;
    private String taxRate;
    private Boolean inventoryItem;
    private String warehouseName;
    private Integer warehouseId;
    private String departmentName;
    private Integer departmentId;
    //    private String locationName;
//    private Integer locationId;
    private String brand;
    private String skuNumber;
    private String status;
    private String rentStatus;
    private String barCodeString;
    private String partNumber;
    private BigDecimal minReorderPoint;
    private Boolean storefrontEnable;
    private ProductKitItem[] productKitItems;
    private BigDecimal totalValue;
    private DateNonConvertable asOf;
    private Integer parentId;

    private BigDecimal currentQty;
    private BigDecimal usedQty;
    private BigDecimal newQty;
    private BigDecimal totalQty;
    private boolean deleted;
    private String vendor;
    private String category;
    private Integer categoryId;
    private SelectItem parentCategory;
    private Boolean active;
    private String defaultPictureUrl;
    private String defaultPictureMiniUrl;
    private Integer productRentalItemId;
    private Integer cogsAccountID;
    private Integer assetAccountID;
    private Integer taxRateID;
    private BigDecimal onPurchaseOrder;
    private BigDecimal taxAmount;
    private String upcNumber;
    private String unitMeasurementName;
    private Integer unitMeasurementId;
    private String manufacturer;
    private String subsidiaryProductUniqNum;
    private BigDecimal averageCost;
    private Boolean inventoryTrackingEnabled;
    private Boolean batchTrackingEnabled;//@TODO must be reviewed or renamed because confusing with below field
    private Boolean trackBatchesEnabled;
    private ArrayList<ProductTrackBatchItem> batchItems;
    private ArrayList<ProductTrackBatchItem> assignedBatchItems;
    private ArrayList<String> serials;
    private ArrayList<String> assignedSerials;
    private boolean productSerialsEnabled;
    private ArrayList<ProductSerialItem> productSerialItems;

    private ArrayList<NewProductCustomDescription> subItemsData;//Customisation

    /* for Assembly Item */
    private Boolean built;

    private ArrayList<CompanyCustomFieldItem> categoryCustomFieldItems;
    private ArrayList<CompanyCustomFieldItem> productCustomFieldItems;
    private HashMap<String, Object> cusFieldMap;
    private ArrayList<AssemblyItem> assemblyItems;
    private Integer projectID;
    private String projectName;
    private HashMap<Integer, ProductItem> qtyByWarehouse;
    private Integer taxAmountId;

    private ProductLocationItem[] productLocations;

    //for stock transfer UI
    private Integer lineItemID;
    private BigDecimal qty;
    private String fromWarehouseName;
    private Integer fromWarehouseId;
    private String toWarehouseName;
    private String fromAccountName;
    private String toAccountName;
    private ArrayList<WarehouseItem> inWarehouseList;
    private LinkedHashMap<String, BigDecimal> multiPrices;
    private Date createdDate;
    private Date updatedDate;
    private SelectItem creator;
    private SelectItem updater;
    private SelectItem[] suppliers;
    private SelectItem[] locations;
    DiscountItem[] discounts;
    private Integer discountType;
    private String discountTypeName;
    private BigDecimal discountAmount;

    public ProductItem() {

    }

    public ProductItem(Integer objectId, String name, String description, BigDecimal itemsInStock) {
        this.objectId = objectId;
        this.name = name;
        this.description = description;
        this.itemsInStock = itemsInStock;
    }

    public ProductItem(Integer objectId, String name, BigDecimal itemsInStock, BigDecimal unitpPrice, String category) {
        this.objectId = objectId;
        this.name = name;
        this.itemsInStock = itemsInStock;
        this.unitpPrice = unitpPrice;
        this.category = category;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitpPrice() {
        return unitpPrice;
    }

    public void setUnitpPrice(BigDecimal unitpPrice) {
        this.unitpPrice = unitpPrice;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getItemsInStock() {
        return itemsInStock != null ? itemsInStock : BigDecimal.ZERO;
    }

    public void setItemsInStock(BigDecimal itemsInStock) {
        this.itemsInStock = itemsInStock;
    }

    public BigDecimal getItemsInWarehouse() {
        return itemsInWarehouse != null ? itemsInWarehouse : BigDecimal.ZERO;
    }

    public void setItemsInWarehouse(BigDecimal itemsInWarehouse) {
        this.itemsInWarehouse = itemsInWarehouse;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public Boolean isInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(Boolean inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(String skuNumber) {
        this.skuNumber = skuNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(String rentStatus) {
        this.rentStatus = rentStatus;
    }

    public String getBarCodeString() {
        return barCodeString;
    }

    public void setBarCodeString(String barCodeString) {
        this.barCodeString = barCodeString;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public BigDecimal getMinReorderPoint() {
        return minReorderPoint;
    }

    public void setMinReorderPoint(BigDecimal minReorderPoint) {
        this.minReorderPoint = minReorderPoint;
    }

    public Boolean isStorefrontEnable() {
        return storefrontEnable;
    }

    public void setStorefrontEnable(Boolean storefrontEnable) {
        this.storefrontEnable = storefrontEnable;
    }

    public ProductKitItem[] getProductKitItems() {
        return productKitItems;
    }

    public void setProductKitItems(ProductKitItem[] productKitItems) {
        this.productKitItems = productKitItems;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public String getCogsAccount() {
        return cogsAccount;
    }

    public void setCogsAccount(String cogsAccount) {
        this.cogsAccount = cogsAccount;
    }

    public String getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(String assetAccount) {
        this.assetAccount = assetAccount;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public DateNonConvertable getAsOf() {
        return asOf;
    }

    public void setAsOf(DateNonConvertable asOf) {
        this.asOf = asOf;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public BigDecimal getOnSaleOrderQty() {
        return onSaleOrderQty != null ? onSaleOrderQty : BigDecimal.ZERO;
    }

    public void setOnSaleOrderQty(BigDecimal onSaleOrderQty) {
        this.onSaleOrderQty = onSaleOrderQty;
    }

//    public BigDecimal getSaleOrderRamaingQty() {
//        return saleOrderRamaingQty != null ? saleOrderRamaingQty : BigDecimal.ZERO;
//    }
//
//    public void setSaleOrderRamaingQty(BigDecimal saleOrderRamaingQty) {
//        this.saleOrderRamaingQty = saleOrderRamaingQty;
//    }

    public BigDecimal getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(BigDecimal currentQty) {
        this.currentQty = currentQty;
    }

    public BigDecimal getUsedQty() {
        return usedQty;
    }

    public void setUsedQty(BigDecimal usedQty) {
        this.usedQty = usedQty;
    }

    public BigDecimal getNewQty() {
        return newQty;
    }

    public void setNewQty(BigDecimal newQty) {
        this.newQty = newQty;
    }

    public BigDecimal getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public SelectItem getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(SelectItem parentCategory) {
        this.parentCategory = parentCategory;
    }

    public ArrayList<NewProductCustomDescription> getSubItemsData() {
        return subItemsData;
    }

    public void setSubItemsData(ArrayList<NewProductCustomDescription> subItemsData) {
        this.subItemsData = subItemsData;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDefaultPictureUrl() {
        return defaultPictureUrl;
    }

    public void setDefaultPictureUrl(String defaultPictureUrl) {
        this.defaultPictureUrl = defaultPictureUrl;
    }

    public String getDefaultPictureMiniUrl() {
        return defaultPictureMiniUrl;
    }

    public void setDefaultPictureMiniUrl(String defaultPictureMiniUrl) {
        this.defaultPictureMiniUrl = defaultPictureMiniUrl;
    }

    public Integer getProductRentalItemId() {
        return productRentalItemId;
    }

    public void setProductRentalItemId(Integer productRentalItemId) {
        this.productRentalItemId = productRentalItemId;
    }

    public Boolean isBuilt() {
        return built;
    }

    public void setBuilt(Boolean built) {
        this.built = built;
    }

    public ArrayList<CompanyCustomFieldItem> getProductCustomFieldItems() {
        return productCustomFieldItems;
    }

    public ArrayList<CompanyCustomFieldItem> getCategoryCustomFieldItems() {
        return categoryCustomFieldItems;
    }

    public void setCategoryCustomFieldItems(ArrayList<CompanyCustomFieldItem> categoryCustomFieldItems) {
        this.categoryCustomFieldItems = categoryCustomFieldItems;
    }

    public void setProductCustomFieldItems(ArrayList<CompanyCustomFieldItem> productCustomFieldItems) {
        this.productCustomFieldItems = productCustomFieldItems;
    }

    public ArrayList<CompanyCustomFieldItem> getProductCustomFields() {
        if (categoryCustomFieldItems != null && productCustomFieldItems != null) {
            categoryCustomFieldItems.addAll(productCustomFieldItems);
            return categoryCustomFieldItems;
        } else {
            return categoryCustomFieldItems != null ? categoryCustomFieldItems : productCustomFieldItems;
        }
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return cusFieldMap != null ? cusFieldMap.get(columnCodeKey) : null;
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        cusFieldMap.put(columnCodeKey, cellValue);
    }

    public HashMap<String, Object> getCustomFieldMap() {
        return cusFieldMap;
    }

    public void setCustomFieldMap(HashMap<String, Object> cusFieldMap) {
        this.cusFieldMap = cusFieldMap;
    }

    public Integer getCogsAccountID() {
        return cogsAccountID;
    }

    public void setCogsAccountID(Integer cogsAccountID) {
        this.cogsAccountID = cogsAccountID;
    }

    public Integer getAssetAccountID() {
        return assetAccountID;
    }

    public void setAssetAccountID(Integer assetAccountID) {
        this.assetAccountID = assetAccountID;
    }

    public Integer getTaxRateID() {
        return taxRateID;
    }

    public void setTaxRateID(Integer taxRateID) {
        this.taxRateID = taxRateID;
    }

    public BigDecimal getOnPurchaseOrder() {
        return onPurchaseOrder;
    }

    public void setOnPurchaseOrder(BigDecimal onPurchaseOrder) {
        this.onPurchaseOrder = onPurchaseOrder;
    }

    public ArrayList<AssemblyItem> getAssemblyItems() {
        return assemblyItems;
    }

    public void setAssemblyItems(ArrayList<AssemblyItem> assemblyItems) {
        this.assemblyItems = assemblyItems;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public HashMap<Integer, ProductItem> getQtyByWarehouse() {
        return qtyByWarehouse;
    }

    public void setQtyByWarehouse(HashMap<Integer, ProductItem> qtyByWarehouse) {
        this.qtyByWarehouse = qtyByWarehouse;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getUpcNumber() {
        return upcNumber;
    }

    public void setUpcNumber(String upcNumber) {
        this.upcNumber = upcNumber;
    }

    public void setTaxAmountId(Integer taxAmountId) {
        this.taxAmountId = taxAmountId;
    }

    public Integer getTaxAmountId() {
        return taxAmountId;
    }

    public Integer getLineItemID() {
        return lineItemID;
    }

    public void setLineItemID(Integer lineItemID) {
        this.lineItemID = lineItemID;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getFromWarehouseName() {
        return fromWarehouseName;
    }

    public void setFromWarehouseName(String fromWarehouseName) {
        this.fromWarehouseName = fromWarehouseName;
    }

    public Integer getFromWarehouseId() {
        return fromWarehouseId;
    }

    public void setFromWarehouseId(Integer fromWarehouseId) {
        this.fromWarehouseId = fromWarehouseId;
    }

    public String getToWarehouseName() {
        return toWarehouseName;
    }

    public void setToWarehouseName(String toWarehouseName) {
        this.toWarehouseName = toWarehouseName;
    }

    public String getFromAccountName() {
        return fromAccountName;
    }

    public void setFromAccountName(String fromAccountName) {
        this.fromAccountName = fromAccountName;
    }

    public String getToAccountName() {
        return toAccountName;
    }

    public void setToAccountName(String toAccountName) {
        this.toAccountName = toAccountName;
    }

    public ProductLocationItem[] getProductLocations() {
        return productLocations;
    }

    public void setProductLocations(ProductLocationItem[] productLocations) {
        this.productLocations = productLocations;
    }

    public String getUnitMeasurementName() {
        return unitMeasurementName;
    }

    public void setUnitMeasurementName(String unitMeasurementName) {
        this.unitMeasurementName = unitMeasurementName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSubsidiaryProductUniqNum() {
        return subsidiaryProductUniqNum;
    }

    public void setSubsidiaryProductUniqNum(String subsidiaryProductUniqNum) {
        this.subsidiaryProductUniqNum = subsidiaryProductUniqNum;
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

    public ArrayList<ProductTrackBatchItem> getBatchItems() {
        return batchItems;
    }

    public void setBatchItems(ArrayList<ProductTrackBatchItem> batchItems) {
        this.batchItems = batchItems;
    }


    public ArrayList<ProductTrackBatchItem> getAssignedBatchItems() {
        return assignedBatchItems;
    }

    public void setAssignedBatchItems(ArrayList<ProductTrackBatchItem> assignedBatchItems) {
        this.assignedBatchItems = assignedBatchItems;
    }

    public ArrayList<String> getSerials() {
        return serials;
    }

    public void setSerials(ArrayList<String> serials) {
        this.serials = serials;
    }

    public ArrayList<String> getAssignedSerials() {
        return assignedSerials;
    }

    public void setAssignedSerials(ArrayList<String> assignedSerials) {
        this.assignedSerials = assignedSerials;
    }

    public boolean isProductSerialsEnabled() {
        return productSerialsEnabled;
    }

    public void setProductSerialsEnabled(boolean productSerialsEnabled) {
        this.productSerialsEnabled = productSerialsEnabled;
    }

    public BigDecimal getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost;
    }

    public ArrayList<ProductSerialItem> getProductSerialItems() {
        return productSerialItems;
    }

    public void setProductSerialItems(ArrayList<ProductSerialItem> productSerialItems) {
        this.productSerialItems = productSerialItems;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setInWarehouseList(ArrayList<WarehouseItem> inWarehouseList) {
        this.inWarehouseList = inWarehouseList;
    }

    public ArrayList<WarehouseItem> getInWarehouseList() {
        return inWarehouseList;
    }

    public LinkedHashMap<String, BigDecimal> getMultiPrices() {
        if(multiPrices == null){
            multiPrices = new LinkedHashMap<>();
        }
        return multiPrices;
    }

    public void setMultiPrices(LinkedHashMap<String, BigDecimal> multiPrices) {
        this.multiPrices = multiPrices;
    }


    public Integer getUnitMeasurementId() {
        return unitMeasurementId;
    }

    public void setUnitMeasurementId(Integer unitMeasurementId) {
        this.unitMeasurementId = unitMeasurementId;
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

    public DiscountItem[] getDiscounts() {
        return discounts;
    }

    public void setDiscounts(DiscountItem[] discounts) {
        this.discounts = discounts;
    }

    public SelectItem getCreator() {
        return this.creator;
    }

    public void setCreator(final SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getUpdater() {
        return this.updater;
    }

    public void setUpdater(final SelectItem updater) {
        this.updater = updater;
    }

    public SelectItem[] getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(SelectItem[] suppliers) {
        this.suppliers = suppliers;
    }

    public SelectItem[] getLocations() {
        return locations;
    }

    public void setLocations(SelectItem[] locations) {
        this.locations = locations;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount != null ? discountAmount : BigDecimal.ZERO;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountTypeName() {
        return discountTypeName;
    }

    public void setDiscountTypeName(String discountTypeName) {
        this.discountTypeName = discountTypeName;
    }
}


