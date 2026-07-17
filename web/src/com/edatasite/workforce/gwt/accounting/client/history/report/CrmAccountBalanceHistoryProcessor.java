package com.edatasite.workforce.gwt.accounting.client.history.report;

import com.edatasite.workforce.gwt.accounting.client.container.report.CrmAccountBalanceSinksContainer;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/19/11
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountBalanceHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {

        return new CrmAccountBalanceSinksContainer(containerName + strings[0], containerName.equals("customerBalance") ? wfmStrings.customerBalance() : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplierBalance(), wfmStrings.supplier()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
