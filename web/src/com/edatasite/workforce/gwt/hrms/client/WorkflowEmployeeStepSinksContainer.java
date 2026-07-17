package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.AddEditEmployeeStepView;

import java.util.LinkedList;

/**
 * Created by Azazello on 7/19/15.
 */
public class WorkflowEmployeeStepSinksContainer extends SinksContainer {
    public WorkflowEmployeeStepSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        Integer objectID = null;
        Integer stepID = null;
        String formID = null;
        Integer workflowID = null;
        if (params.length > 5) {
            objectID = Integer.parseInt(params[1]);
            stepID = Integer.parseInt(params[2]);
            formID = params[3];
            workflowID = Integer.valueOf(params[5]);
        } else if (params.length > 4) {
            stepID = Integer.parseInt(params[1]);
            formID = params[2];
            workflowID = Integer.valueOf(params[4]);
        }
        addView(new AddEditEmployeeStepView(objectID, stepID, formID, workflowID));
    }
}
