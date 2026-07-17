package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.ui.AddGroupGoalView;

import java.util.LinkedList;

public class GroupGoalAddSinksContainer extends SinksContainer {


    public GroupGoalAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        Integer objectId = null;
        if (params.length > 1) {
            objectId = Integer.valueOf(params[1]);
        }
        AddGroupGoalView addGroupGoalView = new AddGroupGoalView(objectId);
        addView(addGroupGoalView);
    }
}
