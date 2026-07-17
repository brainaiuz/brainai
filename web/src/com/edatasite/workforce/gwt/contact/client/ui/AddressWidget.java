
package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.CompanyConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.CountryStates;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 9/6/11
 * Time: 12:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddressWidget extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final boolean withName;
    private final boolean withGoogleRelations;
    private GBox mainBox;
    private Address address = new Address();
    private TextBox name;
    private DataListBox googleRelations;
    private TextBox street;
    private TextBox streetB;
    private TextBox city;
    private DataListBox country;
    private DataListBox state;
    private TextBox postCode;
    private RadioButton primaryButton;
    private Anchor validateAddress;
    private GBoxRow validateAddressRow;

    private Command countryListBoxChangedListener;

    private HTML nameViewMode, googleRelationsViewMode, streetViewMode, streetBViewMode, cityViewMode, countryViewMode, stateViewMode, postCodeViewMode, primaryButtonViewMode;

    private final String uniqueID;

    private AddressWidget copy;
    private KpiModal addressPopup;
    public static SelectItem[] countriesList;
    public static SelectItem[] statesList;
    private final boolean isViewMode;
    private final boolean isRequired;
    private final CrmStrings crmStrings = CrmStrings.App.get();

    public AddressWidget(Address address, String uniqueID, boolean withName, boolean withGoogleRelations, boolean isViewMode, boolean isRequired) {
        this.withName = withName;
        this.withGoogleRelations = withGoogleRelations;
        this.address = address == null ? new Address() : address;
        this.uniqueID = uniqueID == null ? "address" : uniqueID;
        this.isViewMode = isViewMode;
        this.isRequired = isRequired;
        initialize();
        initAddress();
        initWidget(mainBox);
    }

    public AddressWidget(Address address, boolean isContactAddress, String uniqueID, boolean isViewMode, boolean isRequired) {
        this(address, uniqueID, true, isContactAddress, isViewMode, isRequired);
    }

    boolean disableIfLinkedAddress = false;

    private void initialize() {
        if (isViewMode) {
            initializeViewMode();
            return;
        }
        disableIfLinkedAddress = true;
        mainBox = new GBox();
        name = new TextBox();
        name.ensureDebugId("address-name");
        BlurHandler blurHandler = blurEvent -> {
            if (getCopy() != null) {
                getCopy().setAddress(getAddress());
            }
        };
        name.addBlurHandler(blurHandler);
        googleRelations = new DataListBox();
        googleRelations.ensureDebugId("googleRelations");
        googleRelations.setAllowFirstItem(false);
        googleRelations.setWithoutNullLabel(true);
        googleRelations.setItems(getAddressTypes());
        googleRelations.setEnabled(disableIfLinkedAddress);
        street = new TextBox();
        street.setEnabled(disableIfLinkedAddress);
        street.ensureDebugId("street");
        streetB = new TextBox();
        streetB.setEnabled(disableIfLinkedAddress);
        streetB.ensureDebugId("streetB");
        city = new TextBox();
        city.setEnabled(disableIfLinkedAddress);
        city.ensureDebugId("city");
        country = new DataListBox();
        country.ensureDebugId("country");
        country.setEnabled(disableIfLinkedAddress);
        state = new DataListBox();
        state.ensureDebugId("state");
        state.setEnabled(disableIfLinkedAddress);
        postCode = new TextBox();
        postCode.ensureDebugId("postCode");
        postCode.setEnabled(disableIfLinkedAddress);
        primaryButton = new KpiRadioButton(uniqueID, wfmStrings.primaryAddress());
        primaryButton.ensureDebugId("primaryButton");

        if (withGoogleRelations) {
            mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.addressType(), googleRelations)));
        }
        if (withName) {
            mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.addressName(), name)));
        }
        mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.addressLine1() + (isRequired ? "<em class='redTitle'>*</em>" : "") + ":", street)));
        mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.addressLine2() + ":", streetB)));
        mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.city() + (isRequired ? "<em class='redTitle'>*</em>" : "") + ":", city)));
        mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.country(), country)));
        mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.state(), state)));
        mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.postCode() + (isRequired ? "<em class='redTitle'>*</em>" : "") + ":", postCode)));

        validateAddress = new Anchor(wfmStrings.validateAddress());
        validateAddress.addClickHandler(event -> {

            int errors = 0;
            if (!Validation.validateTextBoxRequired(street)) {
                errors++;
            }
            if (!Validation.validateTextBoxRequired(streetB)) {
                errors++;
            }
            if (!Validation.validateTextBoxRequired(city)) {
                errors++;
            }
            if (!Validation.validateTextBoxRequired(postCode)) {
                errors++;
            }

            if (errors > 0) {
                return;
            }

            CommonService.App.get().validateAddressByUSPS(getAddress(), new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    WfmWindow.alert("Address is not valid");
                }

                @Override
                public void onSuccess(String result) {
                    if ("SUCCESS".equals(result)) {
                        WfmWindow.info("Address is valid");
                    } else {
                        WfmWindow.alert((result != null && !"".equals(result.trim())) ? result : "Address is not valid");
                    }
                }
            });
        });
        validateAddressRow = new GBoxRow(new GBoxItem(validateAddress).setStyleNoBorder(true));
        mainBox.add(validateAddressRow);
        validateAddressRow.setVisible(false);

        FlowPanel p = new FlowPanel();
        p.add(primaryButton);
        mainBox.add(new GBoxRow(new GBoxItem(p).setStyleNoBorder(true)));
        setCountriesStates(null, null);
    }

    public static SelectItem[] getAddressTypes() {
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem(AddressReference.WORK.getId(), wfmStrings.contactwork());
        items[1] = new SelectItem(AddressReference.HOME.getId(), wfmStrings.contacthome());
        items[2] = new SelectItem(AddressReference.OTHER.getId(), wfmStrings.other());
        return items;
    }

    private void initializeViewMode() {
        mainBox = new GBox();
        nameViewMode = new HTML();
        googleRelationsViewMode = new HTML();
        streetViewMode = new HTML();
        streetBViewMode = new HTML();
        cityViewMode = new HTML();
        countryViewMode = new HTML();
        stateViewMode = new HTML();
        postCodeViewMode = new HTML();
        primaryButtonViewMode = new HTML();

        int row = 0;
        if (withName) {
            mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.name(), nameViewMode)));
        }
        if (withGoogleRelations) {
            mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.type(), googleRelationsViewMode)));
        }
        mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.streetAddress1(), streetViewMode)));
        mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.streetAddress2(), streetBViewMode)));
        mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.city(), cityViewMode)));
        mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.country(), countryViewMode)));
        mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.state(), stateViewMode)));
        mainBox.add(new GBoxRow(new GBoxItem(wfmStrings.postCode(), postCodeViewMode)));
        mainBox.add(new GBoxRow(new GBoxItem(crmStrings.isPrimaryWidthDot(), primaryButtonViewMode)));
    }

    public void setCountriesStates(SelectItem[] countries, SelectItem[] states) {
        if (!isViewMode) {
            if (countriesList == null) {
                countriesList = countries;
            }
            if (statesList == null) {
                statesList = states;
            }

            if (country != null) {
                country.addValueChangeHandler(event -> {
                    validateAddressLink();
                    if (countryListBoxChangedListener != null) {
                        countryListBoxChangedListener.execute();
                    }
                });
            }
            if (state != null) {
                state.addValueChangeHandler(event -> validateAddressLink());
            }

            CountryStates countryStates = new CountryStates(country, state);
            countryStates.setCountriesStates(countries, states);
            setAddress(getAddress(true));
            countryStates.checkForStates(country, state);
            if (getAddress(true).getCountryId() != null && getAddress(true).getStateId() != null) {
                state.setSelected(getAddress(true).getStateId());
                if (!disableIfLinkedAddress) {
                    state.setEnabled(true);
                }
            }
            validateAddressLink();
        }
    }

    private void drawAddressesPopup(ArrayList<Address> addresses) {
        if (addressPopup == null) {
            addressPopup = new KpiModal();
            addressPopup.setCloseButton(true);
            addressPopup.setSize(350, 150);
            addressPopup.setScrollable(true);
            addressPopup.setTitle(wfmStrings.chooseAddress());
        }
        addressPopup.clear();
        VerticalPanel vp = new VerticalPanel();
        for (final Address address : addresses) {
            SimpleLink link = new SimpleLink(address.toString());
            link.addClickHandler(clickEvent -> {
                setAddress(address);
                addressPopup.close();
            });
            vp.add(link);
        }
        addressPopup.add(vp);
        addressPopup.center();
    }

    private boolean isNotEmpty(String text) {
        return text != null && !"".equals(text);
    }

    public boolean isNotEmpty() {
        if (isRequired) {
            return isNotEmpty(street.getText()) && isNotEmpty(city.getText()) && isNotEmpty(postCode.getText());
        }
        return isNotEmpty(street.getText()) || isNotEmpty(streetB.getText()) || isNotEmpty(city.getText()) || isNotEmpty(postCode.getText()) || (country.getSelectedId() != null && isNotEmpty(country.getSelectedItem().getName())) || (state.getSelectedItem() != null && isNotEmpty(state.getSelectedItem().getName()));
    }

    private void initAddress() {
        if (address != null) {
            if (isViewMode) {
                initAddressViewMode();
                return;
            }
            name.setText(address.getName());
            googleRelations.setSelected(address.getRelationType());
            if (address.getAddress() != null) {
                street.setText(address.getAddress());
            }
            if (address.getAddressb() != null) {
                streetB.setText(address.getAddressb());
            }
            if (address.getCity() != null) {
                city.setText(address.getCity());
            }
            if (!uniqueID.endsWith("add") || !(CompanyConstants.C8032.equals(Utils.getEncryptedCompanyID()) ||
//                    CompanyConstants.C20738.equals(Utils.getEncryptedCompanyID()) ||
                    CompanyConstants.C43501.equals(Utils.getEncryptedCompanyID()))) {
                country.setSelected(address.getCountryId());
            }
            if (address.getCountryId() != null) {
                state.setSelected(address.getStateId());
            }
            if (address.getZipCode() != null) {
                postCode.setText(address.getZipCode());
            }
            primaryButton.setValue(address.isPrimary());
        }
        validateAddressLink();
    }

    private void validateAddressLink() {
        validateAddressRow.setVisible(country.getSelectedId() != null && country.getSelectedId().equals(46) && state.getSelectedId() != null);
    }

    private void initAddressViewMode() {
        if (address.getName() != null) {
            nameViewMode.setText(address.getName());
        }
        if (address.getRelationType() != null) {
            switch (address.getRelationType()) {
                case Constants.G_HOME:
                    googleRelationsViewMode.setHTML(wfmStrings.contacthome());
                    break;
                case Constants.G_WORK:
                    googleRelationsViewMode.setHTML(wfmStrings.contactwork());
                    break;
                case Constants.G_OTHER:
                    googleRelationsViewMode.setHTML(wfmStrings.other());
                    break;
            }
        }
        //googleRelationsViewMode.setHTML(address.getRelationType() + "");
        if (address.getAddress() != null) {
            streetViewMode.setHTML(address.getAddress());
        }
        if (address.getAddressb() != null) {
            streetBViewMode.setHTML(address.getAddressb());
        }
        if (address.getCity() != null) {
            cityViewMode.setHTML(address.getCity());
        }
        if (address.getCountryId() != null) {
            countryViewMode.setHTML(address.getCountry());
        }
        stateViewMode.setHTML(address.getState());
        if (address.getZipCode() != null) {
            postCodeViewMode.setHTML(address.getZipCode());
        }
        primaryButtonViewMode.setHTML(address.isPrimary() ? wfmStrings.yes() : wfmStrings.no());
    }

    public Address getAddress(boolean... returnExistingOne) {
        if (returnExistingOne != null && returnExistingOne.length > 0 && returnExistingOne[0]) {
            return address;
        }
        Address oldAddress = address;
        address = new Address(address.getObjectID());
        address.setAddress(!wfmStrings.addressLine1().equals(street.getText()) ? street.getText() : null);
        address.setAddressb(!wfmStrings.addressLine2().equals(streetB.getText()) ? streetB.getText() : null);
        address.setCity(!wfmStrings.city().equals(city.getText()) ? city.getText() : null);
        if (country.getSelectedId() != null) {
            address.setCountry(country.getSelectedItem().getName());
            address.setCountryId(country.getSelectedItem().getId());
            if (state.getSelectedId() != null) {
                address.setState(state.getSelectedItem().getName());
                address.setStateId(state.getSelectedId());
            }
        }
        address.setZipCode(!wfmStrings.postCode().equals(postCode.getText()) ? postCode.getText() : null);
        address.setName(name.getText());
        address.setRelationType(googleRelations.getSelectedId(true));
        address.setPrimary(primaryButton.getValue());
        address.setLinkedAddress(oldAddress.isLinkedAddress());
        address.setLinkedAddressID(oldAddress.getLinkedAddressID());
        return address;
    }

    public void setCountryListBoxChangedListener(Command countryListBoxChangedListener) {
        this.countryListBoxChangedListener = countryListBoxChangedListener;
    }

    public void setAddress(Address address) {
        this.address = address;
        initAddress();
    }

    public AddressWidget createCopy() {
        copy = new AddressWidget(getAddress().clone(), uniqueID + "_", withName, withGoogleRelations, false, false);
        copy.setCountriesStates(countriesList, statesList);
        copy.setEnabled(false);
        return copy;
    }

    public AddressWidget getCopy() {
        return this.copy;
    }

    public void removeCopy() {
        if (this.copy != null) {
            this.copy.setEnabled(true);
        }
        this.copy = null;
    }

    public void setEnabled(boolean bool) {
        name.setEnabled(bool);
        googleRelations.setEnabled(bool);
        street.setEnabled(bool);
        streetB.setEnabled(bool);
        city.setEnabled(bool);
        country.setEnabled(bool);
        state.setEnabled(bool);
        postCode.setEnabled(bool);
        primaryButton.setEnabled(bool);
    }

    public DataListBox getCountry() {
        return country;
    }
}
