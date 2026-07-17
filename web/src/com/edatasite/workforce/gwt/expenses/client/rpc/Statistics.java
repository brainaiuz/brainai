package com.edatasite.workforce.gwt.expenses.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 05.11.2008
 * Time: 12:37:04
 * To change this template use File | Settings | File Templates.
 */
public class Statistics implements IsSerializable {

    private String month;

    //Total
    private Integer totalCount;
    private Double totalAmount;

    //Approved
    private Integer approvedCount;
    private Double approvedAmount;

    //Declined
    private Integer declinedCount;
    private Double declinedAmount;

    //Submitted
    private Integer submittedCount;
    private Double submittedAmount;

    //Waiting for your approval
    private Integer waitingForApprovalCount;
    private Double waitingForApprovalAmount;

    public Integer getTotalCount() {

        return totalCount == null ? totalCount = 0 : totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Double getTotalAmount() {

        return totalAmount == null ? totalAmount = 0d : totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getApprovedCount() {

        return approvedCount == null ? approvedCount = 0 : approvedCount;
    }

    public void setApprovedCount(Integer approvedCount) {
        this.approvedCount = approvedCount;
    }

    public Double getApprovedAmount() {

        return approvedAmount == null ? approvedAmount = 0d : approvedAmount;
    }

    public void setApprovedAmount(Double approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public Integer getDeclinedCount() {

        return declinedCount == null ? declinedCount = 0 : declinedCount;
    }

    public void setDeclinedCount(Integer declinedCount) {
        this.declinedCount = declinedCount;
    }

    public Double getDeclinedAmount() {

        return declinedAmount == null ? declinedAmount = 0d : declinedAmount;
    }

    public void setDeclinedAmount(Double declinedAmount) {
        this.declinedAmount = declinedAmount;
    }

    public Integer getWaitingForApprovalCount() {

        return waitingForApprovalCount == null ? waitingForApprovalCount = 0 : waitingForApprovalCount;
    }

    public void setWaitingForApprovalCount(Integer waitingForApprovalCount) {
        this.waitingForApprovalCount = waitingForApprovalCount;
    }

    public Double getWaitingForApprovalAmount() {

        return waitingForApprovalAmount == null ? waitingForApprovalAmount = 0d : waitingForApprovalAmount;
    }

    public void setWaitingForApprovalAmount(Double waitingForApprovalAmount) {
        this.waitingForApprovalAmount = waitingForApprovalAmount;
    }

    public Integer getSubmittedCount() {
        return submittedCount;
    }

    public void setSubmittedCount(Integer submittedCount) {
        this.submittedCount = submittedCount;
    }

    public Double getSubmittedAmount() {
        return submittedAmount;
    }

    public void setSubmittedAmount(Double submittedAmount) {
        this.submittedAmount = submittedAmount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
