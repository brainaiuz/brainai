package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.AdditionalPaymentItemListView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.SinglePayrunListView;

import java.util.LinkedList;

/**
 * User: Jonibek
 * Date: Nov 2, 2009
 * Time: 5:08:34 PM
 */
public class PayrollEmployeeViewSinksContainer extends SinksContainer {

    public PayrollEmployeeViewSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    public PayrollEmployeeViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        if (Utils.hasRole(MEM)) {
            addView(new SinglePayrunListView(id));
            addView(new CashAdvanceListView(id, false, true));
            addView(new AdditionalPaymentItemListView(id));
        }
    }
}