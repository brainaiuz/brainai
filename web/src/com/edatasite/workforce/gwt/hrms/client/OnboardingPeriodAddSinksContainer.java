package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.AddEditOnboardingPeriodView;

import java.util.LinkedList;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ONBOARDING_ADD;

/**
 * User: User
 * Date: 8/28/12
 * Time: 12:16 PM
 */
public class OnboardingPeriodAddSinksContainer extends SinksContainer {

    public OnboardingPeriodAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length == 1) {
            if (Utils.hasPermission(HRMS_ONBOARDING_ADD)) {
                addView(new AddEditOnboardingPeriodView());
            }
        } else if (params.length == 2) {
            if (Utils.hasPermission(HRMS_ONBOARDING_ADD)) {
                addView(new AddEditOnboardingPeriodView(Integer.valueOf(params[1])));
            }
        }
    }
}