package com.edatasite.workforce.gwt.profile.client.ui.view.locking;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.rpc.TransactionLockingService;
import com.edatasite.workforce.gwt.profile.client.rpc.locking.TransactionLocking;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;

public class TransactionLockingReasonPopup extends KpiModal {

    private DatePicker lockDate;
    private TextArea reason;
    private WfmButton2 lockUnlock;
    private TransactionLocking data;

    public TransactionLockingReasonPopup(TransactionLocking data) {
        this.data = data;
        setTitle(wfmStrings.block());
        setWidth(400);
        init();
        open();
    }

    private void init() {
        lockDate = new DatePicker();
        lockDate.setWidth("100%");
        addWidget(lockDate, wfmStrings.date());

        if (data.getLockDate() != null) {
            lockDate.setDate(data.getLockDate().getNonConvertedDate());
        }

        reason = new TextArea();
        reason.setWidth("100%");
        addWidget(reason, wfmStrings.reason());

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        lockUnlock = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save(isLocked()));
        addButton(lockUnlock);
    }

    private void save(boolean locked) {
        if (!locked && !Validation.validateTextBoxRequired(reason)) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return;
        }
        if (locked && !Validation.validateDate(lockDate)) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return;
        }
        data.setReason(reason.getValue());
        data.setLockDate(lockDate.getDateAsNonConvertable());
        LoadingPanel.loading(true);
        lockUnlock.setEnabled(false);
        TransactionLockingService.App.get().lock(data, new AsyncCallback<String>() {
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                lockUnlock.setEnabled(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void onSuccess(String status) {
                LoadingPanel.loading(false);
                close();
                Info.show(wfmStrings.success(), Info.Type.INFO);
                data.setStatus(status);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TRANSACTION_LOCKING_EVENT, data, TransactionLockingReasonPopup.this);
            }
        });
    }

    private boolean isLocked() {
        return "locked".equals(data.getStatus());
    }
}
