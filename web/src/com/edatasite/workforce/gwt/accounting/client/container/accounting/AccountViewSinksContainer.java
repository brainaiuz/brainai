package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AddEditAccountView2;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Jonibek
 * Date: Apr 13, 2009
 * Time: 2:23:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class AccountViewSinksContainer extends SinksContainer {
    public AccountViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    protected void initViews() {
        super.addView(new AddEditAccountView2(id));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
