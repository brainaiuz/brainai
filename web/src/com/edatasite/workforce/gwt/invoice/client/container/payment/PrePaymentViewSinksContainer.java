package com.edatasite.workforce.gwt.invoice.client.container.payment;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.payment.AddEditPrepaymentView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/19/12
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrePaymentViewSinksContainer extends SinksContainer{
    public PrePaymentViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        boolean isReceivable = getName().startsWith("prepayment");
        addView(new AddEditPrepaymentView(id, isReceivable, params));
    }
}
