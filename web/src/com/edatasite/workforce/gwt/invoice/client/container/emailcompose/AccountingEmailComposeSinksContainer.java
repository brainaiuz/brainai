package com.edatasite.workforce.gwt.invoice.client.container.emailcompose;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.emailcompose.AccountingEmailComposeView;

import java.util.LinkedList;

public class AccountingEmailComposeSinksContainer extends SinksContainer {

    public AccountingEmailComposeSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new AccountingEmailComposeView(params));
    }
}
