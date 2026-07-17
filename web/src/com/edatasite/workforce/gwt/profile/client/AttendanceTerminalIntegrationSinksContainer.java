package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.AttendanceTerminalListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class AttendanceTerminalIntegrationSinksContainer extends SinksContainer {
    public AttendanceTerminalIntegrationSinksContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
    }

    @Override
    protected void initViews() {
        addView(new AttendanceTerminalListView());
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
