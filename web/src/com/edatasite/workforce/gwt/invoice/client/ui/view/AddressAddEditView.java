package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.CountryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.StateLookUp;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.TextBox;

/**
 * User: Sherzod
 * Date: 5/24/11
 * Time: 12:16 PM
 */
public class AddressAddEditView extends KpiModal {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private TextBox name;
    private TextBox address;
    private TextBox address2;
    private TextBox city;
//    private CountryStates countryStates;
    private CountryLookUp country;
    private StateLookUp state;
    private TextBox zipcode;

    private KpiCheckBox copyBillingAddressToMailingAddress;

    private Integer clientSupplierID;
    private final Integer objectID;
    private final boolean isClient;
    private boolean isCompany;
    private final boolean isBilling;
    final private ExtendedCommand listener;

    private WfmButton2 save;
    private WfmButton2 cancel;
    private Address.EntityType entityType = Address.EntityType.CrmAccount;

    public AddressAddEditView(Integer clientSupplierID, Integer objectID, boolean isClient, boolean isBilling, ExtendedCommand listener) {
        this.clientSupplierID = clientSupplierID;
        this.objectID = objectID;
        this.isClient = isClient;
        this.isBilling = isBilling;
        this.listener = listener;
        initForm();
    }

    public AddressAddEditView(boolean isCompany, Integer selectedId, boolean isClient, boolean isBilling, ExtendedCommand companymailingCommand) {
        this.isCompany = isCompany;
        this.objectID = selectedId;
        this.isClient = isClient;
        this.isBilling = isBilling;
        this.listener = companymailingCommand;
        if (this.isCompany) {
            this.entityType = Address.EntityType.Company;
        }
        initForm();
    }

    private void initForm() {
        name = new TextBox();
        address = new TextBox();
        address2 = new TextBox();
        city = new TextBox();

        country = new CountryLookUp();
        state = new StateLookUp();
        zipcode = new TextBox();
        copyBillingAddressToMailingAddress = new KpiCheckBox(isBilling ? accountingStrings.copyBillingAddressToMailingAddress() : accountingStrings.copyMailingAddressToBillingAddress(), true);

        country.getSuggestBox().addSelectionHandler(event -> setCountryState());

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        save.addClickHandler(clickEvent -> {
            save.setEnabled(false);
            if (!validated()) {
                save.setEnabled(true);
                return;
            }

            final Address data = new Address();
            data.setObjectID(objectID);
            data.setName(name.getText());
            data.setAddress(address.getText());
            data.setAddressb(address2.getText());
            data.setCity(city.getText());
            data.setCountryId(country.getSelectedItemID());
            data.setStateId(state.getSelectedItemID());
            data.setZipCode(zipcode.getText());
            ///clientSupplierID = null bo`lsa company address deb hisoblanadi
            LoadingPanel.loading(true, this);
            ClientService.App.get().saveAddress(data, clientSupplierID, isClient, isBilling, entityType, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    save.setEnabled(true);
                }

                @Override
                public void success(final Integer id) {
                    LoadingPanel.loading(false);
                    if (isClient && objectID == null && copyBillingAddressToMailingAddress.getValue()) {
                        //save mailing address (or copy billing address to mailing address)
                        ClientService.App.get().saveAddress(data, clientSupplierID, isClient, !isBilling, entityType, new AbstractAsyncCallback<Integer>() {
                            @Override
                            public void failure(Throwable throwable) {
                                save.setEnabled(true);
                            }

                            @Override
                            public void success(Integer result) {
                                save.setEnabled(true);
                                if (AddressAddEditView.this.listener != null) {
                                    AddressAddEditView.this.listener.execute(id, result);
                                }
                                close();
                            }
                        });
                    } else {
                        save.setEnabled(true);
                        if (AddressAddEditView.this.listener != null) {
                            AddressAddEditView.this.listener.execute(id);
                        }
                        close();
                    }
                }
            });
        });

        cancel.addClickHandler(clickEvent -> close());

        if (objectID != null) {
            LoadingPanel.loading(true, this);
            ClientService.App.get().editAddress(objectID, new AbstractAsyncCallback<Address>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(Address data) {
                    LoadingPanel.loading(false);
                    if (data.getName() != null) {
                        name.setText(data.getName());
                    }
                    if (data.getAddress() != null) {
                        address.setText(data.getAddress());
                    }
                    if (data.getAddressb() != null) {
                        address2.setText(data.getAddressb());
                    }
                    if (data.getCity() != null) {
                        city.setText(data.getCity());
                    }
                    if (data.getZipCode() != null) {
                        zipcode.setText(data.getZipCode());
                    }

                    if (data.getCountryId() != null) {
                        country.addItem(new SelectItem(data.getCountryId(), data.getCountry()));
                    }
                    setCountryState();
                    if (data.getStateId() != null) {
                        state.addItem(new SelectItem(data.getStateId(), data.getState()));
                    }
                }
            });
        }

        GColumn cityCol = new GColumn(GColumnEnum.COL_6);
        cityCol.add(new FormGroup(wfmStrings.city(), city));

        GColumn stateCol = new GColumn(GColumnEnum.COL_6);
        stateCol.add(new FormGroup(wfmStrings.state(), state));

        GColumn countryCol = new GColumn(GColumnEnum.COL_6);
        countryCol.add(new FormGroup(wfmStrings.country(), country));

        GColumn postcodeCol = new GColumn(GColumnEnum.COL_6);
        postcodeCol.add(new FormGroup(wfmStrings.postCode(), zipcode));

        GRow formRow1 = new GRow();
        GRow formRow2 = new GRow();

        formRow1.add(cityCol);
        formRow1.add(stateCol);

        formRow2.add(countryCol);
        formRow2.add(postcodeCol);

        GColumn column = new GColumn(GColumnEnum.COL_12);

        column.add(new FormGroup(wfmStrings.name(), name));
        column.add(new FormGroup(wfmStrings.address(), address));
        column.add(new FormGroup(wfmStrings.address2(), address2));
        column.add(formRow1);
        column.add(formRow2);
        if (isClient && objectID == null) {
            column.add(new FormGroup("&nbsp;", copyBillingAddressToMailingAddress));
        }

        addButton(cancel);
        addButton(save);

        setTitle((objectID != null ? wfmStrings.edit() + " " : wfmStrings.add() + " ") + (isBilling ? wfmStrings.billingAddress().toLowerCase() : wfmStrings.mailingAddress().toLowerCase()));
        setWidth("500px");
        add(new GRow(column));
        open();
    }

    private boolean validated() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(name)) {
            errors++;
        }
        if (isEmpty(address.getText()) && isEmpty(address2.getText()) && isEmpty(city.getText())
                && !country.isSelected() && !state.isSelected() && isEmpty(zipcode.getText())) {
            Info.show(accountingMessages.thereIsNoDataToSave(), Info.Type.WARNING);
            errors++;
        }
        return errors <= 0;
    }

    private boolean isEmpty(String text) {
        return text == null || "".equals(text.trim());
    }
    private void setCountryState() {
        state.setCountryId(country.getSelectedItemID());
    }
}