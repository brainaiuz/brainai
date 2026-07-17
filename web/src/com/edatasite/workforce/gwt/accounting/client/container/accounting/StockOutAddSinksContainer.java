package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.StockOutView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class StockOutAddSinksContainer extends SinksContainer {

    public StockOutAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        if (params.length == 2) {
            addView(new StockOutView(Integer.valueOf(params[1])));
        } else {
            addView(new StockOutView(params));
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
