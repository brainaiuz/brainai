package com.edatasite.workforce.gwt.issue.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.issue.client.ui.AddIssueView;

import java.util.LinkedList;


public class IssueAddSinksContainer extends SinksContainer {

    public IssueAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.PM_ISSUE_ADD)) {
            Integer fromID;
            String fromType;
            String fromName;
            if (params != null && params.length > 3) {
                fromID = Integer.valueOf(params[1]);
                fromType = params[2];
                fromName = params[3];

                super.addView(new AddIssueView(fromID, fromType, fromName));
            } else {
                super.addView(new AddIssueView());
            }
        }
    }
}
