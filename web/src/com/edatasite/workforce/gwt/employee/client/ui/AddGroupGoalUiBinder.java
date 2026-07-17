package com.edatasite.workforce.gwt.employee.client.ui;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.ui.view.ValidityPeriodsPopup;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ColumnType;
import com.edatasite.workforce.gwt.core.client.form.CustomizeFormItem;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.ui.AsyncWidget;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupGoalITem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_GROUP_GOAL_CUSTOMIZE_BUTTON;

public class AddGroupGoalUiBinder extends AsyncWidget implements Constants, Errors, Colapse {

    interface IGroupGoalViewBinder extends UiBinder<HTMLPanel, AddGroupGoalUiBinder> {
    }

    private static final AddGroupGoalUiBinder.IGroupGoalViewBinder ourUiBinder = GWT.create(AddGroupGoalUiBinder.IGroupGoalViewBinder.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiField
    Div employeeText;
    @UiField
    Div employeeWidget;
    @UiField
    Div employeeFormGroup;
    @UiField
    Div approverText;
    @UiField
    Div approverWidget;
    @UiField
    Div approverFormGroup;
    @UiField
    Div validityPeriodText;
    @UiField
    Div validityPeriodWidget;
    @UiField
    Div validityPeriodFormGroup;
    @UiField
    Div fromDateText;
    @UiField
    Div fromDateWidget;
    @UiField
    Div fromDateFormGroup;
    @UiField
    Div toDateText;
    @UiField
    Div toDateWidget;
    @UiField
    Div toDateFormGroup;
    @UiField
    Div goalsWidget;
    @UiField
    Div goalsFormGroup;

    private final Integer objectId;
    private EmployeeLookUpWithCode employees;
    private ChosenApproversWidget approver;
    private DataListBox validityPeriod;
    private DatePicker fromDate;
    private DatePicker toDate;
    private WfmButton2 saveGoalsButton;
    private MultiGoalPanel multiGoalPanel;
    private LinkedHashMap<String, FormProperty> formPropertyMap = new LinkedHashMap<>();
    private ModelForm modelForm;
    private Integer pendingValidityPeriodId;

    AddGroupGoalUiBinder(Integer objectId) {
        this.objectId = objectId;
    }

    @Override
    protected Widget onInitialize() {
        add(ourUiBinder.createAndBindUi(this));
        setFieldLabel(employeeText, wfmStrings.employee(), true);

        employees = new EmployeeLookUpWithCode();
        employees.setContextCode(PermissionConstants.HRMS_CONTEXT);
        employees.ensureDebugId("group_goal_employees");
        employeeWidget.add(employees);

        saveGoalsButton = new WfmButton2(wfmStrings.submit(), WfmButton2.BTN_PRIMARY);
        saveGoalsButton.addClickHandler(widget -> saveGoals());

        setFieldLabel(approverText, wfmStrings.approver(), true);
        approver = new ChosenApproversWidget(RelationItem.TYPE_GROUP_GOAL, objectId);
        approver.ensureDebugId("approver-list");
        approver.ensureDebugId("group_goal_approver1");
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AddGroupGoalUiBinder.this, (sender, args) -> {
            Timer timer = new Timer() {
                @Override
                public void run() {
                    if (approver.getFirstApproverLookUp() != null) {
                        onManagerSelected();
                        approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> onManagerSelected());
                    }
                }
            };
            timer.schedule(1000);
        });
        approverWidget.add(approver);

        setFieldLabel(validityPeriodText, wfmStrings.validityPeriod(), false);
        validityPeriod = new DataListBox();
        validityPeriod.addStyleName(DEFAULT_WIDTH);
        validityPeriod.ensureDebugId("group_goal_validityPeriod");
        validityPeriod.addValueChangeHandler(event -> {
            if (validityPeriod.isSomethingSelected()) {
                syncDatesWithValidityPeriod();
            }
        });

        AdvancedInputGroup validityPeriodPanel = new AdvancedInputGroup(validityPeriod);
        validityPeriodPanel.setAppender("ficon--plus");
        validityPeriodPanel.appenderClickHandler(() -> {
            ValidityPeriodItem item = new ValidityPeriodItem();
            HashSet<SelectItem> list = new HashSet<>();
            list.add(new SelectItem(0, "", ValidityPeriodItem.VALIDITY_PERIOD_GOAL));
            item.setPeriodTypeItems(list);
            new ValidityPeriodsPopup(item);
        });
        validityPeriodWidget.add(validityPeriodPanel);

        setFieldLabel(fromDateText, wfmStrings.startDate(), true);
        fromDate = new DatePicker(true);
        fromDate.getElement().setId("group_goal_startDate");
        fromDateWidget.add(fromDate);

        setFieldLabel(toDateText, wfmStrings.endDate(), true);
        toDate = new DatePicker(true);
        toDate.getElement().setId("group_goal_toDate");
        toDateWidget.add(toDate);

        multiGoalPanel = new MultiGoalPanel();
        multiGoalPanel.addStyleName("reachFullWidth");
        goalsWidget.add(multiGoalPanel);

        add(createFooter());
        loadFormConfiguration();
        return null;
    }

    private void loadFormConfiguration() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.GroupPersonalGoal, LayoutRPC.GROUP_GOAL_FORM,
                new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
                    @Override
                    public void failure(Throwable throwable) {
                        formPropertyMap = new LinkedHashMap<>();
                        loadModelForm();
                    }

                    @Override
                    public void success(CompanyCfAndPropertyItems result) {
                        formPropertyMap = result != null && result.getFormPropertyMap() != null ? result.getFormPropertyMap() : new LinkedHashMap<>();
                        loadModelForm();
                    }
                });
    }

    private void loadModelForm() {
        AllInOneService.App.get().getModelForm(LayoutRPC.GROUP_GOAL_FORM, new AbstractAsyncCallback<ModelForm>() {
            @Override
            public void failure(Throwable throwable) {
                applyFormConfiguration();
                fillValidityPeriodList();
                loadData();
            }

            @Override
            public void success(ModelForm result) {
                modelForm = result;
                applyFormConfiguration();
                fillValidityPeriodList();
                loadData();
            }
        });
    }

    private void applyFormConfiguration() {
        applyFieldConfiguration(CustomFormConstants.EMPLOYEE, employeeText, employeeFormGroup, wfmStrings.employee(), true, employees);
        applyFieldConfiguration(CustomFormConstants.APPROVERS, approverText, approverFormGroup, wfmStrings.approver(), true, approver);
        applyFieldConfiguration(CustomFormConstants.GOAL_VALIDITY_PERIOD, validityPeriodText, validityPeriodFormGroup, wfmStrings.validityPeriod(), false, validityPeriod);
        applyFieldConfiguration(CustomFormConstants.GOAL_START_DATE, fromDateText, fromDateFormGroup, wfmStrings.startDate(), true, fromDate);
        applyFieldConfiguration(CustomFormConstants.GOAL_TO_DATE, toDateText, toDateFormGroup, wfmStrings.endDate(), true, toDate);
        goalsFormGroup.setVisible(isFieldVisible(ASSIGNED_GOALS));

        if (objectId == null) {
            applyEmployeeDefault();
            applyDateDefault(CustomFormConstants.GOAL_START_DATE, fromDate);
            applyDateDefault(CustomFormConstants.GOAL_TO_DATE, toDate);
        }
    }

    private void fillValidityPeriodList() {
        LoadingPanel.loading(true);
        AssessmentService.App.get().getValidityPeriods(ValidityPeriodItem.VALIDITY_PERIOD_GOAL, new AsyncCallback<ValidityPeriodItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ValidityPeriodItem[] validityPeriodItems) {
                LoadingPanel.loading(false);
                validityPeriod.setItems(validityPeriodItems);
                if (pendingValidityPeriodId != null) {
                    validityPeriod.setSelected(pendingValidityPeriodId);
                    pendingValidityPeriodId = null;
                    return;
                }
                if (objectId == null && applyValidityPeriodDefault()) {
                    syncDatesWithValidityPeriod();
                    return;
                }
                if (objectId == null && validityPeriodItems.length == 1) {
                    validityPeriod.setSelected(validityPeriodItems[validityPeriodItems.length - 1].getId());
                    syncDatesWithValidityPeriod();
                }
            }
        });
    }

    private void loadData() {
        if (objectId != null) {
            LoadingPanel.loading(true);
            HrmsService.App.get().getGroupGoalData(objectId, new AbstractAsyncCallback<GroupGoalITem>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(GroupGoalITem groupGoalITem) {
                    LoadingPanel.loading(false);
                    employees.setSelected(groupGoalITem.getEmployee().getId(), groupGoalITem.getEmployee().getName());
                    if (groupGoalITem.getValidityPeriod() != null) {
                        selectValidityPeriod(groupGoalITem.getValidityPeriod().getId());
                    }
                    if (groupGoalITem.getFromDate() != null) {
                        fromDate.setDate(groupGoalITem.getFromDate().getNonConvertedDate());
                    }
                    if (groupGoalITem.getToDate() != null) {
                        toDate.setDate(groupGoalITem.getToDate().getNonConvertedDate());
                    }
                    multiGoalPanel.fillTable(groupGoalITem.getGoalItems());
                }
            });
        } else {
            multiGoalPanel.createStandartRows();
        }
    }

    private void onManagerSelected() {
        SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
        if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
            saveGoalsButton.setText(wfmStrings.approveAndClose());
        } else {
            saveGoalsButton.setText(wfmStrings.submit());
        }
    }

    private void saveGoals() {
        if (!validate()) {
            return;
        }
        GroupGoalITem groupGoalITem = new GroupGoalITem();
        groupGoalITem.setObjectId(objectId);
        groupGoalITem.setEmployee(employees.getSelectedItem());
        groupGoalITem.setApprovers(approver.getChosenApprovers());

        SelectItem item = approver.getFirstApproverLookUp() != null ? approver.getFirstApproverLookUp().getSelectedItem() : null;
        if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
            groupGoalITem.setSelfApprover(true);
        }

        groupGoalITem.setValidityPeriod((ValidityPeriodItem) validityPeriod.getSelectedItem());
        groupGoalITem.setFromDate(new DateNonConvertable(fromDate.getDate()));
        groupGoalITem.setToDate(new DateNonConvertable(toDate.getDate()));

        multiGoalPanel.save(groupGoalITem);
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();

        if (isFieldVisible(CustomFormConstants.EMPLOYEE) && isFieldRequired(CustomFormConstants.EMPLOYEE, true) && !Validation.validateLookUpRequired(employees)) {
            employees.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }

        if (isFieldVisible(CustomFormConstants.APPROVERS) && isFieldRequired(CustomFormConstants.APPROVERS, true) && !approver.isValid()) {
            approver.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }

        if (isFieldVisible(CustomFormConstants.GOAL_VALIDITY_PERIOD) && isFieldRequired(CustomFormConstants.GOAL_VALIDITY_PERIOD, false)
                && !Validation.validateListBoxRequired(validityPeriod)) {
            validityPeriod.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }

        boolean hasStartDate = fromDate.getDate() != null;
        boolean hasEndDate = toDate.getDate() != null;

        if (isFieldVisible(CustomFormConstants.GOAL_START_DATE) && isFieldRequired(CustomFormConstants.GOAL_START_DATE, true) && !hasStartDate) {
            fromDate.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (isFieldVisible(CustomFormConstants.GOAL_TO_DATE) && isFieldRequired(CustomFormConstants.GOAL_TO_DATE, true) && !hasEndDate) {
            toDate.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }

        if (hasStartDate && hasEndDate && !Validation.validateDateEqualOrAfter(fromDate.getDate(), toDate.getDate(), true)) {
            fromDate.addStyleName(Constants.ERROR_FORM_STYLE);
            toDate.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }

        if (validityPeriod.getSelectedItem() != null) {
            ValidityPeriodItem validityPeriodItem = (ValidityPeriodItem) validityPeriod.getSelectedItem();
            if (hasStartDate && !Validation.validateDateEqualOrAfter(validityPeriodItem.getFromDate(), fromDate.getDate(), true)) {
                validityPeriod.addStyleName(Constants.ERROR_FORM_STYLE);
                errors++;
            }
            if (hasEndDate && !Validation.validateDateOrder(toDate.getDate(), validityPeriodItem.getToDate())) {
                validityPeriod.addStyleName(Constants.ERROR_FORM_STYLE);
                errors++;
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    protected void clearErrorStyle() {
        employees.removeStyleName(Constants.ERROR_FORM_STYLE);
        approver.removeStyleName(Constants.ERROR_FORM_STYLE);
        validityPeriod.removeStyleName(Constants.ERROR_FORM_STYLE);
        fromDate.removeStyleName(Constants.ERROR_FORM_STYLE);
        toDate.removeStyleName(Constants.ERROR_FORM_STYLE);
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return AddGroupGoalUiBinder.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return AddGroupGoalUiBinder.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> items = new ArrayList<>();
        if (canCustomizeGroupGoal()) {
            Div customizeWrapper = new Div();
            customizeWrapper.add(getCustomizeButton());
            items.add(customizeWrapper);
        }
        Div saveWrapper = new Div();
        saveWrapper.add(saveGoalsButton);
        items.add(saveWrapper);
        Div cancelWrapper = new Div();
        items.add(cancelWrapper);
        return items;
    }

    private List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    private boolean canCustomizeGroupGoal() {
        return  Utils.hasRole(Constants.ADMIN) || Utils.hasPermission(HRMS_GROUP_GOAL_CUSTOMIZE_BUTTON);
    }

    private WfmButton2 getCustomizeButton() {
        WfmButton2 customizeButton = new WfmButton2(wfmStrings.customize(), BTN_DEFAULT_OUTLINE);
        customizeButton.ensureDebugId("group_goal_customize_button");
        customizeButton.addClickHandler(click -> {
            String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
            SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add/" + LayoutRPC.GROUP_GOAL_FORM + "/" + (url != null ? URL.encodeQueryString(url) : ""));
        });
        return customizeButton;
    }

    private void applyFieldConfiguration(String fieldId, Div labelWidget, Div formGroup, String defaultTitle, boolean defaultRequired, Widget fieldWidget) {
        FormProperty formProperty = formPropertyMap.get(fieldId);
        String title = formProperty != null && formProperty.isChanged() ? formProperty.getTitle() : defaultTitle;
        setFieldLabel(labelWidget, title, isFieldRequired(fieldId, defaultRequired));
        formGroup.setVisible(isFieldVisible(fieldId));
        setEnabled(fieldWidget, formProperty == null || !formProperty.isDisabled());
    }

    private void setFieldLabel(Div labelContainer, String title, boolean required) {
        labelContainer.clear();
        StringBuilder html = new StringBuilder("<span>").append(title);
        if (required) {
            html.append(" <font color='red'>*</font>");
        }
        html.append("</span>");
        labelContainer.add(new HTML(html.toString()));
    }

    private boolean isFieldRequired(String fieldId, boolean defaultRequired) {
        FormProperty formProperty = formPropertyMap.get(fieldId);
        return formProperty != null ? formProperty.isRequired() : defaultRequired;
    }

    private boolean isFieldVisible(String fieldId) {
        if (modelForm == null) {
            return true;
        }

        if (modelForm.getColumnMap() != null) {
            for (Map.Entry<String, HashMap<ColumnType, LinkedList<CustomizeFormItem>>> sectionEntry : modelForm.getColumnMap().entrySet()) {
                if (!isSectionVisible(sectionEntry.getKey())) {
                    continue;
                }
                for (Map.Entry<ColumnType, LinkedList<CustomizeFormItem>> columnEntry : sectionEntry.getValue().entrySet()) {
                    for (CustomizeFormItem item : columnEntry.getValue()) {
                        if (fieldId.equals(item.getName())) {
                            return item.isActive();
                        }
                    }
                }
            }
        }

        ModelField modelField = modelForm.getFieldByFieldID(fieldId);
        return modelField == null || (!modelField.isHide() && isSectionVisible(modelField.getSection()));
    }

    private boolean isSectionVisible(String sectionName) {
        if (modelForm == null || modelForm.getSectionsRpcMap() == null || sectionName == null) {
            return true;
        }
        if (!modelForm.getSectionsRpcMap().containsKey(sectionName)) {
            return true;
        }
        return modelForm.getSectionsRpcMap().get(sectionName).isActive();
    }

    private void setEnabled(Widget widget, boolean enabled) {
        if (widget instanceof EmployeeLookUpWithCode) {
            ((EmployeeLookUpWithCode) widget).setEnabled(enabled);
        } else if (widget instanceof ChosenApproversWidget) {
            ((ChosenApproversWidget) widget).setEnabled(enabled);
        } else if (widget instanceof DataListBox) {
            ((DataListBox) widget).setEnabled(enabled);
        } else if (widget instanceof DatePicker) {
            ((DatePicker) widget).setEnabled(enabled);
        }
    }

    private void applyEmployeeDefault() {
        FormProperty formProperty = formPropertyMap.get(CustomFormConstants.EMPLOYEE);
        if (formProperty != null && formProperty.getSelectedId() != null && formProperty.getDefaultValue() != null) {
            employees.setSelected(formProperty.getSelectedId(), formProperty.getDefaultValue());
        }
    }

    private boolean applyValidityPeriodDefault() {
        FormProperty formProperty = formPropertyMap.get(CustomFormConstants.GOAL_VALIDITY_PERIOD);
        if (formProperty != null && formProperty.getSelectedId() != null) {
            selectValidityPeriod(formProperty.getSelectedId());
            return true;
        }
        return false;
    }

    private void applyDateDefault(String fieldId, DatePicker datePicker) {
        FormProperty formProperty = formPropertyMap.get(fieldId);
        if (formProperty == null || Utils.isNullOrEmpty(formProperty.getDefaultValue())) {
            return;
        }
        Date date = new Date();
        if ("TOMORROW".equals(formProperty.getDefaultValue())) {
            date = DateUtil.addDays(date, 1);
        } else if ("YESTERDAY".equals(formProperty.getDefaultValue())) {
            date = DateUtil.addDays(date, -1);
        } else if (!"TODAY".equals(formProperty.getDefaultValue())) {
            return;
        }
        datePicker.setDate(date);
    }

    private void selectValidityPeriod(Integer validityPeriodId) {
        pendingValidityPeriodId = validityPeriodId;
        if (validityPeriod.getItems() != null) {
            validityPeriod.setSelected(validityPeriodId);
            pendingValidityPeriodId = null;
        }
    }

    private void syncDatesWithValidityPeriod() {
        if (!validityPeriod.isSomethingSelected()) {
            return;
        }
        ValidityPeriodItem selectedItem = (ValidityPeriodItem) validityPeriod.getSelectedItem();
        Date date = DateUtil.addDays(selectedItem.getFromDate(), 1);
        if (objectId == null || fromDate.getDate() == null) {
            fromDate.setDate(date);
        }
        if (toDate.getDate() != null && toDate.getDate().before(date)) {
            toDate.setDate(date);
        }
    }
}
