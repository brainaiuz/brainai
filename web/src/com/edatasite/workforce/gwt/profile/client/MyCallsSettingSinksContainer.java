package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddMyCallsSettingView;

import java.util.LinkedList;

public class MyCallsSettingSinksContainer extends SinksContainer {

    public MyCallsSettingSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        Integer objectID = null;
        if (params != null && params.length > 1) {
            objectID = Integer.parseInt(params[1]);
        }
        addView(new AddMyCallsSettingView(id == null ? objectID : id));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
