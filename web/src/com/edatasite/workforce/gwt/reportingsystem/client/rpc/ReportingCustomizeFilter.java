package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Virus
 * Date: 3/20/13
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReportingCustomizeFilter implements IsSerializable {
    public static final String ALL_COLUMNS = "ALL_COLUMNS";
    public static final String SELECT_COLUMNS = "SELECT_COLUMNS";
    public static final String CHART_SELECT_Y_COLUMNS = "CHART_SELECT_Y_COLUMNS";

    private Integer id;
    private String name;
    private String dashletGUID;
    private String reportCode;
    private String sortType;
    private String sortColumnName;
    private String ySortColumnName;
    private String selectedViewAsName;
    private Integer rowCount;
    private String widget_GUID;
    private int dashletType = Constants.TABLE;
    private LinkedHashMap<String, LinkedList<ColumnRpc>> columnsMap = new LinkedHashMap<>();
    private ArrayList<FilterRpc> filterRpcs = new ArrayList<>();

    private ArrayList<ReportingRolePermissionItem> viewAs;
    private ArrayList<SelectItem> selectedColumns;
    private ArrayList<SelectItem> ySelectedColumns;

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

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public ArrayList<ReportingRolePermissionItem> getViewAs() {
        return viewAs = (viewAs == null ? new ArrayList<>() : viewAs);
    }

    public void setViewAs(ArrayList<ReportingRolePermissionItem> viewAs) {
        this.viewAs = viewAs;
    }

    public ArrayList<SelectItem> getSelectedColumns() {
        return selectedColumns = (selectedColumns == null ? new ArrayList<>() : selectedColumns);
    }

    public void setSelectedColumns(ArrayList<SelectItem> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public String getDashletGUID() {
        return dashletGUID;
    }

    public void setDashletGUID(String dashletGUID) {
        this.dashletGUID = dashletGUID;
    }

    public String getSortColumnName() {
        return sortColumnName;
    }

    public void setSortColumnName(String sortColumnName) {
        this.sortColumnName = sortColumnName;
    }

    public String getSelectedViewAsName() {
        return selectedViewAsName;
    }

    public void setSelectedViewAsName(String selectedViewAsName) {
        this.selectedViewAsName = selectedViewAsName;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public String getWidget_GUID() {
        return widget_GUID;
    }

    public void setWidget_GUID(String widget_GUID) {
        this.widget_GUID = widget_GUID;
    }

    public String getYSortColumnName() {
        return ySortColumnName;
    }

    public void setYSelectedColumn(String ySortColumnName) {
        this.ySortColumnName = ySortColumnName;
    }

    public ArrayList<SelectItem> getYSelectedColumns() {
        return ySelectedColumns = (ySelectedColumns == null ? new ArrayList<>() : ySelectedColumns);
    }

    public void setYSelectedColumns(ArrayList<SelectItem> ySelectedColumns) {
        this.ySelectedColumns = ySelectedColumns;
    }

    public int getDashletType() {
        return dashletType;
    }

    public void setDashletType(int dashletType) {
        this.dashletType = dashletType;
    }

    public LinkedHashMap<String, LinkedList<ColumnRpc>> getColumnsMap() {
        return columnsMap;
    }

    public ArrayList<FilterRpc> getFilterRpcs() {
        return filterRpcs;
    }

    public void setFilterRpcs(ArrayList<FilterRpc> filterRpcs) {
        this.filterRpcs = filterRpcs;
    }
}
