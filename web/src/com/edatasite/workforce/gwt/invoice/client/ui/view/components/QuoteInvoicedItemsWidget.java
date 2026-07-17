package com.edatasite.workforce.gwt.invoice.client.ui.view.components;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

import java.util.Arrays;
import java.util.List;

public class QuoteInvoicedItemsWidget extends KpiSideNavBox {

    private DataGrid<NewInvoice> dataGrid;
    private ListDataProvider<NewInvoice> dataProvider;
    public static final ProvidesKey<NewInvoice> KEY_PROVIDER = item -> item == null ? null : item.getID();

    private boolean receivable;

    public QuoteInvoicedItemsWidget(NewInvoice[] items, boolean receivable) {
        super(WIDE_FORM_WIDTH);
        this.receivable = receivable;

        dataProvider = new ListDataProvider<>();
        dataGrid = new DataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("510px");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataProvider.addDataDisplay(dataGrid);
        initializeColumns();

        List<NewInvoice> list = dataProvider.getList();
        list.addAll(Arrays.asList(items));
        dataProvider.refresh();

        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.invoicedItems());
        addHeader(header);
        addBody(dataGrid);
    }

    private void initializeColumns() {

        SimpleLinkCell cell = new SimpleLinkCell();
        Column<NewInvoice, String> number = new Column<NewInvoice, String>(cell) {
            @Override
            public String getValue(NewInvoice invoice) {
                cell.setClickHandler(e -> {

                    if (invoice.getInvoiceNumber().trim().length() > 0) {
                        final boolean isProjectBasedInvoice = invoice.isProjectBasedInvoice();
                        final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !Constants.PS_CLOSED.equals(invoice.getProjectStatusCode()));

                        if (receivable) {
                            boolean editPermission = Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_EDIT);
                            boolean editFullPermission = Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_FULL_EDIT_ACCESS);
                            boolean hasAccountingBeforeBlockDate = (Utils.isSalesLocked() && invoice.getInvoiceDate() != null && DateUtils.getTransactionLockDate().after(invoice.getInvoiceDate().getNonConvertedDate()));

                            if ("Draft".equals(invoice.getStatus())) {

                                if (hasAccessToChange && !invoice.hasAnyPayment() && ((invoice.isSubmitter(Utils.getUserID()) && editPermission) || editFullPermission)) {

                                    if (!hasAccountingBeforeBlockDate) {
                                        SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_INVOICE + "|edit/" + invoice.getID() + (isProjectBasedInvoice ? "/projectbased" : "") + ((invoice.getInvoiceCustomType() != null && !invoice.getInvoiceCustomType().isEmpty()) ? ("/" + invoice.getInvoiceCustomType()) : ""), invoice.getInvoiceNumber());
                                    }
                                }
                            } else if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_SUMMARY)) {
                                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_INVOICE + "|summary/" + invoice.getID(), invoice.getInvoiceNumber());
                            }
                        } else {
                            boolean editPermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PURCHASE_INVOICE_EDIT : PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_EDIT);
                            boolean editFullPermission = Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PURCHASE_INVOICE_FULL_EDIT_ACCESS : PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_FULL_EDIT_ACCESS);

                            if ("Draft".equals(invoice.getStatus())) {

                                if (hasAccessToChange && ((invoice.isSubmitter(Utils.getUserID()) && editPermission) || editFullPermission) && !invoice.hasAnyPayment()) {
                                    SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice|edit/" + invoice.getID()
                                            + (isProjectBasedInvoice ? "/projectbased" : ""), invoice.getInvoiceNumber());
                                }
                            } else if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_SUMMARY)) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice|summary/" + invoice.getID(), invoice.getInvoiceNumber());
                            }
                        }
                        remove();
                    }
                });

                return invoice.getInvoiceNumber();
            }
        };
        dataGrid.addColumn(number, wfmStrings.number());
        dataGrid.setColumnWidth(number, 25, com.google.gwt.dom.client.Style.Unit.PCT);

        //date
        Column<NewInvoice, String> date = new Column<NewInvoice, String>(new TextCell()) {
            @Override
            public String getValue(final NewInvoice invoice) {
                return invoice.getInvoiceDate() != null ? DateUtils.format(invoice.getInvoiceDate().getNonConvertedDate()) : "";
            }
        };
        dataGrid.addColumn(date, wfmStrings.invoiceDate());
        dataGrid.setColumnWidth(date, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        //due date
        Column<NewInvoice, String> dueDate = new Column<NewInvoice, String>(new TextCell()) {
            @Override
            public String getValue(NewInvoice invoice) {
                return invoice.getDueDate() != null ? DateUtils.format(invoice.getDueDate().getNonConvertedDate()) : "";
            }
        };
        dataGrid.addColumn(dueDate, wfmStrings.dueDate());
        dataGrid.setColumnWidth(dueDate, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        //amount
        Column<NewInvoice, String> amount = new Column<NewInvoice, String>(new TextCell()) {
            @Override
            public String getValue(NewInvoice invoice) {
                return invoice.getTotal() != null ? AccountingUtils.get().formatPrice(invoice.getTotal()) : "";
            }
        };
        dataGrid.addColumn(amount, AccountingStrings.App.get().invoiceAmount());
        dataGrid.setColumnWidth(amount, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        //status
        Column<NewInvoice, String> status = new Column<NewInvoice, String>(new TextCell()) {
            @Override
            public String getValue(NewInvoice invoice) {
                return invoice.getStatus();
            }
        };
        dataGrid.addColumn(status, wfmStrings.status());
        dataGrid.setColumnWidth(status, 15, com.google.gwt.dom.client.Style.Unit.PCT);
    }
}
