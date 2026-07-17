package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountTypesByCategory;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AddAccountData;
import com.edatasite.workforce.gwt.accounting.client.rpc.AddAccountItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextArea;
import com.edatasite.workforce.gwt.core.client.ui.components.form.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
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
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 23.02.2009
 * Time: 16:31:42
 */
public class AddEditAccountView extends FooteredView implements Constants, Colapse, FittedContent {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private Integer objectID;

    private WfmDropdown accountType;
    private TextBox code;
    private TextBox name;
    private KpiTextArea description;
    private DataListBox currencyListBox;

    private DatePicker openingBalanceDate;
    private TextBox openingBalanceAmount;

    private KpiSwitcher showInExpense, showInLookUp, enablePayments, defaultAccount;
    private WfmButton2 saveAndAdd, save;

    private boolean close = false;

    private MaterialCollapsible ul;

    private FormGroup accountTypeField;
    private FormGroup codeField;
    private FormGroup nameField;
    private Div defaultAccountField;
    private Div showInLookUpField;
    private FormGroup openingBalanceDateField;
    private FormGroup openingBalanceAmountField;
    private FormGroup descriptionField;
    private FormGroup parentField;

    private KpiModal shell;
    private boolean isPopup = false;
    private Command provider;
    private ObjectCommand command;
    private String fromView;

    private AccountsLookUp parentAccount;

    private final String addAccountView = "add_account_view_";
    private Integer baseCurrencyID;

    private Integer systemAccountKey;

    public AddEditAccountView() {
        super("accountadd", wfmStrings.addAccount());
    }

    public AddEditAccountView(Integer objectID) {
        super("edit", wfmStrings.edit());
        this.objectID = objectID;
    }

    public AddEditAccountView(Command provider) {
        this(provider, null);
    }

    public AddEditAccountView(Command provider, String fromView) {
        super("accountadd", wfmStrings.addAccount());
        this.provider = provider;
        this.isPopup = true;
        this.fromView = fromView;
        asyncOnInitialize();
    }

    public AddEditAccountView(ObjectCommand command, String fromView) {
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

        parentAccount.setEnabled(false);
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

        showInLookUp = new KpiSwitcher();
        showInLookUp.ensureDebugId(addAccountView + "showInLookUp");

        enablePayments = new KpiSwitcher();
        enablePayments.ensureDebugId(addAccountView + "enablePayments");

        defaultAccount = new KpiSwitcher();

        accountTypeField = new FormGroup(wfmStrings.accountType(), accountType);
        codeField = new FormGroup(wfmStrings.code(), code); // TODO tooltips accountingStrings.codeFieldDescription()
        nameField = new FormGroup(wfmStrings.name(), name); // TODO tooltips accountingStrings.nameFieldDescription()
        descriptionField = new FormGroup(wfmStrings.description(), description); // TODO tooltips accountingStrings.descriptionFieldDescription()
        parentField = new FormGroup(wfmStrings.parent(), parentAccount);


        InputGroup amountWidget = new InputGroup(openingBalanceAmount, currencyListBox);

        openingBalanceDateField = new FormGroup(wfmStrings.asOfDate(), openingBalanceDate);
        openingBalanceDateField.setVisible(false);
        openingBalanceAmountField = new FormGroup(wfmStrings.amount(), amountWidget);
        openingBalanceAmountField.setVisible(false);

        defaultAccountField = wrapSwitcher(wfmStrings.setAsDefault(), defaultAccount);
        defaultAccountField.setVisible(false);
        Div showInExpenseField = wrapSwitcher(Property.getPluralWithObjectCodeWithReplace(Constants.EXPENSES_CLAIM, accountingStrings.showInExpenseClaims(), wfmStrings.expenseClaims()), showInExpense);
        Div enablePaymentsField = wrapSwitcher(wfmStrings.enablePaymentsToThisAccount(), enablePayments);
        showInLookUpField = wrapSwitcher(accountingStrings.showInLookUps(), showInLookUp);
        showInLookUpField.setVisible(false);

        GColumn column1 = new GColumn(GColumnEnum.COL_6, accountTypeField, nameField, descriptionField, openingBalanceAmountField, openingBalanceDateField);
        GColumn column2 = new GColumn(GColumnEnum.COL_6, codeField, parentField);
        column2.add(defaultAccountField);
        column2.add(showInExpenseField);
        column2.add(enablePaymentsField);
        column2.add(showInLookUpField);

        ul = new MaterialCollapsible();
        ul.setAccordion(false);
        ul.addStyleName("collapsible--panels collapsible--arrows-left");

        MaterialCollapsibleItem li = new MaterialCollapsibleItem();


//        MaterialCollapsibleHeader header = new MaterialCollapsibleHeader(new MaterialLink(objectID != null ? accountingStrings.editAccount() : accountingStrings.addAccount()));
//        header.addStyleName("active");

        MaterialCollapsibleBody body = new MaterialCollapsibleBody();
        body.add(new GRow(column1, column2));

//        li.add(header);
        li.add(body);
        ul.add(li);
        li.setActive(true);
        add(createFooter());

        AccountingService.App.get().getAccountData(objectID, new AsyncCallback<AddAccountData>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(final AddAccountData result) {
                baseCurrencyID = result.getBaseCurrency().getId();
                currencyListBox.setItems(result.getCurrencyItems());
                currencyListBox.setSelected(baseCurrencyID);
                if (result.getAccountItem() != null && !result.getAccountItem().getIsEditable()) {
                    accountType.setEnabled(false);
                }
                AccountingService.App.get().getAccountTypes(new AsyncCallback<AccountTypesByCategory>() {
                    public void onFailure(Throwable throwable) {
                        setEditFormData(result.getAccountItem());
                    }

                    public void onSuccess(AccountTypesByCategory atCat) {
                        accountType.addItems(wfmStrings.assets().toUpperCase(), atCat.getAssets());
                        accountType.addItems(wfmStrings.liabilities().toUpperCase(), atCat.getLiabilities());
                        accountType.addItems(wfmStrings.equity().toUpperCase(), atCat.getEquity());
                        accountType.addItems(wfmStrings.revenue().toUpperCase(), atCat.getRevenue());
                        accountType.addItems(wfmStrings.expenses().toUpperCase(), atCat.getExpenses());

                        if (result.getAccountItem() != null && result.getAccountItem().getAccountTypeId() != null) {
                            accountType.setSelected(result.getAccountItem().getAccountTypeId());
                            changeAccountType();
                        }
                        setEditFormData(result.getAccountItem());
                    }
                });


            }
        });

        if (isPopup) {
            shell.getContent().add(ul);
            shell.addButton(save);
            shell.open();
        } else {
            add(ul);
        }

        return null;
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

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return AddEditAccountView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return AddEditAccountView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> buttonList = new ArrayList<>();

        saveAndAdd = new WfmButton2(accountingStrings.saveAndAddAnother(), WfmButton2.BTN_PRIMARY);
        saveAndAdd.ensureDebugId(addAccountView + "saveAndAdd");

        save = new WfmButton2(objectID != null ? wfmStrings.update() : wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.ensureDebugId(addAccountView + "save");


        saveAndAdd.addClickHandler(sender -> save());
        save.addClickHandler(sender -> {
            close = true;
            save();
        });

        if (Utils.isDemoAccount()) {
            saveAndAdd.setEnabled(false);
            save.setEnabled(false);
        }

        Div saveWrapper = new Div();
        saveWrapper.add(save);

        Div saveAndNewWrapper = new Div();
        saveAndNewWrapper.add(saveAndAdd);

        if (objectID != null) {
            buttonList.add(saveWrapper);
        } else {
            buttonList.add(saveAndNewWrapper);
            buttonList.add(saveWrapper);
        }
        return buttonList;
    }

    private List<Widget> getFooterLeftSideWidgets() {
        return null;
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
            systemAccountKey = editFormData.getAccountKey();
            if (editFormData.getParentAccount() != null) {
                parentAccount.addAccountItem(editFormData.getParentAccount());
            }
            onParentAccountChange();

            if (editFormData.isHasChilds()) {
                parentAccount.setEnabled(true);
            }

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
        if (errors > 0) {
            setButtonsEnabled(true);
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public void refresh() {
        onAccountTypeSelected();
        code.setText("");
        name.setText("");
        description.setText("");
        openingBalanceDate.clearSelected();
        openingBalanceAmount.setText("");
        showInExpense.setValue(false);
        enablePayments.setValue(false);
        showInLookUp.setValue(false);
    }

    public String getIconStyle() {
        return "newClientList new-client-list";
    }

    private void save() {
        setButtonsEnabled(false);
        if (!validate()) {
            return;
        }

        final AddAccountItem accountData = new AddAccountItem();
        accountData.setObjectId(objectID);
        accountData.setAccountTypeId(accountType.getSelectedItem().getId());
        accountData.setName(name.getText());
        accountData.setDescription(description.getText());
        accountData.setCode(code.getText());
        accountData.setShowInExpense(showInExpense.getValue());
        accountData.setEnablePayments(enablePayments.getValue());
        accountData.setParentAccount(parentAccount.getSelectedData());
        accountData.setShowInLookUp(showInLookUp.getValue());
        accountData.setIsDefaultAccount(defaultAccount.getValue());

        accountData.setOpeningDate(openingBalanceDate.getDate());
        accountData.setOpeningAmount(AccountingUtils.get().parseToBigDecimal(openingBalanceAmount.getText()));

        accountData.setCurrencyID(currencyListBox.getSelectedId());

        if (objectID != null) {
            AccountingService.App.get().getAccountCodeUnique(code.getText().trim(), objectID, new AsyncCallback<AccountItem>() {
                public void onFailure(Throwable caught) {

                }

                public void onSuccess(AccountItem result) {
                    if (result == null) {
                        AccountingService.App.get().updateAccount(accountData, new AsyncCallback<Integer>() {
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            public void onSuccess(Integer result) {
                                LoadingPanel.loading(false);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.account()), Info.Type.INFO);
                                if (isPopup) {
                                    provider.execute();
                                }
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNT_SAVED, result, AddEditAccountView.this);
                                onShellOk();
                            }
                        });
                    } else {
                        setButtonsEnabled(true);
                    }
                }
            });
        } else {
            AccountingService.App.get().getAccountCodeUnique(code.getText().trim(), objectID, new AsyncCallback<AccountItem>() {
                public void onFailure(Throwable caught) {

                }

                public void onSuccess(AccountItem result) {
                    if (result == null) {
                        LoadingPanel.loading(true);
                        AccountingService.App.get().createAccount(accountData, new AsyncCallback<AccountItem>() {
                            public void onFailure(Throwable caught) {
                                setButtonsEnabled(true);
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            public void onSuccess(AccountItem result) {
                                setButtonsEnabled(true);
                                LoadingPanel.loading(false);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.account()), Info.Type.INFO);
                                onShellOk();
                                if (isPopup) {
                                    if (provider != null) {
                                        provider.execute();
                                    } else if (command != null) {
                                        command.execute(result);
                                    }
                                }
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNT_SAVED, result, AddEditAccountView.this);
                            }
                        });
                    } else {
                        code.addStyleName(ERROR_FORM_STYLE);
                        code.addKeyDownHandler(event -> code.removeStyleName(ERROR_FORM_STYLE));
                        Info.show(wfmStrings.accountWithThisCompanyNumberAlreadyExists(), Info.Type.WARNING);
                        setButtonsEnabled(true);
                    }
                }
            });
        }
    }

    private void onShellOk() {
        refresh();
        if (close) {
            if (isPopup) {
                shell.close();
            } else {
                if (Utils.isSettings()) {
                    closeTab("accountingSettings|accountList");
                } else {
                    closeTab();
                }
            }
        }
    }

    public void showPopup() {
        if (shell != null) {
            clear();
            isPopup = true;
            onInitialize();
            shell.center();
        }
    }

    private void setButtonsEnabled(boolean enable) {
        saveAndAdd.setEnabled(enable);
        save.setEnabled(enable);
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
}
