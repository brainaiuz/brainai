package com.edatasite.workforce.gwt.expenses.client.sinks;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.expenses.client.ui.view.ExpenseEmailComposeView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Marat
 * Date: 26.03.12
 * Time: 11:26
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseEmailComposeSinksContainer extends SinksContainer {

    public ExpenseEmailComposeSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new ExpenseEmailComposeView(Integer.valueOf(params[1]), params[2]));
    }
}
