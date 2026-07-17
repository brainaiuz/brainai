package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.container.CompanyFileTransferViewSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: Ilhombek
 * Date: 4/18/12
 * Time: 6:33 PM
 */
public class CompanyFileTransferViewHistoryProcessor implements HistoryProcessor {

    private static final BackendStrings backendStrings = BackendStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new CompanyFileTransferViewSinksContainer(containerName + strings[0],
                                                         backendStrings.companyFileTransfer(),
                                                         strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}