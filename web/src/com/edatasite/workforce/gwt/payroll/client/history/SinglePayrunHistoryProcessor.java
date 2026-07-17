package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.SinglePayrunAddSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.SinglePayrunViewSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/27/15
 * Time: 10:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class SinglePayrunHistoryProcessor implements HistoryProcessor {
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private static final WfmStrings wfmStrings = GWT.create(WfmStrings.class);

    public SinksContainer process(String containerName, String[] strings) {
        return new SinglePayrunViewSinksContainer(containerName + strings[0], Property.get(Constants.SINGLE_PAYRUN_LIST, payrollStrings.viewSinglePayrun(), wfmStrings.payslip()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new SinglePayrunAddSinksContainer("singlePayrunadd", Property.get(Constants.SINGLE_PAYRUN_LIST, wfmStrings.payslip()), params);
    }
}
