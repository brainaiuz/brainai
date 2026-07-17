package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarvbek
 * Date: 16.05.2009
 * Time: 15:06:46
 * To change this template use File | Settings | File Templates.
 */

public class TotalCostHours implements IsSerializable {

    private Double totalHours = (double) 0;
    private Double totalCost = (double) 0;
    private Double totalWageCost = (double) 0;

    private Integer[] entryIds;

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Integer[] getEntryIds() {
        return entryIds;
    }

    public void setEntryIds(Integer[] entryIds) {
        this.entryIds = entryIds;
    }

    public Double getTotalWageCost() {
        return totalWageCost;
    }

    public void setTotalWageCost(Double totalWageCost) {
        this.totalWageCost = totalWageCost;
    }
}
