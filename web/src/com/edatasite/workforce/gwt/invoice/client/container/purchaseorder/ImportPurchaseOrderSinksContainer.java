package com.edatasite.workforce.gwt.invoice.client.container.purchaseorder;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder.PurchaseOrderImportView;

import java.util.LinkedList;

public class ImportPurchaseOrderSinksContainer extends SinksContainer {

    public ImportPurchaseOrderSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new PurchaseOrderImportView(Integer.valueOf(params[1])));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

}
