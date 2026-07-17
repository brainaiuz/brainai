package com.edatasite.workforce.gwt.payroll.client.utils;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.OvertimeAddEditView;

import java.util.LinkedList;

public class OvertimeAddSinksContainer extends SinksContainer {
    public OvertimeAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        if (params != null) {
            switch (params.length) {
                case 2:
                    addView(new OvertimeAddEditView(params[1]));
                    break;
                case 3:
                    addView(new OvertimeAddEditView(params[1], Integer.parseInt(params[2])));
                    break;
                default:
                    addView(new OvertimeAddEditView());
            }
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {
    }
}
