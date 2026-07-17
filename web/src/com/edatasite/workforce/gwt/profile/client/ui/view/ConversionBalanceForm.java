package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ConversionBalanceItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_JOURNAL_REPORT;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.STYLE_TOTAL_LABEL;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.STYLE_TOTAL_VALUE;

public class ConversionBalanceForm extends CustomForm implements Colapse, FittedContent, Constants, AccountingConstants {

    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private final DateTimeFormat format_month = DateTimeFormat.getFormat("MMMM");
    private final DateTimeFormat format_year = DateTimeFormat.getFormat("yyyy");
    private Date journalDate;
    private Integer journalId;
    private HTML notice, differenceBalance, debitTotalHTML, creditTotalHTML;
    private BigDecimal debitTotal = ZERO;
    private BigDecimal creditTotal = ZERO;
    private WfmButton2 saveButton;
    private EditableTable dynamicTable;
    private DataListBox month, year;
    private ConversionBalanceItem conversionBalanceItem;

    public ConversionBalanceForm() {
        super("conversionBalance", wfmStrings.conversionBalance());
    }


    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        month = new DataListBox();
        month.setWithoutNullLabel(true);
        setMonthItems();

        year = new DataListBox();
        year.setWithoutNullLabel(true);
        setYearItems();

        createDynamicTable();

        debitTotalHTML = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        creditTotalHTML = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        debitTotalHTML.setStyleName(STYLE_TOTAL_VALUE);
        creditTotalHTML.setStyleName(STYLE_TOTAL_VALUE);

        HTML debitLabel = new HTML(wfmStrings.debit());
        HTML creditLabel = new HTML(wfmStrings.credit());
        HTML totalLabel = new HTML(wfmStrings.total());

        debitLabel.setStyleName(STYLE_TOTAL_LABEL);
        creditLabel.setStyleName(STYLE_TOTAL_LABEL);
        totalLabel.setStyleName(STYLE_TOTAL_LABEL);

        TotalTable totalsTable = new TotalTable();
        totalsTable.addWidgetsInARow(new HTML(""), debitLabel, creditLabel);
        totalsTable.addWidgetsInARow(totalLabel, debitTotalHTML, creditTotalHTML);

        for (int i = 0; i < 3; i++) {
            dynamicTable.addRow(getWidgets(null));
        }

        notice = new HTML("");


        differenceBalance = new HTML("<b>" + wfmStrings.openingBalance() + "</b>" + "-" + wfmStrings.conversionBalanceDifference());

        ActionButton showAllAccounts = new ActionButton("", "btn btn--new btn--circle");
        showAllAccounts.add(new SvgIcon(SvgEnum.plus));

        showAllAccounts.addClickHandler(valueChangeEvent -> {
            Set<Integer> selectedAccountIds = new HashSet<>();
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setAllByFilter(false);
            filterParameter.setLimit(1000);
            AccountingService.App.get().getAccountsForInvoice(filterParameter, null, new AsyncCallback<AccountItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(AccountItem[] accountItems) {
                    if (accountItems != null && accountItems.length > 0) {
                        for (int i = 0; i < dynamicTable.getRowCount(); i++) {
                            AccountsLookUp account = (AccountsLookUp) dynamicTable.getColumnById(i, ItemTableConstants.ACCOUNT);
                            if (account != null && account.getSelectedData() != null) {
                                selectedAccountIds.add(account.getSelectedData().getId());
                            }
                        }

                        for (AccountItem accountItem : accountItems) {
                            if (accountItem != null && !selectedAccountIds.contains(accountItem.getId())) {
                                TransactionItem transactionItem = new TransactionItem();
                                transactionItem.setAccountId(accountItem.getId());
                                transactionItem.setAccountName(accountItem.getName());
                                transactionItem.setAccountCode(accountItem.getCode());

                                dynamicTable.addRow(getWidgets(transactionItem));
                            }
                        }
                    }
                }
            });
        });

        ActionButton removeZeroBalances = new ActionButton("", "btn btn--new btn--circle");
        removeZeroBalances.add(new SvgIcon(SvgEnum.trash2));
        removeZeroBalances.addClickHandler(valueChangeEvent -> {
            LinkedList<TransactionItem> nonZeroAccounts = new LinkedList<>();
            for (int i = 0; i < dynamicTable.getRowCount(); i++) {
                AccountsLookUp account = (AccountsLookUp) dynamicTable.getColumnById(i, ItemTableConstants.ACCOUNT);
                CustomCellTextBox debit = (CustomCellTextBox) dynamicTable.getColumnById(i, ItemTableConstants.DEBIT);
                CustomCellTextBox credit = (CustomCellTextBox) dynamicTable.getColumnById(i, ItemTableConstants.CREDIT);
                if (account != null && account.getSelectedData() != null && (debit != null || credit != null)) {
                    BigDecimal debitAmount = BigDecimal.ZERO;
                    if (debit != null) {
                        debitAmount = AccountingUtils.get().parseToBigDecimal(debit.getText());
                    }
                    BigDecimal creditAmount = BigDecimal.ZERO;
                    if (credit != null) {
                        creditAmount = AccountingUtils.get().parseToBigDecimal(credit.getText());
                    }
                    if (debitAmount.compareTo(BigDecimal.ZERO) != 0 || creditAmount.compareTo(BigDecimal.ZERO) != 0 || ("100".equals(account.getSelectedData().getCode()) || "2100".equals(account.getSelectedData().getCode()) || "3201".equals(account.getSelectedData().getCode()))) {
                        TransactionItem transactionItem = new TransactionItem();
                        transactionItem.setAccountId(account.getSelectedData().getId());
                        transactionItem.setAccountName(account.getSelectedData().getName());
                        transactionItem.setAccountCode(account.getSelectedData().getCode());
                        transactionItem.setDebit(debitAmount);
                        transactionItem.setCredit(creditAmount);
                        nonZeroAccounts.add(transactionItem);
                    }
                }
            }
            dynamicTable.removeAllRows();
            if (nonZeroAccounts != null && !nonZeroAccounts.isEmpty()) {
                for (TransactionItem transactionItem : nonZeroAccounts) {
                    dynamicTable.addRow(getWidgets(transactionItem));
                }
            } else {
                dynamicTable.addRow(getWidgets(null));
            }

        });

        addTitleField(GENERAL_INFORMATION, wfmStrings.generalInformation());
        addField(PERIOD, month, getTitle(wfmStrings.period(), true));
        addField(Constants.WIDGET_DATE_YEAR, year, getTitle(wfmStrings.year(), true));
        addField(NOTE, notice, getTitle(wfmStrings.notice()));
        addField(_TITLE, differenceBalance, null);
        addField(SHOW_ACCOUNTING, showAllAccounts, getTitle(accountingStrings.showAllAccounts()));
        addField(AMOUNT, removeZeroBalances, getTitle(wfmStrings.removeZeroBalances()));
        addField("ITEMS_TABLE", dynamicTable, null);
        addField("totalTable", totalsTable, null);
        show();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONVERSION_BALANCE_RELOAD_PAGE, ConversionBalanceForm.this, (sender, args) -> {
            Window.open(Utils.getPathName(), "_self", "");
        });

    }


    private void createDynamicTable() {

        dynamicTable = new EditableTable(getColumns(), false, false);
        dynamicTable.setDraggable(false);
        dynamicTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                dynamicTable.addRow(getWidgets(null));
            }

            @Override
            public void removeRow() {
                calculateTotal();
            }
        });
    }


    private ColumnConfig[] getColumns() {
        ArrayList<ColumnConfig> columns = new ArrayList<>();
        columns.add(new ColumnConfig(LookUpCell.class, ItemTableConstants.ACCOUNT, wfmStrings.account(), 400, true));
        columns.add(new ColumnConfig(CustomCell.class, ItemTableConstants.DEBIT, wfmStrings.debit(), 150, true));
        columns.add(new ColumnConfig(CustomCell.class, ItemTableConstants.CREDIT, wfmStrings.credit(), 150, true));
        return columns.toArray(new ColumnConfig[columns.size()]);
    }


    private Widget[] getWidgets(TransactionItem data) {
        AccountsLookUp accountsLookUp = new AccountsLookUp(null);
        accountsLookUp.ensureDebugId("account");
        if (data != null && data.getAccountId() != null) {
            accountsLookUp.addAccountItem(new AccountItem(data.getAccountId(), data.getAccountCode(), data.getAccountName()));
        }
        accountsLookUp.setAutocompleteOff();

        CustomCellTextBox credit = new CustomCellTextBox();
        CustomCellTextBox debit = new CustomCellTextBox();
        debit.ensureDebugId("debit");
        debit.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        debit.setMaxLength(16);
        debit.setText(AccountingUtils.getZero());
        Validation.addNumericKeyboardListener(debit, AccountingUtils.calculationScale, true, true);
        addFocusListener(debit, AccountingUtils.getZero(), accountsLookUp);

        if (data != null && data.getDebit() != null) {
            debit.setText(AccountingUtils.get().formatPrice(data.getDebit()));
        }
        debit.addKeyDownHandler(keyDownEvent -> {
            if (!(accountsLookUp.getSelectedData() != null && ("100".equals(accountsLookUp.getSelectedData().getCode()) || "2100".equals(accountsLookUp.getSelectedData().getCode()) || "3201".equals(accountsLookUp.getSelectedData().getCode())))) {
                credit.setText(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
                dynamicTable.refreshCustomCellDisplayValue(dynamicTable.getGrid().getCurrentRow(), ItemTableConstants.CREDIT);
            }
        });

        credit.ensureDebugId("credit");
        credit.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        credit.setText(AccountingUtils.getZero());
        credit.setMaxLength(16);
        Validation.addNumericKeyboardListener(credit, AccountingUtils.calculationScale, true, true);
        addFocusListener(credit, AccountingUtils.getZero(), accountsLookUp);


        if (data != null && data.getCredit() != null) {
            credit.setText(AccountingUtils.get().formatPrice(data.getCredit()));
        }
        credit.addKeyDownHandler(keyDownEvent -> {
            if (!(accountsLookUp.getSelectedData() != null && ("100".equals(accountsLookUp.getSelectedData().getCode()) || "2100".equals(accountsLookUp.getSelectedData().getCode()) || "3201".equals(accountsLookUp.getSelectedData().getCode())))) {
                debit.setText(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
                dynamicTable.refreshCustomCellDisplayValue(dynamicTable.getGrid().getCurrentRow(), ItemTableConstants.DEBIT);
            }
        });

        accountsLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
            calculateTotal();
            if (accountsLookUp.getSelectedData() != null && ("100".equals(accountsLookUp.getSelectedData().getCode()) || "2100".equals(accountsLookUp.getSelectedData().getCode()) || "3201".equals(accountsLookUp.getSelectedData().getCode()))) {
                credit.setEnabled(false);
                debit.setEnabled(false);
            } else {
                credit.setEnabled(true);
                debit.setEnabled(true);
            }
        });
        if ((accountsLookUp.getSelectedData() != null && ("100".equals(accountsLookUp.getSelectedData().getCode()) || "2100".equals(accountsLookUp.getSelectedData().getCode()) || "3201".equals(accountsLookUp.getSelectedData().getCode())))) {
//            dynamicTable.setShowRemoveCell(false);
            accountsLookUp.setEnabled(false);
            credit.setEnabled(false);
            debit.setEnabled(false);
        } else {
//            dynamicTable.setShowRemoveCell(true);
        }

        return new Widget[]{accountsLookUp, debit, credit};

    }

    @Override
    protected void addButtons() {

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(saveButton);
    }

    @Override
    protected void getDataToFillFields() {
        AccountingService.App.get().getConversionBalanceItem(new AbstractAsyncCallback<ConversionBalanceItem>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(ConversionBalanceItem result) {
                super.success(result);
                conversionBalanceItem = result;
                journalId = conversionBalanceItem.getJournalId();
                if (conversionBalanceItem.getJournalDate() != null) {
                    Date date = DateUtil.addDays(conversionBalanceItem.getJournalDate(), 1);
                    month.setSelected(date.getMonth());
                    year.setSelected(Integer.valueOf(format_year.format(date)));
                }
                journalDate = DateUtil.getMonthLastDate(DateUtil.addMonths(new Date(year.getSelectedId() - 1900, month.getSelectedId(), 1), -1));

                notice.setHTML(wfmMessages.conversionBalanceDate(DateUtils.preiewFormat(journalDate)));

                if (conversionBalanceItem != null && conversionBalanceItem.getJournalId() != null && Utils.hasPermission(ACCOUNTING_JOURNAL_REPORT)) {
                    FooterInformer showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
                    showJournal.addClickHandler(clickEvent -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + conversionBalanceItem.getJournalId(), accountingStrings.reportView() + ": " + wfmStrings.conversionBalance(), accountingStrings.reportView() + ": " + wfmStrings.conversionBalance());
                    });
                    showJournal.setBadgeCount(1);

                    footer.addToLeftSide(showJournal);
                }

                FooterInformer customerImport = new FooterInformer(SvgEnum.downloadCloud, accountingStrings.customerBalances(), null);
                ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.CUSTOMER, null);
                customerImport.addClickHandler(clickEvent -> {
                    imp.open();
                });
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        goTo("importclient|add/add/" + imp.getObjectId() + "/" + DateUtils.formatInternal1(journalDate));
                    }
                });

                footer.addToLeftSide(customerImport);


                FooterInformer supplierImport = new FooterInformer(SvgEnum.downloadCloud, accountingStrings.supplierBalances(), null);
                ImportFilePopUp impSup = new ImportFilePopUp(ImportTypeEnum.CUSTOMER, null);
                supplierImport.addClickHandler(clickEvent -> {
                    impSup.open();
                });
                impSup.setSubmitCompleted(() -> {
                    if (impSup.getObjectId() != null) {
                        goTo("importsupplier|add/add/" + impSup.getObjectId() + "/" + DateUtils.formatInternal1(journalDate));
                    }
                });

                footer.addToLeftSide(supplierImport);


                dynamicTable.removeAllRows();
                if (conversionBalanceItem.getItems() != null && conversionBalanceItem.getItems().length > 0) {
                    for (int i = 0; i < conversionBalanceItem.getItems().length; i++) {
                        dynamicTable.addRow(getWidgets(conversionBalanceItem.getItems()[i]));
                    }
                    if (conversionBalanceItem.getItems().length < 3) {
                        for (int i = conversionBalanceItem.getItems().length; i < 3; i++) {
                            dynamicTable.addRow(getWidgets(null));
                        }
                    }
                    calculateTotal();
                }
            }
        });
    }

    private void calculateTotal() {
        debitTotal = ZERO;
        creditTotal = ZERO;
        //Debit
        for (int i = 0; i < dynamicTable.getRowCount(); i++) {
            CustomCellTextBox debit = (CustomCellTextBox) dynamicTable.getColumnById(i, ItemTableConstants.DEBIT);
            if (!"".equals(debit.getText())) {
                BigDecimal debitValue = AccountingUtils.get().parseToBigDecimal(debit.getText());
                debitTotal = debitTotal.add(debitValue);
            }
        }
        //Credit
        for (int i = 0; i < dynamicTable.getRowCount(); i++) {
            CustomCellTextBox credit = (CustomCellTextBox) dynamicTable.getColumnById(i, ItemTableConstants.CREDIT);
            if (!"".equals(credit.getText())) {
                BigDecimal creditValue = AccountingUtils.get().parseToBigDecimal(credit.getText());
                creditTotal = creditTotal.add(creditValue);
            }
        }
        debitTotalHTML.setHTML(AccountingUtils.get().formatPrice(debitTotal));
        creditTotalHTML.setHTML(AccountingUtils.get().formatPrice(creditTotal));
        BigDecimal difference = debitTotal.subtract(creditTotal);
        differenceBalance.setHTML("<b>" + wfmStrings.openingBalance() + "</b>" + "-" + wfmStrings.conversionBalanceDifference() + " (" + AccountingUtils.get().formatPrice(difference) + ")");
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CONVERSION_BALANCE;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
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

    private void save() {
        saveButton.setEnabled(false);
        if (!validate()) {
            saveButton.setEnabled(true);
            return;
        }
        conversionBalanceItem.setJournalDate(journalDate);
        conversionBalanceItem.setJournalId(journalId);
        conversionBalanceItem.setPostedDate(new Date());
        conversionBalanceItem.setTotalDebit(debitTotal);
        conversionBalanceItem.setTotalCredit(creditTotal);

        LinkedList<TransactionItem> transactionItems = new LinkedList<>();
        for (int i = 0; i < dynamicTable.getRowCount(); i++) {

            TransactionItem transactionItem = new TransactionItem();
            AccountsLookUp account = (AccountsLookUp) dynamicTable.getColumnById(i, ItemTableConstants.ACCOUNT);
            CustomCellTextBox debit = (CustomCellTextBox) dynamicTable.getColumnById(i, ItemTableConstants.DEBIT);
            CustomCellTextBox credit = (CustomCellTextBox) dynamicTable.getColumnById(i, ItemTableConstants.CREDIT);
            if (account != null && account.getSelectedData() != null && (ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(debit.getText())) < 0 || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(credit.getText())) < 0)
                    || (account != null && account.getSelectedData() != null && ("100".equals(account.getSelectedData().getCode()) || "2100".equals(account.getSelectedData().getCode()) || "3201".equals(account.getSelectedData().getCode())))) {
                transactionItem.setAccountId(account.getSelectedData().getId());
                transactionItem.setAccountCode(account.getSelectedData().getCode());
                transactionItem.setAccountName(account.getSelectedData().getName());
                if (ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(debit.getText())) < 0) {
                    transactionItem.setDebit(AccountingUtils.get().parseToBigDecimal(debit.getText()));
                }
                if (ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(credit.getText())) < 0) {
                    transactionItem.setCredit(AccountingUtils.get().parseToBigDecimal(credit.getText()));
                }
                transactionItems.add(transactionItem);
            }
        }
        conversionBalanceItem.setItems(transactionItems.toArray(new TransactionItem[]{}));

        AccountingService.App.get().saveAccountBalances(conversionBalanceItem, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(Void result) {
                super.success(result);
                saveButton.setEnabled(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.account()), Info.Type.INFO);
                Window.open(Utils.getPathName(), "_self", "");
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        if (journalDate != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(journalDate)) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.conversionBalance(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            year.addStyleName(ERROR_FORM_STYLE);
            month.addStyleName(ERROR_FORM_STYLE);
            return false;
        }

        boolean errorFound = true;
        if (creditTotal != null && creditTotal.compareTo(BigDecimal.ZERO) > 0) {
            errorFound = false;
        }

        if (debitTotal != null && debitTotal.compareTo(BigDecimal.ZERO) > 0) {
            errorFound = false;
        }

        if (errorFound) {
            AccountsLookUp accountsLookUp = (AccountsLookUp) dynamicTable.getColumnById(0, ItemTableConstants.ACCOUNT);
            CustomCellTextBox debit = (CustomCellTextBox) dynamicTable.getColumnById(0, ItemTableConstants.DEBIT);
            CustomCellTextBox credit = (CustomCellTextBox) dynamicTable.getColumnById(0, ItemTableConstants.CREDIT);

            if (!Validation.validateLookUpRequired(accountsLookUp)) {
                dynamicTable.notValid(0, ItemTableConstants.ACCOUNT);
            }
            boolean db = ("".equals(debit.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(debit.getText())) == 0);
            boolean cr = ("".equals(credit.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(credit.getText())) == 0);
            if (db && cr || !("100".equals(accountsLookUp.getSelectedData().getCode()) || "2100".equals(accountsLookUp.getSelectedData().getCode()) || "3201".equals(accountsLookUp.getSelectedData().getCode()))) {
                dynamicTable.notValid(0, ItemTableConstants.CREDIT);
                dynamicTable.notValid(0, ItemTableConstants.DEBIT);
            }
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }

        HashMap<Integer, Integer> selectedAccounts = new HashMap<>();
        int someAccounts = 0;
        for (int i = 0; i < dynamicTable.getRowCount(); i++) {
            dynamicTable.resetValidation(i);
            AccountsLookUp accountsLookUp = (AccountsLookUp) dynamicTable.getColumnById(i, ItemTableConstants.ACCOUNT);
            if (accountsLookUp != null && accountsLookUp.getSelectedData() != null) {
                if (selectedAccounts.get(accountsLookUp.getSelectedData().getId()) != null) {
                    dynamicTable.notValid(selectedAccounts.get(accountsLookUp.getSelectedData().getId()), ItemTableConstants.ACCOUNT);
                    dynamicTable.notValid(i, ItemTableConstants.ACCOUNT);
                    Utils.scrollIntoView(accountsLookUp.getElement());
                    someAccounts++;
                } else {
                    selectedAccounts.put(accountsLookUp.getSelectedData().getId(), i);
                }
            }
        }

        if (someAccounts > 0) {
            Info.show(wfmStrings.conversionBalanceSameAccount(), Info.Type.WARNING);
            return false;
        }

        return errors <= 0;
    }

    private void setMonthItems() {
        SelectItem[] monthItems = new SelectItem[12];
        Date currentDate = new Date();
        int currentMonth = currentDate.getMonth();
        Date date = DateUtil.getYearFirstDay(currentDate);
        for (int i = 0; i < 12; i++) {
            monthItems[i] = new SelectItem(i, format_month.format(date), String.valueOf(DateUtil.getDateInMonth(date.getYear(), date.getMonth())));
            date = DateUtil.addMonths(date, 1);
        }
        month.setItems(monthItems);
        month.setSelectedNullLabel();
        month.setSelected(currentMonth);

        month.addValueChangeHandler(changeEvent -> {
            journalDate = DateUtil.getMonthLastDate(DateUtil.addMonths(new Date(year.getSelectedId() - 1900, month.getSelectedId(), 1), -1));
            notice.setHTML(wfmMessages.conversionBalanceDate(DateUtils.preiewFormat(journalDate)));
        });
    }

    private void setYearItems() {
        SelectItem[] yearItem = new SelectItem[9];
        Date date = new Date();
        int currentYear = Integer.valueOf(format_year.format(date));

        for (int i = 6, j = 0; j < 6; i--, j++) {
            yearItem[j] = new SelectItem(currentYear - i, String.valueOf(currentYear - i));
        }

        yearItem[6] = new SelectItem(currentYear, String.valueOf(currentYear));

        for (int i = 1, j = 7; i <= 2; i++, j++) {
            yearItem[j] = new SelectItem(currentYear + i, String.valueOf(currentYear + i));
        }
        year.setItems(yearItem);
        year.setSelected(currentYear);

        year.addValueChangeHandler(changeEvent -> {
            journalDate = DateUtil.getMonthLastDate(DateUtil.addMonths(new Date(year.getSelectedId() - 1900, month.getSelectedId(), 1), -1));
            notice.setHTML(wfmMessages.conversionBalanceDate(DateUtils.preiewFormat(journalDate)));
        });

    }


    public void addFocusListener(final TextBox textBox, final String text, AccountsLookUp accountsLookUp) {

        textBox.addFocusListener(new FocusListener() {
            public void onFocus(Widget sender) {
                TextBox textbox = (TextBox) sender;
                if (textbox.getText().equals(text)) {
                    textbox.setText("");
                }
            }

            public void onLostFocus(Widget sender) {
                TextBox textbox = (TextBox) sender;
                if (textbox.getText().equals("")) {
                    textbox.setText(text);
                } else {
                    textBox.setText(AccountingUtils.get().format(AccountingUtils.get().parseToBigDecimal(textbox.getText())));
                }
                if (accountsLookUp.getSelectedItemID() != null) {
                    calculateTotal();
                }
            }
        });
        textBox.addKeyUpHandler(c -> {
            if (accountsLookUp.getSelectedItemID() != null) {
                calculateTotal();
            }
        });
    }
}