package com.finnetlimited.reportservice.core.server.handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Apr 30, 2011
 * Time: 5:20:04 PM
 * To change this template use File | Settings | File Templates.
 */
public final class ExcelReportDataRow {

    private ExcelReportDataTable parent;
    private String[] values;

    public ExcelReportDataRow() {
    }

    public ExcelReportDataRow(ExcelReportDataTable dataTable) {
        parent = dataTable;
        values = new String[parent.getColumnCount()];
    }

    public String[] getRowValue() {
        return values;
    }

    public String getValue(Integer index) {
        return values[index];
    }

    public String getStringValue(String columnName) {
        columnName = columnName.replace("_", ".").replace("+", "_");
        int index = parent.getColumnIndex(columnName);
        if (index != -1) {
            String value = values[index];
            if (value != null && !value.equals("")) {
                return value;
            }
        }
        return "n/a";
    }

    public String getDateValue(String columnName) {
        return getDateValue(columnName, null);
    }

    public String getDateValue(String columnName, String dateFormat) {
        String value = getStringValue(columnName);
        if (value == null || "".equals(value) || value.equals("n/a")) {
            return null;
        }
        if (dateFormat == null) {
            dateFormat = parent.getDefaultShortDateFormat();
        }
        Date date = new Date(value);
        SimpleDateFormat converter = new SimpleDateFormat(dateFormat, Locale.US);
        return converter.format(date);
    }

    public Double getDoubleValue(String columnName) {
        String value = getStringValue(columnName);
        try {
            value = value.replace(",", "").replace("%", "");
            return Double.parseDouble(value);
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public void setValue(int i, String value) {
        values[i] = value;
    }

}
