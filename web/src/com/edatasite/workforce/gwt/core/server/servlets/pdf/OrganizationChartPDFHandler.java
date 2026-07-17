package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class OrganizationChartPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private DepartmentService departmentService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        String content = departmentService.getTeamGraphChart(fp.isShowView(), fp.getLevelOptionList(), false, fp.getDepartmentDoubleClickId(), fp.getShowMembersForOrgChart(), fp.getShowAllSubMembersForOrgChart(), false, null, false);
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        CustomisedITextTable iTextTable = new CustomisedITextTable();
        iTextTable.setName(content);
        customData.put("CONTENT", iTextTable);
        ITextGenericPdfData result = new ITextGenericPdfData();
        result.setCustomData(customData);
        return result;
    }


    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("ORGANIZATION_CHART_" + dateFormat(new Date()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        return "Organization Chart";
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.ORGANIZATION_CHART;
    }
}
