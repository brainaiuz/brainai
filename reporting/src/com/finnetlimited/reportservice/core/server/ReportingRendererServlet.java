package com.finnetlimited.reportservice.core.server;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsLocale;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.LocaleManager;
import com.edatasite.workforce.gwt.core.server.db.settings.CompanySettingsManager;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.ViewRpc;
import com.finnetlimited.reportservice.core.server.utils.SqlQueryUtil;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamReader;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Ulugbek
 * Date: 10/30/12
 * Time: 12:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportingRendererServlet extends HttpServlet implements Constants {
    private CoreServiceLocal reportingCoreService;
    private CoreService reportingService;
    private LocaleManager localeManager;

    @Override
    public void init(ServletConfig config) throws ServletException {
        reportingCoreService = (CoreServiceLocal) ApplicationContextProvider.applicationContext.getBean("reportingCoreService");
        reportingService = (CoreService) ApplicationContextProvider.applicationContext.getBean("reportingCoreService");
        localeManager = (LocaleManager) ApplicationContextProvider.applicationContext.getBean("localeManager");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-Type", "text/html");
        PrintWriter writer = resp.getWriter();
        ReportRpc report = getReportObject(request.getParameter("reportrpc").replace("%2B", "+").replace("%26", "&").replace("%3F", "?"));

        String sessionId = null;
        boolean sessionExists = false;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(SESSION_ID_COOKIE)) {
                    sessionExists = true;
                    sessionId = cookie.getValue();
                }
            }
        }
        sessionId = sessionId.replace("%24", "$");
        if (!sessionExists) {
            Cookie sessionCookie = new Cookie(SESSION_ID_COOKIE, sessionId);
            //We are making SESSION_ID cookie visible for all multisubdomains
            /*if(!"localhost".equalsIgnoreCase(request.getServerName())) {
                sessionCookie.setDomain("." + request.getServerName());
            }*/
            sessionCookie.setPath("/");
            resp.addCookie(sessionCookie);
        }
        ServerSecurityContext.getInstance().setSessionId(sessionId);
        ServerSecurityContext.getInstance().setCompanyId(report.getCompanyId());
        EdsUser edsUser = ((EdsUser) ServerSecurityContext.getInstance().getUser());
        if (edsUser != null) {
            report.setUserID(edsUser.getObjectID());
        }
        ResultSet result = null;
        if (report.getTableType().equals(ReportType.SUMMARY.name())) {
            result = reportingCoreService.getSummaryReportResult(report, report.getUserID());
        } else {
            result = reportingCoreService.getTabularReportResult(report, report.getUserID());
        }
        ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
        int hideColumnCount = ReportType.TABULAR.name().equals(report.getTableType()) ? viewRpc.getHiddenColumnCount() : 0;
        try {
            String footerRow = renderColumnsFooter(result, report, hideColumnCount);
            renderColumnsHeads(report, writer);
            if (report.getTableType().equals(ReportType.SUMMARY.name())) {
                ArrayList<String> selectedColumnNames = new ArrayList<>();
                for (int i = 0; i < report.getSelectedColumns().size(); i++) {
                    selectedColumnNames.add(report.getSelectedColumns().get(i).getName());
                }
                ArrayList<String> summaryColumns = new ArrayList<>();
                for (int i = 0; i < report.getSumaries().size(); i++) {
                    summaryColumns.add(report.getSumaries().get(i).getName());
                }

                renderSummary(report, result, writer, report.getGroupColumns().size() + 1, selectedColumnNames, summaryColumns, selectedColumnNames.size() + 1, 0);
            } else {
                renderTabular(report, result, writer, report.getSelectedColumns().size() + 1, hideColumnCount);
            }
            writer.write(footerRow);
            writer.flush();
            if (result != null && !result.isClosed()) {
                result.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        writer.flush();
        writer.close();
    }

    private void renderColumnsHeads(ReportRpc report, PrintWriter writer) {
        StringBuilder builder = new StringBuilder(100);
        builder.append("<table border='1' class='table-List'>");
        builder.append("<tr>");
        for (ColumnRpc column : report.getSelectedColumns()) {
            builder.append("<td class='thead'>");
//            if (report.getTableType().equals(ReportTypeWidget.TABULAR.name())) {
            builder.append("<a href='javascript:void();' id='" + column.getAlias() + "-sort' >" + column.getTitle() + "</a>");
//            } else {
//                if (report.getGroupColumns().indexOf(column) == 0) {
//                    builder.append("<a href='javascript:void();' id='" + column.getAlias() + "-sort' >" + column.getTitle() + "</a>");
//                } else {
//                    builder.append(column.getTitle());
//                }
//            }
            builder.append("</td>");
        }
        builder.append("</tr>");
        writer.write(builder.toString());
    }

    private String renderColumnsFooter(ResultSet result, ReportRpc report, int hideColumnCount) throws SQLException {
        result.next();
        StringBuilder footerRow = new StringBuilder("<tr>");
        footerRow.append("<td class='thead'><b>Grand total:</b></br><b>");
        footerRow.append(result.getString(1 + hideColumnCount));
        footerRow.append("</b></td>");
        for (int i = 2 + hideColumnCount; i <= report.getSelectedColumns().size() + hideColumnCount; i++) {
            if (report.getSumaries().contains(report.getSelectedColumns().get(i - 1 - hideColumnCount))) {
                footerRow.append("<td class='thead'><b>" + result.getString(i) + "</b></td>");
            } else {
                footerRow.append("<td class='thead'></td>");
            }

        }
        footerRow.append("</tr></table>");
        return footerRow.toString();
    }

    private void renderTabular(ReportRpc report, ResultSet resultSet, PrintWriter writer, Integer columnCount, int hideColumnCount) throws SQLException {
        Integer rowIndex = 0;
        StringBuilder builder = new StringBuilder(1000);

        CompanySettingsManager companySettingsManager = (CompanySettingsManager) ApplicationContextProvider.applicationContext.getBean("companySettingsManager");
        EdsCompanySettings companySettings = companySettingsManager.getCompanySettings(reportingService.getUser().getCompanyId());
        SimpleDateFormat dateFormat = null;
        SimpleDateFormat shortDateFormat = null;
        if (companySettings.getLongDateFormat() != null) {
            dateFormat = new SimpleDateFormat(companySettings.getLongDateFormat());
            shortDateFormat = new SimpleDateFormat(companySettings.getShortDateFormat());
        }
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        Locale locale = Locale.ENGLISH;
        if (user != null && user.getCompany() != null && user.getCompany().getLocale() != null) {
            EdsLocale userLocale = localeManager.getLocaleBylanguageCode(user.getCompany().getLocale());
            if (userLocale != null && userLocale.getLanguageCode() != null && userLocale.getCountry() != null) {
                locale = new Locale(userLocale.getLanguageCode(), userLocale.getCountry());
            } else {
                locale = new Locale(user.getCompany().getLocale());
            }
        }
        NumberFormat numberFormat = NumberFormat.getInstance(locale);

        while (resultSet.next()) {
            if (rowIndex % 2 == 0) {
                builder.append("<tr class='even'>");
            } else {
                builder.append("<tr class='odd'>");
            }
            Integer count = (hideColumnCount > 0 ? (columnCount + hideColumnCount) : columnCount);
            Integer id = 1 + (hideColumnCount > 0 ? hideColumnCount : 0);
            for (int i = id; i < count; i++) {
                ColumnRpc selectedColumn = report.getSelectedColumns().get(i - id);
                String value = resultSet.getString(i);
                if (value == null) {
                    value = companySettings.getReportingEmptyValueString();
                } else
                    switch (selectedColumn.getColumnFormat()) {
                        case SqlQueryUtil.ColumnFormat_MONEY -> {
                            try {
                                value = numberFormat.format(Double.valueOf(value));
                            } catch (Exception e) {
                            }
                        }
                        case SqlQueryUtil.ColumnFormat_DATE -> {
                            try {
                                if ("short".equals(selectedColumn.getCustomDateFormat()) || selectedColumn.getCustomDateFormat() == null) {
                                    Date date = new Date(value);
                                    value = shortDateFormat.format(date);
                                } else if ("long".equals(selectedColumn.getCustomDateFormat())) {
                                    Date date = new Date(value);
                                    value = dateFormat.format(date);
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                builder.append("<td>" + value + "</td>");
            }
            builder.append("</tr>");
            rowIndex++;
        }
        writer.write(builder.toString());
    }

    private void renderSummary(ReportRpc report, ResultSet resultSet, PrintWriter writer, int maxDepth, ArrayList<String> selectedColumns, ArrayList<String> summaryColumns, Integer columnCount, int rowIndex) throws SQLException {

        CompanySettingsManager companySettingsManager = (CompanySettingsManager) ApplicationContextProvider.applicationContext.getBean("companySettingsManager");
        EdsCompanySettings companySettings = companySettingsManager.getCompanySettings(reportingService.getUser().getCompanyId());
        SimpleDateFormat dateFormat = null;
        SimpleDateFormat shortDateFormat = null;
        if (companySettings.getLongDateFormat() != null) {
            dateFormat = new SimpleDateFormat(companySettings.getLongDateFormat());
            shortDateFormat = new SimpleDateFormat(companySettings.getShortDateFormat());
        }
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        Locale locale = Locale.ENGLISH;
        if (user != null && user.getCompany() != null && user.getCompany().getLocale() != null) {
            EdsLocale userLocale = localeManager.getLocaleBylanguageCode(user.getCompany().getLocale());
            if (userLocale != null && userLocale.getLanguageCode() != null && userLocale.getCountry() != null) {
                locale = new Locale(userLocale.getLanguageCode(), userLocale.getCountry());
            } else {
                locale = new Locale(user.getCompany().getLocale());
            }
        }
        NumberFormat numberFormat = NumberFormat.getInstance(locale);

        StringBuilder builder = new StringBuilder(1000);
        try {
            int depth = 0;
            while (resultSet.next()) {
                depth = resultSet.getInt(columnCount) + 1;
                if (rowIndex % 2 != 0) {
                    builder.append("<tr class='even'>");
                } else {
                    builder.append("<tr class='odd'>");
                }
                if (depth > 1) {
                    builder.append("<td colspan='" + (depth - 1) + "'></td>");
                }
                for (int i = depth; i < columnCount; i++) {
                    ColumnRpc selectedColumn = report.getSelectedColumns().get(i - 1);
                    String value = resultSet.getString(i);
                    if (value == null) {
                        value = companySettings.getReportingEmptyValueString();
                    } else
                        switch (selectedColumn.getColumnFormat()) {
                            case SqlQueryUtil.ColumnFormat_MONEY -> {
                                try {
                                    value = numberFormat.format(Double.valueOf(value));
                                } catch (Exception e) {
                                }
                            }
                            case SqlQueryUtil.ColumnFormat_DATE -> {
                                try {
                                    if ("short".equals(selectedColumn.getCustomDateFormat()) || selectedColumn.getCustomDateFormat() == null) {
                                        Date date = new Date(value);
                                        value = shortDateFormat.format(date);
                                    } else if ("long".equals(selectedColumn.getCustomDateFormat())) {
                                        Date date = new Date(value);
                                        value = dateFormat.format(date);
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }

                    if (depth == maxDepth) {
                        builder.append("<td>" + value + "</td>");
                    } else {
                        if (i == depth) {
                            builder.append("<td><b>" + value.replaceAll("\\$\\{.+\\}","") + "</b></td>");
                        } else if (summaryColumns.contains(selectedColumns.get(i - 1))) {
                            builder.append("<td><b>" + value + "</b></td>");
                        } else {
                            builder.append("<td></td>");
                        }
                    }
                }
                builder.append("</tr>");
                rowIndex++;
            }
            writer.write(builder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ReportRpc getReportObject(String serializedRpc) {
        ServerSerializationStreamReader streamReader = new ServerSerializationStreamReader(Thread.currentThread().getContextClassLoader(), null);
        try {
            streamReader.prepareToRead(serializedRpc);
            return (ReportRpc) streamReader.readObject();

        } catch (SerializationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
