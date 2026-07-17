package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.TaxAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.TaxViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 25.02.2009
 * Time: 14:46:57
 * To change this template use File | Settings | File Templates.
 */
public class TaxHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings){
        return new TaxViewSinksContainer(containerName + strings[0], accountingStrings.editTaxRate(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new TaxAddSinksContainer("taxadd", wfmStrings.addTaxRate());  //To change body of implemented methods use File | Settings | File Templates.
    }
}
