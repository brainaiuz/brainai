package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.talentprofile.EducationAddView;

import java.util.LinkedList;

/**
 * User: unni
 * Date: Dec 2, 2009
 * Time: 3:50:13 PM
 */
public class EducationAddSinksContainer extends SinksContainer {

    public EducationAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_ADD)) {
            Integer employeeID = params.length >= 2 ? Integer.valueOf(params[1]) : null;
            boolean isFromCandidate = params.length >= 2 ? Boolean.valueOf(params[2]) : null;
            addView(new EducationAddView(employeeID,isFromCandidate));
        }
    }
}