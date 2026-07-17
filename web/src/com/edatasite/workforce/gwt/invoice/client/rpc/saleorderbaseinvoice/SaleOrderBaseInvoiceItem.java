package com.edatasite.workforce.gwt.invoice.client.rpc.saleorderbaseinvoice;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaleOrderBaseInvoiceItem implements IsSerializable {
    public static final String SALE_ORDER = "SALE_ORDER";
    public static final String SALE_QUOTE = "SALE_QUOTE";
    public static final String GDN = "GDN";

    Integer objectId;
    String number;
    String reference;
    String shippingLabel;
    DateNonConvertable orderDate;
    DateNonConvertable dueDate;
    DateNonConvertable shipDate;
    Integer quoteId;
    String type;
    boolean selected;

    public SaleOrderBaseInvoiceItem() {
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public DateNonConvertable getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(DateNonConvertable orderDate) {
        this.orderDate = orderDate;
    }

    public DateNonConvertable getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateNonConvertable dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShippingLabel() {
        return shippingLabel;
    }

    public void setShippingLabel(String shippingLabel) {
        this.shippingLabel = shippingLabel;
    }

    public DateNonConvertable getShipDate() {
        return shipDate;
    }

    public void setShipDate(DateNonConvertable shipDate) {
        this.shipDate = shipDate;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }
}
