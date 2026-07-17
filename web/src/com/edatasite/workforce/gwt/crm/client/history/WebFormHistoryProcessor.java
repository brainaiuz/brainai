package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.WebFormAddSinksContainer;
import com.edatasite.workforce.gwt.crm.client.WebFormViewSinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:52:49
 * To change this template use File | Settings | File Templates.
 */
public class WebFormHistoryProcessor implements HistoryProcessor {
    private static final CrmStrings crmStrings = CrmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new WebFormViewSinksContainer(containerName + strings[0], crmStrings.webFormView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new WebFormAddSinksContainer("webformadd", crmStrings.addCrmForm(), params);
    }

}