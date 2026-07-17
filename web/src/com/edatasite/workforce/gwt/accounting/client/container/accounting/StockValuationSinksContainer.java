package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewStockValuationView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class StockValuationSinksContainer extends SinksContainer {

    public StockValuationSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {

        Integer warehouseID = null;
        Integer grnGdnID = null;
        String productType = null;
        if (params != null && params.length > 1) {
            try {
                productType = params[1];
            } catch (NumberFormatException e) {
                productType = null;
                e.printStackTrace();
            }
            if (params.length > 2) {
                try {
                    warehouseID = Integer.valueOf(params[2]);
                } catch (NumberFormatException e) {
                    warehouseID = null;
                    e.printStackTrace();
                }
            }
            if (params.length > 3) {
                try {
                    grnGdnID = Integer.valueOf(params[3]);
                } catch (NumberFormatException e) {
                    grnGdnID = null;
                    e.printStackTrace();
                }
            }
        }

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_VALUATION) && (productType == null || (!AccountingConstants.SERVICE_STR.equals(productType) && !AccountingConstants.NON_INVENTORY_ITEM_STR.equals(productType) && !AccountingConstants.OTHER_CHARGE_STR.equals(productType)))) {
            addView(new NewStockValuationView(id, warehouseID, true, grnGdnID));
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
