package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.ReportTemplateCategoryAddEditSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 01.11.11
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */

public class ReportTemplateCategoryHistoryProcessor implements HistoryProcessor {
    public ReportTemplateCategoryHistoryProcessor() {

    }

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ReportTemplateCategoryAddEditSinksContainer(params);
    }
}
