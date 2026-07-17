package com.edatasite.workforce.gwt.core.server.rpc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class TableResult {
    Queue<TableRow> rows;

    public TableResult() {
        rows = new LinkedList<>();
    }

    public static void formTable(ResultSet resultSet, TableResult table) throws SQLException {
        if (resultSet == null) {
            return;
        }

        ResultSetMetaData resultSetMetaData;
        try {
            resultSetMetaData = resultSet.getMetaData();

            while (resultSet.next()) {
                TableRow currentRow = new TableRow();

                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    currentRow.add(resultSet.getObject(i), resultSetMetaData.getColumnTypeName(i));
                }

                table.add(currentRow);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public void add(TableRow row) {
        rows.add(row);
    }

    public TableRow poll() {
        return rows.poll();
    }

    public Queue<TableRow> getRows() {
        return rows;
    }
}
