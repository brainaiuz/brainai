package com.edatasite.workforce.rest.v2.release10.core.to.accounting.expense;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

public class ExpenseListItemTO extends ResponseData {

    private Integer id;
    private String title;
    @Schema(description = "DateTime (format: dd-MM-yyyy'T'hh:mm:ssZ)", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ssZ")
    private String date;
    private Object status;
    private String number;
    private String approver;
    private String reporter;
    private CurrencyValueTO original;
    private CurrencyValueTO paid;
    private CurrencyValueTO due;

    public ExpenseListItemTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public CurrencyValueTO getOriginal() {
        return original;
    }

    public void setOriginal(CurrencyValueTO original) {
        this.original = original;
    }

    public CurrencyValueTO getPaid() {
        return paid;
    }

    public void setPaid(CurrencyValueTO paid) {
        this.paid = paid;
    }

    public CurrencyValueTO getDue() {
        return due;
    }

    public void setDue(CurrencyValueTO due) {
        this.due = due;
    }
}
