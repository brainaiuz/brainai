package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WebHookResponseListView;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeListView;
import com.edatasite.workforce.gwt.hrms.client.ui.PositionView;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.VacancyListView;

import java.util.LinkedList;


public class PositionSummaryViewSinksContainer extends SinksContainer {

    public PositionSummaryViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_POSITION_SUMMARRY)) {
            super.addView(new PositionView(id));
        }

        if (id != null) {
            addView(new EmployeeListView(id, "true"));
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_VACANCY_LIST_VIEW)) {
            addView(new VacancyListView(id, true));
        }

        if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)) {
            addView(new WebHookResponseListView(this.id, RelationItem.TYPE_POSITION));
        }

    }
}