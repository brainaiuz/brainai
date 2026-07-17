package com.edatasite.workforce.gwt.invoice.client.container.payment;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.prepayment.CustomerPrepaymentListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.prepayment.SupplierPrepaymentListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12/23/11
 * Time: 5:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrepaymentListSinksContainer extends SinksContainer{

    public PrepaymentListSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        boolean isReceivable = getName().startsWith("prepayment");
        if (isReceivable) {
            addView(new CustomerPrepaymentListView());
        } else {
            addView(new SupplierPrepaymentListView());
        }
    }
}
