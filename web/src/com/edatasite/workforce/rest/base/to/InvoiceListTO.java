package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Umidbek.
 */
public class InvoiceListTO implements IsSerializable {
    Integer id;
    String number;

    Long invoiceDate;
    Long dueDate;

    Double total;
    Double subtotal;

    public InvoiceListTO() {
    }

    public InvoiceListTO(NewInvoice invoice) {
        this.id = invoice.getID();

        this.number = invoice.getInvoiceNumber();

        if (invoice.getInvoiceDate() != null) {
            this.invoiceDate = invoice.getInvoiceDate().getDateLong();
        }

        if (invoice.getDueDate() != null) {
            this.dueDate = invoice.getDueDate().getDateLong();
        }

        this.total = WrapUtils.getDouble(invoice.getTotal());
        this.subtotal = WrapUtils.getDouble(invoice.getSubtotal());
    }

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

    public Long getStartDate() {
        return invoiceDate;
    }

    public void setStartDate(Long startDate) {
        this.invoiceDate = startDate;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
