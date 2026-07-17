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
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomDatePickerCell;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MultiProgressInvoiceView extends AbstractProgressInvoicingView {


    public MultiProgressInvoiceView(Integer objectId) {
        super(AccountingConstants.BY_MULTI_PROGRESS, objectId);
        setDescription(accountingStrings.multiInvoiceByPercent());
    }

    @Override
    protected ColumnConfig[] getColumns() {
        ColumnConfig[] columns = new ColumnConfig[6];

        columns[0] = new ColumnConfig(CustomCell.class, ItemTableConstants.START_DATE, wfmStrings.startDate(), 100, true);
        columns[1] = new ColumnConfig(CustomCell.class, ItemTableConstants.END_DATE, wfmStrings.endDate(), 100, true);
        columns[2] = new ColumnConfig(CustomCell.class, ItemTableConstants.PERCENTAGE, wfmStrings.percentage(), 100, true, Constants.RIGHT_ALIGN_CELL);
        columns[3] = new ColumnConfig(CustomCell.class, ItemTableConstants.AMOUNT, wfmStrings.amount(), 100, true, Constants.RIGHT_ALIGN_CELL);
        columns[4] = new ColumnConfig(CustomCell.class, ItemTableConstants.STATUS, wfmStrings.status(), 100, false);
        columns[5] = new ColumnConfig(CustomCell.class, "", "", 0, false);

        return columns;
    }

    @Override
    protected Widget[] getWidgets(int row) {
        Widget[] rowWidgets = new Widget[getColumns().length];
        final DatePicker startDate = new CustomDatePickerCell();

        final DatePicker endDate = new CustomDatePickerCell();

        final TextBoxWithRealValue txtPercentage = new TextBoxWithRealValue();
        Validation.addNumericKeyboardListener(txtPercentage, AccountingUtils.calculationScale);
        txtPercentage.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        final TextBoxWithRealValue txtAmount = new TextBoxWithRealValue(true);
        Validation.addNumericKeyboardListener(txtAmount, Utils.getAccountingProgressInvoiceingAmountScale() != null ? Utils.getAccountingProgressInvoiceingAmountScale() : AccountingUtils.calculationScale);
        txtAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        DataListBox statusListBox = new DataListBox();
        statusListBox.setWithoutNullLabel(true);
        statusListBox.setItems(getStatusItems());
        statusListBox.setSelected(2);
        statusListBox.addValueChangeHandler(changeEvent -> copyStatusValues(row));

        txtPercentage.addKeyUpHandler(changeEvent -> calculateOnValueChange(row, false));
        txtAmount.addKeyUpHandler(changeEvent -> calculateOnValueChange(row, true));
        txtPercentage.addFocusHandler(focusEvent -> txtPercentage.selectAll());
        txtAmount.addFocusHandler(focusEvent -> txtAmount.selectAll());

        startDate.addChangeHandler(changeEvent -> setDateValuesOnChange(row));
        endDate.addChangeHandler(changeEvent -> setDateValuesOnChange(row));
        setInitialValues(txtPercentage, txtAmount, row);

        rowWidgets[0] = startDate;
        rowWidgets[1] = endDate;
        rowWidgets[2] = txtPercentage;
        rowWidgets[3] = txtAmount;
        rowWidgets[4] = statusListBox;
        rowWidgets[5] = new HTML();
        return rowWidgets;
    }

    @Override
    protected boolean validate() {
        int errors = 0;
        int closeDateError = 0;
        BigDecimal totalPercent = BigDecimal.ZERO, totalAmount = BigDecimal.ZERO;
        for (int i = 0; i < itemsTable.getRowCount(); i++) {
            CustomDatePickerCell startDate = (CustomDatePickerCell) itemsTable.getColumnById(i, ItemTableConstants.START_DATE);
            CustomDatePickerCell endDate = (CustomDatePickerCell) itemsTable.getColumnById(i, ItemTableConstants.END_DATE);
            TextBoxWithRealValue txtPercentage = (TextBoxWithRealValue) itemsTable.getColumnById(i, ItemTableConstants.PERCENTAGE);
            TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getColumnById(i, ItemTableConstants.AMOUNT);
            DataListBox statusListBox = (DataListBox) itemsTable.getColumnById(i, ItemTableConstants.STATUS);


            totalPercent = totalPercent.add(txtPercentage.getRealValue());
            totalAmount = totalAmount.add(txtAmount.getRealValue());

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
        if (totalPercent.subtract(AccountingUtils.HUNDRED).compareTo(BigDecimal.valueOf(0.1)) <= 0
                && totalPercent.subtract(AccountingUtils.HUNDRED).compareTo(BigDecimal.valueOf(-0.1)) >= 0) {
            correctDiff(totalPercent, true);
        }

        if (totalAmount.compareTo(total) > 0) {
            correctDiff(totalAmount, false);
        } else if (totalAmount.subtract(total).compareTo(BigDecimal.valueOf(0.1)) <= 0
                && totalAmount.subtract(total).compareTo(BigDecimal.valueOf(-0.1)) >= 0) {
            correctDiff(totalAmount, false);
        }

        return true;
    }

    @Override
    protected NewInvoice[] getInvoiceData() {
        ArrayList<NewInvoice> invoiceList = new ArrayList<>();
        BigDecimal exchangeRage = quote.getExchageRate() != null ? quote.getExchageRate() : BigDecimal.ONE;
        for (int i = 0; i < itemsTable.getRowCount(); i++) {
            DatePicker startDate = (CustomDatePickerCell) itemsTable.getColumnById(i, ItemTableConstants.START_DATE);
            DatePicker endDate = (CustomDatePickerCell) itemsTable.getColumnById(i, ItemTableConstants.END_DATE);
            TextBoxWithRealValue txtPercentage = (TextBoxWithRealValue) itemsTable.getColumnById(i, ItemTableConstants.PERCENTAGE);
            TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getColumnById(i, ItemTableConstants.AMOUNT);
            txtAmount.setCustomScale(true);
            DataListBox statusListBox = (DataListBox) itemsTable.getColumnById(i, ItemTableConstants.STATUS);
            if (BigDecimal.ZERO.compareTo(txtAmount.getRealValue()) >= 0 && BigDecimal.ZERO.compareTo(txtPercentage.getRealValue()) >= 0) {
                continue;
            }

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
            invoice.setQuoteId(quote.getQuoteId());
            invoice.setReference(quote.getReference());

            invoice.setInvoiceDate(new DateNonConvertable(DateUtil.resetTime(startDate.getDate())));
            invoice.setDueDate(new DateNonConvertable(endDate.getDate()));


            invoice.setSubtotal((quote.getSubtotal().multiply(txtPercentage.getRealValue()).divide(AccountingUtils.HUNDRED, 5, RoundingMode.HALF_UP)));
            invoice.setTotalInInvoiceCurrency(txtAmount.getRealValue());
            invoice.setTotal(txtAmount.getRealValue().divide(exchangeRage, AccountingUtils.getPriceScale(), RoundingMode.HALF_UP));
            invoice.setType(Constants.RECEIVABLE);
            if (statusListBox.getSelectedId() == null || statusListBox.getSelectedId() == 1) {
                invoice.setStatusCode(Constants.DRAFT);
            } else {
                invoice.setStatusCode(Constants.APPROVE);
            }
            invoice.setTaxCalculationType(AccountingConstants.TAX_CALCULATION_EXCLUSIVE);
            invoice.setBookkeep(true);

            invoice.setConvertedItemID(quote.getID());
            invoice.setConvertedAmount(txtAmount.getRealValue() != null ? txtAmount.getRealValue() : new BigDecimal(txtAmount.getValue()));

            invoice.setConvertedPercent(txtPercentage.getRealValue() != null ? txtPercentage.getRealValue() : new BigDecimal(txtPercentage.getValue()));
            invoice.setProgressInvoicing(true);
            invoice.setProgressInvoicingType(AccountingConstants.BY_MULTI_PROGRESS);

            NewInvoiceItem[] items = new NewInvoiceItem[quote.getItems().length];
            for (int j = 0; j < quote.getItems().length; j++) {
                items[j] = new NewInvoiceItem();
                items[j].setItemID(quote.getItems()[j].getItemID());
                items[j].setItemName(quote.getItems()[j].getItemName());
                items[j].setDescription(quote.getItems()[j].getDescription());
                items[j].setAccountID(quote.getItems()[j].getAccountID());
                items[j].setProject(quote.getItems()[j].getProject());
                items[j].setQuantity(quote.getItems()[j].getQuantity().multiply(txtPercentage.getRealValue()).divide(AccountingUtils.HUNDRED, 5, RoundingMode.HALF_UP));
                items[j].setUnitPrice(quote.getItems()[j].getUnitPrice());
                items[j].setNet(items[j].getQuantity().multiply(items[j].getUnitPrice()).divide(exchangeRage, 5, RoundingMode.HALF_UP));
                items[j].setTotalAmount(items[j].getQuantity().multiply(items[j].getUnitPrice()));
                items[j].setMeasurement(quote.getItems()[j].getMeasurement());
                items[j].setItemCategory(quote.getItems()[j].getItemCategory());
                items[j].setCustomFieldItems(quote.getItems()[j].getCustomFieldItems());
                if (quote.getItems()[j].getDiscountPercent() != null || quote.getItems()[j].getDiscountAmount() != null) {
                    if (quote.getItems()[j].getDiscountPercent() != null) {
                        items[j].setDiscountPercent(quote.getItems()[j].getDiscountPercent());
                    } else if (quote.getItems()[j].getDiscountAmount() != null) {
                        items[j].setDiscountAmount(quote.getItems()[j].getDiscountAmount().multiply(txtPercentage.getRealValue()).divide(AccountingUtils.HUNDRED, 5, RoundingMode.HALF_UP));
                    }
                }
                if (quote.getItems()[j].getTaxAmount() != null && quote.getItems()[j].getTaxItem() != null) {
                    items[j].setTaxItem(quote.getItems()[j].getTaxItem());
                    items[j].setTaxAmount(quote.getItems()[j].getTaxAmount().multiply(txtPercentage.getRealValue()).divide(AccountingUtils.HUNDRED, 5, RoundingMode.HALF_UP));
                }

            }

            invoice.setItems(items);
            invoiceList.add(invoice);
        }

        return invoiceList.toArray(new NewInvoice[]{});
    }
}
