package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.workforcetrack.mobile.rpc.expense.MAccountItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 6/23/11
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "newInvoiceItem")
public class MNewInvoiceItem {

    private Integer objectID;//e.g. objectID of the product.
    private String description;
    private BigDecimal quantity;
    private String unitMeasurement;
    private BigDecimal unitPrice;
    private Integer itemDiscountID;
    private BigDecimal discountPercent;//discount percent
    private BigDecimal discountAmount;
    private BigDecimal unitCost;
    private int accountID;
    private String accountName;
    private MAccountItem accountItem;
    private Integer taxID;
    private String taxName;
    private BigDecimal taxAmount;
    private BigDecimal receivedQty;
    private Integer warehouseID;
    private String warehouseName;
    private Integer locationID;
    private String locationName;
    /**
     * For Tax Exclusive: net = quantity * unitPrice;
     * <p/>
     * For Tax Inclisuve: net = (quantity * unitPrice * 100%) / (100% + tax_percent_value);
     * Tax inclusive is not calculating during a short period.
     */
    private BigDecimal net;

    //Every InvoiceItem has an item which stores data from this invoiceItem.
    // It's for when you choose an invoiceItem must be shown items per it. Now it's not important thing.
    private int itemID;
    private String itemName;
    @XmlElement(name = "itemDiscount")
    private List<MDiscountItem> itemDiscountList;

    private MTypeItem type;

    private BigDecimal totalAmmount;

    private String projectBasedInvoiceDesc;
    private List<Integer> projectBasedEntryIds;

    public MNewInvoiceItem() {
    }

    public MNewInvoiceItem(NewInvoiceItem newInvoiceItem) {
        this.objectID = newInvoiceItem.getID();
        this.description = newInvoiceItem.getDescription();
        this.quantity = newInvoiceItem.getQuantity();
        this.unitPrice = newInvoiceItem.getUnitPrice();
        this.itemDiscountID = newInvoiceItem.getItemDiscountID();
        this.discountPercent = newInvoiceItem.getDiscountPercent();
        this.discountAmount = newInvoiceItem.getDiscountAmount();
        this.unitCost = newInvoiceItem.getUnitCost();
        this.accountID = newInvoiceItem.getAccountID();
        this.accountName = newInvoiceItem.getAccountName();
        this.accountItem = new MAccountItem(newInvoiceItem.getAccountItem());
        if (newInvoiceItem.getTaxItem() != null) {
            this.taxID = newInvoiceItem.getTaxItem().getId();
            this.taxName = newInvoiceItem.getTaxItem().getName();
            this.taxAmount = newInvoiceItem.getTaxItem().getTaxPercent();
        }
        this.receivedQty = newInvoiceItem.getReceivedQty();
//        this.warehouseID = newInvoiceItem.getWarehouseID();
//        this.warehouseName = newInvoiceItem.getWarehouseName();
//        this.locationID = newInvoiceItem.getLocationID();
//        this.locationName = newInvoiceItem.getLocationName();
        this.net = newInvoiceItem.getNet();
        this.itemID = newInvoiceItem.getItemID() != null ? newInvoiceItem.getItemID() : 0;
        this.itemName = newInvoiceItem.getItemName();
        if (newInvoiceItem.getType() != null)
            this.type = new MTypeItem(newInvoiceItem.getType());
        this.totalAmmount = newInvoiceItem.getTotalAmount();
        this.projectBasedInvoiceDesc = newInvoiceItem.getProjectBasedInvoiceDesc();
        if (newInvoiceItem.getProjectBasedEntryIds() != null) {
            this.projectBasedEntryIds = Arrays.asList(newInvoiceItem.getProjectBasedEntryIds());
        }


        if (newInvoiceItem.getItemDiscountList() != null) {
            itemDiscountList = new ArrayList<>();
            for (DiscountItem discountItem : newInvoiceItem.getItemDiscountList()) {
                itemDiscountList.add(new MDiscountItem(discountItem));
            }
        }
    }


    public NewInvoiceItem convertToNewInvoiceItem(NewInvoiceItem newInvoiceItem) {
        if (newInvoiceItem == null) {
            newInvoiceItem = new NewInvoiceItem();
        }
        if (this.getObjectID() != null) {
            newInvoiceItem.setID(this.getObjectID());
        }
        newInvoiceItem.setDescription(this.getDescription());
        newInvoiceItem.setQuantity(this.getQuantity());
        newInvoiceItem.setUnitPrice(this.getUnitPrice());
        newInvoiceItem.setItemDiscountID(this.getItemDiscountID());
        newInvoiceItem.setDiscountPercent(this.getDiscountPercent());
        newInvoiceItem.setDiscountAmount(this.getDiscountAmount());
        newInvoiceItem.setUnitCost(this.getUnitCost());
        newInvoiceItem.setAccountID(this.getAccountID());
        newInvoiceItem.setAccountName(this.getAccountName());
        newInvoiceItem.setAccountItem(this.accountItem !=null ? this.accountItem.convertToAccountItem(null) : null);
        if (this.getTaxID() != null) {
            newInvoiceItem.setTaxItem(new TaxItem(this.getTaxID(), this.getTaxName(), this.getTaxAmount()));
        }
        newInvoiceItem.setReceivedQty(this.getReceivedQty());
//        newInvoiceItem.setWarehouseID(this.getWarehouseID());
//        newInvoiceItem.setWarehouseName(this.getWarehouseName());
//        newInvoiceItem.setLocationID(this.getLocationID());
//        newInvoiceItem.setLocationName(this.getLocationName());
        newInvoiceItem.setNet(this.getNet());
        newInvoiceItem.setItemID(this.getItemID());
        newInvoiceItem.setItemName(this.getItemName());
        newInvoiceItem.setType(this.getType() != null ? this.getType().convertToTypeItem(null) : null);
        newInvoiceItem.setTotalAmount(this.getTotalAmmount());
        newInvoiceItem.setProjectBasedInvoiceDesc(this.getProjectBasedInvoiceDesc());
        if (this.getProjectBasedEntryIds() != null) {
            newInvoiceItem.setProjectBasedEntryIds(this.getProjectBasedEntryIds().toArray(new Integer[0]));
        }

        return newInvoiceItem;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(String unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getItemDiscountID() {
        return itemDiscountID;
    }

    public void setItemDiscountID(Integer itemDiscountID) {
        this.itemDiscountID = itemDiscountID;
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

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public MAccountItem getAccountItem() {
        return accountItem;
    }

    public void setAccountItem(MAccountItem accountItem) {
        this.accountItem = accountItem;
    }

    public Integer getTaxID() {
        return taxID;
    }

    public void setTaxID(Integer taxID) {
        this.taxID = taxID;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(BigDecimal receivedQty) {
        this.receivedQty = receivedQty;
    }

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Integer warehouseID) {
        this.warehouseID = warehouseID;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public MTypeItem getType() {
        return type;
    }

    public void setType(MTypeItem type) {
        this.type = type;
    }

    public BigDecimal getTotalAmmount() {
        return totalAmmount;
    }

    public void setTotalAmmount(BigDecimal totalAmmount) {
        this.totalAmmount = totalAmmount;
    }

    public String getProjectBasedInvoiceDesc() {
        return projectBasedInvoiceDesc;
    }

    public void setProjectBasedInvoiceDesc(String projectBasedInvoiceDesc) {
        this.projectBasedInvoiceDesc = projectBasedInvoiceDesc;
    }

    public List<MDiscountItem> getItemDiscountList() {
        return itemDiscountList;
    }

    public void setItemDiscountList(List<MDiscountItem> itemDiscountList) {
        this.itemDiscountList = itemDiscountList;
    }

    public List<Integer> getProjectBasedEntryIds() {
        return projectBasedEntryIds;
    }

    public void setProjectBasedEntryIds(List<Integer> projectBasedEntryIds) {
        this.projectBasedEntryIds = projectBasedEntryIds;
    }
}