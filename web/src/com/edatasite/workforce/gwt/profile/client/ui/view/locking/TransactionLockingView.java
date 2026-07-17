package com.edatasite.workforce.gwt.profile.client.ui.view.locking;

import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;

public class TransactionLockingView extends FooteredView implements Colapse, FittedContent, Constants {
    public TransactionLockingView() {
        super("transactionLocking");
        setDescription(wfmStrings.transactionLocking());
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        TransactionLockingForm form = new TransactionLockingForm();
        Div formDiv = new Div("add-form");
        formDiv.add(form.getRootElement());
        formDiv.add(new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                List<Widget> rightWidgets = new ArrayList<>();
                if (Utils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_SALES) ||
                        Utils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_PURCHASES) ||
                        Utils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_BANKING) ||
                        Utils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_EMPLOYEES) ||
                        Utils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_ATTENDANCE) ||
                        Utils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_RECRUITMENT) ||
                        Utils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_PAYSLIPS) ||
                        Utils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_CASH_ADVANCES) ||
                        Utils.hasPermission(PermissionConstants.TRANSACTION_LOOKING_ADDITIONAL_PAYMENTS)) {
                    WfmButton2 saveButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
                    saveButton.addClickHandler(event -> new TransactionLockingReasonPopup(form.getData()));
                    rightWidgets.add(saveButton);
                }
                return rightWidgets;
            }
        }));
        add(formDiv);
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
