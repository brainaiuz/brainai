package com.edatasite.workforce.gwt.location.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.location.client.ui.AddLocationView;

import java.util.LinkedList;

/**
 * User: Dilshod
 * Date: 01.12.2009
 * Time: 17:58:36
 */
public class LocationAddSinksContainer extends SinksContainer {

    public LocationAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length > 1) {
            addView(new AddLocationView(params.length > 1 ? Integer.parseInt(params[1]) : null));
        } else {
            addView(new AddLocationView());
        }
    }
}