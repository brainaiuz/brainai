package com.edatasite.workforce.gwt.payroll.client.ui.view.report.salaryDetailedReport;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class SalaryDetailedReportView extends View implements FittedContent {
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    public SalaryDetailedReportView() {
        super("salaryTransactionReport", payrollStrings.salaryTransactions());
    }

    @Override
    protected Widget onInitialize() {
        add(new SalaryDetailedReport());
        return null;
    }

    @Override
    public String getIconStyle() {
        return "payroll efile-to-hmrc";
    }

    @Override
    public void reInitialize() {
        Utils.frame_affix_fixed_top();
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
