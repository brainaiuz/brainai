package com.edatasite.workforce.gwt.assessment.client.ui.view.ui;

import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElemsStruct;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

import java.util.ArrayList;

/**
 * User: Ilhombek
 * Date: 3/17/12
 * Time: 10:02 AM
 */
public class SingleSkillPanel2 extends Composite implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private final String ERROR_STYLE = "x-form-invalid";
    private String savedAsDraftComment;
    private String employeeName;
    private Integer employeeID;
    private String skillComment;
    private boolean isCompetencyContainer;
    private boolean isWeighTable;
    private double overallRate;
    private String overallStatus;
    private String managerName;
    private Integer managerID;
    private boolean isCurrentUserManager = false;
    private Double rate;
    private String grade;
    private boolean ratable = true;
    private ArrayList<Double> rates;
    private boolean reviewOnly;
    private String skillDescription;
    private Integer skillID;
    private String skillName;
    private Integer skillRatingID;

    private String team;
    private boolean turn;
    private boolean userISEmployee;
    private boolean userISManager;
    private Double weight = 0d;
    private TextBox weightBox;
    private EditableSkillRateCommentBuilder2 yourCommentBuilder;

    private AppraisalsSettingsItem settingsItem;
    //
    @UiField
    HTMLPanel headerSkillField;
    @UiField
    HorizontalPanelDiv headerWeightField;
    @UiField
    HTMLPanel contentPanel;
    @UiField
    HTMLPanel footerPanel;
    private Integer employeeAssessmentID;


    interface SingleSkillPanel2UiBinder extends UiBinder<HTMLPanel, SingleSkillPanel2> {
    }

    /**
     * Crate default constructor
     */
    public SingleSkillPanel2() {

        SingleSkillPanel2UiBinder ourUiBinder = GWT.create(SingleSkillPanel2UiBinder.class);
        initWidget(ourUiBinder.createAndBindUi(this));

        rates = new ArrayList<>();
    }

    /**
     * Create constructor with params
     *
     * @param isCompetencyContainer - isCompetencyContainer
     * @param employeeAssessmentID  - employee assessment ID
     * @param allSkills             - all skills
     * @param skill                 - skill element
     * @param isManager             - isCurrentApprover
     * @param userISEmployee        - user isEmployee
     * @param reviewOnly            - review only
     * @param isWeighTable          - isWeighTable
     */
    public SingleSkillPanel2(boolean isCompetencyContainer, Integer employeeAssessmentID, SkillAssessmentElemsStruct allSkills, SkillAssessmentElem skill, boolean isManager, boolean userISEmployee, boolean reviewOnly, boolean isWeighTable, AppraisalsSettingsItem settingsItem,boolean isFromShift) {
        this();
        this.isCompetencyContainer = isCompetencyContainer;
        this.employeeAssessmentID = employeeAssessmentID;
        this.reviewOnly = reviewOnly;
        this.skillName = skill.getSkillName();
        this.skillDescription = skill.getSkillDescription();
        this.skillRatingID = skill.getSkillRatingId();
        this.skillID = skill.getSkillId();
        this.overallStatus = allSkills.getStatus();
        this.ratable = skill.isShowRadio() == null || skill.isShowRadio();
        this.turn = allSkills.isTurn();
        this.settingsItem = settingsItem;
        if (isWeighTable) {
            this.weight = skill.getWeight();
            this.isWeighTable = isWeighTable;
        }
        this.managerName = allSkills.getReviewerName();
        this.managerID = allSkills.getReviewerID();
        this.isCurrentUserManager = allSkills.isCurrentUserReviewer();
        this.employeeName = allSkills.getEmployeeName();
        this.employeeID = allSkills.getEmployeeId();
        this.team = allSkills.getDepartmentName();
        this.grade = skill.getEmployeeGrade();

        this.userISEmployee = userISEmployee;
        this.userISManager = isManager;
        this.savedAsDraftComment = skill.getSavedAsDraftComment();

        initHeader();
//        initEmployeeOrManagerComment(skill,isFromShift);
        initEmployeeOrManagerRate(skill);
        //comment history
        initCommentHistory(skill);
    }

    /**
     * Return comment as String for giving skill
     *
     * @return comment
     */
    public String getYourComment() {
        return yourCommentBuilder.getComment();
    }

    /**
     * Return rate as Integer for giving skill
     *
     * @return rate
     */
    public Double getYourRate() {
        return yourCommentBuilder.getRate();
    }

    public String getYourGrade() {
        return yourCommentBuilder.getGrade();
    }

    /**
     * Return skillRateID
     *
     */
    public Integer getSkillRatingID() {
        return skillRatingID;
    }

    /**
     * Return skillID
     *
     * @return integer
     */
    public Integer getSkillID() {
        return skillID;
    }

    /**
     * Return weight box
     *
     * @return weight box
     */
    public TextBox getWeightBox() {
        return weightBox;
    }

    public boolean isRatable() {
        return ratable;
    }

    public void setRatable(boolean ratable) {
        this.ratable = ratable;
    }

    public void setShowGradePicker(boolean isShow) {
        if (yourCommentBuilder != null) {
            yourCommentBuilder.showRatePicker(isShow);
        }
    }

    private String getSkillDescriptionT(String description) {
        return description != null && !"".equals(description) ? description.replaceAll("\n", "<br/>") : description;
    }

    private void initHeader() {
        String headerString = "<h3>" + skillName + "</h3>";
        if (skillDescription != null) {
            headerString += "<p>" + getSkillDescriptionT(skillDescription) + "</p>";
        }
        HTML headerHTML = new HTML(headerString);
        headerSkillField.add(headerHTML);

        if (isWeighTable) {
            Element span = createWeightFieldSpan();
            headerWeightField.getElement().appendChild(span);

            weightBox = createWeightBox();
            weightBox.setEnabled(isCurrentUserManager && !APPROVED.equals(overallStatus));
            weightBox.addKeyDownHandler(this::handleWeightBoxKeyDown);
            weightBox.addValueChangeHandler(this::handleWeightBoxValueChange);
            Validation.addNumericKeyboardListener(weightBox);
            weightBox.setText(String.valueOf(weight));
            headerWeightField.add(weightBox);
        }
    }

    private Element createWeightFieldSpan() {
        Element span = DOM.createSpan();
        span.getStyle().setColor("#045789");
        span.getStyle().setFontWeight(Style.FontWeight.BOLD);
        span.setInnerHTML(wfmStrings.weight() + ": ");
        return span;
    }

    private TextBox createWeightBox() {
        TextBox weightBoxWidget = new TextBox();
        weightBoxWidget.setWidth("60px");
        weightBoxWidget.setMaxLength(4);
        weightBoxWidget.setEnabled(false);
        return weightBoxWidget;
    }

    private void handleWeightBoxKeyDown(KeyDownEvent event) {
        TextBox textbox = (TextBox) event.getSource();
        if (textbox.getText().length() < 1) {
            textbox.setStyleName(ERROR_STYLE);
        } else if (!"".equals(textbox.getStyleName()) && textbox.getStyleName().contains(ERROR_STYLE)) {
            textbox.removeStyleName(ERROR_STYLE);
        }
    }

    private void handleWeightBoxValueChange(ValueChangeEvent<String> event) {
        String value = event.getValue();
        if (value != null && !"".equals(value)) {
            Double weightValue = 0d;
            try {
                weightValue += Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                weightValue += 0d;
            }
            Double[] returnDoubles = new Double[]{getSkillID().doubleValue(), weightValue, employeeAssessmentID.doubleValue(), (isCompetencyContainer ? (double) 1 : (double) 0)};
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SKILL_WEIGHT_CHANGED, returnDoubles, SingleSkillPanel2.this);
        }
    }


    private void initEmployeeOrManagerComment(SkillAssessmentElem skill,boolean isFromShift) {
        SkillRateCommentBuilder2 commentBuilder = null;
        if (skill.getEmployeesComment() == null) {
            skill.setEmployeesComment("");
        }
        if ((userISEmployee && userISManager && reviewOnly) && !INITIATED.equals(overallStatus) && skill.getEmployeesComment() != null) {
            commentBuilder = new SkillRateCommentBuilder2(skill.getEmployeesComment(), employeeName, hrmsStrings.employeesComment(), team, skill.getEmployeeGrade(), weight, overallStatus, ratable, reviewOnly, settingsItem,isFromShift);
        } else if (userISEmployee && !INITIATED.equals(overallStatus) && !SAVED_AS_DRAFT.equals(overallStatus)) {
            if (!(skill.getReviewersComment() == null && !ratable) && !reviewOnly) {
                commentBuilder = new SkillRateCommentBuilder2(skill.getReviewersComment(), managerName, hrmsStrings.managersComment(), team, skill.getManagersGrade(), weight, overallStatus, ratable, settingsItem,isFromShift);
            }
        } else if (!INITIATED.equals(overallStatus) && skill.getEmployeesComment() != null) {
            boolean employeeRate = settingsItem.isEmployeeRate();
            commentBuilder = new SkillRateCommentBuilder2(skill.getEmployeesComment(), employeeName, hrmsStrings.employeesComment(), team, skill.getEmployeeGrade(), weight, overallStatus, employeeRate, reviewOnly, settingsItem,isFromShift);
        }
        if (commentBuilder != null) {
            contentPanel.add(commentBuilder);
        }
    }

    private void initEmployeeOrManagerRate(SkillAssessmentElem skill) {
        if (userISEmployee && userISManager && reviewOnly) {
            if (APPROVED.equals(overallStatus) || APPROVED_BY_MANAGER.equals(overallStatus)) {
                yourCommentBuilder = new EditableSkillRateCommentBuilder2(isCompetencyContainer, employeeAssessmentID, savedAsDraftComment, skill.getReviewersComment(), grade, weight, true, true, skillID, settingsItem, userISManager);
                yourCommentBuilder.showRatePicker(ratable);
                yourCommentBuilder.setEnabledComment(true);
            } else {
                yourCommentBuilder = new EditableSkillRateCommentBuilder2(isCompetencyContainer, employeeAssessmentID, savedAsDraftComment, skill.getReviewersComment(), grade, weight, true, skillID, settingsItem, userISManager);
                yourCommentBuilder.showRatePicker(ratable);
                yourCommentBuilder.setEnabledComment(reviewOnly);
            }
        } else if (userISEmployee) {
            if (APPROVED.equals(overallStatus) || APPROVED_BY_MANAGER.equals(overallStatus) || reviewOnly) {
                yourCommentBuilder = new EditableSkillRateCommentBuilder2(isCompetencyContainer, employeeAssessmentID, savedAsDraftComment, skill.getEmployeesComment(), skill.getEmployeeGrade(), weight, true, true, skillID, settingsItem, userISManager);
                yourCommentBuilder.showRatePicker(settingsItem.isEmployeeRate() && reviewOnly);
                yourCommentBuilder.setEnabledComment(true);
            } else {
                yourCommentBuilder = new EditableSkillRateCommentBuilder2(isCompetencyContainer, employeeAssessmentID, savedAsDraftComment, skill.getEmployeesComment(), skill.getEmployeeGrade(), weight, true, skillID, settingsItem, false);
                yourCommentBuilder.showRatePicker(settingsItem.isEmployeeRate());
                yourCommentBuilder.setEnabledComment(reviewOnly);
            }
        } else {
            if (APPROVED.equals(overallStatus) || reviewOnly) {
                yourCommentBuilder = new EditableSkillRateCommentBuilder2(isCompetencyContainer, employeeAssessmentID, savedAsDraftComment, skill.getReviewersComment(), skill.getManagersGrade(), weight, true, true, skillID, settingsItem, userISManager);
                yourCommentBuilder.showRatePicker(ratable);
                yourCommentBuilder.setEnabledComment(true);
            } else {
                yourCommentBuilder = new EditableSkillRateCommentBuilder2(isCompetencyContainer, employeeAssessmentID, savedAsDraftComment, skill.getReviewersComment(), skill.getManagersGrade(), weight, true, skillID, settingsItem, userISManager);
                yourCommentBuilder.showRatePicker(ratable);
                yourCommentBuilder.setEnabledComment(reviewOnly);
            }
        }
        footerPanel.add(yourCommentBuilder);
    }

    private void initYourComment() {
        boolean rateBuilderStatic = APPROVED.equals(overallStatus) || APPROVED_BY_MANAGER.equals(overallStatus) || this.reviewOnly;
        if (rates != null) {
            yourCommentBuilder = new EditableSkillRateCommentBuilder2(isCompetencyContainer, employeeAssessmentID, savedAsDraftComment, skillComment, grade, ratable, overallRate, rateBuilderStatic, skillID, settingsItem);
        } else {
            yourCommentBuilder = new EditableSkillRateCommentBuilder2(isCompetencyContainer, employeeAssessmentID, savedAsDraftComment, skillComment, grade, ratable, rateBuilderStatic, skillID, settingsItem);
        }
        footerPanel.add(yourCommentBuilder);
    }

    private void initCommentHistory(SkillAssessmentElem skill) {
        if (skill != null && skill.getRatingCommentItems() != null && !skill.getRatingCommentItems().isEmpty()) {
            //register here comment history
            boolean isFirstEmployeeComment = false;
            if ((userISEmployee && userISManager && reviewOnly) && !INITIATED.equals(overallStatus) && skill.getEmployeesComment() != null) {
                isFirstEmployeeComment = true;
            } else if (userISEmployee && !INITIATED.equals(overallStatus)) {
                if (!(skill.getReviewersComment() == null && !ratable) && !reviewOnly) {
                    isFirstEmployeeComment = false;
                }
            } else if (!INITIATED.equals(overallStatus) && skill.getEmployeesComment() != null) {
                isFirstEmployeeComment = true;
            }
            SkillCommentHistoryPanel skillCommentHistoryPanel = new SkillCommentHistoryPanel(skill.getRatingCommentItems(), isFirstEmployeeComment);
            footerPanel.add(skillCommentHistoryPanel);
        }
    }
}
