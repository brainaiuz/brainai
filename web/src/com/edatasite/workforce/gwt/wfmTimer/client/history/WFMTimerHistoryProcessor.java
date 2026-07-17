package com.edatasite.workforce.gwt.wfmTimer.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.wfmTimer.client.WFMTimerViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 8/26/11
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */

public class WFMTimerHistoryProcessor implements HistoryProcessor, Constants {
    public SinksContainer process(String containerName, String[] strings) {
        return new WFMTimerViewSinksContainer(containerName + strings[0], "Timer", strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
