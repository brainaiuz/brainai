package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: User
 * Date: 9/1/12
 * Time: 11:38 AM
 */
public class OnboardingCheckListSinksContainer extends SinksContainer {

    public OnboardingCheckListSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length == 1) {
            if (Utils.hasPermission(PermissionConstants.HRMS_ONBOARDING_CHECKLIST_EDIT) || Utils.hasPermission(PermissionConstants.HRMS_ONBOARDING_CHECKLIST_VIEW)){
                addView(new AddEditOnboardingCheckView());
            }
        } else if (params.length == 2) {
            if (Utils.hasPermission(PermissionConstants.HRMS_ONBOARDING_CHECKLIST_EDIT) || Utils.hasPermission(PermissionConstants.HRMS_ONBOARDING_CHECKLIST_VIEW)){
                addView(new AddEditOnboardingCheckView(Integer.valueOf(params[1])));
            }
        }
    }
}