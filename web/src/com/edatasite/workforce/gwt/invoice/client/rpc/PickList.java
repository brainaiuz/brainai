package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 23, 2010
 * Time: 2:47:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class PickList implements IsSerializable {

    public static final String ACTION = "action";
    public static final String STATUS = "status";
    public static final String SHIP_DATE = "shipDate";
    public static final String EXPECTED_DATE = "expectedDate";
    public static final String TOTAL = "total";
    public static final String DISCOUNT = "discount";
    public static final String DUE_DATE = "dueDate";
    public static final String CLIENT = "client";

    private Integer id;
    private Integer clientID;
    private String clientName;
    private BigDecimal discount;
    private BigDecimal total;
    private BigDecimal taxAmount;
    private DateNonConvertable dueDate;
    private DateNonConvertable saleOrderDate;
    private DateNonConvertable shipDate;
    private DateNonConvertable pickDate;
    private DateNonConvertable packDate;
    private DateNonConvertable expectedDate;
    private Integer quoteID;
    private boolean fullyPacked;
    private PickListItem[] items;
    private String carrierAccountID;
    private String status;
    private String orderNumber;
    private String poNumber;
    private CurrencyItem baseCurrency;
    private String layoutHtml;
    private BigDecimal grossWeight;
    private BankTransferNumberData gdnNumberData;
    private String shippingLabel;
    private String gdnNumber;
    private String gdnFourDigitNumber;
    private boolean productSerialsEnabled;
    private HashMap<Integer, ArrayList<ProductSerialItem>> productSerialItems;
    private BigDecimal supplierCustomerBalance;
    private Integer gdnCount;
    private boolean isSalesOrder;
    private String paymentInstruction;
    private String reference;
    private SelectItem[] templates;
    private Integer selectedTemplateId;
    private ColumnConfigs[] itemTableColumns;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public DateNonConvertable getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateNonConvertable dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public DateNonConvertable getShipDate() {
        return shipDate;
    }

    public void setShipDate(DateNonConvertable shipDate) {
        this.shipDate = shipDate;
    }

    public DateNonConvertable getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(DateNonConvertable expectedDate) {
        this.expectedDate = expectedDate;
    }

    public Integer getQuoteID() {
        return quoteID;
    }

    public void setQuoteID(Integer quoteID) {
        this.quoteID = quoteID;
    }

    public boolean isFullyPacked() {
        return fullyPacked;
    }

    public void setFullyPacked(boolean fullyPacked) {
        this.fullyPacked = fullyPacked;
    }

    public PickListItem[] getItems() {
        return items;
    }

    public void setItems(PickListItem[] items) {
        this.items = items;
    }

    public String getCarrierAccountID() {
        return carrierAccountID;
    }

    public void setCarrierAccountID(String carrierAccountID) {
        this.carrierAccountID = carrierAccountID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public CurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getLayoutHtml() {
        return layoutHtml;
    }

    public DateNonConvertable getPickDate() {
        return pickDate;
    }

    public void setPickDate(DateNonConvertable pickDate) {
        this.pickDate = pickDate;
    }

    public DateNonConvertable getPackDate() {
        return packDate;
    }

    public void setPackDate(DateNonConvertable packDate) {
        this.packDate = packDate;
    }

    public void setLayoutHtml(String layoutHtml) {
        this.layoutHtml = layoutHtml;
    }

    public BigDecimal getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(BigDecimal grossWeight) {
        this.grossWeight = grossWeight;
    }

    public BankTransferNumberData getGdnNumberData() {
        return gdnNumberData;
    }

    public void setGdnNumberData(BankTransferNumberData gdnNumberData) {
        this.gdnNumberData = gdnNumberData;
    }

    public void setShippingLabel(String shippingLabel) {
        this.shippingLabel = shippingLabel;
    }

    public String getShippingLabel() {
        return shippingLabel;
    }

    public String getGdnNumber() {
        return gdnNumber;
    }

    public void setGdnNumber(String gdnNumber) {
        this.gdnNumber = gdnNumber;
    }

    public void setGdnFourDigitNumber(String gdnFourDigitNumber) {
        this.gdnFourDigitNumber = gdnFourDigitNumber;
    }

    public String getGdnFourDigitNumber() {
        return gdnFourDigitNumber;
    }

    public void setProductSerialsEnabled(boolean productSerialsEnabled) {
        this.productSerialsEnabled = productSerialsEnabled;
    }

    public boolean isProductSerialsEnabled() {
        return productSerialsEnabled;
    }

    public void setProductSerialItems(HashMap<Integer, ArrayList<ProductSerialItem>> productSerialItems) {
        this.productSerialItems = productSerialItems;
    }

    public HashMap<Integer, ArrayList<ProductSerialItem>> getProductSerialItems() {
        return productSerialItems;
    }

    public BigDecimal getSupplierCustomerBalance() {
        return supplierCustomerBalance;
    }

    public void setSupplierCustomerBalance(BigDecimal supplierCustomerBalance) {
        this.supplierCustomerBalance = supplierCustomerBalance;
    }

    public Integer getGdnCount() {
        return gdnCount;
    }

    public void setGdnCount(Integer gdnCount) {
        this.gdnCount = gdnCount;
    }

    public DateNonConvertable getSaleOrderDate() {
        return saleOrderDate;
    }

    public void setSaleOrderDate(DateNonConvertable saleOrderDate) {
        this.saleOrderDate = saleOrderDate;
    }

    public String getPaymentInstruction() {
        return paymentInstruction;
    }

    public void setPaymentInstruction(String paymentInstruction) {
        this.paymentInstruction = paymentInstruction;
    }

    public boolean isSalesOrder() {
        return this.isSalesOrder;
    }

    public void setSalesOrder(final boolean salesOrder) {
        this.isSalesOrder = salesOrder;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public Integer getSelectedTemplateId() {
        return selectedTemplateId;
    }

    public void setSelectedTemplateId(Integer selectedTemplateId) {
        this.selectedTemplateId = selectedTemplateId;
    }

    public ColumnConfigs[] getItemTableColumns() {
        return itemTableColumns;
    }

    public void setItemTableColumns(ColumnConfigs[] itemTableColumns) {
        this.itemTableColumns = itemTableColumns;
    }
}
