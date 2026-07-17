package com.edatasite.workforce.gwt.invoice.client.container.saleinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SaleInvoiceSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SalesInvoiceView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 24.02.2009
 * Time: 14:20:06
 * To change this template use File | Settings | File Templates.
 */
public class SaleInvoiceViewSinksContainer extends SinksContainer {

    public SaleInvoiceViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        boolean isRecurringInvoice = getName().startsWith("recurringinvoice");
        if ("true".equals(Utils.userSettings.get(ACCOUNTING_IS_SETUP))) {
            String invoiceCustomType = null;

            //this code is run when enable invoice custom types
            if (AccountingUtils.get().enableInvoiceCustomTypes() && params.length > 1) {
                invoiceCustomType = params[1];
            }

            addView(new SaleInvoiceSummaryView(id, isRecurringInvoice/*, invoiceCustomType*/));
        }

        addView(new SalesInvoiceView(id, params, isRecurringInvoice));
    }
}
