package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailServiceAsync;
import com.edatasite.workforce.gwt.crm.client.ui.CheckboxMailingListDataGrid;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: Azazello
 * Date: 5/23/12
 * Time: 3:09 PM
 */
public class AddMessageView extends CustomForm2 implements Constants, Colapse {
    protected static final String antiSpamURL = "https://www." + Utils.getHelpHost() + "/company/terms/";
    protected static final CrmStrings crmStrings = CrmStrings.App.get();
    protected static final CrmMessages crmMessages = CrmMessages.App.get();
    private static final MassMailServiceAsync massMailService = MassMailService.App.get();
    private static final EmailTemplateServiceAsync emailTemplateService = EmailTemplateService.App.get();

    protected String debug_id = "mail_message_";
    protected boolean clone;
    protected boolean isView;
    protected boolean isSMS;
    protected Integer objectID;
    protected Integer campaignID;
    protected String campaignName;
    protected MailMessageItem item;
    //Message Details
    protected CheckboxMailingListDataGrid mailListTable;
    protected CRMLookUp campaignSource;
    private TextArea2 subject;
    private TextArea2 preheader;
    private TextBox fromEmail;
    private TextBox fromName;
    private TextBox replyTo;
    protected DatePicker date;
    protected KpiTimePicker time;
    //Content
    private HorizontalPanel contentPanel;
    private KpiRadioButton text;
    private KpiRadioButton html;
    private DataListBox template;
    private KpiEditor areaHTML;
    private TextArea messageText;
    private VerticalPanel personalAttrsPanel;
    protected KpiCheckBox notSpammer;
    private TextBox toMail;
    private WfmButton2 test;
    //Attachments
    protected GeneralFileUpload fileUpload;
    private VerticalPanel templatePanel;
    //Buttons
    protected WfmButton2 save;
    protected WfmButton2 cancel;
    //Additional items
    private final ArrayList<FileResource> templateAttachments = new ArrayList<>();

    public AddMessageView(String name, String description) {
        super(name, description);
    }

    public AddMessageView(Integer objectID) {
        this("addmessage", wfmStrings.addMessage());
        if (objectID != null && !"null".equals(objectID)) {
            setDescription(crmStrings.editMessage());
            this.objectID = objectID;
        }
    }

    public AddMessageView(Integer objectID, Integer campaignID, String campaignName, boolean clone) {
        this(objectID);
        this.campaignID = campaignID;
        this.campaignName = campaignName;
        this.clone = clone;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        initialize();
        drawForm();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MAIL_LIST_ADD, AddMessageView.this, (sender, args) -> mailListTable.refreshData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MAIL_LIST_EDIT, AddMessageView.this, (sender, args) -> mailListTable.refreshData());
    }

    @Override
    protected void initPredefinedValues() {

    }

    protected void initialize() {
        //Message Details
        mailListTable = new CheckboxMailingListDataGrid(objectID, true, null);
        mailListTable.ensureDebugId(debug_id + "mail_lists");
        mailListTable.setHeight("200px");
        campaignSource = new CRMLookUp(CrmConstants.CRM_CAMPAIGN_ID);
        campaignSource.ensureDebugId(debug_id + "campaign");
        if (campaignID != null && campaignName != null) {
            campaignSource.setSelected(campaignID, campaignName);
        }
        campaignSource.addStyleName(DEFAULT_WIDTH);
        campaignSource.setStyleName("file--AddMessageView");
        subject = new TextArea2(250);
        subject.addStyleName("has-tableDescription");
        subject.ensureDebugId(debug_id + "subject");
        subject.setHeight("100px");
        preheader = new TextArea2(100);
        preheader.addStyleName("has-tableDescription");
        preheader.ensureDebugId(debug_id + "preheader");
        preheader.setHeight("100px");
        fromEmail = new TextBox();
        fromEmail.ensureDebugId(debug_id + "from_email");
        fromEmail.addStyleName(DEFAULT_WIDTH);
        fromName = new TextBox();
        fromName.ensureDebugId(debug_id + "from_name");
        fromName.addStyleName(DEFAULT_WIDTH);
        replyTo = new TextBox();
        replyTo.ensureDebugId(debug_id + "reply_to");
        replyTo.addStyleName(DEFAULT_WIDTH);
        date = new DatePicker();
        date.ensureDebugId(debug_id + "date");
        date.addStyleName(DEFAULT_WIDTH);
        date.setDate(new Date());
        time = new KpiTimePicker(true);
        time.ensureDebugId(debug_id + "time");
        time.addStyleName("timepicker form-control ");
        time.setValue(KpiTimePicker.getHoursAndMinutes(new Date()));
        //Content
        text = new KpiRadioButton("mesFormat", wfmStrings.text(), true);
        html = new KpiRadioButton("mesFormat", crmStrings.html(), true);
        html.setValue(true);
        html.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue()) {
                contentPanel.clear();
                contentPanel.add(areaHTML);
                contentPanel.add(personalAttrsPanel);
            }
        });
        text.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue()) {
                contentPanel.clear();
                contentPanel.add(messageText);
                contentPanel.add(personalAttrsPanel);
            }
        });
        templatePanel = new VerticalPanel();
        templatePanel.setSpacing(3);
        template = new DataListBox();
        template.addStyleName(DEFAULT_WIDTH);
        template.addValueChangeHandler(event -> {
            if (template.isSomethingSelected()) {
                final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, crmMessages.template(template.getSelectedItem().getName()),
                        new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                generateTemplate();
                            }
                        });
                wfmMessageBox.setTitle(wfmStrings.warning());
                wfmMessageBox.open();
            }
        });
        templatePanel.add(template);

        areaHTML = new KpiEditor();
        areaHTML.setWidth("770px");

        messageText = new TextArea();
        messageText.setWidth("770px");
        messageText.setHeight("28.25em");

        personalAttrsPanel = new VerticalPanel();
        personalAttrsPanel.setSpacing(3);

        contentPanel = new HorizontalPanel();
        contentPanel.addStyleName("spacing5-padding5");
        contentPanel.setWidth("770px");
        contentPanel.add(areaHTML);
        contentPanel.add(personalAttrsPanel);

        toMail = new TextBox();
        toMail.addStyleName(DEFAULT_WIDTH);
        test = new WfmButton2(crmStrings.sendTestMessage(), WfmButton2.BTN_SECONDARY, clickEvent -> {
            if (!validate(true)) {
                return;
            }
            LoadingPanel.loading(true);
            MailMessageItem testItem = new MailMessageItem();
            testItem.setFrom(fromEmail.getText());
            testItem.setFullName(fromName.getText());
            testItem.setReplyTo(replyTo.getText());
            testItem.setSubject(subject.getText());
            testItem.setPreheader(preheader.getText());
            testItem.setIsHtml(html.getValue());
            testItem.setContent(html.getValue() ? areaHTML.getData() : messageText.getText());
            if (Utils.hasGenericAccess(GenericSettingsEnum.MASSMAILING_ATTACHMENT_ENABLED)) {
                testItem.setAttachments(fileUpload.getAttachedFiles());
            }
            massMailService.sendTestEmail(testItem, toMail.getText(), new AbstractAsyncCallback<Void>() {
                public void failure(Throwable th) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void success(Void a) {
                    LoadingPanel.loading(false);
                    Info.show(crmStrings.testMessageSuccess(), Info.Type.INFO);
                }
            });
        });
        test.ensureDebugId(debug_id + "test");

        notSpammer = new KpiCheckBox(wfmStrings.iHaveRead() + " " + "<a style=\"vertical-align: top;\" href='" + antiSpamURL + "' target=\"_blank\"> " + wfmStrings.termsOfService() + "</a>, " + wfmStrings.andAgreeNotToSpam(), true);
        notSpammer.ensureDebugId(debug_id + "not_spammer");
        if (!clone && objectID != null) {
            notSpammer.setValue(true);
        }

    }

    protected void drawForm() {
        addTitleField(CustomFormConstants.CRM_MESSAGE_DETAILS, wfmStrings.messageDetails());
        addField(CustomFormConstants.CRM_MESSAGE_SUBSCRIPTION_LISTS, mailListTable);//required
        addField(CustomFormConstants.CRM_MESSAGE_SOURCE, campaignSource);
        addField(CustomFormConstants.CRM_MESSAGE_SUBJECT, subject);//*
        addField(CustomFormConstants.CRM_MESSAGE_PREHEADER, preheader);
        addField(CustomFormConstants.CRM_MESSAGE_FROM, fromEmail);//*/*from Email */
        addField(CustomFormConstants.CRM_MESSAGE_FULLNAME, fromName);
        addField(CustomFormConstants.CRM_MESSAGE_REPLYTO, replyTo);
        addField(CustomFormConstants.CRM_MESSAGE_DATETABLE, date);
        addField(CustomFormConstants.CRM_MESSAGE_TIMETABLE, time);

        addTitleField(CustomFormConstants.CRM_MESSAGE_CONTENT, wfmStrings.emailContent());
        HorizontalPanel formatPanel = new HorizontalPanel();
        formatPanel.addStyleName("default-width options-row");
        formatPanel.add(html);
        formatPanel.add(text);
        addField(CustomFormConstants.CRM_MESSAGE_FORMAT, formatPanel);
        addField(CustomFormConstants.CRM_MESSAGE_CATEGORY, templatePanel);
        addField(CustomFormConstants.CRM_MESSAGE_FIELD, contentPanel);//*
//        HorizontalPanel testPanel = new HorizontalPanel();
//        testPanel.addStyleName(DEFAULT_WIDTH);
//        testPanel.setSpacing(3);
//        testPanel.add(toMail);
//        testPanel.add(test);
        addField(CustomFormConstants.CRM_MESSAGE_SENT, new AdvancedInputGroup(null, toMail, test, true, false));
        addField(CustomFormConstants.CRM_MESSAGE_ANTI_SPAN, notSpammer);

        show();
    }

    @Override
    protected void addButtons() {
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save(false));
        saveButton.ensureDebugId(debug_id + "_message_add_view");
        addButton(saveButton);
        addButton(new WfmButton2(wfmStrings.draft(), WfmButton2.BTN_DEFAULT, clickEvent -> save(true)));
    }

    @Override
    protected void getDataToFillFields() {
        massMailService.getMailMessage(objectID, isSMS, isView, new AbstractAsyncCallback<MailMessageItem>() {
            public void failure(Throwable throwable) {
            }

            public void success(final MailMessageItem result) {
                Scheduler.get().scheduleDeferred(() -> {
                    item = result;
                    fillFields();
                });
            }
        });
    }

    protected void fillFields() {
        template.setItems(item.getTemplates());
        if (item.getPersonalAttributes().size() > 0) {
            personalAttrsPanel.add(new HTML("<b class=customTitle>" + wfmStrings.personalizationAttributes() + ":</b>"));
            item.getPersonalAttributes().forEach(attr -> personalAttrsPanel.add(new HTML(attr)));
        }
        if (item.getCampaignId() != null && item.getCampaignName() != null) {
            campaignSource.setSelected(item.getCampaignId(), item.getCampaignName());
        }
        subject.setText(item.getSubject());
        preheader.setText(item.getPreheader());
        fromEmail.setText(item.getFrom());
        fromName.setText(item.getFullName());
        replyTo.setText(item.getReplyTo());
        date.setDate(item.getScheduled());
        if (item.getScheduled() != null) {
            time.setValue(KpiTimePicker.getHoursAndMinutes(item.getScheduled()));
        }
        if (objectID != null) {
            if (item.isHtml()) {
                areaHTML.setData(item.getContent());
            } else {
                text.setValue(true);
                messageText.setText(item.getContent());
                contentPanel.clear();
                contentPanel.add(messageText);
                contentPanel.add(personalAttrsPanel);
            }
        }
        //Attachments
        if (Utils.hasGenericAccess(GenericSettingsEnum.MASSMAILING_ATTACHMENT_ENABLED)) {
            fileUpload = new GeneralFileUpload(Constants.F_MASS_MAILING, objectID, objectID);
            addField(CustomFormConstants.ATTACHMENTS, fileUpload, null, true);
        }
    }

    protected boolean validate(boolean preview) {
        mailListTable.removeStyleName(ERROR_FORM_STYLE);
        date.removeStyleName(ERROR_FORM_STYLE);
        time.removeStyleName(ERROR_FORM_STYLE);
        if (!notSpammer.getValue()) {
            notSpammer.addStyleName(ERROR_FORM_STYLE);
            Info.show(crmStrings.agreeTermsService(), Info.Type.WARNING);
            return false;
        }
        /*if (!preview && date.getDate() == null) {
            Info.show(crmStrings.correctDateTime(), Info.Type.WARNING);
            return false;
        } else if (!preview) {
            Date schDate = new Date(date.getDate().getYear(), date.getDate().getMonth(), date.getDate().getDate(), time.getValue()[0], time.getValue()[1]);
            Date now = new Date();
            if (schDate.before(now)) {
                date.addStyleName(ERROR_FORM_STYLE);
                time.addStyleName(ERROR_FORM_STYLE);
                Info.show(crmStrings.correctDateTime(), Info.Type.WARNING);
                return false;
            } else if (schDate.getYear() == now.getYear() && schDate.getMonth() == now.getMonth() && schDate.getDate() == now.getDate() &&
                    schDate.getHours() == now.getHours() && schDate.getMinutes() - now.getMinutes() <= 5) {
                date.addStyleName(ERROR_FORM_STYLE);
                time.addStyleName(ERROR_FORM_STYLE);
                Info.show(crmStrings.scheduleTimeFiveMinutes(), Info.Type.WARNING);
                return false;
            }
        }*/
        int errors = 0;
        if (!Validation.validateTextBoxRequired(fromEmail)) {
            errors++;
        }
        if (!Validation.validateTextAreaRequired(subject)) {
            errors++;
        }
        if (Utils.isNullOrEmpty(html.getValue() ? areaHTML.getData() : messageText.getText())) {
            areaHTML.addStyleName(ERROR_FORM_STYLE);
            messageText.addStyleName(ERROR_FORM_STYLE);
            errors++;
        }
        if (!preview && mailListTable.getSelectItemsList().size() == 0) {
            mailListTable.addStyleName(ERROR_FORM_STYLE);
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        } else if (!Validation.validateEmailRequired(preview ? toMail : fromEmail, null, false)) {
            Info.show(wfmStrings.pleaseEnterValidEmail(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    protected void setItemValues() {
        item.setObjectID(clone ? null : objectID);
        item.setStatus(MessageStatusEnum.PENDING);
        item.setCampaignId(campaignSource.getSelectedItemID());
        item.setSubject(subject.getText());
        item.setPreheader(preheader.getText());
        item.setFrom(fromEmail.getText());
        item.setFullName(fromName.getText());
        item.setReplyTo(replyTo.getText());
        item.setIsHtml(html.getValue());
        item.setContent(html.getValue() ? areaHTML.getData() : messageText.getText());
        if (date.getDate() != null) {
            item.setScheduled(new Date(date.getDate().getYear(), date.getDate().getMonth(), date.getDate().getDate(), time.getValue()[0], time.getValue()[1]));
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.MASSMAILING_ATTACHMENT_ENABLED)) {
            item.setAttachments(fileUpload.getAttachedFiles());
            item.setTemplateAttachments(templateAttachments);
        }
    }

    protected void save(boolean draft) {
        if (!draft && !validate(false)) {
            return;
        }
        LoadingPanel.loading(true);
        setItemValues();
        item.setStatus(draft ? MessageStatusEnum.DRAFT : MessageStatusEnum.PENDING);
        if (!draft) {
            Date scheduled = null;
            if (date.getDate() != null) {
                scheduled = new Date(date.getDate().getYear(), date.getDate().getMonth(), date.getDate().getDate(), time.getValue()[0], time.getValue()[1]);
            }
            massMailService.checkMassMailLimit(getSubscriptions(), scheduled, new AbstractAsyncCallback<Long>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(Long limit) {
                    LoadingPanel.loading(false);
                    if (limit < 0) {
                        scheduleOrSave();
                    } else {
                        Info.show(crmMessages.massMailingOverLimit(limit + "", Utils.getSupportEmail()), Info.Type.WARNING);
                    }
                }
            });
        } else {
            scheduleOrSave();
        }
    }

    private void scheduleOrSave() {
        LoadingPanel.loading(true);
        massMailService.saveMailMessage(item, getSubscriptions(), new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Integer resultId) {
                LoadingPanel.loading(false);
                if (objectID == null || clone) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MESSAGE_ADD, null, AddMessageView.this);
                } else {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MESSAGE_EDIT, null, AddMessageView.this);
                }
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.message()), Info.Type.INFO);
                closeTab("message|summary/" + resultId + "/" + (item.isSmsMessage() ? "sms" : "") + "/false");

            }
        });
    }

    private void generateTemplate() {
        EntityToEmailTemplate emailTemplateInner = new EntityToEmailTemplate();
        emailTemplateInner.setEmailTemplateId(template.getSelectedId());
        emailTemplateService.generateEmailTemplate(emailTemplateInner, new AbstractAsyncCallback<EmailTemplateItem>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(final EmailTemplateItem result) {
                subject.setText(result.getSubject());
                fromEmail.setText(result.getFromEmail());
                fromName.setText(result.getFromUserName());
                replyTo.setText(result.getReplyTo());
                areaHTML.setData(result.getMessageHTML());
                messageText.setText(result.getMessageHTML());
                if (Utils.hasGenericAccess(GenericSettingsEnum.MASSMAILING_ATTACHMENT_ENABLED)) {
                    initEmailTemplateAttachments(result.getFileResources());
                }
            }
        });
    }

    private void initEmailTemplateAttachments(ArrayList<FileResource> files) {
        //clean other emailtemplate  attachments;
        templateAttachments.clear();
        templatePanel.clear();
        templatePanel.add(template);
        if (files != null && files.size() > 0) {
            for (FileResource fileResource : files) {
                fileResource.setSourceType(FileResource.EMAIL_TEMPLATE);
                addTemplateFiles(fileResource);
            }
        }
    }

    private void addTemplateFiles(final FileResource fileResource) {
        templateAttachments.add(fileResource);
        final HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(5);
        SimpleLink downloadLink = new SimpleLink(fileResource.getName());
        downloadLink.addClickHandler(event -> Window.open(fileResource.getDownloadUrl(GWT.getHostPageBaseURL() + CommandConstants.COMMON_URL + "/"), "_blank", ""));
        hp.add(downloadLink);
        final SimpleLink removeAttachment = new SimpleLink(SimpleLink.REMOVE_ICON);
        removeAttachment.addClickHandler(event -> {
            if (fileResource != null && !"".equals(fileResource)) {
                templateAttachments.remove(fileResource);
            }
            hp.removeFromParent();
        });
        hp.add(removeAttachment);
        templatePanel.add(hp);
    }

    private ArrayList<Integer> getSubscriptions() {
        return mailListTable.getSelectedIdsList();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.MESSAGE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return objectID != null ? PermissionConstants.CRM_MESSAGE_EDIT : PermissionConstants.CRM_MESSAGE_ADD;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
