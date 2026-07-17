package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.MessageAddSinksContainer;
import com.edatasite.workforce.gwt.crm.client.MessageViewSinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Date: 29.01.2010
 * Time: 16:37:41
 * To change this template use File | Settings | File Templates.
 */
public class MessagesHistoryProcessor implements HistoryProcessor {
    private CrmStrings crmStrings = CrmStrings.App.get();
    private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new MessageViewSinksContainer(containerName + strings[0], crmStrings.messageView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        Integer objectID = params != null && params.length > 1 && params[1] != null && params[1].matches(Constants.REGEX_INTEGER) ? Integer.parseInt(params[1]) : null;
        return new MessageAddSinksContainer("messageadd", (objectID != null && !"null".equals(objectID)) ? crmStrings.editMessage() : wfmStrings.addMessage(), params);
    }
}
