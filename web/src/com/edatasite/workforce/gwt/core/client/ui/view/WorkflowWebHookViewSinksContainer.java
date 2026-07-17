package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;


/**
 * User : Akhror
 * Date : 10.01.2022
 */
public class WorkflowWebHookViewSinksContainer extends SinksContainer {
    public WorkflowWebHookViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (id == null && params != null && params.length > 0 && params[0] != null && !"".equals(params[0])) {
            try {
                id = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        String formId = null;
        if (params != null && params.length > 2 && params[2] != null && !"".equals(params[2]) && !"null".equals(params[2])) {
            formId = params[2];
        }
        addView(new WorkflowWebHookSummaryView(id,formId));
        addView(new WebHookResponseListView(id));
    }
}
