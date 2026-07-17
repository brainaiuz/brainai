package com.finnetlimited.reportservice.core.server.handler;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.MinIOManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SecuritryType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.UserSecuritryRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.ViewRpc;
import com.finnetlimited.reportservice.core.server.CoreServiceLocal;
import com.finnetlimited.reportservice.core.server.generate.GenerateReportToExcel;
import com.finnetlimited.reportservice.core.server.parser.XmlParser;
import com.finnetlimited.reportservice.core.server.utils.SqlQueryUtil;
import com.finnetlimited.reportservice.core.server.utils.StrUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Sep 14, 2011
 * Time: 4:26:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExcelReportHandler implements HttpRequestHandler {

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM_dd_yyyy_HH_mm");
    private static final Logger log = LoggerFactory.getLogger(ExcelReportHandler.class);
    private static final String CONTENT_TYPE_EXCEL = "application/vnd.ms-excel:UTF-8";
    private final SimpleDateFormat dateFormatForTitle = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    @Autowired
    @Qualifier("reportingCoreService")
    private CoreService coreService;
    @Autowired
    @Qualifier("reportingCoreService")
    private CoreServiceLocal coreServiceLocal;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private MinIOManager minIOManager;

    @Override
    public void handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String xmlText = httpServletRequest.getParameter(SecuritryType.ReportXmlString.name());
        XmlParser parser = new XmlParser();
        ReportRpc reportRpc = parser.getReportStructure(xmlText);
        reportRpc.setLimit(0);
        Integer excelTemplateId = reportRpc.getExcelTemplateId();
        UserSecuritryRpc user = coreService.getUser();

        if (user == null) {
            return;
        }

        String companyName = user.getCompanyName();
        dateFormat.setTimeZone(TimeZone.getTimeZone(user.getTimezone() != null ? user.getTimezone() : "+0"));
        String fileNameDate = dateFormat.format(new Date());

        dateFormatForTitle.setTimeZone(TimeZone.getTimeZone(user.getTimezone() != null ? user.getTimezone() : "+0"));
        String creationDate = dateFormatForTitle.format(new Date());

        String reportname = reportRpc.getName() != null ? reportRpc.getName() : reportRpc.getViewName();
        reportRpc.setSheetName(getSheetName(reportRpc.getName() != null ? reportRpc.getName() : reportRpc.getViewName()));
        String filename = clearSpaces(reportname) + "_" + fileNameDate;
        String local = ServerUtils.getUserLocale().getLanguage();
        String dateString;
        if ("ru".equals(local)) {
            dateString = "Создано на: " + creationDate;
        } else if ("uz".equals(local)) {
            dateString = "Yaratilgan: " + creationDate;
        } else {
            dateString = "Сreated on: " + creationDate;
        }


        filename = filename.replace(" ", "_").replace("/", "_");
        filename = ServerUtils.normalizeFileNameT(filename);

        String encodedFilename = URLEncoder.encode(filename + ".xls", StandardCharsets.UTF_8.toString());

        httpServletResponse.setHeader("content-disposition", "attachment; filename=\"" + encodedFilename + ".xls\"");
        httpServletResponse.setContentType(CONTENT_TYPE_EXCEL);
        httpServletResponse.setCharacterEncoding("UTF8");

        ResultSet resultSet;
        reportRpc.setPosition(1);
        reportRpc.setLimit(getRowCount(reportRpc.getId()));
        ViewRpc viewRpc = SqlQueryUtil.getViewParser(reportRpc.getViewCode());
        reportRpc.setNoTimeZone(viewRpc.isNoTimezone());
        if (reportRpc.getGroupColumns().isEmpty()) {
            reportRpc.setTableType(ReportType.TABULAR.name());
        }
        TimeZone userTimeZone = uploadManager != null && uploadManager.getUser() != null ? uploadManager.getUser().getUserTimezone() : null;
        if (reportRpc.getTableType().equals(ReportType.TABULAR.name()) || excelTemplateId != null) {
            if (excelTemplateId != null && reportRpc.getMaxExcelRowCount() != null) {
                reportRpc.setLimit(getRowCount(reportRpc.getId()));
            }
            resultSet = coreServiceLocal.getTabularReportResult(reportRpc, null);
        } else {
            resultSet = coreServiceLocal.getSummaryReportResult(reportRpc, null);
        }
        GenerateReportToExcel excelGenerator;
        if (excelTemplateId != null) {
            excelGenerator = new GenerateReportToExcel(reportRpc, resultSet, httpServletResponse.getOutputStream(), dateString, getSavedExcelTemplateInputStream(excelTemplateId), userTimeZone);
        } else {
            excelGenerator = new GenerateReportToExcel(reportRpc, resultSet, httpServletResponse.getOutputStream(), dateString, companyName, userTimeZone);
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setActionType(KpiLog.ActionType.EXPORT);
        if (reportRpc.getId() == null) {
            kpiLog.setEntityType(reportRpc.getViewName());
            kpiLog.setEntityId(reportRpc.getXmlTemplateId());
        } else {
            kpiLog.setEntityId(reportRpc.getId());
            kpiLog.setEntityType(reportRpc.getViewName() + "/" + reportRpc.getName());
        }
        kpiLog.setEntityName(ExcelReportHandler.class.getSimpleName());
        ServerUtils.kpiLog(log, kpiLog, "Export Excel Report");
        excelGenerator.setUploadManager(uploadManager);
        excelGenerator.generate(log, user.getCompanyId());
    }

    private String getSheetName(String name) {
        name = name.replace("/", "").replace("\"", "");
        name = name.replace("?", "").replace("*", "");
        return name;
    }

    public ByteArrayOutputStream run(String xmlText, Integer companyID, EdsUser user) {
        XmlParser parser = new XmlParser();
        ReportRpc reportRpc = parser.getReportStructure(xmlText);
        Integer excelTemplateId = reportRpc != null && reportRpc.getExcelTemplateId() != null ? reportRpc.getExcelTemplateId() : null;
        reportRpc.setUserID(user.getObjectID());
        reportRpc.setCompanyId(companyID);
        reportRpc.setLimit(getRowCount(reportRpc.getId(), companyID));
        reportRpc.setPosition(1);
        ResultSet resultSet;
        if (reportRpc.getTableType().equals(ReportType.TABULAR.name()) || excelTemplateId != null) {
            resultSet = coreServiceLocal.getTabularReportResult(reportRpc, user.getObjectID());
        } else {
            resultSet = coreServiceLocal.getSummaryReportResult(reportRpc, user.getObjectID());
        }
        String dateString = "Created on: " + dateFormatForTitle.format(user.getUserDate());
        GenerateReportToExcel excelGenerator;
        if (excelTemplateId != null) {
            excelGenerator = new GenerateReportToExcel(reportRpc, resultSet, new ByteArrayOutputStream(), dateString, getSavedExcelTemplateInputStream(excelTemplateId), user.getUserTimezone());
        } else {
            excelGenerator = new GenerateReportToExcel(reportRpc, resultSet, new ByteArrayOutputStream(), dateString, user.getUserTimezone());
        }
        excelGenerator.generate(log, companyID);
        return excelGenerator.getStream();
    }

    public InputStream getSavedExcelTemplateInputStream(Integer excelTemplateId) {
        EdsUpload excel = (EdsUpload) uploadManager.get(excelTemplateId);

        return uploadManager.getInputStream(excel);

    }

    private String clearSpaces(String s) {
        if (StrUtils.isEmpty(s)) {
            return "";
        }
        return s.replace(" ", "_");
    }

    private Integer getRowCount(Integer reportId) {
        if (reportId != null) {
            ReportRpc report = coreService.getReport(reportId);
            if ((report != null && report.getMaxExcelRowCount() != null && report.getMaxExcelRowCount() > 0)) {
                return report.getMaxExcelRowCount();
            }
        }

        return 0;
    }

    private Integer getRowCount(Integer reportId, Integer companyId) {
        if (reportId != null) {
            ReportRpc report = coreService.getReport(reportId, companyId);
            if ((report != null && report.getMaxExcelRowCount() != null && report.getMaxExcelRowCount() > 0)) {
                return report.getMaxExcelRowCount();
            }
        }

        return 64000;
    }
}
