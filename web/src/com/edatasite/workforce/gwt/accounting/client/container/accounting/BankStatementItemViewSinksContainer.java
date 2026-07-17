package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BankStatementItemAddEditView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BankStatementItemSummaryView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 17.05.2010
 * Time: 20:26:55
 * To change this template use File | Settings | File Templates.
 */
public class BankStatementItemViewSinksContainer extends SinksContainer {

    public BankStatementItemViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length > 1) {
            addView(new BankStatementItemSummaryView(id,Integer.valueOf(params[1])));
            addView(new BankStatementItemAddEditView(id,Integer.valueOf(params[1])));
        }
    }
}
