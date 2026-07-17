package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import com.edatasite.workforce.core.domain.goal.EdsBusinessGoal;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.core.domain.goal.EdsGoalAssignees;
import com.edatasite.workforce.gwt.core.client.rpc.GoalRequestObject;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.goal.BusinessGoalManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GoalViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private GoalManager goalManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private BusinessGoalManager businessGoalManager;
    private GoalRequestObject requestObject;
    private String type;

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new GoalRequestObject();
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        GoalRequestObject requestObject = (GoalRequestObject) dataClass;
        type = requestObject.getType();
        if (type.equals("personal")) {
            setFileName(commonLocalizer.localize("personalGoal") + "_" + dateFormat(new Date()));
        } else if (type.equals("company")) {
            setFileName(commonLocalizer.localize("companyGoal") + "_" + dateFormat(new Date()));
        } else if (type.equals("business")) {
            setFileName(commonLocalizer.localize("businessGoal") + "_" + dateFormat(new Date()));
        } else if (type.equals("project")) {
            setFileName(commonLocalizer.localize("projectgoal") + "_" + dateFormat(new Date()));
        } else {
            setFileName(commonLocalizer.localize("departmentGoal") + "_" + dateFormat(new Date()));
        }
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        requestObject = (GoalRequestObject) dataClass;
        type = requestObject.getType();
        Integer objectId = requestObject.getObjectID();
        EdsGoal edsGoal = goalManager.get(objectId);
        EdsUser user = goalManager.getUser();
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());

        if (type.equals("company")){
            return getCompanyGoal(objectId);
        }

        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        CustomisedITextTable goalDataTable = new CustomisedITextTable();
        goalDataTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);

        String department = "";
        String projectName = "";
        if (type.equals("personal")) {
            Integer assigneeID = null;
            if (edsGoal.getUndeletedGoalAssignees() != null && edsGoal.getUndeletedGoalAssignees().size() > 0) {
                for (EdsGoalAssignees assignees : edsGoal.getUndeletedGoalAssignees()) {
                    if (assignees.getAssignee() != null) {
                        assigneeID = assignees.getAssignee().getObjectID();
                        break;
                    }
                }
            }
            EdsEmployee edsEmployee = new EdsEmployee();
            edsEmployee = employeeManager.get(assigneeID);
            EdsGoalAssignees assignee = edsGoal.getGoalAssignee(edsEmployee);
            String employeeAssignee = assignee.getAssignee() != null ? assignee.getAssignee().getFullName() : "";
            String target = assignee.getTarget() != null ? String.valueOf(assignee.getTarget().intValue()) : "";
            String actual = assignee.getActual() != null ? String.valueOf(assignee.getActual().intValue()) : "";
            String weight = assignee.getWeight() != null ? String.valueOf(assignee.getWeight().intValue()) : "";

            goalDataTable.addRowWithCode("ASSIGNEE", commonLocalizer.localize("assignee"), employeeAssignee);
            goalDataTable.addRowWithCode("TARGET", commonLocalizer.localize("target"), target);
            goalDataTable.addRowWithCode("ACTUAL", commonLocalizer.localize("actual"), actual);
            goalDataTable.addRowWithCode("WEIGHT", commonLocalizer.localize("weight"), weight);

        } else if (type.equals("department")) {
            if (edsGoal.getDepartment() != null) {
                department = edsGoal.getDepartment().getName();
            }
            goalDataTable.addRowWithCode("DEPARTMENT", commonLocalizer.localize("department"), department);
        } else if (type.equals("project")) {
            if (edsGoal.getProject() != null) {
                projectName = edsGoal.getProject().getName();
            }
            goalDataTable.addRowWithCode("PROJECT_NAME", commonLocalizer.localize("project"), projectName);
        }

        String measurement = edsGoal.getMeasurementUnit() != null ? edsGoal.getMeasurementUnit().getName() : "";
        String actionSteps = edsGoal.getActionSteps();
        String description = edsGoal.getDescription();
        String period = shortDateFormat.format(edsGoal.getFromDate()) + " - " + shortDateFormat.format(edsGoal.getToDate());
        String status = referenceWfmMessageSource.localize(edsGoal.getStatus().getCode(), edsGoal.getStatus().getName());
        String title = edsGoal.getTitle();
        EdsValidityPeriod validityPeriod = edsGoal.getValidityPeriod();
        String validity = validityPeriod != null ? validityPeriod.getName() : "";
        EdsReference edsReference = edsGoal.getScoreCalculation();
        String scoreCalculation = edsReference != null ? edsReference.getName() : null;
        String companyGoal = "";
        if (edsGoal.getBusinessGoal() != null) {
            companyGoal = edsGoal.getBusinessGoal().getTitle();
        }

        goalDataTable.addRowWithCode("STATUS", commonLocalizer.localize("status"), status);
        goalDataTable.addRowWithCode("VALIDITY_PERIOD", commonLocalizer.localize("validity"), validity);
        goalDataTable.addRowWithCode("TITLE", commonLocalizer.localize("title"), title);
        goalDataTable.addRowWithCode("MEASUREMENT", commonLocalizer.localize("measurement"), measurement);
        goalDataTable.addRowWithCode("PERIOD", commonLocalizer.localize("period"), period);
        goalDataTable.addRowWithCode("DESCRIPTION", commonLocalizer.localize("description"), description);
        goalDataTable.addRowWithCode("ACTION_STEPS", commonLocalizer.localize("actionSteps"), actionSteps);
        goalDataTable.addRowWithCode("TYPE", commonLocalizer.localize("type"), type);
        goalDataTable.addRowWithCode("COMPANY_GOAL", commonLocalizer.localize("companyGoal"), companyGoal);
        goalDataTable.addRowWithCode("SCORE_CALCULATION", commonLocalizer.localize("scoreCalculation"), scoreCalculation);

        baseInvoice.setCustomNumberAndDatesTable(goalDataTable);
        pdfData.setBaseInvoice(baseInvoice);
        return pdfData;
    }

    private ITextGenericPdfData getCompanyGoal(Integer objectId) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        EdsUser user = goalManager.getUser();
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        CustomisedITextTable goalDataTable = new CustomisedITextTable();
        goalDataTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);

        EdsBusinessGoal goal = businessGoalManager.get(objectId);
        String title = goal.getTitle();
        String description = goal.getDescription();
        String outcome = goal.getOutcome();
        String status = "";
        if (goal.getStatus() != null) {
            status = referenceWfmMessageSource.localizeRef(goal.getStatus());
        }
        String start = "";
        if (goal.getFromDate() != null) {
            start = shortDateFormat.format(goal.getFromDate());
        }
        String end = "";
        if (goal.getToDate() != null) {
            end = shortDateFormat.format(goal.getToDate());
        }
        String period = start + " - " + end;
        String validity = "";
        if (goal.getValidityPeriod() != null) {
            EdsValidityPeriod validityPeriod = goal.getValidityPeriod();
            validity = validityPeriod.getName();
        }
        String type = requestObject.getType();
        goalDataTable.addRowWithCode("TYPE", commonLocalizer.localize("type"), type);
        goalDataTable.addRowWithCode("STATUS", commonLocalizer.localize("status"), status);
        goalDataTable.addRowWithCode("VALIDITY_PERIOD", commonLocalizer.localize("validity"), validity);
        goalDataTable.addRowWithCode("TITLE", commonLocalizer.localize("title"), title);
        goalDataTable.addRowWithCode("PERIOD", commonLocalizer.localize("period"), period);
        goalDataTable.addRowWithCode("DESCRIPTION", commonLocalizer.localize("description"), description);
        goalDataTable.addRowWithCode("OUTCOME", commonLocalizer.localize("outcome"), outcome);

        baseInvoice.setCustomNumberAndDatesTable(goalDataTable);
        pdfData.setBaseInvoice(baseInvoice);
        return pdfData;
    }


    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((GoalRequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        if (type.equals("personal")) {
            return commonLocalizer.localize("personalGoal");
        } else if (type.equals("company")) {
            return commonLocalizer.localize("companyGoal");
        } else if (type.equals("business")) {
            return commonLocalizer.localize("businessGoal");
        } else if (type.equals("project")) {
            return commonLocalizer.localize("projectgoal");
        } else {
            return commonLocalizer.localize("departmentGoal");
        }
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.Goal;
    }
}
