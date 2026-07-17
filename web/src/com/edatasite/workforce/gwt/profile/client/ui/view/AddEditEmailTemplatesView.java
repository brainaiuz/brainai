package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;

/**
 * User: Admin
 * Date: 15.03.2010
 * Time: 14:13:20
 */
public class AddEditEmailTemplatesView extends CustomForm implements Constants, Colapse {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    private EmailTemplateItem item;
    private KpiCheckBox onlyMine;
    private KpiCheckBox showInMessageCenter; // Visible only when message center module/category is selected
    private KpiSwitcher sendSummaryPdf;
    private FormGroup sendSummaryFormGroup;
    private RadioButton defaultYes;
    private RadioButton defaultNo;
    private KpiEditor editorHTML;
    private Integer int_objectID;
    private WfmButton2 sendToEmail;
    private HTML personalAtt;
    private TextBox templateName;
    private DataListBox templateModule;
    private DataListBox languages;
    private DataListBox templateCategory;
    private TextBox templateSubject;
    private DataListBox templateFromUser;
    private TextBox fromUserName;
    private TextBox replyTo;
    private TextBox templateTestEmail;
    private VerticalPanel verticalPanel;
    private TextArea editorText;
    private HTML templateMessageHTML;
    private HTML templateEmailAddressHTML;
    private GeneralFileUpload fileUpload;
    private FlexTable templateEmailAddressTable;
    private FlexTable templateMessageContentTable;
    private final String test_code_ID_name = "add_email_template_view_";
    private DataListBox pdfTemplates;

    public AddEditEmailTemplatesView() {
        super("addtemplate", wfmStrings.addEmailTemplate());
    }

    public AddEditEmailTemplatesView(Integer int_objectID, String companyTemplate) {
        super("addtemplate", wfmStrings.addEmailTemplate());
        String viewName = wfmStrings.addEmailTemplate();
        if (int_objectID != null) {
            if (EmailTemplateConstants.DEFAULT_EMAIL_TEMPLATE.equals(companyTemplate)) {
                viewName = settingsStrings.copyEmailTemplate();
            } else {
                viewName = settingsStrings.editEmailTemplate();
            }
            this.int_objectID = int_objectID;
        }
        setDescription(viewName);
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        if (int_objectID == null) {
            MaterialLink save = new MaterialLink(wfmStrings.save());
            save.addClickHandler(event -> save(true));
            save.ensureDebugId(test_code_ID_name + "save_and_close_button");
            MaterialSplitButton splitButton = new MaterialSplitButton(save);

            MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
            saveAdd.addClickHandler(event -> save(false));
            saveAdd.ensureDebugId(test_code_ID_name + "save_and_new_button");

            splitButton.addItem(saveAdd);
            addButton(splitButton);

        } else {
            addButton(wfmStrings.update(), null, (test_code_ID_name + "update_button"), event -> save(true));
        }
    }

    @Override
    protected void getDataToFillFields() {
        Scheduler.get().scheduleDeferred(() -> {
            LoadingPanel.loading(true);
            ProfileService.App.get().getEmailTemplate(int_objectID, new AbstractAsyncCallback<EmailTemplateItem>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void success(EmailTemplateItem result) {
                    if (result != null) {
                        item = result;
                        fillFormWithData();
                    }
                    LoadingPanel.loading(false);
                }
            });
        });
    }

    private void fillFormWithData() {
        if (item != null) {
            templateName.setText(item.getName());
            defaultYes.setValue(item.isDefault());
            defaultNo.setValue(!item.isDefault());
            onlyMine.setValue(item.isOnlyMine());
            sendSummaryPdf.setValue(item.isSendSummaryPdf());
            showInMessageCenter.setValue(item.showInMessageCenter() != null ? item.showInMessageCenter() : false);
            templateFromUser.setSelected(item.getFromUserID());
            templateSubject.setText(item.getSubject());
            templateModule.setItems(item.getModules());
            templateModule.setSelected(item.getModuleID());
            fromUserName.setValue(item.getFromUserName());
            if (item.getLangugages() != null) {
                languages.setItems(item.getLangugages());
            }
            if (item.getLanguage() != null) {
                languages.setSelected(item.getLanguage());
            }

            if (item.getModuleID() != null) {
                onModuleChange(true);
            }
            if (item.getMessageHTML() != null) {
                if (isSmsTemplate()) {
                    editorText.setText(item.getMessageHTML());
                } else {
                    editorHTML.setData(item.getMessageHTML());
                }
            }
            templateTestEmail.setText(item.getTestEmail());
            replyTo.setText(item.getReplyTo());
            if (item.getFromUsers() != null) {
                SelectItem[] fromUsers = item.getFromUsers();
                SelectItem[] items = new SelectItem[fromUsers.length + 1];
                items[0] = new SelectItem(-1, wfmStrings.currentUser());
                for (int i = 1; i < items.length; i++) {
                    items[i] = new SelectItem(fromUsers[i - 1].getId(), fromUsers[i - 1].getName());
                }
                templateFromUser.setItems(items);
                templateFromUser.setSelected(-1);
            }
            pdfTemplates.setItems(item.getPdfTemplates());
            pdfTemplates.setSelected(item.getPdfTemplateId());
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.EMAIL_TEMPLATE_FORM;
    }

    @Override
    protected String getFormType() {
        return int_objectID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        //template name
        templateName = new TextBox();
        templateName.addStyleName(DEFAULT_WIDTH);
        templateName.ensureDebugId(test_code_ID_name + "template_name");
        //template module
        templateModule = new DataListBox();
        templateModule.addStyleName(DEFAULT_WIDTH);
        templateModule.ensureDebugId(test_code_ID_name + "template_module");
        templateModule.addValueChangeHandler(changeEvent -> {
            if (isSmsTemplate() ? !(editorText.getText() == null || "".equals(editorText.getText().trim())) : !(editorHTML.getData() == null || "".equals(editorHTML.getData().trim()))) {
                showWarningMessage(true);
            } else {
                onModuleChange(false);
            }
        });
        languages = new DataListBox();
        languages.addStyleName(DEFAULT_WIDTH);
        languages.ensureDebugId(test_code_ID_name + "languages");
        //template category
        templateCategory = new DataListBox();
        templateCategory.addStyleName(DEFAULT_WIDTH);
        templateCategory.ensureDebugId(test_code_ID_name + "template_category");
        templateCategory.setEnabled(false);
        templateCategory.addValueChangeHandler(changeEvent -> {
            if (isSmsTemplate() ? !(editorText.getText() == null || "".equals(editorText.getText().trim())) : !(editorHTML.getData() == null || "".equals(editorHTML.getData().trim()))) {
                showWarningMessage(false);
            } else {
                onCategoryChange(false);
            }
        });
        //template from user
        templateFromUser = new DataListBox();
        templateFromUser.addStyleName(DEFAULT_WIDTH);
        templateFromUser.ensureDebugId(test_code_ID_name + "template_from_user");
        //template from user name
        fromUserName = new TextBox();
        fromUserName.addStyleName(DEFAULT_WIDTH);
        fromUserName.ensureDebugId(test_code_ID_name + "test_from_user_name");
        //template radioButton default YES
        defaultYes = new KpiRadioButton("default", ("&nbsp;" + wfmStrings.yes() + "&nbsp;"), true);
        defaultYes.ensureDebugId(test_code_ID_name + "default_yes");
        //template radioButton default NO
        defaultNo = new KpiRadioButton("default", ("&nbsp;" + wfmStrings.no() + "&nbsp;"), true);
        defaultNo.ensureDebugId(test_code_ID_name + "default_no");
        //show template only for user
        onlyMine = new KpiCheckBox();
        templateName.ensureDebugId(test_code_ID_name + "only_mine");

        sendSummaryPdf = new KpiSwitcher();
        sendSummaryPdf.ensureDebugId(test_code_ID_name + "sendSummaryPdf");
        sendSummaryFormGroup = new FormGroup(settingsStrings.sendPdf(), sendSummaryPdf);
        sendSummaryFormGroup.setVisible(false);
        //template subject
        templateSubject = new TextBox();
        templateSubject.addStyleName(DEFAULT_WIDTH);
        templateSubject.ensureDebugId(test_code_ID_name + "template_subject");
        replyTo = new TextBox();
        replyTo.addStyleName(DEFAULT_WIDTH);
        replyTo.ensureDebugId(test_code_ID_name + "reply_to");
        //template message body (editor)
        editorHTML = new KpiEditor();
        editorHTML.setWidth("757px");
        editorHTML.ensureDebugId(test_code_ID_name + "template_content_editor");
        //template message body (textArea)
        editorText = new TextArea();
        editorText.setHeight("170px");
        editorText.setWidth("550px");
        editorText.setVisible(false);
        editorText.ensureDebugId(test_code_ID_name + "template_content_textarea");
        //template personalization attributes panel
        verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(5);
        verticalPanel.addStyleName("table-default");
        personalAtt = new HTML("<b class=customTitle>" + wfmStrings.personalizationAttributes() + ":</b>");
        //template test email box
        templateTestEmail = new TextBox();
        templateTestEmail.addStyleName(DEFAULT_WIDTH);
        templateTestEmail.ensureDebugId(test_code_ID_name + "template_test_email");
        //template send to test email button
        sendToEmail = new WfmButton2(wfmStrings.send(), event -> sendTestEmail());
        sendToEmail.ensureDebugId(test_code_ID_name + "send_to_test_email_test_button");
        sendToEmail.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        sendToEmail.getElement().getStyle().setMarginBottom(4, Style.Unit.PX);

        showInMessageCenter = new KpiCheckBox(settingsStrings.showInMessageCenter());
        showInMessageCenter.setVisible(false);
        //template file
        fileUpload = new GeneralFileUpload(F_EMAIL_TEMPLATE, int_objectID, int_objectID);
        fileUpload.ensureDebugId(test_code_ID_name + "attachments");
        //template is default table
        FlexTable isDefaultTable = new FlexTable();
        isDefaultTable.setWidget(0, 0, defaultYes);
        isDefaultTable.setWidget(0, 1, defaultNo);
        isDefaultTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        isDefaultTable.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        isDefaultTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
        isDefaultTable.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
        //template message content
        templateMessageHTML = new HTML("<b>" + getTitle(wfmStrings.messageText(), true) + "</b>");
        templateMessageHTML.getElement().getStyle().setMarginLeft(2, Style.Unit.PX);
        //template message content table
        templateMessageContentTable = new FlexTable();
        templateMessageContentTable.setWidget(0, 0, templateMessageHTML);
        templateMessageContentTable.setWidget(0, 1, editorHTML);
        templateMessageContentTable.setWidget(0, 2, editorText);
        templateMessageContentTable.setWidget(0, 3, verticalPanel);
        templateMessageContentTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        templateMessageContentTable.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        templateMessageContentTable.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_TOP);
        templateMessageContentTable.getCellFormatter().setVerticalAlignment(0, 3, HasVerticalAlignment.ALIGN_TOP);
        templateMessageContentTable.getCellFormatter().setWidth(0, 0, "145px");
        templateMessageContentTable.getCellFormatter().setWidth(0, 1, "760px");
        templateMessageContentTable.getCellFormatter().setWidth(0, 2, "1px");
        templateEmailAddressHTML = new HTML("<b>" + getTitle(settingsStrings.sendTestEmail(), true) + "</b>");
        //template send test email (address) table
        templateEmailAddressTable = new FlexTable();
        templateEmailAddressTable.setWidget(0, 0, templateEmailAddressHTML);
        templateEmailAddressTable.setWidget(0, 1, templateTestEmail);
        templateEmailAddressTable.setWidget(0, 2, sendToEmail);
        templateEmailAddressTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
        templateEmailAddressTable.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
        templateEmailAddressTable.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_MIDDLE);
        templateEmailAddressTable.getCellFormatter().setWidth(0, 0, "145px");
        templateEmailAddressTable.getCellFormatter().setWidth(0, 1, DEFAULT_WIDTH);

        //template module
        pdfTemplates = new DataListBox();
        pdfTemplates.addStyleName(DEFAULT_WIDTH);
        pdfTemplates.ensureDebugId(test_code_ID_name + "pdf_templates");
        pdfTemplates.setVisible(false);

        addTitleField(CustomFormConstants.DETAILS, settingsStrings.emailTemplateInfo());
        addField(CustomFormConstants.NAME, templateName, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.MODULE, templateModule, getTitle(wfmStrings.apps(), true));
        addField(CustomFormConstants.LANGUAGE, languages, getTitle(wfmStrings.language()));
        addField(CustomFormConstants.CATEGORY, templateCategory, getTitle(wfmStrings.category()));
        addField(CustomFormConstants.IS_DEFAULT, isDefaultTable, getTitle(wfmStrings.isDefault()));
        addField(CustomFormConstants.ONLY_MINE, onlyMine, getTitle(wfmStrings.priv()));
        addField(CustomFormConstants.PDF_VERSION, sendSummaryFormGroup);
        addField(CustomFormConstants.SHOW_IN_MESSAGE_CENTER, showInMessageCenter, null);
        addField(CustomFormConstants.SUBJECT, templateSubject, getTitle(wfmStrings.subject(), true));
        addField(CustomFormConstants.FROM_USER, templateFromUser, getTitle(wfmStrings.from(), true));
        addField(CustomFormConstants.WORKFLOW_ALERT_FORM.FROM_NAME, fromUserName, getTitle(wfmStrings.fromName()));
        addField(CustomFormConstants.REPLY_TO, replyTo, getTitle(wfmStrings.replyToOnly(), false));
        addField(CustomFormConstants.MESSAGE_CONTENT, templateMessageContentTable, null);
        addField(CustomFormConstants.EMAIL, templateEmailAddressTable, null);
        addField(CustomFormConstants.ATTACHMENTS, fileUpload, wfmStrings.attachments(), true);
        addField(MESSAGE_CENTER.PDF_TEMPLATE, pdfTemplates, wfmStrings.pdfTemplates());
        show();
    }

    private void showWarningMessage(final boolean isModule) {
        final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo,
                settingsStrings.plsInforYourMailCleared(), new CloseHandler() {
            @Override
            public void onSubmit() {
                if (isModule) {
                    onModuleChange(false);
                } else {
                    onCategoryChange(false);
                }
            }
        });
        wfmMessageBox.setTitle(wfmStrings.warning());
        wfmMessageBox.open();
    }

    private void onModuleChange(final boolean isFirstTime) {
        verticalPanel.clear();
        verticalPanel.add(personalAtt);
        if (!isFirstTime) {
            editorText.setText("");
            editorHTML.setData("");
        }
        templateCategory.clear();
        templateCategory.setEnabled(templateModule.isSomethingSelected());

        if ("ET_TASK_MODULE".equals(templateModule.getSelectedItem().getDescription()) || "ET_PLACEMENT_MODULE".equals(templateModule.getSelectedItem().getDescription())) {
            sendSummaryFormGroup.setVisible(true);
            pdfTemplates.setVisible(true);
        } else {
            sendSummaryPdf.setValue(false);
            sendSummaryFormGroup.setVisible(false);
            pdfTemplates.setVisible(false);
            pdfTemplates.setSelectedItem(null);
        }
        if (templateModule.isSomethingSelected()) {
            emailOrSms(EmailTemplateConstants.ET_SMS_MODULE.equals(templateModule.getSelectedItem().getDescription()));
            displayOrBlockShowInMessageCenterOption();
            LoadingPanel.loading(true);
            ProfileService.App.get().getEmailTemplateCategories(templateModule.getSelectedId(), new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(SelectItem[] result) {
                    LoadingPanel.loading(false);
                    templateCategory.setItems(result);
                    if (result != null && result.length == 1) {
                        templateCategory.setSelected(result[0]);
                    }
                    if (isFirstTime && item.getCategoryId() != null) {
                        templateCategory.setSelected(item.getCategoryId());
                        onCategoryChange(isFirstTime);
                    } else {
                        fillAttributes(true);
                    }
                }
            });
        }
    }

    private void onCategoryChange(boolean isFirstTime) {
        verticalPanel.clear();
        verticalPanel.add(personalAtt);
        if (!isFirstTime) {
            editorText.setText("");
            editorHTML.setData("");
        }
        if (templateCategory.isSomethingSelected()) {
            fillAttributes(false);
        } else if (templateModule.isSomethingSelected()) {
            fillAttributes(true);
        }
    }

    private void emailOrSms(boolean isSMS) {
        if (isSMS) {
            templateMessageHTML.setHTML("<b>" + getTitle(wfmStrings.messageText(), true) + "</b>");
            editorText.setWidth("90%");
            editorHTML.setWidth("1px");
            templateMessageContentTable.getCellFormatter().setWidth(0, 1, "1px");
//            templateMessageContentTable.getCellFormatter().setWidth(0, 2, "550px");
            editorHTML.setVisible(false);
            editorText.setVisible(true);
            enableButton(true);
            visibleEmailAddressTable(false);
            templateEmailAddressHTML.setHTML("");
        } else {
            templateMessageHTML.setHTML("<b>" + getTitle(settingsStrings.messageHTML(), true) + "</b>");
            editorHTML.setWidth("90%");
            editorText.setWidth("1px");
//            templateMessageContentTable.getCellFormatter().setWidth(0, 1, "550px");
            templateMessageContentTable.getCellFormatter().setWidth(0, 2, "1px");
            editorText.setVisible(false);
            editorHTML.setVisible(true);
            visibleEmailAddressTable(true);
            templateEmailAddressHTML.setHTML("<b>" + getTitle(settingsStrings.sendTestEmail(), true) + "</b>");
        }
    }

    private void fillAttributes(boolean isModule) {
        if (isModule) {
            ProfileService.App.get().getEmailTemplateModuleAttributes(templateModule.getSelectedId(), new AbstractAsyncCallback<ArrayList<String>>() {
                public void failure(Throwable caught) {
                }

                public void success(ArrayList<String> result) {
                    for (String attr : result) {
                        FlexTable flexTable = new FlexTable();
                        flexTable.setCellSpacing(2);
                        flexTable.setHTML(0, 0, attr);
                        verticalPanel.add(flexTable);
                        verticalPanel.setCellVerticalAlignment(flexTable, HasVerticalAlignment.ALIGN_TOP);
                    }
                }
            });
        } else {
            ProfileService.App.get().getEmailTemplateCategoryFields(templateCategory.getSelectedId(), new AbstractAsyncCallback<ArrayList<String>>() {
                public void failure(Throwable caught) {
                }

                public void success(ArrayList<String> result) {
                    for (String attr : result) {
                        FlexTable flexTable = new FlexTable();
                        flexTable.setCellSpacing(2);
                        flexTable.setHTML(0, 0, attr);
                        verticalPanel.add(flexTable);
                        verticalPanel.setCellVerticalAlignment(flexTable, HasVerticalAlignment.ALIGN_TOP);
                    }
                }
            });
        }
    }

    private void displayOrBlockShowInMessageCenterOption() {
        if (EmailTemplateConstants.ET_LEAD_MODULE.equals(templateModule.getSelectedItem().getReferenceCode())
                || EmailTemplateConstants.ET_CONTACT_MODULE.equals(templateModule.getSelectedItem().getReferenceCode())) {
            showInMessageCenter.setVisible(true);
        } else {
            showInMessageCenter.setVisible(false);
            showInMessageCenter.setValue(false);
        }
    }

    private void save(final boolean closeTabT) {
        if (valid()) {
            enableButton(false);
            EmailTemplateItem templateItem = setValues();
            LoadingPanel.loading(true);
            ProfileService.App.get().createUpdateEmailTemplate(templateItem, new AbstractAsyncCallback<Integer>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    enableButton(true);
                }

                public void success(Integer result) {
                    LoadingPanel.loading(false);
                    enableButton(true);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.emailTemplate()), Info.Type.INFO);
                    onShellOk(closeTabT);
                    if (int_objectID == null) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_TEMPLATE_ADD, result, AddEditEmailTemplatesView.this);
                    } else {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_TEMPLATE_EDIT, result, AddEditEmailTemplatesView.this);
                    }
                }
            });
        }
    }

    private boolean isSmsTemplate() {
        return templateModule.isSomethingSelected() && EmailTemplateConstants.ET_SMS_MODULE.equals(templateModule.getSelectedItem().getDescription());
    }

    private void onShellOk(boolean closeTabT) {
        if (closeTabT) {
            closeTab();
        } else {
            reInit();
        }
    }

    private void reInit() {
        initForm();
        initialize();
    }

    private EmailTemplateItem setValues() {
        EmailTemplateItem templateItem = new EmailTemplateItem();
        //template int_objectID
        templateItem.setObjectId(int_objectID);
        //template name
        templateItem.setName(templateName.getText());
        //template module
        templateItem.setModuleID(templateModule.getSelectedItem().getId());
        //template category ID
        if (templateCategory.getSelectedItem() != null) {
            templateItem.setCategoryId(templateCategory.getSelectedItem().getId());
        }
        //template is default
        templateItem.setDefault(defaultYes.getValue());
        //template only show for user
        templateItem.setOnlyMine(onlyMine.getValue());
        templateItem.setSendSummaryPdf(sendSummaryPdf.getValue());
        //show this template in message center template list
        templateItem.showInMessageCenter(showInMessageCenter.getValue());
        //template subject
        templateItem.setSubject(templateSubject.getText());
        //template reply to email
        templateItem.setReplyTo(replyTo.getText());
        //template from user ID
        templateItem.setFromUserID(templateFromUser.getSelectedId());
        templateItem.setFromUserName(fromUserName.getText());
        //template test email
        templateItem.setTestEmail(templateTestEmail.getText());
        //template locale code
        templateItem.setLocaleCode(languages.getSelectedItem() != null ? languages.getSelectedItem().getName() : null);
        //template message body content
        if (isSmsTemplate()) {
            templateItem.setMessageHTML(editorText.getText());
        } else {
            templateItem.setMessageHTML(editorHTML.getData());
        }
        //template attachments
        if (fileUpload != null) {
            templateItem.setAttachments(fileUpload.getAttachedFiles());
        }

        templateItem.setPdfTemplateId(pdfTemplates.getSelectedId());
        return templateItem;
    }

    private void sendTestEmail() {
        int errors = 0;
        errors += markAsError(templateSubject, !Validation.validateTextBoxRequired(templateSubject));
        if (isSmsTemplate()) {
            errors += markAsError(editorText, !Validation.validateTextAreaRequired(editorText));
        } else {
            errors += markAsError(editorHTML, !Validation.validateMaterialEditorRequired(editorHTML));
            errors += markAsError(templateTestEmail, !Validation.validateEmailRequired(templateTestEmail) && !isSmsTemplate());
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return;
        }
        EmailTemplateItem item = new EmailTemplateItem();
        item.setSubject(templateSubject.getText());
        if (isSmsTemplate()) {
            item.setMessageHTML(editorText.getText());
        } else {
            item.setMessageHTML(editorHTML.getData());
        }
        item.setTestEmail(templateTestEmail.getText());

        LoadingPanel.loading(true);
        sendToEmail.setEnabled(false);
        ProfileService.App.get().sendTestEmail(item, new AbstractAsyncCallback<String>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                sendToEmail.setEnabled(true);
            }

            @Override
            public void success(String result) {
                LoadingPanel.loading(false);
                if ("sent".equalsIgnoreCase(result)) {
                    Info.show(wfmStrings.messageHasBeenSent(), Info.Type.INFO);
                } else if ("limit_exceeded".equalsIgnoreCase(result)) {
                    Info.show(settingsStrings.testEmailLimitExceeded(), Info.Type.WARNING);
                } else if ("failed".equalsIgnoreCase(result)) {
                    Info.show("Failed", Info.Type.WARNING);
                }
                sendToEmail.setEnabled(true);
            }
        });
    }

    private boolean valid() {
        int errors = 0;
        int emailError = 0;
        clearErrorStyle();
        errors += markAsError(templateName, !Validation.validateTextBoxRequired(templateName));
        errors += markAsError(templateSubject, !Validation.validateTextBoxRequired(templateSubject));
        if (replyTo.getText() != null && !"".equals(replyTo.getText())) {
            emailError += markAsError(replyTo, !Validation.validateEmailRequired(replyTo));
        }
        if (isSmsTemplate()) {
            errors += markAsError(editorText, !Validation.validateTextAreaRequired(editorText));
        } else {
            errors += markAsError(templateModule, !Validation.validateListBoxRequired(templateModule, new HTML(), wfmStrings.pleaseSelect()));
            errors += markAsError(templateFromUser, !Validation.validateListBoxRequired(templateFromUser, new HTML(), wfmStrings.pleaseSelect()));
            errors += markAsError(editorHTML, !Validation.validateMaterialEditorRequired(editorHTML));
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        } else {
            if (emailError > 0) {
                Info.show(wfmStrings.emailFormatWrong(), Info.Type.WARNING);
                return false;
            }
        }
        return true;
    }


    private void visibleEmailAddressTable(boolean visible) {
        if (templateEmailAddressTable != null) {
            templateEmailAddressTable.setVisible(visible);
        }
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
