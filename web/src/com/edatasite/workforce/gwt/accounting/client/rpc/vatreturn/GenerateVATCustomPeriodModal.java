package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn;

import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnItem;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;

public class GenerateVATCustomPeriodModal extends KpiModal {

    private final DatePicker fromDatePicker;
    private final DatePicker toDatePicker;
    private final WfmButton2 saveAndClose;

    private final VATSettingsItem settingsItem;
    private final Command saveCmd;

    public GenerateVATCustomPeriodModal(VATSettingsItem settingsItem, Command saveCmd) {
        this.settingsItem = settingsItem;
        this.saveCmd = saveCmd;

        setTitle(wfmStrings.generateVAT());
        setWidth(400);

        fromDatePicker = new DatePicker();
        toDatePicker = new DatePicker();

        addWidget(fromDatePicker, wfmStrings.fromDate());
        addWidget(toDatePicker, wfmStrings.toDate());

        if (settingsItem.getLastTaxGeneratedDate() != null) {
            Date date = DateUtil.resetTime(DateUtil.addDays(settingsItem.getLastTaxGeneratedDate().getNonConvertedDate(), 1));
            fromDatePicker.setDate(date);
            fromDatePicker.setEnabled(false);
        }
        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        saveAndClose = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(saveAndClose);
        open();
    }

    private void save() {
        int errors = 0;

        if (!Validation.validateDate(fromDatePicker)) {
            errors += 1;
        }
        if (!Validation.validateDate(toDatePicker)) {
            errors += 1;
        }

        if (errors > 0) {
            return;
        }

        if (settingsItem.getTaxGenerationDate()!= null && fromDatePicker.getDate().compareTo(settingsItem.getTaxGenerationDate().getNonConvertedDate()) < 0) {
            Info.show("From date cannot be less than tax generation date.", Info.Type.WARNING);
            return;
        }
        if (fromDatePicker.getDate().compareTo(toDatePicker.getDate()) > 0) {
            Info.show("To date cannot be less than from date.", Info.Type.WARNING);
            return;
        }
        VatReturnService.App.get().createVatReturn(new DateNonConvertable(fromDatePicker.getDate()), new DateNonConvertable(toDatePicker.getDate()), new AsyncCallback<VatReturnItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(VatReturnItem item) {
                close();
                SinksContainerFactory.entryPoint.onHistoryChanged("vatreturn|vatReturn/" + item.getObjectID());

                if (saveCmd != null) {
                    saveCmd.execute();
                }
            }
        });
    }
}
