package com.edatasite.workforce.gwt.invoice.client.container.payment;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.payment.BatchPaymentAddView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.payment.BatchPaymentSummaryView;

import java.util.LinkedList;

/**
 * Created by Sherzod on 7/7/2015.
 */
public class BatchPaymentViewSinksContainer extends SinksContainer{

    public BatchPaymentViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        boolean isReceivable = true;
        if (params != null && params.length >= 2 && Constants.PAYABLE.equals(params[1])) {
            isReceivable = false;
        }
        //addView(new BatchPaymentSummaryViewOld(id, isReceivable));
        addView(new BatchPaymentSummaryView(id, isReceivable));
        addView(new BatchPaymentAddView(id, isReceivable));
    }
}
