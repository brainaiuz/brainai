package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.PermissionDeniedView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rentalorder.RentalOrderAddEditView;

import java.util.LinkedList;


public class AddRentalOrderSinksContainer extends SinksContainer implements Constants {
    public AddRentalOrderSinksContainer(String name, String description, String[] params) {
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

        if (params.length == 1 && !(Utils.hasPermission(PermissionConstants.ACCOUNTING_RENTAL_ORDER_ADD))) {
            addView(new PermissionDeniedView("You do not have permission to add Rental Order"));
        } else {
            addView(new RentalOrderAddEditView(productId));
        }

    }
}
