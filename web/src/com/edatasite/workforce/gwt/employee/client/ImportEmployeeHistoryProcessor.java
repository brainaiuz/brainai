package com.edatasite.workforce.gwt.employee.client;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by Dilshod Madrahimov on 8/18/15 4:45 PM
 */
public class ImportEmployeeHistoryProcessor implements HistoryProcessor {

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new ImportEmployeeSinksContainer("importemployeeadd", "Import Employee", params);
    }
}
