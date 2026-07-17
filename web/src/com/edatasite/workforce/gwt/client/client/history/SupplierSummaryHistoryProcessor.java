package com.edatasite.workforce.gwt.client.client.history;

import com.edatasite.workforce.gwt.client.client.ui.SupplierSummarySinksContainer;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: Ilhombek
 * Date: Apr 13, 2010
 * Time: 6:43:17 PM
 */
public class SupplierSummaryHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new SupplierSummarySinksContainer(containerName + strings[0], Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()) + wfmStrings.summaryView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}