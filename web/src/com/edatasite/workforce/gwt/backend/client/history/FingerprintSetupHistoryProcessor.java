package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.FingerprintSetupSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by Muhammad on 09.04.2016.
 */
public class FingerprintSetupHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new FingerprintSetupSinksContainer(containerName + strings[0], wfmStrings.fingerprintSetup(), strings);
    }
    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
