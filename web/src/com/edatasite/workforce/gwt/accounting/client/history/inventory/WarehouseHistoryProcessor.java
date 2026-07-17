package com.edatasite.workforce.gwt.accounting.client.history.inventory;

import com.edatasite.workforce.gwt.accounting.client.container.inventory.WarehouseAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.inventory.WarehouseViewSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;


/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 15, 2010
 * Time: 8:04:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings= WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings)//must be ---> strings.length<=3
    {
        return new WarehouseViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new WarehouseAddSinksContainer("warehouseproductlist", wfmStrings.add());
    }

//    public SinksContainer processView(String[] params) {
//        return new WarehouseAddSinksContainer("warehousesummary", wfmStrings.summaryView());
//    }
//




}
