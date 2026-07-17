package com.edatasite.workforce.gwt.payroll.client.ui.view.additionalpayment;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.FooteredCustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.widget.LinkCellWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollPaymentItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollServiceAsync;
import com.edatasite.workforce.gwt.payroll.client.rpc.SaveResultTO;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.groupPayrun.GroupPayrunContants;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.payment.PayrunPaymentConstants;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.widgets.ExtendedHTMLCell;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PayrollPaymentAddView extends FooteredCustomForm implements Constants, Colapse, FittedContent {
    protected static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    protected static final PayrollServiceAsync payrollService = PayrollService.App.get();

    protected Integer additionalPaymentID;
    protected ListingFilterParameter filterParameter;

    protected EditableTable employeeTable;
    protected EditableGrid grid;

    protected DatePicker paymentDate;
    protected PaymentAccountsLookUp paidFromAccountLookUp;
    protected AccountsLookUp paidToAccountLookUp;
    protected TextBox detailsBox;

    protected CurrencyWidget currencyWidget;

    protected HTML totalLabel;
    protected ExtendedHTMLCell totalAmount;
    protected HTML totalInBaseLabel;
    protected ExtendedHTMLCell totalInBaseAmount;
    protected ReceiptTable totalTable;

    private TextBox tableSearchBox;
    private DataListBox tableLimitListBox;
    private TextBox tableCurrentBox;
    private MaterialLink tablePagingResult;
    private Integer totalTableItems = 0;
    private Integer tableCurrent = 0;
    private Integer tableStart = 0;

    private final Div buttonsPanel = new Div();
    private WfmButton2 saveButton;

    protected Map<String, ColumnConfig> columnsMap;

    protected Map<Integer, PayrollPaymentItem> itemMap = new HashMap<>();
    private final HashMap<Integer, PayrollPaymentItem> changedItemMap = new HashMap<>();
    private final HashMap<Integer, Boolean> deletedItemMap = new HashMap<>();
    protected PayrollPayment paymentObject;

    public PayrollPaymentAddView(Integer additionalPaymentID) {
        this("add");
        this.additionalPaymentID = additionalPaymentID;
        setDescription(wfmStrings.payment());
    }

    protected PayrollPaymentAddView(String name) {
        super(name);
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        loadData();
        return null;
    }

    protected void loadData() {
        ListingFilterParameter filterParameter = getFilterParameter();
        payrollService.initPayrollPayment(filterParameter, new AsyncCallback<PayrollPayment>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(PayrollPayment result) {
                paymentObject = result;
                setPaginationData(result);
                setTableData(result);
                setTotalData(result.getAmount());
                show();
            }
        });
    }

    protected void initialize() {
        initTopPanel();
        initSearchPanel();

        initEmployeeTable();
        initPaginationWidgets();

        registerEventHandlers();
        initTotalTable();
        initButtons();
    }

    private void initTopPanel() {
        paymentDate = new DatePicker(true);
        paymentDate.setDate(new Date());

        paidFromAccountLookUp = new PaymentAccountsLookUp(true);
        paidFromAccountLookUp.ensureDebugId("debitToAccount");
        paidFromAccountLookUp.addStyleName(DEFAULT_WIDTH);
        paidFromAccountLookUp.getSuggestBox().addSelectionHandler((p) -> selectDefaultAccount());

        paidToAccountLookUp = new AccountsLookUp("CURRENT_LIABILITY");
        paidToAccountLookUp.ensureDebugId("creditToAccount");
        paidToAccountLookUp.addStyleName(DEFAULT_WIDTH);
        paidToAccountLookUp.getSuggestBox().addSelectionHandler((p) -> selectDefaultAccount());

        detailsBox = new TextBox();

        currencyWidget = new CurrencyWidget(false);
        currencyWidget.setEnabled(false);
        currencyWidget.setDatePicker(paymentDate, paymentObject != null && paymentObject.getMultiCurrencyEnabled());

        addField(PAYRUN_PAYMENT.PAYMENT_DATE, new FormGroup(wfmStrings.paymentDate(), paymentDate, true));
        addField(PAYRUN_PAYMENT.BANK_ACCOUNT, new FormGroup(wfmStrings.paidFrom(), paidFromAccountLookUp, true));
        addField(PAYRUN_PAYMENT.PAID_TO_ACCOUNT, new FormGroup(wfmStrings.paidTo(), paidToAccountLookUp, true));
        addField(PAYRUN_PAYMENT.DETAILS, new FormGroup(wfmStrings.details(), detailsBox));
    }

    private void selectDefaultAccount() {
        if (employeeTable != null) {
            for (int i = 0; i < employeeTable.getRowCount(); i++) {
                PaymentAccountsLookUp paidFromAccountLookUp = (PaymentAccountsLookUp) employeeTable.getColumnById(i, PayrunPaymentConstants.PAID_FROM);
                AccountsLookUp paidToAccountLookUp = (AccountsLookUp) employeeTable.getColumnById(i, PayrunPaymentConstants.PAID_TO);
                if (paidFromAccountLookUp != null && this.paidFromAccountLookUp != null && this.paidFromAccountLookUp.getSelectedItemID() != null) {
                    paidFromAccountLookUp.addItem(this.paidFromAccountLookUp.getSelectedItem());
                    paidFromAccountLookUp.setSelected(this.paidFromAccountLookUp.getSelectedItem());
                    LookUpCell lookUpCell = (LookUpCell) employeeTable.getColumnCellWidgetById(i, PayrunPaymentConstants.PAID_FROM);
                    lookUpCell.InActive();
                }
                if (paidToAccountLookUp != null && this.paidToAccountLookUp != null && this.paidToAccountLookUp.getSelectedItemID() != null) {
                    paidToAccountLookUp.addItem(this.paidToAccountLookUp.getSelectedItem());
                    paidToAccountLookUp.setSelected(this.paidToAccountLookUp.getSelectedItem());
                    LookUpCell lookUpCell = (LookUpCell) employeeTable.getColumnCellWidgetById(i, PayrunPaymentConstants.PAID_TO);
                    lookUpCell.InActive();
                }
            }
        }
    }

    private void initSearchPanel() {
        tableSearchBox = new TextBox();
        tableSearchBox.addStyleName("gwt-SuggestBox");
        tableSearchBox.setPlaceHolder(wfmStrings.searchEmployee());
        tableSearchBox.addKeyDownHandler((event) -> {
            if (event.getNativeKeyCode() == 13) {
                String searchtext = tableSearchBox.getText();

                if ((searchtext == null || searchtext.trim().isEmpty()) && filterParameter != null && filterParameter.getSqlSearchKey() == null) {
                    return;
                }
                loadData();
            }
        });
        Span btnSearch = new Span();
        btnSearch.setStyleName("caret");
        btnSearch.ensureDebugId("searchForm__btn");
        btnSearch.addClickHandler((event) -> {
            String searchtext = tableSearchBox.getText();

            if (searchtext == null || searchtext.trim().isEmpty()) {
                return;
            }
            loadData();
        });
        Div searchDiv = new Div("simpleGwt-ComboBox");
        searchDiv.add(tableSearchBox);
        searchDiv.add(btnSearch);
        addField(SEARCH, new FormGroup(searchDiv));
    }

    protected void initEmployeeTable() {
        setColumns();
        employeeTable = new EditableTable(getColumns());
        grid = employeeTable.getGrid();

        Div div = new Div();
        div.setStyleName("scroll-box--x");
        div.add(employeeTable);

        addField(PAYRUN_PAYMENT.PAYSLIP_TABLE, div);
    }

    private ColumnConfig[] getColumns() {
        List<ColumnConfig> columns = new ArrayList<>();

        if (columnsMap.containsKey(PayrunPaymentConstants.EMPLOYEE)) {
            columns.add(columnsMap.get(PayrunPaymentConstants.EMPLOYEE));
        }
        if (columnsMap.containsKey(PayrunPaymentConstants.REFERENCE)) {
            columns.add(columnsMap.get(PayrunPaymentConstants.REFERENCE));
        }
        if (columnsMap.containsKey(PayrunPaymentConstants.DUE_DATE)) {
            columns.add(columnsMap.get(PayrunPaymentConstants.DUE_DATE));
        }
        if (columnsMap.containsKey(PayrunPaymentConstants.PAID_FROM)) {
            columns.add(columnsMap.get(PayrunPaymentConstants.PAID_FROM));
        }
        if (columnsMap.containsKey(PayrunPaymentConstants.PAID_TO)) {
            columns.add(columnsMap.get(PayrunPaymentConstants.PAID_TO));
        }
        if (columnsMap.containsKey(PayrunPaymentConstants.DETAILS)) {
            columns.add(columnsMap.get(PayrunPaymentConstants.DETAILS));
        }
        if (columnsMap.containsKey(PayrunPaymentConstants.DUE_AMOUNT)) {
            columns.add(columnsMap.get(PayrunPaymentConstants.DUE_AMOUNT));
        }
        if (columnsMap.containsKey(PayrunPaymentConstants.PAYMENT_AMOUNT)) {
            columns.add(columnsMap.get(PayrunPaymentConstants.PAYMENT_AMOUNT));
        }
        return columns.toArray(new ColumnConfig[]{});
    }

    protected void setColumns() {
        columnsMap = new HashMap<>();
        columnsMap.put(PayrunPaymentConstants.EMPLOYEE, new ColumnConfig(LinkableCell.class, PayrunPaymentConstants.EMPLOYEE, wfmStrings.employee(), 180, true, Constants.LEFT_ALIGN_CELL));
        columnsMap.put(PayrunPaymentConstants.REFERENCE, new ColumnConfig(CustomCell.class, PayrunPaymentConstants.REFERENCE, wfmStrings.reference(), 80, true, Constants.LEFT_ALIGN_CELL));
        columnsMap.put(PayrunPaymentConstants.DUE_DATE, new ColumnConfig(CustomCell.class, PayrunPaymentConstants.DUE_DATE, wfmStrings.dueDate(), 80, true, Constants.RIGHT_ALIGN_CELL));
        columnsMap.put(PayrunPaymentConstants.PAID_FROM, new ColumnConfig(LookUpCell.class, PayrunPaymentConstants.PAID_FROM, wfmStrings.paidFrom(), 120, true, Constants.LEFT_ALIGN_CELL));
        columnsMap.put(PayrunPaymentConstants.PAID_TO, new ColumnConfig(LookUpCell.class, PayrunPaymentConstants.PAID_TO, wfmStrings.paidTo(), 120, true, Constants.LEFT_ALIGN_CELL));
        columnsMap.put(PayrunPaymentConstants.DETAILS, new ColumnConfig(CustomCell.class, PayrunPaymentConstants.DETAILS, wfmStrings.details(), 120, false, Constants.LEFT_ALIGN_CELL));
        columnsMap.put(PayrunPaymentConstants.DUE_AMOUNT, new ColumnConfig(CustomCell.class, PayrunPaymentConstants.DUE_AMOUNT, wfmStrings.dueAmount(), 120, false, Constants.RIGHT_ALIGN_CELL));
        columnsMap.put(PayrunPaymentConstants.PAYMENT_AMOUNT, new ColumnConfig(CustomCell.class, PayrunPaymentConstants.PAYMENT_AMOUNT, wfmStrings.paymentAmount(), 120, false, Constants.RIGHT_ALIGN_CELL));
    }

    private void initPaginationWidgets() {
        if (tableLimitListBox == null) {
            tableLimitListBox = new DataListBox();
            tableLimitListBox.setWithoutNullLabel(true);
            tableLimitListBox.setItems(new SelectItem[]{
                    new SelectItem(10, "10"),
                    new SelectItem(20, "20"),
                    new SelectItem(30, "30"),
                    new SelectItem(50, "50"),
            });
            tableLimitListBox.setSelected(new SelectItem(20, "20"));
        }
        if (tableCurrentBox == null) {
            tableCurrentBox = new TextBox();
            tableCurrentBox.setStyleName("currLoc form-control");
            tableCurrentBox.setValue(tableCurrent.toString());
        }
        if (tablePagingResult == null) {
            tablePagingResult = new MaterialLink();
            tablePagingResult.setHref("javascript:void(0)");
            tablePagingResult.setClass("btn btn--white");
            tablePagingResult.setText("0 - 0 of 0");
        }
    }

    protected void registerEventHandlers() {
        employeeTable.setRemoveRowListener(() -> {
            if (grid.getRowCount() > 1) {
                deleteTableItem(grid.getCurrentRow());
                grid.getModel().removeRow(grid.getCurrentRow());
            } else {
                WfmWindow.alert(wfmStrings.youCanNotRemoveOneLineItem());
            }
        });
    }

    protected Object[] getWidgets(PayrollPaymentItem item) {
        List<Widget> widgets = new ArrayList<>();
        itemMap.put(item.getEmployeeID(), item);

        LinkCellWidget employeeCell = new LinkCellWidget(item.getEmployee(), null);
        employeeCell.setItem(new SelectItem(item.getEmployeeID(), item.getEmployee()));
        widgets.add(employeeCell);

        if (columnsMap.containsKey(PayrunPaymentConstants.REFERENCE)) {
            EditableTextBox reference = new EditableTextBox();
            reference.addValueChangeHandler(event -> saveTableItem(item.getEmployeeID(), reference.getDisplayValue(), PayrunPaymentConstants.REFERENCE));

            if (item.getReference() != null) {
                reference.setText(item.getReference());
            }
            widgets.add(reference);
        }

        if (columnsMap.containsKey(PayrunPaymentConstants.DUE_DATE)) {
            CustomCellLabel dueDate = new CustomCellLabel();
            if (item.getDueDate() != null) {
                dueDate.setText(DateUtils.format(item.getDueDate()));
            }
            widgets.add(dueDate);
        }

        if (columnsMap.containsKey(PayrunPaymentConstants.PAID_FROM)) {
            PaymentAccountsLookUp paidFromAccountLookUp = new PaymentAccountsLookUp(true);
            paidFromAccountLookUp.getSuggestBox().addSelectionHandler(event -> saveTableItem(item.getEmployeeID(), paidFromAccountLookUp.getSelectedItemID(), PayrunPaymentConstants.PAID_FROM));

            if (item.getPaidFromAccountID() != null) {
                paidFromAccountLookUp.addItem(item.getPaidFromAccount());
                paidFromAccountLookUp.setSelected(item.getPaidFromAccountID());
            } else if (this.paidFromAccountLookUp.getSelectedItemID() != null) {
                SelectItem paidFromAccount = this.paidFromAccountLookUp.getSelectedItem();

                paidFromAccountLookUp.addItem(paidFromAccount);
                paidFromAccountLookUp.setSelected(this.paidFromAccountLookUp.getSelectedItemID());
            }
            widgets.add(paidFromAccountLookUp);
        }
        if (columnsMap.containsKey(PayrunPaymentConstants.PAID_TO)) {
            AccountsLookUp paidToAccountLookUp = new AccountsLookUp();
            paidToAccountLookUp.getSuggestBox().addSelectionHandler(event -> saveTableItem(item.getEmployeeID(), paidToAccountLookUp.getSelectedItemID(), PayrunPaymentConstants.PAID_TO));

            if (item.getPaidToAccountID() != null) {
                paidToAccountLookUp.addItem(item.getPaidToAccount());
                paidToAccountLookUp.setSelected(item.getPaidToAccountID());
            } else if (this.paidToAccountLookUp.getSelectedItemID() != null) {
                SelectItem paidToAccount = this.paidToAccountLookUp.getSelectedItem();

                paidToAccountLookUp.addItem(paidToAccount);
                paidToAccountLookUp.setSelected(this.paidToAccountLookUp.getSelectedItemID());
            }
            widgets.add(paidToAccountLookUp);
        }

        if (columnsMap.containsKey(PayrunPaymentConstants.DETAILS)) {
            EditableTextBox details = new EditableTextBox();
            details.addValueChangeHandler(event -> saveTableItem(item.getEmployeeID(), details.getDisplayValue(), PayrunPaymentConstants.DETAILS));

            if (item.getDetails() != null) {
                details.setText(item.getDetails());
            }
            widgets.add(details);
        }

        if (columnsMap.containsKey(PayrunPaymentConstants.DUE_AMOUNT)) {
            CustomCellLabel dueAmount = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
            dueAmount.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            if (item.getDueAmount() != null) {
                dueAmount.setText(PayrollClientUtils.format(item.getDueAmount()));
            }
            widgets.add(dueAmount);
        }

        if (columnsMap.containsKey(PayrunPaymentConstants.PAYMENT_AMOUNT)) {
            EditableTextBox paymentAmount = new EditableTextBox(PayrollClientUtils.format(BigDecimal.ZERO), true);
            paymentAmount.addValueChangeHandler(event -> {
                //validation
                CustomCell paymentCell = (CustomCell) employeeTable.getColumnCellWidgetById(grid.getCurrentRow(), PayrunPaymentConstants.PAYMENT_AMOUNT);
                BigDecimal value = PayrollClientUtils.parse(paymentAmount.getDisplayValue());
                if (BigDecimal.ZERO.compareTo(value) > 0) {
                    paymentAmount.setText(PayrollClientUtils.format(BigDecimal.ZERO));
                    //popup
                    value = BigDecimal.ZERO;
                    WfmWindow.alert(wfmStrings.totalAmountCantLessThanZero());
                    paymentCell.InActive();
                }
                if (value.compareTo(item.getDueAmount()) > 0) {
                    //revert the value
                    paymentAmount.setText(PayrollClientUtils.format(item.getDueAmount()));
                    //popup
                    WfmWindow.alert(payrollStrings.paymentAmountCannotbeMoreThanDueAmount());
                    paymentCell.InActive();
                } else {
                    saveTableItem(item.getEmployeeID(), value, PayrunPaymentConstants.PAYMENT_AMOUNT);
                }
            });

            if (changedItemMap.get(item.getEmployeeID()) != null) {
                paymentAmount.setText(PayrollClientUtils.format(changedItemMap.get(item.getEmployeeID()).getPaymentAmount()));
            } else if (item.getDueAmount() != null) {
                paymentAmount.setText(PayrollClientUtils.format(item.getDueAmount()));
            }
            widgets.add(paymentAmount);
        }

        return widgets.toArray(new Object[]{});
    }

    protected void setTableData(PayrollPayment result) {
        itemMap.clear();
        employeeTable.removeAllRows();
        for (PayrollPaymentItem item : result.getItems()) {
            employeeTable.addRow(getWidgets(item));
        }
    }

    protected void saveTableItem(Integer id, Object value, String columnID) {
        PayrollPaymentItem item = changedItemMap.get(id);
        if (item == null) {
            item = itemMap.get(id);
        }

        switch (columnID) {
            case PayrunPaymentConstants.REFERENCE:
                item.setReference((String) value);
                break;
            case PayrunPaymentConstants.PAID_FROM:
                item.setPaidFromAccountID((Integer) value);
                break;
            case PayrunPaymentConstants.PAID_TO:
                item.setPaidToAccountID((Integer) value);
                break;
            case PayrunPaymentConstants.DETAILS:
                item.setDetails((String) value);
                break;
            case PayrunPaymentConstants.PAYMENT_AMOUNT:
                BigDecimal oldValue = Optional.ofNullable(item.getDueAmount()).orElse(BigDecimal.ZERO);
                BigDecimal newValue = (BigDecimal) value;
                item.setPaymentAmount(newValue);

                updateTotalData(newValue.subtract(oldValue));
                break;
        }
        changedItemMap.put(id, item);
    }

    protected void deleteTableItem(Integer rowId) {
        LinkCellWidget employee = (LinkCellWidget) employeeTable.getColumnById(rowId, GroupPayrunContants.EMPLOYEE);
        Integer employeeID = employee.getItem().getId();

        PayrollPaymentItem item = itemMap.get(employeeID);
        BigDecimal oldValue = item.getDueAmount();

        deletedItemMap.put(employeeID, null);

        updateTotalData(BigDecimal.ZERO.subtract(oldValue));
    }

    protected ListingFilterParameter getFilterParameter() {
        if (filterParameter == null) {
            filterParameter = new ListingFilterParameter();
        }
        filterParameter.setStart(Optional.ofNullable(tableStart).orElse(0));
        filterParameter.setLimit(Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(10));
        filterParameter.setSearchKey(tableSearchBox.getText());
        filterParameter.setGroupPayrunID(additionalPaymentID);
        return filterParameter;
    }

    private Widget drawPaginationPanel() {
        this.initPaginationWidgets();
        GBoxItem limitField = new GBoxItem(tableLimitListBox);
        limitField.setWidth("100px");

        Icon prevIcon = new Icon();
        prevIcon.setClass("ficon--chevron-left");
        MaterialLink prevLink = new MaterialLink();
        prevLink.setStyleName("btn btn--white btn--icon");
        prevLink.add(prevIcon);

        GBoxItem currentItem = new GBoxItem(tableCurrentBox);
        currentItem.addStyleToComponent("paging__currentpage");

        Icon nextIcon = new Icon();
        nextIcon.setClass("ficon--chevron-right");
        MaterialLink nextLink = new MaterialLink();
        nextLink.setStyleName("btn btn--white btn--icon");
        nextLink.add(nextIcon);

        GBoxRow row = new GBoxRow();
        row.add(new GBoxItem(tablePagingResult));
        row.add(limitField);
        row.add(new GBoxItem(prevLink));
        row.add(currentItem);
        row.add(new GBoxItem(nextLink));

        prevLink.addClickHandler((event) -> {
            Integer totalPagesSize = Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20);
            tableStart -= totalPagesSize;
            if (tableStart <= 0) {
                tableStart = 0;
            }
            loadData();
        });

        nextLink.addClickHandler((event) -> {
            Integer selectedLimit = Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20);
            int totalPages = totalTableItems / selectedLimit + (totalTableItems % selectedLimit > 0 ? 1 : 0);
            int currentPage = tableStart / selectedLimit + 1;

            if (currentPage >= totalPages) {
                return;
            }
            tableStart += selectedLimit;
            loadData();
        });

        tableLimitListBox.addValueChangeHandler(event -> {
            tableStart = 0;
            tableCurrent = 0;
            totalTableItems = 0;
            loadData();
        });
        return row;
    }

    @Override
    protected void getDataToFillFields() {
    }

    private void save() {
        if (!isValidFormData()) {
            saveButton.setEnabled(true);
            return;
        }
        PayrollPayment item = getTransferObject();

        LoadingPanel.loading(true);
        payrollService.createPayrollPayment(item, new AbstractAsyncCallback<SaveResultTO<Integer>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                saveButton.setEnabled(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(SaveResultTO<Integer> result) {
                LoadingPanel.loading(false);
                saveButton.setEnabled(true);
                if (result == null) {
                    Info.show(wfmStrings.errorOccurredSavingChanges(), Info.Type.WARNING);
                    return;
                }
                if (result.getResult() != null) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYROLL_PAYMENT_ADD, null, PayrollPaymentAddView.this);
                    closeTab();
                } else if (result.getMessage() != null) {
                    Info.show(result.getMessage(), Info.Type.WARNING);
                }
            }
        });
    }

    private PayrollPayment getTransferObject() {
        PayrollPayment item = new PayrollPayment();
        item.setAdditionalPaymentID(additionalPaymentID);

        item.setPaidFromAccountID(paidFromAccountLookUp.getSelectedItemID());
        item.setPaidToAccountID(paidToAccountLookUp.getSelectedItemID());

        item.setDetails(detailsBox.getText());
        item.setPaymentDate(new DateNonConvertable(paymentDate.getDate()));

        item.setCurrency(currencyWidget.getCurrency());
        item.setExchangeRate(currencyWidget.getExchangeRate());

        item.setChangedItems(changedItemMap);
        item.setDeletedItems(deletedItemMap);
        return item;
    }

    private boolean isValidFormData() {
        int errors = 0;

        if (!Validation.validateDate(paymentDate)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(paidFromAccountLookUp)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(paidToAccountLookUp)) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }
        if (totalTableItems <= 0) {
            Info.show("No employees!", Info.Type.WARNING);
            return false;
        }
        return true;
    }

    void setPaginationData(PayrollPayment result) {
        if (result.getCurrency() != null) {
            currencyWidget.setCurrency(result.getCurrency().getId());
        }
        if (result == null || result.getItems() == null) {
            return;
        }
        totalTableItems = Optional.ofNullable(result.getTotalItems()).orElse(0);

        int pageSize = Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20);
        int position = Optional.ofNullable(tableStart).orElse(0);

        tableCurrent = position / pageSize + 1;

        tableCurrentBox.setValue(tableCurrent.toString());
        tablePagingResult.setText((position + 1) + " - " + ((position + pageSize) < totalTableItems ? (position + pageSize) : totalTableItems) + " " + wfmStrings.of() + " " + totalTableItems);
    }

    private void initTotalTable() {
        totalLabel = new HTML(wfmStrings.total());
        totalInBaseLabel = new HTML(wfmStrings.total());

        totalAmount = new ExtendedHTMLCell(PayrollClientUtils.format(BigDecimal.ZERO));
        totalInBaseAmount = new ExtendedHTMLCell(PayrollClientUtils.format(BigDecimal.ZERO));

        totalTable = new ReceiptTable();
        totalTable.clear();
        totalTable.removeShippingBody();
        totalTable.addGrossItem(totalLabel, totalAmount);

        addField(PAYROLL_STARTER.TOTAL_PANEL, totalTable);
    }

    protected void updateTotalData(BigDecimal addChangedValue) {
        BigDecimal total = Optional.ofNullable(totalAmount.getAmount()).orElse(BigDecimal.ZERO).add(addChangedValue);
        setTotalData(total);
    }

    protected void setTotalData(BigDecimal totalValue) {
        BigDecimal total = totalValue != null ? totalValue : BigDecimal.ZERO;

        totalAmount.setHTML(PayrollClientUtils.format(total));
        totalAmount.setAmount(total);
        if (paymentObject.getMultiCurrencyEnabled()) {
            BigDecimal totalInBase = total.multiply(Optional.ofNullable(currencyWidget.getExchangeRate()).orElse(BigDecimal.ONE));
            totalInBaseAmount.setHTML(PayrollClientUtils.format(totalInBase));
            totalInBaseAmount.setAmount(totalInBase);
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_PAYMENT_FORM;
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
    protected List<Widget> getFooterLeftSideWidgets() {
        List<Widget> widgets = new ArrayList<>();

        Div filterDiv = new Div("frame__info-paging");
        filterDiv.add(drawPaginationPanel());
        widgets.add(filterDiv);
        return widgets;
    }

    protected void initButtons() {
        saveButton = new WfmButton2(wfmStrings.save(), BTN_PRIMARY);
        saveButton.addClickHandler(event -> {
            saveButton.setEnabled(false);
            save();
        });

        buttonsPanel.add(saveButton);
    }

    @Override
    protected void addButtons() {
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightWidgets = new ArrayList<>();
        rightWidgets.add(buttonsPanel);

        return rightWidgets;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
