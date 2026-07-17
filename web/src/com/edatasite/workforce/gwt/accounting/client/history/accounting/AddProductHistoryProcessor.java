package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.AddProductSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.ProductViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 7, 2009
 * Time: 4:52:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddProductHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings){//must be ---> strings.length<=3
        return new ProductViewSinksContainer(containerName + strings[0], Property.get(Constants.PRODUCTS_OR_SERVICES, wfmStrings.summaryView(), wfmStrings.product()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new AddProductSinksContainer("productadd", Property.get(Constants.PRODUCTS_OR_SERVICES, accountingStrings.addProduct(), wfmStrings.product()), params);
    }
}
