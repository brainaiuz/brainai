package com.edatasite.workforce.gwt.accounting.client.container.report;

import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewCrmAccountBalanceView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/19/11
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountBalanceSinksContainer extends SinksContainer{
    public CrmAccountBalanceSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new NewCrmAccountBalanceView(id, params[1]));
    }
}
