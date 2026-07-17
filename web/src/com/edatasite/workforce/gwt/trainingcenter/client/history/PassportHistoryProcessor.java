package com.edatasite.workforce.gwt.trainingcenter.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.PassportAddSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.PassportViewSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 11/06/14
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
 */
public class PassportHistoryProcessor implements HistoryProcessor {
    private static final TCStrings tcStrings = TCStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new PassportViewSinksContainer(containerName + strings[0], tcStrings.issuePassport(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new PassportAddSinksContainer("passportadd", tcStrings.issuePassport(), params);
    }
}
