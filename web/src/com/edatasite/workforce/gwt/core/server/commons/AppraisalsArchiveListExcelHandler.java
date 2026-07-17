package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentsListElem;
import com.edatasite.workforce.gwt.assessment.client.ui.AssessmentHelper;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 22.07.2009
 * Time: 21:00:48
 * To change this template use File | Settings | File Templates.
 */
public class AppraisalsArchiveListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(AppraisalsArchiveListExcelHandler.class);
    @Autowired
    private PropertManager propertManager;
    private String sheetName;

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private UserManager userManager;

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected void setFileName() {
        filename = "Appraisals Archive";
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }

    protected HSSFWorkbook getWorkBook(Object object) {

        DecimalFormat numberFormat = new DecimalFormat(",##0.00");

        String shortDateFormat = "MM/dd/yyyy";
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        EdsUser user = userManager.getUser();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        filename = "Appraisals Archive";
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.appraisalsArchive);
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);

        ListResult<AssessmentsListElem> assessmentList = assessmentService.getAssessmentsList(filterParametrs,null);
        List<AssessmentsListElem> assessmentsListElems = assessmentList.getList();

        ExcelData[] cellDatas;

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(AssessmentsListElem.ACTION);
        header.remove(AssessmentsListElem.PDF_VERSION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(AssessmentsListElem.ASSESSMENT_NAME, commonLocalizer.localize(PdfLocalizationName.employee));
        mapColumnHeader.put(AssessmentsListElem.TEMPLATE_NAME, commonLocalizer.localize(PdfLocalizationName.template));
        mapColumnHeader.put(AssessmentsListElem.INITIATION_DATE, commonLocalizer.localize(PdfLocalizationName.initiatedDate));
        mapColumnHeader.put(AssessmentsListElem.INITIATOR_NAME, commonLocalizer.localize(PdfLocalizationName.initiatedBy));
        mapColumnHeader.put(AssessmentsListElem.REVIEWER_NAME, commonLocalizer.localize(PdfLocalizationName.reviewers));
        mapColumnHeader.put(AssessmentsListElem.ASSESSMENT_TYPE, commonLocalizer.localize(PdfLocalizationName.type));
        mapColumnHeader.put(AssessmentsListElem.ASSESSMENT_STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        mapColumnHeader.put(AssessmentsListElem.OVERALL_SCORE, commonLocalizer.localize(PdfLocalizationName.overallScore));
        mapColumnHeader.put(AssessmentsListElem.OVERALL_GRADE, commonLocalizer.localize(PdfLocalizationName.overallGrade));
        mapColumnHeader.put(AssessmentsListElem.VALIDITY_PERIOD, commonLocalizer.localize(PdfLocalizationName.validityPeriod));

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            List<ExcelData[]> list = new LinkedList<>();

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, " " + excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            cellDatas = new ExcelData[header.size()];
            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)),
                        ExcelData.STRING, header.get(i).equals(AssessmentsListElem.ASSESSMENT_NAME) ? 80 :
                        header.get(i).equals(AssessmentsListElem.ASSESSMENT_TYPE) ? 25 : 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            list.add(cellDatas);


            for (AssessmentsListElem item : assessmentsListElems) {
                List<String> cell = new ArrayList<>();
                for (int ii = 0; ii < header.size(); ii++) {
                    if (AssessmentsListElem.ASSESSMENT_NAME.equals(header.get(ii))) {
                        cell.add(header.indexOf(AssessmentsListElem.ASSESSMENT_NAME), item.getEmployeeName());
                    }
                    if (AssessmentsListElem.TEMPLATE_NAME.equals(header.get(ii))) {
                        cell.add(header.indexOf(AssessmentsListElem.TEMPLATE_NAME), item.getTemplateName());
                    }
                    if (AssessmentsListElem.INITIATION_DATE.equals(header.get(ii))) {
                        cell.add(header.indexOf(AssessmentsListElem.INITIATION_DATE), ServerUtils.dateFormat(item.getInitiationDate(), shortDateFormat));
                    }
                    if (AssessmentsListElem.INITIATOR_NAME.equals(header.get(ii))) {
                        cell.add(header.indexOf(AssessmentsListElem.INITIATOR_NAME), item.getInitiatorName());
                    }
                    if (AssessmentsListElem.REVIEWER_NAME.equals(header.get(ii))) {
                        cell.add(header.indexOf(AssessmentsListElem.REVIEWER_NAME), item.getReviewerName());
                    }
                    if (AssessmentsListElem.ASSESSMENT_TYPE.equals(header.get(ii))) {
                        cell.add(header.indexOf(AssessmentsListElem.ASSESSMENT_TYPE), item.getAssessmentType());
//								Constants.ASSESSMENT_360.equals(item.getAssessmentType()) ? htmlParser.performParse("360&nbsp;&deg;") : "Simple");
                    }
                    if (AssessmentsListElem.ASSESSMENT_STATUS.equals(header.get(ii))) {
                        cell.add(header.indexOf(AssessmentsListElem.ASSESSMENT_STATUS), item.getStatus());
                    }
                    if (AssessmentsListElem.OVERALL_SCORE.equals(header.get(ii))) {
                        cell.add(header.indexOf(AssessmentsListElem.OVERALL_SCORE), numberFormat.format(item.getOverallScore()));
                    }
                    if (AssessmentsListElem.OVERALL_GRADE.equals(header.get(ii))) {
                        cell.add(header.indexOf(AssessmentsListElem.OVERALL_GRADE), (item.getOverallScore() != null && item.getBonusSettingsItem() != null) ? AssessmentHelper.getScoreGradeName(item.getOverallScore(), item.getBonusSettingsItem()) : "");
                    }
                    if (AssessmentsListElem.VALIDITY_PERIOD.equals(header.get(ii))) {
                        cell.add(header.indexOf(AssessmentsListElem.VALIDITY_PERIOD), item.getValidityPeriod());
                    }
                }
                cellDatas = new ExcelData[cell.size()];
                for (int k = 0; k < cell.size(); k++) {
                    cellDatas[k] = new ExcelData(cell.get(k), ExcelData.STRING, header.get(k).equals(AssessmentsListElem.ASSESSMENT_NAME) ? 50 :
                            header.get(k).equals(AssessmentsListElem.ASSESSMENT_TYPE) ? 25 : 20,
                            header.get(k).equals(AssessmentsListElem.ASSESSMENT_TYPE), !header.get(k).equals(AssessmentsListElem.ASSESSMENT_TYPE),
                            ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);

            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 7);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate appraisals archive list excel report, exception: " + e);
        }
        return null;
    }
}