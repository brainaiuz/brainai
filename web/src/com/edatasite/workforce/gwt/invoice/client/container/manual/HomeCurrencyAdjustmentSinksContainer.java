package com.edatasite.workforce.gwt.invoice.client.container.manual;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.manual.CurrencyAdjustmentView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/3/12
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class HomeCurrencyAdjustmentSinksContainer extends SinksContainer{

    public HomeCurrencyAdjustmentSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new CurrencyAdjustmentView());
    }
}
