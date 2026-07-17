package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import java.util.*;

/**
 * Created by Farrukh on 08-Jun-17.
 */
public class CompetenceListPDFHandler extends AbstractITextPostPdfHandler implements Constants {

    private AssessmentService assessmentService;
    private String tableName;
    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom){
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        tableName = property != null ? property.getPlural() : commonLocalizer.localize("competencies");
        filterParametrs.setLimit(1000);
        EdsUser user = uploadManager.getUser();

        ListResult<SkillItem> competenciesList = assessmentService.getCompetencies(filterParametrs);
        List<SkillItem> competencies = competenciesList.getList();

        pdfData.setTableName(tableName);

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        List<String> header = panelTools.getColumnCodeName();
        List<String> header2 = new ArrayList<>();

        header.remove(SkillItem.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(SkillItem.COMPETENCY_GROUP_NAME, commonLocalizer.localize(PdfLocalizationName.skillGroup));
        mapColumnHeader.put(SkillItem.COMPETENCY_NAME, commonLocalizer.localize("competencyName"));
        mapColumnHeader.put(SkillItem.COMPETENCY_DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }

        tableList.addPdfTableHeader(header2.toArray(new String[]{}));

        for (SkillItem competence : competencies) {
            List<String> cell = new ArrayList<>();
            for (int i = 0; i < header.size(); i++) {
                if (SkillItem.COMPETENCY_GROUP_NAME.equals(header.get(i))) {
                    cell.add(header.indexOf(SkillItem.COMPETENCY_GROUP_NAME), (competence.getGroupName() != null ? competence.getGroupName() : ""));
                }
                if (SkillItem.COMPETENCY_NAME.equals(header.get(i))) {
                    cell.add(header.indexOf(SkillItem.COMPETENCY_NAME), (competence.getName() != null ? competence.getName() : ""));
                }
                if (SkillItem.COMPETENCY_DESCRIPTION.equals(header.get(i))) {
                    cell.add(header.indexOf(SkillItem.COMPETENCY_DESCRIPTION), (competence.getDescription() != null ? competence.getDescription() : ""));
                }
            }
            tableList.addPdfTableRows(cell.toArray(new String[]{}));
        }

        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_Competences_" + dateFormat(new Date()));
    }

    public void setAssessmentService(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        tableName = property != null ? property.getPlural() : commonLocalizer.localize("competencies");
        return tableName;
    }
}
