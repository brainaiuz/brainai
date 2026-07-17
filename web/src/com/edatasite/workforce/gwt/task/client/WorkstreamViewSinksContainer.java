package com.edatasite.workforce.gwt.task.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.task.client.ui.view.WorkstreamEditView;
import com.edatasite.workforce.gwt.task.client.ui.view.WorkstreamSummaryView;

import java.util.LinkedList;

/**
 * User: Anvar Akramov
 * Date: 21.11.2008
 * Time: 18:59:46
 */
public class WorkstreamViewSinksContainer extends SinksContainer {

    public WorkstreamViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        super.addView(new WorkstreamSummaryView(id));
        if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(PM)) {
            addView(new WorkstreamEditView(id));
        }
    }
}