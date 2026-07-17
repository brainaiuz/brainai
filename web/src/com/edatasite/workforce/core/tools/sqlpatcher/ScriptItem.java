package com.edatasite.workforce.core.tools.sqlpatcher;

import com.edatasite.workforce.core.tools.SqlScriptExecutor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Sql script containing a series of statements terminated by a delimiter (eg: ;).
 * Single-line (--) and multi-line (/* * /) comments are stripped and ignored.
 */
public class ScriptItem {

    private long version;
    private String filename;
    private String author;
    private String status = SqlScriptExecutor.SCRIPT_RESULT_SUCCESS_MSG;
    private String failedSchemes;
    private String description;
    private long executionTime;
    private File file;
    private short runMode;
    private String clusterType;
    private List<String> statements;
    private String schema;

    public ScriptItem(short runMode) {
        this.runMode = runMode;
    }

    public ScriptItem(long version, String filename, String author, String desc, File file, short runMode) {
        this.version = version;
        this.filename = filename;
        this.author = author;
        this.description = desc;
        this.file = file;
        this.runMode = runMode;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public short getRunMode() {
        return runMode;
    }

    public void setRunMode(short runMode) {
        this.runMode = runMode;
    }

    public String getFailedSchemes() {
        return failedSchemes;
    }

    public void setFailedSchemes(String failedSchemes) {
        this.failedSchemes = failedSchemes;
    }

    public List<String> getStatements() {
        if (statements == null) {
            statements = new ArrayList<>();
        }
        return statements;
    }

    public void setStatements(List<String> statements) {
        this.statements = statements;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getClusterType() {
        return clusterType;
    }

    public void setClusterType(String clusterType) {
        this.clusterType = clusterType;
    }

    public List<SqlStatement> getSqlStatements()  {
        List<String> result = new ArrayList<>();

        if (SqlScriptExecutor.SCHEMA_UPDATE == runMode) {
            result = statements;
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(this.file))) {
                String s;
                while ((s = br.readLine()) != null) {
                    result.add(s);
                }
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

        SqlScript script = new SqlScript();
        return script.linesToStatements(result);
    }
}
