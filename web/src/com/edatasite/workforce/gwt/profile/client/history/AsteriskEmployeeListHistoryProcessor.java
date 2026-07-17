package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.container.AsteriskEmployeeListSinksContainer;

/**
 * Created by Anvar Akramov on 7/4/2020.
 */
public class AsteriskEmployeeListHistoryProcessor implements HistoryProcessor {
    private WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new AsteriskEmployeeListSinksContainer(Constants.ASTERISK_EMPLOYEE_LIST, wfmStrings.employees(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AsteriskEmployeeListSinksContainer(Constants.ASTERISK_EMPLOYEE_LIST, wfmStrings.employees(), params);
    }
}
