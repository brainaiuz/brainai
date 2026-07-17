package com.edatasite.workforce.gwt.invoice.client.container.rfp;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfp.RequestForPurchaseSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfp.RequestForPurchaseView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Murad
 * Date: 4/9/13
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestForPurchaseViewSinksContainer extends SinksContainer {
    public RequestForPurchaseViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new RequestForPurchaseSummaryView(id));
        addView(new RequestForPurchaseView(id));
    }
}
