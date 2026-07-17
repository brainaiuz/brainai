package com.edatasite.workforce.gwt.accounting.client.container.inventory;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.AddEditProductCategoryView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 4, 2010
 * Time: 1:34:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddProductCategorySinksContainer extends SinksContainer {
    public AddProductCategorySinksContainer(final String name, final String description, final String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(final LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (this.params.length == 2) {
            this.addView(new AddEditProductCategoryView(true, Integer.valueOf(this.params[1])));
        } else {
            this.addView(new AddEditProductCategoryView(true, null));
        }
    }
}