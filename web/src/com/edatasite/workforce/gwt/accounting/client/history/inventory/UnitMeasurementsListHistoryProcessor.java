package com.edatasite.workforce.gwt.accounting.client.history.inventory;

import com.edatasite.workforce.gwt.accounting.client.container.inventory.UnitMeasurementsListSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 16, 2010
 * Time: 10:36:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnitMeasurementsListHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new UnitMeasurementsListSinksContainer(containerName + strings[0], accountingStrings.measurements(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new UnitMeasurementsListSinksContainer("unitmeasurementlist", accountingStrings.measurements(), params);
    }
}
