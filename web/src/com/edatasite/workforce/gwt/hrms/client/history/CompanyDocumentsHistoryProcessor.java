package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.AddCompanyDocumentSinksContainer;

/**
 * Created by Djuraev on 9/29/15.
 */
public class CompanyDocumentsHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new AddCompanyDocumentSinksContainer(containerName + strings[0], wfmStrings.addDocument(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AddCompanyDocumentSinksContainer("companyDocumentadd", wfmStrings.addDocument(), params);
    }
}
