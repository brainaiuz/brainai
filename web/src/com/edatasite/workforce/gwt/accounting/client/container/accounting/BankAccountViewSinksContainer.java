package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AddEditBankAccountForm;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BankAccountViewForm;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jul 16, 2009
 * Time: 4:16:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class BankAccountViewSinksContainer extends SinksContainer {
    public BankAccountViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new AddEditBankAccountForm(id));
        addView(new BankAccountViewForm(id));
    }
}
