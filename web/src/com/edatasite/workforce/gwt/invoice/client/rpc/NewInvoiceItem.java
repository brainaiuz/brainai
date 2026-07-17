package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.NewProductCustomDescription;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 05.03.2009
 * Time: 16:58:06
 * To change this template use File | Settings | File Templates.
 */
public class NewInvoiceItem implements Serializable {

    private Integer id;//e.g. objectID of the product.

    private String uuid;
    private String description;
    private ArrayList<NewProductCustomDescription> customDescription;
    private Boolean isLumpsum;
    private BigDecimal quantity;
    private BigDecimal itemsInStockQty;
    private SelectItem measurement;
    private BigDecimal unitPrice;
    private BigDecimal itemOriginalPrice;
    private Integer itemDiscountID;
    private String itemDiscount;
    private Integer itemDoubleDiscountID;
    private String itemDoubleDiscount;
    private BigDecimal discountPercent;//discount percent
    private BigDecimal discountAmount;
    private BigDecimal currentProductDiscountAmount;
    private Integer discountItemStaticType;
    private BigDecimal doubleDiscountPercent;//double discount percent
    private BigDecimal doubleDiscountAmount;
    private BigDecimal unitCost;
    private BigDecimal itemAverageCost;
    private BigDecimal comission;
    private Integer accountID;
    private String accountName;
    private AccountItem salesAccount;
    private AccountItem accountItem;
    private TaxItem taxItem;
    //    private Integer taxID;
//    private String taxName;
    private BigDecimal taxAmount;
    private BigDecimal receivedQty;
    private BigDecimal receivedAmount;
    private BigDecimal shippedQty;
    private BigDecimal receive; //this is for the partial receive PO
    private BigDecimal allocatedExpense;
    private BigDecimal receivedAllocation;
    private ReceiveTypeEnum receiveType;
    private BigDecimal convertedQty; // for progress invoicing by item
    private BigDecimal convertedAmount;
    private Integer quoteItemId; //for progress invoicing by item
    private Integer saleInvoiceId; //for purchase invoice used as billable expense
    private String shortLink; //short link for amazon only for sale invoice, to display to user

    private SelectItem warehouse;
    private SelectItem parentProject;
    private SelectItem project;
    private String productBrand;
    private Integer productBrandID;

    private BigDecimal qtyWithHighScale;//for progress invoicing by amount

    /**
     * For Tax Exclusive: net = quantity * unitPrice;
     * <p/>
     * For Tax Inclisuve: net = (quantity * unitPrice * 100%) / (100% + tax_percent_value);
     * Tax inclusive is not calculating during a short period.
     */
    private BigDecimal net;

    //Every InvoiceItem has an item which stores data from this invoiceItem.
    // It's for when you choose an invoiceItem must be shown items per it. Now it's not important thing.
    private Integer itemID;
    private String itemName;
    private String fullItemName;
    private String itemNumber;
    private Integer itemType;
    private DiscountItem[] itemDiscountList;
    private String itemCategory;
    private Integer supplierID;
    private String supplierName;
    private SelectItem client;

    private ProductSerialItem[] assignedSerials;
    private Integer productType;
    private Boolean isProductPurchasedFromSupplier;
    private Boolean hasInventoryInProductKit;
    private Boolean inventoryTrackingEnabled;
    private Boolean batchTrackingEnabled;//for track serials
    private ArrayList<String> serials;
    private Boolean trackBatchesEnabled;
    private ArrayList<ProductTrackBatchItem> batchItems;
    private TypeItem type;

    private BigDecimal totalAmount;
    private BigDecimal priceLevelAmount;

    private String projectBasedInvoiceDesc;
    private Integer[] projectBasedEntryIds;
    private TaxItem doubleTaxItem;
    private BigDecimal doubleTaxAmount;
    private HashMap<String, BigDecimal> multiPricesMap;

    //for Quickbooks Integration
    private String qbItemId;
    private SelectItem departmentItem;
    private String itemBarcode;
    private boolean fromTimesheet;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private boolean isPickable;
    private boolean soldOut;//is product of PI is sold out
    private ArrayList<FileItem> attachments = new ArrayList<>();

    private DateNonConvertable fromDate;
    private DateNonConvertable toDate;
    private Integer expanceItemId;
    private ShippingDataItem usedInGrn;
    private Integer faiCategoryId;
    private SelectItem faiCategory;

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
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

    public Boolean isLumpsum() {
        return isLumpsum != null ? isLumpsum : false;
    }

    public void setLumpsum(Boolean lumpsum) {
        isLumpsum = lumpsum;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getItemsInStockQty() {
        return itemsInStockQty;
    }

    public void setItemsInStockQty(BigDecimal itemsInStockQty) {
        this.itemsInStockQty = itemsInStockQty;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice != null ? unitPrice : BigDecimal.ZERO;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getItemOriginalPrice() {
        return itemOriginalPrice;
    }

    public void setItemOriginalPrice(BigDecimal itemOriginalPrice) {
        this.itemOriginalPrice = itemOriginalPrice;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getDoubleDiscountPercent() {
        return doubleDiscountPercent;
    }

    public void setDoubleDiscountPercent(BigDecimal doubleDiscountPercent) {
        this.doubleDiscountPercent = doubleDiscountPercent;
    }

    public String getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(String itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public TaxItem getTaxItem() {
        return taxItem;
    }

    public void setTaxItem(TaxItem taxItem) {
        this.taxItem = taxItem;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
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

    public TypeItem getType() {
        return type;
    }

    public void setType(TypeItem type) {
        this.type = type;
    }

    public String getProjectBasedInvoiceDesc() {
        return projectBasedInvoiceDesc;
    }

    public void setProjectBasedInvoiceDesc(String projectBasedInvoiceDesc) {
        this.projectBasedInvoiceDesc = projectBasedInvoiceDesc;
    }

    public Integer[] getProjectBasedEntryIds() {
        return projectBasedEntryIds;
    }

    public void setProjectBasedEntryIds(Integer[] projectBasedEntryIds) {
        this.projectBasedEntryIds = projectBasedEntryIds;
    }

    public BigDecimal getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(BigDecimal receivedQty) {
        this.receivedQty = receivedQty;
    }

    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public BigDecimal getRemainingQty() {
        if (getQuantity() == null) {
            return BigDecimal.ZERO;
        }
        return getQuantity().subtract(getReceivedQty() != null ? getReceivedQty() : BigDecimal.ZERO);
    }

    public BigDecimal getRemainingAmount2() {
        if (getNet() == null) {
            return BigDecimal.ZERO;
        }

        return getNet().subtract(getReceivedAmount() != null ? getReceivedAmount() : BigDecimal.ZERO);
    }

    public BigDecimal getRemainingAmount() {
        if (getNet() == null) {
            return BigDecimal.ZERO;
        }
        return getNet().multiply(getRemainingQty().divide(getQuantity(), 16, RoundingMode.HALF_UP));
    }

    public BigDecimal getNonConvertedQty() {
        if (getReceivedQty() == null) {
            return BigDecimal.ZERO;
        }
        return (getReceivedQty() != null ? getReceivedQty() : BigDecimal.ZERO).subtract(getConvertedQty() != null ? getConvertedQty() : BigDecimal.ZERO);
    }

    public BigDecimal getNonConvertedQty2(boolean isGDN) {
        if (getShippedQty() == null && isGDN) {
            return BigDecimal.ZERO;
        }
        return (getShippedQty() != null && getShippedQty().compareTo(BigDecimal.ZERO) != 0 ? getShippedQty() : getQuantity()).subtract(getConvertedQty() != null ? getConvertedQty() : BigDecimal.ZERO);
    }

    public BigDecimal getNonConvertedAmount() {
        if (getReceivedAmount() == null) {
            return BigDecimal.ZERO;
        }

        return (getReceivedAmount() != null ? getReceivedAmount() : BigDecimal.ZERO).subtract(getConvertedAmount() != null ? getConvertedAmount() : BigDecimal.ZERO);
    }

    public SelectItem getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(SelectItem warehouse) {
        this.warehouse = warehouse;
    }

    public AccountItem getAccountItem() {
        return accountItem;
    }

    public void setAccountItem(AccountItem accountItem) {
        this.accountItem = accountItem;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount != null ? discountAmount : BigDecimal.ZERO;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public SelectItem getDepartmentItem() {
        return departmentItem;
    }

    public void setDepartmentItem(SelectItem departmentItem) {
        this.departmentItem = departmentItem;
    }

    public Integer getDiscountItemStaticType() {
        return discountItemStaticType;
    }

    public void setDiscountItemStaticType(Integer discountItemStaticType) {
        this.discountItemStaticType = discountItemStaticType;
    }

    public BigDecimal getDoubleDiscountAmount() {
        return doubleDiscountAmount != null ? doubleDiscountAmount : BigDecimal.ZERO;
    }

    public void setDoubleDiscountAmount(BigDecimal doubleDiscountAmount) {
        this.doubleDiscountAmount = doubleDiscountAmount;
    }

    public BigDecimal getCurrentProductDiscountAmount() {
        return currentProductDiscountAmount != null ? currentProductDiscountAmount : BigDecimal.ZERO;
    }

    public void setCurrentProductDiscountAmount(BigDecimal currentProductDiscountAmount) {
        this.currentProductDiscountAmount = currentProductDiscountAmount;
    }

    public Integer getItemDiscountID() {
        return itemDiscountID;
    }

    public void setItemDiscountID(Integer itemDiscountID) {
        this.itemDiscountID = itemDiscountID;
    }

    public Integer getItemDoubleDiscountID() {
        return itemDoubleDiscountID;
    }

    public void setItemDoubleDiscountID(Integer itemDoubleDiscountID) {
        this.itemDoubleDiscountID = itemDoubleDiscountID;
    }

    public String getItemDoubleDiscount() {
        return itemDoubleDiscount;
    }

    public void setItemDoubleDiscount(String itemDoubleDiscount) {
        this.itemDoubleDiscount = itemDoubleDiscount;
    }

    public DiscountItem[] getItemDiscountList() {
        return itemDiscountList;
    }

    public void setItemDiscountList(DiscountItem[] itemDiscountList) {
        this.itemDiscountList = itemDiscountList;
    }

    public BigDecimal getComission() {
        return comission;
    }

    public void setComission(BigDecimal comission) {
        this.comission = comission;
    }

    public BigDecimal getReceive() {
        return receive;
    }

    public void setReceive(BigDecimal receive) {
        this.receive = receive;
    }

    public BigDecimal getAllocatedExpense() {
        return allocatedExpense;
    }

    public void setAllocatedExpense(BigDecimal allocatedExpense) {
        this.allocatedExpense = allocatedExpense;
    }

    public BigDecimal getReceivedAllocation() {
        return receivedAllocation;
    }

    public void setReceivedAllocation(BigDecimal receivedAllocation) {
        this.receivedAllocation = receivedAllocation;
    }

    public SelectItem getMeasurement() {
        return measurement;
    }

    public void setMeasurement(SelectItem measurement) {
        this.measurement = measurement;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getFullItemName() {
        return fullItemName;
    }

    public void setFullItemName(String fullItemName) {
        this.fullItemName = fullItemName;
    }

    public BigDecimal getPriceLevelAmount() {
        return priceLevelAmount;
    }

    public void setPriceLevelAmount(BigDecimal priceLevelAmount) {
        this.priceLevelAmount = priceLevelAmount;
    }

    public ProductSerialItem[] getAssignedSerials() {
        return assignedSerials;
    }

    public void setAssignedSerials(ProductSerialItem[] assignedSerials) {
        this.assignedSerials = assignedSerials;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Boolean isProductPurchasedFromSupplier() {
        return isProductPurchasedFromSupplier != null ? isProductPurchasedFromSupplier : false;
    }

    public void setProductPurchasedFromSupplier(Boolean productPurchasedFromSupplier) {
        isProductPurchasedFromSupplier = productPurchasedFromSupplier;
    }

    public Boolean isHasInventoryInProductKit() {
        return hasInventoryInProductKit != null ? hasInventoryInProductKit : false;
    }

    public void setHasInventoryInProductKit(Boolean hasInventoryInProductKit) {
        this.hasInventoryInProductKit = hasInventoryInProductKit;
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

    public ArrayList<String> getSerials() {
        return serials;
    }

    public void setSerials(ArrayList<String> serials) {
        this.serials = serials;
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

    public boolean isIntegerQuantity() {
        if (quantity == null) {
            return false;
        }
        return quantity.subtract(new BigDecimal(quantity.intValue())).compareTo(BigDecimal.ZERO) == 0;
    }

    public TaxItem getDoubleTaxItem() {
        return doubleTaxItem;
    }

    public void setDoubleTaxItem(TaxItem doubleTaxItem) {
        this.doubleTaxItem = doubleTaxItem;
    }

    public BigDecimal getDoubleTaxAmount() {
        return doubleTaxAmount;
    }

    public void setDoubleTaxAmount(BigDecimal doubleTaxAmount) {
        this.doubleTaxAmount = doubleTaxAmount;
    }

    public String getQbItemId() {
        return qbItemId;
    }

    public void setQbItemId(String qbItemId) {
        this.qbItemId = qbItemId;
    }

    public BigDecimal getConvertedQty() {
        return convertedQty;
    }

    public void setConvertedQty(BigDecimal convertedQty) {
        this.convertedQty = convertedQty;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public Integer getQuoteItemId() {
        return quoteItemId;
    }

    public void setQuoteItemId(Integer quoteItemId) {
        this.quoteItemId = quoteItemId;
    }

    public Integer getSaleInvoiceId() {
        return saleInvoiceId;
    }

    public void setSaleInvoiceId(Integer saleInvoiceId) {
        this.saleInvoiceId = saleInvoiceId;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Integer supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public SelectItem getParentProject() {
        return parentProject;
    }

    public void setParentProject(SelectItem parentProject) {
        this.parentProject = parentProject;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public Integer getProductBrandID() {
        return productBrandID;
    }

    public void setProductBrandID(Integer productBrandID) {
        this.productBrandID = productBrandID;
    }

    public ReceiveTypeEnum getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(ReceiveTypeEnum receiveType) {
        this.receiveType = receiveType;
    }

    public BigDecimal getQtyWithHighScale() {
        return qtyWithHighScale;
    }

    public void setQtyWithHighScale(BigDecimal qtyWithHighScale) {
        this.qtyWithHighScale = qtyWithHighScale;
    }

    public HashMap<String, BigDecimal> getMultiPricesMap() {
        if (multiPricesMap == null) {
            multiPricesMap = new HashMap<>();
        }
        return multiPricesMap;
    }

    public BigDecimal getShippedQty() {
        return shippedQty;
    }

    public void setShippedQty(BigDecimal shippedQty) {
        this.shippedQty = shippedQty;
    }

    public void setMultiPricesMap(HashMap<String, BigDecimal> multiPricesMap) {
        this.multiPricesMap = multiPricesMap;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public SelectItem getClient() {
        return client;
    }

    public void setClient(SelectItem client) {
        this.client = client;
    }

    public boolean isFromTimesheet() {
        return fromTimesheet;
    }

    public void setFromTimesheet(boolean fromTimesheet) {
        this.fromTimesheet = fromTimesheet;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public CompanyCustomFieldItem getCustomFieldByCode(String columnCode) {

        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            for (CompanyCustomFieldItem fieldItem : customFieldItems) {

                if (columnCode.equals(fieldItem.getColumnCode())) {
                    return fieldItem;
                }
            }
        }

        return null;
    }

    public CompanyCustomFieldItem getCustomFieldByAlias(String aliasName) {

        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            for (CompanyCustomFieldItem fieldItem : customFieldItems) {

                if (aliasName.equals(fieldItem.getAliasName())) {
                    return fieldItem;
                }
            }
        }

        return null;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public boolean isPickable() {
        return isPickable;
    }

    public void setPickable(boolean pickable) {
        this.isPickable = pickable;
    }

    public boolean isSoldOut() {
        return soldOut;
    }

    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
    }

    public ArrayList<FileItem> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<FileItem> attachment) {
        this.attachments = attachments;
    }

    public AccountItem getSalesAccount() {
        return salesAccount;
    }

    public void setSalesAccount(AccountItem salesAccount) {
        this.salesAccount = salesAccount;
    }

    public BigDecimal getItemAverageCost() {
        return this.itemAverageCost;
    }

    public void setItemAverageCost(final BigDecimal itemAverageCost) {
        this.itemAverageCost = itemAverageCost;
    }

    public DateNonConvertable getFromDate() {
        return fromDate;
    }

    public void setFromDate(DateNonConvertable fromDate) {
        this.fromDate = fromDate;
    }

    public DateNonConvertable getToDate() {
        return toDate;
    }

    public void setToDate(DateNonConvertable toDate) {
        this.toDate = toDate;
    }

    public Integer getExpanceItemId() {
        return expanceItemId;
    }

    public void setExpanceItemId(Integer expanceItemId) {
        this.expanceItemId = expanceItemId;
    }

    public ShippingDataItem getUsedInGrn() {
        return usedInGrn;
    }

    public void setUsedInGrn(ShippingDataItem usedInGrn) {
        this.usedInGrn = usedInGrn;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setFaiCategoryId(Integer faiCategoryId) {
        this.faiCategoryId = faiCategoryId;
    }

    public Integer getFaiCategoryId() {
        return faiCategoryId;
    }

    public SelectItem getFaiCategory() {
        return faiCategory;
    }

    public void setFaiCategory(SelectItem faiCategory) {
        this.faiCategory = faiCategory;
    }

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }

    public static NewInvoiceItem copyData(NewInvoiceItem item) {
        if (item == null)
            throw new IllegalArgumentException("The item to copy cannot be null.");

        NewInvoiceItem newItem = new NewInvoiceItem();

        newItem.setID(item.getID());
        newItem.setNet(item.getNet());
        newItem.setType(item.getType());
        newItem.setItemID(item.getItemID());
        newItem.setToDate(item.getToDate());
        newItem.setClient(item.getClient());
        newItem.setLumpsum(item.isLumpsum());
        newItem.setSoldOut(item.isSoldOut());
        newItem.setReceive(item.getReceive());
        newItem.setSerials(item.getSerials());
        newItem.setProject(item.getProject());
        newItem.setTaxItem(item.getTaxItem());
        newItem.setPickable(item.isPickable());
        newItem.setFromDate(item.getFromDate());
        newItem.setQbItemId(item.getQbItemId());
        newItem.setItemName(item.getItemName());
        newItem.setItemType(item.getItemType());
        newItem.setQuantity(item.getQuantity());
        newItem.setUnitCost(item.getUnitCost());
        newItem.setComission(item.getComission());
        newItem.setAccountID(item.getAccountID());
        newItem.setUnitPrice(item.getUnitPrice());
        newItem.setUsedInGrn(item.getUsedInGrn());
        newItem.setWarehouse(item.getWarehouse());
        newItem.setTaxAmount(item.getTaxAmount());
        newItem.setShortLink(item.getShortLink());
        newItem.setSupplierID(item.getSupplierID());
        newItem.setItemNumber(item.getItemNumber());
        newItem.setBatchItems(item.getBatchItems());
        newItem.setShippedQty(item.getShippedQty());
        newItem.setQuoteItemId(item.getQuoteItemId());
        newItem.setAccountItem(item.getAccountItem());
        newItem.setDescription(item.getDescription());
        newItem.setTotalAmount(item.getTotalAmount());
        newItem.setAttachments(item.getAttachments());
        newItem.setProductType(item.getProductType());
        newItem.setItemBarcode(item.getItemBarcode());
        newItem.setReceivedQty(item.getReceivedQty());
        newItem.setReceiveType(item.getReceiveType());
        newItem.setMeasurement(item.getMeasurement());
        newItem.setAccountName(item.getAccountName());
        newItem.setSalesAccount(item.getSalesAccount());
        newItem.setConvertedQty(item.getConvertedQty());
        newItem.setProductBrand(item.getProductBrand());
        newItem.setItemCategory(item.getItemCategory());
        newItem.setSupplierName(item.getSupplierName());
        newItem.setFullItemName(item.getFullItemName());
        newItem.setItemDiscount(item.getItemDiscount());
        newItem.setFromTimesheet(item.isFromTimesheet());
        newItem.setParentProject(item.getParentProject());
        newItem.setSaleInvoiceId(item.getSaleInvoiceId());
        newItem.setExpanceItemId(item.getExpanceItemId());
        newItem.setDoubleTaxItem(item.getDoubleTaxItem());
        newItem.setReceivedAmount(item.getReceivedAmount());
        newItem.setProductBrandID(item.getProductBrandID());
        newItem.setDepartmentItem(item.getDepartmentItem());
        newItem.setMultiPricesMap(item.getMultiPricesMap());
        newItem.setItemDiscountID(item.getItemDiscountID());
        newItem.setDiscountAmount(item.getDiscountAmount());
        newItem.setDiscountPercent(item.getDiscountPercent());
        newItem.setAssignedSerials(item.getAssignedSerials());
        newItem.setDoubleTaxAmount(item.getDoubleTaxAmount());
        newItem.setItemsInStockQty(item.getItemsInStockQty());
        newItem.setItemAverageCost(item.getItemAverageCost());
        newItem.setConvertedAmount(item.getConvertedAmount());
        newItem.setCustomFieldItems(item.getCustomFieldItems());
        newItem.setQtyWithHighScale(item.getQtyWithHighScale());
        newItem.setItemDiscountList(item.getItemDiscountList());
        newItem.setAllocatedExpense(item.getAllocatedExpense());
        newItem.setPriceLevelAmount(item.getPriceLevelAmount());
        newItem.setCustomDescription(item.getCustomDescription());
        newItem.setItemOriginalPrice(item.getItemOriginalPrice());
        newItem.setReceivedAllocation(item.getReceivedAllocation());
        newItem.setItemDoubleDiscount(item.getItemDoubleDiscount());
        newItem.setTrackBatchesEnabled(item.getTrackBatchesEnabled());
        newItem.setProjectBasedEntryIds(item.getProjectBasedEntryIds());
        newItem.setBatchTrackingEnabled(item.getBatchTrackingEnabled());
        newItem.setItemDoubleDiscountID(item.getItemDoubleDiscountID());
        newItem.setDoubleDiscountAmount(item.getDoubleDiscountAmount());
        newItem.setDoubleDiscountPercent(item.getDoubleDiscountPercent());
        newItem.setDiscountItemStaticType(item.getDiscountItemStaticType());
        newItem.setProjectBasedInvoiceDesc(item.getProjectBasedInvoiceDesc());
        newItem.setHasInventoryInProductKit(item.isHasInventoryInProductKit());
        newItem.setInventoryTrackingEnabled(item.getInventoryTrackingEnabled());
        newItem.setProductPurchasedFromSupplier(item.isProductPurchasedFromSupplier());
        newItem.setCurrentProductDiscountAmount(item.getCurrentProductDiscountAmount());
        newItem.setUuid(item.getUuid());

        return newItem;
    }
}
