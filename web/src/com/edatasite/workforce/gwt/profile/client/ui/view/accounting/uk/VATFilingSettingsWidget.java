package com.edatasite.workforce.gwt.profile.client.ui.view.accounting.uk;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.FinancialSettingsItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.client.ui.html.Br;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

public class VATFilingSettingsWidget extends Composite {

    interface VATFilingSettingsWidgetUiBinder extends UiBinder<HTMLPanel, VATFilingSettingsWidget> {
    }

    private static VATFilingSettingsWidgetUiBinder uiBinder = GWT.create(VATFilingSettingsWidgetUiBinder.class);
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    @UiField
    Div header;
    @UiField
    Div vatReturnFilingContainer;
    @UiField
    Span vatReturnFiling;

    private KpiRadioButton hmrcBox;
    private KpiRadioButton ownBox;

    public VATFilingSettingsWidget() {
        initWidget(uiBinder.createAndBindUi(this));
        initialize();
    }

    private void initialize() {
        header.addClickHandler((e) -> {
            if (header.getStyleName().contains("active")) {
                header.removeStyleName("active");
                header.getParent().removeStyleName("active");
            } else {
                header.addStyleName("active");
                header.getParent().addStyleName("active");
            }
        });
        vatReturnFiling.setText("VAT Return Filing");

        hmrcBox = new KpiRadioButton("vatReturnFiling", accountingStrings.fileThroughHMRC());
        hmrcBox.addValueChangeHandler(valueChangeEvent -> onSelect());
        ownBox = new KpiRadioButton("vatReturnFiling", accountingStrings.fileOnMyOwn());
        vatReturnFilingContainer.add(hmrcBox);
        vatReturnFilingContainer.add(new Br());
        vatReturnFilingContainer.add(ownBox);
    }

    public void setData(FinancialSettingsItem financialSettingsItem) {
        if (financialSettingsItem.getSubmitVatManually() != null) {
            if (financialSettingsItem.getSubmitVatManually()) {
                ownBox.setValue(true);
            } else {
                hmrcBox.setValue(true);
            }
        }
    }

    public FinancialSettingsItem getData(FinancialSettingsItem financialSettingsItem) {
        if (ownBox.getValue() || hmrcBox.getValue()) {
            financialSettingsItem.setSubmitVatManually(ownBox.getValue());
        }
        return financialSettingsItem;
    }

    private void onSelect() {
        if (hmrcBox.getValue()) {
            new HMRCAuthorizationModal();
        }
    }
}
