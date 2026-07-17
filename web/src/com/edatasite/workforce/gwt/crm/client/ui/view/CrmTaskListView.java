package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.task.client.ui.TaskListView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 8/15/11
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmTaskListView extends TaskListView {
    private Integer contactID;
    private Integer accountID;

    public CrmTaskListView(Integer relationID, String relationType, boolean isFromCase) {
        this.relationID = relationID;
        this.relationType = relationType;
        this.isFromCase = isFromCase;
    }

    public CrmTaskListView(Integer relationID, String relationType, Integer contactID, Integer accountID) {
        this.relationID = relationID;
        this.relationType = relationType;
        this.contactID = contactID;
        this.accountID = accountID;
    }

    public CrmTaskListView(Integer relationID, String relationName, String relationType) {
        this.relationID = relationID;
        this.relationName = relationName;
        this.relationType = relationType;
    }

    public ListingFilterParameter getFiterParametrs() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setRelationID(relationID);
        fp.setRelationType(relationType);
        fp.setRelationName(relationName);
        fp.setContactID(contactID);
        fp.setAccountID(accountID);
        fp.setCrmTaskList(true);
        return fp;
    }

    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.CRM_CONTEXT, PermissionConstants.CRM_TASKS_LIST);
    }

    @Override
    protected ListPanelType getPanelType() {
        return ListPanelType.TaskListPanel;
    }

    public String getIconStyle() {
        return "task task-list";
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
}
