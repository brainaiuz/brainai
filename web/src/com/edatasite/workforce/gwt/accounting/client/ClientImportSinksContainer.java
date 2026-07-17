package com.edatasite.workforce.gwt.accounting.client;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.ImportClientView;

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
public class ClientImportSinksContainer extends SinksContainer {

    public ClientImportSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        String objectId = null;
        Date conversionDate = null;
        if (params.length > 1) {
            if (params.length > 2) {
                try {
                    conversionDate = new DateNonConvertable(DateUtils.parseLongFormat(params[2])).getNonConvertedDate();
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
            objectId = params[1];
            addView(new ImportClientView(Integer.valueOf(objectId), conversionDate));
        }

    }
}
