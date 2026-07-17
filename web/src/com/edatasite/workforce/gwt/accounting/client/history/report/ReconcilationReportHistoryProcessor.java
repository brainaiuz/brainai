package com.edatasite.workforce.gwt.accounting.client.history.report;

import com.edatasite.workforce.gwt.accounting.client.container.report.ReconcilationReportSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 17.07.2010
 * Time: 16:01:14
 * To change this template use File | Settings | File Templates.
 */
public class ReconcilationReportHistoryProcessor implements HistoryProcessor {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new ReconcilationReportSinksContainer(containerName + strings[0], accountingStrings.reconcilationReport(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
