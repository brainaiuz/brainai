package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jul 18, 2009
 * Time: 12:26:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseStructure implements IsSerializable {

    private HashMap<Integer, Double> approved;

    private HashMap<Integer, Double> declined;

    private HashMap<Integer, Double> paid;

    private int approvedCount = 0;
    private int declinedCount = 0;
    private int paidCount;

    public HashMap<Integer, Double> getApproved() {
        return approved;
    }

    public void setApproved(HashMap<Integer, Double> approved) {
        this.approved = approved;
    }

    public HashMap<Integer, Double> getDeclined() {
        return declined;
    }

    public void setDeclined(HashMap<Integer, Double> declined) {
        this.declined = declined;
    }

    public HashMap<Integer, Double> getPaid() {
        return paid;
    }

    public void setPaid(HashMap<Integer, Double> paid) {
        this.paid = paid;
    }

    public int getApprovedCount() {
        return approvedCount;
    }

    public void setApprovedCount(int approvedCount) {
        this.approvedCount = approvedCount;
    }

    public int getDeclinedCount() {
        return declinedCount;
    }

    public void setDeclinedCount(int declinedCount) {
        this.declinedCount = declinedCount;
    }

    public int getPaidCount() {
        return paidCount;
    }

    public void setPaidCount(int paidCount) {
        this.paidCount = paidCount;
    }
}
