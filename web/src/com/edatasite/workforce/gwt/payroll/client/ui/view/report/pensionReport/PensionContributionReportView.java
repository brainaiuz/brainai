package com.edatasite.workforce.gwt.payroll.client.ui.view.report.pensionReport;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/13/14
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class PensionContributionReportView extends View {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    public PensionContributionReportView() {
        super("pensionContributionReport", payrollStrings.pensionContribution());
    }

    @Override
    protected Widget onInitialize() {
        add(new PensionContributionReport());
        return null;
    }
    @Override
    public String getIconStyle() {
        return "payroll ukni-bands-list";
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
