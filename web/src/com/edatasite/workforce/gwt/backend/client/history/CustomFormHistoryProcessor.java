package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.CustomFormSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 11.01.13
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public class CustomFormHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new CustomFormSinksContainer("customformlistview", wfmStrings.customForms());
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
