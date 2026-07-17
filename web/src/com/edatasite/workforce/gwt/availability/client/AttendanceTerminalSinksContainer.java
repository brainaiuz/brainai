package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.AttendanceTerminalSummaryView;
import com.edatasite.workforce.gwt.availability.client.ui.view.EditAttendanceTerminalView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class AttendanceTerminalSinksContainer extends SinksContainer {
    public AttendanceTerminalSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        if (params.length > 1 && "edit".equals(params[0])) {
            addView(new EditAttendanceTerminalView(Integer.parseInt(params[1])));
        } else {
            addView(new AttendanceTerminalSummaryView(id));
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {
    }
}
