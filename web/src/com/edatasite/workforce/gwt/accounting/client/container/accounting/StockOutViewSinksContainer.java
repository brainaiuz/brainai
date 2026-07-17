package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.StockOutView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/11/13
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class StockOutViewSinksContainer extends SinksContainer {

    public StockOutViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new StockOutView(id, true));  // View mode
        addView(new StockOutView(id, false)); // Edit mode
    }
}
