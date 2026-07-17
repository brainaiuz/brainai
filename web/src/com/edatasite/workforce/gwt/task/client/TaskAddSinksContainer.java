package com.edatasite.workforce.gwt.task.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.task.client.ui.AddTaskView;

import java.util.ArrayList;
import java.util.LinkedList;

public class TaskAddSinksContainer extends SinksContainer {

    public TaskAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        String projectID = null;
        String workStreamID = null;
        String forWhat = null;
        AddTaskView addTaskView = null;
        if (params.length > 1 && (CrmConstants.CRM_TASK.equals(params[1]) || CrmConstants.CRM_TASK_SHORTEN.equals(params[1]) || Constants.WORKFLOW.equals(params[1]))) {
            forWhat = params[1];
            String toID = params.length > 2 ? params[2] : null;
            String toType = params.length > 3 ? params[3] : null;
            String toName = params.length > 4 ? params[4] : null;
            if (Constants.WORKFLOW.equals(params[1])) {
                ArrayList<RelationItem> relations = new ArrayList<>();
                if (toID != null && toID.matches(Constants.REGEX_INTEGER) && toType != null) {
                    relations.add(new RelationItem(null, Integer.valueOf(toID), toType, toName, null, RelationItem.TYPE_TASK, null));
                }
                addTaskView = new AddTaskView(forWhat, null, toID, null, false, relations);
            } else {
                String copiedTaskName = params.length > 5 ? params[5] : null;
                String copiedTaskDescription = params.length > 6 ? params[6] : null;
                ArrayList<RelationItem> relations = new ArrayList<>();
                Integer contactID = null;
                Integer accountID = null;
                String contactName = null;
                String accountName = null;
                if (toType != null && toID != null && toID.matches(Constants.REGEX_INTEGER)) {
                    if (RelationItem.TYPE_CRM_ACCOUNT.equals(toType)) {
                        relations.add(new RelationItem(null, Integer.valueOf(toID), toType, toName, null, RelationItem.TYPE_TASK, null));
                    } else if (RelationItem.TYPE_CONTACT.equals(toType) || RelationItem.TYPE_LEAD.equals(toType)) {
                        accountID = params.length > 9 && !"null".equals(params[9]) ? Integer.valueOf(params[9]) : null;
                        accountName = params.length > 10 && !"null".equals(params[10]) ? params[10] : null;
                        relations.add(new RelationItem(null, Integer.valueOf(toID), toType, toName, null, RelationItem.TYPE_TASK, null));
                        if (accountID != null) {
                            relations.add(new RelationItem(null, accountID, RelationItem.TYPE_CRM_ACCOUNT, accountName, null, RelationItem.TYPE_TASK, null));
                        }
                    } else {
                        contactID = params.length > 7 && !"null".equals(params[7]) ? Integer.valueOf(params[7]) : null;
                        contactName = params.length > 8 && !"null".equals(params[8]) ? params[8] : null;
                        accountID = params.length > 9 && !"null".equals(params[9]) ? Integer.valueOf(params[9]) : null;
                        accountName = params.length > 10 && !"null".equals(params[10]) ? params[10] : null;
                        relations.add(new RelationItem(null, Integer.valueOf(toID), toType, toName, null, RelationItem.TYPE_TASK, null));
                        if (contactID != null) {
                            relations.add(new RelationItem(null, contactID, RelationItem.TYPE_CONTACT, contactName, null, RelationItem.TYPE_TASK, null));
                        }
                        if (accountID != null) {
                            relations.add(new RelationItem(null, accountID, RelationItem.TYPE_CRM_ACCOUNT, accountName, null, RelationItem.TYPE_TASK, null));
                        }
                    }
                }
                if (!(toID != null && toID.matches(Constants.REGEX_INTEGER))) {
                    relations = new ArrayList<>();
                }
                addTaskView = new AddTaskView(forWhat, copiedTaskName, toID, copiedTaskDescription, !CrmConstants.CRM_TASK_SHORTEN.equals(forWhat), relations);
            }
        } else if (params.length > 2) {
            if ("copytask".equals(params[1])) {
                addView(new AddTaskView(Integer.valueOf(params[2])));
                return;
            } else {
                projectID = params[1];
                workStreamID = params[2];
            }
        } else if (params.length > 1) {
            projectID = params[1];
        }
        if (forWhat == null) {
            if (!Utils.isNullOrEmpty(workStreamID)) {
                addTaskView = new AddTaskView(projectID, workStreamID);
            } else {
                addTaskView = new AddTaskView(projectID);
            }
        }
        if (addTaskView != null) {
            addView(addTaskView);
        }
    }
}
