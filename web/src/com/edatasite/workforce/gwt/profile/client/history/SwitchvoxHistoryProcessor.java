package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.SwitchvoxSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 16.05.12
 * Time: 18:10
 * To change this template use File | Settings | File Templates.
 */
public class SwitchvoxHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new SwitchvoxSinksContainer(containerName + strings[0], "Switchvox", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}