package com.edatasite.workforce.gwt.accounting.client;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.NewTransactionItemView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Power
 * Date: 11/29/12
 * Time: 7:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransactionItemViewSinksContainer extends SinksContainer {

    public TransactionItemViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new NewTransactionItemView(id));
    }
}