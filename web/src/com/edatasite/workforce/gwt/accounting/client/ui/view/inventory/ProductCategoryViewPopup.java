package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.google.gwt.user.client.Command;

/**
 * Created by Fatxulla on 30.11.2015 12:05 PM
 */
public class ProductCategoryViewPopup extends KpiModal {

    AddEditProductCategoryView productCategoryView;
    ExtendedCommand objectCommand;

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public ProductCategoryViewPopup(ExtendedCommand commandProvider, Integer storefrontID, boolean fromTraning) {
        setTitle(wfmStrings.addCategory());
        this.objectCommand = commandProvider;

        init();
        add(productCategoryView);
        open();
    }

    private void init() {
        Command closeCommand = () -> close();
        productCategoryView = new AddEditProductCategoryView(objectCommand, closeCommand, this);
        productCategoryView.onInitialize();

    }
}
