package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.ChangeContractPricesView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 09.01.14
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
public class ChangeContractPriceSinksContainer extends SinksContainer {

    public ChangeContractPriceSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new ChangeContractPricesView(Integer.parseInt(params[0])));
    }
}
