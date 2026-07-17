package com.edatasite.workforce.gwt.assessment.client.ui.view.ui;

import com.edatasite.workforce.gwt.assessment.client.localization.AssessmentMessages;
import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.BonusSettingsItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.ScoreItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElemsStruct;
import com.edatasite.workforce.gwt.assessment.client.ui.AssessmentHelper;
import com.edatasite.workforce.gwt.assessment.client.ui.IssueMessagePanel2;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.PerformanceNoteItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

/**
 * User: Ilhombek
 * Date: 3/13/12
 * Time: 6:25 PM
 */
public class ReviewHeader extends Composite implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final AssessmentMessages assessmentMessages = AssessmentMessages.App.get();
    private final NumberFormat numberFormat = NumberFormat.getFormat(",#0.00");
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private Integer int_employeeAssessmentID;
    private TextBox overallCompetencyWeightPercent;
    private TextBox overallGoalWeightPercent;
    private String overallStatus;
    private boolean isWeighTable;
    private Integer managerID;
    private boolean isCurrentUserManager = false;

    private BonusSettingsItem bonusSettingsItem;
    private AppraisalsSettingsItem settingsItem;

    //header
    @UiField
    SpanElement headerPAEmployeeName;
    @UiField
    SpanElement overallRateField;
    @UiField
    HTMLPanel firstListItem;
    @UiField
    HTMLPanel secondListItem;

    interface ReviewHeaderUiBinder extends UiBinder<HTMLPanel, ReviewHeader> {
    }

    public ReviewHeader() {
        ReviewHeaderUiBinder ourUiBinder = GWT.create(ReviewHeaderUiBinder.class);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void addErrorStyleWeights() {
        if (isWeighTable && overallCompetencyWeightPercent != null && overallGoalWeightPercent != null) {
            overallCompetencyWeightPercent.addStyleName("x-form-invalid");
            overallGoalWeightPercent.addStyleName("x-form-invalid");
        }
    }

    public void drawInitialize(Integer int_employeeAssessmentID, SkillAssessmentElemsStruct competencyStruct, SkillAssessmentElemsStruct goalStruct, String overallStatus, boolean userISNotEmployee, AppraisalsSettingsItem settingsItem) {
        this.int_employeeAssessmentID = int_employeeAssessmentID;
        this.settingsItem = settingsItem;

        if (competencyStruct.getBonusSettingsItem() != null) {
            this.bonusSettingsItem = competencyStruct.getBonusSettingsItem();
        } else if (goalStruct.getBonusSettingsItem() != null) {
            this.bonusSettingsItem = goalStruct.getBonusSettingsItem();
        }

        boolean hasGoal = goalStruct.getEmployeeId() != null;
        isWeighTable = competencyStruct.isWeightable();
        this.overallStatus = overallStatus;
        this.managerID = competencyStruct.getReviewerID();
        this.isCurrentUserManager = competencyStruct.isCurrentUserReviewer();

        double average;
        double competencyOverall = competencyStruct.getCalculatedAverage();
        double goalOverall = hasGoal ? goalStruct.getCalculatedAverage() : 0;
        if (goalOverall > 0 && !isWeighTable) {
            average = (competencyOverall + goalOverall) / 2;
        } else if (goalOverall > 0 && isWeighTable) {
            average = competencyOverall + goalOverall;
        } else {
            average = competencyOverall;
        }

        headerPAEmployeeName.setInnerHTML(assessmentMessages.performanceAppraisal(competencyStruct.getEmployeeName()));
        overallRateField.setInnerHTML(wfmStrings.overallRate());
        redrawRateBuilder(average);

        //register company name
        String[] companyNameT = getIsEmptyTitleS(competencyStruct.getCompanyName());
        Span contentCompanyName = new Span(companyNameT[0]);
        if (!"".equals(companyNameT[1])) {
            contentCompanyName.setText(companyNameT[1]);
        }

        FormGroup titleCompanyName = new FormGroup(wfmStrings.companyName(), contentCompanyName);
        appendDOMElementFirst(titleCompanyName);

        //register department name
        String[] departmentNameT = getIsEmptyTitleS(competencyStruct.getDepartmentName());
        Span contentDepartmentName = new Span(departmentNameT[0]);
        if (!"".equals(departmentNameT[1])) {
            contentDepartmentName.setText(departmentNameT[1]);
        }
        FormGroup titleDepartmentName = new FormGroup(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), contentDepartmentName);
        appendDOMElementFirst(titleDepartmentName);

        //register assessment template name
        if (settingsItem.isUseCompetencies()) {
            String[] assessmentTemplateNameT = getIsEmptyTitleS(competencyStruct.getTemplateName());
            Span contentAssessmentTemplateName = new Span(assessmentTemplateNameT[0]);
            if (!"".equals(assessmentTemplateNameT[1])) {
                contentAssessmentTemplateName.setText(assessmentTemplateNameT[1]);
            }
            FormGroup titleAssessmentTemplateName = new FormGroup(hrmsStrings.assessmentTemplate(), contentAssessmentTemplateName);
            appendDOMElementFirst(titleAssessmentTemplateName);
        }
        //register employee name
        String[] employeeNameT = getIsEmptyTitleS(competencyStruct.getEmployeeName());
        Span contentEmployeeName = new Span(employeeNameT[0]);
        if (!"".equals(employeeNameT[1])) {
            contentEmployeeName.setText(employeeNameT[1]);
        }
        FormGroup titleEmployeeName = new FormGroup(wfmStrings.employee(), contentEmployeeName);
        appendDOMElementSecond(titleEmployeeName);

        //register manager's name
        String[] reviewerNameT = getIsEmptyTitleS(competencyStruct.getReviewerName());
        Span contentManagerName = new Span(reviewerNameT[0]);
        if (!"".equals(reviewerNameT[1])) {
            contentManagerName.setText(reviewerNameT[1]);
        }
        FormGroup titleManagerName = new FormGroup(wfmStrings.ManagerName(), contentManagerName);
        appendDOMElementSecond(titleManagerName);
        //register competency/goal weight percent
        if (isWeighTable) {
            if (settingsItem.isUseCompetencies() && settingsItem.isUseGoals()) {
                getOverallCompetencyGoalWEIGHTPERCENT();

                InputGroup inputGroup = new InputGroup(overallCompetencyWeightPercent, overallGoalWeightPercent);
                overallCompetencyWeightPercent.setText(competencyStruct.getSkillWeigthPercent() + "");
                overallGoalWeightPercent.setText((goalStruct.getGoalWeigthPercent() > 0 ? goalStruct.getGoalWeigthPercent() : 0) + "");

                FormGroup titleCompetencyGoalWeightPercent = new FormGroup(hrmsStrings.skillOrGoalWeighPercent(), inputGroup);
                appendDOMElementSecond(titleCompetencyGoalWeightPercent);
            }
        }

        //register header image
        if (INITIATED.equals(overallStatus) || SAVED_AS_DRAFT.equals(overallStatus)) {
            if (userISNotEmployee) {
//                headerPAReview.setSrc(assessmentLogoClientBundle.paInitiate().getSafeUri().asString());//initiate
            } else {
//                headerPAReview.setSrc(assessmentLogoClientBundle.paReview().getSafeUri().asString());//review
            }
        } else if (REVIEWED_BY_MANAGER.equals(overallStatus) /*|| SUBMITTED_FOR_APPROVE.equals(overallStatus)*/) {
//            headerPAReview.setSrc(assessmentLogoClientBundle.paReview().getSafeUri().asString());//reviewed by manager
        } else if (REVIEWED_BY_EMPLOYEE.equals(overallStatus)) {
//            headerPAReview.setSrc(assessmentLogoClientBundle.paRate().getSafeUri().asString());//reviewed by employee
        } else if (RATED.equals(overallStatus)) {
//            headerPAReview.setSrc(assessmentLogoClientBundle.paRate().getSafeUri().asString());//rated
        } else if (APPROVED_BY_MANAGER.equals(overallStatus)) {
//            headerPAReview.setSrc(assessmentLogoClientBundle.paApprove().getSafeUri().asString());//approved
        }
        drawEmployeePerformanceNotes(competencyStruct);
    }

    public void redrawRateBuilder(double average) {
        if (bonusSettingsItem != null) {
            ScoreItem scoreItem = bonusSettingsItem.getScoreItem(average);
            if (scoreItem != null) {
                overallRateField.setInnerHTML(wfmStrings.overallRate() + ": " + AssessmentHelper.getCustomTITLE(numberFormat.format(average) + " (Score " + scoreItem.getName() + ")"));
            }
        } else {
            if (Utils.isCustomRateEnable()) {
                overallRateField.setInnerHTML(wfmStrings.overallRate() + ": " + AssessmentHelper.getCustomTITLE(AssessmentHelper.getCustomRatingAsString(average, settingsItem.getCustomRates()) + "(" + numberFormat.format(average) + ")"));
            } else {
                overallRateField.setInnerHTML(wfmStrings.overallRate() + ": " + AssessmentHelper.getCustomTITLE(AssessmentHelper.getRatingAsString(average) + "(" + numberFormat.format(average) + ")"));
            }
        }
//        rateBuilder.redrawRateBuilder(roundRating, numberFormat.format(average), true);
    }

    public boolean validateOverallPERCENT() {
        int errors = 0;
        if (isWeighTable) {
            if (settingsItem.isUseCompetencies() && settingsItem.isUseGoals()) {
                if (overallCompetencyWeightPercent.getText() == null || "".equals(overallCompetencyWeightPercent.getText()) ||
                        overallGoalWeightPercent.getText() == null || "".equals(overallGoalWeightPercent.getText())) {
                    overallCompetencyWeightPercent.addStyleName("x-form-invalid");
                    overallGoalWeightPercent.addStyleName("x-form-invalid");
                    errors++;
                }
            }
        }
        return errors == 0;
    }

    private void appendDOMElementFirst(Widget child) {
        firstListItem.add(child);
    }

    private void appendDOMElementSecond(Widget child) {
        secondListItem.add(child);
    }

    private void changePercents(TextBox percentTEXT1, TextBox percentTEXT2, boolean competency) {
        Validation.numberValidation(percentTEXT1);
        Double firstPercent = 0d;
        Double secondPercent = 0d;
        if (!"".equals(percentTEXT1.getText())) {
            try {
                firstPercent = Double.valueOf(percentTEXT1.getText());
                if (firstPercent > -1 && firstPercent < 101 && (!"".equals(overallCompetencyWeightPercent.getText()))) {
                    percentTEXT2.setText("" + (100 - firstPercent));
                    secondPercent = Double.valueOf(percentTEXT2.getText());
                    if (percentTEXT2.getStyleName().contains("x-form-invalid")) {
                        percentTEXT2.removeStyleName("x-form-invalid");
                    }
                } else {
                    percentTEXT1.addStyleName("x-form-invalid");
                }
            } catch (NumberFormatException e) {
                percentTEXT1.addStyleName("x-form-invalid");
                firstPercent = 0d;
                secondPercent = 0d;
            }
        }
        Double[] returnDoubles = ((competency) ? new Double[]{firstPercent, secondPercent, int_employeeAssessmentID.doubleValue()} : new Double[]{secondPercent, firstPercent, int_employeeAssessmentID.doubleValue()});
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SKILL_COMPETENCY_GOAL_RATIO_CHANGED, returnDoubles, ReviewHeader.this);
    }

    private void drawEmployeePerformanceNotes(final SkillAssessmentElemsStruct competencyStruct) {
        HrmsService.App.get().getPerformanceNoteItems(competencyStruct.getEmployeeId(), new AbstractAsyncCallback<PerformanceNoteItem[]>() {
            @Override
            public void success(PerformanceNoteItem[] result) {
                if (result != null && result.length > 0) {
                    IssueMessagePanel2 performanceNotePanel = new IssueMessagePanel2(result, competencyStruct.getEmployeeName());
                    //include note panel
                    secondListItem.add(performanceNotePanel);
                }
            }
        });
    }

    private String[] getIsEmptyTitleS(String sTitle) {
        String[] strings = new String[2];
        String s;
        boolean hasTitle = false;
        if (sTitle == null || "".equals(sTitle)) {
            s = "";
        } else if (sTitle.length() > 17) {
            s = sTitle.substring(0, 17) + "...";//after all length == 20
            hasTitle = true;
        } else {
            s = sTitle;
        }
        strings[0] = s;
        strings[1] = hasTitle ? sTitle : "";
        return strings;
    }

    private void getOverallCompetencyGoalWEIGHTPERCENT() {
        boolean enableTextBox = false;
        if (isCurrentUserManager && !APPROVED_BY_MANAGER.equals(overallStatus)) {
            enableTextBox = true;
        }
        //competency PERCENT textBox
        if (settingsItem.isUseCompetencies()) {
            overallCompetencyWeightPercent = new TextBox();
            overallCompetencyWeightPercent.setWidth("60px");
            overallCompetencyWeightPercent.setEnabled(enableTextBox);
            addListener(overallCompetencyWeightPercent.getElement(), true);
        }
        //goal PERCENT textBox
        if (settingsItem.isUseGoals()) {
            overallGoalWeightPercent = new TextBox();
            overallGoalWeightPercent.setWidth("60px");
            addListener(overallGoalWeightPercent.getElement(), false);
            overallGoalWeightPercent.setEnabled(enableTextBox);
        }
    }

    private void addListener(Element element, final boolean isCompetency) {
        DOM.sinkEvents(element, Event.ONKEYPRESS | Event.ONBLUR);
        DOM.setEventListener(element, event -> {
            switch (DOM.eventGetType(event)) {
                case Event.ONKEYPRESS: {
                    if (overallCompetencyWeightPercent != null && overallCompetencyWeightPercent.getStyleName().contains("x-form-invalid")) {
                        overallCompetencyWeightPercent.removeStyleName("x-form-invalid");
                    }
                    if (overallGoalWeightPercent != null && overallGoalWeightPercent.getStyleName().contains("x-form-invalid")) {
                        overallGoalWeightPercent.removeStyleName("x-form-invalid");
                    }
                    break;
                }
                case Event.ONBLUR: {
                    if (overallCompetencyWeightPercent != null && overallGoalWeightPercent != null) {
                        if (isCompetency) {
                            changePercents(overallCompetencyWeightPercent, overallGoalWeightPercent, isCompetency);
                        } else {
                            changePercents(overallGoalWeightPercent, overallCompetencyWeightPercent, isCompetency);
                        }
                    }
                    break;
                }
            }
        });
    }
}
