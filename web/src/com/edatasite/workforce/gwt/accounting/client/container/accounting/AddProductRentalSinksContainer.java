package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.RentalProductlAddEditView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.PermissionDeniedView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Iftixor
 * Date: 09.08.2021
 * Time: 12:10:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddProductRentalSinksContainer extends SinksContainer implements Constants {
    public AddProductRentalSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        Integer productId = null;
        if (params.length == 2) {
            productId = Integer.valueOf(params[1]);
        }

        if (params.length == 1 && !(Utils.hasPermission(PermissionConstants.ACCOUNTING_RENTAL_ADD))) {
            addView(new PermissionDeniedView("You do not have permission to add Rental Product"));
        } else {
            addView(new RentalProductlAddEditView(productId));
        }

    }
}
