package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ContactAddSinksContainer;
import com.edatasite.workforce.gwt.crm.client.ContactViewSinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:52:49
 * To change this template use File | Settings | File Templates.
 */
public class ContactHistoryProcessor implements HistoryProcessor {

    private CrmStrings crmStrings = CrmStrings.App.get();
    private WfmStrings wfmStrings = WfmStrings.App.get();

    public ContactHistoryProcessor() {

    }

    public SinksContainer process(String containerName, String[] strings) {
        return new ContactViewSinksContainer(containerName + strings[0], Property.get(Constants.Contacts, wfmStrings.summaryView(), wfmStrings.contact()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new ContactAddSinksContainer("contactadd", Property.get(Constants.Contacts, wfmStrings.addMess(), wfmStrings.contact()), params);
    }
}