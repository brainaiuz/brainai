package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ${Dilsh0d}
 * Date: 17.08.2009
 * Time: 16:53:19
 * To change this template use File | Settings | File Templates.
 */
public class ClientByAmountRps implements IsSerializable {
    private String baseCurrunse;
    private ArrayList<String> clientName;
    private double[] ammount;
    private int max;

    public ClientByAmountRps() {
        super();
    }

    public ClientByAmountRps(String baseCurrency) {
        this.baseCurrunse = baseCurrency;
    }

    public String getBaseCurrunse() {
        return baseCurrunse;
    }

    public void setBaseCurrunse(String baseCurrunse) {
        this.baseCurrunse = baseCurrunse;
    }

    public ArrayList<String> getClientName() {
        return clientName;
    }

    public void setClientName(ArrayList<String> clientName) {
        this.clientName = clientName;
    }

    public double[] getAmmount() {
        return ammount;
    }

    public void setAmmount(double[] ammount) {
        this.ammount = ammount;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
