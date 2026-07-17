package com.finnetlimited.reportservice.core.server;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.pdf.EdsPdfTemplateSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextCompanyData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.ViewRpc;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.NameValuePair;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ReportingRecurrencePdfService {

    ByteArrayOutputStream generatePDFFromHTML(ReportRpc reportRpc, EdsReport report, EdsCompany company, String url, EdsCompanyPdfTemplate edsPdfTemplate);

    ByteArrayOutputStream doRequest(ITextGenericPdfData pdfData, List<NameValuePair> params);

    ITextGenericPdfData buildPdfDocumentCustomise(ReportRpc reportRpc, EdsReport report, boolean hasPhantom, EdsCompany company);

    Integer getRowCount(EdsReport report);

    ITextTableList generate(ReportRpc reportRpc, Integer index, EdsCompany company);

    ITextTableList getHeader(ReportRpc reportRpc);

    Integer getAlignment(ColumnRpc columnRpc);

    void generateTabularReport(ResultSet resultSet, ITextTableList tableList, Integer index, ViewRpc viewRpc, ReportRpc reportRpc, EdsCompany company) throws SQLException;

    SimpleDateFormat getCompanyShortDateFormat(EdsCompany company);

    SimpleDateFormat getCompanyLongDateFormat(EdsCompany company);

    void generateSummaryReport(ResultSet resultSet, ITextTableList tableList, ArrayList<String> selectedColumns, ArrayList<String> summaryColumns, Integer maxDepth, Integer columnCount, Integer index, ReportRpc reportRpc, EdsCompany company) throws SQLException;

    String getColumnByName(String columnName, ArrayList<String> columns);

    void getFooter(ITextTableList tableList, ResultSet resultSet, Integer index, ViewRpc viewRpc, ReportRpc reportRpc) throws SQLException;

    PdfParams getParams(ReportRpc reportRpc);

    ITextCompanyData getCompanyData(boolean customised, boolean hasPhantom, EdsCompany company);

    String escapeHtml(String value);

    Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(EdsCompanySettings companySettings, EdsCompany company);

    String getPdfLogoUrl(boolean hasPhantom, EdsCompany company) throws IOException;

    String getCompanyLogoUrl(EdsCompany company);

    String getRealPath(String path) throws IOException;

    String getPdfStampUrl(String type, EdsCompany company) throws IOException;

    String getStampUrl(String type, EdsCompany company) throws IOException;

    String generateHTMLContentWithUrl(EdsPdfTemplateSettings pdfTemplate, ITextGenericPdfData pdfData, String url);

    StringBuilder getGenerateAPIUrl(ITextGenericPdfData pdfData);

}
