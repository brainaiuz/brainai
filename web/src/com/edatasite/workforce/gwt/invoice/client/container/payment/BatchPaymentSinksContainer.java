package com.edatasite.workforce.gwt.invoice.client.container.payment;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.payment.BatchPaymentAddView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/29/11
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchPaymentSinksContainer extends SinksContainer{

    public BatchPaymentSinksContainer(String name, String description, String[] params) {
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
        if (params.length > 3 && "copy".equals(params[2])) {
            Integer copyFromId = Integer.valueOf(params[3]);
            addView(new BatchPaymentAddView(copyFromId, isReceivable, true));
        }
        addView(new BatchPaymentAddView(isReceivable));
    }
}
