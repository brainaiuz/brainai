package com.edatasite.workforce.gwt.accounting.client.history.report;

import com.edatasite.workforce.gwt.accounting.client.container.report.ClickedReportSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 18, 2009
 * Time: 7:37:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClickedReportHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings)//must be ---> strings.length<=3
    {
        SinksContainer report = new ClickedReportSinksContainer(containerName + strings[0], accountingStrings.reportView(), strings);
        report.setCollapsed(true);
        return report;
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
