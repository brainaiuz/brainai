package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Jan 6, 2011
 * Time: 7:29:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DashboardRpc implements IsSerializable {

    private Integer id;
    private String name = "";
    private String code;
    private Integer columnCount;
    private String leftColumnTitle = "";
    private String centerColumnTitle = "";
    private String rightColumnTitle = "";
    private List<DashletRpc> dashlets;
    private boolean isSystem = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(Integer columnCount) {
        this.columnCount = columnCount;
    }

    public String getLeftColumnTitle() {
        return leftColumnTitle;
    }

    public void setLeftColumnTitle(String leftColumnTitle) {
        this.leftColumnTitle = leftColumnTitle;
    }

    public String getCenterColumnTitle() {
        return centerColumnTitle;
    }

    public void setCenterColumnTitle(String centerColumnTitle) {
        this.centerColumnTitle = centerColumnTitle;
    }

    public String getRightColumnTitle() {
        return rightColumnTitle;
    }

    public void setRightColumnTitle(String rightColumnTitle) {
        this.rightColumnTitle = rightColumnTitle;
    }

    public List<DashletRpc> getDashlets() {
        return dashlets;
    }

    public void setDashlets(List<DashletRpc> dashlets) {
        this.dashlets = dashlets;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }
}
