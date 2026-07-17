package com.edatasite.workforce.core.tools.sqlpatcher;

import java.util.ArrayList;
import java.util.List;

public class SqlStatementBuilder {

    protected final Delimiter defaultDelimiter;
    protected StringBuilder statement = new StringBuilder();
    protected int lineNumber;
    protected boolean executeInTransaction = true;
    protected Delimiter delimiter;
    private boolean empty = true;
    private boolean terminated;
    private boolean insideQuoteStringLiteral = false;
    private boolean insideAlternateQuoteStringLiteral = false;
    private String alternateQuote;
    private boolean lineEndsWithSingleLineComment = false;
    private boolean insideMultiLineComment = false;
    private boolean nonCommentStatementPartSeen = false;

    public SqlStatementBuilder(Delimiter defaultDelimiter) {
        this.defaultDelimiter = defaultDelimiter;
        this.delimiter = defaultDelimiter;
    }

    static void stripDelimiter(StringBuilder sql, Delimiter delimiter) {
        int last;

        for (last = sql.length(); last > 0; last--) {
            if (!Character.isWhitespace(sql.charAt(last - 1))) {
                break;
            }
        }

        sql.delete(last - delimiter.getDelimiter().length(), sql.length());
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setDelimiter(Delimiter delimiter) {
        this.delimiter = delimiter;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isTerminated() {
        return terminated;
    }

    public SqlStatement getSqlStatement() {
        return new SqlStatement(lineNumber, statement.toString());
    }

    @SuppressWarnings("UnusedParameters")
    public Delimiter extractNewDelimiterFromLine(String line) {
        return null;
    }

    public boolean isCommentDirective(String line) {
        return false;
    }

    protected boolean isSingleLineComment(String line) {
        return line.startsWith("--");
    }

    public void addLine(String line) {
        if (isEmpty()) {
            empty = false;
        } else {
            statement.append("\n");
        }

        if (isCommentDirective(line.trim())) {
            nonCommentStatementPartSeen = true;
        }

        String lineSimplified = simplifyLine(line);

        applyStateChanges(lineSimplified);
        if (endWithOpenMultilineStringLiteral() || insideMultiLineComment || isSingleLineComment(
                lineSimplified)) {
            statement.append(line);
            return;
        }

        delimiter = changeDelimiterIfNecessary(lineSimplified, delimiter);

        statement.append(line);

        if (!lineEndsWithSingleLineComment && lineTerminatesStatement(lineSimplified, delimiter)) {
            stripDelimiter(statement, delimiter);
            terminated = true;
        }
    }

    boolean endWithOpenMultilineStringLiteral() {
        return insideQuoteStringLiteral || insideAlternateQuoteStringLiteral;
    }

    public boolean canDiscard() {
        return !insideAlternateQuoteStringLiteral && !insideQuoteStringLiteral
                && !insideMultiLineComment && !nonCommentStatementPartSeen;
    }

    protected String simplifyLine(String line) {
        return removeEscapedQuotes(line)
                .replace("--", " -- ")
                .replace("/*", " /* ")
                .replace("*/", " */ ")
                .replaceAll("\\s+", " ").trim().toUpperCase();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    protected Delimiter changeDelimiterIfNecessary(String line, Delimiter delimiter) {
        return delimiter;
    }

    private boolean lineTerminatesStatement(String line, Delimiter delimiter) {
        if (delimiter == null) {
            return false;
        }

        String upperCaseDelimiter = delimiter.getDelimiter().toUpperCase();

        if (delimiter.isAloneOnLine()) {
            return line.equals(upperCaseDelimiter);
        }

        return line.endsWith(upperCaseDelimiter);
    }

    protected String extractAlternateOpenQuote(String token) {
        return null;
    }

    protected String computeAlternateCloseQuote(String openQuote) {
        return openQuote;
    }

    protected void applyStateChanges(String line) {
        String[] tokens = tokenizeLine(line);

        List<TokenType> delimitingTokens = extractStringLiteralDelimitingTokens(tokens);

        lineEndsWithSingleLineComment = false;
        for (TokenType delimitingToken : delimitingTokens) {
            if (!insideQuoteStringLiteral && !insideAlternateQuoteStringLiteral
                    && TokenType.MULTI_LINE_COMMENT_OPEN.equals(delimitingToken)) {
                insideMultiLineComment = true;
            }
            if (!insideQuoteStringLiteral && !insideAlternateQuoteStringLiteral
                    && TokenType.MULTI_LINE_COMMENT_CLOSE.equals(delimitingToken)) {
                insideMultiLineComment = false;
            }

            if (!insideQuoteStringLiteral && !insideAlternateQuoteStringLiteral && !insideMultiLineComment
                    && TokenType.SINGLE_LINE_COMMENT.equals(delimitingToken)) {
                lineEndsWithSingleLineComment = true;
                return;
            }

            if (!insideMultiLineComment && !insideQuoteStringLiteral &&
                    TokenType.ALTERNATE_QUOTE.equals(delimitingToken)) {
                insideAlternateQuoteStringLiteral = !insideAlternateQuoteStringLiteral;
            }

            if (!insideMultiLineComment && !insideAlternateQuoteStringLiteral &&
                    TokenType.QUOTE.equals(delimitingToken)) {
                insideQuoteStringLiteral = !insideQuoteStringLiteral;
            }

            if (!insideMultiLineComment && !insideQuoteStringLiteral && !insideAlternateQuoteStringLiteral
                    &&
                    TokenType.OTHER.equals(delimitingToken)) {
                nonCommentStatementPartSeen = true;
            }
        }
    }

    protected String[] tokenizeLine(String line) {
        return StringUtils.tokenizeToStringArray(line, " @<>;:=|(),+{}");
    }

    private List<TokenType> extractStringLiteralDelimitingTokens(String[] tokens) {
        List<TokenType> delimitingTokens = new ArrayList<>();
        for (String token : tokens) {
            String cleanToken = cleanToken(token);
            boolean handled = false;

            if (alternateQuote == null) {
                String alternateQuoteFromToken = extractAlternateOpenQuote(cleanToken);
                if (alternateQuoteFromToken != null) {
                    String closeQuote = computeAlternateCloseQuote(alternateQuoteFromToken);
                    if (cleanToken.length() >= (alternateQuoteFromToken.length() + closeQuote.length())
                            && cleanToken.startsWith(alternateQuoteFromToken) && cleanToken
                            .endsWith(closeQuote)) {
                        //Skip $$abc$$, ...
                        continue;
                    }

                    alternateQuote = closeQuote;
                    delimitingTokens.add(TokenType.ALTERNATE_QUOTE);

                    continue;
                }
            }
            if ((alternateQuote != null) && cleanToken.endsWith(alternateQuote)) {
                alternateQuote = null;
                delimitingTokens.add(TokenType.ALTERNATE_QUOTE);
                continue;
            }

            if ((cleanToken.length() >= 2) && cleanToken.startsWith("'") && cleanToken.endsWith("'")) {
                //Skip '', 'abc', ...
                continue;
            }
            if ((cleanToken.length() >= 4)) {
                int numberOfOpeningMultiLineComments = StringUtils.countOccurrencesOf(cleanToken, "/*");
                int numberOfClosingMultiLineComments = StringUtils.countOccurrencesOf(cleanToken, "*/");
                if (numberOfOpeningMultiLineComments > 0
                        && numberOfOpeningMultiLineComments == numberOfClosingMultiLineComments) {
                    //Skip /**/, /*comment*/, ...
                    continue;
                }
            }

            if (isSingleLineComment(cleanToken)) {
                delimitingTokens.add(TokenType.SINGLE_LINE_COMMENT);
                handled = true;
            }

            if (cleanToken.contains("/*")) {
                delimitingTokens.add(TokenType.MULTI_LINE_COMMENT_OPEN);
                handled = true;
            } else if (cleanToken.startsWith("'")) {
                delimitingTokens.add(TokenType.QUOTE);
                handled = true;
            }

            if (!cleanToken.contains("/*") && cleanToken.contains("*/")) {
                delimitingTokens.add(TokenType.MULTI_LINE_COMMENT_CLOSE);
                handled = true;
            } else if (!cleanToken.startsWith("'") && cleanToken.endsWith("'")) {
                delimitingTokens.add(TokenType.QUOTE);
                handled = true;
            }

            if (!handled) {
                delimitingTokens.add(TokenType.OTHER);
            }
        }

        return delimitingTokens;
    }

    protected String removeEscapedQuotes(String token) {
        return StringUtils.replaceAll(token, "''", "");
    }

    protected String cleanToken(String token) {
        return token;
    }

    public boolean executeInTransaction() {
        return executeInTransaction;
    }

    private enum TokenType {
        OTHER,
        QUOTE,
        ALTERNATE_QUOTE,
        SINGLE_LINE_COMMENT,
        MULTI_LINE_COMMENT_OPEN,
        MULTI_LINE_COMMENT_CLOSE
    }
}