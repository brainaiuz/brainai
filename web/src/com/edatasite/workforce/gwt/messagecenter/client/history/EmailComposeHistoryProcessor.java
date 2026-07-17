package com.edatasite.workforce.gwt.messagecenter.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.messagecenter.client.container.EmailComposeSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 19.11.2010
 * Time: 18:28:34
 * To change this template use File | Settings | File Templates.
 */
public class EmailComposeHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new EmailComposeSinksContainer("emailcomposeadd", wfmStrings.compose(), params);
    }
}
