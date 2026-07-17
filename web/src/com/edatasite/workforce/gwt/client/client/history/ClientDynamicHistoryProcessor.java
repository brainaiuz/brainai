package com.edatasite.workforce.gwt.client.client.history;

import com.edatasite.workforce.gwt.client.client.ClientDynamicAddSinksContainer;
import com.edatasite.workforce.gwt.client.client.ClientViewSinksContainer;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class ClientDynamicHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private String section;

    public ClientDynamicHistoryProcessor(String section) {
        this.section = section;
    }

    public SinksContainer process(String containerName, String[] strings) {
        return new ClientViewSinksContainer(containerName + strings[0], Property.get(Constants.CLIENT_LIST, wfmStrings.summaryView(), wfmStrings.customer()), strings, section);
    }

    public SinksContainer processAdd(String[] params) {
        return new ClientDynamicAddSinksContainer("clientdynamicadd", Property.get(Constants.CLIENT_LIST, wfmStrings.addMess(), wfmStrings.customer()));
    }

}
