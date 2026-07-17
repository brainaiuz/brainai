package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * User: ${Dilsh0d}
 * Date: 20-Mar-2010
 * Time: 15:54:02
 */
public class ReportGenerateRpc implements IsSerializable {
    private int colspan = 0;
    private String totalType;
    private LinkedList<String> columnData;
    private HashMap<String, String> actionColumnsData;

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public String getTotalType() {
        return totalType;
    }

    public void setTotalType(String totalType) {
        this.totalType = totalType;
    }

    public LinkedList<String> getColumnData() {
        if (columnData == null) {
            columnData = new LinkedList<>();
        }
        return columnData;
    }

    public void setColumnData(LinkedList<String> columnData) {
        this.columnData = columnData;
    }

    public HashMap<String, String> getActionColumnsData() {
        return actionColumnsData;
    }

    public void setActionColumnsData(HashMap<String, String> actionColumnsData) {
        this.actionColumnsData = actionColumnsData;
    }
}
