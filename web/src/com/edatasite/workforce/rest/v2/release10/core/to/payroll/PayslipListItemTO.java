package com.edatasite.workforce.rest.v2.release10.core.to.payroll;

import com.edatasite.workforce.rest.base.to.UserTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.StatusTO;

/**
 * Created by Abdurakhmonov Farrukh on 11/28/2017.
 */
public class PayslipListItemTO extends ResponseData {

    private Integer id;
    private String date;
    private StatusTO status;
    private CurrencyValueTO payments;
    private CurrencyValueTO deductions;
    private CurrencyValueTO total;
    private UserTO approverItem;
    private UserTO employeeItem;

    public PayslipListItemTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public StatusTO getStatus() {
        return status;
    }

    public void setStatus(StatusTO status) {
        this.status = status;
    }

    public CurrencyValueTO getPayments() {
        return payments;
    }

    public void setPayments(CurrencyValueTO payments) {
        this.payments = payments;
    }

    public CurrencyValueTO getDeductions() {
        return deductions;
    }

    public void setDeductions(CurrencyValueTO deductions) {
        this.deductions = deductions;
    }

    public CurrencyValueTO getTotal() {
        return total;
    }

    public void setTotal(CurrencyValueTO total) {
        this.total = total;
    }

    public UserTO getApproverItem() {
        return approverItem;
    }

    public void setApproverItem(UserTO approverItem) {
        this.approverItem = approverItem;
    }

    public UserTO getEmployeeItem() {
        return employeeItem;
    }

    public void setEmployeeItem(UserTO employeeItem) {
        this.employeeItem = employeeItem;
    }
}
