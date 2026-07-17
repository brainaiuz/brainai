package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.ImportLogsListView;
import com.edatasite.workforce.gwt.profile.client.ui.MessagesListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.RolePermissionHistoryListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.WorkflowActivitiesListView;

import java.util.LinkedList;

public class SystemLogsSinksContainer extends SinksContainer {

    public SystemLogsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new ImportLogsListView());
        addView(new MessagesListView());
        addView(new MessagesListView(true));
        addView(new WorkflowActivitiesListView());
        if (Utils.hasPermission(PermissionConstants.PERMISSION_LOGS)) {
            addView(new RolePermissionHistoryListView());
        }
    }
}
