package com.edatasite.workforce.gwt.profile.client.ui.view.accounting.uk;

import com.edatasite.workforce.gwt.accounting.client.rpc.FinancialSettingsItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.hmrc.HmrcMtdService;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.ui.view.accounting.VATSettingsWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Br;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

public class UKVatSettingsWidget extends VATSettingsWidget {
    interface UKVATSettingsWidgetUiBinder extends UiBinder<HTMLPanel, UKVatSettingsWidget> {
    }

    private static final UKVATSettingsWidgetUiBinder uiBinder = GWT.create(UKVATSettingsWidgetUiBinder.class);

    @UiField
    KpiCheckBox vatRegistered;
    @UiField
    HTMLPanel pnlContainer;
    @UiField
    HTMLPanel pnlVatConfigContainer;
    @UiField
    Span vatLabel;
    @UiField
    TextBox txtTaxIdDisplayName;
    @UiField
    TextBox txtTaxIdNumber;
    @UiField
    Span vatRegisteredOnLabel;
    @UiField
    DatePicker vatRegistrationDate;
    @UiField
    Span accountingBasisLabel;
    @UiField
    Div accountingBasisContainer;
    @UiField
    Span internationalTradeLabel;
    @UiField
    KpiCheckBox enableTradeOutside;
    @UiField
    Span reverseChargeLabel;
    @UiField
    Span vatNumberResult;
    @UiField
    KpiCheckBox enableReverseCharge;

    private KpiRadioButton accrualBox;
    private KpiRadioButton cashBox;

    @Override
    protected Widget getMainWidget() {
        return uiBinder.createAndBindUi(this);
    }

    @Override
    protected void initialize() {
        vatRegistered.setText(accountingStrings.vatRegistered());
        vatRegistered.addValueChangeHandler(valueChangeEvent -> showFields(valueChangeEvent.getValue()));

        vatLabel.setText(accountingStrings.vatRegistrationNumber());
        Validation.addNumericKeyboardListener(txtTaxIdNumber);
        txtTaxIdNumber.addValueChangeHandler(valueChangeEvent -> getTaxNumberIndo());

        vatRegisteredOnLabel.setText(accountingStrings.vatRegisteredOn());
        accountingBasisLabel.setText(accountingStrings.vatAccountingBasis());

        accrualBox = new KpiRadioButton("vatAccountingBasis", accountingStrings.accrual());
        accrualBox.setFormValue(AccountingConstants.ACCRUAL);

        cashBox = new KpiRadioButton("vatAccountingBasis", accountingStrings.cashVAT());
        cashBox.setFormValue(AccountingConstants.CASH);

        accountingBasisContainer.add(accrualBox);
        accountingBasisContainer.add(new Br());
        accountingBasisContainer.add(cashBox);

        internationalTradeLabel.setText(accountingStrings.internationalTrade());
        enableTradeOutside.setText(accountingStrings.internationalTradeUK());
        reverseChargeLabel.setText(accountingStrings.reverseCharge());
        enableReverseCharge.setText(accountingStrings.enableDomesticReverseCharge());
    }

    @Override
    public void fillSettings(FinancialSettingsItem settingsItem) {
        vatRegistered.setValue(settingsItem.getVatRegistered());
        txtTaxIdDisplayName.setText(settingsItem.getTaxIdDisplayNumber());
        txtTaxIdNumber.setText(settingsItem.getTaxIdNumber());

        if (settingsItem.getVatRegisteredOn() != null) {
            vatRegistrationDate.setDate(settingsItem.getVatRegisteredOn().getDate());
        }
        if (AccountingConstants.CASH.equals(settingsItem.getVatAccountingBasis())) {
            cashBox.setValue(true);
        } else {
            accrualBox.setValue(true);
        }
        enableTradeOutside.setValue(settingsItem.isEnableContractOutsite());
        enableReverseCharge.setValue(settingsItem.getEnableReverseCharge());
        showFields(settingsItem.getVatRegistered());
    }

    @Override
    public FinancialSettingsItem getSettingsData(FinancialSettingsItem settingsItem) {
        settingsItem.setVatRegistered(vatRegistered.getValue());
        settingsItem.setTaxIdNumber(txtTaxIdNumber.getText());
        settingsItem.setVatRegisteredOn(vatRegistrationDate.getDateAsNonConvertable());
        settingsItem.setVatAccountingBasis(accrualBox.getValue() ? accrualBox.getFormValue() : cashBox.getFormValue());
        settingsItem.setEnableContractOutsite(enableTradeOutside.getValue());
        settingsItem.setEnableReverseCharge(enableReverseCharge.getValue());
        settingsItem.setVatReturnReportVisibility(true);
        return settingsItem;
    }

    @Override
    public boolean validate() {
        int errors = 0;
        if (vatRegistered.getValue()) {
            if (!Validation.validateTextBoxRequired(txtTaxIdNumber)) {
                errors++;
            }
        }
        return errors == 0;
    }

    @Override
    public void initSpecificSettings(Command command) {

    }

    private void getTaxNumberIndo() {
        String vatNumber = txtTaxIdNumber.getText();
        if (vatNumber != null && vatNumber.length() > 8) {
            HmrcMtdService.App.get().checkVatNumber(vatNumber, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable throwable) {
                    Info.warn("Something went wrong");
                }

                @Override
                public void onSuccess(String vatInfo) {
                    vatNumberResult.setText(vatInfo);
                }
            });
        }
    }

    private void showFields(boolean show) {
        pnlContainer.clear();
        if (show) {
            pnlContainer.add(pnlVatConfigContainer);
        }
    }

}
