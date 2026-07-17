package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.SinglePayrunGenerateView;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.SinglePayrunSummaryView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/27/15
 * Time: 10:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class SinglePayrunViewSinksContainer extends SinksContainer {

    public SinglePayrunViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params.length > 1) {
            addView(new SinglePayrunGenerateView(id, Integer.parseInt(params[1])));
        } else {
            addView(new SinglePayrunSummaryView(id));
        }
    }
}
