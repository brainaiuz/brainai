package com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.progressinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Normurod on 2/9/16.
 */
public class CustomizedProgressInvoice extends Composite implements Constants {
    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private final ScrollPanel panel;
    private TextBox txtPeriod;
    private TextBox txtTotal;
    private TextBox txtTotalAmount;
    private final BigDecimal total;
    private Integer period = 1;
    private final Integer limit = 60;

    private FlexTable itemsTable;
    private final NewInvoice quote;

    CustomizedProgressInvoice(NewInvoice quote) {
        this.quote = quote;
        this.total = PARTIAL_INVOICED.equals(quote.getStatusCode()) ? quote.getTotalInInvoiceCurrency().subtract(quote.getInvoicedAmount()) : quote.getTotal();
        panel = new ScrollPanel();
        //panel.setWidth("800px");
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

                if (Integer.parseInt(txtPeriod.getText()) != 0) {
                    period = Integer.parseInt(txtPeriod.getText());
                }
            }
        });

        TextBoxWithRealValue txtTotal = new TextBoxWithRealValue();
        txtTotal.setWidth("200px");
        txtTotal.setEnabled(false);

        if (total != null) {
            txtTotal.setRealValue(total);
            txtTotal.setValue(AccountingUtils.get().formatPrice(total));
        }
        Validation.addNumericKeyboardListener(txtTotal, 2);

        txtTotalAmount = new TextBox();
        txtTotalAmount.setWidth("200px");
        txtTotalAmount.setEnabled(false);
        txtTotalAmount.setValue(AccountingUtils.get().formatPrice(BigDecimal.ZERO));

        Validation.addNumericKeyboardListener(txtTotal, 2);

        VerticalPanel pnlContainer = new VerticalPanel();
        pnlContainer.setSpacing(5);
        panel.add(pnlContainer);

        FlexTable table = new FlexTable();
        table.setWidth("100%");
        table.setCellPadding(0);
        table.setCellSpacing(0);
        txtPeriod.setTitle("test");
        table.setWidget(0, 0, new HTML("<b style=\"margin-right: 10px\">" + wfmStrings.period() + " (max: " + limit + ")" + ":</b>"));
        table.setWidget(0, 1, txtPeriod);
        table.setWidget(0, 2, new HTML("<b style=\"margin-right: 10px\">" + wfmStrings.amount() + ":</b>"));
        table.setWidget(0, 3, txtTotal);
        table.getFlexCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
        table.getFlexCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
        table.getFlexCellFormatter().setAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
        table.getFlexCellFormatter().setAlignment(0, 3, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
        table.setStyleName("mb-3");
        pnlContainer.add(table);

        itemsTable = new FlexTable();
        itemsTable.setStyleName("flexTable");
        itemsTable.setCellPadding(0);
        itemsTable.setCellSpacing(0);

        itemsTable.setWidget(0, 0, new HTML(wfmStrings.itemName()));
        itemsTable.setWidget(0, 1, new HTML(wfmStrings.description()));
        itemsTable.setWidget(0, 2, new HTML(wfmStrings.startDate()));
        itemsTable.setWidget(0, 3, new HTML(wfmStrings.endDate()));
        itemsTable.setWidget(0, 4, new HTML(wfmStrings.account()));
        itemsTable.setWidget(0, 5, new HTML(wfmStrings.percentage()));
        itemsTable.setWidget(0, 6, new HTML(wfmStrings.amount()));
        itemsTable.setWidget(0, 7, new HTML(Property.get(Constants.PROJECT, wfmStrings.project())));
        itemsTable.setWidget(0, 8, new HTML(wfmStrings.status()));
        itemsTable.setWidget(0, 9, new HTML(""));
        itemsTable.getFlexCellFormatter().setStyleName(0, 0, "flexTable-Label");
        itemsTable.getFlexCellFormatter().setStyleName(0, 1, "flexTable-Label");
        itemsTable.getFlexCellFormatter().setStyleName(0, 2, "flexTable-Label");
        itemsTable.getFlexCellFormatter().setStyleName(0, 3, "flexTable-Label");
        itemsTable.getFlexCellFormatter().setStyleName(0, 4, "flexTable-Label");
        itemsTable.getFlexCellFormatter().setStyleName(0, 5, "flexTable-Label");
        itemsTable.getFlexCellFormatter().setStyleName(0, 6, "flexTable-Label");
        itemsTable.getFlexCellFormatter().setStyleName(0, 7, "flexTable-Label");
        itemsTable.getFlexCellFormatter().setStyleName(0, 8, "flexTable-Label");
        itemsTable.getFlexCellFormatter().setStyleName(0, 9, "flexTable-Label");
        itemsTable.setStyleName("mb-3");
        pnlContainer.add(itemsTable);

        addItemsRow();

        FlexTable totalTable = new FlexTable();
        totalTable.setCellPadding(0);
        totalTable.setCellSpacing(0);
        totalTable.setWidget(0, 0, new HTML("<b style=\"margin-right: 10px\">" + wfmStrings.total() + ":</b>"));
        totalTable.setWidget(0, 1, txtTotalAmount);
        totalTable.getFlexCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
        totalTable.getFlexCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
        totalTable.getElement().setAttribute("align", "right");
        totalTable.getElement().getStyle().setProperty("width", "auto");
        pnlContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        pnlContainer.add(totalTable);
    }

    private void addItemsRow() {
        final TextBox txtName = new TextBox();
        txtName.setWidth("150px");

        final TextBox txtDescription = new TextBox();
        txtDescription.setWidth("250px");

        final DatePicker startDate = new DatePicker();
        startDate.setWidth("100px");

        final DatePicker endDate = new DatePicker();
        endDate.setWidth("100px");

        final AccountsLookUp accountsList = new AccountsLookUp(Constants.RECEIVABLE);
        accountsList.getSuggestBox().setWidth("120px");

        final TextBoxWithRealValue txtPercentage = new TextBoxWithRealValue();
        txtPercentage.setWidth("60px");
        Validation.addNumericKeyboardListener(txtPercentage, 4);

        final TextBoxWithRealValue txtAmount = new TextBoxWithRealValue();
        Validation.addNumericKeyboardListener(txtAmount, AccountingUtils.calculationScale);
        txtAmount.setWidth("100px");

        final ProjectLookUp projectLookUp = new ProjectLookUp(Constants.RECEIVABLE, null);
        projectLookUp.setClientSupplierID(quote.getClientID());
        projectLookUp.getSuggestBox().setWidth("100px");

        DataListBox statusListBox = new DataListBox();
        statusListBox.setWithoutNullLabel(true);
        SelectItem[] statusItems = new SelectItem[]{
                new SelectItem(1, Constants.DRAFT),
                new SelectItem(2, Constants.APPROVE)
        };
        statusListBox.setItems(statusItems);
        statusListBox.setSelected(2);
        statusListBox.setWidth("100px");

        txtPercentage.addChangeHandler(changeEvent -> {
            txtPercentage.setRealValue(new BigDecimal(txtPercentage.getValue()));
            BigDecimal totalPercents = getTotalPercentageFromItems();

            if (totalPercents.compareTo(AccountingUtils.HUNDRED) > 0) {
                txtPercentage.setStyleName("x-form-invalid");
                Info.show("Total percent cannot be more than 100%.", Info.Type.WARNING);
            } else {
                txtPercentage.removeStyleName("x-form-invalid");
                BigDecimal amount = total.multiply(txtPercentage.getRealValue()).divide(AccountingUtils.HUNDRED, 5, RoundingMode.HALF_UP);
                txtAmount.setRealValue(amount);
                txtAmount.setText(AccountingUtils.get().formatPrice(amount));
//                correctDiff(true);
                txtTotalAmount.setText(AccountingUtils.get().format(totalPercents));
            }
        });
        txtAmount.addChangeHandler(changeEvent -> {
            txtAmount.setRealValue(new BigDecimal(txtAmount.getValue()));
            BigDecimal totalAmount = getTotalAmountFromItems();

            if (totalAmount.compareTo(total) > 0) {
                txtAmount.setStyleName("x-form-invalid");
                Info.show("Total amount cannot be more than " + AccountingUtils.get().formatPrice(total) + "!", Info.Type.WARNING);
            } else {
                txtAmount.removeStyleName("x-form-invalid");
                BigDecimal percent = txtAmount.getRealValue().multiply(AccountingUtils.HUNDRED).divide(total, 5, RoundingMode.HALF_UP);
                txtPercentage.setRealValue(percent);
                txtPercentage.setText(AccountingUtils.get().format(percent));
//                correctDiff(false);
                txtTotalAmount.setText(AccountingUtils.get().format(totalAmount));
            }
        });

        ActionButton copyToAllButton = new ActionButton("&nbsp;", "icon-copy2");
        copyToAllButton.ensureDebugId("copy");
        copyToAllButton.setTitle(wfmStrings.copyToAll());
        copyToAllButton.setVisible(true);

        final int row = itemsTable.getRowCount();
        itemsTable.setWidget(row, 0, txtName);
        itemsTable.setWidget(row, 1, txtDescription);
        itemsTable.setWidget(row, 2, startDate);
        itemsTable.setWidget(row, 3, endDate);
        itemsTable.setWidget(row, 4, accountsList);
        itemsTable.setWidget(row, 5, txtPercentage);
        itemsTable.setWidget(row, 6, txtAmount);
        itemsTable.setWidget(row, 7, projectLookUp);
        itemsTable.setWidget(row, 8, statusListBox);
        itemsTable.setWidget(row, 9, copyToAllButton);
        itemsTable.getFlexCellFormatter().setStyleName(row, 0, "flexTable-td");
        itemsTable.getFlexCellFormatter().setStyleName(row, 1, "flexTable-td");
        itemsTable.getFlexCellFormatter().setStyleName(row, 2, "flexTable-td");
        itemsTable.getFlexCellFormatter().setStyleName(row, 3, "flexTable-td");
        itemsTable.getFlexCellFormatter().setStyleName(row, 4, "flexTable-td");
        itemsTable.getFlexCellFormatter().setStyleName(row, 5, "flexTable-td");
        itemsTable.getFlexCellFormatter().setStyleName(row, 6, "flexTable-td");
        itemsTable.getFlexCellFormatter().setStyleName(row, 7, "flexTable-td");
        itemsTable.getFlexCellFormatter().setStyleName(row, 8, "flexTable-td");
        itemsTable.getFlexCellFormatter().setStyleName(row, 9, "flexTable-td");

        copyToAllButton.addClickHandler(clickEvent -> copyToAllRowWithTheseValues(row + 1, txtName.getText(), txtDescription.getText(), startDate.getDate(), endDate.getDate(), accountsList.getSelectedItem(), projectLookUp.getSelectedItem()));
    }

    private void copyToAllRowWithTheseValues(int row, String name, String description, Date startdate, Date endDate, SelectItem account, SelectItem project) {
        for (int i = row; i < itemsTable.getRowCount(); i++) {
            TextBox txtItem = (TextBox) itemsTable.getWidget(i, 0);
            TextBox txtDescription = (TextBox) itemsTable.getWidget(i, 1);
            DatePicker startDatePicker = (DatePicker) itemsTable.getWidget(i, 2);
            DatePicker endDatePicker = (DatePicker) itemsTable.getWidget(i, 3);
            AccountsLookUp accountsLookUp = (AccountsLookUp) itemsTable.getWidget(i, 4);
            ProjectLookUp projectLookUp = (ProjectLookUp) itemsTable.getWidget(i, 7);

            if (txtItem != null && name != null) {
                txtItem.setText(name);
            }
            if (txtDescription != null && description != null) {
                txtDescription.setText(description);
            }
            if (startDatePicker != null && startdate != null) {
                startDatePicker.setDate((Date) startdate.clone());
            }
            if (endDatePicker != null && endDate != null) {
                endDatePicker.setDate((Date) endDate.clone());
            }
            if (accountsLookUp != null && account != null) {
                accountsLookUp.addItem(account);
            }
            if (projectLookUp != null && project != null) {
                projectLookUp.addItem(project);
            }
        }
    }

    private BigDecimal getTotalAmountFromItems() {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (int row = 1; row < itemsTable.getRowCount(); row++) {
            TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getWidget(row, 6);

            totalAmount = totalAmount.add(txtAmount.getRealValue());
        }
        return totalAmount.setScale(5, RoundingMode.HALF_UP);
    }

    private BigDecimal getTotalPercentageFromItems() {
        BigDecimal totalPercent = BigDecimal.ZERO;

        for (int row = 1; row < itemsTable.getRowCount(); row++) {
            TextBoxWithRealValue txtPercent = (TextBoxWithRealValue) itemsTable.getWidget(row, 5);

            totalPercent = totalPercent.add(txtPercent.getRealValue());
        }
        return totalPercent.setScale(5, RoundingMode.HALF_UP);
    }

    private void correctDiff(BigDecimal totalForDiff, boolean byPercent) {
        TextBoxWithRealValue txtPercent = (TextBoxWithRealValue) itemsTable.getWidget(itemsTable.getRowCount() - 1, 5);
        TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getWidget(itemsTable.getRowCount() - 1, 6);

        if (byPercent) {
            if (totalForDiff.compareTo(AccountingUtils.HUNDRED) != 0) {
                BigDecimal changedPercent = txtPercent.getRealValue().add(AccountingUtils.HUNDRED.subtract(totalForDiff));
                txtPercent.setRealValue(changedPercent);
                txtPercent.setText(AccountingUtils.get().format(changedPercent));
            }
        } else {
            if (totalForDiff.compareTo(total) != 0) {
                BigDecimal changedAmount = txtAmount.getRealValue().add(total.subtract(totalForDiff));
                txtAmount.setRealValue(changedAmount);
                txtAmount.setText(AccountingUtils.get().format(changedAmount));
            }

        }
    }

    public boolean validate() {
        int errors = 0;
        BigDecimal totalPercent = BigDecimal.ZERO, totalAmount = BigDecimal.ZERO;

        for (int i = 1; i < itemsTable.getRowCount(); i++) {
            TextBox txtItem = (TextBox) itemsTable.getWidget(i, 0);
            DatePicker startDate = (DatePicker) itemsTable.getWidget(i, 2);
            DatePicker endDate = (DatePicker) itemsTable.getWidget(i, 3);
            AccountsLookUp accountsLookUp = (AccountsLookUp) itemsTable.getWidget(i, 4);
            TextBoxWithRealValue txtPercentage = (TextBoxWithRealValue) itemsTable.getWidget(i, 5);
            TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getWidget(i, 6);

            totalPercent = totalPercent.add(txtPercentage.getRealValue());
            totalAmount = totalAmount.add(txtAmount.getRealValue());

            if (!Validation.validateTextBoxRequired(txtItem)) {
                errors++;
            }
            if (!Validation.validateStartEndDate(startDate, endDate)) {
                errors++;
            }
            if (!Validation.validateLookUpRequired(accountsLookUp)) {
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

        totalPercent = totalPercent.setScale(5, RoundingMode.HALF_UP);
        totalAmount = totalAmount.setScale(5, RoundingMode.HALF_UP);

        if (totalPercent.compareTo(AccountingUtils.HUNDRED) > 0) {
            correctDiff(totalPercent, true);
//            Info.show("Total percent cannot be more than 100%.", Info.Type.WARNING);
//            return false;
        }
        if (totalPercent.subtract(AccountingUtils.HUNDRED).compareTo(BigDecimal.valueOf(0.1)) <= 0
                && totalPercent.subtract(AccountingUtils.HUNDRED).compareTo(BigDecimal.valueOf(-0.1)) >= 0) {
            correctDiff(totalPercent, true);
        }

        if (totalAmount.compareTo(total) > 0) {
            correctDiff(totalAmount, false);
//            Info.show("Total amount cannot be more than " + AccountingUtils.get().formatPrice(total) + ".!", Info.Type.WARNING);
//            return false;
        }
        if (totalAmount.subtract(total).compareTo(BigDecimal.valueOf(0.1)) <= 0
                && totalAmount.subtract(total).compareTo(BigDecimal.valueOf(-0.1)) >= 0) {
            correctDiff(totalAmount, false);
        }

        return true;
    }

    public NewInvoice[] getInvoiceData() {
        ArrayList<NewInvoice> invoiceList = new ArrayList<>();

        for (int i = 1; i < itemsTable.getRowCount(); i++) {
            TextBox txtItem = (TextBox) itemsTable.getWidget(i, 0);
            TextBox txtDescription = (TextBox) itemsTable.getWidget(i, 1);
            DatePicker startDate = (DatePicker) itemsTable.getWidget(i, 2);
            DatePicker endDate = (DatePicker) itemsTable.getWidget(i, 3);
            AccountsLookUp accountsLookUp = (AccountsLookUp) itemsTable.getWidget(i, 4);
            TextBoxWithRealValue txtPercentage = (TextBoxWithRealValue) itemsTable.getWidget(i, 5);
            TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getWidget(i, 6);
            ProjectLookUp projectLookUp = (ProjectLookUp) itemsTable.getWidget(i, 7);
            DataListBox statusListBox = (DataListBox) itemsTable.getWidget(i, 8);

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
            invoice.setStatusCode(statusListBox.getSelectedId() != null ? statusListBox.getSelectedItem().getName() : Constants.DRAFT);
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
