package com.edatasite.workforce.gwt.availability.client.history;

import com.edatasite.workforce.gwt.availability.client.ui.AddBenefitRequestSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * Created by Djuraev on 8/7/15.
 */
public class BenefitRequestHistoryProcessor implements HistoryProcessor {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new AddBenefitRequestSinksContainer(containerName + strings[0], hrmsStrings.editBenefitRequest(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AddBenefitRequestSinksContainer("benefitRequestadd", hrmsStrings.addBenefitRequest(), params);
    }
}
