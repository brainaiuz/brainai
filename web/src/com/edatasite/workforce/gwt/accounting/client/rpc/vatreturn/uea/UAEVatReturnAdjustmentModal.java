package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uea;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatAdjustmentItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnService;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;

public class UAEVatReturnAdjustmentModal extends KpiModal {
    private final DatePicker datePicker;
    private final TextBox txtReference;
    private final TextBox txtAmount;
    private final AccountsLookUp accountsLookUp;
    private final TextArea2 txtReason;
    private final WfmButton2 saveAndClose;
    private final Integer vatReturnId;
    private final Command saveCmd;

    public UAEVatReturnAdjustmentModal(Integer vatReturnId, Command saveCmd) {
        this.vatReturnId = vatReturnId;
        this.saveCmd = saveCmd;

        setTitle("Adjust amount in box - 6");
        setWidth(500);

        datePicker = new DatePicker();
        addWidget(datePicker, wfmStrings.date());

        txtReference = new TextBox();
        addWidget(txtReference, wfmStrings.reference());

        txtAmount = new TextBox();
        Validation.addNumericKeyboardListener(txtAmount, 2, true);
        addWidget(txtAmount, wfmStrings.amount());

        accountsLookUp = new AccountsLookUp();
        addWidget(accountsLookUp, wfmStrings.account());

        txtReason = new TextArea2();
        txtReason.setMAX_LENGTH(500);
        addWidget(txtReason, wfmStrings.reason());

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        saveAndClose = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(saveAndClose);
        open();
    }

    private void save() {

        if (!validateRequired()) {
            return;
        }

        VatAdjustmentItem adjustmentItem = new VatAdjustmentItem();
        adjustmentItem.setDate(new DateNonConvertable(DateUtil.resetTime(datePicker.getDate())));
        adjustmentItem.setReference(txtReference.getText());
        adjustmentItem.setAmount(AccountingUtils.parsePriceToBigDecimal(txtAmount.getText()));
        adjustmentItem.setAccount(accountsLookUp.getSelectedItem());
        adjustmentItem.setReason(txtReason.getText());

        LoadingPanel.loading(true, this);
        VatReturnService.App.get().createVatAdjustment(vatReturnId, adjustmentItem, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void aVoid) {
                LoadingPanel.loading(false);
                close();

                if (saveCmd != null) {
                    saveCmd.execute();
                }
            }
        });
    }

    private boolean validateRequired() {
        int errors = 0;

        if (!Validation.validateDate(datePicker)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(txtAmount)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(accountsLookUp)) {
            errors++;
        }
        if (!Validation.validateTextAreaRequired(txtReason)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
        }
        return errors == 0;
    }
}
