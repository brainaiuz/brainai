package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.EnumStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.RecurringWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.view.WorkflowFilterTable;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class AddWorkflowRule extends CustomForm2 implements Constants, Colapse {
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected static final ProfileServiceAsync profileService = ProfileService.App.get();
    protected static final EnumStrings enumStrings = GWT.create(EnumStrings.class);

    private TextBox name;
    private DataListBox module;
    private KpiSwitcher status;
    private TextArea description;

    private VerticalPanel executionCriteria;
    private KpiRadioButton create;
    private KpiRadioButton edit;
    private KpiRadioButton createOrEdit;
    private KpiRadioButton delete;
    private KpiRadioButton updateField;
    private DataListBox executionCriteriaUpdateField;
    private MaterialPanel updateFieldPanel;

    private WorkflowFilterTable ruleCriteria;
    private RecurringWidget recurringWidget;

    protected WorkflowRule item;
    protected Integer objectId;
    protected String fromType;

    private final String nickDebugId = "add_workflow_view_";
    private boolean saveAndClose = false;
    protected boolean recurrence = false;
    private final LinkedHashMap<String, ModelField> fields = new LinkedHashMap<>();

    public AddWorkflowRule(boolean recurrence) {
        super("workflow", settingsStrings.workflow());
        this.recurrence = recurrence;
    }

    public AddWorkflowRule(Integer objectId, String fromType, boolean recurrence) {
        this(recurrence);
        this.objectId = objectId;
        this.fromType = fromType;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    protected void registerFields() {
        name = new TextBox();
        name.addStyleName(DEFAULT_WIDTH);
        name.ensureDebugId(this.nickDebugId + "name");
        module = new DataListBox();
        module.addStyleName(DEFAULT_WIDTH);
        module.ensureDebugId(this.nickDebugId + "module");
        module.addValueChangeHandler(changeEvent -> {
            initColumns(module.getSelectedItem() != null ? module.getSelectedItem().getReferenceCode() : null);
        });
        status = new KpiSwitcher(null, null, true);
        status.addStyleName(DEFAULT_WIDTH);
        status.ensureDebugId(this.nickDebugId + "active");
        description = new TextArea();
        description.addStyleName(DEFAULT_WIDTH);
        description.ensureDebugId(this.nickDebugId + "startDate");

        executionCriteria = new VerticalPanel();
        executionCriteria.addStyleName("spacing5-padding5");
        executionCriteria.setSpacing(5);
        create = new KpiRadioButton("EXECUTION_CRITERIA");
        create.setText(enumStrings._WORKFLOW_EXECUTION_CRITERIA_CREATE());
        create.addValueChangeHandler(valueChangeEvent -> executionCriteria.remove(updateFieldPanel));
        create.setValue(true);
        edit = new KpiRadioButton("EXECUTION_CRITERIA");
        edit.setText(enumStrings._WORKFLOW_EXECUTION_CRITERIA_EDIT());
        edit.addValueChangeHandler(valueChangeEvent -> executionCriteria.remove(updateFieldPanel));
        createOrEdit = new KpiRadioButton("EXECUTION_CRITERIA");
        createOrEdit.setText(enumStrings._WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT());
        createOrEdit.addValueChangeHandler(valueChangeEvent -> executionCriteria.remove(updateFieldPanel));
        delete = new KpiRadioButton("EXECUTION_CRITERIA");
        delete.setText(enumStrings._WORKFLOW_EXECUTION_CRITERIA_REMOVE());
        delete.addValueChangeHandler(valueChangeEvent -> executionCriteria.remove(updateFieldPanel));
        updateField = new KpiRadioButton("EXECUTION_CRITERIA");
        updateField.setText(enumStrings._WORKFLOW_EXECUTION_CRITERIA_UPDATE_SPECIFIED_FIELD());
        updateField.addValueChangeHandler(valueChangeEvent -> {
            if (valueChangeEvent.getValue()) {
                executionCriteria.add(updateFieldPanel);
            }
        });
        executionCriteria.add(create);
        executionCriteria.add(edit);
        executionCriteria.add(createOrEdit);
        executionCriteria.add(delete);
        executionCriteria.add(updateField);
        executionCriteriaUpdateField = new DataListBox();
        executionCriteriaUpdateField.addStyleName(DEFAULT_WIDTH);
        executionCriteriaUpdateField.ensureDebugId("executionCriteriaSelectOption");
        executionCriteriaUpdateField.setWithoutNullLabel(true);
        executionCriteriaUpdateField.setItems(new SelectItem[0]);

        updateFieldPanel = new MaterialPanel("margin-top");
        updateFieldPanel.add(executionCriteriaUpdateField);

        ruleCriteria = new WorkflowFilterTable();
        recurringWidget = new RecurringWidget(SchedulerConstant.RECURRING_WORKFLOW_FORM, GBox.STYLE_NO_PADDING);

        addField(WORKFLOW_FORM.NAME, name, getTitle(wfmStrings.name(), true));
        addField(WORKFLOW_FORM.MODULE, module, getTitle(wfmStrings.apps(), true));
        addField(WORKFLOW_FORM.STATUS, status, getTitle(wfmStrings.status()));
        addField(WORKFLOW_FORM.DESCRIPTION, description, getTitle(wfmStrings.description()));
        addField(WORKFLOW_FORM.EXECUTION_CRITERIA, recurrence ? recurringWidget : executionCriteria, recurrence ? getTitle(settingsStrings.recurrenceCriteria()) : getTitle(wfmStrings.executionCriteria()));
        addField(WORKFLOW_FORM.RULE_CRITERIA, ruleCriteria, getTitle(wfmStrings.ruleCriteria()));
        show();
    }

    @Override
    protected void addButtons() {
        MaterialLink save = new MaterialLink(wfmStrings.save());
        MaterialSplitButton splitButton = new MaterialSplitButton(save);
        save.addClickHandler(event -> {
            saveAndClose = true;
            save();
        });

        MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
        saveAdd.addClickHandler(event -> save());
        splitButton.addItem(saveAdd);
        addButton(splitButton);

    }

    @Override
    protected void getDataToFillFields() {
        profileService.editWorkflowRule(objectId, new AbstractAsyncCallback<WorkflowRule>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(WorkflowRule result) {
                item = result;
                if (item != null) {
                    setValuesToWidgets();
                    initColumns(item.getModule());
                }
            }
        });
    }

    protected void setValuesToWidgets() {
        module.setItems(item.getModules());
        if (objectId != null) {

            name.setValue(item.getName());
            description.setValue(item.getDescription());
            status.setValue(item.isActive());
            module.setSelectedByCode(item.getModule());

            if (recurrence && item.getRecurrenceJobItem() != null) {
                recurringWidget.setData(item.getRecurrenceJobItem());
            } else {
                if (WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE.equals(item.getExecutionCriteria())) {
                    create.setValue(true);
                } else if (WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_EDIT.equals(item.getExecutionCriteria())) {
                    edit.setValue(true);
                } else if (WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT.equals(item.getExecutionCriteria())) {
                    createOrEdit.setValue(true);
                } else if (WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_REMOVE.equals(item.getExecutionCriteria())) {
                    delete.setValue(true);
                } else if (WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_UPDATE_SPECIFIED_FIELD.equals(item.getExecutionCriteria())) {
                    updateField.setValue(true);
                    executionCriteria.add(updateFieldPanel);
                    if (item.getExecutionCriteriaUpdateField() != null) {
                        executionCriteriaUpdateField.setSelectedByCode(item.getExecutionCriteriaUpdateField());
                    }
                }
            }
        }
    }

    private void setValuesToItem() {
        item.setName(name.getValue());
        item.setDescription(description.getValue());
        item.setActive(status.getValue());
        item.setModule(module.getSelectedItem(true).getReferenceCode());
        if (recurrence && recurringWidget.getData() != null) {
            item.setRecurrenceJobItem(recurringWidget.getData());
            item.setExecutionCriteria(WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_RECURRENCE);
        } else {
            if (create.getValue()) {
                item.setExecutionCriteria(WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE);
            } else if (edit.getValue()) {
                item.setExecutionCriteria(WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_EDIT);
            } else if (createOrEdit.getValue()) {
                item.setExecutionCriteria(WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT);
            } else if (delete.getValue()) {
                item.setExecutionCriteria(WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_REMOVE);
            } else if (updateField.getValue()) {
                item.setExecutionCriteria(WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_UPDATE_SPECIFIED_FIELD);
                item.setExecutionCriteriaUpdateField(executionCriteriaUpdateField.getSelectedItem(true).getReferenceCode());
            }
        }
        item.setConditions(ruleCriteria.getConditions());
        item.setPattern(ruleCriteria.getPattern());
        item.setDynamicCondition(ruleCriteria.isDynamicContion());
        item.setDynamicConditionQuery(ruleCriteria.getDynamicConditionQuery());
    }

    private void save() {
        if (!validate()) {
            return;
        }
        setValuesToItem();
        LoadingPanel.loading(true);
        profileService.saveWorkflowRule(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(item.getObjectID() != null ? WfmUiEventType.ON_WORKFLOW_UPDATE : WfmUiEventType.ON_WORKFLOW_ADD, item, AddWorkflowRule.this);
                item.setObjectID(result);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.rules()), Info.Type.INFO);
                onShellOk();
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        errors += markAsError(name, Utils.isNullOrEmpty(name.getText()));
        errors += markAsError(module, module != null && module.getSelectedItem() == null);

        if (recurrence && !recurringWidget.validate()) {
            errors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return false;
        }
        return true;
    }

    private void initColumns(String code) {
        if (code != null) {
            service.getDefaultModelForm(getFormIDOfModule(code), new AsyncCallback<ModelForm>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(ModelForm modelForm) {
                    fields.clear();
                    if (modelForm != null && modelForm.getFields().size() > 0) {
                        for (ModelField field : modelForm.getFields()) {
                            if (field.isUsableByWorkflow() && !field.isWorkflowAttribute()) {
                                fields.put(field.getField_ID(), field);
                            }
                        }
                    }
                    notifyAllFieldRelateds(modelForm != null && modelForm.getAdditionalFields() != null ? modelForm.getAdditionalFields() : null);
                }
            });
        }
    }

    private void notifyAllFieldRelateds(String[] additionalFields) {
        if (ruleCriteria != null) {
            ruleCriteria.setFields(fields);
            ruleCriteria.setAdditionalFields(additionalFields);
            ruleCriteria.clear();
            if (item != null) {
                ruleCriteria.setConditions(item.getConditions(), item.isDynamicCondition());
                ruleCriteria.setDynamicConditionQuery(item.getDynamicConditionQuery());
                ruleCriteria.setPattern(item.getPattern());
            }
        }
        if (executionCriteriaUpdateField != null) {
            executionCriteriaUpdateField.setItems(getColumnsAsReferenceItems());
            if (item.getExecutionCriteriaUpdateField() != null && item.getExecutionCriteria() != null && WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_UPDATE_SPECIFIED_FIELD.equals(item.getExecutionCriteria())) {
                executionCriteriaUpdateField.setSelectedByCode(item.getExecutionCriteriaUpdateField());
            }
        }
    }

    public String getFormIDOfModule(String code) {
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

    public SelectItem[] getColumnsAsReferenceItems() {
        ArrayList<SelectItem> result = new ArrayList<>();
        if (fields != null && fields.size() > 0) {
            for (Map.Entry<String, ModelField> entry : fields.entrySet()) {
                GWT.log("disave : "+ entry.getValue().isDisableUpdate());
                if (!entry.getValue().isDisableUpdate()) {
                    ReferenceItem referenceItem = entry.getValue().asReferenceItem();
                    String localized = null;
                    if (!Utils.isNullOrEmpty(entry.getValue().getDynamicLabel())) {
                        localized = entry.getValue().getDynamicLabel();
                    } else if (entry.getValue().isIsCustomField()) {
                        localized = entry.getValue().getLabel();
                    } else {
                        localized = getLocalizer().localizeByFieldID(entry.getValue().getForm_ID(), entry.getValue().getField_ID());
                    }
                    referenceItem.setName(localized != null ? localized : referenceItem.getName());
                    result.add(referenceItem);
                }
            }
        }
        result.sort(Comparator.comparing(SelectItem::getName));
        return result.toArray(new SelectItem[]{});
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            if (WORKFLOW_FORM.NAME.equals(fieldID)) {
                return wfmStrings.name();
            } else if (WORKFLOW_FORM.MODULE.equals(fieldID)) {
                return wfmStrings.apps();
            } else if (WORKFLOW_FORM.STATUS.equals(fieldID)) {
                return wfmStrings.status();
            } else if (WORKFLOW_FORM.DESCRIPTION.equals(fieldID)) {
                return wfmStrings.description();
            } else if (WORKFLOW_FORM.EXECUTION_CRITERIA.equals(fieldID)) {
                return recurrence ? settingsStrings.recurrenceCriteria() : wfmStrings.executionCriteria();
            } else if (WORKFLOW_FORM.RULE_CRITERIA.equals(fieldID)) {
                return wfmStrings.ruleCriteria();
            } else if (WORKFLOW_FORM.RULE_INFORMATION.equals(fieldID)) {
                return wfmStrings.ruleInformation();
            }
        }
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.WORKFLOW_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return "icon-workflow icon-workflow-list";
    }

    private void onShellOk() {
        if (saveAndClose) {
            closeTab();
            if (objectId == null && item.getObjectID() != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("workflow|summary/" + item.getObjectID() + "/" + item.getModule(), item.getName());
            }
        } else {
            reinit();
        }
    }

    public void reinit() {
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
