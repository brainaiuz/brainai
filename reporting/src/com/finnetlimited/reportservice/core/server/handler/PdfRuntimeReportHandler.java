package com.finnetlimited.reportservice.core.server.handler;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SecuritryType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.ReportRequestObject;
import com.finnetlimited.reportservice.core.server.parser.XmlParser;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: May 16, 2011
 * Time: 5:56:34 PM
 * To change this template use File | Settings | File Templates.
 */
public final class PdfRuntimeReportHandler extends PdfReportHandler {
    private ReportRequestObject reportRequestObject;
    private Boolean isLandscapeForm = false;
    public boolean prepareRequest(HttpServletRequest request) {
        reportRequestObject = new ReportRequestObject();
        XmlParser parser = new XmlParser();
        String xmlText = request.getParameter(SecuritryType.ReportXmlString.name());
        ReportRpc reportRpc = parser.getReportStructure(xmlText);
        if(reportRpc != null && reportRpc.isLandscape() != null && reportRpc.isLandscape()) {
            isLandscapeForm = true;
        }
        setCurrentReportID(reportRpc.getId());
        reportRequestObject.setXml(xmlText);
        return true;
    }

    protected Object getDataClass(HttpServletRequest request) {
        return new ReportRequestObject();
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {

      //document.setPageSize(PageSize.A4);
        //   document.setPageSize(PageSize.A4.rotate());

        return super.buildPdfDocument(reportRequestObject, document, writer);
    }
    @Override
    protected Document newDocument(EdsCompany edsCompany, Object dataClass) {
        if(isLandscapeForm) {
            return new Document(PageSize.A4.rotate(), 20, 20, 120, 50);
        } else {
           return new Document(PageSize.A4, 20, 20, 120, 50);
        }
    }
    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        return super.buildPdfDocumentCustomise(reportRequestObject, company, hasPhantom);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ReportRpc reportRpc = wrapReport(reportRequestObject);
        String reportName = reportRpc.getName();
        if (reportName == null || reportName.equals("")) {
            reportName = reportRpc.getViewName();
        }
        setFileName(clearSpaces(reportName) + "_" + dateFormat.format(user.getUserDate()));

    }

    @Override
    protected PdfParams getParams(Object dataClass) {
        return super.getParams(reportRequestObject);
    }

    @Override
    protected String getTableName(Object dataClass) {
        return super.getTableName(reportRequestObject);
    }

    @Override
    public String getDownloadType() {
        return PDFDownloadType.ATTACHMENT;
    }
}
