package com.edatasite.workforce.gwt.expenses.client;

import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.expenses.client.factory.ExpenseSinksContainerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 13.10.2008
 * Time: 17:53:19
 * To change this template use File | Settings | File Templates.
 */
public class Expenses extends WorkforceEntryPoint {

    public void initSinksContainerFactory() {
        containerFactory = new ExpenseSinksContainerFactory(this);
    }
}
