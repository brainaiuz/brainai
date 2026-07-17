package com.edatasite.workforce.gwt.dashboardwidget.client.view.settings.quickadd;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Label;

import java.util.Collections;

public class EmployeeProfileQuickAdd extends KpiSideNavBox {

    interface EmployeeProfileQuickAddUiBinder extends UiBinder<Widget, EmployeeProfileQuickAdd> {}

    private static final EmployeeProfileQuickAddUiBinder uiBinder = GWT.create(EmployeeProfileQuickAddUiBinder.class);

    

    @UiField
    HTMLPanel container;
    @UiField
    Label firstNameLabel;
    @UiField
    TextBox firstNameField;
    @UiField
    Label lastNameLabel;
    @UiField
    TextBox lastNameField;
    @UiField
    Label emailLabel;
    @UiField
    TextBox emailField;
    @UiField
    Label phoneLabel;
    @UiField
    TextBox phoneField;
    @UiField
    Label dobLabel;
    @UiField
    Div dobField;
    @UiField
    Label hireDateLabel;
    @UiField
    Div hireDateField;
    @UiField
    Label addressLabel;
    @UiField
    Label streetLabel;
    @UiField
    TextBox streetField;
    @UiField
    Label streetBLabel;
    @UiField
    TextBox streetBField;
    @UiField
    Label cityLabel;
    @UiField
    TextBox cityField;
    @UiField
    Label zipCodeLabel;
    @UiField
    TextBox zipCodeField;
    @UiField
    Label countryLabel;
    @UiField
    WfmDropdown countryField;

    private DatePicker dateofBirth, hireDate;
    private WfmButton2 btnSave, btnCancel, btnMarkAsDone;

    private EmployeeProfileItem item;

    public EmployeeProfileQuickAdd() {
        super(DEFAULT_WIDTH);
        uiBinder.createAndBindUi(this);

        addOpeningHandler(o -> loadData());
        show();
        initInternal();
    }

    private void initInternal() {
        //header
        addHeader(new HTML(wfmStrings.updateProfile()));

        firstNameLabel.setText(wfmStrings.firstName());
        lastNameLabel.setText(wfmStrings.lastName());
        emailLabel.setText(wfmStrings.email());
        phoneLabel.setText(wfmStrings.phone());
        dobLabel.setText(wfmStrings.dateOfBirth());
        hireDateLabel.setText(wfmStrings.hireDate());
        addressLabel.setText(wfmStrings.homeAddress());

        dateofBirth = new DatePicker();
        dobField.add(dateofBirth);

        hireDate = new DatePicker();
        hireDateField.add(hireDate);

        addressLabel.setText(wfmStrings.homeAddress());
        streetLabel.setText(wfmStrings.street1());
        streetBLabel.setText(wfmStrings.street2());
        cityLabel.setText(wfmStrings.city());
        zipCodeLabel.setText(wfmStrings.postCode());
        countryLabel.setText(wfmStrings.country());

        //body
        addBody(container);

        btnSave = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        btnSave.addClickHandler(ch -> save());

        btnCancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        btnCancel.addClickHandler(ch -> remove());

        btnMarkAsDone = new WfmButton2(wfmStrings.markAsDone(), WfmButton2.BTN_SUCCESS);
        btnMarkAsDone.addClickHandler(event -> command.execute());
//        btnMarkAsDone.setVisible(false);

        //footer
        addFooter(btnMarkAsDone);
        addFooter(btnSave);
        addFooter(btnCancel);
    }

    private void loadData() {
        LoadingPanel.loading(true, getBody());
        DashboardWidgetService.App.get().getEmployeeProfileGettingStarted(new AsyncCallback<EmployeeProfileItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(EmployeeProfileItem result) {
                LoadingPanel.loading(false);
                item = result;
                setData();
                initCountryList();
            }
        });
    }

    private void initCountryList() {
        AllInOneService.App.get().getCountryList(new ListingFilterParameter(), new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SelectItem[] result) {
                countryField.setPlaceholder(wfmStrings.pleaseSelect());
                if (result != null) {
                    for (SelectItem selectItem : result) {
                        countryField.addItem(selectItem.getName(), selectItem);
                    }
                }
                if(item.getHomeAddress() != null && item.getHomeAddress().getCountryId() != null){
                    countryField.setValue(Collections.singletonList(new SelectItem(item.getHomeAddress().getCountryId())));
                }
            }
        });
    }

    private void setData() {
        firstNameField.setText(item.getFirstName());
        lastNameField.setText(item.getLastName());
        emailField.setText(item.getEmail());
        phoneField.setText(item.getPhone());
        dateofBirth.setDate(item.getDob() != null ? item.getDob().getDate() : null);
        hireDate.setDate(item.getHireDate() != null ? item.getHireDate().getDate() : null);
        if (item.getHomeAddress() != null) {
            streetField.setValue(item.getHomeAddress().getAddress());
            streetBField.setValue(item.getHomeAddress().getAddressb());
            cityField.setValue(item.getHomeAddress().getCity());
            zipCodeField.setValue(item.getHomeAddress().getZipCode());
        }
    }

    private EmployeeProfileItem getData() {
        EmployeeProfileItem item = new EmployeeProfileItem();
        item.setFirstName(firstNameField.getText());
        item.setLastName(lastNameField.getText());
        item.setEmail(emailField.getText());
        item.setPhone(phoneField.getText());
        item.setDob(dateofBirth.getDate() != null ? new DateNonConvertable(dateofBirth.getDate()) : null);
        item.setHireDate(hireDate.getDate() != null ? new DateNonConvertable(hireDate.getDate()) : null);
        item.setHomeAddress(getAddress());

        return item;
    }

    private Address getAddress() {
        Address address = new Address();

        address.setCountryId(countryField.getSingleValue() != null ? countryField.getSingleValue().getId() : null);
        address.setCountry(countryField.getSingleValue() != null ? countryField.getSingleValue().getName() : null);
        address.setCountryCode(countryField.getSingleValue() != null? countryField.getSingleValue().getDescription() : null);
        address.setCity(cityField.getValue());
        address.setAddress(streetField.getValue());
        address.setAddressb(streetBField.getValue());
        address.setRelationType(AddressReference.HOME.getId());
        address.setZipCode(zipCodeField.getValue());
        address.setPrimary(true);

        return address;
    }

    private boolean validateForm() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(firstNameField)) {
            errors++;
        }
        /*if (!Validation.validateTextBoxRequired(phoneField)) {
            errors++;
        }*/
        if (!Validation.validateEmailRequired(emailField)) {
            errors++;
        }

        if (errors > 0) {
            Info.warn(wfmStrings.fillRequiredField(), Info.Position.TOP_RIGHT);
        }
        return errors == 0;
    }

    private void save() {
        enableButtons(false);

        if (validateForm()) {
            LoadingPanel.loading(true, getBody());
            DashboardWidgetService.App.get().saveEmployeeProfileGettingStarted(getData(), new AsyncCallback() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Object o) {
                    LoadingPanel.loading(false);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.employeeProfile()), Info.Position.TOP_RIGHT);

                    enableButtons(true);
//                    btnMarkAsDone.setVisible(true);
//                    btnSave.setVisible(false);
                }
            });
        } else {
            enableButtons(true);
        }
    }

    private void enableButtons(boolean enable) {
        btnMarkAsDone.setEnabled(enable);
        btnSave.setEnabled(enable);
        btnCancel.setEnabled(enable);
    }
}
