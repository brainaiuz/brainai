package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class InvoiceFieldsUpdaterDto {
    @NotNull(message = "Id is required.")
    private Integer id;
    private String reference;
    private String number;
    private Date dueDate;
    private Integer taxCalcType;

    public InvoiceFieldsUpdaterDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getTaxCalcType() {
        return taxCalcType;
    }

    public void setTaxCalcType(Integer taxCalcType) {
        this.taxCalcType = taxCalcType;
    }
}
