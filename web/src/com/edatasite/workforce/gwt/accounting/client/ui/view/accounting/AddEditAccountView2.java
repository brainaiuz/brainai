package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountTypesByCategory;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AddAccountData;
import com.edatasite.workforce.gwt.accounting.client.rpc.AddAccountItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextArea;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.listener.DropdownListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.util.LinkedHashMap;

public class AddEditAccountView2 extends CustomForm2 implements Constants, Colapse, FittedContent {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final String addAccountView = "add_account_view_";
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private Integer objectID;
    private boolean saveAndClose = false;
    private WfmDropdown accountType;
    private AccountsLookUp parentAccount;
    private TextBox code;
    private TextBox name;
    private KpiTextArea description;
    private DataListBox currencyListBox;
    private DatePicker openingBalanceDate;
    private TextBox openingBalanceAmount;
    private KpiSwitcher showInExpense, showInLookUp, enablePayments, defaultAccount,active;
    private Div defaultAccountField;
    private Div showInLookUpField;
    private FormGroup openingBalanceDateField;
    private FormGroup openingBalanceAmountField;
    private KpiModal shell;
    private boolean isPopup = false;
    private Command provider;
    private ObjectCommand command;
    private String fromView;
    private Integer baseCurrencyID;
    private Integer systemAccountKey;
    private FormHasCustomField customFieldUtil;


    public AddEditAccountView2() {
        super("accountadd", wfmStrings.addAccount());
    }

    public AddEditAccountView2(Integer objectID) {
        super("edit", wfmStrings.edit());
        this.objectID = objectID;
    }

    public AddEditAccountView2(Command provider, String fromView) {
        super("accountadd", wfmStrings.addAccount());
        this.provider = provider;
        this.isPopup = true;
        this.fromView = fromView;
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

    public AddEditAccountView2(ObjectCommand command, String fromView) {
        super("accountadd", wfmStrings.addAccount());
        this.command = command;
        this.isPopup = true;
        this.fromView = fromView;
        asyncOnInitialize();
    }

    protected Widget onInitialize() {
        if (isPopup) {
            shell = new KpiModal();
        }
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Account, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                AddEditAccountView2.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private void onAccountTypeSelected() {
        if (accountType.getSelectedId() != null) {
            LoadingPanel.loading(true);
            AccountingService.App.get().getGeneratedAccountNumber(accountType.getSelectedId(), new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    throwable.printStackTrace();
                }

                @Override
                public void onSuccess(String generatedNumber) {
                    LoadingPanel.loading(false);
                    code.setValue(generatedNumber);
                    changeAccountType();
                }

            });
        } else {
            changeAccountType();
        }
    }


    private Div wrapSwitcher(String text, KpiSwitcher switcher) {
        Div div = new Div("grid-row margin-top");
        Div label = new Div("col-6 offset-4");
        Div widget = new Div("col-2");

        label.add(new Label(text));
        widget.add(switcher);

        div.add(label);
        div.add(widget);
        return div;
    }

    private void onParentAccountChange() {
        AccountItem selectedData = parentAccount.getSelectedData();
        showLookUpCheckBox(selectedData != null && selectedData.getAccountKey() != null);

        defaultAccount.setValue(false);//reset

        boolean isARorAP = (AccountingConstants.ACCOUNTS_RECEIVABLE_KEY.equals(systemAccountKey) || AccountingConstants.ACCOUNTS_PAYABLE_KEY.equals(systemAccountKey));
        boolean isParentARorAP = selectedData != null && selectedData.getAccountKey() != null
                && (AccountingConstants.ACCOUNTS_RECEIVABLE_KEY.equals(selectedData.getAccountKey()) || AccountingConstants.ACCOUNTS_PAYABLE_KEY.equals(selectedData.getAccountKey()));
        defaultAccountField.setVisible(isARorAP || isParentARorAP);
    }

    private void showLookUpCheckBox(boolean enable) {
        showInLookUp.setValue(false);
        showInLookUp.setVisible(enable);
    }

    private void setEditFormData(AddAccountItem editFormData) {
        if (editFormData != null) {
            if (editFormData.getParentAccountList().length > 0) {
                parentAccount.setItems(editFormData.getParentAccountList());
            }
            systemAccountKey = editFormData.getAccountKey();
            if (editFormData.getParentAccount() != null) {
                parentAccount.addAccountItem(editFormData.getParentAccount());
            }
            onParentAccountChange();
            defaultAccount.setValue(editFormData.getIsDefaultAccount());

            if (editFormData.getOpeningDate() != null) {
                openingBalanceDate.setDate(editFormData.getOpeningDate());
            }

            if (editFormData.getOpeningAmount() != null) {
                openingBalanceAmount.setText(AccountingUtils.get().formatPrice(editFormData.getOpeningAmount()));
            }

            if (editFormData.getCurrencyID() != null) {
                currencyListBox.setSelected(editFormData.getCurrencyID());
                onCurrencyChange();
            }

            if (editFormData.isUsedInSystem()) {
                currencyListBox.setEnabled(false);
                openingBalanceDate.setEnabled(false);
                openingBalanceAmount.setEnabled(false);
            }

            code.setText(editFormData.getCode());
            code.setEnabled(true);
            name.setText(editFormData.getName());
            description.setText(editFormData.getDescription());
            showInExpense.setValue(editFormData.isShowInExpense());
            active.setValue(editFormData.isActive());
            enablePayments.setValue(editFormData.isEnablePayments());
            showInLookUp.setValue(editFormData.isShowInLookUp());

        }
    }

    private void onCurrencyChange() {
        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED)) {
            if (baseCurrencyID.equals(currencyListBox.getSelectedId())) {
                openingBalanceAmount.setEnabled(true);
                openingBalanceDate.setEnabled(true);
            } else {
                openingBalanceAmount.setText("");
                openingBalanceDate.clearSelected();
                openingBalanceAmount.setEnabled(false);
                openingBalanceDate.setEnabled(false);
            }
        }
    }

    private void changeAccountType() {

        if (accountType.getSelectedId() != null) {
            String type = accountType.getItemGroup().get(accountType.getSelected());
            parentAccount.setType(type);
            parentAccount.setEnabled(true);
            parentAccount.clear();
            setVisibleBalanceFields(accountType.getSelectedId().equals(4));
        } else {
            parentAccount.setEnabled(false);
            openingBalanceAmount.setText("");
            openingBalanceDate.clearSelected();
            setVisibleBalanceFields(false);
        }
    }

    private void setVisibleBalanceFields(boolean visible) {
        openingBalanceAmountField.setVisible(visible);
        openingBalanceDateField.setVisible(visible);
    }


    @Override
    protected void registerFields() {
        accountType = new WfmDropdown();
        accountType.ensureDebugId(addAccountView + "accountType");
        accountType.addEventHandler(new DropdownListener() {
            @Override
            public void itemSelected() {
                onAccountTypeSelected();
            }

            @Override
            public void saveNewItem() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        code = new TextBox();
        code.ensureDebugId(addAccountView + "code");

        name = new TextBox();
        name.ensureDebugId(addAccountView + "name");

        description = new KpiTextArea();
        description.ensureDebugId(addAccountView + "description");

        currencyListBox = new DataListBox();
        currencyListBox.setWithoutNullLabel(true);
        currencyListBox.ensureDebugId(addAccountView + "currency");

        currencyListBox.addValueChangeHandler(event -> onCurrencyChange());

        openingBalanceAmount = new TextBox();
        openingBalanceAmount.ensureDebugId(addAccountView + "openingBalanceAmount");

        openingBalanceDate = new DatePicker();
        openingBalanceDate.ensureDebugId(addAccountView + "openingBalanceDate");

        openingBalanceAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(openingBalanceAmount, AccountingUtils.calculationScale, true);

        code.setMaxLength(20);
        name.setMaxLength(150);

        parentAccount = new AccountsLookUp();
        parentAccount.setObjectID(objectID);
        parentAccount.setValidateChildAccounts(true);

        parentAccount.setSystem(true);
        parentAccount.ensureDebugId("parentLookUp");

        if (Utils.hasGenericAccess(GenericSettingsEnum.TAX_ACCOUNT_ENABLED)) {
            parentAccount.setSystemAccountKeys("2202");
        }

        Validation.addNumericKeyboardListener(code);
        parentAccount.getSuggestBox().addSelectionHandler(selectionEvent -> onParentAccountChange());

        showInExpense = new KpiSwitcher();
        showInExpense.ensureDebugId(addAccountView + "showInExpense");
        showInExpense.setValue(EXPENSES.equals(fromView));

        active = new KpiSwitcher();
        active.ensureDebugId(addAccountView + "active");

        showInLookUp = new KpiSwitcher();
        showInLookUp.ensureDebugId(addAccountView + "showInLookUp");

        enablePayments = new KpiSwitcher();
        enablePayments.ensureDebugId(addAccountView + "enablePayments");

        defaultAccount = new KpiSwitcher();

        InputGroup amountWidget = new InputGroup(openingBalanceAmount, currencyListBox);

        openingBalanceDateField = new FormGroup(wfmStrings.asOfDate(), openingBalanceDate);
        openingBalanceDateField.setVisible(false);
        openingBalanceAmountField = new FormGroup(wfmStrings.amount(), amountWidget);
        openingBalanceAmountField.setVisible(false);

        defaultAccountField = wrapSwitcher(wfmStrings.setAsDefault(), defaultAccount);
        defaultAccountField.setVisible(false);
        showInLookUpField = wrapSwitcher(accountingStrings.showInLookUps(), showInLookUp);
        showInLookUpField.setVisible(false);


        drawMainSection();
        setDefaultValues();
    }

    @Override
    protected void initPredefinedValues() {

    }


    @Override
    protected void addButtons() {
        if (objectID == null) {
            MaterialLink save = new MaterialLink(wfmStrings.save());
            MaterialSplitButton splitButton = new MaterialSplitButton(save);
            //save & close
            save.addClickHandler(event -> {
                saveAndClose = true;
                enableButton(false);
                save();
            });

            //save & new
            MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
            saveAdd.addClickHandler(event -> {
                saveAndClose = false;
                enableButton(false);
                save();
                saveAdd.ensureDebugId(addAccountView + "saveAndAdd");
            });
            splitButton.addItem(saveAdd);
            addButton(splitButton);

        } else {
            //update
            addButton(wfmStrings.update(), WfmButton2.BTN_PRIMARY, event -> {
                saveAndClose = true;
                save();
            });
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        AccountingService.App.get().getAccountData(objectID, new AsyncCallback<AddAccountData>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(final AddAccountData result) {
                GWT.log("12345");
                baseCurrencyID = result.getBaseCurrency().getId();
                currencyListBox.setItems(result.getCurrencyItems());
                currencyListBox.setSelected(baseCurrencyID);

                getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems());
                setDefaultValues();
                if (objectID == null) {
                    setDefaultValuesByFormProperty();
                }
                if (result.getAccountItem() != null && !result.getAccountItem().getIsEditable()) {
                    accountType.setEnabled(false);
                }
                AccountingService.App.get().getAccountTypes(new AsyncCallback<AccountTypesByCategory>() {
                    public void onFailure(Throwable throwable) {
                        setEditFormData(result.getAccountItem());
                    }

                    public void onSuccess(AccountTypesByCategory atCat) {
                        LoadingPanel.loading(false);
                        accountType.addItems(wfmStrings.assets().toUpperCase(), atCat.getAssets());
                        accountType.addItems(wfmStrings.liabilities().toUpperCase(), atCat.getLiabilities());
                        accountType.addItems(wfmStrings.equity().toUpperCase(), atCat.getEquity());
                        accountType.addItems(wfmStrings.revenue().toUpperCase(), atCat.getRevenue());
                        accountType.addItems(wfmStrings.expenses().toUpperCase(), atCat.getExpenses());

                        if (result.getAccountItem() != null && result.getAccountItem().getAccountTypeId() != null) {
                            accountType.setSelected(result.getAccountItem().getAccountTypeId());
                        }
                        setEditFormData(result.getAccountItem());
                    }
                });


            }
        });
    }


    private void drawMainSection() {
        addTitleField(CustomFormConstants.GENERAL_INFORMATION, wfmStrings.basicDetails());
        if (formPropertyMap != null && formPropertyMap.get(CHART_ACCOUNT_TYPE) != null) {
            addField(CHART_ACCOUNT_TYPE, accountType, getTitle(formPropertyMap.get(CHART_ACCOUNT_TYPE).isChanged() ?
                    formPropertyMap.get(CHART_ACCOUNT_TYPE).getTitle() : wfmStrings.type(), formPropertyMap.get(CHART_ACCOUNT_TYPE).isRequired()));
            accountType.setEnabled(!formPropertyMap.get(CustomFormConstants.CHART_ACCOUNT_TYPE).isDisabled());
        } else {
            addField(CHART_ACCOUNT_TYPE, accountType, wfmStrings.type());
        }

        if (formPropertyMap != null && formPropertyMap.get(CHART_ACCOUNT_PARENT) != null) {
            addField(CHART_ACCOUNT_PARENT, parentAccount, getTitle(formPropertyMap.get(CHART_ACCOUNT_PARENT).isChanged() ?
                    formPropertyMap.get(CHART_ACCOUNT_PARENT).getTitle() : wfmStrings.parent(), formPropertyMap.get(CHART_ACCOUNT_PARENT).isRequired()));
            parentAccount.setEnabled(!formPropertyMap.get(CustomFormConstants.CHART_ACCOUNT_PARENT).isDisabled());
        } else {
            addField(CHART_ACCOUNT_PARENT, parentAccount, wfmStrings.parent());
        }

        if (formPropertyMap != null && formPropertyMap.get(CHART_ACCOUNT_CODE) != null) {
            addField(CHART_ACCOUNT_CODE, code, getTitle(formPropertyMap.get(CHART_ACCOUNT_CODE).isChanged() ?
                    formPropertyMap.get(CHART_ACCOUNT_CODE).getTitle() : wfmStrings.code(), formPropertyMap.get(CHART_ACCOUNT_CODE).isRequired()));
            code.setEnabled(!formPropertyMap.get(CustomFormConstants.CHART_ACCOUNT_CODE).isDisabled());
        } else {
            addField(CHART_ACCOUNT_CODE, code, wfmStrings.code());
        }

        if (formPropertyMap != null && formPropertyMap.get(CHART_ACCOUNT_NAME) != null) {
            addField(CHART_ACCOUNT_NAME, name, getTitle(formPropertyMap.get(CHART_ACCOUNT_NAME).isChanged() ?
                    formPropertyMap.get(CHART_ACCOUNT_NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(CHART_ACCOUNT_NAME).isRequired()));
            name.setEnabled(!formPropertyMap.get(CustomFormConstants.CHART_ACCOUNT_NAME).isDisabled());
        } else {
            addField(CHART_ACCOUNT_NAME, name, wfmStrings.name());
        }

        if (formPropertyMap != null && formPropertyMap.get(CHART_ACCOUNT_DESCRIPTION) != null) {
            addField(CHART_ACCOUNT_DESCRIPTION, description, getTitle(formPropertyMap.get(CHART_ACCOUNT_DESCRIPTION).isChanged() ?
                    formPropertyMap.get(CHART_ACCOUNT_DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(CHART_ACCOUNT_DESCRIPTION).isRequired()));
            description.setEnabled(!formPropertyMap.get(CustomFormConstants.CHART_ACCOUNT_DESCRIPTION).isDisabled());
        } else {
            addField(CHART_ACCOUNT_DESCRIPTION, description, wfmStrings.description());
        }


        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_CURRENCY_LIST) != null) {
            addField(ACCOUNT_CURRENCY_LIST, name, getTitle(formPropertyMap.get(ACCOUNT_CURRENCY_LIST).isChanged() ?
                    formPropertyMap.get(ACCOUNT_CURRENCY_LIST).getTitle() : wfmStrings.currency(), formPropertyMap.get(ACCOUNT_CURRENCY_LIST).isRequired()));
            currencyListBox.setEnabled(!formPropertyMap.get(CustomFormConstants.ACCOUNT_CURRENCY_LIST).isDisabled());
        } else {
            addField(ACCOUNT_CURRENCY_LIST, currencyListBox, wfmStrings.currency());
        }


        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_OPENING_BALANCE_DATE) != null) {
            addField(ACCOUNT_OPENING_BALANCE_DATE, openingBalanceDate, getTitle(formPropertyMap.get(ACCOUNT_OPENING_BALANCE_DATE).isChanged() ?
                    formPropertyMap.get(ACCOUNT_OPENING_BALANCE_DATE).getTitle() : wfmStrings.date(), formPropertyMap.get(ACCOUNT_OPENING_BALANCE_DATE).isRequired()));
            openingBalanceDate.setEnabled(!formPropertyMap.get(CustomFormConstants.ACCOUNT_OPENING_BALANCE_DATE).isDisabled());
        } else {
            addField(ACCOUNT_OPENING_BALANCE_DATE, openingBalanceDate, wfmStrings.date());
        }

        if (formPropertyMap != null && formPropertyMap.get(OPENING_BALANCE_AMOUNT) != null) {
            addField(OPENING_BALANCE_AMOUNT, openingBalanceAmount, getTitle(formPropertyMap.get(OPENING_BALANCE_AMOUNT).isChanged() ?
                    formPropertyMap.get(OPENING_BALANCE_AMOUNT).getTitle() : wfmStrings.amount(), formPropertyMap.get(OPENING_BALANCE_AMOUNT).isRequired()));
            openingBalanceAmount.setEnabled(!formPropertyMap.get(CustomFormConstants.OPENING_BALANCE_AMOUNT).isDisabled());
        } else {
            addField(OPENING_BALANCE_AMOUNT, openingBalanceAmount, wfmStrings.amount());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SHOW_IN_EXPENCE) != null) {
            addField(CustomFormConstants.SHOW_IN_EXPENCE, showInExpense, getTitle(formPropertyMap.get(CustomFormConstants.SHOW_IN_EXPENCE).isChanged() ? formPropertyMap.get(CustomFormConstants.SHOW_IN_EXPENCE).getTitle() : accountingStrings.showInExpenseClaim(), formPropertyMap.get(CustomFormConstants.SHOW_IN_EXPENCE).isRequired()));
            showInExpense.setEnabled(!formPropertyMap.get(CustomFormConstants.SHOW_IN_EXPENCE).isDisabled());
        } else {
            addField(CustomFormConstants.SHOW_IN_EXPENCE, showInExpense, getTitle(accountingStrings.showInExpenseClaim()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS_OF_ACCOUNT) != null) {
            addField(CustomFormConstants.STATUS_OF_ACCOUNT, active, getTitle(formPropertyMap.get(CustomFormConstants.STATUS_OF_ACCOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS_OF_ACCOUNT).getTitle() : wfmStrings.active(), formPropertyMap.get(CustomFormConstants.STATUS_OF_ACCOUNT).isRequired()));
            active.setEnabled(!formPropertyMap.get(CustomFormConstants.STATUS_OF_ACCOUNT).isDisabled());
        } else {
            addField(CustomFormConstants.STATUS_OF_ACCOUNT, active, getTitle(wfmStrings.active()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ENABLE_PAYMENTS) != null) {
            addField(CustomFormConstants.ENABLE_PAYMENTS, enablePayments, getTitle(formPropertyMap.get(CustomFormConstants.ENABLE_PAYMENTS).isChanged() ? formPropertyMap.get(CustomFormConstants.ENABLE_PAYMENTS).getTitle() : wfmStrings.enablePaymentsToThisAccount(), formPropertyMap.get(CustomFormConstants.ENABLE_PAYMENTS).isRequired()));
            enablePayments.setEnabled(!formPropertyMap.get(CustomFormConstants.ENABLE_PAYMENTS).isDisabled());
        } else {
            addField(CustomFormConstants.ENABLE_PAYMENTS, enablePayments, getTitle(wfmStrings.enablePaymentsToThisAccount()));
        }
        getCustomFieldUtil().drawCustomFields(this, objectID, false);
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());

        show();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CHART_OF_ACCOUNT_FORM;
    }

    @Override
    protected String getFormType() {
        return this.objectID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return "newClientList new-client-list";
    }


    public void refresh() {
        onAccountTypeSelected();
        code.setText("");
        name.setText("");
        description.setText("");
        openingBalanceDate.clearSelected();
        openingBalanceAmount.setText("");
        showInExpense.setValue(false);
        active.setValue(false);
        enablePayments.setValue(false);
        showInLookUp.setValue(false);
    }

    private void save() {

        if (!validate()) {
            enableButton(true);
            return;
        }

        final AddAccountItem accountData = new AddAccountItem();
        accountData.setObjectId(objectID);
        accountData.setAccountTypeId(accountType.getSelectedItem().getId());
        accountData.setName(name.getText());
        accountData.setDescription(description.getText());
        accountData.setCode(code.getText());
        accountData.setShowInExpense(showInExpense.getValue());
        accountData.setActive(active.getValue());
        accountData.setEnablePayments(enablePayments.getValue());
        accountData.setParentAccount(parentAccount.getSelectedData());
        accountData.setShowInLookUp(showInLookUp.getValue());
        accountData.setIsDefaultAccount(defaultAccount.getValue());

        accountData.setOpeningDate(openingBalanceDate.getDate());
        accountData.setOpeningAmount(AccountingUtils.get().parseToBigDecimal(openingBalanceAmount.getText()));

        accountData.setCurrencyID(currencyListBox.getSelectedId());

        accountData.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());


        if (objectID != null) {
            AccountingService.App.get().getAccountCodeUnique(code.getText().trim(), objectID, new AsyncCallback<AccountItem>() {
                public void onFailure(Throwable caught) {
                    enableButton(true);

                }

                public void onSuccess(AccountItem result) {
                    if (result == null) {
                        AccountingService.App.get().updateAccount(accountData, new AsyncCallback<Integer>() {
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                                enableButton(true);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            public void onSuccess(Integer result) {
                                LoadingPanel.loading(false);
                                enableButton(true);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.account()), Info.Type.INFO);
                                if (isPopup) {
                                    provider.execute();
                                }
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNT_SAVED, result, AddEditAccountView2.this);
                                onShellOk();
                            }
                        });
                    }
                }
            });
        } else {
            AccountingService.App.get().getAccountCodeUnique(code.getText().trim(), objectID, new AsyncCallback<AccountItem>() {
                public void onFailure(Throwable caught) {
                    enableButton(true);

                }

                public void onSuccess(AccountItem result) {
                    if (result == null) {
                        LoadingPanel.loading(true);
                        AccountingService.App.get().createAccount(accountData, new AsyncCallback<AccountItem>() {
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                enableButton(true);
                            }

                            public void onSuccess(AccountItem result) {
                                LoadingPanel.loading(false);
                                enableButton(true);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.account()), Info.Type.INFO);
                                onShellOk();
                                if (isPopup) {
                                    if (provider != null) {
                                        provider.execute();
                                    } else if (command != null) {
                                        command.execute(result);
                                    }
                                }
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNT_SAVED, result, AddEditAccountView2.this);
                            }
                        });
                    } else {
                        enableButton(true);
                        code.addStyleName(ERROR_FORM_STYLE);
                        code.addKeyDownHandler(event -> code.removeStyleName(ERROR_FORM_STYLE));
                        Info.show(wfmStrings.accountWithThisCompanyNumberAlreadyExists(), Info.Type.WARNING);

                    }
                }
            });
        }

    }

    public void onShellOk() {
        if (saveAndClose) {
            closeTab();
            enableButton(false);
            if (isPopup) {
                shell.close();
            } else {
                if (Utils.isSettings()) {
                    closeTab("accountingSettings|accountList");
                } else {
                    closeTab();
                }
            }
        } else {
            closeTab("account|add/add");
            reInit();
            enableButton(true);
        }
    }

    public void reInit() {
        objectID = null;
        saveAndClose = false;
        clear();
        registerFields();
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(code)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(name)) {
            errors++;
        }
        if (!Validation.validateWfmDropdown(accountType)) {
            errors++;
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CHART_ACCOUNT_DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.CHART_ACCOUNT_DESCRIPTION).isRequired()) {
            errors += markAsError(description, !Validation.validateTextAreaRequired(description));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CHART_ACCOUNT_PARENT) != null && formPropertyMap.get(CustomFormConstants.CHART_ACCOUNT_PARENT).isRequired()) {
            errors += markAsError(parentAccount, !Validation.validateLookUpRequired(parentAccount));
        }
        errors += getCustomFieldUtil().validateCustomFields();
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public void showPopup() {
        if (shell != null) {
            clear();
            isPopup = true;
            onInitialize();
            shell.center();
        }
    }

    private void setDefaultValuesByFormProperty(){
        if (formPropertyMap != null && formPropertyMap.get(CHART_ACCOUNT_CODE) != null && formPropertyMap.get(CHART_ACCOUNT_CODE).getDefaultValue() != null) {
            code.setText(formPropertyMap.get(CHART_ACCOUNT_CODE).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CHART_ACCOUNT_NAME) != null && formPropertyMap.get(CHART_ACCOUNT_NAME).getDefaultValue() != null) {
            name.setText(formPropertyMap.get(CHART_ACCOUNT_NAME).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CHART_ACCOUNT_DESCRIPTION) != null && formPropertyMap.get(CHART_ACCOUNT_DESCRIPTION).getDefaultValue() != null) {
            description.setText(formPropertyMap.get(CHART_ACCOUNT_DESCRIPTION).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SHOW_IN_EXPENCE) != null && formPropertyMap.get(CustomFormConstants.SHOW_IN_EXPENCE).getDefaultValue() != null && showInExpense != null) {
            showInExpense.setValue("true".equals(formPropertyMap.get(CustomFormConstants.SHOW_IN_EXPENCE).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS_OF_ACCOUNT) != null && formPropertyMap.get(CustomFormConstants.STATUS_OF_ACCOUNT).getDefaultValue() != null && active != null) {
            active.setValue("true".equals(formPropertyMap.get(CustomFormConstants.STATUS_OF_ACCOUNT).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ENABLE_PAYMENTS) != null && formPropertyMap.get(CustomFormConstants.ENABLE_PAYMENTS).getDefaultValue() != null && enablePayments != null) {
            enablePayments.setValue("true".equals(formPropertyMap.get(CustomFormConstants.ENABLE_PAYMENTS).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CHART_ACCOUNT_PARENT) != null && formPropertyMap.get(CustomFormConstants.CHART_ACCOUNT_PARENT).getDefaultValue() != null) {
            parentAccount.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CHART_ACCOUNT_PARENT).getSelectedId(), formPropertyMap.get(CustomFormConstants.CHART_ACCOUNT_PARENT).getDefaultValue()));
        }

    }

    public void asyncOnInitialize() {
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

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
