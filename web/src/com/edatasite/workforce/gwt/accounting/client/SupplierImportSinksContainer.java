package com.edatasite.workforce.gwt.accounting.client;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.Date;
import java.util.LinkedList;
//import com.edatasite.workforce.gwt.crm.client.ui.view.ImportClientView;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: Aug 5, 2009
 * Time: 7:47:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupplierImportSinksContainer extends SinksContainer {

    public SupplierImportSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        String objectId = null;
        Date conversionDate = null;

        if (params.length > 2) {
            objectId = params[1];
            try {
                conversionDate = new DateNonConvertable(DateUtils.parseLongFormat(params[2])).getNonConvertedDate();
            } catch (DateFormatException e) {
                e.printStackTrace();
            }
            addView(new ImportSupplierView(Integer.valueOf(objectId), conversionDate));
        } else if (params.length > 1) {
            objectId = params[1];
            addView(new ImportSupplierView(Integer.valueOf(objectId), conversionDate));
        }

    }
}
