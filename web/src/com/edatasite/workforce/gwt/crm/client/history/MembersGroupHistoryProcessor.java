package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.MailListViewSinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;

/**
 * Created with IntelliJ IDEA.
 * User: Java6
 * Date: 14.08.12
 * Time: 14:58
 * To change this template use File | Settings | File Templates.
 */
public class MembersGroupHistoryProcessor implements HistoryProcessor {
    private static final CrmStrings crmStrings = CrmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new MailListViewSinksContainer(containerName + strings[0], crmStrings.membersList(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
