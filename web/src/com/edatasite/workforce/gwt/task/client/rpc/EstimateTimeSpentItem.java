package com.edatasite.workforce.gwt.task.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Mar 15, 2011
 * Time: 2:47:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class EstimateTimeSpentItem implements IsSerializable {

    private Integer estimatedTime;
    private Integer timeSpent;

    public EstimateTimeSpentItem() {
    }

    public EstimateTimeSpentItem(Integer estimatedTime, Integer timeSpent) {
        this.estimatedTime = estimatedTime;
        this.timeSpent = timeSpent;
    }

    public Integer getEstimatedTime() {
        return estimatedTime != null ? estimatedTime : 0;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Integer getTimeSpent() {
        return timeSpent != null ? timeSpent : 0;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }
}
