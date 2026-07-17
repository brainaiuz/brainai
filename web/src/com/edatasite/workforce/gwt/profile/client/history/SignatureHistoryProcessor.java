package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.SignatureAddSinksContainer;
import com.edatasite.workforce.gwt.profile.client.SignatureViewSinksContainer;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 12.02.13
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
public class SignatureHistoryProcessor implements HistoryProcessor {
    private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new SignatureViewSinksContainer(containerName + strings[0], wfmStrings.signatureView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new SignatureAddSinksContainer("signatureadd", wfmStrings.addSignature(), params);
    }
}
