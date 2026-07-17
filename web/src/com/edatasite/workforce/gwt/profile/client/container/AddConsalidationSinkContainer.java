package com.edatasite.workforce.gwt.profile.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddCompanyConsolidation;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 08/10/12
 * Time: 17:35
 * To change this template use File | Settings | File Templates.
 */
public class AddConsalidationSinkContainer extends SinksContainer {

    public AddConsalidationSinkContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new AddCompanyConsolidation());
    }
}
