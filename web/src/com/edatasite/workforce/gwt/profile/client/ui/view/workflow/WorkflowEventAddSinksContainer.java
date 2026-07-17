package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 28-Feb-2014
 * Time: 18:23:07
 * To change this template use File | Settings | File Templates.
 */
public class WorkflowEventAddSinksContainer extends SinksContainer {

    public WorkflowEventAddSinksContainer(String name, String description, String[] params) {
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
        if (params != null && params.length > 2 && params[2] != null && !"".equals(params[2])) {
            try {
                workflowID = Integer.parseInt(params[2]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        boolean isCallLog = false;
        if (params != null && params.length > 3) {
            isCallLog = params[3].equals("call");
        }
        addView(new AddWorkflowEvent(id, workflowID, isCallLog));
    }
}
