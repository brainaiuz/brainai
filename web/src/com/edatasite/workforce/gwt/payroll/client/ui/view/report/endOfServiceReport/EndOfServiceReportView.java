package com.edatasite.workforce.gwt.payroll.client.ui.view.report.endOfServiceReport;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/16/15
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class EndOfServiceReportView extends View {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    public EndOfServiceReportView() {
        super("eosReport", payrollStrings.endOfService());
    }

    @Override
    protected Widget onInitialize() {
        add(new EndOfServiceReport(this));
        return null;
    }

    @Override
    public void reInitialize() {
        Utils.frame_affix_fixed_top();
    }

    @Override
    public String getIconStyle() {
        return "payroll efile-to-hmrc";
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
