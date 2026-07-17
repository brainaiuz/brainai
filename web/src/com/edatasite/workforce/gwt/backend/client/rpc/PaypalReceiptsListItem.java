package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Alisher
 * Date: 05.05.2010
 * Time: 12:53:47
 * To change this template use File | Settings | File Templates.
 */
public class PaypalReceiptsListItem implements IsSerializable {

    public static final String COMPANY_ID = "companyID";
    public static final String COMPANY_NAME = "companyName";
    public static final String SUBSCSTARTDATE = "subscStartDate";
    public static final String SUBSCENDDATE = "subscEndDate";
    public static final String NUMBEROFEMPLOYEES = "numberOfEmployees";
    public static final String PAIDAMOUNT = "paidAmount";
    public static final String PAYMENTTYPE = "paymentType";
    public static final String PAIDDATE = "paidDate";
    public static final String STATUS = "status";

    private Integer objectId;
    private String companyName;
    private Integer companyID;
    private String subscStartDate;
    private String subscEndDate;
    private Integer numberOfEmployees;
    private Float paidAmount;
    private String paidDate;
    private String status;
    private String paymenttype;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getSubscStartDate() {
        return subscStartDate;
    }

    public void setSubscStartDate(String subscStartDate) {
        this.subscStartDate = subscStartDate;
    }

    public String getSubscEndDate() {
        return subscEndDate;
    }

    public void setSubscEndDate(String subscEndDate) {
        this.subscEndDate = subscEndDate;
    }

    public Integer getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(Integer numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public Float getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Float paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }
}
