package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddReferenceView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 7/24/11
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReferenceAddSinksContainer extends SinksContainer {

    public ReferenceAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (id == null && params != null && params.length > 1) {
            id = params[1].equals("") ? null : Integer.valueOf(params[1]);
        }
        addView(new AddReferenceView(id));
    }
}
