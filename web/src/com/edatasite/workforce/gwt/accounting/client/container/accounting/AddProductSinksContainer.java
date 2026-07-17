package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.EditProductView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.PermissionDeniedView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 7, 2009
 * Time: 4:53:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddProductSinksContainer extends SinksContainer implements Constants {
    public AddProductSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {

        EditProductView productView = new EditProductView();
        if (params.length > 3) {
            if (params[1].equals(COPY_FROM_EXISTING)) {
                if (params[2].equals(FROM_INVENTORY)) {
                    productView.setExternalId(Integer.valueOf(params[3]));
                    productView.setFromInventory(true);
                } else if (params[2].equals(FROM_ASSEMBLY)) {
                    productView.setExternalId(Integer.valueOf(params[3]));
                    productView.setFromAssembly(true);
                }
            }
        } else if (params.length > 2) {
            if (params[2].equals(BY_CATEGORY)) {
                productView.setCategoryID(Integer.valueOf(params[1]));
            } else if (params[2].equals(BY_STOREFRONT)) {
                productView.setStorefrontID(Integer.valueOf(params[1]));
            } else if (params[1].equals(COPY_FROM_EXISTING)) {
                productView.setExternalId(Integer.valueOf(params[2]));
                productView.setCopied(true);
            }
        } else if (params.length > 1) {
            if (params[1].equals(FROM_INVENTORY)) {
                productView.setFromInventory(true);
            } else if (params[1].equals(FROM_ASSEMBLY)) {
                productView.setFromAssembly(true);
            } else {
                productView.setProductType(Integer.valueOf(params[1]));
            }
        }
        if (params.length == 1 && !Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_ADD)) {
            addView(new PermissionDeniedView("You do not have permission to add Product"));
        } else {
            addView(productView);
        }

    }
}
