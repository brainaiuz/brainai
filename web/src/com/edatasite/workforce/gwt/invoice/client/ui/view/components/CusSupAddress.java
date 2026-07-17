package com.edatasite.workforce.gwt.invoice.client.ui.view.components;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

public class CusSupAddress extends Composite {
    interface AddressUiBinder extends UiBinder<Widget, CusSupAddress> {
    }

    private static AddressUiBinder ourUiBinder = GWT.create(AddressUiBinder.class);

    @UiField(provided = true)
    DataListBox addressList;

    @UiField
    MaterialLink editAddressLink;
    @UiField
    MaterialLink addAddressLink;

    @UiField
    Div addressDescription;

    public CusSupAddress() {
        addressList = new DataListBox();
        addressList.setItems(new SelectItem[]{});

        initWidget(ourUiBinder.createAndBindUi(this));

        Icon editIcon = new Icon();
        editIcon.setClass("ficon--edit");
        editAddressLink.add(editIcon);

        Icon addIcon = new Icon();
        addIcon.setClass("ficon--plus");
        addAddressLink.add(addIcon);

        addressList.addValueChangeHandler(ch -> {

            if (addressList.getSelectedId() != null) {
                editAddressLink.setVisible(true);
            } else {
                editAddressLink.setVisible(false);
            }
        });
    }

    public MaterialLink getEditAddressLink() {
        return editAddressLink;
    }

    public MaterialLink getAddAddressLink() {
        return addAddressLink;
    }

    public DataListBox getAddressList() {
        return addressList;
    }

    public void setAddressDescription(String address) {
        addressDescription.getElement().setInnerHTML(address);
    }
}