package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.CustomFormAddSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:52:49
 * To change this template use File | Settings | File Templates.
 */
public class CustomFormAddHistoryProcessor implements HistoryProcessor {

    private static final BackendStrings backendStrings = BackendStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new CustomFormAddSinksContainer("customformadd", backendStrings.addCustomForm(), params);
    }
}