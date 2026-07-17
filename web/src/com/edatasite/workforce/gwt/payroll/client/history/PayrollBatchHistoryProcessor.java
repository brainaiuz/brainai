package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PayrollBatchAddSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PayrollBatchViewSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/21/15
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollBatchHistoryProcessor implements HistoryProcessor {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new PayrollBatchViewSinksContainer(containerName + strings[0], "Edit", strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new PayrollBatchAddSinksContainer("payrollBatchadd", payrollStrings.payrollBatches(), params);
    }
}
