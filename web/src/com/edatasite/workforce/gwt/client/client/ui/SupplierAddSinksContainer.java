package com.edatasite.workforce.gwt.client.client.ui;

import com.edatasite.workforce.gwt.client.client.ui.view.AddSupplierView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 11, 2009
 * Time: 5:32:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupplierAddSinksContainer extends SinksContainer {
    public SupplierAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new AddSupplierView(null, params));
    }
}
