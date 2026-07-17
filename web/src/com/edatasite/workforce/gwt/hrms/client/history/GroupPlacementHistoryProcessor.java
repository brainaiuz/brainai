package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.GroupPlacementAddSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.GroupPlacementSummarySinksContainer;

public class GroupPlacementHistoryProcessor implements HistoryProcessor {
    private WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new GroupPlacementSummarySinksContainer(containerName + strings[0] + strings[1], wfmStrings.group() + " " + wfmStrings.placement(), strings);

    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new GroupPlacementAddSinksContainer("addGroupPlacement", wfmStrings.group() + " " + wfmStrings.placement(), params);

    }
}
