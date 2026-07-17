package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.ContactItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.fileUpload.KpiFileUploadForm;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectContactLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SendToFormFillingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteServiceAsync;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: Azazello
 * Date: 1/31/2018
 * Time: 3:37 PM
 */
public class AccountingQuickCompose extends Composite implements Constants {
    interface AccountingQuickComposeUiBinder extends UiBinder<HTMLPanel, AccountingQuickCompose> {
    }

    private static final AccountingQuickComposeUiBinder ourUiBinder = GWT.create(AccountingQuickComposeUiBinder.class);
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final InvoiceServiceAsync invoiceService = InvoiceService.App.get();
    private final QuoteServiceAsync quoteService = QuoteService.App.get();

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
    Label contactLabel;
    @UiField
    DataListBox contact;
    @UiField
    Label templateLabel;
    @UiField
    DataListBox template;
    @UiField
    Label pdfTemplateLabel;
    @UiField
    DataListBox pdfTemplate;
    @UiField
    HTMLPanel content;
    @UiField
    Label copyToMeLabel;
    @UiField
    KpiSwitcher copyToMe;
    @UiField
    MaterialPanel uploadPanel;

    KpiFileUploadForm attachments;

    private ExtendedCommand command;
    private MultiSelectContactLookUp toText;
    private VerticalPanel bccCCPanel;
    private HorizontalPanel linkPanel;
    private MaterialLink bccLink;
    private MaterialLink ccLink;
    private MultiSelectContactLookUp bccText;
    private MultiSelectContactLookUp ccText;
    private String ccBccDefaultText;
    private KpiEditor editor;
    private boolean ccShown;
    private boolean bccShown;
    private boolean subjectCheked;
    private boolean showSignatureOnTop;
    private String signature;
    private HTML defaultHTML = new HTML();
    private boolean isRecurringInvoice;
    private String type;
    private final Integer clientOrManagerID;
    private Integer invoiceID;
    private Integer contactID;
    private Integer pdfTemplateID;
    private Integer mailReceiverId;
    private boolean isReceipt;
    private Date fromDate;
    private Date toDate;
    private boolean includeSubAccountTransaction;

    public AccountingQuickCompose(String type, Integer clientOrManagerId, Integer invoiceId, Integer contactId, Integer pdfTemplateId, boolean isReceipt) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.type = type;
        this.clientOrManagerID = clientOrManagerId;
        this.invoiceID = invoiceId;
        this.contactID = contactId;
        this.pdfTemplateID = pdfTemplateId;
        this.isReceipt = isReceipt;

        if (RECURRING_INVOICE_CATEGORY.equals(type)) {
            isRecurringInvoice = true;
            this.type = SALES_INVOICE_CATEGORY;
        }
        init();
    }

    public AccountingQuickCompose(String type, Integer clientOrManagerId, Date fromDate, Date toDate, boolean includeSubAccountTransaction) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.type = type;
        this.clientOrManagerID = clientOrManagerId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.includeSubAccountTransaction = includeSubAccountTransaction;
        init();
    }

    private void init() {
        fromLabel.setText(wfmStrings.from());
        toLabel.setText(wfmStrings.to());
        subjectLabel.setText(wfmStrings.subject());
        contactLabel.setText(Property.get(Constants.Contacts, wfmStrings.contact()));
        templateLabel.setText(wfmStrings.template());
        pdfTemplateLabel.setText(accountingStrings.pdfTemplate());
        copyToMeLabel.setText(accountingStrings.sendCopyToMe());

        toText = new MultiSelectContactLookUp(Constants.BY_BOTH, this.panel);
        toText.getList().setWidth("100%");
        to.add(toText);

        ccLink = new MaterialLink(wfmStrings.cc());
        ccLink.addClickHandler(clickEvent -> showBccCC(true));
        ccText = new MultiSelectContactLookUp(Constants.BY_BOTH, this.panel);
        ccBccDefaultText = ccText.getTextBox().getValue();
        ccText.getList().setWidth("100%");
        bccLink = new MaterialLink(wfmStrings.bcc());
        bccLink.addClickHandler(clickEvent -> showBccCC(false));
        bccText = new MultiSelectContactLookUp(Constants.BY_BOTH, this.panel);
        bccText.getList().setWidth("100%");
        linkPanel = new HorizontalPanel();
        linkPanel.setWidth("150px");
        linkPanel.setSpacing(3);
        linkPanel.add(ccLink);
        linkPanel.add(bccLink);
        bccCCPanel = new VerticalPanel();
        bccCCPanel.setSpacing(3);
        bccPanel.add(bccCCPanel);
        bccPanel.add(linkPanel);

        contact.addValueChangeHandler(changeEvent -> {
            if (contact.getSelectedItem() != null) {
                setToEmail(contact.getSelectedItem().getName());
            }
            generateMessageText();
        });
        template.addValueChangeHandler(changeEvent -> generateMessageText());
        editor = new KpiEditor(true);
        content.add(editor);
        copyToMe.setValue(true);

        attachments = new KpiFileUploadForm(F_SALE_INV);
        uploadPanel.add(attachments);

        sign();
    }

    private void setToEmail(String email) {
        if (!Utils.isNullOrEmpty(email)) {
            toText.setEmails(email);
        }
    }

    private void sign() {
        /*if (signature == null) {
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
                    sign();//bu yerda recursiya kerak emas, Signature != null bulgandagi holati ishlatilish kerak.
                }
            });
        } else {
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
            editor.setData("<br>" + defaultHTML.getHTML());
        }*/ /*Accounting signature hide qilib turinglar deb aytishdi.*/

        editor.setData(defaultHTML.getHTML() != null ? defaultHTML.getHTML() : "");
    }

    private void showBccCC(boolean cc) {
        HTMLPanel panel = new HTMLPanel("");
        panel.addStyleName("form-group");
        Label label = new Label();
        label.addStyleName("form-group__label");
        label.setText(cc ? wfmStrings.cc() : wfmStrings.bcc());
        panel.add(label);
        panel.add(cc ? ccText : bccText);
        bccCCPanel.add(panel);
        linkPanel.clear();
        if (cc ? !bccShown : !ccShown) {
            linkPanel.add(cc ? bccLink : ccLink);
        }
        ccShown = cc;
        bccShown = !cc;
    }

    private void generateMessageText() {
        if (template.isSomethingSelected()) {
            EntityToEmailTemplate item = new EntityToEmailTemplate();
            item.setEntityId(invoiceID);
            item.setEntityType(type);
            if (contact.getSelectedId() != null) {
                item.setMailReceiverId(contact.getSelectedId());
            } else if (mailReceiverId != null) {
                item.setMailReceiverId(mailReceiverId);
            } else {
                item.setMailReceiverId(contactID);
            }
            item.setEmailTemplateId(template.getSelectedItem(true).getId());
            EmailTemplateService.App.get().generateEmailTemplateData(item, null, new AbstractAsyncCallback<EmailTemplateItem>() {
                public void failure(Throwable caught) {
                }

                public void success(final EmailTemplateItem result) {
                    if (result != null && result.getMessageHTML() != null) {
                        subject.setText(result.getSubject());
                        defaultHTML.setHTML(result.getMessageHTML());
                        sign();
                        if (result.getFileResources() != null && result.getFileResources().size() > 0) {
                            attachments.clearFiles();
                            attachments.addFiles(result.getFileResources());
                        }
                    }
                }
            });
        }
    }

    public void getQuickData() {
        MessageCenterService.App.get().getUserEmailAccounts(true, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                from.setItems(result);
                for (SelectItem it : result) {
                    if (it.isSelected()) {
                        from.setSelected(it);
                        break;
                    }
                }
                from.setEnabled(result.length > 1);
            }
        });
        EmailTemplateService.App.get().getEmailTemplates(isReceipt ? RECEIPT_CATEGORY : PROJECT_BASE_INVOICE_CATEGORY.equals(type) ? SALES_INVOICE_CATEGORY : type, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final SelectItem[] result) {
                LoadingPanel.loading(false);
                template.setItems(result);
            }
        });
        if (fromDate == null || toDate == null) {
            SendToFormFillingData data = new SendToFormFillingData(clientOrManagerID, type);
            LoadingPanel.loading(true);
            invoiceService.getSendToFormData(data, contactID, true, null, new AbstractAsyncCallback<SendToFormFillingData>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(SendToFormFillingData data) {
                    if (data.getPrimaryContact() != null) {
                        setToEmail(data.getPrimaryContact().getName());
                    } else if (data.getContacts() != null && data.getContacts().length > 0) {
                        setToEmail(data.getContacts()[0].getName());
                    }
                    if (data.getContacts() != null && data.getContacts().length > 0) {
                        setContacts(data.getContacts());
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
                    if(mailReceiverId != null){
                        contact.setSelected(mailReceiverId);
                    }
                    if (data.getTemplateData() != null && data.getTemplateData().getItems() != null && data.getTemplateData().getItems().length > 1) {
                        pdfTemplate.setItems(data.getTemplateData().getItems());
                        if (pdfTemplateID != null) {
                            pdfTemplate.setSelected(pdfTemplateID);
                        } else if (data.getTemplateData().getDefaultTemplateID() != null) {
                            pdfTemplate.setSelected(data.getTemplateData().getDefaultTemplateID());
                        }
                    }
                }
            });
        }
    }

    public void setContacts(ContactItem[] contacts) {
        List<SelectItem> result = new ArrayList<>();
        for (ContactItem item : contacts) {
            if (item.getId() != null && item.getName() != null) {
                result.add(new SelectItem(item.getId(), item.getName()));
            }
        }
        contact.setItems(result.toArray(new SelectItem[]{}));
    }

    public boolean validate() {
        int errors = 0;
        if (!validateEmail(toText, true)) {
            errors++;
        }
        if (!validateEmail(ccText, isNotEmptyAlso(ccText))) {
            errors++;
        }
        if (!validateEmail(bccText, isNotEmptyAlso(bccText))) {
            errors++;
        }
        if (Utils.isNullOrEmpty(editor.getData())) {
            editor.addStyleName(ERROR_FORM_STYLE);
            editor.addKeyDownHandler(event -> editor.removeStyleName(ERROR_FORM_STYLE));
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        } else if (Utils.isNullOrEmpty(subject.getText()) && !subjectCheked) {
            subject.addStyleName(ERROR_FORM_STYLE);
            subject.addKeyDownHandler(event -> subject.removeStyleName(ERROR_FORM_STYLE));
            subjectCheked = true;
            Info.show(wfmMessages.sendingMessageWithoutSubjectWarn(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private boolean isNotEmptyAlso(MultiSelectContactLookUp lookUp) {
        String newText = lookUp.getTextBox().getValue();
        return !newText.isEmpty() && !newText.equalsIgnoreCase(ccBccDefaultText);
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

    public void sendMessage(WfmButton2 saveBtn) {
        saveBtn.setEnabled(false);

        MessageItem messageItem = new MessageItem();
        messageItem.setSubject(subject.getText());
        messageItem.setClient(!type.equals(PURCHASE_ORDER_CATEGORY));
        messageItem.setInvoiceID(invoiceID);
        messageItem.setSendCopyToMe(copyToMe.getValue());
        messageItem.setMailContent(editor.getData());

        if (contact.getSelectedId() != null) {
            messageItem.setContactId(contact.getSelectedId());
        } else if (toText.getSelectedItems() != null && toText.getSelectedItems().size() > 0) {
            messageItem.setContactId(toText.getSelectedItems().get(0).getId());
        }
        messageItem.setReceipt(isReceipt);
        messageItem.setEmailTemplateID(template.getSelectedId());
        messageItem.setPdfTemplateID(pdfTemplate.getSelectedId());
        messageItem.setType(type);
        messageItem.setToEmails(toText.getSelectedItemsAsString());
        messageItem.setCc(ccText.getSelectedItemsAsString());
        messageItem.setBcc(bccText.getSelectedItemsAsString());

        messageItem.setFromEmail(from.getSelectedItem() != null ? from.getSelectedItem().getName() : null);
        ArrayList<FileResource> fileResources = new ArrayList<>(attachments.getFiles());
        messageItem.setFileResources(fileResources);
        LoadingPanel.loading(true, panel);
        if(fromDate != null && toDate != null){
            messageItem.setAccountId(clientOrManagerID);
            messageItem.setIncludeSubAccountTransaction(includeSubAccountTransaction);
            ClientService.App.get().sendCustomerBalanceEmail(messageItem, Utils.getStartDateNC(fromDate), Utils.getEndDateNC(toDate), getVoidAsyncCallback(saveBtn));
        } else if (type.equals(SALES_INVOICE_CATEGORY) || type.equals(RECEIPT_CATEGORY)
                || type.equals(CREDIT_NOTE_CATEGORY) || type.equals(PROJECT_BASE_INVOICE_CATEGORY)) {
            if (isRecurringInvoice) {
                invoiceService.saveSendToClientDetails(messageItem, getVoidAsyncCallback(saveBtn));
            } else {
                invoiceService.sendToClient(messageItem, getAsyncCallback(saveBtn));
            }
        } else {
            quoteService.sendToClientOrSupplier(messageItem, getAsyncCallback(saveBtn));
        }
    }

    private AbstractAsyncCallback getAsyncCallback(WfmButton2 saveButton) {
        return new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false, panel);
                saveButton.setEnabled(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Integer result) {
                LoadingPanel.loading(false, panel);
                saveButton.setEnabled(true);
                if (result != null) {
                    Info.show(accountingMessages.dynamicSentSuccessfully(getTypeText()), Info.Type.INFO);
                    if (command != null) {
                        command.execute(result);
                    }
                } else {
                    Info.show("Can not send email. Please check your email account settings.", Info.Type.WARNING);
                }
            }
        };
    }

    private AbstractAsyncCallback getVoidAsyncCallback(WfmButton2 saveButton) {
        return new AbstractAsyncCallback() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false, panel);
                saveButton.setEnabled(true);
                Info.show("Can not send email. Please check your email account settings.", Info.Type.WARNING);
            }

            public void success(Void result) {
                LoadingPanel.loading(false, panel);
                saveButton.setEnabled(true);
                Info.show(accountingMessages.dynamicSentSuccessfully(getTypeText()), Info.Type.INFO);
                if (command != null) {
                    command.execute(isRecurringInvoice ? 0 : -1);
                }
            }
        };
    }

    private String getTypeText() {
        if (type != null) {
            if (SALES_INVOICE_CATEGORY.equals(type) || PROJECT_BASE_INVOICE_CATEGORY.equals(type)) {
                return wfmStrings.salesInvoice();
            } else if (SALES_ORDER_CATEGORY.equals(type)) {
                return wfmStrings.salesOrders();
            } else if (RECURRING_INVOICE_CATEGORY.equals(type)) {
                return accountingStrings.recurringInvoice();
            } else if (SALES_QUOTE_CATEGORY.equals(type) || SALES_QUOTE_MANAGER_CATEGORY.equals(type)) {
                return wfmStrings.salesQuote();
            } else if (PURCHASE_ORDER_CATEGORY.equals(type) || PURCHASE_ORDER_MANAGER_CATEGORY.equals(type)) {
                return wfmStrings.purchaseorder();
            } else if (RECEIPT_CATEGORY.equals(type)) {
                return accountingStrings.receipt();
            } else if (CREDIT_NOTE_CATEGORY.equals(type)) {
                return accountingStrings.creditNote();
            }
        }
        return "";
    }

    public void clearForm() {
        from.clear();
        template.clear();
        subject.setText("");
        editor.setData("");
        toText.clear();
        bccCCPanel.clear();
        bccText.clear();
        ccText.clear();
        linkPanel.clear();
        linkPanel.add(ccLink);
        linkPanel.add(bccLink);
        attachments.clearFiles();
        subjectCheked = false;
        signature = null;
        defaultHTML = new HTML();
    }

    public ExtendedCommand getCommand() {
        return command;
    }

    public void setCommand(ExtendedCommand command) {
        this.command = command;
    }
}