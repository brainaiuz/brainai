package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ${Dilsh0d}
 * Date: 17.08.2009
 * Time: 16:16:14
 * To change this template use File | Settings | File Templates.
 */
public class MoneyComingGoingRps implements IsSerializable {

    public String currentMoney;
    public ArrayList<String> labels;
    public double[][] dueOverDuePaid;
    public int max;

    public MoneyComingGoingRps() {
        super();
    }

    public MoneyComingGoingRps(String currentMoney) {
        this.currentMoney = currentMoney;
    }

    public String getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentMoney(String currentMoney) {
        this.currentMoney = currentMoney;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public double[][] getDueOverDuePaid() {
        return dueOverDuePaid;
    }

    public void setDueOverDuePaid(double[][] dueOverDuePaid) {
        this.dueOverDuePaid = dueOverDuePaid;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
