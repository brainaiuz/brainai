package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.CreateEMLTemplatesSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 18.09.2010
 * Time: 16:52:22
 * To change this template use File | Settings | File Templates.
 */
public class CreateEMLTemplatesHistoryProcessor implements HistoryProcessor {
    private final WfmStrings wfmStrings = WfmStrings.App.get();
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new CreateEMLTemplatesSinksContainer(containerName + strings[0], wfmStrings.emailTemplate(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
