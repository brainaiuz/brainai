package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.AddEditWorkflowEmployee;

import java.util.LinkedList;

/**
 * Created by Azazello on 4/26/16.
 */
public class WorkflowEmployeeSinksContainer extends SinksContainer {

    public WorkflowEmployeeSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        Integer objectID = null;
        Integer workflowID = Integer.valueOf(params[1]);
        if(params.length > 2){
            objectID = Integer.valueOf(params[2]);
        }
        addView(new AddEditWorkflowEmployee(objectID, workflowID));
    }
}
