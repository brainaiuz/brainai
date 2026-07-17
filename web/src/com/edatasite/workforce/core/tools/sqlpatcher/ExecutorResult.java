package com.edatasite.workforce.core.tools.sqlpatcher;

import com.edatasite.workforce.core.tools.SqlScriptExecutor;

public class ExecutorResult {
    private int code = SqlScriptExecutor.RESULT_SUCCESS;
    private String message = SqlScriptExecutor.SCRIPT_RESULT_SUCCESS_MSG;
    private String query;
    private String filename;
    private String database;
    private String schema;
    private long elapsed;
    private String failedSchemes;

    public ExecutorResult() {
    }

    public ExecutorResult(int code, String message, String query, String filename, String database) {
        this.code = code;
        this.message = message;
        this.query = query;
        this.filename = filename;
        this.database = database;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public long getElapsed() {
        return elapsed;
    }

    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }

    public String getFailedSchemes() {
        return failedSchemes;
    }

    public void setFailedSchemes(String failedSchemes) {
        this.failedSchemes = failedSchemes;
    }
}