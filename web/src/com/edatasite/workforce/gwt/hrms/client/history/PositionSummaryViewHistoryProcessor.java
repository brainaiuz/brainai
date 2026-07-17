package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.PositionSummaryViewSinksContainer;

/**
 * User: Admin
 * Date: 16.12.2009
 * Time: 16:34:54
 */
public class PositionSummaryViewHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings= WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new PositionSummaryViewSinksContainer(containerName + strings[0],wfmStrings.summaryView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new PositionSummaryViewSinksContainer("positionsummaryview", wfmStrings.summaryView(), params);
    }
}