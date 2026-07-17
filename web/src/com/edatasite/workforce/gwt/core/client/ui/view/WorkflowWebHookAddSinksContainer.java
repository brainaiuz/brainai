package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class WorkflowWebHookAddSinksContainer extends SinksContainer {
    public WorkflowWebHookAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (id == null && params != null && params.length > 1 && params[1] != null && !"".equals(params[1])) {
            try {
                id = Integer.parseInt(params[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        Integer workflowID = null;
        if (params != null && params.length > 2 && params[2] != null && !"".equals(params[2]) && !"null".equals(params[2])) {
            try {
                workflowID = Integer.parseInt(params[2]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        String formId = null;
        if (params != null && params.length > 3 && params[3] != null && !"".equals(params[3]) && !"null".equals(params[3])) {
            formId = params[3];
        }
        String uuid = null;
        boolean itemTable = false;
        if (params != null && params.length > 4 && params[4] != null && !"".equals(params[4])) {
            uuid = !params[4].equals("null") ? params[4] : null;
            itemTable = !params[4].equals("null");
        }
        addView(new AddWorkflowWebHook(id, workflowID, formId, uuid, itemTable));
    }
}
