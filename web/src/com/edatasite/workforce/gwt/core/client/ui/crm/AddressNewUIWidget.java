
package com.edatasite.workforce.gwt.core.client.ui.crm;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 22/03/18
 * Time: 12:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddressNewUIWidget extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    public KpiRadioButton primaryField;
    public TextBox nameField;
    public Div addressViewField;
    public WfmButton2 editButton;

    boolean disableIfLinkedAddress = false;
    private final boolean withName;
    private final boolean withGoogleRelations;
    private Address address = new Address();
    private AddressNewUIWidget copy;
    private final String uniqueID;
    private AddressModal addressPopup;
    private final boolean isViewMode;
    private final boolean isRequired;
    private AddressChange countryListBoxChangedListener;
    private ListingFilterParameter filterParametrs = new ListingFilterParameter();
    private boolean isClientAddress = false;
    private boolean cityDistrictEnabled = false;

    public AddressNewUIWidget(Address address, String uniqueID, boolean withName, boolean withGoogleRelations, boolean isViewMode, boolean isRequired, ListingFilterParameter filterParametrs) {
        this.withName = withName;
        this.withGoogleRelations = withGoogleRelations;
        this.address = address == null ? new Address() : address;
        this.uniqueID = uniqueID == null ? "address" : uniqueID;
        this.isViewMode = isViewMode;
        this.isRequired = isRequired;
        this.filterParametrs = filterParametrs;
        init();
    }

    public AddressNewUIWidget(Address address, boolean isContactAddress, String uniqueID, boolean isViewMode, boolean isRequired, ListingFilterParameter filterParametrs) {
        this(address, uniqueID, true, isContactAddress, isViewMode, isRequired, filterParametrs);
    }

    public AddressNewUIWidget(Address address, boolean isContactAddress, String uniqueID, boolean isViewMode, boolean isRequired, ListingFilterParameter filterParametrs, boolean isClientAdress) {
        this.withName = true;
        this.withGoogleRelations = isContactAddress;
        this.address = address == null ? new Address() : address;
        this.uniqueID = uniqueID == null ? "address" : uniqueID;
        this.isViewMode = isViewMode;
        this.isRequired = isRequired;
        this.filterParametrs = filterParametrs;
        this.isClientAddress = isClientAdress;
        init();
    }

    public AddressNewUIWidget(Address address, String uniqueID) {
        this(address, uniqueID, true, false, false, false, null);
    }

    private void init() {
        nameField = new TextBox();
        nameField.setPlaceHolder(wfmStrings.addressName());
        nameField.addStyleName("input-group-text");
        nameField.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NOWRAP);
        nameField.setWidth("auto");
        nameField.addBlurHandler(event -> {
            if (nameField.getValue() != null && nameField.getValue().length() > 0) {
                address.setName(nameField.getValue());
            }
        });
        primaryField = new KpiRadioButton(uniqueID != null ? uniqueID : "primaryAddress");
        primaryField.addValueChangeHandler(event -> {
            address.setPrimary(event.getValue());
            if (countryListBoxChangedListener != null) {
                countryListBoxChangedListener.execute(address);
            }
        });
        new KpiToolTip(primaryField, wfmStrings.primary());
//        primaryField.addStyleName("input-group-text");
        addressViewField = new Div("input-group-content input-group-text");
        editButton = new WfmButton2("", WfmButton2.BTN_WHITE);
        new KpiToolTip(editButton, wfmStrings.edit());
        editButton.addStyleName("ficon--edit");
        if (filterParametrs.isHasAccessToChange()) {
            addressViewField.addClickHandler(event -> edit());
            editButton.addClickHandler(event -> {
                edit();
            });
        }
        fill();
        initWidget(new Div());
    }

    public void setAddress(Address address) {
        this.address = address;
        fill();
    }

    private void fill() {
        Span span = new Span();
        span.getElement().addClassName("text-ellipsis");
        span.setText(address.asString());
        addressViewField.clear();
        addressViewField.add(span);
        nameField.setValue(address.getName() != null ? address.getName() : "");
        primaryField.setValue(address.isPrimary(),true);
        nameField.setEnabled(!isViewMode);
        primaryField.setEnabled(!isViewMode);

        if (addressPopup != null && addressPopup.isCountryChange() && countryListBoxChangedListener != null) {
            countryListBoxChangedListener.execute(address);
        }
    }

    public void edit() {
        addressPopup = new AddressModal(withName, withGoogleRelations, address, disableIfLinkedAddress, uniqueID, filterParametrs, isClientAddress, cityDistrictEnabled);
        addressPopup.center();
        addressPopup.addCloseHandler(event -> fill());
        addressPopup.addOpenHandler(event -> addressPopup.setCountryChange(false));
    }

    /**
     * Enables the region->district cascading city selection in the AddressModal opened by this widget.
     * Off by default; parent forms turn it on only for customer addresses.
     */
    public void setCityDistrictEnabled(boolean cityDistrictEnabled) {
        this.cityDistrictEnabled = cityDistrictEnabled;
    }

    public boolean validateAddressModelPopUp() {
        return addressPopup != null && addressPopup.isAddressFillForSaudi();
    }

    public boolean isNotEmpty() {
        return !getAddress().asString().isEmpty();
    }

    public Address getAddress() {
        address.setPrimary(primaryField.getValue());
        return address;
    }

    public void setEnabled(boolean bool) {
        nameField.setEnabled(bool);
    }

    public AddressNewUIWidget createCopy() {
        copy = new AddressNewUIWidget(getAddress().clone(), uniqueID + "_", withName, withGoogleRelations, false, false, filterParametrs);
        copy.setEnabled(false);
        return copy;
    }

    public AddressNewUIWidget getCopy() {
        return this.copy;
    }

    public void removeCopy() {
        if (this.copy != null) {
            this.copy.setEnabled(true);
        }
        this.copy = null;
    }

    public void setCountryListBoxChangedListener(AddressChange countryListBoxChangedListener) {
        this.countryListBoxChangedListener = countryListBoxChangedListener;
    }

    public Widget createWidget() {
        AdvancedInputGroup widgets = new AdvancedInputGroup(nameField, addressViewField, editButton, true, true);
        return widgets;
    }

    public interface AddressChange {
        void execute(Address address);
    }

    public boolean isClientAddress() {
        return isClientAddress;
    }

    public void setClientAddress(boolean clientAddress) {
        isClientAddress = clientAddress;
    }
}
