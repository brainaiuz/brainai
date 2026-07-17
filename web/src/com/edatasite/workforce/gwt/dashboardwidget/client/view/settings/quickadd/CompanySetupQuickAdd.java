package com.edatasite.workforce.gwt.dashboardwidget.client.view.settings.quickadd;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.AttachmentStrategy;
import com.edatasite.workforce.gwt.core.client.ui.upload.LogoField;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.CompanySettingsItem;
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

public class CompanySetupQuickAdd extends KpiSideNavBox {

    interface CompanySetupQuickAddUiBinder extends UiBinder<Widget, CompanySetupQuickAdd> {}

    private static CompanySetupQuickAddUiBinder uiBinder = GWT.create(CompanySetupQuickAddUiBinder.class);


    @UiField
    Label logoLabel;
    @UiField
    Div logoField;
    @UiField
    HTMLPanel container;
    @UiField
    Label nameLabel;
    @UiField
    TextBox nameField;
    @UiField
    Label emailLabel;
    @UiField
    TextBox emailField;
    @UiField
    Label phoneLabel;
    @UiField
    TextBox phoneField;
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

    private LogoField pdfLogo;
    private WfmButton2 btnSave, btnCancel, btnMarkAsDone;

    private CompanySettingsItem data;

    public CompanySetupQuickAdd() {
        super(DEFAULT_WIDTH);
        uiBinder.createAndBindUi(this);

        addOpeningHandler(o -> loadData());
        show();
        initInternal();
    }

    private void initInternal() {
        //header
        addHeader(new HTML(wfmStrings.companySetup()));

        logoLabel.setText(wfmStrings.companyLogo());
        nameLabel.setText(wfmStrings.companyName());
        emailLabel.setText(wfmStrings.companyEmail());
        phoneLabel.setText(wfmStrings.companyPhone());

        addressLabel.setText(wfmStrings.companyAddress());
        streetLabel.setText(wfmStrings.street1());
        streetBLabel.setText(wfmStrings.street2());
        cityLabel.setText(wfmStrings.city());
        zipCodeLabel.setText(wfmStrings.postCode());
        countryLabel.setText(wfmStrings.country());

        pdfLogo = new LogoField(CommandConstants.FOR_PDF) {
            @Override
            public AttachmentStrategy attachmentStrategy() {
                return () -> data.getId();
            }
        };
        logoField.add(pdfLogo);

        //body
        addBody(container);

        btnSave = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        btnSave.addClickHandler(ch -> save(false));

        btnCancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        btnCancel.addClickHandler(ch -> remove());

        btnMarkAsDone = new WfmButton2(wfmStrings.submit(), WfmButton2.BTN_SUCCESS);
        btnMarkAsDone.addClickHandler(event -> save(true));

        //footer
        addFooter(btnMarkAsDone);
        addFooter(btnSave);
        addFooter(btnCancel);
    }

    private void loadData() {
        LoadingPanel.loading(true, getBody());
        DashboardWidgetService.App.get().getCompanySettingsGettingStarted(new AsyncCallback<CompanySettingsItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(CompanySettingsItem result) {
                LoadingPanel.loading(false);
                data = result;
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
                if(data.getAddress() != null && data.getAddress().getCountryId() != null){
                    countryField.setValue(Collections.singletonList(new SelectItem(data.getAddress().getCountryId())));
                }
            }
        });
    }

    private void setData() {
        nameField.setText(data.getName());
        emailField.setText(data.getEmail());
        phoneField.setText(data.getPhone());

        if (data.getAddress() != null) {
            streetField.setValue(data.getAddress().getAddress());
            streetBField.setValue(data.getAddress().getAddressb());
            cityField.setValue(data.getAddress().getCity());
            zipCodeField.setValue(data.getAddress().getZipCode());
        }
    }

    private CompanySettingsItem getData() {
        CompanySettingsItem data = new CompanySettingsItem();
        data.setName(nameField.getText());
        data.setEmail(emailField.getText());
        data.setPhone(phoneField.getText());
        data.setAddress(getAddress());
        return data;
    }

    private Address getAddress() {
        Address address = new Address();

        address.setCountryId(countryField.getSingleValue() != null ? countryField.getSingleValue().getId() : null);
        address.setCountry(countryField.getSingleValue() != null ? countryField.getSingleValue().getName() : null);
        address.setCountryCode(countryField.getSingleValue() != null? countryField.getSingleValue().getDescription() : null);
        address.setCity(cityField.getValue());
        address.setAddress(streetField.getValue());
        address.setAddressb(streetBField.getValue());
        address.setRelationType(AddressReference.WORK.getId());
        address.setZipCode(zipCodeField.getValue());
        address.setPrimary(true);

        return address;
    }

    private boolean validateForm() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(nameField)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(phoneField)) {
            errors++;
        }
        if (!Validation.validateEmailRequired(emailField)) {
            errors++;
        }

        if (errors > 0) {
            Info.warn(wfmStrings.fillRequiredField(), Info.Position.TOP_RIGHT);
        }
        return errors == 0;
    }

    private void save(boolean isDone) {
        enableButtons(false);

       if (validateForm()) {
           data = getData();
           data.setSetup(isDone);
           LoadingPanel.loading(true, getBody());
           DashboardWidgetService.App.get().updateCompanyInfoGettingStarted(data, new AsyncCallback() {
               @Override
               public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                   enableButtons(true);
               }

               @Override
               public void onSuccess(Object o) {
                   LoadingPanel.loading(false);
                   Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.companyDetailsP()), Info.Position.TOP_RIGHT);

                   enableButtons(true);

                   if (isDone) {
                       Utils.userSettings.put(Constants.ACCOUNTING_IS_SETUP, "true");
                       command.execute();
                   }
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
