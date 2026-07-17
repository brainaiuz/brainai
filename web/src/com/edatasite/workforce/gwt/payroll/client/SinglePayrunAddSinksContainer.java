package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.SinglePayrunGenerateView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/27/15
 * Time: 10:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class SinglePayrunAddSinksContainer extends SinksContainer {

    public SinglePayrunAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        SinglePayrunGenerateView payslipGenerateView = new SinglePayrunGenerateView();
        addView(payslipGenerateView);
    }
}
