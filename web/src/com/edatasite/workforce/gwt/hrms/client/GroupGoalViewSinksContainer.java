package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.ui.AddGroupGoalView;

import java.util.LinkedList;

public class GroupGoalViewSinksContainer extends SinksContainer {


    public GroupGoalViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        AddGroupGoalView addGroupGoalView = new AddGroupGoalView("", id);
        addView(addGroupGoalView);
    }
}
