package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.ui.OnboardingCheckListSinksContainer;

/**
 * User: User
 * Date: 9/1/12
 * Time: 11:37 AM
 */
public class OnboardingCheckListHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new OnboardingCheckListSinksContainer("onboardingCheckadd", hrmsStrings.onboardingChecklist(), params);
    }
}