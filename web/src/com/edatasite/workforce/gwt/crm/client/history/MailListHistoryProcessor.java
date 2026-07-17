package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.MailListViewSinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Date: 29.01.2010
 * Time: 16:37:41
 * To change this template use File | Settings | File Templates.
 */
public class MailListHistoryProcessor implements HistoryProcessor {
    private static final CrmStrings crmStrings = CrmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new MailListViewSinksContainer(containerName + strings[0], crmStrings.editMailList(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}