package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;
import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElemsStruct;
import com.edatasite.workforce.gwt.assessment.server.app.AssessmentCircularResolver;
import com.edatasite.workforce.gwt.assessment.server.app.AssessmentServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeAssessmentManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AssessmentViewPDFHandler2 extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private AssessmentCircularResolver assessmentCircularResolver;
    @Autowired
    private EmployeeAssessmentManager employeeAssessmentManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private AssessmentServiceLocal assessmentService;

    @Autowired
    private HrmsService hrmsService;

    private final DecimalFormat format = new DecimalFormat("#.##");
    private String sessionIDParam;

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        final RequestObject requestObject = new RequestObject();
        final String objectID = request.getParameter("objectID");

        if (objectID != null) {
            requestObject.setObjectID(Integer.valueOf(objectID));
        }
        return requestObject;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        final RequestObject requestObject = (RequestObject) dataClass;
        final ITextGenericPdfData pdfData = new ITextGenericPdfData();

        SimpleDateFormat previewformat = new SimpleDateFormat("MMMMM dd, yyyy", commonLocalizer.initializeUserLocale());
        EdsUser user = uploadManager.getUser();
        Integer assessElemId = requestObject.getObjectID();
        Integer currentUserID = requestObject.getUserID();
        Integer loggedUserID = user != null ? user.getObjectID() : currentUserID;
        sessionIDParam = SESSION_ID_COOKIE + "=" + ServerSecurityContext.getInstance().getSessionId();

        AppraisalsSettingsItem appraisalsSettings = assessmentService.getAppraisalsSettings(loggedUserID);
        boolean hasReviewerSupervisor = appraisalsSettings.getReviewers().contains(EdsRole.SUPERVISOR_CODE);

        EdsEmployeeAssessment employeeAssessment = employeeAssessmentManager.get(assessElemId);
        List<EdsEmployeeAssessment> employeeAssessmentList = employeeAssessmentManager.getEmployeeSimpleAssessments(employeeAssessment.getEmployee(), true);
        SkillAssessmentElemsStruct struct = assessmentCircularResolver.getSkillAssessmentElemGroups(assessElemId, loggedUserID, hasReviewerSupervisor);
        SkillAssessmentElemsStruct goalStruct = assessmentCircularResolver.getGoalAssessmentElemGroups(assessElemId, loggedUserID, hasReviewerSupervisor);
        SkillAssessmentElem[] groups = struct.getElems();
        SkillAssessmentElem[] goalGroups = goalStruct.getElems();

        //Getting last review overall rate
        EdsEmployeeAssessment lastEmployeeAssessment = null;
        double lastReviewOverall = 0d;
        double goalLastReviewOverall = 0d;

        if (employeeAssessmentList.size() > 1) {
            for (int i = 0; i < employeeAssessmentList.size(); i++) {
                EdsEmployeeAssessment assessment = employeeAssessmentList.get(i);
                if (assessment.equals(employeeAssessment) && (i + 1 < employeeAssessmentList.size())) {
                    lastEmployeeAssessment = employeeAssessmentList.get(i + 1);
                    break;
                }
            }
            if (lastEmployeeAssessment != null) {
                SkillAssessmentElemsStruct skillAssessmentElemGroupsT = assessmentCircularResolver.getSkillAssessmentElemGroups(lastEmployeeAssessment.getObjectID(), loggedUserID, hasReviewerSupervisor);
                lastReviewOverall = skillAssessmentElemGroupsT != null ? skillAssessmentElemGroupsT.getCalculatedAverage() : 0d;

                SkillAssessmentElemsStruct goalAssessmentElemGroupsT = assessmentCircularResolver.getGoalAssessmentElemGroups(lastEmployeeAssessment.getObjectID(), loggedUserID, hasReviewerSupervisor);
                goalLastReviewOverall = goalAssessmentElemGroupsT != null ? goalAssessmentElemGroupsT.getCalculatedAverage() : 0d;
            }
        }

        String assessmentStatus = struct.getStatus();
        String assessmentName = null;
        if (INITIATED.equals(assessmentStatus)) {
            assessmentName = commonLocalizer.localize(PdfLocalizationName.managerInitiated);
        } else if (RATED.equals(assessmentStatus)) {
            assessmentName = commonLocalizer.localize(PdfLocalizationName.managerRated);
        } else if (REVIEWED_BY_MANAGER.equals(assessmentStatus)) {
            assessmentName = commonLocalizer.localize(PdfLocalizationName.employeeReviewed);
        } else if (APPROVED_BY_MANAGER.equals(assessmentStatus) || APPROVED.equals(assessmentStatus)) {
            assessmentName = commonLocalizer.localize(PdfLocalizationName.approvedbyManager);
        }

        double fromScaleRATE = appraisalsSettings.getFromScale();
        double toScaleRATE = appraisalsSettings.getToScale();
        double stepSizeRATE = appraisalsSettings.getStepSize();

        final CustomisedITextTable singleAppraisalTable = new CustomisedITextTable();
        singleAppraisalTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);

        singleAppraisalTable.addRowWithCode("ASSESSMENT_NAME", "Assessment Name", assessmentName);
//        if (commonLocalizer.initializeUserLocale().toString().equals("ru")) {
//            singleAppraisalTable.addRowWithCode("TITLE_IMAGE", "Image", getRealPath("hrms/images/simple_pa_ru.png"));
//        } else if (commonLocalizer.initializeUserLocale().toString().equals("nl")) {
//            singleAppraisalTable.addRowWithCode("TITLE_IMAGE", "", getRealPath("hrms/images/simple_pa_de.png"));
//        } else {
//            singleAppraisalTable.addRowWithCode("TITLE_IMAGE", "", getRealPath("hrms/images/simple_pa.png"));
//        }

        String empName = employeeAssessment != null && employeeAssessment.getEmployee() != null ? escapeHtml(employeeAssessment.getEmployee().getName()) : "";
        String managerName = employeeAssessment != null && employeeAssessment.getEmployee() != null &&
                             employeeAssessment.getAssessment().getReviewer() != null ? escapeHtml(employeeAssessment.getAssessment().getReviewer().getName()) : "";
        String date = "";
        if (employeeAssessment.getAssessment() != null && employeeAssessment.getAssessment().getInititateDate() != null) {
            date = (user != null ? previewformat.format(user.getUserDate(employeeAssessment.getAssessment().getInititateDate())) :
                    previewformat.format(employeeAssessment.getAssessment().getInititateDate()));
        }

        singleAppraisalTable.addRowWithCode("COMPANY", "", company.getName());
        singleAppraisalTable.addRowWithCode("SIMPLE_APPRAISAL", "", hrmsLocalizer.localize(PdfLocalizationName.simpleAppraisals));
        singleAppraisalTable.addRowWithCode("EMPLOYE_NAME", commonLocalizer.localize(PdfLocalizationName.employee), empName);
        singleAppraisalTable.addRowWithCode("MANAGER_NAME", commonLocalizer.localize(PdfLocalizationName.ManagerName), managerName);
        singleAppraisalTable.addRowWithCode("SIGNATURE", "", commonLocalizer.localize(PdfLocalizationName.signature));
        singleAppraisalTable.addRowWithCode("DATE", commonLocalizer.localize(PdfLocalizationName.date), date);
        singleAppraisalTable.addRowWithCode("ASSIGNED_GOALS", "", commonLocalizer.localize(PdfLocalizationName.assignedGoals));
        singleAppraisalTable.addRowWithCode("OVERALL_COMPETENCIES_AND_GOALS_RATE", "", commonLocalizer.localize(PdfLocalizationName.overallCompetenciesandGoalsRate));
        singleAppraisalTable.addRowWithCode("SCORE", "", commonLocalizer.localize(PdfLocalizationName.score));
        singleAppraisalTable.addRowWithCode("UNACCEPTABLE", "", commonLocalizer.localize(PdfLocalizationName.unacceptable));
        singleAppraisalTable.addRowWithCode("COMPETENCY_RATIO", "", commonLocalizer.localize(PdfLocalizationName.competencyRatio));
        singleAppraisalTable.addRowWithCode("GOAL_RATIO", "", commonLocalizer.localize(PdfLocalizationName.goalRatio));
        singleAppraisalTable.addRowWithCode("EMPLOYEE_COMPETENCIES", "", commonLocalizer.localize(PdfLocalizationName.employeeCompetencies));
        singleAppraisalTable.addRowWithCode("OVERALL_RATE", "", commonLocalizer.localize(PdfLocalizationName.overallRate));
        singleAppraisalTable.addRowWithCode("OVERALL_RATE_COMPARISON", "", commonLocalizer.localize(PdfLocalizationName.overallRateComparison));
        singleAppraisalTable.addRowWithCode("THIS_REVIEW_OVERALL_RATE", "", commonLocalizer.localize(PdfLocalizationName.thisReviewOverallRate));
        singleAppraisalTable.addRowWithCode("LAST_REVIEW_OVERALL_RATE", "", commonLocalizer.localize(PdfLocalizationName.lastReviewOverallRate));
        singleAppraisalTable.addRowWithCode("COMPARISON_THIS_REVIEW", "", commonLocalizer.localize(PdfLocalizationName.comparisonThisReview));
        singleAppraisalTable.addRowWithCode("DIFFERENCE", "", commonLocalizer.localize(PdfLocalizationName.difference));
        singleAppraisalTable.addRowWithCode("MANAGER", "", commonLocalizer.localize(PdfLocalizationName.manager));
        singleAppraisalTable.addRowWithCode("COMMENTS", "", commonLocalizer.localize(PdfLocalizationName.comments));

        /*if (attachmentsLists != null && attachmentsLists.length > 0) {
            CustomisedITextTable projectAttachments = new CustomisedITextTable();
            projectAttachments.setName(commonLocalizer.localize(PdfLocalizationName.attachments));

            projectAttachments.addColumn(NAME, commonLocalizer.localize(PdfLocalizationName.name));
            projectAttachments.addColumn(ITEM_DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
            projectAttachments.addColumn("SIZE", commonLocalizer.localize(PdfLocalizationName.sizeField));
            projectAttachments.addColumn(DATE, commonLocalizer.localize(PdfLocalizationName.date));
            projectAttachments.addColumn("FILE_DOWNLOAD_URL", commonLocalizer.localize(PdfLocalizationName.urlname));

            for (FileResource attachment : attachmentsLists) {
                String attachments = getResultOrLongDash(attachment.getEncodedName());
                String description = getResultOrLongDash(attachment.getDescription());
                String date = longDateFormat(attachment.getModificationDate()) != null ? longDateFormat(attachment.getModificationDate()) : "—";
                String fileSize = attachment.getContentLength() != null ? getFileSizeAsString(attachment.getContentLength()) : "—";
                String downloadUrl = getDownloadURL(attachment);
                projectAttachments.addRow(escapeHtml(attachments), escapeHtml(description), fileSize, date, downloadUrl);
            }
            customData.put(ATTACHMENTS, projectAttachments);
        }*/

        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        String fromToStepSizeRATE = "fromRateScale=" + fromScaleRATE + "&" + "toRateScale=" + toScaleRATE + "&" + "stepSizeRate=" + stepSizeRATE;

        List<CustomisedITextTable> customisedITextTables = new ArrayList<>();

        if (groups != null) {
            CustomisedITextTable skillTable = skillData(groups, employeeAssessment);
            customisedITextTables.add(skillTable);
        }
        if (goalGroups != null) {
            CustomisedITextTable goalTable = skillData(goalGroups, employeeAssessment);
            customisedITextTables.add(goalTable);
        }

        baseInvoice.setCustomProductTableList(customisedITextTables);
        baseInvoice.setCustomNumberAndDatesTable(singleAppraisalTable);

        pdfData.setBaseInvoice(baseInvoice);
        return pdfData;
    }

    public CustomisedITextTable skillData(SkillAssessmentElem[] groups, EdsEmployeeAssessment employeeAssessment) {
        CustomisedITextTable skillDataTable = new CustomisedITextTable();

        skillDataTable.addColumn("SKILL_NAME", commonLocalizer.localize(PdfLocalizationName.name));
        skillDataTable.addColumn("SKILL_DESCRIPTION", commonLocalizer.localize(PdfLocalizationName.description));
        skillDataTable.addColumn("WEIGHT_AMOUNT", commonLocalizer.localize(PdfLocalizationName.sizeField));
//        skillDataTable.addColumn("HAS_CHART", commonLocalizer.localize(PdfLocalizationName.date));
//        skillDataTable.addColumn("CHART_URL", commonLocalizer.localize(PdfLocalizationName.urlname));
//        skillDataTable.addColumn("SCORE", commonLocalizer.localize(PdfLocalizationName.urlname));
        skillDataTable.addColumn("HAS_COMMENT", commonLocalizer.localize(PdfLocalizationName.urlname));
        skillDataTable.addColumn("ROLE", commonLocalizer.localize(PdfLocalizationName.urlname));
        skillDataTable.addColumn("COMMENTS", commonLocalizer.localize(PdfLocalizationName.urlname));
        int i = 0;
        for (SkillAssessmentElem skillAssessmentElem : groups) {
            String skillName = ++i + ". " + skillAssessmentElem.getSkillName();
            String skillDescription= skillAssessmentElem.getSkillDescription();
            String weightAmount = skillAssessmentElem.getWeight() != null ? ("Weight:" + format.format(skillAssessmentElem.getWeight().doubleValue())) : " ";
//            if ((skillAssessmentElem.getRaiting() != null && skillAssessmentElem.getRaiting() != 0)) {
//                String hasChart= "yes";
//                String chartUrl= EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + skillAssessmentElem.getRaiting() + "&chartType=skill" + "&" + fromToStepSizeRATE + "&" + sessionIDParam;
//                String score= String.valueOf(format.format(skillAssessmentElem.getRaiting())) + ".00";
//            }

            String hasComments = "";
            String role = "";
            String comments = "";
            if (isCommentInclude(skillAssessmentElem.getEmployeesComment())) {
                hasComments= "yes";
                role = "Self - " + employeeAssessment.getEmployee().getName();
                comments = skillAssessmentElem.getEmployeesComment();
            }
            skillDataTable.addRow(escapeHtml(skillName), escapeHtml(skillDescription), weightAmount, hasComments, role, comments);
        }

        return skillDataTable;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("");
//        RequestObject requestObject = (RequestObject) dataClass;
//        Integer assessElemId = requestObject.getObjectID();
//        EdsEmployeeAssessment employeeAssessment = employeeAssessmentManager.get(assessElemId);
//        if (employeeAssessment != null) {
//            if (user == null) {
//                user = employeeAssessment.getEmployee();
//            }
//
//            setFileName(employeeAssessment.getEmployee().getFirstName() + "_" + employeeAssessment.getEmployee().getLastName() +
//                    "_PerformanceAppraisals_" + (employeeAssessment.getDate() == null ? "" :
//                    (user != null ? dateFormat(user.getUserDate(employeeAssessment.getDate())) : dateFormat(employeeAssessment.getDate()))));
//        }

    }

    private boolean isCommentInclude(String comment) {
        return comment != null && !comment.equals("") && !comment.equals(" ");
    }

    @Override
    protected String getTableName(Object dataClass) {
        return hrmsLocalizer.localize(PdfLocalizationName.simpleAppraisals);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.APPRAISALS_ARCHIVE;
    }
}
