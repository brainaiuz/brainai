package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.pricelevel.EditPriceLevelView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 25, 2011
 * Time: 11:36:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class PriceLevelAddSinksContainer extends SinksContainer {
    public PriceLevelAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new EditPriceLevelView());
    }
}
