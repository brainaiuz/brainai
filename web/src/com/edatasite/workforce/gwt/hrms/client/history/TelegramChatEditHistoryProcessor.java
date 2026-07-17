package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.TelegramChatEditSinksContainer;

/**
 * User: Abror Abdukadirov
 * Date: 01.08.2017 2:07
 */
public class TelegramChatEditHistoryProcessor implements HistoryProcessor {

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new TelegramChatEditSinksContainer(containerName + strings[0], "Edit Chat", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
