package com.edatasite.workforce.gwt.invoice.client.container.salequote;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.PermissionDeniedView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SalesQuoteView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 06.03.2009
 * Time: 15:56:50
 * To change this template use File | Settings | File Templates.
 */
public class SaleQuoteAddSinksContainer extends SinksContainer {

    public SaleQuoteAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        Integer fromId = null;
        String from = null;
        if (params.length >= 3) {
            from = params[1];
            try {
                fromId = Integer.valueOf(params[2]);
            } catch (NumberFormatException e) {
//                e.printStackTrace();
            }
        }
        if (getName() != null && getName().equals("saleorderadd")) {
            addView(params.length >= 4 ? new SalesQuoteView(true, params) : new SalesQuoteView(true, fromId, from));
        } else if (params.length >= 3 && "fromProductList".equals(params[1])) {
            addView(new SalesQuoteView(params));
        } else {
            if (params.length == 1 && getName().equals("salequoteadd") && !Utils.hasPermission(Utils.isCRM() ? PermissionConstants.CRM_SALES_QUOTE_ADD : PermissionConstants.ACCOUNTING_SALES_QUOTE_ADD)) {
                addView(new PermissionDeniedView("You do not have permission to add Sales Quote"));
            } else {
                addView(params.length >= 4 ? new SalesQuoteView(params) : new SalesQuoteView(fromId, from));
            }
        }
    }
}
