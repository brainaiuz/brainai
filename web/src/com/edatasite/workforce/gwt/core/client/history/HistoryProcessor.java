package com.edatasite.workforce.gwt.core.client.history;

import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public interface HistoryProcessor {
    SinksContainer process(String containerName, String[] strings);//must be ---> strings.length<=3

    SinksContainer processAdd(String[] params);
}
