package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;


/**
 * User : Akhror
 * Date : 13.03.2025
 */
public class PublicWebHookViewSinksContainer extends SinksContainer {
    public PublicWebHookViewSinksContainer(String name, String description, String[] params) {
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
                e.fillInStackTrace();
            }
        }
        addView(new WorkflowWebHookSummaryView(id));
    }
}
