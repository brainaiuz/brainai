package com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice;

import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.edatasite.workforce.gwt.accounting.client.AccountingUtils.accountingStrings;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.*;

public class BillableExpenseWidget extends KpiSideNavBox implements Colapse {


    private DataGrid<ExpenseListItem> dataGrid;
    private ListDataProvider<ExpenseListItem> dataProvider;
    private Integer invoiceId;
    public static final ProvidesKey<ExpenseListItem> KEY_PROVIDER = item -> item == null ? null : item.getId();

    public BillableExpenseWidget(Integer invoiceId) {
        super(KpiSideNavBox.WIDE_FORM_WIDTH);
        this.invoiceId = invoiceId;
        init();
    }

    private void init() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new DataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("510px");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataProvider.addDataDisplay(dataGrid);
        loadData();
        Heading header = new Heading(HeadingSize.H1);
        header.setText("Billable Expense");
        addHeader(header);
        addBody(dataGrid);
    }

    private void loadData() {
        InvoiceService.App.get().getInvoiceBillableExpensesList(invoiceId, new AbstractAsyncCallback<ArrayList<ExpenseListItem>>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(ArrayList<ExpenseListItem> result) {
                super.success(result);
                setValues(result);
                initTableColumns();
            }
        });
    }

    private void setValues(List<ExpenseListItem> result) {
        ExpenseListItem[] expenseItem = new ExpenseListItem[result.size()];
        int i = 0;
        for (ExpenseListItem shippingData1 : result) {
            expenseItem[i] = shippingData1;
            i++;
        }
        initDataProviderApply(expenseItem);
        dataProvider.refresh();
    }

    private void initDataProviderApply(ExpenseListItem[] expenseItem) {
        List<ExpenseListItem> items = dataProvider.getList();
        items.clear();
        Collections.addAll(items, expenseItem);
    }

    private void initTableColumns() {

        final SimpleLinkCell[] cell = {new SimpleLinkCell()};
        Column<ExpenseListItem, String> number = new Column<ExpenseListItem, String>(cell[0]) {
            @Override
            public String getValue(ExpenseListItem data) {
                if (data.getExpenseReportNumber() != null) {
                    cell[0].setClickHandler(e -> {
                        if (BillableExpenseItem.EXPENSE.equals(data.getType())) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|previewReport/" + data.getReportId(), data.getExpenseReportNumber());
                        } else if (BillableExpenseItem.PURCHASE_AS_EXPENSE.equals(data.getType())) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice|summary/" + data.getReportId(), data.getExpenseReportNumber());
                        } else if (BillableExpenseItem.MANUAL_TRANSACTION_AS_EXPENSE.equals(data.getType())) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("manual|summary/" + data.getReportId(), data.getExpenseReportNumber());
                        } else if (BillableExpenseItem.BANK_TRANSFER_AS_EXPENSE.equals(data.getType())) {
                            if (CASH_PAYMENT.equals(data.getBankTransferType())) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|summary/" + data.getReportId() + "/" + CASH_PAYMENT_STR);
                            } else if (SPEND_MONEY.equals(data.getBankTransferType())) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|summary/" + data.getReportId() + "/" + SPEND_MONEY_STR);
                            }
                        } else if (BillableExpenseItem.CHECK_AS_EXPENSE.equals(data.getType())) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("check|summary/" + data.getReportId(), data.getExpenseReportNumber());
                        }
                        remove();
                    });
                }
                return data.getExpenseReportNumber();
            }
        };
        dataGrid.addColumn(number, wfmStrings.number());
        dataGrid.setColumnWidth(number, 25, com.google.gwt.dom.client.Style.Unit.PCT);

        //ExpenseDate
        Column<ExpenseListItem, String> expenseDate = new Column<ExpenseListItem, String>(new TextCell()) {
            @Override
            public String getValue(final ExpenseListItem data) {
                return data.getExpenseDate() != null ? DateUtils.format(data.getExpenseDate()) : "";
            }
        };
        dataGrid.addColumn(expenseDate, accountingStrings.expenseDate());
        dataGrid.setColumnWidth(expenseDate, 30, com.google.gwt.dom.client.Style.Unit.PCT);

        //category
        Column<ExpenseListItem, String> category = new Column<ExpenseListItem, String>(new TextCell()) {
            @Override
            public String getValue(final ExpenseListItem data) {
                return data.getCategoryName() != null ? data.getCategoryName() : "";
            }
        };
        dataGrid.addColumn(category, wfmStrings.category());
        dataGrid.setColumnWidth(category, 30, com.google.gwt.dom.client.Style.Unit.PCT);

        //ExpenseTotal
        Column<ExpenseListItem, String> expenseTotal = new Column<ExpenseListItem, String>(new TextCell()) {
            @Override
            public String getValue(final ExpenseListItem data) {
                return data.getExpenseAmountInCurency() != null ? Utils.formatDouble(data.getExpenseAmountInCurency().doubleValue()) : "";
            }
        };
        dataGrid.addColumn(expenseTotal, accountingStrings.expenseTotal());
        dataGrid.setColumnWidth(expenseTotal, 30, com.google.gwt.dom.client.Style.Unit.PCT);

        //markupAmount
        Column<ExpenseListItem, String> markupAmount = new Column<ExpenseListItem, String>(new TextCell()) {
            @Override
            public String getValue(final ExpenseListItem data) {
                return data.getMarkupAmount() != null ? Utils.formatDouble(data.getMarkupAmount().doubleValue()) : "";
            }
        };
        dataGrid.addColumn(markupAmount, accountingStrings.markupAmountOrPercent());
        dataGrid.setColumnWidth(markupAmount, 30, com.google.gwt.dom.client.Style.Unit.PCT);

        //AppliedAmount
        Column<ExpenseListItem, String> appliedAmount = new Column<ExpenseListItem, String>(new TextCell()) {
            @Override
            public String getValue(final ExpenseListItem data) {
                return data.getExpenseAmountInCurency() != null ? Utils.formatDouble((data.getExpenseAmountInCurency().add(data.getMarkupAmount())).doubleValue()) : "";
            }
        };
        dataGrid.addColumn(appliedAmount, accountingStrings.appliedAmount());
        dataGrid.setColumnWidth(appliedAmount, 30, com.google.gwt.dom.client.Style.Unit.PCT);

    }
}
