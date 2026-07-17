package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.OnboardingItem;
import com.edatasite.workforce.gwt.profile.client.ui.view.ReferenceItemsTab;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * User: User
 * Date: 8/25/12
 * Time: 12:38 PM
 */
public class AddEditOnboardingStepView extends CustomForm2 implements Constants, Colapse {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private OnboardingItem item;
    private TextBox activityName;
    private TextArea2 activityDescription;
    private DataListBox activityPeriod;
    private DataListBox parentSteps;
    private ReferenceItemsTab statusTab;
    private KpiCheckBox showInEmployeeProfile;
    private KpiCheckBox createForm;
    private Integer objectId;
    private boolean saveCloseClicked = false;
    private final String addEditPositionView = "add_edit_onboarding_step_view_";
    private FormHasCustomField customFieldUtil;

    public AddEditOnboardingStepView() {
        super("addonboardingstep", hrmsStrings.addOnboardingStep());
        setDescription(hrmsStrings.addOnboardingStep());
    }

    public AddEditOnboardingStepView(Integer objectID) {
        super("addonboardingstep", hrmsStrings.onboardingEditStep());
        this.objectId = objectID;
    }

    @Override
    public String getIconStyle() {
        return "icon-edit";
    }

    @Override
    protected void addButtons() {

        addButton(wfmStrings.save(), event -> {
            saveCloseClicked = true;
            save();
        });

    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        HrmsService.App.get().getOnboardingStep(objectId, new AbstractAsyncCallback<OnboardingItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(OnboardingItem object) {
                LoadingPanel.loading(false);
                item = object;
                fillTable();
                getCustomFieldUtil().fillCustomFieldsWithData(object.getCustomFieldItems());
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ONBOARDING_STEP_FORM;
    }

    @Override
    protected String getFormType() {
        return objectId != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFields(ViewName.OnboardingStep, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                AddEditOnboardingStepView.super.onInitialize();
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result);
                }
                AddEditOnboardingStepView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void initPredefinedValues() {
        addPredefinedValues(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_ACTIVITY_PERIOD, item.getPeriods());
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    @Override
    protected String getWikiCode() {
        return objectId == null ? PermissionConstants.HRMS_ONBOARDING_STEP_ADD : PermissionConstants.HRMS_ONBOARDING_STEP_EDIT;
    }

    protected void registerFields() {
        activityName = new TextBox();
        activityName.ensureDebugId(addEditPositionView + "activityName");
        activityName.addStyleName(DEFAULT_WIDTH);
        activityName.addKeyUpHandler(keyUpEvent -> activityName.removeStyleName(ERROR_FORM_STYLE));
        activityName.addFocusHandler(focusEvent -> activityName.removeStyleName(ERROR_FORM_STYLE));

        activityDescription = new TextArea2(wfmStrings.description());
        activityDescription.ensureDebugId("onboarding_step-description");
        activityDescription.ensureDebugId(addEditPositionView + "activityDescription");
        activityDescription.addStyleName("file--AddEditOnboardingStepVew");

        activityPeriod = new DataListBox();
        activityPeriod.ensureDebugId(addEditPositionView + "activityPeriod");
        activityPeriod.addStyleName(DEFAULT_WIDTH);

        parentSteps = new DataListBox();
        parentSteps.ensureDebugId(addEditPositionView + "parentSteps");
        parentSteps.addStyleName(DEFAULT_WIDTH);

        showInEmployeeProfile = new KpiCheckBox();
        showInEmployeeProfile.ensureDebugId(addEditPositionView + "showInEmployeeProfile");

        createForm = new KpiCheckBox();
        createForm.ensureDebugId(addEditPositionView + "createForm");

        addTitleField(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_TITLE, wfmStrings.onboardingDetails());
        addTitleField(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_EMPLOYEES, wfmStrings.responsiblePeople());
        addTitleField(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_STATUSES, wfmStrings.onboardingStepStatuses());

        addField(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_ACTIVITY_NAME, activityName, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_ACTIVITY_DESCRIPTION, activityDescription, null);
        addField(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_ACTIVITY_PERIOD, activityPeriod, getTitle(wfmStrings.period()));
        addField(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_PARENT_STEPS, parentSteps, getTitle(wfmStrings.previousStep()));
        addField(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_SHOW_IN_EMPLOYEE_PROFILE, showInEmployeeProfile, getTitle(wfmStrings.showInEmployeeProfile()));
        addField(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_CREATE_FORM, createForm, getTitle(wfmStrings.createOnboardingEntryForm()));
        getCustomFieldUtil().drawCustomFields(this, objectId);
        show();
    }


    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private void fillTable() {
        initPredefinedValues();
        statusTab = new ReferenceItemsTab(item.getStatusItems(), false);
        addField(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_STATUSES, statusTab, null);
        statusTab.getElement().addClassName("has-tableDescription");
        if (item.getStepName() != null) {
            activityName.setValue(item.getStepName());
        }
        if (item.getStepDescription() != null) {
            activityDescription.setText(item.getStepDescription());
        }
        activityPeriod.setItems(item.getPeriods());
        if (item.getPeriodId() != null) {
            activityPeriod.setSelected(item.getPeriodId());
        }
        if (item.getShowInEmployeeProfile() != null) {
            showInEmployeeProfile.setValue(item.getShowInEmployeeProfile());
        }
        if (item.isCreateForm()) {
            createForm.setValue(item.isCreateForm());
            createForm.setEnabled(false);
        }
        parentSteps.setItems(item.getParentSteps());
        if (item.getParentID() != null) {
            parentSteps.setSelected(item.getParentID());
        }
    }

    private void save() {
        if (!validate()) {
            return;
        }
        if (!validateOnboardingStepName()) {
            return;
        }
        item = item == null ? new OnboardingItem() : item;
        if (objectId != null) {
            item.setStepId(objectId);
        }
        item.setStepName(activityName.getValue());
        item.setStepDescription(activityDescription.getText());
        item.setPeriodId(activityPeriod.getSelectedId());
        item.setParentID(parentSteps.getSelectedId());


        item.setShowInEmployeeProfile(showInEmployeeProfile.getValue());
        item.setCreateForm(createForm.getValue() != null && createForm.getValue());
        item.setStatusItems(statusTab.save(null));
        item.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        if (item.getFormID() == null) {
            item.setFormID(Constants.ONBOARDING_STEP_FORM + item.getStepName().toUpperCase());
        }

        LoadingPanel.loading(true);
        HrmsService.App.get().saveOnboardingStep(item, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(final Integer o) {
                LoadingPanel.loading(false);
                if (o > 0) {
                    if (objectId == null) {
                        item.setStepId(o);
                    }
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.onboardingStep()), Info.Type.INFO);
                    onShellOk();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ONBOARDING_STEP_ADD_EDIT, item, AddEditOnboardingStepView.this);
                    if (objectId == null && item.isCreateForm()) {
                        saveReportTemplateForOnboarding(o);
                    }
                } else {
                    Info.show(hrmsStrings.onboardingStepWithThisNameAlreadyExists(), Info.Type.WARNING);
                    activityName.addStyleName(ERROR_FORM_STYLE);
                }
                saveCloseClicked = false;
            }
        });
    }

    private void saveReportTemplateForOnboarding(final Integer o) {
        HrmsService.App.get().createReportXmlTemplate(item.getStepName(), o, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(Void result) {
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();
        customValidate();
        errors += markAsError(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_ACTIVITY_NAME, activityName, activityName.getValue() == null || "".equals(activityName.getValue()));
//        errors += markAsError(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_ACTIVITY_DESCRIPTION, activityDescription, activityDescription.getText() == null || "".equals(activityDescription.getText()));
//        errors += markAsError(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_ACTIVITY_PERIOD, activityPeriod, activityPeriod.getSelectedItem(false) == null);
//        errors += markAsError(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_ROLES, roles, roles.getSelectedData() == null);
//        errors += markAsError(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_EMPLOYEES, employees, employees.getSelectedData() == null);
        errors += getCustomFieldUtil().validateCustomFields();
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private boolean validateOnboardingStepName() {
        int errors = 0;
        errors += markAsError(CustomFormConstants.ONBOARDING.ONBOARDING_STEP_ACTIVITY_NAME, activityName,
                activityName.getValue().contains("/")
                        || activityName.getValue().contains("\\")
                        || activityName.getValue().contains("|"));

        if (errors > 0) {
            Info.show(wfmStrings.onboardingNameCannotSymbols(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void onShellOk() {
        if (saveCloseClicked) {
            closeTab();
        } else {
            reInit();
        }
    }

    private void reInit() {
        objectId = null;
        registerFields();
        initForm();
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}