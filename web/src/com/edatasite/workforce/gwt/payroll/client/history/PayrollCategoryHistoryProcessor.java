package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.CategoryAddSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 27.02.2009
 * Time: 3:54:16
 * To change this template use File | Settings | File Templates.
 */
public class PayrollCategoryHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        /*SinksContainer container =  new ClientViewSinksContainer(containerName+strings[0], "Client View", strings);
          return container;*/
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new CategoryAddSinksContainer("payrollcategoryadd", wfmStrings.addCategory(), params);
    }

}