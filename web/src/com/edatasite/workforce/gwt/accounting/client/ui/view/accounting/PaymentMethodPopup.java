package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;

public class PaymentMethodPopup extends KpiModal {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private Integer objectID;
    private Command closeCommand;

    private WfmButton2 saveButton;
    private WfmButton2 closeButton;

    public PaymentMethodPopup() {
        this(null);
    }

    public PaymentMethodPopup(Integer objectID) {
        this.objectID = objectID;
        init();
        open();
    }

    private void init() {
        this.setWidth("400px");
        closeCommand = PaymentMethodPopup.this::close;
        AddPaymentMethodView view;
        Command temp = null;
        if (objectID == null) {
            view = new AddPaymentMethodView(closeCommand);
        } else {
            view = new AddPaymentMethodView(objectID, closeCommand);
        }
        view.onReadyToInitialize();
        add(view);
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler((e) -> {
            setButtonsEnabled(false);
            if (!view.save()) {
                setButtonsEnabled(true);
            }
        });
        closeButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, clickEvent -> close());
        addButton(closeButton);
        addButton(saveButton);
    }

    private void setButtonsEnabled(boolean enabled) {
        saveButton.setEnabled(enabled);
        closeButton.setEnabled(enabled);
    }

}
