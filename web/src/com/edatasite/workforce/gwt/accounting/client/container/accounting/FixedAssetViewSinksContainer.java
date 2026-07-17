package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AddEditFixedAssetForm;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.ViewFixedAssetForm;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/9/11
 * Time: 1:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixedAssetViewSinksContainer extends SinksContainer {

    public FixedAssetViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new ViewFixedAssetForm(id));
        addView(new AddEditFixedAssetForm(id));
    }
}
