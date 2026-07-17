package com.edatasite.workforce.gwt.location.client.ui;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompLocationRpc;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.CountryStates;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.SelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.view.AddEditLocaleView;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.location.client.rpc.LocationService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * User: Dilshod
 * Date: 01.12.2009
 * Time: 17:53:46
 */
public class AddLocationView extends CustomForm2 implements Errors, Colapse {

    private final String location_add_edit_view = "add_edit_location_";
    private final int limitAll = 10000;
    private final HashSet<Integer> updatedEmployees = new HashSet<>();
    private final HashSet<Integer> teams = new HashSet<>();
    private final int limitSelected = 200;
    private Numbering code;
    private DataListBox country;
    private DataListBox state;
    private DataListBox cityDistrict;
    private TextBox name;
    private TextBox city;
    private TextBox latitude;
    private TextBox longitude;
    private TextBox radius;
    private TextBox email;
    private TextBox phone;
    private TextBox fax;
    private TextBox zipCode;
    private Command command;
    private KpiModal shell;
    private CountryStates cs = null;
    private Integer locationId;
    private SelectPanel employeesPanel;
    private int limit = 200;
    private int offset = 0;
    private int employeeCount = 0;
    private boolean isEmpty = false;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private FormHasCustomField customFieldUtil;
    private WfmButton2 locale;
    private FlexTable localedNameBox;
    private ReferenceLocale localeItem;
    private AddEditLocaleView localeView;
    private LocationLookUpWithCode parent;
    private CompLocationRpc compLocationRpc;
    private MultiSelectEmployeeLookUp ownersLookUp;
    private int offsetSelected = 0;
    private int countSelected = 0;
    private boolean isEmptySelected = false;

    public AddLocationView(Command command) {
        this.command = command;
        shell = new KpiModal();
        shell.setSize(500, 300);
        LoadingPanel.loading(true);
        asyncOnInitialize(new AbstractAsyncCallback<Widget>() {
            public void failure(Throwable reason) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.failedToDownloadCodeForThisWidget() + " (" + reason + ")", Info.Type.WARNING);
            }

            public void success(Widget result) {
                LoadingPanel.loading(false);
            }
        });
    }

    public AddLocationView() {
        super("addlocation", wfmStrings.addLocation());
    }

    public AddLocationView(Integer locationId) {
        super("edit", locationId != null ? (wfmStrings.edit() + " " + Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()).toLowerCase()) : wfmStrings.addLocation());
        this.locationId = locationId;
    }

    public static native void empoyeeListScrollDownEvent(AddLocationView view) /*-{
        var timerID;
        $wnd.jQuery(".treePanel-class").scroll(function () {
            clearTimeout(timerID);
            if ($wnd.jQuery(this).scrollTop() + $wnd.jQuery(this).innerHeight() + 100 >= $wnd.jQuery(this)[0].scrollHeight) {
                timerID = setTimeout(function () {
                    view.@com.edatasite.workforce.gwt.location.client.ui.AddLocationView::getEmployeeList()();
                }, 200)
            }
        });
    }-*/;

    public static native void selectedEmpoyeeListScrollDownEvent(AddLocationView view) /*-{
        var timerID;
        $wnd.jQuery("div.blue-border").scroll(function () {
            clearTimeout(timerID);
            if ($wnd.jQuery(this).scrollTop() + $wnd.jQuery(this).innerHeight() + 100 >= $wnd.jQuery(this)[0].scrollHeight) {
                timerID = setTimeout(function () {
                    view.@com.edatasite.workforce.gwt.location.client.ui.AddLocationView::getSelectedEmployees()();
                }, 200)
            }
        });
    }-*/;

    @Override
    public String getIconStyle() {
        return "location location-edit";
    }

    @Override
    protected Widget onInitialize() {
        if (locationId != null && container != null) {
            setCollapse(true);
        }
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Location, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                AddLocationView.super.onInitialize();
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYE_TREE_WIDGET_REFRESH, AddLocationView.this, (sender, args) -> getEmployeeList(true));

//        registerFields();
        return null;
    }

    @Override
    protected String getWikiCode() {
        return locationId == null ? PermissionConstants.HRMS_ADD_NEW_LOCATION : PermissionConstants.HRMS_EDIT_LOCATION;
    }

    @Override
    protected void getDataToFillFields() {
        if (locationId == null) {
            setDefaultValuesByFormProperty();
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.LOCATION_FORM;
    }

    @Override
    protected String getFormType() {
        return locationId != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void registerFields() {

        //location name
        name = new TextBox();
//        name.setWidth("98%");
        name.getElement().setId("name");
        name.addStyleName(Constants.DEFAULT_WIDTH);

        if (locationId != null) {
            parent = new LocationLookUpWithCode(locationId);
            parent.addStyleName(Constants.DEFAULT_WIDTH);
        } else {
            parent = new LocationLookUpWithCode();
            parent.addStyleName(Constants.DEFAULT_WIDTH);
        }
        locale = new WfmButton2(wfmStrings.vacancyLocale());
//        locale.setStyleName("font-style: italic;", true);
        locale.addClickHandler(event -> {
            if (localeView == null) {
                localeView = new AddEditLocaleView(name.getText(), localeItem);
            } else {
                localeView.setLocaleItem(localeItem);
                localeView.setNameValue(name.getText());
                localeView.showView();
            }
        });
//        locale.ensureDebugId(test_code_ID_name + "locale");
        localedNameBox = new FlexTable();
        localedNameBox.addStyleName("formLine-table");
        localedNameBox.setWidget(0, 0, name);
        localedNameBox.setWidget(0, 1, locale);
        localedNameBox.getCellFormatter().addStyleName(0, 1, "formLine-table__act");
//        localedNameBox.ensureDebugId(test_code_ID_name + "localedNameBox");
        //location city
        city = new TextBox();
        city.getElement().setId("city");
        city.addStyleName(Constants.DEFAULT_WIDTH);

        latitude = new TextBox();
        latitude.getElement().setId("latitude");
        latitude.addStyleName(Constants.MIN_DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(latitude, 7);

        longitude = new TextBox();
        longitude.getElement().setId("longitude");
        longitude.addStyleName(Constants.MIN_DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(longitude, 7);

        radius = new TextBox();
        radius.getElement().setId("radius");
        radius.addStyleName(Constants.MIN_DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(radius, 0);

        //location email
        email = new TextBox();
        email.getElement().setId("email");
        email.setMaxLength(99);
        email.addStyleName(Constants.DEFAULT_WIDTH);
        //location fax
        fax = new TextBox();
        fax.getElement().setId("fax");
        fax.setMaxLength(49);
        fax.addStyleName(Constants.DEFAULT_WIDTH);
        //location zip code
        zipCode = new TextBox();
        zipCode.getElement().setId("zipcode");
        zipCode.addStyleName(Constants.DEFAULT_WIDTH);
        //location phone
        phone = new TextBox();
        phone.getElement().setId("phone");
        phone.setMaxLength(49);
        phone.addStyleName(Constants.DEFAULT_WIDTH);
        //location country
        country = new DataListBox();
        country.getElement().setId("country");
        country.addStyleName(Constants.DEFAULT_WIDTH);
        //location state
        state = new DataListBox();
        state.getElement().setId("state");
        state.addStyleName(Constants.DEFAULT_WIDTH);
        state.setEnabled(false);

        cityDistrict = new DataListBox();
        cityDistrict.getElement().setId("cityDistric");
        cityDistrict.addStyleName(Constants.DEFAULT_WIDTH);
        cityDistrict.setEnabled(false);

        state.addValueChangeHandler(handler -> {
            getCityOrDistrictByRegionId(state.getSelectedId());
        });

        code = new Numbering(false);
        code.addStyleName(Constants.DEFAULT_WIDTH);
        if (locationId != null) {
            code.getTxtPrefix().setWidth("100%");
        }
        ownersLookUp = new MultiSelectEmployeeLookUp();
        ownersLookUp.getFilterParametrs().setType(LookUpConstants.LOCATION_OWNERS);
        ownersLookUp.addStyleName(Constants.DEFAULT_WIDTH);

        //location employees
        final TableColumn[] assignColumns = new TableColumn[2];
        assignColumns[0] = new TableColumn(wfmStrings.department(), wfmStrings.department());
        assignColumns[1] = new TableColumn(wfmStrings.action(), wfmStrings.action());
        employeesPanel = new SelectPanel(assignColumns);
        employeesPanel.setDefaultSettings();


        FlexTable buttonPanel = new FlexTable();
        buttonPanel.setCellPadding(5);
        buttonPanel.setCellSpacing(5);

        WfmForm locationForm = new WfmForm();
        locationForm.setCellPadding(15);
        locationForm.setCellSpacing(15);

        addTitleField(LOCATION.GENERAL_DETAILS, wfmStrings.generalInformation());
        name.ensureDebugId(location_add_edit_view + "name");
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.NAME) != null) {
            addField(LOCATION.NAME, localedNameBox, getTitle(formPropertyMap.get(LOCATION.NAME).isChanged() ? formPropertyMap.get(LOCATION.NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(LOCATION.NAME).isRequired()));
            name.setEnabled(!formPropertyMap.get(LOCATION.NAME).isDisabled());
        } else {
            addField(LOCATION.NAME, localedNameBox, getTitle(wfmStrings.name(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(LOCATION.PARENT) != null) {
            addField(LOCATION.PARENT, parent, getTitle(formPropertyMap.get(LOCATION.PARENT).isChanged() ? formPropertyMap.get(LOCATION.PARENT).getTitle() : wfmStrings.parent(), formPropertyMap.get(LOCATION.PARENT).isRequired()));
            parent.setEnabled(!formPropertyMap.get(LOCATION.PARENT).isDisabled());
        } else {
            addField(LOCATION.PARENT, parent, getTitle(wfmStrings.parent(), false));
        }

        code.ensureDebugId(location_add_edit_view + "code");
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.CODE) != null) {
            addField(LOCATION.CODE, code, getTitle(formPropertyMap.get(LOCATION.CODE).isChanged() ? formPropertyMap.get(LOCATION.CODE).getTitle() : wfmStrings.number(), formPropertyMap.get(LOCATION.CODE).isRequired()));
            code.setEnabled(!formPropertyMap.get(LOCATION.CODE).isDisabled());
        } else {
            addField(LOCATION.CODE, code, getTitle(wfmStrings.number(), true));
        }

        country.ensureDebugId(location_add_edit_view + "country");
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.COUNTRY) != null) {
            addField(LOCATION.COUNTRY, country, getTitle(formPropertyMap.get(LOCATION.COUNTRY).isChanged() ? formPropertyMap.get(LOCATION.COUNTRY).getTitle() : wfmStrings.country(), formPropertyMap.get(LOCATION.COUNTRY).isRequired()));
            country.setEnabled(!formPropertyMap.get(LOCATION.COUNTRY).isDisabled());
        } else {
            addField(LOCATION.COUNTRY, country, getTitle(wfmStrings.country(), true));
        }

        state.ensureDebugId(location_add_edit_view + "state");
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.STATE) != null) {
            addField(LOCATION.STATE, state, getTitle(formPropertyMap.get(LOCATION.STATE).isChanged() ? formPropertyMap.get(LOCATION.STATE).getTitle() : wfmStrings.state(), formPropertyMap.get(LOCATION.STATE).isRequired()));
            state.setEnabled(!formPropertyMap.get(LOCATION.STATE).isDisabled());
        } else {
            addField(LOCATION.STATE, state, getTitle(wfmStrings.state()));
        }

        cityDistrict.ensureDebugId(location_add_edit_view + "cityDistric");
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.CITY_DESTRICT) != null) {
            addField(LOCATION.CITY_DESTRICT, cityDistrict, getTitle(formPropertyMap.get(LOCATION.CITY_DESTRICT).isChanged() ? formPropertyMap.get(LOCATION.CITY_DESTRICT).getTitle() : wfmStrings.cityOrDistrict(), formPropertyMap.get(LOCATION.CITY_DESTRICT).isRequired()));
            cityDistrict.setEnabled(!formPropertyMap.get(LOCATION.CITY_DESTRICT).isDisabled());
        } else {
            addField(LOCATION.CITY_DESTRICT, cityDistrict, getTitle(wfmStrings.cityOrDistrict()));
        }

        city.ensureDebugId(location_add_edit_view + "city");
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.CITY) != null) {
            addField(LOCATION.CITY, city, getTitle(formPropertyMap.get(LOCATION.CITY).isChanged() ? formPropertyMap.get(LOCATION.CITY).getTitle() : wfmStrings.city(), formPropertyMap.get(LOCATION.CITY).isRequired()));
            city.setEnabled(!formPropertyMap.get(LOCATION.CITY).isDisabled());
        } else {
            addField(LOCATION.CITY, city, getTitle(wfmStrings.city(), true));
        }

        latitude.ensureDebugId(location_add_edit_view + "latitude");
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.GEO_LATITUDE) != null) {
            addField(LOCATION.GEO_LATITUDE, latitude, getTitle(formPropertyMap.get(LOCATION.GEO_LATITUDE).isChanged() ? formPropertyMap.get(LOCATION.GEO_LATITUDE).getTitle() : wfmStrings.latitude(), formPropertyMap.get(LOCATION.GEO_LATITUDE).isRequired()));
            latitude.setEnabled(!formPropertyMap.get(LOCATION.GEO_LATITUDE).isDisabled());
        } else {
            addField(LOCATION.GEO_LATITUDE, latitude, getTitle(wfmStrings.latitude(), true));
        }

        longitude.ensureDebugId(location_add_edit_view + "longitude");
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.GEO_LONGITUDE) != null) {
            addField(LOCATION.GEO_LONGITUDE, longitude, getTitle(formPropertyMap.get(LOCATION.GEO_LONGITUDE).isChanged() ? formPropertyMap.get(LOCATION.GEO_LONGITUDE).getTitle() : wfmStrings.longitude(), formPropertyMap.get(LOCATION.GEO_LONGITUDE).isRequired()));
            longitude.setEnabled(!formPropertyMap.get(LOCATION.GEO_LONGITUDE).isDisabled());
        } else {
            addField(LOCATION.GEO_LONGITUDE, longitude, getTitle(wfmStrings.longitude(), true));
        }

        radius.ensureDebugId(location_add_edit_view + "radius");
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.GEO_RADIUS) != null) {
            addField(LOCATION.GEO_RADIUS, radius, getTitle(formPropertyMap.get(LOCATION.GEO_RADIUS).isChanged() ? formPropertyMap.get(LOCATION.GEO_RADIUS).getTitle() : wfmStrings.radius(), formPropertyMap.get(LOCATION.GEO_RADIUS).isRequired()));
            radius.setEnabled(!formPropertyMap.get(LOCATION.GEO_RADIUS).isDisabled());
        } else {
            addField(LOCATION.GEO_RADIUS, radius, getTitle(wfmStrings.radius(), true));
        }

        email.ensureDebugId(location_add_edit_view + "email");
        addField(LOCATION.EMAIL, email, getTitle(wfmStrings.email(), false));

        phone.ensureDebugId(location_add_edit_view + "phone");
        addField(LOCATION.PHONE, phone, getTitle(wfmStrings.phone(), false));

        fax.ensureDebugId(location_add_edit_view + "fax");
        addField(LOCATION.FAX, fax, getTitle(wfmStrings.fax(), false));

        zipCode.ensureDebugId(location_add_edit_view + "zip_code");
        addField(ZIP_CODE, zipCode, getTitle(wfmStrings.postCode()));

        employeesPanel.ensureDebugId(location_add_edit_view + "locationEmployee");
        addTitleField(LOCATION.EMPLOYEE_LOCATION, wfmStrings.departments());
        if (locationId == null) {
            addField(LOCATION.EMPLOYEES, employeesPanel, null);
        }
        ownersLookUp.ensureDebugId(location_add_edit_view + "ownersLookUp");
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.OWNERS) != null) {
            addField(LOCATION.OWNERS, ownersLookUp, getTitle(formPropertyMap.get(LOCATION.OWNERS).isChanged() ? formPropertyMap.get(LOCATION.OWNERS).getTitle() : wfmStrings.owners(), formPropertyMap.get(LOCATION.OWNERS).isRequired()));
            ownersLookUp.setEnabled(!formPropertyMap.get(LOCATION.OWNERS).isDisabled());
        } else {
            addField(LOCATION.OWNERS, ownersLookUp, wfmStrings.owners(), false);
        }
        getCustomFieldUtil().drawCustomFields(this, locationId, false);

        cs = new CountryStates(country, state);
        cs.init();
        cs.onDataSet(() -> {
            if (locationId != null) {
                LoadingPanel.loading(true);
                ReportService.App.get().getLocation(locationId, new AbstractAsyncCallback<CompLocationRpc>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(CompLocationRpc compLocationRpc) {
                        LoadingPanel.loading(false);
                        code.setNumberData(compLocationRpc.getNumberData());
                        code.getTxtNumber().removeFromParent();
                        name.setText(compLocationRpc.getName());
                        city.setText(compLocationRpc.getCityName());
                        if (compLocationRpc.getLatitude() != null) {
                            latitude.setText(compLocationRpc.getLatitude().toString());
                        }
                        if (compLocationRpc.getLongitude() != null) {
                            longitude.setText(compLocationRpc.getLongitude().toString());
                        }
                        if (compLocationRpc.getRadius() != null) {
                            radius.setText(compLocationRpc.getRadius().toString());
                        }
                        email.setText(compLocationRpc.getEmail());
                        phone.setText(compLocationRpc.getPhoneNumber());
                        fax.setText(compLocationRpc.getFax());
                        zipCode.setText(compLocationRpc.getZipCode());
                        country.setSelected(compLocationRpc.getCountryId());
                        if (cs.hasStates()) {
                            state.setItems(cs.getStates(compLocationRpc.getCountryId()));
                            state.setSelected(compLocationRpc.getStateId());
                            state.setEnabled(true);
                            getCityOrDistrictByRegionId(compLocationRpc.getStateId());
                        } else {
                            state.setEnabled(false);
                        }
                        if (compLocationRpc.getCityOrDistrict() != null) {
                            cityDistrict.setSelected(compLocationRpc.getCityOrDistrict());
                        }
                        if (compLocationRpc.getLocaleItem() != null) {
                            localeItem = compLocationRpc.getLocaleItem();
                            localeView.setLocaleItem(localeItem);
                        }
                        if (compLocationRpc.getParent() != null) {
                            parent.setSelected(compLocationRpc.getParent());
                        }
                        ownersLookUp.setSelectedItems(compLocationRpc.getOwners());

                        getCustomFieldUtil().fillCustomFieldsWithData(compLocationRpc.getCustomFieldItems());
                    }
                });
            } else {
                LoadingPanel.loading(true);
                LocationService.App.get().generateLocationNumber(new AbstractAsyncCallback<NumberData>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(NumberData result) {
                        LoadingPanel.loading(false);
                        code.setNumberData(result);
                    }
                });
                setDefaultCountryFromCompanySettings();
            }
            getEmployeeList();
            empoyeeListScrollDownEvent(AddLocationView.this);

            if (locationId != null) {
                getSelectedEmployees();
                selectedEmpoyeeListScrollDownEvent(AddLocationView.this);
            }
        });

        VerticalPanel vPanel = new VerticalPanel();
        vPanel.setSpacing(5);
        vPanel.add(locationForm);
        vPanel.add(buttonPanel);
        if (command != null) {
            shell.add(vPanel);
            shell.addCloseHandler(handler -> command.execute());
            shell.open();
        } /*else {
            add(vPanel);
        }*/
        show();
    }

    private void getCityOrDistrictByRegionId(Integer selectedId) {
        cityDistrict.clear();
        ReportService.App.get().getCityOrDistrictByRegionId(selectedId, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                cityDistrict.setItems(selectItems);
                cityDistrict.setEnabled(selectItems.length != 0);
            }
        });
    }

    private void setDefaultCountryFromCompanySettings() {
        CommonService.App.get().getCompanyDefaultCountry(new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(SelectItem defaultCountry) {
                if (defaultCountry != null) {
                    country.setSelected(defaultCountry);
                    if (cs.hasStates()) {
                        state.setItems(cs.getStates(defaultCountry.getId()));
                        state.setEnabled(true);
                    } else {
                        state.setEnabled(false);
                    }
                }
            }
        });
    }

    @Override
    protected void addButtons() {
        if (locationId != null) {
            //update button
            addButton(wfmStrings.update(), WfmButton2.BTN_PRIMARY, null, location_add_edit_view.concat("update_location_button"), event -> {
                updateData();
                updateEmployeeLocation(true);
            });
        } else {
            MaterialLink save = new MaterialLink(wfmStrings.save());
            MaterialSplitButton splitButton = new MaterialSplitButton(save);
            //save and close button
            save.addClickHandler(event -> save(true));

            //save and new button
            MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
            saveAdd.addClickHandler(event -> save(false));
            splitButton.addItem(saveAdd);

            addButton(splitButton);
        }
    }

    private void getSelectedEmployees() {
        countSelected = 0;
        if (!isEmptySelected) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStart(offsetSelected);
            fp.setLimit(limitSelected);
            fp.setLocationId(locationId);
            fp.setCorporate(true);
            fp.setType(0);
            if (offsetSelected != 0) {
                LoadingPanel.loading(true);
            }
            ReportService.App.get().getTeamsMap(fp, LayoutRPC.LOCATION_FORM, new AbstractAsyncCallback<HashMap<WfmTreeItem, LinkedList<WfmTreeItem>>>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> items) {
                    LoadingPanel.loading(false);
                    TreeSelect.setTickAllVisible(items.size() != 0);
                    if (items.size() > 0) {
                        countSelected = employeesPanel.addSelectedItems(items);
                        if (countSelected < 200) {
                            isEmptySelected = true;
                        }
                    } else {
                        isEmptySelected = true;
                    }
                }
            });
            offsetSelected += limitSelected;
        }
    }


    public void getEmployeeList() {
        getEmployeeList(false);
    }

    public void getEmployeeList(boolean search) {
        employeeCount = 0;
        if (!isEmpty && limit < limitAll) {
            ListingFilterParameter fp = new ListingFilterParameter();
            if (search) {
                limit = limitAll;
            }
            fp.setStart(offset);
            fp.setLimit(limitAll);
            fp.setRelationToID(locationId);
            fp.setCorporate(true);
            fp.setType(0);
            LoadingPanel.loading(true);
            ReportService.App.get().getTeamsMap(fp, LayoutRPC.LOCATION_FORM, new AbstractAsyncCallback<HashMap<WfmTreeItem, LinkedList<WfmTreeItem>>>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> items) {
                    LoadingPanel.loading(false);
                    TreeSelect.setTickAllVisible(items.size() != 0);
                    if (items.size() > 0) {
                        employeeCount = employeesPanel.addItems(items);
                        employeesPanel.expandTreeView();
                        isEmpty = employeeCount < 200;
                    } else {
                        isEmpty = true;
                    }
                }
            });
            offset += limit;
        }
    }

    private void save(final boolean b) {

        if (validation()) {
            enableButton(false);
            CompLocationRpc compLocationRpc = new CompLocationRpc();
            compLocationRpc.setNumberData(code.getNumberData(true));
            compLocationRpc.setName(name.getText());
            compLocationRpc.setCityName(city.getText());
            compLocationRpc.setCountryId(country.getSelectedItem() != null ? country.getSelectedItem().getId() : null);
            compLocationRpc.setParent(parent.getSelectedItem());
            if (cs.getStates(country) != null && state.getSelectedItem() != null) {
                compLocationRpc.setStateId(state.getSelectedItem().getId());
            }
            if (localeView != null && localeView.getLocaleItem() != null) {
                localeItem = localeView.getLocaleItem();
                compLocationRpc.setLocaleItem(localeItem);
            }
            compLocationRpc.setEmail(email.getText());
            compLocationRpc.setFax(fax.getText());
            compLocationRpc.setZipCode(zipCode.getText());
            compLocationRpc.setPhoneNumber(phone.getText());
            compLocationRpc.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
            if (locationId == null) {
                if (employeesPanel.getTreeSelect() != null && employeesPanel.getTreeSelect().getCheckedItems() != null && employeesPanel.getTreeSelect().getCheckedItems().length > 0) {
                    WfmTreeItem[] checkedItems = employeesPanel.getTreeSelect().getCheckedItems();
                    teams.clear();
                    for (WfmTreeItem wfmTreeItem : checkedItems) {
                        Integer teamId = wfmTreeItem.getId();
                        teams.add(teamId);
                    }
                    compLocationRpc.setTeams(teams);
                }
            }
            compLocationRpc.setCityOrDestrictId(cityDistrict.getSelectedId());
            compLocationRpc.setUpdatedEmployees(updatedEmployees);
            compLocationRpc.setOwners(ownersLookUp.getSelectedItems());
            compLocationRpc.setOwnersId(ownersLookUp.getSelectedItemsIdsAsString());
            if (latitude.getText() != null && !latitude.getText().trim().isEmpty()) {
                compLocationRpc.setLatitude(Double.parseDouble(latitude.getText()));
            }
            if (longitude.getText() != null && !longitude.getText().trim().isEmpty()) {
                compLocationRpc.setLongitude(Double.parseDouble(longitude.getText()));
            }
            if (radius.getText() != null && !radius.getText().trim().isEmpty()) {
                compLocationRpc.setRadius(Integer.parseInt(radius.getText()));
            }

            LoadingPanel.loading(true);
            ReportService.App.get().saveLocation(compLocationRpc, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable throwable) {
                    enableButton(true);
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(Integer o) {
                    enableButton(true);
                    LoadingPanel.loading(false);
                    if (o != null) {
                        if (o == THIS_LOCATION_ALREADY_EXISTS) {
                            Info.show(wfmStrings.thisLocationAlreadyExists(), Info.Type.WARNING);
                        } else {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LOCATION_ADD, o, AddLocationView.this);
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())), Info.Type.INFO);
                            if (b) {
                                closeTab();
                            } else if (!b) {
                                closeTab("location|add/add");
                            } else {
                                reInit();
                            }
                        }
                    }
                }
            });
        }
    }

    private void reInit() {
        locationId = null;
        initForm();
        registerFields();
    }

    private void updateEmployeeLocation(boolean isChecked) {
        HashSet<Integer> ids = new HashSet<>();
        if (employeesPanel.tableItemTreeMap != null) {
            employeesPanel.tableItemTreeMap.forEach((t, v) -> {
                ids.add(v.getItem().getId());
            });
            ReportService.App.get().saveEmployeeLocation(ids, locationId, isChecked, new AbstractAsyncCallback<Void>() {
                @Override
                public void success(Void o) {
                    LoadingPanel.loading(false);
                }
            });
        }
    }

    private void updateData() {
        if (validation()) {
            enableButton(false);
            CompLocationRpc location = new CompLocationRpc();
            location.setObjectID(locationId);
            location.setNumberData(code.getNumberData(true));
            location.setName(name.getText());
            location.setCityName(city.getText());
            if (latitude.getText() != null && !latitude.getText().trim().isEmpty()) {
                location.setLatitude(Double.parseDouble(latitude.getText()));
            }
            if (longitude.getText() != null && !longitude.getText().trim().isEmpty()) {
                location.setLongitude(Double.parseDouble(longitude.getText()));
            }
            if (radius.getText() != null && !radius.getText().trim().isEmpty()) {
                location.setRadius(Integer.parseInt(radius.getText()));
            }
            location.setCountryId(country.getSelectedItem().getId());
            if (cs.getStates(country) != null && state.getSelectedItem() != null) {
                location.setStateId(state.getSelectedItem().getId());
            } else {
                location.setStateId(null);
            }
            if (localeView != null && localeView.getLocaleItem() != null) {
                localeItem = localeView.getLocaleItem();
                location.setLocaleItem(localeItem);
            }
            location.setEmail(email.getText());
            location.setFax(fax.getText());
            location.setZipCode(zipCode.getText());
            location.setPhoneNumber(phone.getText());
            location.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
            location.setUpdatedEmployees(updatedEmployees);
            location.setCityOrDestrictId(cityDistrict.getSelectedId());
            location.setParent(parent.getSelectedItem());
            location.setOwners(ownersLookUp.getSelectedItems());
            location.setOwnersId(ownersLookUp.getSelectedItemsIdsAsString());
            LoadingPanel.loading(true);//
            ReportService.App.get().updateLocation(location, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable throwable) {
                    enableButton(true);
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(Integer o) {
                    enableButton(true);
                    LoadingPanel.loading(false);
                    if (o != null) {
                        if (o != Errors.THIS_LOCATION_ALREADY_EXISTS) {
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LOCATION_EDIT, o, AddLocationView.this);
                            closeTab("location|summary/" + locationId);
                        } else {
                            Info.show(wfmStrings.thisLocationAlreadyExists(), Info.Type.WARNING);
                        }
                    }
                }
            });
        }
    }

    private boolean validation() {
        int error = 0;
        clearErrorStyle();

        error += getCustomFieldUtil().validateCustomFields();

        if (formPropertyMap != null && formPropertyMap.get(LOCATION.COUNTRY) != null && formPropertyMap.get(LOCATION.COUNTRY).isRequired()) {
            error += markAsError(LOCATION.COUNTRY, country, country.getSelectedItem() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.CITY) != null && formPropertyMap.get(LOCATION.CITY).isRequired()) {
            error += markAsError(LOCATION.CITY, city, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(LOCATION.CITY).isChanged() ?
                    formPropertyMap.get(LOCATION.CITY).getTitle() : wfmStrings.city(), city, formPropertyMap.get(LOCATION.CITY).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.NAME) != null && formPropertyMap.get(LOCATION.NAME).isRequired()) {
            error += markAsError(LOCATION.NAME, name, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(LOCATION.NAME).isChanged() ?
                    formPropertyMap.get(LOCATION.NAME).getTitle() : wfmStrings.name(), name, formPropertyMap.get(LOCATION.NAME).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(ZIP_CODE) != null && formPropertyMap.get(ZIP_CODE).isRequired()) {
            error += markAsError(ZIP_CODE, zipCode, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(ZIP_CODE).isChanged() ?
                    formPropertyMap.get(ZIP_CODE).getTitle() : wfmStrings.postCode(), zipCode, formPropertyMap.get(ZIP_CODE).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.EMAIL) != null && formPropertyMap.get(LOCATION.EMAIL).isRequired()) {
            error += markAsError(LOCATION.EMAIL, email, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(LOCATION.EMAIL).isChanged() ?
                    formPropertyMap.get(LOCATION.EMAIL).getTitle() : wfmStrings.email(), email, formPropertyMap.get(LOCATION.EMAIL).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.FAX) != null && formPropertyMap.get(LOCATION.FAX).isRequired()) {
            error += markAsError(LOCATION.FAX, fax, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(LOCATION.FAX).isChanged() ?
                    formPropertyMap.get(LOCATION.FAX).getTitle() : wfmStrings.fax(), fax, formPropertyMap.get(LOCATION.FAX).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.PHONE) != null && formPropertyMap.get(LOCATION.PHONE).isRequired()) {
            error += markAsError(LOCATION.PHONE, phone, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(LOCATION.PHONE).isChanged() ?
                    formPropertyMap.get(LOCATION.PHONE).getTitle() : wfmStrings.phone(), phone, formPropertyMap.get(LOCATION.PHONE).getMinChar()));
        }
        if (cs.hasStates()) {
            if (formPropertyMap != null && formPropertyMap.get(LOCATION.STATE) != null && formPropertyMap.get(LOCATION.STATE).isRequired()) {
                error += markAsError(LOCATION.STATE, state, !Validation.validateDataListBoxRequired(state));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(LOCATION.CITY_DESTRICT) != null && formPropertyMap.get(LOCATION.CITY_DESTRICT).isRequired()) {
            error += markAsError(LOCATION.CITY_DESTRICT, cityDistrict, !Validation.validateDataListBoxRequired(cityDistrict));
        }

        if (formPropertyMap != null && formPropertyMap.get(LOCATION.PARENT) != null && formPropertyMap.get(LOCATION.PARENT).isRequired()) {
            error += markAsError(LOCATION.PARENT, parent, !Validation.validateLookUpRequired(parent));
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.OWNERS) != null && formPropertyMap.get(LOCATION.OWNERS).isRequired()) {
            error += !Validation.validateEmployeeMultiSelectLookUpRequired(ownersLookUp) ? 1 : 0;
        }

        if (fax.getText().length() > 49 || email.getText().length() > 99 || phone.getText().length() > 49) {
            WfmMessageBox alert = new WfmMessageBox(IconEnum.ERROR, Action.OK);
            alert.setTitle(wfmStrings.error());
            alert.setMessage("Please, enter really value. There are too much character");
            alert.center();
            return false;
        }

        if (error > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.NAME) != null && formPropertyMap.get(LOCATION.NAME).getDefaultValue() != null) {
            name.setText(formPropertyMap.get(LOCATION.NAME).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.EMAIL) != null && formPropertyMap.get(LOCATION.EMAIL).getDefaultValue() != null) {
            email.setText(formPropertyMap.get(LOCATION.EMAIL).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.PHONE) != null && formPropertyMap.get(LOCATION.PHONE).getDefaultValue() != null) {
            phone.setText(formPropertyMap.get(LOCATION.PHONE).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.FAX) != null && formPropertyMap.get(LOCATION.FAX).getDefaultValue() != null) {
            fax.setText(formPropertyMap.get(LOCATION.FAX).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.CITY) != null && formPropertyMap.get(LOCATION.CITY).getDefaultValue() != null) {
            city.setText(formPropertyMap.get(LOCATION.CITY).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.COUNTRY) != null && formPropertyMap.get(LOCATION.COUNTRY).getDefaultValue() != null) {
            country.setSelected(new SelectItem(formPropertyMap.get(LOCATION.COUNTRY).getSelectedId(), formPropertyMap.get(LOCATION.COUNTRY).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.STATE) != null && formPropertyMap.get(LOCATION.STATE).getDefaultValue() != null) {
            state.setSelected(new SelectItem(formPropertyMap.get(LOCATION.STATE).getSelectedId(), formPropertyMap.get(LOCATION.STATE).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(LOCATION.CITY_DESTRICT) != null && formPropertyMap.get(LOCATION.CITY_DESTRICT).getDefaultValue() != null) {
            cityDistrict.setSelected(new SelectItem(formPropertyMap.get(LOCATION.CITY_DESTRICT).getSelectedId(), formPropertyMap.get(LOCATION.CITY_DESTRICT).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(ZIP_CODE) != null && formPropertyMap.get(ZIP_CODE).getDefaultValue() != null) {
            zipCode.setText(formPropertyMap.get(ZIP_CODE).getDefaultValue());
        }
    }


    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
