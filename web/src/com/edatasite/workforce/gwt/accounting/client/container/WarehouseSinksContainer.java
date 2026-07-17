package com.edatasite.workforce.gwt.accounting.client.container;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.StockTransferListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.WarehousesListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.report.StockAdjustmentsListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 6/1/12
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseSinksContainer extends SinksContainer implements PermissionConstants {

    public WarehouseSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(ACCOUNTING_WAREHOUSE_LIST)) {
            addView(new WarehousesListView());
        }

        if (Utils.hasPermission(ACCOUNTING_STOCK_ADJUSTMENT_LIST)) {
            addView(new StockAdjustmentsListView());
        }
        if (Utils.hasPermission(ACCOUNTING_STOCK_TRANSFER_LIST)) {
            addView(new StockTransferListView());
        }
    }
}
