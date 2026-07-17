package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.GroupPayrunAddSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.GroupPayrunViewSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 03.03.14
 * Time: 19:04
 * To change this template use File | Settings | File Templates.
 */
public class GroupPayrunHistoryProcessor implements HistoryProcessor {
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new GroupPayrunViewSinksContainer(containerName + strings[0], Property.getPluralWithObjectCode(Constants.PAYSLIP_TABLE_LIST,  payrollStrings.groupPayruns()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new GroupPayrunAddSinksContainer("payslipTableadd", Property.getPluralWithObjectCode(Constants.PAYSLIP_TABLE_LIST,  payrollStrings.groupPayruns()), params);
    }
}
