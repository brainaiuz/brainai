package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddSipuniSettingView;

import java.util.LinkedList;

public class SipuniSettingAddSinksContainer extends SinksContainer {

    public SipuniSettingAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }


    @Override
    protected void initViews() {
        Integer objectID = null;
        if (params != null && params.length > 1) {
            objectID = Integer.parseInt(params[1]);
        }
        addView(new AddSipuniSettingView(id == null ? objectID : id));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
