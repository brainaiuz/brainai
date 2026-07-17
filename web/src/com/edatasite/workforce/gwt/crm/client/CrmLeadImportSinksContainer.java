package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.ImportCrmLeadView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 24, 2009
 * Time: 11:39:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrmLeadImportSinksContainer extends SinksContainer {

    public CrmLeadImportSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        String objectId = null;
        if (params.length > 1) {
            objectId = params[1];
            addView(new ImportCrmLeadView(Integer.valueOf(objectId)));
        }

    }
}
