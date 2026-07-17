package com.edatasite.workforce.gwt.invoice.client.container.purchaseorder;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder.PurchaseOrderSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder.PurchaseOrderView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 18.04.2009
 * Time: 15:20:10
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseOrderViewSinksContainer extends SinksContainer {

    public PurchaseOrderViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length == 3 && "CONVERT".equals(params[0])) {
            addView(new PurchaseOrderView(params));
        } else {
            addView(new PurchaseOrderSummaryView(id));
            addView(new PurchaseOrderView(id));
        }
//        addView(new GoodsReceivedNotesListView(id));
    }
}
