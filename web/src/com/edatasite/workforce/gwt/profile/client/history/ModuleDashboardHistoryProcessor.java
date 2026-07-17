package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ModuleDashboardViewSinksContainer;

/**
 * User: Abror Abdukadirov
 * Date: 10.04.2018 16:01
 */
public class ModuleDashboardHistoryProcessor implements HistoryProcessor {

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ModuleDashboardViewSinksContainer(containerName + strings[0], "Dashboard View", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
