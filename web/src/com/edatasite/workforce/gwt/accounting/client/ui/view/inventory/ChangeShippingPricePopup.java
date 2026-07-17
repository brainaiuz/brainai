package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.google.gwt.user.client.ui.TextBox;

import java.math.BigDecimal;

/**
 * Created by Normurod on 10/4/2016.
 */
public class ChangeShippingPricePopup extends KpiModal {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private TextBox txtName;
    private TextBox price;
    private WfmButton2 btnSave, btnCancel;

    private ShippingMethod shippingMethod;
    private ShippingMethodCommand providerCommand;
    private BigDecimal shippingPrice;

    public ChangeShippingPricePopup(ShippingMethod shippingMethod, BigDecimal shippingPrice, ShippingMethodCommand command) {
        super();
        this.shippingMethod = shippingMethod;
        this.shippingPrice = shippingPrice;
        this.providerCommand = command;
        initInternal();
    }

    private void initInternal() {
        txtName = new TextBox();
        txtName.setEnabled(false);
        txtName.setText(shippingMethod.getName());

        price = new TextBox();
        Validation.addNumericKeyboardListener(price, 4);
        price.setText(AccountingUtils.get().formatPrice(shippingPrice));

        btnSave = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        btnSave.addClickHandler(clickEvent -> {
            close();
            shippingMethod.setPrice(AccountingUtils.get().parseToBigDecimal(price.getText()));
            providerCommand.execute(shippingMethod);
        });
        btnCancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        btnCancel.addClickHandler(clickEvent -> close());

        FormGroup nameField = new FormGroup(wfmStrings.name(), txtName);
        FormGroup priceField = new FormGroup(wfmStrings.price(), price);
        GColumn column = new GColumn(GColumnEnum.COL_12, nameField, priceField);
        add(new GRow(column));

        setWidth("400px");
        setTitle("Change Shipping Price");
        open();

        addButton(btnCancel);
        addButton(btnSave);
    }
}
