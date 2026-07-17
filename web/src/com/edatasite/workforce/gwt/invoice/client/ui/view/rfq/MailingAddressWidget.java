package com.edatasite.workforce.gwt.invoice.client.ui.view.rfq;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.ClientSupplierAddressData;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyAddress;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AddressAddEditView;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

public class MailingAddressWidget extends Composite {

    private DataListBox addressList;
    private final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private Integer selectedId;
    private SelectItem[] addresses;
    private Label addressHTML;

    public MailingAddressWidget() {
        onInitialize();
        CommonService.App.get().getCompanyAddress(new AbstractAsyncCallback<CompanyAddress>() {
            @Override
            public void success(CompanyAddress result) {
                if (result != null && result.getMailAddresses() != null) {
                    addresses = result.getMailAddresses();
                    setData();
                }
            }
        });
    }

    protected void onInitialize() {
        MaterialPanel rootElement = new MaterialPanel();
        rootElement.setWidth("500px");

        final ExtendedCommand companymailingCommand = new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
                if (id != null) {
                    TypeItem item = new TypeItem();
                    item.setMailAddressID(id);
                    initContactAddress(item);
                }
            }

            @Override
            public void execute(Integer id1, Integer id2) {
                if (id1 != null && id2 != null) {
                    TypeItem item = new TypeItem();
                    item.setMailAddressID(id1);
                    item.setBillAddressID(id2);
                    initContactAddress(item);
                }
            }
        };

        addressList = new DataListBox();
        addressList.getElement().setPropertyString("aria-hidden", "true");
        addressList.setWidth("145px");
        addressList.addValueChangeHandler(changeEvent -> addressHTML.setText(addressList.getSelectedItem().getDescription()));

        Div linkDiv = new Div();

        WfmButton2 editLink = new WfmButton2("", WfmButton2.BTN_WHITE);
        new KpiToolTip(editLink, wfmStrings.edit());
        editLink.addStyleDependentName("btn--icon ficon--edit");
        editLink.addClickHandler(event -> new AddressAddEditView(true, addressList.getSelectedId(), false, false, companymailingCommand));

        WfmButton2 addLink = new WfmButton2("", WfmButton2.BTN_WHITE);
        new KpiToolTip(addLink, wfmStrings.add());
        addLink.addStyleName("btn--icon ficon--plus");
        addLink.addClickHandler(event -> new AddressAddEditView(true, null, false, false, companymailingCommand));

        linkDiv.add(editLink);
        linkDiv.add(addLink);

        Label label = new Label(accountingStrings.mailing());

        addressHTML = new Label();
        Div addressData = new Div();
        addressData.addStyleName("address_data");
        addressData.add(addressHTML);

        setData();

        InputGroup inputGroup = new InputGroup(addressList, addressData);
        AdvancedInputGroup advancedInputGroup = new AdvancedInputGroup(null, inputGroup, linkDiv, true, true);
        advancedInputGroup.addStyleName("mailAddress_input-group");
        rootElement.add(advancedInputGroup);

        initWidget(rootElement);
    }

    private void setData() {
        if (addresses != null && addressList != null) {
            addressList.setItems(addresses);
            if (selectedId != null) {
                addressList.setSelected(selectedId);
            } else {
                for (SelectItem address : addresses) {
                    if (address.isSelected() || addresses.length < 2) {
                        addressList.setSelected(address.getId());
                        addressHTML.setText(address.getDescription());
                    }
                }
            }
        }
    }

    private void initContactAddress(TypeItem item) {
        LoadingPanel.loading(true);
        ClientService.App.get().getAddressData(item.getId(), false, Address.EntityType.Company, new AbstractAsyncCallback<ClientSupplierAddressData>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ClientSupplierAddressData result) {
                LoadingPanel.loading(false);
                addressHTML.setText("");
                if (result != null && result.getMailAddresses() != null) {
                    addressList.setItems(result.getMailAddresses());
                    if (addressList.isSomethingSelected()) {
                        addressHTML.setText(addressList.getSelectedItem().getDescription());
                    }
                }
            }
        });
    }

    public void setText(String addressAsString) {
        addressHTML.setText(addressAsString);
    }

    public void setSelected(Integer selectedId) {
        this.selectedId = selectedId;
        addressList.setSelected(selectedId);
    }

    public Integer getSelectedId() {
        return addressList.getSelectedId();
    }
}