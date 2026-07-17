package com.edatasite.workforce.gwt.accounting.client.history.inventory;

import com.edatasite.workforce.gwt.accounting.client.container.inventory.ProductCategoriesListSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 3, 2010
 * Time: 5:55:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCategoriesListHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new ProductCategoriesListSinksContainer(containerName + strings[0], accountingStrings.productCategories(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new ProductCategoriesListSinksContainer("productcategorylist", accountingStrings.productCategories(), params);
    }
}
