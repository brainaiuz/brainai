package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ${Dilsh0d}
 * Date: 29.10.2009
 * Time: 1:35:00
 * To change this template use File | Settings | File Templates.
 */
public class ProjectCostRps implements IsSerializable {

    private ArrayList<String> names;
    private double[][] cost;
    private int max;

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public double[][] getCost() {
        return cost;
    }

    public void setCost(double[][] cost) {
        this.cost = cost;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
