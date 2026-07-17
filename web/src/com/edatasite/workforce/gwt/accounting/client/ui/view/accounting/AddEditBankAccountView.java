package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccount;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FocusListenerAdapter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BANKACCOUNT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created by IntelliJ IDEA.
 * User: java
 * Date: 26.02.2009
 * Time: 18:07:03
 * To change this template use File | Settings | File Templates.
 */
public class AddEditBankAccountView extends View implements Colapse {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private TextBox code;
    private TextBox bankName;
    private TextBox accountNumber;
    private TextBox bankBranch;
    private TextArea bankAddress;
    private TextBox accauntName;
    private TextBox swiftCode;
    private TextBox ibanCode;
    private TextBox sortCode;
    private TextBox abaCode;
    private TextBox agentID;

    private TextBox streetAddress;
    private TextBox city;
    private DataListBox country;
    private DataListBox state;
    private TextBox postCode;
    private TextBox phoneNumber;

    private CurrencyWidget currencyWidget;

    private DatePicker openingBalanceDate;
    private TextBox openingBalanceAmount;
    private Label openingBalanceAmountCurrencyLabel;

    private WfmButton2 saveClose;
    private WfmButton2 saveaddAnother;

    private WfmForm table;
    private WfmForm.Field codeField;
    private WfmForm.Field bankNameField;
    private WfmForm.Field accountNumberField;
    private WfmForm.Field openingBalanceDateField;

    private KpiModal shell;
    private Command provider;
    private boolean isPopup = false;

    private Integer objectID;
    private Integer accountID;
    private String accCode;
    private final Map<Integer, List<SelectItem>> statesMap = new HashMap<>();

    private final String addEditBankAccountView = "add_edit_bank_account_view_";

    public AddEditBankAccountView() {
        super("bankadd");
        setDescription(property.getSingular(accountingStrings.addbankaccount()));
    }

    public AddEditBankAccountView(Integer objectID) {
        super("edit");
        setDescription(property.getSingular(accountingStrings.editBankAccount(), wfmStrings.bankAccount()));
        this.objectID = objectID;
    }

    public AddEditBankAccountView(Command provider) {
        super("bankadd");
        setDescription(property.getSingular(accountingStrings.addbankaccount()));
        this.provider = provider;
        this.isPopup = true;
        asyncOnInitialize();
    }

    public AddEditBankAccountView(Integer objectId, Command provider) {
        this(objectId);
        this.provider = provider;
        this.isPopup = true;
        asyncOnInitialize();
    }

    protected Widget onInitialize() {
        drawForm();
        return null;
    }

    public void drawForm() {
        if (isPopup) {
            shell = new KpiModal();
            shell.setWidth(700);
            shell.setScrollable(true);
        }
        table = new WfmForm();

        code = new TextBox();
        code.ensureDebugId(addEditBankAccountView + "code");

        bankName = new TextBox();
        bankName.ensureDebugId(addEditBankAccountView + "bankName");

        accountNumber = new TextBox();
        accountNumber.ensureDebugId(addEditBankAccountView + "accountNumber");

        bankBranch = new TextBox();
        bankBranch.ensureDebugId(addEditBankAccountView + "bankBranch");

        bankAddress = new TextArea();
        bankAddress.ensureDebugId(addEditBankAccountView + "bankAddress");

        accauntName = new TextBox();
        accauntName.ensureDebugId(addEditBankAccountView + "accauntName");

        swiftCode = new TextBox();
        swiftCode.ensureDebugId(addEditBankAccountView + "swiftCode");

        ibanCode = new TextBox();
        ibanCode.ensureDebugId(addEditBankAccountView + "ibanCode");

        sortCode = new TextBox();
        sortCode.ensureDebugId(addEditBankAccountView + "sortCode");

        abaCode = new TextBox();
        abaCode.ensureDebugId(addEditBankAccountView + "abaCode");

        agentID = new TextBox();
        agentID.ensureDebugId(addEditBankAccountView + "agentID");

        streetAddress = new TextBox();
        streetAddress.ensureDebugId(addEditBankAccountView + "streetAddress");

        city = new TextBox();
        city.ensureDebugId(addEditBankAccountView + "city");

        country = new DataListBox();
        country.ensureDebugId(addEditBankAccountView + "country");

        state = new DataListBox();
        state.ensureDebugId(addEditBankAccountView + "state");

        postCode = new TextBox();
        postCode.ensureDebugId(addEditBankAccountView + "postCode");

        phoneNumber = new TextBox();
        phoneNumber.ensureDebugId(addEditBankAccountView + "phoneNumber");

        currencyWidget = new CurrencyWidget(objectID == null);

        currencyWidget.addListener(() -> onCurrencyChange());

        openingBalanceAmountCurrencyLabel = new Label();
        openingBalanceAmount = new TextBox();
        openingBalanceAmount.addChangeHandler(event -> {
            openingBalanceDateField.setRequired(openingBalanceAmount.getValue().length() > 0);
        });
        openingBalanceAmount.ensureDebugId(addEditBankAccountView + "openingBalanceAmount");

        openingBalanceDate = new DatePicker();
        openingBalanceDate.ensureDebugId(addEditBankAccountView + "openingBalanceDate");

        openingBalanceAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(openingBalanceAmount, AccountingUtils.calculationScale, true);

        code.setMaxLength(10);
        bankName.setMaxLength(30);

        code.addStyleName(DEFAULT_WIDTH);
        bankName.addStyleName(DEFAULT_WIDTH);
        accountNumber.addStyleName(DEFAULT_WIDTH);
        bankBranch.addStyleName(DEFAULT_WIDTH);
        bankAddress.addStyleName(DEFAULT_WIDTH);
        accauntName.addStyleName(DEFAULT_WIDTH);
        swiftCode.addStyleName(DEFAULT_WIDTH);
        ibanCode.addStyleName(DEFAULT_WIDTH);
        sortCode.addStyleName(DEFAULT_WIDTH);
        abaCode.addStyleName(DEFAULT_WIDTH);
        agentID.addStyleName(DEFAULT_WIDTH);

        streetAddress.addStyleName(DEFAULT_WIDTH);
        city.addStyleName(DEFAULT_WIDTH);
        country.addStyleName(DEFAULT_WIDTH);
        state.addStyleName(DEFAULT_WIDTH);
        postCode.addStyleName(DEFAULT_WIDTH);
        phoneNumber.addStyleName(DEFAULT_WIDTH);
//        currencyListBox.setWidth("100px");

        saveClose = new WfmButton2(wfmStrings.saveAndClose());
        saveClose.ensureDebugId(addEditBankAccountView + "saveClose");

        saveaddAnother = new WfmButton2(accountingStrings.saveAndAddAnother());
        saveaddAnother.ensureDebugId(addEditBankAccountView + "saveaddAnother");

        AccountingService.App.get().getBankAccountForEdit(objectID, new AsyncCallback<BankAccount>() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(BankAccount acc) {
                setBankAccountData(acc);
            }
        });

        VerticalPanel titlePanel = new VerticalPanel();
        titlePanel.setWidth("100%");
        titlePanel.setSpacing(10);
        HTML title = new HTML("<div style='margin:10px 0 0 15px;'><b class=customTitle><font size=+1>" +
                (objectID != null ? property.getSingular(accountingStrings.editBankAccount(), wfmStrings.bankAccount()) : property.getSingular(accountingStrings.addbankaccount())) +
                                "</f></b></div>");

        HTML line = new HTML("<div class=line></div>");
        titlePanel.add(title);
        titlePanel.add(line);

        bankNameField = table.addField(wfmStrings.bankName(), bankName, wfmStrings.bankNameAppearLimitedTo30Characters(), true);
        accountNumberField = table.addField(wfmStrings.accountNumber(), accountNumber, wfmStrings.accountNumberFieldDescription(), true);
        codeField = table.addField(wfmStrings.accountCode(), code, accountingStrings.codeFieldDescription(), true/*required*/);

        table.addField(wfmStrings.accountName(), accauntName);
        table.addField(wfmStrings.bankBranch(), bankBranch);
        country.setAllowFirstItem(true);
        state.setAllowFirstItem(true);
        country.addValueChangeHandler(sender -> {
            if (isStateEnabled(country)) {
                state.setItems(statesMap.get(country.getSelectedId()).toArray(new SelectItem[]{}));
                state.setEnabled(true);
            } else {
                state.setEnabled(false);
                state.clear();
            }
        });
        state.setEnabled(false);

        table.addField(wfmStrings.streetAddress(), streetAddress);
        table.addField(wfmStrings.city(), city);
        table.addField(wfmStrings.country(), country);
        table.addField(wfmStrings.state(), state);
        table.addField(wfmStrings.postCode(), postCode);
        table.addField(wfmStrings.phone(), phoneNumber);
        table.addField(wfmStrings.swiftCode(), swiftCode);
        table.addField(wfmStrings.sortCode(), sortCode);
        table.addField(wfmStrings.ibanCode(), ibanCode);
        table.addField(wfmStrings.abaCode(), abaCode);
        table.addField(wfmStrings.agentID(), agentID, wfmStrings.upto9Characters(), false);
        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED)) {
            table.addField(wfmStrings.currency(), currencyWidget);
        }

        table.addTitleField(wfmStrings.openingBalance());
        openingBalanceDateField = table.addField(wfmStrings.asOfDate(), openingBalanceDate);
        table.addField(wfmStrings.amount(), new Widget[]{openingBalanceAmount, openingBalanceAmountCurrencyLabel}, false);

        code.addFocusListener(new FocusListenerAdapter() {
            public void onLostFocus(Widget widget) {
                if (!"".equals(code.getText())) {
                    if (objectID == null || (!code.getText().equals(accCode))) {
                        AccountingService.App.get().getAccountCodeUnique(code.getText().trim(), objectID, new AsyncCallback<AccountItem>() {
                            public void onFailure(Throwable throwable) {
                            }

                            public void onSuccess(AccountItem account) {
                                if (account != null && account.getCode() != null) {
                                    code.setText("");
                                    Validation.validateUserCredentialsRequired(code, codeField, wfmStrings.accountName() + " " + account.getName() + " " + wfmStrings.code() + "<font color='red'>" + account.getCode() + "</font> " + accountingStrings.validateUserCredentialsMessagePart3(), wfmStrings.pleaseEnterValue());
                                }
                            }
                        });
                    }
                }
            }
        });

        if (Utils.isDemoAccount()) {
            saveaddAnother.setEnabled(false);
            saveClose.setEnabled(false);
        }

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setStyleName("buttonn");

        if (objectID == null) {
            buttonPanel.add(saveaddAnother);
        }
        buttonPanel.add(saveClose);

        table.addHorizontalLine();

        saveClose.addClickHandler(widget -> {
            setEnabledButtons(false);
            saveButtonAction(1);
        });

        saveaddAnother.addClickHandler(widget -> saveButtonAction(0));

        if (isPopup) {
            shell.add(titlePanel);
            shell.add(table);
            shell.addButton(saveClose);
            if (objectID == null) {
                shell.addButton(saveaddAnother);
            }
            shell.addCloseHandler(popupPanelCloseEvent -> provider.execute());
            shell.open();
        } else {
            add(titlePanel);
            add(table);
            add(buttonPanel);
        }
    }

    private void onCurrencyChange() {
        openingBalanceAmountCurrencyLabel.setText(currencyWidget.getCurrencyName());
    }

    private void setEnabledButtons(boolean b) {
        if (saveClose != null) {
            saveClose.setEnabled(b);
        }
        if (saveaddAnother != null) {
            saveaddAnother.setEnabled(b);
        }
    }

    private void setBankAccountData(BankAccount acc) {
        Map<String, SelectItem[]> countriesAndRegions = acc.getCountryAndRegionItems();
        country.setItems(countriesAndRegions.get("country"));
        initStatesMap(countriesAndRegions.get("state"));

        currencyWidget.setCurrency((acc.getCurrency() != null ? acc.getCurrency().getId() : null), acc.getExchangeRate());
        onCurrencyChange();

        accountID = acc.getAccountId();
        accCode = acc.getCode();

        code.setText(acc.getCode());
        bankName.setText(acc.getName());
        accountNumber.setText(acc.getAccountNumber());
        if (acc.getBankBranch() != null) {
            bankBranch.setText(acc.getBankBranch());
        }
        if (acc.getBankAddress() != null) {
            bankAddress.setText(acc.getBankAddress());
        }
        if (acc.getAccauntName() != null) {
            accauntName.setText(acc.getAccauntName());
        }
        if (acc.getSwiftCode() != null) {
            swiftCode.setText(acc.getSwiftCode());
        }
        if (acc.getIbanCode() != null) {
            ibanCode.setText(acc.getIbanCode());
        }
        if (acc.getSortCode() != null) {
            sortCode.setText(acc.getSortCode());
        }
        if (acc.getAbaCode() != null) {
            abaCode.setText(acc.getAbaCode());
        }
        if (acc.getAgentID() != null) {
            agentID.setText(acc.getAgentID());
        }
        if (acc.getStreetAddress() != null) {
            streetAddress.setText(acc.getStreetAddress());
        }
        if (acc.getCity() != null) {
            city.setText(acc.getCity());
        }

        setCountryAndRegion(acc.getCountryId(), acc.getStateId());

        if (acc.getPostCode() != null) {
            postCode.setText(acc.getPostCode());
        }
        if (acc.getPhoneNumber() != null) {
            phoneNumber.setText(acc.getPhoneNumber());
        }

        if (acc.getOpeningDate() != null) {
            openingBalanceDate.setDate(acc.getOpeningDate().getNonConvertedDate());
        }
        if (acc.getOpeningAmount() != null) {
            openingBalanceAmount.setText(AccountingUtils.get().formatPrice(acc.getOpeningAmount()));
        }
        if (acc.isUsedInSystem()) {
            openingBalanceDate.setEnabled(false);
            openingBalanceAmount.setEnabled(false);
            currencyWidget.setEnabled(false);
        }
    }

    private void setCountryAndRegion(Integer countryID, Integer stateID) {
        if (countryID != null) {
            country.setSelected(countryID);
        } else {
            country.setSelectedNullLabel();
        }
        if (isStateEnabled(country)) {
            state.setEnabled(true);
            state.setItems(statesMap.get(countryID).toArray(new SelectItem[]{}));
            if (stateID != null) {
                state.setSelected(stateID);
            }
        } else {
            state.setEnabled(false);
            state.setSelectedNullLabel();
        }
    }

    public void initStatesMap(SelectItem[] result) {
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
        BankAccount bankAccount = new BankAccount();
        bankAccount.setObjectId(objectID);
        bankAccount.setAccountId(accountID);
        bankAccount.setCode(code.getText());
        bankAccount.setName(bankName.getText());
        bankAccount.setAccountNumber(accountNumber.getText());
        bankAccount.setAccauntName(accauntName.getText());
        bankAccount.setBankBranch(bankBranch.getText());
        bankAccount.setBankAddress(bankAddress.getText());
        bankAccount.setSwiftCode(swiftCode.getText());
        bankAccount.setIbanCode(ibanCode.getText());
        bankAccount.setSortCode(sortCode.getText());
        bankAccount.setAbaCode(abaCode.getText());
        bankAccount.setAgentID(agentID.getText());
        bankAccount.setStreetAddress(streetAddress.getText());
        bankAccount.setCity(city.getText());
        if (country.getSelectedItem() != null) {
            bankAccount.setCountryId(country.getSelectedItem().getId());
        }
        if (state.getSelectedItem() != null) {
            bankAccount.setStateId(state.getSelectedItem().getId());
        }
        bankAccount.setPostCode(postCode.getText());
        bankAccount.setPhoneNumber(phoneNumber.getText());

        bankAccount.setCurrency(currencyWidget.getCurrency());
        bankAccount.setExchangeRate(currencyWidget.getExchangeRate());

        bankAccount.setOpeningDate(openingBalanceDate.getDate() != null ? new DateNonConvertable(openingBalanceDate.getDate()) : null);
        bankAccount.setOpeningAmount(AccountingUtils.get().parseToBigDecimal(openingBalanceAmount.getText()));

        return bankAccount;
    }

    public boolean validate() {
        int error = 0;
        table.cleanupErrors();

        if (!Validation.validateTextBoxRequired(bankName, bankNameField)) {
            error++;
        }
        if (!Validation.validateTextBoxRequired(accountNumber, accountNumberField)) {
            error++;
        }
        if (!Validation.validateTextBoxRequired(code, codeField)) {
            error++;
        }
        if (!Validation.validateDate(openingBalanceDate, openingBalanceDateField, openingBalanceDateField.getRequired())) {
            error++;
        }

        if (error > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void refresh() {
        code.setText("");
        bankName.setText("");
        accountNumber.setText("");
        bankBranch.setText("");
        bankAddress.setText("");
        accauntName.setText("");
        swiftCode.setText("");
        ibanCode.setText("");
        sortCode.setText("");
        abaCode.setText("");
        agentID.setText("");
        streetAddress.setText("");
        city.setText("");
        country.setSelectedNullLabel();
        state.setSelectedNullLabel();
        postCode.setText("");
        phoneNumber.setText("");
        openingBalanceAmount.setText("");
        openingBalanceDate.clearSelected();
    }

    private void saveButtonAction(final int close) {
        if (!validate()) {
            setEnabledButtons(true);
            return;
        }

        if (objectID == null || (!code.getText().equals(accCode))) {
            AccountingService.App.get().getAccountCodeUnique(code.getText().trim(), objectID, new AsyncCallback<AccountItem>() {
                public void onFailure(Throwable caught) {
                    setEnabledButtons(true);
                }

                public void onSuccess(AccountItem accItem) {
                    setEnabledButtons(true);
                    if (accItem == null) {
                        saveBankAccount(close);
                    } else {
                        codeField.setErrorMessage(accountingStrings.accountWithThisCodeAlreadyExists(), "");
                        code.addKeyboardListener(new KeyboardListenerAdapter() {
                            public void onKeyPress(Widget sender, char keyCode, int modifiers) {
                                validateCodeField(sender, keyCode, codeField);
                            }
                        });
                    }
                }
            });
        } else {
            saveBankAccount(close);
        }
    }

    private void saveBankAccount(final int close) {
        BankAccount bankAccount = getBankAccountData();
        LoadingPanel.loading(true);
        AccountingService.App.get().createBankAccount(bankAccount, new AsyncCallback<Void>() {
            public void onFailure(Throwable throwable) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void onSuccess(Void o) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.bankAccount()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANKACCOUNT_SAVED, null, AddEditBankAccountView.this);
                onShellOk(close);
            }
        });
    }

    private void onShellOk(int close) {
        if (close == 1) {
            if (isPopup) {
                shell.close();
            } else {
                closeTab("accounting|bankaccount");
            }
        } else {
            refresh();
        }
    }

    private void validateCodeField(Widget sender, char keyCode, WfmForm.Field field) {
        if (((TextBox) sender).getText().length() > 0 && (Character.isDigit(keyCode))) {
            field.setErrorMessage(null, "");
        }
    }

    private boolean isStateEnabled(DataListBox c) {
        return (c.getSelectedId() != null && statesMap.get(c.getSelectedId()) != null && statesMap.get(c.getSelectedId()).size() > 0);
    }

    public String getIconStyle() {
        return null;
    }

    @Override
    public String getPropertyCode() {
        return BANKACCOUNT;
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
