package com.edatasite.workforce.gwt.core.client.history;

import com.edatasite.workforce.gwt.core.client.form.DynamicFormIntroSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class IntroductionPageHistoryProcessor implements HistoryProcessor {
    private WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new DynamicFormIntroSinksContainer("introPageadd", wfmStrings.introductionPage(), params);
    }
}
