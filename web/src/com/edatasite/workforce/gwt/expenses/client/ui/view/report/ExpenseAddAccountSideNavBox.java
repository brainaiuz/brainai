package com.edatasite.workforce.gwt.expenses.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountTypesByCategory;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AddAccountItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.listener.DropdownListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

public class ExpenseAddAccountSideNavBox extends KpiSideNavBox implements Constants, Colapse {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private FlowPanel panel;
    private TextBox name;
    private TextArea2 description;
    private WfmDropdown accountType;
    private TextBox code;
    private AccountsLookUp parentAccount;
    private AccountTypesByCategory categories;
    private WfmButton2 saveAndCloseButton;
    private final AccountExecuteCommand command;

    public ExpenseAddAccountSideNavBox(AccountExecuteCommand accountExecuteCommand) {
        this.command = accountExecuteCommand;
        AccountingService.App.get().getAccountTypes(new AsyncCallback<AccountTypesByCategory>() {
            public void onFailure(Throwable throwable) {
            }

            public void onSuccess(AccountTypesByCategory atCat) {
                categories = atCat;
                init();
            }
        });
    }

    private void init() {
        panel = new FlowPanel();
        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.addCategory());
        addHeader(header);

        name = new TextBox();
        name.setMaxLength(150);

        description = new TextArea2();

        code = new TextBox();
        code.setMaxLength(20);
        Validation.addNumericKeyboardListener(code);


        parentAccount = new AccountsLookUp();
        parentAccount.setValidateChildAccounts(true);
        parentAccount.setSystem(true);
        parentAccount.ensureDebugId("parentLookUp");

        if (Utils.hasGenericAccess(GenericSettingsEnum.TAX_ACCOUNT_ENABLED)) {
            parentAccount.setSystemAccountKeys("2202");
        }

        accountType = new WfmDropdown();
        accountType.addEventHandler(new DropdownListener() {
            @Override
            public void itemSelected() {
                onAccountTypeSelected(accountType.getSelectedId());
            }

            @Override
            public void saveNewItem() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        if (categories != null) {
            accountType.addItems(wfmStrings.assets().toUpperCase(), categories.getAssets());
            accountType.addItems(wfmStrings.liabilities().toUpperCase(), categories.getLiabilities());
            accountType.addItems(wfmStrings.equity().toUpperCase(), categories.getEquity());
            accountType.addItems(wfmStrings.revenue().toUpperCase(), categories.getRevenue());
            accountType.addItems(wfmStrings.expenses().toUpperCase(), categories.getExpenses());
            if (categories.getDefaultAccount() != null && categories.getDefaultAccount().getId() != null) {
                accountType.setSelected(categories.getDefaultAccount().getId());
                onAccountTypeSelected(categories.getDefaultAccount().getId());
            }
        }

        panel.add(new FormGroup(wfmStrings.name(), name));
        panel.add(new FormGroup(wfmStrings.description(), description));
        panel.add(new FormGroup(wfmStrings.accountType(), accountType));
        panel.add(new FormGroup(wfmStrings.code(), code));
        panel.add(new FormGroup(wfmStrings.parent(), parentAccount));


        //init buttons
        saveAndCloseButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        saveAndCloseButton.addClickHandler(sender -> {
            saveAndCloseButton.setEnabled(false);
            save();
        });


        addBody(panel);
        addFooter(saveAndCloseButton);
        show();
    }

    private void onAccountTypeSelected(Integer accountId) {
        if (accountId != null) {
            LoadingPanel.loading(true);
            AccountingService.App.get().getGeneratedAccountNumber(accountId, new AsyncCallback<String>() {
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

    private void changeAccountType() {

        if (accountType.getSelectedId() != null) {
            String type = accountType.getItemGroup().get(accountType.getSelected());
            parentAccount.setType(type);
            parentAccount.setEnabled(true);
            parentAccount.clear();
        } else {
            parentAccount.setEnabled(false);
        }
    }

    public boolean validate() {
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
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void save() {
        saveAndCloseButton.setEnabled(false);
        if (!validate()) {
            saveAndCloseButton.setEnabled(true);
            return;
        }

        final AddAccountItem accountData = new AddAccountItem();
        accountData.setAccountTypeId(accountType.getSelectedItem().getId());
        accountData.setName(name.getText());
        accountData.setDescription(description.getText());
        accountData.setCode(code.getText());
        accountData.setShowInExpense(true);
        accountData.setParentAccount(parentAccount.getSelectedData());


        AccountingService.App.get().getAccountCodeUnique(code.getText().trim(), null, new AsyncCallback<AccountItem>() {
            public void onFailure(Throwable caught) {

            }

            public void onSuccess(AccountItem result) {
                if (result == null) {
                    LoadingPanel.loading(true);
                    AccountingService.App.get().createAccount(accountData, new AsyncCallback<AccountItem>() {
                        public void onFailure(Throwable caught) {
                            saveAndCloseButton.setEnabled(true);
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        public void onSuccess(AccountItem result) {
                            saveAndCloseButton.setEnabled(true);
                            LoadingPanel.loading(false);
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.account()), Info.Type.INFO);
                            if (command != null) {
                                command.execute(result);
                            }
                            remove();
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNT_SAVED, result, ExpenseAddAccountSideNavBox.this);
                        }
                    });
                } else {
                    saveAndCloseButton.setEnabled(true);
                    code.addStyleName(ERROR_FORM_STYLE);
                    code.addKeyDownHandler(event -> code.removeStyleName(ERROR_FORM_STYLE));
                    Info.show(wfmStrings.accountWithThisCompanyNumberAlreadyExists(), Info.Type.WARNING);
                }
            }
        });
    }
}
