package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.AddEmployeeDocumentSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.HrmsDocumentsSinksContainer;

/**
 * Created by Djuraev on 9/23/15.
 */
public class EmployeeDocumentHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new HrmsDocumentsSinksContainer(containerName, wfmStrings.documents(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AddEmployeeDocumentSinksContainer("employeeDocumentadd", wfmStrings.addDocument(), params);
    }
}
