package com.edatasite.workforce.gwt.client.client.history;

import com.edatasite.workforce.gwt.client.client.ClientEditSinksContainer;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 3, 2010
 * Time: 6:27:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientEditHistoryProcessor implements HistoryProcessor {


    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new ClientEditSinksContainer(containerName + strings[0], Property.get(Constants.CLIENT_LIST, wfmStrings.edit(), wfmStrings.customer()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
