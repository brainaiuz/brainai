package com.edatasite.workforce.gwt.accounting.client.history.inventory;

import com.edatasite.workforce.gwt.accounting.client.container.inventory.WarehouseLocationListSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 16, 2010
 * Time: 8:26:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddWarehouseLocationListHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings)//must be ---> strings.length<=3
    {
        return new WarehouseLocationListSinksContainer(containerName + strings[0], wfmStrings.locations(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new WarehouseLocationListSinksContainer("warehouselocationlist", wfmStrings.locations(), params);
    }
}