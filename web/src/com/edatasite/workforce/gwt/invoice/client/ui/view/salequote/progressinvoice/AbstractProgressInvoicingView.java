package com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.progressinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PostFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.calendardatepicker.CalendarUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnOffsetEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.PDFProgressInvoiceTransferObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomDatePickerCell;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public abstract class AbstractProgressInvoicingView extends FooteredView implements FittedContent, Constants {

    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private Integer objectId;
    private TextBox txtPeriod;
    private Integer limit = 100;
    private Integer period = 3;
    private HTMLPanel mainPanel;
    protected EditableTable itemsTable;
    protected BigDecimal total;
    protected NewInvoice quote;
    protected ReceiptTable receiptTable;
    protected DataListBox recurrenceType;
    protected TextBox txtMargin;
    protected GColumn gapColumn;
    private SplitButton printPdfSplitButton;

    public AbstractProgressInvoicingView(String name, Integer objectId) {
        super(name);
        this.objectId = objectId;
    }

    @Override
    protected Widget onInitialize() {
        loadData();
        return null;
    }

    public void loadData() {
        QuoteService.App.get().getQuoteSummaryData(objectId, new AsyncCallback<NewInvoice>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(NewInvoice newInvoice) {
                quote = newInvoice;
                initialize();
                pdfTool(quote);
            }
        });
    }

    public void initialize() {
        mainPanel = new HTMLPanel("");
        mainPanel.setStyleName("add-form content-box content-box--white");
        total = PARTIAL_INVOICED.equals(quote.getStatusCode()) ? quote.getTotalInInvoiceCurrency().subtract(quote.getInvoicedAmount()) : quote.getTotalInInvoiceCurrency();

        txtPeriod = new TextBox();
        txtPeriod.addChangeHandler(changeEvent -> onPeriodChange());
        txtPeriod.setText(String.valueOf(period));
        txtPeriod.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(txtPeriod, 0, false);

        TextBox txtTotalAmount = new TextBox();
        txtTotalAmount.setEnabled(false);
        txtTotalAmount.setText(AccountingUtils.get().formatPrice(total));
        txtTotalAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        recurrenceType = new DataListBox();
        recurrenceType.setWithoutNullLabel(true);
        recurrenceType.setItems(getRecurrenceTypeItems());
        recurrenceType.setSelected(3);

        txtMargin = new TextBox();
        txtMargin.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        txtMargin.setText("1");
        txtMargin.addKeyUpHandler(keyUpEvent -> setDateValuesOnChange(0));
        Validation.addNumericKeyboardListener(txtMargin, 0, false);

        itemsTable = new EditableTable(getColumns(), false, false);
        itemsTable.setDraggable(true);
        drawItemTableRows();

        initTotalsTable();

        GRow firstRow = new GRow();
        firstRow.add(new GColumn(GColumnEnum.COL_3, new FormGroup(accountingStrings.installments(), txtPeriod)));
        firstRow.add(new GColumn(GColumnEnum.COL_3, new FormGroup(wfmStrings.amount(), txtTotalAmount)));
        firstRow.add(new GColumn(GColumnEnum.COL_3, new FormGroup(wfmStrings.recurrenceType(), recurrenceType)));

        onRecurrenceTypeChange(firstRow);

        recurrenceType.addValueChangeHandler(valueChangeEvent -> onRecurrenceTypeChange(firstRow));

        Div secondRow = new Div("invoice__products-table");
        secondRow.add(itemsTable);

        GColumn totalColumn = new GColumn(GColumnEnum.COL_3);
        totalColumn.setOffset(GColumnOffsetEnum.OFFSET_9);
        totalColumn.add(receiptTable);

        GRow thirdRow = new GRow();
        thirdRow.addStyleName("mt-5");
        thirdRow.add(totalColumn);

        mainPanel.add(firstRow);
        mainPanel.add(secondRow);
        mainPanel.add(thirdRow);
        mainPanel.add(this::createFooter);
        add(mainPanel);
    }

    private void onPeriodChange() {
        if (limit < Integer.parseInt(txtPeriod.getText())) {
            Info.show(accountingMessages.maxInstallmentLimit(String.valueOf(limit)), Info.Type.WARNING);
        } else if (Integer.parseInt(txtPeriod.getText()) > 0) {
            period = Integer.parseInt(txtPeriod.getText());
            drawItemTableRows();
        }
    }

    private void onRecurrenceTypeChange(GRow row) {
        if (RecurrenceType.MANUAL.equals(recurrenceType.getSelectedId())) {
            gapColumn.removeFromParent();
        } else {
            if (gapColumn != null) {
                gapColumn.removeFromParent();
            }
            gapColumn = new GColumn(GColumnEnum.COL_2, new FormGroup(recurrenceType.getSelectedItem().getName() + " " + accountingStrings.increment(), txtMargin));
            row.add(gapColumn);
        }
    }

    private void drawItemTableRows() {
        itemsTable.removeAllRows();
        for (int i = 0; i < period; i++) {
            itemsTable.addRow(getWidgets(i));
        }
    }

    protected SelectItem[] getStatusItems() {
        return new SelectItem[]{
                new SelectItem(1, wfmStrings.draft()),
                new SelectItem(2, wfmStrings.approve())
        };
    }

    protected SelectItem[] getRecurrenceTypeItems() {
        return new SelectItem[]{
                new SelectItem(1, accountingStrings.manual()),
                new SelectItem(2, wfmStrings.day()),
                new SelectItem(3, wfmStrings.month())
        };
    }

    private void save() {
        if (!validate()) {
            return;
        }

        LoadingPanel.loading(true);
        InvoiceService.App.get().saveBatchInvoiceData(getInvoiceData(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void aVoid) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_CONVERTED_TO_SALEORDER, null, AbstractProgressInvoicingView.this);
                closeTab();
            }
        });
    }

    protected void setInitialValues(TextBoxWithRealValue txtPercentage, TextBoxWithRealValue txtAmount, Integer row) {
        BigDecimal percentage = AccountingUtils.HUNDRED.divide(BigDecimal.valueOf(period), AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP);
        if (row == period - 1) {
            percentage = AccountingUtils.HUNDRED.subtract(percentage.multiply(BigDecimal.valueOf(period - 1)));
            txtAmount.setEnabled(false);
            txtPercentage.setEnabled(false);
        }
        txtPercentage.setRealValue(percentage);
        txtPercentage.setValue(AccountingUtils.get().format(percentage));

        BigDecimal amount = total.multiply(percentage).divide(AccountingUtils.HUNDRED, AccountingUtils.calculationScale, BigDecimal.ROUND_HALF_UP);
        txtAmount.setRealValue(amount);
        txtAmount.setValue(AccountingUtils.get().formatPrice(amount));
    }

    protected void correctDiff(BigDecimal totalForDiff, boolean byPercent) {
        TextBoxWithRealValue txtPercent = (TextBoxWithRealValue) itemsTable.getColumnById(itemsTable.getRowCount() - 1, ItemTableConstants.PERCENTAGE);
        TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getColumnById(itemsTable.getRowCount() - 1, ItemTableConstants.AMOUNT);
        txtAmount.setCustomScale(true);
        CustomCell percentageCell = (CustomCell) itemsTable.getColumnCellWidgetById(itemsTable.getRowCount() - 1, ItemTableConstants.PERCENTAGE);
        CustomCell amountCell = (CustomCell) itemsTable.getColumnCellWidgetById(itemsTable.getRowCount() - 1, ItemTableConstants.AMOUNT);

        if (byPercent) {
            if (totalForDiff.compareTo(AccountingUtils.HUNDRED) != 0) {
                BigDecimal changedPercent = txtPercent.getRealValue().add(AccountingUtils.HUNDRED.subtract(totalForDiff));
                txtPercent.setRealValue(changedPercent);
                txtPercent.setText(AccountingUtils.get().format(changedPercent));
                percentageCell.InActive();
            }
        } else {
            if (totalForDiff.compareTo(total) != 0) {
                BigDecimal changedAmount = txtAmount.getRealValue().add(total.subtract(totalForDiff));
                txtAmount.setRealValue(changedAmount);
                txtAmount.setText(AccountingUtils.get().format(changedAmount));
                amountCell.InActive();
            }

        }
    }

    protected void copyStatusValues(int row) {
        DataListBox currentStatusBox = (DataListBox) itemsTable.getColumnById(row, ItemTableConstants.STATUS);
        for (int i = row + 1; i < itemsTable.getRowCount(); i++) {
            DataListBox statusBox = (DataListBox) itemsTable.getColumnById(i, ItemTableConstants.STATUS);
            statusBox.setSelected(currentStatusBox.getSelectedItem());
            itemsTable.refreshCustomCellDisplayValue(i, ItemTableConstants.STATUS);
        }
    }

    protected void copyTextBoxValues(int row, String key) {
        CustomCellTextBox currentTextBox = (CustomCellTextBox) itemsTable.getColumnById(row, key);
        for (int i = row + 1; i < itemsTable.getRowCount(); i++) {
            CustomCellTextBox textBox = (CustomCellTextBox) itemsTable.getColumnById(i, key);
            textBox.setText(currentTextBox.getText());
            itemsTable.refreshCustomCellDisplayValue(i, key);
        }
    }

    protected void copyLookUpValues(int row, String key) {
        LookUp currentLookUp = (LookUp) itemsTable.getColumnById(row, key);
        for (int i = row + 1; i < itemsTable.getRowCount(); i++) {
            LookUp lookUp = (LookUp) itemsTable.getColumnById(i, key);
            lookUp.setSelected(currentLookUp.getSelectedItem());
            LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(i, key);
            lookUpCell.InActive();
        }
    }

    protected void setDateValuesOnChange(int row) {
        CustomDatePickerCell startDate = (CustomDatePickerCell) itemsTable.getColumnById(row, ItemTableConstants.START_DATE);
        CustomDatePickerCell endDate = (CustomDatePickerCell) itemsTable.getColumnById(row, ItemTableConstants.END_DATE);
        if (startDate.getDate() != null && endDate.getDate() == null) {
            endDate.setItemValue(startDate.getDate());
            itemsTable.refreshCustomCellDisplayValue(row, ItemTableConstants.END_DATE);
        } else if (endDate.getDate() != null && startDate.getDate() == null) {
            startDate.setItemValue(endDate.getDate());
            itemsTable.refreshCustomCellDisplayValue(row, ItemTableConstants.START_DATE);
        }
        if (validateStartDateAndEndDate(startDate, endDate, row) && !RecurrenceType.MANUAL.equals(recurrenceType.getSelectedId())) {
            Date resultStartDate = startDate.getDate();
            Date resultEndDate = endDate.getDate();

            for (int i = row + 1; i < itemsTable.getRowCount(); i++) {
                if (RecurrenceType.DAY.equals(recurrenceType.getSelectedId())) {
                    CalendarUtil.addDaysToDate(resultStartDate, Integer.parseInt(txtMargin.getValue()));
                    CalendarUtil.addDaysToDate(resultEndDate, Integer.parseInt(txtMargin.getValue()));
                } else if (RecurrenceType.MONTH.equals(recurrenceType.getSelectedId())) {
                    CalendarUtil.addMonthsToDate(resultStartDate, Integer.parseInt(txtMargin.getValue()));
                    CalendarUtil.addMonthsToDate(resultEndDate, Integer.parseInt(txtMargin.getValue()));
                }
                CustomDatePickerCell startDateWidget = (CustomDatePickerCell) itemsTable.getColumnById(i, ItemTableConstants.START_DATE);
                CustomDatePickerCell endDateWidget = (CustomDatePickerCell) itemsTable.getColumnById(i, ItemTableConstants.END_DATE);

                startDateWidget.setItemValue(resultStartDate);
                endDateWidget.setItemValue(resultEndDate);

                itemsTable.refreshCustomCellDisplayValue(i, ItemTableConstants.START_DATE);
                itemsTable.refreshCustomCellDisplayValue(i, ItemTableConstants.END_DATE);
            }
        }
    }

    protected boolean validateStartDateAndEndDate(DatePicker startDate, DatePicker endDate, int row) {
        if (startDate.getDate() != null && endDate.getDate() != null && !Validation.validateStartEndDate(startDate, endDate)) {
            itemsTable.notValid(row, ItemTableConstants.START_DATE);
            itemsTable.notValid(row, ItemTableConstants.END_DATE);
            Info.show(wfmStrings.startDateNotLaterDueDate(), Info.Type.WARNING);
            return false;
        }

        return true;
    }


    protected void calculateOnValueChange(int row, boolean isAmount) {
        TextBoxWithRealValue txtCurrentPercentage = (TextBoxWithRealValue) itemsTable.getColumnById(row, ItemTableConstants.PERCENTAGE);
        TextBoxWithRealValue txtCurrentAmount = (TextBoxWithRealValue) itemsTable.getColumnById(row, ItemTableConstants.AMOUNT);
        txtCurrentAmount.setCustomScale(true);
        if (isAmount) {
            txtCurrentAmount.setRealValue(AccountingUtils.get().parseToBigDecimal(txtCurrentAmount.getValue()));
            BigDecimal percent = txtCurrentAmount.getRealValue().multiply(AccountingUtils.HUNDRED).divide(total, 5, BigDecimal.ROUND_HALF_UP);
            txtCurrentPercentage.setRealValue(percent);
            txtCurrentPercentage.setItemValue(AccountingUtils.get().format(percent));
            itemsTable.refreshCustomCellDisplayValue(row, ItemTableConstants.PERCENTAGE);
        } else {
            txtCurrentPercentage.setRealValue(AccountingUtils.get().parseToBigDecimal(txtCurrentPercentage.getValue()));
            BigDecimal amount = total.multiply(txtCurrentPercentage.getRealValue()).divide(AccountingUtils.HUNDRED, 5, BigDecimal.ROUND_HALF_UP);
            txtCurrentAmount.setRealValue(amount);
            txtCurrentAmount.setItemValue(AccountingUtils.get().formatPrice(amount, Utils.getAccountingProgressInvoiceingAmountScale() != null ? Utils.getAccountingProgressInvoiceingAmountScale() : AccountingUtils.calculationScale));
            itemsTable.refreshCustomCellDisplayValue(row, ItemTableConstants.AMOUNT);
        }

        BigDecimal initialAmountSum = BigDecimal.ZERO;
        BigDecimal initialPercentageSum = BigDecimal.ZERO;
        BigDecimal enteredAmount = BigDecimal.ZERO;
        BigDecimal enteredPercentage = BigDecimal.ZERO;
        for (int i = 0; i <= row; i++) {

            TextBoxWithRealValue txtPercentage = (TextBoxWithRealValue) itemsTable.getColumnById(i, ItemTableConstants.PERCENTAGE);
            TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getColumnById(i, ItemTableConstants.AMOUNT);
            txtAmount.setCustomScale(true);

            BigDecimal amount = AccountingUtils.get().parseToBigDecimal(txtAmount.getValue());
            BigDecimal percentage = AccountingUtils.get().parseToBigDecimal(txtPercentage.getValue());

            initialAmountSum = initialAmountSum.add(amount);
            initialPercentageSum = initialPercentageSum.add(percentage);

            enteredAmount = amount;
            enteredPercentage = percentage;
        }
        if (isAmount && initialAmountSum.compareTo(total) > 0) {
            itemsTable.notValid(row, ItemTableConstants.AMOUNT);
            Info.show("Total amount cannot be more than " + AccountingUtils.get().formatPrice(total) + "!", Info.Type.WARNING);
            return;
        } else if (!isAmount && initialPercentageSum.compareTo(AccountingUtils.HUNDRED) > 0) {
            itemsTable.notValid(row, ItemTableConstants.PERCENTAGE);
            Info.show("Total percent cannot be more than 100%.", Info.Type.WARNING);
            return;
        }

        BigDecimal remainingAmount = total.subtract(initialAmountSum);
        BigDecimal remainingPercentage = AccountingUtils.HUNDRED.subtract(initialPercentageSum);

        for (int i = row + 1; i < itemsTable.getRowCount(); i++) {
            TextBoxWithRealValue txtPercentage = (TextBoxWithRealValue) itemsTable.getColumnById(i, ItemTableConstants.PERCENTAGE);
            TextBoxWithRealValue txtAmount = (TextBoxWithRealValue) itemsTable.getColumnById(i, ItemTableConstants.AMOUNT);
            txtAmount.setCustomScale(true);
            BigDecimal amount = BigDecimal.ZERO;
            BigDecimal percentage = BigDecimal.ZERO;
            if (row == 0) {
                amount = remainingAmount.divide(BigDecimal.valueOf(period - row - 1), 5, BigDecimal.ROUND_HALF_UP);
                percentage = remainingPercentage.divide(BigDecimal.valueOf(period - row - 1), 5, BigDecimal.ROUND_HALF_UP);
            } else if (i == itemsTable.getRowCount() - 1) {
                amount = remainingAmount;
                percentage = remainingPercentage;
            } else if (remainingAmount.compareTo(enteredAmount) >= 0 && remainingPercentage.compareTo(enteredPercentage) >= 0) {
                amount = enteredAmount;
                percentage = enteredPercentage;
                remainingPercentage = remainingPercentage.subtract(enteredPercentage);
                remainingAmount = remainingAmount.subtract(enteredAmount);
            }

            txtPercentage.setRealValue(percentage);
            txtPercentage.setValue(AccountingUtils.get().format(percentage));
            txtAmount.setRealValue(amount);
            txtAmount.setValue(AccountingUtils.get().formatPrice(amount, Utils.getAccountingProgressInvoiceingAmountScale() != null ? Utils.getAccountingProgressInvoiceingAmountScale() : AccountingUtils.calculationScale));

            itemsTable.refreshCustomCellDisplayValue(i, ItemTableConstants.AMOUNT);
            itemsTable.refreshCustomCellDisplayValue(i, ItemTableConstants.PERCENTAGE);
        }
    }

    protected void initTotalsTable() {
        receiptTable = new ReceiptTable(false);
        receiptTable.clear();
        receiptTable.removeShippingBody();
        receiptTable.addGrossItemWithStringValues(accountingStrings.totalwithpersentage() + " : ", AccountingUtils.get().format(AccountingConstants.HUNDRED));
        receiptTable.addGrossItemWithStringValues(wfmStrings.totalAmount() + " : ", AccountingUtils.get().formatPrice(total));
    }

    public ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return AbstractProgressInvoicingView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> result = new ArrayList<>();

        Div applyWrapper = new Div();
        Div pdfSplitWrapper = new Div();

        WfmButton2 applyButton = new WfmButton2(wfmStrings.proceed(), WfmButton2.BTN_PRIMARY);
        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);


        applyButton.addClickHandler(clickEvent -> save());

        applyWrapper.add(applyButton);
        pdfSplitWrapper.add(printPdfSplitButton);

        result.add(pdfSplitWrapper);
        result.add(applyWrapper);

        return result;
    }

    private void pdfTool(NewInvoice data) {
        if (printPdfSplitButton == null) {
            return;
        }
        Integer defaultTemplateId = null;
        List<SplitButtonItem> pdfButtonItems = new ArrayList<>();

        if (data != null
                && data.getProgressInvoicePDFTemplateList() != null
                && data.getProgressInvoicePDFTemplateList().getItems() != null
                && data.getProgressInvoicePDFTemplateList().getItems().length > 0) {

            for (SelectItem pdfItem : data.getProgressInvoicePDFTemplateList().getItems()) {

                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfButtonItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(mainPanel, pdfItem.getId())));
            }
        }

        Integer finalDefaultTemplateId = defaultTemplateId;
        pdfButtonItems.add(new SplitButtonItem("PDF_VERSION", wfmStrings.pdfVersion(), () -> generatePDF(mainPanel, data != null && data.getPdfTemplateID() != null ? data.getPdfTemplateID() : finalDefaultTemplateId), true));

        printPdfSplitButton.addItemList(pdfButtonItems);
    }

    private void generatePDF(Panel hp, Integer pdfTemplateID) {
        String pdfURL = CommandConstants.PDF_URL + "/progressInvoicingViewPDFHandler";
        PostFormPanel post = new PostFormPanel(pdfURL, "_blank");
        hp.add(post);
        quote.setPdfTemplateID(pdfTemplateID);
        new PDFProgressInvoiceTransferObject(post, getInvoiceData(), quote);
        post.submit();
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
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

    protected abstract ColumnConfig[] getColumns();

    protected abstract Widget[] getWidgets(int row);

    protected abstract boolean validate();

    protected abstract NewInvoice[] getInvoiceData();

    protected class TextBoxWithRealValue extends TextBox implements CustomCellInterface {

        BigDecimal realValue;
        boolean customScale;

        TextBoxWithRealValue() {
            this.realValue = BigDecimal.ZERO;
        }

        TextBoxWithRealValue(boolean customScale) {
            this.realValue = BigDecimal.ZERO;
            this.customScale = customScale;
        }

        public BigDecimal getRealValue() {
            return realValue;
        }

        public void setRealValue(BigDecimal realValue) {
            this.realValue = realValue;
        }

        public BigDecimal getBigDecimalValue() {
            if (isCustomScale()) {
                return AccountingUtils.get().parseToBigDecimal(getRealValue().toString(), Utils.getCalculationNumberFormatWithCustomScale(Utils.getAccountingProgressInvoiceingAmountScale() != null ? Utils.getAccountingProgressInvoiceingAmountScale() : AccountingUtils.calculationScale));
            } else {
                return AccountingUtils.get().parseToBigDecimal(getText());
            }
        }

        @Override
        public String getDisplayValue() {

            if (isCustomScale()) {
                return Utils.getCalculationNumberFormatWithCustomScale(Utils.getAccountingProgressInvoiceingAmountScale() != null ? Utils.getAccountingProgressInvoiceingAmountScale() : AccountingUtils.calculationScale).format(getBigDecimalValue());
            } else {
                return AccountingUtils.get().format(getBigDecimalValue());
            }

        }

        @Override
        public void setItemValue(Object value) {
            setText(String.valueOf(value));
        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }

        public boolean isCustomScale() {
            return this.customScale;
        }

        public void setCustomScale(final boolean customScale) {
            this.customScale = customScale;
        }
    }
}
