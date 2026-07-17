package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.CrmAccountImportSinksContainer;


/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 5, 2009
 * Time: 7:45:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountImportHistoryProcessor implements HistoryProcessor {
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new CrmAccountImportSinksContainer("importaccountadd", wfmStrings.importAccount(), params);
    }
}
