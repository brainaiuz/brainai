package com.edatasite.workforce.gwt.invoice.client.ui.view.emailcompose;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.ContactItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.FooteredCustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.addLinkSideNavBox.AddLinkSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectContactLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadFormPanel;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SendToFormFillingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Dilsh0d Madrahimov on 2/18/2019.
 */
public class AccountingEmailComposeView extends FooteredCustomForm implements Constants, Colapse {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private DataListBox fromListBox;
    private MultiSelectContactLookUp toLookUp;
    private MultiSelectContactLookUp bccLookUp;
    private MultiSelectContactLookUp ccLookUp;
    private MaterialLink ccLink;
    private MaterialLink bccLink;
    private TextBox subjectTextBox;
    private DataListBox contactListBox;

    private FlexTable table;

    private DataListBox customerListBox;
    private DataListBox templateListBox;
    private DataListBox pdfTemplateListBox;
    private KpiSwitcher copyToMe;
    private KpiEditor editor;
    private final HTML defaultHTML = new HTML();

    private String type;
    private Integer clientOrManagerID;
    private Integer invoiceID;
    private Integer contactID;
    private Integer pdfTemplateID;
    private Integer mailReceiverId;
    private boolean isReceipt;
    private boolean isRecurringInvoice;
    private Date fromDate;
    private Date toDate;
    private boolean includeSubAccountTransaction;
    private String ccBccDefaultText;

    private FormGroup ccField;
    private FormGroup bccField;
    private WfmButton2 sendButton, closeButton;

    private FooterUploadFormPanel footerUploadFormPanel;
    private FooterInformer linkInformer;
    AtomicBoolean firstClick = new AtomicBoolean(true);
    private AddLinkSideNavBox addLinkSideNavBox;
    private final ArrayList<RelationItem> relationItems = new ArrayList<>();
    private Integer formDataid;


    public AccountingEmailComposeView(String[] params) {
        super("accountingemailcomposeadd", wfmStrings.composeMail());
        initParams(params);
    }

    private void initParams(String[] params) {
        if (params != null && params.length > 0) {
            if (params.length == 7) {
                this.type = params[1];
                this.clientOrManagerID = (!Utils.isNullOrEmpty(params[2]) && !"null".equals(params[2])) ? Integer.valueOf(params[2]) : null;
                this.invoiceID = (!Utils.isNullOrEmpty(params[4]) && !"null".equals(params[3])) ? Integer.valueOf(params[3]) : null;
                this.contactID = (!Utils.isNullOrEmpty(params[4]) && !"null".equals(params[4])) ? Integer.valueOf(params[4]) : null;
                this.pdfTemplateID = (!Utils.isNullOrEmpty(params[5]) && !"null".equals(params[5])) ? Integer.valueOf(params[5]) : null;
                this.isReceipt = Boolean.parseBoolean(params[6]);
                if (RECURRING_INVOICE_CATEGORY.equals(type)) {
                    isRecurringInvoice = true;
                    this.type = SALES_INVOICE_CATEGORY;
                }
            } else if (params.length == 6) {
                this.type = params[1];
                this.clientOrManagerID = (!Utils.isNullOrEmpty(params[2]) && !"null".equals(params[2])) ? Integer.valueOf(params[2]) : null;
                this.fromDate = new Date(Long.parseLong(params[3]));
                this.toDate = new Date(Long.parseLong(params[4]));
                this.includeSubAccountTransaction = Boolean.parseBoolean(params[5]);
            } else if (params.length == 5 && params[1].equals(RECEIVE_PAYMENT_CATEGORY)) {
                this.type = params[1];
                this.clientOrManagerID = (!Utils.isNullOrEmpty(params[2]) && !"null".equals(params[2])) ? Integer.valueOf(params[2]) : null;
                this.invoiceID = (!Utils.isNullOrEmpty(params[3]) && !"null".equals(params[3])) ? Integer.valueOf(params[3]) : null;
                this.pdfTemplateID = (!Utils.isNullOrEmpty(params[4]) && !"null".equals(params[4])) ? Integer.valueOf(params[4]) : null;
            } else if (params.length == 5) {
                this.type = params[1];
                this.clientOrManagerID = (!Utils.isNullOrEmpty(params[2]) && !"null".equals(params[2])) ? Integer.valueOf(params[2]) : null;
                this.contactID = (!Utils.isNullOrEmpty(params[3]) && !"null".equals(params[3])) ? Integer.valueOf(params[3]) : null;
                this.invoiceID = (!Utils.isNullOrEmpty(params[4]) && !"null".equals(params[4])) ? Integer.valueOf(params[4]) : null;
            }
        }
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        fromListBox = new DataListBox();
        toLookUp = new MultiSelectContactLookUp(BY_BOTH);
        toLookUp.getList().addHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                if (toLookUp.getSelectedItems() != null && toLookUp.getSelectedItems().size() > 0) {
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
                if (ccLookUp.getSelectedItems() != null && ccLookUp.getSelectedItems().size() > 0) {
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


        ccBccDefaultText = ccLookUp.getTextBox().getValue();
        bccLookUp = new MultiSelectContactLookUp(BY_BOTH);

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

        templateListBox = new DataListBox();
        templateListBox.setNullLabel(wfmStrings.emailTemplate());

        pdfTemplateListBox = new DataListBox();
        pdfTemplateListBox.setNullLabel(accountingStrings.pdfTemplate());

        contactListBox = new DataListBox();
        contactListBox.setNullLabel(Property.get(Constants.Contacts, wfmStrings.contact()));

        customerListBox = new DataListBox();
        customerListBox.setNullLabel(wfmStrings.customer());

        table = new FlexTable();
        table.setWidget(0, 0, contactListBox);
        table.getColumnFormatter().setWidth(0, "50%");
        table.setWidget(0, 1, customerListBox);
        table.getColumnFormatter().setWidth(1, "50%");


        copyToMe = new KpiSwitcher();
        copyToMe.setValue(true);

        editor = new KpiEditor(true);

        FormGroup fromField = new FormGroup(wfmStrings.fromN(), fromListBox);
        FormGroup toField = new FormGroup(wfmStrings.toN(), toLookUp);
        FormGroup contactField = new FormGroup("", table);
        FormGroup templateField = new FormGroup("", templateListBox);
        FormGroup subjectField = new FormGroup(wfmStrings.subject(), subjectTextBox);
        FormGroup pdfTemplateField = new FormGroup("", pdfTemplateListBox);
        FormGroup sendCopyField = new FormGroup(accountingStrings.sendCopyToMe(), copyToMe);

        toField.getGroupContent().addStyleName("compose__to");

        ccField = new FormGroup(wfmStrings.cc(), ccLookUp);
        bccField = new FormGroup(wfmStrings.bcc(), bccLookUp);

        Div ccBccPanel = new Div();
        ccBccPanel.setStyleName("compose__cc");
        ccLink = new MaterialLink(wfmStrings.cc());
        ccLink.setMarginRight(5);
        bccLink = new MaterialLink(wfmStrings.bcc());
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

        contactListBox.addValueChangeHandler(changeEvent -> {
            if (contactListBox.getSelectedItem() != null && contactListBox.getSelectedItem().getId() > 0) {
                setToEmail(contactListBox.getSelectedItem().getName());
            }
            generateMessageText();
        });

        customerListBox.addValueChangeHandler(changeEvent -> {
            if (customerListBox.getSelectedItem() != null && customerListBox.getSelectedItem().getId() > 0) {
                setToEmail(customerListBox.getSelectedItem().getName());
            }
            generateMessageText();
        });

        templateListBox.addValueChangeHandler(changeEvent -> generateMessageText());

        addField(MESSAGE_CENTER.FROM, fromField);
        addField(MESSAGE_CENTER.TO, toField);
        addField(MESSAGE_CENTER.CC, ccField);
        addField(MESSAGE_CENTER.BCC, bccField);
        addField(MESSAGE_CENTER.CONTACT, contactField);
        addField(MESSAGE_CENTER.SUBJECT, subjectField);
        addField(MESSAGE_CENTER.TEMPLATE, templateField);
        addField(MESSAGE_CENTER.PDF_TEMPLATE, pdfTemplateField);
        addField(MESSAGE_CENTER.EDITOR, editor);
        addField(MESSAGE_CENTER.SEND_COPY, sendCopyField);

        RootPanel.get().addStyleName("fitted-content");
        show();
    }

    private void setToEmail(String email) {
        if (!Utils.isNullOrEmpty(email)) {
            toLookUp.setEmails(email);
        }
    }

    private void generateMessageText() {
        if (templateListBox.isSomethingSelected()) {
            footerUploadFormPanel.clearFiles();
            EntityToEmailTemplate item = new EntityToEmailTemplate();
            if (invoiceID != null) {
                item.setEntityId(invoiceID);
            } else if (clientOrManagerID != null) {
                item.setEntityId(clientOrManagerID);
            }
            item.setEntityType(type);
            if (contactListBox.getSelectedId() != null) {
                item.setMailReceiverId(contactListBox.getSelectedId());
            } else if (customerListBox.getSelectedId() != null) {
                item.setMailReceiverId(customerListBox.getSelectedId());
            } else if (mailReceiverId != null) {
                item.setMailReceiverId(mailReceiverId);
            } else {
                item.setMailReceiverId(contactID);
            }
            item.setEmailTemplateId(templateListBox.getSelectedItem(true).getId());
            EmailTemplateService.App.get().generateEmailTemplateForAccountingComposeView(item, null, new AbstractAsyncCallback<EmailTemplateItem>() {
                public void failure(Throwable caught) {
                }

                public void success(final EmailTemplateItem result) {
                    if (result != null && result.getMessageHTML() != null) {
                        subjectTextBox.setText(result.getSubject());
                        defaultHTML.setHTML(result.getMessageHTML());
                        editor.setData(defaultHTML.getHTML() != null ? defaultHTML.getHTML() : "");
                        if (result.getFileResources() != null && result.getFileResources().size() > 0) {
                            footerUploadFormPanel.setFilesToPanel(result.getFileResources());
                        }
                    }
                }
            });
        }
    }

    @Override
    protected List<Widget> getFooterLeftSideWidgets() {
        ArrayList<Widget> result = new ArrayList<>();
        footerUploadFormPanel = new FooterUploadFormPanel(F_DEFAULT);
        FooterInformer attachment = new FooterInformer(SvgEnum.uploadCloud, wfmStrings.attachments(), null);
        footerUploadFormPanel.setActivator(attachment);
        result.add(attachment);

        linkInformer = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        linkInformer.addClickHandler(clickEvent -> {
            drawLinks(null, null);
        });
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

    private void drawLinks(Integer trackerID, String subject) {
        if (firstClick.get()) {
            if (addLinkSideNavBox == null) {
                addLinkSideNavBox = new AddLinkSideNavBox(trackerID, RelationItem.TYPE_EMAIL_TRACKER, subject, true);
            }
            addLinkSideNavBox.setSelectedRelations(RelationItem.TYPE_EMAIL_TRACKER, trackerID, relationItems);
            firstClick.set(false);
        } else {
            addLinkSideNavBox.show();
        }
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
    }

    @Override
    protected void getDataToFillFields() {
        MessageCenterService.App.get().getUserEmailAccounts(true, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                fromListBox.setItems(result);
                for (SelectItem it : result) {
                    if (it.isSelected()) {
                        fromListBox.setSelected(it);
                        break;
                    }
                }
                //fromListBox.setEnabled(result.length > 1);
            }
        });
        EmailTemplateService.App.get().getEmailTemplates(isReceipt ? RECEIPT_CATEGORY : PROJECT_BASE_INVOICE_CATEGORY.equals(type) ? SALES_INVOICE_CATEGORY : SALES_ORDER_CATEGORY.equals(type) ? SALES_QUOTE_MANAGER_CATEGORY : type, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final SelectItem[] result) {
                LoadingPanel.loading(false);
                if (result != null && result.length > 0) {
                    templateListBox.setItems(result);
                    SelectItem defaultTemplateItem = result[0];
                    for (SelectItem item : result) {
                        if (item.isSelected()) {
                            defaultTemplateItem = item;
                            break;
                        }
                    }
                    templateListBox.setSelected(defaultTemplateItem);
                    generateMessageText();
                } else {
                    templateListBox.setWithoutNullLabel(wfmStrings.emailTemplate());
                }
            }
        });
        if (SALES_INVOICE_CATEGORY.equals(type)) {
            formDataid = invoiceID;
        }
        SendToFormFillingData data = new SendToFormFillingData(clientOrManagerID, type);
        LoadingPanel.loading(true);
        InvoiceService.App.get().getSendToFormData(data, contactID, true, formDataid, new AbstractAsyncCallback<SendToFormFillingData>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SendToFormFillingData data) {
                LoadingPanel.loading(false);
                relationItems.addAll(data.getRelationItems());
                if (data.getPrimaryContact() != null) {
                    setToEmail(data.getPrimaryContact().getName());
                } else if (data.getContacts() != null && data.getContacts().length > 0) {
                    setToEmail(data.getContacts()[0].getName());
                }
                if (data.getContacts() != null && data.getContacts().length > 0) {
                    setContacts(data.getContacts());
                } else {
                    contactListBox.setWithoutNullLabel(Property.get(Constants.Contacts, wfmStrings.contact()));
                }
                if (clientOrManagerID != null) {
                    getCustomerEmail();
                } else {
                    customerListBox.setWithoutNullLabel(wfmStrings.customer());
                }
                if (data.getPrimaryContact() != null) {
                    mailReceiverId = data.getPrimaryContact().getId();
                    contactID = data.getPrimaryContact().getId();
                } else if (contactID != null) {
                    mailReceiverId = contactID;
                } else if (data.getContacts() != null && data.getContacts().length > 0) {
                    mailReceiverId = data.getContacts()[0].getId();
                    contactID = data.getContacts()[0].getId();
                }
                if (mailReceiverId != null) {
                    contactListBox.setSelected(mailReceiverId);
                }
                if (data.getTemplateData() != null && data.getTemplateData().getItems() != null && data.getTemplateData().getItems().length > 0) {
                    pdfTemplateListBox.setItems(data.getTemplateData().getItems());
                    SelectItem defaultPdfItem = data.getTemplateData().getItems()[0];
                    for (SelectItem item : data.getTemplateData().getItems()) {
                        if (pdfTemplateID != null && pdfTemplateID.equals(item.getId())) {
                            defaultPdfItem = item;
                            break;
                        } else if (data.getTemplateData().getDefaultTemplateID() != null && data.getTemplateData().getDefaultTemplateID().equals(item.getId())) {
                            defaultPdfItem = item;
                            break;
                        }
                    }
                    pdfTemplateListBox.setSelected(defaultPdfItem);
                } else {
                    pdfTemplateListBox.setWithoutNullLabel(accountingStrings.pdfTemplate());
                }
            }
        });
    }

    public void setContacts(ContactItem[] contacts) {
        ArrayList<SelectItem> result = new ArrayList<>();

        for (ContactItem item : contacts) {
            if (item.getId() != null && item.getName() != null) {
                result.add(new SelectItem(item.getId(), item.getName()));
            }
        }
        contactListBox.setItems(result.toArray(new SelectItem[result.size()]));
    }

    private void getCustomerEmail() {
        ArrayList<SelectItem> result = new ArrayList<>();
        InvoiceService.App.get().getCustomerEmailToSend(clientOrManagerID, new AsyncCallback<CrmAccountItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(CrmAccountItem crmAccountItem) {
                result.add(new SelectItem(crmAccountItem.getObjectId(), crmAccountItem.getEmail()));
                customerListBox.setItems(result.toArray(new SelectItem[result.size()]));

            }
        });
    }

    public void sendMessage() {
        sendButton.setEnabled(false);

        if (!validate()) {
            sendButton.setEnabled(true);
            return;
        }

        MessageItem messageItem = new MessageItem();
        messageItem.setSubject(subjectTextBox.getText());
        messageItem.setClient(!type.equals(PURCHASE_ORDER_CATEGORY));
        messageItem.setInvoiceID(invoiceID);
        messageItem.setSendCopyToMe(copyToMe.getValue());
        messageItem.setMailContent(editor.getData());

        if (contactListBox.getSelectedId() != null) {
            messageItem.setContactId(contactListBox.getSelectedId());
        } else if (customerListBox.getSelectedId() != null) {
            messageItem.setContactId(customerListBox.getSelectedId());
        } else if (toLookUp.getSelectedItems() != null && toLookUp.getSelectedItems().size() > 0) {
            messageItem.setContactId(toLookUp.getSelectedItems().get(0).getId());
        }
        messageItem.setReceipt(isReceipt);
        messageItem.setEmailTemplateID(templateListBox.getSelectedId());
        messageItem.setPdfTemplateID(pdfTemplateListBox.getSelectedId());
        messageItem.setType(type);
        messageItem.setToEmails(toLookUp.getSelectedItemsAsString());
        messageItem.setCc(ccLookUp.getSelectedItemsAsString());
        messageItem.setBcc(bccLookUp.getSelectedItemsAsString());

        if (addLinkSideNavBox != null && addLinkSideNavBox.getSelectedRelations() != null && addLinkSideNavBox.getSelectedRelations().size() > 0) {
            messageItem.setRelations(addLinkSideNavBox.getSelectedRelations());
        }

        messageItem.setFromEmail(fromListBox.getSelectedItem() != null ? fromListBox.getSelectedItem().getName() : null);
        messageItem.setFileResources(footerUploadFormPanel.getFileResources());
        LoadingPanel.loading(true, panel);
        if (fromDate != null && toDate != null) {
            messageItem.setAccountId(clientOrManagerID);
            messageItem.setIncludeSubAccountTransaction(includeSubAccountTransaction);
            ClientService.App.get().sendCustomerBalanceEmail(messageItem, Utils.getStartDateNC(fromDate), Utils.getEndDateNC(toDate), new AsyncCallback() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false, panel);
                    sendButton.setEnabled(true);
                    Info.show("Can not send email. Please check your email account settings.", Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Object o) {
                    LoadingPanel.loading(false, panel);
                    sendButton.setEnabled(true);
                    Info.show(accountingMessages.dynamicSentSuccessfully(getTypeText()), Info.Type.INFO);
                    closeTab();
                    if (isRecurringInvoice) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, 0, AccountingEmailComposeView.this);
                    }
                }
            });//getVoidAsyncCallback
        } else if (type.equals(SALES_INVOICE_CATEGORY) || type.equals(RECEIPT_CATEGORY)
                || type.equals(CREDIT_NOTE_CATEGORY) || type.equals(PROJECT_BASE_INVOICE_CATEGORY)) {
            if (isRecurringInvoice) {
                InvoiceService.App.get().saveSendToClientDetails(messageItem, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false, panel);
                        sendButton.setEnabled(true);
                        Info.show("Can not send email. Please check your email account settings.", Info.Type.WARNING);
                    }

                    @Override
                    public void onSuccess(Void aVoid) {
                        LoadingPanel.loading(false, panel);
                        sendButton.setEnabled(true);
                        Info.show(accountingMessages.dynamicSentSuccessfully(getTypeText()), Info.Type.INFO);
                        closeTab();
                        if (isRecurringInvoice) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, 0, AccountingEmailComposeView.this);
                        }
                    }
                });//getVoidAsyncCallback
            } else {
                InvoiceService.App.get().sendToClient(messageItem, new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false, panel);
                        sendButton.setEnabled(true);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        LoadingPanel.loading(false, panel);
                        sendButton.setEnabled(true);
                        if (result != null) {
                            Info.show(accountingMessages.dynamicSentSuccessfully(getTypeText()), Info.Type.INFO);
                            closeTab();
                            if (result > 0) {
                                if (SALES_INVOICE_CATEGORY.equals(type) || RECEIPT_CATEGORY.equals(type) || CREDIT_NOTE_CATEGORY.equals(type) || isRecurringInvoice) {
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, AccountingEmailComposeView.this);
                                } else if (SALES_QUOTE_CATEGORY.equals(type) || SALES_QUOTE_MANAGER_CATEGORY.equals(type)) {
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, AccountingEmailComposeView.this);
                                } else if (PURCHASE_ORDER_CATEGORY.equals(type)) {
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_ADDED, result, AccountingEmailComposeView.this);
                                }
                            } else if (result == 0) {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, AccountingEmailComposeView.this);
                            }
                        } else {
                            Info.show("Can not send email. Please check your email account settings.", Info.Type.WARNING);
                            closeTab();
                        }
                    }
                });//getAsyncCallback()
            }
        } else if (REQUEST_FOR_QUOTE_CATEGORY.equals(type)) {
            MessageCenterService.App.get().sendMessage(messageItem.getAsEmailObject(), new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false, panel);
                    sendButton.setEnabled(true);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Integer result) {
                    LoadingPanel.loading(false, panel);
                    sendButton.setEnabled(true);
                    if (result == null) {
                        Info.show("Can not send email. Please check your email account settings.", Info.Type.WARNING);
                        closeTab();
                    }
                }
            });
        } else if (RECEIVE_PAYMENT_CATEGORY.equals(type)) {
            sendButton.setEnabled(false);
            InvoiceService.App.get().sendEmail(messageItem, new AsyncCallback<Integer>() {
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    sendButton.setEnabled(true);
                }

                public void onSuccess(Integer result) {
                    LoadingPanel.loading(false, panel);
                    sendButton.setEnabled(true);
                    if (result != null) {
                        Info.show(accountingMessages.dynamicSentSuccessfully(getTypeText()), Info.Type.INFO);
                        LoadingPanel.loading(false);
                        sendButton.setEnabled(true);
                        SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|summary/" + invoiceID + "/" + RECEIVABLE);
                        closeTab();
                    } else {
                        Info.show("Can not send email. Please check your email account settings.", Info.Type.WARNING);
                        closeTab();
                    }
                }
            });

        } else {
            QuoteService.App.get().sendToClientOrSupplier(messageItem, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false, panel);
                    sendButton.setEnabled(true);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Integer result) {
                    LoadingPanel.loading(false, panel);
                    sendButton.setEnabled(true);
                    if (result != null) {
                        Info.show(accountingMessages.dynamicSentSuccessfully(getTypeText()), Info.Type.INFO);
                        closeTab();
                        if (result > 0) {
                            if (SALES_INVOICE_CATEGORY.equals(type) || RECEIPT_CATEGORY.equals(type) || CREDIT_NOTE_CATEGORY.equals(type) || isRecurringInvoice) {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, AccountingEmailComposeView.this);
                            } else if (SALES_QUOTE_CATEGORY.equals(type) || SALES_QUOTE_MANAGER_CATEGORY.equals(type)) {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, AccountingEmailComposeView.this);
                            } else if (PURCHASE_ORDER_CATEGORY.equals(type)) {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_ADDED, result, AccountingEmailComposeView.this);
                            }
                        } else if (result == 0) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, AccountingEmailComposeView.this);
                        }
                    } else {
                        Info.show("Can not send email. Please check your email account settings.", Info.Type.WARNING);
                        closeTab();
                    }
                }
            });//getAsyncCallback()
        }
    }

    /*private AbstractAsyncCallback getVoidAsyncCallback() {
        return new AbstractAsyncCallback() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false, panel);
                sendButton.setEnabled(true);
                Info.show("Can not send email. Please check your email account settings.", Info.Type.WARNING);
            }

            public void success(Void result) {
                LoadingPanel.loading(false, panel);
                sendButton.setEnabled(true);
                Info.show(accountingMessages.dynamicSentSuccessfully(getTypeText()), Info.Type.INFO);
                closeTab();
                if (isRecurringInvoice) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, 0, AccountingEmailComposeView.this);
                }
            }
        };
    }

    private AbstractAsyncCallback getAsyncCallback() {
        return new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false, panel);
                sendButton.setEnabled(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Integer result) {
                LoadingPanel.loading(false, panel);
                sendButton.setEnabled(true);
                if (result != null) {
                    Info.show(accountingMessages.dynamicSentSuccessfully(getTypeText()), Info.Type.INFO);
                    closeTab();
                    if (result > 0) {
                        if (SALES_INVOICE_CATEGORY.equals(type) || RECEIPT_CATEGORY.equals(type) || CREDIT_NOTE_CATEGORY.equals(type) || isRecurringInvoice) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, AccountingEmailComposeView.this);
                        } else if (SALES_QUOTE_CATEGORY.equals(type) || SALES_QUOTE_MANAGER_CATEGORY.equals(type)) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, AccountingEmailComposeView.this);
                        } else if (PURCHASE_ORDER_CATEGORY.equals(type)) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_ADDED, result, AccountingEmailComposeView.this);
                        }
                    } else if (result == 0) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, AccountingEmailComposeView.this);
                    }
                } else {
                    Info.show("Can not send email. Please check your email account settings.", Info.Type.WARNING);
                    closeTab();
                }
            }
        };
    }*/

    private String getTypeText() {
        if (type != null) {
            switch (type) {
                case SALES_INVOICE_CATEGORY:
                case PROJECT_BASE_INVOICE_CATEGORY:
                    return wfmStrings.salesInvoice();
                case SALES_ORDER_CATEGORY:
                    return wfmStrings.salesOrders();
                case RECURRING_INVOICE_CATEGORY:
                    return accountingStrings.recurringInvoice();
                case SALES_QUOTE_CATEGORY:
                case SALES_QUOTE_MANAGER_CATEGORY:
                    return wfmStrings.salesQuote();
                case PURCHASE_ORDER_CATEGORY:
                case PURCHASE_ORDER_MANAGER_CATEGORY:
                    return wfmStrings.purchaseorder();
                case RECEIPT_CATEGORY:
                    return accountingStrings.receipt();
                case CREDIT_NOTE_CATEGORY:
                    return accountingStrings.creditNote();
                case REQUEST_FOR_QUOTE_CATEGORY:
                    return wfmStrings.requestForQuote();
                case RECEIVE_PAYMENT_CATEGORY:
                    return accountingStrings.receivePayment();
            }
        }
        return "";
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ACCOUNTING_EMAIL_COMPOSE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    private boolean validate() {
        int errors = 0;
        if (!validateEmail(toLookUp, true)) {
            errors++;
        }
        if (!validateEmail(ccLookUp, isNotEmptyAlso(ccLookUp))) {
            errors++;
        }
        if (!validateEmail(bccLookUp, isNotEmptyAlso(bccLookUp))) {
            errors++;
        }
        if (Utils.isNullOrEmpty(editor.getData())) {
            editor.addStyleName(ERROR_FORM_STYLE);
            editor.addKeyDownHandler(event -> editor.removeStyleName(ERROR_FORM_STYLE));
            errors++;
        }
        if (Utils.isNullOrEmpty(subjectTextBox.getText())) {
            subjectTextBox.addStyleName(ERROR_FORM_STYLE);
            subjectTextBox.addKeyDownHandler(event -> subjectTextBox.removeStyleName(ERROR_FORM_STYLE));
            errors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.pleaseEnterValidEmail());
            return false;
        }

        return true;
    }

    private boolean validateEmail(final MultiSelectContactLookUp textBox, boolean notEmptyAlso) {
        int errors = 0;
        boolean foundAtLeastOne = false;
        if (textBox.getSelectedItems().size() != 0) {
            for (SelectItem item : textBox.getSelectedItems()) {
                item.setName(item.getName().trim());
                foundAtLeastOne = true;
                if (item.getName().contains("<") && item.getName().contains(">")) {
                    String email = item.getName().substring(item.getName().lastIndexOf("<") + 1, item.getName().lastIndexOf(">"));
                    if (!Utils.validateEmail(email, false)) {
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
