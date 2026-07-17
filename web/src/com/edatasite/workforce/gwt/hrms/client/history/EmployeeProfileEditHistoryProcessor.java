package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.EmployeeProfileEditSinksContainer;

/**
 * User: unni
 * Date: Nov 12, 2009
 * Time: 1:11:35 PM
 */
public class EmployeeProfileEditHistoryProcessor implements HistoryProcessor {
    
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new EmployeeProfileEditSinksContainer(containerName + strings[0], wfmStrings.editEmployeeProfile(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}