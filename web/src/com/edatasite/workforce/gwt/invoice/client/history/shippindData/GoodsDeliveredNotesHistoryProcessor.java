package com.edatasite.workforce.gwt.invoice.client.history.shippindData;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.shippingData.GoodsDeliveredNotesSinksContainer;

/**
 * User: Murad Satimov
 * Date: 18.04.2009
 * Time: 15:13:18
 */
public class GoodsDeliveredNotesHistoryProcessor implements HistoryProcessor {

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new GoodsDeliveredNotesSinksContainer(containerName + strings[0],
                AccountingStrings.App.get().gdnNumber(),
                                                     strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}