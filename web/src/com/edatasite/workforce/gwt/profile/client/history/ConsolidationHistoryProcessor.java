package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.container.AddConsalidationSinkContainer;
import com.edatasite.workforce.gwt.profile.client.container.ConsalidationSinkContainer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 05/10/12
 * Time: 21:12
 * To change this template use File | Settings | File Templates.
 */
public class ConsolidationHistoryProcessor implements HistoryProcessor {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ConsalidationSinkContainer(containerName, settingsStrings.companyConsalidationList(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AddConsalidationSinkContainer("consolidationadd", settingsStrings.addNewCompany(), params);
    }
}
