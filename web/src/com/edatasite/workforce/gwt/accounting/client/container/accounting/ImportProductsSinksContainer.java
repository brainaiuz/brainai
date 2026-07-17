package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ImportProductsView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Sep 21, 2010
 * Time: 1:35:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportProductsSinksContainer extends SinksContainer {

    public ImportProductsSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        Integer objectId = null;
        String type = null;
        String fromView = null;

        if (params.length > 1) {
            try {
                objectId = Integer.parseInt(params[1]);
            } catch (NumberFormatException e) {

            }
        }

        if (params.length > 2) {
            type = params[2];
        }
        if (params.length > 3) {
            fromView = params[3];
        }

        addView(new ImportProductsView(objectId, type, fromView));
    }
}