package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.pricelevel.EditPriceLevelView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.google.gwt.core.client.GWT;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 25, 2011
 * Time: 11:35:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class PriceLevelSinksContainer extends SinksContainer {

    public PriceLevelSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        GWT.log("paramLength  -  " + params[0]);
        if (params.length > 1) {
            addView(new EditPriceLevelView(Integer.valueOf(params[1]), true));
        } else {
            addView(new EditPriceLevelView(Integer.valueOf(params[0]), false));
        }
    }
}
