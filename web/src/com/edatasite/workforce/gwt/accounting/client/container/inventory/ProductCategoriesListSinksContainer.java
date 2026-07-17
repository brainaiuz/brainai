package com.edatasite.workforce.gwt.accounting.client.container.inventory;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductCategoriesListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 3, 2010
 * Time: 8:01:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCategoriesListSinksContainer extends SinksContainer {
    public ProductCategoriesListSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new ProductCategoriesListView());
    }
}
