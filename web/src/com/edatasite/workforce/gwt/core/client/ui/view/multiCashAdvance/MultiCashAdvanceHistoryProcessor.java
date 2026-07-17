package com.edatasite.workforce.gwt.core.client.ui.view.multiCashAdvance;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.MULTI_CASH_ADVANCE_LIST;


public class MultiCashAdvanceHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new MultiCashAdvanceViewSinksContainer(containerName + strings[0], Property.get(MULTI_CASH_ADVANCE_LIST, wfmStrings.multiCashAdvance()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new MultiCashAdvanceAddSinksContainer("cashAdvanceadd", Property.get(MULTI_CASH_ADVANCE_LIST, wfmStrings.multiCashAdvance()), params);
    }
}
