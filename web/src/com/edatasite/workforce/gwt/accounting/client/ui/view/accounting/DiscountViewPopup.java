package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.google.gwt.user.client.Command;

/**
 * Created by Fatxulla on 30.11.2015 12:05 PM
 */
public class DiscountViewPopup extends KpiModal {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    EditDiscountView discountView;
    private Integer productID;
    private ExtendedCommand popupExtendCommand;

    public DiscountViewPopup(ExtendedCommand popupCommand) {
        this(popupCommand, null);
    }

    public DiscountViewPopup(ExtendedCommand popupCommand, Integer productID) {
        setTitle(accountingStrings.addDiscount());
        this.popupExtendCommand = popupCommand;
        this.productID = productID;
        init();
        add(discountView);
        open();
    }


    private void init() {
        Command closeCommand = this::close;
        discountView = new EditDiscountView(popupExtendCommand, productID, closeCommand, this);
        discountView.onInitialize();
    }
}
