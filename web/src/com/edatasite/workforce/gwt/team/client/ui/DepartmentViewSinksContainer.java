package com.edatasite.workforce.gwt.team.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WebHookResponseListView;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeListView;
import com.edatasite.workforce.gwt.team.client.ui.view.AddDepartmentView;
import com.edatasite.workforce.gwt.team.client.ui.view.DepartmentView;

import java.util.LinkedList;

public class DepartmentViewSinksContainer extends SinksContainer {

    public DepartmentViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }


    @Override
    protected void initViews(LinkedList<View> viewList) {

    }


    protected void initViews() {
        addView(new DepartmentView(id));

        if (Utils.hasRole(ADMIN) || Utils.hasPermission(PermissionConstants.HRMS_EDIT_DEPARTMENT)) {
            addView(new AddDepartmentView(id));
        }

        if (id != null) {
            addView(new EmployeeListView(id));
        }

//        if (Utils.hasPermission(PermissionConstants.HRMS_POSITION_LIST)) {
//            addView(new PositionListView(id));
//        }

        if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)) {
            addView(new WebHookResponseListView(this.id, RelationItem.TYPE_DEPARTMENT));
        }

    }

}
