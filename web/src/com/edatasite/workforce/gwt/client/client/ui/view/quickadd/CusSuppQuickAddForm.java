package com.edatasite.workforce.gwt.client.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.ui.view.AccountsReceivablePayableLookUp;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: Abror Abdukadirov
 * Date: 16.12.2017 10:42
 */
public class CusSuppQuickAddForm extends Composite implements Constants {

    interface CustomerQuickAddFormUiBinder extends UiBinder<Widget, CusSuppQuickAddForm> {
    }

    private static final CustomerQuickAddFormUiBinder ourUiBinder = GWT.create(CustomerQuickAddFormUiBinder.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    @UiField
    HTMLPanel panel;
    @UiField
    Span accountTitle;
    @UiField
    Label nameLabel;
    @UiField
    TextBox name;
    @UiField
    Label ownerLabel;
    @UiField
    HTMLPanel ownerDiv;
    @UiField
    Label phoneLabel;
    @UiField
    HTMLPanel phoneDiv;
    @UiField
    Span financialTitle;
    @UiField
    Label bankAccountLabel;
    @UiField
    HTMLPanel bankAccountDiv;
    @UiField
    DataListBox bankAccount;
    @UiField
    Label currencyLabel;
    @UiField
    DataListBox currency;
    @UiField
    Label accountReceivableLabel;
    @UiField
    HTMLPanel accountReceivableDiv;
    @UiField
    KpiCheckBox createGlAccountCheckBox;
    @UiField
    Label balanceDateLabel;
    @UiField
    DatePicker balanceDate;
    @UiField
    Label balanceAmountLabel;
    @UiField(provided = true)
    TextBox balanceAmount;
    @UiField
    Label emailLabel;
    @UiField
    TextBox email;
    @UiField
    Label industryLabel;
    @UiField
    DataListBox industry;

    private PhoneNumber phone;
    protected AccountsReceivablePayableLookUp accountReceivableLookUp;

    protected ExtendedCommand command;
    protected CrmAccountItem item;

    private final String accountType;
    private KpiSelect2 owners;

    private final String debugId = "customer_quick_add_";

    protected static NumberFormat numberFormat = Utils.getCalculationNumberFormat();
    private final Integer calculationScale = Utils.getAccountingCalculationScale() != null
                                       ? Utils.getAccountingCalculationScale()
                                       : 2;

    public CusSuppQuickAddForm(String accountType) {
        this.accountType = accountType;

        balanceAmount = new TextBox();
        balanceAmount.ensureDebugId(this.debugId + "balanceAmount");
        Validation.addNumericKeyboardListener(balanceAmount, calculationScale, true);

        initWidget(ourUiBinder.createAndBindUi(this));

        initForm();
    }

    private void initForm() {
        accountTitle.setText(wfmStrings.basicInfo());
        nameLabel.setText(wfmStrings.name());
        ownerLabel.setText(wfmStrings.owners());
        phoneLabel.setText(wfmStrings.phone());
        emailLabel.setText(wfmStrings.email());
        emailLabel.ensureDebugId("customer_quick_add_email");
        industryLabel.setText(wfmStrings.industry());
        industryLabel.ensureDebugId("customer_quick_add_industry");

        financialTitle.setText(wfmStrings.financialInformation());
        bankAccountLabel.setText(Property.getPluralWithObjectCode(Constants.BANKACCOUNT, wfmStrings.bankAccounts()));
        currencyLabel.setText(wfmStrings.currency());
        accountReceivableLabel.setText(CrmAccountItem.SUPPLIER.equals(accountType)
                                       ? wfmStrings.accountsPayable()
                                       : wfmStrings.accountsReceivable());

        balanceDateLabel.setText(wfmStrings.asOfDate());
        balanceAmountLabel.setText(wfmStrings.openingBalance());

        name.ensureDebugId(this.debugId + "name");

        owners = new KpiSelect2(true);
        owners.ensureDebugId(this.debugId.concat("owners"));
        ownerDiv.add(owners);

        phone = new PhoneNumber("");
        phone.ensureDebugId(this.debugId + "phone");
        phoneDiv.add(phone.getPhoneFeild());

        bankAccount.ensureDebugId(this.debugId + "bank_account");
        if (CrmAccountItem.SUPPLIER.equals(accountType)) {
            bankAccountDiv.setVisible(false);
        }

        currency.ensureDebugId(this.debugId + "currency");
        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED)) {
            currency.addValueChangeHandler(event -> onMultiCurrencyChange());
        }
        accountReceivableLookUp = new AccountsReceivablePayableLookUp(CrmAccountItem.SUPPLIER.equals(accountType)
                                                                      ? Constants.PAYABLE
                                                                      : Constants.RECEIVABLE);
        accountReceivableLookUp.ensureDebugId(this.debugId + "accountreceivable");
        accountReceivableDiv.add(accountReceivableLookUp);

        createGlAccountCheckBox.setText(wfmStrings.createGLAccount());
        createGlAccountCheckBox.ensureDebugId(this.debugId + "createGlAccount");
        createGlAccountCheckBox.addValueChangeHandler(event -> {
            accountReceivableLookUp.setEnabled(!createGlAccountCheckBox.getValue());
            accountReceivableLookUp.clear();
        });
        balanceDate.ensureDebugId(this.debugId + "balanceDate");
    }

    public void getCustomerQuickData() {
        LoadingPanel.loading(true, panel);
        ClientService.App.get().getCustomerQuickData(accountType, new AbstractAsyncCallback<CrmAccountItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void success(CrmAccountItem result) {
                LoadingPanel.loading(false, panel);
                item = result;
                fillFields();
            }
        });
    }

    private void fillFields() {
        if (item.getOwnerItems() != null) {
            owners.setItems(new ArrayList<>(Arrays.asList(item.getOwnerItems())));
        }
        /*if (item.getOwnerID() != null) {
            owner.setSelected(item.getOwnerID());
        } else {
            owner.setSelected(Utils.getUserID());
        }*/
        industry.setItems(item.getIndustries());

        if (item.getBankAccounts() != null) {
            bankAccount.setItems(item.getBankAccounts());
            bankAccount.setSelected(item.getBankAccountId());
        }
        if (item.getCurrencies() != null) {
            currency.setItems(item.getCurrencies());
        }
        currency.setSelected(item.getCurrencyId());
    }

    public boolean validate() {
        int errors = 0;
        if (name.getText() == null || "".equals(name.getText())) {
            name.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (accountReceivableLookUp.getSelectedItemID() == null && accountReceivableLookUp.getText().trim() != null && accountReceivableLookUp.getText().trim().length() > 0 && !LookUp.wfmStrings.searchTypeMessage().equals(accountReceivableLookUp.getText())) {
            accountReceivableLookUp.getTextBox().addStyleName(Constants.ERROR_FORM_STYLE);
            Utils.openParentSection(accountReceivableLookUp);
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        } else {
            accountReceivableLookUp.getTextBox().removeStyleName(Constants.ERROR_FORM_STYLE);
        }
        if (!Utils.isNullOrEmpty(email.getText()) && !Validation.validEmailFormat(email.getText(), false)) {
            email.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (currency != null && currency.getSelectedItem() == null) {
            currency.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    public void save() {
        LoadingPanel.loading(true, panel);
        setValuesToRPC();
        CRMService.App.get().saveAccount(item, accountType, null, false, false, false, true, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false, panel);
                if (result != null) {
                    if (result == -1) {
                        name.setFocus(true);
                        name.addStyleName(Constants.ERROR_FORM_STYLE);
                        Info.show(wfmStrings.accountWithThisCompanyNameAlreadyExists(), Info.Type.WARNING);
                    } else if (result == -2) {
                        Info.show(wfmStrings.accountWithThisCompanyNumberAlreadyExists(), Info.Type.WARNING);
                    } else {
                        Info.show(Property.get(Constants.CLIENT_LIST, wfmStrings.messSuccessfullyAdded(), wfmStrings.customer()), Info.Type.INFO);
                    }
                }
                if (command != null) {
                    command.execute(result);
                }
            }
        });
    }

    private void setValuesToRPC() {
        /*item.setOwnerName(null);

        if (owner.getSelectedItem() != null) {
            item.setOwnerID(owner.getSelectedItem().getId());
            item.setOwnerName(owner.getSelectedItem().getName());
        }*/
        item.setSelectedOwners(owners.getSelectedItems());
        if (industry.getSelectedItem() != null) {
            item.setIndustryID(industry.getSelectedId());
            item.setIndustry(industry.getSelectedItem().getName());
        }
        item.setName(name.getText());
        item.setEmail(email.getText());
        item.setPhone(phone.toString());
        item.setCurrency(null);

        if (currency.getSelectedItem() != null) {
            item.setCurrencyId(currency.getSelectedId());
            item.setCurrency(currency.getSelectedItem().getName());
        }

        if (CrmAccountItem.CUSTOMER.equals(accountType)) {
            item.setBalanceDate(balanceDate.getDate() != null ? new DateNonConvertable(balanceDate.getDate()) : null);
        } else if (CrmAccountItem.SUPPLIER.equals(accountType)) {
            item.setSupplierBalanceDate(balanceDate.getDate() != null ? new DateNonConvertable(balanceDate.getDate()) : null);
        }

        if (balanceAmount.getText() != null && !balanceAmount.getText().trim().isEmpty()) {
            try {
                if (CrmAccountItem.CUSTOMER.equals(accountType)) {
                    item.setBalanceAmount(numberFormat.parse(balanceAmount.getText()));
                } else if (CrmAccountItem.SUPPLIER.equals(accountType)) {
                    item.setSupplierBalanceAmount(numberFormat.parse(balanceAmount.getText()));
                }
            } catch (Exception e) {
            }
        } else {
            item.setBalanceAmount(null);
        }
        item.setAccountsReceivablePayable(accountReceivableLookUp.getSelectedData());
        item.setCreateGlAccount(createGlAccountCheckBox.getValue());
        item.setBankAccountId(null);

        if (bankAccount.getSelectedItem() != null) {
            item.setBankAccountId(bankAccount.getSelectedItem().getId());
            item.setBankAccount(bankAccount.getSelectedItem().getName());
        }
    }

    private void onMultiCurrencyChange() {
        boolean isBaseCurrency = item.getBaseCurrencyID() != null && item.getBaseCurrencyID().equals(currency.getSelectedId());
        accountReceivableLookUp.clear();
        accountReceivableLookUp.setCurrencyID(currency.getSelectedId());
        balanceAmount.setEnabled(isBaseCurrency);
        balanceDate.setEnabled(isBaseCurrency);
        if (!isBaseCurrency) {
            balanceAmount.setText("");
        }
    }

    public void clearForm() {
        name.setText("");
        name.removeStyleName(Constants.ERROR_FORM_STYLE);
        email.setText("");
        email.removeStyleName(Constants.ERROR_FORM_STYLE);
        //owners.clearSelected();
        phone.clearPhoneData();
        industry.clearSelected();
        bankAccount.clearSelected();
        currency.clearSelected();
        currency.removeStyleName(Constants.ERROR_FORM_STYLE);
        accountReceivableLookUp.clear();
        createGlAccountCheckBox.setValue(false);
        balanceDate.clearSelected();
        balanceAmount.setText("");
    }

    public void setCommand(ExtendedCommand command) {
        this.command = command;
    }

    public void setDefaultName(String defaultName) {
        name.setText(defaultName);
    }
}
