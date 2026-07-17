package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn;

import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

import java.util.Date;

public class FileVatReturnModal extends KpiModal {

    private DatePicker datePicker;
    private WfmButton2 fileAndClose;

    private VatReturnItem vatReturnItem;
    private Command fileCmd;

    public FileVatReturnModal(VatReturnItem vatReturnItem, Command cmd) {
        this.vatReturnItem = vatReturnItem;
        this.fileCmd = cmd;

        setTitle("File Tax Return");
        setWidth(400);

        datePicker = new DatePicker();
        HTML introduction = new HTML("Are you sure you want to mark this return as filed ? Once marked, the transactions for this Tax period cannot be edited!");
        introduction.addStyleName("mb-2");
        add(introduction);

        addWidget(datePicker, "Date of Filing");

        Date date = DateUtil.resetTime(DateUtil.addDays(vatReturnItem.getToDate().getNonConvertedDate(), 1));
        datePicker.setDate(date);

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        fileAndClose = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY, clickEvent -> file());
        addButton(fileAndClose);
        open();
    }

    private void file() {
        int errors = 0;

        if (!Validation.validateDate(datePicker)) {
            errors += 1;
        }
        if (errors > 0) {
            return;
        }
        boolean filed = AccountingConstants.VAT_RETURN_STATUS.FILED.equalsIgnoreCase(vatReturnItem.getStatus().getCode());
        VatReturnService.App.get().fileUnfileVatReturn(vatReturnItem.getObjectID(), new DateNonConvertable(datePicker.getDate()), !filed, new AsyncCallback<VatReturnItem>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(VatReturnItem item) {
                if (fileCmd != null) {
                    fileCmd.execute();
                }
            }
        });
    }
}
