package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class ImportGroupPayrunHistoryProcessor implements HistoryProcessor {

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new ImportGroupPayrunSinksContainer("importgrouppayrunadd", "Import Group Payrun", params);
    }
}