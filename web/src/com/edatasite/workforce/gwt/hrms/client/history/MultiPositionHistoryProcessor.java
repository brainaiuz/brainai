package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.MultiPositionSinksContainer;

public class MultiPositionHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new MultiPositionSinksContainer(containerName + strings[0], wfmStrings.add() + " " + Property.get("position", wfmStrings.position()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new MultiPositionSinksContainer("multipositionadd", wfmStrings.add() + " " + Property.get("position", wfmStrings.position()), params);
    }
}
