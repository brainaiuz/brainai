package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.FingerPrintDeviceStatusHistoryListSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by Farrukh on 26.10.2017.
 */
public class FingerPrintDeviceStatusHistoryProcessor implements HistoryProcessor {

    private static final BackendStrings backendStrings = BackendStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new FingerPrintDeviceStatusHistoryListSinksContainer(containerName + strings[0], backendStrings.fingerprintDeviceStatusHistory(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
