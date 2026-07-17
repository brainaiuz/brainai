package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class WorkflowWebHookEditSinksContainer extends SinksContainer {

    public WorkflowWebHookEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (id == null && params != null && params.length > 0 && params[0] != null && !params[0].isEmpty()) {
            try {
                id = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        Integer workflowID = null;
        if (params != null && params.length > 1 && params[1] != null && !params[1].isEmpty() && !"null".equals(params[1])) {
            try {
                workflowID = Integer.parseInt(params[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        String formId = null;
        if (params != null && params.length > 2 && params[2] != null && !params[2].isEmpty() && !"null".equals(params[2])) {
            formId = params[2];
        }
        String uuid = null;
        boolean itemTable = false;
        if (params != null && params.length > 3 && params[3] != null && !params[3].isEmpty()) {
            uuid = !params[3].equals("null") ? params[3] : null;
            itemTable = !params[3].equals("null");
        }
        addView(new AddWorkflowWebHook(id, workflowID, formId, uuid, itemTable));
    }
}
