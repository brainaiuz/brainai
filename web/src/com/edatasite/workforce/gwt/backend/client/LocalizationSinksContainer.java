package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.EditLocalizationView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class LocalizationSinksContainer extends SinksContainer {

    public LocalizationSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        int objectId = 0;
        String property = "";
        if (params.length > 1 && params[1].substring(0, 1).matches("[0-9]")) {
            objectId = Integer.parseInt(params[1]);
        } else {
            property = params[1];
        }
        if (objectId == 0) {
            addView(new EditLocalizationView(objectId, property));
        } else {
            addView(new EditLocalizationView(objectId));
        }

    }
}