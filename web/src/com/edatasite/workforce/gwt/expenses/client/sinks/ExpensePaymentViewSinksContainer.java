package com.edatasite.workforce.gwt.expenses.client.sinks;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.expenses.client.ui.view.ExpensePaymentEditView;
import com.edatasite.workforce.gwt.expenses.client.ui.view.ExpensePaymentView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Marat
 * Date: 26.03.12
 * Time: 11:26
 * To change this template use File | Settings | File Templates.
 */
public class ExpensePaymentViewSinksContainer extends SinksContainer{

    public ExpensePaymentViewSinksContainer(String name, String description, String[] params){
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        ExpensePaymentView paymentView = new ExpensePaymentView(id);
        addView(paymentView);
        ExpensePaymentEditView paymentEditView = new ExpensePaymentEditView(id);
        addView(paymentEditView);
    }
}
