package com.edatasite.workforce.gwt.task.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.task.client.ui.AddMultiTaskView;

import java.util.LinkedList;

public class MultiTaskAddSinksContainer extends SinksContainer {

    public MultiTaskAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.PM_TASKS_ADD_MULTI)) {
            String projectID = null;
            String workStreamID = null;
            if (params.length > 2) {
                projectID = params[1];
                workStreamID = params[2];
            } else {
                if (params.length > 1) {
                    projectID = params[1];
                }
            }
            if (workStreamID != null && !workStreamID.equals("")) {
                AddMultiTaskView addMultiTaskView = new AddMultiTaskView(projectID, workStreamID);
                addView(addMultiTaskView);
            } else {
                AddMultiTaskView addMultiTaskView = new AddMultiTaskView(projectID);
                addView(addMultiTaskView);
            }
        }
    }
}