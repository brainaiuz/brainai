package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.utils.AdditionalPaymentAddSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.utils.AdditionalPaymentViewSinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;

/**
 * Created by Shohruh on 28 Oct 2016.
 */
public class AdditionalPaymentHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new AdditionalPaymentViewSinksContainer(containerName + strings[0], Property.get(Constants.ADDITIONAL_PAYMENT_LIST, wfmStrings.additionalPayment()), strings, PayrollConstants.CATEGORY_PAYMENT);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AdditionalPaymentAddSinksContainer("additionalPaymentadd", Property.get(Constants.ADDITIONAL_PAYMENT_LIST, wfmStrings.additionalPayment()), params, PayrollConstants.CATEGORY_PAYMENT);
    }
}
