package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.container.EmailFilterSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 12/1/11
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmailFilterHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new EmailFilterSinksContainer(containerName + strings[0], this.wfmStrings.addEmailFilter(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new EmailFilterSinksContainer("emailfilteradd", wfmStrings.addEmailFilter(), params);
    }
}
