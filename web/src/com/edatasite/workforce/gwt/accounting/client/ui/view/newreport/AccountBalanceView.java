package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport;

import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.CrmAccountBalance.NewCrmAccountBalance;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class AccountBalanceView extends View {
    private Integer accountId;
    private String crmAccountType;

    public AccountBalanceView(Integer accountId, String crmAccountType) {
        super(CrmAccountItem.CUSTOMER.equals(crmAccountType) ? "customerBalance" : "supplierBalance", "SOA");
        this.accountId = accountId;
        this.crmAccountType = crmAccountType;
    }

    @Override
    protected Widget onInitialize() {
        add(new NewCrmAccountBalance(accountId, crmAccountType));
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
