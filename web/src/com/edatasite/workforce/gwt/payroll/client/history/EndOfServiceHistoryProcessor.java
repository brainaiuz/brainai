package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.EndOfServiceSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 13.05.14
 * Time: 23:43
 * To change this template use File | Settings | File Templates.
 */
public class EndOfServiceHistoryProcessor implements HistoryProcessor {

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new EndOfServiceSinksContainer(containerName + strings[0], "EndOfService", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new EndOfServiceSinksContainer("endOfServiceadd", PayrollStrings.App.get().endOfService(), params);
    }
}
