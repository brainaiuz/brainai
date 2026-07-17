package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.AddEditRotationView;

import java.util.LinkedList;

public class RotationAddSinksContainer extends SinksContainer {
    public RotationAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new AddEditRotationView());
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {
        if (params != null) {
            switch (params.length) {
                case 2:
                    addView(new AddEditRotationView(Integer.parseInt(params[1])));
                    break;
                default:
                    addView(new AddEditRotationView());
            }
        }
    }
}
