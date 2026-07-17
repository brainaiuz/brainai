package com.edatasite.workforce.core.tools.sqlpatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostgreSQLSqlStatementBuilder extends SqlStatementBuilder {

    static final String DOLLAR_QUOTE_REGEX = "(\\$[A-Za-z0-9_]*\\$).*";
    private static final Delimiter COPY_DELIMITER = new Delimiter("\\.", true);
    private boolean firstLine = true;
    private String copyStatement;
    private boolean pgCopy;
    private String statementStart = "";

    public PostgreSQLSqlStatementBuilder(Delimiter defaultDelimiter) {
        super(defaultDelimiter);
    }

    @Override
    public SqlStatement getSqlStatement() {
        return super.getSqlStatement();
    }

    @Override
    protected void applyStateChanges(String line) {
        super.applyStateChanges(line);

        if (!executeInTransaction) {
            return;
        }

        if (StringUtils.countOccurrencesOf(statementStart, " ") < 100) {
            statementStart += line;
            statementStart += " ";
            statementStart = statementStart.replaceAll("\\s+", " ");
        }

        if (statementStart.matches("(CREATE|DROP) (DATABASE|TABLESPACE) .*")
                || statementStart.matches("ALTER SYSTEM .*")
                || statementStart.matches("(CREATE|DROP)( UNIQUE)? INDEX CONCURRENTLY .*")
                || statementStart.matches("REINDEX( VERBOSE)? (SCHEMA|DATABASE|SYSTEM) .*")
                || statementStart.matches("VACUUM .*")
                || statementStart.matches("DISCARD ALL .*")
                || statementStart.matches("ALTER TYPE .* ADD VALUE .*")
                ) {
            executeInTransaction = false;
        }
    }

    @Override
    protected String[] tokenizeLine(String line) {
        return StringUtils.tokenizeToStringArray(line, " @<>;:=|(),+{}\\[\\]");
    }

    @Override
    protected String extractAlternateOpenQuote(String token) {
        Matcher matcher = Pattern.compile(DOLLAR_QUOTE_REGEX).matcher(token);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Override
    protected Delimiter changeDelimiterIfNecessary(String line, Delimiter delimiter) {
        if (pgCopy) {
            return COPY_DELIMITER;
        }

        if (firstLine) {
            firstLine = false;
            if (line.matches("COPY|COPY\\s.*")) {
                copyStatement = line;
            }
        } else if (copyStatement != null) {
            copyStatement += " " + line;
        }

        if (copyStatement != null && copyStatement.contains(" FROM STDIN")) {
            pgCopy = true;
            return COPY_DELIMITER;
        }

        if (statementStart
                .matches("CREATE( OR REPLACE)? RULE .* DO (ALSO|INSTEAD) \\(.+;\\w?\\)\\w?;")) {
            return Delimiter.SEMICOLON;
        }

        if (statementStart.matches("CREATE( OR REPLACE)? RULE .* DO (ALSO|INSTEAD) \\(.*")) {
            return null;
        }

        return delimiter;
    }

    @Override
    protected String cleanToken(String token) {
        if (token.startsWith("E'")) {
            return token.substring(token.indexOf("'"));
        }

        return token;
    }
}