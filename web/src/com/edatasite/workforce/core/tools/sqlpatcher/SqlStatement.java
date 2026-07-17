package com.edatasite.workforce.core.tools.sqlpatcher;

public class SqlStatement {
    private int line;
    private String sql;

    public SqlStatement(int lineNumber, String sql) {
        this.line = lineNumber;
        this.sql = sql;
    }

    public int getLineNumber() {
        return line;
    }

    public String getSql() {
        return sql;
    }
}