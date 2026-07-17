package com.edatasite.workforce.gwt.invoice.client.container.payment;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.payment.CrmAccountProjectBalanceView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/28/11
 * Time: 8:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountProjectBalanceSinksContainer extends SinksContainer {

    public CrmAccountProjectBalanceSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new CrmAccountProjectBalanceView(id));
    }
}
