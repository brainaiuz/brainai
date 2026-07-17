package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.ExchangeRateView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 09/11/12
 * Time: 18:17
 * To change this template use File | Settings | File Templates.
 */
public class ExchangeRateSinksContainer extends SinksContainer {

    public ExchangeRateSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    public ExchangeRateSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new ExchangeRateView());
    }
}
