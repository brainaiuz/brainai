package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.talentprofile.AwardAddView;

import java.util.LinkedList;

/**
 * User: unni
 * Date: Dec 3, 2009
 * Time: 2:29:50 PM
 */
public class AwardAddSinksContainer extends SinksContainer {

    public AwardAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_ADD)) {
            Integer employeeID = params.length >= 2 ? Integer.valueOf(params[1]) : null;
            addView(new AwardAddView(employeeID));
        }
    }
}