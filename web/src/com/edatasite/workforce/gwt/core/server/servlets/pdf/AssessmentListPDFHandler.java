package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentsListElem;
import com.edatasite.workforce.gwt.assessment.client.ui.AssessmentHelper;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssessmentListPDFHandler extends AbstractITextPostPdfHandler implements Constants {

    private AssessmentService assessmentService;
    private String tableName;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {

        DecimalFormat numberFormat = new DecimalFormat(",##0.00");

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        EdsUser user = uploadManager.getUser();
        filterParametrs.setIssueRelatedTo(EMPLOYEE_ISSUE);
        ListResult<AssessmentsListElem> assessmentList = assessmentService.getAssessmentsList(filterParametrs,null);
        List<AssessmentsListElem> assessElems = assessmentList.getList();
        pdfData.setTableName(commonLocalizer.localize(PdfLocalizationName.appraisalsArchive, "Appraisals List"));
        pdfData.setExtraData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertDateFormatFromEngToUzb(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + "  Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        List<String> header = panelTools.getColumnCodeName();
        List<String> header2 = new ArrayList<>();

        header.remove(AssessmentsListElem.ACTION);
        header.remove(AssessmentsListElem.PDF_VERSION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(AssessmentsListElem.ASSESSMENT_NAME, commonLocalizer.localize(PdfLocalizationName.employee));
        mapColumnHeader.put(AssessmentsListElem.TEMPLATE_NAME, commonLocalizer.localize(PdfLocalizationName.templateName));
        mapColumnHeader.put(AssessmentsListElem.INITIATION_DATE, commonLocalizer.localize(PdfLocalizationName.initiatedDate));
        mapColumnHeader.put(AssessmentsListElem.INITIATOR_NAME, commonLocalizer.localize(PdfLocalizationName.initiatedBy));
        mapColumnHeader.put(AssessmentsListElem.REVIEWER_NAME, commonLocalizer.localize(PdfLocalizationName.reviewers));
        mapColumnHeader.put(AssessmentsListElem.ASSESSMENT_TYPE, commonLocalizer.localize(PdfLocalizationName.type));
        mapColumnHeader.put(AssessmentsListElem.ASSESSMENT_STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        mapColumnHeader.put(AssessmentsListElem.OVERALL_SCORE, commonLocalizer.localize(PdfLocalizationName.overallScore));
        mapColumnHeader.put(AssessmentsListElem.OVERALL_GRADE, commonLocalizer.localize(PdfLocalizationName.overallGrade));
        mapColumnHeader.put(AssessmentsListElem.VALIDITY_PERIOD, commonLocalizer.localize(PdfLocalizationName.validityPeriod));
        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }

        tableList.addPdfTableHeader(header2.toArray(new String[]{}));


        for (AssessmentsListElem assessElem : assessElems) {
            List<String> cell = new ArrayList<>();
            for (int ii = 0; ii < header.size(); ii++) {
                if (AssessmentsListElem.ASSESSMENT_NAME.equals(header.get(ii))) {
                    cell.add(header.indexOf(AssessmentsListElem.ASSESSMENT_NAME), assessElem.getEmployeeName());
                }
                if (AssessmentsListElem.TEMPLATE_NAME.equals(header.get(ii))) {
                    cell.add(header.indexOf(AssessmentsListElem.TEMPLATE_NAME), assessElem.getTemplateName());
                }
                if (assessElem.getInitiationDate() != null && AssessmentsListElem.INITIATION_DATE.equals(header.get(ii))) {
                    cell.add(header.indexOf(AssessmentsListElem.INITIATION_DATE), assessElem.getInitiationDate() != null ? dateFormat(assessElem.getInitiationDate()) : "");
                }
                if (AssessmentsListElem.INITIATOR_NAME.equals(header.get(ii))) {
                    cell.add(header.indexOf(AssessmentsListElem.INITIATOR_NAME), assessElem.getInitiatorName());
                }
                if (AssessmentsListElem.REVIEWER_NAME.equals(header.get(ii))) {
                    cell.add(header.indexOf(AssessmentsListElem.REVIEWER_NAME), assessElem.getReviewerName());
                }
                if (assessElem.getAssessmentType() != null && AssessmentsListElem.ASSESSMENT_TYPE.equals(header.get(ii))) {
//                        try {
                    cell.add(header.indexOf(AssessmentsListElem.ASSESSMENT_TYPE), assessElem.getAssessmentType());
//									ASSESSMENT_360.equals(assessElem.getAssessmentType()) ? htmlParser.performParse("360&nbsp;&deg;") : "Simple");
//                        } catch (SAXException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                }
                if (AssessmentsListElem.ASSESSMENT_STATUS.equals(header.get(ii))) {
                    cell.add(header.indexOf(AssessmentsListElem.ASSESSMENT_STATUS), assessElem.getStatus());
                }
                if (AssessmentsListElem.OVERALL_SCORE.equals(header.get(ii))) {
                    cell.add(header.indexOf(AssessmentsListElem.OVERALL_SCORE), numberFormat.format(assessElem.getOverallScore()));
                }
                if (AssessmentsListElem.OVERALL_GRADE.equals(header.get(ii))) {
                    cell.add(header.indexOf(AssessmentsListElem.OVERALL_GRADE), (assessElem.getOverallScore() != null && assessElem.getBonusSettingsItem() != null) ? AssessmentHelper.getScoreGradeName(assessElem.getOverallScore(), assessElem.getBonusSettingsItem()) : "");
                }
                if (AssessmentsListElem.VALIDITY_PERIOD.equals(header.get(ii))) {
                    cell.add(header.indexOf(AssessmentsListElem.VALIDITY_PERIOD), assessElem.getValidityPeriod());
                }
            }

            tableList.addPdfTableRows(cell.toArray(new String[]{}));
        }


        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_PerformanceAppraisals_" + dateFormat(user.getUserDate()));
    }

    public void setAssessmentService(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }
}
