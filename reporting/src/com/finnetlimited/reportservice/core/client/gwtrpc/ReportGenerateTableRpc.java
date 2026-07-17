package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportDataTable;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * User: ${Dilsh0d}
 * Date: 30-Mar-2010
 * Time: 18:18:16
 */

public class ReportGenerateTableRpc implements IsSerializable {

    private int limit;
    private int nowPosition = 1;
    private int nowLastPosition;
    private int rowCount;
    private ReportRpc report;
    private LinkedList<ColumnRpc> titleRows;
    private ArrayList<ReportGenerateRpc> totalPosition;
    private LinkedList<ReportGenerateRpc> rows;
    private ReportDataTable reportDataTable;
    private ArrayList<ReportTreeItem> treeItems;
    private String textExceptionLog;
    private String reportLink;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLastPosition() {
        return nowLastPosition;
    }

    public void setNowLastPosition(int nowLastPosition) {
        this.nowLastPosition = nowLastPosition;
    }

    public int getNowPosition() {
        return nowPosition;
    }

    public void setNowPosition(int nowPosition) {
        this.nowPosition = nowPosition;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public ReportRpc getReport() {
        return report;
    }

    public void setReport(ReportRpc report) {
        this.report = report;
    }

    public LinkedList<ColumnRpc> getTitleRows() {
        if (titleRows == null) {
            titleRows = new LinkedList<>();
        }
        return titleRows;
    }

    public void setTitleRows(LinkedList<ColumnRpc> titleRows) {
        this.titleRows = titleRows;
    }

    public ArrayList<ReportGenerateRpc> getTotalPosition() {
        if (totalPosition == null) {
            totalPosition = new ArrayList<>();
        }
        return totalPosition;
    }

    public void setTotalPosition(ArrayList<ReportGenerateRpc> totalPosition) {
        this.totalPosition = totalPosition;
    }

    public LinkedList<ReportGenerateRpc> getRows() {
        if (rows == null) {
            rows = new LinkedList<>();
        }
        return rows;
    }

    public void setRows(LinkedList<ReportGenerateRpc> rows) {
        this.rows = rows;
    }

    public ArrayList<ReportTreeItem> getTreeItems() {
        return treeItems;
    }

    public void setTreeItems(ArrayList<ReportTreeItem> treeItems) {
        this.treeItems = treeItems;
    }

    public ReportDataTable getReportDataTable() {
        return reportDataTable;
    }

    public void setReportDataTable(ReportDataTable reportDataTable) {
        this.reportDataTable = reportDataTable;
    }

    public void setTextExceptionLog(StringBuffer textExceptionLog) {
        this.textExceptionLog = textExceptionLog.toString();
    }

    public String getTextExceptionLog() {
        return this.textExceptionLog;
    }

    public String getReportLink() {
        return reportLink;
    }

    public void setReportLink(String reportLink) {
        this.reportLink = reportLink;
    }
}
