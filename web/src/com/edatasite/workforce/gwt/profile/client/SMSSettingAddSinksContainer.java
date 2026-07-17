package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddSmsSettingView;

import java.util.LinkedList;

/**
 * Created by Azazello on 2/5/16.
 */
public class SMSSettingAddSinksContainer extends SinksContainer {
    public SMSSettingAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        Integer objectID = null;
        if (params != null && params.length > 1) {
            objectID = Integer.parseInt(params[1]);
        }
        addView(new AddSmsSettingView(id == null ? objectID : id));

    }
}
