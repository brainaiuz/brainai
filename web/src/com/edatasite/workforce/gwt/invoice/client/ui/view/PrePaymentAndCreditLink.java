package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTooltip;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECEIVABLE;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/24/12
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrePaymentAndCreditLink extends SimpleLink {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private KpiModal creditDialogBox;

    private final NewInvoice invoiceData;
    private PaymentData[] paymentsCredits;
    private FlexTable creditTable;
    private final PrePaymentLinkProvider creditLinkProvider;
    private boolean bussy = false;

    // Включает простой tooltip вместо dropdown-меню
    private static final boolean USE_SIMPLE_TOOLTIP = true;

    // Включает двуязычную подпись в тултипе: "EN / RU".
// По умолчанию выключено, чтобы не менять текущее поведение.
// В будущем можно вынести в конфиг/переменную окружения.
    private static final boolean DUAL_LANG = false;

    // Формирует подпись для tooltip / пункта меню
    /**
     * Формирует текст подсказки для иконки "i".
     * EN-часть берётся из i18n-бандлов (WfmStrings/AccountingStrings).
     * При включённом DUAL_LANG добавляем краткий RU-дуближ после слэша.
     *
     * Важно:
     * - Кодовые названия и i18n-методы (manualTransactions, bankTransfers и т.д.) НЕ переводим.
     * - Если появится полноценный RU-бандл, замените хардкод RU-строк на wfmStringsRu.*().
     */
    private String tooltipLabel(PaymentData pc) {
        String en;
        if (pc.getManualJournalID() != null) {
            en = wfmStrings.manualTransactions();          // "Manual Transactions"
        } else if (pc.getBankTransferID() != null) {
            en = accountingStrings.bankTransfers();        // "Bank Transfers"
        } else if (pc.getBankCheckItem() != null) {
            en = wfmStrings.checks();                      // "Checks"
        } else {
            en = wfmStrings.prepayments();                 // "Prepayments"
        }

        if (!DUAL_LANG) return en;

        // Быстрое RU-сопровождение; можешь заменить на свой RU-бандл
        String ru;
        if (pc.getManualJournalID() != null) ru = "Ручные проводки";
        else if (pc.getBankTransferID() != null) ru = "Банковские переводы";
        else if (pc.getBankCheckItem() != null) ru = "Чеки";
        else ru = "Предоплаты";
        return en + " / " + ru;
    }


    public PrePaymentAndCreditLink(NewInvoice invoiceData, PrePaymentLinkProvider creditLinkProvider) {
        super(accountingStrings.applyCredit());
        this.invoiceData = invoiceData;
        this.creditLinkProvider = creditLinkProvider;
        setVisible(false);
        addClickHandler(clickEvent -> {

            if (creditDialogBox == null) {
                initializePrepaymentDialogBox();
            }
            creditDialogBox.open();
        });
    }


    // КОНСТАНТЫ ДЛЯ КОЛОНОК ТАБЛИЦЫ
    private static final int COL_CHECKBOX = 0;
    private static final int COL_AMOUNT = 1;
    private static final int COL_REMAINING_INFO = 2;
    private static final int COL_NUMBER = 3;
    private static final int COL_PAYMENT_DATE = 4;
    private static final int COL_REFERENCE = 5;
    private static final int COL_PO_OR_SQ = 6;
    private static final int COL_APPLY_DATE = 7;

/**
 * Главный метод, который теперь просто вызывает вспомогательные методы.
 * Читается как план действий.
 */
private void initializePrepaymentDialogBox() {
    creditDialogBox = new KpiModal();
    creditDialogBox.setTitle(accountingStrings.applyCredit());
    creditDialogBox.addStyleName("modal--fitContent");

    creditTable = createCreditTableHeader();
    populateTableRows();

    FlexTable mainLayout = setupMainLayout();
    setupDialogActions();

    creditDialogBox.add(mainLayout);
}

/**
 * Создает и настраивает таблицу, а также формирует ее заголовок.
 * @return Сконфигурированный экземпляр FlexTable.
 */
private FlexTable createCreditTableHeader() {
    FlexTable table = new FlexTable();
    table.setStyleName("tbl tbl--rowSpace tbl--bordered tbl--striped");
    // setCellSpacing устарел, лучше управлять отступами через CSS с помощью tbl--rowSpace

    // Формирование заголовка с использованием констант
    table.setText(0, COL_CHECKBOX, "");
    table.setText(0, COL_AMOUNT, wfmStrings.amount() + " (" + invoiceData.getCurrencyName() + ")");
    table.setText(0, COL_REMAINING_INFO, wfmStrings.remaining() + " " + wfmStrings.amount());
    table.setText(0, COL_NUMBER, wfmStrings.number());
    table.setText(0, COL_PAYMENT_DATE, wfmStrings.paymentDate());
    table.setText(0, COL_REFERENCE, wfmStrings.reference());

    if (RECEIVABLE.equals(invoiceData.getType())) {
        table.setText(0, COL_PO_OR_SQ, Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()));
    } else {
        table.setText(0, COL_PO_OR_SQ, Property.get(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder()));
    }
    table.setText(0, COL_APPLY_DATE, wfmStrings.date());
    table.getRowFormatter().setStyleName(0, "thead");

    // Управление шириной через CSS классы вместо setWidth()
    HTMLTable.ColumnFormatter cf = table.getColumnFormatter();
    cf.setStyleName(COL_CHECKBOX, "col-checkbox");
    cf.setStyleName(COL_PAYMENT_DATE, "col-date");
    cf.setStyleName(COL_APPLY_DATE, "col-date");
    // Остальные колонки будут иметь auto-ширину по умолчанию

    return table;
}

/**
 * Наполняет таблицу данными из массива paymentsCredits.
 * Вся логика создания виджетов для одной строки находится здесь.
 */
/**
 * Наполняет таблицу данными из массива paymentsCredits.
 * Вся логика создания виджетов для одной строки находится здесь.
 */
private void populateTableRows() {
    // --- Диагностический блок (можете оставить или удалить) ---
    if (paymentsCredits == null || paymentsCredits.length == 0) {
        GWT.log("!!! ДИАГНОСТИКА: Массив paymentsCredits пуст или null. Рендерить нечего.");
        return;
    }
    GWT.log("!!! ДИАГНОСТИКА: Найдено " + paymentsCredits.length + " записей для отображения.");
    // --- Конец блока ---

    int currentRow = 1;
    for (PaymentData pc : paymentsCredits) {
        // Логика создания виджетов для строки...
        // (Этот блок остается таким же сложным, но теперь он хотя бы инкапсулирован)
        // Использование констант делает код ниже гораздо более читаемым
        BigDecimal amountForPay = pc.getPaymentAmount().multiply(getPaymentExchangeRate()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
        if (invoiceData.getDueAmount().compareTo(amountForPay) < 0) {
            amountForPay = invoiceData.getDueAmount();
        }
        final AmountTextBox amountTxtBox = new AmountTextBox(pc.getPaymentAmount());
        final CreditBalanceCheckBox checkBox = new CreditBalanceCheckBox(pc, amountForPay);
        final DatePicker datePicker = new DatePicker();
        datePicker.addStyleName("input-datepicker");

        amountTxtBox.addStyleName("input-amount");
        Validation.addNumericKeyboardListener(amountTxtBox, AccountingUtils.calculationScale);
        amountTxtBox.setText(AccountingUtils.get().formatPrice(amountForPay));

        checkBox.addClickHandler(clickEvent -> {
            amountTxtBox.setEnabled(checkBox.getValue());
            datePicker.setEnabled(checkBox.getValue());
        });

        amountTxtBox.addKeyUpHandler(keyUpEvent -> {
            BigDecimal amountPay = AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText());
            amountTxtBox.setAmountInBaseCurrency(amountPay.divide(getPaymentExchangeRate(), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
        });

        StringBuilder info = new StringBuilder();
        if (pc.isCrmAccountCredit()) {
            info.append(wfmStrings.openingBalance() + ". ");
        }

        info.append(" ");
        info.append(wfmStrings.amount() + " : " + AccountingUtils.get().formatPrice(pc.getPaymentAmount()) + " " + invoiceData.getBaseCurrencyName() + "<br/>");
        HTML infoLabel = new HTML(info.toString());

        amountTxtBox.setEnabled(false);
        datePicker.setEnabled(false);
        creditTable.setWidget(currentRow, COL_CHECKBOX, checkBox);
        creditTable.setWidget(currentRow, COL_AMOUNT, amountTxtBox);
        creditTable.setWidget(currentRow, COL_REMAINING_INFO, infoLabel);


// 2) СБОРКА TOOLTIP (иконка + MaterialTooltip)
        Div tooltipWrapper = new Div();
        tooltipWrapper.addStyleName("txt-tltp__i");
        // иконка
        Icon iInfo = new Icon();
        iInfo.setClass("ficon--info");

        MaterialLink iconLink = new MaterialLink();
        iconLink.add(iInfo);

        // сам tooltip
        String tip = tooltipLabel(pc);
        MaterialTooltip tooltip = new MaterialTooltip(iconLink, tip);
// tooltip.setPosition(Position.RIGHT); // опционально

        // прикрепляем к wrapper
        tooltipWrapper.add(iconLink);
        tooltipWrapper.add(tooltip);

// 3) ЗАПОЛНЕНИЕ КОЛОНОК ТАБЛИЦЫ
        String number;
        HTML reference;
        MaterialLink numberLink;

        if (pc.isManualJournal()) {
            number = pc.getInvoiceNumber();
            reference = new HTML(pc.getReferenceNumber());
            numberLink = new MaterialLink(number, "manual|summary/" + pc.getManualJournalID());
        } else if (pc.isPrepayment()) {
            number = pc.getNumber();
            reference = new HTML(pc.getReference());
            numberLink = new MaterialLink(number, "invoicepayment|paymentView/" + pc.getObjectID());
        } else if (pc.isBankTransafer()) {
            String type;
            number = pc.getNumber() != null ? pc.getNumber() : "";
            if (number.startsWith("CP")) {
                type = "CASH_PAYMENT";
            } else if (number.startsWith("CR")) {
                type = "CASH_RECEIPT";
            } else if (number.startsWith("REM")) {
                type = "RECEIVE_MONEY";
            } else {
                type = "SPEND_MONEY";
            }
            reference = new HTML(pc.getReferenceNumber());
            numberLink = new MaterialLink(number, "spendreceivemoney|summary/" + pc.getBankTransferID() + "/" + type);
        } else {
            number = pc.getNumber();
            reference = new HTML(pc.getReferenceNumber());
            numberLink = new MaterialLink(number, "check|summary/" + pc.getObjectID());
        }
        creditTable.setWidget(currentRow, COL_NUMBER, numberLink);

        // paymentDate (только для информации)
        DatePicker paymentDate = new DatePicker();
        paymentDate.setEnabled(false);
        if (pc.getDate() != null) {
            paymentDate.setDate(pc.getDate().getNonConvertedDate());
        }
        creditTable.setWidget(currentRow, COL_PAYMENT_DATE, paymentDate);

        // referencePanel: текст + tooltip
        FlowPanel referencePanel = new FlowPanel();
        referencePanel.addStyleName("txt-tltp");
        // reference (текст)
        reference.addStyleName("txt-tltp__txt");
        referencePanel.add(reference);
        // tooltip (иконка + tooltip)
        referencePanel.add(tooltipWrapper);
        // ставим в основную таблицу
        creditTable.setWidget(currentRow, COL_REFERENCE, referencePanel);

        if (RECEIVABLE.equals(invoiceData.getType())) {
            HTML safeHtml = new HTML("");
            if (pc.getSaleQuoteItem() != null) {
                safeHtml.setHTML(pc.getSaleQuoteItem().getName());
            }
            creditTable.setWidget(currentRow, COL_PO_OR_SQ, safeHtml);
        } else {
            HTML safeHtml = new HTML("");
            if (pc.getPurchaseOrderItem() != null) {
                safeHtml.setHTML(pc.getPurchaseOrderItem().getName());
            }
            creditTable.setWidget(currentRow, COL_PO_OR_SQ, safeHtml);
        }

        creditTable.setWidget(currentRow, COL_APPLY_DATE, datePicker);

        currentRow++;
    }
}

/**
 * Создает основной макет диалогового окна и настраивает кнопки действий.
 * @return Сконфигурированный экземпляр FlexTable для основного макета.
 */
private FlexTable setupMainLayout() {
    FlexTable mainTable = new FlexTable();
    mainTable.setWidth("100%");
    int row = 0;

    if (!invoiceData.getCurrencyName().equals(invoiceData.getBaseCurrencyName())) {
        mainTable.setWidget(row++, 0, new HTML(1 + " " + invoiceData.getBaseCurrencyName() + " = " + getPaymentExchangeRate() + " " + invoiceData.getCurrencyName()));
    }

    Div divPanel = new Div();
    divPanel.add(creditTable);
    mainTable.setWidget(row, 0, divPanel);
    return mainTable;
}

private void setupDialogActions() {
    WfmButton2 apply = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_PRIMARY);
    WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_WHITE_OUTLINE);

    apply.addClickHandler(clickEvent -> {
        if (!bussy) {
            bussy = true;
            applyCreditsToInvoice(creditTable);
        }
    });
    cancel.addClickHandler(clickEvent -> creditDialogBox.close());

    creditDialogBox.addButton(cancel);
    creditDialogBox.addButton(apply);
}

    public class CreditBalanceCheckBox extends KpiCheckBox {
        private final BigDecimal maximalPayableAmount;
        private final PaymentData credit;

        public CreditBalanceCheckBox(PaymentData credit, BigDecimal maximalPayableAmount) {
            this.maximalPayableAmount = maximalPayableAmount;
            this.credit = credit;
        }

        public BigDecimal getMaximalPayableAmount() {
            return maximalPayableAmount;
        }

        public SelectItem getCrmAccount() {
            return credit.getCrmAccount();
        }

        public SelectItem getBankCheckItem() {
            return credit.getBankCheckItem();
        }

        public Integer getProjectID() {
            return credit.getRelatedObjectID();
        }

        public boolean isCrmAccountCredit() {
            return credit.isCrmAccountCredit();
        }

        public boolean isPrepayment() {
            return credit.isPrepayment();
        }

        public boolean isManualJournal() {
            return credit.isManualJournal();
        }

        public Integer getManualJournalID() {
            return credit.getManualJournalID();
        }

        public boolean isBankTransfer() {
            return credit.isBankTransafer();
        }

        public Integer getBankTransferID() {
            return credit.getBankTransferID();
        }

        public SelectItem getPaymentAccount() {
            return credit.getPaymentAccount();
        }

        public Date getDate() {
            return credit.getDate() != null ? DateUtil.resetTime(credit.getDate().getNonConvertedDate()) : null;
        }

        public Integer getObjectID() {
            return credit.getObjectID();
        }

        public BigDecimal getCreditAmount() {
            return credit.getPaymentAmount();
        }

        public AccountItem getReceivablePayable() {
            return credit.getReceivablePayable();
        }
    }

    public class AmountTextBox extends TextBox {
        private BigDecimal amountInBaseCurrency;

        public AmountTextBox(BigDecimal amountInBaseCurrency) {
            this.amountInBaseCurrency = amountInBaseCurrency;
        }

        public BigDecimal getAmountInBaseCurrency() {
            return amountInBaseCurrency;
        }

        public void setAmountInBaseCurrency(BigDecimal amountInBaseCurrency) {
            this.amountInBaseCurrency = amountInBaseCurrency;
        }
    }

    private void applyCreditsToInvoice(FlexTable creditTable) {
        int errors = 0, dateValidateErrors = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (int i = 1; i < creditTable.getRowCount(); i++) {
            CreditBalanceCheckBox checkBox = (CreditBalanceCheckBox) creditTable.getWidget(i, 0);
            if (checkBox.getValue()) {
                final TextBox amountTxtBox = (TextBox) creditTable.getWidget(i, 1);
                if (!Validation.validateTextBoxRequired(amountTxtBox)) {
                    errors++;
                } else {
                    BigDecimal amount = AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    totalAmount = totalAmount.add(amount);
                    String errorMessage = null;
                    if (amount.compareTo(BigDecimal.ZERO) == 0) {
                        errorMessage = accountingStrings.amountShouldNotBeZero();
                    } else if (amount.compareTo(checkBox.getMaximalPayableAmount().setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP)) > 0) {
                        errorMessage = accountingStrings.amountShouldNotBeMoreThan() + " " + AccountingUtils.get().formatPrice(checkBox.getMaximalPayableAmount());
                    }
                    if (errorMessage != null) {
                        amountTxtBox.setStyleName("x-form-invalid");
                        amountTxtBox.addKeyDownHandler(keyDownEvent -> {
                            if (!"".equals(amountTxtBox.getStyleName()))
                                amountTxtBox.removeStyleName(amountTxtBox.getStyleName());
                        });
                        errors++;
                    }
                }

                DatePicker datePicker = (DatePicker) creditTable.getWidget(i, 7);
                if (datePicker.getDate() == null) {
                    Validation.validateDate(datePicker, new HTML(), false);
                    errors++;
                }

                if (datePicker.getDate() != null) {
                    DateUtil.resetTime(datePicker.getDate());
                    if (datePicker.getDate().before(invoiceData.getInvoiceDate().getNonConvertedDate())) {
                        dateValidateErrors++;
                        datePicker.addStyleName(Constants.ERROR_FORM_STYLE);
                        Utils.scrollIntoView(datePicker.getElement());
                    } else {
                        datePicker.removeStyleName(Constants.ERROR_FORM_STYLE);
                    }

                    if (checkBox.getDate() != null && datePicker.getDate().before(checkBox.getDate())) {
                        datePicker.addStyleName(Constants.ERROR_FORM_STYLE);
                        Utils.scrollIntoView(datePicker.getElement());

                        bussy = false;
                        Info.show(accountingMessages.paymentCreditDateMessage(DateUtil.getDateTimeFormat().format(checkBox.getDate())), Info.Type.WARNING);
                        return;
                    }
                }
            }
        }
        if (errors > 0) {
            bussy = false;
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return;
        }
        if (totalAmount.compareTo(creditLinkProvider.getDueAmount()) > 0) {
            bussy = false;
            Info.show(accountingStrings.amountNotMoreDueAmount(), Info.Type.WARNING);
            return;
        }

        if (dateValidateErrors > 0) {
            bussy = false;
            Info.show(accountingStrings.datePaidMessage(), Info.Type.WARNING);
            return;
        }

        List<PaymentData> appliedCreditList = new LinkedList<>();
        for (int i = 1; i < creditTable.getRowCount(); i++) {
            CreditBalanceCheckBox checkBox = (CreditBalanceCheckBox) creditTable.getWidget(i, 0);
            if (checkBox.getValue()) {
                AmountTextBox amountTxtBox = (AmountTextBox) creditTable.getWidget(i, 1);
                DatePicker datePicker = (DatePicker) creditTable.getWidget(i, 7);

                PaymentData paymentData = new PaymentData();
                paymentData.setObjectID(checkBox.getObjectID());
                paymentData.setRelatedObjectID(checkBox.getProjectID());
                paymentData.setInvoiceID(invoiceData.getID());
                paymentData.setCrmAccount(checkBox.getCrmAccount());
                paymentData.setBankCheckItem(checkBox.getBankCheckItem());
                paymentData.setCrmAccountCredit(checkBox.isCrmAccountCredit());
                paymentData.setPrepayment(checkBox.isPrepayment());
                paymentData.setReceivablePayable(checkBox.getReceivablePayable());

                BigDecimal creditAmountInInvoiceCurrency = checkBox.getCreditAmount().multiply(getPaymentExchangeRate());

                if (AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP).compareTo(creditAmountInInvoiceCurrency.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP)) == 0) {
                    paymentData.setPaymentAmount(creditAmountInInvoiceCurrency);
                    paymentData.setBaseAmount(checkBox.getCreditAmount());
                } else {
                    paymentData.setPaymentAmount(AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText()));
                    paymentData.setBaseAmount(amountTxtBox.getAmountInBaseCurrency());
                }
                paymentData.setDate(new DateNonConvertable(datePicker.getDate()));
                paymentData.setManualJournalID(checkBox.getManualJournalID());
                paymentData.setBankTransferID(checkBox.getBankTransferID());
                paymentData.setPaymentAccount(checkBox.getPaymentAccount());
                paymentData.setExchangeRate(getPaymentExchangeRate());
                paymentData.setCurrency(new SelectItem(invoiceData.getCurrencyID()));

                if (RECEIVABLE.equals(invoiceData.getType())) {
                    if (checkBox.isCrmAccountCredit()) {
                        paymentData.setType(AccountingConstants.RECEIVABLE_CRM_ACCOUNT_CREDIT);
                    } else if (checkBox.isManualJournal()) {
                        paymentData.setType(AccountingConstants.RECEIVABLE_MANUAL_CREDIT);
                    } else if (checkBox.isBankTransfer()) {
                        paymentData.setType(AccountingConstants.RECEIVABLE_BANKTRANSFER_CREDIT);
                    } else if (checkBox.isPrepayment()) {
                        paymentData.setType(AccountingConstants.RECEIVABLE_PREPAYMENT_SHARE);
                    }
                } else {
                    if (checkBox.isCrmAccountCredit()) {
                        paymentData.setType(AccountingConstants.PAYABLE_CRM_ACCOUNT_CREDIT);
                    } else if (checkBox.isManualJournal()) {
                        paymentData.setType(AccountingConstants.PAYABLE_MANUAL_CREDIT);
                    } else if (checkBox.isPrepayment()) {
                        paymentData.setType(AccountingConstants.PAYABLE_SUPPLIER_CREDIT_SHARE);
                    } else if (checkBox.isBankTransfer()) {
                        paymentData.setType(AccountingConstants.PAYABLE_BANKTRANSFER_CREDIT);
                    } else {
                        paymentData.setType(AccountingConstants.PAYABLE_BANK_CHECK_SHARE);
                    }
                }
                paymentData.setTotal(invoiceData.getTotalInInvoiceCurrency());
                appliedCreditList.add(paymentData);
            }
        }

        if (appliedCreditList.size() == 0) {
            bussy = false;
            Info.show(accountingStrings.thereIsNoCreditToApply(), Info.Type.INFO);
            return;
        }

        if (BigDecimal.ZERO.compareTo(creditLinkProvider.getDueAmount()) > 0) {
            bussy = false;
            Info.show(accountingStrings.amountNotMoreDueAmount(), Info.Type.INFO);
            return;
        }

        LoadingPanel.loading(true);
        ReceivePaymentData appliedCreditData = new ReceivePaymentData();
        appliedCreditData.setPayments(appliedCreditList.toArray(new PaymentData[]{}));

        if (Constants.PAYABLE.equals(invoiceData.getType())) {

            BigDecimal payAmount = BigDecimal.ZERO, baseAmount = BigDecimal.ZERO;
            for (PaymentData bcipData : appliedCreditList) {
                if (AccountingConstants.PAYABLE_BANK_CHECK_SHARE.equals(bcipData.getType())) {
                    payAmount = payAmount.add(bcipData.getPaymentAmount());
                    baseAmount = baseAmount.add(bcipData.getBaseAmount());
                }
            }

            if (payAmount.compareTo(BigDecimal.ZERO) > 0 && baseAmount.compareTo(BigDecimal.ZERO) > 0) {
                PaymentData bankCheckTotalData = new PaymentData();
                bankCheckTotalData.setType(AccountingConstants.PAYABLE_BANK_CHECK_SHARE);
                bankCheckTotalData.setInvoiceID(invoiceData.getID());
                if (invoiceData.getClientID() != null) {
                    bankCheckTotalData.setCrmAccount(new SelectItem(invoiceData.getClientID(), invoiceData.getClientName()));
                }
                bankCheckTotalData.setTotal(invoiceData.getTotalInInvoiceCurrency());
                bankCheckTotalData.setDate(new DateNonConvertable(new Date()));
                bankCheckTotalData.setExchangeRate(getPaymentExchangeRate());

                bankCheckTotalData.setPaymentAmount(payAmount);
                bankCheckTotalData.setBaseAmount(baseAmount);
                appliedCreditData.setBankCheckTotalData(bankCheckTotalData);
            }

            InvoiceService.App.get().applySupplierCreditData(appliedCreditData, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    bussy = false;
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Integer result) {
                    bussy = false;
                    LoadingPanel.loading(false);
                    creditLinkProvider.fireInvoicePaymentChange(invoiceData.getType());
                    Info.show(accountingStrings.creditAppliedSuccessfully(), Info.Type.INFO);
                    PrePaymentAndCreditLink.this.setVisible(false);
                    creditDialogBox.close();
                }
            });
        } else {
            InvoiceService.App.get().saveReceivePaymentData(appliedCreditData, true, new AsyncCallback<BatchPaymentResult>() {
                @Override
                public void onFailure(Throwable throwable) {
                    bussy = false;
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(BatchPaymentResult result) {
                    bussy = false;
                    LoadingPanel.loading(false);
                    creditLinkProvider.fireInvoicePaymentChange(invoiceData.getType());
                    Info.show(accountingStrings.prepaymentAppliedSuccessfully(), Info.Type.INFO);
                    PrePaymentAndCreditLink.this.setVisible(false);
                    creditDialogBox.close();
                }
            });
        }
    }

    /**
     * For the Pre payment we have to use invoice exchange rate not the payment exchange rate
     *
     * @return
     */
    private BigDecimal getPaymentExchangeRate() {
        return invoiceData.getExchageRate().setScale(AccountingUtils.get().getExRateScale(), RoundingMode.HALF_UP);

        /*if (!"".equals(creditLinkProvider.getPaymentExRateTxtBox().getText())) {
            return AccountingUtils.get().parseToBigDecimal(creditLinkProvider.getPaymentExRateTxtBox().getText());
        } else {
            return new BigDecimal("1.00");
        }*/
    }

    public void applyCreditData(PaymentData[] paymentsCredits) {
        creditDialogBox = null;
        this.paymentsCredits = paymentsCredits;
        //setVisible(paymentsCredits != null && paymentsCredits.length > 0);
    }

    public void hideDialogBoxIfOpen() {
        if (creditDialogBox != null && creditDialogBox.isShowing()) {
            creditDialogBox.close();
        }
    }
}
