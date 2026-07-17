package com.edatasite.workforce.gwt.team.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.team.client.ui.view.AddDepartmentView;

import java.util.LinkedList;

public class DepartmentAddSinksContainer extends SinksContainer {

    public DepartmentAddSinksContainer(String name, String description) {
        super(name, description);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (!Utils.isPM()) {//for now
            AddDepartmentView addTeamView = new AddDepartmentView();
            addView(addTeamView);
        }
    }
}
