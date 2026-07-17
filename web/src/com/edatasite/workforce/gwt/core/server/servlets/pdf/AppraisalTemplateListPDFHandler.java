package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.TemplateListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

public class AppraisalTemplateListPDFHandler extends AbstractITextPostPdfHandler implements Constants {

    private AssessmentService assessmentService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextTableList tableList = new ITextTableList(1);
        pdfData.setListTable(tableList);
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsUser user = uploadManager.getUser();

        ListLoadConfig config = new ListLoadConfig();
        if (filterParametrs.getStart() != null) {
            config.setStart(filterParametrs.getStart());
        }
//        if (filterParametrs.getLimit() != null) {
//            config.setLimit(filterParametrs.getLimit());
//        } else {
//            config.setLimit(20);
//        }
        if (filterParametrs.getSortField() != null) {
            config.setSortField(filterParametrs.getSortField());
        }
        if (filterParametrs.getSortDir() != null) {
            config.setSortDir(filterParametrs.getSortDir());
        }
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        String tableName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.onlyAppraisalTemplates);
        fp.setSortField(TemplateListItem.NAME);
        pdfData.setTableName(tableName);
        ListResult<TemplateListItem> templateList = assessmentService.getTemplates(fp);
        tableList.addPdfTableHeader(commonLocalizer.localize(PdfLocalizationName.name));
        if (templateList != null && templateList.getList() != null && templateList.getList().size() > 0) {
            for (TemplateListItem template : templateList.getList()) {
                tableList.addPdfTableRows(template.getName());
            }
        }

        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_AppraisalTemplates_" + dateFormat(user.getUserDate()));
    }

    public void setAssessmentService(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }
}
