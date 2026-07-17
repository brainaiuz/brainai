package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.RecurringWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TelegramChatLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowTelegramAlert;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_CHART;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_CREATED_BY;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_CREATION_DATE;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_CSV;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_CSV_CAPTION;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_CURRENT_TIME;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_DESCRIPTION;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_FOLDER;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_MODIFICATION_DATE;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_MODIFIED_BY;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_NAME;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_PDF;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_PDF_CAPTION;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_RULE_NAME;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_XLS;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TELEGRAM_REPORTING_RECURRENCE.REPORT_XLS_CAPTION;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRENCE_TYPE_DAILY;

public class TelegramReportingRecurrenceView extends CustomForm2 implements Constants, Colapse {
    private final String[] attributes = {REPORT_RULE_NAME, REPORT_CSV, REPORT_PDF, REPORT_XLS, REPORT_CHART, REPORT_DESCRIPTION, REPORT_FOLDER, REPORT_NAME, REPORT_CREATION_DATE, REPORT_CREATED_BY, REPORT_MODIFICATION_DATE, REPORT_MODIFIED_BY, REPORT_CURRENT_TIME, REPORT_XLS_CAPTION, REPORT_PDF_CAPTION, REPORT_CSV_CAPTION, REPORT_CSV_CAPTION};

    private final Integer reportId;
    private final String ruleName;
    private final Map<String, ModelField> fields = new LinkedHashMap<>();
    private String reportType;
    private TextBox rule;
    private DataListBox telegramBots;
    private TelegramChatLookUp telegramChatLookUp;
    private KpiSwitcher status;
    private TextArea2 content;
    private WfmButton2 btnSave;
    private WfmButton2 btnDelete;
    private DataListBox fieldCodes;
    private RecurringWidget recurringWidget;
    private HTML attribute;
    private MaterialPanel descriptionPanel;
    private WorkflowTelegramAlert alert;
    private Localize localize;
    private TelegramRecurrenceMessage telegramRecurrenceMessage;
    private boolean isNew = true;

    public TelegramReportingRecurrenceView(Integer reportId, String ruleName) {
        super("savereportalert", wfmStrings.telegramAlert());
        this.reportId = reportId;
        this.ruleName = ruleName;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        initializeFields();
        drawContentPanel();
        drawFields();
        show();
    }

    private void initializeFields() {
        telegramBots = new DataListBox();
        telegramBots.addValueChangeHandler(valueChangeEvent -> {
            telegramChatLookUp.clear();
            telegramChatLookUp.refreshOracle(true);
            telegramChatLookUp.clearOracleItems();
            telegramChatLookUp.setAccessToken(valueChangeEvent.getValue().getDescription());
        });
        telegramChatLookUp = new TelegramChatLookUp();
        rule = new TextBox();
        status = new KpiSwitcher(null, null, true);
        recurringWidget = new RecurringWidget(11);
        if (ruleName != null) {
            isNew = false;
        }
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
        attribute.getElement().getStyle().setCursor(Style.Cursor.POINTER);

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

    private void drawFields() {
        addField(WORKFLOW_TELEGRAM_ALERT_FORM.RULE_NAME, rule, getTitle(wfmStrings.name(), true));
        addField(WORKFLOW_TELEGRAM_ALERT_FORM.TELEGRAM_BOT, telegramBots, getTitle(wfmStrings.telegramBot(), true));
        addField(WORKFLOW_TELEGRAM_ALERT_FORM.RECEIVER, telegramChatLookUp, getTitle(wfmStrings.recipients(), true));
        addField(WORKFLOW_TELEGRAM_ALERT_FORM.STATUS, status, getTitle(wfmStrings.status()));
        addField(WORKFLOW_TELEGRAM_ALERT_FORM.CONTENT, descriptionPanel, getTitle(wfmStrings.attributes(), true));
        addField(WORKFLOW_TIME_BASED, recurringWidget, null);
    }

    @Override
    protected void getDataToFillFields() {
        ProfileService.App.get().getWorkflowTelegramAlert(new AsyncCallback<WorkflowTelegramAlert>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(WorkflowTelegramAlert workflowTelegramAlert) {
                LoadingPanel.loading(false);
                alert = workflowTelegramAlert;
                getReportType();
            }
        });
    }

    public void getReportType() {
        ReportingService.App.get().getReportType(reportId, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(String type) {
                reportType = type;
                initializeFieldValues();
                ModelForm modelForm = generateAttributes();
                notifyAllFieldRelateds(modelForm.getAttributes());
            }
        });
    }

    private ModelForm generateAttributes() {
        ModelForm modelForm = new ModelForm();
        Arrays.stream(attributes).forEach((attribute) -> {
            ModelField field = new ModelField();
            field.setField_ID(attribute);
            modelForm.getFields().add(field);
        });
        if (reportType.equals("SUMMARY")) {
            ModelField field = new ModelField();
            field.setField_ID(REPORT_SUMMARY);
            modelForm.getFields().add(field);
        }
        modelForm.getFields().stream().peek(field -> field.setEntityField(true)).forEach(field -> fields.put(field.getField_ID(), field));
        return modelForm;
    }


    private void notifyAllFieldRelateds(SelectItem[] additionalAttributes) {
        SelectItem[] items = getColumnsAsReferenceItems(additionalAttributes);
        fieldCodes.setItems(items);
        fieldCodes.addValueChangeHandler(changeEvent -> {
            Utils.copyToClipBoard(attribute.getHTML());
            Info.show(wfmStrings.successfullyCopiedLinkToClipboard(), Info.Type.INFO, Info.Position.TOP_RIGHT);
            attribute.setText("");
            if (fieldCodes.getSelectedItem() != null && fieldCodes.getSelectedItem().getDescription() != null) {
                attribute.setText(fieldCodes.getSelectedItem().getDescription());
            }
        });
    }

    private SelectItem[] getColumnsAsReferenceItems(SelectItem[] additionalAttributes) {
        List<SelectItem> result = new ArrayList<>();
        for (Map.Entry<String, ModelField> entry : fields.entrySet()) {
            String localized = getLocalizer().localizeByFieldID(entry.getValue().getForm_ID(), entry.getValue().getField_ID());
            String name = localized != null ? localized : (entry.getValue().getField_ID().contains("string_value") || entry.getValue().getField_ID().contains("double_value") || entry.getValue().getField_ID().contains("date_value") ? entry.getValue().getLabel() : entry.getValue().getField_ID());
            String description = entry.getValue().getField_ID() != null ? entry.getValue().getField_ID().toLowerCase() : entry.getValue().getField_ID();
            result.add(new SelectItem(entry.getValue().getObjectID(), name, description));
        }
        if (additionalAttributes != null && additionalAttributes.length > 0) {
            result.addAll(Arrays.asList(additionalAttributes));
        }
        result.sort(Comparator.comparing(SelectItem::getName));
        return result.toArray(new SelectItem[]{});
    }

    public void initializeFieldValues() {
        telegramBots.setItems(alert.getTelegramBots());

        if (isNew) {
            telegramRecurrenceMessage = new TelegramRecurrenceMessage();

            RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
            recurrenceJobItem.setType(RECURRENCE_TYPE_DAILY);
            recurrenceJobItem.setInterval(1);

            Date now = new Date();
            Date afterTwoMinutes = new Date();
            afterTwoMinutes.setMinutes(now.getMinutes() + 3);
            afterTwoMinutes.setHours(now.getHours());
            recurrenceJobItem.setStartDate(afterTwoMinutes);
            recurringWidget.setData(recurrenceJobItem);
        } else {
            getExistingRule();
        }
    }

    private void getExistingRule() {
        ReportingService.App.get().getRuleByName(reportId, ruleName, new AsyncCallback<TelegramRecurrenceMessage>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(TelegramRecurrenceMessage message) {
                telegramRecurrenceMessage = message;
                fillRuleNameField();
                fillTelegramBotField();
                fillTelegramChatsField();
                status.setValue(telegramRecurrenceMessage.getStatus());
                fillContentMessageField();
                fillRecurrenceTimeDetailsField();
            }
        });
    }

    private void fillRuleNameField() {
        String ruleName = telegramRecurrenceMessage.getRuleName();
        rule.setText(ruleName);
    }

    private void fillTelegramBotField() {
        TelegramSettingsItem telegramSettingsItem = telegramRecurrenceMessage.getTelegramSettingsItem();
        Integer botId = telegramSettingsItem.getId();
        telegramBots.setSelected(botId);
    }

    private void fillTelegramChatsField() {
        List<TelegramChatListItem> telegramChatListItems = telegramRecurrenceMessage.getTelegramChatListItems();
        telegramChatLookUp.setSelectedItems(telegramChatListItems.stream().map(x -> new SelectItem(x.getObjectId(), x.getChatName(), x.getTelegramBotToken())).toArray(SelectItem[]::new));
        telegramChatLookUp.setAccessToken(telegramRecurrenceMessage.getTelegramSettingsItem().getToken());
    }

    private void fillContentMessageField() {
        String messageContent = telegramRecurrenceMessage.getContent();
        content.setText(messageContent);
    }

    private void fillRecurrenceTimeDetailsField() {
        RecurrenceJobItem recurrenceJobItem = telegramRecurrenceMessage.getRecurrenceJobItem();
        recurringWidget.setData(recurrenceJobItem);
    }

    private void save() {
        if (!validate()) {
            return;
        }
        getFieldsValues();
        btnSave.setEnabled(false);
        LoadingPanel.loading(true);
        ReportingService.App.get().saveTelegramReportingRecurrence(telegramRecurrenceMessage, isNew, new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                btnSave.setEnabled(true);
            }

            @Override
            public void onSuccess(ArrayList<String> names) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TELEGRAM_REPORTING_RULE_SAVE, names, TelegramReportingRecurrenceView.this);
                LoadingPanel.loading(false);
                btnSave.setEnabled(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.item()), Info.Type.INFO);
                closeTab();
            }
        });
    }

    private void getFieldsValues() {
        String ruleName = rule.getText();
        telegramRecurrenceMessage.setRuleName(ruleName);

        SelectItem botSelectItem = telegramBots.getSelectedItem();
        Integer botId = botSelectItem.getId();
        String botName = botSelectItem.getName();
        String botDescription = botSelectItem.getDescription();
        TelegramSettingsItem telegramSettingsItem = new TelegramSettingsItem(botId, botName, botDescription);
        telegramRecurrenceMessage.setTelegramSettingsItem(telegramSettingsItem);

        List<TelegramChatListItem> chats = new ArrayList<>();
        for (SelectItem chatSelectItem : telegramChatLookUp.getSelectedItems()) {
            chats.add(new TelegramChatListItem(chatSelectItem.getId()));
        }
        telegramRecurrenceMessage.setTelegramChatListItems(chats);

        boolean isActive = status.getValue();
        telegramRecurrenceMessage.setStatus(isActive);

        String message = content.getText();
        telegramRecurrenceMessage.setContent(message);

        RecurrenceJobItem recurrenceJobItem = recurringWidget.getData();
        telegramRecurrenceMessage.setRecurrenceJobItem(recurrenceJobItem);

        telegramRecurrenceMessage.setReportId(reportId);
    }

    private void delete() {
        LoadingPanel.loading(true);
        ReportingService.App.get().deleteTelegramReportingRecurrenceRule(telegramRecurrenceMessage.getRuleId(), new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                btnDelete.setEnabled(true);
            }

            @Override
            public void onSuccess(ArrayList<String> names) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TELEGRAM_REPORTING_RULE_SAVE, names, TelegramReportingRecurrenceView.this);
                LoadingPanel.loading(false);
                btnDelete.setEnabled(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.reports()), Info.Type.INFO);
                closeTab();
            }
        });
    }

    @Override
    protected void initPredefinedValues() {
    }

    @Override
    protected void addButtons() {
        btnSave = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        btnDelete = new WfmButton2(wfmStrings.delete(), WfmButton2.BTN_PRIMARY, clickEvent -> delete());
        btnSave.ensureDebugId("save_button");
        addButton(btnSave);
        addButton(btnDelete);
    }

    public boolean validate() {
        boolean error = !Validation.validateDataListBoxRequired(telegramBots);

        if (markAsError(telegramChatLookUp, telegramChatLookUp.getSelectedItems() == null || telegramChatLookUp.getSelectedItems().size() == 0) > 0) {
            error = true;
        }

        if (!Validation.validateTextAreaRequired(content)) {
            error = true;
        }

        if (!Validation.validateTextAreaRequired(rule)) {
            error = true;
        }

        return !error;
    }

    protected Localize getLocalizer() {
        if (localize == null) {
            localize = new Localize();
        }
        return localize;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.TELEGRAM_RECURRENCE_FORM;
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
