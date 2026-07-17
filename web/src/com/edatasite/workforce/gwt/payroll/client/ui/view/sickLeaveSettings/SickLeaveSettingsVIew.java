package com.edatasite.workforce.gwt.payroll.client.ui.view.sickLeaveSettings;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class SickLeaveSettingsVIew extends View {
    private final static PayrollStrings payrollStrings = PayrollStrings.App.get();

    public SickLeaveSettingsVIew() {
        super("sickleavesettingsadd", wfmStrings.annualLeaveSettings());
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        Widget result = super.onInitialize();
        final SickLeaveSettingsUIBinder uiBinder = new SickLeaveSettingsUIBinder();
        uiBinder.init();
        add(uiBinder.getRootElement());
        return result;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
