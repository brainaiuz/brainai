package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.AddEditOnboardingStepView;

import java.util.LinkedList;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ONBOARDING_STEP_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ONBOARDING_STEP_EDIT;

/**
 * User: User
 * Date: 8/29/12
 * Time: 3:11 PM
 */
public class OnboardingStepAddSinksContainer extends SinksContainer {

    public OnboardingStepAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length == 1) {
            if (Utils.hasPermission(HRMS_ONBOARDING_STEP_ADD)) {
                addView(new AddEditOnboardingStepView());
            }
        } else if (params.length == 2) {
            if (Utils.hasPermission(HRMS_ONBOARDING_STEP_EDIT)) {
                addView(new AddEditOnboardingStepView(Integer.valueOf(params[1])));
            }
        }
    }
}