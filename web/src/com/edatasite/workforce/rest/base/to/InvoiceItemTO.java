package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilshod Madrahimov.
 */
public class InvoiceItemTO implements IsSerializable {

    Integer id;
    String name;
    String description;
    ItemTO item;
    SelectItemTO account;
    SelectItemTO warehouse;
    SelectItemTO project;
    SelectItemTO department;

    SelectItemTO tax;
    BigDecimal taxAmount;

    SelectItemTO doubleTax;
    BigDecimal doubleTaxAmount;

    SelectItemTO measurement;

    BigDecimal unitPrice;
    BigDecimal unitCost;
    BigDecimal baseUnitPrice;
    BigDecimal qty;
    BigDecimal netAmount;
    BigDecimal totalAmount;

    SelectItemTO discount;
    BigDecimal discountPercent;
    BigDecimal discountAmount;

    SelectItemTO doubleDiscount;
    BigDecimal doubleDiscountAmount;
    BigDecimal doubleDiscountPercent;

    private BigDecimal receivedQty;
    private BigDecimal receivedAmount;
    private BigDecimal receive; //this is for the partial receive PO
    private BigDecimal allocatedExpense;
    private BigDecimal receivedAllocation;
    private ReceiveTypeEnum receiveType;

    private BigDecimal convertedQty; // for progress invoicing by item
    private BigDecimal convertedAmount;
    private Integer quoteItemId; //for progress invoicing by item
    private Integer saleInvoiceId; //for purchase invoice used as billable expense


    private SelectItemTO parentProject;
    private String productBrand;
    private Integer productBrandID;

    private BigDecimal qtyWithHighScale;//for progress invoicing by amount

    private BigDecimal net;
    private Integer itemType;
    private String itemCategory;
    private Integer supplierID;
    private String supplierName;
    private SelectItemTO client;

    private Integer productType;
    private Boolean isProductPurchasedFromSupplier;
    private Boolean hasInventoryInProductKit;

    private BigDecimal priceLevelAmount;

    private String projectBasedInvoiceDesc;
    private Integer[] projectBasedEntryIds;
    private TaxItem doubleTaxItem;

    private String qbItemId;
    private String itemBarcode;
    private boolean fromTimesheet;
    private List<CustomFieldTO> customFieldItems;

    public InvoiceItemTO() {
    }

    public InvoiceItemTO(NewInvoiceItem invoiceItem) {
        this.id = invoiceItem.getItemID();
        this.name = invoiceItem.getItemName();
        this.description = invoiceItem.getDescription();
        this.unitPrice = invoiceItem.getUnitPrice();
        this.baseUnitPrice = invoiceItem.getItemOriginalPrice();
        this.unitCost = invoiceItem.getUnitCost();
        this.qty = invoiceItem.getQuantity();
        this.item = new ItemTO(invoiceItem.getItemID(), invoiceItem.getItemName(), invoiceItem.getItemNumber(), "");
        this.netAmount = invoiceItem.getNet();
        this.account = new SelectItemTO(invoiceItem.getAccountID(), invoiceItem.getAccountName());
        this.totalAmount = invoiceItem.getTotalAmount();
        this.warehouse = WrapUtils.wrapSelectItemTO(invoiceItem.getWarehouse());

        this.tax = WrapUtils.wrapSelectItemTO(invoiceItem.getTaxItem());
        this.taxAmount = invoiceItem.getTaxAmount();

        this.doubleTax = WrapUtils.wrapSelectItemTO(invoiceItem.getDoubleTaxItem());
        this.doubleTaxAmount = invoiceItem.getDoubleTaxAmount();

        this.discountPercent = invoiceItem.getDiscountPercent();
        this.discountAmount = invoiceItem.getDiscountAmount();
        if (invoiceItem.getItemDiscountID() != null) {
            this.discount = new SelectItemTO(invoiceItem.getItemDiscountID(), invoiceItem.getItemDiscount());
        }

        this.doubleDiscountAmount = invoiceItem.getDoubleDiscountAmount();
        this.doubleDiscountPercent = invoiceItem.getDoubleDiscountPercent();
        if (invoiceItem.getItemDoubleDiscountID() != null) {
            this.doubleDiscount = new SelectItemTO(invoiceItem.getItemDoubleDiscountID(), invoiceItem.getItemDoubleDiscount());
        }

        this.project = WrapUtils.wrapSelectItemTO(invoiceItem.getProject());
        this.department = WrapUtils.wrapSelectItemTO(invoiceItem.getDepartmentItem());
        this.measurement = WrapUtils.wrapSelectItemTO(invoiceItem.getMeasurement());

        this.receivedQty = invoiceItem.getReceivedQty();
        this.receivedAmount = invoiceItem.getReceivedAmount();
        this.receive = invoiceItem.getReceive();
        this.allocatedExpense = invoiceItem.getAllocatedExpense();
        this.receivedAllocation = invoiceItem.getReceivedAllocation();
        this.receiveType = invoiceItem.getReceiveType();
        this.convertedQty = invoiceItem.getConvertedQty();
        this.convertedAmount = invoiceItem.getConvertedAmount();
        this.quoteItemId = invoiceItem.getQuoteItemId();
        this.saleInvoiceId = invoiceItem.getSaleInvoiceId();
        this.parentProject = WrapUtils.wrapSelectItemTO(invoiceItem.getParentProject());
        this.productBrand = invoiceItem.getProductBrand();
        this.productBrandID = invoiceItem.getProductBrandID();
        this.qtyWithHighScale = invoiceItem.getQtyWithHighScale();

        this.net = invoiceItem.getNet();
        this.itemType = invoiceItem.getItemType();
        this.itemCategory = invoiceItem.getItemCategory();
        this.supplierID = invoiceItem.getSupplierID();
        this.supplierName = invoiceItem.getSupplierName();
        this.client = WrapUtils.wrapSelectItemTO(invoiceItem.getClient());
        this.productType = invoiceItem.getProductType();
        this.isProductPurchasedFromSupplier = invoiceItem.isProductPurchasedFromSupplier();
        this.hasInventoryInProductKit = invoiceItem.isHasInventoryInProductKit();
        this.priceLevelAmount = invoiceItem.getPriceLevelAmount();
        this.projectBasedInvoiceDesc = invoiceItem.getProjectBasedInvoiceDesc();
        this.projectBasedEntryIds = invoiceItem.getProjectBasedEntryIds();
        this.qbItemId = invoiceItem.getQbItemId();
        this.itemBarcode = invoiceItem.getItemBarcode();
        this.fromTimesheet = invoiceItem.isFromTimesheet();
        if (invoiceItem.getCustomFieldItems() != null && invoiceItem.getCustomFieldItems().size() > 0) {
            this.customFieldItems = convertToCustomFieldTO(invoiceItem.getCustomFieldItems());
        }

    }

    private List<CustomFieldTO> convertToCustomFieldTO(List<CompanyCustomFieldItem> customFieldItems) {
        List<CustomFieldTO> fieldTOS = new ArrayList<>();
        for (CompanyCustomFieldItem item : customFieldItems) {
            fieldTOS.add(new CustomFieldTO(item));

        }
        return fieldTOS;
    }

    private ArrayList<CompanyCustomFieldItem> convertFromCustomFieldTO(List<CustomFieldTO> customFieldTOs) {
        ArrayList<CompanyCustomFieldItem> customFields = new ArrayList<>();
        for (CustomFieldTO customFieldTO : customFieldTOs) {
            CompanyCustomFieldItem customField = customFieldTO.convertToCustomField();
            customFields.add(customField);
        }
        return customFields;
    }

    public NewInvoiceItem wrap(InvoiceItemTO invoiceItemTO) {
        NewInvoiceItem invoiceItem = new NewInvoiceItem();
        invoiceItem.setID(invoiceItemTO.getId());
        invoiceItem.setItemID(invoiceItemTO.getId());
        invoiceItem.setItemName(invoiceItemTO.getName());
        invoiceItem.setDescription(invoiceItemTO.getDescription());
        invoiceItem.setUnitPrice(invoiceItemTO.getUnitPrice());
        invoiceItem.setItemOriginalPrice(invoiceItemTO.getBaseUnitPrice());
        invoiceItem.setUnitCost(invoiceItemTO.getUnitCost());
        invoiceItem.setQuantity(invoiceItemTO.getQty());
        invoiceItem.setNet(invoiceItemTO.getNetAmount());
        if (invoiceItemTO.getAccount() != null) {
            invoiceItem.setAccountID(invoiceItemTO.getAccount().getId());
        }
        invoiceItem.setTotalAmount(invoiceItemTO.getTotalAmount());
        if (invoiceItemTO.getWarehouse() != null) {
            invoiceItem.setWarehouse(invoiceItemTO.getWarehouse().wrap(invoiceItemTO.getWarehouse()));
        }
        if (invoiceItemTO.getTax() != null) {
            TaxItem taxItem = new TaxItem(invoiceItemTO.getTax().getId(), invoiceItemTO.getTax().getName());
            invoiceItem.setTaxItem(taxItem);
        }
        invoiceItem.setTaxAmount(invoiceItemTO.getTaxAmount());
        if (invoiceItemTO.getDoubleTax() != null) {
            TaxItem doubleTaxItem = new TaxItem(invoiceItemTO.getDoubleTax().getId(), invoiceItemTO.getDoubleTax().getName());
            invoiceItem.setDoubleTaxItem(doubleTaxItem);
        }
        invoiceItem.setDoubleTaxAmount(invoiceItemTO.getDoubleTaxAmount());

        invoiceItem.setDiscountPercent(invoiceItemTO.getDiscountPercent());
        invoiceItem.setDiscountAmount(invoiceItemTO.getDiscountAmount());
        if (invoiceItemTO.getDiscount() != null) {
            invoiceItem.setItemDiscountID(invoiceItemTO.getDiscount().getId());
        }
        if (invoiceItemTO.getProject() != null) {
            invoiceItem.setProject(invoiceItemTO.getProject().wrap(invoiceItemTO.getProject()));
        }
        if (invoiceItemTO.getDepartment() != null) {
            invoiceItem.setDepartmentItem(invoiceItemTO.getDepartment().wrap(invoiceItemTO.getDepartment()));
        }
        if (invoiceItemTO.getMeasurement() != null) {
            invoiceItem.setMeasurement(invoiceItemTO.getMeasurement().wrap(invoiceItemTO.getMeasurement()));
        }
        invoiceItem.setReceivedQty(invoiceItemTO.getReceivedQty());
        invoiceItem.setReceivedAmount(invoiceItemTO.getReceivedAmount());
        invoiceItem.setReceive(invoiceItemTO.getReceive());
        invoiceItem.setAllocatedExpense(invoiceItemTO.getAllocatedExpense());
        invoiceItem.setReceivedAllocation(invoiceItemTO.getReceivedAllocation());
        invoiceItem.setReceiveType(invoiceItemTO.getReceiveType());
        invoiceItem.setConvertedQty(invoiceItemTO.getConvertedQty());
        invoiceItem.setConvertedAmount(invoiceItemTO.getConvertedAmount());
        invoiceItem.setQuoteItemId(invoiceItemTO.getQuoteItemId());
        invoiceItem.setSaleInvoiceId(invoiceItemTO.getSaleInvoiceId());
        if (invoiceItemTO.getParentProject() != null) {
            invoiceItem.setParentProject(invoiceItemTO.getParentProject().wrap(invoiceItemTO.getParentProject()));
        }
        invoiceItem.setProductBrand(invoiceItemTO.getProductBrand());
        invoiceItem.setProductBrandID(invoiceItemTO.getProductBrandID());
        invoiceItem.setQtyWithHighScale(invoiceItemTO.getQtyWithHighScale());
        invoiceItem.setNet(invoiceItemTO.getNet());
        invoiceItem.setItemType(invoiceItemTO.getItemType());
        invoiceItem.setItemCategory(invoiceItemTO.getItemCategory());
        invoiceItem.setSupplierID(invoiceItemTO.getSupplierID());
        invoiceItem.setSupplierName(invoiceItemTO.getSupplierName());
        if (invoiceItemTO.getClient() != null) {
            invoiceItem.setClient(invoiceItemTO.getClient().wrap(invoiceItemTO.getClient()));
        }
        invoiceItem.setProductType(invoiceItemTO.getProductType());
        invoiceItem.setProductPurchasedFromSupplier(invoiceItemTO.getProductPurchasedFromSupplier());
        invoiceItem.setHasInventoryInProductKit(invoiceItemTO.getHasInventoryInProductKit());
        invoiceItem.setPriceLevelAmount(invoiceItemTO.getPriceLevelAmount());
        invoiceItem.setProjectBasedInvoiceDesc(invoiceItemTO.getProjectBasedInvoiceDesc());
        invoiceItem.setProjectBasedEntryIds(invoiceItemTO.getProjectBasedEntryIds());
        invoiceItem.setQbItemId(invoiceItemTO.getQbItemId());
        invoiceItem.setItemBarcode(invoiceItemTO.getItemBarcode());
        invoiceItem.setFromTimesheet(invoiceItemTO.isFromTimesheet());
        if (invoiceItemTO.getCustomFieldItems() != null && invoiceItemTO.getCustomFieldItems().size() > 0) {
            invoiceItem.setCustomFieldItems(convertFromCustomFieldTO(invoiceItemTO.getCustomFieldItems()));
        }

        return invoiceItem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public ItemTO getItem() {
        return item;
    }

    public void setItem(ItemTO item) {
        this.item = item;
    }

    public SelectItemTO getAccount() {
        return account;
    }

    public void setAccount(SelectItemTO account) {
        this.account = account;
    }

    public SelectItemTO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(SelectItemTO warehouse) {
        this.warehouse = warehouse;
    }

    public SelectItemTO getTax() {
        return tax;
    }

    public void setTax(SelectItemTO tax) {
        this.tax = tax;
    }

    public SelectItemTO getDoubleTax() {
        return doubleTax;
    }

    public void setDoubleTax(SelectItemTO doubleTax) {
        this.doubleTax = doubleTax;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public SelectItemTO getDiscount() {
        return discount;
    }

    public void setDiscount(SelectItemTO discount) {
        this.discount = discount;
    }

    public SelectItemTO getProject() {
        return project;
    }

    public void setProject(SelectItemTO project) {
        this.project = project;
    }

    public SelectItemTO getDepartment() {
        return department;
    }

    public void setDepartment(SelectItemTO department) {
        this.department = department;
    }

    public BigDecimal getBaseUnitPrice() {
        return baseUnitPrice;
    }

    public void setBaseUnitPrice(BigDecimal baseUnitPrice) {
        this.baseUnitPrice = baseUnitPrice;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getDoubleTaxAmount() {
        return doubleTaxAmount;
    }

    public void setDoubleTaxAmount(BigDecimal doubleTaxAmount) {
        this.doubleTaxAmount = doubleTaxAmount;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public SelectItemTO getMeasurement() {
        return measurement;
    }

    public void setMeasurement(SelectItemTO measurement) {
        this.measurement = measurement;
    }


    public BigDecimal getDoubleDiscountAmount() {
        return doubleDiscountAmount;
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

    public BigDecimal getReceive() {
        return receive;
    }

    public BigDecimal getAllocatedExpense() {
        return allocatedExpense;
    }

    public BigDecimal getReceivedAllocation() {
        return receivedAllocation;
    }

    public ReceiveTypeEnum getReceiveType() {
        return receiveType;
    }

    public BigDecimal getConvertedQty() {
        return convertedQty;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }


    public Integer getQuoteItemId() {
        return quoteItemId;
    }

    public Integer getSaleInvoiceId() {
        return saleInvoiceId;
    }

    public SelectItemTO getParentProject() {
        return parentProject;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public Integer getProductBrandID() {
        return productBrandID;
    }

    public BigDecimal getQtyWithHighScale() {
        return qtyWithHighScale;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public Integer getItemType() {
        return itemType;
    }

    public String getItemCategory() {
        return itemCategory;
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

    public SelectItemTO getClient() {
        return client;
    }

    public void setClient(SelectItemTO client) {
        this.client = client;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Boolean getProductPurchasedFromSupplier() {
        return isProductPurchasedFromSupplier;
    }

    public Boolean getHasInventoryInProductKit() {
        return hasInventoryInProductKit;
    }

    public BigDecimal getPriceLevelAmount() {
        return priceLevelAmount;
    }

    public String getProjectBasedInvoiceDesc() {
        return projectBasedInvoiceDesc;
    }

    public Integer[] getProjectBasedEntryIds() {
        return projectBasedEntryIds;
    }

    public TaxItem getDoubleTaxItem() {
        return doubleTaxItem;
    }

    public String getQbItemId() {
        return qbItemId;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public boolean isFromTimesheet() {
        return fromTimesheet;
    }

    public List<CustomFieldTO> getCustomFieldItems() {
        return customFieldItems;
    }
}
