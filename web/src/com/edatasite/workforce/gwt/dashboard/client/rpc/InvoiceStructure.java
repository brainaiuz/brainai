package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 10.07.2009
 * Time: 15:02:15
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceStructure implements IsSerializable {

    private HashMap<Integer, Double> due;

    private HashMap<Integer, Double> overDue;

    private HashMap<Integer, Double> paid;

    private int dueCount = 0;
    private int overDueCount = 0;
    private int paidCount = 0;

    public InvoiceStructure() {
    }

    public InvoiceStructure(HashMap<Integer, Double> due, HashMap<Integer, Double> overDue, HashMap<Integer, Double> paid) {
        this.due = due;
        this.overDue = overDue;
        this.paid = paid;
    }

    public HashMap<Integer, Double> getDue() {
        return due;
    }

    public void setDue(HashMap<Integer, Double> due) {
        this.due = due;
    }

    public HashMap<Integer, Double> getOverDue() {
        return overDue;
    }

    public void setOverDue(HashMap<Integer, Double> overDue) {
        this.overDue = overDue;
    }

    public HashMap<Integer, Double> getPaid() {
        return paid;
    }

    public void setPaid(HashMap<Integer, Double> paid) {
        this.paid = paid;
    }

    public int getDueCount() {
        return dueCount;
    }

    public void setDueCount(int dueCount) {
        this.dueCount = dueCount;
    }

    public int getOverDueCount() {
        return overDueCount;
    }

    public void setOverDueCount(int overDueCount) {
        this.overDueCount = overDueCount;
    }

    public int getPaidCount() {
        return paidCount;
    }

    public void setPaidCount(int paidCount) {
        this.paidCount = paidCount;
    }
}
