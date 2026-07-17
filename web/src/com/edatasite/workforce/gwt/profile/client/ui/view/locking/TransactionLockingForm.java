package com.edatasite.workforce.gwt.profile.client.ui.view.locking;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.profile.client.rpc.TransactionLockingService;
import com.edatasite.workforce.gwt.profile.client.rpc.locking.TransactionLocking;
import com.edatasite.workforce.gwt.profile.client.rpc.locking.TransactionLockingModule;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;

import java.util.HashMap;

public class TransactionLockingForm implements Constants {
    interface TransactionLockingFormUiBinder extends UiBinder<HTMLPanel, TransactionLockingForm> {}
    private static final TransactionLockingFormUiBinder uiBinder = GWT.create(TransactionLockingFormUiBinder.class);
    private final WfmStrings wfmStrings = WfmStrings.App.get();
    @UiField
    Heading headingTitle;
    @UiField
    Div headingDescription;
    @UiField
    Div body;
    private final HTMLPanel rootElement;
    private TransactionLocking data;

    public TransactionLockingForm() {
        rootElement = uiBinder.createAndBindUi(this);
        inititialize();
        initEventListeners();
    }

    private void inititialize() {
        LoadingPanel.loading(true);
        TransactionLockingService.App.get().getLock(new AsyncCallback<TransactionLocking>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(TransactionLocking result) {
                data = result;
                LoadingPanel.loading(false);

                headingTitle.setText(wfmStrings.transactionLocking());
//                headingDescription.getElement().setInnerText(data.getDescription() != null ? data.getDescription() : "There is no one who loves pain itself, who seeks after it and wants to have it, simply because it is pain...");
                initHeader();
                setItems();
            }
        });
    }

    public void setItems() {
        for (HashMap.Entry<String, TransactionLockingModule> item : data.getModules().entrySet()) {
            body.add(new TransactionLockingFormItem(item.getValue()));
        }
    }

    private void initHeader() {
        /*MaterialIconMorph lockMorph = new MaterialIconMorph();
        lockMorph.setIconSize(IconSize.SMALL);
        lockMorph.setSource(new MaterialIcon(IconType.LOCK_OPEN));
        lockMorph.setTarget(new MaterialIcon(IconType.LOCK_OUTLINE));
        if (!isLocked()) {
            lockMorph.getElement().toggleClassName("morphed");
        }
        lockMorph.addIconMorphedHandler(event -> {
            if (event.isMorphed()) {
                this.data.setStatus("unlocked");
            } else {
                this.data.setStatus("locked");
            }
        });

        figureActions.add(lockMorph);*/
        String description = localeStatus(data.getStatus());
        String lockDate = DateUtils.format(data.getLockDate());
        if (lockDate != null && !lockDate.isEmpty()) {
            description += " | " + lockDate;
        }
        headingDescription.getElement().setInnerHTML(description);
    }

    private void initEventListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TRANSACTION_LOCKING_EVENT, (sender, args) -> {
            TransactionLocking result = (TransactionLocking) args;
            String description = localeStatus(result.getStatus());
            String lockDate = DateUtils.format(result.getLockDate());
            if (lockDate != null && !lockDate.isEmpty()) {
                description += " | " + lockDate;
            }
            headingDescription.getElement().setInnerHTML(description);

            if (("locked".equals(result.getStatus()) || "partially_unlocked".equals(result.getStatus())) && result.getLockDate() != null) {
                DateUtils.setTransactionLockDate(result.getLockDate().getNonConvertedDate());
            }
        });
    }

    private String localeStatus(String status) {
        switch (status) {
            case "locked": return "Locked";
            case "partially_unlocked": return "Partially Unlocked";
            case "unlocked": return "Unlocked";
            default: return "Disabled";
        }
    }

    private boolean isLocked() {
        return "locked".equals(data.getStatus());
    }

    public HTMLPanel getRootElement() {
        return rootElement;
    }

    public TransactionLocking getData() {
        return data;
    }
}
