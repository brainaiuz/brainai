package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.AssemblyItemListview;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.BuildAssemblyItemListview;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class ProductionSinksContainer extends SinksContainer implements PermissionConstants, AccountingConstants {

    public ProductionSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(ACCOUNTING_ASSEMBLY_ITEM_LIST)) {
            addView(new AssemblyItemListview());
        }
        if (Utils.hasPermission(ACCOUNTING_BUILD_ASSEMBLY_LIST)) {
            addView(new BuildAssemblyItemListview());
        }    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
