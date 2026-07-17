package com.edatasite.workforce.core.tools.sqlpatcher;

import com.edatasite.workforce.core.UpdateLogger;
import com.edatasite.workforce.core.tools.SqlScriptExecutor;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Normurod Buriev.
 * Date: 7/3/2020 8:46 PM
 */

public class RunnableSqlScript implements Callable<Void> {
    private static final Logger log = LoggerFactory.getLogger(SqlScriptExecutor.class);
    String schema;
    Connection connection;
    Connection logConnection;
    List<ScriptItem> scriptList;

    boolean isUpdateSchemaScript;

    /**
     * @param connection
     * @param logConnection
     * @param scriptSupplier
     * @param schema
     */
    public RunnableSqlScript(Connection connection, Connection logConnection, List<ScriptItem> scriptList, String schema, boolean isUpdateSchemaScript) {
        this.schema = schema;
        this.connection = connection;
        this.logConnection = logConnection;
        this.scriptList = scriptList;
        this.isUpdateSchemaScript = isUpdateSchemaScript;
    }

    @Override
    public Void call() throws Exception {
        if (!isUpdateSchemaScript) {
            createVersionTable();
        }

        for (ScriptItem script : scriptList) {
            List<SqlStatement> statements = script.getSqlStatements();
            List<String> sqlList = statements.stream().filter(statement -> {
                if (SqlScriptExecutor.PUBLIC_SCHEMA.equalsIgnoreCase(schema)) {
                    return !isPrivateStatement(statement);
                } else {
                    return isPrivateStatement(statement);
                }
            }).map(SqlStatement::getSql).collect(Collectors.toList());

            ExecutorResult result = new ExecutorResult();
            if (CollectionUtils.isNotEmpty(sqlList)) {
                try (Statement stmt = connection.createStatement()) {
                    for (String sql : sqlList) {
                        try {
                            if (!SqlScriptExecutor.PUBLIC_SCHEMA.equalsIgnoreCase(schema)) {
                                sql = sql.replace("\"anv\"", "\"" + schema + "\"");
                            }
                            result.setQuery(sql);
                            stmt.addBatch(sql);
                        } catch (SQLException e) {
                            result.setCode(SqlScriptExecutor.RESULT_FAIL);
                            result.setMessage(e.toString());
                            log.error("SQL Batch error: ", e);
                        }
                    }

                    stmt.executeBatch();
                    connection.commit();

                    if (isUpdateSchemaScript) {
                        sqlList.forEach(sql -> log.debug(sql.replace("\"anv\"", "\"" + schema + "\"")));
                    } else {
                        log.debug("Schema: {}, ScriptName: {} is SUCCESSFULLY RUN!!!", schema, script.getFilename());
                    }
                } catch (SQLException e) {
                    UpdateLogger.putIntoScriptLog(script.getClusterType(), script.getRunMode(), script.getFilename(), schema);
                    try {
                        connection.rollback();
                    } catch (SQLException e1) {
                        log.error("ERROR: on Connection.rollback(), execption: ", e1);
                    }

                    result.setCode(SqlScriptExecutor.RESULT_FAIL);
                    result.setMessage(e.toString());

                    if (isUpdateSchemaScript) {
                        log.error("Schema: {}, update schema exception: {}", schema, e.getNextException().toString(), e);
                    } else {
                        UpdateLogger.putRunScriptErrors(script.getFilename(), "SQLException(2): " + e.getNextException().toString());
                        log.error("Schema: {}, Filename: {} is FAILED!!!", schema, script.getFilename(), e);
                    }
                }

            }

            if (!isUpdateSchemaScript) {
                script.setExecutionTime(result.getElapsed());
                script.setStatus((result.getCode() == 0 || result.getCode() == SqlScriptExecutor.RESULT_SUCCESS) ? SqlScriptExecutor.SCRIPT_RESULT_SUCCESS_MSG : SqlScriptExecutor.SCRIPT_RESULT_FAIL_MSG);
                script.setDescription(script.getDescription());
                saveLog(script);
            }
        }
        return null;
    }

    boolean isPrivateStatement(SqlStatement statement) {
        String sql = statement.getSql();
        if (sql.indexOf(" anv ") >= 0 || sql.indexOf("\"anv\".") >= 0) {
            return true;
        }
        return false;
    }

    void saveLog(ScriptItem item) {
        String sql = "INSERT INTO \"" + schema + "\".schema_version(version_id, run_mode, author, status, script_name, description, execution_time)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = logConnection.prepareStatement(sql)) {
            ps.setLong(1, item.getVersion());
            ps.setShort(2, item.getRunMode());
            ps.setString(3, item.getAuthor());
            ps.setString(4, item.getStatus());
            ps.setString(5, item.getFilename());
            ps.setString(6, item.getDescription());
            ps.setLong(7, item.getExecutionTime());
            ps.execute();
        } catch (SQLException e) {
            log.error("ERROR: LOG was not saved in SCHEMA_VERSION !!!" + e.toString() + ", Schema: " + schema);
        }
    }

    void createVersionTable() {
        createVersionTable(0);
    }

    void createVersionTable(int attempt) {
        try (Statement stmt = connection.createStatement()) {
            String create = "CREATE TABLE IF NOT EXISTS \"" + schema + "\".schema_version(" +
                    "id SERIAL PRIMARY KEY," +
                    "version_id BIGINT, " +
                    "run_mode SMALLINT DEFAULT 0, " +
                    "author varchar(500) NOT NULL," +
                    "status varchar(20) NOT NULL," + // success/fail/success_with_fail
                    "script_name varchar(500) NOT NULL," +
                    "description varchar(800) NOT NULL," +
                    "installed_on TIMESTAMP DEFAULT NOW() NOT NULL," +
                    "execution_time BIGINT NOT NULL" +
                    ");";
            stmt.execute(create);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if (attempt < 3) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100L * (attempt + 1));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                createVersionTable(++attempt);
            }
        }
    }

    public void run() {
        try {
            call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
