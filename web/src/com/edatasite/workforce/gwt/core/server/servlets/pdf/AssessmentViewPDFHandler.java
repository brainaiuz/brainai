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
import com.edatasite.workforce.gwt.assessment.server.struct.ServerAssessmentHelper;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeAssessmentManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AssessmentViewPDFHandler extends PostPDFHandler implements IPostPDFHandler {

    private static final String ASSESSMENT_NODE_NAME = "assessment";
    private static final String COMPANY_NODE_NAME = "company";
    private static final String TEAM_NODE_NAME = "team";
    private static final String TEMPLATE_NODE_NAME = "template";
    private static final String EVALUATOR_NODE_NAME = "evaluator";
    private static final String EMPLOYEE_NODE_NAME = "employee";
    private static final String DATE_NODE_NAME = "date";
    private static final String OVERALL_NODE_NAME = "overall";
    private static final String SKILL_RATING_GROUP_NODE_NAME = "skill-rating-group";
    private static final String SKILL_RATING_NODE_NAME = "skill-rating";
    private static final String SKILL_NODE_NAME = "skill";
    private static final String NAME_NODE_NAME = "name";
    private static final String DESCRIPTION_NODE_NAME = "description";
    private static final String RATING_NODE_NAME = "rating";
    //    private static final String REVIEWERS_COMMENT = "reviewers-comment";
    private static final String EMPLOYEES_COMMENT = "employees-comment";
    private static final String MANAGERS_COMMENT = "managers-comment";
    private static final String MANAGER_NODE_NAME = "manager";
    private static final String APPROVE_NODE = "approve";

    private AssessmentCircularResolver assessmentCircularResolver;
    private EmployeeAssessmentManager employeeAssessmentManager;
    private DecimalFormat format = new DecimalFormat("#.##");
    private String sessionIDParam;
    private CompanyManager companyManager;
    @Autowired
    private AssessmentServiceLocal assessmentService;
    @Autowired
    @Qualifier("hrmsLocalizer")
    private WfmMessageSource hrmsLocalizer;
    @Autowired
    private HrmsService hrmsService;

    public void setCompanyManager(CompanyManager companyManager) {
        this.companyManager = companyManager;
    }

    protected String getFileRepository() {
        return "/WEB-INF/xslts/simple_pa_view.xslt";
    }

    public void writePDF(Object object) {
        try {
            SimpleDateFormat previewformat = new SimpleDateFormat("MMMMM dd, yyyy", commonLocalizer.initializeUserLocale());
            EdsUser user = uploadManager.getUser();
            RequestObject requestObject = (RequestObject) object;
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
            startDocument();

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

            if (assessmentName != null) {
                writeElement("assName", "(" + assessmentName + ")");
            }

            EdsCompany company = companyManager.get(Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()));

            double fromScaleRATE = appraisalsSettings.getFromScale();
            double toScaleRATE = appraisalsSettings.getToScale();
            double stepSizeRATE = appraisalsSettings.getStepSize();


            if (commonLocalizer.initializeUserLocale().toString().equals("ru")) {
                writeElement("title_image", getRealPath("hrms/images/simple_pa_ru.png"));
            } else if (commonLocalizer.initializeUserLocale().toString().equals("nl")){
                writeElement("title_image", getRealPath("hrms/images/simple_pa_de.png"));
            }else {
                writeElement("title_image", getRealPath("hrms/images/simple_pa.png"));
            }
            String logoPath = EdsContextParams.getPdfLogo() != null ? getRealPath(EdsContextParams.getPdfLogo()) : getRealPath("/customisation/kpi/images/kpilogo.png");
            String companyLOGO = getCompanyLogoUrl(company);

            if (companyLOGO != null) {
                writeElement("logoPath", companyLOGO);
            } else if (company.getShowWorkforceLogoOnPDF()) {
                writeElement("logoPath", logoPath);
            }
            writeElement("company", company.getName());
            writeElement("simpleAppraisal", hrmsLocalizer.localize(PdfLocalizationName.simpleAppraisals));

            writeElement("companyStreet", company.getAddress1() != null ? company.getAddress1() : "");
            writeElement("companyCity", company.getCity() + " " != null ? company.getCity() : "");
            writeElement("companyPostCode", company.getPostCode() != null ? company.getPostCode() : "");
            writeElement("companyCountry", company.getCountryZone().getCountry().getName() != null ? company.getCountryZone().getCountry().getName() : "");

            if (employeeAssessment != null) {
                if (user == null) {
                    user = employeeAssessment.getEmployee();
                }

                setFileName(employeeAssessment.getEmployee().getFirstName() + "_" + employeeAssessment.getEmployee().getLastName() +
                        "_PerformanceAppraisals_" + (employeeAssessment.getDate() == null ? "" :
                        (user != null ? dateFormat(user.getUserDate(employeeAssessment.getDate()), company) : dateFormat(employeeAssessment.getDate(), company))));

                writeElement("employeNameLocalizer", commonLocalizer.localize(PdfLocalizationName.employee));
                writeElement("managerNameLocalizer", commonLocalizer.localize(PdfLocalizationName.ManagerName));
                writeElement("signatureLocalizer", commonLocalizer.localize(PdfLocalizationName.signature));
                writeElement("dateLocalizer", commonLocalizer.localize(PdfLocalizationName.date));
                writeElement("assignedGoalsLocalizer", commonLocalizer.localize(PdfLocalizationName.assignedGoals));
                writeElement("overallCompetenciesandGoalsRate", commonLocalizer.localize(PdfLocalizationName.overallCompetenciesandGoalsRate));
                writeElement("scoreLocalizer", commonLocalizer.localize(PdfLocalizationName.score));
                writeElement("unacceptableLocalizer", commonLocalizer.localize(PdfLocalizationName.unacceptable));
                writeElement("competencyRatioLocalizer", commonLocalizer.localize(PdfLocalizationName.competencyRatio));
                writeElement("goalRatioLocalizer", commonLocalizer.localize(PdfLocalizationName.goalRatio));
                writeElement("employeeCompetenciesLocalizer", commonLocalizer.localize(PdfLocalizationName.employeeCompetencies));
                writeElement("overallRateLocalizer", commonLocalizer.localize(PdfLocalizationName.overallRate));
                writeElement("overallRateComparisonLocalizer", commonLocalizer.localize(PdfLocalizationName.overallRateComparison));
                writeElement("thisReviewOverallRateLocalizer", commonLocalizer.localize(PdfLocalizationName.thisReviewOverallRate));
                writeElement("lastReviewOverallRateLocalizer", commonLocalizer.localize(PdfLocalizationName.lastReviewOverallRate));
                writeElement("comparisonThisReviewLocalizer", commonLocalizer.localize(PdfLocalizationName.comparisonThisReview));
                writeElement("differenceLocalizer", commonLocalizer.localize(PdfLocalizationName.difference));
                writeElement("managerLocalizer", commonLocalizer.localize(PdfLocalizationName.manager));
                writeElement("commentsLocalizer", commonLocalizer.localize(PdfLocalizationName.comments));

                //
                writeRateAsString(appraisalsSettings, stepSizeRATE);
                //

                writeElement("empName", employeeAssessment.getEmployee().getName());
                writeElement("managerName", employeeAssessment.getAssessment().getReviewer().getName());
                if (employeeAssessment.getAssessment() != null && employeeAssessment.getAssessment().getInititateDate() != null) {
                    writeElement("date", (user != null ? previewformat.format(user.getUserDate(employeeAssessment.getAssessment().getInititateDate())) :
                            previewformat.format(employeeAssessment.getAssessment().getInititateDate())));
                }

                String fromToStepSizeRATE = "fromRateScale=" + fromScaleRATE + "&" + "toRateScale=" + toScaleRATE + "&" + "stepSizeRate=" + stepSizeRATE;
                // Total overals
                if ((struct.getCalculatedAverage() != 0d) && (goalStruct.getCalculatedAverage() != 0d)) {
                    boolean isWeightable = struct.isWeightable();
                    double rating;
                    double skillOverall = struct.getCalculatedAverage();
                    double goalOverall = goalStruct.getCalculatedAverage();
                    if (goalOverall > 0 && !isWeightable) {
                        rating = (skillOverall + goalOverall) / 2;
                    } else if (goalOverall > 0 && isWeightable) {
                        rating = skillOverall + goalOverall;
                    } else {
                        rating = skillOverall;
                    }
//                    float rating = (struct.getCalculatedAverage() + goalStruct.getCalculatedAverage()) / 2;
                    writeElement("totalAvail", "yes");
                    writeElement("totalOverallChartUrl", EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + rating + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    writeElement("totalOverallRateValue", String.valueOf(format.format(rating)));
                }

                double thisOverallRate;
                // Employee Skills //
                // This and last review comparison
                if ((struct.getCalculatedAverage() != 0d) && (lastReviewOverall != 0d)) {
                    writeElement("comparisonRate", "yes");
                    // This review overall rate
                    thisOverallRate = struct.getCalculatedAverage();
                    writeElement("thisOverallChartUrl", EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + thisOverallRate + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    writeElement("thisOverallRateValue", String.valueOf(format.format(thisOverallRate)));

                    //Last review overall rate
                    writeElement("lastOverallChartUrl", EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + lastReviewOverall + "&chartType=last" + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    writeElement("lastOverallRateValue", String.valueOf(format.format(lastReviewOverall)));

                    // Overall rates comparison
                    writeElement("comparisonChartUrl", EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + thisOverallRate + "&lastRate=" + lastReviewOverall + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    writeElement("comparisonRateValue", String.valueOf(format.format(thisOverallRate - lastReviewOverall)));

                } else if (struct.getCalculatedAverage() != 0d) {
                    writeElement("avail", "yes");
                    thisOverallRate = struct.getCalculatedAverage();
                    writeElement("overallChartUrl", EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + thisOverallRate + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    writeElement("overallRateValue", String.valueOf(format.format(thisOverallRate)));
                }

                // Goals //
                // This and last review comparison
                if ((goalStruct.getCalculatedAverage() != 0d) && (goalLastReviewOverall != 0d)) {
                    writeElement("goalComparisonRate", "yes");
                    // This review overall rate
                    thisOverallRate = goalStruct.getCalculatedAverage();
                    writeElement("goalThisOverallChartUrl", EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + thisOverallRate + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    writeElement("goalThisOverallRateValue", String.valueOf(format.format(thisOverallRate)));

                    //Last review overall rate
                    writeElement("goalLastOverallChartUrl", EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + goalLastReviewOverall + "&chartType=last" + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    writeElement("goalLastOverallRateValue", String.valueOf(format.format(goalLastReviewOverall)));

                    // Overall rates comparison
                    writeElement("goalComparisonChartUrl", EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + thisOverallRate + "&lastRate=" + goalLastReviewOverall + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    writeElement("goalComparisonRateValue", String.valueOf(format.format(thisOverallRate - goalLastReviewOverall)));

                } else if (goalStruct.getCalculatedAverage() != 0d) {
                    writeElement("goalAvail", "yes");
                    thisOverallRate = goalStruct.getCalculatedAverage();
                    writeElement("goalOverallChartUrl", EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + thisOverallRate + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    writeElement("goalOverallRateValue", String.valueOf(format.format(thisOverallRate)));
                }

                // Skills //
                showCharts(groups, "skillChart", employeeAssessment, appraisalsSettings, fromToStepSizeRATE, stepSizeRATE);

                // Goals //
                if (goalGroups != null) {
                    showCharts(goalGroups, "goalChart", employeeAssessment, appraisalsSettings, fromToStepSizeRATE, stepSizeRATE);
                }

            }

            endDocument();
        } catch (SAXException | IOException ex) {
            ex.printStackTrace();
        }
    }

    private void writeRateAsString(AppraisalsSettingsItem appraisalsSettings, double stepSizeRATE) throws SAXException {

        StringBuilder localizeStringBuilder = new StringBuilder();
        Map<String, ArrayList<Double>> rateStringSetMap = new LinkedHashMap<>();

        /*DecimalFormat numberFormat = new DecimalFormat(",#0.00");*/
        ArrayList<Double> rateScaleValues = new ArrayList<>();
        HashMap<Double, String> ratingAsStrings = hrmsService.getAssassmentRatings();
        double lDoubleValue = appraisalsSettings.getFromScale();
        for (double i = appraisalsSettings.getFromScale(); i <= appraisalsSettings.getToScale(); i += stepSizeRATE) {
            Double aDouble = new BigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            rateScaleValues.add(aDouble);
            lDoubleValue = aDouble;
            //
            rateStringSetMap.computeIfAbsent(ServerAssessmentHelper.getRatingAsString(ratingAsStrings, i, appraisalsSettings), k -> new ArrayList<>());
            if (rateStringSetMap.get(ServerAssessmentHelper.getRatingAsString(ratingAsStrings, i, appraisalsSettings)) != null) {
                rateStringSetMap.get(ServerAssessmentHelper.getRatingAsString(ratingAsStrings, i, appraisalsSettings)).add(aDouble);
            }
        }
        if (lDoubleValue < appraisalsSettings.getToScale()) {
            Double aDouble = new BigDecimal(appraisalsSettings.getToScale()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            rateScaleValues.add(aDouble);
            rateStringSetMap.computeIfAbsent(ServerAssessmentHelper.getRatingAsString(ratingAsStrings, appraisalsSettings.getToScale(), appraisalsSettings), k -> new ArrayList<>());
            if (rateStringSetMap.get(ServerAssessmentHelper.getRatingAsString(ratingAsStrings, appraisalsSettings.getToScale(), appraisalsSettings)) != null) {
                rateStringSetMap.get(ServerAssessmentHelper.getRatingAsString(ratingAsStrings, appraisalsSettings.getToScale(), appraisalsSettings)).add(aDouble);
            }
        }

        for (String rateAsString : rateStringSetMap.keySet()) {
            ArrayList<Double> doubles = rateStringSetMap.get(rateAsString);
            String doublesName;
            if (doubles.size() > 1) {
                doublesName = String.valueOf(doubles.get(0)) + "-" + String.valueOf(doubles.get(doubles.size() - 1));
            } else {
                doublesName = String.valueOf(doubles.get(0));
            }

            localizeStringBuilder.append(" ").append(doublesName).append("-");
            localizeStringBuilder.append(rateAsString).append(";");
        }

        writeElement("unacceptableLocalizerSTRING", localizeStringBuilder.toString());
        getRateScaleTABLE(rateScaleValues);
    }


    private void getRateScaleTABLE(ArrayList<Double> tableColumnValues) throws SAXException {
        double tableColumnWidth = (100d / ((tableColumnValues.size() - 1.15d) > 1 ? (tableColumnValues.size() - 1.15d) : 1)) - 1d;


        for (int tableColumn = 1; tableColumn <= tableColumnValues.size(); tableColumn++) {
            startElement("rateScaleTABLECOLUMNNUMBER");
            writeElement("columnNUMBER", String.valueOf(tableColumn));
            if (tableColumn == tableColumnValues.size()) {
                writeElement("columnWIDTH", "1%");
            } else {
                writeElement("columnWIDTH", String.valueOf(tableColumnWidth) + "%");
            }
            endElement("rateScaleTABLECOLUMNNUMBER");
        }

        for (Double tableColumnValue : tableColumnValues) {
            startElement("rateScaleTABLECOLUMN");
            writeElement("tableColumnVALUE", String.valueOf(tableColumnValue));
            endElement("rateScaleTABLECOLUMN");
        }
    }


    private void showCharts(SkillAssessmentElem[] groups, String chartType, EdsEmployeeAssessment employeeAssessment, AppraisalsSettingsItem appraisalsSettings, String fromToStepSizeRATE, double stepSizeRATE) throws SAXException {
        int i = 0;
        for (SkillAssessmentElem skillAssessmentElem : groups) {

            startElement(chartType);
            writeElement("skillName", ++i + ". " + skillAssessmentElem.getSkillName());
            writeElement("weightAmount", skillAssessmentElem.getWeight() != null ? ("Weight:" + format.format(skillAssessmentElem.getWeight().doubleValue())) : " ");
            writeElement("skillDescription", skillAssessmentElem.getSkillDescription());
            if ((skillAssessmentElem.getRaiting() != null && skillAssessmentElem.getRaiting() != 0)) {
                writeElement("hasChart", "yes");
                writeElement("chartUrl", EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + skillAssessmentElem.getRaiting() + "&chartType=skill" + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                writeElement("score", String.valueOf(format.format(skillAssessmentElem.getRaiting())) + ".00");
            }

            if (isCommentInclude(skillAssessmentElem.getEmployeesComment()) || isCommentInclude(skillAssessmentElem.getReviewersComment())) {
                writeElement("hasComments", "yes");

                if (isCommentInclude(skillAssessmentElem.getEmployeesComment())) {
                    startElement("comments");
                    writeElement("role", "Self - " + employeeAssessment.getEmployee().getName());
                    putComment(skillAssessmentElem.getEmployeesComment());
                    endElement("comments");
                }
                if (isCommentInclude(skillAssessmentElem.getReviewersComment())) {
                    String rate = "N/A";
                    if (skillAssessmentElem.getRaiting() != null && skillAssessmentElem.getRaiting() != 0d) {
                        rate = "Rate - " + String.valueOf(format.format(skillAssessmentElem.getRaiting())) + ".00";
                    }

                    startElement("comments");
                    writeElement("rate", rate);
                    writeElement("role", "Manager - " + employeeAssessment.getAssessment().getReviewer().getName());
                    putComment(skillAssessmentElem.getReviewersComment());
                    endElement("comments");
                }
            }
            //
            writeRateAsString(appraisalsSettings, stepSizeRATE);
            //
            endElement(chartType);
        }

    }

    private void putComment(String commentText) throws SAXException {
        String[] comments = commentText.split("\n");
        if (comments.length > 0) {
            for (int i = 0; i < comments.length; i++) {
                if (i == comments.length - 1) {
                    comments[i] += "\"";
                }
                startElement("commentFormed");
                writeElement("comment", comments[i]);
                endElement("commentFormed");
            }
        }
    }

    private boolean isCommentInclude(String comment) {
        return comment != null && !comment.equals("") && !comment.equals(" ");
    }

    protected Object getDataClass() {
        return new RequestObject();
    }

    public void setEmployeeAssessmentManager(EmployeeAssessmentManager employeeAssessmentManager) {
        this.employeeAssessmentManager = employeeAssessmentManager;
    }

    public void setAssessmentCircularResolver(AssessmentCircularResolver assessmentCircularResolver) {
        this.assessmentCircularResolver = assessmentCircularResolver;
    }


}
