package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AccountTransactionsListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 15.05.2010
 * Time: 17:59:28
 * To change this template use File | Settings | File Templates.
 */
public class AccountTransactionsSinksContainer extends SinksContainer {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public AccountTransactionsSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        if (params != null && params.length > 2) {
            addView(new AccountTransactionsListView(id, AccountingConstants.BANK_ACCOUNT.equals(params[1]), params[2]));
        } else if (params != null && params.length == 2) {
            addView(new AccountTransactionsListView(id, AccountingConstants.BANK_ACCOUNT.equals(params[1])));
        } else {
            addView(new AccountTransactionsListView(id, false));
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
