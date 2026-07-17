package com.edatasite.workforce.gwt.invoice.client.container.saleinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.PermissionDeniedView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SalesInvoiceView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 24.02.2009
 * Time: 19:16:47
 * To change this template use File | Settings | File Templates.
 */
public class SaleInvoiceAddSinksContainer extends SinksContainer implements Constants {

    public SaleInvoiceAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if ("recurringinvoiceadd".equals(getName())) {
            addView(new SalesInvoiceView(params, RECURRING_INVOICE));
        } else {
            String invoiceType = SALE_INVOICE;
            if (params.length > 1 && AccountingUtils.get().enableInvoiceCustomTypes()) {
                invoiceType = params[1];
            }
            if (params.length == 1 && "saleinvoiceadd".equals(getName()) && !Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_ADD)) {
                addView(new PermissionDeniedView("You do not have permission to add Sales Invoice"));
            } else {
                addView(new SalesInvoiceView(params, invoiceType));
            }
        }
    }
}
