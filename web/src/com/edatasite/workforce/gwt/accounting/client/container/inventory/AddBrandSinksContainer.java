package com.edatasite.workforce.gwt.accounting.client.container.inventory;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.AddBrandView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 17, 2010
 * Time: 3:37:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddBrandSinksContainer extends SinksContainer {
    public AddBrandSinksContainer(String name, String description) {
        super(name, description);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new AddBrandView());
    }
}