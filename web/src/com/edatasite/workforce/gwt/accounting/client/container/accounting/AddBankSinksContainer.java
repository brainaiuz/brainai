package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AddEditBankAccountForm;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: java
 * Date: 26.02.2009
 * Time: 18:32:47
 * To change this template use File | Settings | File Templates.
 */
public class AddBankSinksContainer extends SinksContainer {
    public AddBankSinksContainer(String name, String description) {
        super(name, description);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new AddEditBankAccountForm());
    }
}
