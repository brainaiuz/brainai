package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * User: Anvar Akramov
 * Date: 04.05.2019
 * Time: 15:10:59
 */
public class BillOfEntry implements Serializable {

    private Integer objectID;

    //    private NewInvoice purchaseInvoice;
    private Integer purchaseInvoiceId;

    private String boeNumber;
    private Integer portId;
    private String portCode;
    private String portName;
    private Date boeDate;
    private String reference;
    private String description;

    private ArrayList<BillOfEntryItem> items;

    //Paid Through
    private SelectItem paidThrough;

    private BigDecimal totalCustomDuty;//Total Custom Duty + Additional Charges
    private BigDecimal totalTaxAmount;//Total Tax Amount
    private BigDecimal totalAmount;//Total Amount (AED) :

    private ArrayList<TaxItem> zeroTaxes;

    public BillOfEntry() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    /*public NewInvoice getPurchaseInvoice() {
        return purchaseInvoice;
    }

    public void setPurchaseInvoice(NewInvoice purchaseInvoice) {
        this.purchaseInvoice = purchaseInvoice;
    }*/

    public Integer getPurchaseInvoiceId() {
        return purchaseInvoiceId;
    }

    public void setPurchaseInvoiceId(Integer purchaseInvoiceId) {
        this.purchaseInvoiceId = purchaseInvoiceId;
    }

    public String getBoeNumber() {
        return boeNumber;
    }

    public void setBoeNumber(String boeNumber) {
        this.boeNumber = boeNumber;
    }

    public Integer getPortId() {
        return portId;
    }

    public void setPortId(Integer portId) {
        this.portId = portId;
    }

    public String getPortCode() {
        return portCode;
    }

    public void setPortCode(String portCode) {
        this.portCode = portCode;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public Date getBoeDate() {
        return boeDate;
    }

    public void setBoeDate(Date boeDate) {
        this.boeDate = boeDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SelectItem getPaidThrough() {
        return paidThrough;
    }

    public void setPaidThrough(SelectItem paidThrough) {
        this.paidThrough = paidThrough;
    }

    public BigDecimal getTotalCustomDuty() {
        return totalCustomDuty;
    }

    public void setTotalCustomDuty(BigDecimal totalCustomDuty) {
        this.totalCustomDuty = totalCustomDuty;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<BillOfEntryItem> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(ArrayList<BillOfEntryItem> items) {
        this.items = items;
    }

    public ArrayList<TaxItem> getZeroTaxes() {
        if (zeroTaxes == null) {
            zeroTaxes = new ArrayList<>();
        }
        return zeroTaxes;
    }

    public void setZeroTaxes(ArrayList<TaxItem> zeroTaxes) {
        this.zeroTaxes = zeroTaxes;
    }
}
