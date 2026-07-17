package com.edatasite.workforce.rest.v2.release10.core.to.accounting.order;

import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.CurrencyTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Created by Dilsh0d Madrahimov on 3/16/18.
 */
public class SalesOrderListTO extends ResponseData {
    private Integer id;
    private String number;
    private String po_number;
    @Schema(description = "Date Format (format: dd-MM-yyyy'T'hh:mm:ssZ)", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ssZ")
    private String date;
    @Schema(required = true)
    private InvoiceStatusTO status;
    private BigDecimal total;
    private CurrencyTO currency;
    private String relatedProject;

    public SalesOrderListTO() {
    }

    public SalesOrderListTO(Integer id, String number) {
        this.id = id;
        this.number = number;
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

    public String getPo_number() {
        return po_number;
    }

    public void setPo_number(String po_number) {
        this.po_number = po_number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public InvoiceStatusTO getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatusTO status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }

    public String getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(String relatedProject) {
        this.relatedProject = relatedProject;
    }
}
