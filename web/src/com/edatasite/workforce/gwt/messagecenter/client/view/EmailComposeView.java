package com.edatasite.workforce.gwt.messagecenter.client.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.FooteredCustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.addLinkSideNavBox.AddLinkSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectContactLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadFormPanel;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class EmailComposeView extends FooteredCustomForm implements Constants, Colapse {

    private DataListBox fromListBox;
    private MultiSelectContactLookUp toLookUp;
    private MultiSelectContactLookUp bccLookUp;
    private MultiSelectContactLookUp ccLookUp;
    private MaterialLink ccLink;
    private MaterialLink bccLink;
    private TextBox subjectTextBox;
    private DataListBox templateListBox;
    private KpiEditor editor;
    private FooterUploadFormPanel footerUploadFormPanel;
    private AddLinkSideNavBox addLinkSideNavBox;
    private FooterInformer linkInformer;
    private boolean fromLead;
    private boolean fromContact;
    private boolean fromEmployee;
    private boolean fromRfq;
    private boolean fromCandidate;
    private boolean fromEvent;
    private boolean fromOpportunity;
    private Email emailItem;
    private CaseItem caseItem;
    private Integer caseID;
    private boolean reply;
    private boolean replyAll;
    private boolean forward;
    private String signature;
    private boolean showSignatureOnTop;
    private HTML defaultHTML = new HTML();
    private String email;
    private String ccBccDefaultText;
    private Integer rfqId;
    private Integer employeeId;
    private Integer candidateId;
    private Integer opportunityId;

    private FormGroup ccField;
    private FormGroup bccField;

    private WfmButton2 sendButton;

    private ArrayList<RelationItem> relationItems = new ArrayList<>();
    AtomicBoolean firstClick = new AtomicBoolean(true);
    private Integer eventID;


    public EmailComposeView(String[] params) {
        super("emailcomposeadd", wfmStrings.composeMail());
        initParams(params);
    }

    private void initParams(String[] params) {
        if (params != null && params.length > 0) {
            if (RelationItem.emailItem != null) {
                this.emailItem = RelationItem.emailItem;
            }
            if (params.length == 2) {
                this.email = params[1];
            } else if (params.length == 3 && params[1].equals(RelationItem.TYPE_EVENT)) {
                this.eventID = Integer.valueOf(params[2]);
                this.fromEvent = true;
            } else if (params.length == 4) {
                this.caseID = (!Utils.isNullOrEmpty(params[1]) && !"null".equals(params[1])) ? Integer.valueOf(params[1]) : null;
                this.reply = Boolean.valueOf(params[2]);
                this.forward = Boolean.valueOf(params[3]);
            } else if (params.length == 5) {
                this.email = params[1];
                RelationItem relationItem = RelationItem.newEventRelation(params[2], Integer.valueOf(params[3]), params[4]);
                if (RelationItem.TYPE_LEAD.equals(relationItem.getToType())) {
                    fromLead = true;
                } else if (RelationItem.TYPE_CONTACT.equals(relationItem.getToType())) {
                    fromContact = true;
                } else if (RelationItem.TYPE_REQUEST_FOR_QUOTE.equals(relationItem.getToType())) {
                    fromRfq = true;
                    rfqId = Integer.valueOf(params[3]);
                } else if (RelationItem.TYPE_EMPLOYEE.equals(relationItem.getToType())) {
                    fromEmployee = true;
                    employeeId = Integer.valueOf(params[3]);
                } else if (RelationItem.TYPE_CANDIDATE.equals(relationItem.getToType())) {
                    fromCandidate = true;
                } else if (RelationItem.TYPE_OPPORTUNITY.equals(relationItem.getToType())) {
                    fromOpportunity = true;
                    opportunityId = Integer.valueOf(params[3]);
                }
                relationItem.setFromType(RelationItem.TYPE_EMAIL_TRACKER);

                this.relationItems.add(relationItem);
            } else if (params.length == 8) {
                this.email = params[1];
                RelationItem relationItem = RelationItem.newEventRelation(params[2], Integer.valueOf(params[3]), params[4]);
                RelationItem relationItem2 = RelationItem.newEventRelation(params[5], Integer.valueOf(params[6]), params[7]);
                if (RelationItem.TYPE_LEAD.equals(relationItem.getToType())) {
                    fromLead = true;
                } else if (RelationItem.TYPE_CONTACT.equals(relationItem.getToType())) {
                    fromContact = true;
                } else if (RelationItem.TYPE_EMPLOYEE.equals(relationItem.getToType())) {
                    fromEmployee = true;
                }
                relationItem.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                relationItem2.setFromType(RelationItem.TYPE_EMAIL_TRACKER);


                this.relationItems.add(relationItem);
                this.relationItems.add(relationItem2);
            } else if (params.length == 9) {
                this.caseID = Integer.valueOf(params[1]);
                this.reply = Boolean.valueOf(params[2]);
                this.forward = Boolean.valueOf(params[3]);
                this.replyAll = Boolean.valueOf(params[7]);
                RelationItem relationItem = RelationItem.newEventRelation(params[4], Integer.valueOf(params[1]), params[6].replace("&", "/"));
                relationItem.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                relationItem.setFromID(Integer.valueOf(params[5]));
                this.relationItems.add(relationItem);
            }
        }
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SELECT_TAB, EmailComposeView.this, (sender, args) -> editor.setData(editor.getData()));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SELECTION_CHANGE_TAB, EmailComposeView.this, (sender, args) -> editor.setData(editor.getData()));
        return null;
    }

    private void initialize() {
        fromListBox = new DataListBox();
        fromListBox.ensureDebugId("email_from");
        toLookUp = new MultiSelectContactLookUp(BY_BOTH);
        toLookUp.ensureDebugId("email_to");
        toLookUp.getList().addHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                if (toLookUp.getSelectedItems() != null && !toLookUp.getSelectedItems().isEmpty()) {
                    for (int i = 0; i < toLookUp.getSelectedItems().size(); i++) {
                        if (toLookUp.getSelectedItems().get(i).getId() != null) {
                            RelationItem relationItemContact = RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, toLookUp.getSelectedItems().get(i).getId(), toLookUp.getSelectedItems().get(i).getName());
                            relationItemContact.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                            MessageCenterService.App.get().getContactAccounts(toLookUp.getSelectedItems().get(i).getId(), new AsyncCallback<ArrayList<SelectItem>>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                }

                                @Override
                                public void onSuccess(ArrayList<SelectItem> crmAccountItems) {
                                    relationItems.add(relationItemContact);
                                    if (crmAccountItems != null && !crmAccountItems.isEmpty()) {
                                        for (SelectItem item : crmAccountItems) {
                                            RelationItem relationItem = RelationItem.newEventRelation(item.getReferenceCode(), item.getId(), item.getName());
                                            relationItem.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                                            addLinkSideNavBox.addItem(relationItem);
                                        }
                                    }
//                                    drawLinks(false, emailItem != null ? emailItem.getTrackerID() : caseItem != null ? caseItem.getTrackerID() : null, null);
                                }
                            });
                        }
                    }
                }
            }
        }, ChangeEvent.getType());
        ccLookUp = new MultiSelectContactLookUp(BY_BOTH);
        ccLookUp.getList().addHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                if (ccLookUp.getSelectedItems() != null && !ccLookUp.getSelectedItems().isEmpty()) {
                    for (int i = 0; i < ccLookUp.getSelectedItems().size(); i++) {
                        if (ccLookUp.getSelectedItems().get(i).getId() != null) {
                            RelationItem relationItemContact = RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, ccLookUp.getSelectedItems().get(i).getId(), ccLookUp.getSelectedItems().get(i).getName());
                            relationItemContact.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                            MessageCenterService.App.get().getContactAccounts(ccLookUp.getSelectedItems().get(i).getId(), new AsyncCallback<ArrayList<SelectItem>>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                }

                                @Override
                                public void onSuccess(ArrayList<SelectItem> crmAccountItems) {
                                    relationItems.add(relationItemContact);
                                    if (crmAccountItems != null && !crmAccountItems.isEmpty()) {
                                        for (SelectItem item : crmAccountItems) {
                                            RelationItem relationItem = RelationItem.newEventRelation(item.getReferenceCode(), item.getId(), item.getName());
                                            relationItem.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                                            addLinkSideNavBox.addItem(relationItem);
                                        }
                                    }
//                                    drawLinks(false, emailItem != null ? emailItem.getTrackerID() : caseItem != null ? caseItem.getTrackerID() : null, null);
                                }
                            });
                        }
                    }
                }
            }
        }, ChangeEvent.getType());
        ccLookUp.ensureDebugId("email_cc");
        ccBccDefaultText = ccLookUp.getTextBox().getValue();
        bccLookUp = new MultiSelectContactLookUp(BY_BOTH);
        bccLookUp.ensureDebugId("email_bcc");
        bccLookUp.getList().addHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                if (bccLookUp.getSelectedItems() != null && bccLookUp.getSelectedItems().size() > 0) {
                    for (int i = 0; i < bccLookUp.getSelectedItems().size(); i++) {
                        if (bccLookUp.getSelectedItems().get(i).getId() != null) {
                            RelationItem relationItemContact = RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, bccLookUp.getSelectedItems().get(i).getId(), bccLookUp.getSelectedItems().get(i).getName());
                            relationItemContact.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                            MessageCenterService.App.get().getContactAccounts(bccLookUp.getSelectedItems().get(i).getId(), new AsyncCallback<ArrayList<SelectItem>>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                }

                                @Override
                                public void onSuccess(ArrayList<SelectItem> crmAccountItems) {
                                    relationItems.add(relationItemContact);
                                    if (crmAccountItems != null && !crmAccountItems.isEmpty()) {
                                        for (SelectItem item : crmAccountItems) {
                                            RelationItem relationItem = RelationItem.newEventRelation(item.getReferenceCode(), item.getId(), item.getName());
                                            relationItem.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                                            addLinkSideNavBox.addItem(relationItem);
                                        }
                                    }
//                                    drawLinks(false, emailItem != null ? emailItem.getTrackerID() : caseItem != null ? caseItem.getTrackerID() : null, null);
                                }
                            });
                        }
                    }
                }
            }
        }, ChangeEvent.getType());

        subjectTextBox = new TextBox();
        subjectTextBox.setPlaceHolder(wfmStrings.emailSubjectHere());
        subjectTextBox.ensureDebugId("email_subject");

        templateListBox = new DataListBox();
        templateListBox.setNullLabel(wfmStrings.template());
        templateListBox.ensureDebugId("email_template");

        editor = new KpiEditor(true);
        editor.ensureDebugId("email_content");

        FormGroup fromField = new FormGroup(wfmStrings.fromN(), fromListBox);
        fromField.ensureDebugId("email_from_field");
        FormGroup toField = new FormGroup(wfmStrings.toN(), toLookUp);
        toField.ensureDebugId("email_to_field");
        FormGroup templateField = new FormGroup("", templateListBox);
        templateField.ensureDebugId("email_template_field");
        FormGroup subjectField = new FormGroup(wfmStrings.subject(), subjectTextBox);
        subjectField.ensureDebugId("email_subject_field");

        toField.getGroupContent().addStyleName("compose__to");

        ccField = new FormGroup(wfmStrings.cc(), ccLookUp);
        ccField.ensureDebugId("email_cc_field");
        bccField = new FormGroup(wfmStrings.bcc(), bccLookUp);
        bccField.ensureDebugId("email_bcc_field");

        Div ccBccPanel = new Div();
        ccBccPanel.setStyleName("compose__cc");
        ccLink = new MaterialLink(wfmStrings.cc());
        ccLink.ensureDebugId("email_cc_link");
        ccLink.setTooltip(wfmStrings.carbonCopy());
        ccLink.setMarginRight(5);
        bccLink = new MaterialLink(wfmStrings.bcc());
        bccLink.ensureDebugId("email_bcc_link");
        bccLink.setTooltip(wfmStrings.blindCarbonCopy());
        ccBccPanel.add(ccLink);
        ccBccPanel.add(bccLink);

        toField.addToContent(ccBccPanel);

        ccField.setVisible(false);
        bccField.setVisible(false);

        ccLink.addClickHandler(clickEvent -> {
            ccField.setVisible(true);
            ccLink.setVisible(false);
        });

        bccLink.addClickHandler(clickEvent -> {
            bccField.setVisible(true);
            bccLink.setVisible(false);
        });

        templateListBox.addValueChangeHandler(changeEvent -> onTemplateChange());

        addField(MESSAGE_CENTER.FROM, fromField);
        addField(MESSAGE_CENTER.TO, toField);
        addField(MESSAGE_CENTER.CC, ccField);
        addField(MESSAGE_CENTER.BCC, bccField);
        addField(MESSAGE_CENTER.SUBJECT, subjectField);
        addField(MESSAGE_CENTER.TEMPLATE, templateField);
        addField(MESSAGE_CENTER.EDITOR, editor);

        RootPanel.get().addStyleName("fitted-content");
        show();
    }

    private void fillEmailTemplateContent(EmailTemplateItem result) {
        if (!reply) {
            subjectTextBox.setText(result.getSubject());
        }
        editor.setData(result.getMessageHTML() + (defaultHTML.getHTML() != null ? defaultHTML.getHTML() : ""));
        if (result.getFileResources() != null && !result.getFileResources().isEmpty()) {
            footerUploadFormPanel.setFilesToPanel(result.getFileResources());
        }
    }

    private String getModuleCode() {
        return fromLead ? "ET_LEAD_MODULE" :
                fromContact ? "ET_CONTACT_MODULE" :
                        fromRfq ? "ET_RFQ_MODULE" :
                                fromEmployee ? "ET_EMPLOYEE_MODULE" :
                                        fromCandidate ? "ET_CANDIDATE_MODULE" :
                                                fromEvent ? "ET_EVENT_MODULE" :
                                                        fromOpportunity ? "ET_OPPORTUNITY_MODULE" : "";
    }

    private void drawLinks(Integer trackerID, String subject) {
        if (firstClick.get()) {
            if (addLinkSideNavBox == null) {
                addLinkSideNavBox = new AddLinkSideNavBox(trackerID, RelationItem.TYPE_EMAIL_TRACKER, subject, true);
            }
            addLinkSideNavBox.setSelectedRelations(RelationItem.TYPE_EMAIL_TRACKER, trackerID, relationItems);
            firstClick.set(false);
        } else {
            addLinkSideNavBox.open();
        }
    }


    private void getOpeningData() {
        panel.addStyleName("compose");
        MessageCenterService.App.get().getUserEmailAccounts(true, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                fromListBox.setWithoutNullLabel(true);
                fromListBox.setItems(result);
                for (SelectItem it : result) {
                    if (it.isSelected()) {
                        fromListBox.setSelected(it);
                        break;
                    }
                }
                //Dont display from fields if there is only one option
                //fromField.setVisible(result.length > 1);
            }
        });
        if (eventID != null) {
            AllInOneService.App.get().getEventDetails(eventID, new AbstractAsyncCallback<EmailTemplateItem>() {
                public void success(EmailTemplateItem result) {
                    fromListBox.setSelected(new SelectItem(result.getObjectId(), result.getFromEmail()));
                    toLookUp.setEmails(result.getToEmail());
                    subjectTextBox.setText(result.getSubject());
                    editor.setData(result.getMessageHTML());
                    templateListBox.setEnabled(false);

                    if (result.getCc() != null) {
                        ccField.setVisible(true);
                        ccLink.setVisible(false);
                        ccLookUp.setEmails(result.getCc());
                    }
                    if (result.getBcc() != null) {
                        bccField.setVisible(true);
                        bccLink.setVisible(false);
                        bccLookUp.setEmails(result.getBcc());
                    }

                }
            });
        }
        if (caseID == null) {
            EmailTemplateService.App.get().getMessageCenterEmailTemplates(new ArrayList<>(Collections.singletonList(getModuleCode())), new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] result) {
                    if (result != null && result.length > 0) {
                        templateListBox.setItems(result);
                        if (result.length == 1) {
                            templateListBox.setSelected(result[0]);
                            onTemplateChange();
                        } else {
                            for (SelectItem item : result) {
                                if (item.isSelected()) {
                                    templateListBox.setSelected(item);
                                    onTemplateChange();
                                }
                            }
                        }
                    } else {
                        templateListBox.setWithoutNullLabel(wfmStrings.template());
                    }
                    //Dont display template if there are no options
                    //templateField.setVisible(result.length > 0);
                }
            });
        } else {
            EmailTemplateService.App.get().getEmailTemplates(CASE_REPLIED_CATEGORY, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void onSuccess(SelectItem[] result) {
                    if (result != null && result.length > 0) {
                        templateListBox.setItems(result);
                        if (result.length == 1) {
                            templateListBox.setSelected(result[0]);
                        } else {
                            for (SelectItem item : result) {
                                if (item.isSelected()) {
                                    templateListBox.setSelected(item);
                                }
                            }
                        }
                    } else {
                        templateListBox.setWithoutNullLabel(wfmStrings.template());
                    }
                }
            });
        }
        if (email != null) {
            toLookUp.setEmails(email);
            getSignature();
        } else if (emailItem != null) {
            setReplyOrForwardValues();
            getSignature();
        } else if (caseID != null) {
            MessageCenterService.App.get().getCase(caseID, new AbstractAsyncCallback<CaseItem>() {
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
        footerUploadFormPanel.clearFiles();
        Date createdDate = emailItem != null ? emailItem.getDate() : caseItem.getCreatedDate();
        String fromEmail = emailItem != null ? emailItem.getFromEmail() : caseItem.getEmail();
        String fromName = emailItem != null ? emailItem.getFromName() : caseItem.getReportedBy();
        String toName = emailItem != null ? emailItem.getToName() : caseItem.getCaseAssigneeName();
        String subjectText = emailItem != null ? emailItem.getSubject() : caseItem.getSubject();
        String content = emailItem != null ? emailItem.getContent() : caseItem.getDescription();
        if (!forward) {
            toLookUp.setEmails(fromEmail);
        }
        if (!(reply || forward) || replyAll) {
            String cc = emailItem != null ? emailItem.getCc() : caseItem.getCcEmails();
            if (!Utils.isNullOrEmpty(cc)) {
                ccField.setVisible(true);
                ccLink.setVisible(false);
                ccLookUp.setEmails(cc);
            }
            if (emailItem != null && !Utils.isNullOrEmpty(emailItem.getBcc())) {
                bccField.setVisible(true);
                bccLink.setVisible(false);
                bccLookUp.setEmails(emailItem.getBcc());
            }
        }
        if (!Utils.isNullOrEmpty(subjectText)) {
            subjectTextBox.setText(forward ? "Fwd: " + subjectText : subjectText);
        }
        defaultHTML.setHTML(forward ? getForwardContent(createdDate, fromName, fromEmail, toName, subjectText, content) : getReplyContent(createdDate, fromName, fromEmail, content));
        if (forward && emailItem != null && emailItem.getAttachments() != null && !emailItem.getAttachments().isEmpty()) {
            footerUploadFormPanel.setFilesToPanel(emailItem.getAttachments());
        }
    }


    private String getReplyContent(Date createdDate, String fromName, String fromEmail, String content) {
        //If the content has style like <style>font-size:12px;font-family:Arial;</style>, remove the block of <style> </style>
        /*if (!Utils.isNullOrEmpty(content) && content.contains("<style>")) {
            String parts[] = content.split("</style>");
            String result = "";
            for (String part : parts) {
                result = result.concat(part.split("<style>")[0].trim()).concat(" ");
            }
            content = result;
        }*/
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
        return "</br>---------- " + wfmMessages.forwardedMessage() + " ----------<br/>" +
                wfmMessages.from() + ": " + (!Utils.isNullOrEmpty(fromName) ? fromName : Utils.getUserFullName()) +
                " &lt;" + (!Utils.isNullOrEmpty(fromEmail) ? fromEmail : Utils.getUserEmail()) + "&gt; " + "<br/>" +
                wfmStrings.date() + ": " + DateTimeFormat.getFormat("EEE, MMM dd, yyyy [HH:mm]").format(createdDate) + "<br/>" +
                wfmStrings.subject() + ": " + (!Utils.isNullOrEmpty(subject) ? subject : "") + "<br/>" +
                wfmMessages.to() + ": " + (!Utils.isNullOrEmpty(recipient) ? recipient : "") + "<br/><br/>" + (!Utils.isNullOrEmpty(content) ? content : "");
    }

    private void getSignature() {
        service.getSignature(new AbstractAsyncCallback<SignatureItem>() {
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
        if (!Utils.isNullOrEmpty(defaultHTML.getHTML())) {
            editor.setData("<br>" + defaultHTML.getHTML());
        }
    }

    private void onTemplateChange() {
        if (templateListBox.isSomethingSelected()) {
            footerUploadFormPanel.clearFiles();
            EntityToEmailTemplate emailTemplateInner = new EntityToEmailTemplate();
            if (caseID != null) {
                emailTemplateInner.setEntityId(caseID);
            } else if (toLookUp.getSelectedItems() != null && toLookUp.getSelectedItems().size() > 0 && toLookUp.getSelectedItems().get(0) != null) {
                emailTemplateInner.setMailReceiverEmail(toLookUp.getSelectedItems().get(0).getName());
            }
            emailTemplateInner.setEntityType(caseID != null ? CASE_REPLIED_CATEGORY : "emailItem");
            emailTemplateInner.setEmailTemplateId(templateListBox.getSelectedId());
            LoadingPanel.loading(true, panel);
            if (caseID == null) {
                EmailTemplateService.App.get().generateMessageCenterTemplateItem(emailTemplateInner, rfqId, employeeId, opportunityId, new AsyncCallback<EmailTemplateItem>() {
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
                service.generateReplyToReporterCaseItem(emailTemplateInner, null, new AbstractAsyncCallback<EmailTemplateItem>() {
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
            if (emailItem != null || caseItem != null) {
                setReplyOrForwardValues();
                drawSignature();
            } else {
                subjectTextBox.setText("");
                defaultHTML.setHTML("");
                drawSignature();
            }
        }
    }

    @Override
    protected List<Widget> getFooterLeftSideWidgets() {
        ArrayList<Widget> result = new ArrayList<>();

        footerUploadFormPanel = new FooterUploadFormPanel(F_DEFAULT);
        FooterInformer attachment = new FooterInformer(SvgEnum.uploadCloud, wfmStrings.attachments(), null);
        footerUploadFormPanel.setActivator(attachment);
        new KpiToolTip(attachment, wfmStrings.uploadFile());

        linkInformer = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        drawLinks(emailItem != null ? emailItem.getTrackerID() : caseItem != null ? caseItem.getTrackerID() : null, null);
        linkInformer.addClickHandler(clickEvent -> {
            addLinkSideNavBox.open();
        });
        result.add(attachment);
        result.add(linkInformer);
        return result;
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        ArrayList<Widget> result = new ArrayList<>();

        sendButton = new WfmButton2(wfmStrings.send(), Constants.BTN_PRIMARY);

        sendButton.addClickHandler(clickEvent -> sendMessage());

        Div sendWrapper = new Div();
        sendWrapper.add(sendButton);

        result.add(sendWrapper);

        return result;
    }

    @Override
    protected void addButtons() {

    }

    @Override
    protected void getDataToFillFields() {
        getOpeningData();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.EMAIL_COMPOSE_FORM;
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

    public boolean validate() {
        int errors = 0;
        if (!validateEmail(toLookUp, true)) {
            toLookUp.addStyleName(ERROR_FORM_STYLE);
            toLookUp.addValueChangeHandler(event -> ccLookUp.removeStyleName(ERROR_FORM_STYLE));
            Info.warn(wfmStrings.pleaseEnterValidEmail());
            errors++;
        }
        if (!validateEmail(ccLookUp, isNotEmptyAlso(ccLookUp))) {
            ccLookUp.addStyleName(ERROR_FORM_STYLE);
            ccLookUp.addValueChangeHandler(event -> ccLookUp.removeStyleName(ERROR_FORM_STYLE));
            Info.warn(wfmStrings.fillRequiredField());
            errors++;
        }
        if (!validateEmail(bccLookUp, isNotEmptyAlso(bccLookUp))) {
            bccLookUp.addStyleName(ERROR_FORM_STYLE);
            bccLookUp.addValueChangeHandler(event -> bccLookUp.removeStyleName(ERROR_FORM_STYLE));
            Info.warn(wfmStrings.fillRequiredField());
            errors++;
        }
        if (!Validation.validateEditorRequired(editor)) {
            editor.addStyleName(ERROR_FORM_STYLE);
            editor.addKeyDownHandler(event -> editor.removeStyleName(ERROR_FORM_STYLE));
            Info.warn(wfmStrings.fillRequiredField());
            errors++;
        }
        if (Utils.isNullOrEmpty(subjectTextBox.getText())) {
            subjectTextBox.addStyleName(ERROR_FORM_STYLE);
            subjectTextBox.addKeyDownHandler(event -> subjectTextBox.removeStyleName(ERROR_FORM_STYLE));
            Info.warn(wfmStrings.fillRequiredField());
            errors++;
        }
        return errors <= 0;
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

    private boolean isNotEmptyAlso(MultiSelectContactLookUp lookUp) {
        String newText = lookUp.getTextBox().getValue();
        return !newText.isEmpty() && !newText.equalsIgnoreCase(ccBccDefaultText);
    }

    public void sendMessage() {
        enableButtons(false);
        if (!validate()) {
            enableButtons(true);
            return;
        }
        Email newEmail = new Email();
        newEmail.setFromEmail(fromListBox.getSelectedItem().getName());
        newEmail.setSubject(subjectTextBox.getText());
        newEmail.setContent(editor.getData());
        newEmail.setToEmails(toLookUp.getSelectedItemsAsString());
        newEmail.setCc(ccLookUp.getSelectedItemsAsString());
        newEmail.setBcc(bccLookUp.getSelectedItemsAsString());
        newEmail.setAttachments(footerUploadFormPanel.getFileResources());
        if (addLinkSideNavBox != null && addLinkSideNavBox.getSelectedRelations() != null && addLinkSideNavBox.getSelectedRelations().size() > 0) {
            newEmail.setRelations(addLinkSideNavBox.getSelectedRelations());
        }
        newEmail.setForward(forward);
        newEmail.setCaseID(caseID);
        if (caseItem != null) {
            newEmail.setTrackerID(caseItem.getTrackerID());
        } else if (emailItem != null) {
            newEmail.setTrackerID(emailItem.getTrackerID());
        }
        LoadingPanel.loading(true, panel);
        MessageCenterService.App.get().sendMessage(newEmail, new AbstractAsyncCallback<Integer>() {
            public void success(Integer result) {
                LoadingPanel.loading(false, panel);
                enableButtons(true);
                if (result != null) {
                    Info.show(wfmStrings.yourMessageHasBeenSent(), Info.Type.INFO);
                    closeTab();
                    if (caseID != null) {
                        if (!forward) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASE_FORWARDED, result, null);
                        } else {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASE_REPLY_TO_REPORTER, result, EmailComposeView.this);
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
        sendButton.setEnabled(enable);
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
