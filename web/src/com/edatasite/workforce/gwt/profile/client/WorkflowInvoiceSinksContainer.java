package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.AddEditWorkflowInvoice;

import java.util.LinkedList;

/**
 * Created by Azazello on 10/6/16.
 */
public class WorkflowInvoiceSinksContainer extends SinksContainer {
    public WorkflowInvoiceSinksContainer(String name, String description, String[] params) {
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
        addView(new AddEditWorkflowInvoice(objectID, workflowID));
    }
}
