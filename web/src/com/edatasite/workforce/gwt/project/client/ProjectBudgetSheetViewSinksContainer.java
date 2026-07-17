package com.edatasite.workforce.gwt.project.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ui.view.ProjectBudgetSheetView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 8/15/11
 * Time: 5:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectBudgetSheetViewSinksContainer extends SinksContainer {

    public ProjectBudgetSheetViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        ProjectBudgetSheetView projectBudget = new ProjectBudgetSheetView(id);
        addView(projectBudget);
    }
}
