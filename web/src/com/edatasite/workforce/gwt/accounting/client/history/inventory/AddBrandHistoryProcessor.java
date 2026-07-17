package com.edatasite.workforce.gwt.accounting.client.history.inventory;

import com.edatasite.workforce.gwt.accounting.client.container.inventory.AddBrandSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.inventory.BrandViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 17, 2010
 * Time: 3:34:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddBrandHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings)//must be ---> strings.length<=3
    {
        return new BrandViewSinksContainer(containerName + strings[0], accountingStrings.editBrand(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new AddBrandSinksContainer("brandadd", accountingStrings.addBrand());
    }
}