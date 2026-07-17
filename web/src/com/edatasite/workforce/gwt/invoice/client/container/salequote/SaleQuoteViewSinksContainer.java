package com.edatasite.workforce.gwt.invoice.client.container.salequote;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleQuoteSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SalesQuoteView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 06.03.2009
 * Time: 16:10:54
 * To change this template use File | Settings | File Templates.
 */
public class SaleQuoteViewSinksContainer extends SinksContainer {

    public SaleQuoteViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (getName() != null && getName().contains("saleorder")) {
            if (params.length == 3 && "CONVERT".equals(params[0])) {
                addView(new SalesQuoteView(true, params));
            } else {
                addView(new SaleQuoteSummaryView(id, true));
                addView(new SalesQuoteView(id, true));
            }
        } else {
            if (params.length == 3 && "CONVERT".equals(params[0])) {
                addView(new SalesQuoteView(params));
            } else {
                addView(new SaleQuoteSummaryView(id));
                addView(new SalesQuoteView(id, false));
            }
        }
    }
}
