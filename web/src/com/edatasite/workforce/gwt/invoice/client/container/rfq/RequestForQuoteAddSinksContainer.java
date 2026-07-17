package com.edatasite.workforce.gwt.invoice.client.container.rfq;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfq.RequestForQuoteView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/27/12
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestForQuoteAddSinksContainer extends SinksContainer{

    public RequestForQuoteAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new RequestForQuoteView(params));
    }
}
