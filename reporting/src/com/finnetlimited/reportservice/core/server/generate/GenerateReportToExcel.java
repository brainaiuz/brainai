package com.finnetlimited.reportservice.core.server.generate;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.commons.ExcelData;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.settings.CompanySettingsManager;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.utils.EdsContextParams;
import com.finnetlimited.reportservice.core.client.gwtrpc.ViewRpc;
import com.finnetlimited.reportservice.core.server.handler.ExcelReportDataColumn;
import com.finnetlimited.reportservice.core.server.handler.ExcelReportDataRow;
import com.finnetlimited.reportservice.core.server.handler.ExcelReportDataTable;
import com.finnetlimited.reportservice.core.server.parser.HTMLParser;
import com.finnetlimited.reportservice.core.server.utils.SqlQueryUtil;
import com.finnetlimited.reportservice.core.server.utils.StrUtils;
import net.sf.jxls.reader.ReaderConfig;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.gwtwidgets.server.spring.ServletUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormatSymbols;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Ulugbek
 * Date: 11/7/12
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
public final class GenerateReportToExcel {

    private static final String ColumnFormat_NUMBER = "number";
    private static final String ColumnFormat_DOUBLE = "double";
    private static final String ColumnFormat_PERCENT = "percent";
    private static final String ColumnFormat_WITHOUT_TIME_ZONE_AND_TIME = "without_time_zone_and_time";
    private static final String ColumnFormat_WITHOUT_TIME_ZONE = "without_time_zone";
    private static final String ColumnFormat_DATE_WITHOUT_TIME_ZONE = "date_without_time_zone";
    private static final String ColumnFormat_DATE_WITHOUT_TIME = "date_without_time";
    private static final String ColumnFormat_DATE = "date";
    private static final String ColumnFormat_LONG_DATE = "long";
    private static final String ColumnFormat_SHORT_DATE = "short";
    private static final String ColumnFormat_MONEY = "money";
    private static final String ColumnFormat_TIME = "time";

    public static final String ColumnFormat_IMAGE = "image";

    private String companyName;
    private final String dateString;
    private final ReportRpc reportRpc;
    private final ResultSet resultSet;
    private OutputStream outputStream;
    private InputStream inputStream;
    private ByteArrayOutputStream stream;
    private ReportingWorkBook reportingWorkBook;
    private final TimeZone userTimeZone;

    private ExcelData[] cellDatas;

    private HSSFWorkbook workBook;
    private static final int numeric_cell_width = 7;
    private static final int string_cell_width = 20;
    private static final int date_cell_width = 10;
    private static final int time_cell_width = 7;
    private UploadManager uploadManager;

    public GenerateReportToExcel(ReportRpc report, ResultSet resultSet, OutputStream outputStream, String dateString, String companyName, TimeZone userTimeZone) {
        this.reportRpc = report;
        this.resultSet = resultSet;
        this.outputStream = outputStream;
        this.companyName = companyName;
        this.dateString = dateString;
        this.userTimeZone = userTimeZone;
    }

    public GenerateReportToExcel(ReportRpc report, ResultSet resultSet, OutputStream outputStream, String dateString, InputStream inputStream, TimeZone userTimeZone) {
        this.reportRpc = report;
        this.resultSet = resultSet;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.dateString = dateString;
        this.userTimeZone = userTimeZone;
    }

    public GenerateReportToExcel(ReportRpc report, ResultSet resultSet, ByteArrayOutputStream stream, String dateString, TimeZone userTimeZone) {
        this.reportRpc = report;
        this.resultSet = resultSet;
        this.stream = stream;
        this.dateString = dateString;
        this.userTimeZone = userTimeZone;
    }

    public GenerateReportToExcel(ReportRpc report, ResultSet resultSet, ByteArrayOutputStream stream, String dateString, InputStream inputStream, TimeZone userTimeZone) {
        this.reportRpc = report;
        this.resultSet = resultSet;
        this.stream = stream;
        this.inputStream = inputStream;
        this.dateString = dateString;
        this.userTimeZone = userTimeZone;
    }

    private static ExcelReportDataTable getReportDataTable(ResultSet resultSet, ReportRpc report, Logger log) {
        ViewRpc viewRpc = ReportType.TABULAR.name().equals(report.getTableType()) ? SqlQueryUtil.getViewParser(report.getViewCode()) : null;
        int x = ((viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? (report.getSelectedColumns().size() + viewRpc.getHiddenColumnCount()) : report.getSelectedColumns().size()) + 1;
        int id = 1 + ((viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? viewRpc.getHiddenColumnCount() : 0);
        ExcelReportDataTable result = new ExcelReportDataTable();
        try {
            LinkedList<ExcelReportDataRow> rows = new LinkedList<>();
            LinkedList<ExcelReportDataColumn> columns = new LinkedList<>();
            for (int i = id; i < x; i++) {
                ExcelReportDataColumn column = new ExcelReportDataColumn();
                if (report.getSelectedColumns().size() > (i - id)) {
                    column.setName(report.getSelectedColumns().get(i - id).getName());
                } else {
                    column.setName("column" + (i - id));
                }
                columns.add(column);
            }
            result.setColumns(columns);
            resultSet.next();
            while (resultSet.next()) {
                ExcelReportDataRow row = result.newRow();
                for (int i = id; i < x; i++) {
                    String value = resultSet.getString(i);
                    if (value != null) {
                        value = Jsoup.parse(value).text();
                    }
                    row.setValue(i - id, value);
                }
                rows.add(row);
            }
            result.setRows(rows);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return result;
    }

    private void generateHeader() {
        int columnCount = reportRpc.getSelectedColumns().size();

        ExcelData emptyData = new ExcelData(null, ExcelData.STRING, string_cell_width, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData[] emptyRow = new ExcelData[]{ emptyData };
        reportingWorkBook.registerHeaderRow(emptyRow);
        reportingWorkBook.addRow(emptyRow);

        ExcelData titleData = ExcelData.getReportNameData(reportRpc.getName(), string_cell_width, columnCount);
        ExcelData[] titleRow = new ExcelData[]{ titleData };
        reportingWorkBook.registerHeaderRow(titleRow);
        reportingWorkBook.addRow(titleRow);

        ExcelData companyData = ExcelData.getReportNameChildData(companyName, string_cell_width, columnCount);
        ExcelData[] companyRow = new ExcelData[]{ companyData };
        reportingWorkBook.registerHeaderRow(companyRow);
        reportingWorkBook.addRow(companyRow);

        ExcelData dateData = ExcelData.getReportNameChildData(dateString, string_cell_width, columnCount);
        ExcelData[] dateRow = new ExcelData[]{ dateData };
        reportingWorkBook.registerHeaderRow(dateRow);
        reportingWorkBook.addRow(dateRow);

        reportingWorkBook.registerHeaderRow(emptyRow);
        reportingWorkBook.addRow(emptyRow);

        int index = 0;
        ExcelData[] headerColumns = new ExcelData[columnCount];
        for (ColumnRpc columnRpc : reportRpc.getSelectedColumns()) {

            String columnFormat = columnRpc.getColumnFormat() != null ? columnRpc.getColumnFormat() : "";

            int dataType = switch (columnFormat) {
                case ColumnFormat_NUMBER -> ExcelData.INTEGER;
                case ColumnFormat_DOUBLE -> ExcelData.DOUBLE;
                case ColumnFormat_MONEY -> ExcelData.NUMBER_FORMAT_0_00;
                case ColumnFormat_PERCENT -> ExcelData.NUMBER_FORMAT_PERCENTAGE;
                case ColumnFormat_TIME -> ExcelData.TIME_FORMAT_Short_time_HH_mm;
                case ColumnFormat_DATE, ColumnFormat_DATE_WITHOUT_TIME,
                     ColumnFormat_WITHOUT_TIME_ZONE, ColumnFormat_WITHOUT_TIME_ZONE_AND_TIME,
                     ColumnFormat_DATE_WITHOUT_TIME_ZONE, ColumnFormat_LONG_DATE,
                     ColumnFormat_SHORT_DATE -> ExcelData.DATE;
                default -> ExcelData.STRING;
            };

            headerColumns[index++] = new ExcelData(columnRpc.getTitle(), dataType, string_cell_width, ExcelData.HEADER);
        }

        reportingWorkBook.registerHeaderRow(headerColumns);
        reportingWorkBook.addRow(headerColumns);
    }

    private void generateTabularReport(ViewRpc viewRpc, Integer companyId) throws SQLException {
        char groupingSeparator = getGroupingSeparator();
        resultSet.next();
        int x = 1 + ((viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? (reportRpc.getSelectedColumns().size() + viewRpc.getHiddenColumnCount()) : reportRpc.getSelectedColumns().size());
        int id = 1 + ((viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? viewRpc.getHiddenColumnCount() : 0);

        CompanySettingsManager companySettingsManager = (CompanySettingsManager) ApplicationContextProvider.applicationContext.getBean("companySettingsManager");
        EdsCompanySettings companySettings = companySettingsManager.getCompanySettings(companyId);
        String shortDateFormatString = "dd/MM/yyyy";
        String longDateFormatString = "dd/MM/yyyy HH:mm";
        if (companySettings.getShortDateFormat() != null) {
            shortDateFormatString = companySettings.getShortDateFormat();
        }
        if (companySettings.getLongDateFormat() != null) {
            longDateFormatString = companySettings.getLongDateFormat();
        }
        while (resultSet.next()) {
            cellDatas = new ExcelData[reportRpc.getSelectedColumns().size()];
            for (int i = id; i < x; i++) {
                String value = resultSet.getString(i);
                if (value == null) {
                    value = companySettings.getReportingEmptyValueString();
                }
                value = Jsoup.parse(value).text();
                value = replace(value, new String[]{"(?s)<!--.*?-->", "\\<[^>]*>"}, new String[]{"", ""});
                value = replace(HTMLParser.getText(value), new String[]{"&nbsp;", "&quot;"}, new String[]{"\n", "\""}).trim();
                String columnFormat = (columnFormat = reportRpc.getSelectedColumns().get(i - id).getColumnFormat()) != null ? columnFormat : "";
                switch (columnFormat) {
                    case ColumnFormat_NUMBER -> {
//                        if (value.equals("n/a") || value.isEmpty()) {
//                            value = "0";
//                        }
                        value = value.replace("" + groupingSeparator, "");
                        cellDatas[i - id] = getCell(value, ExcelData.INTEGER);
                    }
                    case ColumnFormat_IMAGE -> {
                        if (value.equals("n/a") || value.isEmpty()) {
                            value = "0";
                        }
                        value = value.replace("" + groupingSeparator, "");
                        value = getFileUrl(Integer.valueOf(value));
                        cellDatas[i - id] = getCell(value, ExcelData.STRING);
                    }
                    case ColumnFormat_DOUBLE -> {
//                        if (value.equals("n/a") || value.isEmpty()) {
//                            value = "0";
//                        }
                        value = value.replace("" + groupingSeparator, "");
                        cellDatas[i - id] = getCell(value, ExcelData.DOUBLE);
                    }
                    case ColumnFormat_MONEY -> {
//                        if (value.equals("n/a") || value.isEmpty()) {
//                            value = "0";
//                        }
                        value = value.replace("" + groupingSeparator, "");
                        cellDatas[i - id] = getCell(value, ExcelData.NUMBER_FORMAT_0_00);
                    }
                    case ColumnFormat_PERCENT -> {
//                        if (value.equals("n/a") || value.isEmpty()) {
//                            value = "0";
//                        }
                        value = value.replace("" + groupingSeparator, "");
                        cellDatas[i - id] = getCell(value, ExcelData.NUMBER_FORMAT_PERCENTAGE);
                    }
                    case ColumnFormat_TIME -> {
//                        if (value.equals("n/a") || value.isEmpty()) {
//                            value = "00:00";
//                        }
                        value = value.replace("" + groupingSeparator, "");
                        cellDatas[i - id] = getCell(value, ExcelData.TIME_FORMAT_Short_time_HH_mm);
                    }
                    case ColumnFormat_DATE, ColumnFormat_DATE_WITHOUT_TIME, ColumnFormat_WITHOUT_TIME_ZONE, ColumnFormat_WITHOUT_TIME_ZONE_AND_TIME, ColumnFormat_DATE_WITHOUT_TIME_ZONE, ColumnFormat_LONG_DATE, ColumnFormat_SHORT_DATE -> {
                        String customDateFormat = reportRpc.getSelectedColumns().get(i - id).getCustomDateFormat();
                        if (customDateFormat.equals(ColumnFormat_LONG_DATE)) {
                            customDateFormat = longDateFormatString;
                        } else if (customDateFormat.equals(ColumnFormat_SHORT_DATE)) {
                            customDateFormat = shortDateFormatString;
                        }
                        if (customDateFormat.toLowerCase().contains("h")) {
                            cellDatas[i - id] = getCell(value, ExcelData.DATE_LONG);
                        } else {
                            cellDatas[i - id] = getCell(value, ExcelData.DATE);
                        }
                        cellDatas[i - id].setCustomDateFormat(customDateFormat);
                    }
                    default ->
                            cellDatas[i - id] = new ExcelData(value, ExcelData.STRING, string_cell_width, ExcelData.NORMAL);
                }
            }
            reportingWorkBook.addRow(cellDatas);
        }
    }

    public String getFileUrl(Integer fileId) {
        String url;
        EdsUpload upload = (EdsUpload) uploadManager.get(fileId);
        if (upload != null) {
            String getLink = uploadManager.getFileURL(upload);
            if (!StringUtil.isEmpty(getLink)) {
                url = getLink;
            } else {
                url = "File not found";
            }
        } else {
            url = EdsContextParams.getFullHost() + "common/downloadFile?id=" + fileId;
        }
        return url;
    }

    private char getGroupingSeparator() {
        char groupingSeparator = ',';
        try {
            if (ServletUtils.getRequest() != null && ServletUtils.getRequest().getServerName() != null) {
                Locale locale = EdsContextParams.getDefaultLocale(ServletUtils.getRequest().getServerName());
                groupingSeparator = DecimalFormatSymbols.getInstance(locale).getGroupingSeparator();
                groupingSeparator = !(("" + groupingSeparator).isEmpty()) ? groupingSeparator : ',';
            }
        } catch (Exception e) {
            groupingSeparator = DecimalFormatSymbols.getInstance(Locale.getDefault()).getGroupingSeparator();
            groupingSeparator = !(("" + groupingSeparator).isEmpty()) ? groupingSeparator : ',';
        }
        return groupingSeparator;
    }

    public static String replace(String text, String[] repl, String[] with) {

        StringBuilder buf = new StringBuilder(text);
        for (int i = 0; i < repl.length; i++) {
            text = buf.toString();
            if (text.contains(repl[i])) {
                buf.delete(0, buf.length());
                String[] arrays = text.split(repl[i]);
                for (String item : arrays) {
                    buf.append(item).append(with[i]);
                }
            }
        }
        return buf.toString();
    }

    private void generateSummaryReport(ArrayList<String> selectedColumns, ArrayList<String> summaryColumns, Integer maxDepth, Integer columnCount, Integer companyId) throws SQLException {
        char groupingSeparator = getGroupingSeparator();
        CompanySettingsManager companySettingsManager = (CompanySettingsManager) ApplicationContextProvider.applicationContext.getBean("companySettingsManager");
        EdsCompanySettings companySettings = companySettingsManager.getCompanySettings(companyId);
        String shortDateFormatString = "dd/MM/yyyy";
        String longDateFormatString = "dd/MM/yyyy HH:mm";
        if (companySettings.getShortDateFormat() != null) {
            shortDateFormatString = companySettings.getShortDateFormat();
        }
        if (companySettings.getLongDateFormat() != null) {
            longDateFormatString = companySettings.getLongDateFormat();
        }
        String sortableColumn = getColumnByName(reportRpc.getSortTableByColumn(), selectedColumns);
        String local = ServerUtils.getUserLocale().getLanguage();
        String totalStr;
        if ("ru".equals(local)) {
            totalStr = "Итог";
        } else if ("uz".equals(local)) {
            totalStr = "Jami";
        } else {
            totalStr = "Total";
        }

        resultSet.next();
        int lastDepth = 1;
        while (resultSet.next()) {
            short color = -1;
            cellDatas = new ExcelData[reportRpc.getSelectedColumns().size()];
            int depth;
            if (sortableColumn != null) {
                depth = resultSet.getInt(columnCount + 1) + 1;
            } else {
                depth = resultSet.getInt(columnCount) + 1;
            }
            if (!Objects.equals(depth, maxDepth) || lastDepth != 1) {
                for (int i = 0; i < depth - 1; i++) {
                    if (i < lastDepth - 1) {
                        cellDatas[i] = new ExcelData(null, ExcelData.STRING, string_cell_width, ExcelData.NORMAL);
                    } else {
                        String value = resultSet.getString(i + 1);
                        if (value == null) {
                            value = companySettings.getReportingEmptyValueString();
                        }
                        value = Jsoup.parse(value).text();
                        cellDatas[i] = new ExcelData(value, ExcelData.STRING, string_cell_width, ExcelData.HEADER_LEFT);
                    }
                }
            } else {
                for (int i = 0; i < depth - 1; i++) {
                    String value = resultSet.getString(i + 1);
                    if (value == null) {
                        value = companySettings.getReportingEmptyValueString();
                    }
                    value = Jsoup.parse(value).text();
                    cellDatas[i] = new ExcelData(value, ExcelData.STRING, string_cell_width, ExcelData.HEADER_LEFT);
                }
            }
            for (int i = 1; i < depth && i < columnCount; i++) {
                switch (i) {
                    case 1:
                        cellDatas[i - 1] = new ExcelData("", ExcelData.STRING, string_cell_width, ExcelData.HEADER_LEFT);
                        cellDatas[i - 1].setBgcolor(HSSFColor.GREY_40_PERCENT.index);
                        break;
                    case 2:
                        cellDatas[i - 1] = new ExcelData("", ExcelData.STRING, string_cell_width, ExcelData.HEADER_LEFT);
                        cellDatas[i - 1].setBgcolor(HSSFColor.GREY_25_PERCENT.index);
                        break;
                }
            }
            int j = 1;
            for (int i = depth; i < columnCount; i++) {
                String value = resultSet.getString(i);
                if (value == null) {
                    value = companySettings.getReportingEmptyValueString();
                }
                value = Jsoup.parse(value).text();
                if (j == 1 && !Objects.equals(depth, maxDepth)) {
                    value = totalStr + " : " + value;
                    color = switch (depth) {
                        case 1 -> HSSFColor.GREY_40_PERCENT.index;
                        case 2 -> HSSFColor.GREY_25_PERCENT.index;
                        default -> color;
                    };
                }
                j++;
                value = replace(value, new String[]{",", "(?s)<!--.*?-->", "\\<[^>]*>", "&nbsp;", "&quot;", "-  #"}, new String[]{"", "", "", "\n", "\"", "", ""}).trim();
                value = replace(value, new String[]{",", "(?s)<!--.*?-->", "\\<[^>]*>", "&nbsp;", "&quot;"}, new String[]{"", "", "", "\n", "\""}).trim();

                String columnFormat = reportRpc.getSelectedColumns().get(i - 1).getColumnFormat();

                if (Objects.equals(depth, maxDepth)) {
                    switch (columnFormat) {
                        case ColumnFormat_NUMBER -> {
//                            if (value.equals("n/a") || value.isEmpty()) {
//                                value = "0";
//                            }

                            value = value.replace(String.valueOf(groupingSeparator), "");
                            cellDatas[i - 1] = getCell(value, ExcelData.INTEGER);
                        }
                        case ColumnFormat_IMAGE -> {
                            if (value.equals("n/a") || value.isEmpty()) {
                                value = "0";
                            }
                            value = getFileUrl(Integer.valueOf(value));
                            value = value.replace("" + groupingSeparator, "");
                            cellDatas[i - 1] = getCell(value, ExcelData.STRING);
                        }
                        case ColumnFormat_DOUBLE -> {
//                            if (value.equals("n/a") || value.isEmpty()) {
//                                value = "0";
//                            }

                            value = value.replace(String.valueOf(groupingSeparator), "");
                            cellDatas[i - 1] = getCell(value, ExcelData.DOUBLE);
                        }
                        case ColumnFormat_MONEY -> {
//                            if (value.equals("n/a") || value.isEmpty()) {
//                                value = "0";
//                            }

                            value = value.replace(String.valueOf(groupingSeparator), "");
                            cellDatas[i - 1] = getCell(value, ExcelData.NUMBER_FORMAT_0_00);
                        }
                        case ColumnFormat_PERCENT -> {
//                            if (value.equals("n/a") || value.isEmpty()) {
//                                value = "0";
//                            }
                            value = value.replace(String.valueOf(groupingSeparator), "");
                            cellDatas[i - 1] = getCell(value, ExcelData.NUMBER_FORMAT_PERCENTAGE);
                        }
                        case ColumnFormat_DATE, ColumnFormat_DATE_WITHOUT_TIME, ColumnFormat_WITHOUT_TIME_ZONE, ColumnFormat_WITHOUT_TIME_ZONE_AND_TIME, ColumnFormat_DATE_WITHOUT_TIME_ZONE, ColumnFormat_LONG_DATE, ColumnFormat_SHORT_DATE -> {
                            String customDateFormat = reportRpc.getSelectedColumns().get(i - 1).getCustomDateFormat();
                            if (customDateFormat.equals(ColumnFormat_LONG_DATE)) {
                                customDateFormat = longDateFormatString;
                            } else if (customDateFormat.equals(ColumnFormat_SHORT_DATE)) {
                                customDateFormat = shortDateFormatString;
                            }
                            if (customDateFormat.toLowerCase().contains("h")) {
                                cellDatas[i - 1] = getCell(value, ExcelData.DATE_LONG);
                            } else {
                                cellDatas[i - 1] = getCell(value, ExcelData.DATE);
                            }

                            cellDatas[i - 1].setCustomDateFormat(customDateFormat);
                        }
                        default -> {
                            cellDatas[i - 1] = new ExcelData(value, ExcelData.STRING, string_cell_width, ExcelData.NORMAL);
                        }
                    }
                } else {
                    if (i == depth || summaryColumns.contains(selectedColumns.get(i - 1))) {
                        switch (columnFormat) {
                            case ColumnFormat_NUMBER -> {
//                                if (value.equals("n/a") || value.isEmpty()) {
//                                    value = "0";
//                                }
                                value = value.replace(String.valueOf(groupingSeparator), "");
                                cellDatas[i - 1] = getCell(value, ExcelData.INTEGER);
                            }
                            case ColumnFormat_MONEY -> {
//                                if (value.equals("n/a") || value.isEmpty()) {
//                                    value = "0";
//                                }
                                value = value.replace(String.valueOf(groupingSeparator), "");
                                cellDatas[i - 1] = getCell(value, ExcelData.NUMBER_FORMAT_0_00);
                            }
                            case ColumnFormat_PERCENT -> {
//                                if (value.equals("n/a") || value.isEmpty()) {
//                                    value = "0";
//                                }
                                value = value.replace(String.valueOf(groupingSeparator), "");
                                cellDatas[i - 1] = getCell(value, ExcelData.NUMBER_FORMAT_PERCENTAGE);
                            }
                            case ColumnFormat_DATE, ColumnFormat_DATE_WITHOUT_TIME, ColumnFormat_WITHOUT_TIME_ZONE, ColumnFormat_WITHOUT_TIME_ZONE_AND_TIME, ColumnFormat_DATE_WITHOUT_TIME_ZONE, ColumnFormat_LONG_DATE, ColumnFormat_SHORT_DATE -> {
                                cellDatas[i - 1] = getCell(value.replaceAll("\\$\\{.+}", ""), ExcelData.DATE);
                            }
                            default -> {
                                cellDatas[i - 1] = new ExcelData(value, ExcelData.STRING, string_cell_width, ExcelData.HEADER_LEFT);
                            }
                        }
                    } else {
                        cellDatas[i - 1] = new ExcelData(null, ExcelData.STRING, string_cell_width, ExcelData.NORMAL);
                    }
                }

            }
            if (color != -1) {
                for (int i = depth; i < columnCount; i++) {
                    cellDatas[i - 1].setBgcolor(color);
                }
            }
            lastDepth = depth;
            reportingWorkBook.addRow(cellDatas);
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

    private void generateFooterColumns(ViewRpc viewRpc) throws SQLException {
        int x = ((viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? (reportRpc.getSelectedColumns().size() + viewRpc.getHiddenColumnCount()) : reportRpc.getSelectedColumns().size()) + 1;
        int id = 1 + ((viewRpc != null && viewRpc.getHiddenColumnCount() > 0) ? viewRpc.getHiddenColumnCount() : 0);
        if (resultSet.first()) {
            cellDatas = new ExcelData[reportRpc.getSelectedColumns().size()];
            if (ReportType.TABULAR.name().equals(reportRpc.getTableType()) || reportRpc.getGroupColumns().isEmpty()) {
                cellDatas[0] = new ExcelData(null, ExcelData.STRING, string_cell_width, ExcelData.HEADER);
            } else {
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    String totalGrand = "Hammasi:" + resultSet.getString(id).replace(",", "").replaceAll("(?s)<!--.*?-->", "").replaceAll("<[^>]*>", "").replace("&nbsp;", "\n").replace("&quot;", "\"").replaceAll("- {2}#", "\n").trim();
                    cellDatas[0] = new ExcelData(totalGrand, ExcelData.STRING, string_cell_width, ExcelData.HEADER);
                } else if (ServerUtils.getUserLocale().getLanguage().equals("ru")) {
                    String totalGrand = "ОБЩИЙ ИТОГ :" + resultSet.getString(id).replace(",", "").replaceAll("(?s)<!--.*?-->", "").replaceAll("<[^>]*>", "").replace("&nbsp;", "\n").replace("&quot;", "\"").replaceAll("- {2}#", "\n").trim();
                    cellDatas[0] = new ExcelData(totalGrand, ExcelData.STRING, string_cell_width, ExcelData.HEADER);
                } else {
                    String totalGrand = "Grand Total:" + resultSet.getString(id).replace(",", "").replaceAll("(?s)<!--.*?-->", "").replaceAll("<[^>]*>", "").replace("&nbsp;", "\n").replace("&quot;", "\"").replaceAll("- {2}#", "\n").trim();
                    cellDatas[0] = new ExcelData(totalGrand, ExcelData.STRING, string_cell_width, ExcelData.HEADER);
                }
            }
            for (int i = id + 1; i < x; i++) {
                String value = resultSet.getString(i);
                if (value != null) {
                    value = Jsoup.parse(value).text();
                }
                if (reportRpc.getSumaries().contains(reportRpc.getSelectedColumns().get(i - id))) {
                    value = value.replace(",", "").replaceAll("(?s)<!--.*?-->", "")
                            .replaceAll("<[^>]*>", "").replace("&nbsp;", "\n").replace("&quot;", "\"")
                            .replaceAll("- {2}#", "");
                    cellDatas[i - id] = new ExcelData(value, ExcelData.STRING, string_cell_width, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
                } else {
                    cellDatas[i - id] = new ExcelData(null, ExcelData.STRING, string_cell_width, ExcelData.HEADER);
                }
            }
            reportingWorkBook.addRow(cellDatas);
//            list.add(cellDatas);
        }
    }

    public void generate(Logger log, Integer companyId) {
        reportingWorkBook = new ReportingWorkBook();
        workBook = reportingWorkBook.getWorkBook(reportRpc.getSheetName() != null ? reportRpc.getSheetName() : reportRpc.getViewName(), userTimeZone);
//        workBook.setRepeatingRowsAndColumns(0, 0, reportRpc.getSelectedColumns().size() - 1, 0, 4);
        try {
            if (reportRpc.getExcelTemplateId() != null) {
                generateCustomSavedReport(log);
            } else {
                generateHeader();
                ViewRpc viewRpc = ReportType.TABULAR.name().equals(reportRpc.getTableType()) ? SqlQueryUtil.getViewParser(reportRpc.getViewCode()) : null;
                if (reportRpc.getTableType().equals(ReportType.TABULAR.name())) {
                    generateTabularReport(viewRpc, companyId);
                } else {
                    ArrayList<String> selectedColumnNames = new ArrayList<>();
                    for (int i = 0; i < reportRpc.getSelectedColumns().size(); i++) {
                        selectedColumnNames.add(reportRpc.getSelectedColumns().get(i).getName());
                    }
                    ArrayList<String> summaryColumns = new ArrayList<>();
                    for (int i = 0; i < reportRpc.getSumaries().size(); i++) {
                        summaryColumns.add(reportRpc.getSumaries().get(i).getName());
                    }

                    generateSummaryReport(selectedColumnNames, summaryColumns, reportRpc.getGroupColumns().size() + 1, reportRpc.getSelectedColumns().size() + 1, companyId);
                }
                generateFooterColumns(viewRpc);

                resultSet.close();
            }
            if (stream != null) {
                workBook.write(stream);
            } else {
                workBook.write(outputStream);
                outputStream.flush();
                outputStream.close();
            }
        } catch (SQLException | IOException e) {
            log.error(e.getMessage());
        }
    }

    private void generateCustomSavedReport(Logger log) {
        ExcelReportDataTable reportTable = getReportDataTable(resultSet, reportRpc, log);
        if (reportTable != null) {
            HashMap<String, LinkedList<ExcelReportDataRow>> beans = new HashMap<>();
            //     HashMap<String, ArrayList<ReportDataTable>> beans1 = new HashMap<String, ArrayList<ReportDataTable>>();
            beans.put("reportItem", reportTable.getRows());
            XLSTransformer transformer = new XLSTransformer();
            ReaderConfig.getInstance().setUseDefaultValuesForPrimitiveTypes(true);
            try {
                workBook = (HSSFWorkbook) transformer.transformXLS(inputStream, beans);
            } catch (InvalidFormatException e) {
                log.error(e.getMessage());
            }
        }
    }

    public ByteArrayOutputStream getStream() {
        return stream;
    }

    private ExcelData getCell(String value, Integer cellType) {

        switch (cellType) {
            case ExcelData.INTEGER -> {
                if (isNumeric(value)) {
                    return new ExcelData(new BigInteger(value), ExcelData.INTEGER, numeric_cell_width, ExcelData.NORMAL);
                } else {
                    return new ExcelData(value, ExcelData.STRING, string_cell_width, ExcelData.NORMAL);
                }
            }
            case ExcelData.DOUBLE -> {
                if (isNumeric(value)) {
                    return new ExcelData(Double.valueOf(value), ExcelData.DOUBLE, numeric_cell_width, ExcelData.NORMAL);
                } else {
                    return new ExcelData(value, ExcelData.STRING, string_cell_width, ExcelData.NORMAL);
                }
            }
            case ExcelData.NUMBER_FORMAT_0_00 -> {
                if (isNumeric(value)) {
                    return new ExcelData(Double.parseDouble(value), ExcelData.NUMBER_FORMAT_0_00, numeric_cell_width, ExcelData.NORMAL);
                } else {
                    return new ExcelData(value, ExcelData.STRING, string_cell_width, ExcelData.NORMAL);
                }
            }
            case ExcelData.NUMBER_FORMAT_PERCENTAGE -> {
                String tempValue = value;
                if (value.endsWith("%")) {
                    tempValue = value.substring(0, value.length() - 1);
                }
                if (isNumeric(tempValue)) {
                    return new ExcelData(Double.parseDouble(tempValue) / 100, ExcelData.NUMBER_FORMAT_PERCENTAGE, numeric_cell_width, ExcelData.NORMAL);
                } else {
                    return new ExcelData(value, ExcelData.STRING, string_cell_width, ExcelData.NORMAL);
                }
            }
            case ExcelData.DATE -> {
                return new ExcelData(value, ExcelData.DATE, date_cell_width, ExcelData.NORMAL);
            }
            case ExcelData.DATE_LONG -> {
                return new ExcelData(value, ExcelData.DATE_LONG, date_cell_width, ExcelData.NORMAL);
            }
            case ExcelData.TIME_FORMAT_Short_time_HH_mm -> {
                return new ExcelData(value, ExcelData.TIME_FORMAT_Short_time_HH_mm, time_cell_width, ExcelData.NORMAL);
            }
            default -> {
                return new ExcelData(value, ExcelData.STRING, string_cell_width, ExcelData.NORMAL);
            }
        }
    }

    public boolean isNumeric(String str) {
        return str.matches("-?\\d+(.\\d+)?");
    }

    public void setUploadManager(UploadManager uploadManager) {
        this.uploadManager = uploadManager;
    }

    public UploadManager getUploadManager() {
        return uploadManager;
    }
}
