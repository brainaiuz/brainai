package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Jan 7, 2011
 * Time: 3:38:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class DashletRpc implements IsSerializable {

    private Integer id;
    private String code;
    private DashboardRpc dashboard;
    private Integer dashletType;
    private Integer columnIndex;
    private Integer verticalPosition;
    private Integer reportId;
    private String reportCode;
    private ReportRpc report;
    private boolean isSystem = false;
    private String chartfilter;


    private String title;
    private String chartImageData;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DashboardRpc getDashboard() {
        return dashboard;
    }

    public void setDashboard(DashboardRpc dashboard) {
        this.dashboard = dashboard;
    }

    public Integer getDashletType() {
        return dashletType;
    }

    public void setDashletType(Integer dashletType) {
        this.dashletType = dashletType;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public Integer getVerticalPosition() {
        return verticalPosition;
    }

    public void setVerticalPosition(Integer verticalPosition) {
        this.verticalPosition = verticalPosition;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public ReportRpc getReport() {
        return report;
    }

    public void setReport(ReportRpc report) {
        this.report = report;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChartImageData() {
        return chartImageData;
    }

    public void setChartImageData(String chartImageData) {
        this.chartImageData = chartImageData;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public void setChartFilter(String filter) {
        this.chartfilter = filter;
    }

    public String getChartFilter() {
        return this.chartfilter;
    }
}
