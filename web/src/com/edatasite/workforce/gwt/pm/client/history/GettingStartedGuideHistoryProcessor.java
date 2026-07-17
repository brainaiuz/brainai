package com.edatasite.workforce.gwt.pm.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.gettingstarted.client.GettingStartedGuideSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhom Lutfullaev
 * Date: 28.01.2010
 * Time: 18:34:06
 * To change this template use File | Settings | File Templates.
 */
public class GettingStartedGuideHistoryProcessor implements HistoryProcessor {
    private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new GettingStartedGuideSinksContainer(containerName + strings[0], wfmStrings.gettingStarted(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
