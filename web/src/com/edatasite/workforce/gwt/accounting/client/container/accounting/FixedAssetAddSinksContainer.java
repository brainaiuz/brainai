package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AddEditFixedAssetForm;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/6/11
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixedAssetAddSinksContainer extends SinksContainer{
    public FixedAssetAddSinksContainer(String name, String description) {
        super(name, description);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new AddEditFixedAssetForm());
    }
}
