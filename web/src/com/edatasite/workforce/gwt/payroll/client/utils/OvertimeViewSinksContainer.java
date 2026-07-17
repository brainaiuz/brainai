package com.edatasite.workforce.gwt.payroll.client.utils;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.OvertimeAddEditView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.OvertimeSummaryView;

import java.util.LinkedList;

public class OvertimeViewSinksContainer extends SinksContainer {
    public OvertimeViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        if (params != null && params.length > 1) {
            if ("edit".equalsIgnoreCase(params[0]) && params.length > 2) {
                addView(new OvertimeAddEditView(params[1], Integer.parseInt(params[2])));
            } else {
                addView(new OvertimeSummaryView(params[1], Integer.parseInt(params[0])));
            }
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
