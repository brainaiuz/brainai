package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.google.gwt.user.client.Command;

/**
 * Created by Fatxulla on 30.11.2015 12:05 PM
 */
public class BrandViewPopup extends KpiModal {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    AddBrandView brandView;
    ExtendedCommand providerCommand;

    public BrandViewPopup(ExtendedCommand popupCommand) {
        setTitle(accountingStrings.addBrand());
        this.providerCommand = popupCommand;
        init();
        add(brandView);
        open();
    }

    private void init() {
        Command closeCommand = () -> close();
        brandView = new AddBrandView(providerCommand, closeCommand, this);
        brandView.onInitialize();

    }
}
