package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.Date;

/**
 * User: Dilsh0d
 * Date: 23-May-2010
 * Time: 14:02:01
 */
public class ProjectCostSelectItem extends SelectItem {

    private Float rate = 0.0f;
    private Float percent = 0.0f;
    private Float amount = 0.0f;
    private boolean logicPercent = false;

    private Date startDate;
    private Date endDate;

    public ProjectCostSelectItem(Integer id, String name) {
        super(id, name);
    }

    public ProjectCostSelectItem() {
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public boolean isLogicPercent() {
        return logicPercent;
    }

    public void setLogicPercent(boolean logicPercent) {
        this.logicPercent = logicPercent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
