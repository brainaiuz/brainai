package com.edatasite.workforce.gwt.accounting.client.history.inventory;

import com.edatasite.workforce.gwt.accounting.client.container.inventory.AddProductCategorySinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.inventory.ProductCategoryViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 4, 2010
 * Time: 12:13:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddProductCategoryHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings)//must be ---> strings.length<=3
    {
        return new ProductCategoryViewSinksContainer(containerName + strings[0], accountingStrings.categoryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new AddProductCategorySinksContainer("productcategoryadd", wfmStrings.addCategory(), params);
    }
}