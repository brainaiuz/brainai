package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.HelpTextPanel;
import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomList;
import com.edatasite.workforce.gwt.core.client.ui.customlist.Design;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.ProfileMessages;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ConsolidationCompanyItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ConsolidationCompanySaveItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 06/10/12
 * Time: 16:07
 * To change this template use File | Settings | File Templates.
 */
public class AddCompanyConsolidation extends CustomForm implements Colapse, Constants {

    public static final SettingStrings settingsStrings = SettingStrings.App.get();
    public static final ProfileMessages profileMessages = ProfileMessages.App.get();
    private final ProfileServiceAsync profileService = ProfileService.App.get();

    private TextBox firstName;
    private TextBox lastName;
    private TextBox email;
    private TextBox phone;
    private TextBox subsidiaryName;
    private DataListBox companyCountry;

    //Billing Address
    private TextBox billingAddress;
    private TextBox billingAddress2;
    private TextBox billingCity;
    private DataListBox billingCountry;
    private DataListBox billingState;
    private TextBox billingPostCode;
    private HelpTextPanel biilingHelpMsg;

    //Shipping Address
    private KpiCheckBox sameAs;
    private TextBox shippingAddress;
    private TextBox shippingAddress2;
    private TextBox shippingCity;
    private DataListBox shippingCountry;
    private DataListBox shippingState;
    private TextBox shippingPostCode;
    private HelpTextPanel shippingHelpMsg;

    private DataListBox baseCurrency;
    private CustomList operatingCurrencies;

    private HorizontalPanel hpBaseCurrency;
    private HorizontalPanel hpOperatingCurrencies;

    private ConsolidationCompanyItem consolidationCompanyItem;

    private SelectItem previouseSelectedBaseCurrency;

    public AddCompanyConsolidation() {
        super("addconsolidation", settingsStrings.addNewCompany());
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), event -> saveConsolidationCompany(true));

        addButton(wfmStrings.saveAndNew(), event -> saveConsolidationCompany(false));

    }

    private void saveConsolidationCompany(final boolean saveAndClose) {
        if (!validation()) {
            return;
        }
        LoadingPanel.loading(true);
        ConsolidationCompanySaveItem consolidationCompanySaveItem = getSavedData();
        profileService.saveConsolidationCompany(consolidationCompanySaveItem, new AsyncCallback<KeyValueStruct>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(KeyValueStruct result) {
                LoadingPanel.loading(false);
                if (result.getType().equals(SUCCESS)) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), settingsStrings.subsidiaryCompany()));
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONSOLIDATION_COMPANY_ADD, true, AddCompanyConsolidation.this);
                    if (saveAndClose) {
                        closeTab();
                    } else {
                        reInit();
                    }
                } else if (FINANCIALSETTINGS_NOT_FOUND.equals(result.getKey())) {
                    Info.warn(settingsStrings.financialSettingsError());
                } else {
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }
            }
        });
    }

    private void reInit() {
        // Clear company information
        firstName.setText(null);
        lastName.setText(null);
        email.setText(null);
        phone.setText(null);
        subsidiaryName.setText(null);
        companyCountry.setSelectedNullLabel();
        // Clear adrress information
        // Biiling
        billingAddress.setText(null);
        billingAddress2.setText(null);
        billingCity.setText(null);
        billingCountry.setSelectedNullLabel();
        billingState.setEnabled(false);
        billingState.setSelectedNullLabel();
        billingState.clear();
        billingPostCode.setText(null);
        // Shipping
        shippingAddress.setText(null);
        shippingAddress2.setText(null);
        shippingCity.setText(null);
        shippingCountry.setSelectedNullLabel();
        shippingState.setEnabled(false);
        shippingState.setSelectedNullLabel();
        shippingState.clear();
        shippingPostCode.setText(null);
        // Clear finnnacial settings
        baseCurrency.setSelectedNullLabel();
        operatingCurrencies.refreshEnabled();
        operatingCurrencies.setCheckAllItems(false);
    }

    private ConsolidationCompanySaveItem getSavedData() {
        // Company Setting Data
        SettingsData settingsData = new SettingsData();
        // billing address
        settingsData.setCompanyAddress(billingAddress.getText());
        settingsData.setCompanyAddress2(billingAddress2.getText());
        settingsData.setCity(billingCity.getText());
        settingsData.setCountryID(billingCountry.getSelectedId());
        settingsData.setStateID(billingState.getSelectedId());
        settingsData.setPostCode(billingPostCode.getText());
        // shipping address
        settingsData.setMailingAddress(shippingAddress.getText());
        settingsData.setMailingAddress2(shippingAddress2.getText());
        settingsData.setMailingCity(shippingCity.getText());
        settingsData.setMailingCountryId(shippingCountry.getSelectedId());
        settingsData.setMailingStateId(shippingState.getSelectedId());
        settingsData.setMailingPostCode(shippingPostCode.getText());

        // All data
        ConsolidationCompanySaveItem consolidationCompanySaveItem = new ConsolidationCompanySaveItem();
        consolidationCompanySaveItem.setBaseCurrency(baseCurrency.getSelectedItem());
        consolidationCompanySaveItem.setOperatingCurrencies(operatingCurrencies.getSelectItems());
        consolidationCompanySaveItem.setFirstName(firstName.getText());
        consolidationCompanySaveItem.setLastName(lastName.getText());
        consolidationCompanySaveItem.setEmail(email.getText());
        consolidationCompanySaveItem.setPhone(phone.getText());
        consolidationCompanySaveItem.setCompanyName(subsidiaryName.getText());
        consolidationCompanySaveItem.setCountryId(companyCountry.getSelectedId());
        consolidationCompanySaveItem.setHost(Utils.getHostName());
        consolidationCompanySaveItem.setSettingsData(settingsData);
        return consolidationCompanySaveItem;
    }

    private boolean validation() {
        int validateSucces = 0;
        clearErrorStyle();

        validateSucces += markAsError(CompanyConsalidation.FIRST_NAME, firstName, !Validation.validateTextBoxRequired(firstName));
        validateSucces += markAsError(CompanyConsalidation.LAST_NAME, lastName, !Validation.validateTextBoxRequired(lastName));
        validateSucces += markAsError(CompanyConsalidation.E_MAIL, email, !Validation.validateEmailRequired(email, "", "", new StringBuffer()));
        validateSucces += markAsError(CompanyConsalidation.PHONE_NUMBER, phone, !Validation.validateTextBoxRequired(phone));
        validateSucces += markAsError(CompanyConsalidation.SUBSIDIARY_NAME, subsidiaryName, !Validation.validateTextBoxRequired(subsidiaryName));
        validateSucces += markAsError(CompanyConsalidation.COUNTRY, companyCountry, !Validation.validateListBoxRequired(companyCountry, (HTML) null, ""));
        validateSucces += markAsError(CompanyConsalidation.BASE_CURRENCY, baseCurrency, !Validation.validateListBoxRequired(baseCurrency, (HTML) null, ""));
        validateSucces += markAsError(CompanyConsalidation.OPERATING_CURRENCIES, operatingCurrencies, !(operatingCurrencies.getCheckedItemCount() > 0));

        if (validateSucces > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        profileService.getConsolidationCompanyItems(new AsyncCallback<ConsolidationCompanyItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ConsolidationCompanyItem consolidationItem) {
                LoadingPanel.loading(false);
                consolidationCompanyItem = consolidationItem;
                setUpData();
            }
        });
    }

    private void setUpData() {
        baseCurrency.setItems(consolidationCompanyItem.getCurrencyItem());
        companyCountry.setItems(consolidationCompanyItem.getCountryItem());
        billingCountry.setItems(consolidationCompanyItem.getCountryItem());
        shippingCountry.setItems(consolidationCompanyItem.getCountryItem());
        for (SelectItem item : consolidationCompanyItem.getCurrencyItem()) {
            operatingCurrencies.add(item);
        }

        applyParentBaseCurrency();
    }

    private void applyParentBaseCurrency() {
        if (consolidationCompanyItem.getBaseCurrency() != null) {
            operatingCurrencies.setEnabledItem(consolidationCompanyItem.getBaseCurrency(), false, true);
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CONSOLIDATION_COMPANY_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initializationCustomForm();
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void initializationCustomForm() {
        // Company Information
        // First Name
        firstName = new TextBox();
        firstName.addStyleName(DEFAULT_WIDTH);
        // Last Name
        lastName = new TextBox();
        lastName.addStyleName(DEFAULT_WIDTH);
        // User Email
        email = new TextBox();
        email.addStyleName(DEFAULT_WIDTH);
        // User Phone
        phone = new TextBox();
        phone.addStyleName(DEFAULT_WIDTH);
        // Company Name
        subsidiaryName = new TextBox();
        subsidiaryName.addStyleName(DEFAULT_WIDTH);
        // Country Name
        companyCountry = new DataListBox();
        companyCountry.addStyleName(DEFAULT_WIDTH);

        // Address Information
        // Billing Address
        billingAddress = new TextBox();
        billingAddress.addStyleName(DEFAULT_WIDTH);
        // Billing Address1
        billingAddress2 = new TextBox();
        billingAddress2.addStyleName(DEFAULT_WIDTH);
        //Billing City
        billingCity = new TextBox();
        billingCity.addStyleName(DEFAULT_WIDTH);
        //Billing Country
        billingCountry = new DataListBox();
        billingCountry.addStyleName(DEFAULT_WIDTH);
        billingCountry.setEnabled(false);
        //Billing state
        billingState = new DataListBox();
        billingState.addStyleName(DEFAULT_WIDTH);
        billingState.setEnabled(false);
        //Billing Post Code
        billingPostCode = new TextBox();
        billingPostCode.addStyleName(DEFAULT_WIDTH);
        // Billing help message
        biilingHelpMsg = new HelpTextPanel(settingsStrings.companyBillingAddress());

        // Shipping Address
        shippingAddress = new TextBox();
        shippingAddress.addStyleName(DEFAULT_WIDTH);
        // Shipping Address1
        shippingAddress2 = new TextBox();
        shippingAddress2.addStyleName(DEFAULT_WIDTH);
        //Shipping City
        shippingCity = new TextBox();
        shippingCity.addStyleName(DEFAULT_WIDTH);
        //Shipping Country
        shippingCountry = new DataListBox();
        shippingCountry.addStyleName(DEFAULT_WIDTH);

        //Shipping state
        shippingState = new DataListBox();
        shippingState.addStyleName(DEFAULT_WIDTH);
        shippingState.setEnabled(false);
        //Shipping Post Code
        shippingPostCode = new TextBox();
        shippingPostCode.addStyleName(DEFAULT_WIDTH);
        // Shipping help message
        shippingHelpMsg = new HelpTextPanel(settingsStrings.companyMailingOrShippingAddress());

        // Company Financial Settings
        // Company Base currency
        baseCurrency = new DataListBox();
        baseCurrency.addStyleName(DEFAULT_WIDTH);

        HelpTextPanel baseCurrencyHelpPanel = new HelpTextPanel(settingsStrings.baseCurrencyHelpMessageText(), 200);

        hpBaseCurrency = new HorizontalPanel();
        hpBaseCurrency.add(baseCurrency);
        hpBaseCurrency.add(baseCurrencyHelpPanel);
        hpBaseCurrency.setCellVerticalAlignment(baseCurrency, HasAlignment.ALIGN_MIDDLE);
        hpBaseCurrency.setCellVerticalAlignment(baseCurrencyHelpPanel, HasAlignment.ALIGN_MIDDLE);
        hpBaseCurrency.setWidth("500px");

        // Operating Currencies
        operatingCurrencies = new CustomList(Design.CHECK, true);
        operatingCurrencies.setSearchText(wfmStrings.search());
        operatingCurrencies.addStyleName(DEFAULT_WIDTH);
        operatingCurrencies.setHeight(200);

        HelpTextPanel operatingCurrenciesHelpPanel = new HelpTextPanel(settingsStrings.operatingCurrenciesHelpMessageText(), 200);

        hpOperatingCurrencies = new HorizontalPanel();
        hpOperatingCurrencies.add(operatingCurrencies);
        hpOperatingCurrencies.add(operatingCurrenciesHelpPanel);
        hpOperatingCurrencies.setCellVerticalAlignment(operatingCurrencies, HasAlignment.ALIGN_MIDDLE);
        hpOperatingCurrencies.setCellVerticalAlignment(operatingCurrenciesHelpPanel, HasAlignment.ALIGN_MIDDLE);
        hpOperatingCurrencies.setWidth("500px");

        addWidgetsToForm();
        addWidgetsEventListener();
        show();
    }

    private void addWidgetsToForm() {
        // 1
        addTitleField(CompanyConsalidation.COMPANY_INFORMATION, wfmStrings.companyInformation());
        // 1.1
        addField(CompanyConsalidation.FIRST_NAME, firstName, getTitle(wfmStrings.firstName(), true));
        // 2.1
        addField(CompanyConsalidation.LAST_NAME, lastName, getTitle(wfmStrings.lastName(), true));
        // 3.1
        addField(CompanyConsalidation.E_MAIL, email, getTitle(wfmStrings.email(), true));
        // 2.1
        addField(CompanyConsalidation.PHONE_NUMBER, phone, getTitle(wfmStrings.phone(), true));
        // 2.2
        addField(CompanyConsalidation.SUBSIDIARY_NAME, subsidiaryName, getTitle(settingsStrings.subsidiaryName(), true));
        // 2.3
        addField(CompanyConsalidation.COUNTRY, companyCountry, getTitle(wfmStrings.country(), true));

        // 2
        addTitleField(CompanyConsalidation.ADDRESS_INFORMATION, wfmStrings.addressInformation());
        // 1.1
        addTitleField(CompanyConsalidation.BILLING_ADDRESS, wfmStrings.billingAddress());
        // 2.1
        addField(CompanyConsalidation.BILLING_ADDRESS_LINE1, billingAddress, getTitle(wfmStrings.addressLine1()));
        // 3.1
        addField(CompanyConsalidation.BILLING_ADDRESS_LINE2, billingAddress2, getTitle(wfmStrings.addressLine2()));
        // 4.1
        addField(CompanyConsalidation.BILLING_CITY, billingCity, getTitle(wfmStrings.city()));
        // 5.1
        addField(CompanyConsalidation.BILLING_COUNTRY, billingCountry, getTitle(wfmStrings.country()));
        // 6.1
        addField(CompanyConsalidation.BILLING_STATE, billingState, getTitle(wfmStrings.state()));
        // 7.1
        addField(CompanyConsalidation.BILLING_POST_CODE, billingPostCode, getTitle(wfmStrings.postCode()));
//        // 8.1
//        addField(CompanyConsalidation.BILLING_HELP_MSG, biilingHelpMsg);

        // 1.2
        addTitleField(CompanyConsalidation.SHIPPING_ADDRESS, wfmStrings.shippingAddress());
        // 2.2
        addField(CompanyConsalidation.SHIPPING_ADDRESS_LINE1, shippingAddress, getTitle(wfmStrings.addressLine1()));
        // 3.2
        addField(CompanyConsalidation.SHIPPING_ADDRESS_LINE2, shippingAddress2, getTitle(wfmStrings.addressLine2()));
        // 4.2
        addField(CompanyConsalidation.SHIPPING_CITY, shippingCity, getTitle(wfmStrings.city()));
        // 5.2
        addField(CompanyConsalidation.SHIPPING_COUNTRY, shippingCountry, getTitle(wfmStrings.country()));
        // 6.2
        addField(CompanyConsalidation.SHIPPING_STATE, shippingState, getTitle(wfmStrings.state()));
        // 7.2
        addField(CompanyConsalidation.SHIPPING_POST_CODE, shippingPostCode, getTitle(wfmStrings.postCode()));
//        // 8.2
//        addField(CompanyConsalidation.SHIPPING_HELP_MSG, shippingHelpMsg);

        // 3
        addTitleField(CompanyConsalidation.FINANCIAL_SETTINGS, wfmStrings.financialInformation());
        // 3.1
        addField(CompanyConsalidation.BASE_CURRENCY, hpBaseCurrency, getTitle(wfmStrings.baseCurrency(), true));
        // 3.2
        addField(CompanyConsalidation.OPERATING_CURRENCIES, hpOperatingCurrencies, getTitle(settingsStrings.operatingCurrencies(), true));

    }

    private void addWidgetsEventListener() {
        billingCountry.addValueChangeHandler(changeEvent -> {
            if (billingCountry.getSelectedId() != null && consolidationCompanyItem.getStatesMap().containsKey(billingCountry.getSelectedId())) {
                billingState.setEnabled(true);
                billingState.setItems(consolidationCompanyItem.getStatesMap().get(billingCountry.getSelectedId()).toArray(new SelectItem[]{}));
            } else {
                billingState.setEnabled(false);
                billingState.setSelectedNullLabel();
                billingState.clear();
            }
        });

        shippingCountry.addValueChangeHandler(changeEvent -> {
            if (shippingCountry.getSelectedId() != null && consolidationCompanyItem.getStatesMap().containsKey(shippingCountry.getSelectedId())) {
                shippingState.setEnabled(true);
                shippingState.setItems(consolidationCompanyItem.getStatesMap().get(shippingCountry.getSelectedId()).toArray(new SelectItem[]{}));
            } else {
                shippingState.setEnabled(false);
                shippingState.setSelectedNullLabel();
                shippingState.clear();
            }
        });

        baseCurrency.addValueChangeHandler(changeEvent -> {
            operatingCurrencies.refreshEnabled();

            if (previouseSelectedBaseCurrency != null) {
                operatingCurrencies.setEnabledItem(previouseSelectedBaseCurrency, true, false);
            }
            previouseSelectedBaseCurrency = baseCurrency.getSelectedItem();

            if (baseCurrency.getSelectedItem() != null) {
                operatingCurrencies.setEnabledItem(baseCurrency.getSelectedItem(), false, true);
            }
            applyParentBaseCurrency();
        });

        companyCountry.addValueChangeHandler(changeEvent -> {
            if (companyCountry.getSelectedId() != null) {
                billingCountry.setSelected(companyCountry.getSelectedId());
                billingState.setEnabled(true);
                List<SelectItem> selectItems = consolidationCompanyItem.getStatesMap().get(billingCountry.getSelectedId());
                if (selectItems != null && !selectItems.isEmpty()) {
                    billingState.setItems(selectItems.toArray(new SelectItem[]{}));
                }
            } else {
                billingCountry.setSelectedNullLabel();
                billingCountry.clear();
                billingState.setEnabled(false);
                billingState.setSelectedNullLabel();
                billingState.clear();
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
