package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BankStatementListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 17.05.2010
 * Time: 20:25:15
 * To change this template use File | Settings | File Templates.
 */
public class BankStatementSinksContainer extends SinksContainer {

    public BankStatementSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length > 1) {
            addView(new BankStatementListView(id, params[1]));
        } else {
            addView(new BankStatementListView(id));
        }
    }
}
