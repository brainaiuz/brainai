package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.CASH_ADVANCE_LIST;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 02.08.14
 * Time: 5:44
 * To change this template use File | Settings | File Templates.
 */
public class CashAdvanceHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new CashAdvanceViewSinksContainer(containerName + strings[0], Property.get(CASH_ADVANCE_LIST, wfmStrings.cashAdvance()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new CashAdvanceAddSinksContainer("cashAdvanceadd", Property.get(CASH_ADVANCE_LIST, wfmStrings.cashAdvance()), params);
    }
}
