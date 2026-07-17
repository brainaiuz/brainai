package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.CustomisedPhantomPdfSettingsView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 04.10.2018 15:45
 */
public class CustomisedPhantomPDFSettingsAddSinksContainer extends SinksContainer {

    public CustomisedPhantomPDFSettingsAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new CustomisedPhantomPdfSettingsView(Integer.parseInt(params[1])));
    }
}
