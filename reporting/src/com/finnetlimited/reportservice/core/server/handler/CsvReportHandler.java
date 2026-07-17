package com.finnetlimited.reportservice.core.server.handler;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SecuritryType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.ViewRpc;
import com.finnetlimited.reportservice.core.server.CoreServiceLocal;
import com.finnetlimited.reportservice.core.server.generate.GenerateReportToCsv;
import com.finnetlimited.reportservice.core.server.parser.XmlParser;
import com.finnetlimited.reportservice.core.server.utils.SqlQueryUtil;
import org.gwtwidgets.server.spring.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * User: ${Dilsh0d}
 * Date: 14-Apr-2010
 * Time: 17:09:03
 */
public final class CsvReportHandler implements HttpRequestHandler {

    @Autowired
    @Qualifier("reportingCoreService")
    private CoreService coreService;

    @Autowired
    @Qualifier("reportingCoreService")
    private CoreServiceLocal coreServiceLocal;
    private static Logger log = LoggerFactory.getLogger(CsvReportHandler.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM_dd_yyyy_HH_mm");
    private static final String CONTENT_TYPE = "text/csv; charset=UTF-8";

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String xmlText = request.getParameter(SecuritryType.ReportXmlString.name());
        XmlParser parser = new XmlParser();
        ReportRpc report = parser.getReportStructure(xmlText);
        report.setForPrint(true);

        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding("UTF-8");
        dateFormat.setTimeZone(TimeZone.getTimeZone(coreService.getUser().getTimezone() != null ? coreService.getUser().getTimezone() : "+0"));
        String reportname = report.getName() != null ? report.getName() : report.getViewName();
        String fileName = clearSpaces(reportname) + "_" + dateFormat.format(new Date());
        fileName = ServerUtils.normalizeFileNameT(fileName);

        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".csv\"");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");

        ServletUtils.setResponse(response);


        ResultSet result;
        report.setPosition(1);
        report.setLimit(getRowCount(report.getId()));
        ViewRpc viewRpc = SqlQueryUtil.getViewParser(report.getViewCode());
        report.setNoTimeZone(viewRpc.isNoTimezone());
        if (report.getTableType().equals(ReportType.SUMMARY.name())) {
            result = coreServiceLocal.getSummaryReportResult(report, null);
        } else {
            result = coreServiceLocal.getTabularReportResult(report, null);
        }
        GenerateReportToCsv csvGenerator = new GenerateReportToCsv(report, result, response.getOutputStream());

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.EXPORT);
        kpiLog.setEntityName(CsvReportHandler.class.getSimpleName());
        if (report.getId() == null) {
            kpiLog.setEntityId(report.getXmlTemplateId());
            kpiLog.setEntityType(report.getViewName());
        } else {
            kpiLog.setEntityId(report.getId());
            kpiLog.setEntityType(report.getViewName() + "/" + report.getName());
        }
        ServerUtils.kpiLog(log, kpiLog, "Csv Excel Report");

        csvGenerator.generate(log);
    }

    private Integer getRowCount(Integer reportId) {
        if (reportId != null) {
            ReportRpc report = coreService.getReport(reportId);
            if ((report != null && report.getMaxExcelRowCount() != null && report.getMaxExcelRowCount() > 0)) {
                return report.getMaxExcelRowCount();
            }
        }

        return 64000;
    }

    private String clearSpaces(String s) {
        if ("".equals(s)) {
            return "";
        }
        return s.replace(" ", "_");
    }
}
