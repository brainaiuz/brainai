package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.ui.SolrCoreCompanySinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 15/03/12
 * Time: 20:53
 * To change this template use File | Settings | File Templates.
 */
public class SolrCoreCompanyListHistoryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new SolrCoreCompanySinksContainer(containerName + strings[0], strings[1].toUpperCase() + " Company List", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
