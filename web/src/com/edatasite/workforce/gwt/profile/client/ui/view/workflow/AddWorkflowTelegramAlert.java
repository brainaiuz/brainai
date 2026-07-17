package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TelegramChatLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowTelegramAlert;
import com.edatasite.workforce.gwt.profile.client.ui.view.WorkflowDateSelecter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AddWorkflowTelegramAlert extends CustomForm2 implements Constants, Colapse {
    private DataListBox telegramBots;
    private TelegramChatLookUp telegramChatLookUp;
    private TextArea2 content;
    private WfmButton2 btnSave;
    private DataListBox fieldCodes;
    private KpiSwitcher workflowTimeBasedAction;
    private WorkflowDateSelecter workflowTimeBasedActionDate;
    private HTML attribute;
    private MaterialPanel descriptionPanel;
    private FooterUploadPanel footerUploadPanel;
    private TextBox receiverAttributes;

    private final Integer objectId;
    private final Integer workflowId;
    private WorkflowTelegramAlert alert;
    private final LinkedHashMap<String, ModelField> fields = new LinkedHashMap<>();
    private Localize localize;

    public AddWorkflowTelegramAlert(Integer objectId, Integer workflowId) {
        super("workflowtelegramalert", wfmStrings.telegramAlert());
        this.objectId = objectId;
        this.workflowId = workflowId;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        telegramBots = new DataListBox();
        telegramBots.addValueChangeHandler(valueChangeEvent -> {
            telegramChatLookUp.clear();
            telegramChatLookUp.refreshOracle(true);
            telegramChatLookUp.clearOracleItems();
            telegramChatLookUp.setAccessToken(valueChangeEvent.getValue().getDescription());
        });
        telegramChatLookUp = new TelegramChatLookUp();

        receiverAttributes = new TextBox();

        drawContentPanel();

        workflowTimeBasedAction = new KpiSwitcher("", null, false);
        workflowTimeBasedAction.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        workflowTimeBasedActionDate = new WorkflowDateSelecter(false, true);
        workflowTimeBasedActionDate.setVisible(false);
        workflowTimeBasedAction.addValueChangeHandler(booleanValueChangeEvent -> workflowTimeBasedActionDate.setVisible(booleanValueChangeEvent.getValue()));

        addField(WORKFLOW_TELEGRAM_ALERT_FORM.TELEGRAM_BOT, telegramBots, wfmStrings.telegramBot());
        addField(WORKFLOW_TELEGRAM_ALERT_FORM.RECEIVER, telegramChatLookUp, wfmStrings.recipients());
        addField(WORKFLOW_TELEGRAM_ALERT_FORM.RECIPIENT_ATTRIBUTES, receiverAttributes, wfmStrings.individualRecipient());
        addField(WORKFLOW_TELEGRAM_ALERT_FORM.CONTENT, descriptionPanel, wfmStrings.attributes());
        addTitleField(WORKFLOW_TIME_BASED_HEADER, wfmStrings.timeBasedAction());
        addField(WORKFLOW_TIME_BASED, new InputGroup(workflowTimeBasedAction, workflowTimeBasedActionDate), getTitle(wfmStrings.executionTime()));
        show();
    }

    private void drawContentPanel() {
        descriptionPanel = new MaterialPanel();

        MaterialPanel firstRow = new MaterialPanel("grid-row");

        MaterialPanel dropdownPanel = new MaterialPanel("col-9");
        MaterialPanel attributePanel = new MaterialPanel("col-3");
        firstRow.add(dropdownPanel);
        firstRow.add(attributePanel);

        fieldCodes = new DataListBox();
        fieldCodes.addStyleName(DEFAULT_WIDTH);
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

        content = new TextArea2(4096, true);
        content.setWidth("100%");
        content.setHeight(230);
        contentPanel.add(content);

        descriptionPanel.add(firstRow);
        descriptionPanel.add(secondRow);

    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void addButtons() {
        footerUploadPanel = new FooterUploadPanel(Constants.F_TELEGRAM, objectId, true);
        footer.addToLeftSide(footerUploadPanel);

        btnSave = new WfmButton2(objectId == null ? wfmStrings.save() : wfmStrings.update(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        btnSave.ensureDebugId("save_button");
        addButton(btnSave);
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ProfileService.App.get().getWorkflowTelegramAlert(objectId, workflowId, new AsyncCallback<WorkflowTelegramAlert>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(WorkflowTelegramAlert workflowTelegramAlert) {
                LoadingPanel.loading(false);
                alert = workflowTelegramAlert;
                setValues();
                initColumns(workflowTelegramAlert.getWorkflowRule().getModule());
            }
        });
    }

    private void setValues() {
        content.setText(alert.getMessage());
        telegramBots.setItems(alert.getTelegramBots());

        if (alert.getTelegramChatListItems() != null) {
            telegramChatLookUp.setSelectedItems(alert.getTelegramChatListItems().stream().map(x -> new SelectItem(x.getObjectId(), x.getChatName(), x.getTelegramBotToken())).toArray(SelectItem[]::new));
        }
        if (alert.getTelegramBot() != null) {
            telegramBots.setSelected(alert.getTelegramBot().getId());
            telegramChatLookUp.setAccessToken(alert.getTelegramBot().getToken());
        }
        receiverAttributes.setText(alert.getReceiverAttributes());

        workflowTimeBasedAction.setValue(alert.isWorkflowActionTimeBased(), true);
        workflowTimeBasedActionDate.setStartDate(alert.getWorkflowActionStartTime());
        workflowTimeBasedActionDate.setDueDate(alert.getWorkflowActionStartTimeUnit());
        workflowTimeBasedActionDate.setDueDateGranularity(alert.getWorkflowActionStartTimeGranularity());

    }

    private void getValues() {
        alert.setTelegramBot(new TelegramSettingsItem(telegramBots.getSelectedItem().getId()));
        if (telegramChatLookUp.getSelectedItems() != null && !telegramChatLookUp.getSelectedItems().isEmpty()) {
            List<TelegramChatListItem> telegramChatListItems = new ArrayList<>();

            for (SelectItem selectItem : telegramChatLookUp.getSelectedItems()) {
                telegramChatListItems.add(new TelegramChatListItem(selectItem.getId()));
            }
            alert.setTelegramChatListItems(telegramChatListItems);
        }
        alert.setMessage(content.getText());
        alert.setWorkflowActionTimeBased(workflowTimeBasedAction.getValue());
        alert.setWorkflowActionStartTime(workflowTimeBasedActionDate.getWorkflowStartDate());
        alert.setWorkflowActionStartTimeUnit(workflowTimeBasedActionDate.getWorkflowDueDateUnit());
        alert.setWorkflowActionStartTimeGranularity(workflowTimeBasedActionDate.getWorkflowDueDateGranularity());
        alert.setAttachments(footerUploadPanel.getAttachedFiles());
        alert.setReceiverAttributes(receiverAttributes.getText());
    }

    private void save() {
        if (!validate()) {
            return;
        }
        btnSave.setEnabled(false);
        getValues();
        LoadingPanel.loading(true);
        ProfileService.App.get().saveWorkflowTelegramAlert(alert, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                btnSave.setEnabled(true);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                btnSave.setEnabled(true);
                Info.show(wfmMessages.smsAlertSuccesfully(objectId == null ? wfmStrings.saved() : Property.get(Constants.TASK, wfmStrings.updated(), wfmStrings.task())), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TELEGRAM_ALERT_ADD_EDIT, result, AddWorkflowTelegramAlert.this);
                closeTab();
            }
        });
    }


    public boolean validate() {
        boolean error = !Validation.validateDataListBoxRequired(telegramBots);

        if (markAsError(telegramChatLookUp, telegramChatLookUp.getSelectedItems() == null || telegramChatLookUp.getSelectedItems().size() == 0) > 0 && (receiverAttributes.getText() == null || "".equals(receiverAttributes.getText()))) {
            error = true;
        }

        if (!Validation.validateTextAreaRequired(content)) {
            error = true;
        }

        return !error;
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
                    if (modelForm != null && modelForm.getFields().size() > 0) {
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
        if (fields != null && fields.size() > 0) {
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
        if (fields != null && fields.size() > 0) {
            for (Map.Entry<String, ModelField> entry : fields.entrySet()) {
                String localized = getLocalizer().localizeByFieldID(entry.getValue().getForm_ID(), entry.getValue().getField_ID());
                String name = localized != null ? localized : (entry.getValue().getField_ID().contains("string_value") || entry.getValue().getField_ID().contains("double_value") || entry.getValue().getField_ID().contains("date_value") ? entry.getValue().getLabel() : entry.getValue().getField_ID());
                String description = entry.getValue().getField_ID().split(",")[0];
                description = description != null ? ("${" + description.toLowerCase() + "}") : null;
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

    @Override
    protected String getFormID() {
        return LayoutRPC.WORKFLOW_TELEGRAM_ALERT_FORM;
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
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
