package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.InventoryStockTransferView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by Dilshod Madrahimov on 2/26/15.
 */
public class StockTransferAddSinksContainer  extends SinksContainer {

    public StockTransferAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length == 2) {
            addView(new InventoryStockTransferView(Integer.valueOf(params[1])));
        } else {
            addView(new InventoryStockTransferView());
        }
    }
}
