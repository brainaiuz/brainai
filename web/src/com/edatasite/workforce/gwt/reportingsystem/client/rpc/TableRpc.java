package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * User: ${Dilsh0d}
 * Date: 18-Mar-2010
 * Time: 16:53:00
 */
public class TableRpc implements IsSerializable {
    private String tableName;
    private LinkedList<ColumnRpc> columns;
    private HashMap<String, Integer> columnIndex = new HashMap<>();

    //Custom field info
    private String customFieldType;
    private String customFieldEntityName;
    private String customFieldAlias;
    private String customFieldJoin;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public LinkedList<ColumnRpc> getColumns() {
        return columns;
    }

    public void setColumns(LinkedList<ColumnRpc> columns) {
        this.columns = columns;
    }

    public String getCustomFieldType() {
        return customFieldType;
    }

    public void setCustomFieldType(String customFieldType) {
        this.customFieldType = customFieldType;
    }

    public String getCustomFieldEntityName() {
        return customFieldEntityName;
    }

    public void setCustomFieldEntityName(String customFieldEntityName) {
        this.customFieldEntityName = customFieldEntityName;
    }

    public String getCustomFieldAlias() {
        return customFieldAlias;
    }

    public void setCustomFieldAlias(String customFieldAlias) {
        this.customFieldAlias = customFieldAlias;
    }

    public void setCustomFieldJoin(String customFieldJoin) {
        this.customFieldJoin = customFieldJoin;
    }

    public String getCustomFieldJoin() {
        return customFieldJoin;
    }

    public HashMap<String, Integer> getColumnIndex() {
        if (columnIndex.isEmpty()) {
            int i = -1;
            for (ColumnRpc rpc : columns) {
                columnIndex.put(rpc.getName(), ++i);
            }
        }
        return columnIndex;
    }
}
