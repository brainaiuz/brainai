package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet.ImportExport;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class ImportBudgetManagerHistoryProcessor implements HistoryProcessor {

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ImportBudgetManagerSinksContainer("importbudgetmanageradd", WfmStrings.App.get().importBudgetManager(), params);

    }
}
