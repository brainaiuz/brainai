package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

import java.util.Date;

public class AccountingComposeView extends Composite implements Constants {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private KpiSideNavBox composeMessage;
    private String type;
    private Integer clientOrManagerID;
    private Integer invoiceID;
    private Integer contactID;
    private Integer pdfTemplateID;
    private boolean isReceipt;
    private boolean isRecurringInvoice;
    private Date fromDate;
    private Date toDate;
    private boolean includeSubAccountTransaction;

    public AccountingComposeView(String type, Integer clientOrManagerId, Integer invoiceId, Integer contactId, Integer pdfTemplateId, boolean isReceipt) {
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

    public AccountingComposeView(String type, Integer clientOrManagerID, Date fromDate, Date toDate, boolean includeSubAccountTransaction) {
        this.type = type;
        this.clientOrManagerID = clientOrManagerID;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.includeSubAccountTransaction = includeSubAccountTransaction;
        init();
    }

    private void init() {
        composeMessage = new KpiSideNavBox(KpiSideNavBox.WIDE_FORM_WIDTH);
        composeMessage.setStyleName(composeMessage.getElement(), "quick-add", true);

        AccountingQuickCompose quickAddForm = fromDate != null && toDate != null ? new AccountingQuickCompose(type, clientOrManagerID, fromDate, toDate, includeSubAccountTransaction) :
                new AccountingQuickCompose(type, clientOrManagerID, invoiceID, contactID, pdfTemplateID, isReceipt);

        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.compose());

        WfmButton2 saveBtn = new WfmButton2(wfmStrings.send(), WfmButton2.BTN_PRIMARY);

        saveBtn.addClickHandler(event -> {
            if (quickAddForm.validate()) {
                quickAddForm.sendMessage(saveBtn);
            }
        });
        quickAddForm.setCommand(new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
                composeMessage.remove();
                if (id > 0) {
                    if (SALES_INVOICE_CATEGORY.equals(type) || RECEIPT_CATEGORY.equals(type) || CREDIT_NOTE_CATEGORY.equals(type) || isRecurringInvoice) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, id, AccountingComposeView.this);
                    } else if (SALES_QUOTE_CATEGORY.equals(type) || SALES_QUOTE_MANAGER_CATEGORY.equals(type)) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, id, AccountingComposeView.this);
                    } else if (PURCHASE_ORDER_CATEGORY.equals(type)) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_ADDED, id, AccountingComposeView.this);
                    }
                } else if (id == 0){
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, id, AccountingComposeView.this);
                }
            }
        });
        composeMessage.addOpeningHandler(event -> quickAddForm.getQuickData());
        composeMessage.addHeader(header);
        composeMessage.addBody(quickAddForm);
        composeMessage.addFooter(saveBtn);
        composeMessage.show();
    }
}
