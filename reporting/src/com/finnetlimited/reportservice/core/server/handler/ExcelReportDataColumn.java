package com.finnetlimited.reportservice.core.server.handler;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Apr 30, 2011
 * Time: 5:13:20 PM
 * To change this template use File | Settings | File Templates.
 */
public final class ExcelReportDataColumn {

    private String name;
    private String value;

    public ExcelReportDataColumn() {
    }

    public ExcelReportDataColumn(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof String) {
            return obj.toString().equals(this.name);
        }
        return false;
    }


    protected ExcelReportDataColumn clone() {
        return new ExcelReportDataColumn(name, value);
    }
}
