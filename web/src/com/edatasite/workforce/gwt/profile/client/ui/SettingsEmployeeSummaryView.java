package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.contact.client.ui.NewGlobalEmployeeSummaryView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Hurshid on 12/11/2015.
 */
public class SettingsEmployeeSummaryView extends NewGlobalEmployeeSummaryView {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    public SettingsEmployeeSummaryView() {
        super(Utils.getUserID(), "profile", wfmStrings.contactprofile(), FROM_SETTINGS_EMPLOYEE_VIEW);
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SETTINGS_EMPLOYEE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected void registerFields() {
        super.registerFields();

        drawEmployeeInformation();
        drawContactDetails();

        drawAddressInformation();
        drawAttachments();

        show();
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
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
