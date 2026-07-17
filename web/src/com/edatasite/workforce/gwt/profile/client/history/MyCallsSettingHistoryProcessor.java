package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.MyCallsSettingSinksContainer;

public class MyCallsSettingHistoryProcessor implements HistoryProcessor {
       private WfmStrings wfmStrings = WfmStrings.App.get();


    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
     return new MyCallsSettingSinksContainer("myCallsSettingadd", params.length > 1 ? wfmStrings.edit() : wfmStrings.add(), params);

    }
}
