package com.edatasite.workforce.gwt.invoice.client.container.rfp;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfp.RequestForPurchaseView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Murad
 * Date: 4/9/13
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestForPurchaseAddSinksContainer extends SinksContainer {

    public RequestForPurchaseAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new RequestForPurchaseView(params));
    }
}
