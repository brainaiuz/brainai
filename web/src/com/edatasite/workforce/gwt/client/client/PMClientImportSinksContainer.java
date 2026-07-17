package com.edatasite.workforce.gwt.client.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.ImportClientView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 3, 2010
 * Time: 6:17:10 PM
 */
public class PMClientImportSinksContainer extends SinksContainer {

    public PMClientImportSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        String objectId = null;
        if (params.length > 1) {
            objectId = params[1];
            addView(new ImportClientView(Integer.valueOf(objectId), null));
        }
    }
}
