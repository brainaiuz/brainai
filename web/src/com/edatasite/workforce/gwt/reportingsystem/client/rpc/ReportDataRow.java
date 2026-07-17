package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Apr 30, 2011
 * Time: 5:20:04 PM
 * To change this template use File | Settings | File Templates.
 */
public final class ReportDataRow implements IsSerializable {

    private ArrayList<ReportDataColumn> columns;
    private HashMap<String, Integer> columnsName = new HashMap<>();

    public ReportDataRow() {
    }

    public ReportDataRow(HashMap<String, Integer> columnsName, ArrayList<ReportDataColumn> columns) {
        this.columnsName = columnsName;
        this.columns = columns;
    }

    public ArrayList<ReportDataColumn> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<ReportDataColumn> columns) {
        this.columns = columns;
        columnsName.clear();
        Integer i = -1;
        for (ReportDataColumn column : columns) {
            columnsName.put(column.getName(), ++i);
        }
    }

    public ArrayList<String> getRowValue() {
        ArrayList<String> result = new ArrayList<>();
        for (ReportDataColumn column : columns) {
            result.add(column.getValue());
        }
        return result;
    }

    public String getValue(Integer index) {
        if (columns != null && columns.size() > 0) {
            return columns.get(index).getValue();
        }
        return null;
    }

    public String getStringValue(String columnName) {
        columnName = columnName.replace("_", ".").replace("+", "_");
        Integer i = getColumnsName().get(columnName);
        if (i != null) {
            String value = columns.get(i).getValue();
            if (value != null) {
                return value;
            }
        }
        return "n/a";
    }

    public Date getDateValue(String columnName) {
        String value = getStringValue(columnName);
        if (value == null || "".equals(value) || value.equals("n/a")) {
            return null;
        }
        return new Date(value);
    }

    public Double getDoubleValue(String columnName) {
        String value = getStringValue(columnName);
        try {
            value = value.replace(",", "").replace("%", "");
            return Double.parseDouble(value);
        } catch (Exception ex) {
            //       ex.printStackTrace();
        }
        return 0.0;
    }

    public HashMap<String, Integer> getColumnsName() {
        Integer i = -1;
        if (columnsName == null || columnsName.size() == 0) {
            for (ReportDataColumn column : columns) {
                columnsName.put(column.getName(), ++i);
            }
        }
        return columnsName;
    }
//
//    public String getDateValue(String columnName, String arg) {
//        Date date = getDateValue(columnName);
//        if (arg != null && !arg.equals("") && date != null) {
//            return ServerUtils.dateFormat(date, arg);
//        }
//        return "";
//    }
//
//    public String getUtcDateValue(String columnName, String arg) {
//        Date date = getDateValue(columnName);
//        if (arg != null && !arg.equals("") && date != null) {
//            return DateUtils.format(date, arg);
//        }
//        return "";
//    }
}
