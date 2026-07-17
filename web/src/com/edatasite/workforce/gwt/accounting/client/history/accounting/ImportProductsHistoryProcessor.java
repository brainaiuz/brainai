package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.ImportProductsSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Sep 21, 2010
 * Time: 1:34:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportProductsHistoryProcessor implements HistoryProcessor {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;//new ImportProductsSinksContainer(containerName + strings[0], "Import Products", strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new ImportProductsSinksContainer("importproductsadd", accountingStrings.importProducts(), params);
    }
}