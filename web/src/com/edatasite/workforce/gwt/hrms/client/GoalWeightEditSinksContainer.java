package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.GoalWeightEditView;

import java.util.LinkedList;

/**
 * User: unni
 * Date: Oct 26, 2009
 * Time: 8:53:51 PM
 */
public class GoalWeightEditSinksContainer extends SinksContainer {

    public GoalWeightEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_GOAL_WEIGHTS)) {
            super.addView(new GoalWeightEditView(id));
        }
    }
}