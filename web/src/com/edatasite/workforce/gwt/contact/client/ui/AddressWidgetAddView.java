package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.CompanyConstants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.Div;

import java.util.Collections;
import java.util.LinkedHashMap;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.LANGUAGE_FOR_USER;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SA;

public class AddressWidgetAddView extends Composite {
    protected static final CommonServiceAsync commonService = CommonService.App.get();
    protected static final AllInOneServiceAsync service = AllInOneService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static SelectItem[] countriesList;
    public static SelectItem[] statesList;
    public LinkedHashMap<Integer, GBox> mainBoxes = new LinkedHashMap<>();
    public Address address = new Address();
    public TextBox name;
    public TextBox street;
    public TextBox streetB;
    public TextBox city;
    public WfmDropdown cityDistrict;
    public Div cityContainer;
    private boolean cityDistrictEnabled = false;
    private boolean cityDistrictMode = false;
    private final boolean withName;
    private final boolean withGoogleRelations;
    private final String uniqueID;
    public WfmDropdown country;
    public WfmDropdown state;
    public TextBox postCode;
    public KpiRadioButton primaryButton;
    boolean disableIfLinkedAddress = false;
    private final boolean isViewMode;
    private final boolean isRequired;
    private DataListBox googleRelations;
    private Anchor validateAddress;
    private GBoxRow validateAddressRow;
    private Command onLinesAdded;
    private Command onLinesRemoved;
    private Command countryListBoxChangedListener;
    private HTML nameViewMode, googleRelationsViewMode, streetViewMode, streetBViewMode, cityViewMode, countryViewMode, stateViewMode, postCodeViewMode, primaryButtonViewMode;
    private final GBox box = new GBox();
    private final boolean isKSACompany;

    {
        if (SA.equalsIgnoreCase(Utils.getCompanyrCountryCode())) {
            Utils.isVatRegistered();
        }
        isKSACompany = true;
    }

    private AddressWidgetAddView copy;
    // citySubdivisionName,plotIdentification,buildingNumber fields reqired for SA companies
    public TextBox citySubdivisionName;
    public TextBox plotIdentification;
    public TextBox buildingNumber;
    private int count = 0;
    private boolean isCountryChange = false;
    private HTML citySubdivisionNameViewMode, plotIdentificationViewMode, buildingNumberViewMode;
    public boolean dontValidateForSaudiRequirements = false;
    private boolean isClientAddress = false;
    private FormGroup buildingNumberFormGroup;
    private FormGroup plotIdentificationFormGroup;
    private FormGroup citySubdivisionFormGroup;

    public AddressWidgetAddView(Address address, String uniqueID, boolean withName, boolean withGoogleRelations, boolean isViewMode, boolean isRequired) {
        this(address, uniqueID, withName, withGoogleRelations, isViewMode, isRequired, false);
    }

    public AddressWidgetAddView(Address address, String uniqueID, boolean withName, boolean withGoogleRelations, boolean isViewMode, boolean isRequired, boolean isClientAddress) {
        this.withName = withName;
        this.withGoogleRelations = withGoogleRelations;
        this.address = address == null ? new Address() : address;
        this.uniqueID = uniqueID == null ? "address" : uniqueID;
        this.isViewMode = isViewMode;
        this.isRequired = isRequired;
        this.isClientAddress = isClientAddress;
        initialize();
    }


    public AddressWidgetAddView(Address address, boolean isContactAddress, String uniqueID, boolean isViewMode, boolean isRequired) {
        this(address, uniqueID, true, isContactAddress, isViewMode, isRequired);
    }

    public AddressWidgetAddView(Address address, boolean isContactAddress, String uniqueID, boolean isViewMode, boolean isRequired, boolean isClientAddress) {
        this(address, uniqueID, true, isContactAddress, isViewMode, isRequired, isClientAddress);
    }

    public static SelectItem[] getAddressTypes() {
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem(AddressReference.WORK.getId(), wfmStrings.contactwork());
        items[1] = new SelectItem(AddressReference.HOME.getId(), wfmStrings.contacthome());
        items[2] = new SelectItem(AddressReference.OTHER.getId(), wfmStrings.other());
        return items;
    }

    private void initialize() {
        disableIfLinkedAddress = true;
        name = new TextBox();
        name.ensureDebugId(uniqueID + count + "_address-name");
        BlurHandler blurHandler = blurEvent -> {
            if (getCopy() != null) {
                getCopy().setAddress(getAddress());
            }
        };
        name.addBlurHandler(blurHandler);
        street = new TextBox();
        street.setEnabled(disableIfLinkedAddress);
        street.ensureDebugId(uniqueID + count + "_street");
        streetB = new TextBox();
        streetB.setEnabled(disableIfLinkedAddress);
        streetB.ensureDebugId(uniqueID + count + "_streetB");
        city = new TextBox();
        city.setEnabled(disableIfLinkedAddress);
        city.ensureDebugId(uniqueID + count + "_city");
        cityDistrict = new WfmDropdown();
        cityDistrict.ensureDebugId(uniqueID + count + "_cityDistrict");
        cityDistrict.setEnabled(disableIfLinkedAddress);
        cityDistrict.setPlaceholder(wfmStrings.pleaseSelect());
        cityContainer = new Div();
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
        });
        state = new WfmDropdown();
        state.ensureDebugId("state");
        state.setEnabled(disableIfLinkedAddress);
        state.addValueChangeHandler(changeEvent -> initCityDistrictList(state.getSingleValue() != null ? state.getSingleValue().getId() : null));
        postCode = new TextBox();
        postCode.ensureDebugId(uniqueID + count + "_postCode");
        postCode.setEnabled(disableIfLinkedAddress);
        primaryButton = new KpiRadioButton(uniqueID != null ? uniqueID : "primaryAddress", wfmStrings.primary());
        primaryButton.ensureDebugId(uniqueID);
        primaryButton.addValueChangeHandler(valueChangeEvent -> {
            address.setPrimary(valueChangeEvent.getValue());
        });
//        if (ServerUtils.isKSACompany())
        plotIdentification = new TextBox();
        plotIdentification.setEnabled(true);
        plotIdentification.ensureDebugId(uniqueID + count + "_plotIdentification");
        buildingNumber = new TextBox();
        buildingNumber.setEnabled(true);
        buildingNumber.ensureDebugId(uniqueID + count + "_buildingNumber");
        citySubdivisionName = new TextBox();
        citySubdivisionName.setEnabled(true);
        citySubdivisionName.ensureDebugId(uniqueID + count + "_citySubdivisionName");
        count++;
        fill();
        initWidget(new Div());
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

    /**
     * Cascading region->district selection is opt-in: the parent form decides whether the
     * city field is rendered as the plain TextBox or as the cityContainer with the swap logic.
     */
    public void enableCityDistrict() {
        cityDistrictEnabled = true;
        cityContainer.add(city);
    }

    private void initCityDistrictList(Integer regionId) {
        if (!cityDistrictEnabled) {
            return;
        }
        cityDistrict.clear();
        if (regionId == null) {
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
        if (!cityDistrictEnabled) {
            return;
        }
        cityDistrictMode = districtsAvailable;
        cityContainer.clear();
        if (districtsAvailable) {
            cityContainer.add(cityDistrict);
        } else {
            cityContainer.add(city);
        }
    }

    private String getCityText() {
        if (cityDistrictMode) {
            return cityDistrict.getSingleValue() != null ? cityDistrict.getSingleValue().getName() : "";
        }
        return city.getText();
    }

    private void fill() {
        initCountryList();
        name.setValue(address.getName() != null ? address.getName() : "");
        street.setValue(address.getAddress());
        streetB.setValue(address.getAddressb());
        city.setValue(address.getCity());
        country.setSelected(address.getCountryId());
        state.setSelected(address.getStateId());
        postCode.setValue(address.getZipCode());
        primaryButton.setValue(address.isPrimary(), true);
        if (isKSACompany) {
            buildingNumber.setValue(address.getBuildingNumber());
            citySubdivisionName.setValue(address.getCitySubdivisionName());
            plotIdentification.setValue(address.getPlotIdentification());
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
                if (result != null) {
                    for (SelectItem selectItem : result) {
                        country.addItem(selectItem.getName(), selectItem);
                        country.setSelected(selectItem.getSelectedId());
                        if (!SA.equalsIgnoreCase(selectItem.getDescription())) {
                            dontValidateForSaudiRequirements = isClientAddress && true;
                        } else {
                            dontValidateForSaudiRequirements = false;
                        }
                    }
                }
                if (country.getSelected() != null) {
                    initStateList(country.getSelectedId());
                }
                if (address != null && address.getCountryId() != null) {
                    country.setValue(Collections.singletonList(new SelectItem(address.getCountryId())));
                    initStateList(address.getCountryId());
                }
            }
        });
    }

    private boolean isNotEmpty(String text) {
        return text != null && !"".equals(text);
    }

    public boolean isNotEmpty() {
        if (isRequired) {
            return isNotEmpty(street.getText()) && isNotEmpty(getCityText()) && isNotEmpty(postCode.getText());
        }
        Boolean bool = Boolean.FALSE;
        if (isKSACompany) {
            bool = isNotEmpty(buildingNumber.getText()) && isNotEmpty(plotIdentification.getText()) && isNotEmpty(citySubdivisionName.getText());
        }
        return !bool || isNotEmpty(street.getText()) || isNotEmpty(streetB.getText()) || isNotEmpty(getCityText()) || isNotEmpty(postCode.getText()) || (country.getSelectedId() != null && isNotEmpty(country.getSelectedItem().getName())) || (state.getSelectedItem() != null && isNotEmpty(state.getSelectedItem().getName()));
    }

    private void initAddress() {
        if (address != null) {
            name.setText(address.getName());
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
            if (isKSACompany) {
                if (address.getPlotIdentification() != null) {
                    plotIdentification.setText(address.getPlotIdentification());
                }
                if (address.getCitySubdivisionName() != null) {
                    citySubdivisionName.setText(address.getCitySubdivisionName());
                }
                if (address.getBuildingNumber() != null) {
                    buildingNumber.setText(address.getBuildingNumber());
                }
            }
            primaryButton.setValue(address.isPrimary());
        }
        validateAddressLink();
    }

    private void validateAddressLink() {
        /*validateAddressRow.setVisible(country.getSelectedId() != null && country.getSelectedId().equals(46) && state.getSelectedId() != null);*/
    }

    public Address getAddress(boolean... returnExistingOne) {
        if (returnExistingOne != null && returnExistingOne.length > 0 && returnExistingOne[0]) {
            return address;
        }
        Address oldAddress = address;
        address = new Address(address.getObjectID());
        address.setAddress(!wfmStrings.addressLine1().equals(street.getText()) ? street.getText() : null);
        address.setAddressb(!wfmStrings.addressLine2().equals(streetB.getText()) ? streetB.getText() : null);
        if (cityDistrictMode) {
            address.setCity(cityDistrict.getSingleValue() != null ? cityDistrict.getSingleValue().getName() : oldAddress.getCity());
        } else {
            address.setCity(!wfmStrings.city().equals(city.getText()) ? city.getText() : null);
        }
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
        address.setPrimary(primaryButton.getValue());
        address.setLinkedAddress(oldAddress.isLinkedAddress());
        address.setLinkedAddressID(oldAddress.getLinkedAddressID());
        if (isKSACompany) {
            address.setCitySubdivisionName(!wfmStrings.citySubdivisionName().equals(citySubdivisionName.getText()) ? citySubdivisionName.getText() : null);
            address.setPlotIdentification(!wfmStrings.plotIdentification().equals(plotIdentification.getText()) ? plotIdentification.getText() : null);
            address.setBuildingNumber(!wfmStrings.buildingNumber().equals(buildingNumber.getText()) ? buildingNumber.getText() : null);
        }
        return address;
    }

    public AddressWidgetAddView getCopy() {
        return this.copy;
    }

    public LinkedHashMap<Integer, GBox> getList() {
        return mainBoxes;
    }

    public void setAddress(Address address) {
        this.address = address;
        initAddress();
    }

    public void setEnabled(boolean bool) {
        name.setEnabled(bool);
        street.setEnabled(bool);
        streetB.setEnabled(bool);
        city.setEnabled(bool);
        cityDistrict.setEnabled(bool);
        country.setEnabled(bool);
        state.setEnabled(bool);
        postCode.setEnabled(bool);
        primaryButton.setEnabled(bool);
        if (isKSACompany) {
            citySubdivisionName.setEnabled(bool);
            plotIdentification.setEnabled(bool);
            buildingNumber.setEnabled(bool);
        }
    }

    public WfmDropdown getCountry() {
        return country;
    }

    public boolean isClientAddress() {
        return isClientAddress;
    }

    public void setClientAddress(boolean clientAddress) {
        isClientAddress = clientAddress;
    }
}
