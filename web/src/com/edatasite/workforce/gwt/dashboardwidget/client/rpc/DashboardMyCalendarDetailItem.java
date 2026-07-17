package com.edatasite.workforce.gwt.dashboardwidget.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * User: Abror Abdukadirov
 * Date: 28.05.2018 14:30
 */
public class DashboardMyCalendarDetailItem implements IsSerializable {

    private Integer objectId;
    private String name;
    private String description;
    private String type;
    private Date startDate;
    private Date endDate;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
