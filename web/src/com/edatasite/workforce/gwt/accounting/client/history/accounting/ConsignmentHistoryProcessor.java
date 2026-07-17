package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.ConsignmentAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.ConsignmentViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/6/11
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConsignmentHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingString = AccountingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ConsignmentViewSinksContainer(containerName + strings[0], wfmStrings.consignments(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ConsignmentAddSinksContainer("consignmentadd", accountingString.addConsignment());
    }
}
