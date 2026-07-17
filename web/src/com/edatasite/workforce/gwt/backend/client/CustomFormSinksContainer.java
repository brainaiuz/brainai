package com.edatasite.workforce.gwt.backend.client;


import com.edatasite.workforce.gwt.backend.client.ui.view.CustomFormsListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 10.01.13
 * Time: 15:58
 * To change this template use File | Settings | File Templates.
 */
public class CustomFormSinksContainer extends SinksContainer {

    public CustomFormSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new CustomFormsListView());
    }
}
