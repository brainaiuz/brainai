package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class ImportPositionSinksContainer extends SinksContainer {

    public ImportPositionSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new ImportPositionView(Integer.valueOf(params[1]), null));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
