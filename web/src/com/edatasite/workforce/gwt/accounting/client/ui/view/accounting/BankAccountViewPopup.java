package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.google.gwt.dom.client.Style;

/**
 * Created by Dilshod Madrahimov on 5/25/15 12:05 PM
 */
public class BankAccountViewPopup extends KpiModal {
    BankAccountViewForm bankAccountViewForm;
    private Integer objectId;


    public BankAccountViewPopup(Integer objectId) {
        this.objectId = objectId;
        setSize(900, 400);
        setScrollable(true);
        setTitle(Property.get(Constants.BANKACCOUNT, WfmStrings.App.get().summaryView(), WfmStrings.App.get().bankAccount()));
        getScrollPanel().getElement().getStyle().setOverflowX(Style.Overflow.HIDDEN);
        init();
        add(bankAccountViewForm);
    }

    private void init() {
        bankAccountViewForm = new BankAccountViewForm(objectId, () -> close());
        bankAccountViewForm.onInitialize();

    }
}
