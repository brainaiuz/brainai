package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.ChartOfAccountsSummarySinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 18.01.2011
 * Time: 0:35:26
 * To change this template use File | Settings | File Templates.
 */
public class ChartOfAccountsSummaryHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ChartOfAccountsSummarySinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
