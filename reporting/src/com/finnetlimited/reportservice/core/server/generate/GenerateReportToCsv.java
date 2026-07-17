package com.finnetlimited.reportservice.core.server.generate;

import com.csvreader.CsvWriter;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.ViewRpc;
import com.finnetlimited.reportservice.core.server.utils.SqlQueryUtil;
import com.finnetlimited.reportservice.core.server.utils.StrUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * User: ${Dilsh0d}
 * Date: 14-Apr-2010
 * Time: 13:03:56
 */
public final class GenerateReportToCsv {
    private CsvWriter csv;
    private ByteArrayOutputStream stream;
    private OutputStream outputStream;
    private ReportRpc reportRpc;
    private ResultSet resultSet;

    public GenerateReportToCsv(ReportRpc report, ResultSet resultSet, ByteArrayOutputStream stream) {
        this.reportRpc = report;
        this.resultSet = resultSet;
        this.stream = stream;
        this.stream.write(239);
        this.stream.write(187);
        this.stream.write(191);
    }

    public GenerateReportToCsv(ReportRpc report, ResultSet resultSet, OutputStream stream) throws IOException {
        this.reportRpc = report;
        this.resultSet = resultSet;
        this.outputStream = stream;
        this.outputStream.write(239);
        this.outputStream.write(187);
        this.outputStream.write(191);
    }

    public void generate(Logger log) {
        csv = new CsvWriter(stream == null ? outputStream : stream, ';', StandardCharsets.UTF_8);
        try {
            generateHeaderColumns();
            ViewRpc viewRpc = ReportType.TABULAR.name().equals(reportRpc.getTableType()) ? SqlQueryUtil.getViewParser(reportRpc.getViewCode()) : null;
            //ignore first row
            resultSet.next();
            if (reportRpc.getTableType().equals(ReportType.TABULAR.name())) {
                generateTabularReport(viewRpc);
            } else {
                ArrayList<String> selectedColumnNames = new ArrayList<>();
                for (int i = 0; i < reportRpc.getSelectedColumns().size(); i++) {
                    selectedColumnNames.add(reportRpc.getSelectedColumns().get(i).getName());
                }
                ArrayList<String> summaryColumns = new ArrayList<>();
                for (int i = 0; i < reportRpc.getSumaries().size(); i++) {
                    summaryColumns.add(reportRpc.getSumaries().get(i).getName());
                }
                generateSummaryReport(selectedColumnNames, summaryColumns, reportRpc.getGroupColumns().size() + 1, reportRpc.getSelectedColumns().size() + 1, 0);
            }
            generateFooterColumns(viewRpc, ReportType.TABULAR.name().equals(reportRpc.getTableType()));
            resultSet.close();
            csv.flush();
            csv.close();
            if (stream != null) {
                stream.flush();
                stream.close();
            } else {
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException | SQLException e) {
            log.error(e.getMessage());
        }
    }

    private void generateHeaderColumns() throws IOException {

        for (int i = 0; i < reportRpc.getSelectedColumns().size(); i++) {
            csv.write(reportRpc.getSelectedColumns().get(i).getTitle().toUpperCase());
        }
        csv.endRecord();
    }

    private void generateTabularReport(ViewRpc viewRpc) throws SQLException, IOException {
        int x = ((viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? (reportRpc.getSelectedColumns().size() + viewRpc.getHiddenColumnCount()) : reportRpc.getSelectedColumns().size()) + 1;
        int id = 1 + ((viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? viewRpc.getHiddenColumnCount() : 0);
        while (resultSet.next()) {
            for (int i = id; i <= x; i++) {
                String value = resultSet.getString(i);
                if (value == null) {
                    value = "n/a";
                }
                value = Jsoup.parse(value).text();
                csv.write(value.replace(",", "").replaceAll("(?s)<!--.*?-->", "").replaceAll("\\<[^>]*>", "").replace("&nbsp;", "\n").replace("&quot;", "\""), true);
            }
            csv.endRecord();
        }
    }

    private void generateSummaryReport(ArrayList<String> selectedColumns, ArrayList<String> summaryColumns, Integer maxDepth, final Integer columnCount, int rowIndex) throws SQLException, IOException {
        String sorderColumn = getColumnByName(reportRpc.getSortTableByColumn(), selectedColumns);
        String value;
        int depth;
        while (resultSet.next()) {
            if (sorderColumn != null) {
                depth = resultSet.getInt(columnCount + 1) + 1;
            } else {
                depth = resultSet.getInt(columnCount) + 1;
            }
            if (depth > 1) {
                for (int i = 0; i < depth - 1; i++) {
                    csv.write("", true);
                }
            }
            for (int i = depth; i < columnCount; i++) {
                value = resultSet.getString(i);
                if (value == null) {
                    value = "n/a";
                }
                value = Jsoup.parse(value).text();
                value = value.replace(",", "").replaceAll("(?s)<!--.*?-->", "").replaceAll("\\<[^>]*>", "").replace("&nbsp;", "\n").replace("&quot;", "\"").replaceAll("\\$\\{.+\\}", "");
                if (depth == maxDepth) {
                    csv.write(value, true);
                } else {
                    if (i == depth) {
                        csv.write(value, true);
                    } else if (summaryColumns.contains(selectedColumns.get(i - 1))) {
                        csv.write(value, true);
                    } else {
                        csv.write("", true);
                    }
                }
            }
            csv.endRecord();
            rowIndex++;
        }
    }


    private String getColumnByName(String columnName, ArrayList<String> columns) {
        if (!StrUtils.isEmpty(columnName)) {
            for (String column : columns) {
                if (columnName.equals(column) || columnName.replace("_", ".").equals(column.replace("_", "."))) {
                    return column;
                }
            }
        }
        return null;
    }

    private void generateFooterColumns(ViewRpc viewRpc, boolean isTabular) throws SQLException, IOException {
        Integer x = ((viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? (reportRpc.getSelectedColumns().size() + viewRpc.getHiddenColumnCount()) : reportRpc.getSelectedColumns().size()) + 1;
        int id = 1 + ((viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? viewRpc.getHiddenColumnCount() : 0);
        if (resultSet.first()) {
            if (isTabular) {
                csv.write("");
            } else {
                csv.write("Grand Total:" + resultSet.getString(id).replace(",", "").replaceAll("(?s)<!--.*?-->", "").replaceAll("\\<[^>]*>", "").replace("&nbsp;", "\n").replace("&quot;", "\"").trim());
            }

            for (int i = id; i < x; i++) {
                String value = resultSet.getString(i);
                if (reportRpc.getSumaries().contains(reportRpc.getSelectedColumns().get(i - id)) && value != null) {
                    csv.write(value.replace(",", "").replaceAll("(?s)<!--.*?-->", "").replaceAll("\\<[^>]*>", "").replace("&nbsp;", "\n").replace("&quot;", "\""), true);
                } else {
                    csv.write("", true);
                }
            }
            csv.endRecord();
        }
    }

    public ByteArrayOutputStream getStream() {
        return stream;
    }
}
