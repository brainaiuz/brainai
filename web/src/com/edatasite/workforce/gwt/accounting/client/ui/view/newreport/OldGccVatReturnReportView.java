package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnReport.NewVatReturnReport;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class OldGccVatReturnReportView extends View implements Colapse, AccountingConstants, FittedContent {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private Integer vatReturnId;

    public OldGccVatReturnReportView() {
        super("oldGccVatReturn");
        setDescription(property.getPlural("(" + wfmStrings.oldW() + ")" + accountingStrings.vatReturn()));

    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        add(new NewVatReturnReport(true));
        return null;
    }

    @Override
    public void reInitialize() {
        Utils.frame_affix_fixed_top();
    }

    @Override
    public String getIconStyle() {
        return "salQuoLits sales-quote-list";
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

    @Override
    public String getPropertyCode() {
        return "oldGccVatReturn";
    }
}
