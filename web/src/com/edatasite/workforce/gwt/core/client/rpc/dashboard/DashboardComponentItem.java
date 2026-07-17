package com.edatasite.workforce.gwt.core.client.rpc.dashboard;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.HashMap;

/**
 * User: Abror Abdukadirov
 * Date: 11.04.2018 19:52
 */
public class DashboardComponentItem extends SelectItem {

    private int x;
    private int y;
    private int width;
    private int height;
    private int minHeight = 2;
    private int minWidth = 2;
    private String componentCode;
    private Integer reportId;
    private Integer reportWidgetId;
    private boolean fromResetButton;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getReportWidgetId() {
        return reportWidgetId;
    }

    public void setReportWidgetId(Integer reportWidgetId) {
        this.reportWidgetId = reportWidgetId;
    }

    public HashMap<String, String> getValueMap() {
        return getInstance();
    }

    public boolean isFromResetButton() {
        return fromResetButton;
    }

    public void setFromResetButton(boolean fromResetButton) {
        this.fromResetButton = fromResetButton;
    }
}
