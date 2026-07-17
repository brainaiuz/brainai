package com.edatasite.workforce.rest.v2.release10.core.to.payroll;

import com.edatasite.workforce.rest.base.to.UserTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.StatusTO;

import java.util.ArrayList;

public class PayslipDetailsTO extends ResponseData {
    private Integer id;
    private String date;
    private StatusTO status;
    private ArrayList<PayslipTotalTO> total;
    private PayslipItemTableTO allowances;
    private PayslipItemTableTO deductions;
    private UserTO approver;
    private UserTO employee;


    public PayslipDetailsTO() {
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

    public ArrayList<PayslipTotalTO> getTotal() {
        return total;
    }

    public void setTotal(ArrayList<PayslipTotalTO> total) {
        this.total = total;
    }

    public PayslipItemTableTO getAllowances() {
        return allowances;
    }

    public void setAllowances(PayslipItemTableTO allowances) {
        this.allowances = allowances;
    }

    public PayslipItemTableTO getDeductions() {
        return deductions;
    }

    public void setDeductions(PayslipItemTableTO deductions) {
        this.deductions = deductions;
    }

    public UserTO getApprover() {
        return approver;
    }

    public void setApprover(UserTO approver) {
        this.approver = approver;
    }

    public UserTO getEmployee() {
        return employee;
    }

    public void setEmployee(UserTO employee) {
        this.employee = employee;
    }
}
