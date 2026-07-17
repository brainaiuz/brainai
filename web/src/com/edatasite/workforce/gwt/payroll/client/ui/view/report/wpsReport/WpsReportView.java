package com.edatasite.workforce.gwt.payroll.client.ui.view.report.wpsReport;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Shohruh on 27-Dec-16.
 */
public class WpsReportView extends View {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    public WpsReportView() {
        super("wpsReport", payrollStrings.wps());
    }

    @Override
    protected Widget onInitialize() {
        add(new WpsReport());
        return null;
    }

    @Override
    public String getIconStyle() {
        return "payroll ukni-bands-list";
    }

    @Override
    public void reInitialize() {
        Utils.frame_affix_fixed_top();
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
