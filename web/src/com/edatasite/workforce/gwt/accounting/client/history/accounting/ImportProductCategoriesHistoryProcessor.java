package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.ImportProductCategoriesSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

public class ImportProductCategoriesHistoryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ImportProductCategoriesSinksContainer("importproductcategoriesadd", "Import Product Categories", params);
    }
}
