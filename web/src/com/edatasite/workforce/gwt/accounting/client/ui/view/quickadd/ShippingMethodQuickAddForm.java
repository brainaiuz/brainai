package com.edatasite.workforce.gwt.accounting.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ShippingMethodCommand;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartTaxRateLookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Label;

public class ShippingMethodQuickAddForm extends KpiSideNavBox {
    interface ShippingMethodQuickAddFormUiBinder extends UiBinder<Widget, ShippingMethodQuickAddForm> {
    }

    private static final ShippingMethodQuickAddFormUiBinder ourUiBinder = GWT.create(ShippingMethodQuickAddFormUiBinder.class);
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiField
    HTMLPanel container;
    @UiField
    TextBox txtName;
    @UiField
    TextBox txtPrice;
    @UiField
    TextArea txtDescription;
    @UiField(provided = true)
    CurrencyWidget currencyWidget;
    @UiField(provided = true)
    SmartTaxRateLookUp taxLookUp;
    @UiField(provided = true)
    AccountsLookUp accountLookUp;
    @UiField
    Div pnlCustomer;
    @UiField
    Label nameLabel;
    @UiField
    Label descriptionLabel;
    @UiField
    Label priceLabel;
    @UiField
    Label currencyLabel;
    @UiField
    Label accountLabel;
    @UiField
    Label taxLabel;
    @UiField
    Label customerLabel;

    private MultiSelectEmployeeLookUp customerLookUp;
    private final Integer objectID;
    private final SelectItem customer;

    private WfmButton2 btnSave;
    private final ShippingMethodCommand providerCommand;

    public ShippingMethodQuickAddForm() {
        this(null);
    }

    public ShippingMethodQuickAddForm(Integer objectID) {
        this(objectID, null, null);
    }

    public ShippingMethodQuickAddForm(Integer objectID, SelectItem customer, ShippingMethodCommand command) {
        super(DEFAULT_WIDTH);
        this.objectID = objectID;
        this.customer = customer;
        this.providerCommand = command;

        initProvidedWidgets();
        ourUiBinder.createAndBindUi(this);

        addOpeningHandler(o -> loadData());

        show();
    }

    @Override
    public void show() {
        super.show();
        onInitialize();
    }

    private void loadData() {
        LoadingPanel.loading(true, getBody());
        InvoiceService.App.get().getShippingMethod(objectID, customer != null ? customer.getId() : null, new AbstractAsyncCallback<ShippingMethod>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                GWT.log(caught.getMessage());

            }

            public void success(final ShippingMethod result) {
                LoadingPanel.loading(false);
                initFormData(result);
            }
        });
    }

    private void initProvidedWidgets() {
        customerLookUp = new MultiSelectEmployeeLookUp();
        customerLookUp.getFilterParametrs().setType(CrmConstants.TYPE_CRM_CONTACT);
        customerLookUp.getFilterParametrs().setLimit(222);

        currencyWidget = new CurrencyWidget(objectID == null);
        currencyWidget.addListener(() -> customerLookUp.getFilterParametrs().setCurrencyID(currencyWidget.getCurrencyID()));
        currencyWidget.setLoadingContainer(getBody());

        taxLookUp = new SmartTaxRateLookUp(Constants.RECEIVABLE);
        taxLookUp.ensureDebugId("taxRate-listBox");
        //taxLookUp.setLinkCommand(() -> addTaxView = new TaxViewPopup(o -> taxLookUp.addTaxItem((TaxItem) o)));
        taxLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> taxLookUp.islink());

        accountLookUp = new AccountsLookUp(Constants.REVENUE);
        accountLookUp.ensureDebugId("accounts-lookUp");
    }

    private void onInitialize() {
        clear();

        pnlCustomer.add(customerLookUp);

        //header
        addHeader(new HTML(objectID != null ? accountingStrings.addEditShippingMethod() : accountingStrings.addShippingMethod()));

        //field labels
        nameLabel.setText(wfmStrings.name());
        descriptionLabel.setText(wfmStrings.description());
        priceLabel.setText(wfmStrings.price());
        accountLabel.setText(wfmStrings.account());
        taxLabel.setText(wfmStrings.taxRate());
        currencyLabel.setText(wfmStrings.currency());
        customerLabel.setText(wfmStrings.customers());

        //body
        addBody(container);

        btnSave = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        btnSave.addClickHandler(ch -> {
            enableBtns(false);

            if (validateForm()) {
                save();
            } else {
                enableBtns(true);
            }
        });

        //footer
        addFooter(btnSave);
    }

    private void initFormData(ShippingMethod shippingMethod) {
        txtName.setText(shippingMethod.getName());
        txtDescription.setText(shippingMethod.getDescription());

        if (shippingMethod.getPrice() != null) {
            txtPrice.setText(AccountingUtils.get().formatPrice(shippingMethod.getPrice()));
        }
        if (shippingMethod.getAccount() != null) {
            accountLookUp.addItem(shippingMethod.getAccount());
        }
        if (shippingMethod.getTaxItem() != null) {
            taxLookUp.addTaxItem(shippingMethod.getTaxItem());
        }

        currencyWidget.setCurrency(shippingMethod.getCurrencyId(), shippingMethod.getExchangeRate());

        if (shippingMethod.getAppliedClients() != null && shippingMethod.getAppliedClients().length > 0) {
            customerLookUp.getFilterParametrs().setType(CrmConstants.TYPE_CRM_CONTACT);
            customerLookUp.setSelectedItems(shippingMethod.getAppliedClients());
        } else if (customer != null) {
            customerLookUp.getFilterParametrs().setType(CrmConstants.TYPE_CRM_CONTACT);
            customerLookUp.getFilterParametrs().setCurrencyID(currencyWidget.getCurrencyID());
            customerLookUp.setSelectedItems(customer);
        } else {
            customerLookUp.getFilterParametrs().setType(CrmConstants.TYPE_CRM_CONTACT);
        }
    }

    private void save() {
        LoadingPanel.loading(true, getBody());
        InvoiceService.App.get().saveShippingMethod(getFormData(), new AbstractAsyncCallback<ShippingMethod>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), accountingStrings.shippingMethod()), Info.Type.WARNING);
            }

            public void success(ShippingMethod result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(objectID != null ? wfmStrings.messSuccessfullyUpdated() : wfmStrings.messSuccessfullySaved(), accountingStrings.shipping()), Info.Type.INFO);

                if (providerCommand != null) {
                    providerCommand.execute(result);
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SHIPPINGMETHOD_SAVED, result, null);
                remove();
            }
        });
    }

    private ShippingMethod getFormData() {
        ShippingMethod shippingMethodItem = new ShippingMethod();
        shippingMethodItem.setId(objectID);
        shippingMethodItem.setName(txtName.getText());
        shippingMethodItem.setDescription(txtDescription.getText());
        shippingMethodItem.setTaxItem(taxLookUp.getSelectedData());
        shippingMethodItem.setPrice(AccountingUtils.get().parseToBigDecimal(txtPrice.getText()));
        shippingMethodItem.setAccount(accountLookUp.getSelectedData());

        if (customerLookUp != null && customerLookUp.getSelectedItems() != null && !customerLookUp.getSelectedItems().isEmpty()) {
            shippingMethodItem.setAppliedClients(customerLookUp.getSelectedItems().toArray(new SelectItem[customerLookUp.getSelectedItems().size()]));
        }
        shippingMethodItem.setCurrencyId(currencyWidget.getCurrencyID());
        shippingMethodItem.setExchangeRate(currencyWidget.getExchangeRate());
        return shippingMethodItem;
    }

    private boolean validateForm() {
        int errors = 0;

        if (!Validation.validateTextBoxRequired(txtName)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(txtPrice)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(accountLookUp)) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void enableBtns(boolean enable) {
        btnSave.setEnabled(enable);
    }

}