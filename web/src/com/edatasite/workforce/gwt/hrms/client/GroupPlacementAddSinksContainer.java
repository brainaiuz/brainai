package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.AddEditGroupPlacementView;

import java.util.LinkedList;

public class GroupPlacementAddSinksContainer extends SinksContainer {
    public GroupPlacementAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new AddEditGroupPlacementView());
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {
        if (params != null) {
            switch (params.length) {
                case 2:
                    addView(new AddEditGroupPlacementView(Integer.parseInt(params[1])));
                    break;
                default:
                    addView(new AddEditGroupPlacementView());
            }
        }
    }
}
