package com.edatasite.workforce.gwt.project.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ui.view.projectcost.AddProjectActualCostView;

import java.util.LinkedList;

/**
 * User: Dilsh0d
 * Date: 19-May-2010
 * Time: 15:09:37
 */
public class ProjectCostActualAddSinksContainer extends SinksContainer {
    public ProjectCostActualAddSinksContainer(String name, String description) {
        super(name, description);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        AddProjectActualCostView addProjectViewActual = new AddProjectActualCostView();
        addView(addProjectViewActual);
    }
}
