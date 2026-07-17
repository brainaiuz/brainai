package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollBatchAddEditView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/22/15
 * Time: 9:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollBatchViewSinksContainer extends SinksContainer {

    public PayrollBatchViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new PayrollBatchAddEditView(id));
    }
}
