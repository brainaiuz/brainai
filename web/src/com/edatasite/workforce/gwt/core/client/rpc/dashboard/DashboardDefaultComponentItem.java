package com.edatasite.workforce.gwt.core.client.rpc.dashboard;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * User: Abror Abdukadirov
 * Date: 11.04.2018 16:47
 */
public class DashboardDefaultComponentItem implements IsSerializable {

    private Integer objectId;
    private Integer reportId;
    private Integer reportWidgetId;
    private String componentName;
    private String componentCode;
    private ArrayList<String> modules;

    /*default component coordinates*/
    private int width = 4;
    private int height = 4;
    private int minHeight = 2;
    private int minWidth = 2;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public ArrayList<String> getModules() {
        return modules;
    }

    public void setModules(ArrayList<String> modules) {
        this.modules = modules;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public Integer getReportWidgetId() {
        return reportWidgetId;
    }

    public void setReportWidgetId(Integer reportWidgetId) {
        this.reportWidgetId = reportWidgetId;
    }
}
