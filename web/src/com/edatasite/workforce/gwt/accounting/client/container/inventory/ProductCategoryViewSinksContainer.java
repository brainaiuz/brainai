package com.edatasite.workforce.gwt.accounting.client.container.inventory;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.AddEditProductCategoryView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductCategoryCustomFieldListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductsServicesListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 4, 2010
 * Time: 1:28:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCategoryViewSinksContainer extends SinksContainer {
    public ProductCategoryViewSinksContainer(final String name, final String description, final String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(final LinkedList<View> viewList) {

    }

    protected void initViews() {
        this.addView(new AddEditProductCategoryView(this.id));
        this.addView(new ProductsServicesListView(this.id));
        this.addView(new ProductCategoryCustomFieldListView(this.id));
    }
}
