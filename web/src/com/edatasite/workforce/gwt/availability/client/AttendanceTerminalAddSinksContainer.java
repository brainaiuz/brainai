package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.AddAttendanceTerminalView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class AttendanceTerminalAddSinksContainer extends SinksContainer {
    public AttendanceTerminalAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new AddAttendanceTerminalView());
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {
    }
}
