package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.ui.view.ValidityPeriodsPopup;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityServiceAsync;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.IconCell;
import com.edatasite.workforce.gwt.core.client.ui.components.form.*;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MeasurementsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.MaterialLink;

import java.util.*;


public class GoalAddEditView2 extends CustomForm2 implements HasLinksInterface, FormHasCustomFieldInterface, Constants, Colapse {

    public static String ADD_GOAL_VIEW = "ADD_GOAL_VIEW";
    public boolean saveAndClose = false;
    protected String viewName;
    protected String type;
    protected String dataListBoxString = "";
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    protected LinkedHashMap<KpiTreeInfo, GoalAssigneeItem> goalAssignees = new LinkedHashMap<>();
    protected LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assignees = new LinkedHashMap<>();
    protected Integer selectedEmployeeID = null;
    protected Integer relatedProjectID;
    protected Integer objectId;
    protected int folderType;
    protected GoalItem item;
    protected GeneralFileUpload attachment = null;
    protected DataListBox validityPeriod;
    protected boolean isProjectGoal = false;
    protected boolean isPersonGoal = false;
    protected boolean isBusinessGoal = false;
    private VerticalPanel addLinkAndLinks;
    private TextBox title;
    private TextBox target;
    private TextBox progress;
    private TextBox personalWeight;
    private TextBox personalAvailableWeight;
    // private TextBox measurementUnit;
    private MeasurementsLookUp measurementUnit;
    private TextBox actual;
    private TextArea2 description;
    private TextArea2 actionSteps;
    private String goalAddEditView = "goal_add_edit_view_";
    private static final AvailabilityServiceAsync availabilityService = AvailabilityService.App.get();
    private ProjectLookUp projectLookUp;
    private Numbering goalCode;
    private NoteWidget noteWidget;
    private LinkedHashMap<String, FormProperty> formProperty;
    private KpiCellTree dynamicSelector;
    private int goalCategoryID;
    private InputGroup personalWeightWidget;
    private HasLinks linkingUtil;
    private FormHasCustomField customFieldUtil;
    private final Integer MAX_LENGTH = 1000;
    private DatePicker startDate;
    private DatePicker endDate;
    private DataListBox status;
    private DataListBox scoreCalculation;
    private DataListBox resolver;
    private DataListBox personalBox;
    private DataListBox dataListBox;
    private DataListBox companyGoal;
    private boolean fromProject;
    private Date projectStartDate = null;
    private Date projectEndDate = null;
    private AdvancedInputGroup validityPeriodPanel;

    public GoalAddEditView2(String viewName, String description) {
        super(viewName, description);
    }

    public GoalAddEditView2(Integer objectId, String type) {
        super(type);
        this.objectId = objectId;
        this.type = type;

        if (PERSONAL_GOAL.equals(type)) {
            isPersonGoal = true;
            this.viewName = hrmsStrings.personalGoal();
            folderType = F_PERS_GOAL;
            goalAddEditView = "personal_goal_";
        } else if (PROJECT_GOAL.equals(type)) {
            isProjectGoal = true;
            this.viewName = Property.get(PROJECT_GOAL, hrmsStrings.projectgoal());
            folderType = F_PROJ_GOAL;
            goalAddEditView = "project_goal_";
        } else if (BUSINESS_GOAL.equals(type)) {
            isBusinessGoal = true;
            this.viewName = hrmsStrings.businessGoals();
            folderType = F_BUSS_GOAL;
            goalAddEditView = "business_goal_";
        }
    }

    public GoalAddEditView2(boolean fromProject, Integer relatedProjectID, String type) {
        super("addgoal", hrmsStrings.addGoal());
        this.fromProject = fromProject;
        this.relatedProjectID = relatedProjectID;
        this.type = type;
        if (PERSONAL_GOAL.equals(type)) {
            isPersonGoal = true;
            this.viewName = hrmsStrings.personalGoal();
            folderType = F_PERS_GOAL;
            goalAddEditView = "personal_goal_";
        } else if (PROJECT_GOAL.equals(type)) {
            isProjectGoal = true;
            this.viewName = Property.get(PROJECT_GOAL, hrmsStrings.projectgoal());
            folderType = F_PROJ_GOAL;
            goalAddEditView = "project_goal_";
        }
    }

    @Override
    protected String getFormID() {
        if (isPersonGoal) {
            return LayoutRPC.PERSONAL_GOAL_FORM;
        }
        if (isProjectGoal) {
            return LayoutRPC.PROJECT_GOAL_FORM;
        }
        if (isBusinessGoal) {
            return LayoutRPC.BUSINESS_GOAL_FORM;
        }
        return null;
    }

    @Override
    protected String getFormType() {
        return objectId == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected void addButtons() {
        MaterialLink save = new MaterialLink(wfmStrings.save());
        MaterialSplitButton splitButton = new MaterialSplitButton(save);
        save.addClickHandler(event -> {
            saveAndClose = true;
            save();
        });
        save.getElement().setId(goalAddEditView + "save_and_close_button");

        MaterialLink saveAndNewButton = new MaterialLink(wfmStrings.saveAndNew());
        saveAndNewButton.addClickHandler(event -> {
            saveAndClose = false;
            save();
        });
        saveAndNewButton.getElement().setId(goalAddEditView + "save_and_new_button");

        splitButton.addItem(saveAndNewButton);
        addButton(splitButton);

    }

    public void onShellOk() {
        if (saveAndClose) {
            closeTab();
            if (objectId != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("goal|summary/" + objectId + "/" + type, item.getTitle());
            }
        } else {
            reinit();
        }
    }

    public void reinit() {
        objectId = null;
        initForm();
        initialize();
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        ViewName viewNameConst = isPersonGoal ? ViewName.PersonalGoal : isProjectGoal ? ViewName.ProjectGoal : ViewName.BusinessGoal;
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(viewNameConst, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formProperty = result.getFormPropertyMap();
                initialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {

    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected String getWikiCode() {
        return null; //To change body of implemented methods use File | Settings | File Templates.
    }

    private final String successMessage = Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.goal());

    private void getValidityPeriodItems(Integer validityPeriodId) {
        AssessmentService.App.get().getValidityPeriods(ValidityPeriodItem.VALIDITY_PERIOD_GOAL, new AsyncCallback<ValidityPeriodItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(ValidityPeriodItem[] validityPeriodItems) {
                validityPeriod.clear();
                validityPeriod.setItems(validityPeriodItems);
                if (validityPeriodId != null) {
                    validityPeriod.setSelected(validityPeriodId);
                }
            }
        });
    }

    public void addFieldsToForm() {
        addTitleField(CustomFormConstants.GOAL_DETAILS, wfmStrings.basicDetails());
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());

        FormProperty goalNumberProperties = formProperty.get(CustomFormConstants.GOAL_NUMBER);
        if (formProperty != null && goalNumberProperties != null) {
            addField(GOAL_NUMBER, goalCode, getTitle(goalNumberProperties.isChanged() ? goalNumberProperties.getTitle() : wfmStrings.number(), goalNumberProperties.isRequired()), false);
            goalCode.setEnabled(!goalNumberProperties.isDisabled());
        } else {
            addField(CustomFormConstants.GOAL_NUMBER, goalCode, getTitle(wfmStrings.number()));
        }

        if (formProperty != null && formProperty.get(PROJECT_GOAL_LOOKUP) != null) {
            addField(PROJECT_GOAL_LOOKUP, projectLookUp, getTitle(formProperty.get(PROJECT_GOAL_LOOKUP).isChanged() ? formProperty.get(PROJECT_GOAL_LOOKUP).getTitle() : hrmsStrings.projectgoal(), formProperty.get(PROJECT_GOAL_LOOKUP).isRequired()));
        } else {
            addField(PROJECT_GOAL_LOOKUP, projectLookUp, getTitle(Property.get(Constants.PROJECT_GOAL, hrmsStrings.projectgoal())));
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_PERSONAL_ASSINESS) != null) {
            addField(GOAL_PERSONAL_ASSINESS, personalBox, getTitle(formProperty.get(CustomFormConstants.GOAL_PERSONAL_ASSINESS).isChanged() ? formProperty.get(CustomFormConstants.GOAL_PERSONAL_ASSINESS).getTitle() : wfmStrings.assignee(),
                            formProperty.get(CustomFormConstants.GOAL_PERSONAL_ASSINESS).isRequired()), false,
                    formProperty.get(CustomFormConstants.GOAL_PERSONAL_ASSINESS).isInformation());
            if (formProperty.get(CustomFormConstants.GOAL_PERSONAL_ASSINESS).isInformation()) {
                new KpiToolTip(personalBox, formProperty.get(CustomFormConstants.GOAL_PERSONAL_ASSINESS).getInformationText());
            }
        } else {
            addField(CustomFormConstants.GOAL_PERSONAL_ASSINESS, personalBox, getTitle(wfmStrings.assignee(), true));
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_TITLE) != null) {
            addField(GOAL_TITLE, title, getTitle(formProperty.get(CustomFormConstants.GOAL_TITLE).isChanged() ? formProperty.get(CustomFormConstants.GOAL_TITLE).getTitle() : wfmStrings.title(),
                            formProperty.get(CustomFormConstants.GOAL_TITLE).isRequired()), false,
                    formProperty.get(CustomFormConstants.GOAL_TITLE).isInformation());
            title.setEnabled(!formProperty.get(CustomFormConstants.GOAL_TITLE).isDisabled());
            if (formProperty.get(CustomFormConstants.GOAL_TITLE).isInformation()) {
                new KpiToolTip(title, formProperty.get(CustomFormConstants.GOAL_TITLE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.GOAL_TITLE, title, getTitle(wfmStrings.title(), true));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_DESCRIPTION) != null) {
            description = new TextArea2(1000, formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).isChanged() ? formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).getTitle() : wfmStrings.description());
            description.setEnabled(!formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).isDisabled());
        } else {
            description = new TextArea2(MAX_LENGTH, wfmStrings.description());
        }
        description.addStyleName("GoalAddEditView2-description");
        description.getTextArea().getElement().setId(goalAddEditView + "description");
        addField(CustomFormConstants.GOAL_DESCRIPTION, description, null);

        GColumn column1 = new GColumn(GColumnEnum.COL_6, startDate);
        GColumn column2 = new GColumn(GColumnEnum.COL_6, endDate);
        GRow gRow = new GRow(column1, column2);
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_START_DATE) != null) {
            addField(GOAL_START_DATE, gRow, getTitle(formProperty.get(CustomFormConstants.GOAL_START_DATE).isChanged() ? formProperty.get(CustomFormConstants.GOAL_START_DATE).getTitle() : wfmStrings.period(),
                            formProperty.get(CustomFormConstants.GOAL_START_DATE).isRequired()), false,
                    formProperty.get(CustomFormConstants.GOAL_START_DATE).isInformation());
            if (formProperty.get(CustomFormConstants.GOAL_START_DATE).isInformation()) {
                new KpiToolTip(gRow, formProperty.get(CustomFormConstants.GOAL_START_DATE).getInformationText());
            }
            startDate.setEnabled(!formProperty.get(CustomFormConstants.GOAL_START_DATE).isDisabled());
            endDate.setEnabled(!formProperty.get(CustomFormConstants.GOAL_START_DATE).isDisabled());
        } else {
            addField(CustomFormConstants.GOAL_START_DATE, gRow, getTitle(wfmStrings.period(), true));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_PROORDEP) != null) {
            addField(GOAL_PROORDEP, dataListBox, getTitle(formProperty.get(CustomFormConstants.GOAL_PROORDEP).isChanged() ? formProperty.get(CustomFormConstants.GOAL_PROORDEP).getTitle() : dataListBoxString,
                            formProperty.get(CustomFormConstants.GOAL_PROORDEP).isRequired()), false,
                    formProperty.get(CustomFormConstants.GOAL_PROORDEP).isInformation());
            dataListBox.setEnabled(!formProperty.get(CustomFormConstants.GOAL_PROORDEP).isDisabled());
            if (formProperty.get(CustomFormConstants.GOAL_PROORDEP).isInformation()) {
                new KpiToolTip(dataListBox, formProperty.get(CustomFormConstants.GOAL_PROORDEP).getInformationText());
            }
        } else {
            addField(CustomFormConstants.GOAL_PROORDEP, dataListBox, getTitle(dataListBoxString, true));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.COMPANY_GOAL) != null) {
            addField(CustomFormConstants.COMPANY_GOAL, companyGoal, getTitle(formProperty.get(CustomFormConstants.COMPANY_GOAL).isChanged() ? formProperty.get(CustomFormConstants.COMPANY_GOAL).getTitle() : hrmsStrings.companyGoal(),
                            formProperty.get(CustomFormConstants.COMPANY_GOAL).isRequired()), false,
                    formProperty.get(CustomFormConstants.COMPANY_GOAL).isInformation());
            companyGoal.setEnabled(!formProperty.get(CustomFormConstants.COMPANY_GOAL).isDisabled());
            if (formProperty.get(CustomFormConstants.COMPANY_GOAL).isInformation()) {
                new KpiToolTip(companyGoal, formProperty.get(CustomFormConstants.COMPANY_GOAL).getInformationText());
            }
        } else {
            addField(CustomFormConstants.COMPANY_GOAL, companyGoal, getTitle(hrmsStrings.companyGoal()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_ACTION_STEPS) != null) {
            actionSteps = new TextArea2(MAX_LENGTH, formProperty.get(CustomFormConstants.GOAL_ACTION_STEPS).isChanged() ? formProperty.get(CustomFormConstants.GOAL_ACTION_STEPS).getTitle() : wfmStrings.actionSteps());
            actionSteps.setEnabled(!formProperty.get(CustomFormConstants.GOAL_ACTION_STEPS).isDisabled());
        } else {
            actionSteps = new TextArea2(MAX_LENGTH, wfmStrings.actionSteps());
        }
        actionSteps.addStyleName("GoalAddEditView2-actionSteps");
        actionSteps.getTextArea().getElement().setId(goalAddEditView + "actionSteps");
        addField(CustomFormConstants.GOAL_ACTION_STEPS, actionSteps, null);
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_RESOLVER) != null) {
            addField(GOAL_RESOLVER, resolver, getTitle(formProperty.get(CustomFormConstants.GOAL_RESOLVER).isChanged() ? formProperty.get(CustomFormConstants.GOAL_RESOLVER).getTitle() : wfmStrings.manager(),
                            formProperty.get(CustomFormConstants.GOAL_RESOLVER).isRequired()), false,
                    formProperty.get(CustomFormConstants.GOAL_RESOLVER).isInformation());
            if (formProperty.get(CustomFormConstants.GOAL_RESOLVER).isInformation()) {
                new KpiToolTip(resolver, formProperty.get(CustomFormConstants.GOAL_RESOLVER).getInformationText());
            }
            resolver.setEnabled(!formProperty.get(CustomFormConstants.GOAL_RESOLVER).isDisabled());
        } else {
            addField(CustomFormConstants.GOAL_RESOLVER, resolver, getTitle(wfmStrings.manager()));
        }
        addField(CustomFormConstants.GOAL_PROGRESS, progress, getTitle(wfmStrings.progress()));//title progresga o'zgarishi kerak
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_STATUS) != null) {
            addField(GOAL_STATUS, status, getTitle(formProperty.get(CustomFormConstants.GOAL_STATUS).isChanged() ? formProperty.get(CustomFormConstants.GOAL_STATUS).getTitle() : wfmStrings.status(),
                            formProperty.get(CustomFormConstants.GOAL_STATUS).isRequired()), false,
                    formProperty.get(CustomFormConstants.GOAL_STATUS).isInformation());
            if (formProperty.get(CustomFormConstants.GOAL_STATUS).isInformation()) {
                new KpiToolTip(status, formProperty.get(CustomFormConstants.GOAL_STATUS).getInformationText());
            }
            status.setEnabled(!formProperty.get(CustomFormConstants.GOAL_STATUS).isDisabled());
        } else {
            addField(CustomFormConstants.GOAL_STATUS, status, getTitle(wfmStrings.status(), true));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_WEIGHT) != null) {
            addField(GOAL_WEIGHT, personalWeightWidget, getTitle(formProperty.get(CustomFormConstants.GOAL_WEIGHT).isChanged() ? formProperty.get(CustomFormConstants.GOAL_WEIGHT).getTitle() : wfmStrings.status(), formProperty.get(CustomFormConstants.GOAL_WEIGHT).isRequired()),
                    false, formProperty.get(CustomFormConstants.GOAL_WEIGHT).isInformation());
            if (formProperty.get(CustomFormConstants.GOAL_WEIGHT).isRequired()) {
                new KpiToolTip(personalWeightWidget, formProperty.get(CustomFormConstants.GOAL_WEIGHT).getInformationText());
            }
        } else {
            addField(CustomFormConstants.GOAL_WEIGHT, personalWeightWidget, getTitle(wfmStrings.weight()));
        }

        addTitleField(CustomFormConstants.ASSIGNEES, getTitle(wfmStrings.assignees(), true));
        addField(CustomFormConstants.GOAL_ASSIGNEES, dynamicSelector, getTitle(wfmStrings.assignees(), true));
        addField(CustomFormConstants.ATTACHMENTS, attachment, null);
        addTitleField(CustomFormConstants.ATTACHMENTS_TITLE, wfmStrings.attachments());
        addTitleField(CustomFormConstants.NOTES, wfmStrings.notes());
        addField(CustomFormConstants.CRM_NOTE, noteWidget, null);
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD) != null) {
            addField(GOAL_VALIDITY_PERIOD, validityPeriodPanel, getTitle(formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).isChanged() ? formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).getTitle() : wfmStrings.validityPeriod(),
                            formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).isRequired()), false,
                    formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).isInformation());
            if (formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).isInformation()) {
                new KpiToolTip(validityPeriodPanel, formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).getInformationText());
            }
            validityPeriod.setEnabled(!formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).isDisabled());
        } else {
            addField(CustomFormConstants.GOAL_VALIDITY_PERIOD, validityPeriodPanel, getTitle(wfmStrings.validityPeriod()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_MEASUREMENT_UNIT) != null) {
            addField(GOAL_MEASUREMENT_UNIT, measurementUnit, getTitle(formProperty.get(CustomFormConstants.GOAL_MEASUREMENT_UNIT).isChanged() ? formProperty.get(CustomFormConstants.GOAL_MEASUREMENT_UNIT).getTitle() : wfmStrings.measurementUnit(),
                            formProperty.get(CustomFormConstants.GOAL_MEASUREMENT_UNIT).isRequired()), false,
                    formProperty.get(CustomFormConstants.GOAL_MEASUREMENT_UNIT).isInformation());
            if (formProperty.get(CustomFormConstants.GOAL_MEASUREMENT_UNIT).isInformation()) {
                new KpiToolTip(measurementUnit, formProperty.get(CustomFormConstants.GOAL_MEASUREMENT_UNIT).getInformationText());
            }
            measurementUnit.setEnabled(!formProperty.get(CustomFormConstants.GOAL_MEASUREMENT_UNIT).isDisabled());
        } else {
            addField(CustomFormConstants.GOAL_MEASUREMENT_UNIT, measurementUnit, getTitle(wfmStrings.measurementUnit()));
        }
        if (Utils.showScoreCalculation()) {
            if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_SCORE_CALCULATION) != null) {
                addField(GOAL_SCORE_CALCULATION, scoreCalculation, getTitle(formProperty.get(CustomFormConstants.GOAL_SCORE_CALCULATION).isChanged() ? formProperty.get(CustomFormConstants.GOAL_SCORE_CALCULATION).getTitle() : wfmStrings.scoreCalculation()), false,
                        formProperty.get(CustomFormConstants.GOAL_SCORE_CALCULATION).isInformation());
                scoreCalculation.setEnabled(!formProperty.get(CustomFormConstants.GOAL_SCORE_CALCULATION).isDisabled());
                if (formProperty.get(CustomFormConstants.GOAL_SCORE_CALCULATION).isInformation()) {
                    new KpiToolTip(scoreCalculation, formProperty.get(CustomFormConstants.GOAL_SCORE_CALCULATION).getInformationText());
                }
            } else {
                addField(CustomFormConstants.GOAL_SCORE_CALCULATION, scoreCalculation, getTitle(wfmStrings.scoreCalculation()));
            }
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_TARGET) != null) {
            addField(GOAL_TARGET, target, getTitle(formProperty.get(CustomFormConstants.GOAL_TARGET).isChanged() ? formProperty.get(CustomFormConstants.GOAL_TARGET).getTitle() : wfmStrings.target(),
                            formProperty.get(CustomFormConstants.GOAL_TARGET).isRequired()), false,
                    formProperty.get(CustomFormConstants.GOAL_TARGET).isInformation());
            target.setEnabled(!formProperty.get(CustomFormConstants.GOAL_TARGET).isDisabled());
            if (formProperty.get(CustomFormConstants.GOAL_TARGET).isInformation()) {
                new KpiToolTip(target, formProperty.get(CustomFormConstants.GOAL_TARGET).getInformationText());
            }
        } else {
            addField(CustomFormConstants.GOAL_TARGET, target, getTitle(wfmStrings.target()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_ACTUAL) != null) {
            addField(GOAL_ACTUAL, actual, getTitle(formProperty.get(CustomFormConstants.GOAL_ACTUAL).isChanged() ? formProperty.get(CustomFormConstants.GOAL_ACTUAL).getTitle() : wfmStrings.actual(),
                            formProperty.get(CustomFormConstants.GOAL_ACTUAL).isRequired()), false,
                    formProperty.get(CustomFormConstants.GOAL_ACTUAL).isInformation());
            actual.setEnabled(!formProperty.get(CustomFormConstants.GOAL_ACTUAL).isDisabled());
            if (formProperty.get(CustomFormConstants.GOAL_TARGET).isInformation()) {
                new KpiToolTip(actual, formProperty.get(CustomFormConstants.GOAL_ACTUAL).getInformationText());
            }
        } else {
            addField(CustomFormConstants.GOAL_ACTUAL, actual, getTitle(wfmStrings.actual()));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_GOAL_LINKS)) {
            addTitleField(LINKS2, wfmStrings.links());
            showSection(LINKS2);
            addField(CustomFormConstants.LINKS, addLinkAndLinks, null);
        } else {
            hideSection(LINKS2);
        }
        show();
    }

    @Override
    protected void getDataToFillFields() {
        HrmsService.App.get().editGoal(objectId, type, new AbstractAsyncCallback<GoalItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                LoadingPanel.loading(false);
            }

            public void success(final GoalItem o) {
                LoadingPanel.loading(false);
                item = o;
                LoadingPanel.loading(false);
                fillFieldWithValue();
                getEmployees();
                if (objectId == null) {
                    setDefaultValuesByFormProperty();
                }
            }
        });
    }

    public void fillFieldWithValue() {

        if (item.getGoalNumber() != null) {
            goalCode.setNumberData(item.getGoalNumber());
            if (objectId != null) {
                goalCode.getTxtNumber().removeFromParent();
            }
        }
        projectLookUp.setItems(null, item.getProjectGoals());
        if (item.getSelectedProjectGoalId() != null) {
            projectLookUp.setSelected(item.getSelectedProjectGoalId());
        } else if (relatedProjectID != null && Constants.PERSONAL_GOAL.equals(type)) {
            projectLookUp.setSelected(relatedProjectID);
        }
        title.setText(item.getTitle());

        description.setText(item.getDescription());

        if (item.getFromDate() != null)
            startDate.setDate(item.getFromDate().getNonConvertedDate());

        if (item.getToDate() != null)
            endDate.setDate(item.getToDate().getNonConvertedDate());

        actionSteps.setText(item.getActionSteps());

        scoreCalculation.setAllowFirstItem(false);
        scoreCalculation.setWithoutNullLabel(true);
        scoreCalculation.setItems(item.getScores());

        if (item.getScore() != null) {
            scoreCalculation.setSelected(item.getScore());
        }

        measurementUnit.setSelected(item.getMeasurementUnit());
        if (item.getProgress() != null) {
            progress.setText(item.getProgress().toString());
        }

        companyGoal.setItems(item.getCompanyGoals());
        if (item.getCompanyGoalId() != null) {
            companyGoal.setSelected(item.getCompanyGoalId());
        }

        if (isProjectGoal) {
            dataListBox.setItems(Utils.sortSelectItemByName(item.getProjects()));
            if (item.getProjectId() != null) {
                dataListBox.setSelected(item.getProjectId());
            } else if (relatedProjectID != null && fromProject) {
                dataListBox.setSelected(relatedProjectID);
            }
        }

        status.setItems(Utils.sortSelectItemByName(item.getStatuss()));
        if (item.getStatusId() != null) {
            status.setSelected(item.getStatusId());
        } else {
            for (int i = 0; i < item.getStatuss().length; i++) {
                if (item.getStatuss()[i].getName().equals(wfmStrings.notStarted())) {
                    status.setSelected(item.getStatuss()[i].getId());
                }
            }
        }

        if (isPersonGoal) {
            selectedEmployeeID = item.getSelectedEmployeeID();
            goalCategoryID = item.getPersonalGoalId();
        }else if (isProjectGoal) {
            goalCategoryID = item.getProjectGoalId();
        } else if (isBusinessGoal) {
            goalCategoryID = item.getBusinGoalId();
        }

        validityPeriod.setItems(item.getValidityPeriodItems());
        validityPeriod.setSelected(item.getValidityPeriodItem());
        if (item.getObjectId() == null) {
            if (item.getValidityPeriodItem() != null) {
                Date date = item.getValidityPeriodItem().getFromDate();
                date = DateUtil.addDays(date, 1);
                startDate.setDate(date);
            }
        }
        if (item.getProjectStartDate() != null) {
            projectStartDate = item.getProjectStartDate().getNonConvertedDate();
        }
        if (item.getProjectEndDate() != null) {
            projectEndDate = item.getProjectEndDate().getNonConvertedDate();
        }
        if (!isPersonGoal) {
            EmployeeService.App.get().getCompanyEmployeesAsSelectItems(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void success(SelectItem[] result) {
                    resolver.setItems(result);
                    if (item.getResolverId() != null) {
                        resolver.setSelected(item.getResolverId());
                    }
                }
            });
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_GOAL_LINKS)) {
            getLinkingUtil().getTaggingView().setFromName(item.getTitle());
            getLinkingUtil().getTaggingView().setSelectedRelations(item.getRelations());
            getLinkingUtil().drawLinks();
        }
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields());
    }

    @Override
    public HasLinks getLinkingUtil() {

        if (linkingUtil == null) {
            linkingUtil = new HasLinks(GoalAddEditView2.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;

                }

                @Override
                protected Integer getRelationID() {
                    return objectId;
                }

                @Override
                protected String getRelationType() {
                    return isPersonGoal ? RelationItem.TYPE_PERSONAL_GOAL : isProjectGoal ? RelationItem.TYPE_PROJECT_GOAL : RelationItem.TYPE_BUSINESS_GOAL;
                }

                @Override
                protected String getRelationName() {
                    return item != null ? item.getTitle() : null;
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    private void setPersonalGoal(GoalAssigneeItem[] assigneeItems) {
        if (personalBox != null) {
            personalBox.setItems(assigneeItems);
            if (selectedEmployeeID != null) {
                personalBox.setSelected(selectedEmployeeID);
                if (personalBox.isSomethingSelected()) {
                    GoalAssigneeItem assigneeItem = (GoalAssigneeItem) personalBox.getSelectedItem();
                    personalWeight.setText(String.valueOf(assigneeItem.getWeight()));
                    personalAvailableWeight.setText(String.valueOf(assigneeItem.getAvaWeight()));
                    target.setText(String.valueOf(assigneeItem.getTarget()));
                    actual.setText(String.valueOf(assigneeItem.getActual()));
                }
            } else {
                personalBox.setSelected(Utils.getUserID());
            }
        }
    }

    private void projectChange() {
        if (dataListBox.getSelectedIndex() == 0) {
            dynamicSelector.clear();
        }
        getEmployees();
    }

    private void getEmployees() {
        ListingFilterParameter parameter = new ListingFilterParameter();
        if (dataListBox != null && dataListBox.getSelectedItem() != null) {
            if (isProjectGoal && dataListBox.isSomethingSelected()) {
                parameter.setProjectId(dataListBox.getSelectedItem().getId());
            }
        }

        if (validityPeriod != null && validityPeriod.getSelectedItem() != null) {
            parameter.setValidityPeriodId(validityPeriod.getSelectedItem().getId());
        }
        parameter.setResignedEmployeesIncluded(false);
        parameter.setObjectId(objectId);
        HrmsService.App.get().getEmployees(parameter, new AbstractAsyncCallback<GoalAssigneeItem[]>() {
            public void success(GoalAssigneeItem[] result) {
                setAssigneeMembers(result);
            }
        });
    }

    public void setAssigneeMembers(GoalAssigneeItem[] members) {
        if (members == null) {
            return;
        }
        if (isPersonGoal) {
            setPersonalGoal(members);
        } else {
            assignees.clear();
            goalAssignees.clear();
            Map<Integer, List<GoalAssigneeItem>> teams = new HashMap<>();
            for (GoalAssigneeItem member1 : members) {
                List<GoalAssigneeItem> teamMembers = teams.computeIfAbsent(member1.getDepartmentId(), k -> new ArrayList<>());
                teamMembers.add(member1);
            }

            for (Integer teamById : teams.keySet()) {
                List<GoalAssigneeItem> mm = teams.get(teamById);
                ArrayList<KpiTreeInfo> kpiTreeInfoList = new ArrayList<>();
                String tmpItem = null;
                for (GoalAssigneeItem item : mm) {
                    tmpItem = item.getDepartmentName();
                    KpiTreeInfo info = new KpiTreeInfo(item.getId(), item.getName());
                    info.setSelected(item.isAssignee());
                    info.setEmployeeId(item.getId());
                    info.setDepartmentId(teamById);

                    goalAssignees.put(info, item);

                    kpiTreeInfoList.add(info);
                }
                if (tmpItem != null) {
                    KpiTreeInfo teamItem = new KpiTreeInfo(teamById, tmpItem);
                    assignees.put(teamItem, kpiTreeInfoList);
                }
            }

            if (dynamicSelector != null) {
                dynamicSelector.setItems(assignees);
            }
        }
    }

    @Override
    public String getIconStyle() {
        return "hrms hrms-edit";
    }

    public int errors = 0;
    private final String errorMessage = wfmStrings.errorOccurredSavingChanges();

    protected void setDefaultValuesByFormProperty() {
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_TITLE) != null && formProperty.get(CustomFormConstants.GOAL_TITLE).getDefaultValue() != null) {
            title.setText(formProperty.get(CustomFormConstants.GOAL_TITLE).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_STATUS) != null && formProperty.get(CustomFormConstants.GOAL_STATUS).getDefaultValue() != null) {
            status.setSelected(new SelectItem(formProperty.get(CustomFormConstants.GOAL_STATUS).getSelectedId(), formProperty.get(CustomFormConstants.GOAL_STATUS).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD) != null && formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).getDefaultValue() != null) {
            AssessmentService.App.get().getValidityPeriod(formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).getSelectedId(), new AsyncCallback<ValidityPeriodItem>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(ValidityPeriodItem validityPeriodItem) {
                    validityPeriod.setSelected(validityPeriodItem);
                }
            });
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_PROORDEP) != null && formProperty.get(CustomFormConstants.GOAL_PROORDEP).getDefaultValue() != null) {
            dataListBox.setSelected(new SelectItem(formProperty.get(CustomFormConstants.GOAL_PROORDEP).getSelectedId(), formProperty.get(CustomFormConstants.GOAL_PROORDEP).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_SCORE_CALCULATION) != null && formProperty.get(CustomFormConstants.GOAL_SCORE_CALCULATION).getDefaultValue() != null) {
            scoreCalculation.setSelected(new SelectItem(formProperty.get(CustomFormConstants.GOAL_SCORE_CALCULATION).getSelectedId(), formProperty.get(CustomFormConstants.GOAL_SCORE_CALCULATION).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_MEASUREMENT_UNIT) != null && formProperty.get(CustomFormConstants.GOAL_MEASUREMENT_UNIT).getDefaultValue() != null) {
            measurementUnit.setSelected(formProperty.get(CustomFormConstants.GOAL_MEASUREMENT_UNIT).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.COMPANY_GOAL) != null && formProperty.get(CustomFormConstants.COMPANY_GOAL).getDefaultValue() != null) {
            companyGoal.setSelected(new SelectItem(formProperty.get(CustomFormConstants.COMPANY_GOAL).getSelectedId(), formProperty.get(CustomFormConstants.COMPANY_GOAL).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_DESCRIPTION) != null && formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).getDefaultValue() != null) {
            description.setText(formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_ACTION_STEPS) != null && formProperty.get(CustomFormConstants.GOAL_ACTION_STEPS).getDefaultValue() != null) {
            actionSteps.setText(formProperty.get(CustomFormConstants.GOAL_ACTION_STEPS).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_RESOLVER) != null && formProperty.get(CustomFormConstants.GOAL_RESOLVER).getDefaultValue() != null) {
            resolver.setSelected(new SelectItem(formProperty.get(CustomFormConstants.GOAL_RESOLVER).getSelectedId(), formProperty.get(CustomFormConstants.GOAL_RESOLVER).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_ACTUAL) != null && formProperty.get(CustomFormConstants.GOAL_ACTUAL).getDefaultValue() != null) {
            actual.setText(formProperty.get(CustomFormConstants.GOAL_ACTUAL).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_TARGET) != null && formProperty.get(CustomFormConstants.GOAL_TARGET).getDefaultValue() != null) {
            target.setText(formProperty.get(CustomFormConstants.GOAL_TARGET).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_PERSONAL_ASSINESS) != null && formProperty.get(CustomFormConstants.GOAL_PERSONAL_ASSINESS).getDefaultValue() != null) {
            personalBox.setSelected(new SelectItem(formProperty.get(CustomFormConstants.GOAL_PERSONAL_ASSINESS).getSelectedId(), formProperty.get(CustomFormConstants.GOAL_PERSONAL_ASSINESS).getDefaultValue()));
        }
    }

    public void initialize() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VALIDITY_PERIOD_CHANGED, GoalAddEditView2.this, (sender, args) -> {
            final Integer validityPeriodId = Integer.parseInt(args.toString());
            getValidityPeriodItems(validityPeriodId);
        });
        getValidityPeriodItems(null);

        LoadingPanel.loading(true);

        goalCode = new Numbering(false);
        goalCode.addStyleName(Constants.DEFAULT_WIDTH);
        goalCode.ensureDebugId(goalAddEditView + "code");
        if (objectId != null) {
            goalCode.getTxtPrefix().setWidth("100%");
        }

        projectLookUp = new ProjectLookUp(Constants.PROJECT_GOAL);
        projectLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
            HrmsService.App.get().getGoal(projectLookUp.getSelectedItem().getId(), "projectgoal", new AbstractAsyncCallback<GoalItem>() {
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    LoadingPanel.loading(false);
                }

                public void success(final GoalItem o) {
                    projectStartDate = o.getProjectStartDate().getNonConvertedDate();
                    projectEndDate = o.getProjectEndDate().getDate();
                }
            });
        });

        title = new TextBox();
        title.addStyleName(DEFAULT_WIDTH);
// title.ensureDebugId(goalAddEditView + "title");
        title.getElement().setId(goalAddEditView + "title");

        startDate = new DatePicker(true);
        startDate.getElement().setId(goalAddEditView + "startDate");
        startDate.addChangeHandler(handler -> {
            startDate.removeStyleName(ERROR_FORM_STYLE);
            endDate.removeStyleName(ERROR_FORM_STYLE);
        });

        endDate = new DatePicker(true);
        endDate.getElement().setId(goalAddEditView + "toDate");
        endDate.addChangeHandler(handler -> {
            startDate.removeStyleName(ERROR_FORM_STYLE);
            endDate.removeStyleName(ERROR_FORM_STYLE);
            endDate.removeStyleName(ERROR_FORM_STYLE);
        });

        dataListBox = new DataListBox();
        dataListBox.addStyleName(DEFAULT_WIDTH);
        dataListBox.getElement().setId(goalAddEditView + "dataListBox");
        dataListBox.addValueChangeHandler(widget -> {
            if (isProjectGoal) {
                availabilityService.getProject(dataListBox.getSelectedItem().getId(), new AsyncCallback<ProjectSingleItem>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(ProjectSingleItem projectSingleItem) {
                        projectStartDate = projectSingleItem.getStartDate();
                        projectEndDate = projectSingleItem.getEndDate();
                    }
                });
            }
            projectChange();
        });
        if (isProjectGoal) {
            dataListBoxString = Property.get(Constants.PROJECT, wfmStrings.project());
        }

        companyGoal = new DataListBox();
        companyGoal.addStyleName(DEFAULT_WIDTH);
        companyGoal.getElement().setId(goalAddEditView + "companyGoal");

        personalBox = new DataListBox();
        personalBox.getElement().setId(goalAddEditView + "personalBox");
        personalBox.addStyleName(DEFAULT_WIDTH);
        personalBox.setEnabled(false);
//        personalBox.addValueChangeHandler(event -> {
//            selectedEmployeeID = personalBox.getSelectedId();
//            if (personalBox.isSomethingSelected()) {
//                GoalAssigneeItem assigneeItem = (GoalAssigneeItem) personalBox.getSelectedItem();
//                personalWeight.setText(String.valueOf(assigneeItem.getWeight()));
//                personalAvailableWeight.setText(String.valueOf(assigneeItem.getAvaWeight()));
//            }
//        });

        personalWeight = new TextBox();
        Validation.addNumericKeyboardListener(personalWeight);
        personalWeight.getElement().setId(goalAddEditView + "personalWeight");

        personalAvailableWeight = new TextBox();
        personalAvailableWeight.setReadOnly(true);
        Validation.addNumericKeyboardListener(personalAvailableWeight);
        personalAvailableWeight.addKeyUpHandler(event -> {
            TextBox boxWeight = (TextBox) event.getSource();
            TextBox availableWeight = personalAvailableWeight;
            String txt;
            if (boxWeight.getName() != null) {
                if (boxWeight.getText().equals("")) {
                    txt = "0";
                } else {
                    txt = boxWeight.getText();
                }
                if (Double.valueOf(txt) > Double.valueOf(availableWeight.getValue())) {
                    boxWeight.setText(availableWeight.getText());
                }
            }
        });
        personalAvailableWeight.getElement().setId(goalAddEditView + "personalAvailableWeight");

        personalWeightWidget = new InputGroup(personalWeight, personalAvailableWeight);

        resolver = new DataListBox();
        resolver.addStyleName(DEFAULT_WIDTH);
        resolver.getElement().setId(goalAddEditView + "resolver");

        scoreCalculation = new DataListBox();
        scoreCalculation.addStyleName(DEFAULT_WIDTH);
        scoreCalculation.getElement().setId(goalAddEditView + "scoreCalculation");
        scoreCalculation.addValueChangeHandler(event -> dynamicSelector.refreshDataGrid());


        validityPeriod = new DataListBox();
        validityPeriod.addStyleName(DEFAULT_WIDTH);
        validityPeriod.ensureDebugId(goalAddEditView + "validityPeriod");
        validityPeriod.addValueChangeHandler(event -> {
            if (dynamicSelector != null) {
                dynamicSelector.clear();
            }
            if (validityPeriod.isSomethingSelected()) {
                ValidityPeriodItem selectedItem = (ValidityPeriodItem) validityPeriod.getSelectedItem();
                Date date = selectedItem.getFromDate();
                date = DateUtil.addDays(date, 1);
                startDate.setDate(date);
                if (endDate.getDate() != null && endDate.getDate().before(date)) {
                    endDate.setDate(date);
                }
                projectChange();
            }
        });
        if (isPersonGoal) {
            projectChange();
        }

        validityPeriodPanel = new AdvancedInputGroup(validityPeriod);

        validityPeriodPanel.setAppender("ficon--plus");
        validityPeriodPanel.appenderClickHandler(() -> {
            ValidityPeriodItem item = new ValidityPeriodItem();
            HashSet<SelectItem> list = new HashSet<>();
            list.add(new SelectItem(0, "", ValidityPeriodItem.VALIDITY_PERIOD_GOAL));
            item.setPeriodTypeItems(list);
            new ValidityPeriodsPopup(item);
        });

        measurementUnit = new MeasurementsLookUp();
        measurementUnit.addStyleName(DEFAULT_WIDTH);
        measurementUnit.getElement().setId(goalAddEditView + "measurementUnit");

        target = new TextBox();
        target.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(target);
        target.getElement().setId(goalAddEditView + "target");

        actual = new TextBox();
        actual.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(actual);
        actual.getElement().setId(goalAddEditView + "actual");

        progress = new TextBox();
        progress.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(progress);
        progress.getElement().setId(goalAddEditView + "progress");

        status = new DataListBox();
        status.addStyleName(DEFAULT_WIDTH);
        status.getElement().setId(goalAddEditView + "status");

        if (!isPersonGoal) {
            dynamicSelector = new KpiCellTree();
            dynamicSelector.drawSelectedSide(new SelectionContainer() {
                @Override
                public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
//Employee Name Blow
                    Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {

                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return object.getName();
                        }
                    };
                    employee.setSortable(true);
                    sortHandler.setComparator(employee, Comparator.comparing(KpiTreeInfo::getName));
                    selectedDataGrid.addColumn(employee, wfmStrings.employee());
                    selectedDataGrid.setColumnWidth(employee, 120, Style.Unit.PX);

//weight
                    final TextInputCell textInputCell = new TextInputCell();
                    Column<KpiTreeInfo, String> weight = new Column<KpiTreeInfo, String>(textInputCell) {

                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            GoalAssigneeItem item = goalAssignees.get(object);
                            return item.getWeight() > 0 ? String.valueOf(item.getWeight()) : "";
                        }
                    };

                    weight.setFieldUpdater((index, object, value) -> {
                        GoalAssigneeItem item = goalAssignees.get(object);
                        try {
                            Double weightValue = Double.valueOf(value);
                            if (weightValue > item.getAvaWeight()) {
                                weightValue = item.getAvaWeight();
                                Info.show("Weight cannot be more than an available weight!", Info.Type.WARNING);
                                textInputCell.clearViewData(KpiTreeInfo.KEY_PROVIDER.getKey(object));
                            }
                            if (weightValue < 0) {
                                weightValue = (-1) * weightValue;
                                textInputCell.clearViewData(KpiTreeInfo.KEY_PROVIDER.getKey(object));
                            }
                            item.setWeight(weightValue);

                            selectedDataGrid.redraw();
                        } catch (NumberFormatException e) {
                            textInputCell.clearViewData(KpiTreeInfo.KEY_PROVIDER.getKey(object));
                            selectedDataGrid.redraw();
                        }
                    });
                    selectedDataGrid.addColumn(weight, hrmsStrings.goalWeight());
                    selectedDataGrid.setColumnWidth(weight, 70, Style.Unit.PX);

//available weight
                    Column<KpiTreeInfo, String> availableWeight = new Column<KpiTreeInfo, String>(new TextCell()) {

                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            GoalAssigneeItem item = goalAssignees.get(object);
                            return String.valueOf(item.getAvaWeight());
                        }
                    };

                    selectedDataGrid.addColumn(availableWeight, hrmsStrings.availableWeight());
                    selectedDataGrid.setColumnWidth(availableWeight, 70, Style.Unit.PX);

//target
                    final TextInputCell targetCell = new TextInputCell();
                    final TextInputCell actualCell = new TextInputCell();
                    Column<KpiTreeInfo, String> target = new Column<KpiTreeInfo, String>(targetCell) {

                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            GoalAssigneeItem item = goalAssignees.get(object);
                            return item.getTarget() > 0 ? String.valueOf(item.getTarget()) : "";
                        }
                    };

                    target.setFieldUpdater((index, object, value) -> {
                        GoalAssigneeItem item = goalAssignees.get(object);
                        try {
                            item.setTarget(Double.valueOf(value));
                        } catch (NumberFormatException e) {
                            item.setTarget(0d);
                            targetCell.clearViewData(KpiTreeInfo.KEY_PROVIDER.getKey(object));

                        }
                        selectedDataGrid.redraw();
                    });
                    selectedDataGrid.addColumn(target, wfmStrings.target());
                    selectedDataGrid.setColumnWidth(target, 70, Style.Unit.PX);

//actual
                    Column<KpiTreeInfo, String> actual = new Column<KpiTreeInfo, String>(actualCell) {

                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            GoalAssigneeItem item = goalAssignees.get(object);
                            return item.getActual() > 0 ? String.valueOf(item.getActual()) : "";
                        }
                    };

                    actual.setFieldUpdater((index, object, value) -> {
                        GoalAssigneeItem item = goalAssignees.get(object);
                        try {
                            item.setActual(Double.valueOf(value));
                        } catch (NumberFormatException ignore) {
                            item.setActual(0d);
                            actualCell.clearViewData(KpiTreeInfo.KEY_PROVIDER.getKey(object));
                        }
                        selectedDataGrid.redraw();
                    });
                    selectedDataGrid.addColumn(actual, wfmStrings.actual());
                    selectedDataGrid.setColumnWidth(actual, 70, Style.Unit.PX);

                    if (Utils.showScoreCalculation()) {
//score
                        Column<KpiTreeInfo, String> score = new Column<KpiTreeInfo, String>(new TextCell()) {

                            @Override
                            public String getValue(final KpiTreeInfo object) {
                                GoalAssigneeItem item = goalAssignees.get(object);
                                Double score = item.getScore(scoreCalculation.getSelectedItem(true).getDescription());
                                return score != null ? Utils.formatDouble(score) : "";
                            }
                        };

                        selectedDataGrid.addColumn(score, wfmStrings.score());
                        selectedDataGrid.setColumnWidth(score, 70, Style.Unit.PX);

//final score
                        Column<KpiTreeInfo, String> finalScore = new Column<KpiTreeInfo, String>(new TextCell()) {

                            @Override
                            public String getValue(final KpiTreeInfo object) {
                                GoalAssigneeItem item = goalAssignees.get(object);
                                Double finalScore = item.getFinalScore(scoreCalculation.getSelectedItem(true).getDescription());
                                return finalScore != null ? Utils.formatDouble(finalScore) : "";
                            }
                        };

                        selectedDataGrid.addColumn(finalScore, hrmsStrings.finalScore());
                        selectedDataGrid.setColumnWidth(finalScore, 70, Style.Unit.PX);
                    }
//Remove Action
                    final Column<KpiTreeInfo, String> action = new Column<KpiTreeInfo, String>(new IconCell("ficon--trash pointer")) {

                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return null;
                        }
                    };
                    action.setFieldUpdater((index, object, value) -> {
                        List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                        contacts.remove(object);
                        object.setSelected(false);
                        selectionModel.setSelected(object, false);
                    });
                    selectedDataGrid.addColumn(action, "");
                    action.setCellStyleNames("center");
                    selectedDataGrid.setColumnWidth(action, 70, Style.Unit.PX);
                }

                @Override
                public void additionalActions(HTMLPanel actionsPanel) {
                }

            });

// dynamicSelector.getSelectAll().getElement().setId("eligible_assignees");
        }
        attachment = new GeneralFileUpload(folderType, objectId, objectId);
        attachment.getPanel().getElement().setId(goalAddEditView + "attachment");

//goal notes
        String noteEntityName = isPersonGoal ? PERSONAL_GOAL : isProjectGoal ? PROJECT_GOAL : BUSINESS_GOAL;
        noteWidget = new NoteWidget(objectId, noteEntityName);
        noteWidget.getTextBox().getElement().setId(goalAddEditView + "notes");

        addLinkAndLinks = new VerticalPanel();
        addLinkAndLinks.add(getLinkingUtil().getAddLink());
        addLinkAndLinks.add(getLinkingUtil().getLinksPanel());
        addLinkAndLinks.getElement().setId(goalAddEditView + "addLinkAndLinks");

        LoadingPanel.loading(true);
        getCustomFieldUtil().drawCustomFields(this, objectId);
        addFieldsToForm();
    }

    protected boolean validate() {
        errors = 0;
        StringBuilder message = new StringBuilder(wfmStrings.sureEnteredAllData());
        clearErrorStyle();

        if (validityPeriod.getSelectedItem() != null && validityPeriod.getSelectedItem() instanceof ValidityPeriodItem) {
            boolean dateValidation = false;
            ValidityPeriodItem validityPeriodItem = (ValidityPeriodItem) validityPeriod.getSelectedItem();
            if (markAsError(startDate, !Validation.validateDateEqualOrAfter(validityPeriodItem.getFromDate(), startDate.getDate(), true)) == 1) {
                message.append("<br>* " + wfmStrings.goalDatesValidate());
                dateValidation = true;
                errors++;
            }
            if (markAsError(endDate, !Validation.validateDateOrder(endDate.getDate(), validityPeriodItem.getToDate())) == 1) {
                errors++;
                if (!dateValidation) {
                    message.append("<br>* " + wfmStrings.goalDatesValidate());
                    dateValidation = true;
                }
            }
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_NUMBER) != null && formProperty.get(CustomFormConstants.GOAL_NUMBER).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_NUMBER, goalCode, !goalCode.validate());
        }

        if (formProperty != null && formProperty.get(PROJECT_GOAL_LOOKUP) != null && formProperty.get(PROJECT_GOAL_LOOKUP).isRequired()) {
            errors += markAsError(PROJECT_GOAL_LOOKUP, projectLookUp, !Validation.validateLookUpRequired(projectLookUp));
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_TITLE) != null && formProperty.get(CustomFormConstants.GOAL_TITLE).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_TITLE, title, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.GOAL_TITLE).isChanged() ?
                    formProperty.get(CustomFormConstants.GOAL_TITLE).getTitle() : wfmStrings.title(), title, formProperty.get(CustomFormConstants.GOAL_TITLE).getMinChar()));
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_STATUS) != null && formProperty.get(CustomFormConstants.GOAL_STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_STATUS, status, !Validation.validateListBoxRequired(status));
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD) != null && formProperty.get(CustomFormConstants.GOAL_VALIDITY_PERIOD).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_VALIDITY_PERIOD, validityPeriodPanel, !Validation.validateListBoxRequired(validityPeriod));
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_PROORDEP) != null && formProperty.get(CustomFormConstants.GOAL_PROORDEP).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_PROORDEP, dataListBox, !Validation.validateListBoxRequired(dataListBox));
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_MEASUREMENT_UNIT) != null && formProperty.get(CustomFormConstants.GOAL_MEASUREMENT_UNIT).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_MEASUREMENT_UNIT, measurementUnit, !Validation.validateLookUpRequired(measurementUnit));
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_START_DATE) != null && formProperty.get(CustomFormConstants.GOAL_START_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_START_DATE, startDate, startDate.getDate() == null);
            errors += markAsError(CustomFormConstants.GOAL_START_DATE, endDate, endDate.getDate() == null);
        }

        errors += !validatePeriod() ? 1 : 0;

        if (formProperty != null && formProperty.get(CustomFormConstants.COMPANY_GOAL) != null && formProperty.get(CustomFormConstants.COMPANY_GOAL).isRequired()) {
            errors += markAsError(CustomFormConstants.COMPANY_GOAL, companyGoal, !Validation.validateListBoxRequired(companyGoal));
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_DESCRIPTION) != null && formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_DESCRIPTION, description, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).isChanged() ? formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).getTitle() : wfmStrings.description(), description.getTextArea(), formProperty.get(CustomFormConstants.GOAL_DESCRIPTION).getMinChar()));
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_ACTION_STEPS) != null && formProperty.get(CustomFormConstants.GOAL_ACTION_STEPS).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_ACTION_STEPS, actionSteps, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.GOAL_ACTION_STEPS).isChanged() ? formProperty.get(CustomFormConstants.GOAL_ACTION_STEPS).getTitle() : wfmStrings.actionSteps(), actionSteps.getTextArea(), formProperty.get(CustomFormConstants.GOAL_ACTION_STEPS).getMinChar()));
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_RESOLVER) != null && formProperty.get(CustomFormConstants.GOAL_RESOLVER).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_RESOLVER, resolver, !Validation.validateListBoxRequired(resolver));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_PERSONAL_ASSINESS) != null && formProperty.get(CustomFormConstants.GOAL_PERSONAL_ASSINESS).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_PERSONAL_ASSINESS, personalBox, !Validation.validateListBoxRequired(personalBox));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_ACTUAL) != null && formProperty.get(CustomFormConstants.GOAL_ACTUAL).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_ACTUAL, actual, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.GOAL_ACTUAL).isChanged()
                    ? formProperty.get(CustomFormConstants.GOAL_ACTUAL).getTitle() : wfmStrings.actual(), actual, formProperty.get(CustomFormConstants.GOAL_ACTUAL).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.GOAL_TARGET) != null && formProperty.get(CustomFormConstants.GOAL_TARGET).isRequired()) {
            errors += markAsError(CustomFormConstants.GOAL_TARGET, target, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.GOAL_TARGET).isChanged()
                    ? formProperty.get(CustomFormConstants.GOAL_TARGET).getTitle() : wfmStrings.inTarget(), target, formProperty.get(CustomFormConstants.GOAL_TARGET).getMinChar()));
        }
        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(message.toString(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private boolean validatePeriod() {
        int errors = 0;

        if (isPersonGoal) {
            errors += !isValidPersonalGoalPeriod() ? 1 : 0;
        } else if (isProjectGoal) {
            errors += !isValidProjectGoalPeriod() ? 1 : 0;
        }
        return errors == 0;
    }

    private boolean isValidPersonalGoalPeriod() {
        StringBuilder message = new StringBuilder(wfmStrings.sureEnteredAllData());
        if (projectLookUp.isSelected()) {
            int errors = 0;
            boolean dateValidation = false;
            if (markAsError(startDate, !Validation.validateDateEqualOrAfter(projectStartDate, startDate.getDate(), true)) == 1) {
                message.append("<br>* " + wfmStrings.goalDatesValidate());
                dateValidation = true;
                errors++;
            }
            if (markAsError(endDate, !Validation.validateDateOrder(endDate.getDate(), projectEndDate)) == 1) {
                errors++;
                if (!dateValidation) {
                    message.append("<br>* " + wfmStrings.goalDatesValidate());
                    dateValidation = true;
                }
            }
            errors += markAsError(startDate, !Validation.validateDateEqualOrAfter(startDate.getDate(), endDate.getDate(), true));
            if (errors > 0) {
                Info.show(message.toString(), Info.Type.WARNING);
                return false;
            }
            return true;
        }
        markAsError(CustomFormConstants.PROJECT_GOAL_LOOKUP, projectLookUp, !Validation.validateLookUpRequired(projectLookUp));
        return false;
    }

    private boolean isValidProjectGoalPeriod() {
        StringBuilder message = new StringBuilder(wfmStrings.sureEnteredAllData());
        if (dataListBox.isSomethingSelected()) {
            int errors = 0;
            boolean dateValidation = false;
            if (markAsError(startDate, !Validation.validateDateEqualOrAfter(projectStartDate, startDate.getDate(), true)) == 1) {
                message.append("<br>* " + wfmStrings.goalDatesValidate());
                dateValidation = true;
                errors++;
            }
            if (markAsError(endDate, !Validation.validateDateOrder(endDate.getDate(), projectEndDate)) == 1) {
                errors++;
                if (!dateValidation) {
                    message.append("<br>* " + wfmStrings.goalDatesValidate());
                    dateValidation = true;
                }
            }
            errors += markAsError(startDate, !Validation.validateDateEqualOrAfter(startDate.getDate(), endDate.getDate(), true));
            if (errors > 0) {
                Info.show(message.toString(), Info.Type.WARNING);
                return false;
            }
            return true;
        }
        markAsError(CustomFormConstants.GOAL_PROORDEP, dataListBox, !Validation.validateListBoxRequired(dataListBox));
        return false;
    }

    protected void save() {
        if (!validate()) {
            return;
        }

        LoadingPanel.loading(true);
        enableButton(false);
        item = setValuesToRPC(item);
        item.setRelations(getLinkingUtil().getTaggingView().getSelectedRelations());
        item.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());
        HrmsService.App.get().saveGoal(item, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Integer o) {
                LoadingPanel.loading(false);
                enableButton(true);
                if (o == -1) {
                    Info.show(hrmsStrings.goalWithThisCodeAlreadyExists(), Info.Type.WARNING);
                } else {
                    Info.show(successMessage, Info.Type.INFO);
                    onShellOk();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GOAL_ADD, o, GoalAddEditView2.this);
                }
            }
        });
    }

    public GoalItem setValuesToRPC(GoalItem item) {
        if (objectId != null) {
            item.setObjectId(objectId);
        }

        if (isProjectGoal || isPersonGoal) {
            item.setGoalNumber(goalCode.getNumberData(true));
        }

        item.setTitle(title.getText());

        item.setDescription(description.getText());

        item.setFromDate(new DateNonConvertable(startDate.getDate()));

        item.setToDate(new DateNonConvertable(endDate.getDate()));

        if (dataListBox.getSelectedItem() != null) {
            item.setProjectId(dataListBox.getSelectedItem().getId());
        }

        item.setCompanyGoalId(null);
        if (companyGoal.getSelectedItem() != null) {
            item.setCompanyGoalId(companyGoal.getSelectedItem().getId());
        }

        item.setActionSteps(actionSteps.getText());
        if ("".equals(progress.getText())) {
            progress.setText("0");
        }

        item.setResolverId(null);
        if (resolver.getSelectedItem() != null) {
            item.setResolverId(resolver.getSelectedItem().getId());
        }
        item.setWeightId(null);
        if (scoreCalculation.getSelectedItem(true) != null) {
            item.setScore(scoreCalculation.getSelectedItem(true));
        }

        if ("".equals(progress.getText())) {
            progress.setText("0");
        }
        item.setProgress(Double.parseDouble(progress.getText()));

        item.setStatusId(null);
        if (status.getSelectedItem() != null) {
            item.setStatusId(status.getSelectedItem().getId());
        }

        if (dataListBox.getSelectedItem() != null && isProjectGoal) {
            item.setProjectId(dataListBox.getSelectedItem().getId());
        }

        item.setAttachments(attachment.getAttachedFiles());

        if (isPersonGoal) {
            GoalAssigneeItem assigneeItem = (GoalAssigneeItem) personalBox.getSelectedItem();
            try {
                assigneeItem.setActual(Double.valueOf(actual.getText()));
            } catch (NumberFormatException ex) {
                assigneeItem.setActual(0d);
            }
            try {
                assigneeItem.setTarget(Double.valueOf(target.getText()));
            } catch (NumberFormatException ex) {
                assigneeItem.setTarget(0d);
            }
            try {
                assigneeItem.setWeight(Double.valueOf(personalWeight.getText()));
            } catch (NumberFormatException ex) {
                assigneeItem.setWeight(0d);
            }
            item.setGoalAssigneeItem(new GoalAssigneeItem[]{assigneeItem});
            if (projectLookUp.getSelectedItem() != null) {
                item.setSelectedProjectGoalId(projectLookUp.getSelectedItem().getId());
            }
        } else {
            GoalAssigneeItem[] assigneeItems = new GoalAssigneeItem[dynamicSelector.getSelectedData().size()];
            int i = 0;
            for (KpiTreeInfo info : dynamicSelector.getSelectedData()) {
                assigneeItems[i++] = goalAssignees.get(info);
            }

            item.setGoalAssigneeItem(assigneeItems);
        }

        item.setGoalCategoryId(goalCategoryID);
        item.setValidityPeriodItem((ValidityPeriodItem) validityPeriod.getSelectedItem());
        item.setMeasurementUnit(measurementUnit.getSelectedItem());
        item.setNotes(noteWidget.getNewNotesToSave());

        return item;
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

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}