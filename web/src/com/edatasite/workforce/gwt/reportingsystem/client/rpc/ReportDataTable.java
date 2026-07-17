package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Apr 30, 2011
 * Time: 5:59:31 PM
 * To change this template use File | Settings | File Templates.
 */
public final class ReportDataTable implements IsSerializable {
    private String name;
    private ArrayList<ReportDataColumn> columns;
    private LinkedList<ReportDataRow> rows;
    private Integer currentIndex = 0;
    private HashMap<String, Integer> columnsName;

    public ReportDataTable() {
        columnsName = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ReportDataColumn> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<ReportDataColumn> columns) {
        this.columns = columns;
        columnsName.clear();
        for (int i = 0; i < columns.size(); i++) {
            columnsName.put(columns.get(i).getName(), i);
        }
    }

    public LinkedList<ReportDataRow> getRows() {
        return rows;
    }

    public void setRows(LinkedList<ReportDataRow> rows) {
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

    public ReportDataRow newRow() {
        return new ReportDataRow(columnsName, cloneColumns());
    }

    private ArrayList<ReportDataColumn> cloneColumns() {
        ArrayList<ReportDataColumn> result = new ArrayList<>();
        for (ReportDataColumn column : columns) {
            result.add(column.clone());
        }
        Date finish = new Date();
        return result;
    }

}
