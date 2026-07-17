package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.SignatureEditSinksContainer;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 14.02.13
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
public class SignatureEditHistoryProcessor implements HistoryProcessor {
    private WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new SignatureEditSinksContainer(containerName + strings[0], wfmStrings.signatureEdit(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
