package com.edatasite.workforce.gwt.invoice.client.container.purchaseorder;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.PermissionDeniedView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder.PurchaseOrderView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 18.04.2009
 * Time: 15:18:18
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseOrderAddSinksContainer extends SinksContainer {

    public PurchaseOrderAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params.length == 1 && !(Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PURCHASE_ORDER_ADD : PermissionConstants.ACCOUNTING_PURCHASE_ORDER_ADD))) {
            addView(new PermissionDeniedView("You do not have permission to add Purchase Order"));
        } else {
            addView(new PurchaseOrderView(params));
        }
    }
}