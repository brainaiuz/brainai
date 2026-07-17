package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: muratov
 * Date: Mar 19, 2010
 * Time: 5:36:10 PM
 */
public class EmailTemplateSummaryView extends CustomForm implements Constants, Colapse {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    private WfmButton2 editButton;
    private final Integer int_objectID;
    private EmailTemplateItem templateItem;
    private HTML template_name, template_module, template_type, template_category, template_is_default, template_subject, template_from_user_email, template_from_user_name, template_message_content, template_only_mine, template_reply_to, template_language;
    private HTML sendSummaryPdf;

    private final String test_code_ID_name = "summary_template_view_";


    public EmailTemplateSummaryView(Integer int_objectID) {
        super("summary", settingsStrings.emailTemplateSummary());
        this.int_objectID = int_objectID;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        //edit button
        editButton = addButton(wfmStrings.edit(), BTN_DEFAULT_OUTLINE, null, (test_code_ID_name + "edit_button"), event -> {
            //register edit logic
            SinksContainerFactory.entryPoint.onHistoryChanged("templateedit|addtemplate/" + templateItem.getObjectId() + "/" + templateItem.getCompanyEmailTemplate(), templateItem.getName());
        });
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ProfileService.App.get().getEmailTemplate(int_objectID, new AbstractAsyncCallback<EmailTemplateItem>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(EmailTemplateItem result) {
                LoadingPanel.loading(false);
                templateItem = result;
                if (EmailTemplateConstants.DEFAULT_EMAIL_TEMPLATE.equals(templateItem.getCompanyEmailTemplate())) {
                    editButton.setText(wfmStrings.copy());
                }
                fillFormWithData();
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.EMAIL_TEMPLATE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
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
        template_name = new HTML();
        template_name.addStyleName(DEFAULT_WIDTH);
        template_name.ensureDebugId(test_code_ID_name + "template_name");
        //template type
        template_type = new HTML();
        template_type.addStyleName(DEFAULT_WIDTH);
        template_type.ensureDebugId(test_code_ID_name + "template_type");
        //template module
        template_module = new HTML();
        template_module.addStyleName(DEFAULT_WIDTH);
        template_module.ensureDebugId(test_code_ID_name + "template_module");
        //template category
        template_category = new HTML();
        template_category.addStyleName(DEFAULT_WIDTH);
        template_category.ensureDebugId(test_code_ID_name + "template_category");
        //template is default
        template_is_default = new HTML();
        template_is_default.addStyleName(DEFAULT_WIDTH);
        template_is_default.ensureDebugId(test_code_ID_name + "template_is_default");
        //template only mine
        template_only_mine = new HTML();
        template_only_mine.addStyleName(DEFAULT_WIDTH);
        template_only_mine.ensureDebugId(test_code_ID_name + "template_only_mine");
        //template subject
        template_subject = new HTML();
        template_subject.addStyleName(DEFAULT_WIDTH);
        template_subject.ensureDebugId(test_code_ID_name + "template_subject");
        //template from email
        template_from_user_email = new HTML();
        template_from_user_email.addStyleName(DEFAULT_WIDTH);
        template_from_user_email.ensureDebugId(test_code_ID_name + "template_from_user_email");
        //template from email
        template_from_user_name = new HTML();
        template_from_user_name.addStyleName(DEFAULT_WIDTH);
        template_from_user_name.ensureDebugId(test_code_ID_name + "template_from_user_name");
        //template message content body
        template_message_content = new HTML();
        template_message_content.addStyleName(DEFAULT_WIDTH);
        template_message_content.ensureDebugId(test_code_ID_name + "template_message_content");
        //template message reply to
        template_reply_to = new HTML();
        template_reply_to.addStyleName(DEFAULT_WIDTH);
        template_reply_to.ensureDebugId(test_code_ID_name + "template_message_reply_to");
        //template language
        template_language = new HTML();
        template_language.addStyleName(DEFAULT_WIDTH);
        template_language.ensureDebugId(test_code_ID_name + "template_language");

        sendSummaryPdf = new HTML();
        sendSummaryPdf.addStyleName(DEFAULT_WIDTH);
        sendSummaryPdf.ensureDebugId(test_code_ID_name + "template_summary_pdf");

        FlexTable templateMessageContentTable = new FlexTable();
        templateMessageContentTable.setHTML(0, 0, "<b>" + getTitle(wfmStrings.templateBody()) + "</b>");
        templateMessageContentTable.setWidget(0, 1, template_message_content);
        templateMessageContentTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        templateMessageContentTable.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        templateMessageContentTable.getCellFormatter().setWidth(0, 0, "145px");

        //template attachments
        GeneralFileUpload fileUpload = new GeneralFileUpload(F_EMAIL_TEMPLATE, int_objectID, int_objectID);
        fileUpload.ensureDebugId(test_code_ID_name + "attachments");

        //template items -> 1
        addTitleField(CustomFormConstants.DETAILS, settingsStrings.emailTemplateInfo());

        addField(CustomFormConstants.NAME, template_name, getTitle(wfmStrings.name()));
        addField(CustomFormConstants.TYPE, template_type, getTitle(wfmStrings.type()));
        addField(CustomFormConstants.CATEGORY, template_category, getTitle(wfmStrings.category()));
        addField(CustomFormConstants.REPLY_TO, template_reply_to, getTitle(wfmStrings.replyToOnly()));
        addField(CustomFormConstants.ONLY_MINE, template_only_mine, getTitle(wfmStrings.priv()));
        addField(CustomFormConstants.PDF_VERSION, sendSummaryPdf, getTitle(settingsStrings.sendPdf()));

        addField(CustomFormConstants.MODULE, template_module, getTitle(wfmStrings.apps()));
        addField(CustomFormConstants.IS_DEFAULT, template_is_default, getTitle(wfmStrings.isDefault()));
        addField(CustomFormConstants.SUBJECT, template_subject, getTitle(wfmStrings.subject()));
        addField(CustomFormConstants.LANGUAGE, template_language, getTitle(wfmStrings.language()));

        addField(CustomFormConstants.FROM_USER, template_from_user_email, getTitle(wfmStrings.fromEmail()));
        addField(WORKFLOW_ALERT_FORM.FROM_NAME, template_from_user_name, getTitle(wfmStrings.fromName()));
        addField(CustomFormConstants.MESSAGE_CONTENT, templateMessageContentTable, null);
        addField(CustomFormConstants.EMAIL, new FlexTable(), null);
        //attachments -> 2
        addField(CustomFormConstants.ATTACHMENTS, fileUpload, wfmStrings.attachments(), true);

        show();
    }

    private void fillFormWithData() {
        //template name
        template_name.setHTML(templateItem.getName() != null ? templateItem.getName() : "");
        //template type
        template_type.setHTML("html");
        //template module
        template_module.setHTML(templateItem.getModule() != null ? templateItem.getModule() : "");
        //template category
        template_category.setHTML(templateItem.getCategoryName() != null ? templateItem.getCategoryName() : "");
        //template is default
        template_is_default.setHTML(templateItem.isDefault() ? wfmStrings.yes() : wfmStrings.no());
        //template is only mine
        template_only_mine.setHTML(templateItem.isOnlyMine() ? wfmStrings.yes() : wfmStrings.no());
        //template subject
        template_subject.setHTML(templateItem.getSubject() != null ? templateItem.getSubject() : "");
        //template from email
        template_from_user_email.setHTML(templateItem.getFromEmail() != null ? templateItem.getFromEmail() : "");
        //template from name
        template_from_user_name.setHTML(templateItem.getFromUserName() != null ? templateItem.getFromUserName() : "");
        //template content message
        template_message_content.setHTML(templateItem.getMessageHTML() != null ? templateItem.getMessageHTML() : "");

        template_reply_to.setHTML(templateItem.getReplyTo() != null ? templateItem.getReplyTo() : "");

        template_language.setHTML(templateItem.getLanguage() != null ? templateItem.getLanguage().getName() : "");

        sendSummaryPdf.setHTML(templateItem.isSendSummaryPdf() ? wfmStrings.yes() : wfmStrings.no());
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
