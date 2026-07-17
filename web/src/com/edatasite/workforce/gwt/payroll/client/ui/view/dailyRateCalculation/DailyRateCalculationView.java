package com.edatasite.workforce.gwt.payroll.client.ui.view.dailyRateCalculation;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class DailyRateCalculationView extends View {
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    public DailyRateCalculationView() {
        super("dailyRateCalculation", payrollStrings.dailyRateCalculation());

    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public Widget onInitialize() {
        Widget result = super.onInitialize();
        final DailyRateCalculationUIBinder uiBinder = new DailyRateCalculationUIBinder();
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
