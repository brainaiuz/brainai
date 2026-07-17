package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class InvoiceListItemTO extends ResponseData {

    private Integer id;
    private String invoice_number;
    @Schema(description = "Date Format (format: dd-MM-yyyy'T'hh:mm:ssZ)", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ssZ")
    private String invoice_date;
    @Schema(required = true)
    private InvoiceStatusTO invoice_status;
    private BigDecimal invoice_subtotal;
    private BigDecimal invoice_total;
    private CurrencyTO invoice_currency;
    private String relatedProject;

    public InvoiceListItemTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public InvoiceStatusTO getInvoice_status() {
        return invoice_status;
    }

    public void setInvoice_status(InvoiceStatusTO invoice_status) {
        this.invoice_status = invoice_status;
    }

    public BigDecimal getInvoice_total() {
        return invoice_total;
    }

    public void setInvoice_total(BigDecimal invoice_total) {
        this.invoice_total = invoice_total;
    }

    public BigDecimal getInvoice_subtotal() {
        return invoice_subtotal;
    }

    public void setInvoice_subtotal(BigDecimal invoice_subtotal) {
        this.invoice_subtotal = invoice_subtotal;
    }

    public CurrencyTO getInvoice_currency() {
        return invoice_currency;
    }

    public void setInvoice_currency(CurrencyTO invoice_currency) {
        this.invoice_currency = invoice_currency;
    }

    public String getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(String relatedProject) {
        this.relatedProject = relatedProject;
    }
}
