package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.ReportingExcelTemplateAddEditSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 22.10.2011
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */

public class ReportingExcelTemplatesHistoryProcessor implements HistoryProcessor {
    public ReportingExcelTemplatesHistoryProcessor() {

    }

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ReportingExcelTemplateAddEditSinksContainer(params);
    }
}
