package com.edatasite.workforce.gwt.core.server.rabbitmq.data;

import java.io.Serializable;

public class ListPanelItemMQ implements Serializable {

    private Integer objectID;

    private String panelType;

    private String settingsJSONData;

    private String sortBy;

    private Integer userId;

    private Integer parentId;

    public ListPanelItemMQ() {

    }

    public ListPanelItemMQ(String panelType, String settingsJSONData, String sortBy, Integer userId, Integer parentiD) {
        this.panelType = panelType;
        this.settingsJSONData = settingsJSONData;
        this.sortBy = sortBy;
        this.userId = userId;
        this.parentId = parentiD;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getPanelType() {
        return panelType;
    }

    public void setPanelType(String panelType) {
        this.panelType = panelType;
    }

    public String getSettingsJSONData() {
        return settingsJSONData;
    }

    public void setSettingsJSONData(String settingsJSONData) {
        this.settingsJSONData = settingsJSONData;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "ListPanelItemMQ{" +
                "objectID=" + objectID +
                ", panelType='" + panelType + '\'' +
                ", settingsJSONData='" + settingsJSONData + '\'' +
                ", sortBy='" + sortBy + '\'' +
                ", userId=" + userId +
                ", parentId=" + parentId +
                '}';
    }
}
