package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.ReportingTemplatesAddSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Sep 19, 2011
 * Time: 7:04:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportingTemplatesHistoryProcessor implements HistoryProcessor {
    public SinksContainer process(String containerName, String[] strings) {
        return new ReportingTemplatesAddSinksContainer(containerName + strings[0], "Edit  Templates", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
