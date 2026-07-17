package com.edatasite.workforce.gwt.assessment.client.ui.view.ui;

import com.edatasite.workforce.gwt.assessment.client.localization.AssessmentMessages;
import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.GoalSkillItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.InitiatedAssessmentItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElem;
import com.edatasite.workforce.gwt.assessment.client.rpc.TemplateItem;
import com.edatasite.workforce.gwt.assessment.client.ui.AssessmentHelper;
import com.edatasite.workforce.gwt.assessment.client.ui.view.AddTemplateView;
import com.edatasite.workforce.gwt.assessment.client.ui.view.ValidityPeriodsPopup;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelectCallback;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelectShell;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.ui.AddEmployeeView;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.ui.GoalAddEditView2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Ilhombek
 * Date: 2/22/12
 * Time: 2:14 PM
 */
public class AddInitiateSimpleAppraisalView extends FooteredView implements Constants, Colapse, FittedContent {

    private static final AssessmentMessages assessmentMessages = AssessmentMessages.App.get();
    private final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private Div competencyLinksHorizontalPanelDiv;
    private final WfmButton2 addCompetencyFromTemplateLink = new WfmButton2(hrmsStrings.addCompetencies());
    private final WfmButton2 addLastInitiatedCompetencyLink = new WfmButton2(hrmsStrings.addLastInitiatedCompetencies());
    private final WfmButton2 addPersonalGoalLink = new WfmButton2(hrmsStrings.addPersonalGoal());
    private final WfmButton2 addBusinessGoalLink = new WfmButton2(hrmsStrings.addBusinessGoal());
    private final WfmButton2 addProjectGoalLink = new WfmButton2(Property.get(Constants.PROJECT, hrmsStrings.addProjectGoal(), wfmStrings.project()));
    private final WfmButton2 addDepartmentGoalLink = new WfmButton2(Property.get(Constants.DEPARTMENT_LIST, hrmsStrings.addDepartmentGoal(), wfmStrings.department()));
    private EmployeeLookUpWithCode employeeNameBox;
    private EmployeeLookUp reviewerNameBox;
    private DataListBox templateNameBox;
    private DataListBox validityPeriodBox;
    private final HTML counterCompetencies = new HTML();
    private final HTML counterGoals = new HTML();
    private TextBox competencyPercentTEXT;
    private TextBox goalPercentTEXT;
    private List<WfmTreeItem> competencyItems;
    private GoalItem[] goalItems;
    private CompetencyListWidget competencyListWidget;
    private GoalListWidget goalListWidget;
    private final ArrayList<Integer> ratedCompetencyIds = new ArrayList<>();
    private final ArrayList<Integer> ratedGoalIds = new ArrayList<>();
    private AppraisalsSettingsItem settingsItem;
    private Integer int_employeeID;
    private Integer shiftItemId;
    private boolean isWeightTable = false;
    private boolean managerFirstAppraisal;
    private WfmButton2 initiateAndReview;
    private WfmButton2 submitAppraisal;
    private WfmButton2 submitAppraisalToManager;
    private String status;
    private FormGroup competencyWeightGroup;
    private FormGroup goalWeightGroup;
    private final String initiateSimpleView2 = "initiate_simple_view_2_";
    private int int_competencyPercentINT = 50;
    private int int_goalPercentINT = 50;
    private DatePicker assessmentDate;
    private InitiatedAssessmentItem shiftData;
    private boolean isFromShift = false;
    private SimpleSkillContainer2 competencyContainer;
    private SkillAssessmentElem[] competencyElements;

    private boolean hasPersonalGoalPermission = Utils.hasPermission(PermissionConstants.HRMS_PERSONAL_GOALS);
    private boolean hasBusinessGoalPermission = Utils.hasPermission(PermissionConstants.HRMS_BUSINESS_GOALS);
    private boolean hasProjectGoalPermission = Utils.hasPermission(PermissionConstants.HRMS_PROJECT_GOALS);
    private boolean hasDepartmentGoalPermission = Utils.hasPermission(PermissionConstants.HRMS_DEPARTMENT_GOALS);

    @UiField
    Span title;
    @UiField
    HTMLPanel employeeRowPanel;
    @UiField
    HTMLPanel templateRowPanel;
    @UiField
    HTMLPanel competencyGoalField;
    @UiField
    HTMLPanel assessmentDateBox;

    interface AddInitiateSimpleAppraisalViewUiBinder extends UiBinder<HTMLPanel, AddInitiateSimpleAppraisalView> {
    }

    public AddInitiateSimpleAppraisalView() {
        super("initiate", hrmsStrings.addCompetencies());
    }

    public AddInitiateSimpleAppraisalView(Integer empId) {
        super("initiate", hrmsStrings.addCompetencies());
        int_employeeID = empId;
    }

    public AddInitiateSimpleAppraisalView(Integer empId, Integer shiftItemId) {
        super("initiate", hrmsStrings.addCompetencies());
        int_employeeID = empId;
        this.shiftItemId = shiftItemId;
        isFromShift = true;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VALIDITY_PERIOD_CHANGED, AddInitiateSimpleAppraisalView.this, (sender, args) -> {
            final Integer validityPeriodId = Integer.parseInt(args.toString());
            AssessmentService.App.get().getValidityPeriods(ValidityPeriodItem.VALIDITY_PERIOD_APPRAISAL, new AsyncCallback<ValidityPeriodItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(ValidityPeriodItem[] validityPeriodItems) {
                    validityPeriodBox.clear();
                    validityPeriodBox.setItems(validityPeriodItems);
                    validityPeriodBox.setSelected(validityPeriodId);
                }
            });

        });
        AddInitiateSimpleAppraisalViewUiBinder ourUiBinder = GWT.create(AddInitiateSimpleAppraisalViewUiBinder.class);
        add(ourUiBinder.createAndBindUi(this));
        LoadingPanel.loading(true);
        AssessmentService.App.get().getAppraisalsSettings(new AbstractAsyncCallback<AppraisalsSettingsItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(AppraisalsSettingsItem result) {
                LoadingPanel.loading(false);
                settingsItem = result;
                drawInitialize();
            }
        });

        AssessmentService.App.get().getDataFromShift(shiftItemId,new AbstractAsyncCallback<InitiatedAssessmentItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(InitiatedAssessmentItem result) {
                shiftData = result;
            }
        });

        return null;
    }

    /**
     * Register competency and goal tab widgets
     *
     * @param widget - competencyGoalWidget
     */
    private void addFormWidgetCompetencyGoalField(Widget widget) {
        widget.getElement().getStyle().setMarginBottom(25, Style.Unit.PX);
        competencyGoalField.add(widget);
    }


    /**
     * initialize
     */
    private void drawInitialize() {

        addCompetencyFromTemplateLink.ensureDebugId(initiateSimpleView2 + "addCompetencyLink");
        addLastInitiatedCompetencyLink.ensureDebugId(initiateSimpleView2 + "addLastInitiatedCompetencyLink");

        addPersonalGoalLink.ensureDebugId(initiateSimpleView2 + "addPersonalGoalLink");
        addBusinessGoalLink.ensureDebugId(initiateSimpleView2 + "addBusinessGoalLink");
        addProjectGoalLink.ensureDebugId(initiateSimpleView2 + "addProjectGoalLink");
        addDepartmentGoalLink.ensureDebugId(initiateSimpleView2 + "addDepartmentGoalLink");

//        box_email.ensureDebugId(initiateSimpleView2 + "box_email");
//        box_firstName.ensureDebugId(initiateSimpleView2 + "box_firstName");
//        box_lastName.ensureDebugId(initiateSimpleView2 + "box_lastName");

        title.setText(wfmStrings.basicDetails());
        drawing();

        LoadingPanel.loading(true);
        AssessmentService.App.get().managersFirstAppraisal(new AbstractAsyncCallback<Boolean>() {
            @Override
            public void success(Boolean result) {
                LoadingPanel.loading(false);
                managerFirstAppraisal = result;
                //employee name and reviewer name register
                drawInitializeEmployeeFields();
                //appraisal template register
                drawInitializeTemplateFields();
                //assessment date
                drawAssessmentDateField();
                //competency and goal tab widgets register
                drawInitializeCompetencyGoalTabFields();
                //button panel register
                drawInitializeButtonsFields();
                //reload competency/goal tables
                renderCompetencies();
                if (hasPersonalGoalPermission || hasBusinessGoalPermission
                        || hasProjectGoalPermission || hasDepartmentGoalPermission) {
                    renderGoals();
                }
                getTemplateList();
            }
        });
        drawReInitFields();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TEMPLATE_ADD_DELETE, AddInitiateSimpleAppraisalView.this, (sender, args) -> getTemplateList());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GOAL_ADD, AddInitiateSimpleAppraisalView.this, (sender, args) -> {
            if (int_employeeID != null) {
                checkEmployeeGoalsList(int_employeeID);
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_ADD, AddInitiateSimpleAppraisalView.this, (sender, args) -> {
            SelectItem selectedItem = employeeNameBox.getSelectedItem();
            getEmployeeList();
            if (selectedItem != null) {
                employeeNameBox.setSelected(selectedItem);
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_COMPETENCY, AddInitiateSimpleAppraisalView.this, (sender, args) -> {
            //
            if (args instanceof WfmTreeItem[]) {
                WfmTreeItem[] target = (WfmTreeItem[]) args;
                competencyListWidget.getListItems().addAll(Arrays.asList(target));
                competencyItems = competencyListWidget.getListItems();
                for (WfmTreeItem wti : target) {
                    if (!ratedCompetencyIds.contains(wti.getId())) {
                        ratedCompetencyIds.add(wti.getId());
                    }
                }
                competencyListWidget.clearErrors();
                renderCompetencies();
            }
        });
        if (int_employeeID != null) {
            checkEmployeeGoalsList(int_employeeID);
            employeeNameBox.setSelected(int_employeeID);
        }

    }

    private void drawing() {
        //related employee
        employeeNameBox = new EmployeeLookUpWithCode();
        employeeNameBox.setContextCode(PermissionConstants.HRMS_CONTEXT);
        employeeNameBox.ensureDebugId(initiateSimpleView2 + "employeeNameBox");
        employeeNameBox.getSuggestBox().addSelectionHandler(event -> {
            int_employeeID = employeeNameBox.getSelectedItem().getId();
            templateNameBox.setEnabled(true);
            getTemplateList();
            competencyListWidget.getListItems().clear();
            checkIfEmployeeHasSavedCompetencies();
            checkEmployeeGoalsList(int_employeeID);
            getEmployeeReviewerList(int_employeeID);
            if (employeeNameBox.getSelectedItem() != null) {
                reviewerNameBox.setEnabled(!isFromShift);
                if (EMPLOYEE_STATUS_NO_ACCCESS.equals(employeeNameBox.getSelectedItem().getDescription())) {
                    submitAppraisal.setVisible(false);
                    submitAppraisalToManager.setVisible(reviewerNameBox.getSelectedItem() == null || !reviewerNameBox.getSelectedItem().getId().equals(Utils.getUserID()));
                } else {
                    submitAppraisal.setVisible(reviewerNameBox.getSelectedItem() == null || !reviewerNameBox.getSelectedItem().getId().equals(Utils.getUserID()));
                    submitAppraisalToManager.setVisible(false);
                }
            } else {
                reviewerNameBox.setEnabled(false);
            }
        });
        //related reviewer
        reviewerNameBox = new EmployeeLookUp(false, false, false);
        reviewerNameBox.ensureDebugId(initiateSimpleView2 + "reviewerNameBox");
//        reviewerNameBox.setAllowFirstItem(true);
        reviewerNameBox.addValueChangeHandler(event -> {
            //register other listener
            if (Utils.hasRole(ADMIN) || Utils.hasRole(DR) || Utils.hasRole(ADMIN_LOCATION) || Utils.hasRole(HR) || Utils.hasRole(TL)) {
                if (reviewerNameBox.getSelectedItem() != null && reviewerNameBox.getSelectedItem().getId() != null && !reviewerNameBox.getSelectedItem().getId().equals(Utils.getUserID())) {
                    initiateAndReview.setVisible(false);
                    submitAppraisal.setVisible(true);
                    if (employeeNameBox.getSelectedItem() != null && EMPLOYEE_STATUS_NO_ACCCESS.equals(employeeNameBox.getSelectedItem().getDescription())) {
                        submitAppraisalToManager.setVisible(true);
                    }
                } else {
                    submitAppraisal.setVisible(false);
                    initiateAndReview.setVisible(true);
                    submitAppraisalToManager.setVisible(false);
                }
            }
        });
        //Validity Period
        validityPeriodBox = new DataListBox();
        validityPeriodBox.ensureDebugId(initiateSimpleView2 + "validityPeriodBox");
        validityPeriodBox.setAllowFirstItem(true);
        validityPeriodBox.addValueChangeHandler(event -> {
            checkEmployeeGoalsList(int_employeeID);
        });

        //related template
        if (settingsItem.isUseCompetencies()) {
            templateNameBox = new DataListBox();
            templateNameBox.setEnabled(false);
            templateNameBox.ensureDebugId(initiateSimpleView2 + "templateNameBox");
            templateNameBox.setAllowFirstItem(true);
            templateNameBox.addValueChangeHandler(event -> {
                ratedCompetencyIds.clear();
                loadTemplates();
            });
            //competency and goal tab widgets
            competencyListWidget = new CompetencyListWidget(this);
            competencyListWidget.addStyleName("init-smpl-apprsl-competency-list-widget");
        }
        if (hasPersonalGoalPermission || hasBusinessGoalPermission
                || hasProjectGoalPermission || hasDepartmentGoalPermission) {
            goalListWidget = new GoalListWidget(this);
        }
        //related buttons
        submitAppraisal = new WfmButton2(hrmsStrings.initiateSend(), WfmButton2.BTN_PRIMARY);
        submitAppraisal.ensureDebugId(initiateSimpleView2 + "submitAppraisal");

        initiateAndReview = new WfmButton2(isFromShift ? wfmStrings.approve() : hrmsStrings.initiateReview(), WfmButton2.BTN_PRIMARY);
        initiateAndReview.ensureDebugId(initiateSimpleView2 + "initiateAndReview");

        submitAppraisalToManager = new WfmButton2(hrmsStrings.initiateAndSubmitToReviewer(), WfmButton2.BTN_PRIMARY);
        submitAppraisalToManager.ensureDebugId(initiateSimpleView2 + "submitAppraisalToManager");
        submitAppraisalToManager.setVisible(false);
    }

    private void drawInitializeButtonsFields() {
        //register something code!!!
        submitAppraisal.addClickHandler(event -> {
            //register save listener
            if (validate()) {
                save(true, true);
            }
        });
        initiateAndReview.addClickHandler(event -> {
            //register save listener
            if (validate()) {
                save(false, false);
            }
        });

        submitAppraisalToManager.addClickHandler(event -> {
            if (validate()) {
                status = REVIEWED_BY_EMPLOYEE;
                save(true, false);
            }
        });

        add(createFooter());
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return AddInitiateSimpleAppraisalView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return AddInitiateSimpleAppraisalView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> buttonList = new ArrayList<>();

        Div competencyLinkButtonWrapper = new Div();
        competencyLinkButtonWrapper.add(addCompetencyFromTemplateLink);

        MaterialLink addPersonalGoal = new MaterialLink(hrmsStrings.addPersonalGoal());
        addPersonalGoal.addClickHandler(event -> {
            String actionPersonal = employeeNameBox.getSelectedItem() != null ? "goal|add/add//" + PERSONAL_GOAL + "/" + employeeNameBox.getSelectedItem().getId() : "goal|add/add//" + PERSONAL_GOAL;
            SinksContainerFactory.entryPoint.onHistoryChanged(actionPersonal);
        });
        MaterialSplitButton splitButton = null;
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PERSONAL_GOALS)) {
            splitButton = new MaterialSplitButton(addPersonalGoal);
        }

        MaterialLink addBusinessGoal = new MaterialLink(hrmsStrings.addBusinessGoal());
        addBusinessGoal.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("busingoal|add/add//" + BUSINESS_GOAL));
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_BUSINESS_GOALS)) {
            if (splitButton != null) {
                splitButton.addItem(addBusinessGoal);
            } else {
                splitButton = new MaterialSplitButton(addBusinessGoal);
            }
        }

        MaterialLink addProjectGoal = new MaterialLink(Property.get(Constants.PROJECT, hrmsStrings.addProjectGoal(), wfmStrings.project()));
        addProjectGoal.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("projectgoal|add/add//" + PROJECT_GOAL));
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PROJECT_GOALS)) {
            if (splitButton != null) {
                splitButton.addItem(addProjectGoal);
            } else {
                splitButton = new MaterialSplitButton(addProjectGoal);
            }
        }

        MaterialLink addDepartmentGoal = new MaterialLink(Property.get(Constants.DEPARTMENT_LIST, hrmsStrings.addDepartmentGoal(), wfmStrings.department()));
        addDepartmentGoal.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("departmentgoal|add/add//" + DEPARTMENT_GOAL));
        Div goalLinkButtonWrapper = new Div();
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_DEPARTMENT_GOALS)) {
            if (splitButton != null) {
                splitButton.addItem(addDepartmentGoal);
            } else {
                splitButton = new MaterialSplitButton(addDepartmentGoal);
            }
        }
        if (splitButton != null) {
            goalLinkButtonWrapper.add(splitButton);
        }

        Div submitAppraisalWrapper = new Div();
        submitAppraisalWrapper.add(submitAppraisal);

        Div submitAppraisalToManagerWrapper = new Div();
        submitAppraisalToManagerWrapper.add(submitAppraisalToManager);

        Div initiateAndReviewWrapper = new Div();
        initiateAndReviewWrapper.add(initiateAndReview);

        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_COMPETENCES)) {
            buttonList.add(competencyLinkButtonWrapper);
        }

        if (splitButton != null) {
            buttonList.add(goalLinkButtonWrapper);
        }

        if (!isFromShift && (Utils.hasRole(ADMIN) || Utils.hasRole(DR) || Utils.hasRole(ADMIN_LOCATION) || Utils.hasRole(HR) || Utils.hasRole(TL))) {
            buttonList.add(submitAppraisalWrapper);
            buttonList.add(submitAppraisalToManagerWrapper);
        }
        buttonList.add(initiateAndReviewWrapper);

        return buttonList;
    }

    private List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    private void drawInitializeCompetencyGoalTabFields() {
        //include competency tab


        if (settingsItem.isUseCompetencies() && !isFromShift) {
            addFormWidgetCompetencyGoalField(competencyListWidget);
            counterCompetencies.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            addFormWidgetCompetencyGoalField(counterCompetencies);
        }
        //include goal tab
        if ((hasPersonalGoalPermission || hasBusinessGoalPermission
                || hasProjectGoalPermission || hasDepartmentGoalPermission) && !isFromShift) {
            addFormWidgetCompetencyGoalField(goalListWidget);
            counterGoals.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            addFormWidgetCompetencyGoalField(counterGoals);
        }
    }

    private void drawInitializeEmployeeFields() {
        if (Utils.hasPermission(PermissionConstants.HRMS_APPRAISAL_EMPLOYEE_LIST_BOX)) {

            FormGroup employeeGroup = new FormGroup(wfmStrings.employee(), employeeNameBox, true);
            FormGroup reviewerGroup = new FormGroup(wfmStrings.reviewer(), reviewerNameBox, true);

            if (shiftData != null) {
                int_employeeID = shiftData.getEmployee().getId();
                employeeNameBox.setSelected(shiftData.getEmployee());
                employeeNameBox.setEnabled(false);
                reviewerNameBox.setSelected(new SelectItem(Utils.getUserID(), Utils.getUserFullName()));
                reviewerNameBox.setEnabled(false);
                checkEmployeeGoalsList(int_employeeID);
                templateNameBox.setEnabled(true);
                getTemplateList();
                competencyListWidget.getListItems().clear();
                checkIfEmployeeHasSavedCompetencies();
                checkEmployeeGoalsList(int_employeeID);
                getEmployeeReviewerList(int_employeeID);

            }
            employeeRowPanel.add(new GColumn(GColumnEnum.COL_4, employeeGroup));
            employeeRowPanel.add(new GColumn(GColumnEnum.COL_4, reviewerGroup));
        } else {
            if (shiftData != null) {
                int_employeeID = shiftData.getEmployee().getId();
                employeeNameBox.setSelected(shiftData.getEmployee());
                checkEmployeeGoalsList(int_employeeID);
                getTemplateList();
                competencyListWidget.getListItems().clear();
                checkIfEmployeeHasSavedCompetencies();
                checkEmployeeGoalsList(int_employeeID);
                getEmployeeReviewerList(int_employeeID);
                reviewerNameBox.setSelected(new SelectItem(Utils.getUserID(), Utils.getUserName()));
                reviewerNameBox.setEnabled(false);
            } else {
                int_employeeID = Utils.getUserID();
                employeeNameBox.setSelected(int_employeeID);
            }
            employeeNameBox.setEnabled(false);
            FormGroup employeeGroup = new FormGroup(wfmStrings.employee(), employeeNameBox, true);
            FormGroup reviewerGroup = new FormGroup(wfmStrings.reviewer(), reviewerNameBox, true);

            employeeRowPanel.add(new GColumn(GColumnEnum.COL_4, employeeGroup));
            employeeRowPanel.add(new GColumn(GColumnEnum.COL_4, reviewerGroup));
        }
        AdvancedInputGroup validityPeriodPanel = new AdvancedInputGroup(validityPeriodBox);
        validityPeriodPanel.setAppender("ficon--plus");
        validityPeriodPanel.appenderClickHandler(() -> {
            ValidityPeriodItem item = new ValidityPeriodItem();
            HashSet<SelectItem> list = new HashSet<>();
            list.add(new SelectItem(0, "", ValidityPeriodItem.VALIDITY_PERIOD_APPRAISAL));
            item.setPeriodTypeItems(list);
            new ValidityPeriodsPopup(item);
        });
        FormGroup validityPeriodGroup = new FormGroup(wfmStrings.validityPeriod(), validityPeriodPanel, true);

        GColumnEnum columnEnum = null;
        if (managerFirstAppraisal) {
            columnEnum = GColumnEnum.COL_3;
        } else {
            columnEnum = GColumnEnum.COL_4;
        }
        employeeRowPanel.add(new GColumn(columnEnum, validityPeriodGroup));

        getValidityPeriodList();

        checkIfEmployeeHasSavedCompetencies();

        if (int_employeeID != null) {
            checkEmployeeGoalsList(int_employeeID);
            getEmployeeReviewerList(int_employeeID);
        }
        getEmployeeReviewerList(int_employeeID);
    }

    private void drawAssessmentDateField() {
        assessmentDate = new DatePicker();
        assessmentDate.ensureDebugId(initiateSimpleView2 + "datePicker");
        if (shiftData != null && shiftData.getAssessmentDate() != null) {
            assessmentDate.setDate(shiftData.getAssessmentDate());
            assessmentDate.setEnabled(false);
        }
        FormGroup assessmentDateGroup = new FormGroup(wfmStrings.date(), assessmentDate, true);
        assessmentDateBox.add(new GColumn(GColumnEnum.COL_4, assessmentDateGroup));
    }

    private void drawInitializeTemplateFields() {
        //appraisal template register
        if (settingsItem.isUseCompetencies()) {
            AdvancedInputGroup templatePanel = new AdvancedInputGroup(templateNameBox);
            templatePanel.setAppender("ficon--plus");
            templatePanel.appenderClickHandler(() -> {
                SinksContainerFactory.entryPoint.onHistoryChanged("addTemplate|add/");
            });
            FormGroup templateGroup = new FormGroup(wfmStrings.template(), templatePanel);
            templateRowPanel.add(new GColumn(GColumnEnum.COL_4, templateGroup));
        }

        competencyPercentTEXT = new TextBox();
        competencyPercentTEXT.setVisible(settingsItem.isUseCompetencies());
        competencyPercentTEXT.addBlurHandler(event -> changePercents(competencyPercentTEXT, goalPercentTEXT));
        competencyPercentTEXT.ensureDebugId(initiateSimpleView2 + "competencyPercentTEXT");
        competencyPercentTEXT.addKeyPressHandler(event -> {
            if (competencyPercentTEXT.getStyleName().contains("x-form-invalid")) {
                competencyPercentTEXT.removeStyleName("x-form-invalid");
            }
        });
        goalPercentTEXT = new TextBox();
        goalPercentTEXT.setVisible(hasPersonalGoalPermission || hasBusinessGoalPermission || hasProjectGoalPermission || hasDepartmentGoalPermission);
        goalPercentTEXT.ensureDebugId(initiateSimpleView2 + "goalPercentTEXT");
        goalPercentTEXT.addBlurHandler(event -> changePercents(goalPercentTEXT, competencyPercentTEXT));
        goalPercentTEXT.addKeyPressHandler(event -> {
            if (goalPercentTEXT.getStyleName().contains("x-form-invalid")) {
                goalPercentTEXT.removeStyleName("x-form-invalid");
            }
        });

        competencyWeightGroup = new FormGroup(hrmsStrings.competencyWeight(), competencyPercentTEXT, true);
        competencyWeightGroup.setVisible(false);

        goalWeightGroup = new FormGroup(hrmsStrings.goalWeight(), goalPercentTEXT, true);
        goalWeightGroup.setVisible(false);

        KpiRadioButton weightTableYES = new KpiRadioButton("weightTableRadio", wfmStrings.yes());
        if (isWeightTable) {
            weightTableYES.setValue(true);
        }
        weightTableYES.addClickHandler(event -> {
            isWeightTable = true;
            checkWeightPercentPanel();
            competencyWeightGroup.setVisible(true);
            goalWeightGroup.setVisible(true);
            changeCompetenciesAndGoals(isWeightTable);
        });
        //weightTable radio - NO
        KpiRadioButton weightTableNO = new KpiRadioButton("weightTableRadio", wfmStrings.no());
        if (!isWeightTable) {
            weightTableNO.setValue(true);
        }
        weightTableNO.addClickHandler(event -> {
            isWeightTable = false;
            if (settingsItem.isUseCompetencies()) {
                competencyPercentTEXT.setText("");
            }
            if (hasPersonalGoalPermission || hasBusinessGoalPermission || hasProjectGoalPermission || hasDepartmentGoalPermission) {
                goalPercentTEXT.setText("");
            }
            checkWeightPercentPanel();
            competencyWeightGroup.setVisible(false);
            goalWeightGroup.setVisible(false);
            changeCompetenciesAndGoals(isWeightTable);
        });
        Div hpPanelDivWeightTable = new Div("controlsLine");
        hpPanelDivWeightTable.add(weightTableYES);
        hpPanelDivWeightTable.add(weightTableNO);

        FormGroup wiightGroup = new FormGroup(hrmsStrings.weightable(), hpPanelDivWeightTable);
        templateRowPanel.add(new GColumn(GColumnEnum.COL_4, wiightGroup));

        InputGroup widgets = new InputGroup(competencyWeightGroup, goalWeightGroup);
        GColumn column1 = new GColumn(GColumnEnum.COL_6, competencyWeightGroup);
        GColumn column2 = new GColumn(GColumnEnum.COL_6, goalWeightGroup);
        GRow gRow = new GRow(column1, column2);
        templateRowPanel.add(new GColumn(GColumnEnum.COL_4, gRow));

        drawTemplateChooser(isWeightTable);
    }

    private void drawReInitFields() {
        if (getFromView() != null && AddEmployeeView.EMPLOYEE_VIEW.equals(getFromView())) {
            getEmployeeList();
        }
        if (getFromView() != null && GoalAddEditView2.ADD_GOAL_VIEW.equals(getFromView())) {
            if (int_employeeID != null) {
                checkEmployeeGoalsList(int_employeeID);
            }
        }
        if (getFromView() != null && AddTemplateView.ADD_TEMPLATE_VIEW.equals(getFromView())) {
            getTemplateList();
        }
        if (getFromView() == null) {
            getEmployeeList();
            getTemplateList();
        }
    }

    private void drawTemplateChooser(boolean weighTable) {
        if (settingsItem.isUseCompetencies()) {
//            addCompetencyLink.addClickHandler(event -> showCompetencyAddForm());

            addLastInitiatedCompetencyLink.addClickHandler(event -> getCompetencyListAsTableItem());

            addCompetencyFromTemplateLink.addClickHandler(event -> renderTemplateCompetency());
        }
        if (hasPersonalGoalPermission || hasBusinessGoalPermission || hasProjectGoalPermission || hasDepartmentGoalPermission) {
            addBusinessGoalLink.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("busingoal|add/add//" + BUSINESS_GOAL));

            addDepartmentGoalLink.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("departmentgoal|add/add//" + DEPARTMENT_GOAL));

            addProjectGoalLink.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("projectgoal|add/add//" + PROJECT_GOAL));

            addPersonalGoalLink.addClickHandler(event -> {
                String actionPersonal = employeeNameBox.getSelectedItem() != null ? "goal|add/add//" + PERSONAL_GOAL + "/" + employeeNameBox.getSelectedItem().getId() : "goal|add/add//" + PERSONAL_GOAL;
                SinksContainerFactory.entryPoint.onHistoryChanged(actionPersonal);
            });
        }
    }

    private void changePercents(TextBox percentTEXT1, TextBox percentTEXT2) {
        Validation.numberValidation(percentTEXT1);
        if (!"".equals(percentTEXT1.getText())) {
            int firstPercent = Integer.valueOf(percentTEXT1.getText());
            if (firstPercent > -1 && firstPercent < 101 && (!"".equals(competencyPercentTEXT.getText()))) {
                percentTEXT2.setText("" + (100 - firstPercent));
                if (percentTEXT2.getStyleName().contains("x-form-invalid")) {
                    percentTEXT2.removeStyleName("x-form-invalid");
                }
            } else {
                percentTEXT1.addStyleName("x-form-invalid");
            }
        }
    }

    private void changeCompetenciesAndGoals(boolean weightTable) {
        isWeightTable = weightTable;
        renderCompetencies();
        if (hasPersonalGoalPermission || hasBusinessGoalPermission
                || hasProjectGoalPermission || hasDepartmentGoalPermission) {
            renderGoals();
        }
        if (!isWeightTable) {
            counterCompetencies.setHTML("");
            counterGoals.setHTML("");
        }
    }

    private void checkIfEmployeeHasSavedCompetencies() {
        //register something code!!!
        if (settingsItem.isUseCompetencies()) {
            addLastInitiatedCompetencyLink.setVisible(false);
            LoadingPanel.loading(true);
            HrmsService.App.get().checkCompetencyList(int_employeeID, ASSESSMENT_SKILLS_SIMPLE, new AbstractAsyncCallback<Boolean>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(Boolean result) {
                    if (result) {
                        //register add last initiated competency link
                        addLastInitiatedCompetencyLink.setVisible(true);
                        competencyLinksHorizontalPanelDiv.add(addLastInitiatedCompetencyLink);
                        if (getSelectedTemplateBox() == null) {
                            getCompetencyListAsTableItem();
                        }
                        LoadingPanel.loading(false);
                    } else {
                        if (getSelectedTemplateBox() == null) {
                            competencyItems = null;
                            renderCompetencies();
                        }
                        LoadingPanel.loading(false);
                    }
                    recalculateWeights(true);
                }
            });
        }
    }

    private void checkEmployeeGoalsList(Integer employeeID) {
        //register something code!!!
        if ((hasPersonalGoalPermission || hasBusinessGoalPermission || hasProjectGoalPermission || hasDepartmentGoalPermission && employeeNameBox.getSelectedItem() != null) || employeeID != null) {
            Integer validityPeriodId = validityPeriodBox.getSelectedId();
            ListingFilterParameter parameter = new ListingFilterParameter();
            if (validityPeriodId != null) {
                parameter.setValidityPeriodId(validityPeriodId);
            }
            parameter.setEmployeeId(employeeID);
            HrmsService.App.get().getOwnEmployeeGoalList(parameter, new AbstractAsyncCallback<GoalItem[]>() {
                @Override
                public void success(GoalItem[] result) {
                    goalItems = result;
                    for (GoalItem gi : result) {
                        if (!ratedGoalIds.contains(gi.getObjectId())) {
                            ratedGoalIds.add(gi.getObjectId());
                        }
                    }
                    if (hasPersonalGoalPermission || hasBusinessGoalPermission
                            || hasProjectGoalPermission || hasDepartmentGoalPermission) {
                        renderGoals();
                    }
                    recalculateWeights(false);
                }
            });
        }
    }

    private void getCompetencyListAsTableItem() {
        LoadingPanel.loading(true);
        ratedCompetencyIds.clear();
        Scheduler.get().scheduleDeferred(() -> AssessmentService.App.get().getCompetencyListAsTableItem(getSelectedEmployeeBox(), ASSESSMENT_SKILLS_SIMPLE, new AbstractAsyncCallback<TemplateItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(TemplateItem result) {
                if (!"Empty".equals(result.getName())) {
                    competencyItems = result.getItems();
                    for (WfmTreeItem wfmTreeItem : result.getItems()) {
                        if (wfmTreeItem.isChecked() && !ratedCompetencyIds.contains(wfmTreeItem.getId())) {
                            ratedCompetencyIds.add(wfmTreeItem.getId());
                        }
                    }
                    if (result.getItems().size() > 0) {
                        competencyListWidget.clearErrors();
                    }
                    renderCompetencies();
                }
                LoadingPanel.loading(false);
            }
        }));
    }

    private void getEmployeeList() {
        AssessmentService.App.get().getEmployeesByReviewerId(null, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(final SelectItem[] result) {
                Scheduler.get().scheduleDeferred(() -> {
                    if (!Utils.hasRole(ADMIN) && !Utils.hasRole(DR) && !Utils.hasRole(ADMIN_LOCATION) && !Utils.hasRole(HR) && !Utils.hasRole(TL)) {
                        if (int_employeeID != null && int_employeeID.equals(Utils.getUserID())) {
                            employeeNameBox.setSelected(int_employeeID);
                        }
                    }
                    if (!isFromShift) {
                        reviewerNameBox.setEnabled(employeeNameBox.getSelectedItem() != null);
                    }
                });
            }
        });
    }

    private void getEmployeeReviewerList(final Integer employeeID) {
        if (!isFromShift) {
            reviewerNameBox.clear();
            if (employeeID != null && employeeID > 0) {
                AssessmentService.App.get().getReviewers(employeeID, new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void success(SelectItem[] result) {
                        reviewerNameBox.setItems(result);
                        if (!Utils.hasRole(ADMIN) && !Utils.hasRole(DR) && !Utils.hasRole(ADMIN_LOCATION) && !Utils.hasRole(HR) && !Utils.hasRole(TL)) {
                            if (employeeID.equals(Utils.getUserID())) {
                                employeeNameBox.setSelected(employeeID);
                            }
                        }
                        reviewerNameBox.setEnabled(employeeNameBox.getSelectedItem() != null);
                    }
                });
            }
        }
    }

    private void getValidityPeriodList() {
        AssessmentService.App.get().getValidityPeriods(ValidityPeriodItem.VALIDITY_PERIOD_APPRAISAL, new AbstractAsyncCallback<ValidityPeriodItem[]>() {
            @Override
            public void success(ValidityPeriodItem[] result) {
                validityPeriodBox.setItems(result);
                for (ValidityPeriodItem item : result) {
                    if (item.isDefault()) {
                        validityPeriodBox.setSelected(item);
                        break;
                    }
                }
            }
        });
    }

    private void getTemplateList() {
        if (settingsItem.isUseCompetencies()) {
            AssessmentService.App.get().getAssessmentTemplates(employeeNameBox.getSelectedItemID(),new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void success(SelectItem[] result) {
                    Integer selectedID = templateNameBox.getSelectedId();
                    templateNameBox.setItems(result);
                    if (selectedID != null && selectedID > 0) {
                        templateNameBox.setSelected(selectedID);
                    }
                }
            });
        }
    }

    /**
     * Related competencies link panel
     *
     * @return link - panel
     */
    private HorizontalPanelDiv getCompetencyLinksPanel() {
        HorizontalPanelDiv horizontalDiv = new HorizontalPanelDiv();
        horizontalDiv.getElement().getStyle().setProperty("display", "flex");
        horizontalDiv.getElement().getStyle().setProperty("justifyContent", "space-between");
        //register competencies word
        HTML competenciesHTML = getLabelTEXT(wfmStrings.competencies());
        horizontalDiv.add(competenciesHTML);
        competenciesHTML.getElement().getStyle().setProperty("display", "flex");
        competenciesHTML.getElement().getStyle().setProperty("alignItems", "center");

        competencyLinksHorizontalPanelDiv = new Div("btn-group");
        competencyLinksHorizontalPanelDiv.add(addCompetencyFromTemplateLink);
//        competencyLinksHorizontalPanelDiv.add(addCompetencyLink);
        horizontalDiv.add(competencyLinksHorizontalPanelDiv);

        return horizontalDiv;
    }

    /**
     * Related goals link panel
     *
     * @return link - panel
     */
    private HorizontalPanelDiv getGoalLinksPanel() {
        HorizontalPanelDiv linksPanel = new HorizontalPanelDiv();
        linksPanel.getElement().getStyle().setProperty("display", "flex");
        linksPanel.getElement().getStyle().setProperty("justifyContent", "space-between");
        linksPanel.getElement().getStyle().setMarginTop(30, Style.Unit.PX);
        //register assign goal word
        HTML assignedGoalHTML = getLabelTEXT(wfmStrings.assignedGoals());
        linksPanel.add(assignedGoalHTML);
        assignedGoalHTML.getElement().getStyle().setProperty("display", "flex");
        assignedGoalHTML.getElement().getStyle().setProperty("alignItems", "center");

        Div btnGroup = new Div("btn-group");
        //register personal goal link
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PERSONAL_GOALS)) {
            btnGroup.add(addPersonalGoalLink);
        }
        //register project goal link
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PROJECT_GOALS)) {
            btnGroup.add(addProjectGoalLink);
        }
        //register department goal link
        if (Utils.hasPermission(PermissionConstants.HRMS_DEPARTMENT_GOALS)) {
            btnGroup.add(addDepartmentGoalLink);
        }
        //register business goal link
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_BUSINESS_GOALS)) {
            btnGroup.add(addBusinessGoalLink);
        }
        linksPanel.add(btnGroup);
        return linksPanel;
    }

    private void ratableItems(boolean ratable) {
        List<WfmTreeItem> wfmTreeItems = competencyListWidget.getListItems();
        for (WfmTreeItem wfmTreeItem : wfmTreeItems) {
            if (ratable) {
                if (!ratedCompetencyIds.contains(wfmTreeItem.getId())) {
                    ratedCompetencyIds.add(wfmTreeItem.getId());
                }
            } else {
                ratedCompetencyIds.remove(wfmTreeItem.getId());
            }
        }
        List<GoalItem> goalItems1 = goalListWidget.getListItems();
        for (GoalItem goalItem : goalItems1) {
            if (ratable) {
                if (!ratedGoalIds.contains(goalItem.getObjectId())) {
                    ratedGoalIds.add(goalItem.getObjectId());
                }
            } else {
                ratedGoalIds.remove(goalItem.getObjectId());
            }
        }
    }

    private void checkWeightPercentPanel() {
        if (isWeightTable && settingsItem.isUseCompetencies() && hasPersonalGoalPermission || hasBusinessGoalPermission || hasProjectGoalPermission || hasDepartmentGoalPermission) {
            competencyWeightGroup.setVisible(true);
            goalWeightGroup.setVisible(true);
        } else if (isWeightTable) {
            if (settingsItem.isUseCompetencies()) {
                competencyPercentTEXT.setText("100");
            } else {
                goalPercentTEXT.setText("100");
            }
            competencyWeightGroup.setVisible(false);
            goalWeightGroup.setVisible(false);
        }
    }

    private Integer getSelectedEmployeeBox() {
        if (!managerFirstAppraisal) {
            return employeeNameBox.getSelectedItemID();
        } else {
            return int_employeeID;
        }
    }

    private Integer getSelectedTemplateBox() {
        return settingsItem.isUseCompetencies() ? templateNameBox.getSelectedId() : null;
    }

    private void registerEnableDisableOptionSaveButtons(boolean enable) {
        if (submitAppraisal != null) {
            submitAppraisal.setEnabled(enable);
        }
        if (initiateAndReview != null) {
            initiateAndReview.setEnabled(enable);
        }
        if (submitAppraisalToManager != null) {
            submitAppraisalToManager.setEnabled(enable);
        }
    }

    private void renderTemplateCompetency() {
        TreeSelectCallback treeSelectCallback = (parent, command) -> AssessmentService.App.get().getSkills(parent.getItem().getId(), new AbstractAsyncCallback<LinkedList<WfmTreeItem>>() {
            @Override
            public void failure(Throwable throwable) {
                command.execute();
            }

            @Override
            public void success(LinkedList<WfmTreeItem> result) {
                parent.addItems(result);
                command.execute();
            }
        });
        final TreeSelectShell selectShell = new TreeSelectShell(wfmStrings.skills(), treeSelectCallback);
//        selectShell.setSize(300, 370);
        selectShell.getTreeSelect().setSearchText(wfmStrings.skills());
        selectShell.getTreeSelect().hideAvailablityCheckBox();
        selectShell.getTreeSelect().getTickAll().setVisible(false);
        selectShell.getTreeSelect().getSearchPanel().setVisible(false);

        selectShell.addStyleName("skillsPopup");
        selectShell.getTreeSelect().getPanel().addStyleName("skillsPopupTreeSelectPanel");

        selectShell.setOnItemSelected(items -> {
            ArrayList<WfmTreeItem> wfmTreeItems = new ArrayList<>();
            for (WfmTreeItem wti : items) {
                if (wti.getId() != null && wti.getId() > 0) {
                    wfmTreeItems.add(wti);
                }
            }
            competencyListWidget.getListItems().addAll(wfmTreeItems);
            competencyItems = competencyListWidget.getListItems();
            for (WfmTreeItem wti : wfmTreeItems) {
                if (/*wti.isChecked() && */!ratedCompetencyIds.contains(wti.getId())) {
                    ratedCompetencyIds.add(wti.getId());
                }
            }
            competencyListWidget.clearErrors();
            renderCompetencies();
        });

        selectShell.open();
        LoadingPanel.loading(true);
        AssessmentService.App.get().getGroups(new AbstractAsyncCallback<LinkedList<WfmTreeItem>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(LinkedList<WfmTreeItem> result) {
                selectShell.addRootItems(result);
                LoadingPanel.loading(false);
            }
        });
    }

    /**
     * reload competencies table
     */
    private void renderCompetencies() {
        if (settingsItem.isUseCompetencies()) {
            competencyListWidget.drawInitialize(isWeightTable, competencyItems);
        }
    }

    /**
     * reload goals table
     */
    private void renderGoals() {
        if (hasPersonalGoalPermission || hasBusinessGoalPermission || hasProjectGoalPermission || hasDepartmentGoalPermission) {
            goalListWidget.drawInitialize(isWeightTable, goalItems);
        }
    }

    public void recalculateWeights(boolean isCompetency) {
        if (isWeightTable) {
            double counter = 0;
            if (isCompetency) {
                counterCompetencies.setHTML("");
                List<WfmTreeItem> listItems = competencyListWidget.getListItems();
                competencyItems = listItems;
                for (WfmTreeItem wfmTreeItem : listItems) {
                    if (wfmTreeItem.getDoubleValue() != null) {
                        counter += wfmTreeItem.getDoubleValue();
                    }
                }

                counterCompetencies.setHTML(hrmsStrings.overallCompetencyWeight() + ": <b " + (counter > 100 ? "style='color:red;'>" : "style='color:darkgreen;'>") + numberFormat.format(counter) + "</b>");
            } else {
                counterGoals.setHTML("");
                List<GoalItem> listItems = goalListWidget.getListItems();
                goalItems = listItems.toArray(new GoalItem[]{});
                for (GoalItem goalItem : listItems) {
                    counter += goalItem.getWeight();
                }
                counterGoals.setHTML(hrmsStrings.overallGoalWeight() + ": <b " + (counter > 100 ? "style='color:red;'>" : "style='color:darkgreen;'>") + numberFormat.format(counter) + "</b>");
            }
        }
    }

    private HTML getLabelTEXT(String labelText) {
        return new HTML("<span class=form-group__label>" + labelText + "</span>");
    }

    private void loadTemplates() {
        LoadingPanel.loading(true);
        Scheduler.get().scheduleDeferred(() -> AssessmentService.App.get().getTemplate(templateNameBox.getSelectedItem().getId(), new AbstractAsyncCallback<TemplateItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(TemplateItem result) {
                if (result == null) {
                    competencyItems = null;
                } else {
                    competencyItems = result.getItems();
                    for (WfmTreeItem wfmTreeItem : result.getItems()) {
                        if (wfmTreeItem.isChecked() && !ratedCompetencyIds.contains(wfmTreeItem.getId())) {
                            ratedCompetencyIds.add(wfmTreeItem.getId());
                        }
                    }
                    if (result.getItems().size() > 0) {
                        competencyListWidget.clearErrors();
                    }
                }
                if (isFromShift) {
                    drawSimpleSkilContainer(result);
                }
                renderCompetencies();
                LoadingPanel.loading(false);
            }
        }));

    }

    private void drawSimpleSkilContainer(TemplateItem templateItem) {
        competencyContainer = new SimpleSkillContainer2(false, true,true, settingsItem,templateItem);
        competencyContainer.addStyleName("assessmentSkillRateContainer");
        InitiatedAssessmentItem assessmentItem = new InitiatedAssessmentItem();
        assessmentItem.setEmployee(employeeNameBox.getSelectedItem());
        assessmentItem.setReviewer(reviewerNameBox.getSelectedItem());
        competencyContainer.initCompetencyByTemplate(templateItem,assessmentItem);

        addFormWidgetCompetencyGoalField(competencyContainer);
        counterCompetencies.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        addFormWidgetCompetencyGoalField(competencyContainer);
    }

    private void save(boolean goToReview, boolean sendEmailToEmployee) {
        if (isWeightTable) {
            try {
                int_competencyPercentINT = Integer.valueOf(competencyPercentTEXT.getText());
            } catch (NumberFormatException ignore) {
                int_competencyPercentINT = 0;
            }
            try {
                int_goalPercentINT = Integer.valueOf(goalPercentTEXT.getText());
            } catch (NumberFormatException ignore) {
                int_goalPercentINT = 0;
            }
        }
        InitiatedAssessmentItem assessmentItem = new InitiatedAssessmentItem();

        assessmentItem.setEmployeeId(int_employeeID);
        assessmentItem.setGoToReview(goToReview);
        assessmentItem.setSendEmailToEmployee(sendEmailToEmployee);
        assessmentItem.setStatus(status);
        assessmentItem.setAssessmentDate(assessmentDate.getDate());
        if (isFromShift) {
            assessmentItem.setFromShift(true);
            assessmentItem.setShiftItemId(shiftItemId);
            competencyElements = competencyContainer.getDataToSave(true);
            assessmentItem.setCompetencyElements(competencyElements);
        }

        ArrayList<GoalSkillItem> skillItems = new ArrayList<>();
        if (settingsItem.isUseCompetencies()) {
            List<WfmTreeItem> competencyListItems = competencyListWidget.getListItems();
            for (WfmTreeItem wfmTreeItem : competencyListItems) {
                GoalSkillItem skillItem = new GoalSkillItem();
                skillItem.setShowSlider(ratedCompetencyIds.contains(wfmTreeItem.getId()));
                skillItem.setObjectId(wfmTreeItem.getId());
                if (isWeightTable) {
                    skillItem.setWeight(wfmTreeItem.getDoubleValue());
                    skillItem.setGivenScore(wfmTreeItem.getGivenScore());
                }
                skillItems.add(skillItem);
            }
        }
        assessmentItem.setSkillItems(skillItems);
        ArrayList<GoalSkillItem> goalSkillItems = new ArrayList<>();
        if (hasPersonalGoalPermission || hasBusinessGoalPermission || hasProjectGoalPermission || hasDepartmentGoalPermission) {
            for (GoalItem goalItem : goalListWidget.getListItems()) {
                goalSkillItems.add(new GoalSkillItem(goalItem.getObjectId(), goalItem.getWeight(), goalItem.getGivenScore(), ratedGoalIds.contains(goalItem.getObjectId())));
            }
        }
        assessmentItem.setGoalItems(goalSkillItems);
        assessmentItem.setWeightTable(isWeightTable);
        assessmentItem.setGoToReview(!assessmentItem.isGoToReview());
        {
            saveAssessment(assessmentItem);
        }
    }


    private void saveAssessment(final InitiatedAssessmentItem assessmentItem) {
        assessmentItem.setGoalPercentINT(int_goalPercentINT);
        assessmentItem.setCompetencyPercentINT(int_competencyPercentINT);
        if (settingsItem.isUseCompetencies() && competencyListWidget.getListItems().size() == 0) {
            final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            wfmMessageBox.setTitle(wfmStrings.information());
            wfmMessageBox.setMessage(hrmsStrings.rememberCompetencies());
            wfmMessageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onCancel() {
                    assessmentItem.setSaveCompetencies(false);
                    save(assessmentItem);
                }

                @Override
                public void onSubmit() {
                    assessmentItem.setSaveCompetencies(true);
                    save(assessmentItem);
                }
            });
            wfmMessageBox.setWidth("300px");
            wfmMessageBox.open();
        } else {
            assessmentItem.setSaveCompetencies(false);
            save(assessmentItem);
        }
    }

    private void save(final InitiatedAssessmentItem assessmentItem) {

        Integer reviewerID = reviewerNameBox != null && reviewerNameBox.getSelectedItem() != null ? reviewerNameBox.getSelectedItem().getId() : null;
        Integer validityPeriodId = validityPeriodBox.getSelectedId();
        registerEnableDisableOptionSaveButtons(false);
        LoadingPanel.loading(true);
        assessmentItem.setDate(new Date());
        assessmentItem.setValidityPeriodId(validityPeriodId);
        assessmentItem.setReviewerId(reviewerID);
        assessmentItem.setTemplateID(getSelectedTemplateBox());

        AssessmentService.App.get().initiateAssessment(assessmentItem, new AbstractAsyncCallback<InitiatedAssessmentItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                registerEnableDisableOptionSaveButtons(true);
            }

            @Override
            public void success(final InitiatedAssessmentItem result) {
                LoadingPanel.loading(false);
                registerEnableDisableOptionSaveButtons(true);
                WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, true);
                wfmMessageBox.setTitle(wfmStrings.information());
                wfmMessageBox.setMessage(hrmsStrings.performanceAppraisalSaved());
                wfmMessageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SIMPLE_APPRAISAL_INITIATED, "", AddInitiateSimpleAppraisalView.this);
                        if (result.isGoToReview()) {
                            closeTab(AssessmentHelper.getReviewLinkForUI(result.getId(), "Reviewed"));
                        } else {
                            closeTab("pa|home");
                        }
                    }
                });
                wfmMessageBox.setWidth("300px");
                wfmMessageBox.open();
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        String errorMessage = "";
        if (Utils.hasRole(ADMIN)) {

            {
                if (employeeNameBox.getSelectedItem() == null) {
                    employeeNameBox.getSuggestBox().addStyleName("x-form-invalid");
                    errors++;
                }
                if (reviewerNameBox != null && reviewerNameBox.getSelectedItem() == null) {
                    reviewerNameBox.setStyleName("x-form-invalid");
                    errors++;
                }
            }
        } else {
            if (reviewerNameBox != null && reviewerNameBox.getSelectedItem() == null) {
                reviewerNameBox.setStyleName("x-form-invalid");
                errors++;
            }
        }
        if (isWeightTable) {
            if ((settingsItem.isUseCompetencies() && "".equals(competencyPercentTEXT.getText())) || (hasPersonalGoalPermission || hasBusinessGoalPermission || hasProjectGoalPermission || hasDepartmentGoalPermission && "".equals(goalPercentTEXT.getText()))) {
                competencyPercentTEXT.setStyleName("x-form-invalid");
                goalPercentTEXT.setStyleName("x-form-invalid");
                errors++;
                errorMessage += hrmsStrings.enterCompetencyOrGoalRatios() + "</br>";
            }
            double competencyWeight = 0;

            if (settingsItem.isUseCompetencies()) {
                for (WfmTreeItem wfmTreeItem : competencyListWidget.getListItems()) {
                    if (wfmTreeItem.getDoubleValue() != null) {
                        competencyWeight += wfmTreeItem.getDoubleValue();
                    }
                }
                if (BigDecimal.valueOf(competencyWeight).setScale(3, RoundingMode.HALF_UP).doubleValue() != 100) {
                    errorMessage += assessmentMessages.summaryShouldBe(wfmStrings.competency().toLowerCase(), "100") + "</br>";
                    competencyListWidget.addStyle("x-form-invalid");
                    errors++;
                } else {
                    competencyListWidget.clearErrors();
                }
            }
            if (hasPersonalGoalPermission || hasBusinessGoalPermission || hasProjectGoalPermission || hasDepartmentGoalPermission) {
                List<GoalItem> goalItemList = goalListWidget.getListItems();
                if (!goalItemList.isEmpty()) {
                    if ("".equals(goalPercentTEXT.getText()) && !"".equals(competencyPercentTEXT.getText())) {
                        errorMessage += hrmsStrings.removeGoalsOrGiveCGRatio() + "</br>";
                        errors++;
                    }
                    double goalWeight = 0;
                    for (GoalItem aGoalItemList : goalItemList) {
                        goalWeight += aGoalItemList.getWeight();
                    }
                    if (goalWeight != 100) {
                        errorMessage += assessmentMessages.summaryShouldBe(wfmStrings.goal().toLowerCase(), "100") + "</br>";
                        errors++;
                    }
                } else {
                    if (isWeightTable && !"100".equals(competencyPercentTEXT.getText())) {
                        competencyPercentTEXT.addStyleName("x-form-invalid");
                        errorMessage += hrmsStrings.correctCompetencyGoalWeight() + "</br>";
                        competencyPercentTEXT.setText("100");
                        goalPercentTEXT.setText("0");
                        errors++;
                    }
                }
            }
        }
        if (!errorMessage.isEmpty()) {
            Info.show(errorMessage, Info.Type.WARNING);
        } else {
            if (errors > 0) {
                Info.show(hrmsStrings.sureSelectAllRequiredData(), Info.Type.WARNING);
            }
        }

        return errors == 0;
    }


    public AppraisalsSettingsItem getSettingsItem() {
        return settingsItem;
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
