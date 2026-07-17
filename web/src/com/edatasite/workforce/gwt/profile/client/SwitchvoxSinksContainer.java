package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddSwitchvoxView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 16.05.12
 * Time: 18:13
 * To change this template use File | Settings | File Templates.
 */
public class SwitchvoxSinksContainer extends SinksContainer {

    public SwitchvoxSinksContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new AddSwitchvoxView());
    }
}