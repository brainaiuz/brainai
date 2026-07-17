package com.finnetlimited.reportservice.core.server.handler;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Apr 30, 2011
 * Time: 5:59:31 PM
 * To change this template use File | Settings | File Templates.
 */
public final class ExcelReportDataTable {

    private String name;
    private Integer currentIndex = 0;
    private String defaultShortDateFormat = "dd/MM/yyyy";

    private LinkedList<ExcelReportDataColumn> columns;
    private LinkedList<ExcelReportDataRow> rows;
    private HashMap<String, Integer> columnNames;

    public ExcelReportDataTable() {
        columnNames = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultShortDateFormat() {
        return defaultShortDateFormat;
    }

    public LinkedList<ExcelReportDataColumn> getColumns() {
        return columns;
    }

    public int getColumnCount() {
        return columnNames.size();
    }

    public void setColumns(LinkedList<ExcelReportDataColumn> columns) {
        this.columns = columns;
        columnNames.clear();
        for (int i = 0; i < columns.size(); i++) {
            columnNames.put(columns.get(i).getName(), i);
        }
    }

    public int getColumnIndex(String columnName) {
        if (columnNames.containsKey(columnName)) {
            return columnNames.get(columnName);
        }
        return -1;
    }

    public LinkedList<ExcelReportDataRow> getRows() {
        return rows;
    }

    public void setRows(LinkedList<ExcelReportDataRow> rows) {
        this.rows = rows;
    }

    public String getValue(Integer rowIndex, Integer columnIndex) {
        if (rows != null && rows.size() > 0) {
            return rows.get(rowIndex).getValue(columnIndex);
        }
        return null;
    }

    public String getValue(Integer rowIndex, String columnName) {
        if (rows != null && rows.size() > 0) {
            return rows.get(rowIndex).getStringValue(columnName);
        }
        return null;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(Integer currentIndex) {
        this.currentIndex = currentIndex;
    }

    public ExcelReportDataRow newRow() {
        return new ExcelReportDataRow(this);
    }
}
