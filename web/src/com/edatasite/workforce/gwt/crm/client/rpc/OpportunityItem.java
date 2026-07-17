package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 5/27/11
 * Time: 6:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class OpportunityItem extends SelectItem {

    private Integer opportunityID;
    private Integer itemID;
    private String itemName;
    private String itemNumber;
    private BigDecimal price;
    private BigDecimal discountPercent;//discount percent
    private BigDecimal discountAmount;
    private BigDecimal qty;
    private BigDecimal qtyOnHand;
    private Integer supplierID;
    private String supplierName;
    private SelectItem unitMeasurement;
    private SelectItem project;
    private DiscountItem[] discountItems;
    private Integer discountItemID;
    private String discountItemName;
    private Integer discountItemFixedType;
    private String currency;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;
    private SelectItem productCategory;
    private SelectItem productBrand;
    private TaxItem taxItem;
    private BigDecimal taxAmount;
    private BigDecimal net;
    private BigDecimal subTotal;

    public Integer getOpportunityID() {
        return opportunityID;
    }

    public void setOpportunityID(Integer opportunityID) {
        this.opportunityID = opportunityID;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount != null ? discountAmount : BigDecimal.ZERO;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getQtyOnHand() {
        return this.qtyOnHand;
    }

    public void setQtyOnHand(final BigDecimal qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
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

    public SelectItem getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(SelectItem unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public DiscountItem[] getDiscountItems() {
        return discountItems;
    }

    public void setDiscountItems(DiscountItem[] discountItems) {
        this.discountItems = discountItems;
    }

    public Integer getDiscountItemID() {
        return discountItemID;
    }

    public void setDiscountItemID(Integer discountItemID) {
        this.discountItemID = discountItemID;
    }

    public String getDiscountItemName() {
        return discountItemName;
    }

    public void setDiscountItemName(String discountItemName) {
        this.discountItemName = discountItemName;
    }

    public void setDiscountItemFixedType(Integer discountItemFixedType) {
        this.discountItemFixedType = discountItemFixedType;
    }

    public Integer getDiscountItemFixedType() {
        return discountItemFixedType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LinkedHashMap<String, CompanyCustomFieldItem> getCustomFieldValuesAsMap() {
        if (getItemCustomFields() != null) {
            LinkedHashMap<String, CompanyCustomFieldItem> map = new LinkedHashMap<>();
            for (CompanyCustomFieldItem item : getItemCustomFields()) {
                map.put(item.getColumnCode(), item);
            }
            return map;
        }
        return null;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    public SelectItem getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(SelectItem productCategory) {
        this.productCategory = productCategory;
    }

    public SelectItem getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(SelectItem productBrand) {
        this.productBrand = productBrand;
    }

    public TaxItem getTaxItem() {
        return taxItem;
    }

    public void setTaxItem(TaxItem taxItem) {
        this.taxItem = taxItem;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public SelectItem getProject() {
        return this.project;
    }

    public void setProject(final SelectItem project) {
        this.project = project;
    }
}
