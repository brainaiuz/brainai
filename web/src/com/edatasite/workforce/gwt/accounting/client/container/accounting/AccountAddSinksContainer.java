package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AddEditAccountView2;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 25.02.2009
 * Time: 15:55:07
 * To change this template use File | Settings | File Templates.
 */
public class AccountAddSinksContainer extends SinksContainer {
    public AccountAddSinksContainer(String name, String description) {
        super(name, description);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new AddEditAccountView2());
    }
}
