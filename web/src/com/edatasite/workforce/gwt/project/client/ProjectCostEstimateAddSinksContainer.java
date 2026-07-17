package com.edatasite.workforce.gwt.project.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ui.view.projectcost.AddProjectEstimateCostView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 29.04.2010
 * Time: 12:35:23
 * To change this template use File | Settings | File Templates.
 */
public class ProjectCostEstimateAddSinksContainer extends SinksContainer {

    public ProjectCostEstimateAddSinksContainer(String name, String description) {
        super(name, description);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        AddProjectEstimateCostView addProjectViewEstimate = new AddProjectEstimateCostView();
        addView(addProjectViewEstimate);
    }


}
