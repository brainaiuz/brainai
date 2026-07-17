package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.utils.AdditionalPaymentAddSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.utils.AdditionalPaymentViewSinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;

/**
 * Created by Omonullo Abdullaev on 12/13/2016.
 */
public class AdditionalDeductionHisotryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new AdditionalPaymentViewSinksContainer(containerName + strings[0], "Additional Deduction", strings, PayrollConstants.CATEGORY_DEDUCTION);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AdditionalPaymentAddSinksContainer("additionalDeductionadd", "Additional Deduction", params, PayrollConstants.CATEGORY_DEDUCTION);
    }
}
