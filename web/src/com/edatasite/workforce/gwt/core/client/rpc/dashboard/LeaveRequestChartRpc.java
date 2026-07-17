package com.edatasite.workforce.gwt.core.client.rpc.dashboard;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: HRS
 * Date: 07.12.2009
 * Time: 18:14:08
 * To change this template use File | Settings | File Templates.
 */
public class LeaveRequestChartRpc implements IsSerializable {

    private String name;
    private LinkedHashMap<String, String> topNames;

    private Double[] paid;
    private Double[] nonPaid;
    private Double[] left;
    private Double[] exceeded;

    public LeaveRequestChartRpc() {
        super();
    }

    public LeaveRequestChartRpc(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedHashMap<String, String> getTopNames() {
        return topNames;
    }

    public void setTopNames(LinkedHashMap<String, String> topNames) {
        this.topNames = topNames;
    }

    public Double[] getPaid() {
        return paid;
    }

    public void setPaid(Double[] paid) {
        this.paid = paid;
    }

    public Double[] getNonPaid() {
        return nonPaid;
    }

    public void setNonPaid(Double[] nonPaid) {
        this.nonPaid = nonPaid;
    }

    public Double[] getLeft() {
        return left;
    }

    public void setLeft(Double[] left) {
        this.left = left;
    }

    public Double[] getExceeded() {
        return exceeded;
    }

    public void setExceeded(Double[] exceeded) {
        this.exceeded = exceeded;
    }
}
