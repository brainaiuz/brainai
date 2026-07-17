package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.TransferMoneySummaryView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.TransferMoneyView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 16.05.12
 * Time: 16:27
 * To change this template use File | Settings | File Templates.
 */
public class TransferMoneyViewSinksContainer extends SinksContainer {

    public TransferMoneyViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new TransferMoneySummaryView(id));
        addView(new TransferMoneyView(id,true));

    }
}
