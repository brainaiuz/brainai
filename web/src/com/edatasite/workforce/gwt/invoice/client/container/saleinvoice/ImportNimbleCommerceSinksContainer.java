package com.edatasite.workforce.gwt.invoice.client.container.saleinvoice;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.ImportNimbleCommerceView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/15/12
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportNimbleCommerceSinksContainer extends SinksContainer {

    public ImportNimbleCommerceSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        String objectId = null;
        if (params.length > 1) {
            objectId = params[1];
            addView(new ImportNimbleCommerceView(Integer.valueOf(objectId)));
        }
    }
}
