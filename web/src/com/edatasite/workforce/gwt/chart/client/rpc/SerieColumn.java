package com.edatasite.workforce.gwt.chart.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class SerieColumn implements IsSerializable, Serializable {

    /**
     * column code in query mode exm. t.paidAmount
     */
    private String column;
    /**
     * column type {number, money, string, time}
     */
    private String columnType;
    /**
     * column format {percent, string, money, time ...}
     */
    private String columnFormat;
    /**
     * column title  exm. Paid amount
     */
    private String columnTitle;

    public SerieColumn() {}

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnFormat() {
        return columnFormat;
    }

    public void setColumnFormat(String columnFormat) {
        this.columnFormat = columnFormat;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }
}
