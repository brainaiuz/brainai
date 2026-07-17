package com.edatasite.workforce.gwt.task.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WebHookResponseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.CaseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventListView;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.edatasite.workforce.gwt.issue.client.ui.IssueListView;
import com.edatasite.workforce.gwt.messagecenter.client.view.EmailListView;
import com.edatasite.workforce.gwt.task.client.ui.TaskLogHistoryListView;
import com.edatasite.workforce.gwt.task.client.ui.view.TaskEditView;
import com.edatasite.workforce.gwt.task.client.ui.view.TaskSummaryView;
import com.edatasite.workforce.gwt.task.client.ui.view.TaskTimeEntriesView;
import com.google.gwt.user.client.Command;

import java.util.LinkedList;

public class TaskViewSinksContainer extends SinksContainer {

    public TaskViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    protected void checkForAccess(Command grantAccess, Command denyAccess) {
        renderSinksContainer();
    }

    public void initViews() {
        boolean hasAccessToChange = params.length <= 1 || Boolean.parseBoolean(params[1]);

        super.addView(new TaskSummaryView(id));

        if (Utils.hasPermission(Utils.isCRM() ? PermissionConstants.CRM_TASKS_EDIT : PermissionConstants.PM_TASKS_EDIT) && hasAccessToChange) {
            super.addView(new TaskEditView(id));
        }

        if (Utils.hasPermission(Utils.isCRM() ? PermissionConstants.CRM_TASKS_TIMER : PermissionConstants.PM_TASKS_TIMER)) {
            super.addView(new TaskTimeEntriesView(id));
        }

        if (Utils.hasPermission(Utils.isCRM() ? PermissionConstants.CRM_TASKS_DOCUMENTS : PermissionConstants.PM_TASKS_DOCUMENTS)) {
            super.addView(new DocumentsView(F_TASK, this.id, true, true));
        }
        if (Utils.hasPermission(Utils.isCRM() ? PermissionConstants.CRM_TASKS_ISSUE : PermissionConstants.PM_TASKS_ISSUE)) {
            addView(new IssueListView(id, RelationItem.TYPE_TASK));
        }

        if (Utils.hasPermission(PermissionConstants.CRM_CASES_LIST)) {
            addView(new CaseListView(id, RelationItem.TYPE_TASK));
        }
        if (Utils.hasPermission(PermissionConstants.PM_TASKS_EMAILS)) {
            EmailListView emailListView = new EmailListView(RelationItem.TYPE_TASK, this.id);
            addView(emailListView);
        }
        if (Utils.hasPermission(PermissionConstants.CRM_ACTIVITIES_LIST)) {
            addView(new EventListView(null, this.id, RelationItem.TYPE_TASK));
        }
        if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)) {
            addView(new WebHookResponseListView(this.id, RelationItem.TYPE_TASK));
        }
        if (id != null) {
            addDynamicView(CustomFieldLookUpTypeEnum.TASK, id);
        }
        addView(new TaskLogHistoryListView(TASK_UPDATES_LIST, this.id));
    }

    @Override
    public void activate(View view) {
        super.activate(view);
        if (view instanceof DocumentsView) {
            DocumentsView.setSingleton((DocumentsView) view);
        }
    }

    @Override
    public void reInit() {
        super.reInit();
        if (getWorkarea() != null && getWorkarea().getCurrentView() != null && getWorkarea().getCurrentView() instanceof DocumentsView) {
            DocumentsView.setSingleton((DocumentsView) getWorkarea().getCurrentView());
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
