package com.edatasite.workforce.gwt.invoice.client.container.saleorderbaseinvoice;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleorderbaseinvoice.SaleOrderBaseInvoiceView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 13.05.2009
 * Time: 13:12:09
 * To change this template use File | Settings | File Templates.
 */
public class SaleOrderBaseInvoiceAddSinksContainer extends SinksContainer {

    public SaleOrderBaseInvoiceAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params.length > 1) {
            addView(new SaleOrderBaseInvoiceView(params[1]));
        } else {
            addView(new SaleOrderBaseInvoiceView());
        }
    }
}
