package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.OnboardingPeriodAddSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * User: User
 * Date: 8/28/12
 * Time: 12:14 PM
 */
public class OnboardingPeriodHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        String description = hrmsStrings.addOnboardingPeriod();
        if (params.length == 2 && params[1] != null) {
            description = hrmsStrings.onboardingEditPeriod();
        }
        return new OnboardingPeriodAddSinksContainer("onboardingPeriodadd", description, params);
    }
}