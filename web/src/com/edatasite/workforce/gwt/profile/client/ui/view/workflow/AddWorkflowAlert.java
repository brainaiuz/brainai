package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowAlert;
import com.edatasite.workforce.gwt.profile.client.ui.view.WorkflowDateSelecter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class AddWorkflowAlert extends CustomForm2 implements Constants, Colapse {
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected static final ProfileServiceAsync profileService = ProfileService.App.get();
    private final EmailTemplateServiceAsync emailTemplateService = EmailTemplateService.App.get();

    protected WorkflowAlert item;
    protected Integer workflowID;
    protected Integer objectID;

    private TextBox fromName;
    private TextBox subject;
    private VerticalPanel vp;
    private KpiRadioButton standartEmailsRadio;
    private KpiRadioButton dynamicEmailsRadio;
    private TextBox recepient;
    private TextArea dynamicRecipientArea;

    private MaterialPanel bccPanelGroup;
    private TextBox bccText;
    private TextBox ccText;
    private VerticalPanel bccCCPanel;
    private HorizontalPanel bccCCLinkPanel;
    private MaterialLink bccLink;
    private MaterialLink ccLink;
    private boolean ccShown;
    private boolean bccShown;

    private KpiSwitcher includeAttachment;
    private DataListBox emailTemplate;
    private DataListBox fromUser;
    private KpiSwitcher workflowTimeBasedAction;
    private WorkflowDateSelecter workflowTimeBasedActionDate;

    private MaterialPanel descriptionPanel;
    private DataListBox fieldCodes;
    private HTML attribute;
    private KpiEditor content;

    private final String nickDebugId = "add_workflow_alert_view_";
    private boolean saveAndClose = false;
    private final LinkedHashMap<String, ModelField> fields = new LinkedHashMap<>();

    public AddWorkflowAlert(String name, String description, Integer objectID, Integer workflowID) {
        super(name, description);
        this.objectID = objectID;
        this.workflowID = workflowID;
    }

    public AddWorkflowAlert(Integer objectID, Integer workflowID) {
        super("workflowalert", settingsStrings.emailAlert());
        this.objectID = objectID;
        this.workflowID = workflowID;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    protected void registerFields() {
        LoadingPanel.loading(true);
        fromUser = new DataListBox();
        fromUser.ensureDebugId(this.nickDebugId + "fromUser");
        fromUser.setWithoutNullLabel(true);
        drawBccCcPanel();
        fromName = new TextBox();
        fromName.ensureDebugId(this.nickDebugId + "fromName");

        vp = new VerticalPanel();
        initStandartRecepient();
        initDynamicRecepient();

        subject = new TextBox();
        subject.ensureDebugId(this.nickDebugId + "subject");
        emailTemplate = new DataListBox();
        emailTemplate.ensureDebugId(this.nickDebugId + "emailTemplate");
        emailTemplate.addValueChangeHandler(changeEvent -> onTemplateChanged());
        includeAttachment = new KpiSwitcher();
        includeAttachment.ensureDebugId(this.nickDebugId + "includePDF");

        drawDescriptionPanel();

        workflowTimeBasedAction = new KpiSwitcher();
        workflowTimeBasedAction.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        workflowTimeBasedActionDate = new WorkflowDateSelecter(false, true);
        workflowTimeBasedActionDate.setVisible(false);
        workflowTimeBasedAction.addValueChangeHandler(booleanValueChangeEvent -> workflowTimeBasedActionDate.setVisible(booleanValueChangeEvent.getValue()));

        addField(WORKFLOW_ALERT_FORM.RECEPIENT, vp, getTitle(wfmStrings.recipient()));
        addField(WORKFLOW_ALERT_FORM.BCC_CC_PANEL, bccPanelGroup);
        addField(WORKFLOW_ALERT_FORM.TEMPLATE, emailTemplate, getTitle(wfmStrings.template()));
        addField(WORKFLOW_ALERT_FORM.SUBJECT, subject, getTitle(wfmStrings.subject()));
        addField(WORKFLOW_ALERT_FORM.CONTENT, descriptionPanel, getTitle(wfmStrings.emailContent()));
        addField(WORKFLOW_ALERT_FORM.FROM_EMAIL, fromUser, getTitle(wfmStrings.fromEmail()));
        addField(WORKFLOW_ALERT_FORM.FROM_NAME, fromName, getTitle(wfmStrings.fromName()));
        addTitleField(WORKFLOW_TIME_BASED_HEADER, wfmStrings.timeBasedAction());
        addField(WORKFLOW_TIME_BASED, new InputGroup(workflowTimeBasedAction, workflowTimeBasedActionDate), getTitle(wfmStrings.executionTime()));
        show();
    }


    private void initStandartRecepient() {
        vp.clear();

        standartEmailsRadio = new KpiRadioButton("ruleCriteriaType", wfmStrings.standardKpi());
        standartEmailsRadio.setValue(true);
        vp.add(standartEmailsRadio);

        recepient = new TextBox();
        recepient.ensureDebugId(this.nickDebugId + "recepient");
        new KpiToolTip(recepient, settingsStrings.enterEmailAttribute());
        vp.add(recepient);

        standartEmailsRadio.addValueChangeHandler(event -> {
            if (standartEmailsRadio.getValue()) {
                recepient.setVisible(true);
                dynamicRecipientArea.setVisible(false);
            }
        });
    }

    private void initDynamicRecepient() {

        dynamicEmailsRadio = new KpiRadioButton("ruleCriteriaType", wfmStrings.dynamicType());
        dynamicEmailsRadio.setValue(false);

        dynamicRecipientArea = new TextArea();
        dynamicRecipientArea.ensureDebugId("forkflowFilterTable_dynamicCriteriaPanel");
        dynamicRecipientArea.setCharacterWidth(5000);
        dynamicRecipientArea.setVisible(false);
        new KpiToolTip(dynamicRecipientArea, "Query should return email. Multiple emails must be separated by \",\"");

        if (Utils.isAdmin()) {
            vp.add(dynamicEmailsRadio);
            vp.add(dynamicRecipientArea);
        }

        dynamicEmailsRadio.addValueChangeHandler(event -> {
            if (dynamicEmailsRadio.getValue()) {
                dynamicRecipientArea.setVisible(true);
                recepient.setVisible(false);
            }
        });
    }

    private void drawBccCcPanel() {
        bccPanelGroup = new MaterialPanel("form-group");
        MaterialPanel bccPanel = new MaterialPanel();
        bccPanelGroup.add(bccPanel);

        ccLink = new MaterialLink(wfmStrings.cc());
        ccLink.ensureDebugId(this.nickDebugId + "ccLink");
        ccLink.setTooltip(wfmStrings.carbonCopy());
        ccLink.addClickHandler(clickEvent -> showBccCC(true));
        ccText = new TextBox();
        ccText.ensureDebugId(this.nickDebugId + "ccText");
        new KpiToolTip(ccText, settingsStrings.recipientInform());


        bccLink = new MaterialLink(wfmStrings.bcc());
        bccLink.ensureDebugId(this.nickDebugId + "bccLink");
        bccLink.setTooltip(wfmStrings.blindCarbonCopy());
        bccLink.addClickHandler(clickEvent -> showBccCC(false));
        bccText = new TextBox();
        bccText.ensureDebugId(this.nickDebugId + "bccText");
        new KpiToolTip(bccText, settingsStrings.recipientInform());

        bccCCLinkPanel = new HorizontalPanel();
        bccCCLinkPanel.setWidth("150px");
        bccCCLinkPanel.setSpacing(3);
        bccCCLinkPanel.add(ccLink);
        bccCCLinkPanel.add(bccLink);
        bccCCPanel = new VerticalPanel();
        bccCCPanel.setSpacing(3);
        bccPanel.add(bccCCPanel);
        bccPanel.add(bccCCLinkPanel);
    }

    private void showBccCC(boolean cc) {
        bccCCPanel.add(new FormGroup(cc ? wfmStrings.cc() : wfmStrings.bcc(), cc ? ccText : bccText));
        bccCCLinkPanel.clear();
        if (cc ? !bccShown : !ccShown) {
            bccCCLinkPanel.add(cc ? bccLink : ccLink);
        }
        ccShown = cc;
        bccShown = !cc;
    }

    private void drawDescriptionPanel() {
        descriptionPanel = new MaterialPanel();

        MaterialPanel firstRow = new MaterialPanel("grid-row");

        MaterialPanel dropdownPanel = new MaterialPanel("col-10");
        MaterialPanel attributePanel = new MaterialPanel("col-2");
        firstRow.add(dropdownPanel);
        firstRow.add(attributePanel);

        fieldCodes = new DataListBox();
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

        content = new KpiEditor(false);
        content.ensureDebugId(this.nickDebugId + "content");
        contentPanel.add(content);

        descriptionPanel.add(firstRow);
        descriptionPanel.add(secondRow);
    }

    private void onTemplateChanged() {
        subject.setEnabled(false);
        if (emailTemplate.getSelectedId() != null) {
            EntityToEmailTemplate emailTemplateInner = new EntityToEmailTemplate();
            emailTemplateInner.setEntityType("email");
            emailTemplateInner.setEmailTemplateId(emailTemplate.getSelectedId());
            if (fieldCodes != null) {
                fieldCodes.setEnabled(false);
            }
            emailTemplateService.generateMessageCenterTemplateItem(emailTemplateInner, null, null, null, new AsyncCallback<EmailTemplateItem>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(EmailTemplateItem result) {
                    if (result != null) {
                        content.setData(result.getMessageHTML());
                        subject.setText(result.getSubject());
                        fromName.setText(result.getFromUserName());
                    }
                }
            });
        } else {
            subject.setEnabled(true);
            if (fieldCodes != null) {
                fieldCodes.setEnabled(true);
            }
        }
    }

    private void initColumns(String code) {
        if (code != null) {
            service.getDefaultModelForm(getFormIDOfModule(code), new AsyncCallback<ModelForm>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(ModelForm modelForm) {
                    //show Only showInForm fields
                    fields.clear();
                    if (modelForm != null && modelForm.getFields().size() > 0) {
                        for (ModelField field : modelForm.getFields()) {
                            if (field.isEntityField() && !field.getField_ID().contains(",")) {
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
        fieldCodes.clear();
        fieldCodes.setItems(items);
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
    protected void getDataToFillFields() {
        profileService.editWorkflowAlert(objectID, workflowID, new AbstractAsyncCallback<WorkflowAlert>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(WorkflowAlert result) {
                item = result;
                setValuesToWidgets();
                if (item.getObjectID() == null) {
                    setDefaultValues();
                }
                if (WorkflowRule._WORKFLOW_MODULE_SALE_INVOICE.equals(item.getWorkflowRule().getModule()) ||
                        WorkflowRule._WORKFLOW_MODULE_ACTIVITY.equals(item.getWorkflowRule().getModule()) ||
                        WorkflowRule._WORKFLOW_MODULE_LOGACALL.equals(item.getWorkflowRule().getModule()) ||
                    WorkflowRule._WORKFLOW_MODULE_PICKLIST.equals(item.getWorkflowRule().getModule())) {

                    String title = settingsStrings.includeICal();
                    if (WorkflowRule._WORKFLOW_MODULE_SALE_INVOICE.equals(item.getWorkflowRule().getModule()) ||
                        WorkflowRule._WORKFLOW_MODULE_PICKLIST.equals(item.getWorkflowRule().getModule())) {
                        title = wfmStrings.attachPdf();
                    }
                    addField(WORKFLOW_ALERT_FORM.INCLUDE_PDF, includeAttachment, getTitle(title));
                }
                initColumns(item.getWorkflowRule().getModule());
            }
        });
    }

    private void save() {
        if (validate()) {
            setValuesToItem();
            LoadingPanel.loading(true);
            profileService.saveWorkflowAlert(item, new AbstractAsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    super.onFailure(caught);
                }

                @Override
                public void onSuccess(Integer result) {
                    LoadingPanel.loading(false);
                    WfmUiEventsBus.fireWfmUiEvent(item.getObjectID() != null ? WfmUiEventType.ON_WORKFLOW_ALERT_UPDATE : WfmUiEventType.ON_WORKFLOW_ALERT_ADD, item, AddWorkflowAlert.this);
                    item.setObjectID(result);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.alert()));
                    closeTab();
                    if (!saveAndClose) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("workflowalert|add/add//" + workflowID);
                    }
                }
            });
        }
    }

    private boolean validate() {
        recepient.removeStyleName(Constants.ERROR_FORM_STYLE);
        int error = customValidate();
        if (standartEmailsRadio.getValue()) {
            if (Utils.isNullOrEmpty(recepient.getText()) || recepient.getText().contains(";")) {
                recepient.addStyleName(Constants.ERROR_FORM_STYLE);
                error++;
            } else if (!Utils.validateEmail(recepient.getText(), true) && !recepient.getText().matches("\\$\\{.*\\}")) {
                recepient.addStyleName(Constants.ERROR_FORM_STYLE);
                error++;
            }
        } else if (Utils.isNullOrEmpty(dynamicRecipientArea.getText())) {
            dynamicRecipientArea.addStyleName(Constants.ERROR_FORM_STYLE);
            error++;
        }
//        else if (recepient.getText().matches("\\$\\{.*\\}") && !recepient.getText().toLowerCase().contains("email")) {
//            recepient.addStyleName(Constants.ERROR_FORM_STYLE);
//            error++;
//        }
//        if (!Utils.isNullOrEmpty(recepient.getText())) {
//            String[] emails = recepient.getText().split(",");
//            if (emails.length > 0) {
//                for (String email : emails) {
//                    email = email.trim();
//                    if (email.matches("\\$\\{.*\\}") && !email.toLowerCase().contains("email")) {
//                        if (!recepient.getStyleName().contains(Constants.ERROR_FORM_STYLE)) {
//                            recepient.addStyleName(Constants.ERROR_FORM_STYLE);
//                        }
//                        error++;
//                        break;
//                    }
//                }
//            }
//        }            //munir aka customfieldla uchun soradiila:  customfieldda field_id da email bolmaydi shuni hisobiga commitga olib qoyildi

        if (!Utils.isNullOrEmpty(ccText.getText())) {
            if (ccText.getText().contains(";") || (!Utils.validateEmail(ccText.getText(), true) && !ccText.getText().matches("\\$\\{.*\\}"))) {
                ccText.addStyleName(Constants.ERROR_FORM_STYLE);
                error++;
            } else if (ccText.getText().matches("\\$\\{.*\\}") && !ccText.getText().toLowerCase().contains("email")) {
                ccText.addStyleName(Constants.ERROR_FORM_STYLE);
                error++;
            }
        }
        if (!Utils.isNullOrEmpty(bccText.getText())) {
            if (bccText.getText().contains(";") || (!Utils.validateEmail(bccText.getText(), true) && !bccText.getText().matches("\\$\\{.*\\}"))) {
                bccText.addStyleName(Constants.ERROR_FORM_STYLE);
                error++;
            } else if (bccText.getText().matches("\\$\\{.*\\}") && !bccText.getText().toLowerCase().contains("email")) {
                bccText.addStyleName(Constants.ERROR_FORM_STYLE);
                error++;
            }
        }

        if (error > 0) {
            Info.warn(wfmStrings.fillRequiredField());
        }

        recepient.addKeyDownHandler(event -> recepient.removeStyleName(ERROR_FORM_STYLE));
        standartEmailsRadio.addValueChangeHandler(event -> {
            recepient.removeStyleName(ERROR_FORM_STYLE);
            dynamicRecipientArea.removeStyleName(ERROR_FORM_STYLE);
        });
        dynamicEmailsRadio.addValueChangeHandler(event -> {
            recepient.removeStyleName(ERROR_FORM_STYLE);
            dynamicRecipientArea.removeStyleName(ERROR_FORM_STYLE);
        });
        ccText.addKeyDownHandler(event -> recepient.removeStyleName(ERROR_FORM_STYLE));
        bccText.addKeyDownHandler(event -> recepient.removeStyleName(ERROR_FORM_STYLE));

        return error == 0;
    }

    private void setValuesToItem() {
        item.setEmailSettingID(fromUser.getSelectedId());
        item.setFromName(fromName.getText());
        item.setSubject(subject.getValue());
        item.setContent(content.getData());
        item.setEmailTemplate(emailTemplate.getSelectedItem());
        item.setRecepient(recepient.getValue());
        item.setDynamicRecipient(dynamicEmailsRadio.getValue());
        item.setDynamicRecipientQuery(dynamicRecipientArea.getValue());
        item.setToBCC(bccText.getValue());
        item.setToCC(ccText.getValue());
        item.setIncludeAttachment(includeAttachment.getValue());
        item.setWorkflowActionTimeBased(workflowTimeBasedAction.getValue());
        item.setWorkflowActionStartTime(workflowTimeBasedActionDate.getWorkflowStartDate());
        item.setWorkflowActionStartTimeUnit(workflowTimeBasedActionDate.getWorkflowDueDateUnit());
        item.setWorkflowActionStartTimeGranularity(workflowTimeBasedActionDate.getWorkflowDueDateGranularity());
    }

    protected void setValuesToWidgets() {
        if (item != null) {
            Timer timer = new Timer() {
                @Override
                public void run() {
                    content.setData(item.getContent());
                }
            };
            timer.schedule(500);
            subject.setValue(item.getSubject());
            setRecepientValues(item);
            if (!Utils.isNullOrEmpty(item.getToCC())) {
                ccText.setValue(item.getToCC());
                showBccCC(true);
            }
            if (!Utils.isNullOrEmpty(item.getToBCC())) {
                bccText.setValue(item.getToBCC());
                showBccCC(false);
            }
            includeAttachment.setValue(item.isIncludeAttachment());
            emailTemplate.setItems(item.getEmailTemplates());
            emailTemplate.setSelected(item.getEmailTemplate());
            if (item.getFromUsers() != null) {
                fromUser.setItems(item.getFromUsers());
                if (objectID != null) {
                    fromUser.setSelected(item.getEmailSettingID() != null ? item.getEmailSettingID() : 0);
                } else {
                    for (SelectItem it : item.getFromUsers()) {
                        if (it.isSelected()) {
                            fromUser.setSelected(it);
                            break;
                        }
                    }
                }
            }
            fromName.setText(item.getFromName());
            workflowTimeBasedAction.setValue(item.isWorkflowActionTimeBased(), true);
            workflowTimeBasedActionDate.setStartDate(item.getWorkflowActionStartTime());
            workflowTimeBasedActionDate.setDueDate(item.getWorkflowActionStartTimeUnit());
            workflowTimeBasedActionDate.setDueDateGranularity(item.getWorkflowActionStartTimeGranularity());
            onTemplateChanged();
            initPredefinedValues();
        }
    }

    private void setRecepientValues(WorkflowAlert item) {
        recepient.setValue(item.getRecepient());
        dynamicEmailsRadio.setValue(item.isDynamicRecipient());
        dynamicRecipientArea.setVisible(item.isDynamicRecipient());
        dynamicRecipientArea.setValue(item.getDynamicRecipientQuery());
        if (!Utils.isAdmin()) {
            dynamicRecipientArea.setVisible(false);
        }
    }

    public SelectItem[] getColumnsAsReferenceItems(SelectItem[] additionalAttributes) {
        ArrayList<SelectItem> result = new ArrayList<>();
        if (fields != null && fields.size() > 0) {
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

    @Override
    protected void initPredefinedValues() {
        if (item != null) {
            addPredefinedValues(WORKFLOW_ALERT_FORM.TEMPLATE, item.getEmailTemplates());
            addPredefinedValues(WORKFLOW_ALERT_FORM.FROM_EMAIL, item.getFromUsers());
        }
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            switch (fieldID) {
                case WORKFLOW_ALERT_FORM.SUBJECT:
                    return wfmStrings.subject();
                case WORKFLOW_ALERT_FORM.TEMPLATE:
                    return wfmStrings.template();
                case WORKFLOW_ALERT_FORM.CONTENT:
                    return wfmStrings.emailContent();
                case WORKFLOW_ALERT_FORM.RECEPIENT:
                    return wfmStrings.recipient();
                case WORKFLOW_ALERT_FORM.FROM_EMAIL:
                    return wfmStrings.fromEmail();
                case WORKFLOW_ALERT_FORM.FROM_NAME:
                    return wfmStrings.fromName();
                case WORKFLOW_TIME_BASED_HEADER:
                    return wfmStrings.timeBasedAction();
                case WORKFLOW_ALERT_FORM.BCC_CC_PANEL:
                    return wfmStrings.bccAndCcPanel();
                case WORKFLOW_ALERT_FORM.INCLUDE_PDF:
                    return wfmStrings.attachPdf();
            }
        }
        return null;
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
    protected String getFormID() {
        return LayoutRPC.WORKFLOW_ALERT_FORM;
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
