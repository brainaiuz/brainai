package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BankStatementItemAddEditView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BankStatementItemSummaryView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Dilshod Madrahimov
 * Date: 11.28.2015
 */
public class BankStatementItemAddEditSinksContainer extends SinksContainer {

    public BankStatementItemAddEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length > 1) {
            addView(new BankStatementItemAddEditView(id,Integer.valueOf(params[1])));
            addView(new BankStatementItemSummaryView(id,Integer.valueOf(params[1])));
        }
    }
}
