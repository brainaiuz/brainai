package com.edatasite.workforce.gwt.accounting.client.container.inventory;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.UnitMeasurementsListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 16, 2010
 * Time: 11:51:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnitMeasurementsListSinksContainer extends SinksContainer {
    public UnitMeasurementsListSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new UnitMeasurementsListView());
    }
}
