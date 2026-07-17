package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 01.06.2011
 * Time: 8:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountLookUpForExpense extends AccountsLookUp {
    public AccountLookUpForExpense(String type) {
        super(type);
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        filterParametrs.setAccountCode(accountCode);
        filterParametrs.setCurrencyID(currencyID);

        if (types != null && !types.isEmpty()) {
            filterParametrs.setAccountType(types.get(0));
        }
        AccountingService.App.get().getAccountsForExpense(filterParametrs, new AsyncCallback<AccountItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(AccountItem[] result) {
                AccountLookUpForExpense.this.onSuccess(result, filterParametrs);
            }
        });
    }
}
