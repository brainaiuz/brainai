package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverListStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Farrukh Abdurakhmonov on 19/01/2018.
 */
public class ApprovalExpensesClaimTO extends ResponseData {
    private Integer id;
    private String title;
    private String date;
    private ApproverListStatusTO status;
    private String number;
    private String approver;
    private String reporter;
    private CurrencyValueTO original;
    private CurrencyValueTO due;

    public ApprovalExpensesClaimTO() {
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

    public ApproverListStatusTO getStatus() {
        return status;
    }

    public void setStatus(ApproverListStatusTO status) {
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

    public CurrencyValueTO getDue() {
        return due;
    }

    public void setDue(CurrencyValueTO due) {
        this.due = due;
    }
}
