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
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomList;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomListItem;
import com.edatasite.workforce.gwt.core.client.ui.customlist.Design;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.ui.AddCrmAccountDynamicView;
import com.edatasite.workforce.gwt.crm.client.ui.view.crmsubitemtable.CrmSubItemTable;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartTaxRateLookUp;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
public class ClientDynamicView extends AddCrmAccountDynamicView {

    protected static NumberFormat numberFormat = Utils.getCalculationNumberFormat();

    private Integer calculationScale = Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2;
    private PriceLevelServiceAsync priceLevelService = PriceLevelService.App.get();
    private SubsidiariesLookUp subsidiariesLookUp;
    private SmartTaxRateLookUp taxLookUp;
    private AccountsReceivablePayableLookUp accountsReceivableLookUp;
    private KpiCheckBox createGlAccountCheckBox;

    private DatePicker customerBalanceDate;
    private TextBox customerBalanceAmount;
    private Label customerBalanceCurrencyLabel;
    private DatePicker supplierBalanceDate;
    private TextBox supplierBalanceAmount;
    private Label supplierBalanceCurrencyLabel;
    private CustomList priceLavelList;
    private CustomList discountList;
    private Div itemTableWrap;

    private TextBox creditLimit;
    private TextBox quoteCreditLimit;


    protected boolean isOpeningBalanceEnabled = true;
    private String nameDefaultValue = "";

    protected String FORM_NAME = "";
    protected SelectItem[] appliedPriceLavel;
    private DataListBox bankAccounts;
    private DataListBox clientTypes;

    private WarehouseLookUp warehouse;
    private DepartmentLookUp department;
    private CrmSubItemTable itemTable;

    @Override
    protected String getFormID() {
        return LayoutRPC.CLIENT_FORM;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.PM_CUSTOMER_ADD_CLIENT;
    }

    public ClientDynamicView(Integer objectId) {
        super("addaccountdynamic", Property.get(Constants.CLIENT_LIST, wfmStrings.addMess(), wfmStrings.customer()));
        if (objectId != null) {
            setDescription(Property.get(Constants.CLIENT_LIST, wfmStrings.editClient(), wfmStrings.customer()));
            super.viewName = Property.get(Constants.CLIENT_LIST, wfmStrings.editClient(), wfmStrings.customer());
            super.objectId = objectId;
        }
        setClientAddView(true);
    }

    public ClientDynamicView(Integer objectId, String[] params) {
        this(objectId);
        if (params != null && params.length > 1 && params[1] != null) {
            FORM_NAME = params[1];
        }
    }

    public ClientDynamicView(String viewName, String viewDescription) {
        super(viewName, viewDescription);
    }

    public String getNameDefaultValue() {
        return this.nameDefaultValue;
    }

    @Override
    public void initialize() {
        super.initialize();
        subsidiariesLookUp = Utils.hasGenericAccess(GenericSettingsEnum.CONSIGNMENT_FUNCTION_ENABLE) && Utils.isMultiCompanySubsidiary() ? new SubsidiariesLookUp(true) : new SubsidiariesLookUp();
        subsidiariesLookUp.ensureDebugId("subsidiariesLookUp");

        subsidiariesLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onSubsidiaryChange());
        subsidiariesLookUp.getSuggestBox().addKeyUpHandler(event -> onSubsidiaryChange());

        taxLookUp = new SmartTaxRateLookUp(RECEIVABLE);
        taxLookUp.ensureDebugId("taxLookUp");
        taxLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> taxLookUp.islink());

        accountsReceivableLookUp = new AccountsReceivablePayableLookUp(isClientAddView() ? Constants.RECEIVABLE : Constants.PAYABLE);
        accountsReceivableLookUp.ensureDebugId("accountreceivable-lookUp");

        createGlAccountCheckBox = new KpiCheckBox();
        createGlAccountCheckBox.ensureDebugId("createGlAccountCheckBox");
        createGlAccountCheckBox.addValueChangeHandler(valueChangeEvent -> {
            accountsReceivableLookUp.setEnabled(!createGlAccountCheckBox.getValue());
            accountsReceivableLookUp.clear();
        });
        customerBalanceDate = new DatePicker();
        customerBalanceDate.ensureDebugId("asOfDatePicker");
        customerBalanceAmount = new TextBox();
        customerBalanceAmount.ensureDebugId("openingBalance-textbox");
        customerBalanceAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        supplierBalanceDate = new DatePicker();
        supplierBalanceAmount = new TextBox();
        supplierBalanceAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        Validation.addNumericKeyboardListener(customerBalanceAmount, calculationScale, true);
        Validation.addNumericKeyboardListener(supplierBalanceAmount, calculationScale, true);

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
        boolean enable = this.isOpeningBalanceEnabled && isBaseCurrency;
        accountsReceivableLookUp.clear();
        accountsReceivableLookUp.setCurrencyID(currency.getSelectedId());
        customerBalanceAmount.setEnabled(enable);
        supplierBalanceAmount.setEnabled(enable);
        customerBalanceDate.setEnabled(enable);
        supplierBalanceDate.setEnabled(enable);
        if (isBaseCurrency) {
            if (item.getBalanceAmount() != null) {
                customerBalanceAmount.setText(numberFormat.format(item.getBalanceAmount()));
            }
            if (item.getSupplierBalanceAmount() != null) {
                supplierBalanceAmount.setText(numberFormat.format(item.getSupplierBalanceAmount()));
            }
        } else {
            customerBalanceAmount.setText("");
            supplierBalanceAmount.setText("");
        }
    }

    @Override
    public void addFieldsToForm() {
        super.addFieldsToForm();
        if (Utils.isAccounting() || Utils.isLogistics()) {
            addField(CLIENT_AS_OF_DATE, customerBalanceDate, wfmStrings.asOfDate());
            addField(CustomFormConstants.CLIENT_AMOUNT, customerBalanceAmount, getTitle(wfmStrings.openingBalance()));
            addField(CustomFormConstants.CLIENT_AMOUNT_CURRENCY, customerBalanceCurrencyLabel, null);

            addField(SUPPLIER_AS_OF_DATE, supplierBalanceDate, wfmStrings.asOfDate());
            addField(CustomFormConstants.SUPPLIER_AMOUNT, supplierBalanceAmount, getTitle(wfmStrings.openingBalance()));
            addField(CustomFormConstants.SUPPLIER_AMOUNT_CURRENCY, supplierBalanceCurrencyLabel, null);

            addField(CustomFormConstants.CLIENT_CREDIT_LIMIT, creditLimit, getTitle(wfmStrings.creditLimit()));
            if (Utils.hasGenericAccess(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
                addField(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT, quoteCreditLimit, getTitle(wfmStrings.quoteCreditLimit()));
            }
            addField(CustomFormConstants.PRICE_LEVEL, priceLavelList, getTitle(wfmStrings.priceLevel()));
            addField(CustomFormConstants.CLIENT_DISCOUNT, discountList, getTitle(wfmStrings.discount()));

            addField(CustomFormConstants.CLIENT_BANK_ACCOUNT, bankAccounts, getTitle(Property.getPluralWithObjectCode(Constants.BANKACCOUNT, wfmStrings.bankAccounts())));
            addField(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE, accountsReceivableLookUp, getTitle(isClientAddView() ? wfmStrings.accountsReceivable() : wfmStrings.accountsPayable()));
            addField(CustomFormConstants.GL_ACCOUNT, createGlAccountCheckBox, getTitle(wfmStrings.createGLAccount()));

            if (Utils.hasGenericAccess(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP) || Utils.isMultiCompanySubsidiary()) {
                addField(CustomFormConstants.CLIENT_SUBSIDIARIES, subsidiariesLookUp, getTitle(Utils.hasGenericAccess(GenericSettingsEnum.CONSIGNMENT_FUNCTION_ENABLE) && Utils.isMultiCompanySubsidiary() ? wfmStrings.headOffice() : wfmStrings.subsidiary()));
            }
            if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                addField(CustomFormConstants.CLIENT_VAT, taxLookUp, getTitle(wfmStrings.tax()));
            } else if (!GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode())) {
                addField(CustomFormConstants.CLIENT_VAT, taxLookUp, getTitle(wfmStrings.tax()));
            }
            addField(CustomFormConstants.CLIENT_TYPE, clientTypes, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.clientType(), wfmStrings.customer())));
            addField(CustomFormConstants.IMAGE_UPLOAD, profilePicture, null, true);

            addField(CustomFormConstants.WAREHOUSE, warehouse, getTitle(wfmStrings.defaultWarehouse()));
            addField(CustomFormConstants.DEPARTMENT, department, getTitle(wfmStrings.default2() + " " + Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
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

    private void fillDiscountList(SelectItem[] appliedDiscounts) {
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

    @Override
    public void fillFieldWithValue() {
        super.fillFieldWithValue();
        if (item.getBalanceDate() != null && item.getBalanceDate().getDate() != null) {
            customerBalanceDate.setDate(item.getBalanceDate().getNonConvertedDate());
        }
        if (item.getBalanceAmount() != null) {
            customerBalanceAmount.setText(numberFormat.format(item.getBalanceAmount()));
        }

        if (item.getSupplierBalanceDate() != null && item.getSupplierBalanceDate().getDate() != null) {
            supplierBalanceDate.setDate(item.getSupplierBalanceDate().getNonConvertedDate());
        }
        if (item.getSupplierBalanceAmount() != null) {
            supplierBalanceAmount.setText(numberFormat.format(item.getSupplierBalanceAmount()));
        }
        if (item.getCreditLimit() != null) {
            creditLimit.setText(numberFormat.format(item.getCreditLimit()));
        }
        if (item.getQuoteCreditLimit() != null) {
            quoteCreditLimit.setText(numberFormat.format(item.getQuoteCreditLimit()));
        }
        if (item.getSubsidiary() != null) {
            subsidiariesLookUp.addItem(item.getSubsidiary());
            onSubsidiaryChange();
        }

        if (item.getVat() != null) {
            taxLookUp.addItem(item.getVat());
            taxLookUp.setSelected(item.getVat().getId());
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

        if (item.getWarehouse() != null) {
            warehouse.setSelected(item.getWarehouse());
        }
        if (item.getDepartment() != null) {
            department.setSelected(item.getDepartment());
        }

        if (objectId != null) {
            if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED)) {
                isOpeningBalanceEnabled = item.isOpeningBalanceEditable() && (item.getBaseCurrencyID() != null && item.getBaseCurrencyID().equals(currency.getSelectedId()));
            } else {
                isOpeningBalanceEnabled = item.isOpeningBalanceEditable();
            }
            customerBalanceDate.setEnabled(isOpeningBalanceEnabled);
            customerBalanceAmount.setEnabled(isOpeningBalanceEnabled);
            supplierBalanceDate.setEnabled(isOpeningBalanceEnabled);
            supplierBalanceAmount.setEnabled(isOpeningBalanceEnabled);
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
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CLIENT_ADD, item.getObjectId(), ClientDynamicView.this);
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
        if (super.validate()) {
            if (isOpeningBalanceEnabled) {
                //add
                if (Utils.isAccounting() && item.getObjectId() == null && !"".equals(supplierBalanceAmount.getText().trim()) && supplierBalanceDate.getDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(supplierBalanceDate.getDate())) {
                    Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.supplierOpeningBalance(), Utils.getTransactionLockDate()), Info.Type.WARNING);
                    return false;
                }

                if (Utils.isAccounting() && item.getObjectId() == null && !"".equals(customerBalanceAmount.getText().trim()) && customerBalanceDate.getDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(customerBalanceDate.getDate())) {
                    Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(Property.get(Constants.CLIENT_LIST, wfmStrings.customerOpeningBalance(), wfmStrings.customer()), Utils.getTransactionLockDate()), Info.Type.WARNING);
                    return false;
                }

                //edit
                if (Utils.isAccounting() && item.getObjectId() != null) {
                    Double supplierBalanceAmountValue = null;
                    if (supplierBalanceAmount.getText() != null && !supplierBalanceAmount.getText().trim().isEmpty()) {
                        try {
                            supplierBalanceAmountValue = numberFormat.parse(supplierBalanceAmount.getText());
                        } catch (Exception e) {
                            supplierBalanceAmountValue = Double.valueOf("0.00");
                        }
                    }
                    if (supplierBalanceDate.getDate() != null && Utils.isBankingLocked() && (DateUtils.getTransactionLockDate().after(supplierBalanceDate.getDate())
                            || item.getSupplierBalanceDate() != null && DateUtils.getTransactionLockDate().after(item.getSupplierBalanceDate().getNonConvertedDate()) && DateUtils.getTransactionLockDate().before(supplierBalanceDate.getDate()))) {

                        if (item.getSupplierBalanceDate() == null || item.getSupplierBalanceDate().getNonConvertedDate() == null || !DateUtil.resetTime(item.getSupplierBalanceDate().getNonConvertedDate()).equals(DateUtil.resetTime(supplierBalanceDate.getDate())) || supplierBalanceAmountValue.compareTo(item.getSupplierBalanceAmount()) != 0) {
                            Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.supplierOpeningBalance(), Utils.getTransactionLockDate()), Info.Type.WARNING);
                            return false;
                        }
                    }
                }

                if (Utils.isAccounting() && item.getObjectId() != null) {
                    Double customerBalanceAmountValue = null;
                    if (customerBalanceAmount.getText() != null && !customerBalanceAmount.getText().trim().isEmpty()) {
                        try {
                            customerBalanceAmountValue = numberFormat.parse(customerBalanceAmount.getText());
                        } catch (Exception e) {
                            customerBalanceAmountValue = Double.valueOf("0.00");
                        }
                    }
                    if (customerBalanceDate.getDate() != null && Utils.isBankingLocked() && (DateUtils.getTransactionLockDate().after(customerBalanceDate.getDate())
                            || item.getBalanceDate() != null && DateUtils.getTransactionLockDate().after(item.getBalanceDate().getNonConvertedDate()) && DateUtils.getTransactionLockDate().before(customerBalanceDate.getDate()))) {

                        if (customerBalanceAmountValue != null && item.getBalanceAmount() != null && (item.getBalanceDate() == null || item.getBalanceDate().getNonConvertedDate() == null || !DateUtil.resetTime(item.getBalanceDate().getNonConvertedDate()).equals(DateUtil.resetTime(customerBalanceDate.getDate())) || customerBalanceAmountValue.compareTo(item.getBalanceAmount()) != 0)) {
                            Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(Property.get(Constants.CLIENT_LIST, wfmStrings.customerOpeningBalance(), wfmStrings.customer()), Utils.getTransactionLockDate()), Info.Type.WARNING);
                            return false;
                        }
                    }
                }

                if (!"".equals(customerBalanceAmount.getText().trim())) {
                    if (!Validation.validateDate(customerBalanceDate)) {
                        errors++;
                    }
                }

                if (!"".equals(supplierBalanceAmount.getText().trim())) {
                    if (!Validation.validateDate(supplierBalanceDate)) {
                        errors++;
                    }
                }
            }

            if (errors > 0) {
                Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
                return false;
            }
            return true;
        }
        return false;
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
        item.setBalanceDate(customerBalanceDate.getDate() != null ? new DateNonConvertable(customerBalanceDate.getDate()) : null);
        if (customerBalanceAmount.getText() != null && !customerBalanceAmount.getText().trim().isEmpty()) {
            try {
                item.setBalanceAmount(numberFormat.parse(customerBalanceAmount.getText()));
            } catch (Exception e) {
            }
        } else {
            item.setBalanceAmount(null);
        }

        //Supplier Balance
        item.setSupplierBalanceDate(supplierBalanceDate.getDate() != null ? new DateNonConvertable(supplierBalanceDate.getDate()) : null);
        if (supplierBalanceAmount.getText() != null && !supplierBalanceAmount.getText().trim().isEmpty()) {
            try {
                item.setSupplierBalanceAmount(numberFormat.parse(supplierBalanceAmount.getText()));
            } catch (Exception e) {
            }
        } else {
            item.setSupplierBalanceAmount(null);
        }
        if (priceLavelList.getItems() != null && priceLavelList.getItems().size() > 0) {
            List<SelectItem> appliedPriveces = new ArrayList<>();
            for (CustomListItem client : priceLavelList.getItems()) {
                if (client.getValue()) {
                    appliedPriveces.add(client.getItem());
                }
            }
            item.setAppliedPriceLavel(appliedPriveces.toArray(new SelectItem[]{}));
        }
        if (discountList.getItems() != null && discountList.getItems().size() > 0) {
            List<SelectItem> appliedDiscounts = new ArrayList<>();
            appliedDiscounts.addAll(discountList.getSelectItems());
            item.setAppliedDiscounts(appliedDiscounts.toArray(new SelectItem[]{}));
        }

        item.setSubsidiary(subsidiariesLookUp.getSelectedItem());
        item.setAccountsReceivablePayable(accountsReceivableLookUp.getSelectedData());
        item.setCreateGlAccount(createGlAccountCheckBox.getValue());
        item.setVat((TaxItem) taxLookUp.getSelectedItem());
        item.setBankAccountId(null);
        if (bankAccounts.getSelectedItem() != null) {
            item.setBankAccountId(bankAccounts.getSelectedItem().getId());
            item.setBankAccount(bankAccounts.getSelectedItem().getName());
        }
        if (clientTypes.getSelectedItem() != null) {
            item.setClientType(clientTypes.getSelectedItem());
        }
        if (warehouse.getSelectedItem() != null) {
            item.setWarehouse(new SelectItem(warehouse.getSelectedItemID(), warehouse.getStyleName()));
        }
        if (department.getSelectedItem() != null) {
            item.setDepartment(new SelectItem(department.getSelectedItemID(), department.getStyleName()));
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
        if (panel.getElementById("customerOpeningBalanceTitle") != null && panel.getElementById("supplierOpeningBalanceTitle") != null) {
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
}
