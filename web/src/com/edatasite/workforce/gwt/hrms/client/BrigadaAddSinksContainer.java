package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.AddBrigadaView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class BrigadaAddSinksContainer extends SinksContainer {

    public BrigadaAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new AddBrigadaView(params));

    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
