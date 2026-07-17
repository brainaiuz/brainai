package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnOffsetEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseMarkupWidget;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/4/15
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseMarkupPopup extends KpiSideNavBox implements Constants, AccountingConstants {

    public static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    public static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private Command expensesButtonListener, cancelButtonListener;
    public FlexTable expenseItemTable;
    public ArrayList<BillableExpenseItem> expenseItems;

    //    public KpiModal expenseBox;
    private Label totalWithMarkup;
    public Label taxTotal;
    public TextBox markupAmount;
    public DataListBox markupType;
    private TaxLookUp markupTax;
    public AccountsLookUp markupAccount;
    public BigDecimal markupAmountOrPercent;
    public String baseCurrency;
    public ArrayList<BillableExpenseItem> expenses;
    public boolean isPercent;

    private TotalTable totalTable;

    private WfmButton2 applyExpenseButton;
    public WfmButton2 cancelButton;
    public CheckBox selectAllBox;

    private ArrayList<ExpenseMarkupWidget> markupWidgets;

    private BigDecimal exchangeRate;

    public ExpenseMarkupPopup() {
        super(1200);
        expenses = new ArrayList<>();
        expenseItems = new ArrayList<>();
        init();
    }

    private void init() {
//        expenseBox = new KpiModal();
//        expenseBox.setWidth(1200);
//        expenseBox.setTitle(accountingStrings.billableExpenseAmount());
        Heading header = new Heading(HeadingSize.H1);
        header.setText(accountingStrings.billableExpenseAmount());
        addHeader(header);
        /*start top panel*/
        selectAllBox = new KpiCheckBox();
        selectAllBox.addClickHandler(clickEvent -> selectAllItems());

        markupType = new DataListBox();
        markupType.setWithoutNullLabel(true);
        markupType.setItems(new SelectItem[]{
                new SelectItem(0, "Fixed Amount"),
                new SelectItem(1, "Percentage")
        });
        markupType.setSelected(0);
        markupType.addValueChangeHandler(changeEvent -> {
            if (!markupAmount.getText().isEmpty()) {
                calculate("type");
            }
        });
        markupAmount = new TextBox();
        Validation.addNumericKeyboardListener(markupAmount, AccountingUtils.calculationScale);

        markupAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        markupAmount.addKeyboardListener(new KeyboardListenerAdapter() {
            public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                calculate("markup");
            }
        });

        markupAccount = new AccountsLookUp(REVENUE);
        markupAccount.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> calculate("account"));

        markupTax = new TaxLookUp(PAYABLE);
        markupTax.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> calculate("tax"));

        GRow row = new GRow();
        row.add(new GColumn(new FormGroup(accountingStrings.markupType(), markupType)));
        row.add(new GColumn(new FormGroup(accountingStrings.markupAmountOrPercent(), markupAmount)));
        row.add(new GColumn(new FormGroup(accountingStrings.markupAccount(), markupAccount)));
        row.add(new GColumn(new FormGroup(wfmStrings.taxRate(), markupTax)));
        addBody(row);

        /*end top panel*/
        expenseItemTable = new FlexTable();
        expenseItemTable.setStyleName("flexTable");
        expenseItemTable.setCellPadding(0);
        expenseItemTable.setCellSpacing(0);
        expenseItemTable.setWidget(0, 0, selectAllBox);
        expenseItemTable.setWidget(0, 1, new HTML("<span  style='width:15%'>" + wfmStrings.number() + "</span>"));
        expenseItemTable.setWidget(0, 2, new HTML("<span  style='width:20%'>" + wfmStrings.account() + "</span>"));
        expenseItemTable.setWidget(0, 3, new HTML("<span  style='width:15%'>" + wfmStrings.description() + "</span>"));
        expenseItemTable.setWidget(0, 4, new ExpenseMarkupWidget("<span  style='width:15%'>" + wfmStrings.amount() + "</span>"));
        expenseItemTable.setWidget(0, 5, new HTML("<span  style='width:15%'>" + accountingStrings.markupAmountOrPercent() + "</span>"));
        expenseItemTable.setWidget(0, 6, new HTML("<span  style='width:15%'>" + accountingStrings.totalBeforeTax() + "</span>"));
        expenseItemTable.setWidget(0, 7, new HTML("<span  style='width:15%'>" + accountingStrings.markupAccount() + "</span>"));
        expenseItemTable.setWidget(0, 8, new HTML("<span  style='width:15%'>" + wfmStrings.taxRate() + "</span>"));
        expenseItemTable.getFlexCellFormatter().setStyleName(0, 0, "flexTable-Label");
        expenseItemTable.getFlexCellFormatter().setStyleName(0, 1, "flexTable-Label");
        expenseItemTable.getFlexCellFormatter().setStyleName(0, 2, "flexTable-Label");
        expenseItemTable.getFlexCellFormatter().setStyleName(0, 3, "flexTable-Label");
        expenseItemTable.getFlexCellFormatter().setStyleName(0, 4, "flexTable-Label");
        expenseItemTable.getFlexCellFormatter().setStyleName(0, 5, "flexTable-Label");
        expenseItemTable.getFlexCellFormatter().setStyleName(0, 6, "flexTable-Label");
        expenseItemTable.getFlexCellFormatter().setStyleName(0, 7, "flexTable-Label");
        expenseItemTable.getFlexCellFormatter().setStyleName(0, 8, "flexTable-Label");

        addBody(expenseItemTable);

        totalWithMarkup = new Label(AccountingUtils.getZero());
        taxTotal = new Label(AccountingUtils.getZero());

        totalTable = new TotalTable();
        totalTable.addItem(accountingMessages.totalSelectedWithMarkup(), totalWithMarkup);
        totalTable.addItem(wfmStrings.taxTotal(), taxTotal);

        GRow totalRow = new GRow();
        totalRow.addStyleName("margin-top");

        GColumn column = new GColumn(GColumnEnum.COL_4);
        column.setOffset(GColumnOffsetEnum.OFFSET_8);
        column.add(totalTable);

        totalRow.add(column);
        addBody(totalRow);

        applyExpenseButton = new WfmButton2(wfmStrings.ok());
        applyExpenseButton.addClickHandler(clickEvent -> expensesButtonListener.execute());
        cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        cancelButton.addClickHandler(clickEvent -> cancelButtonListener.execute());

        addFooter(applyExpenseButton);
    }

//    public void open() {
//        show();
//    }

//    public void close() {
//        hide();
//    }

    public void selectAllItems() {
        for (int i = 1; i < expenseItemTable.getRowCount(); i++) {
            KpiCheckBox checkBox = (KpiCheckBox) expenseItemTable.getWidget(i, 0);
            checkBox.setValue(selectAllBox.getValue());
        }
        calculate(null);
    }

    public void clearAllData() {
        totalWithMarkup.setText(ZERO.toString());
        markupAmountOrPercent = ZERO;
        expenses.clear();
        isPercent = false;
        markupAccount.clearAndClearItems();
    }

    public void calculate(String action) {
        clearAndInitMarkupWidgets();
        boolean isPercent = markupType.getSelectedId() == 1;
        markupAmountOrPercent = ZERO;
        BigDecimal totalItem = ZERO, total = ZERO, totalbt = ZERO, tax = ZERO, taxTot = ZERO, markup = ZERO, markupItem = ZERO;

        if (action != null) {
            markupAmountOrPercent = AccountingUtils.get().parseToBigDecimal(markupAmount.getText());
            for (int i = 1; i < expenseItemTable.getRowCount(); i++) {
                KpiCheckBox checkBox = (KpiCheckBox) expenseItemTable.getWidget(i, 0);
                if (!checkBox.isEnabled()) {
                    continue;
                }
                TextBox markupCell = (TextBox) expenseItemTable.getWidget(i, 5);
                ExtendedTaxLookUp taxLookUp = (ExtendedTaxLookUp) expenseItemTable.getWidget(i, 8);
                AccountsLookUp markupAcc = (AccountsLookUp) expenseItemTable.getWidget(i, 7);
                ExpenseMarkupWidget totalCell = (ExpenseMarkupWidget) expenseItemTable.getWidget(i, 4);
                Label totalBeforeTaxCell = (Label) expenseItemTable.getWidget(i, 6);
                total = AccountingUtils.get().parseToBigDecimal(totalCell.getText());

                if ((action.equals("markup") || action.equals("type")) && markupAmountOrPercent != null) {
                    if (!isPercent) {
                        markupCell.setText(AccountingUtils.get().formatPrice(markupAmountOrPercent));
                        totalbt = total.add(markupAmountOrPercent);
                    } else {
                        markup = total.multiply(markupAmountOrPercent.divide(HUNDRED, 18, RoundingMode.HALF_UP));
                        markupCell.setText(AccountingUtils.get().formatPrice(markup));
                        totalbt = total.add(markup);
                    }
                } else {
                    totalbt = AccountingUtils.get().parseToBigDecimal(totalBeforeTaxCell.getText());
                }
                if (action.equals("account")) {
                    markupAcc.setSelected(markupAccount.getSelectedData());
                } else if (action.equals("tax")) {
                    taxLookUp.addTaxItem(markupTax.getSelectedData());
                }
                totalBeforeTaxCell.setText(AccountingUtils.get().formatPrice(totalbt));
            }
        }

        total = ZERO;
        for (int i = 1; i < expenseItemTable.getRowCount(); i++) {
            KpiCheckBox checkBox = (KpiCheckBox) expenseItemTable.getWidget(i, 0);

            if (checkBox != null && checkBox.getValue()) {
                Label totalBeforeTaxCell = (Label) expenseItemTable.getWidget(i, 6);
                TextBox markupCell = (TextBox) expenseItemTable.getWidget(i, 5);
                ExpenseMarkupWidget totalCell = (ExpenseMarkupWidget) expenseItemTable.getWidget(i, 4);
                ExtendedTaxLookUp taxLookUp = (ExtendedTaxLookUp) expenseItemTable.getWidget(i, 8);

                totalItem = AccountingUtils.get().parseToBigDecimal(totalCell.getText());
                markupItem = AccountingUtils.get().parseToBigDecimal(markupCell.getText());

                totalbt = totalItem.add(markupItem);
                totalBeforeTaxCell.setText(AccountingUtils.get().formatPrice(totalbt));
                total = total.add(totalbt);

                if (taxLookUp.getSelectedItemID() != null && taxLookUp.getSelectedData() != null) {
                    TaxItem taxItem = taxLookUp.getSelectedData();
                    tax = totalbt.multiply(taxItem.getEffectiveTaxPercent().divide(HUNDRED, 18, RoundingMode.HALF_UP));
                    taxLookUp.setTaxAmount(tax);
                    taxTot = taxTot.add(tax);
                    totalCell.setEffectiveTaxPercent(taxItem.getEffectiveTaxPercent());
                } else {
                    taxLookUp.setTaxAmount(BigDecimal.ZERO);
                    totalCell.setEffectiveTaxPercent(null);
                }
                totalCell.setMarkupAmount(markupItem, exchangeRate);
                markupWidgets.add(totalCell);
            }
        }
        totalWithMarkup.setText(AccountingUtils.get().formatPrice(total));
        taxTotal.setText(AccountingUtils.get().formatPrice(taxTot));
    }

    public void onCurrencyChange(Integer currencyId, BigDecimal exchangeRate) {

        for (int i = 1; i < expenseItemTable.getRowCount(); i++) {
            HTML totalBeforeTaxCell = (HTML) expenseItemTable.getWidget(i, 6);
            TextBox markupCell = (TextBox) expenseItemTable.getWidget(i, 5);
            ExpenseMarkupWidget totalCell = (ExpenseMarkupWidget) expenseItemTable.getWidget(i, 4);

            BillableExpenseItem exp = totalCell.getBillableExpenseItem();

            if (exp != null) {
                BigDecimal amount = currencyId.equals(exp.getCurrencyID()) ? exp.getAmountInCurrency() : exp.getAmountInBase().multiply(exchangeRate);
                BigDecimal markUpAmount = exp.getMarkupAmountInBase() != null ? exp.getMarkupAmountInBase().multiply(exchangeRate) : BigDecimal.ZERO;
                BigDecimal totalBeforeTax = amount.add(markUpAmount);

                markupCell.setText(AccountingUtils.get().formatPrice(markUpAmount));
                totalCell.setText(AccountingUtils.get().formatPrice(amount));
                totalBeforeTaxCell.setText(AccountingUtils.get().formatPrice(totalBeforeTax));
            }
        }

        this.exchangeRate = exchangeRate;
        calculate(null);
    }

    public boolean validate() {
        expenses.clear();
        for (int i = 1; i < expenseItemTable.getRowCount(); i++) {
            KpiCheckBox checkBox = (KpiCheckBox) expenseItemTable.getWidget(i, 0);
            TextBox markupCell = (TextBox) expenseItemTable.getWidget(i, 5);
            AccountsLookUp markupAccount = (AccountsLookUp) expenseItemTable.getWidget(i, 7);
            ExtendedTaxLookUp taxLookUp = (ExtendedTaxLookUp) expenseItemTable.getWidget(i, 8);

            if (checkBox.getValue()) {
                BillableExpenseItem exp = expenseItems.get(i - 1);
                expenses.add(exp);

                exp.setMarkupTaxAmount(taxLookUp.getTaxAmount());
                exp.setMarkupTax(taxLookUp.getSelectedData());

                if (markupAccount.getSelectedItemID() != null) {
                    exp.setMarkupAmount(AccountingUtils.get().parseToBigDecimal(markupCell.getText()));
                    exp.setMarkupAccount(markupAccount.getSelectedItem());
                } else {
                    exp.setMarkupAmount(null);
                    exp.setMarkupAccount(null);
                    BigDecimal value = AccountingUtils.get().parseToBigDecimal(markupCell.getText());

                    if (ZERO.compareTo(value) != 0) {
                        return Validation.validateLookUpRequired(markupAccount);
                    }
                }
            }
        }

        return true;
    }

    private void clearAndInitMarkupWidgets() {

        if (markupWidgets == null) {
            markupWidgets = new ArrayList<>();
        }
        markupWidgets.clear();
    }

    public void setValues(List<BillableExpenseItem> expenseItems, Integer currencyId, BigDecimal exchangeRate) {

        this.expenseItems.clear();
        clearAndInitMarkupWidgets();

        for (int j = 1; j < expenseItemTable.getRowCount(); j++) {
            expenseItemTable.removeRow(j);
        }
        if (expenseItems == null || expenseItems.isEmpty()) {
            return;
        }
        this.expenseItems.addAll(expenseItems);
        this.exchangeRate = exchangeRate;

        boolean calculate = false;

        int index = 1;

        for (BillableExpenseItem exp : expenseItems) {
            KpiCheckBox checkBox = new KpiCheckBox();
            checkBox.addClickHandler(clickEvent -> calculate(null));

            if (exp.isSelected()) {
                calculate = true;
                checkBox.setValue(true);
                expenses.add(exp);
            }
            AccountsLookUp itemMarkupAccount = new AccountsLookUp(REVENUE);

            ExtendedTaxLookUp taxLookUp = new ExtendedTaxLookUp(PAYABLE);
            taxLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> calculate(null));
            taxLookUp.getSuggestBox().addKeyUpHandler(e -> calculate(null));
            taxLookUp.setWidth(NORMAL_WIDTH);

            TextBox markup = new TextBox();
            Validation.addNumericKeyboardListener(markup, AccountingUtils.calculationScale);
            markup.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            markup.setWidth(SHORT_WIDTH);
            markup.addKeyboardListener(new KeyboardListenerAdapter() {
                public void onKeyUp(Widget sender, char keyCode, int modifiers) {
                    calculate(null);
                }
            });

            BigDecimal amount = currencyId.equals(exp.getCurrencyID()) ? exp.getAmountInCurrency() : exp.getAmountInBase().multiply(exchangeRate);
            BigDecimal markUpAmount = currencyId.equals(exp.getCurrencyID()) ? exp.getMarkupAmount() : exp.getMarkupAmountInBase().multiply(exchangeRate);
            BigDecimal totalBeforeTax = amount.add(markUpAmount);

            markup.setText(AccountingUtils.get().formatPrice(markUpAmount));

            ExpenseMarkupWidget markupWidget = new ExpenseMarkupWidget(AccountingUtils.get().formatPrice(amount));
            markupWidget.setBillableExpenseItem(exp);

            if (exp.getMarkupAccount() != null) {
                itemMarkupAccount.addItem(exp.getMarkupAccount());
            }
            if (exp.getMarkupTax() != null) {
                taxLookUp.addTaxItem(exp.getMarkupTax());
            }
            HTML accountHtml = new HTML(exp.getAccount() != null ? exp.getAccount().getName() : "");
            accountHtml.setTitle(String.valueOf(exp.getAccount().getId()));
            accountHtml.setWidth(MIN_DEFAULT_WIDTH);
            HTML descriptionHtml = new HTML(exp.getDescription());
            descriptionHtml.setWidth(MAX_DEFAULT_WIDTH);

            expenseItemTable.setWidget(index, 0, checkBox);
            expenseItemTable.setWidget(index, 1, new HTML(exp.getNumber()));
            expenseItemTable.setWidget(index, 2, accountHtml);
            expenseItemTable.setWidget(index, 3, descriptionHtml);
            expenseItemTable.setWidget(index, 4, markupWidget);
            expenseItemTable.setWidget(index, 5, markup);
            expenseItemTable.setWidget(index, 6, new HTML(AccountingUtils.get().formatPrice(totalBeforeTax)));
            expenseItemTable.setWidget(index, 7, itemMarkupAccount);
            expenseItemTable.setWidget(index, 8, taxLookUp);
            expenseItemTable.getFlexCellFormatter().setStyleName(index, 0, "flexTable-td");
            expenseItemTable.getFlexCellFormatter().setStyleName(index, 1, "flexTable-td");
            expenseItemTable.getFlexCellFormatter().setStyleName(index, 2, "flexTable-td");
            expenseItemTable.getFlexCellFormatter().setStyleName(index, 3, "flexTable-td");
            expenseItemTable.getFlexCellFormatter().setStyleName(index, 4, "flexTable-td");
            expenseItemTable.getFlexCellFormatter().setStyleName(index, 5, "flexTable-td");
            expenseItemTable.getFlexCellFormatter().setStyleName(index, 6, "flexTable-td");
            expenseItemTable.getFlexCellFormatter().setStyleName(index, 7, "flexTable-td");
            expenseItemTable.getFlexCellFormatter().setStyleName(index, 8, "flexTable-td");
            index++;
        }

        if (calculate) {
            calculate(null);
        }
    }

    public ArrayList<NewInvoiceItem> getExpanseItemsAsLineItems() {
        ArrayList<NewInvoiceItem> invoiceItems = new ArrayList<>();
        expenses.clear();
        for (int i = 1; i < expenseItemTable.getRowCount(); i++) {
            KpiCheckBox checkBox = (KpiCheckBox) expenseItemTable.getWidget(i, 0);
            HTML number = (HTML) expenseItemTable.getWidget(i, 1);
            HTML account = (HTML) expenseItemTable.getWidget(i, 2);
            HTML description = (HTML) expenseItemTable.getWidget(i, 3);
            // mark up amount and markup account is seperated, thats why we need only exact amount
            ExpenseMarkupWidget amount = (ExpenseMarkupWidget) expenseItemTable.getWidget(i, 4);
            TextBox markupCell = (TextBox) expenseItemTable.getWidget(i, 5);
            AccountsLookUp subMarkupAccount = (AccountsLookUp) expenseItemTable.getWidget(i, 7);
            ExtendedTaxLookUp taxLookUp = (ExtendedTaxLookUp) expenseItemTable.getWidget(i, 8);


            if (checkBox.getValue()) {
                BillableExpenseItem exp = expenseItems.get(i - 1);
                expenses.add(exp);

                NewInvoiceItem invoiceItem = new NewInvoiceItem();
                invoiceItem.setID(exp.getObjectID());
                invoiceItem.setFullItemName(account.getText());
                invoiceItem.setItemName(account.getText());
                invoiceItem.setDescription(description.getText() + " -> " + number.getText());
                invoiceItem.setQuantity(BigDecimal.ONE);
                invoiceItem.setUnitPrice(BigDecimal.valueOf(Utils.universalParse(NumberFormat.getFormat(",##0.#"), amount.getText())));
                invoiceItem.setAccountItem(subMarkupAccount.getSelectedData() != null ? subMarkupAccount.getSelectedData() : markupAccount.getSelectedData());
                invoiceItem.setTaxItem(taxLookUp.getSelectedData() != null ? taxLookUp.getSelectedData() : null);
                invoiceItem.setExpanceItemId(exp.getObjectID());
                invoiceItems.add(invoiceItem);
                // if it has markup it will create another line item for inovie
                if (markupCell.getValue() != null && !markupCell.getValue().isEmpty() && subMarkupAccount.getSelectedData() != null) {
                    NewInvoiceItem markupItem = new NewInvoiceItem();
                    markupItem.setID(invoiceItem.getID());
                    markupItem.setFullItemName(invoiceItem.getFullItemName());
                    markupItem.setItemName(invoiceItem.getItemName());
                    markupItem.setDescription(invoiceItem.getDescription() + " ( MARKUP ) ");
                    markupItem.setQuantity(invoiceItem.getQuantity());
                    markupItem.setUnitPrice(BigDecimal.valueOf(Utils.universalParse(NumberFormat.getFormat(",##0.#"), markupCell.getText())));
                    markupItem.setAccountItem(subMarkupAccount.getSelectedData());
                    markupItem.setTaxItem(invoiceItem.getTaxItem());
                    markupItem.setExpanceItemId(invoiceItem.getExpanceItemId());

                    invoiceItems.add(markupItem);
                }

                enableColumns(checkBox.getValue(), i);
            }
        }
        return invoiceItems;
    }

    public void enableColumns(Boolean isEnable, Integer rowId) {
        KpiCheckBox checkBox = (KpiCheckBox) expenseItemTable.getWidget(rowId, 0);
        TextBox markupCell = (TextBox) expenseItemTable.getWidget(rowId, 5);
        AccountsLookUp subMarkupAccount = (AccountsLookUp) expenseItemTable.getWidget(rowId, 7);
        ExtendedTaxLookUp taxLookUp = (ExtendedTaxLookUp) expenseItemTable.getWidget(rowId, 8);
        checkBox.setEnabled(!isEnable);
        markupCell.setEnabled(!isEnable);
        subMarkupAccount.setEnabled(!isEnable);
        taxLookUp.setEnabled(!isEnable);
    }

    public void changeCheckBoxValue(Integer expId, Boolean isChange) {
        AtomicInteger index = new AtomicInteger();
        BillableExpenseItem exp = expenseItems.stream()
                .peek(item -> index.getAndIncrement())
                .filter(item -> (item != null && item.getObjectID() != null && item.getObjectID().equals(expId)))
                .findFirst().orElse(null);
        if (exp != null) {
            KpiCheckBox checkBox = (KpiCheckBox) expenseItemTable.getWidget(index.get(), 0);
            ExpenseMarkupWidget totalCell = (ExpenseMarkupWidget) expenseItemTable.getWidget(index.get(), 4);
            TextBox markupCell = (TextBox) expenseItemTable.getWidget(index.get(), 5);
            HTML totalAmount = (HTML) expenseItemTable.getWidget(index.get(), 6);
            AccountsLookUp subMarkupAccount = (AccountsLookUp) expenseItemTable.getWidget(index.get(), 7);
            ExtendedTaxLookUp taxLookUp = (ExtendedTaxLookUp) expenseItemTable.getWidget(index.get(), 8);
            if (isChange) {
                // After deleted SI line item, need to clear it from expinse midget also.
                checkBox.setValue(Boolean.FALSE);
                markupCell.setValue("0.00");
                totalAmount.setText(totalCell.getText());
                subMarkupAccount.clear();
                taxLookUp.clear();
                enableColumns(Boolean.FALSE, index.get());
            } else {
                checkBox.setValue(Boolean.TRUE);
                enableColumns(Boolean.TRUE, index.get());
            }
            calculate(null);
            expenses.remove(exp);
        }
    }

    public void setExpensesButtonListener(Command expensesButtonListener) {
        this.expensesButtonListener = expensesButtonListener;
    }

    public void setCancelButtonListener(Command cancelButtonListener) {
        this.cancelButtonListener = cancelButtonListener;
    }

    public BigDecimal getTotalWithMarkup() {
        return AccountingUtils.get().parseToBigDecimal(totalWithMarkup.getText());
    }

    public BigDecimal getTaxTotal() {
        return AccountingUtils.get().parseToBigDecimal(taxTotal.getText());
    }

    public ArrayList<BillableExpenseItem> getExpenses() {
        return expenses;
    }

    public class ExtendedTaxLookUp extends TaxLookUp {
        public BigDecimal taxAmount;

        public ExtendedTaxLookUp(String type) {
            super(type);
            taxAmount = ZERO;
        }

        public BigDecimal getTaxAmount() {
            return taxAmount;
        }

        public void setTaxAmount(BigDecimal taxAmount) {
            this.taxAmount = taxAmount;
        }
    }

    public ArrayList<ExpenseMarkupWidget> getMarkupWidgets() {
        return markupWidgets;
    }
}
