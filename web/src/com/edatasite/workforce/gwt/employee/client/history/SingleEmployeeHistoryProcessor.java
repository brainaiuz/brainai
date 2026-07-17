package com.edatasite.workforce.gwt.employee.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.SingleEmployeeAddSinksContainer;

/**
 * Created by Dilshod Madrahimov on 9/7/15 2:51 PM
 */
public class SingleEmployeeHistoryProcessor implements HistoryProcessor {

    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new SingleEmployeeAddSinksContainer("singleemployeeadd", wfmStrings.addEmployee(), params);
    }
}
