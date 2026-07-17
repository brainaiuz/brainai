package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.AttendanceReportView2;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class AttendanceReportViewSinksContainer extends SinksContainer {
    public AttendanceReportViewSinksContainer(String name, String description, String[] param) {
        super(name, description, param);
    }

    @Override
    protected void initViews() {
        addView(new AttendanceReportView2(params[1], params[2]));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
