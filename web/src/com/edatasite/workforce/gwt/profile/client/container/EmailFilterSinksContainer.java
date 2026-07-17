package com.edatasite.workforce.gwt.profile.client.container;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 12/1/11
 * Time: 10:19 AM
 * To change this template use File | Settings | File Templates.
 */

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.AddEmailChildFilter;
import com.edatasite.workforce.gwt.profile.client.ui.AddEmailParentFilter;

import java.util.LinkedList;

public class EmailFilterSinksContainer extends SinksContainer {

    public EmailFilterSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params.length > 1) {
            addView(new AddEmailParentFilter(id));
        } else {
            addView(new AddEmailChildFilter(id));
        }
    }
}