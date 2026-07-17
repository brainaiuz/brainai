package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.FinancialSettingsItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BalanceSheet.NewBalanceSheet;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by admin on 17.09.2014.
 */
public class NewBalanceSheetView extends View implements Colapse, AccountingConstants, FittedContent {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public NewBalanceSheetView() {
        super("balanceSheet");
        setDescription(property.getPlural(wfmStrings.balanceSheet()));
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        AccountingService.App.get().getCompanyFinancialSettings(new AsyncCallback<FinancialSettingsItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn("Error! Could not load company financial settings.");
            }

            @Override
            public void onSuccess(FinancialSettingsItem financialSettingsItem) {
                add(new NewBalanceSheet());
            }
        });

        return null;
    }

    @Override
    public void reInitialize() {
        Utils.frame_affix_fixed_top();
    }

    @Override
    public String getIconStyle() {
        return "accountMark report-list";
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
        return "balanceSheet";
    }
}
