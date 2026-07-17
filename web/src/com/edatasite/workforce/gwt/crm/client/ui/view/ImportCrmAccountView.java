package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 5, 2009
 * Time: 7:50:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportCrmAccountView extends ImportCrmAccountAbstractView implements Colapse {
    private static final String NAME_LIST_BOX = "NAME_LIST_BOX";
    private static final String ADDRESS_LIST_BOX = "ADDRESS_LIST_BOX";
    private static final String ADDRESS2_LIST_BOX = "ADDRESS2_LIST_BOX";
    private static final String CITY_LIST_BOX = "CITY_LIST_BOX";
    private static final String COUNTRY_LIST_BOX = "COUNTRY_LIST_BOX";
    private static final String STATE_LIST_BOX = "STATE_LIST_BOX";
    private static final String POSTCODE_LIST_BOX = "POSTCODE_LIST_BOX";
    protected HTML titleView;
    public String viewName;
    private TextBox accountEmployee;
    private DataListBox accountName;
    private DataListBox parent;
    private DataListBox number;
    private DataListBox accountType;
    private DataListBox industry;
    private DataListBox email;
    private DataListBox phone;
    private DataListBox fax;
    private DataListBox webSite;

    private MultiTable billAddress;
    private MultiTable mailAddress;

    private DataListBox currency;
    private DataListBox vatNumber;
    private DataListBox paymentMethod;
    private DataListBox registrationNumber;
    private DataListBox term;
    private DataListBox note;

    private ArrayList<CompanyCustomFieldItem> companyCustomFieldItems;
    private DataListBox[] tbValues;

    public ImportCrmAccountView(Integer objectId) {
        super("addimport", wfmStrings.importAccount());
        this.objectId = objectId;
        viewName = wfmStrings.importAccount();
        successMessage = wfmMessages.messItemSucImported(wfmStrings.crmAccount());
        errorMessage = wfmMessages.messImportItemError(wfmStrings.crmAccount());
    }

    public ImportCrmAccountView(String viewName, String viewDescription, Integer objectId) {
        super(viewName, viewDescription);
        this.objectId = objectId;
    }

    @Override
    public void loadPage() {
        CommonService.App.get().getCompanyCustomFields(ViewName.CrmAccount, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            public void failure(Throwable throwable) {
            }

            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    companyCustomFieldItems = result;
                    initialize();
                    addFieldsToTable();
                }
            }
        });
    }

    public void initialize() {
        //Information
        accountEmployee = new TextBox();
        accountEmployee.addStyleName(DEFAULT_WIDTH);
        accountEmployee.setText(Utils.userSettings.get(FULL_NAME));
        accountEmployee.setEnabled(false);
        accountName = new DataListBox();
        accountName.addStyleName(DEFAULT_WIDTH);
        parent = new DataListBox();
        parent.addStyleName(DEFAULT_WIDTH);
        number = new DataListBox();
        number.addStyleName(DEFAULT_WIDTH);
        accountType = new DataListBox();
        accountType.addStyleName(DEFAULT_WIDTH);
        industry = new DataListBox();
        industry.addStyleName(DEFAULT_WIDTH);
        email = new DataListBox();
        email.addStyleName(DEFAULT_WIDTH);
        phone = new DataListBox();
        phone.addStyleName(DEFAULT_WIDTH);
        fax = new DataListBox();
        fax.addStyleName(DEFAULT_WIDTH);
        webSite = new DataListBox();
        webSite.addStyleName(DEFAULT_WIDTH);
        //Address Information
        billAddress = new MultiTable(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets();
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });
        mailAddress = new MultiTable(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets();
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });
        //Financial Information
        currency = new DataListBox();
        currency.addStyleName(DEFAULT_WIDTH);
        vatNumber = new DataListBox();
        vatNumber.addStyleName(DEFAULT_WIDTH);
        paymentMethod = new DataListBox();
        paymentMethod.addStyleName(DEFAULT_WIDTH);
        registrationNumber = new DataListBox();
        registrationNumber.addStyleName(DEFAULT_WIDTH);
        term = new DataListBox();
        term.addStyleName(DEFAULT_WIDTH);
        note = new DataListBox();
        note.addStyleName(DEFAULT_WIDTH);
    }

    @Override
    public void setItems(SelectItem[] items) {
        setItems();
    }

    private WidgetsMap getAddressWidgets() {
        WidgetsMap widgetsMap = new WidgetsMap();

        DataListBox name = new DataListBox();
        name.addStyleName(DEFAULT_WIDTH);
        DataListBox address = new DataListBox();
        address.addStyleName(DEFAULT_WIDTH);
        DataListBox address2 = new DataListBox();
        address2.addStyleName(DEFAULT_WIDTH);
        DataListBox city = new DataListBox();
        city.addStyleName(DEFAULT_WIDTH);
        DataListBox country = new DataListBox();
        country.addStyleName(DEFAULT_WIDTH);
        DataListBox state = new DataListBox();
        state.addStyleName(DEFAULT_WIDTH);
        DataListBox postCode = new DataListBox();
        postCode.addStyleName(DEFAULT_WIDTH);

        if (items != null && items.length > 0) {
            name.setItems(items);
            address.setItems(items);
            address2.setItems(items);
            city.setItems(items);
            country.setItems(items);
            state.setItems(items);
            postCode.setItems(items);
        }

        HTML nameLabel = new HTML(wfmStrings.name());
        nameLabel.setWidth("100px");
        HTML address1Label = new HTML(wfmMessages.address("1"));
        address1Label.setWidth("100px");
        HTML address2Label = new HTML(wfmMessages.address("2"));
        address2Label.setWidth("100px");
        HTML cityLabel = new HTML(wfmStrings.city());
        cityLabel.setWidth("100px");
        HTML countryLabel = new HTML(wfmStrings.country());
        countryLabel.setWidth("100px");
        HTML stateLabel = new HTML(wfmStrings.state());
        stateLabel.setWidth("100px");
        HTML postCodeLabel = new HTML(wfmStrings.postCode());
        postCodeLabel.setWidth("100px");

        FlexTable addressPanel = new FlexTable();
        addressPanel.setCellSpacing(10);
        addressPanel.setCellPadding(10);
        addressPanel.setWidget(0, 0, nameLabel);
        addressPanel.setWidget(0, 1, name);
        addressPanel.setWidget(1, 0, address1Label);
        addressPanel.setWidget(1, 1, address);
        addressPanel.setWidget(2, 0, address2Label);
        addressPanel.setWidget(2, 1, address2);
        addressPanel.setWidget(3, 0, cityLabel);
        addressPanel.setWidget(3, 1, city);
        addressPanel.setWidget(4, 0, countryLabel);
        addressPanel.setWidget(4, 1, country);
        addressPanel.setWidget(5, 0, stateLabel);
        addressPanel.setWidget(5, 1, state);
        addressPanel.setWidget(6, 0, postCodeLabel);
        addressPanel.setWidget(6, 1, postCode);

        country.setAllowFirstItem(true);
        state.setAllowFirstItem(true);
        widgetsMap.addWidgetToMap(NAME_LIST_BOX, name);
        widgetsMap.addWidgetToMap(ADDRESS_LIST_BOX, address);
        widgetsMap.addWidgetToMap(ADDRESS2_LIST_BOX, address2);
        widgetsMap.addWidgetToMap(CITY_LIST_BOX, city);
        widgetsMap.addWidgetToMap(COUNTRY_LIST_BOX, country);
        widgetsMap.addWidgetToMap(STATE_LIST_BOX, state);
        widgetsMap.addWidgetToMap(POSTCODE_LIST_BOX, postCode);
        widgetsMap.addWidgets(addressPanel);
        return widgetsMap;
    }

    public void addFieldsToTable() {
        if (companyCustomFieldItems != null && companyCustomFieldItems.size() > 0) {
            tbValues = new DataListBox[companyCustomFieldItems.size()];
            for (int i = 0; i < companyCustomFieldItems.size(); i++) {
                tbValues[i] = new DataListBox();
                tbValues[i].addStyleName(DEFAULT_WIDTH);
                switch (companyCustomFieldItems.get(i).getDataType()) {
                    case Constants.DATA_TYPE_NUMBER:
                        addField("string_value" + (i + 1), tbValues[i], companyCustomFieldItems.get(i).getFieldName());
                        break;
                    case DATA_TYPE_DATE:
                        addField("string_value" + (i + 1), tbValues[i], companyCustomFieldItems.get(i).getFieldName());
                        break;
                    default:
                        addField("string_value" + (i + 1), tbValues[i], companyCustomFieldItems.get(i).getFieldName());
                        break;
                }
            }
        }
        super.initialize();
    }

    public void setItems() {
        accountName.setItems(items, wfmStrings.name());
        parent.setItems(items, wfmStrings.parentaccount());
        number.setItems(items, wfmStrings.accountNumber());
        if (ImportTypeEnum.CRM_ACCOUNT.equals(getImportType())) {
            accountType.setItems(items, wfmStrings.accountType());
        }
        industry.setItems(items, wfmStrings.industry());
        email.setItems(items, wfmStrings.email());
        phone.setItems(items, wfmStrings.phone());
        fax.setItems(items, wfmStrings.fax());
        webSite.setItems(items, wfmStrings.website());

        setAddressItems(billAddress, items, true);
        setAddressItems(mailAddress, items, false);

        currency.setItems(items, wfmStrings.currency());
        vatNumber.setItems(items, wfmStrings.vatNumber());
        registrationNumber.setItems(items, wfmStrings.registrationNumber());
        term.setItems(items, wfmStrings.terms());
        note.setItems(items, wfmStrings.note());
        paymentMethod.setItems(items, wfmStrings.paymentMethod());
        if (tbValues != null && tbValues.length > 0) {
            int i = 0;
            for (DataListBox dataListBox : tbValues) {
                if (dataListBox != null) {
                    String title = companyCustomFieldItems.size() > i && companyCustomFieldItems.get(i) != null ? companyCustomFieldItems.get(i).getFieldName() : null;
                    dataListBox.setItems(items, title);
                    i++;
                }
            }
        }
    }

    private Address[] getAddressData(MultiTable addressTable) {
        ArrayList<HashMap<String, Widget>> widgetsList = addressTable.getWidgets();
        Address[] array = new Address[widgetsList.size()];
        int i = 0;
        for (HashMap<String, Widget> widgets : widgetsList) {
            DataListBox name = (DataListBox) widgets.get(NAME_LIST_BOX);
            DataListBox address = (DataListBox) widgets.get(ADDRESS_LIST_BOX);
            DataListBox address2 = (DataListBox) widgets.get(ADDRESS2_LIST_BOX);
            DataListBox city = (DataListBox) widgets.get(CITY_LIST_BOX);
            DataListBox country = (DataListBox) widgets.get(COUNTRY_LIST_BOX);
            DataListBox state = (DataListBox) widgets.get(STATE_LIST_BOX);
            DataListBox postcode = (DataListBox) widgets.get(POSTCODE_LIST_BOX);

            array[i] = new Address();
            array[i].setNameId(name.getSelectedId());
            array[i].setAddressId(address.getSelectedId());
            array[i].setAddressBId(address2.getSelectedId());
            array[i].setCityId(city.getSelectedId());
            array[i].setCountryId(country.getSelectedId());
            array[i].setStateId(state.getSelectedId());
            array[i].setZipCodeId(postcode.getSelectedId());
            i++;
        }
        return array;
    }

    private void setAddressItems(MultiTable addressTable, SelectItem[] items, boolean isBilling) {
        ArrayList<HashMap<String, Widget>> widgetsList = addressTable.getWidgets();
        for (HashMap<String, Widget> widgets : widgetsList) {
            ((DataListBox) widgets.get(NAME_LIST_BOX)).setItems(items, isBilling ? crmStrings.baName() : crmStrings.maName());
            ((DataListBox) widgets.get(ADDRESS_LIST_BOX)).setItems(items, isBilling ? crmStrings.baAddress1() : crmStrings.maAddress1());
            ((DataListBox) widgets.get(ADDRESS2_LIST_BOX)).setItems(items, isBilling ? crmStrings.baAddress2() : crmStrings.maAddress2());
            ((DataListBox) widgets.get(CITY_LIST_BOX)).setItems(items, isBilling ? crmStrings.baCity() : crmStrings.maCity());
            ((DataListBox) widgets.get(COUNTRY_LIST_BOX)).setItems(items, isBilling ? crmStrings.baCountry() : crmStrings.maCountry());
            ((DataListBox) widgets.get(STATE_LIST_BOX)).setItems(items, isBilling ? crmStrings.baState() : crmStrings.maState());
            ((DataListBox) widgets.get(POSTCODE_LIST_BOX)).setItems(items, isBilling ? crmStrings.baPostCode() : crmStrings.maPostCode());
        }
    }

    public void save() {
        if (!validate()) {
            return;
        }
        LoadingPanel.loading(true);
        CrmAccountItem item = new CrmAccountItem();
        save(getImportFile(item));
    }

    private ImportFile getImportFile(CrmAccountItem item) {
        item = getValuesIntoItem(item);
        boolean hasHeader_ = hasHeader.getValue();
        ImportFile importFile = item.importFile();
        importFile.setDefaultSeparator(defaultSeparator);
        importFile.setHasHeader(hasHeader_);
        importFile.setType(getImportType());
        return importFile;
    }

    public CrmAccountItem getValuesIntoItem(CrmAccountItem item) {
        //Information
        item.setObjectId(objectId);
        item.setNameId(accountName.getSelectedId());
        item.setParentID(parent.getSelectedId());
        item.setNumberId(number.getSelectedId());
        item.setAccountTypeID(accountType.getSelectedId());
        item.setIndustryID(industry.getSelectedId());
        item.setEmailId(email.getSelectedId());
        item.setPhoneId(phone.getSelectedId());
        item.setFaxId(fax.getSelectedId());
        item.setWebsiteId(webSite.getSelectedId());
        //Address Information
        item.setBillAddresses(getAddressData(billAddress));
        item.setMailAddresses(getAddressData(mailAddress));
        //Financial Information
        item.setCurrencyId(currency.getSelectedId());
        item.setVatNumberId(vatNumber.getSelectedId());
        item.setRegistrationNumberId(registrationNumber.getSelectedId());
        item.setPaymentMethodId(paymentMethod.getSelectedId());
        item.setTermsId(term.getSelectedId());
        item.setNoteId(note.getSelectedId());
        if (tbValues != null && tbValues.length > 0) {
            ArrayList<CompanyCustomFieldItem> resultItemList = new ArrayList<>();
            for (int i = 0; i < tbValues.length; i++) {
                CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                resultItem.setObjectId(companyCustomFieldItems.get(i).getObjectId());
                resultItem.setDataType(companyCustomFieldItems.get(i).getDataType());
                resultItem.setUiType(companyCustomFieldItems.get(i).getUiType());
                resultItem.setColumnCode(companyCustomFieldItems.get(i).getColumnCode());
                resultItem.setCustomFieldSettingID(companyCustomFieldItems.get(i).getCustomFieldSettingID());
                resultItem.setPredefinedValues(companyCustomFieldItems.get(i).getPredefinedValues());
                if (tbValues[i].getSelectedItem() != null) {
                    resultItem.setFieldStringValue(tbValues[i].getSelectedItem().getId().toString());
                }
                resultItemList.add(resultItem);
            }
            item.setCustomFields(resultItemList);
        }
        return item;
    }

    public ImportTypeEnum getImportType() {
        return ImportTypeEnum.CRM_ACCOUNT;
    }

    public boolean validate() {
        errors = 0;
        if (!Validation.validateListBoxRequired(accountName, new HTML(), wfmStrings.pleaseSelect())) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.IMPORT_CRM_ACCOUNT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.IMPORT;
    }

    @Override
    protected void getDataToFillFields() {
        CommonService.App.get().getCSVColumns(objectId, new AbstractAsyncCallback<HashMap<String, SelectItem[]>>() {
            public void failure(Throwable d) {
                LoadingPanel.loading(false);
                closeTab();
            }

            public void success(final HashMap<String, SelectItem[]> o) {
                DeferredCommand.addCommand(() -> {
                    for (Map.Entry<String, SelectItem[]> entry : o.entrySet()) {
                        String key = entry.getKey();
                        items = entry.getValue();
                        if (!key.equals(String.valueOf(defaultSeparator))) {
                            defaultSeparator = key.charAt(0);
                        }
                    }
                    setItems(items);
                    LoadingPanel.loading(false);
                });
            }
        });

    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), BTN_PRIMARY, event -> save());
    }

    protected void drawForm() {
        addField(TITLE, titleView);
        addTitleField(BASIC_DETAILS, wfmStrings.basicDetails());
        addField(MY_CSV_FILE_HAS_HEADERS, hasHeader, getTitle(wfmStrings.myCSVFileHasHeaders()));
        addField(DUPLICATE, duplicateActionTable, getTitle(wfmStrings.duplicateAction()));
        //Information
        addField(ACCOUNT_NAME, accountName, getTitle(wfmStrings.companyName(), true));
        addField(ACCOUNT_NUMBER, number, getTitle(wfmStrings.accountNumber()));
        addField(PARENT, parent, getTitle(wfmStrings.parent()));
        if (ImportTypeEnum.CRM_ACCOUNT.equals(getImportType())) {
            addField(CustomFormConstants.CRM_ACCOUNT_TYPE, accountType, getTitle(wfmStrings.accountType()));
        }
        addField(INDUSTRY, industry, getTitle(wfmStrings.industry()));
        addField(ACCOUNT_EMAIL, email, getTitle(wfmStrings.email()));
        addField(ACCOUNT_PHONE, phone, getTitle(wfmStrings.phone()));
        addField(ACCOUNT_FAX, fax, getTitle(wfmStrings.fax()));
        addField(ACCOUNT_WEB_SITE, webSite, getTitle(wfmStrings.website()));
        //Address Information
        addTitleField(ADDRESS_INFORMATION, wfmStrings.addressInformation());
        addField(BILLING_ADDRESS, billAddress, getTitle(wfmStrings.billingAddress()));
        addField(MAILING_ADDRESS, mailAddress, getTitle(wfmStrings.mailingAddress()));
        //Financial Information
        addTitleField(FINANCIAL_INFORMATION, wfmStrings.financialInformation());
        addField(CURRENCY, currency, getTitle(wfmStrings.currency()));
        addField(VAT_NUMBER, vatNumber, getTitle(wfmStrings.vatNumber()));
        addField(PAYMENT_METHOD, paymentMethod, getTitle(wfmStrings.paymentMethod()));
        addField(REGISTRATION_NUMBER, registrationNumber, getTitle(wfmStrings.registrationNumber()));
        addField(CLIENT_INVOICE_TERM, term, getTitle(wfmStrings.terms()));
        addField(CRM_NOTE, note, getTitle(wfmStrings.note()));
        //Custom Fields
        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(getCustomForm(), objectId);
        show();
    }

    FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    protected CustomForm getCustomForm() {
        return this;
    }
}
