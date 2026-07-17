package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 04.05.2019
 * Time: 16:58:06
 * To change this template use File | Settings | File Templates.
 */
public class BillOfEntryItem implements Serializable {


    private Integer id;//e.g. objectID of the product.
    private BigDecimal assessableValue;//Assessable Value( Quantity x Rate ) + Additional Charges if any
    private BigDecimal customDutyAdditionalCharges;//Custom Duty + Additional Charges
    private BigDecimal taxableAmount;//Taxable Amount
    private TaxItem tax;

    private Integer itemID;
    private String itemName;
    private String fullItemName;
    private String itemNumber;
    private Integer itemType;
    private String itemCategory;

    private Boolean deleted = false;

    private Integer sorder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAssessableValue() {
        return assessableValue!=null ? assessableValue : BigDecimal.ZERO;
    }

    public void setAssessableValue(BigDecimal assessableValue) {
        this.assessableValue = assessableValue;
    }

    public BigDecimal getCustomDutyAdditionalCharges() {
        return customDutyAdditionalCharges!=null ? customDutyAdditionalCharges : BigDecimal.ZERO;
    }

    public void setCustomDutyAdditionalCharges(BigDecimal customDutyAdditionalCharges) {
        this.customDutyAdditionalCharges = customDutyAdditionalCharges;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount!=null ? taxableAmount : BigDecimal.ZERO;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
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

    public String getFullItemName() {
        return fullItemName;
    }

    public void setFullItemName(String fullItemName) {
        this.fullItemName = fullItemName;
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

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public TaxItem getTax() {
        return tax;
    }

    public void setTax(TaxItem tax) {
        this.tax = tax;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }
}
