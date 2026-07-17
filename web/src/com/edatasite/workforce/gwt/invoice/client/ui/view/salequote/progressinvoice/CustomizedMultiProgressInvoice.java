package com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.progressinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.google.gwt.user.client.ui.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Shohruh on 16-Mar-16.
 */
public class CustomizedMultiProgressInvoice extends Composite {
    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private final ScrollPanel panel;
    private TextBox txtPeriod;
    private final BigDecimal total;
    private Integer period = 1;
    private final Integer limit = 100;

    private FlexTable itemsTable;
    private final NewInvoice quote;

    CustomizedMultiProgressInvoice(NewInvoice quote) {
        this.quote = quote;
        this.total = quote.getTotalInInvoiceCurrency();
        panel = new ScrollPanel();

        panel.setHeight("300px");

        initWidget(panel);

        initInternal();
    }

    private void initInternal() {
        txtPeriod = new TextBox();
        txtPeriod.setValue(String.valueOf(period));
        txtPeriod.setWidth("200px");
        Validation.addNumericKeyboardListener(txtPeriod, 0);
        txtPeriod.addChangeHandler(changeEvent -> {
            if (txtPeriod.getText() != null && !txtPeriod.getText().isEmpty()) {
                BigDecimal p = AccountingUtils.get().parseToBigDecimal(txtPeriod.getText());
                if (p.compareTo(BigDecimal.valueOf(limit)) > 0) {
                    txtPeriod.setValue(limit.toString());
                    p = BigDecimal.valueOf(limit);
                }
                p = p.subtract(new BigDecimal(period));
                if (Integer.parseInt(txtPeriod.getText()) != 0) {
                    period = Integer.parseInt(txtPeriod.getText());
                }
                if (p.compareTo(BigDecimal.ZERO) > 0) {
                    for (int i = 0; i < p.intValue(); i++) {
                        addItemsRow();
                    }
                } else if (p.compareTo(BigDecimal.ZERO) < 0) {
                    p = p.multiply(new BigDecimal(-1));
                    if (p.intValue() < (itemsTable.getRowCount() - 1)) {
                        for (int i = 0; i < p.intValue(); i++) {
                            itemsTable.removeRow(itemsTable.getRowCount() - 1);
                        }
                    }
                }
                for (int i = 1; i < itemsTable.getRowCount(); i++) {
                    TextBoxWithRealValue txtPercentage = (TextBoxWithRealValue) itemsTable.getWidget(i, 2);
                    TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getWidget(i, 3);
                    BigDecimal percentage = AccountingUtils.HUNDRED.divide(BigDecimal.valueOf(period), 5, RoundingMode.HALF_UP);
                    if (i == itemsTable.getRowCount() - 1) {
                        percentage = AccountingUtils.HUNDRED.subtract(percentage.multiply(BigDecimal.valueOf(period - 1)));
                    }
                    txtPercentage.setRealValue(percentage);
                    txtPercentage.setValue(AccountingUtils.get().format(percentage));

                    BigDecimal amount = total.multiply(percentage).divide(AccountingUtils.HUNDRED, 5, RoundingMode.HALF_UP);
                    txtAmount.setRealValue(amount);
                    txtAmount.setValue(AccountingUtils.get().formatPrice(amount));

                    final int row = i;
                    txtAmount.addChangeHandler(changeEvent1 -> recalculateAmountOnValueChange(row));
                }
            }
        });

        TextBox txtTotal = new TextBox();
        txtTotal.setWidth("200px");
        txtTotal.setEnabled(false);

        if (total != null) {
            txtTotal.setValue(AccountingUtils.get().formatPrice(total));
        }
        Validation.addNumericKeyboardListener(txtTotal, 2);

        VerticalPanel pnlContainer = new VerticalPanel();
        pnlContainer.setSpacing(5);
        panel.add(pnlContainer);

        FlexTable table = new FlexTable();
        table.setWidth("100%");
        table.setCellPadding(0);
        table.setCellSpacing(0);
        table.setWidget(0, 0, new HTML("<b>" + wfmStrings.period() + " (max: " + limit + ")" + "</b>"));
        table.setWidget(0, 1, new HTML("<b>" + wfmStrings.amount() + "</b>"));
        table.setWidget(1, 0, txtPeriod);
        table.setWidget(1, 1, txtTotal);
        table.getFlexCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
        table.getFlexCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
        table.getFlexCellFormatter().setAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
        table.getFlexCellFormatter().setAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
        pnlContainer.add(table);

        itemsTable = new FlexTable();
        itemsTable.setStyleName("flexTable");
        itemsTable.setCellPadding(0);
        itemsTable.setCellSpacing(0);

        int column = 0;
        itemsTable.setWidget(0, column++, new HTML(wfmStrings.startDate()));
        itemsTable.setWidget(0, column++, new HTML(wfmStrings.endDate()));
        itemsTable.setWidget(0, column++, new HTML(wfmStrings.percentage()));
        itemsTable.setWidget(0, column++, new HTML(wfmStrings.amount()));
        itemsTable.setWidget(0, column++, new HTML(wfmStrings.status()));
        itemsTable.setWidget(0, column++, new HTML(""));
        itemsTable.getFlexCellFormatter().setStyleName(0, 0, "flexTable-Label");
        itemsTable.getFlexCellFormatter().setStyleName(0, 1, "flexTable-Label");
        itemsTable.getFlexCellFormatter().setStyleName(0, 2, "flexTable-Label");
        itemsTable.getFlexCellFormatter().setStyleName(0, 3, "flexTable-Label");
        itemsTable.getFlexCellFormatter().setStyleName(0, 4, "flexTable-Label");
        itemsTable.getFlexCellFormatter().setStyleName(0, 5, "flexTable-Label");
        pnlContainer.add(itemsTable);

        addItemsRow();
    }

    private void addItemsRow() {
        final DatePicker startDate = new DatePicker();
        startDate.setWidth("100px");

        final DatePicker endDate = new DatePicker();
        endDate.setWidth("100px");

        final TextBoxWithRealValue txtPercentage = new TextBoxWithRealValue();
        txtPercentage.setValue(AccountingUtils.get().format(AccountingUtils.HUNDRED));
        txtPercentage.setRealValue(AccountingUtils.HUNDRED);
        txtPercentage.setWidth("60px");
        Validation.addNumericKeyboardListener(txtPercentage, 4);

        final TextBoxWithRealValue txtAmount = new TextBoxWithRealValue();
        txtAmount.setValue(AccountingUtils.get().formatPrice(total));
        txtAmount.setRealValue(total);
        Validation.addNumericKeyboardListener(txtAmount, AccountingUtils.calculationScale);
        txtAmount.setWidth("100px");

        DataListBox statusListBox = new DataListBox();
        statusListBox.setWithoutNullLabel(true);
        SelectItem[] statusItems = new SelectItem[]{
                new SelectItem(1, Constants.DRAFT),
                new SelectItem(2, Constants.APPROVE)
        };
        statusListBox.setItems(statusItems);
        statusListBox.setSelected(1);
        statusListBox.setWidth("100px");

//        txtPercentage.addChangeHandler(changeEvent -> {
//            txtPercentage.setRealValue(new BigDecimal(txtPercentage.getValue()));
//            BigDecimal totalPercents = getTotalPercentageFromItems();
//            txtPercentage.removeStyleName("x-form-invalid");
//            BigDecimal amount = total.multiply(txtPercentage.getRealValue()).divide(AccountingUtils.HUNDRED, 5, BigDecimal.ROUND_HALF_UP);
//            txtAmount.setRealValue(amount);
//            txtAmount.setText(AccountingUtils.get().formatPrice(amount));
//
//        });
//        txtAmount.addChangeHandler(changeEvent -> {
//            txtAmount.setRealValue(new BigDecimal(txtAmount.getValue()));
//            BigDecimal totalAmount = getTotalAmountFromItems();
//
//            txtAmount.removeStyleName("x-form-invalid");
//            BigDecimal percent = txtAmount.getRealValue().multiply(AccountingUtils.HUNDRED).divide(total, 5, BigDecimal.ROUND_HALF_UP);
//            txtPercentage.setRealValue(percent);
//            txtPercentage.setText(AccountingUtils.get().format(percent));
//
//        });

        ActionButton copyToAllButton = new ActionButton("&nbsp;", "icon-copy2");
        copyToAllButton.ensureDebugId("copy");
        copyToAllButton.setTitle(wfmStrings.copyToAll());
        copyToAllButton.setVisible(true);

        final int row = itemsTable.getRowCount();
        int column = 0;
        itemsTable.setWidget(row, column++, startDate);
        itemsTable.setWidget(row, column++, endDate);
        itemsTable.setWidget(row, column++, txtPercentage);
        itemsTable.setWidget(row, column++, txtAmount);
        itemsTable.setWidget(row, column++, statusListBox);
        itemsTable.setWidget(row, column++, copyToAllButton);
        itemsTable.getFlexCellFormatter().setStyleName(row, 0, "flexTable-td");
        itemsTable.getFlexCellFormatter().setStyleName(row, 1, "flexTable-td");
        itemsTable.getFlexCellFormatter().setStyleName(row, 2, "flexTable-td");
        itemsTable.getFlexCellFormatter().setStyleName(row, 3, "flexTable-td");
        itemsTable.getFlexCellFormatter().setStyleName(row, 4, "flexTable-td");
        itemsTable.getFlexCellFormatter().setStyleName(row, 5, "flexTable-td");

        copyToAllButton.addClickHandler(clickEvent -> copyToAllRowWithTheseValues(row + 1, startDate.getDate(), endDate.getDate()));
    }

    private void recalculateAmountOnValueChange(int row) {

        BigDecimal initialAmount = BigDecimal.ZERO;
        for (int i = 1; i <= row; i++) {
            TextBoxWithRealValue txtPercentage = (TextBoxWithRealValue) itemsTable.getWidget(i, 2);
            TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getWidget(i, 3);

            BigDecimal amount = AccountingUtils.get().parseToBigDecimal(txtAmount.getValue());
            BigDecimal percentage = AccountingUtils.HUNDRED.multiply(amount).divide(total, 5, RoundingMode.HALF_UP);

            txtPercentage.setValue(AccountingUtils.get().format(percentage));
            txtPercentage.setRealValue(percentage);

            initialAmount = initialAmount.add(amount);
        }

        BigDecimal subTotal = total.subtract(initialAmount);
        for (int i = row + 1; i < itemsTable.getRowCount(); i++) {
            TextBoxWithRealValue txtPercentage = (TextBoxWithRealValue) itemsTable.getWidget(i, 2);
            TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getWidget(i, 3);

            BigDecimal amount = subTotal.divide(BigDecimal.valueOf(period - row), 5, RoundingMode.HALF_UP);
            BigDecimal percentage = AccountingUtils.HUNDRED.multiply(amount).divide(total, 5, RoundingMode.HALF_UP);

            txtPercentage.setRealValue(percentage);
            txtPercentage.setValue(AccountingUtils.get().format(percentage));
            txtAmount.setRealValue(amount);
            txtAmount.setValue(AccountingUtils.get().format(amount));
        }
    }

    private void copyToAllRowWithTheseValues(int row, Date startdate, Date endDate) {
        for (int i = row; i < itemsTable.getRowCount(); i++) {
            DatePicker startDatePicker = (DatePicker) itemsTable.getWidget(i, 0);
            DatePicker endDatePicker = (DatePicker) itemsTable.getWidget(i, 1);

            if (startDatePicker != null && startdate != null) {
                startDatePicker.setDate((Date) startdate.clone());
            }

            if (endDatePicker != null && endDate != null) {
                endDatePicker.setDate((Date) endDate.clone());
            }
        }
    }

    private BigDecimal getTotalAmountFromItems() {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (int row = 1; row < itemsTable.getRowCount(); row++) {
            TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getWidget(row, 3);

            totalAmount = totalAmount.add(txtAmount.getRealValue());
        }
        return totalAmount.setScale(5, RoundingMode.HALF_UP);
    }

    private BigDecimal getTotalPercentageFromItems() {
        BigDecimal totalPercent = BigDecimal.ZERO;

        for (int row = 1; row < itemsTable.getRowCount(); row++) {
            TextBoxWithRealValue txtPercent = (TextBoxWithRealValue) itemsTable.getWidget(row, 2);

            totalPercent = totalPercent.add(txtPercent.getRealValue());
        }
        return totalPercent.setScale(5, RoundingMode.HALF_UP);
    }

    public boolean validate() {
        int errors = 0;
        BigDecimal totalPercent = BigDecimal.ZERO, totalAmount = BigDecimal.ZERO;

        for (int i = 1; i < itemsTable.getRowCount(); i++) {
            int column = 0;
            DatePicker startDate = (DatePicker) itemsTable.getWidget(i, column++);
            DatePicker endDate = (DatePicker) itemsTable.getWidget(i, column++);
            TextBoxWithRealValue txtPercentage = (TextBoxWithRealValue) itemsTable.getWidget(i, column++);
            TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getWidget(i, column++);

            totalPercent = totalPercent.add(txtPercentage.getRealValue());
            totalAmount = totalAmount.add(txtAmount.getRealValue());

            if (!Validation.validateStartEndDate(startDate, endDate)) {
                errors++;
            }
            if (!Validation.validateTextBoxRequired(txtPercentage)) {
                errors++;
            }
            if (!Validation.validateTextBoxRequired(txtAmount)) {
                errors++;
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.unableToSave(), Info.Type.WARNING);
            return false;
        }

        totalPercent.setScale(5, RoundingMode.HALF_UP);
        totalAmount.setScale(5, RoundingMode.HALF_UP);

        if (totalPercent.compareTo(AccountingUtils.HUNDRED) > 0) {
            Info.show("Total percent cannot be more than 100%.", Info.Type.WARNING);
            return false;
        }

        if (totalAmount.compareTo(total) > 0) {
            Info.show("Total amount cannot be more than " + AccountingUtils.get().formatPrice(total) + ".!", Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public NewInvoice[] getInvoiceData() {
        ArrayList<NewInvoice> invoiceList = new ArrayList<>();
        BigDecimal exchangeRage = quote.getExchageRate() != null ? quote.getExchageRate() : BigDecimal.ONE;

        for (int i = 1; i < itemsTable.getRowCount(); i++) {
            int column = 0;
            DatePicker startDate = (DatePicker) itemsTable.getWidget(i, column++);
            DatePicker endDate = (DatePicker) itemsTable.getWidget(i, column++);
            TextBoxWithRealValue txtPercentage = (TextBoxWithRealValue) itemsTable.getWidget(i, column++);
            TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getWidget(i, column++);
            DataListBox statusListBox = (DataListBox) itemsTable.getWidget(i, column++);

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


            invoice.setSubtotal(txtAmount.getRealValue());
            invoice.setTotalInInvoiceCurrency(invoice.getSubtotal());
            invoice.setTotal(invoice.getSubtotal().divide(exchangeRage, AccountingUtils.getPriceScale(), RoundingMode.HALF_UP));
            invoice.setType(Constants.RECEIVABLE);
            invoice.setStatusCode(statusListBox.getSelectedId() != null ? statusListBox.getSelectedItem().getName() : Constants.DRAFT);
            invoice.setTaxCalculationType(AccountingConstants.TAX_CALCULATION_EXCLUSIVE);
            invoice.setBookkeep(true);

            invoice.setConvertedItemID(quote.getID());
            invoice.setConvertedAmount(txtAmount.getRealValue() != null ? txtAmount.getRealValue() : new BigDecimal(txtAmount.getValue()));

            invoice.setConvertedPercent(txtPercentage.getRealValue() != null ? txtPercentage.getRealValue() : new BigDecimal(txtPercentage.getValue()));
            invoice.setProgressInvoicing(true);
            invoice.setProgressInvoicingType(AccountingConstants.BY_PERCENTAGE);

            NewInvoiceItem[] items = new NewInvoiceItem[quote.getItems().length];
            for (int j = 0; j < quote.getItems().length; j++) {
                items[j] = new NewInvoiceItem();
                items[j].setItemName(quote.getItems()[j].getItemName());
                items[j].setDescription(quote.getItems()[j].getDescription());
                items[j].setAccountID(quote.getItems()[j].getAccountID());
                items[j].setProject(quote.getItems()[j].getProject());
                items[j].setQuantity(quote.getItems()[j].getQuantity().multiply(txtPercentage.getRealValue()).divide(AccountingUtils.HUNDRED, 5, RoundingMode.HALF_UP));
                items[j].setUnitPrice(quote.getItems()[j].getUnitPrice());
                items[j].setNet(items[j].getQuantity().multiply(items[j].getUnitPrice()).divide(exchangeRage, 5, RoundingMode.HALF_UP));
                items[j].setTotalAmount(items[j].getQuantity().multiply(items[j].getUnitPrice()));
            }

            invoice.setItems(items);
            invoiceList.add(invoice);
        }

        return invoiceList.toArray(new NewInvoice[]{});
    }

    private class TextBoxWithRealValue extends TextBox {
        BigDecimal realValue;

        TextBoxWithRealValue() {
            this.realValue = BigDecimal.ZERO;
        }

        public BigDecimal getRealValue() {
            return realValue;
        }

        public void setRealValue(BigDecimal realValue) {
            this.realValue = realValue;
        }
    }
}

