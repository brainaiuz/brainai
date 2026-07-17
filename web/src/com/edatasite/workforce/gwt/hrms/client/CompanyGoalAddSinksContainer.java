package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.CompanyGoalAddEditView2;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 23, 2009
 * Time: 4:55:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyGoalAddSinksContainer extends SinksContainer {
    public CompanyGoalAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_COMPANY_GOALS)) {
            addView(new CompanyGoalAddEditView2(id));
        }

    }
}