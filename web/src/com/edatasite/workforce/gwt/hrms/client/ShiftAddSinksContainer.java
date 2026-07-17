package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.ShiftEditView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class ShiftAddSinksContainer extends SinksContainer {

    public ShiftAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        if (params != null) {
            switch (params.length) {
                case 2:
                    addView(new ShiftEditView(Integer.parseInt(params[1])));
                    break;
                case 3:
                    addView(new ShiftEditView(String.valueOf(params[1]), true));
                    break;
                case 4:
                    addView(new ShiftEditView(Integer.parseInt(params[1]), String.valueOf(params[2]), true));
                    break;
                default:
                    addView(new ShiftEditView());
            }
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
