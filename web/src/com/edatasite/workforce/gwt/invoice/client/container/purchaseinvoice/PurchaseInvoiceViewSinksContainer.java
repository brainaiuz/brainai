package com.edatasite.workforce.gwt.invoice.client.container.purchaseinvoice;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice.PurchaseInvoiceSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice.PurchaseInvoiceView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 06.03.2009
 * Time: 16:19:04
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseInvoiceViewSinksContainer extends SinksContainer {

    public PurchaseInvoiceViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        boolean isRecurringBill = getName().startsWith("recurringbill");
        if ("true".equals(Utils.userSettings.get(ACCOUNTING_IS_SETUP))) {
            addView(new PurchaseInvoiceSummaryView(id, isRecurringBill));
        }

        addView(new PurchaseInvoiceView(id, params, isRecurringBill));
    }
}
