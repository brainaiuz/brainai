package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;
import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElemsStruct;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillCommentItem;
import com.edatasite.workforce.gwt.assessment.server.app.AssessmentCircularResolver;
import com.edatasite.workforce.gwt.assessment.server.app.AssessmentServiceLocal;
import com.edatasite.workforce.gwt.assessment.server.struct.ServerAssessmentHelper;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeAssessmentManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextFontTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextCustomView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.PerformanceNoteItem;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: dilsh0d
 * Date: 08/01/13
 * Time: 15:57
 */
public class NewAssessmentViewPDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, PDFConstants {

    private final DecimalFormat format = new DecimalFormat("#.##");

    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;

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

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        ITextGenericPdfData iTextGenericPdfData = new ITextGenericPdfData();
        iTextGenericPdfData.setPdfViewType(ITextPdfViewTypeEnum.CUSTOMVIEW);

        PdfPTable assessmentTable = new PdfPTable(1);
        assessmentTable.setWidthPercentage(100);// width set in percent
        assessmentTable.getDefaultCell().setBorderWidth(0f);
        assessmentTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        assessmentTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        assessmentTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        ITextCustomView iTextCustomView = new ITextCustomView();
        iTextCustomView.setCustomTable(assessmentTable);


        SimpleDateFormat previewformat = new SimpleDateFormat("MMMMM dd, yyyy", commonLocalizer.initializeUserLocale());
        EdsUser user = uploadManager.getUser();
        RequestObject requestObject = (RequestObject) dataClass;
        Integer assessElemId = requestObject.getObjectID();
        Integer currentUserID = requestObject.getUserID();
        Integer loggedUserID = user != null ? user.getObjectID() : currentUserID;
        String sessionIDParam = SESSION_ID_COOKIE + "=" + ServerSecurityContext.getInstance().getSessionId();

        AppraisalsSettingsItem appraisalsSettings = assessmentService.getAppraisalsSettings(loggedUserID);
        boolean hasReviewerSupervisor = appraisalsSettings.getReviewers().contains(EdsRole.SUPERVISOR_CODE);

        EdsEmployeeAssessment employeeAssessment = employeeAssessmentManager.get(assessElemId);
        List<EdsEmployeeAssessment> employeeAssessmentList = employeeAssessmentManager.getEmployeeSimpleAssessments(employeeAssessment.getEmployee(), true);
        SkillAssessmentElemsStruct struct = assessmentCircularResolver.getSkillAssessmentElemGroups(assessElemId, loggedUserID, hasReviewerSupervisor);
        SkillAssessmentElemsStruct goalStruct = assessmentCircularResolver.getGoalAssessmentElemGroups(assessElemId, loggedUserID, hasReviewerSupervisor);
        SkillAssessmentElem[] groups = struct.getElems();
        SkillAssessmentElem[] goalGroups = goalStruct.getElems();

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
        /* Gettting started generated Performance Appriasal */

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

        EdsCompany company = companyManager.get(Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()));

        double fromScaleRATE = appraisalsSettings.getFromScale();
        double toScaleRATE = appraisalsSettings.getToScale();
        double stepSizeRATE = appraisalsSettings.getStepSize();


        // Perfomance Appraisal banner image
        Image imageAppraisalBanner = null;
        try {
            if (commonLocalizer.initializeUserLocale().toString().equals("nl")) {
                imageAppraisalBanner = Image.getInstance(getRealPath("hrms/images/simple_pa_de.png"));
            } else {
                imageAppraisalBanner = Image.getInstance(getRealPath("hrms/images/simple_pa.png"));
            }
        } catch (BadElementException e) {
            e.printStackTrace();
        }
        Font perfomanceAppriasalFont = FontFactory.getFont(ITextFontTypeEnum.ARIALUNI.getName(), BaseFont.IDENTITY_H, 16, Font.BOLD);
        perfomanceAppriasalFont.setColor(Color.WHITE);

        Font assessmentNameFont = FontFactory.getFont(ITextFontTypeEnum.ARIALUNI.getName(), BaseFont.IDENTITY_H, 20, Font.NORMAL);
        assessmentNameFont.setColor(12, 74, 127);

        Font assessmentUserNameFont = FontFactory.getFont(ITextFontTypeEnum.ARIALUNI.getName(), BaseFont.IDENTITY_H, 26, Font.NORMAL);
        Font assessmentCompanyNameFont = FontFactory.getFont(ITextFontTypeEnum.ARIALUNI.getName(), BaseFont.IDENTITY_H, 20, Font.NORMAL);
        Font normal14Font = FontFactory.getFont(ITextFontTypeEnum.ARIALUNI.getName(), BaseFont.IDENTITY_H, 14, Font.NORMAL);
        Font normal12Font = FontFactory.getFont(ITextFontTypeEnum.ARIALUNI.getName(), BaseFont.IDENTITY_H, 12, Font.NORMAL);
        Font normal11Font = FontFactory.getFont(ITextFontTypeEnum.ARIALUNI.getName(), BaseFont.IDENTITY_H, 11, Font.NORMAL);
        Font bold11Font = FontFactory.getFont(ITextFontTypeEnum.ARIALUNI.getName(), BaseFont.IDENTITY_H, 11, Font.BOLD);
        Font normal9Font = FontFactory.getFont(ITextFontTypeEnum.ARIALUNI.getName(), BaseFont.IDENTITY_H, 9, Font.NORMAL);
        Font whitel2Font = FontFactory.getFont(ITextFontTypeEnum.ARIALUNI.getName(), BaseFont.IDENTITY_H, 12, Font.BOLD);
        whitel2Font.setColor(Color.WHITE);

        PdfPTable pdfAppraisalTable = new PdfPTable(new float[]{0.45f, 0.65f});
        pdfAppraisalTable.getDefaultCell().setBorderWidth(0f);
        pdfAppraisalTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfAppraisalTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        pdfAppraisalTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfAppraisalTable.setWidthPercentage(100);

        PdfPCell marginCell = new PdfPCell();
        marginCell.setPaddingBottom(45);
        marginCell.setBorder(0);

        PdfPCell mainLabel = new PdfPCell();
        mainLabel.setBorder(0);
        mainLabel.setColspan(2);
        mainLabel.setPadding(45);
        mainLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        mainLabel.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.performanceAppraisal), FontFactory.getFont(ITextFontTypeEnum.TIMES_NEW_ROMAN.getName(), BaseFont.IDENTITY_H, 32, Font.NORMAL, Color.WHITE)));
        mainLabel.setBackgroundColor(new Color(0, 72, 128));
        pdfAppraisalTable.addCell(mainLabel);

        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(0);
        cell1.setMinimumHeight(15);
        cell1.setBackgroundColor(new Color(0, 72, 128));
        pdfAppraisalTable.addCell(cell1);

        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setMinimumHeight(15);
        cell2.setBackgroundColor(new Color(250, 167, 55));
        pdfAppraisalTable.addCell(cell2);

        PdfPCell pdfCellAssessmentNameCell = new PdfPCell();
        pdfCellAssessmentNameCell.setBorder(0);
        pdfCellAssessmentNameCell.setPaddingBottom(20);
        pdfCellAssessmentNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (assessmentName != null) {
            pdfCellAssessmentNameCell.setPhrase(new Phrase("(" + assessmentName + ")", assessmentNameFont));
        }

        PdfPCell pdfCellEmployeeNameCell = new PdfPCell();
        pdfCellEmployeeNameCell.setBorder(0);
        pdfCellEmployeeNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfCellEmployeeNameCell.setPhrase(new Phrase(employeeAssessment.getEmployee().getName(), assessmentUserNameFont));

        PdfPCell pdfCellCompanyNameCell = new PdfPCell();
        pdfCellCompanyNameCell.setBorder(0);
        pdfCellCompanyNameCell.setPaddingBottom(30);
        pdfCellCompanyNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfCellCompanyNameCell.setPhrase(new Phrase(company.getName(), assessmentCompanyNameFont));


        PdfPCell pdfCellDateCell = new PdfPCell();
        pdfCellDateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfCellDateCell.setBorder(0);
        pdfCellDateCell.setPaddingBottom(190);

        if (employeeAssessment.getAssessment() != null && employeeAssessment.getAssessment().getInititateDate() != null) {
            pdfCellDateCell.setPhrase(new Phrase((user != null ? previewformat.format(user.getUserDate(employeeAssessment.getAssessment().getInititateDate())) :
                    previewformat.format(employeeAssessment.getAssessment().getInititateDate())), assessmentCompanyNameFont));
        }

        assessmentTable.addCell(pdfAppraisalTable);
        assessmentTable.addCell(marginCell);
        if (assessmentName != null) {
            assessmentTable.addCell(pdfCellAssessmentNameCell);
        }
        assessmentTable.addCell(pdfCellEmployeeNameCell);
        assessmentTable.addCell(pdfCellCompanyNameCell);
        assessmentTable.addCell(pdfCellDateCell);

        if (employeeAssessment != null) {
            if (user == null) {
                user = employeeAssessment.getEmployee();
            }

//            setFileName(employeeAssessment.getEmployee().getFirstName() + "_" + employeeAssessment.getEmployee().getLastName() +
//                    "_PerformanceAppraisals_" + (employeeAssessment.getDate() == null ? "" :
//                    (user != null ? dateFormat(user.getUserDate(employeeAssessment.getDate()), loggedUserID) : dateFormat(employeeAssessment.getDate(), loggedUserID))));
            // Overal note
            if (employeeAssessment.getAssessment() != null && employeeAssessment.getAssessment().getGeneralComment() != null && !"".equals(employeeAssessment.getAssessment().getGeneralComment().trim())) {
                PdfPCell overalCommmentTitleCell = new PdfPCell();
                overalCommmentTitleCell.setPaddingBottom(20);
                overalCommmentTitleCell.setBorder(0);
                overalCommmentTitleCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.overallCommentsForThisAssessment), normal14Font));

                PdfPTable overallCommentTable = getCommmentTable(commonLocalizer.localize(PdfLocalizationName.comment), normal12Font);
                PdfPCell pdfCommentCell = new PdfPCell();
                pdfCommentCell.setPhrase(new Phrase(employeeAssessment.getAssessment().getGeneralComment(), normal11Font));
                overallCommentTable.addCell(pdfCommentCell);

                PdfPTable commentsTitleTable = new PdfPTable(1);
                commentsTitleTable.setWidthPercentage(100f);
                commentsTitleTable.getDefaultCell().setBorderWidth(0);
                commentsTitleTable.getDefaultCell().setPadding(0);
                commentsTitleTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                commentsTitleTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                commentsTitleTable.setHorizontalAlignment(Element.ALIGN_LEFT);

                commentsTitleTable.addCell(overalCommmentTitleCell);
                commentsTitleTable.addCell(overallCommentTable);

                assessmentTable.addCell(commentsTitleTable);
                assessmentTable.addCell("");
                assessmentTable.addCell("");
                assessmentTable.addCell("");
                assessmentTable.addCell("");
            }

            // Employee PA note
            PerformanceNoteItem[] performanceNoteItems = hrmsService.getPerformanceNoteItems(employeeAssessment.getEmployee().getObjectID());
            if (performanceNoteItems.length != 0) {
                PdfPCell paNotesCell = new PdfPCell();
                paNotesCell.setPaddingBottom(20);
                paNotesCell.setBorder(0);
                paNotesCell.setPhrase(new Phrase(commonLocalizer.localizeWithParam(PdfLocalizationName.perfomanceNoteListTableName, employeeAssessment.getEmployee().getName()), normal14Font));
                assessmentTable.addCell(paNotesCell);
                assessmentTable.addCell(getPerformanceNoteTable(performanceNoteItems, normal9Font));
                assessmentTable.addCell("");
                assessmentTable.addCell("");
                assessmentTable.addCell("");
                assessmentTable.addCell("");
            }

            try {
                /*pdf table column count*/
                int table_Col_Count = (int) ((toScaleRATE - fromScaleRATE) / stepSizeRATE);

                /* Draw rate tables */
                Object[] rateDateObject = getRateList(appraisalsSettings, stepSizeRATE);
                ArrayList<Double> rateScaleValues = (ArrayList<Double>) rateDateObject[0];
                String ratingAsAtringName = (String) rateDateObject[1];

                String fromToStepSizeRATE = "fromRateScale=" + fromScaleRATE + "&" + "toRateScale=" + toScaleRATE + "&" + "stepSizeRate=" + stepSizeRATE;

                // Total overals
                if ((struct.getCalculatedAverage() != 0d) && (goalStruct.getCalculatedAverage() != 0d)) {
                    boolean isWeightable = struct.isWeightable();
                    double rating;
                    double skillOverall = struct.getCalculatedAverage();
                    double goalOverall = goalStruct.getCalculatedAverage();
                    /*if (isWeightable && struct.getSkillWeigthPercent() != 0) {
                        skillOverall = (skillOverall / struct.getSkillWeigthPercent()) * 100;
                    }
                    if (isWeightable && goalStruct.getGoalWeigthPercent() != 0) {
                        goalOverall = (goalOverall / goalStruct.getGoalWeigthPercent()) * 100;
                    }*/
                    if (goalOverall > 0 && isWeightable) {
                        rating = skillOverall + goalOverall;
                    } else if (goalOverall > 0 && !isWeightable) {
                        rating = (skillOverall + goalOverall) / 2;
                    } else {
                        rating = skillOverall;
                    }

                    Image totalOveralImage = null;
                    try {
                        totalOveralImage = Image.getInstance(EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + rating + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    } catch (BadElementException e) {
                        e.printStackTrace();
                    }
                    PdfPTable assessmentOveralTotalTable = getRateScaleTABLE(rateScaleValues, ratingAsAtringName, normal12Font, normal9Font, totalOveralImage, rating);

                    PdfPCell cellOveralTotal = new PdfPCell(assessmentOveralTotalTable);
                    cellOveralTotal.setBorderWidth(0);
                    cellOveralTotal.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPTable overalTotalHeaderTabel = new PdfPTable(1);
                    overalTotalHeaderTabel.setWidthPercentage(100f);
                    overalTotalHeaderTabel.getDefaultCell().setBorderWidth(0);
                    overalTotalHeaderTabel.getDefaultCell().setPadding(0);
                    overalTotalHeaderTabel.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                    overalTotalHeaderTabel.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                    overalTotalHeaderTabel.setHorizontalAlignment(Element.ALIGN_LEFT);

                    overalTotalHeaderTabel.addCell(getHeaderTable(commonLocalizer.localize(PdfLocalizationName.overallCompetenciesandGoalsRate), whitel2Font));
                    overalTotalHeaderTabel.addCell(assessmentOveralTotalTable);

                    assessmentTable.addCell(overalTotalHeaderTabel);
                }

                // Employee Skills //
                // This and last review comparison
                PdfPCell employeeCompetenciesCell = new PdfPCell();
                employeeCompetenciesCell.setPaddingTop(20);
                employeeCompetenciesCell.setPaddingBottom(30);
                employeeCompetenciesCell.setBorder(0);
                employeeCompetenciesCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.employeeCompetencies), normal14Font));
                assessmentTable.addCell(employeeCompetenciesCell);

                if ((struct.getCalculatedAverage() != 0d) && (lastReviewOverall != 0d)) {
                    // This review overall rate
                    double thisOverallRate = struct.getCalculatedAverage();
                    if (struct.isWeightable() && struct.getSkillWeigthPercent() != 0) {
                        thisOverallRate = (thisOverallRate / struct.getSkillWeigthPercent()) * 100;
                        lastReviewOverall = (lastReviewOverall / struct.getSkillWeigthPercent()) * 100;
                    }
                    Image totalOveralReviewImage = null;
                    try {
                        totalOveralReviewImage = Image.getInstance(EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + thisOverallRate + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    } catch (BadElementException e) {
                        e.printStackTrace();
                    }
                    PdfPTable assessmentOveralReviewTotalTable = getRateScaleTABLE(rateScaleValues, ratingAsAtringName, normal12Font, normal9Font, totalOveralReviewImage, thisOverallRate);

                    PdfPCell cellOveralReviewTotal = new PdfPCell(assessmentOveralReviewTotalTable);
                    cellOveralReviewTotal.setBorderWidth(0);
                    cellOveralReviewTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    assessmentTable.addCell(getHeaderTable(commonLocalizer.localize(PdfLocalizationName.overallRateComparison), whitel2Font));
                    assessmentTable.addCell(getRateNames(commonLocalizer.localize(PdfLocalizationName.thisReviewOverallRate), normal12Font));
                    assessmentTable.addCell(cellOveralReviewTotal);

                    //Last review overall rate
                    Image totalOveralLastReviewImage = null;
                    try {
                        totalOveralLastReviewImage = Image.getInstance(EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + lastReviewOverall + "&chartType=last" + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    } catch (BadElementException e) {
                        e.printStackTrace();
                    }
                    PdfPTable assessmentOveralLastReviewTotalTable = getRateScaleTABLE(rateScaleValues, ratingAsAtringName, normal12Font, normal9Font, totalOveralLastReviewImage, lastReviewOverall);

                    PdfPCell cellOveralLastReviewTotal = new PdfPCell(assessmentOveralLastReviewTotalTable);
                    cellOveralLastReviewTotal.setBorderWidth(0);
                    cellOveralLastReviewTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    assessmentTable.addCell(getRateNames(commonLocalizer.localize(PdfLocalizationName.lastReviewOverallRate), normal12Font));
                    assessmentTable.addCell(cellOveralLastReviewTotal);

                    // Overall rates comparison
                    Image totalOveralComparisonImage = null;
                    try {
                        totalOveralComparisonImage = Image.getInstance(EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + thisOverallRate + "&lastRate=" + lastReviewOverall + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    } catch (BadElementException e) {
                        e.printStackTrace();
                    }
                    PdfPTable assessmentOveralComparisonTotalTable = getRateScaleTABLE(rateScaleValues, ratingAsAtringName, normal12Font, normal9Font, totalOveralComparisonImage, (thisOverallRate - lastReviewOverall));

                    PdfPCell cellOveralComparisonTotal = new PdfPCell(assessmentOveralComparisonTotalTable);
                    cellOveralComparisonTotal.setBorderWidth(0);
                    cellOveralComparisonTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    assessmentTable.addCell(getRateNames(commonLocalizer.localize(PdfLocalizationName.comparisonThisReview), normal12Font));
                    assessmentTable.addCell(cellOveralComparisonTotal);

                    assessmentTable.addCell(getOverallRateChart(commonLocalizer.localize(PdfLocalizationName.thisReviewOverallRate), normal12Font, new Color(174, 0, 0), false));
                    assessmentTable.addCell(getOverallRateChart(commonLocalizer.localize(PdfLocalizationName.lastReviewOverallRate), normal12Font, new Color(7, 143, 86), true));

                } else if (struct.getCalculatedAverage() != 0d) {
                    double thisOverallRate = struct.getCalculatedAverage();
                    if (struct.isWeightable() && struct.getSkillWeigthPercent() != 0) {
                        thisOverallRate = (thisOverallRate / struct.getSkillWeigthPercent()) * 100;
                    }
                    Image totalOveralImage = null;
                    try {
                        totalOveralImage = Image.getInstance(EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + thisOverallRate + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    } catch (BadElementException e) {
                        e.printStackTrace();
                    }
                    PdfPTable assessmentOveralTotalTable = getRateScaleTABLE(rateScaleValues, ratingAsAtringName, normal12Font, normal9Font, totalOveralImage, thisOverallRate);

                    PdfPCell cellOveralTotal = new PdfPCell(assessmentOveralTotalTable);
                    cellOveralTotal.setBorderWidth(0);
                    cellOveralTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    assessmentTable.addCell(getHeaderTable(commonLocalizer.localize(PdfLocalizationName.overallRate), whitel2Font));
                    assessmentTable.addCell(cellOveralTotal);
                }

                // Skills //
                showCharts(loggedUserID, groups, "skillChart", employeeAssessment, appraisalsSettings, fromToStepSizeRATE, stepSizeRATE, assessmentTable, normal12Font, normal11Font, bold11Font, normal9Font, whitel2Font, rateScaleValues, ratingAsAtringName, sessionIDParam);


                // Goals //
                // This and last review comparison
                PdfPCell assignedGoalsCell = new PdfPCell();
                assignedGoalsCell.setPaddingTop(20);
                assignedGoalsCell.setPaddingBottom(30);
                assignedGoalsCell.setBorder(0);
                assignedGoalsCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.assignedGoals), normal14Font));
                assessmentTable.addCell(assignedGoalsCell);

                if ((goalStruct.getCalculatedAverage() != 0d) && (goalLastReviewOverall != 0d)) {
                    // This review overall rate
                    double thisOverallRate = goalStruct.getCalculatedAverage();
                    if (goalStruct.isWeightable() && goalStruct.getGoalWeigthPercent() != 0) {
                        thisOverallRate = (thisOverallRate / goalStruct.getGoalWeigthPercent()) * 100;
                        goalLastReviewOverall = (goalLastReviewOverall / goalStruct.getGoalWeigthPercent()) * 100;
                    }

                    Image totalGoalOveralReviewImage = null;
                    try {
                        totalGoalOveralReviewImage = Image.getInstance(EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + thisOverallRate + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    } catch (BadElementException e) {
                        e.printStackTrace();
                    }
                    PdfPTable assessmentOveralGoalTotalTable = getRateScaleTABLE(rateScaleValues, ratingAsAtringName, normal12Font, normal9Font, totalGoalOveralReviewImage, thisOverallRate);

                    PdfPCell cellOveralGoalTotal = new PdfPCell(assessmentOveralGoalTotalTable);
                    cellOveralGoalTotal.setBorderWidth(0);
                    cellOveralGoalTotal.setHorizontalAlignment(Element.ALIGN_CENTER);

                    assessmentTable.addCell(getHeaderTable(commonLocalizer.localize(PdfLocalizationName.overallRateComparison), whitel2Font));
                    assessmentTable.addCell(getRateNames(commonLocalizer.localize(PdfLocalizationName.thisReviewOverallRate), normal12Font));
                    assessmentTable.addCell(cellOveralGoalTotal);

                    //Last review overall rate
                    Image totalGoalLastOveralReviewImage = null;
                    try {
                        totalGoalLastOveralReviewImage = Image.getInstance(EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + goalLastReviewOverall + "&chartType=last" + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    } catch (BadElementException e) {
                        e.printStackTrace();
                    }
                    PdfPTable assessmentOveralLastGoalTotalTable = getRateScaleTABLE(rateScaleValues, ratingAsAtringName, normal12Font, normal9Font, totalGoalLastOveralReviewImage, goalLastReviewOverall);

                    PdfPCell cellOveralLastGoalTotal = new PdfPCell(assessmentOveralLastGoalTotalTable);
                    cellOveralLastGoalTotal.setBorderWidth(0);
                    cellOveralLastGoalTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    assessmentTable.addCell(getRateNames(commonLocalizer.localize(PdfLocalizationName.lastReviewOverallRate), normal12Font));
                    assessmentTable.addCell(cellOveralLastGoalTotal);

                    // Overall rates comparison
                    Image totalGoalComparisonImage = null;
                    try {
                        totalGoalComparisonImage = Image.getInstance(EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + thisOverallRate + "&lastRate=" + goalLastReviewOverall + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    } catch (BadElementException e) {
                        e.printStackTrace();
                    }
                    PdfPTable assessmentOveralGoalComparisonTotalTable = getRateScaleTABLE(rateScaleValues, ratingAsAtringName, normal12Font, normal9Font, totalGoalComparisonImage, (thisOverallRate - goalLastReviewOverall));

                    PdfPCell cellOveralGoalComparisonTotal = new PdfPCell(assessmentOveralGoalComparisonTotalTable);
                    cellOveralGoalComparisonTotal.setBorderWidth(0);
                    cellOveralGoalComparisonTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    assessmentTable.addCell(getRateNames(commonLocalizer.localize(PdfLocalizationName.comparisonThisReview), normal12Font));
                    assessmentTable.addCell(cellOveralGoalComparisonTotal);

                    assessmentTable.addCell(getOverallRateChart(commonLocalizer.localize(PdfLocalizationName.thisReviewOverallRate), normal12Font, new Color(174, 0, 0), false));
                    assessmentTable.addCell(getOverallRateChart(commonLocalizer.localize(PdfLocalizationName.lastReviewOverallRate), normal12Font, new Color(7, 143, 86), true));

                } else if (goalStruct.getCalculatedAverage() != 0d) {
                    double thisOverallRate = goalStruct.getCalculatedAverage();
                    if (goalStruct.isWeightable() && goalStruct.getGoalWeigthPercent() != 0) {
                        thisOverallRate = (thisOverallRate / goalStruct.getGoalWeigthPercent()) * 100;
                    }
                    Image totalGoalOveralImage = null;
                    try {
                        totalGoalOveralImage = Image.getInstance(EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + thisOverallRate + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                    } catch (BadElementException e) {
                        e.printStackTrace();
                    }

                    PdfPTable assessmentOveralGoalRateTotalTable = getRateScaleTABLE(rateScaleValues, ratingAsAtringName, normal12Font, normal9Font, totalGoalOveralImage, thisOverallRate);

                    PdfPCell cellOveralGoalRateTotal = new PdfPCell(assessmentOveralGoalRateTotalTable);
                    cellOveralGoalRateTotal.setBorderWidth(0);
                    cellOveralGoalRateTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    assessmentTable.addCell(getHeaderTable(commonLocalizer.localize(PdfLocalizationName.overallRate), whitel2Font));
                    assessmentTable.addCell(cellOveralGoalRateTotal);
                }
                // Goals //
                if (goalGroups != null) {
                    showCharts(loggedUserID, goalGroups, "goalChart", employeeAssessment, appraisalsSettings, fromToStepSizeRATE, stepSizeRATE, assessmentTable, normal12Font, normal11Font, bold11Font, normal9Font, whitel2Font, rateScaleValues, ratingAsAtringName, sessionIDParam);
                }
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }

        PdfPCell employeeSignatureDateCell = new PdfPCell(getFooterTable(commonLocalizer.localize(PdfLocalizationName.employee) + ": " + employeeAssessment.getEmployee().getName(),
                commonLocalizer.localize(PdfLocalizationName.signature) + ":________________" + commonLocalizer.localize(PdfLocalizationName.date) + ":____________", normal12Font));
        employeeSignatureDateCell.setBorder(0);

        PdfPCell managerSignatureDateCell = new PdfPCell(getFooterTable(commonLocalizer.localize(PdfLocalizationName.ManagerName) + ": " + employeeAssessment.getAssessment().getReviewer().getName(),
                commonLocalizer.localize(PdfLocalizationName.signature) + ":________________" + commonLocalizer.localize(PdfLocalizationName.date) + ":____________", normal12Font));
        managerSignatureDateCell.setBorder(0);


        PdfPTable lastPageBottomTextTable = new PdfPTable(1);
        lastPageBottomTextTable.getDefaultCell().setBorderWidth(0f);
        lastPageBottomTextTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        lastPageBottomTextTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        lastPageBottomTextTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        lastPageBottomTextTable.addCell(employeeSignatureDateCell);
        lastPageBottomTextTable.addCell(managerSignatureDateCell);

        PdfPCell lastTextCell = new PdfPCell(lastPageBottomTextTable);
        lastTextCell.setBorder(0);
        lastTextCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        lastTextCell.setTop(600);

        assessmentTable.addCell(lastPageBottomTextTable);

        iTextGenericPdfData.setCustomView(iTextCustomView);
        return iTextGenericPdfData;
    }

    private PdfPCell getPerformanceNoteTable(PerformanceNoteItem[] performanceNoteItems, Font normal9Font) {
        PdfPTable performanceNoteTable = new PdfPTable(new float[]{0.07f, 0.22f, 0.45f, 0.1f, 0.15f});
        performanceNoteTable.getDefaultCell().setBorderWidth(0f);
        performanceNoteTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        performanceNoteTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        performanceNoteTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        performanceNoteTable.setWidthPercentage(100);

        PdfPCell noteNumCell = new PdfPCell(new Phrase(commonLocalizer.localize(PdfLocalizationName.number), normal9Font));
        noteNumCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        noteNumCell.setBackgroundColor(Color.LIGHT_GRAY);
        noteNumCell.setPadding(3);

        PdfPCell noteNameCell = new PdfPCell(new Phrase(commonLocalizer.localize(PdfLocalizationName.name), normal9Font));
        noteNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        noteNameCell.setBackgroundColor(Color.LIGHT_GRAY);
        noteNameCell.setPadding(3);

        PdfPCell noteDescCell = new PdfPCell(new Phrase(commonLocalizer.localize(PdfLocalizationName.description), normal9Font));
        noteDescCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        noteDescCell.setBackgroundColor(Color.LIGHT_GRAY);
        noteDescCell.setPadding(3);

        PdfPCell noteStatusCell = new PdfPCell(new Phrase(commonLocalizer.localize(PdfLocalizationName.status), normal9Font));
        noteStatusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        noteStatusCell.setBackgroundColor(Color.LIGHT_GRAY);
        noteStatusCell.setPadding(3);

        PdfPCell noteReviewerCell = new PdfPCell(new Phrase(commonLocalizer.localize(PdfLocalizationName.reviewer), normal9Font));
        noteReviewerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        noteReviewerCell.setBackgroundColor(Color.LIGHT_GRAY);
        noteReviewerCell.setPadding(3);

        performanceNoteTable.addCell(noteNumCell);
        performanceNoteTable.addCell(noteNameCell);
        performanceNoteTable.addCell(noteDescCell);
        performanceNoteTable.addCell(noteStatusCell);
        performanceNoteTable.addCell(noteReviewerCell);

        performanceNoteTable.setHeaderRows(performanceNoteTable.size());
        int i = 0;
        for (PerformanceNoteItem performanceNoteItem : performanceNoteItems) {
            PdfPCell noteNumber = new PdfPCell(new Phrase(String.valueOf(++i), normal9Font));
            noteNumber.setHorizontalAlignment(Element.ALIGN_LEFT);
            noteNumber.setPadding(3);

            PdfPCell noteName = new PdfPCell(new Phrase(performanceNoteItem.getName(), normal9Font));
            noteName.setHorizontalAlignment(Element.ALIGN_LEFT);
            noteName.setPadding(3);

            PdfPCell noteDesc = new PdfPCell(new Phrase(performanceNoteItem.getDescription(), normal9Font));
            noteDesc.setHorizontalAlignment(Element.ALIGN_LEFT);
            noteDesc.setPadding(3);

            PdfPCell noteStatus = new PdfPCell(new Phrase(performanceNoteItem.getStatusName(), normal9Font));
            noteStatus.setHorizontalAlignment(Element.ALIGN_LEFT);
            noteStatus.setPadding(3);

            PdfPCell noteReviewer = new PdfPCell(new Phrase(performanceNoteItem.getResolverName() != null ? performanceNoteItem.getResolverName() : "", normal9Font));
            noteReviewer.setHorizontalAlignment(Element.ALIGN_LEFT);
            noteReviewer.setPadding(3);

            performanceNoteTable.addCell(noteNumber);
            performanceNoteTable.addCell(noteName);
            performanceNoteTable.addCell(noteDesc);
            performanceNoteTable.addCell(noteStatus);
            performanceNoteTable.addCell(noteReviewer);
        }

        PdfPCell performanceNoteCell = new PdfPCell(performanceNoteTable);
        performanceNoteCell.setBorder(0);
        return performanceNoteCell;
    }

    private PdfPCell getRateNames(String name, Font normal12Font) {
        PdfPCell rateCellName = new PdfPCell();
        rateCellName.setBorder(0);
        rateCellName.setPhrase(new Phrase(name, normal12Font));
        return rateCellName;
    }

    private PdfPTable getHeaderTable(String name, Font boldl2Font) {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.getDefaultCell().setBorderWidth(0f);
        headerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        headerTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        headerTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        // Paragraph name
        PdfPCell paragraphCell = new PdfPCell();
        paragraphCell.setPhrase(new Phrase(name, boldl2Font));
        paragraphCell.setBorder(0);
        paragraphCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        paragraphCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        paragraphCell.setBackgroundColor(new Color(91, 91, 93));

        PdfPCell paragraphSpaceCell = new PdfPCell();
        paragraphSpaceCell.setBorder(0);
        paragraphSpaceCell.setPaddingBottom(10);

        headerTable.addCell(paragraphCell);
        headerTable.addCell(paragraphSpaceCell);
        return headerTable;
    }

    private PdfPTable getFooterTable(String valu1, String valu2, Font normall2Font) {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.getDefaultCell().setBorderWidth(0f);
        headerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        headerTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        headerTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        // Paragraph name
        PdfPCell leftCell = new PdfPCell();
        leftCell.setBorder(0);
        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        leftCell.setPhrase(new Phrase(valu1, normall2Font));

        PdfPCell rightCell = new PdfPCell();
        rightCell.setBorder(0);
        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        rightCell.setPhrase(new Phrase(valu2, normall2Font));


        headerTable.addCell(leftCell);
        headerTable.addCell(rightCell);
        return headerTable;
    }

    private PdfPTable getOverallRateChart(String name, Font normal12Font, Color Color, boolean space) {
        PdfPTable overalChartTable = new PdfPTable(new float[]{0.17f, 0.03f, 0.8f});
        overalChartTable.setWidthPercentage(0.6f);
        overalChartTable.getDefaultCell().setBorderWidth(0f);
        overalChartTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        overalChartTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        overalChartTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell chartSpaceCell = new PdfPCell();
        chartSpaceCell.setBorder(0);
        chartSpaceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        chartSpaceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell chartCell = new PdfPCell();
        chartCell.setPhrase(new Phrase("", normal12Font));
        chartCell.setBorder(0);
        chartCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        chartCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        chartCell.setBackgroundColor(Color);

        PdfPCell chartNameCell = new PdfPCell();
        chartNameCell.setPhrase(new Phrase(name, normal12Font));
        chartNameCell.setBorder(0);
        chartNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        chartNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell pdfCellColspaceSpace = new PdfPCell();
        pdfCellColspaceSpace.setBorder(0);
        pdfCellColspaceSpace.setColspan(3);
        pdfCellColspaceSpace.setPaddingBottom(20);

        overalChartTable.addCell(chartSpaceCell);
        overalChartTable.addCell(chartCell);
        overalChartTable.addCell(chartNameCell);
        if (space) {
            overalChartTable.addCell(pdfCellColspaceSpace);
        }

        return overalChartTable;
    }

    private Object[] getRateList(AppraisalsSettingsItem appraisalsSettings, double stepSizeRATE) {

        Map<String, ArrayList<Double>> rateStringSetMap = new LinkedHashMap<>();
        HashMap<Double, String> ratingAsStrings = hrmsService.getAssassmentRatings();
        /*DecimalFormat numberFormat = new DecimalFormat(",#0.00");*/
        ArrayList<Double> rateScaleValues = new ArrayList<>();

        double lDoubleValue = appraisalsSettings.getFromScale();
        for (double i = appraisalsSettings.getFromScale(); i <= appraisalsSettings.getToScale(); i += stepSizeRATE) {
            Double aDouble = new BigDecimal(i).setScale(2, RoundingMode.HALF_UP).doubleValue();
            rateScaleValues.add(aDouble);
            lDoubleValue = aDouble;
            //
            rateStringSetMap.computeIfAbsent(ServerAssessmentHelper.getRatingAsString(ratingAsStrings, i, appraisalsSettings), k -> new ArrayList<>());
            if (rateStringSetMap.get(ServerAssessmentHelper.getRatingAsString(ratingAsStrings, i, appraisalsSettings)) != null) {
                rateStringSetMap.get(ServerAssessmentHelper.getRatingAsString(ratingAsStrings, i, appraisalsSettings)).add(aDouble);
            }
        }
        if (lDoubleValue < appraisalsSettings.getToScale()) {
            Double aDouble = BigDecimal.valueOf(appraisalsSettings.getToScale()).setScale(2, RoundingMode.HALF_UP).doubleValue();
            rateScaleValues.add(aDouble);
            rateStringSetMap.computeIfAbsent(ServerAssessmentHelper.getRatingAsString(ratingAsStrings, appraisalsSettings.getToScale(), appraisalsSettings), k -> new ArrayList<>());
            if (rateStringSetMap.get(ServerAssessmentHelper.getRatingAsString(ratingAsStrings, appraisalsSettings.getToScale(), appraisalsSettings)) != null) {
                rateStringSetMap.get(ServerAssessmentHelper.getRatingAsString(ratingAsStrings, appraisalsSettings.getToScale(), appraisalsSettings)).add(aDouble);
            }
        }

        String localizeStringBuilder = getRateNamesString(rateStringSetMap).toString();
        return new Object[]{rateScaleValues, localizeStringBuilder};
    }

    private StringBuilder getRateNamesString(Map<String, ArrayList<Double>> rateStringSetMap) {
        StringBuilder localizeStringBuilder = new StringBuilder();
        for (String rateAsString : rateStringSetMap.keySet()) {
            ArrayList<Double> doubles = rateStringSetMap.get(rateAsString);
            String doublesName;
            if (doubles.size() > 1) {
                doublesName = doubles.get(0) + "-" + doubles.get(doubles.size() - 1);
            } else {
                doublesName = String.valueOf(doubles.get(0));
            }

            localizeStringBuilder.append(" ").append(doublesName).append("-");
            localizeStringBuilder.append(rateAsString).append(";");
        }
        return localizeStringBuilder;
    }


    private PdfPTable getRateScaleTABLE(ArrayList<Double> tableColumnValues, String ratingAsAtringName, Font normal2Font, Font normal9Font, Image image, double rating) throws SAXException {
        PdfPTable rateScaleTable = new PdfPTable(tableColumnValues.size() + 1);
        rateScaleTable.getDefaultCell().setBorderWidth(0f);
        rateScaleTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        rateScaleTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        rateScaleTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell pdfNoBorderCell = new PdfPCell();
        pdfNoBorderCell.setColspan(tableColumnValues.size());
        pdfNoBorderCell.setBorder(0);

        PdfPCell pdfCellTotalCell = new PdfPCell();
        pdfCellTotalCell.setPhrase(new Phrase(format.format(rating), normal2Font));
        pdfCellTotalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfCellTotalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell pdfCellTotalScoreCell = new PdfPCell();
        pdfCellTotalScoreCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfCellTotalScoreCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.score), normal2Font));
        pdfCellTotalScoreCell.setBackgroundColor(new Color(206, 207, 209));

        PdfPCell pdfCellTotalChartCell = new PdfPCell();
        pdfCellTotalChartCell.setImage(image);
        pdfCellTotalChartCell.setColspan(tableColumnValues.size());

        PdfPCell pdfCellTotalNumberSpaceCell = new PdfPCell();
        pdfCellTotalNumberSpaceCell.setPhrase(new Phrase(ratingAsAtringName, normal9Font));
        pdfCellTotalNumberSpaceCell.setColspan(tableColumnValues.size());
        pdfCellTotalNumberSpaceCell.setBorder(0);

        rateScaleTable.addCell(pdfNoBorderCell);
        rateScaleTable.addCell(pdfCellTotalScoreCell);
        rateScaleTable.addCell(pdfCellTotalChartCell);
        rateScaleTable.addCell(pdfCellTotalCell);

        int i = 0;
        for (Double tableColumnValue : tableColumnValues) {
            i++;
            PdfPCell pdfCell = new PdfPCell();
            pdfCell.setBorderWidth(0);
            pdfCell.setBorderWidthBottom(0.5f);
            pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (i == 1) {
                pdfCell.setBorderWidthLeft(0.5f);
                pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            }
            if (i == tableColumnValues.size()) {
                pdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            }
            pdfCell.setPhrase(new Phrase(String.valueOf(tableColumnValue), normal2Font));
            rateScaleTable.addCell(pdfCell);
        }

        PdfPCell pdf1Cell = new PdfPCell();
        pdf1Cell.setBorder(0);
        pdf1Cell.setBorderWidthLeft(0.5f);


        PdfPCell pdf2Cell = new PdfPCell();
        pdf2Cell.setBorder(0);

        rateScaleTable.addCell(pdf1Cell);
        rateScaleTable.addCell(pdfCellTotalNumberSpaceCell);
        rateScaleTable.addCell(pdf2Cell);

        return rateScaleTable;
    }

    private PdfPTable getWeightRateTable(ArrayList<Double> tableColumnValues, String ratingAsAtringName, Font normal2Font, Font normal9Font, Image weightChartImage, Double raiting) throws SAXException {

        PdfPTable rateScaleTable = new PdfPTable(tableColumnValues.size() + 2);
        rateScaleTable.getDefaultCell().setBorderWidth(0f);
        rateScaleTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        rateScaleTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        rateScaleTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell pdfNoBorderCell = new PdfPCell();
        pdfNoBorderCell.setColspan(tableColumnValues.size() + 1);
        pdfNoBorderCell.setBorder(0);

        PdfPCell pdfCellTotalScoreCell = new PdfPCell();
        pdfCellTotalScoreCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.score), normal2Font));
        pdfCellTotalScoreCell.setBackgroundColor(new Color(206, 207, 209));
        pdfCellTotalScoreCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfCellTotalScoreCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell pdfCellTotalChartFirstCell = new PdfPCell();

        PdfPCell pdfCellTotalChartCell = new PdfPCell();
        pdfCellTotalChartCell.setImage(weightChartImage);
        pdfCellTotalChartCell.setColspan(tableColumnValues.size());

        PdfPCell pdfCellTotalCell = new PdfPCell();
        pdfCellTotalCell.setPhrase(new Phrase(format.format(raiting) + ".00", normal2Font));
        pdfCellTotalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfCellTotalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell pdfCellTotalNumberSpaceCell = new PdfPCell();
        pdfCellTotalNumberSpaceCell.setColspan(tableColumnValues.size());
        pdfCellTotalNumberSpaceCell.setPhrase(new Phrase(ratingAsAtringName, normal9Font));
        pdfCellTotalNumberSpaceCell.setBorder(0);


        rateScaleTable.addCell(pdfNoBorderCell);
        rateScaleTable.addCell(pdfCellTotalScoreCell);
        rateScaleTable.addCell(pdfCellTotalChartFirstCell);
        rateScaleTable.addCell(pdfCellTotalChartCell);
        rateScaleTable.addCell(pdfCellTotalCell);

        PdfPCell pdfSpaceCell = new PdfPCell();
        pdfSpaceCell.setPhrase(new Phrase("", normal2Font));
        pdfSpaceCell.setBorder(0);
        pdfSpaceCell.setBorderWidthRight(0.5f);
        rateScaleTable.addCell(pdfSpaceCell);

        int i = 0;
        for (Double tableColumnValue : tableColumnValues) {
            i++;
            PdfPCell pdfCell = new PdfPCell();
            pdfCell.setBorderWidth(0f);
            pdfCell.setBorderWidthBottom(0.5f);
            pdfCell.setPhrase(new Phrase(String.valueOf(tableColumnValue), normal2Font));
            if (tableColumnValues.size() == i) {
                pdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            }
            rateScaleTable.addCell(pdfCell);
        }

        PdfPCell pdf1Cell = new PdfPCell();
        pdf1Cell.setBorder(0);
        pdf1Cell.setBorderWidthLeft(0.5f);

        PdfPCell pdf2Cell = new PdfPCell();
        pdf2Cell.setBorder(0);

        PdfPCell pdf3Cell = new PdfPCell();
        pdf3Cell.setBorder(0);


        rateScaleTable.addCell(pdf1Cell);
        rateScaleTable.addCell(pdf2Cell);
        rateScaleTable.addCell(pdfCellTotalNumberSpaceCell);
        rateScaleTable.addCell(pdf3Cell);

        return rateScaleTable;
    }

    private void showCharts(Integer loggedUserID, SkillAssessmentElem[] groups, String chartType, EdsEmployeeAssessment employeeAssessment, AppraisalsSettingsItem appraisalsSettings, String fromToStepSizeRATE, double stepSizeRATE, PdfPTable assessmentTable, Font normal2Font, Font normal11Font, Font bold11Font, Font normal9Font, Font boldl2Font, ArrayList<Double> rateScaleValues, String ratingAsAtringName, String sessionIDParam) throws SAXException {
        int i = 0;
        for (SkillAssessmentElem skillAssessmentElem : groups) {
            PdfPTable groupsTables = new PdfPTable(2);
            groupsTables.setWidthPercentage(100f);
            groupsTables.getDefaultCell().setBorderWidth(0f);
            groupsTables.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            groupsTables.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
            groupsTables.setHorizontalAlignment(Element.ALIGN_LEFT);

            // Paragraph name
            PdfPCell paragraphCell = new PdfPCell();
            paragraphCell.setPhrase(new Phrase(++i + ". " + skillAssessmentElem.getSkillName(), boldl2Font));
            paragraphCell.setBorder(0);
            paragraphCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            paragraphCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            paragraphCell.setBackgroundColor(new Color(91, 91, 93));
            // Paragraph Weight
            PdfPCell paragraphWeightCell = new PdfPCell();
            paragraphWeightCell.setPhrase(new Phrase(skillAssessmentElem.getWeight() != null ? ("Weight:" + format.format(skillAssessmentElem.getWeight().doubleValue())) : " ", boldl2Font));
            paragraphWeightCell.setBorder(0);
            paragraphWeightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paragraphWeightCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            paragraphWeightCell.setBackgroundColor(new Color(91, 91, 93));
            // Paragraph Description
            PdfPCell paragraphDescriptionCell = new PdfPCell();
            paragraphDescriptionCell.setPhrase(new Phrase(skillAssessmentElem.getSkillDescription(), normal11Font));
            paragraphDescriptionCell.setBorder(0);
            paragraphDescriptionCell.setColspan(2);
            paragraphDescriptionCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            paragraphDescriptionCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            groupsTables.addCell(paragraphCell);
            groupsTables.addCell(paragraphWeightCell);
            groupsTables.addCell(paragraphDescriptionCell);

            PdfPCell groupsCellTable = new PdfPCell(groupsTables);
            groupsCellTable.setBorderWidth(0);
            groupsCellTable.setPaddingBottom(10);
            assessmentTable.addCell(groupsCellTable);

            if ((skillAssessmentElem.getRaiting() != null && skillAssessmentElem.getRaiting() != 0)) {
                Image weightChartImage = null;
                try {
                    weightChartImage = Image.getInstance(EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + skillAssessmentElem.getRaiting() + "&chartType=skill" + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                } catch (BadElementException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PdfPTable weightTable = getWeightRateTable(rateScaleValues, ratingAsAtringName, normal2Font, normal9Font, weightChartImage, skillAssessmentElem.getRaiting());
                PdfPCell weightCellTable = new PdfPCell(weightTable);
                weightCellTable.setBorder(0);
                weightCellTable.setPaddingBottom(20);

                PdfPTable managerRatedTable = new PdfPTable(1);
                managerRatedTable.setWidthPercentage(100);
                managerRatedTable.getDefaultCell().setBorderWidth(0f);
                managerRatedTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                managerRatedTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                managerRatedTable.setHorizontalAlignment(Element.ALIGN_LEFT);

                managerRatedTable.addCell(new Phrase("Manager's rate", normal2Font));
                managerRatedTable.addCell(weightCellTable);

                assessmentTable.addCell(managerRatedTable);

            }

            if (skillAssessmentElem.getEmployeeRating() != null && skillAssessmentElem.getEmployeeRating() != 0 && appraisalsSettings.isEmployeeRate()) {
                Image employeeChartImage = null;
                try {
                    employeeChartImage = Image.getInstance(EdsContextParams.getFullHost() + COMMON_URL + "/downloadPAPDFChart?overRate=" + skillAssessmentElem.getEmployeeRating() + "&chartType=skill" + "&" + fromToStepSizeRATE + "&" + sessionIDParam);
                } catch (BadElementException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                PdfPTable employeeTable = getWeightRateTable(rateScaleValues, ratingAsAtringName, normal2Font, normal9Font, employeeChartImage, skillAssessmentElem.getEmployeeRating());
                PdfPCell employeeRateCellTable = new PdfPCell(employeeTable);
                employeeRateCellTable.setBorder(0);
                employeeRateCellTable.setPaddingBottom(20);

                PdfPTable employeeRatedTable = new PdfPTable(1);
                employeeRatedTable.setWidthPercentage(100);
                employeeRatedTable.getDefaultCell().setBorderWidth(0f);
                employeeRatedTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                employeeRatedTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                employeeRatedTable.setHorizontalAlignment(Element.ALIGN_LEFT);

                employeeRatedTable.addCell(new Phrase("Employee's rate", normal2Font));
                employeeRatedTable.addCell(employeeRateCellTable);

                assessmentTable.addCell(employeeRatedTable);
            }

            PdfPTable skillCommentsTable = getCommmentTable(commonLocalizer.localize(PdfLocalizationName.comments), normal2Font);
            skillCommentsTable.getDefaultCell().setPadding(0f);
            // Last add Note
            addHistoryCommentTable(loggedUserID, employeeAssessment, skillAssessmentElem.getEmployeesComment(), skillAssessmentElem.getReviewersComment(), null, skillAssessmentElem.getRaiting(), true, skillCommentsTable, bold11Font, normal11Font);
            // Note History
            for (SkillCommentItem skillCommentItem : skillAssessmentElem.getRatingCommentItems()) {
                addHistoryCommentTable(loggedUserID, employeeAssessment, skillCommentItem.getEmployeeComment(), skillCommentItem.getReviewerComment(), skillCommentItem.getLastUpdateTime(), null, false, skillCommentsTable, bold11Font, normal11Font);
            }

            if (skillCommentsTable.getRows().size() > 1) {
                assessmentTable.addCell(skillCommentsTable);
                assessmentTable.addCell("");
                assessmentTable.addCell("");
                assessmentTable.addCell("");
            }
        }
    }

    private void addHistoryCommentTable(Integer loggedUserID, EdsEmployeeAssessment employeeAssessment, String employeeComment, String reviewerComment, Date lastUpdateTime, Double raiting, boolean lastHistory, PdfPTable skillComments, Font bold11Font, Font normal11Font) {
        if (isCommentInclude(employeeComment) || isCommentInclude(reviewerComment)) {
            if (isCommentInclude(employeeComment)) {
                PdfPTable commentsTable = new PdfPTable(2);
                commentsTable.setWidthPercentage(100f);
                commentsTable.getDefaultCell().setBorderWidth(0);
                commentsTable.getDefaultCell().setPadding(5f);
                commentsTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                commentsTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                commentsTable.setHorizontalAlignment(Element.ALIGN_LEFT);


                PdfPCell pdfSelfCell = new PdfPCell();
                pdfSelfCell.setBorder(0);
                pdfSelfCell.setPhrase(new Phrase("Self - " + employeeAssessment.getEmployee().getName(), bold11Font));

                PdfPCell pdfDateCell = new PdfPCell();
                pdfDateCell.setBorder(0);
                if (!lastHistory) {
                    pdfDateCell.setPhrase(new Phrase(dateFormat(lastUpdateTime, loggedUserID), bold11Font));
                }
                pdfDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

                PdfPCell pdfCommentCell = new PdfPCell();
                pdfCommentCell.setBorder(0);
                pdfCommentCell.setPhrase(new Phrase(employeeComment, normal11Font));
                pdfCommentCell.setColspan(2);
                pdfCommentCell.setPadding(5f);

                commentsTable.addCell(pdfSelfCell);
                commentsTable.addCell(pdfDateCell);
                commentsTable.addCell(pdfCommentCell);

                PdfPCell commentBodyTableCell = new PdfPCell(commentsTable);
                skillComments.addCell(commentBodyTableCell);
                skillComments.addCell("");
            }

            if (isCommentInclude(reviewerComment)) {
                PdfPTable commentsTable = new PdfPTable(2);
                commentsTable.setWidthPercentage(100f);
                commentsTable.getDefaultCell().setBorderWidth(0);
                commentsTable.getDefaultCell().setPadding(5f);
                commentsTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                commentsTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
                commentsTable.setHorizontalAlignment(Element.ALIGN_LEFT);

                PdfPCell pdfManagerCell = new PdfPCell();
                pdfManagerCell.setBorder(0);
                pdfManagerCell.setPhrase(new Phrase("Manager - " + employeeAssessment.getAssessment().getReviewer().getName(), bold11Font));

                PdfPCell pdfDateCell = new PdfPCell();
                pdfDateCell.setBorder(0);
                if (!lastHistory) {
                    pdfDateCell.setPhrase(new Phrase(dateFormat(lastUpdateTime, loggedUserID), bold11Font));
                } else {
                    String rate = "N/A";
                    if (raiting != null && raiting != 0d) {
                        rate = "Rate - " + format.format(raiting) + ".00";
                    }
                    pdfDateCell.setPhrase(new Phrase(rate, bold11Font));
                }
                pdfDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

                PdfPCell pdfCommentCell = new PdfPCell();
                pdfCommentCell.setBorder(0);
                pdfCommentCell.setPhrase(new Phrase(reviewerComment, normal11Font));
                pdfCommentCell.setColspan(2);
                pdfCommentCell.setPadding(5f);

                commentsTable.addCell(pdfManagerCell);
                commentsTable.addCell(pdfDateCell);
                commentsTable.addCell(pdfCommentCell);

                PdfPCell commentBodyTableCell = new PdfPCell(commentsTable);
                skillComments.addCell(commentBodyTableCell);
                skillComments.addCell("");
            }
        }
    }

    private PdfPTable getCommmentTable(String name, Font normal12Font) {
        PdfPTable commentsTable = new PdfPTable(1);
        commentsTable.setWidthPercentage(100f);
        commentsTable.getDefaultCell().setBorderWidth(0);
        commentsTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        commentsTable.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        commentsTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell pdfHeaderCell = new PdfPCell();
        pdfHeaderCell.setBackgroundColor(new Color(206, 207, 209));
        pdfHeaderCell.setPhrase(new Phrase(name, normal12Font));
        pdfHeaderCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfHeaderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        commentsTable.addCell(pdfHeaderCell);

        return commentsTable;
    }

    private boolean isCommentInclude(String comment) {
        return comment != null && !"".equals(comment.trim());
    }

    protected Object getDataClass(HttpServletRequest request) {
        RequestObject requestObject = new RequestObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        return requestObject;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        if (user == null) {
            user = userManager.get(getUserId(dataClass));
        }
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_PerformanceAppraisals_" + dateFormat(new Date(), user.getObjectID()));
    }

    @Override
    protected Integer getUserId(Object object) {
        return ((RequestObject) object).getUserID();
    }

    private String dateFormat(Date date, Integer loggedUserID) {
        EdsUser currentUser;
        if (loggedUserID != null) {
            currentUser = userManager.get(loggedUserID);
        } else {
            currentUser = userManager.getUser();
        }
        return ServerUtils.shortDateFormat(date, currentUser, true);
    }
}
