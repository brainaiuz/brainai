package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.MultiPriceItem;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.ProductPicture;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Jonibek
 * Date: Feb 25, 2009
 * Time: 8:46:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewProduct implements Serializable {

    private Integer objectId;
    private String objectKey;
    private Integer itemNameID;
    private String itemName;
    private String description;
    private ArrayList<NewProductCustomDescription> customDescription;
    private BigDecimal unitPrice;
    private BigDecimal unitCost;
    private BigDecimal itemsInStock;
    private Integer accountId;
    private com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem accountItem;
    private TaxItem taxItem;
    private TaxItem doubleTaxItem;
    private Double effectiveTaxRate;
    private Integer type;
    private String typeName;
    private Boolean trackInventory;
    private BigDecimal quantity;
    private BigDecimal pkItemQty;
    private BigDecimal consignedQty;
    private BigDecimal consignedQtyToSell;
    private BigDecimal totalQtyOnHand;
    private BigDecimal comission;
    private Boolean rentalItem;
    private Integer rentalPeriod;
    private BigDecimal rentalRate;
    private BigDecimal overdueRate;
    private Integer cancelationPeriod;
    private Integer cancelationPeriodType;
    private BigDecimal cancelationFee;

    private Integer categoryID;
    private String categoryName;
    private String internalSKUNumber;
    private String barCodeText;
    private String upcNumber;
    private Integer unitMeasurementID;
    private SelectItem unitMeasurement;
    private SelectItem vendorItem;
    private Integer vendorCurrencyID;
    private String weightPerUnit;
    private BigDecimal sellingPrice;
    private Integer[] taxIDs;
    private Integer vatId;
    private Integer doubleVatId;
    private ProductLocationItem[] productLocations;
    private ArrayList<CompanyCustomFieldItem> categoryCustomFieldItems;
    private ArrayList<CompanyCustomFieldItem> productCustomFieldItems;
    private ArrayList<AssemblyItem> assemblyItems;
    private ArrayList<RentalProductItem> rentalProductItems;
    private ColumnConfigs[] customItemColumns;
    private BigDecimal extraHour;
    private BigDecimal extraDay;
    private BigDecimal securityTime;
    private SelectItem[] templates;

//    private boolean showOnOpportunity;

    private ProductKitItem[] productKitItems;

    private DiscountItem[] discountItems;
    private Integer discountItemStaticType;
    private Integer itemDiscountID;

    private Boolean sfEnable;

    private Integer order;
    private Integer brandID;
    private String brandName;
    private Boolean featured;
    private Boolean special;
    private Boolean showOnHomepage;
    private Boolean virtual;
    private Boolean freeShipping;
    private Integer condition;

    //For Purchase Items only
    private BigDecimal usedItems;

    //Filling Data
    private SelectItem[] unitMeasurements;
    private SelectItem[] productCategories;
    //    private SelectItem[] vendors;
    private SelectItem[] brands;
    private AccountsByCategory accounts;
    private TaxList taxList;

    private com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem defaultReceivableAccount;
    private com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem defaultPayableAccount;
    private com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem defaultAssetAccount;

    private ArrayList<SelectItem> accountItemList;
    private ArrayList<SelectItem> costOfSalesAccountItemList;

    private com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem cogsAccount;
    private com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem assetAccount;

    private Integer cogsAccountID;
    private Integer assetAccountID;

    private Boolean purchasedFromSupplier = false;
    private Boolean soldToCustomer = true;
    private BigDecimal globalReorderPoint;
    private BigDecimal totalValue;
    private DateNonConvertable asOf;

    private boolean enableCompanyIT = true;
    private boolean enableIT;

    private Integer parentId;

    private String parentName;

    private NewProduct[] childProducts;
    private Boolean hasVariations;
    private ArrayList<String> variationCombinate = new ArrayList<>();

    private Integer QRCodeSizeID;

    private NumberData numberData;

    private Integer intNumber;

    private SelectItem warehouse;

    private SelectItem warehouseByOwner;
    private SelectItem defaultItemWarehouse;

    private String layoutHTML;
    private String saasuGUID;
    private Date sasuuLastUpdatedDate;
    private boolean hasUsed;
    private String manufacturer;
    private String partNumber;
    private SelectItem customer;
    private boolean fromSaasu;
    private boolean doubleTaxEnabled;
    private boolean fromQuickbooks;

    private String quickbookItemID;
    private String quickbookEditSequence;

    private Integer magentoEntityID;
    private Date magentoLastSyncDate;

    private String nimbleOfferID;

    private String subsidiaryProductUniqueID;

    private ProductPicture[] imageGallery;
    private FileItem[] attachments;

    private Boolean active;
    private Boolean trackBatchesEnabled;
    private Boolean hasInventoryInProductKit;
    private SelectItem[] localeList;
    private Integer localeID;
    private Integer subItemID;
    private ArrayList<ProductSerialItem> productSerialItems;
    private boolean productSerialsEnabled;
    private Boolean inventoryTrackingEnabled;
    private Boolean batchTrackingEnabled;
    private Boolean trackBatchEnabled;
    private ArrayList<ProductTrackBatchItem> trackBatchItems;
    private ArrayList<MultiPriceItem> multiPrices;
    private HashMap<String, BigDecimal> multiPricesMap;

    private SelectItem positionItem;
    private SelectItem[] positionItems;
    private SelectItem[] suppliers;
    private BigDecimal averageCost;
    private CurrencyItem[] currencies;
    private Integer currencyId; //Initially is implemented for Assembly Items
    private boolean copied;
    private Date lastUpdateTime;
    private boolean barcodeNumberingEnabled;
    private Integer barcodeID;
    private String barcodeChecksum;

    private BigDecimal exchangeRate;
    private SelectItem costAllocationType;
    private Date createdDate;
    private Integer imageId;

    private SelectItem defaultDepartment;

    private Long zapiervariantid;//were added for zapier/shopify integration

    private Integer discountType;

    private String discountTypeName;

    private BigDecimal discountAmount;

    private ArrayList<SelectItem> locations;

    private ArrayList<Integer> locationIds;

    private SelectItem rentStatus;

    private SelectItem rentItem;

    private Boolean sentToTextileFinds = false;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public Integer getItemNameID() {
        return itemNameID;
    }

    public void setItemNameID(Integer itemNameID) {
        this.itemNameID = itemNameID;
    }

    public SelectItem getDefaultDepartment() {
        return defaultDepartment;
    }

    public void setDefaultDepartment(SelectItem defaultDepartment) {
        this.defaultDepartment = defaultDepartment;
    }

    public SelectItem getWarehouseByOwner() {
        return warehouseByOwner;
    }

    public void setWarehouseByOwner(SelectItem warehouseByOwner) {
        this.warehouseByOwner = warehouseByOwner;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<NewProductCustomDescription> getCustomDescription() {
        return customDescription;
    }

    public void setCustomDescription(ArrayList<NewProductCustomDescription> customDescription) {
        this.customDescription = customDescription;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getItemsInStock() {
        return itemsInStock;
    }

    public void setItemsInStock(BigDecimal itemsInStock) {
        this.itemsInStock = itemsInStock;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public TaxItem getTaxItem() {
        return taxItem;
    }

    public void setTaxItem(TaxItem taxItem) {
        this.taxItem = taxItem;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Boolean getTrackInventory() {
        return trackInventory;
    }

    public void setTrackInventory(Boolean trackInventory) {
        this.trackInventory = trackInventory;
    }

    public BigDecimal getQuantity() {
        return quantity != null ? quantity : BigDecimal.ZERO;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getConsignedQty() {
        return consignedQty != null ? consignedQty : BigDecimal.ZERO;
    }

    public void setConsignedQty(BigDecimal consignedQty) {
        this.consignedQty = consignedQty;
    }

    public BigDecimal getConsignedQtyToSell() {
        return consignedQtyToSell;
    }

    public void setConsignedQtyToSell(BigDecimal consignedQtyToSell) {
        this.consignedQtyToSell = consignedQtyToSell;
    }

    public Boolean getRentalItem() {
        return rentalItem;
    }

    public void setRentalItem(Boolean rentalItem) {
        this.rentalItem = rentalItem;
    }

    public Integer getRentalPeriod() {
        return rentalPeriod;
    }

    public void setRentalPeriod(Integer rentalPeriod) {
        this.rentalPeriod = rentalPeriod;
    }

    public BigDecimal getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }

    public BigDecimal getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }

    public Integer getCancelationPeriod() {
        return cancelationPeriod;
    }

    public void setCancelationPeriod(Integer cancelationPeriod) {
        this.cancelationPeriod = cancelationPeriod;
    }

    public Integer getCancelationPeriodType() {
        return cancelationPeriodType;
    }

    public void setCancelationPeriodType(Integer cancelationPeriodType) {
        this.cancelationPeriodType = cancelationPeriodType;
    }

    public BigDecimal getCancelationFee() {
        return cancelationFee;
    }

    public void setCancelationFee(BigDecimal cancelationFee) {
        this.cancelationFee = cancelationFee;
    }

    public BigDecimal getUsedItems() {
        return usedItems;
    }

    public void setUsedItems(BigDecimal usedItems) {
        this.usedItems = usedItems;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getInternalSKUNumber() {
        return internalSKUNumber;
    }

    public void setInternalSKUNumber(String internalSKUNumber) {
        this.internalSKUNumber = internalSKUNumber;
    }

    public String getBarCodeText() {
        return barCodeText;
    }

    public void setBarCodeText(String barCodeText) {
        this.barCodeText = barCodeText;
    }

    public String getUpcNumber() {
        return upcNumber;
    }

    public void setUpcNumber(String upcNumber) {
        this.upcNumber = upcNumber;
    }

    public Integer getUnitMeasurementID() {
        return unitMeasurementID;
    }

    public void setUnitMeasurementID(Integer unitMeasurementID) {
        this.unitMeasurementID = unitMeasurementID;
    }

    public SelectItem getVendorItem() {
        return vendorItem;
    }

    public void setVendorItem(SelectItem vendorItem) {
        this.vendorItem = vendorItem;
    }

    public String getWeightPerUnit() {
        return weightPerUnit;
    }

    public void setWeightPerUnit(String weightPerUnit) {
        this.weightPerUnit = weightPerUnit;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Integer[] getTaxIDs() {
        return taxIDs;
    }

    public void setTaxIDs(Integer[] taxIDs) {
        this.taxIDs = taxIDs;
    }

    public ProductLocationItem[] getProductLocations() {
        return productLocations;
    }

    public void setProductLocations(ProductLocationItem[] productLocations) {
        this.productLocations = productLocations;
    }

    public Integer getVendorCurrencyID() {
        return vendorCurrencyID;
    }

    public void setVendorCurrencyID(Integer vendorCurrencyID) {
        this.vendorCurrencyID = vendorCurrencyID;
    }

    public Boolean isStorefrontEnable() {
        return sfEnable;
    }

    public void setStorefrontEnable(Boolean sfEnable) {
        this.sfEnable = sfEnable;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getBrandID() {
        return brandID;
    }

    public void setBrandID(Integer brandID) {
        this.brandID = brandID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getSpecial() {
        return special;
    }

    public void setSpecial(Boolean special) {
        this.special = special;
    }

    public Boolean getShowOnHomepage() {
        return showOnHomepage;
    }

    public void setShowOnHomepage(Boolean showOnHomepage) {
        this.showOnHomepage = showOnHomepage;
    }

    public Boolean getVirtual() {
        return virtual;
    }

    public void setVirtual(Boolean virtual) {
        this.virtual = virtual;
    }

    public Boolean getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(Boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public SelectItem[] getUnitMeasurements() {
        return unitMeasurements;
    }

    public void setUnitMeasurements(SelectItem[] unitMeasurements) {
        this.unitMeasurements = unitMeasurements;
    }

    public SelectItem[] getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(SelectItem[] productCategories) {
        this.productCategories = productCategories;
    }

    public SelectItem[] getBrands() {
        return brands;
    }

    public void setBrands(SelectItem[] brands) {
        this.brands = brands;
    }

    public AccountsByCategory getAccounts() {
        return accounts;
    }

    public void setAccounts(AccountsByCategory accounts) {
        this.accounts = accounts;
    }

    public TaxList getTaxList() {
        return taxList;
    }

    public void setTaxList(TaxList taxList) {
        this.taxList = taxList;
    }

    public ProductKitItem[] getProductKitItems() {
        return productKitItems;
    }

    public void setProductKitItems(ProductKitItem[] productKitItems) {
        this.productKitItems = productKitItems;
    }

    public Double getEffectiveTaxRate() {
        return effectiveTaxRate;
    }

    public void setEffectiveTaxRate(Double effectiveTaxRate) {
        this.effectiveTaxRate = effectiveTaxRate;
    }

    public com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem getAccountItem() {
        return accountItem;
    }

    public void setAccountItem(com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem accountItem) {
        this.accountItem = accountItem;
    }

    public DiscountItem[] getDiscountItems() {
        return discountItems;
    }

    public void setDiscountItems(DiscountItem[] discountItems) {
        this.discountItems = discountItems;
    }

    public Integer getDiscountItemStaticType() {
        return discountItemStaticType;
    }

    public void setDiscountItemStaticType(Integer discountItemStaticType) {
        this.discountItemStaticType = discountItemStaticType;
    }

    public Integer getItemDiscountID() {
        return itemDiscountID;
    }

    public void setItemDiscountID(Integer itemDiscountID) {
        this.itemDiscountID = itemDiscountID;
    }

    public com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem getDefaultReceivableAccount() {
        return defaultReceivableAccount;
    }

    public void setDefaultReceivableAccount(com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem defaultReceivableAccount) {
        this.defaultReceivableAccount = defaultReceivableAccount;
    }

    public com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem getDefaultPayableAccount() {
        return defaultPayableAccount;
    }

    public void setDefaultPayableAccount(com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem defaultPayableAccount) {
        this.defaultPayableAccount = defaultPayableAccount;
    }

    public ArrayList<SelectItem> getAccountItemList() {
        return accountItemList;
    }

    public void setAccountItemList(ArrayList<SelectItem> accountItemList) {
        this.accountItemList = accountItemList;
    }

    public ArrayList<SelectItem> getCostOfSalesAccountItemList() {
        return costOfSalesAccountItemList;
    }

    public void setCostOfSalesAccountItemList(ArrayList<SelectItem> costOfSalesAccountItemList) {
        this.costOfSalesAccountItemList = costOfSalesAccountItemList;
    }

    public com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem getCogsAccount() {
        return cogsAccount;
    }

    public void setCogsAccount(com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem cogsAccount) {
        this.cogsAccount = cogsAccount;
    }

    public com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(AccountItem assetAccount) {
        this.assetAccount = assetAccount;
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

    public BigDecimal getGlobalReorderPoint() {
        return globalReorderPoint;
    }

    public void setGlobalReorderPoint(BigDecimal globalReorderPoint) {
        this.globalReorderPoint = globalReorderPoint;
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

    public Boolean isPurchasedFromSupplier() {
        return purchasedFromSupplier;
    }

    public void setPurchasedFromSupplier(Boolean purchasedFromSupplier) {
        this.purchasedFromSupplier = purchasedFromSupplier;
    }

    public boolean enableCompanyIT() {
        return enableCompanyIT;
    }

    public void setEnableCompanyIT(boolean enableCompanyIT) {
        this.enableCompanyIT = enableCompanyIT;
    }

    public boolean enableIT() {
        return enableIT;
    }

    public void setEnableIT(boolean enableIT) {
        this.enableIT = enableIT;
    }

    public ArrayList<CompanyCustomFieldItem> getCategoryCustomFieldItems() {
        return categoryCustomFieldItems;
    }

    public void setCategoryCustomFieldItems(ArrayList<CompanyCustomFieldItem> categoryCustomFieldItems) {
        this.categoryCustomFieldItems = categoryCustomFieldItems;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public NewProduct[] getChildProducts() {
        return childProducts;
    }

    public void setChildProducts(NewProduct[] childProducts) {
        this.childProducts = childProducts;
    }

    public Boolean getHasVariations() {
        return hasVariations;
    }

    public void setHasVariations(Boolean hasVariations) {
        this.hasVariations = hasVariations;
    }

    public BigDecimal getComission() {
        return comission;
    }

    public void setComission(BigDecimal comission) {
        this.comission = comission;
    }

//    public boolean isShowOnOpportunity() {
//        return showOnOpportunity;
//    }

//    public void setShowOnOpportunity(boolean showOnOpportunity) {
//        this.showOnOpportunity = showOnOpportunity;
//    }

    public Integer getQRCodeSizeID() {
        return QRCodeSizeID;
    }

    public void setQRCodeSizeID(Integer QRCodeSizeID) {
        this.QRCodeSizeID = QRCodeSizeID;
    }

    public ArrayList<String> getVariationCombinate() {
        return variationCombinate;
    }

    public void setVariationCombinate(ArrayList<String> variationCombinate) {
        this.variationCombinate = variationCombinate;
    }

    public void setUnitMeasurement(SelectItem unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public SelectItem getUnitMeasurement() {
        return unitMeasurement;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public ArrayList<AssemblyItem> getAssemblyItems() {
        return assemblyItems;
    }

    public void setAssemblyItems(ArrayList<AssemblyItem> assemblyItems) {
        this.assemblyItems = assemblyItems;
    }

    public SelectItem getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(SelectItem warehouse) {
        this.warehouse = warehouse;
    }

    public SelectItem getDefaultItemWarehouse() {
        return defaultItemWarehouse;
    }

    public void setDefaultItemWarehouse(SelectItem defaultItemWarehouse) {
        this.defaultItemWarehouse = defaultItemWarehouse;
    }

    public String getLayoutHTML() {
        return layoutHTML;
    }

    public void setLayoutHTML(String layoutHTML) {
        this.layoutHTML = layoutHTML;
    }

    public String getSaasuGUID() {
        return saasuGUID;
    }

    public void setSaasuGUID(String saasuGUID) {
        this.saasuGUID = saasuGUID;
    }

    public Date getSasuuLastUpdatedDate() {
        return sasuuLastUpdatedDate;
    }

    public void setSasuuLastUpdatedDate(Date sasuuLastUpdatedDate) {
        this.sasuuLastUpdatedDate = sasuuLastUpdatedDate;
    }

    public ProductPicture[] getImageGallery() {
        return imageGallery;
    }

    public void setImageGallery(ProductPicture[] imageGallery) {
        this.imageGallery = imageGallery;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public boolean hasUsed() {
        return hasUsed;
    }

    public void setHasUsed(boolean hasUsed) {
        this.hasUsed = hasUsed;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public SelectItem getCustomer() {
        return customer;
    }

    public void setCustomer(SelectItem customer) {
        this.customer = customer;
    }

    public boolean isFromSaasu() {
        return fromSaasu;
    }

    public void setFromSaasu(boolean fromSaasu) {
        this.fromSaasu = fromSaasu;
    }

    public String getQuickbookItemID() {
        return quickbookItemID;
    }

    public void setQuickbookItemID(String quickbookItemID) {
        this.quickbookItemID = quickbookItemID;
    }

    public String getQuickbookEditSequence() {
        return quickbookEditSequence;
    }

    public boolean isFromQuickbooks() {
        return fromQuickbooks;
    }

    public void setFromQuickbooks(boolean fromQuickbooks) {
        this.fromQuickbooks = fromQuickbooks;
    }

    public void setQuickbookEditSequence(String quickbookEditSequence) {
        this.quickbookEditSequence = quickbookEditSequence;
    }

    public TaxItem getDoubleTaxItem() {
        return doubleTaxItem;
    }

    public void setDoubleTaxItem(TaxItem doubleTaxItem) {
        this.doubleTaxItem = doubleTaxItem;
    }

    public boolean isDoubleTaxEnabled() {
        return doubleTaxEnabled;
    }

    public void setDoubleTaxEnabled(boolean doubleTaxEnabled) {
        this.doubleTaxEnabled = doubleTaxEnabled;
    }

    public Integer getVatId() {
        return vatId;
    }

    public void setVatId(Integer vatId) {
        this.vatId = vatId;
    }

    public Integer getDoubleVatId() {
        return doubleVatId;
    }

    public void setDoubleVatId(Integer doubleVatId) {
        this.doubleVatId = doubleVatId;
    }

    public String getNimbleOfferID() {
        return nimbleOfferID;
    }

    public void setNimbleOfferID(String nimbleOfferID) {
        this.nimbleOfferID = nimbleOfferID;
    }

    public String getSubsidiaryProductUniqueID() {
        return subsidiaryProductUniqueID;
    }

    public void setSubsidiaryProductUniqueID(String subsidiaryProductUniqueID) {
        this.subsidiaryProductUniqueID = subsidiaryProductUniqueID;
    }

    public Boolean isActive() {
        return active != null ? active : true;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getTrackBatchesEnabled() {
        return trackBatchesEnabled != null ? trackBatchesEnabled : false;
    }

    public void setTrackBatchesEnabled(Boolean trackBatchesEnabled) {
        this.trackBatchesEnabled = trackBatchesEnabled;
    }

    public ArrayList<CompanyCustomFieldItem> getProductCustomFieldItems() {
        return productCustomFieldItems;
    }

    public void setProductCustomFieldItems(ArrayList<CompanyCustomFieldItem> productCustomFieldItems) {
        this.productCustomFieldItems = productCustomFieldItems;
    }

    public Boolean isHasInventoryInProductKit() {
        return hasInventoryInProductKit;
    }

    public void setHasInventoryInProductKit(Boolean hasInventoryInProductKit) {
        this.hasInventoryInProductKit = hasInventoryInProductKit;
    }

    public SelectItem[] getLocaleList() {
        return localeList;
    }

    public void setLocaleList(SelectItem[] localeList) {
        this.localeList = localeList;
    }

    public Integer getLocaleID() {
        return localeID;
    }

    public void setLocaleID(Integer localeID) {
        this.localeID = localeID;
    }

    public Integer getSubItemID() {
        return subItemID;
    }

    public void setSubItemID(Integer subItemID) {
        this.subItemID = subItemID;
    }

    public ArrayList<ProductSerialItem> getProductSerialItems() {
        return productSerialItems;
    }

    public void setProductSerialItems(ArrayList<ProductSerialItem> productSerialItems) {
        this.productSerialItems = productSerialItems;
    }

    public BigDecimal getTotalQtyOnHand() {
        return totalQtyOnHand;
    }

    public void setTotalQtyOnHand(BigDecimal totalQtyOnHand) {
        this.totalQtyOnHand = totalQtyOnHand;
    }

    public boolean isProductSerialsEnabled() {
        return productSerialsEnabled;
    }

    public void setProductSerialsEnabled(boolean productSerialsEnabled) {
        this.productSerialsEnabled = productSerialsEnabled;
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


    public Boolean getTrackBatchEnabled() {
        return trackBatchEnabled != null ? trackBatchEnabled : false;
    }

    public void setTrackBatchEnabled(Boolean trackBatchEnabled) {
        this.trackBatchEnabled = trackBatchEnabled;
    }

    public ArrayList<ProductTrackBatchItem> getTrackBatchItems() {
        return trackBatchItems;
    }

    public void setTrackBatchItems(ArrayList<ProductTrackBatchItem> trackBatchItems) {
        this.trackBatchItems = trackBatchItems;
    }

    public ArrayList<MultiPriceItem> getMultiPrices() {
        if (multiPrices == null) {
            multiPrices = new ArrayList<>();
        }
        return multiPrices;
    }

    public void setMultiPrices(ArrayList<MultiPriceItem> multiPrices) {
        this.multiPrices = multiPrices;
    }

    public HashMap<String, BigDecimal> getMultiPricesMap() {
        if (multiPricesMap == null) {
            multiPricesMap = new HashMap<>();
        }
        return multiPricesMap;
    }

    public void setMultiPricesMap(HashMap<String, BigDecimal> multiPricesMap) {
        this.multiPricesMap = multiPricesMap;
    }

    public SelectItem getPositionItem() {
        return positionItem;
    }

    public void setPositionItem(SelectItem positionItem) {
        this.positionItem = positionItem;
    }

    public SelectItem[] getPositionItems() {
        return positionItems;
    }

    public void setPositionItems(SelectItem[] positionItems) {
        this.positionItems = positionItems;
    }

    public SelectItem[] getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(SelectItem[] suppliers) {
        this.suppliers = suppliers;
    }

    public boolean isCopied() {
        return copied;
    }

    public void setCopied(boolean copied) {
        this.copied = copied;
    }

    public BigDecimal getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost;
    }

    public Integer getMagentoEntityID() {
        return magentoEntityID;
    }

    public void setMagentoEntityID(Integer magentoEntityID) {
        this.magentoEntityID = magentoEntityID;
    }

    public Date getMagentoLastSyncDate() {
        return magentoLastSyncDate;
    }

    public void setMagentoLastSyncDate(Date magentoLastSyncDate) {
        this.magentoLastSyncDate = magentoLastSyncDate;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public CurrencyItem[] getCurrencies() {
        return currencies;
    }

    public void setCurrencies(CurrencyItem[] currencies) {
        this.currencies = currencies;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public boolean isBarcodeNumberingEnabled() {
        return barcodeNumberingEnabled;
    }

    public void setBarcodeNumberingEnabled(boolean barcodeNumberingEnabled) {
        this.barcodeNumberingEnabled = barcodeNumberingEnabled;
    }

    public Integer getBarcodeID() {
        return barcodeID;
    }

    public void setBarcodeID(Integer barcodeID) {
        this.barcodeID = barcodeID;
    }

    public String getBarcodeChecksum() {
        return barcodeChecksum;
    }

    public void setBarcodeChecksum(String barcodeChecksum) {
        this.barcodeChecksum = barcodeChecksum;
    }

    public SelectItem getCostAllocationType() {
        return costAllocationType;
    }

    public void setCostAllocationType(SelectItem costAllocationType) {
        this.costAllocationType = costAllocationType;
    }

    public BigDecimal getPkItemQty() {
        return pkItemQty;
    }

    public void setPkItemQty(BigDecimal pkItemQty) {
        this.pkItemQty = pkItemQty;
    }

    public Long getZapiervariantid() {
        return zapiervariantid;
    }

    public void setZapiervariantid(Long zapiervariantid) {
        this.zapiervariantid = zapiervariantid;
    }

    public ColumnConfigs[] getCustomItemColumns() {
        return this.customItemColumns;
    }

    public void setCustomItemColumns(final ColumnConfigs[] customItemColumns) {
        this.customItemColumns = customItemColumns;
    }

    public BigDecimal getExtraHour() {
        return this.extraHour;
    }

    public void setExtraHour(final BigDecimal extraHour) {
        this.extraHour = extraHour;
    }

    public BigDecimal getExtraDay() {
        return this.extraDay;
    }

    public void setExtraDay(final BigDecimal extraDay) {
        this.extraDay = extraDay;
    }

    public BigDecimal getSecurityTime() {
        return this.securityTime;
    }

    public void setSecurityTime(final BigDecimal securityTime) {
        this.securityTime = securityTime;
    }

    public ArrayList<RentalProductItem> getRentalProductItems() {
        return this.rentalProductItems;
    }

    public void setRentalProductItems(final ArrayList<RentalProductItem> rentalProductItems) {
        this.rentalProductItems = rentalProductItems;
    }

    public CompanyCustomFieldItem getCustomFieldByAlias(String aliasName) {
        if (productCustomFieldItems != null && !productCustomFieldItems.isEmpty()) {
            for (CompanyCustomFieldItem fieldItem : productCustomFieldItems) {

                if (aliasName.toLowerCase().equals(fieldItem.getAliasName().toLowerCase())) {
                    return fieldItem;
                }
            }
        }

        return null;

    }

    public SelectItem[] getTemplates() {
        return this.templates;
    }

    public void setTemplates(final SelectItem[] templates) {
        this.templates = templates;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public AccountItem getDefaultAssetAccount() {
        return defaultAssetAccount;
    }

    public void setDefaultAssetAccount(AccountItem defaultAssetAccount) {
        this.defaultAssetAccount = defaultAssetAccount;
    }

    public Boolean isSoldToCustomer() {
        return soldToCustomer;
    }

    public void setSoldToCustomer(Boolean soldToCustomer) {
        this.soldToCustomer = soldToCustomer;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public String getDiscountTypeName() {
        return discountTypeName;
    }

    public void setDiscountTypeName(String discountTypeName) {
        this.discountTypeName = discountTypeName;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount != null ? discountAmount : BigDecimal.ZERO;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public ArrayList<SelectItem> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<SelectItem> locations) {
        this.locations = locations;
    }

    public ArrayList<Integer> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(ArrayList<Integer> locationIds) {
        this.locationIds = locationIds;
    }

    public SelectItem getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(SelectItem rentStatus) {
        this.rentStatus = rentStatus;
    }

    public SelectItem getRentItem() {
        return rentItem;
    }

    public void setRentItem(SelectItem rentItem) {
        this.rentItem = rentItem;
    }

    public Boolean isSentToTextileFinds() {
        return sentToTextileFinds != null ? sentToTextileFinds : false;
    }

    public void setSentToTextileFinds(Boolean sentToTextileFinds) {
        this.sentToTextileFinds = sentToTextileFinds;
    }
}
