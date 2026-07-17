package com.edatasite.workforce.gwt.core.client.ui.crm;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Power
 * Date: 5/22/12
 * Time: 7:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddressModal extends KpiModal implements Constants {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final AllInOneServiceAsync service = AllInOneService.App.get();
    protected static final CommonServiceAsync commonService = CommonService.App.get();
    protected static final WfmMessages wfmMessages = WfmMessages.App.get();
    private boolean withName = true;
    private boolean withGoogleRelations = true;

    private TextBox name;
    private DataListBox googleRelations;
    private TextBox street;
    private TextBox streetB;
    private TextBox city;
    private WfmDropdown country;
    private WfmDropdown state;
    private WfmDropdown cityDistrict;
    private TextBox postCode;
    private KpiSwitcher primaryButton;
    private final Address address;
    private final String uniqueID;
    public TextBox citySubdivisionName;
    public TextBox plotIdentification;
    private boolean initialized = false;
    boolean disableIfLinkedAddress = false;
    public TextBox buildingNumber;
    private Anchor validateAddress;
    private boolean isCountryChange = false;
    private ListingFilterParameter filterParametrs = new ListingFilterParameter();
    private final HashMap<String, Widget> errorWidgets = new HashMap<>();
    public boolean dontValidateForSaudiRequirements = false;
    private boolean isClientAddress = false;
    private FormGroup buildingNumberFormGroup;
    private FormGroup plotIdentificationFormGroup;
    private FormGroup citySubdivisionFormGroup;
    private FormGroup cityFormGroup;
    private FormGroup cityDistrictFormGroup;
    private boolean cityDistrictEnabled = false;
    private Boolean isB2B = false;

    public AddressModal(boolean withName, boolean withGoogleRelations, Address address, boolean disableIfLinkedAddress, String uniqueID, ListingFilterParameter filterParametrs, boolean isClientAddress) {
        this(withName, withGoogleRelations, address, disableIfLinkedAddress, uniqueID, filterParametrs, isClientAddress, false);
    }

    public AddressModal(boolean withName, boolean withGoogleRelations, Address address, boolean disableIfLinkedAddress, String uniqueID, ListingFilterParameter filterParametrs, boolean isClientAddress, boolean cityDistrictEnabled) {
        this.withName = withName;
        this.withGoogleRelations = withGoogleRelations;
        this.address = address;
        this.disableIfLinkedAddress = disableIfLinkedAddress;
        this.uniqueID = uniqueID;
        this.filterParametrs = filterParametrs;
        this.isClientAddress = isClientAddress;
        this.cityDistrictEnabled = cityDistrictEnabled;
        this.isB2B = filterParametrs.isShowHidden();
        init();
    }

    public void init() {
        setTitle(wfmStrings.addressInformation());
        initFields();
        addButton(new WfmButton2(wfmStrings.cancel(), event -> close()));
        addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> {
            if (Utils.isSaudiCompany() && Utils.isVatRegistered()) {
                if (validateSaudiAddress()) {
                    fillAddress();
                    save();
                    close();
                }
            } else {
                if (Utils.isSettings()) {
                    if (validate()) {
                        fillAddress();
                        save();
                        close();
                    }
                } else {
                    fillAddress();
                    save();
                    close();
                }

            }

        }));
        setWidth("450px");
        initCountryList();
        initialized = true;
    }

    public boolean isAddressFillForSaudi() {
        return validateSaudiAddress();
    }

    public boolean validate() {
        String errorMessage = "";
        int error = 0;
        if (!Validation.validateWfmDropdown(country)) {
            errorMessage = wfmStrings.fillRequiredField();
            error++;
        }
        if (error > 0) {
            Info.warn(errorMessage);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateSaudiAddress() {
        if (isClientAddress) {
            if (dontValidateForSaudiRequirements) {
                return true;
            }
        } else if (country.getSelectedItem() != null && !SA.equalsIgnoreCase(country.getSelectedItem().getCode())) {
            return true;
        }
        String errorMessage = "";
        int error = 0;
        if (!Validation.validateTextBoxRequired(plotIdentification) || (plotIdentification.getText().length() != 4)) {
            errorMessage = wfmMessages.widgetRequiredFormat(wfmStrings.plotIdentification(), "4");
            error++;
        } else if (!Validation.validateTextBoxRequired(buildingNumber) || (buildingNumber.getText().length() != 4)) {
            errorMessage = wfmMessages.widgetRequiredFormat(wfmStrings.buildingNumber(), "4");
            error++;
        } else if (!Validation.validateTextBoxRequired(citySubdivisionName)) {
            errorMessage = wfmMessages.fieldRequired(wfmStrings.citySubdivisionName());
            error++;
        } else if (!Validation.validateTextBoxRequired(postCode) || (postCode.getText().length() != 5)) {
            errorMessage = wfmMessages.widgetRequiredFormat(wfmStrings.postCode(), "5");
            error++;
        }else if (!Validation.validateTextBoxRequired(city) && isB2B ) {
            errorMessage = wfmMessages.widgetRequiredFormat(wfmStrings.city(), "");
            error++;
        }else if (!Validation.validateTextBoxRequired(street) && isB2B ) {
            errorMessage = wfmMessages.widgetRequiredFormat(wfmStrings.street(), "");
            error++;
        } else if (!Validation.validateWfmDropdownRequired(state,null,wfmStrings.street()) && isB2B ) {
            errorMessage = wfmMessages.widgetRequiredFormat(wfmStrings.street(), "");
            error++;
        } else if (state == null || state.getSelectedId() == null) {
            errorMessage = wfmMessages.fieldRequired(wfmStrings.state());
            error++;
        } else {
            errorMessage = wfmStrings.sorrySomethingWentWrong();
        }

        if (error > 0) {
            Info.warn(errorMessage);
            return false;
        } else {
            return true;
        }
    }

    private void save() {
        if (filterParametrs != null && filterParametrs.getObjectId() != null) {
            Integer type = uniqueID.contains("billPrimary") ? 0 : 1;
            CRMService.App.get().updateAddress(filterParametrs, address, type,
                    new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void failure(Throwable throwable) {

                        }

                        @Override
                        public void success(Boolean result) {
                            if (result) {
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.address()), Info.Type.INFO);
                            } else {
                                Info.show(wfmStrings.someErrorsOccurred(), Info.Type.WARNING);
                            }
                        }
                    });
        }
    }

    private void initCountryList() {
        ListingFilterParameter fb = new ListingFilterParameter();
        fb.setLanguage(Utils.userSettings.get(LANGUAGE_FOR_USER));
        commonService.getCountries(fb, true, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SelectItem[] result) {
                country.setPlaceholder(wfmStrings.pleaseSelect());
                state.setPlaceholder(wfmStrings.pleaseSelect());
                SelectItem selectedItem = null;
                if (result != null) {
                    for (SelectItem selectItem : result) {
                        country.addItem(selectItem.getName(), selectItem);
                        if (address != null && address.getCountryId() != null && address.getCountryId().equals(selectItem.getId())) {
                            selectedItem = selectItem;
                        }
                    }
                    if (!SA.equalsIgnoreCase(selectedItem.getDescription())) {
                        dontValidateForSaudiRequirements = isClientAddress && true;
                    } else {
                        dontValidateForSaudiRequirements = false;
                    }
                    visibilityOfSaudiRequiredFields(SA.equalsIgnoreCase(selectedItem.getDescription()));

                }
                if (address != null && address.getCountryId() != null) {
                    country.setValue(Collections.singletonList(new SelectItem(address.getCountryId())));
                    initStateList(address.getCountryId());
                }
            }
        });
    }

    private void visibilityOfSaudiRequiredFields(Boolean isVisible) {
        plotIdentificationFormGroup.setVisible(isVisible);
        buildingNumberFormGroup.setVisible(isVisible);
        citySubdivisionFormGroup.setVisible(isVisible);
    }

    private void initStateList(Integer countryId) {
        state.clear();
        initCityDistrictList(null);
        if (countryId != null) {
            ListingFilterParameter fb = new ListingFilterParameter();
            fb.setLanguage(Utils.userSettings.get(LANGUAGE_FOR_USER));
            commonService.getRegions(countryId, new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(SelectItem[] result) {
                    if (result != null) {
                        for (SelectItem region : result) {
                            state.addItem(region.getName(), region);
                        }
                    }
                    if (address != null && address.getStateId() != null) {
                        state.setSingleValue(new SelectItem(address.getStateId()));
                    }
                    initCityDistrictList(state.getSingleValue() != null ? state.getSingleValue().getId() : null);
                }
            });
        }
    }

    private void initCityDistrictList(Integer regionId) {
        cityDistrict.clear();
        if (!cityDistrictEnabled || regionId == null) {
            showCityDistrictDropdown(false);
            return;
        }
        ReportService.App.get().getCityOrDistrictByRegionId(regionId, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                showCityDistrictDropdown(false);
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                Integer selectedRegionId = state.getSingleValue() != null ? state.getSingleValue().getId() : null;
                if (!regionId.equals(selectedRegionId)) {
                    return;
                }
                if (result == null || result.length == 0) {
                    showCityDistrictDropdown(false);
                    return;
                }
                cityDistrict.clear();
                SelectItem selectedDistrict = null;
                for (SelectItem district : result) {
                    cityDistrict.addItem(district.getName(), district);
                    if (address != null && address.getCity() != null && address.getCity().equals(district.getName())) {
                        selectedDistrict = district;
                    }
                }
                if (selectedDistrict != null) {
                    cityDistrict.setSingleValue(selectedDistrict);
                }
                showCityDistrictDropdown(true);
            }
        });
    }

    private void showCityDistrictDropdown(boolean districtsAvailable) {
        cityDistrictFormGroup.setVisible(districtsAvailable);
        cityFormGroup.setVisible(!districtsAvailable);
    }

    private void fillAddress() {
        address.setName(name.getValue());
        address.setCountryId(country.getSingleValue() != null ? country.getSingleValue().getId() : null);
        address.setCountry(country.getSingleValue() != null ? country.getSingleValue().getName() : null);
        address.setCountryCode(country.getSingleValue() != null ? country.getSingleValue().getDescription() : null);
        if (cityDistrictFormGroup.isVisible()) {
            if (cityDistrict.getSingleValue() != null) {
                address.setCity(cityDistrict.getSingleValue().getName());
            }
        } else {
            address.setCity(city.getValue());
        }
        address.setStateId(state.getSingleValue() != null ? state.getSingleValue().getId() : null);
        if (address.getStateId() != null) {
            address.setState(state.getSingleValue() != null ? state.getSingleValue().getName() : null);
        }
        address.setAddress(street.getValue());
        address.setAddressb(streetB.getValue());
        address.setRelationType(googleRelations.getSelectedId(true));
        address.setZipCode(postCode.getValue());
        address.setPrimary(primaryButton.getValue());
        if (SA.equalsIgnoreCase(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            address.setCitySubdivisionName(citySubdivisionName.getValue());
            address.setPlotIdentification(plotIdentification.getValue());
            address.setBuildingNumber(buildingNumber.getValue());
        }
    }

    private void initFields() {
        disableIfLinkedAddress = true;//todo !address.isLinkedAddress();
        name = new TextBox();
        name.ensureDebugId("address-name");
        googleRelations = new DataListBox();
        googleRelations.ensureDebugId("googleRelations");
        googleRelations.addStyleName(DEFAULT_WIDTH);
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
        country = new WfmDropdown();
        country.ensureDebugId("country");
        country.setEnabled(disableIfLinkedAddress);
        country.addValueChangeHandler(changeEvent -> {
            this.isCountryChange = true;
            initStateList(country.getSingleValue() != null ? country.getSingleValue().getId() : null);
            if (!SA.equalsIgnoreCase(country.getSelectedItem().getDescription())) {
                dontValidateForSaudiRequirements = isClientAddress && true;
            } else {
                dontValidateForSaudiRequirements = false;
            }
            visibilityOfSaudiRequiredFields(SA.equalsIgnoreCase(country.getSelectedItem().getDescription()));

        });
        state = new WfmDropdown();
        state.ensureDebugId("state");
        state.setEnabled(disableIfLinkedAddress);
        state.addValueChangeHandler(changeEvent -> initCityDistrictList(state.getSingleValue() != null ? state.getSingleValue().getId() : null));
        cityDistrict = new WfmDropdown();
        cityDistrict.ensureDebugId("cityDistrict");
        cityDistrict.setEnabled(disableIfLinkedAddress);
        cityDistrict.setPlaceholder(wfmStrings.pleaseSelect());
        postCode = new TextBox();
        postCode.ensureDebugId("postCode");
        postCode.setEnabled(disableIfLinkedAddress);
        GRow stateAndPostCode = new GRow(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.state()+ "<em class='redTitle'>*</em>", state)),
                new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.postCode() + "<em class='redTitle'>*</em>", postCode)));
        primaryButton = new KpiSwitcher(null, null, false);
        primaryButton.ensureDebugId("primaryButton");
        buildingNumber = new TextBox();
        plotIdentification = new TextBox();
        citySubdivisionName = new TextBox();
        Validation.addNumericKeyboardListener(buildingNumber);
        Validation.addNumericKeyboardListener(plotIdentification);
//        Validation.addNumericKeyboardListener(postCode);

        buildingNumberFormGroup = new FormGroup(wfmStrings.buildingNumber(), buildingNumber);
        plotIdentificationFormGroup = new FormGroup(wfmStrings.plotIdentification(), plotIdentification);
        citySubdivisionFormGroup = new FormGroup(wfmStrings.citySubdivisionName(), citySubdivisionName);
        visibilityOfSaudiRequiredFields(SA.equalsIgnoreCase(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered());

        buildingNumber.setEnabled(disableIfLinkedAddress);
        plotIdentification.setEnabled(disableIfLinkedAddress);
        citySubdivisionName.setEnabled(disableIfLinkedAddress);
        if (withGoogleRelations) {
            addWidget(googleRelations, wfmStrings.addressType());
        }
        if (withName) {
            GRow row = new GRow();
            row.add(new GColumn(GColumnEnum.COL_9, new FormGroup(wfmStrings.addressName(), name)));
            row.add(new GColumn(GColumnEnum.COL_3, new FormGroup(wfmStrings.primary(), primaryButton)));
            add(row);
        } else {
            addWidget(primaryButton, wfmStrings.primary());
        }
        addWidget(street, wfmStrings.addressLine1()+ "<em class='redTitle'>*</em>");
        addWidget(streetB, wfmStrings.addressLine2());
        cityFormGroup = new FormGroup(wfmStrings.city() + "<em class='redTitle'>*</em>", city);
        cityDistrictFormGroup = new FormGroup(wfmStrings.cityOrDistrict() + "<em class='redTitle'>*</em>", cityDistrict);
        cityDistrictFormGroup.setVisible(false);
        add(cityFormGroup);
        add(cityDistrictFormGroup);
        addWidget(country, wfmStrings.country() + "<em class='redTitle'>*</em>");
        if (SA.equalsIgnoreCase(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            add(buildingNumberFormGroup);
            add(plotIdentificationFormGroup);
            add(citySubdivisionFormGroup);
        }
        add(stateAndPostCode);

        if (address != null) {
            name.setValue(address.getName());
            googleRelations.setSelected(address.getRelationType());
            street.setValue(address.getAddress());
            streetB.setValue(address.getAddressb());
            city.setValue(address.getCity());
            postCode.setValue(address.getZipCode());
            primaryButton.setValue(address.isPrimary(), true);
            if (SA.equalsIgnoreCase(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                plotIdentification.setValue(address.getPlotIdentification());
                citySubdivisionName.setValue(address.getCitySubdivisionName());
                buildingNumber.setValue(address.getBuildingNumber());
            }
        }
    }

    public static SelectItem[] getAddressTypes() {
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem(AddressReference.WORK.getId(), wfmStrings.contactwork());
        items[1] = new SelectItem(AddressReference.HOME.getId(), wfmStrings.contacthome());
        items[2] = new SelectItem(AddressReference.OTHER.getId(), wfmStrings.other());
        return items;
    }

    public void setCountryChange(boolean countryChange) {
        isCountryChange = countryChange;
    }

    public boolean isCountryChange() {
        return isCountryChange;
    }


    public boolean isClientAddress() {
        return isClientAddress;
    }

    public void setClientAddress(boolean clientAddress) {
        isClientAddress = clientAddress;
    }
}
