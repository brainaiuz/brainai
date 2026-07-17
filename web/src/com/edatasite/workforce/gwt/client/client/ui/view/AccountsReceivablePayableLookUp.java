package com.edatasite.workforce.gwt.client.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * Created by Sherzod on 9/14/2015.
 */
public class AccountsReceivablePayableLookUp extends AccountsLookUp {

    private boolean isPrepayment;

    public AccountsReceivablePayableLookUp(String type) {
        super(type);
    }

    public AccountsReceivablePayableLookUp(String type, boolean isPrepayment) {
        super(type);
        this.isPrepayment = isPrepayment;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    public void clear() {
        super.clear();
        clearOracleItems();
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.loading(true);
        if (types != null && !types.isEmpty()) {
            filterParametrs.setAccountType(types.get(0));
        }
        filterParametrs.setPrepayment(isPrepayment);
        filterParametrs.setCurrencyID(currencyID);
        AccountingService.App.get().getAccountsReceivablePayable(filterParametrs, new AsyncCallback<ArrayList<AccountItem>>() {
            @Override
            public void onFailure(Throwable caught) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<AccountItem> result) {
                AccountsReceivablePayableLookUp.super.onSuccess(result.toArray(new AccountItem[]{}), filterParametrs);
            }
        });
    }

}
