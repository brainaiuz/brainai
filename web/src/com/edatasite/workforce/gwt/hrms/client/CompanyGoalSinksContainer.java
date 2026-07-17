package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.CompanyEditGoalViewFrom;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 23, 2009
 * Time: 4:33:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyGoalSinksContainer extends SinksContainer {
    public CompanyGoalSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_COMPANY_GOAL_SUMMARY)) {
            super.addView(new CompanyGoalViewForm(id));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_COMPANY_GOAL)) {
            super.addView(new CompanyEditGoalViewFrom(id));
        }

    }

}