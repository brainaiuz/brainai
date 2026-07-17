package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;

public class TermPopupView extends KpiModal {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final Integer objectID;
    private InvoiceTermsItem item;
    private Command externalCommand;
    private TextBox nameTxtBox;
    private TextBox daysTxtBox;
    private WfmButton2 saveButton;

    public TermPopupView() {
        this(null);
    }

    public TermPopupView(Integer objectID) {
        this.objectID = objectID;
        setTitle(objectID != null ? accountingStrings.editTerm() : accountingStrings.addTerm());
        setWidth(300);
        init();
        getData();
        open();
    }

    private void init() {
        nameTxtBox = new TextBox();
        nameTxtBox.ensureDebugId("termName-textBox");
        daysTxtBox = new TextBox();
        daysTxtBox.ensureDebugId("termDays-textBox");
        Validation.addNumericKeyboardListener(daysTxtBox, 0);

        addWidget(nameTxtBox, wfmStrings.name());
        addWidget(daysTxtBox, wfmStrings.days());
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        addButton(saveButton);
    }

    private void getData() {
        if (objectID != null) {
            LoadingPanel.loading(true, TermPopupView.this);
            InvoiceService.App.get().getInvoiceTerm(objectID, new AsyncCallback<InvoiceTermsItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false, TermPopupView.this);
                }

                @Override
                public void onSuccess(InvoiceTermsItem result) {
                    LoadingPanel.loading(false, TermPopupView.this);
                    nameTxtBox.setText(result.getName());
                    daysTxtBox.setText(result.getDays().toString());
                }
            });
        }
    }

    private void save() {
        if(!validate()){
            return;
        }
        if (item == null) {
            item = new InvoiceTermsItem();
        }
        item.setId(objectID);
        item.setName(nameTxtBox.getText());
        item.setDays((int) NumberFormat.getDecimalFormat().parse(daysTxtBox.getText()));
        saveButton.setEnabled(false);
        LoadingPanel.loading(true, TermPopupView.this);
        InvoiceService.App.get().saveInvoiceTerms(item, new AsyncCallback<InvoiceTermsItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false, TermPopupView.this);
                saveButton.setEnabled(true);
            }

            @Override
            public void onSuccess(InvoiceTermsItem result) {
                LoadingPanel.loading(false, TermPopupView.this);
                item = result;
                close();
                if (externalCommand != null) {
                    externalCommand.execute();
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.terms()), Info.Type.INFO);
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICE_TERM_SAVED, result, TermPopupView.this);
            }
        });
    }

    @Override
    public boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(nameTxtBox)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(daysTxtBox)) {
            errors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return false;
        }
        return true;
    }

    public void setExternalCommand(Command externalCommand) {
        this.externalCommand = externalCommand;
    }

    public InvoiceTermsItem getItem() {
        return item;
    }
}
