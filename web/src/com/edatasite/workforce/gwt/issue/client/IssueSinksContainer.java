package com.edatasite.workforce.gwt.issue.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.CompanyConstants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.issue.client.ui.EditIssueForm;
import com.edatasite.workforce.gwt.issue.client.ui.IssueSummaryView;
import com.google.gwt.user.client.Command;

import java.util.LinkedList;

public class IssueSinksContainer extends SinksContainer {

    public IssueSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    protected void checkForAccess(Command grantAccess, Command denyAccess) {
        renderSinksContainer();
        return;
//        if (Utils.hasRole(ADMIN)) {
//            grantAccess.execute();
//            return;
//        }
//        IssueService.App.get().getPermissions(id, PermissionConstants.PM_CONTEXT, new AbstractAsyncCallback<HashSet<String>>() {
//            @Override
//            public void failure(Throwable throwable) {
//                denyAccess.execute();
//            }
//
//            @Override
//            public void success(HashSet<String> permissions) {
//                Utils.setUserPermissions(permissions);
//                grantAccess.execute();
//                showPrepared();
//                MainLayout.get().addDynamicContainer(IssueSinksContainer.this);
//            }
//        });
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }


    protected void initViews() {
        super.addView(new IssueSummaryView(id));
        if (Utils.hasRole(ADMIN) || Utils.hasPermission(PermissionConstants.PM_ISSUE_EDIT) && !CompanyConstants.C28492.equals(Utils.getEncryptedCompanyID())) {
            super.addView(new EditIssueForm(id));
        }
//        super.addView(new IssueNotesView(id));
    }
}