package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class SupervisorStructurePDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private EmployeeService employeeService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        String content = employeeService.getEmployeeGraphChart(fp.isShowView(), fp.getLevelOptionListForSprvs(), fp.isLevelActive());
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        CustomisedITextTable iTextTable = new CustomisedITextTable();
        iTextTable.setName(content);
        customData.put("SUPERVISOR", iTextTable);
        ITextGenericPdfData result = new ITextGenericPdfData();
        result.setCustomData(customData);
        return result;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("SUPERVISOR_STRUCTURE_" + dateFormat(new Date()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        return "Supervisor Structure";
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.SUPERVISOR_STRUCTURE;
    }
}
