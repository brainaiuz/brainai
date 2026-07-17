package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.InventoryStockAdjustmentView;
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
public class StockAdjustmentViewSinksContainer extends SinksContainer{

    public StockAdjustmentViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new InventoryStockAdjustmentView(id, true));
        addView(new InventoryStockAdjustmentView(id, false));
    }
}
