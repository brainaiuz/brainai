package com.edatasite.workforce.gwt.invoice.client.container.purchaseinvoice;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.PermissionDeniedView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice.PurchaseInvoiceView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 06.03.2009
 * Time: 15:54:00
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseInvoiceAddSinksContainer extends SinksContainer {

    public PurchaseInvoiceAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if ("recurringbilladd".equals(getName())) {
            addView(new PurchaseInvoiceView(params, true));
        } else {
            if (params.length == 1 && !(Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PURCHASE_INVOICE_ADD : PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_ADD))) {
                addView(new PermissionDeniedView("You do not have permission to add Purchase Invoice"));
            } else {
                addView(new PurchaseInvoiceView(params));
            }

        }
    }
}
