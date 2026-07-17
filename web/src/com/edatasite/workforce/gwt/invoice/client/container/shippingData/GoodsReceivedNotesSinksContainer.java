package com.edatasite.workforce.gwt.invoice.client.container.shippingData;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.GoodsReceivedNotesSummaryView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 18.04.2009
 * Time: 15:20:10
 * To change this template use File | Settings | File Templates.
 */
public class GoodsReceivedNotesSinksContainer extends SinksContainer {

    public GoodsReceivedNotesSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        this.addView(new GoodsReceivedNotesSummaryView(id));
    }
}
