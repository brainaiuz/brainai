package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OrgPositionDTO implements IsSerializable {
    private Integer id;
    private String title;
    private String ownerName;
    private String description;
    private String metrics;
    private String colorKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public String getColorKey() {
        return colorKey;
    }

    public void setColorKey(String colorKey) {
        this.colorKey = colorKey;
    }
}
