package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataStatus;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataType;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Murad Satimov
 * Date: 1/10/18 3:46 PM
 */
public class ShippingData implements IsSerializable, ListingCustomFields {
    public static final String NUMBER = "NUMBER";
    public static final String DATE = "DATE";
    public static final String SHIPPING_LABEL = "SHIPPING_LABEL";
    public static final String SUPPLIER = "SUPPLIER";
    public static final String GDNSUPPLIER = "GDNSUPPLIER";
    public static final String CURRENCY = "CURRENCY";
    public static final String AMOUNT = "AMOUNT";
    public static final String ORDER_NUMBER = "ORDER_NUMBER";
    public static final String CREATOR = "CREATOR";
    public static final String INVOICE_NUMBER = "INVOICE_NUMBER";
    public static final String INVOICE_STATUS = "INVOICE_STATUS";
    public static final String STATUS = "STATUS";

    private Integer id;
    private String number;
    private String clientName;
    private String creatorName;
    private SelectItem creator;
    private String shippingLabel;
    private String currencyName;
    private ShippingDataStatus status;
    private ShippingDataType shippingType;
    private DateNonConvertable shippingDate;
    private ArrayList<ShippingDataItem> items = new ArrayList<>();
    private CrmAccountItem customer;
    private CrmAccountItem supplier;
    private String invoiceNumber;
    private String invoiceStatus;
    private String customerName;
    private Date invoiceDate;
    private BigDecimal invoiceTotal;
    private NewInvoice invoice;
    private SelectItem[] templates;
    private Integer selectedTemplateId;
    private String orderNumber;
    private String layoutHtml;
    private Integer quoteId;
    private Integer invoiceId;
    private Boolean isSalesOrder;
    private Integer journalId;
    private HashMap<Integer, BigDecimal> relatedExpenses;
    private BigDecimal totalAllocatedAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public DateNonConvertable getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(DateNonConvertable shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getShippingLabel() {
        return shippingLabel;
    }

    public void setShippingLabel(String shippingLabel) {
        this.shippingLabel = shippingLabel;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public ArrayList<ShippingDataItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ShippingDataItem> items) {
        this.items = items;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setShippingType(ShippingDataType shippingType) {
        this.shippingType = shippingType;
    }

    public ShippingDataType getShippingType() {
        return shippingType;
    }

    public ShippingDataStatus getStatus() {
        return status;
    }

    public void setStatus(ShippingDataStatus status) {
        this.status = status;
    }

    public CrmAccountItem getCustomer() {
        return customer;
    }

    public void setCustomer(CrmAccountItem customer) {
        this.customer = customer;
    }

    public CrmAccountItem getSupplier() {
        return this.supplier;
    }

    public void setSupplier(final CrmAccountItem supplier) {
        this.supplier = supplier;
    }

    public NewInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(NewInvoice invoice) {
        this.invoice = invoice;
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getLayoutHtml() {
        return layoutHtml;
    }

    public void setLayoutHtml(String layoutHtml) {
        this.layoutHtml = layoutHtml;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return null;
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {

    }

    public SelectItem getCreator() {
        return this.creator;
    }

    public void setCreator(final SelectItem creator) {
        this.creator = creator;
    }

    public Boolean getSalesOrder() {
        return this.isSalesOrder;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public void setSalesOrder(final Boolean salesOrder) {
        this.isSalesOrder = salesOrder;
    }

    public Integer getJournalId() {
        return this.journalId;
    }

    public void setJournalId(final Integer journalId) {
        this.journalId = journalId;
    }

    public HashMap<Integer, BigDecimal> getRelatedExpenses() {
        return relatedExpenses;
    }

    public void setRelatedExpenses(HashMap<Integer, BigDecimal> relatedExpenses) {
        this.relatedExpenses = relatedExpenses;
    }

    public BigDecimal getTotalAllocatedAmount() {
        return totalAllocatedAmount;
    }

    public void setTotalAllocatedAmount(BigDecimal totalAllocatedAmount) {
        this.totalAllocatedAmount = totalAllocatedAmount;
    }
}
