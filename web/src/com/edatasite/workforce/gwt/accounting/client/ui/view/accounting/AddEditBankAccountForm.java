package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccount;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.BalanceAsOfDateWidget;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
public class AddEditBankAccountForm extends CustomForm2 implements Constants, Colapse {
    public static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    public static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    public static final AccountingServiceAsync accountingService = AccountingService.App.get();
    protected Integer objectID,accountID;
    private TextBox bankName,accountCode;
    private TextArea bankAddress;
    private TextBox phoneNumber,accountName,accountNumber,bankBranch;
    private KpiSwitcher activeBankCheckBox;
    private TextBox ibanCode,swiftCode,sortCode,abaCode,agentID;
    private KpiSelect2 owners;
    private TextBox streetAddress,city,postCode;
    private CurrencyWidget currencyWidget;
    private BalanceAsOfDateWidget balanceAsOfDate;
    private Label openingBalanceAmountCurrencyLabel;
    private WfmDropdown country,state;
    private String accCode;
    protected GeneralFileUpload fileUpload;
    protected FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private final Map<Integer, List<SelectItem>> statesMap = new HashMap<>();
    private final String addEditBankAccountView = "add_edit_bank_account_view_";
    protected BankAccount bankAccountItem;
    public boolean isPopup = false;

    public AddEditBankAccountForm() {
        super("bankadd");
        setDescription(property.getSingular(accountingStrings.addbankaccount()));
    }

    public AddEditBankAccountForm(Integer objectID) {
        super("edit");
        setDescription(property.getSingular(accountingStrings.editBankAccount(), wfmStrings.bankAccount()));
        this.objectID = objectID;
    }

    public AddEditBankAccountForm(String viewBankAccount, String s) {
        super(viewBankAccount, s);
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.BankAccounts, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                reinit();
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                formPropertyMap = result.getFormPropertyMap();
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                }
                reinit();
            }
        });

        return null;
    }

    @Override
    protected void initPredefinedValues() {
        Map<String, SelectItem[]> countriesAndRegions = bankAccountItem.getCountryAndRegionItems();
        addPredefinedValues(COUNTRY, countriesAndRegions.get("country"));
        addPredefinedValues(STATE, countriesAndRegions.get("state"));
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.BANK_ACCOUNT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), event -> save());
    }

    protected void reinit() {
        super.onInitialize();
    }

    @Override
    protected void registerFields() {
        bankName = new TextBox(true);
        bankName.addStyleName(DEFAULT_WIDTH);
        bankName.ensureDebugId(addEditBankAccountView + "bankName");
        bankName.setMaxLength(256);

        accountNumber = new TextBox(true);
        accountNumber.addStyleName(DEFAULT_WIDTH);
        accountNumber.ensureDebugId(addEditBankAccountView + "accountNumber");

        owners = new KpiSelect2(true);
        owners.addStyleName(DEFAULT_WIDTH);

        activeBankCheckBox = new KpiSwitcher();
        activeBankCheckBox.ensureDebugId("active_switcher");

        accountCode = new TextBox(true);
        accountCode.addStyleName(DEFAULT_WIDTH);
        accountCode.ensureDebugId(addEditBankAccountView + "code");
        accountCode.setMaxLength(20);

        accountName = new TextBox(true);
        accountName.addStyleName(DEFAULT_WIDTH);
        accountName.ensureDebugId(addEditBankAccountView + "accountName");

        bankBranch = new TextBox(true);
        bankBranch.addStyleName(DEFAULT_WIDTH);
        bankBranch.ensureDebugId(addEditBankAccountView + "bankBranch");

        streetAddress = new TextBox(true);
        streetAddress.addStyleName(DEFAULT_WIDTH);
        streetAddress.ensureDebugId(addEditBankAccountView + "streetAddress");

        city = new TextBox(true);
        city.addStyleName(DEFAULT_WIDTH);
        city.ensureDebugId(addEditBankAccountView + "city");

        country = new WfmDropdown();
        country.addStyleName(DEFAULT_WIDTH);
        country.ensureDebugId(addEditBankAccountView + "country");
        country.addValueChangeHandler(sender -> {
            if (isStateEnabled(country)) {
                state.setItems(asList(statesMap.get(country.getSelectedId()).toArray(new SelectItem[]{})));
                state.setEnabled(true);
            } else {
                state.setEnabled(false);
                state.clear();
            }
        });

        state = new WfmDropdown();
        state.addStyleName(DEFAULT_WIDTH);
        state.setEnabled(false);
        state.ensureDebugId(addEditBankAccountView + "state");

        postCode = new TextBox(true);
        postCode.addStyleName(DEFAULT_WIDTH);
        postCode.ensureDebugId(addEditBankAccountView + "postCode");

        phoneNumber = new TextBox(true);
        phoneNumber.addStyleName(DEFAULT_WIDTH);
        phoneNumber.ensureDebugId(addEditBankAccountView + "phoneNumber");

        swiftCode = new TextBox(true);
        swiftCode.addStyleName(DEFAULT_WIDTH);
        swiftCode.ensureDebugId(addEditBankAccountView + "swiftCode");

        sortCode = new TextBox(true);
        sortCode.addStyleName(DEFAULT_WIDTH);
        sortCode.ensureDebugId(addEditBankAccountView + "sortCode");

        ibanCode = new TextBox(true);
        ibanCode.addStyleName(DEFAULT_WIDTH);
        ibanCode.ensureDebugId(addEditBankAccountView + "ibanCode");

        abaCode = new TextBox(true);
        abaCode.addStyleName(DEFAULT_WIDTH);
        abaCode.ensureDebugId(addEditBankAccountView + "abaCode");

        agentID = new TextBox(true);
        agentID.addStyleName(DEFAULT_WIDTH);
        agentID.ensureDebugId(addEditBankAccountView + "agentID");

        bankAddress = new TextArea();
        bankAddress.addStyleName(DEFAULT_WIDTH);
        bankAddress.ensureDebugId(addEditBankAccountView + "bankAddress");

        currencyWidget = new CurrencyWidget(objectID == null);

        currencyWidget.addListener(() -> onCurrencyChange());
        balanceAsOfDate = new BalanceAsOfDateWidget();
        balanceAsOfDate.addStyleName(DEFAULT_WIDTH);
        balanceAsOfDate.ensureDebugId(addEditBankAccountView + "openingBalanceDate");

        currencyWidget.setDatePicker(balanceAsOfDate.getDateField());

        openingBalanceAmountCurrencyLabel = new Label();

        Validation.addNumericKeyboardListener(balanceAsOfDate.getBalanceField(), AccountingUtils.calculationScale, true);
        balanceAsOfDate.getBalanceField().ensureDebugId(addEditBankAccountView + "openingBalanceAmount");

        fileUpload = new GeneralFileUpload(F_BANK_ACCOUNT, objectID, objectID);
        fileUpload.ensureDebugId(addEditBankAccountView + "attachments");

        owners.addStyleName(DEFAULT_WIDTH);

        accountCode.addChangeHandler(changeEvent -> {
            if (!"".equals(accountCode.getText())) {
                if (objectID == null || (!accountCode.getText().equals(accCode))) {
                    accountingService.getAccountCodeUnique(accountCode.getText().trim(), objectID, new AsyncCallback<AccountItem>() {
                        public void onFailure(Throwable throwable) {
                        }

                        public void onSuccess(AccountItem account) {
                            if (account != null && account.getCode() != null) {
                                Validation.validateTextBoxRequired(accountCode);
                                accountCode.setText("");
                                Info.show(wfmStrings.accountName() + " " + account.getName() + " " + wfmStrings.code() + " " + "<font color='#141414'>" + account.getCode() + "</font> " + " " + accountingStrings.validateUserCredentialsMessagePart3(), Info.Type.WARNING);
                            }
                        }
                    });
                }
            }
        });
        // section - accountInformation
        addTitleField(ACCOUNT_INFORMATION, wfmStrings.basicInfo());
        if (formPropertyMap != null && formPropertyMap.get(BANK_NAME) != null) {
            addField(BANK_NAME, bankName, getTitle(formPropertyMap.get(BANK_NAME).isChanged() ? formPropertyMap.get(BANK_NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(BANK_NAME).isRequired()));
            bankName.setEnabled(!formPropertyMap.get(BANK_NAME).isDisabled());
        } else {
            addField(BANK_NAME, bankName, getTitle(wfmStrings.name(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_NUMBER) != null) {
            addField(ACCOUNT_NUMBER, accountNumber, getTitle(formPropertyMap.get(ACCOUNT_NUMBER).isChanged() ? formPropertyMap.get(ACCOUNT_NUMBER).getTitle() : wfmStrings.accountNumber(), formPropertyMap.get(ACCOUNT_NUMBER).isRequired()));
            accountNumber.setEnabled(!formPropertyMap.get(ACCOUNT_NUMBER).isDisabled());
        } else {
            addField(ACCOUNT_NUMBER, accountNumber, getTitle(wfmStrings.accountNumber(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(OWNER) != null) {
            addField(OWNER, owners, getTitle(formPropertyMap.get(OWNER).isChanged() ? formPropertyMap.get(OWNER).getTitle() : wfmStrings.owners(), formPropertyMap.get(OWNER).isRequired()));
            owners.setEnabled(!formPropertyMap.get(OWNER).isDisabled());
        } else {
            addField(OWNER, owners, getTitle(wfmStrings.owners()));
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_CODE) != null) {
            addField(ACCOUNT_CODE, accountCode, getTitle(formPropertyMap.get(ACCOUNT_CODE).isChanged() ? formPropertyMap.get(ACCOUNT_CODE).getTitle() : wfmStrings.accountCode(), formPropertyMap.get(ACCOUNT_CODE).isRequired()));
            accountCode.setEnabled(!formPropertyMap.get(ACCOUNT_CODE).isDisabled());
        } else {
            addField(ACCOUNT_CODE, accountCode, getTitle(wfmStrings.accountCode(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_NAME) != null) {
            addField(ACCOUNT_NAME, accountName, getTitle(formPropertyMap.get(ACCOUNT_NAME).isChanged() ? formPropertyMap.get(ACCOUNT_NAME).getTitle() : wfmStrings.accountName(), formPropertyMap.get(ACCOUNT_NAME).isRequired()));
            accountName.setEnabled(!formPropertyMap.get(ACCOUNT_NAME).isDisabled());
        } else {
            addField(ACCOUNT_NAME, accountName, getTitle(wfmStrings.accountName()));
        }

        if (formPropertyMap != null && formPropertyMap.get(BANK_BRANCH) != null) {
            addField(BANK_BRANCH, bankBranch, getTitle(formPropertyMap.get(BANK_BRANCH).isChanged() ? formPropertyMap.get(BANK_BRANCH).getTitle() : wfmStrings.bankBranch(), formPropertyMap.get(BANK_BRANCH).isRequired()));
            bankBranch.setEnabled(!formPropertyMap.get(BANK_BRANCH).isDisabled());
        } else {
            addField(BANK_BRANCH, bankBranch, getTitle(wfmStrings.bankBranch()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACTIVE) != null) {
            addField(CustomFormConstants.ACTIVE, activeBankCheckBox, getTitle(formPropertyMap.get(CustomFormConstants.ACTIVE).isChanged() ? formPropertyMap.get(CustomFormConstants.ACTIVE).getTitle() : wfmStrings.active()));
            activeBankCheckBox.setEnabled(!formPropertyMap.get(CustomFormConstants.ACTIVE).isDisabled());
        } else {
            addField(CustomFormConstants.ACTIVE, activeBankCheckBox, null);
        }
        //section - addressInformation
        addTitleField(ADDRESS_INFORMATION, wfmStrings.addressInformation());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STREET_ADDRESS) != null) {
            addField(CustomFormConstants.STREET_ADDRESS, streetAddress, getTitle(formPropertyMap.get(CustomFormConstants.STREET_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.STREET_ADDRESS).getTitle() : wfmStrings.streetAddress(), formPropertyMap.get(CustomFormConstants.STREET_ADDRESS).isRequired()));
            streetAddress.setEnabled(!formPropertyMap.get(CustomFormConstants.STREET_ADDRESS).isDisabled());
        } else {
            addField(STREET_ADDRESS, streetAddress, getTitle(wfmStrings.streetAddress()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CITY) != null) {
            addField(CustomFormConstants.CITY, city, getTitle(formPropertyMap.get(CustomFormConstants.CITY).isChanged() ? formPropertyMap.get(CustomFormConstants.CITY).getTitle() : wfmStrings.city(), formPropertyMap.get(CustomFormConstants.CITY).isRequired()));
            city.setEnabled(!formPropertyMap.get(CustomFormConstants.CITY).isDisabled());
        } else {
            addField(CITY, city, getTitle(wfmStrings.city()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.COUNTRY) != null) {
            addField(CustomFormConstants.COUNTRY, country, getTitle(formPropertyMap.get(CustomFormConstants.COUNTRY).isChanged() ? formPropertyMap.get(CustomFormConstants.COUNTRY).getTitle() : wfmStrings.country(), formPropertyMap.get(CustomFormConstants.COUNTRY).isRequired()));
            country.setEnabled(!formPropertyMap.get(CustomFormConstants.COUNTRY).isDisabled());
        } else {
            addField(COUNTRY, country, getTitle(wfmStrings.country()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATE) != null) {
            addField(CustomFormConstants.STATE, state, getTitle(formPropertyMap.get(CustomFormConstants.STATE).isChanged() ? formPropertyMap.get(CustomFormConstants.STATE).getTitle() : wfmStrings.state(), formPropertyMap.get(CustomFormConstants.STATE).isRequired()));
            state.setEnabled(!formPropertyMap.get(CustomFormConstants.STATE).isDisabled());
        } else {
            addField(STATE, state, getTitle(wfmStrings.state()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.POST_CODE) != null) {
            addField(CustomFormConstants.POST_CODE, postCode, getTitle(formPropertyMap.get(CustomFormConstants.POST_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.POST_CODE).getTitle() : wfmStrings.postCode(), formPropertyMap.get(CustomFormConstants.POST_CODE).isRequired()));
            postCode.setEnabled(!formPropertyMap.get(CustomFormConstants.POST_CODE).isDisabled());
        } else {
            addField(POST_CODE, postCode, getTitle(wfmStrings.postCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PHONE_NUMBER) != null) {
            addField(CustomFormConstants.PHONE_NUMBER, phoneNumber, getTitle(formPropertyMap.get(CustomFormConstants.PHONE_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.PHONE_NUMBER).getTitle() : wfmStrings.phone(), formPropertyMap.get(CustomFormConstants.PHONE_NUMBER).isRequired()));
            phoneNumber.setEnabled(!formPropertyMap.get(CustomFormConstants.PHONE_NUMBER).isDisabled());
        } else {
            addField(PHONE_NUMBER, phoneNumber, getTitle(wfmStrings.phone()));
        }

        //section -  additionalInformation
        addTitleField("CODES", wfmStrings.code());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SWIFT_CODE) != null) {
            addField(CustomFormConstants.SWIFT_CODE, swiftCode, getTitle(formPropertyMap.get(CustomFormConstants.SWIFT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SWIFT_CODE).getTitle() : wfmStrings.swiftCode(), formPropertyMap.get(CustomFormConstants.SWIFT_CODE).isRequired()));
            swiftCode.setEnabled(!formPropertyMap.get(CustomFormConstants.SWIFT_CODE).isDisabled());
        } else {
            addField(SWIFT_CODE, swiftCode, getTitle(wfmStrings.swiftCode()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SORT_CODE) != null) {
            addField(CustomFormConstants.SORT_CODE, sortCode, getTitle(formPropertyMap.get(CustomFormConstants.SORT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SORT_CODE).getTitle() : wfmStrings.sortCode(), formPropertyMap.get(CustomFormConstants.SORT_CODE).isRequired()));
            sortCode.setEnabled(!formPropertyMap.get(CustomFormConstants.SORT_CODE).isDisabled());
        } else {
            addField(SORT_CODE, sortCode, getTitle(wfmStrings.sortCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.IBAN_CODE) != null) {
            addField(CustomFormConstants.IBAN_CODE, ibanCode, getTitle(formPropertyMap.get(CustomFormConstants.IBAN_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.IBAN_CODE).getTitle() : wfmStrings.ibanCode(), formPropertyMap.get(CustomFormConstants.IBAN_CODE).isRequired()));
            ibanCode.setEnabled(!formPropertyMap.get(CustomFormConstants.IBAN_CODE).isDisabled());
        } else {
            addField(IBAN_CODE, ibanCode, getTitle(wfmStrings.ibanCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ABA_CODE) != null) {
            addField(CustomFormConstants.ABA_CODE, abaCode, getTitle(formPropertyMap.get(CustomFormConstants.ABA_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.ABA_CODE).getTitle() : wfmStrings.abaCode(), formPropertyMap.get(CustomFormConstants.ABA_CODE).isRequired()));
            abaCode.setEnabled(!formPropertyMap.get(CustomFormConstants.ABA_CODE).isDisabled());
        } else {
            addField(ABA_CODE, abaCode, getTitle(wfmStrings.abaCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.AGENT_ID) != null) {
            addField(CustomFormConstants.AGENT_ID, agentID, getTitle(formPropertyMap.get(CustomFormConstants.AGENT_ID).isChanged() ? formPropertyMap.get(CustomFormConstants.AGENT_ID).getTitle() : wfmStrings.agentID(), formPropertyMap.get(CustomFormConstants.AGENT_ID).isRequired()));
            agentID.setEnabled(!formPropertyMap.get(CustomFormConstants.AGENT_ID).isDisabled());
        } else {
            addField(AGENT_ID, agentID, getTitle(wfmStrings.agentID()));
        }

        //section - financialInformation
        addTitleField(FINANCIAL_INFORMATION, wfmStrings.financialInformation());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CURRENCY) != null) {
            addField(CustomFormConstants.CURRENCY, currencyWidget, getTitle(formPropertyMap.get(CustomFormConstants.CURRENCY).isChanged() ? formPropertyMap.get(CustomFormConstants.CURRENCY).getTitle() : wfmStrings.currency(), formPropertyMap.get(CustomFormConstants.CURRENCY).isRequired()));
            currencyWidget.setEnabled(!formPropertyMap.get(CustomFormConstants.CURRENCY).isDisabled());
        } else {
            addField(CURRENCY, currencyWidget, getTitle(wfmStrings.currency()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.OPENING_BALANCE_DATE) != null) {
            addField(CustomFormConstants.OPENING_BALANCE_DATE, balanceAsOfDate, getTitle(formPropertyMap.get(CustomFormConstants.OPENING_BALANCE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.OPENING_BALANCE_DATE).getTitle() : wfmStrings.openingBalanceAsOfDate(), false));
            balanceAsOfDate.setEnabled(!formPropertyMap.get(CustomFormConstants.OPENING_BALANCE_DATE).isDisabled());
        } else {
            addField(OPENING_BALANCE_DATE, balanceAsOfDate, getTitle(wfmStrings.openingBalanceAsOfDate()));
        }

        //section - Attachments
        addTitleField(ATTACHMENTS, wfmStrings.attachments());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ATTACHMENTS) != null) {
            addField(CustomFormConstants.ATTACHMENTS, fileUpload, getTitle(formPropertyMap.get(CustomFormConstants.ATTACHMENTS).isChanged() ? formPropertyMap.get(CustomFormConstants.ATTACHMENTS).getTitle() : wfmStrings.attachments(), formPropertyMap.get(CustomFormConstants.ATTACHMENTS).isRequired()));
        } else {
            addField(ATTACHMENTS, fileUpload, getTitle(wfmStrings.attachments(), false), true);
        }

        //section Custom Field
        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID);

        show();

    }

    private void save() {
        enableButtons(false);
        if (!validate()) {
            enableButtons(true);
            return;
        }
        if (objectID == null || (!accountCode.getText().equals(accCode))) {
            accountingService.getAccountCodeUnique(accountCode.getText().trim(), objectID, new AsyncCallback<AccountItem>() {
                public void onFailure(Throwable caught) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    enableButtons(true);
                }

                public void onSuccess(AccountItem accItem) {
                    if (accItem == null) {
                        saveBankAccount();
                    } else {
                        Info.show(accountingStrings.accountWithThisCodeAlreadyExists(), Info.Type.WARNING);
                        enableButtons(true);
                    }
                }
            });
        } else {
            saveBankAccount();
        }
    }

    protected void saveBankAccount() {
        BankAccount bankAccount = getBankAccountData();
        LoadingPanel.loading(true);
        accountingService.createBankAccount(bankAccount, new AsyncCallback<Void>() {
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                enableButtons(true);
            }

            public void onSuccess(Void o) {
                LoadingPanel.loading(false);
                enableButtons(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.bankAccount()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANKACCOUNT_SAVED, null, AddEditBankAccountForm.this);
                closeTab("accounting|bankaccount");
            }
        });
    }

    private void enableButtons(boolean enable) {
        if (widgets.size() > 0) {
            enableButton(enable);
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        accountingService.getBankAccountForEdit(objectID, new AbstractAsyncCallback<BankAccount>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(final BankAccount bankAccount) {
                Scheduler.get().scheduleDeferred(() -> {
                    bankAccountItem = bankAccount;
                    fillFormWithData();
                    initPredefinedValues();
                    if (objectID == null) {
                        setDefaultValues();
                    }
                    LoadingPanel.loading(false);
                });
            }
        });

    }

    protected void fillFormWithData() {
        Map<String, SelectItem[]> countriesAndRegions = bankAccountItem.getCountryAndRegionItems();
        country.setItems(asList(countriesAndRegions.get("country")));
        initStatesMap(countriesAndRegions.get("state"));

        currencyWidget.setCurrency((bankAccountItem.getCurrency() != null ? bankAccountItem.getCurrency().getId() : null), bankAccountItem.getExchangeRate());
        onCurrencyChange();

        accountID = bankAccountItem.getAccountId();
        accCode = bankAccountItem.getCode();
        accountCode.setText(bankAccountItem.getCode());
        bankName.setText(bankAccountItem.getName());
        accountNumber.setText(bankAccountItem.getAccountNumber());
        owners.setItems(new ArrayList<>(Arrays.asList(bankAccountItem.getOwnerItems())));
        if (bankAccountItem.getBankBranch() != null) {
            bankBranch.setText(bankAccountItem.getBankBranch());
        }
        if (bankAccountItem.getBankAddress() != null) {
            bankAddress.setText(bankAccountItem.getBankAddress());
        }
        if (bankAccountItem.getAccauntName() != null) {
            accountName.setText(bankAccountItem.getAccauntName());
        }
        if (bankAccountItem.getSwiftCode() != null) {
            swiftCode.setText(bankAccountItem.getSwiftCode());
        }
        if (bankAccountItem.getIbanCode() != null) {
            ibanCode.setText(bankAccountItem.getIbanCode());
        }
        if (bankAccountItem.getSortCode() != null) {
            sortCode.setText(bankAccountItem.getSortCode());
        }
        if (bankAccountItem.getAbaCode() != null) {
            abaCode.setText(bankAccountItem.getAbaCode());
        }
        if (bankAccountItem.getAgentID() != null) {
            agentID.setText(bankAccountItem.getAgentID());
        }
        if (bankAccountItem.getStreetAddress() != null) {
            streetAddress.setText(bankAccountItem.getStreetAddress());
        }
        if (bankAccountItem.getCity() != null) {
            city.setText(bankAccountItem.getCity());
        }

        setCountryAndRegion(bankAccountItem.getCountryId(), bankAccountItem.getStateId());

        if (bankAccountItem.getPostCode() != null) {
            postCode.setText(bankAccountItem.getPostCode());
        }
        if (bankAccountItem.getPhoneNumber() != null) {
            phoneNumber.setText(bankAccountItem.getPhoneNumber());
        }

        if (bankAccountItem.getOpeningDate() != null) {
            balanceAsOfDate.setDate(bankAccountItem.getOpeningDate().getNonConvertedDate());
        }
        if (bankAccountItem.getOpeningAmount() != null) {
            balanceAsOfDate.setText(AccountingUtils.get().formatPrice(bankAccountItem.getOpeningAmount()));
        }

        activeBankCheckBox.setValue(bankAccountItem.isActive());

        getCustomFieldUtil().fillCustomFieldsWithData(bankAccountItem.getCustomFieldItems());
        if (objectID == null) {
            setDefaultValuesByFormProperty();
        }
    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(BANK_NAME) != null && formPropertyMap.get(BANK_NAME).getDefaultValue() != null) {
            bankName.setText(formPropertyMap.get(BANK_NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_NUMBER) != null && formPropertyMap.get(ACCOUNT_NUMBER).getDefaultValue() != null) {
            accountNumber.setText(formPropertyMap.get(ACCOUNT_NUMBER).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(OWNER) != null && formPropertyMap.get(OWNER).getDefaultValue() != null && owners != null) {
            List<SelectItem> items = new ArrayList<>();
            items.add(new SelectItem(formPropertyMap.get(OWNER).getSelectedId(), formPropertyMap.get(OWNER).getDefaultValue()));
            owners.setSelectedItems(items);
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_CODE) != null && formPropertyMap.get(ACCOUNT_CODE).getDefaultValue() != null) {
            accountCode.setText(formPropertyMap.get(ACCOUNT_CODE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_NAME) != null && formPropertyMap.get(ACCOUNT_NAME).getDefaultValue() != null) {
            accountName.setText(formPropertyMap.get(ACCOUNT_NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(BANK_BRANCH) != null && formPropertyMap.get(BANK_BRANCH).getDefaultValue() != null) {
            bankBranch.setText(formPropertyMap.get(BANK_BRANCH).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(STREET_ADDRESS) != null && formPropertyMap.get(STREET_ADDRESS).getDefaultValue() != null) {
            streetAddress.setText(formPropertyMap.get(STREET_ADDRESS).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CITY) != null && formPropertyMap.get(CITY).getDefaultValue() != null) {
            city.setText(formPropertyMap.get(CITY).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(COUNTRY) != null && formPropertyMap.get(COUNTRY).getDefaultValue() != null) {
            country.setSingleValue(new SelectItem(formPropertyMap.get(COUNTRY).getSelectedId(), formPropertyMap.get(COUNTRY).getDefaultValue()));
            if (isStateEnabled(country)) {
                state.setItems(asList(statesMap.get(country.getSelectedId()).toArray(new SelectItem[]{})));
                state.setEnabled(true);
            } else {
                state.setEnabled(false);
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(POST_CODE) != null && formPropertyMap.get(POST_CODE).getDefaultValue() != null) {
            postCode.setText(formPropertyMap.get(POST_CODE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(PHONE_NUMBER) != null && formPropertyMap.get(PHONE_NUMBER).getDefaultValue() != null) {
            phoneNumber.setText(formPropertyMap.get(PHONE_NUMBER).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(SWIFT_CODE) != null && formPropertyMap.get(SWIFT_CODE).getDefaultValue() != null) {
            swiftCode.setText(formPropertyMap.get(SWIFT_CODE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(SORT_CODE) != null && formPropertyMap.get(SORT_CODE).getDefaultValue() != null) {
            sortCode.setText(formPropertyMap.get(SORT_CODE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(IBAN_CODE) != null && formPropertyMap.get(IBAN_CODE).getDefaultValue() != null) {
            ibanCode.setText(formPropertyMap.get(IBAN_CODE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(ABA_CODE) != null && formPropertyMap.get(ABA_CODE).getDefaultValue() != null) {
            abaCode.setText(formPropertyMap.get(ABA_CODE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(AGENT_ID) != null && formPropertyMap.get(AGENT_ID).getDefaultValue() != null) {
            agentID.setText(formPropertyMap.get(AGENT_ID).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CURRENCY) != null && formPropertyMap.get(CURRENCY).getDefaultValue() != null) {
            currencyWidget.setCurrency(new CurrencyItem(formPropertyMap.get(CURRENCY).getSelectedId(), formPropertyMap.get(CURRENCY).getDefaultValue()));
        }
    }

    private void onCurrencyChange() {
        openingBalanceAmountCurrencyLabel.setText(currencyWidget.getCurrencyName());
    }

    private void setCountryAndRegion(Integer countryID, Integer stateID) {
        if (countryID != null) {
            country.setSelected(countryID);
        }
        if (isStateEnabled(country)) {
            state.setEnabled(true);
            state.setItems(asList(statesMap.get(countryID).toArray(new SelectItem[]{})));
            if (stateID != null) {
                state.setSelected(stateID);
            }
        } else {
            state.setEnabled(false);
        }
    }

    private void initStatesMap(SelectItem[] result) {
        for (SelectItem aResult : result) {
            if (aResult.getDescription() != null && !"".equals(aResult.getDescription())) {
                Integer countryID = null;
                try {
                    countryID = Integer.parseInt(aResult.getDescription());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (statesMap.containsKey(countryID)) {
                    statesMap.get(countryID).add(aResult);
                } else {
                    List<SelectItem> statesList = new LinkedList<>();
                    statesList.add(aResult);
                    statesMap.put(countryID, statesList);
                }
            }
        }
    }

    public BankAccount getBankAccountData() {
        bankAccountItem.setObjectId(objectID);
        bankAccountItem.setAccountId(accountID);
        bankAccountItem.setCode(accountCode.getText());
        bankAccountItem.setName(bankName.getText());
        bankAccountItem.setAccountNumber(accountNumber.getText());
        bankAccountItem.setSelectedOwners(owners.getSelectedItems());
        bankAccountItem.setAccauntName(accountName.getText());
        bankAccountItem.setBankBranch(bankBranch.getText());
        bankAccountItem.setBankAddress(bankAddress.getText());
        bankAccountItem.setSwiftCode(swiftCode.getText());
        bankAccountItem.setIbanCode(ibanCode.getText());
        bankAccountItem.setSortCode(sortCode.getText());
        bankAccountItem.setAbaCode(abaCode.getText());
        bankAccountItem.setAgentID(agentID.getText());
        bankAccountItem.setStreetAddress(streetAddress.getText());
        bankAccountItem.setCity(city.getText());
        if (country.getSelectedItem() != null) {
            bankAccountItem.setCountryId(country.getSelectedItem().getId());
        }
        if (state.getSelectedItem() != null) {
            bankAccountItem.setStateId(state.getSelectedItem().getId());
        }
        bankAccountItem.setPostCode(postCode.getText());
        bankAccountItem.setPhoneNumber(phoneNumber.getText());

        CurrencyItem currencyItem = currencyWidget.getCurrency();
        if (currencyItem != null && currencyItem.getId() != null) {
            bankAccountItem.setCurrency(new CurrencyItem(currencyItem.getId(), currencyItem.getName(), null));
            bankAccountItem.setExchangeRate(currencyWidget.getExchangeRate());
        }

        bankAccountItem.setOpeningDate(balanceAsOfDate.getDate() != null ? new DateNonConvertable(balanceAsOfDate.getDate()) : null);
        bankAccountItem.setOpeningAmount(AccountingUtils.get().parseToBigDecimal(balanceAsOfDate.getText()));
        bankAccountItem.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        bankAccountItem.setAttachments(fileUpload.getAttachedFiles());
        bankAccountItem.setActive(activeBankCheckBox.getValue());

        return bankAccountItem;
    }

    public boolean validate() {
        int errors = 0;
        int invalidInput = 0;

        if (formPropertyMap != null && formPropertyMap.get(BANK_NAME) != null && formPropertyMap.get(BANK_NAME).isRequired()) {
            errors += markAsError(bankName, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(BANK_NAME).isChanged()
                    ? formPropertyMap.get(BANK_NAME).getTitle() : wfmStrings.bankName(), bankName, formPropertyMap.get(BANK_NAME).getMinChar()));
        }

        if (!Validation.validateUserInputWithCyrilic(bankName)) {
            invalidInput++;
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_NUMBER) != null && formPropertyMap.get(ACCOUNT_NUMBER).isRequired()) {
            errors += markAsError(accountNumber, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(ACCOUNT_NUMBER).isChanged()
                    ? formPropertyMap.get(ACCOUNT_NUMBER).getTitle() : wfmStrings.accountNumber(), accountNumber, formPropertyMap.get(ACCOUNT_NUMBER).getMinChar()));
        }

        if (!Validation.validateUserInput(accountNumber)) {
            invalidInput++;
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_CODE) != null && formPropertyMap.get(ACCOUNT_CODE).isRequired()) {
            errors += markAsError(accountCode, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(ACCOUNT_CODE).isChanged() ?
                    formPropertyMap.get(ACCOUNT_CODE).getTitle() : wfmStrings.accountCode(), accountCode, formPropertyMap.get(ACCOUNT_CODE).getMinChar()));
        }

        if (!Validation.validateUserInput(accountCode)) {
            invalidInput++;
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.OWNER) != null && formPropertyMap.get(CustomFormConstants.OWNER).isRequired() && owners != null) {
            errors += markAsError(owners, owners.getSelectedItems() == null || owners.getSelectedItems() != null && owners.getSelectedItems().size() == 0);
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_NAME) != null && formPropertyMap.get(ACCOUNT_NAME).isRequired()) {
            errors += markAsError(accountName, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(ACCOUNT_NAME).isChanged() ?
                    formPropertyMap.get(ACCOUNT_NAME).getTitle() : wfmStrings.accountName(), accountName, formPropertyMap.get(ACCOUNT_NAME).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(BANK_BRANCH) != null && formPropertyMap.get(BANK_BRANCH).isRequired()) {
            errors += markAsError(bankBranch, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(BANK_BRANCH).isChanged()
                    ? formPropertyMap.get(BANK_BRANCH).getTitle() : wfmStrings.bankBranch(), bankBranch, formPropertyMap.get(BANK_BRANCH).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STREET_ADDRESS) != null && formPropertyMap.get(CustomFormConstants.STREET_ADDRESS).isRequired()) {
            errors += markAsError(streetAddress, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.STREET_ADDRESS).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.STREET_ADDRESS).getTitle() : wfmStrings.streetAddress(), streetAddress, formPropertyMap.get(CustomFormConstants.STREET_ADDRESS).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CITY) != null && formPropertyMap.get(CustomFormConstants.CITY).isRequired()) {
            errors += markAsError(city, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.CITY).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.CITY).getTitle() : wfmStrings.city(), city, formPropertyMap.get(CustomFormConstants.CITY).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.COUNTRY) != null && formPropertyMap.get(CustomFormConstants.COUNTRY).isRequired()) {
            errors += markAsError(country, !Validation.validateWfmDropdown(country));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATE) != null && formPropertyMap.get(CustomFormConstants.STATE).isRequired()) {
            errors += markAsError(state, !Validation.validateWfmDropdown(state));
        }

        if (formPropertyMap != null && formPropertyMap.get(POST_CODE) != null && formPropertyMap.get(POST_CODE).isRequired()) {
            errors += markAsError(postCode, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(POST_CODE).isChanged() ?
                    formPropertyMap.get(POST_CODE).getTitle() : wfmStrings.postCode(), postCode, formPropertyMap.get(POST_CODE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(PHONE_NUMBER) != null && formPropertyMap.get(PHONE_NUMBER).isRequired()) {
            errors += markAsError(phoneNumber, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(PHONE_NUMBER).isChanged() ?
                    formPropertyMap.get(PHONE_NUMBER).getTitle() : wfmStrings.phone(), phoneNumber, formPropertyMap.get(PHONE_NUMBER).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(SWIFT_CODE) != null && formPropertyMap.get(SWIFT_CODE).isRequired()) {
            errors += markAsError(swiftCode, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(SWIFT_CODE).isChanged() ?
                    formPropertyMap.get(SWIFT_CODE).getTitle() : wfmStrings.swiftCode(), swiftCode, formPropertyMap.get(SWIFT_CODE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(SORT_CODE) != null && formPropertyMap.get(SORT_CODE).isRequired()) {
            errors += markAsError(sortCode, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(SORT_CODE).isChanged() ?
                    formPropertyMap.get(SORT_CODE).getTitle() : wfmStrings.sortCode(), sortCode, formPropertyMap.get(SORT_CODE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(IBAN_CODE) != null && formPropertyMap.get(IBAN_CODE).isRequired()) {
            errors += markAsError(ibanCode, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(IBAN_CODE).isChanged() ?
                    formPropertyMap.get(IBAN_CODE).getTitle() : wfmStrings.ibanCode(), ibanCode, formPropertyMap.get(IBAN_CODE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(ABA_CODE) != null && formPropertyMap.get(ABA_CODE).isRequired()) {
            errors += markAsError(abaCode, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(ABA_CODE).isChanged() ?
                    formPropertyMap.get(ABA_CODE).getTitle() : wfmStrings.abaCode(), abaCode, formPropertyMap.get(ABA_CODE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(AGENT_ID) != null && formPropertyMap.get(AGENT_ID).isRequired()) {
            errors += markAsError(agentID, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(AGENT_ID).isChanged() ?
                    formPropertyMap.get(AGENT_ID).getTitle() : wfmStrings.agentID(), agentID, formPropertyMap.get(AGENT_ID).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CURRENCY) != null && formPropertyMap.get(CURRENCY).isRequired()) {
            errors += markAsError(currencyWidget, !Validation.validateListBoxRequired(currencyWidget.getCurrencyListBox()));
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED)) {
            BigDecimal exRateAmount = currencyWidget.getExchangeRate();
            if (exRateAmount == null || exRateAmount.setScale(AccountingUtils.customExRateScale, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) == 0) {
                Info.show("Exchange rate shouldn't be zero", Info.Type.WARNING);
                errors++;
            }
        }
        if (balanceAsOfDate.getText().length() > 0 && !"0.00".equals(balanceAsOfDate.getText())) {
            if (balanceAsOfDate.getDateField() != null && !Validation.validateDate(balanceAsOfDate.getDateField(), new HTML(), false)) {
                errors++;
            }
        }
        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        if (balanceAsOfDate.getDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(balanceAsOfDate.getDate()) && balanceAsOfDate.isDateFieldEnabled()) {
            Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate("Bank Opening balance", Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }

        if (invalidInput > 0) {
            Info.show(wfmStrings.invalidUserInput(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private boolean isStateEnabled(WfmDropdown c) {
        return (c.getSelectedId() != null && statesMap.get(c.getSelectedId()) != null && statesMap.get(c.getSelectedId()).size() > 0);
    }

    public String getIconStyle() {
        return null;
    }

    @Override
    public String getPropertyCode() {
        return BANKACCOUNT;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    public void asyncOnInitialize() {
        asyncOnInitialize(new AbstractAsyncCallback<Widget>() {
            public void failure(Throwable reason) {
                Info.show(wfmStrings.failedToDownloadCodeForThisWidget() + " (" + reason + ")", Info.Type.WARNING);
            }

            public void success(Widget result) {
            }
        });
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