package com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.progressinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomDatePickerCell;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class CustomProgressInvoicingView extends AbstractProgressInvoicingView {
    public CustomProgressInvoicingView(Integer objectId) {
        super(AccountingConstants.BY_CUSTOM_PERCENTAGE, objectId);
        setDescription(accountingStrings.byPercentage());
    }


    @Override
    protected ColumnConfig[] getColumns() {
        ColumnConfig[] columns = new ColumnConfig[10];
        columns[0] = new ColumnConfig(CustomCell.class, ItemTableConstants.PRODUCT, wfmStrings.itemName(), 150, true);
        columns[1] = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, wfmStrings.description(), 200, false);
        columns[2] = new ColumnConfig(CustomCell.class, ItemTableConstants.START_DATE, wfmStrings.startDate(), 100, true);
        columns[3] = new ColumnConfig(CustomCell.class, ItemTableConstants.END_DATE, wfmStrings.endDate(), 100, true);
        columns[4] = new ColumnConfig(LookUpCell.class, ItemTableConstants.ACCOUNT, wfmStrings.account(), 120, true);
        columns[5] = new ColumnConfig(CustomCell.class, ItemTableConstants.PERCENTAGE, wfmStrings.percentage(), 100, true, Constants.RIGHT_ALIGN_CELL);
        columns[6] = new ColumnConfig(CustomCell.class, ItemTableConstants.AMOUNT, wfmStrings.amount(), 100, true, Constants.RIGHT_ALIGN_CELL);
        columns[7] = new ColumnConfig(LookUpCell.class, ItemTableConstants.PROJECT, wfmStrings.project(), 150, false);
        columns[8] = new ColumnConfig(CustomCell.class, ItemTableConstants.STATUS, wfmStrings.status(), 100, false);
        columns[9] = new ColumnConfig(CustomCell.class, "", "", 0, false);

        return columns;
    }


    @Override
    public Widget[] getWidgets(int row) {
        Widget[] rowWidgets = new Widget[getColumns().length];
        final TextBox txtName = new CustomCellTextBox();
        txtName.addKeyUpHandler(handler -> copyTextBoxValues(row, ItemTableConstants.PRODUCT));

        final TextBox txtDescription = new CustomCellTextBox();
        txtDescription.addKeyUpHandler(handler -> copyTextBoxValues(row, ItemTableConstants.DESCRIPTION));

        final DatePicker startDate = new CustomDatePickerCell();

        final DatePicker endDate = new CustomDatePickerCell();

        final AccountsLookUp accountsList = new AccountsLookUp(Constants.RECEIVABLE);
        accountsList.getSuggestBox().addSelectionHandler(valueChangeEvent -> copyLookUpValues(row, ItemTableConstants.ACCOUNT));

        final TextBoxWithRealValue txtPercentage = new TextBoxWithRealValue();
        Validation.addNumericKeyboardListener(txtPercentage, 4);
        txtPercentage.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        final TextBoxWithRealValue txtAmount = new TextBoxWithRealValue();
        Validation.addNumericKeyboardListener(txtAmount, AccountingUtils.calculationScale);
        txtAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        final ProjectLookUp projectLookUp = new ProjectLookUp(Constants.RECEIVABLE, null);
        projectLookUp.setClientSupplierID(quote.getClientID());
        projectLookUp.getSuggestBox().addSelectionHandler(valueChangeEvent -> copyLookUpValues(row, ItemTableConstants.PROJECT));

        DataListBox statusListBox = new DataListBox();
        statusListBox.setWithoutNullLabel(true);
        statusListBox.setItems(getStatusItems());
        statusListBox.setSelected(2);
        statusListBox.addValueChangeHandler(changeEvent -> copyStatusValues(row));

        txtPercentage.addKeyUpHandler(changeEvent -> calculateOnValueChange(row, false));
        txtAmount.addKeyUpHandler(changeEvent -> calculateOnValueChange(row, true));


        startDate.addChangeHandler(changeEvent -> setDateValuesOnChange(row));
        endDate.addChangeHandler(changeEvent -> setDateValuesOnChange(row));

        setInitialValues(txtPercentage, txtAmount, row);
        rowWidgets[0] = txtName;
        rowWidgets[1] = txtDescription;
        rowWidgets[2] = startDate;
        rowWidgets[3] = endDate;
        rowWidgets[4] = accountsList;
        rowWidgets[5] = txtPercentage;
        rowWidgets[6] = txtAmount;
        rowWidgets[7] = projectLookUp;
        rowWidgets[8] = statusListBox;
        rowWidgets[9] = new HTML();
        return rowWidgets;
    }

    @Override
    protected NewInvoice[] getInvoiceData() {
        ArrayList<NewInvoice> invoiceList = new ArrayList<>();

        for (int i = 0; i < itemsTable.getRowCount(); i++) {
            CustomCellTextBox txtItem = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.PRODUCT);
            CustomCellTextBox txtDescription = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
            CustomDatePickerCell startDate = (CustomDatePickerCell) itemsTable.getColumnById(i, ItemTableConstants.START_DATE);
            CustomDatePickerCell endDate = (CustomDatePickerCell) itemsTable.getColumnById(i, ItemTableConstants.END_DATE);
            AccountsLookUp accountsLookUp = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
            TextBoxWithRealValue txtPercentage = (TextBoxWithRealValue) itemsTable.getColumnById(i, ItemTableConstants.PERCENTAGE);
            TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getColumnById(i, ItemTableConstants.AMOUNT);
            ProjectLookUp projectLookUp = (ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT);
            DataListBox statusListBox = (DataListBox) itemsTable.getColumnById(i, ItemTableConstants.STATUS);

            if (BigDecimal.ZERO.compareTo(txtAmount.getRealValue()) >= 0 && BigDecimal.ZERO.compareTo(txtPercentage.getRealValue()) >= 0) {
                continue;
            }

            BigDecimal exchangeRage = quote.getExchageRate() != null ? quote.getExchageRate() : BigDecimal.ONE;

            NewInvoice invoice = new NewInvoice();
            invoice.setClientID(quote.getClientID());
            invoice.setBillAddressID(quote.getBillAddressID());
            invoice.setMailAddressID(quote.getMailAddressID());
            invoice.setCurrencyID(quote.getCurrencyID());
            invoice.setExchageRate(exchangeRage);
            invoice.setRelatedProject(quote.getRelatedProject());
            invoice.setShippingMethodID(quote.getShippingMethodID());
            invoice.setShippingPrice(quote.getShippingPrice());
            invoice.setProjectBasedInvoice(false);
            invoice.setRecurringInvoice(false);
            invoice.setQuoteNumber(quote.getInvoiceNumber());
            invoice.setReference(quote.getReference());

            invoice.setInvoiceDate(new DateNonConvertable(DateUtil.resetTime(startDate.getDate())));
            invoice.setDueDate(new DateNonConvertable(endDate.getDate()));


            invoice.setSubtotal(txtAmount.getRealValue());
            invoice.setTotalInInvoiceCurrency(invoice.getSubtotal());
            invoice.setTotal(invoice.getSubtotal().divide(exchangeRage, AccountingUtils.getPriceScale(), RoundingMode.HALF_UP));
            invoice.setType(Constants.RECEIVABLE);
            if (statusListBox.getSelectedId() == null || statusListBox.getSelectedId() == 1) {
                invoice.setStatusCode(Constants.DRAFT);
            } else {
                invoice.setStatusCode(Constants.APPROVE);
            }
            invoice.setTaxCalculationType(AccountingConstants.TAX_CALCULATION_EXCLUSIVE);
            invoice.setBookkeep(true);

            invoice.setConvertedItemID(quote.getID());
            BigDecimal quoteTotal = quote.getTotal();
            BigDecimal invoiceTotal = AccountingUtils.get().parseToBigDecimal(txtAmount.getText());
            BigDecimal percent = invoiceTotal.multiply(AccountingUtils.HUNDRED).divide(quoteTotal, 2, RoundingMode.HALF_UP);
            invoice.setConvertedAmount(txtAmount.getRealValue() != null ? txtAmount.getRealValue() : new BigDecimal(txtAmount.getValue()));
            invoice.setConvertedPercent(percent);
            invoice.setProgressInvoicing(true);
            invoice.setProgressInvoicingType(AccountingConstants.BY_CUSTOM_PERCENTAGE);

            NewInvoiceItem item = new NewInvoiceItem();
            item.setItemName(txtItem.getText());
            item.setDescription(txtDescription.getText());
            item.setAccountID(accountsLookUp.getSelectedItemID());
            item.setProject(projectLookUp.getSelectedItem());
            item.setQuantity(BigDecimal.ONE);
            item.setUnitPrice(txtAmount.getRealValue());
            item.setNet(item.getUnitPrice().divide(exchangeRage, 4, RoundingMode.HALF_UP));
            item.setTotalAmount(item.getUnitPrice());

            invoice.setItems(new NewInvoiceItem[]{item});
            invoiceList.add(invoice);
        }

        return invoiceList.toArray(new NewInvoice[]{});
    }

    @Override
    protected boolean validate() {
        int errors = 0;
        int closeDateError = 0;
        BigDecimal totalPercent = BigDecimal.ZERO, totalAmount = BigDecimal.ZERO;

        for (int i = 0; i < itemsTable.getRowCount(); i++) {
            CustomCellTextBox txtItem = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.PRODUCT);
            CustomDatePickerCell startDate = (CustomDatePickerCell) itemsTable.getColumnById(i, ItemTableConstants.START_DATE);
            CustomDatePickerCell endDate = (CustomDatePickerCell) itemsTable.getColumnById(i, ItemTableConstants.END_DATE);
            AccountsLookUp accountsLookUp = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
            TextBoxWithRealValue txtPercentage = (TextBoxWithRealValue) itemsTable.getColumnById(i, ItemTableConstants.PERCENTAGE);
            TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getColumnById(i, ItemTableConstants.AMOUNT);
            DataListBox statusListBox = (DataListBox) itemsTable.getColumnById(i, ItemTableConstants.STATUS);

            totalPercent = totalPercent.add(txtPercentage.getRealValue());
            totalAmount = totalAmount.add(txtAmount.getRealValue());

            if (!Validation.validateTextBoxRequired(txtItem)) {
                itemsTable.notValid(i, ItemTableConstants.PRODUCT);
                errors++;
            }

            if (!Validation.validateDate(startDate)) {
                itemsTable.notValid(i, ItemTableConstants.START_DATE);
                errors++;
            } else if (Utils.isSalesLocked() && DateUtils.getTransactionLockDate().after(startDate.getDate()) && (statusListBox.getSelectedId() != null && statusListBox.getSelectedId() == 2)) {
                closeDateError++;
                startDate.addStyleName(ERROR_FORM_STYLE);
                itemsTable.notValid(i, ItemTableConstants.START_DATE);
            }

            if (!Validation.validateDate(endDate)) {
                itemsTable.notValid(i, ItemTableConstants.END_DATE);
                errors++;
            }
            if (!validateStartDateAndEndDate(startDate, endDate, i)) {
                errors++;
            }
            if (!Validation.validateLookUpRequired(accountsLookUp)) {
                itemsTable.notValid(i, ItemTableConstants.ACCOUNT);
                errors++;
            }
            if (!Validation.validateTextBoxRequired(txtPercentage)) {
                itemsTable.notValid(i, ItemTableConstants.PERCENTAGE);
                errors++;
            }
            if (!Validation.validateTextBoxRequired(txtAmount)) {
                itemsTable.notValid(i, ItemTableConstants.AMOUNT);
                errors++;
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.unableToSave(), Info.Type.WARNING);
            return false;
        }
        if (closeDateError > 0) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(accountingStrings.invoice(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }


        totalPercent = totalPercent.setScale(5, RoundingMode.HALF_UP);
        totalAmount = totalAmount.setScale(5, RoundingMode.HALF_UP);

        if (totalPercent.compareTo(AccountingUtils.HUNDRED) > 0) {
            correctDiff(totalPercent, true);
        }
        if (totalPercent.subtract(AccountingUtils.HUNDRED).

                compareTo(BigDecimal.valueOf(0.1)) <= 0
                && totalPercent.subtract(AccountingUtils.HUNDRED).

                compareTo(BigDecimal.valueOf(-0.1)) >= 0) {
            correctDiff(totalPercent, true);
        }

        if (totalAmount.compareTo(total) > 0) {
            correctDiff(totalAmount, false);
        }
        if (totalAmount.subtract(total).

                compareTo(BigDecimal.valueOf(0.1)) <= 0
                && totalAmount.subtract(total).

                compareTo(BigDecimal.valueOf(-0.1)) >= 0) {
            correctDiff(totalAmount, false);
        }

        return true;
    }
}
