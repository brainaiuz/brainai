package com.edatasite.workforce.gwt.invoice.client.history.saleinvoice;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.saleinvoice.ImportNimbleCommerceSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/15/12
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportNimbleCommerceHistoryProcessor implements HistoryProcessor {

    private static AccountingStrings accountingStrings = AccountingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ImportNimbleCommerceSinksContainer("importnimblecommerceadd", accountingStrings.importNimbleCommerce(), params);
    }
}
