package com.edatasite.workforce.gwt.invoice.client.container.shippingData;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.GoodsDeliveredNotesSummaryView;

import java.util.LinkedList;

/**
 * User: murad
 * Date: 2/17/18 1:31 AM
 */
public class GoodsDeliveredNotesSinksContainer extends SinksContainer {
    public GoodsDeliveredNotesSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        this.addView(new GoodsDeliveredNotesSummaryView(id));
    }
}
