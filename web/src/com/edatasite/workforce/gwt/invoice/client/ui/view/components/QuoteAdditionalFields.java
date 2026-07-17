package com.edatasite.workforce.gwt.invoice.client.ui.view.components;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceCustomFieldsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;

public class QuoteAdditionalFields extends Composite {
    interface QuoteAdditionalFieldsUiBinder extends UiBinder<HTMLPanel, QuoteAdditionalFields> {
    }

    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final QuoteAdditionalFieldsUiBinder ourUiBinder = GWT.create(QuoteAdditionalFieldsUiBinder.class);

    @UiField
    MaterialCollapsible optionsContainer;
    @UiField
    Span optionsContainerTitle;
    @UiField
    MaterialCollapsibleBody optionsBodyContainer;
    @UiField
    MaterialCollapsible customFieldContainer;
    @UiField
    Span customFieldContainerTitle;
    @UiField
    MaterialCollapsibleBody customFieldBodyContainer;
    @UiField
    MaterialCollapsibleItem customFieldContainerItem;
    @UiField
    MaterialCollapsible mailAddressFieldContainer;
    @UiField
    Span mailAddressFieldContainerTitle;
    @UiField
    MaterialCollapsibleBody mailAddressFieldBodyContainer;

    private InvoiceCustomFieldsView customFieldsView;

    public QuoteAdditionalFields(InvoiceAdvancedFields invoiceAdvancedFields) {
        this(invoiceAdvancedFields, true);
    }

    public QuoteAdditionalFields(InvoiceAdvancedFields invoiceAdvancedFields, Boolean address) {

        initWidget(ourUiBinder.createAndBindUi(this));
        optionsContainerTitle.setText(accountingStrings.otherFields());
        customFieldContainerTitle.setText(wfmStrings.customFields());
        mailAddressFieldContainerTitle.setText(wfmStrings.shippingAddress());

        for (Widget widget : invoiceAdvancedFields.getOptionWidgets()) {
            addToBodyContainer(widget);
        }
        if (address) {
            mailAddressFieldContainer.setVisible(true);
        }
    }

    public void addToBodyContainer(Widget widget) {
        if (widget == null) {
            return;
        }
        optionsBodyContainer.add(widget);
    }

    public void addToMailAddressBodyContainer(Widget widget) {
        if (widget == null) {
            return;
        }
        mailAddressFieldBodyContainer.add(widget);
    }

    public void createAndAppendQuoteCustomFieldsView(ViewAddFiledsCodeName viewCodeName, ArrayList<CompanyCustomFieldItem> getCustomFieldList) {
        customFieldContainer.setVisible(true);
        customFieldsView = new InvoiceCustomFieldsView(viewCodeName, getCustomFieldList, null, null);
        customFieldBodyContainer.clear();
        customFieldBodyContainer.add(customFieldsView);
    }

    public void initCustomFieldSummaryWidget(Widget customFieldSumaryWidget) {
        customFieldContainer.setVisible(true);
        customFieldContainer.setActive(0);
        customFieldBodyContainer.clear();
        customFieldBodyContainer.add(customFieldSumaryWidget);
    }

    public boolean validateCustomFieldRequiredFields() {
        if (customFieldsView == null) {
            return true;
        }
        return customFieldsView.validateRequiredFields();
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldsData() {
        if (customFieldsView == null) {
            return null;
        }
        return customFieldsView.getData();
    }

    public MaterialCollapsible getCustomFieldContainer() {
        return customFieldContainer;
    }

    public MaterialCollapsible getOptionsContainer() {
        return optionsContainer;
    }

    public MaterialCollapsible getMailAddressFieldContainer(){
        return mailAddressFieldContainer;
    }

}
