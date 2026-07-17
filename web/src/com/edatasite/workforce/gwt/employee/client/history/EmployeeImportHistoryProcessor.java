package com.edatasite.workforce.gwt.employee.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.EmployeeImportSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 10, 2010
 * Time: 9:30:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeImportHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new EmployeeImportSinksContainer("importemployeesadd", wfmStrings.importEmployees(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new EmployeeImportSinksContainer("importemployeesadd", wfmStrings.importEmployees(), params);
    }

}