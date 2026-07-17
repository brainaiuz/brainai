package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.AwardAddSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.AwardViewSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * User: unni
 * Date: Dec 3, 2009
 * Time: 2:33:47 PM
 */
public class AwardHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new AwardViewSinksContainer(containerName + strings[0], hrmsStrings.awardView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AwardAddSinksContainer("awardadd", hrmsStrings.addAward(), params);
    }
}