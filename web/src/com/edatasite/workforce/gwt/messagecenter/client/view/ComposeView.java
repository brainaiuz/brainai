package com.edatasite.workforce.gwt.messagecenter.client.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.fileUpload.KpiFileUploadForm;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectContactLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by: Azazello
 * Date: 1/31/2018
 * Time: 5:16 PM
 */
public class ComposeView extends KpiSideNavBox implements Constants {
    interface ComposeViewUiBinder extends UiBinder<HTMLPanel, ComposeView> {
    }

    private static ComposeViewUiBinder ourUiBinder = GWT.create(ComposeViewUiBinder.class);
    private static final MessageCenterServiceAsync service = MessageCenterService.App.get();

    @UiField
    HTMLPanel panel;
    @UiField
    Label fromLabel;
    @UiField
    DataListBox from;
    @UiField
    Label toLabel;
    @UiField
    HTMLPanel to;
    @UiField
    HTMLPanel bccPanel;
    @UiField
    Label subjectLabel;
    @UiField
    TextBox subject;
    @UiField
    Label templateLabel;
    @UiField
    DataListBox template;
    @UiField
    HTMLPanel content;
    @UiField
    MaterialPanel uploadPanel;
    @UiField
    HTMLPanel fromContainer;
    @UiField
    HTMLPanel templateContainer;
    @UiField
    Label linkLabel;
    @UiField
    MaterialPanel linkPanel;

    KpiFileUploadForm attachments;

    private MultiSelectContactLookUp toText;
    private VerticalPanel bccCCPanel;
    private HorizontalPanel bccCCLinkPanel;
    private MaterialLink bccLink;
    private MaterialLink ccLink;
    private MultiSelectContactLookUp bccText;
    private MultiSelectContactLookUp ccText;
    private String ccBccDefaultText;
    private KpiEditor editor;
    private AddTaggingView taggingShell;
    private SimpleLink addLink;
    private boolean ccShown;
    private boolean bccShown;
    private boolean subjectCheked;
    private boolean showSignatureOnTop;
    private boolean fromLead;
    private boolean fromContact;
    private String signature;
    private HTML defaultHTML = new HTML();
    private ArrayList<RelationItem> relationItems = new ArrayList<>();
    private WfmButton2 saveButton, closeButton;

    private String email;
    private Integer caseID;
    private Email emailItem;
    private CaseItem caseItem;
    private boolean reply;
    private boolean forward;

    public ComposeView(String email, RelationItem... relationItems) {
        super(KpiSideNavBox.WIDE_FORM_WIDTH);
        setStyleName(getElement(), "quick-add", true);
        ourUiBinder.createAndBindUi(this);
        this.email = email;
        if (relationItems != null && relationItems.length > 0) {
            for (RelationItem rel : relationItems) {
                if (rel != null) {
                    if (RelationItem.TYPE_LEAD.equals(rel.getToType())) {
                        fromLead = true;
                    } else if (RelationItem.TYPE_CONTACT.equals(rel.getToType())) {
                        fromContact = true;
                    }
                    rel.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                    this.relationItems.add(rel);
                }
            }
        }
        init();
    }

    public ComposeView(Integer caseID, Email emailItem, boolean reply, boolean forward, RelationItem... relationItems) {
        super(KpiSideNavBox.WIDE_FORM_WIDTH);
        setStyleName(getElement(), "quick-add", true);
        ourUiBinder.createAndBindUi(this);
        this.emailItem = emailItem;
        this.caseID = caseID;
        this.reply = reply;
        this.forward = forward;
        if (relationItems != null && relationItems.length > 0) {
            for (RelationItem item : relationItems) {
                if (item == null) {
                    continue;
                }
                this.relationItems.add(item);
            }
        }
        init();
    }

    private void init() {
        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.compose());
        addHeader(header);

        fromLabel.setText(wfmStrings.from());
        toLabel.setText(wfmStrings.to());
        subjectLabel.setText(wfmStrings.subject());
        templateLabel.setText(wfmStrings.template());
        linkLabel.setText(wfmStrings.links());

        toText = new MultiSelectContactLookUp(Constants.BY_BOTH, this.panel);
        toText.getList().setWidth("100%");
        to.add(toText);

        ccLink = new MaterialLink(wfmStrings.cc());
        ccLink.addClickHandler(clickEvent -> showBccCC(true));
        ccLink.setTooltip(wfmStrings.carbonCopy());
        ccText = new MultiSelectContactLookUp(Constants.BY_BOTH, this.panel);
        ccBccDefaultText = ccText.getTextBox().getValue();
        ccText.getList().setWidth("100%");
        bccLink = new MaterialLink(wfmStrings.bcc());
        bccLink.addClickHandler(clickEvent -> showBccCC(false));
        bccLink.setTooltip(wfmStrings.blindCarbonCopy());
        bccText = new MultiSelectContactLookUp(Constants.BY_BOTH, this.panel);
        bccText.getList().setWidth("100%");
        bccCCLinkPanel = new HorizontalPanel();
        bccCCLinkPanel.setWidth("150px");
        bccCCLinkPanel.setSpacing(3);
        bccCCLinkPanel.add(ccLink);
        bccCCLinkPanel.add(bccLink);
        bccCCPanel = new VerticalPanel();
        bccCCPanel.setSpacing(3);
        bccPanel.add(bccCCPanel);
        bccPanel.add(bccCCLinkPanel);

        attachments = new KpiFileUploadForm(F_DEFAULT);
        uploadPanel.add(attachments);

        template.addValueChangeHandler(changeEvent -> onTemplateChange());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_RELATION, linkPanel, (sender, args) -> {
            if (args != null) {
                relationItems = (ArrayList<RelationItem>) args;
            }
            drawLinks(false, emailItem != null ? emailItem.getTrackerID() : caseItem != null ? caseItem.getTrackerID() : null, subject.getText());
        });

        editor = new KpiEditor(true);
        content.add(editor);
        drawLinks(true, emailItem != null ? emailItem.getTrackerID() : caseItem != null ? caseItem.getTrackerID() : null, null);

        addBody(panel);
        addOpeningHandler(event -> getOpeningData());

        saveButton = new WfmButton2(wfmStrings.send(), WfmButton2.BTN_PRIMARY);
        saveButton.ensureDebugId("compose_email_send");
        closeButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_RESET);

        saveButton.addClickHandler(event -> sendMessage());
        closeButton.addClickHandler(event -> remove());
        addFooter(saveButton);
        addFooter(closeButton);

        show();
    }

    public void getOpeningData() {
        service.getUserEmailAccounts(true, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                from.setWithoutNullLabel(true);
                from.setItems(result);
//                String fromEmail = null;
//                if((caseItem != null || emailItem != null) && !forward){
//                    fromEmail = emailItem != null ? emailItem.getToEmail() : caseItem.getReplyTo();
//                }
//                if (!Utils.isNullOrEmpty(fromEmail)) {
//                    from.setSelectedByValue(fromEmail);
//                } else {
//                }
                for (SelectItem it : result) {
                    if (it.isSelected()) {
                        from.setSelected(it);
                        break;
                    }
                }
//                from.setEnabled(result.length > 1);
                //Dont display from fields if there is only one option
                fromContainer.setVisible(result.length > 1);
            }
        });
        if (caseID == null) {
            EmailTemplateService.App.get().getMessageCenterEmailTemplates(new ArrayList<>(Collections.singletonList(getModuleCode())), new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] result) {
                    template.setItems(result);
                    //Dont display template if there are no options
                    templateContainer.setVisible(result.length > 0);
                }
            });
        } else {
            EmailTemplateService.App.get().getEmailTemplates(CASE_REPLIED_CATEGORY, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void onSuccess(SelectItem[] result) {
                    template.setItems(result);
                }
            });
        }
        if (email != null) {
            toText.setEmails(email);
            getSignature();
        } else if (emailItem != null) {
            setReplyOrForwardValues();
            getSignature();
        } else if (caseID != null) {
            service.getCase(caseID, new AbstractAsyncCallback<CaseItem>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(CaseItem result) {
                    caseItem = result;
                    setReplyOrForwardValues();
                    getSignature();
                }
            });
        } else {
            getSignature();
        }
    }

    private void setReplyOrForwardValues() {
        Date createdDate = emailItem != null ? emailItem.getDate() : caseItem.getCreatedDate();
        String fromEmail = emailItem != null ? emailItem.getFromEmail() : caseItem.getEmail();
        String fromName = emailItem != null ? emailItem.getFromName() : caseItem.getReportedBy();
        String toEmail = emailItem != null ? emailItem.getToEmail() : caseItem.getReplyTo();
        String toName = emailItem != null ? emailItem.getToName() : caseItem.getCaseAssigneeName();
        String subjectText = emailItem != null ? emailItem.getSubject() : caseItem.getSubject();
        String content = emailItem != null ? emailItem.getContent() : caseItem.getDescription();
        if (!forward) {
//            if (!Utils.isNullOrEmpty(toEmail)) {
//                from.setSelectedByValue(toEmail);
//            }
            if (!Utils.isNullOrEmpty(fromEmail)) {
                toText.setEmails(fromEmail);
            }
        }
        if (!(reply || forward)) {
            String cc = emailItem != null ? emailItem.getCc() : caseItem.getCcEmails();
            if (!Utils.isNullOrEmpty(cc)) {
                showBccCC(true);
                ccText.setEmails(cc);
            }
            if (emailItem != null && !Utils.isNullOrEmpty(emailItem.getBcc())) {
                showBccCC(false);
                bccText.setEmails(emailItem.getBcc());
            }
        }
        if (!Utils.isNullOrEmpty(subjectText)) {
            subject.setText(forward ? "Fwd: " + subjectText : subjectText);
        }
        defaultHTML.setHTML(forward ? getForwardContent(createdDate, fromName, fromEmail, toName, subjectText, content) : getReplyContent(createdDate, fromName, fromEmail, content));
        if (forward && emailItem != null && emailItem.getAttachments() != null && !emailItem.getAttachments().isEmpty()) {
            attachments.addFiles(emailItem.getAttachments());
        }
    }

    private String getReplyContent(Date createdDate, String fromName, String fromEmail, String content) {
        return "<html style=\"background:none repeat scroll 0% 0% transparent;\">" +
                "<head><style>body {margin:8px} .LW-yrriRe {font:normal small arial}\n" +
                " img {-moz-force-broken-image-icon: 1;}</style></head>" +
                "<body><div>" + "On " + DateTimeFormat.getFormat("MMM dd, yyyy [HH:mm]").format(createdDate) + ", " +
                (!Utils.isNullOrEmpty(fromName) ? "<b>" + fromName + "</b>" : "") + " " +
                (!Utils.isNullOrEmpty(fromEmail) ? "&lt;" + fromEmail + "&gt;" : "") + " wrote:" +
                "<blockquote style=\"margin: 0pt 0pt 0pt 0.8ex; border-left: 1px solid rgb(204, 204, 204); padding-left: 1ex;\">" +
                (!Utils.isNullOrEmpty(content) ? content : "") + "</blockquote></div></body></html>";
    }

    private String getForwardContent(Date createdDate, String fromName, String fromEmail, String recipient, String subject, String content) {
        return "</br>---------- Forwarded message ----------<br/>" +
                "From: " + (!Utils.isNullOrEmpty(fromName) ? fromName : Utils.getUserFullName()) +
                " &lt;" + (!Utils.isNullOrEmpty(fromEmail) ? fromEmail : Utils.getUserEmail()) + "&gt; " + "<br/>" +
                "Date: " + DateTimeFormat.getFormat("EEE, MMM dd, yyyy [HH:mm]").format(createdDate) + "<br/>" +
                "Subject: " + (!Utils.isNullOrEmpty(subject) ? subject : "") + "<br/>" +
                "To: " + (!Utils.isNullOrEmpty(recipient) ? recipient : "") + "<br/><br/>" + (!Utils.isNullOrEmpty(content) ? content : "");
    }

    private void getSignature() {
        AllInOneService.App.get().getSignature(new AbstractAsyncCallback<SignatureItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SignatureItem result) {
                if (result != null && !Utils.isNullOrEmpty(result.getSignature())) {
                    signature = result.getSignature();
                    showSignatureOnTop = result.isShowSignatureOnTop();
                } else {
                    signature = "";
                    showSignatureOnTop = true;
                }
                drawSignature();
            }
        });
    }

    private void drawSignature() {
        if (defaultHTML.getHTML() == null) {
            defaultHTML.setHTML("");
        }
        if (showSignatureOnTop ? !defaultHTML.getHTML().startsWith(signature) : !defaultHTML.getHTML().endsWith(signature)) {
            if (showSignatureOnTop) {
                defaultHTML.setHTML(signature + defaultHTML.getHTML());
            } else {
                defaultHTML.setHTML("<br>----------------------------------------<br>" + defaultHTML.getHTML() + signature);
            }
        }
        if (editor != null) {
            editor.setData("<br>" + defaultHTML.getHTML());
        }
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

    private void onTemplateChange() {
        if (template.isSomethingSelected()) {
            EntityToEmailTemplate emailTemplateInner = new EntityToEmailTemplate();
            if (caseID != null) {
                emailTemplateInner.setEntityId(caseID);
            } else if (toText.getSelectedItems() != null && toText.getSelectedItems().size() > 0 && toText.getSelectedItems().get(0) != null) {
                emailTemplateInner.setMailReceiverEmail(toText.getSelectedItems().get(0).getName());
            }
            emailTemplateInner.setEntityType(caseID != null ? CASE_REPLIED_CATEGORY : "emailItem");
            emailTemplateInner.setEmailTemplateId(template.getSelectedId());
            LoadingPanel.loading(true, panel);
            if (caseID == null) {
                EmailTemplateService.App.get().generateMessageCenterTemplateItem(emailTemplateInner, null, null, null, new AsyncCallback<EmailTemplateItem>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false, panel);
                    }

                    @Override
                    public void onSuccess(EmailTemplateItem result) {
                        LoadingPanel.loading(false, panel);
                        if (result != null) {
                            fillEmailTemplateContent(result);
                        }
                    }
                });
            } else {
                AllInOneService.App.get().generateReplyToReporterCaseItem(emailTemplateInner, null, new AbstractAsyncCallback<EmailTemplateItem>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false, panel);
                    }

                    @Override
                    public void onSuccess(EmailTemplateItem result) {
                        LoadingPanel.loading(false, panel);
                        if (result != null) {
                            fillEmailTemplateContent(result);
                        }
                    }
                });
            }
        } else {
            attachments.clearFiles();
            if (emailItem != null || caseItem != null) {
                setReplyOrForwardValues();
                drawSignature();
            } else {
                subject.setText("");
                defaultHTML.setHTML("");
                drawSignature();
            }
        }
    }

    private void fillEmailTemplateContent(EmailTemplateItem result) {
        if(!reply) {
            subject.setText(result.getSubject());
        }
        editor.setData(result.getMessageHTML() + (defaultHTML.getHTML() != null ? defaultHTML.getHTML() : ""));
        if (result.getFileResources() != null && result.getFileResources().size() > 0) {
            attachments.clearFiles();
            attachments.addFiles(result.getFileResources());
        }
    }

    private String getModuleCode() {
        return fromLead ? "ET_LEAD_MODULE" : fromContact ? "ET_CONTACT_MODULE" : null;
    }

    private void drawLinks(boolean forceToCreateNew, Integer trackerID, String subject) {
        if (taggingShell == null || forceToCreateNew) {
            taggingShell = new AddTaggingView(trackerID, RelationItem.TYPE_EMAIL_TRACKER, subject, wfmStrings.addLink(), false);
        }
        taggingShell.setSelectedRelations(RelationItem.TYPE_EMAIL_TRACKER, trackerID, relationItems);
        linkPanel.clear();
        if (taggingShell.getSelectedRelations() != null && taggingShell.getSelectedRelations().size() > 0) {
            linkPanel.add(AddTaggingView.drawTags(RelationItem.TYPE_EMAIL_TRACKER, trackerID, taggingShell.getSelectedRelations().toArray(new RelationItem[]{})));
        } else {
            linkPanel.add(AddTaggingView.drawTags(RelationItem.TYPE_EMAIL_TRACKER, trackerID, relationItems.toArray(new RelationItem[]{})));
        }
        addLink = AddTaggingView.getAddLink(taggingShell, wfmStrings.addLink(), RelationItem.TYPE_EMAIL_TRACKER, trackerID);
        linkPanel.add(addLink);
    }

    public boolean validate() {
        int errors = 0;
//        if(!Validation.validateDataListBoxRequired(from)){
//            errors++;
//        }
        if (!validateEmail(toText, true)) {
            errors++;
        }
        if (!validateEmail(ccText, isNotEmptyAlso(ccText))) {
            errors++;
        }
        if (!validateEmail(bccText, isNotEmptyAlso(bccText))) {
            errors++;
        }
        if (!Validation.validateEditorRequired(editor)) {
            errors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.fillRequiredField());
            return false;
        } else if (Utils.isNullOrEmpty(subject.getText()) && !subjectCheked) {
            subject.addStyleName(ERROR_FORM_STYLE);
            subject.addKeyDownHandler(event -> subject.removeStyleName(ERROR_FORM_STYLE));
            subjectCheked = true;
            Info.warn(wfmMessages.sendingMessageWithoutSubjectWarn());
            return false;
        }
        return true;
    }

    private boolean isNotEmptyAlso(MultiSelectContactLookUp lookUp) {
        String newText = lookUp.getTextBox().getValue();
        return !newText.isEmpty() && !newText.equalsIgnoreCase(ccBccDefaultText) ? true : false;
    }

    private boolean validateEmail(final MultiSelectContactLookUp textBox, boolean notEmptyAlso) {
        int errors = 0;
        boolean foundAtLeastOne = false;
        if (textBox.getSelectedItems().size() != 0) {
            for (SelectItem item : textBox.getSelectedItems()) {
                item.setName(item.getName().trim());
                foundAtLeastOne = true;
                if (item.getName().contains("<") && item.getName().contains(">")) {
                    String emailItem = item.getName().substring(item.getName().lastIndexOf("<") + 1, item.getName().lastIndexOf(">"));
                    if (!Utils.validateEmail(emailItem, false)) {
                        textBox.addStyleName(ERROR_FORM_STYLE);
                        textBox.getSuggestBox().addKeyDownHandler(event -> textBox.removeStyleName(ERROR_FORM_STYLE));
                        errors++;
                    }
                } else if (!Utils.validateEmail(item.getName(), false)) {
                    textBox.addStyleName(ERROR_FORM_STYLE);
                    textBox.getSuggestBox().addKeyDownHandler(event -> textBox.removeStyleName(ERROR_FORM_STYLE));
                    errors++;
                }
            }
        }
        if (notEmptyAlso && !foundAtLeastOne) {
            textBox.addStyleName(ERROR_FORM_STYLE);
            textBox.getSuggestBox().addKeyDownHandler(event -> textBox.removeStyleName(ERROR_FORM_STYLE));
            errors++;
        }
        return errors == 0;
    }

    public void sendMessage() {
        enableButtons(false);
        if(!validate()){
            enableButtons(true);
            return;
        }
        Email newEmail = new Email();
        newEmail.setFromEmail(from.getSelectedItem().getName());
        newEmail.setSubject(subject.getText());
        newEmail.setContent(editor.getData());
        newEmail.setToEmails(toText.getSelectedItemsAsString());
        newEmail.setCc(ccText.getSelectedItemsAsString());
        newEmail.setBcc(bccText.getSelectedItemsAsString());
        newEmail.setAttachments(attachments.getFiles());
        if (taggingShell != null && taggingShell.getSelectedRelations() != null && taggingShell.getSelectedRelations().size() > 0) {
            newEmail.setRelations(taggingShell.getSelectedRelations());
        }
        newEmail.setForward(forward);
        newEmail.setCaseID(caseID);
        if (caseItem != null) {
            newEmail.setTrackerID(caseItem.getTrackerID());
        } else if(emailItem != null){
            newEmail.setTrackerID(emailItem.getTrackerID());
        }
        LoadingPanel.loading(true, panel);
        service.sendMessage(newEmail, new AbstractAsyncCallback<Integer>() {
            public void success(Integer result) {
                LoadingPanel.loading(false, panel);
                enableButtons(true);
                if (result != null) {
                    Info.show(wfmStrings.yourMessageHasBeenSent(), Info.Type.INFO);
                    remove();
//                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_LIST_CHANGE, id, ComposeView.this);//Todo when draft
                    if (caseID != null) {
                        if (!forward) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASE_FORWARDED, result, null);
                        } else {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASE_REPLY_TO_REPORTER, result, ComposeView.this);
                        }
                    }
                } else {
                    Info.show(wfmStrings.couldNotConnectToTheServer(), Info.Type.WARNING);
                }
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
                enableButtons(true);
                Info.show(wfmStrings.unexpectedErrorOccuredWhileSending(), Info.Type.WARNING);
            }
        });
    }

    private void enableButtons(boolean enable) {
        saveButton.setEnabled(enable);
        closeButton.setEnabled(enable);
    }
}
