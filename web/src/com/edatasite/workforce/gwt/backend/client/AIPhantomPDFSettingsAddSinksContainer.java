package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.UploadAIPhantomPdfView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class AIPhantomPDFSettingsAddSinksContainer extends SinksContainer {

    public AIPhantomPDFSettingsAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new UploadAIPhantomPdfView(Integer.parseInt(params[1])));
    }
}
