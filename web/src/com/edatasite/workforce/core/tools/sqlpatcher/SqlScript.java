package com.edatasite.workforce.core.tools.sqlpatcher;

import java.util.ArrayList;
import java.util.List;

public class SqlScript {

    public SqlScript() {

    }

    List<SqlStatement> linesToStatements(List<String> lines) {
        List<SqlStatement> statements = new ArrayList<>();

        Delimiter nonStandardDelimiter = null;
        SqlStatementBuilder sqlStatementBuilder = createSqlStatementBuilder();

        for (int lineNumber = 1; lineNumber <= lines.size(); lineNumber++) {
            String line = lines.get(lineNumber - 1);

            if (sqlStatementBuilder.isEmpty()) {
                if (!StringUtils.hasText(line)) {
                    // Skip empty line between statements.
                    continue;
                }

                Delimiter newDelimiter = sqlStatementBuilder.extractNewDelimiterFromLine(line);
                if (newDelimiter != null) {
                    nonStandardDelimiter = newDelimiter;
                    // Skip this line as it was an explicit delimiter change directive outside of any statements.
                    continue;
                }

                sqlStatementBuilder.setLineNumber(lineNumber);

                // Start a new statement, marking it with this line number.
                if (nonStandardDelimiter != null) {
                    sqlStatementBuilder.setDelimiter(nonStandardDelimiter);
                }
            }

            try {
                sqlStatementBuilder.addLine(line);
            } catch (Exception e) {
//                throw new FlywayException("Flyway parsing bug (" + e.getMessage() + ") at line " + lineNumber + ": " + line, e);
            }

            if (sqlStatementBuilder.canDiscard()) {
                sqlStatementBuilder = createSqlStatementBuilder();
            } else if (sqlStatementBuilder.isTerminated()) {
                addStatement(statements, sqlStatementBuilder);
                sqlStatementBuilder = createSqlStatementBuilder();
            }
        }

        // Catch any statements not followed by delimiter.
        if (!sqlStatementBuilder.isEmpty()) {
            addStatement(statements, sqlStatementBuilder);
        }

        return statements;
    }

    protected SqlStatementBuilder createSqlStatementBuilder() {
        return new PostgreSQLSqlStatementBuilder(Delimiter.SEMICOLON);
    }

    private void addStatement(List<SqlStatement> statements,
                              SqlStatementBuilder sqlStatementBuilder) {
        SqlStatement sqlStatement = sqlStatementBuilder.getSqlStatement();
        statements.add(sqlStatement);

//        if (sqlStatementBuilder.executeInTransaction()) {
//        } else {
//        }

//        if (!mixed && transactionalStatementFound && nonTransactionalStatementFound) {
//            throw new FlywayException(
//                    "Detected both transactional and non-transactional statements within the same migration"
//                            + " (even though mixed is false). Offending statement found at line "
//                            + sqlStatement.getLineNumber() + ": " + sqlStatement.getSql()
//                            + (sqlStatementBuilder.executeInTransaction() ? "" : " [non-transactional]"));
//        }

    }

}