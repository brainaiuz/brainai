package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.GoogleContactImportSinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:52:49
 * To change this template use File | Settings | File Templates.
 */
public class GoogleContactImportHistoryProcessor implements HistoryProcessor {
    private CrmStrings crmStrings = CrmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new GoogleContactImportSinksContainer("gcontact0", crmStrings.exportContacts(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new GoogleContactImportSinksContainer("gcontactadd", crmStrings.importContacts(), params);
    }
}