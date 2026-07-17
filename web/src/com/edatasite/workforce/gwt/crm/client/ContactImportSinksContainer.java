package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.ImportContactView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:23:07
 * To change this template use File | Settings | File Templates.
 */
public class ContactImportSinksContainer extends SinksContainer {

    public ContactImportSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        Integer objectId = params[1] != null ? Integer.parseInt(params[1]) : null;
        Integer mailListId = params.length > 2 && params[2] != null ? Integer.parseInt(params[2]) : null;
        addView(new ImportContactView(objectId, mailListId));
    }
}