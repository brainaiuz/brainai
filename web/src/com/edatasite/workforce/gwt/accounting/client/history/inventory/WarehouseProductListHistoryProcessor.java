package com.edatasite.workforce.gwt.accounting.client.history.inventory;

import com.edatasite.workforce.gwt.accounting.client.container.inventory.WarehouseProductsListSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 21, 2010
 * Time: 2:13:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseProductListHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings)
    {
        return new WarehouseProductsListSinksContainer(containerName + strings[0], wfmStrings.products(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new WarehouseProductsListSinksContainer("warehouseproductlist", wfmStrings.products(), params);
    }
}