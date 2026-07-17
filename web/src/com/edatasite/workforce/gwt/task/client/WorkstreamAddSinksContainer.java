package com.edatasite.workforce.gwt.task.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.task.client.ui.AddWorkstreamView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 10.11.2008
 * Time: 23:12:36
 * To change this template use File | Settings | File Templates.
 */
public class WorkstreamAddSinksContainer extends SinksContainer {

    public WorkstreamAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        String projectID = "";
        String workStreamID = "";
        if (params.length > 2) {
            projectID = params[1];
            workStreamID = params[2];
        } else {
            if (params.length > 1) {
                projectID = params[1];
            }
        }
        if (workStreamID != null && !"".equals(workStreamID) && projectID != null && !"".equals(projectID)) {
            addView(new AddWorkstreamView(projectID, workStreamID));
        } else if (projectID != null && !"".equals(projectID)) {
            addView(new AddWorkstreamView(projectID));
        } else {
            addView(new AddWorkstreamView());
        }
    }
}
