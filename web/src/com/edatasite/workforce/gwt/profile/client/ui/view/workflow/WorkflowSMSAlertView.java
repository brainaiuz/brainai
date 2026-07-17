package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.SMSTemplateItem;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowSMSAlert;
import com.edatasite.workforce.gwt.profile.client.ui.view.WorkflowDateSelecter;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Azazello on 4/23/15.
 */
public class WorkflowSMSAlertView extends KpiModal implements Constants {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    private DataListBox provider;
    private DataListBox template;
    private TextBox phone;
    private KpiSwitcher workflowTimeBasedAction;
    private WorkflowDateSelecter workflowTimeBasedActionDate;
    private WfmButton2 btnSave;
    private MaterialPanel descriptionPanel;
    private TextArea2 content;
    private DataListBox fieldCodes;
    private HTML attribute;
    private Div container;

    private final Integer objectID;
    private final Integer workflowID;
    private WorkflowSMSAlert alert;
    private final LinkedHashMap<String, ModelField> fields = new LinkedHashMap<>();
    private Localize localize;
    private final String debug_id = "workflow_sms_";
    private DataListBox taskSMSrecipients;
    private FormGroup taskRecipientsFormGroup;

    public WorkflowSMSAlertView(Integer objectID, Integer workflowID) {
        this.objectID = objectID;
        this.workflowID = workflowID;
        setTitle(settingsStrings.addSMSAlert());
        setWidth("775px");
        init();
        addButtons();
        fillFields();
    }


    private void init() {
        provider = new DataListBox();
        provider.ensureDebugId(debug_id + "provider");

        template = new DataListBox();
        template.ensureDebugId(debug_id + "template");
        template.addValueChangeHandler(changeEvent -> {
            if (template.getSelectedItem() != null) {
                LoadingPanel.loading(true, WorkflowSMSAlertView.this);
                ProfileService.App.get().getSMSTemplateForWorkflow(template.getSelectedId(), new AbstractAsyncCallback<SMSTemplateItem>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false, WorkflowSMSAlertView.this);
                    }

                    @Override
                    public void onSuccess(SMSTemplateItem result) {
                        LoadingPanel.loading(false, WorkflowSMSAlertView.this);
                        if (result != null && result.getContent() != null) {
                            if (Constants._WORKFLOW_MODULE_TASK.equals(result.getModuleCode())) {
                                taskRecipientsFormGroup.setVisible(true);
                            } else {
                                taskRecipientsFormGroup.setVisible(false);
                                taskSMSrecipients.clearSelected();
                                taskSMSrecipients.setSelectedNullLabel();
                            }
                            content.setText(result.getContent());
                        }
                    }
                });
            } else {
                content.setText("");
            }
        });

        phone = new TextBox();
        phone.ensureDebugId(debug_id + "phone");

        taskSMSrecipients = new DataListBox();
        taskSMSrecipients.setSelectedNullLabel();
        taskSMSrecipients.addItem(new SelectItem(0, ""), wfmStrings.pleaseSelect());
        taskSMSrecipients.addItem(new SelectItem(1, Constants.TASK_CREATOR), wfmStrings.createdBy());
        taskSMSrecipients.addItem(new SelectItem(2, Constants.TASK_EMPLOYEES), wfmStrings.assignedEmployees());

        drawDescriptionPanel();

        workflowTimeBasedAction = new KpiSwitcher(wfmStrings.executionTime(), null, false);
        workflowTimeBasedAction.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        workflowTimeBasedActionDate = new WorkflowDateSelecter(false, true);
        workflowTimeBasedActionDate.setVisible(false);
        workflowTimeBasedAction.addValueChangeHandler(booleanValueChangeEvent -> workflowTimeBasedActionDate.setVisible(booleanValueChangeEvent.getValue()));


        container = new Div();
        GRow row = new GRow();
        row.addAll(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.provider(), provider)), new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.template(), template)));
        container.add(row);
        addWidget(phone, wfmStrings.individualRecipient());

        taskRecipientsFormGroup = new FormGroup(wfmStrings.recipient(), taskSMSrecipients);
        taskRecipientsFormGroup.setVisible(false);
        add(taskRecipientsFormGroup);

        add(container);
        addWidget(descriptionPanel, wfmStrings.content());
//        addWidget(Utils.getInHorizontalPanel(5, 100, false, workflowTimeBasedAction, workflowTimeBasedActionDate), wfmStrings.timeBasedAction());

        open();
    }

    private void drawDescriptionPanel() {
        descriptionPanel = new MaterialPanel();

        MaterialPanel firstRow = new MaterialPanel("grid-row");

        MaterialPanel dropdownPanel = new MaterialPanel("col-9");
        MaterialPanel attributePanel = new MaterialPanel("col-3");
        firstRow.add(dropdownPanel);
        firstRow.add(attributePanel);

        fieldCodes = new DataListBox();
        fieldCodes.ensureDebugId(debug_id + "fieldCodes");
        fieldCodes.addValueChangeHandler(changeEvent -> {
            if (changeEvent.getValue() != null && changeEvent.getValue().getDescription() != null) {
                attribute.setHTML(changeEvent.getValue().getDescription());
            } else {
                attribute.setHTML(wfmStrings.noAttributesSelected());
            }
        });
        dropdownPanel.add(fieldCodes);

        attribute = new HTML(wfmStrings.noAttributesSelected());
        attribute.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        attribute.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        attributePanel.add(attribute);

        MaterialPanel secondRow = new MaterialPanel("grid-row");
        secondRow.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
        MaterialPanel contentPanel = new MaterialPanel("col-12");
        secondRow.add(contentPanel);

        content = new TextArea2(true, 5000);
        content.ensureDebugId(debug_id + "content");
        content.setHeight(200);
        contentPanel.add(content);

        descriptionPanel.add(firstRow);
        descriptionPanel.add(secondRow);
    }

    private void addButtons() {
        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        btnSave = new WfmButton2(objectID == null ? wfmStrings.save() : wfmStrings.update(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        btnSave.ensureDebugId("save_button");
        addButton(btnSave);
    }

    private void fillFields() {
        LoadingPanel.loading(true, WorkflowSMSAlertView.this);
        ProfileService.App.get().getWorkflowSMSAlert(objectID, workflowID, new AbstractAsyncCallback<WorkflowSMSAlert>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false, WorkflowSMSAlertView.this);
            }

            @Override
            public void onSuccess(WorkflowSMSAlert result) {
                LoadingPanel.loading(false, WorkflowSMSAlertView.this);
                alert = result;
                setValues();
                initColumns(alert.getWorkflow().getModule());
            }
        });
    }

    private void setValues() {
        provider.setItems(alert.getProviders());
        if (alert.getProviderID() != null) {
            provider.setSelected(alert.getProviderID());
        }
        template.setItems(alert.getSmsTemplates());
        if (alert.getTemplateID() != null) {
            template.setSelected(alert.getTemplateID());
        }
        if (alert.getContent() != null) {
            content.setText(alert.getContent());
        }
        if (alert.getPhone() != null) {
            phone.setText(alert.getPhone());
        }



        if (Constants.TASK_EMPLOYEES.equals(alert.getTaskSMSrecipientType())) {
            taskSMSrecipients.setSelected(new SelectItem(2, Constants.TASK_EMPLOYEES));
        } else if (Constants.TASK_CREATOR.equals(alert.getTaskSMSrecipientType())) {
            taskSMSrecipients.setSelected(new SelectItem(1, Constants.TASK_CREATOR));
        } else {
            taskSMSrecipients.setSelectedNullLabel();
        }

        boolean isTask = Constants._WORKFLOW_MODULE_TASK.equals(alert.getWorkflow().getModule());
        taskRecipientsFormGroup.setVisible(isTask);
        workflowTimeBasedAction.setValue(alert.isWorkflowActionTimeBased(), true);
        workflowTimeBasedActionDate.setStartDate(alert.getWorkflowActionStartTime());
        workflowTimeBasedActionDate.setDueDate(alert.getWorkflowActionStartTimeUnit());
        workflowTimeBasedActionDate.setDueDateGranularity(alert.getWorkflowActionStartTimeGranularity());
    }

    public boolean validate() {
        int errors = 0;
        if (!Validation.validateDataListBoxRequired(provider)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void getValues() {
        alert.setProviderID(provider.getSelectedId());
        if (template.getSelectedItem() != null) {
            alert.setTemplateID(template.getSelectedId());
        }
        alert.setPhone(phone.getText());
        alert.setContent(content.getText());
        alert.setWorkflowActionTimeBased(workflowTimeBasedAction.getValue());
        alert.setWorkflowActionStartTime(workflowTimeBasedActionDate.getWorkflowStartDate());
        alert.setWorkflowActionStartTimeUnit(workflowTimeBasedActionDate.getWorkflowDueDateUnit());
        alert.setWorkflowActionStartTimeGranularity(workflowTimeBasedActionDate.getWorkflowDueDateGranularity());
        alert.setTaskSMSrecipientType(
                taskRecipientsFormGroup.isVisible() && taskSMSrecipients.getSelectedValue() != null
                        ? taskSMSrecipients.getSelectedValue().getName()
                        : null
        );  }

    private void save() {
        if (!validate()) {
            return;
        }
        btnSave.setEnabled(false);
        getValues();
        LoadingPanel.loading(true, WorkflowSMSAlertView.this);
        ProfileService.App.get().saveWorkflowSMSAlert(alert, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false, WorkflowSMSAlertView.this);
                btnSave.setEnabled(true);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false, WorkflowSMSAlertView.this);
                btnSave.setEnabled(true);
                Info.show(wfmMessages.smsAlertSuccesfully(objectID == null ? wfmStrings.saved() : Property.get(Constants.TASK, wfmStrings.updated(), wfmStrings.task())), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SMS_ALERT_ADD_EDIT, result, WorkflowSMSAlertView.this);
                close();
            }
        });
    }

    private void initColumns(String code) {
        if (code != null) {
            AllInOneService.App.get().getDefaultModelForm(getFormIDOfModule(code), new AsyncCallback<ModelForm>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ModelForm modelForm) {
                    fields.clear();
                    if (modelForm != null && !modelForm.getFields().isEmpty()) {
                        for (ModelField field : modelForm.getFields()) {
                            if (field.isEntityField()) {
                                fields.put(field.getField_ID(), field);
                            }
                        }
                    }
                    notifyAllFieldRelateds(modelForm.getAttributes());
                }
            });
        }
    }

    private void notifyAllFieldRelateds(SelectItem[] additionalAttributes) {
        SelectItem[] items = getColumnsAsReferenceItems(additionalAttributes);
        if (workflowTimeBasedActionDate != null) {
            workflowTimeBasedActionDate.setDateItems(getDateTypeColums());
        }
        fieldCodes.setItems(items);
        fieldCodes.addValueChangeHandler(changeEvent -> {
            attribute.setText("");
            if (fieldCodes.getSelectedItem() != null && fieldCodes.getSelectedItem().getDescription() != null) {
                attribute.setText(fieldCodes.getSelectedItem().getDescription());
            }
        });
    }

    private ArrayList<SelectItem> getDateTypeColums() {
        ArrayList<SelectItem> result = new ArrayList<>();
        int i = 4;
        if (fields != null && !fields.isEmpty()) {
            for (Map.Entry<String, ModelField> entry : fields.entrySet()) {
                if (entry.getValue().getType() != null && DATA_TYPE_DATE.equals(entry.getValue().getType())) {
                    String localized = getLocalizer().localizeByFieldID(entry.getValue().getForm_ID(), entry.getValue().getField_ID());
                    String name = localized != null ? localized : (entry.getValue().getField_ID().contains("string_value") || entry.getValue().getField_ID().contains("double_value") || entry.getValue().getField_ID().contains("date_value") ? entry.getValue().getLabel() : entry.getValue().getField_ID());
                    result.add(new SelectItem(i++, name, entry.getValue().getField_ID()));
                }
            }
        }
        result.sort(Comparator.comparing(SelectItem::getName));
        return result;
    }

    private SelectItem[] getColumnsAsReferenceItems(SelectItem[] additionalAttributes) {
        ArrayList<SelectItem> result = new ArrayList<>();
        if (fields != null && !fields.isEmpty()) {
            for (Map.Entry<String, ModelField> entry : fields.entrySet()) {
                String localized = getLocalizer().localizeByFieldID(entry.getValue().getForm_ID(), entry.getValue().getField_ID());
                String name = localized != null ? localized : (entry.getValue().getField_ID().contains("string_value") || entry.getValue().getField_ID().contains("double_value") || entry.getValue().getField_ID().contains("date_value") ? entry.getValue().getLabel() : entry.getValue().getField_ID());
                String description = entry.getValue().getField_ID() != null ? ("${" + entry.getValue().getField_ID().toLowerCase() + "}") : entry.getValue().getField_ID();
                result.add(new SelectItem(entry.getValue().getObjectID(), name, description));
            }
        }
        if (additionalAttributes != null && additionalAttributes.length > 0) {
            result.addAll(Arrays.asList(additionalAttributes));
        }
        result.sort(Comparator.comparing(SelectItem::getName));
        return result.toArray(new SelectItem[]{});
    }

    protected Localize getLocalizer() {
        if (localize == null) {
            localize = new Localize();
        }
        return localize;
    }

    private String getFormIDOfModule(String code) {
        if (code != null && !"".equals(code)) {
            if (WorkflowRule._WORKFLOW_MODULE_MANUAL_JOURNAL.equals(code)) {
                return LayoutRPC.MANUAL_JOURNAL_FORM;
            } else if (WorkflowRule._WORKFLOW_MODULE_TASK.equals(code)) {
                return LayoutRPC.TASK_MAX_FORM;
            }
            //dont' forget the code of the module reference must be build like this. "_WORKFLOW_MODULE_" + formID.replaceAll("_FORM", "");
            //for example :module for lead. "_WORKFLOW_MODULE_" + LEAD_FORM.replaceAll("_FORM", "") = "_WORKFLOW_MODULE_LEAD";
            //why we need formID? Because all fields for this form is in form(database.tablename = model).
            return code.replace("_WORKFLOW_MODULE_", "") + "_FORM";
        }
        return null;
    }
}
