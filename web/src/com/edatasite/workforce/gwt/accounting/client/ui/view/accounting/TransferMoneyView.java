package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransferMoneyData;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.NumberUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.BankAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Feb 24, 2008
 * Time: 6:06:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransferMoneyView extends CustomForm2 implements Colapse {

    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private BankAccountLookUp fromAccountLookUp;
    private BankAccountLookUp toAccountLookUp;
    private TextBox reference;
    private TextBox amount;
    private DatePicker transferMoneyDate;
    private CurrencyWidget currencyWidget;
    private TextBox fromExchangeRate;
    private TextBox fromAccountCurrency;
    private TextBox toExchangeRate;
    private TextBox toAccountCurrency;
//    private DataListBox amountCurrencyListBox;

    private Integer objectID;
    private Integer fromAccountID;

    private CurrencyItem baseCurrency;
    private HTML toAccountExchangeRatePanel;


    //The currency of the account selected must either match the Transfer Currency or be your home currency
    public TransferMoneyView() {
        super("transferadd", accountingStrings.transferMoney());

    }

    public TransferMoneyView(Integer fromAccountID) {
        super("transferadd", accountingStrings.transferMoney());
        this.fromAccountID = fromAccountID;
    }

    public TransferMoneyView(Integer objectID, boolean isEditForm) {
        super("edit", accountingStrings.editTransferMoney());
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }


    private final HashMap<Integer, CurrencyItem> usedCurrenciesList = new HashMap<>();

    @Override
    protected void initPredefinedValues() {

    }

    private void drawForm(TransferMoneyData result) {

        if (result.getFromAccount() != null) {
            fromAccountLookUp.addBankAccountItem(result.getFromAccount());
        }
        if (objectID != null) {
            transferMoneyDate.setDate(result.getTransferMoneyDate().getNonConvertedDate());
            toAccountLookUp.addBankAccountItem(result.getToAccount());
            reference.setText(result.getReference());
            amount.setText(result.getAmount().setScale(2, RoundingMode.HALF_UP).toString());
        }

//        currencyWidget.setCurrencies(new CurrencyItem[]{baseCurrency});
//        currencyWidget.setCurrency(baseCurrency.getId());

        if (result.getAmountCurrencyID() != null) {
//            currencyWidget.setCurrency(result.getAmountCurrencyID());
        }


        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED)) {
//            currencyWidget.setCurrency(result.getCurrency().getId(), result.getExchangeRate());
        }

    }


    @Override
    protected void getDataToFillFields() {
        AccountingService.App.get().getBankAccountSummaryData(objectID, fromAccountID, new AsyncCallback<TransferMoneyData>() {

            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(TransferMoneyData result) {
                baseCurrency = result.getCurrency();
                drawForm(result);
                onBankAccountChange();
            }
        });

    }

    private void addBankAccountLookUpHandler(final BankAccountLookUp bankAccountLookUp) {
        bankAccountLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onBankAccountChange());

        bankAccountLookUp.getSuggestBox().addKeyUpHandler(keyUpEvent -> {
            onBankAccountChange();
        });
    }

    @Override
    protected void registerFields() {
        toAccountExchangeRatePanel = new HTML();
        toAccountExchangeRatePanel.setVisible(false);

        transferMoneyDate = new DatePicker(new Date());

        fromAccountLookUp = new BankAccountLookUp();
        fromAccountLookUp.setBeforeSearch(() -> {
            fromAccountLookUp.getFilterParametrs().setCheckBeforeSelected(true);
            fromAccountLookUp.getFilterParametrs().setBeforeSelectedId(toAccountLookUp.getSelectedItemID());
        });
        FormGroup fromAccountLookUpWidget = new FormGroup(wfmStrings.from(), fromAccountLookUp, true);
        fromAccountLookUpWidget.setWidth("100%");
        fromAccountLookUpWidget.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        toAccountLookUp = new BankAccountLookUp();
        toAccountLookUp.setBeforeSearch(() -> {
            toAccountLookUp.getFilterParametrs().setCheckBeforeSelected(true);
            toAccountLookUp.getFilterParametrs().setBeforeSelectedId(fromAccountLookUp.getSelectedItemID());
        });
        FormGroup toAccountLookUpWidget = new FormGroup(wfmStrings.to(), toAccountLookUp, true);
        toAccountLookUpWidget.setWidth("100%");
        toAccountLookUpWidget.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        toAccountLookUpWidget.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        reference = new TextBox();
        amount = new TextBox();
        Validation.addNumericKeyboardListener(amount, AccountingUtils.calculationScale);

        addBankAccountLookUpHandler(fromAccountLookUp);
        addBankAccountLookUpHandler(toAccountLookUp);

//        currencyWidget = new CurrencyWidget();
//        currencyWidget.setEnabled(true);
//        currencyWidget.setDatePicker(transferMoneyDate);

        fromExchangeRate = new TextBox();
        fromExchangeRate.setEnabled(true);
        fromExchangeRate.setWidth("80%");

        fromAccountCurrency = new TextBox();
        fromAccountCurrency.setEnabled(false);
        fromAccountCurrency.setWidth("20%");

        toExchangeRate = new TextBox();
        toExchangeRate.setEnabled(true);
        toExchangeRate.setWidth("80%");

        toAccountCurrency = new TextBox();
        toAccountCurrency.setEnabled(false);
        toAccountCurrency.setWidth("20%");

//        amountCurrencyListBox = new DataListBox();
//        amountCurrencyListBox.setWithoutNullLabel(true);
//        amountCurrencyListBox.getElement().setAttribute("style", "margin-left:2px");

        FormGroup amountWidget = new FormGroup(wfmStrings.amount(), amount, true);
        amountWidget.setWidth("100%");
        amountWidget.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        FormGroup fromExRateGroupWidget = new FormGroup(wfmStrings.from() + " " + wfmStrings.exchangeRate(), new InputGroup(fromExchangeRate, fromAccountCurrency), true);
        fromExRateGroupWidget.setWidth("100%");
        fromExRateGroupWidget.getElement().getStyle().setMarginRight(10, Style.Unit.PX);
        fromExRateGroupWidget.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        FormGroup toExRateGroupWidget = new FormGroup(wfmStrings.to() + " " + wfmStrings.exchangeRate(), new InputGroup(toExchangeRate, toAccountCurrency), true);
        toExRateGroupWidget.setWidth("100%");
        toExRateGroupWidget.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        toExRateGroupWidget.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);


        addTitleField(INFORMATION, wfmStrings.information());
        addField(FROM_ACCOUNT_LOOKUP, new InputGroup(fromAccountLookUpWidget, toAccountLookUpWidget), null);
        addField(TRANSFER_MONEY_DATE, transferMoneyDate, getTitle(wfmStrings.date(), true));
        addField(TRANSFER_MONEY_AMOUNT_CURRENCY, new InputGroup(fromExRateGroupWidget, toExRateGroupWidget), null);
        addField(TRANSFER_MONEY_AMOUNT, amountWidget, null);
        addField(REFERENCE, reference, wfmStrings.reference());
        show();
    }

    private void onBankAccountChange() {
        usedCurrenciesList.clear();
        BankAccountItem fromAccData = fromAccountLookUp.getSelectedData();
        final BankAccountItem toAccData = toAccountLookUp.getSelectedData();
        if (fromAccData != null && fromAccData.getCurrency() != null && !baseCurrency.getId().equals(fromAccData.getCurrency().getId())) {
            usedCurrenciesList.put(fromAccData.getCurrency().getId(), fromAccData.getCurrency());
        }
        if (toAccData != null && toAccData.getCurrency() != null && !baseCurrency.getId().equals(toAccData.getCurrency().getId())
                && !usedCurrenciesList.containsKey(toAccData.getCurrency().getId())) {

//            if (usedCurrenciesList.size() == 0) {
//                usedCurrenciesList.put(toAccData.getCurrency().getId(), toAccData.getCurrency());
//                toAccountExchangeRatePanel.setVisible(false);
//                toAccountExchangeRatePanel.setHTML("");
//            } else {

//            }
        }

        if (fromAccData != null && fromAccData.getCurrency() != null) {
            LoadingPanel.loading(true);
            AccountingService.App.get().getBankAccountLastExchangeRate(fromAccData.getId(), new AsyncCallback<BigDecimal>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(BigDecimal exchageRate) {
                    LoadingPanel.loading(false);
                    fromExchangeRate.setValue(exchageRate.setScale(AccountingUtils.getExRateScale(), RoundingMode.HALF_UP).toString());
                    String fromCurrencyCode = Optional.ofNullable(fromAccountLookUp)
                            .map(BankAccountLookUp::getSelectedData)
                            .map(BankAccountItem::getCurrency)
                            .map(SelectItem::getName)
                            .filter(Objects::nonNull)
                            .orElse("");
                    fromAccountCurrency.setValue(fromCurrencyCode);
                }
            });
        }

        if (toAccData != null && toAccData.getCurrency() != null) {
            LoadingPanel.loading(true);
            AccountingService.App.get().getBankAccountLastExchangeRate(toAccData.getId(), new AsyncCallback<BigDecimal>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(BigDecimal exchageRate) {
                    LoadingPanel.loading(false);
                    BMTHtmlTemplatesInterface template = GWT.create(BMTHtmlTemplatesInterface.class);
                    toAccountExchangeRatePanel.setVisible(true);
                    toAccountExchangeRatePanel.setHTML(template.toAccountExchangeRate(baseCurrency.getName(), AccountingUtils.get().formatExRate(exchageRate), toAccData.getCurrency().getName()));
                    toExchangeRate.setValue(exchageRate.setScale(AccountingUtils.getExRateScale(), RoundingMode.HALF_UP).toString());
                    String toCurrencyCode = Optional.ofNullable(toAccountLookUp)
                            .map(BankAccountLookUp::getSelectedData)
                            .map(BankAccountItem::getCurrency)
                            .map(SelectItem::getName)
                            .filter(Objects::nonNull)
                            .orElse("");
                    toAccountCurrency.setValue(toCurrencyCode);
                }
            });
        }

        /*if (usedCurrenciesList.size() >= 2) {
            Info.show("", "The currency of the account selected must either match the Transfer Currency or be your home currency", Info.Type.INFO);
            return;
        }*/
        if (usedCurrenciesList.size() == 1) {
//            currencyWidget.setCurrency(usedCurrenciesList.values().toArray(new CurrencyItem[]{})[0].getId());
        }

        if (!usedCurrenciesList.containsKey(baseCurrency.getId())) {
            usedCurrenciesList.put(baseCurrency.getId(), baseCurrency);
        }
        List<CurrencyItem> currencyItems = new LinkedList<>();
        currencyItems.add(baseCurrency);
        for (CurrencyItem item : usedCurrenciesList.values()) {
            if (item.equals(baseCurrency)) continue;
            currencyItems.add(item);
        }
//        currencyWidget.setCurrencies(currencyItems.toArray(new CurrencyItem[]{}));
//        currencyWidget.setCurrency(baseCurrency.getId());
    }

    @Override
    protected void addButtons() {
        WfmButton2 transfer = new WfmButton2(objectID != null
                ? wfmStrings.update()
                : accountingStrings.transfer(), WfmButton2.BTN_PRIMARY);
        transfer.addClickHandler(event -> transferMoney(true));
        addButton(transfer);
    }

    private void transferMoney(boolean checkExistingReference) {
        if (!validate()) return;

        TransferMoneyData transferData = new TransferMoneyData();
        transferData.setObjectID(objectID);

        transferData.setTransferMoneyDate(new DateNonConvertable(transferMoneyDate.getDate()));
        transferData.setFromAccount(fromAccountLookUp.getSelectedData());
        transferData.setToAccount(toAccountLookUp.getSelectedData());
        transferData.setReference(reference.getText());
        transferData.setValidateReference(checkExistingReference);
        transferData.setAmount(BigDecimal.valueOf(NumberUtils.parseCurrency(amount.getText())).setScale(2, RoundingMode.HALF_UP));

        transferData.setCurrency(fromAccountLookUp.getSelectedData().getCurrency());
        transferData.setFromExchangeRate(new BigDecimal(fromExchangeRate.getText()).setScale(AccountingUtils.getExRateScale(), RoundingMode.HALF_UP));
        transferData.setToExchangeRate(new BigDecimal(toExchangeRate.getText()).setScale(AccountingUtils.getExRateScale(), RoundingMode.HALF_UP));
        transferData.setExchangeRate(new BigDecimal(toExchangeRate.getText()).setScale(AccountingUtils.getExRateScale(), RoundingMode.HALF_UP));
        transferData.setAmountCurrencyID(fromAccountLookUp.getSelectedData().getCurrency().getId());
        transferData.setBaseAmount(transferData.getAmount().divide(new BigDecimal(fromExchangeRate.getText()), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));


        AccountingService.App.get().transferMoney(transferData, new AsyncCallback<Integer>() {
            public void onFailure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void onSuccess(Integer result) {
                if (result != null && result == -1) {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.information());
                    messageBox.setMessage(accountingMessages.referenceWithNumberExists(reference.getText()));
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onSubmit() {
                            transferMoney(false);
                        }
                    });
                    messageBox.setWidth("300px");
                    messageBox.open();
                } else {
                    Info.show(accountingStrings.moneyTransferredSuccessfully(), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MONEY_TRANSFER, result, TransferMoneyView.this);
                    closeTab();
                }
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        if (transferMoneyDate.getDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(transferMoneyDate.getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate("Transfer Money", Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }
        if (!Validation.validateLookUpRequired(fromAccountLookUp)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(toAccountLookUp)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(amount)) {
            errors++;
        } else if (!amount.getValue().matches("^\\d{1,25}(\\.\\d{1,2})?$")) {
            amount.addStyleName("x-form-invalid");
            Info.show(wfmMessages.notMatchingPrecisionAndScale(wfmStrings.amount(), 25, 2), Info.Type.WARNING);
            return false;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }


    @Override
    protected String getFormID() {
        return LayoutRPC.TRANSFER_MONEY_VIEW;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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

    interface BMTHtmlTemplatesInterface extends SafeHtmlTemplates {
        @Template("<div class=\"value-style\">1 {0} = {1} {2} </div>")
        SafeHtml toAccountExchangeRate(String baseCurrency, String exchnageRate, String accountCurrency);
    }

}
