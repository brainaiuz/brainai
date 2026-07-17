package com.edatasite.workforce.gwt.assessment.client.ui.view.ui;

import com.edatasite.workforce.gwt.assessment.client.localization.AssessmentMessages;
import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.BonusSettingsItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.ScoreItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElemsStruct;
import com.edatasite.workforce.gwt.assessment.client.ui.AssessmentHelper;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: Ilhombek
 * Date: 3/12/12
 * Time: 6:03 PM
 */
public class SimpleAppraisalView extends CustomForm2 implements FormHasCustomFieldInterface, Constants, Colapse {


    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final AssessmentMessages assessmentMessages = AssessmentMessages.App.get();
    private final Integer employeeAssessmentID;
    private final NumberFormat numberFormat = NumberFormat.getFormat(",#0.00");
    boolean isreviewadded = false;
    boolean isEmployeeReviewed = false;
    private SkillAssessmentElemsStruct competencyStruct;
    private SkillAssessmentElemsStruct goalStuct;
    private SkillAssessmentElem[] competencyElements;
    private SkillAssessmentElem[] goalElements;
    boolean applyApproveButton = false;
    private Map<Integer, Double[]> competencyRating;
    private Map<Integer, Double[]> goalRating;
    private ArrayList<Integer> competencyRatingIDs;
    private ArrayList<Integer> goalRatingIDs;
    private SimpleSkillContainer2 competencyContainer;
    private SimpleSkillContainer2 goalContainer;
    private AppraisalsSettingsItem settingsItem;
    private String overallStatus;
    private boolean isWeighTable;
    double average;
    double competencyOverall;
    private boolean reviewOnly;
    private boolean userISManager;
    private boolean userISNotEmployee;
    double goalOverall;
    private WfmButton2 approveButton;
    private WfmButton2 rateButton; // Review and submit to employee
    private WfmButton2 reviewButton; //Review and submit to reviewer
    private WfmButton2 saveAsDraftButton;
    private int competencyPercentINT = 50;
    private int goalPercentINT = 50;
    private TextArea2 generalCommentArea;
    private Span employeeName;
    private Span managerName;
    private Span departmentName;
    private Span templateName;
    private boolean selfInitiated;
    private Span overAllRate;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private BonusSettingsItem bonusSettingsItem;
    private FormHasCustomField customFieldUtil;
    private Span assessmentDate;

    public SimpleAppraisalView(Integer employeeAssessmentID, String status) {
        super("assessment", hrmsStrings.assessViewPanel());
        this.employeeAssessmentID = employeeAssessmentID;
        this.overallStatus = status;
    }

    public SimpleAppraisalView(Integer employeeAssessmentID, String status, boolean reviewOnly) {
        this(employeeAssessmentID, status);
        this.reviewOnly = reviewOnly;
    }

    @Override
    public String getDescription() {
        return wfmStrings.review();
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);

        AssessmentService.App.get().getAppraisalsSettings(new AbstractAsyncCallback<AppraisalsSettingsItem>() {
            @Override
            public void success(AppraisalsSettingsItem result) {
                LoadingPanel.loading(false);
                settingsItem = result;
                SimpleAppraisalView.super.onInitialize();
            }
        });

        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Assessment, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                GWT.log("", throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
            }
        });

        return null;
    }

    @Override
    protected void registerFields() {
        competencyContainer = new SimpleSkillContainer2(employeeAssessmentID, reviewOnly, true, settingsItem);
        competencyContainer.addStyleName("assessmentSkillRateContainer");
        goalContainer = new SimpleSkillContainer2(employeeAssessmentID, reviewOnly, false, settingsItem);
        goalContainer.addStyleName("assessmentGoalСontainer");


        //overall comment builder
        generalCommentArea = new TextArea2(3000);
        generalCommentArea.getTextArea().getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        generalCommentArea.getTextArea().getElement().getStyle().setBorderColor("#000000");
        generalCommentArea.getTextArea().getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
        generalCommentArea.getTextArea().getElement().getStyle().setHeight(100, Style.Unit.PX);
        generalCommentArea.setVisible(false);

        FormGroup overAllCommentForm = new FormGroup(wfmStrings.comments(), generalCommentArea);

        departmentName = new Span();
        FormGroup departmentForm = new FormGroup(wfmStrings.department(), departmentName);

        employeeName = new Span();
        FormGroup employeeNameForm = new FormGroup(wfmStrings.employee(), employeeName);

        managerName = new Span();
        FormGroup managerNameForm = new FormGroup(wfmStrings.ManagerName(), managerName);

        templateName = new Span();
        FormGroup templateForm = new FormGroup(wfmStrings.template(), templateName);

        overAllRate = new Span(wfmStrings.overallRate());

        assessmentDate = new Span();
        FormGroup assessmentDateForm = new FormGroup(wfmStrings.date(), assessmentDate);

        addField(DEPARTMENT_NAME, departmentForm, null);
        addField(EMPLOYEE_NAME, employeeNameForm, null);
        addField(MANAGER_NAME, managerNameForm, null);
        addField(TEMPLATE_NAME, templateForm, null);
        addField(OVERALL_COMMENTS, overAllCommentForm, null);
        addField(OVERALL_RATE, overAllRate, null);
        addField(EMPLOYEE_COMPETENCIES, competencyContainer, null);
        addField(ASSESSMENT_DATE,assessmentDateForm,null);

        show();
    }

    @Override
    protected void initPredefinedValues() {
// To do
    }

    @Override
    protected void addButtons() {
        rateButton = new WfmButton2(hrmsStrings.reviewSubmitEmployee(), BTN_DEFAULT_OUTLINE);
        reviewButton = new WfmButton2(hrmsStrings.reviewSubmitManager(), BTN_DEFAULT_OUTLINE);
        saveAsDraftButton = new WfmButton2(wfmStrings.saveAsDraft(), BTN_DEFAULT_OUTLINE);
        approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_PRIMARY);
        approveButton.setVisible(false);
        rateButton.setVisible(false);
        reviewButton.setVisible(false);
        saveAsDraftButton.setVisible(false);
    }

    @Override
    protected void getDataToFillFields() {
        competencyContainer.setVisible(settingsItem.isUseCompetencies());
        goalContainer.setVisible(settingsItem.isUseGoals());

        registerEventListeners();
        loadSkillAssessmentElemGroups();
    }

    private void registerEventListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SKILL_CHANGED, SimpleAppraisalView.this, (sender, args) -> handleSkillChangedEvent(args));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SKILL_WEIGHT_CHANGED, SimpleAppraisalView.this, (sender, args) -> handleSkillWeightChangedEvent(args));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SKILL_COMPETENCY_GOAL_RATIO_CHANGED, SimpleAppraisalView.this, (sender, args) -> handleSkillCompetencyGoalRatioChangedEvent(args));
    }

    private void loadSkillAssessmentElemGroups() {
        LoadingPanel.loading(true);

        AssessmentService.App.get().getSkillAssessmentElemGroups(employeeAssessmentID, new AbstractAsyncCallback<SkillAssessmentElemsStruct>() {
            @Override
            public void success(SkillAssessmentElemsStruct result) {
                LoadingPanel.loading(false);
                competencyStruct = result;
                overallStatus = competencyStruct.getStatus();

                boolean isSupervisor = competencyStruct.isCurrentUserSupervisor();
                boolean isManager = competencyStruct.isCurrentUserReviewer();
                boolean isEmployee = competencyStruct.isCurrentUserEmployee();
                boolean isSelfUser = isManager || !isEmployee;
                userISManager = ((Utils.hasRole(ADMIN) || Utils.hasRole(DR) || Utils.hasRole(ADMIN_LOCATION) || Utils.hasRole(HR) || Utils.hasRole(TL) || isSupervisor) && isSelfUser);
                selfInitiated = competencyStruct.getEmployeeId().equals(competencyStruct.getInitiatorID());

                userISNotEmployee = !isEmployee;

                loadGoalAssessmentElemGroups(isManager, isEmployee);
            }
        });
    }

    private void handleSkillChangedEvent(Object args) {
        if (args instanceof Integer[]) {
            Integer[] argsIntegers = (Integer[]) args;
            handleIntegerArgs(argsIntegers);
        } else if (args instanceof String[]) {
            String[] argsStrings = (String[]) args;
            handleStringArgs(argsStrings);
        } else if (args instanceof Double[]) {
            Double[] argsDoubles = (Double[]) args;
            handleDoubleArgs(argsDoubles);
        }
    }

    private void handleIntegerArgs(Integer[] argsIntegers) {
        Integer ratingID = argsIntegers[0];
        Double rate = argsIntegers[1].doubleValue();
        Integer employeeAssessmentId = argsIntegers[2];
        Boolean isCompetency = argsIntegers[3] != null && argsIntegers[3] == 1;
        if (employeeAssessmentID.equals(employeeAssessmentId)) {
            changeCompetenciesAndGoalsRate(ratingID, rate, isCompetency);
        }
    }

    private void handleStringArgs(String[] argsStrings) {
        Integer ratingID = Integer.parseInt(argsStrings[0]);
        Double rate;
        try {
            rate = Double.parseDouble(argsStrings[1]);
        } catch (NumberFormatException ex) {
            rate = 0d;
        }
        Integer employeeAssessmentId = Integer.parseInt(argsStrings[2]);
        Boolean isCompetency = argsStrings[3] != null && Integer.parseInt(argsStrings[3]) == 1;
        if (employeeAssessmentID.equals(employeeAssessmentId)) {
            changeCompetenciesAndGoalsRate(ratingID, rate, isCompetency);
        }
    }

    private void handleDoubleArgs(Double[] argsDoubles) {
        Integer ratingID = argsDoubles[0].intValue();
        Double rate = argsDoubles[1];
        Integer employeeAssessmentId = argsDoubles[2].intValue();
        Boolean isCompetency = argsDoubles[3] != null && argsDoubles[3].intValue() == 1;
        if (employeeAssessmentID.equals(employeeAssessmentId)) {
            changeCompetenciesAndGoalsRate(ratingID, rate, isCompetency);
        }
    }


    private void handleSkillWeightChangedEvent(Object args) {
        if (args instanceof Double[] && isWeighTable) {
            Double[] argsDoubles = (Double[]) args;
            Integer ratingID = argsDoubles[0].intValue();
            Double weight = argsDoubles[1];
            Integer employeeAssessmentId = argsDoubles[2].intValue();
            Boolean isCompetency = Boolean.FALSE;
            if (argsDoubles[3] != null) {
                isCompetency = argsDoubles[3].intValue() == 1 ? Boolean.TRUE : Boolean.FALSE;
            }
            if (employeeAssessmentID.equals(employeeAssessmentId)) {
                changeCompetenciesAndGoalsWeight(ratingID, weight, isCompetency);
            }
        }
    }

    private void handleSkillCompetencyGoalRatioChangedEvent(Object args) {
        if (args instanceof Double[] && isWeighTable) {
            Double[] argsDoubles = (Double[]) args;
            Double overallCompetencyWeight = argsDoubles[0];
            Double overallGoalWeight = argsDoubles[1];
            Integer employeeAssessmentId = argsDoubles[2].intValue();
            if (employeeAssessmentID.equals(employeeAssessmentId)) {
                competencyStruct.setSkillWeigthPercent(overallCompetencyWeight.intValue());
                goalStuct.setGoalWeigthPercent(overallGoalWeight.intValue());

                rateChanger();
            }
        }
    }


    private void loadGoalAssessmentElemGroups(boolean isManager, boolean isEmployee) {
        LoadingPanel.loading(true);

        AssessmentService.App.get().getGoalAssessmentElemGroups(employeeAssessmentID, new AbstractAsyncCallback<SkillAssessmentElemsStruct>() {
            @Override
            public void success(SkillAssessmentElemsStruct resultGoal) {
                LoadingPanel.loading(false);
                goalStuct = resultGoal;

                if (goalStuct.getEmployeeId() != null) {
                    if (resultGoal.getElems() != null && resultGoal.getElems().length > 0) {
                        addField(ASSIGNED_GOALS, goalContainer, null);
                    }
                    goalContainer.addHeader(getHeaderTITLE(wfmStrings.assignedGoals()));
                    goalContainer.init(goalStuct);
                    goalRatingIDs = new ArrayList<>();
                    goalRating = new HashMap<>();
                    if (goalStuct.getElems() != null && goalStuct.getElems().length > 0) {
                        SkillAssessmentElem[] gElements = goalStuct.getElems();
                        for (SkillAssessmentElem goalAssessmentElem : gElements) {
                            Double[] doubles = new Double[]{Boolean.TRUE.equals(goalStuct.isWeightable()) ? goalAssessmentElem.getWeight() : 0, goalAssessmentElem.getRaiting()};

                            goalRatingIDs.add(goalAssessmentElem.getSkillId());
                            goalRating.put(goalAssessmentElem.getSkillId(), doubles);
                        }
                    }
                }
                //register add to container

                getHeader(competencyStruct, goalStuct);
                loadCompetencyAssessmentElemGroups(isManager, isEmployee);
            }
        });
    }

    private void loadCompetencyAssessmentElemGroups(boolean isManager, boolean isEmployee) {
        LoadingPanel.loading(true);
        competencyContainer.init(competencyStruct);
        competencyRatingIDs = new ArrayList<>();
        competencyRating = new HashMap<>();

        if (competencyStruct.getElems() != null && competencyStruct.getElems().length > 0) {
            SkillAssessmentElem[] cElements = competencyStruct.getElems();
            for (SkillAssessmentElem skillAssessmentElem : cElements) {
                Double[] doubles = new Double[]{Boolean.TRUE.equals(competencyStruct.isWeightable()) ? skillAssessmentElem.getWeight() : 0, skillAssessmentElem.getRaiting()};
                competencyRatingIDs.add(skillAssessmentElem.getSkillId());
                competencyRating.put(skillAssessmentElem.getSkillId(), doubles);
            }
        }

        competencyContainer.setSkillRatingIDs(competencyRatingIDs);
        competencyContainer.setSkillRating(competencyRating);

        loadGeneralComments(isManager, isEmployee);
    }

    private void loadGeneralComments(boolean isManager, boolean isEmployee) {
        generalCommentArea.setVisible(true);
        generalCommentArea.setEnabled(!APPROVED.equals(overallStatus));
        generalCommentArea.setText(competencyStruct.getGeneralComment());

        registerAddNewAndButtonsPanel(isManager, isEmployee);
        LoadingPanel.loading(false);
    }


    @Override
    protected String getFormID() {
        return LayoutRPC.ASSESSMENT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    /**
     * Related after change competencies calculate rate
     *
     * @param ratingID     - ratingID
     * @param rate         - rate
     * @param isCompetency - isCompetency
     */
    private void changeCompetenciesAndGoalsRate(Integer ratingID, Double rate, boolean isCompetency) {
        if (isCompetency) {
            if (competencyRating != null && competencyRatingIDs.contains(ratingID)) {
                competencyRating.put(ratingID, new Double[]{competencyRating.get(ratingID)[0], rate});
                rateChanger();
            }
        } else {
            if (goalRatingIDs != null && goalRatingIDs.contains(ratingID)) {
                goalRating.put(ratingID, new Double[]{goalRating.get(ratingID)[0], rate});
                rateChanger();
            }
        }
    }

    private void changeCompetenciesAndGoalsWeight(Integer ratingID, Double weight, boolean isCompetency) {
        if (isCompetency) {
            if (competencyRating != null && competencyRatingIDs.contains(ratingID)) {
                competencyRating.put(ratingID, new Double[]{weight, competencyRating.get(ratingID)[1]});
                rateChanger();
            }
        } else {
            if (goalRatingIDs != null && goalRatingIDs.contains(ratingID)) {
                goalRating.put(ratingID, new Double[]{weight, goalRating.get(ratingID)[1]});
                rateChanger();
            }
        }
    }

    private void getHeader(SkillAssessmentElemsStruct competencyStrict, SkillAssessmentElemsStruct goalStruct) {
        isWeighTable = competencyStrict.isWeightable();
        departmentName.setText(competencyStrict.getDepartmentName());
        templateName.setText(competencyStrict.getTemplateName());
        employeeName.setText(competencyStrict.getEmployeeName());
        managerName.setText(competencyStrict.getReviewerName());
        overAllRate.setText(wfmStrings.overallRate() + ": ");
        assessmentDate.setText(DateUtils.format(competencyStrict.getAssessmentDate()));

        if (competencyStrict.getBonusSettingsItem() != null) {
            this.bonusSettingsItem = competencyStrict.getBonusSettingsItem();
        } else if (goalStruct.getBonusSettingsItem() != null) {
            this.bonusSettingsItem = goalStruct.getBonusSettingsItem();
        }

        getAvarage(competencyStrict, goalStruct);
    }

    private void getAvarage(SkillAssessmentElemsStruct competencyStruct, SkillAssessmentElemsStruct goalStruct) {
        boolean hasGoal = goalStruct.getEmployeeId() != null;
        competencyOverall = competencyStruct.getCalculatedAverage();
        goalOverall = hasGoal ? goalStruct.getCalculatedAverage() : 0;
        if (goalOverall > 0 && !isWeighTable) {
            average = (competencyOverall + goalOverall) / 2;
        } else if (goalOverall > 0) {
            average = competencyOverall + goalOverall;
        } else {
            average = competencyOverall;
        }
        rateBuilder(average);
    }

    public void rateBuilder(double average) {
        if (bonusSettingsItem != null) {
            ScoreItem scoreItem = bonusSettingsItem.getScoreItem(average);
            if (scoreItem != null) {
                overAllRate.setText(wfmStrings.overallRate() + ": " + (numberFormat.format(average) + " (Score " + scoreItem.getName() + ")"));
            }
        } else {
            if (Boolean.TRUE.equals(Utils.isCustomRateEnable())) {
                overAllRate.setText(wfmStrings.overallRate() + ": " + AssessmentHelper.getCustomRatingAsString(average, settingsItem.getCustomRates()) + "(" + numberFormat.format(average) + ")");
            } else {
                overAllRate.setText(wfmStrings.overallRate() + ": " + AssessmentHelper.getRatingAsString(average) + "(" + numberFormat.format(average) + ")");
            }
        }
    }

    private Div getHeaderTITLE(String headerTitle) {
        Div titleDiv = new Div("form-group__label");
        titleDiv.getElement().setInnerText(headerTitle);
        return titleDiv;
    }

    private double getCalculationAverage() {
        boolean hasGoal = goalRating != null && !goalRating.isEmpty();

        if (settingsItem.isUseCompetencies() && settingsItem.isUseGoals()) {
            return calculateAverageWithCompetenciesAndGoals(hasGoal);
        } else if (settingsItem.isUseCompetencies()) {
            return calculateAverageWithCompetencies();
        } else {
            return calculateAverageWithGoals(hasGoal);
        }
    }

    private double calculateAverageWithCompetenciesAndGoals(boolean hasGoal) {
        double competencyOverallRate = getCalculatedAverage(competencyRating != null ? competencyRating : new HashMap<>(), competencyStruct.getSkillWeigthPercent());
        Map<Integer, Double[]> goalRatings = goalRating != null ? goalRating : new HashMap<>();
        double goalOverallRate = hasGoal ? getCalculatedAverage(goalRatings, goalStuct.getGoalWeigthPercent()) : 0;

        if (goalOverallRate > 0 && !isWeighTable) {
            return (competencyOverallRate + goalOverallRate) / 2;
        } else if (goalOverallRate > 0 && isWeighTable) {
            return competencyOverallRate + goalOverallRate;
        } else {
            return competencyOverallRate;
        }
    }

    private double calculateAverageWithCompetencies() {
        return getCalculatedAverage(competencyRating != null ? competencyRating : new HashMap<>(), competencyStruct.getSkillWeigthPercent());
    }

    private double calculateAverageWithGoals(boolean hasGoal) {
        Map<Integer, Double[]> map = goalRating != null ? goalRating : new HashMap<>();
        return hasGoal ? getCalculatedAverage(map, goalStuct.getGoalWeigthPercent()) : 0;
    }


    private float getCalculatedAverage(Map<Integer, Double[]> rating, int percent) {
        float ratingSum = 0f;
        int count = 0;

        for (Double[] rateDoubles : rating.values()) {
            if (rateDoubles[1] != null && rateDoubles[1] > 0) {
                ratingSum += isWeighTable ? (rateDoubles[0] * rateDoubles[1] / 100f) : rateDoubles[1];
                count++;
            }
        }
        float avg = count > 0 ? ratingSum / count : 0;
        return isWeighTable ? (ratingSum * percent / 100) : avg;
    }


    private void rateChanger() {
        rateBuilder(getCalculationAverage());
    }

    // Только локально
    private static boolean isLocalhost() {
        String h = Window.Location.getHostName();
        return "localhost".equals(h) || "127.0.0.1".equals(h);
    }

    // ?forceAdd=1|true|yes (должно быть ДО # в URL)
    private static boolean forceShowAdd() {
        String v = Window.Location.getParameter("forceAdd");
        if (v == null) v = Window.Location.getParameter("showAdd");
        if (v == null) return false;
        v = v.trim().toLowerCase();
        return "1".equals(v) || "true".equals(v) || "yes".equals(v);
    }


    private void registerAddNewAndButtonsPanel(final boolean manager, final boolean employee) {
        if (shouldShowCompetencyAddNewField() || (isLocalhost() && forceShowAdd())) {
            competencyContainer.initAddNewField();
        }

        registerButtonListeners(rateButton, REVIEWED_BY_MANAGER);
        registerButtonListeners(reviewButton, REVIEWED_BY_EMPLOYEE);
        registerButtonListeners(saveAsDraftButton, SAVED_AS_DRAFT);
        registerButtonListeners(approveButton, APPROVED_BY_MANAGER);

        approveButton.setVisible(true);
        rateButton.setVisible(!EMPLOYEE_STATUS_NO_ACCCESS.equals(competencyStruct.getEmployeeStatus()));
        reviewButton.setVisible(true);
        saveAsDraftButton.setVisible(true);

        registerButtonPanel(manager, employee);
    }

    private boolean shouldShowCompetencyAddNewField() {
        return userISNotEmployee &&
                !(REVIEWED_BY_MANAGER.equals(overallStatus) || APPROVED.equals(overallStatus) || APPROVED_BY_MANAGER.equals(overallStatus)) &&
                !reviewOnly &&
                settingsItem.isUseCompetencies();
    }

    private void registerButtonPanel(final boolean manager, final boolean employee) {
        if (SAVED_AS_DRAFT.equals(overallStatus) && competencyStruct.isCurrentUserLastUpdater()) {
            handleSavedAsDraft();
            return;
        }

        if ((REJECTED.equals(overallStatus) || INITIATED.equals(overallStatus)) && !reviewOnly) {
            handleRejectedOrInitiated();
        } else if (REVIEWED_BY_EMPLOYEE.equals(overallStatus) && userISNotEmployee && !reviewOnly) {
            handleReviewedByEmployee();
        } else if (REVIEWED_BY_MANAGER.equals(overallStatus) && !userISNotEmployee && !reviewOnly) {
            handleReviewedByManager();
        } else if (APPROVED_BY_MANAGER.equals(overallStatus) && userISManager) {
            handleApprovedByManager();
        }

        handleOtherStatus(manager, employee, isreviewadded, isEmployeeReviewed, applyApproveButton);
    }

    private void handleSavedAsDraft() {
        if (userISNotEmployee) {
            isEmployeeReviewed = true;
            applyApproveButton = true;
            addButton(rateButton);
            addButton(approveButton);
        } else {
            addButton(reviewButton);
            isreviewadded = true;
        }
    }

    private void handleRejectedOrInitiated() {
        if (userISNotEmployee) {
            addButton(rateButton);
            isEmployeeReviewed = true;
            addButton(approveButton);
            applyApproveButton = true;
        } else {
            addButton(reviewButton);
            isreviewadded = true;
        }
    }

    private void handleReviewedByEmployee() {
        addButton(rateButton);
        isEmployeeReviewed = true;
        addButton(approveButton);
        applyApproveButton = true;
    }

    private void handleReviewedByManager() {
        addButton(reviewButton);
        isreviewadded = true;
    }

    private void handleApprovedByManager() {
        addButton(rateButton);
        isEmployeeReviewed = true;
        applyApproveButton = true;
    }

    private void handleOtherStatus(boolean manager, boolean employee, boolean isreviewadded, boolean isEmployeeReviewed, boolean applyApproveButton) {
        if ((APPROVED.equals(overallStatus) || APPROVED_BY_MANAGER.equals(overallStatus)) || reviewOnly) {
            return; // No need to proceed if already approved or in review-only mode
        }

        if (!SAVED_AS_DRAFT.equals(overallStatus)) {
            if (manager && employee && !applyApproveButton) {
                addButton(approveButton);
            }

            if (REVIEWED_BY_MANAGER.equals(overallStatus) && selfInitiated) {
                addButton(reviewButton);
                isreviewadded = true;
            }

            if (!isreviewadded && !isEmployeeReviewed) {
                addButton(reviewButton);
            }

            addButton(saveAsDraftButton);
        } else {
            handleSavedAsDraftForLastUpdater(manager, employee, applyApproveButton);
        }
    }


    private void handleSavedAsDraftForLastUpdater(boolean manager, boolean employee, boolean applyApproveButton) {
        if (competencyStruct.isCurrentUserLastUpdater()) {
            if (manager && employee && !applyApproveButton) {
                addButton(approveButton);
            }
            addButton(saveAsDraftButton);
        }
    }

    private void registerButtonListeners(WfmButton2 button, final String statusAction) {
        button.getElement().getStyle().setMarginRight(5, Style.Unit.PX);
        button.addClickHandler(event -> {
            competencyElements = competencyContainer.getDataToSave(true);
            goalElements = goalContainer.getDataToSave(false);

            String successMessage = Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.assessment());

            if (employeeAssessmentID != null) {
                successMessage = Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.assessment());
            }
            if (INITIATED.equals(statusAction)) {
                successMessage = hrmsStrings.initiatedSuccessfully();
            }
            if (REVIEWED_BY_EMPLOYEE.equals(statusAction)) {
                successMessage = Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.review());
            }
            if (REVIEWED_BY_MANAGER.equals(statusAction)) {
                successMessage = Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.review());
            }
            if (APPROVED_BY_MANAGER.equals(statusAction)) {
                successMessage = hrmsStrings.approvedSuccessfully();
            }
            if (REVIEWED_BY_EMPLOYEE.equals(statusAction) || REVIEWED_BY_MANAGER.equals(statusAction)) {
                if (validateSkills(goalContainer, competencyContainer)) {
                    saveAssessment(successMessage, statusAction);
                } else {
                    Info.warn(hrmsStrings.pleaseCommentAndRate());
                }
            } else {
                saveAssessment(successMessage, statusAction);
            }
        });
    }

    private boolean validateSkills(SimpleSkillContainer2 goalContainer, SimpleSkillContainer2 competencyContainer) {
        return goalContainer.validateRateContainer() && competencyContainer.validateRateContainer();
    }

    private void registerButtonsEnableDisableOption(boolean b) {
        if (approveButton != null) {
            approveButton.setEnabled(b);
        }
        if (rateButton != null) {
            rateButton.setEnabled(b);
        }
        if (reviewButton != null) {
            reviewButton.setEnabled(b);
        }
        if (saveAsDraftButton != null) {
            saveAsDraftButton.setEnabled(b);
        }
    }

    private void saveAssessment(final String successMessage, String status) {
        registerButtonsEnableDisableOption(false);
        if (!validateWeightCount()) {
            registerButtonsEnableDisableOption(true);
            return;
        }
        String generalCommentT = generalCommentArea != null ? generalCommentArea.getText() : "";
        if (isWeighTable) {
            competencyPercentINT = competencyStruct.getSkillWeigthPercent();
            goalPercentINT = goalStuct.getGoalWeigthPercent();
        }
        if (status.equals(APPROVED_BY_MANAGER)) {
            status = APPROVED;
        }
        LoadingPanel.loading(true);
        AssessmentService.App.get().assess2(employeeAssessmentID, competencyElements, goalElements, status, generalCommentT,
                competencyPercentINT, goalPercentINT, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        registerButtonsEnableDisableOption(true);
                    }

                    @Override
                    public void success(Void result) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SIMPLE_APPRAISAL_INITIATED, "", SimpleAppraisalView.this);
                        LoadingPanel.loading(false);
                        Info.show(successMessage, Info.Type.INFO);
                        closeTab("pa|home");
                        registerButtonsEnableDisableOption(true);
                    }
                });
    }

    private boolean validateWeightCount() {
        int errors = 0;
        String errorMessage = "";
        if (!competencyContainer.validateOverallWeight()) {
            errorMessage += assessmentMessages.summaryShouldBe(wfmStrings.competency().toLowerCase(), "100") + "</br>";
            competencyContainer.addErrorStyleWeights();
            errors++;
        }
        if (!goalContainer.validateOverallWeight()) {
            errorMessage += assessmentMessages.summaryShouldBe(wfmStrings.goal().toLowerCase(), "100") + "</br>";
            goalContainer.addErrorStyleWeights();
            errors++;
        }
        if (errorMessage.length() > 0) {
            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
        }

        return errors == 0;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
