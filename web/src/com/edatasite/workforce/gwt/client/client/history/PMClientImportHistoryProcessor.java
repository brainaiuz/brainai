package com.edatasite.workforce.gwt.client.client.history;

import com.edatasite.workforce.gwt.client.client.PMClientImportSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 3, 2010
 * Time: 6:27:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class PMClientImportHistoryProcessor implements HistoryProcessor {
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new PMClientImportSinksContainer("importclientadd", "Import Client", params);
    }
}