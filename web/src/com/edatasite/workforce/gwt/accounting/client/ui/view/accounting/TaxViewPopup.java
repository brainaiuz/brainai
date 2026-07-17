package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.google.gwt.user.client.Command;

/**
 * Created by Fatxulla on 30.11.2015 12:05 PM
 */
public class TaxViewPopup extends KpiModal {
    AddTaxView addTax2View;
    Integer objectID;
    private ObjectCommand popupCommand;
    private ExtendedCommand popupExtendCommand;
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public TaxViewPopup(ObjectCommand popupCommand) {
        setTitle(wfmStrings.addTaxRate());
        this.popupCommand = popupCommand;
        this.objectID = objectID;
        init();
        add(addTax2View);
        open();
    }


    private void init() {
        Command closeCommand = () -> close();
        if (objectID == null) {
            addTax2View = new AddTaxView(popupCommand, closeCommand, this);
        } else {
            addTax2View = new AddTaxView(objectID, popupExtendCommand, closeCommand, this);
        }

        addTax2View.onInitialize();

    }
}
