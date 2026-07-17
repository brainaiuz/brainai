package com.edatasite.workforce.gwt.accounting.client.container.inventory;

//import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.LocationsListView;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 16, 2010
 * Time: 8:29:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseLocationListSinksContainer extends SinksContainer {
    public WarehouseLocationListSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
//        addView(new LocationsListView(id));
    }
}
