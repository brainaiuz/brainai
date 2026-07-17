package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BankTransferAddEditView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BankTransferSummaryView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 15.07.2010
 * Time: 19:08:03
 * To change this template use File | Settings | File Templates.
 */
public class SpendReceiveMoneyViewSinksContainer extends SinksContainer {

    public SpendReceiveMoneyViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new BankTransferAddEditView(params));
        addView(new BankTransferSummaryView(params));
    }
}
