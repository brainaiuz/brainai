package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jan 20, 2010
 * Time: 7:40:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrunItem implements IsSerializable {

    private Integer employeeID;
    private String employeeName;

    private PayslipObject payslipObject;
    private PayslipRequestObject pdfObject;

    private boolean isPayslipCalculated = false;

    private boolean alreadyPaid;

    private Integer status;

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public PayslipObject getPayslipObject() {
        return payslipObject;
    }

    public void setPayslipObject(PayslipObject payslipObject) {
        this.payslipObject = payslipObject;
    }

    public PayslipRequestObject getPdfObject() {
        return pdfObject;
    }

    public void setPdfObject(PayslipRequestObject pdfObject) {
        this.pdfObject = pdfObject;
    }

    public boolean isAlreadyPaid() {
        return alreadyPaid;
    }

    public void setAlreadyPaid(boolean alreadyPaid) {
        this.alreadyPaid = alreadyPaid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean isPayslipCalculated() {
        return isPayslipCalculated;
    }

    public void setPayslipCalculated(boolean payslipCalculated) {
        isPayslipCalculated = payslipCalculated;
    }
}
