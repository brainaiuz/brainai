package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PayrollGlobalSettingsAddSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PayrollGlobalSettingsViewSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/21/15
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollGlobalSettingsHistoryProcessor implements HistoryProcessor {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new PayrollGlobalSettingsViewSinksContainer(containerName + strings[0], payrollStrings.bulkUpdate(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new PayrollGlobalSettingsAddSinksContainer("payrollGlobalSettingsadd", payrollStrings.bulkUpdate(), params);
    }
}
