package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.reconciliation;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.bundles.AccountingReportsImageBundles;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccountStatementTO;
import com.edatasite.workforce.gwt.accounting.client.rpc.FindMatchFilterData;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BankTransferAddEditView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableMatchedBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ExtendedDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.widget.LinkCellWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDialogContent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewWidget extends Composite {
    interface ReviewViewUiBinder extends UiBinder<Widget, ReviewWidget> {
    }

    private static ReviewViewUiBinder uiBinder = GWT.create(ReviewViewUiBinder.class);
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingReportsImageBundles reportsImageBundle = AccountingReportsImageBundles.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();


    private EditableTable reviewTable;

    private FindMatchFilterData filterData;
    private KpiModal shell;
    private ArrayList<Integer> selected;
    private HTML amountMatchHTML;
    private HTML totalAmountHTML;
    private final Map<String, KpiCheckBox> checkboxs = new HashMap<>();
    private DynamicTable dynamicTable;
    private WfmButton2 reconcileButton;
    private ScrollPanel scrollPanel;
    private Integer loadedAttachmentID;

    @UiField
    HTMLPanel reviewContent;
    @UiField
    HTMLPanel tableContent;
    @UiField
    Label transactionCount;

    public ReviewWidget(Integer attachmentId) {
        initWidget(uiBinder.createAndBindUi(this));
        reviewTable = new EditableTable(getColumns(), false, false);
        reviewTable.setWidth("100%");
        initStatements(attachmentId);
        this.loadedAttachmentID = attachmentId;
        tableContent.add(reviewTable);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BANK_TRANSFER_ADD, ReviewWidget.this, (sender, args) -> initStatements(attachmentId));
    }

    private void initRows(BankAccountStatementTO[] statementItems) {
        LoadingPanel.loading(true);
        if (statementItems != null) {
            reviewTable.removeAllRows();
            int currentRow = 0;
            for (BankAccountStatementTO statementItem : statementItems) {
                final int finalRow = currentRow;
                List<Object> widgets = new ArrayList<>();

                ExtendedDatePicker transactionDate = new ExtendedDatePicker();
                EditableTextBox amount = new EditableTextBox();
                EditableTextBox description = new EditableTextBox();
                EditableTextBox reference = new EditableTextBox();
                EditableTextBox transactionType = new EditableTextBox();
                LinkCellWidget findAndMatch = new LinkCellWidget("", null);
                LinkCellWidget createTransaction = new LinkCellWidget("", null);
                EditableMatchedBox matchedBox= new EditableMatchedBox();
                LinkCellWidget matchedTransactionBox= new LinkCellWidget("",null);




                transactionDate.setEnabled(false);
                amount.setEnabled(false);
                description.setEnabled(false);
                reference.setEnabled(false);
                transactionType.setEnabled(false);
                amount.setTextAlignment(TextBox.ALIGN_RIGHT);


                transactionDate.setDate(statementItem.getTransactionDate());
                if (statementItem.isDebitCredit()) {
                    amount.setText(AccountingUtils.get().format(statementItem.getDebit()));
                } else {
                    amount.setText("-" + AccountingUtils.get().format(statementItem.getCredit()));
                    amount.getText().replace("--", "-");
                }
                description.setText(statementItem.getDescription());
                reference.setText(statementItem.getReference());
                transactionType.setText(statementItem.isDebitCredit() ? accountingStrings.received() : accountingStrings.spent());
                findAndMatch.setText(accountingStrings.findAndMatch());
                createTransaction.setText(accountingStrings.createNewTransaction());

                findAndMatch.setClickHandler(() -> {
                    showReconcileView(statementItem, true);
                });

                if (statementItem.getTransaction() != null) {
                    matchedTransactionBox.setText(statementItem.getTransaction().getJournalName());
                    matchedTransactionBox.setClickHandler(() -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + statementItem.getTransaction().getJournalId());
                    });
                }

                createTransaction.setClickHandler(() -> {
                    new BankTransferAddEditView(statementItem.getBankAccountID(), statementItem.getAmount().abs(),
                            statementItem.getTransactionDate(), statementItem.isDebitCredit(), statementItem.getReference(), statementItem.getDescription());
                });

                if (statementItem.isReconsiled()) {
                    matchedBox.getSaveButton().setVisible(true);
                    matchedBox.getSaveButton().getElement().setInnerHTML("<i class='" + WfmButton2.ICON_CHECK + "'></i>");
                    matchedBox.getSaveButton().setStyleName("btn btn-sm btn-default");

                } else if (statementItem.getTransaction() != null) {
                    matchedBox.getSaveButton().setVisible(true);
                } else {
                    matchedBox.getSaveButton().setVisible(false);
                }

                matchedBox.getSaveButton().addClickHandler(new ClickHandler(){
                    public void onClick(ClickEvent event) {
                        ArrayList<Integer> selected = new ArrayList();
                        selected.add(statementItem.getTransaction().getTransactionId());
                        AccountingService.App.get().reconcileStatement(statementItem.getBankStatementItemID(), selected, statementItem.getBankGlAccountID(), new AsyncCallback<Boolean>() {
                            public void onFailure(Throwable caught) {
                                //To change body of implemented methods use File | Settings | File Templates.
                            }

                            public void onSuccess(Boolean result) {
                                initStatements(loadedAttachmentID);
                            }
                        });
                    }

                });

                widgets.add(transactionDate);
                widgets.add(amount);
                widgets.add(description);
                widgets.add(reference);
                widgets.add(transactionType);
                widgets.add(findAndMatch);
                widgets.add(createTransaction);
                widgets.add(matchedBox);
                widgets.add(matchedTransactionBox);

                reviewTable.addRow(widgets.toArray());
                ((CustomCell) reviewTable.getColumnCellWidgetById(currentRow, "MATCH_FOUND")).displayActive(true);
                currentRow++;
            }
        }
        LoadingPanel.loading(false);
    }

    private ColumnConfig[] getColumns() {
        ArrayList<ColumnConfig> columns = new ArrayList<>();
        columns.add(new ColumnConfig(CustomCell.class, "TRANSACTION_DATE", Constants.TRANSACTION_DATE_STR, 120));
        columns.add(new ColumnConfig(CustomCell.class, "TRANSACTION_AMOUNT", Constants.TRANSACTION_AMOUNT_STR, 100,false, Constants.RIGHT_ALIGN_CELL));
        columns.add(new ColumnConfig(CustomCell.class, "TRANSACTION_DESCRIPTION", Constants.TRANSACTION_DESCRIPTION_STR, 150));
        columns.add(new ColumnConfig(CustomCell.class, "TRANSACTION_REFERENCE", Constants.TRANSACTION_REFERENCE_STR, 150));
        columns.add(new ColumnConfig(CustomCell.class, "TRANSACTION_TYPE", Constants.TRANSACTION_TYPE, 100));
        columns.add(new ColumnConfig(LinkableCell.class, "FIND_AND_MATCH", Constants.FIND_AND_MATCH_STR, 100));
        columns.add(new ColumnConfig(LinkableCell.class, "CREATE_TRANSACTION", Constants.FIND_AND_MATCH_STR, 100));
        columns.add(new ColumnConfig(CustomCell.class, "MATCH_FOUND", Constants.MATCH_FOUND, 100));
        columns.add(new ColumnConfig(LinkableCell.class, "MATCH_TRANSACTION", Constants.MATCH_TRANSACTION, 120));
        return columns.toArray(new ColumnConfig[]{});
    }
    private void initStatements(Integer attachmentId) {

        LoadingPanel.loading(true);

        AccountingService.App.get().getStatementItems(attachmentId, new AsyncCallback<BankAccountStatementTO[]>() {
            public void onFailure(Throwable caught) {
                Window.alert("Error: " + caught.getMessage());
            }

            public void onSuccess(BankAccountStatementTO[] result) {
                initRows(result);
                transactionCount.setText(result.length + " transaction(s) are ready to import");
                LoadingPanel.loading(false);
            }
        });
    }


    private void showReconcileView(final BankAccountStatementTO transaction, final boolean isFindAndMatch) {

        filterData = new FindMatchFilterData();
        filterData.setGlAccountID(transaction.getBankGlAccountID());
        filterData.setDebitCredit(transaction.isDebitCredit());

        shell = new KpiModal();
        shell.addStyleName("import-transactions-modal");
        shell.setTitle(accountingStrings.selectMatchingTransaction());
//        shell.setWidth(1300);

        selected = new ArrayList<>();
        amountMatchHTML = new HTML("Matched");
        amountMatchHTML.setStyleName("bankStatementReconcileBackground");
        amountMatchHTML.setVisible(false);

        totalAmountHTML = new HTML();

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(5);
        buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        final MaterialDialogContent mainPanel = shell.getContent();
        mainPanel.setWidth("100%");

        final KpiCheckBox unchecked = new KpiCheckBox(wfmStrings.deselect());
        unchecked.setValue(transaction.getTransaction() != null);
        unchecked.setEnabled(transaction.getTransaction() != null);
        unchecked.addClickHandler(event -> {
            for (Object o : checkboxs.entrySet()) {
                CustomCheckBox ck;
                Map.Entry pairs = (Map.Entry) o;
                ck = (CustomCheckBox) pairs.getValue();
                ck.setValue(unchecked.getValue());
            }
        });

        final TextBox searchTextBox = new TextBox();
        final TextBox startAmount = new TextBox();

        final TextBox endAmount = new TextBox();

        startAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        endAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(startAmount, 2);
        Validation.addNumericKeyboardListener(endAmount, 2);

        final DatePicker startDate = new DatePicker();
        final DatePicker endDate = new DatePicker();


        WfmButton2 applyFilterButton = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_PRIMARY);
        WfmButton2 resetButton = new WfmButton2(wfmStrings.reset());

        FlexTable topFilterTable = new FlexTable();
        topFilterTable.addStyleName("matching-transactions-table");
        topFilterTable.setCellSpacing(10);
        topFilterTable.setWidget(0, 0, new HTML(wfmStrings.search()));
        topFilterTable.setWidget(0, 1, new HTML(accountingStrings.amountMoreThan()));
        topFilterTable.setWidget(0, 2, new HTML(accountingStrings.amountLessThan()));
        topFilterTable.setWidget(0, 3, new HTML(wfmStrings.startDate()));
        topFilterTable.setWidget(0, 4, new HTML(wfmStrings.endDate()));
        topFilterTable.setWidget(1, 0, new HTML("<span class='cell-search'>" + searchTextBox + "</span>"));
        topFilterTable.setWidget(1, 1, new HTML("<span class='cell-amountMore'>" + startAmount + "</span>"));
        topFilterTable.setWidget(1, 2, new HTML("<span class='cell-amountLess'>" + endAmount + "</span>"));
        topFilterTable.setWidget(1, 3, startDate);
        topFilterTable.setWidget(1, 4, endDate);
        topFilterTable.setWidget(1, 5, applyFilterButton);
        topFilterTable.setWidget(1, 6, resetButton);
        topFilterTable.setWidget(2, 0, unchecked);
        topFilterTable.setWidget(2, 1, new HTML(accountingStrings.statementAmount()));
        topFilterTable.setWidget(2, 2, new HTML((transaction.isDebitCredit() ? transaction.getDebit() + "" : transaction.getCredit() + "")));
        topFilterTable.setWidget(2, 3, new HTML(accountingStrings.kpiAmount()));
        topFilterTable.setWidget(2, 4, totalAmountHTML);
        topFilterTable.setWidget(2, 5, amountMatchHTML);
        mainPanel.add(topFilterTable);

        DynamicTableColumn[] columns = new DynamicTableColumn[6];
        columns[0] = new DynamicTableColumn("", "checkBox", 20);
        columns[1] = new DynamicTableColumn(wfmStrings.date(), "date", 100);
        columns[2] = new DynamicTableColumn(wfmStrings.name(), "name", 200);
        columns[3] = new DynamicTableColumn(wfmStrings.number(), "refNumber", 100);
        columns[4] = new DynamicTableColumn(accountingStrings.spent(), "spent", 100);
        columns[5] = new DynamicTableColumn(accountingStrings.received(), "received", 100);

        dynamicTable = new DynamicTable(columns, false);
        dynamicTable.setWidth("100%");
        dynamicTable.setBorderWidth(0);
        dynamicTable.setStyleName(AccountingCustomFormConstants.STYLE_PRODUCT_TABLE);

        reconcileButton = new WfmButton2(wfmStrings.reconcile());
        WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        scrollPanel = new ScrollPanel();

        applyFilterButton.addClickHandler(clickEvent -> {
            filterData.setSearchKey(searchTextBox.getText());
            if (startAmount.getText() != null && !startAmount.getText().trim().isEmpty()) {
                filterData.setStartAmount(AccountingUtils.get().parseToBigDecimal(startAmount.getText().trim()));
            } else {
                filterData.setStartAmount(null);
            }
            if (endAmount.getText() != null && !endAmount.getText().trim().isEmpty()) {
                filterData.setEndAmount(AccountingUtils.get().parseToBigDecimal(endAmount.getText().trim()));
            } else {
                filterData.setEndAmount(null);
            }
            filterData.setStartDate(Utils.getStartDateNC(startDate.getDate()));
            filterData.setEndDate(Utils.getEndDateNC(endDate.getDate()));
            loadReconcileContentData(transaction, isFindAndMatch);
        });

        resetButton.addClickHandler(clickEvent -> {
            searchTextBox.setText("");
            startAmount.setText("");
            endAmount.setText("");
            startDate.clearSelected();
            endDate.clearSelected();

            filterData.setSearchKey(null);
            filterData.setStartAmount(null);
            filterData.setEndAmount(null);
            filterData.setStartDate(null);
            filterData.setEndDate(null);
            loadReconcileContentData(transaction, isFindAndMatch);
        });

        reconcileButton.setVisible(false);
        reconcileButton.addClickHandler(event -> {
            BigDecimal total = calculate(transaction);
            if (transaction.getAmount() != null && transaction.getAmount().setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP).compareTo(total) == 0) {
                AccountingService.App.get().reconcileStatement(transaction.getBankStatementItemID(), selected, transaction.getBankGlAccountID(), new AsyncCallback<Boolean>() {
                    public void onFailure(Throwable caught) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    public void onSuccess(Boolean result) {
                        if (result) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TRANSACTION_RECONCILED, result, ReviewWidget.this);
                            shell.close();
                            initStatements(loadedAttachmentID);
                        }
                    }
                });
            } else {
                Info.show(accountingStrings.sumOfSelectedTransactionsMustMatch() + " " + AccountingUtils.get().formatPrice(transaction.getAmount()), Info.Type.WARNING);
            }
        });
        cancelButton.addClickHandler(event -> shell.close());

        loadReconcileContentData(transaction, isFindAndMatch);

        buttonPanel.add(reconcileButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(scrollPanel);
        mainPanel.add(buttonPanel);
        shell.open();
    }

    private void loadReconcileContentData(final BankAccountStatementTO transaction, boolean isFindAndMatch) {
        LoadingPanel.loading(true);
        scrollPanel.clear();
        if (isFindAndMatch) {
            AccountingService.App.get().findAndMatchTransactions(filterData, new AsyncCallback<ArrayList<Transaction>>() {
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void onSuccess(ArrayList<Transaction> result) {
                    drawReconcileContent(transaction, result);
                    LoadingPanel.loading(false);
                }
            });
        } else {
            filterData.setTransactionAmount(transaction.getAmount());
            AccountingService.App.get().findOthers(filterData, new AsyncCallback<ArrayList<Transaction>>() {
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.show(accountingStrings.errorOccuredDuringTheLoad(), Info.Type.WARNING);
                }

                public void onSuccess(ArrayList<Transaction> result) {
                    drawReconcileContent(transaction, result);
                    LoadingPanel.loading(false);
                }
            });
        }
    }


    private void drawReconcileContent(final BankAccountStatementTO transaction, List<Transaction> result) {
        if (result != null && result.size() > 0) {
            dynamicTable.clear();
            for (Transaction trans : result) {
                Widget[] widgets = new Widget[6];
                CustomCheckBox checkBox = new CustomCheckBox(trans);
                if (transaction.getTransaction() != null && trans.getTransactionId().equals(transaction.getTransaction().getTransactionId())) {
                    checkBox.setValue(true);
                    checkboxs.put(trans.getJournalId().toString(), checkBox);
                }
                widgets[0] = checkBox;
                widgets[1] = new Label(DateUtils.format(trans.getJournalDate()));
                widgets[2] = new Label(trans.getJournalName());
                widgets[3] = new Label(trans.getReference() != null ? trans.getReference() : " ");
                widgets[4] = new Label(trans.getTotalCredit() != null ? AccountingUtils.get().formatPrice(trans.getTotalCredit()) : "");
                widgets[5] = new Label(trans.getTotalDebit() != null ? AccountingUtils.get().formatPrice(trans.getTotalDebit()) : "");
                dynamicTable.addRow(widgets);
                checkBox.addClickHandler(clickEvent -> calculate(transaction));
            }

            scrollPanel.add(dynamicTable);
            scrollPanel.addStyleName("matching-transactions-results");
            reconcileButton.setVisible(true);
        } else {
            scrollPanel.add(new HTML("<b>" + accountingStrings.noMatchFound() + "</b>"));
            scrollPanel.addStyleName("matching-transactions-results matching-transactions-results--null");
            reconcileButton.setVisible(false);
        }
    }

    private BigDecimal calculate(final BankAccountStatementTO transaction) {
        selected.clear();
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
            DynamicTableItem item = dynamicTable.getItem(i);
            CustomCheckBox checkBox = (CustomCheckBox) item.getColumnById("checkBox");
            if (checkBox.getValue()) {
                Transaction checkedTransaction = checkBox.getTransaction();
                selected.add(checkedTransaction.getTransactionId());
                if (transaction.isDebitCredit()) {
                    if (checkedTransaction.getTotalDebit() != null) {
                        total = total.add(checkedTransaction.getTotalDebit()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    }
                    if (checkedTransaction.getTotalCredit() != null) {
                        total = total.subtract(checkedTransaction.getTotalCredit()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    }
                } else {
                    if (checkedTransaction.getTotalDebit() != null) {
                        total = total.subtract(checkedTransaction.getTotalDebit()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    }
                    if (checkedTransaction.getTotalCredit() != null) {
                        total = total.add(checkedTransaction.getTotalCredit()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    }
                }
            }
        }
        amountMatchHTML.setVisible(total.compareTo((transaction.isDebitCredit() ? transaction.getDebit() : transaction.getCredit())) == 0);
        totalAmountHTML.setHTML(total + "");
        return total;
    }


    private class CustomCheckBox extends KpiCheckBox {
        private final Transaction transaction;

        private CustomCheckBox(Transaction transaction) {
            this.transaction = transaction;
        }

        public Transaction getTransaction() {
            return transaction;
        }
    }

}
