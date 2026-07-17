package com.edatasite.workforce.gwt.invoice.client.ui.view.components;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.client.client.ui.view.AccountsReceivablePayableLookUp;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentAddEditData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
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

public class InvoiceAdvancedOptions extends Composite {
    interface InvoiceAdvancedOptionsUiBinder extends UiBinder<HTMLPanel, InvoiceAdvancedOptions> {
    }

    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final InvoiceAdvancedOptionsUiBinder ourUiBinder = GWT.create(InvoiceAdvancedOptionsUiBinder.class);

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
    MaterialCollapsible addressFieldContainer;
    @UiField
    Span addressFieldContainerTitle;
    @UiField
    MaterialCollapsibleBody addressFieldBodyContainer;
    @UiField
    MaterialCollapsible mailAddressFieldContainer;
    @UiField
    Span mailAddressFieldContainerTitle;
    @UiField
    MaterialCollapsibleBody mailAddressFieldBodyContainer;

    private AccountsReceivablePayableLookUp receivablePayableLookUp;
    private ProjectLookUp relatedProjectLookUp;

    private InvoiceCustomFieldsView customFieldsView;

    public InvoiceAdvancedOptions(InvoiceAdvancedFields invoiceAdvancedFields) {
        this(invoiceAdvancedFields, true);
    }

    public InvoiceAdvancedOptions(InvoiceAdvancedFields invoiceAdvancedFields, Boolean address) {

        initWidget(ourUiBinder.createAndBindUi(this));
        optionsContainerTitle.setText(accountingStrings.otherFields());
        customFieldContainerTitle.setText(wfmStrings.customFields());
        addressFieldContainerTitle.setText(wfmStrings.billingAddress());
        mailAddressFieldContainerTitle.setText(wfmStrings.shippingAddress());

        if (!invoiceAdvancedFields.getOptionWidgets().isEmpty()) {
            optionsContainer.setVisible(true);
            for (Widget widget : invoiceAdvancedFields.getOptionWidgets()) {
                addToBodyContainer(widget);
            }
        }
        if (address) {
            addressFieldContainer.setVisible(true);
            mailAddressFieldContainer.setVisible(true);
        }
    }

    public void addToBodyContainer(Widget widget) {
        if (widget == null) {
            return;
        }
        optionsBodyContainer.add(widget);
    }

    public void addToAddressBodyContainer(Widget widget) {
        if (widget == null) {
            return;
        }
        addressFieldBodyContainer.add(widget);
    }

    public void addToMailAddressBodyContainer(Widget widget) {
        if (widget == null) {
            return;
        }
        mailAddressFieldBodyContainer.add(widget);
    }

    public void createAndAppendInvoiceCustomFieldsView(ViewAddFiledsCodeName viewCodeName, NewInvoice invoice) {

        if (invoice.getCustomFieldItems() != null && !invoice.getCustomFieldItems().isEmpty()) {
            customFieldContainer.setVisible(true);

            if (invoice.getID() != null && invoice.getID() > 0) {
                customFieldContainer.setActive(0);
            }
            customFieldsView = new InvoiceCustomFieldsView(viewCodeName, invoice.getCustomFieldItems(), null, null);
            customFieldBodyContainer.clear();
            customFieldBodyContainer.add(customFieldsView);
        }
    }

    public void createAndAppendExpenseCustomFieldsView(ViewAddFiledsCodeName viewCodeName, ExpenseReportsListItem expenseReportsListItem) {

        if (expenseReportsListItem.getCustomFieldItems() != null && !expenseReportsListItem.getCustomFieldItems().isEmpty()) {
            customFieldContainer.setVisible(true);

            if (expenseReportsListItem.getId() != null && expenseReportsListItem.getId() > 0) {
                customFieldContainer.setActive(0);
            }
            customFieldsView = new InvoiceCustomFieldsView(viewCodeName, expenseReportsListItem.getCustomFieldItems(), null, null);
            customFieldBodyContainer.clear();
            customFieldBodyContainer.add(customFieldsView);
        }
    }

    public void createAndAppendEventAndCallCustomFieldsView(ViewAddFiledsCodeName viewCodeName, ArrayList<CompanyCustomFieldItem> customFieldItems) {

        if (!customFieldItems.isEmpty()) {
            customFieldContainer.setVisible(true);

            customFieldsView = new InvoiceCustomFieldsView(viewCodeName, customFieldItems, null, null);
            customFieldBodyContainer.clear();
            customFieldBodyContainer.add(customFieldsView);
        }
    }

    public void createAndAppendBatchPaymentCustomFieldsView(ViewAddFiledsCodeName viewCodeName, BatchPaymentAddEditData batchPaymentAddEditData) {

        if (batchPaymentAddEditData.getData().getCustomFieldItems() != null && !batchPaymentAddEditData.getData().getCustomFieldItems().isEmpty()) {
            customFieldContainer.setVisible(true);

            if (batchPaymentAddEditData.getData().getObjectID() != null && batchPaymentAddEditData.getData().getObjectID() > 0) {
                customFieldContainer.setActive(0);
            }
            customFieldsView = new InvoiceCustomFieldsView(viewCodeName, batchPaymentAddEditData.getData().getCustomFieldItems(), null, null);
            customFieldBodyContainer.clear();
            customFieldBodyContainer.add(customFieldsView);
        }
    }

    public void createAndAppendBankTransferCustomFieldsView(ViewAddFiledsCodeName viewCodeName, NewManualTransaction newManualTransaction) {

        if (newManualTransaction.getCustomFieldItems() != null && !newManualTransaction.getCustomFieldItems().isEmpty()) {
            customFieldContainer.setVisible(true);

            if (newManualTransaction.getObjectId() != null && newManualTransaction.getObjectId() > 0) {
                customFieldContainer.setActive(0);
            }
            customFieldsView = new InvoiceCustomFieldsView(viewCodeName, newManualTransaction.getCustomFieldItems(), null, null);
            customFieldBodyContainer.clear();
            customFieldBodyContainer.add(customFieldsView);
        }
    }

    public void createAndAppendCustomFieldsView(ViewAddFiledsCodeName viewCodeName, ArrayList<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            customFieldContainer.setVisible(true);

            customFieldContainer.setActive(0);

            customFieldsView = new InvoiceCustomFieldsView(viewCodeName, customFieldItems, null, null);
            customFieldBodyContainer.clear();
            customFieldBodyContainer.add(customFieldsView);
        }
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

    public InvoiceCustomFieldsView getCustomFieldsView() {
        return this.customFieldsView;
    }
}
