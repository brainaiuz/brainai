package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.OnboardingStepAddSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * User: User
 * Date: 8/29/12
 * Time: 3:10 PM
 */
public class OnboardingStepHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        String description = hrmsStrings.addOnboardingStep();
        if (params.length == 2 && params[1] != null) {
            description = hrmsStrings.onboardingEditStep();
        }
        return new OnboardingStepAddSinksContainer("onboardingStepadd", description, params);
    }
}