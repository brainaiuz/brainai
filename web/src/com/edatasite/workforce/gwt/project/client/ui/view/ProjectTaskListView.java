package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.task.client.ui.TaskListView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by IntelliJ IDEA.
 * User: java
 * Date: 13.03.2009
 * Time: 12:32:21
 * To change this template use File | Settings | File Templates.
 */
public class ProjectTaskListView extends TaskListView {
    private Integer projectID;

    public ProjectTaskListView(Integer projectID, boolean hasAccessToChange) {
        this.projectID = projectID;
        this.hasAccessToChange = hasAccessToChange;
    }

    public ListingFilterParameter getFiterParametrs() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setProjectId(projectID);
        fp.setCrmTaskList(isCrmTaskListView());
        return fp;
    }

    @Override
    protected Integer getTaskParentId() {
        return projectID;
    }

    @Override
    protected ListPanelType getPanelType() {
        return ListPanelType.TaskListPanel;
    }

    public String getIconStyle() {
        return "bgMark pm-new-summary";
    }

    /*public AbstractImagePrototype getIconImage() {
        return ProjectViewImageBundles.App.get().projectTaskList();
    }*/

    public boolean isCrmTaskListView() {
        return false;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = getFiterParametrs();
        fp.setLimit(1);
        if (parentId != null) {
            initTaskList(fp, null, container);
            onInitialize();
            clear();
        }
    }
}
