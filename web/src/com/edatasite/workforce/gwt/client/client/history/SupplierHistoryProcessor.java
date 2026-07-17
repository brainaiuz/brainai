package com.edatasite.workforce.gwt.client.client.history;

import com.edatasite.workforce.gwt.client.client.ui.SupplierAddSinksContainer;
import com.edatasite.workforce.gwt.client.client.ui.SupplierEditSinksContainer;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 11, 2009
 * Time: 5:31:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupplierHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings)//must be ---> strings.length<=3
    {
        return new SupplierEditSinksContainer(containerName + strings[0], Property.get(Constants.SUPPLIER_LIST, wfmStrings.editSupplierOrBill(), wfmStrings.supplier()), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new SupplierAddSinksContainer("supplieradd", Property.get(Constants.SUPPLIER_LIST, wfmStrings.addMess(), wfmStrings.supplier()), params);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
