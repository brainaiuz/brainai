package com.edatasite.workforce.gwt.client.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountService;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelService;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelServiceAsync;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.BalanceAsOfDateWidget;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomList;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomListItem;
import com.edatasite.workforce.gwt.core.client.ui.customlist.Design;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.ui.AddCrmAccountView;
import com.edatasite.workforce.gwt.crm.client.ui.view.crmsubitemtable.CrmSubItemTable;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.GccTreatmentSettingWidget;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 5/31/11
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddClientView extends AddCrmAccountView {

    protected static NumberFormat numberFormat = Utils.getCalculationNumberFormat();

    private final Integer calculationScale = Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2;
    private final PriceLevelServiceAsync priceLevelService = PriceLevelService.App.get();
    private SubsidiariesLookUp subsidiariesLookUp;
    private AccountsReceivablePayableLookUp accountsReceivableLookUp;
    private KpiSwitcher createGlAccountCheckBox;

    private Label customerBalanceCurrencyLabel;
    private BalanceAsOfDateWidget supplierBalanceAsOfDate;
    private Label supplierBalanceCurrencyLabel;
    private CustomList priceLavelList;
    private CustomList discountList;
    public Div itemTableWrap;

    private TextBox creditLimit;
    private TextBox quoteCreditLimit;

    private TextBox pasportNumber;

    protected boolean isOpeningBalanceEnabled = true;
    private final String nameDefaultValue = "";

    protected String FORM_NAME = "";
    protected SelectItem[] appliedPriceLavel;
    private DataListBox bankAccounts;
    private DataListBox clientTypes;
    private DataListBox vatCategories;

    private WarehouseLookUp warehouse;
    private DepartmentLookUp department;
    private CrmSubItemTable itemTable;
    private BalanceAsOfDateWidget balanceAsOfDate;
    private boolean isvalid = false;
    private boolean isvalide = false;

    @Override
    protected String getFormID() {
        return LayoutRPC.CLIENT_FORM;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.PM_CUSTOMER_ADD_CLIENT;
    }

    @Override
    protected boolean cityDistrictEnabledForAddress() {
        return true;
    }

    public AddClientView(Integer objectId) {
        super(SUPPLIER_LIST);
        property = new Property(getPropertyCode());
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.customer()));
        isClientAddress = true;
        if (objectId != null) {
            setDescription(property.getSingular(wfmStrings.editClient(), wfmStrings.customer()));
            super.viewName = property.getSingular(wfmStrings.editClient(), wfmStrings.customer());
            super.objectId = objectId;
        }
        setClientAddView(true);
    }

    public AddClientView(Integer objectId, String[] params) {
        this(objectId);
        isAddView = "add".equals(params[0]);
        if (params != null && params.length > 1 && params[1] != null) {
            FORM_NAME = params[1];
        }
    }

    public AddClientView(String viewName) {
        super(viewName);
    }

    public AddClientView(String viewName, String viewDescription) {
        super(viewName, viewDescription);
    }

    public String getNameDefaultValue() {
        return this.nameDefaultValue;
    }

    @Override
    protected void registerFields() {
        LoadingPanel.loading(true);
        ClientService.App.get().editAccount(objectId, CrmAccountItem.CUSTOMER, new AbstractAsyncCallback<CrmAccountItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(CrmAccountItem clientData) {
                LoadingPanel.loading(false);

                formPropertyMap = clientData.getFormProperty();
                initialize();
                addFieldsToForm();
                getCustomFieldUtil().drawCustomFields(AddClientView.this, objectId, LayoutRPC.VIEW.equals(getFormType()));
                show();

                if (clientData.getLogoUrl() != null && clientData.getLogoUrl().length() > 0) {
                    profilePicture.initialize(clientData.getLogoUrl(), "", "", true);
                } else {
                    profilePicture.initialize(imageUrl, "", "", true);
                }

                setItem(clientData);
                fillFieldWithValue();
                fillPriceLavel(clientData.getCurrencyId());
                fillDiscountList(clientData.getAppliedDiscounts());

                itemTable = new CrmSubItemTable(clientData);
                itemTableWrap.add(itemTable);

                if (objectId == null) {
                    setDefaultValues();
                    setDefaultValuesByFormProperty();
                }
            }
        });
    }


    public void initialize() {
        super.initialize();
        subsidiariesLookUp = Utils.hasGenericAccess(GenericSettingsEnum.CONSIGNMENT_FUNCTION_ENABLE) && Utils.isMultiCompanySubsidiary() ? new SubsidiariesLookUp(true) : new SubsidiariesLookUp();
        subsidiariesLookUp.ensureDebugId("subsidiariesLookUp");

        subsidiariesLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onSubsidiaryChange());
        subsidiariesLookUp.getSuggestBox().addKeyUpHandler(event -> onSubsidiaryChange());

        accountsReceivableLookUp = new AccountsReceivablePayableLookUp(isSupplierAddView() ? Constants.PAYABLE : Constants.RECEIVABLE);
        accountsReceivableLookUp.ensureDebugId("accountreceivable-lookUp");

        createGlAccountCheckBox = new KpiSwitcher();
        createGlAccountCheckBox.ensureDebugId("createGlAccountCheckBox");
        createGlAccountCheckBox.addValueChangeHandler(valueChangeEvent -> {
            accountsReceivableLookUp.setEnabled(!createGlAccountCheckBox.getValue());
            accountsReceivableLookUp.clear();
        });

        balanceAsOfDate = new BalanceAsOfDateWidget();

        supplierBalanceAsOfDate = new BalanceAsOfDateWidget();

        Validation.addNumericKeyboardListener(balanceAsOfDate.getBalanceField(), calculationScale, true);
        Validation.addNumericKeyboardListener(supplierBalanceAsOfDate.getBalanceField(), calculationScale, false);

        customerBalanceCurrencyLabel = new Label();
        supplierBalanceCurrencyLabel = new Label();

        creditLimit = new TextBox();
        creditLimit.ensureDebugId("creditLimit");
        creditLimit.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(creditLimit, calculationScale);

        quoteCreditLimit = new TextBox();
        quoteCreditLimit.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(quoteCreditLimit, calculationScale);

        priceLavelList = new CustomList(Design.CHECK, true);
        priceLavelList.setSearchText(wfmStrings.priceLevel());
        priceLavelList.setHeight(150);
        priceLavelList.ensureDebugId("text-area");
        priceLavelList.addStyleName(DEFAULT_WIDTH);

        discountList = new CustomList(Design.CHECK, true);
        discountList.setSearchText(wfmStrings.discount());
        discountList.setHeight(150);
        discountList.addStyleName(DEFAULT_WIDTH);

        bankAccounts = new DataListBox();
        bankAccounts.ensureDebugId("bankAccount");
        bankAccounts.addStyleName(DEFAULT_WIDTH);
        bankAccounts.addClickHandler(clickEvent -> item.setBankAccountId(bankAccounts.getSelectedId()));

        clientTypes = new DataListBox();
        clientTypes.addStyleName(DEFAULT_WIDTH);
        clientTypes.ensureDebugId("clienttype-checkBox");

        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED)) {
            currency.addValueChangeHandler(event -> onMultiCurrencyChange());
        }

        vatCategories = new DataListBox();
        vatCategories.addStyleName(DEFAULT_WIDTH);
        vatCategories.ensureDebugId("vatCategory-checkBox");

        pasportNumber = new TextBox();
        pasportNumber.ensureDebugId( "pasportNumber");

        if (treatmentWidget instanceof GccTreatmentSettingWidget) {
            GccTreatmentSettingWidget gccWidget = (GccTreatmentSettingWidget) treatmentWidget;
            gccWidget.getTaxTreatments().addValueChangeHandler(valueChangeEvent -> {
                vatCategories.setSelectedNullLabel();
                vatCategories.setValue(null);
                vatCategories.setEnabled(false);
                pasportNumber.setText("");
                pasportNumber.setEnabled(false);
                taxLookUp.setEnabled(true);
                isvalid = false;
                if (NON_VAT_REGISTERED.equals(gccWidget.getTaxTreatments().getSelectedItem().getCode())) {
                    vatCategories.setEnabled(true);
                    pasportNumber.setEnabled(true);
                    isvalid = true;
                    isvalide = false;
                    taxLookUp.setEnabled(false);
                    taxLookUp.setValue(null);
                }
                if (VAT_REGISTERED.equals(gccWidget.getTaxTreatments().getSelectedItem().getCode())) {
                    isvalide = true;
                    isvalid = false;
                }

            });
        }

        warehouse = new WarehouseLookUp();
        department = new DepartmentLookUp();
        itemTableWrap = new Div();
    }

    private void onSubsidiaryChange() {
        SelectItem selectedSubsidiary = subsidiariesLookUp.getSelectedItem();
        if (selectedSubsidiary != null) {
            Integer currencyID = subsidiariesLookUp.getSubsidiaryCurrencyID();
            currency.setSelected(currencyID);
            currency.setEnabled(false);
            onMultiCurrencyChange();
        } else {
            currency.setSelectedNullLabel();
            currency.setEnabled(true);
            onMultiCurrencyChange();
        }
    }

    private void onMultiCurrencyChange() {
        boolean isBaseCurrency = item.getBaseCurrencyID() != null && item.getBaseCurrencyID().equals(currency.getSelectedId());
//        boolean enable = this.isOpeningBalanceEnabled && isBaseCurrency;
        accountsReceivableLookUp.clear();
        accountsReceivableLookUp.setCurrencyID(currency.getSelectedId());
        supplierBalanceAsOfDate.setEnabled(isOpeningBalanceEnabled);
        balanceAsOfDate.setEnabled(isOpeningBalanceEnabled);
        if (isBaseCurrency) {
            if (item.getBalanceAmount() != null) {
                balanceAsOfDate.setText(numberFormat.format(item.getBalanceAmount()));
            }
            if (item.getSupplierBalanceAmount() != null) {
                supplierBalanceAsOfDate.setText(numberFormat.format(item.getSupplierBalanceAmount()));
            }
        } else {
            balanceAsOfDate.setText("");
            supplierBalanceAsOfDate.setText("");
        }
    }

    @Override
    public void addFieldsToForm() {
        super.addFieldsToForm();
        if (Utils.isAccounting() || Utils.isLogistics()) {

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_AS_OF_DATE) != null) {
                addField(CustomFormConstants.CLIENT_AS_OF_DATE, balanceAsOfDate, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_AS_OF_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_AS_OF_DATE).getTitle() : wfmStrings.openingBalanceAsOfDate(), formPropertyMap.get(CustomFormConstants.CLIENT_AS_OF_DATE).isRequired()));
//                balanceAsOfDate.setEnabled(!formPropertyMap.get(CustomFormConstants.CLIENT_AS_OF_DATE).isDisabled());
                balanceAsOfDate.setEnabled(isOpeningBalanceEnabled);
            } else {
                addField(CLIENT_AS_OF_DATE, balanceAsOfDate, wfmStrings.openingBalanceAsOfDate());
            }

            addField(CustomFormConstants.CLIENT_AMOUNT_CURRENCY, customerBalanceCurrencyLabel, null);

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_AS_OF_DATE) != null) {
                addField(CustomFormConstants.SUPPLIER_AS_OF_DATE, supplierBalanceAsOfDate, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_AS_OF_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_AS_OF_DATE).getTitle() : wfmStrings.openingBalanceAsOfDate(), formPropertyMap.get(CustomFormConstants.SUPPLIER_AS_OF_DATE).isRequired()));
                supplierBalanceAsOfDate.setEnabled(isOpeningBalanceEnabled);
            } else {
                addField(SUPPLIER_AS_OF_DATE, supplierBalanceAsOfDate, wfmStrings.openingBalanceAsOfDate());
            }

            addField(CustomFormConstants.SUPPLIER_AMOUNT_CURRENCY, supplierBalanceCurrencyLabel, null);

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_CREDIT_LIMIT) != null) {
                addField(CustomFormConstants.CLIENT_CREDIT_LIMIT, creditLimit, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_CREDIT_LIMIT).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_CREDIT_LIMIT).getTitle() : wfmStrings.creditLimit(), formPropertyMap.get(CustomFormConstants.CLIENT_CREDIT_LIMIT).isRequired()));
                creditLimit.setEnabled(!formPropertyMap.get(CustomFormConstants.CLIENT_CREDIT_LIMIT).isDisabled());
            } else {
                addField(CustomFormConstants.CLIENT_CREDIT_LIMIT, creditLimit, getTitle(wfmStrings.creditLimit()));
            }
            if (Utils.hasGenericAccess(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
                if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT) != null) {
                    addField(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT, quoteCreditLimit, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT).getTitle() : wfmStrings.quoteCreditLimit(), formPropertyMap.get(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT).isRequired()));
                    quoteCreditLimit.setEnabled(!formPropertyMap.get(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT).isDisabled());
                } else {
                    addField(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT, quoteCreditLimit, getTitle(wfmStrings.quoteCreditLimit()));
                }
            }
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRICE_LEVEL) != null) {
                addField(CustomFormConstants.PRICE_LEVEL, priceLavelList, getTitle(formPropertyMap.get(CustomFormConstants.PRICE_LEVEL).isChanged() ? formPropertyMap.get(CustomFormConstants.PRICE_LEVEL).getTitle() : wfmStrings.priceLevel(), formPropertyMap.get(CustomFormConstants.PRICE_LEVEL).isRequired()));
            } else {
                addField(CustomFormConstants.PRICE_LEVEL, priceLavelList, getTitle(wfmStrings.priceLevel()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_DISCOUNT) != null) {
                addField(CustomFormConstants.CLIENT_DISCOUNT, discountList, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_DISCOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_DISCOUNT).getTitle() : wfmStrings.discount(), formPropertyMap.get(CustomFormConstants.CLIENT_DISCOUNT).isRequired()));
            } else {
                addField(CustomFormConstants.CLIENT_DISCOUNT, discountList, getTitle(wfmStrings.discount()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_BANK_ACCOUNT) != null) {
                addField(CustomFormConstants.CLIENT_BANK_ACCOUNT, bankAccounts, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_BANK_ACCOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_BANK_ACCOUNT).getTitle() : Property.getPluralWithObjectCode(Constants.BANKACCOUNT, wfmStrings.bankAccounts()), formPropertyMap.get(CustomFormConstants.CLIENT_BANK_ACCOUNT).isRequired()));
                bankAccounts.setEnabled(!formPropertyMap.get(CustomFormConstants.CLIENT_BANK_ACCOUNT).isDisabled());
            } else {
                addField(CustomFormConstants.CLIENT_BANK_ACCOUNT, bankAccounts, getTitle(Property.getPluralWithObjectCode(Constants.BANKACCOUNT, wfmStrings.bankAccounts())));
            }
            if (VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()){
                if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VAT_CATEGORIES) != null) {
                    addField(VAT_CATEGORIES, vatCategories, getTitle(formPropertyMap.get(CustomFormConstants.VAT_CATEGORIES).isChanged() ? formPropertyMap.get(CustomFormConstants.VAT_CATEGORIES).getTitle() : wfmStrings.taxExemptionReason(), formPropertyMap.get(CustomFormConstants.VAT_CATEGORIES).isRequired()));
                    vatCategories.setEnabled(false);
                } else {
                    addField(CustomFormConstants.VAT_CATEGORIES, vatCategories, getTitle(wfmStrings.taxExemptionReason()));
                }
            }
            if (VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PASSPORT_NUM) != null) {
                    addField(CustomFormConstants.PASSPORT_NUM, pasportNumber, getTitle(formPropertyMap.get(CustomFormConstants.PASSPORT_NUM).isChanged() ? formPropertyMap.get(CustomFormConstants.PASSPORT_NUM).getTitle() : wfmStrings.passportNumber(), formPropertyMap.get(CustomFormConstants.PASSPORT_NUM).isRequired()));
                    pasportNumber.setEnabled(false);
                } else {
                    addField(CustomFormConstants.PASSPORT_NUM, pasportNumber, getTitle(wfmStrings.passportNumber()));
                }
            }
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE) != null) {
                addField(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE, accountsReceivableLookUp, getTitle(formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE).isChanged() ? formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE).getTitle() : (isSupplierAddView() ? wfmStrings.accountsPayable() : wfmStrings.accountsReceivable()), formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE).isRequired()));
                accountsReceivableLookUp.setEnabled(!formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE).isDisabled());
            } else {
                addField(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE, accountsReceivableLookUp, getTitle(isSupplierAddView() ? wfmStrings.accountsPayable() : wfmStrings.accountsReceivable()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.GL_ACCOUNT) != null) {
                addField(CustomFormConstants.GL_ACCOUNT, createGlAccountCheckBox, getTitle(formPropertyMap.get(CustomFormConstants.GL_ACCOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.GL_ACCOUNT).getTitle() : wfmStrings.createGLAccount(), formPropertyMap.get(CustomFormConstants.GL_ACCOUNT).isRequired()));
                createGlAccountCheckBox.setEnabled(!formPropertyMap.get(CustomFormConstants.GL_ACCOUNT).isDisabled());
            } else {
                addField(CustomFormConstants.GL_ACCOUNT, createGlAccountCheckBox, getTitle(wfmStrings.createGLAccount()));
            }

            if (Utils.hasGenericAccess(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP) || Utils.isMultiCompanySubsidiary()) {
                if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_SUBSIDIARIES) != null) {
                    addField(CustomFormConstants.CLIENT_SUBSIDIARIES, subsidiariesLookUp, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_SUBSIDIARIES).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_SUBSIDIARIES).getTitle() : Utils.hasGenericAccess(GenericSettingsEnum.CONSIGNMENT_FUNCTION_ENABLE) && Utils.isMultiCompanySubsidiary() ? wfmStrings.headOffice() : wfmStrings.subsidiary(), formPropertyMap.get(CustomFormConstants.CLIENT_SUBSIDIARIES).isRequired()));
                    subsidiariesLookUp.setEnabled(!formPropertyMap.get(CustomFormConstants.CLIENT_SUBSIDIARIES).isDisabled());
                } else {
                    addField(CustomFormConstants.CLIENT_SUBSIDIARIES, subsidiariesLookUp, getTitle(Utils.hasGenericAccess(GenericSettingsEnum.CONSIGNMENT_FUNCTION_ENABLE) && Utils.isMultiCompanySubsidiary() ? wfmStrings.headOffice() : wfmStrings.subsidiary()));
                }
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_TYPE) != null) {
                addField(CustomFormConstants.CLIENT_TYPE, clientTypes, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_TYPE).getTitle() : Property.get(Constants.CLIENT_LIST, wfmStrings.clientType(), wfmStrings.customer()), formPropertyMap.get(CustomFormConstants.CLIENT_TYPE).isRequired()));
                clientTypes.setEnabled(!formPropertyMap.get(CustomFormConstants.CLIENT_TYPE).isDisabled());
            } else {
                addField(CustomFormConstants.CLIENT_TYPE, clientTypes, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.clientType(), wfmStrings.customer())));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WAREHOUSE) != null) {
                addField(CustomFormConstants.WAREHOUSE, warehouse, getTitle(formPropertyMap.get(CustomFormConstants.WAREHOUSE).isChanged() ? formPropertyMap.get(CustomFormConstants.WAREHOUSE).getTitle() : wfmStrings.defaultWarehouse(), formPropertyMap.get(CustomFormConstants.WAREHOUSE).isRequired()));
                warehouse.setEnabled(!formPropertyMap.get(CustomFormConstants.WAREHOUSE).isDisabled());
            } else {
                addField(CustomFormConstants.WAREHOUSE, warehouse, getTitle(wfmStrings.defaultWarehouse()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null) {
                addField(CustomFormConstants.DEPARTMENT, department, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT).getTitle() : wfmStrings.default2() + " " + Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), formPropertyMap.get(CustomFormConstants.DEPARTMENT).isRequired()));
                department.setEnabled(!formPropertyMap.get(CustomFormConstants.DEPARTMENT).isDisabled());
            } else {
                addField(CustomFormConstants.DEPARTMENT, department, getTitle(wfmStrings.default2() + " " + Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
            }
            addField(CustomFormConstants.CLIENT_ITEM_TABLE, itemTableWrap, null, true);
        }
    }

    public void fillPriceLavel(Integer currenceID) {
        if (currenceID != null) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setCurrencyID(currenceID);
            fp.setCorporate(true);
            fp.setShowHidden(true);
            priceLevelService.getPriceLevelList(fp, new AsyncCallback<ListResult<PriceLevelItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void onSuccess(ListResult<PriceLevelItem> result) {

                    SelectItem all = new SelectItem(0, "<b>" + wfmStrings.selectAll() + "</b>");
                    initClientListWidget(all, result, appliedPriceLavel);
                }
            });
        }
    }

    public void fillDiscountList(SelectItem[] appliedDiscounts) {
        if (!Utils.hasPermission(PermissionConstants.ACCOUNTING_DISCOUNTS_LIST)) {
            return;
        }
        ListingFilterParameter lp = new ListingFilterParameter();
        DiscountService.App.get().getDiscountList(lp, new AsyncCallback<ListResult<DiscountItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(ListResult<DiscountItem> result) {
                SelectItem all = new SelectItem(0, "<b>" + wfmStrings.selectAll() + "</b>");
                initClientDscounts(all, result, appliedDiscounts);
            }
        });
    }

    @Override
    protected void getDataToFillFields() {
    }

    @Override
    public void fillFieldWithValue() {
        super.fillFieldWithValue();
        if (item.getBalanceDate() != null && item.getBalanceDate().getDate() != null) {
            balanceAsOfDate.setDate(item.getBalanceDate().getNonConvertedDate());
        }
        if (item.getBalanceAmount() != null) {
            balanceAsOfDate.setText(numberFormat.format(item.getBalanceAmount()));
        }

        if (item.getSupplierBalanceDate() != null && item.getSupplierBalanceDate().getDate() != null) {
            supplierBalanceAsOfDate.setDate(item.getSupplierBalanceDate().getNonConvertedDate());
        }
        if (item.getSupplierBalanceAmount() != null) {
            supplierBalanceAsOfDate.setText(numberFormat.format(item.getSupplierBalanceAmount()));
        }
        if (item.getCreditLimit() != null) {
            creditLimit.setText(numberFormat.format(item.getCreditLimit()));
        }
        if (item.getPassportNumber() != null) {
            pasportNumber.setText(item.getPassportNumber());
        }
        if (item.getQuoteCreditLimit() != null) {
            quoteCreditLimit.setText(numberFormat.format(item.getQuoteCreditLimit()));
        }
        if (item.getSubsidiary() != null) {
            subsidiariesLookUp.addItem(item.getSubsidiary());
            onSubsidiaryChange();
        }

        if (item.getBankAccounts() != null) {
            bankAccounts.setItems(item.getBankAccounts());
            bankAccounts.setSelected(item.getBankAccountId());
        }
        if (item.getClientTypes() != null) {
            clientTypes.setItems(item.getClientTypes());
            if (item.getClientType() != null) {
                clientTypes.setSelected(item.getClientType().getId());
            }
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED)) {
            onMultiCurrencyChange();
        }

        if (item.getAccountsReceivablePayable() != null) {
            accountsReceivableLookUp.addAccountItem(item.getAccountsReceivablePayable());
        }

        if (item.isCreateGlAccount()) {
            createGlAccountCheckBox.setValue(item.isCreateGlAccount());
        }

        if (item.getWarehouse() != null) {
            warehouse.setSelected(item.getWarehouse());
        }
        if (item.getDepartment() != null) {
            department.setSelected(item.getDepartment());
        }

        if (item.getVatCategories() != null) {
            vatCategories.setItems(item.getVatCategories());
            if (item.getVatCategory() != null) {
                vatCategories.setSelected(item.getVatCategory().getId());
            }
        }

        if (objectId != null) {
//            if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED)) {
//                isOpeningBalanceEnabled = item.isOpeningBalanceEditable() && (item.getBaseCurrencyID() != null && item.getBaseCurrencyID().equals(currency.getSelectedId()));
//            } else {
//                isOpeningBalanceEnabled = item.isOpeningBalanceEditable();
//            }
            balanceAsOfDate.setEnabled(isOpeningBalanceEnabled);
            supplierBalanceAsOfDate.setEnabled(isOpeningBalanceEnabled);
        }
        if (item.getTaxTreatment() != null
                && (NON_VAT_REGISTERED.equals(item.getTaxTreatment().getCode()))){
            vatCategories.setEnabled(true);
            pasportNumber.setEnabled(true);
            taxLookUp.setEnabled(false);
            isvalid = true;
            isvalide = false;
        }
        if (item.getTaxTreatment() != null
                && (VAT_REGISTERED.equals(item.getTaxTreatment().getCode()))){
            isvalide = true;
            isvalid = false;
        }
    }

    public void setItem(CrmAccountItem item) {
        this.item = item;
        this.appliedPriceLavel = item.getAppliedPriceLavel();
    }

    @Override
    public void save() {
        if (!validate()) {
            enableButton(true);
            return;
        }
        setValuesToRPC();
        if (itemTable != null) {
            item.setItems(itemTable.getItemsData());
        }
        LoadingPanel.loading(true);
        enableButton(false);
        CRMService.App.get().saveAccount(item, CrmAccountItem.CUSTOMER, null, false, false, false, true, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer accountID) {
                LoadingPanel.loading(false);
                enableButton(true);
                if (accountID != null) {
                    if (accountID > 0) {
                        item.setObjectId(accountID);
                        Info.show(getSuccessMessage(), Info.Type.INFO);
                        onShellOk(accountID);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CLIENT_ADD, item.getObjectId(), AddClientView.this);
                    } else {
                        if (accountID == -1) {
                            name.getTextBox().setFocus(true);
                            name.getTextBox().addStyleName(ERROR_FORM_STYLE);
                            Info.show(wfmStrings.accountWithThisCompanyNameAlreadyExists(), Info.Type.WARNING);
                        }
                        if (accountID == -2) {
                            number.setFocus(true);
                            number.addStyleName(ERROR_FORM_STYLE);
                            Info.show(wfmStrings.accountWithThisCompanyNumberAlreadyExists(), Info.Type.WARNING);
                        }
                    }
                }

            }
        });
    }

    @Override
    protected boolean validate() {
        clearErrorStyle();
        TextBox clientNumberBox = new TextBox();
//        name.getTextBox().setFocus(true);
//        name.getTextBox().removeStyleName(ERROR_FORM_STYLE);
        errors = 0;
        errors = super.customValidate();
        if (isAddView) {
            clientNumberBox.setText(prefix.getValue().toString() + intNumber.getValue().toString());
        } else {
            clientNumberBox.setText(prefix.getText());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).isRequired()) {
            errors += markAsError(email, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).getTitle() : wfmStrings.email(), email, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_EMAIL).getMinChar()) && !Validation.validEmailFormat(email.getText(), false));
        } else {
            errors += markAsError(email, !"".equals(email.getText()) && !Validation.validEmailFormat(email.getText(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isRequired()) {
            errors += markAsError(name, name.getTextBox().getText() == null || "".equals(name.getTextBox().getText()) || LookUp.wfmStrings.searchTypeMessage().equals(name.getTextBox().getText()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_OWNER) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_OWNER).isRequired()) {
            errors += markAsError(owners, owners.getSelectedItem() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE) != null && formPropertyMap.get(CustomFormConstants.CRM_NOTE).isRequired() && noteWidget != null && noteWidget.getTextBox() != null) {
            errors += markAsError(noteWidget, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_NOTE).getTitle() : wfmStrings.notes(), noteWidget.getTextBox().getTextArea(), formPropertyMap.get(CustomFormConstants.CRM_NOTE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PARENT).isRequired()) {
            errors += markAsError(parentName.getTextBox(), parentName.getSelectedItemID() != null && parentName.getSelectedItemID().equals(objectId));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).isRequired()) {
            errors += markAsError(number, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).isChanged()
                    ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).getTitle() : wfmStrings.number(), clientNumberBox, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NUMBER).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT) != null && formPropertyMap.get(CustomFormConstants.PRIMARY_CONTACT).isRequired()) {
            errors += markAsError(primaryContactLookup, primaryContactLookup.getSelectedItemID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isRequired()) {
            errors += markAsError(types, types.getValuesMap() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isRequired()) {
            errors += markAsError(industry, industry.getSelectedId() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).isRequired()) {
            errors += markAsError(phone.getPhoneFeild(), !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).getTitle() : wfmStrings.phone(), phone.getPhoneFeild(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_PHONE).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).isRequired()) {
            errors += markAsError(fax, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).getTitle() : wfmStrings.fax(), fax.getPhoneFeild(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_FAX).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).isRequired()) {
            errors += markAsError(webSite, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).getTitle() : wfmStrings.accountWebsite(), webSite, formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_WEBSITE).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CURRENCY) != null && formPropertyMap.get(CustomFormConstants.CURRENCY).isRequired()) {
            errors += markAsError(currency, currency != null && currency.getSelectedItem() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD) != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).isRequired()) {
            errors += markAsError(paymentMethod, paymentMethod != null && paymentMethod.getSelectedId() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM) != null && formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).isRequired()) {
            errors += markAsError(termsLookUp, termsLookUp != null && termsLookUp.getSelectedItemID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_BANK_ACCOUNT) != null && formPropertyMap.get(CustomFormConstants.CLIENT_BANK_ACCOUNT).isRequired()) {
            errors += markAsError(bankAccounts, bankAccounts != null && bankAccounts.getSelectedId() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE) != null && formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE).isRequired()) {
            errors += markAsError(accountsReceivableLookUp, accountsReceivableLookUp != null && accountsReceivableLookUp.getSelectedItemID() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VAT_CATEGORIES) != null && formPropertyMap.get(CustomFormConstants.VAT_CATEGORIES).isRequired()) {
            errors += markAsError(vatCategories, vatCategories != null && vatCategories.getSelectedId() == null);
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP) || Utils.isMultiCompanySubsidiary()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_SUBSIDIARIES) != null && formPropertyMap.get(CustomFormConstants.CLIENT_SUBSIDIARIES).isRequired()) {
                errors += markAsError(subsidiariesLookUp, subsidiariesLookUp != null && subsidiariesLookUp.getSelectedItemID() == null);
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_VAT) != null && formPropertyMap.get(CustomFormConstants.CLIENT_VAT).isRequired()) {
            errors += markAsError(taxLookUp, taxLookUp != null && taxLookUp.getSelectedItemID() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_TYPE) != null && formPropertyMap.get(CustomFormConstants.CLIENT_TYPE).isRequired()) {
            errors += markAsError(clientTypes, clientTypes != null && clientTypes.getSelectedId() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WAREHOUSE) != null && formPropertyMap.get(CustomFormConstants.WAREHOUSE).isRequired()) {
            errors += markAsError(warehouse, warehouse != null && warehouse.getSelectedItemID() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT).isRequired()) {
            errors += markAsError(department, department != null && department.getSelectedItemID() == null);
        }

        if (VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && isCustomerOrSupplier() && Utils.isVatRegistered()) {
            if (!treatmentWidget.validate()) {
                Utils.openParentSection(treatmentWidget);
                errors++;
            }
        } else if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && isCustomerOrSupplier() && Utils.isVatRegistered()) {
            if (!treatmentWidget.validate()) {
                Utils.openParentSection(treatmentWidget);
                errors++;
            }
        } else {

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER) != null && formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).isRequired()) {
                errors += markAsError(registrationNumber, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).isChanged() ?
                        formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).getTitle() : wfmStrings.registrationNumber(), registrationNumber, formPropertyMap.get(CustomFormConstants.REGISTRATION_NUMBER).getMinChar()));
            }
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VAT_NUMBER) != null && formPropertyMap.get(CustomFormConstants.VAT_NUMBER).isRequired()) {
                errors += markAsError(vatNumber, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.VAT_NUMBER).isChanged() ?
                        formPropertyMap.get(CustomFormConstants.VAT_NUMBER).getTitle() : wfmStrings.vatNumber(), vatNumber, formPropertyMap.get(CustomFormConstants.VAT_NUMBER).getMinChar()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALES_TYPE) != null && formPropertyMap.get(CustomFormConstants.SALES_TYPE).isRequired()) {
            errors += markAsError(salesType, salesType != null && salesType.getSelectedId() == null);
        }

        if (isvalid){
            errors += markAsError(vatCategories, vatCategories != null && vatCategories.getSelectedId() == null);
        }
        if (isvalide) {
            errors += markAsError(taxLookUp, taxLookUp != null && taxLookUp.getSelectedItemID() == null);
        }
        if (vatCategories.getSelectedValue() != null && isvalid) {
            if ("VATEX-SA-EDU".equals(vatCategories.getSelectedValue().getCode())){
                errors += markAsError(pasportNumber, pasportNumber != null && pasportNumber.getText() == null || "".equals(pasportNumber.getText()));
            }
        }
        if (!isAddView) {
            if (!validateAddressTable(billAddresses)) {
                errors++;
            }
            if (!validateAddressTable(mailAddresses)) {
                errors++;
            }
        } else {
            if (!validateAddressTableColumn(billingAddresses)) {
                errors++;
            }
            if (!validateAddressTableColumn(mailingAddresses)) {
                errors++;
            }
        }
        errors += getCustomFieldUtil().validateCustomFields();

        if (isOpeningBalanceEnabled) {
            //add
            if (Utils.isAccounting() && item.getObjectId() == null && !"".equals(supplierBalanceAsOfDate.getText().trim()) && supplierBalanceAsOfDate.getDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(supplierBalanceAsOfDate.getDate())) {
                Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.supplierOpeningBalance(), Utils.getTransactionLockDate()), Info.Type.WARNING);
                return false;
            }

            if (Utils.isAccounting() && item.getObjectId() == null && !"".equals(balanceAsOfDate.getText().trim()) && balanceAsOfDate.getDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(balanceAsOfDate.getDate())) {
                Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(property.getSingular(wfmStrings.customerOpeningBalance(), wfmStrings.customer()), Utils.getTransactionLockDate()), Info.Type.WARNING);
                return false;
            }

            //edit
            if (Utils.isAccounting() && item.getObjectId() != null) {
                Double supplierBalanceAmountValue = null;
                if (supplierBalanceAsOfDate.getText() != null && !supplierBalanceAsOfDate.getText().trim().isEmpty()) {
                    try {
                        supplierBalanceAmountValue = numberFormat.parse(supplierBalanceAsOfDate.getText());
                    } catch (Exception e) {
                        supplierBalanceAmountValue = Double.valueOf("0.00");
                    }
                }
                if (supplierBalanceAsOfDate.getDate() != null && Utils.isBankingLocked() && (DateUtils.getTransactionLockDate().after(supplierBalanceAsOfDate.getDate())
                        || item.getSupplierBalanceDate() != null && DateUtils.getTransactionLockDate().after(item.getSupplierBalanceDate().getNonConvertedDate()) && DateUtils.getTransactionLockDate().before(supplierBalanceAsOfDate.getDate()))) {

                    if (item.getSupplierBalanceDate() == null ||
                            item.getSupplierBalanceDate().getNonConvertedDate() == null ||
                            !DateUtil.resetTime(item.getSupplierBalanceDate().getNonConvertedDate()).equals(DateUtil.resetTime(supplierBalanceAsOfDate.getDate())) ||
                            (item.getSupplierBalanceAmount() != null && supplierBalanceAmountValue != null && supplierBalanceAmountValue.compareTo(item.getSupplierBalanceAmount()) != 0)) {
                        Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.supplierOpeningBalance(), Utils.getTransactionLockDate()), Info.Type.WARNING);
                        return false;
                    }
                }
            }

            if (Utils.isAccounting() && item.getObjectId() != null) {
                Double customerBalanceAmountValue = null;
                if (balanceAsOfDate.getText() != null && !balanceAsOfDate.getText().trim().isEmpty()) {
                    try {
                        customerBalanceAmountValue = numberFormat.parse(balanceAsOfDate.getText());
                    } catch (Exception e) {
                        customerBalanceAmountValue = Double.valueOf("0.00");
                    }
                }
                if (balanceAsOfDate.getDate() != null && Utils.isBankingLocked() && (DateUtils.getTransactionLockDate().after(balanceAsOfDate.getDate())
                        || item.getBalanceDate() != null && DateUtils.getTransactionLockDate().after(item.getBalanceDate().getNonConvertedDate()) && DateUtils.getTransactionLockDate().before(balanceAsOfDate.getDate()))) {

                    if (customerBalanceAmountValue != null && item.getBalanceAmount() != null && (item.getBalanceDate() == null || item.getBalanceDate().getNonConvertedDate() == null || !DateUtil.resetTime(item.getBalanceDate().getNonConvertedDate()).equals(DateUtil.resetTime(balanceAsOfDate.getDate())) || customerBalanceAmountValue.compareTo(item.getBalanceAmount()) != 0)) {
                        Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(property.getSingular(wfmStrings.customerOpeningBalance(), wfmStrings.customer()), Utils.getTransactionLockDate()), Info.Type.WARNING);
                        return false;
                    }
                }
            }

            if (!"".equals(balanceAsOfDate.getText().trim())) {
                if (!Validation.validateDate(balanceAsOfDate.getDateField(), new HTML(""), false)) {
                    errors++;
                }
            }

            if (!"".equals(supplierBalanceAsOfDate.getText().trim())) {
                if (!Validation.validateDate(supplierBalanceAsOfDate.getDateField(), new HTML(""), false)) {
                    errors++;
                }
            }
        }

        errors += this.markAsError(CustomFormConstants.ATTACHMENTS, this.attachment, this.attachment.getAttachedFiles() == null);

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    public void onShellOk(Integer accountID) {
        if (saveAndClose && !saveAndAddContact) {
            closeTab();
            if (objectId != null && !INVOICE_QUOTE_FORM.equals(FORM_NAME)) {
                SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + objectId, item.getNumber(), item.getName());
            }
        } else if (saveAndAddContact && accountID != null) {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged("contact|add/add" + "/" + accountID);
        } else {
            closeTab("client|add/add");
        }
    }

    @Override
    public void setValuesToRPC(/*do not use this argument in the method*/) {
        super.setValuesToRPC();
        if (creditLimit.getText() != null && !"".equals(creditLimit.getText())) {
            item.setCreditLimit(new BigDecimal(numberFormat.parse(creditLimit.getText())));
        } else {
            item.setCreditLimit(null);
        }
        if (quoteCreditLimit.getText() != null && !"".equals(quoteCreditLimit.getText())) {
            item.setQuoteCreditLimit(new BigDecimal(numberFormat.parse(quoteCreditLimit.getText())));
        } else {
            item.setQuoteCreditLimit(null);
        }

        //Customer Balance
        item.setBalanceDate(balanceAsOfDate.getDate() != null ? new DateNonConvertable(balanceAsOfDate.getDate()) : null);
        if (balanceAsOfDate.getText() != null && !balanceAsOfDate.getText().trim().isEmpty()) {
            try {
                item.setBalanceAmount(numberFormat.parse(balanceAsOfDate.getText()));
            } catch (Exception e) {
            }
        } else {
            item.setBalanceAmount(null);
        }

        //Supplier Balance
        item.setSupplierBalanceDate(supplierBalanceAsOfDate.getDate() != null ? new DateNonConvertable(supplierBalanceAsOfDate.getDate()) : null);
        if (supplierBalanceAsOfDate.getText() != null && !supplierBalanceAsOfDate.getText().trim().isEmpty()) {
            try {
                item.setSupplierBalanceAmount(numberFormat.parse(supplierBalanceAsOfDate.getText()));
            } catch (Exception e) {
            }
        } else {
            item.setSupplierBalanceAmount(null);
        }
        if (priceLavelList != null && priceLavelList.getItems() != null && priceLavelList.getItems().size() > 0) {
            List<SelectItem> appliedPriveces = new ArrayList<>();
            for (CustomListItem client : priceLavelList.getItems()) {
                if (client.getValue()) {
                    appliedPriveces.add(client.getItem());
                }
            }
            item.setAppliedPriceLavel(appliedPriveces.toArray(new SelectItem[]{}));
        }
        if (discountList != null && discountList.getItems() != null && discountList.getItems().size() > 0) {
            List<SelectItem> appliedDiscounts = new ArrayList<>();
            appliedDiscounts.addAll(discountList.getSelectItems());
            item.setAppliedDiscounts(appliedDiscounts.toArray(new SelectItem[]{}));
        }

        item.setSubsidiary(subsidiariesLookUp.getSelectedItem());
        item.setAccountsReceivablePayable(accountsReceivableLookUp.getSelectedData());
        item.setCreateGlAccount(createGlAccountCheckBox.getValue());
        item.setBankAccountId(null);
        if (bankAccounts != null && bankAccounts.getSelectedItem() != null) {
            item.setBankAccountId(bankAccounts.getSelectedItem().getId());
            item.setBankAccount(bankAccounts.getSelectedItem().getName());
        }
        if (clientTypes != null && clientTypes.getSelectedItem() != null) {
            item.setClientType(clientTypes.getSelectedItem());
        }
        if (warehouse.getSelectedItem() != null) {
            item.setWarehouse(new SelectItem(warehouse.getSelectedItemID(), warehouse.getStyleName()));
        }
        if (department.getSelectedItem() != null) {
            item.setDepartment(new SelectItem(department.getSelectedItemID(), department.getStyleName()));
        }

        if (vatCategories != null && vatCategories.isSomethingSelected()) {
            item.setVatCategory(vatCategories.getSelectedItem());
        }
        if (pasportNumber.getText() != null) {
            item.setPassportNumber(pasportNumber.getText());
        }
        if (item.getTaxTreatment() == null
                || (!NON_VAT_REGISTERED.equals(item.getTaxTreatment().getCode()))){
            vatCategories.setSelectedNullLabel();
            vatCategories.setValue(null);
            pasportNumber.setText("");
            item.setVatCategory(null);
        }
    }

    protected void initClientListWidget(SelectItem selectall, ListResult<PriceLevelItem> priceLevels, SelectItem[] appliedPriceLavel) {
        if (priceLavelList.getItems() != null) {
            priceLavelList.removeItems();
        }

        if (priceLevels != null && priceLevels.getTotal() > 0) {
            final CustomListItem checkall = new CustomListItem(selectall);
            priceLavelList.add(checkall);
            checkall.addValueChangeHandler(booleanValueChangeEvent -> priceLavelList.setCheckAllItems(booleanValueChangeEvent.getValue()));


            for (SelectItem level : priceLevels.getList()) {
                CustomListItem item = new CustomListItem(level);
                priceLavelList.add(item);

                /* if (clientID != null && clientID.equals(client.getId())) { // this part run when create price level from sale invoic/qoute form
                    item.setCheck(true);
                }*/

                if (appliedPriceLavel != null && appliedPriceLavel.length > 0) {
                    for (SelectItem appliedClient : appliedPriceLavel) {
                        if (appliedClient.getId().equals(level.getId())) {
                            item.setCheck(true);
                        }
                    }
                }
            }
        }
    }

    private void initClientDscounts(SelectItem selectall, ListResult<DiscountItem> discounts, SelectItem[] appliedDiscounts) {
        if (discountList.getItems() != null) {
            discountList.removeItems();
        }

        if (discounts != null && discounts.getTotal() > 0) {
            final CustomListItem checkall = new CustomListItem(selectall);
            discountList.add(checkall);
            checkall.addValueChangeHandler(event -> discountList.setCheckAllItems(event.getValue()));

            for (SelectItem discount : discounts.getList()) {
                CustomListItem item = new CustomListItem(discount);
                discountList.add(item);

                if (appliedDiscounts != null && appliedDiscounts.length > 0) {
                    for (SelectItem appliedDiscount : appliedDiscounts) {
                        if (appliedDiscount.getId().equals(discount.getId())) {
                            item.setCheck(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onAccountTypeChange() {
        ArrayList<String> checkedTypesList = new ArrayList<>();
        Set<Object> itemsSet = types.getValuesMap().keySet();
        for (Object data : itemsSet) {
            if (data instanceof ReferenceItem) {
                ReferenceItem referenceItem = ((ReferenceItem) data);
                if (referenceItem.isSelected()) {
                    checkedTypesList.add(referenceItem.getCode());
                }
            }
        }

        boolean isCustomerAndSupplierEnabled = checkedTypesList.contains(CrmAccountItem.CUSTOMER) && checkedTypesList.contains(CrmAccountItem.SUPPLIER);
        if (panel != null && panel.getElementById("customerOpeningBalanceTitle") != null && panel.getElementById("supplierOpeningBalanceTitle") != null) {
            if (isCustomerAndSupplierEnabled) {
                panel.getElementById("customerOpeningBalance").getStyle().setDisplay(Style.Display.BLOCK);
                panel.getElementById("supplierOpeningBalance").getStyle().setDisplay(Style.Display.BLOCK);
            } else if (checkedTypesList.contains(CrmAccountItem.CUSTOMER)) {
                panel.getElementById("customerOpeningBalance").getStyle().setDisplay(Style.Display.BLOCK);
                panel.getElementById("supplierOpeningBalance").getStyle().setDisplay(Style.Display.NONE);
            } else if (checkedTypesList.contains(CrmAccountItem.SUPPLIER)) {
                panel.getElementById("customerOpeningBalance").getStyle().setDisplay(Style.Display.NONE);
                panel.getElementById("supplierOpeningBalance").getStyle().setDisplay(Style.Display.BLOCK);
            } else {
                panel.getElementById("customerOpeningBalance").getStyle().setDisplay(Style.Display.BLOCK);
                panel.getElementById("supplierOpeningBalance").getStyle().setDisplay(Style.Display.NONE);
            }

            panel.getElementById("customerOpeningBalanceTitle").getStyle().setDisplay(isCustomerAndSupplierEnabled ? Style.Display.BLOCK : Style.Display.NONE);
            panel.getElementById("supplierOpeningBalanceTitle").getStyle().setDisplay(isCustomerAndSupplierEnabled ? Style.Display.BLOCK : Style.Display.NONE);
        }
    }

    @Override
    public String getPropertyCode() {
        return Constants.CLIENT_LIST;
    }

    protected void setDefaultValuesByFormProperty() {
        super.setDefaultValuesByFormProperty();

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_AS_OF_DATE) != null && formPropertyMap.get(CustomFormConstants.CLIENT_AS_OF_DATE).getDefaultValue() != null && balanceAsOfDate != null) {
            try {
                balanceAsOfDate.setDate(DateUtils.parse(formPropertyMap.get(CustomFormConstants.CLIENT_AS_OF_DATE).getDefaultValue()));
            } catch (DateFormatException e) {
                e.printStackTrace();
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_AS_OF_DATE) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_AS_OF_DATE).getDefaultValue() != null && supplierBalanceAsOfDate != null) {
            try {
                supplierBalanceAsOfDate.setDate(DateUtils.parse(formPropertyMap.get(CustomFormConstants.SUPPLIER_AS_OF_DATE).getDefaultValue()));
            } catch (DateFormatException e) {
                e.printStackTrace();
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_AMOUNT) != null && formPropertyMap.get(CustomFormConstants.CLIENT_AMOUNT).getDefaultValue() != null && balanceAsOfDate != null) {
            balanceAsOfDate.setText(formPropertyMap.get(CustomFormConstants.CLIENT_AMOUNT).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_AMOUNT) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_AMOUNT).getDefaultValue() != null && supplierBalanceAsOfDate != null) {
            supplierBalanceAsOfDate.setText(formPropertyMap.get(CustomFormConstants.SUPPLIER_AMOUNT).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_CREDIT_LIMIT) != null && formPropertyMap.get(CustomFormConstants.CLIENT_CREDIT_LIMIT).getDefaultValue() != null && creditLimit != null) {
            creditLimit.setText(formPropertyMap.get(CustomFormConstants.CLIENT_CREDIT_LIMIT).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT) != null && formPropertyMap.get(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT).getDefaultValue() != null && quoteCreditLimit != null) {
            quoteCreditLimit.setText(formPropertyMap.get(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_BANK_ACCOUNT) != null && formPropertyMap.get(CustomFormConstants.CLIENT_BANK_ACCOUNT).getDefaultValue() != null && bankAccounts != null) {
            bankAccounts.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CLIENT_BANK_ACCOUNT).getSelectedId(), formPropertyMap.get(CustomFormConstants.CLIENT_BANK_ACCOUNT).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VAT_CATEGORIES) != null && formPropertyMap.get(CustomFormConstants.VAT_CATEGORIES).getSelectedId() != null) {
            vatCategories.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.VAT_CATEGORIES).getSelectedId(), formPropertyMap.get(CustomFormConstants.VAT_CATEGORIES).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PASSPORT_NUM) != null && formPropertyMap.get(CustomFormConstants.PASSPORT_NUM).getDefaultValue() != null && pasportNumber != null) {
            pasportNumber.setText(formPropertyMap.get(CustomFormConstants.PASSPORT_NUM).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE) != null && formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE).getDefaultValue() != null && accountsReceivableLookUp != null) {
            accountsReceivableLookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE).getSelectedId(), formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.GL_ACCOUNT) != null && formPropertyMap.get(CustomFormConstants.GL_ACCOUNT).getDefaultValue() != null && createGlAccountCheckBox != null) {
            createGlAccountCheckBox.setValue("true".equals(formPropertyMap.get(CustomFormConstants.GL_ACCOUNT).getDefaultValue()));
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP) || Utils.isMultiCompanySubsidiary()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_SUBSIDIARIES) != null && formPropertyMap.get(CustomFormConstants.CLIENT_SUBSIDIARIES).getDefaultValue() != null && subsidiariesLookUp != null) {
                subsidiariesLookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CLIENT_SUBSIDIARIES).getSelectedId(), formPropertyMap.get(CustomFormConstants.CLIENT_SUBSIDIARIES).getDefaultValue()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_VAT) != null && formPropertyMap.get(CustomFormConstants.CLIENT_VAT).getDefaultValue() != null && taxLookUp != null) {
            taxLookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CLIENT_VAT).getSelectedId(), formPropertyMap.get(CustomFormConstants.CLIENT_VAT).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TAX_TREATMENT) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TAX_TREATMENT).getDefaultValue() != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TAX_TREATMENT).getDefaultValue().contains("###") && treatmentWidget != null) {

            String[] values = formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TAX_TREATMENT).getDefaultValue().split("###");
            if (values != null && values.length > 0) {
                String code = values[0];
                String name = values[1];
                SelectItem item = new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TAX_TREATMENT).getSelectedId(), name);
                item.setCode(code);

                treatmentWidget.setTreatment(item);
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_TYPE) != null && formPropertyMap.get(CustomFormConstants.CLIENT_TYPE).getDefaultValue() != null && clientTypes != null) {
            clientTypes.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CLIENT_TYPE).getSelectedId(), formPropertyMap.get(CustomFormConstants.CLIENT_TYPE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WAREHOUSE) != null && formPropertyMap.get(CustomFormConstants.WAREHOUSE).getDefaultValue() != null && warehouse != null) {
            warehouse.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.WAREHOUSE).getSelectedId(), formPropertyMap.get(CustomFormConstants.WAREHOUSE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT).getDefaultValue() != null && department != null) {
            department.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.DEPARTMENT).getSelectedId(), formPropertyMap.get(CustomFormConstants.DEPARTMENT).getDefaultValue()));
        }
    }

    protected void getSelectedAccountItems() {
        LoadingPanel.loading(true);
        ClientService.App.get().editAccount(objectId, CrmAccountItem.CUSTOMER, new AbstractAsyncCallback<CrmAccountItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(CrmAccountItem clientData) {
                LoadingPanel.loading(false);

                if (clientData.getLogoUrl() != null && clientData.getLogoUrl().length() > 0) {
                    profilePicture.initialize(clientData.getLogoUrl(), "", "", true);
                } else {
                    profilePicture.initialize(imageUrl, "", "", true);
                }

                if (objectId == null) {
                    setDefaultValues();
                }

                setItem(clientData);
                fillFieldWithValue();
                fillPriceLavel(clientData.getCurrencyId());
                fillDiscountList(clientData.getAppliedDiscounts());

                itemTable = new CrmSubItemTable(clientData);
                itemTableWrap.add(itemTable);
            }
        });
    }
}
