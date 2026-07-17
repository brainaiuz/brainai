package com.edatasite.workforce.gwt.invoice.client.history.purchaseorder;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.purchaseorder.PurchaseOrderAddSinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.purchaseorder.PurchaseOrderViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 18.04.2009
 * Time: 15:13:18
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseOrderHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new PurchaseOrderViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new PurchaseOrderAddSinksContainer("purchaseorderadd", Property.get(Constants.PURCHASE_ORDER, wfmStrings.addMess(), wfmStrings.purchaseorder()), params);
    }
}