package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.groupPayrun;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.FooteredCustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.Frequency;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.calendardatepicker.CalendarUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.widget.LinkCellWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeByPermissionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollBatchLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollLocationLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.GroupPayrunData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrolTableItemListResult;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollServiceAsync;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayslipFilter;
import com.edatasite.workforce.gwt.payroll.client.rpc.SaveResultTO;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.client.ui.PayrollContants;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.widgets.ExtendedHTMLCell;
import com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.widgets.PayrunItemModal;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
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

public class GroupPayrunAddView extends FooteredCustomForm implements Constants, Colapse, FittedContent {
    protected static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    protected static final PayrollServiceAsync payrollService = PayrollService.App.get();
    final SelectItem TYPE_GROUP = new SelectItem(1, payrollStrings.payrollGroup());
    protected final SelectItem TYPE_PROJECT = new SelectItem(2, wfmStrings.project());
    protected final SelectItem TYPE_LOCATION = new SelectItem(3, wfmStrings.location());

    protected EditableTable employeeTable;
    protected EditableGrid grid;

    protected CurrencyWidget currencyWidget;
    private EmployeeByPermissionLookUp approver2;
    protected DatePicker processDate;
    TextBox tableSearchBox;
    private TextBox tableCurrentBox;
    DataListBox tableLimitListBox;
    protected DataListBox month;
    protected DataListBox year;
    protected DataListBox frequency;
    private MaterialLink tablePagingResult;
    protected EmployeeByPermissionLookUp approver;
    PayrollBatchLookUp payrollBatchLookUp;
    private WfmButton2 submitForApprovalButton;
    private WfmButton2 saveAsDraftButton;

    DataListBox paymentMethodListBox;
    protected ProjectLookUp projectLookUp;
    protected PayrollLocationLookUp locationLookUp;
    DataListBox typeListBox;
    protected KpiSwitcher sendNotification;
    private FormGroup lookupBox;
    private PayrunItemModal payrunItemModal;
    protected Div buttonsPanel;

    private Integer totalTableItems = 0;
    private Integer tableCurrent = 0;
    Integer tableStart = 0;

    protected HTML totalLabel;
    protected ExtendedHTMLCell totalAmount;
    HTML totalInBaseLabel;
    ExtendedHTMLCell totalInBaseAmount;
    protected ReceiptTable totalTable;

    protected Integer id;
    protected Map<String, ColumnConfig> columnsMap;
    protected GroupPayrunData groupPayrunData;
    private PayslipFilter filterParameter;
    protected Map<Integer, SinglePayrunItem> itemMap = new HashMap<>();
    private final HashMap<Integer, SinglePayrunItem> changedItemMap = new HashMap<>();
    private final HashMap<Integer, Boolean> deletedItemMap = new HashMap<>();

    protected MaterialLink portrait;
    protected MaterialLink landscape;
    protected MaterialLink pdfVersion;

    private static boolean saveInProgress = false;

    public GroupPayrunAddView() {
        super("add");
        setDescription(property.getPlural(payrollStrings.groupPayruns()));
    }

    GroupPayrunAddView(String name) {
        super(name);
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        loadMainData();
        return null;
    }

    protected void loadMainData() {
        payrollService.getGroupPayrunSettings(new AsyncCallback<GroupPayrunData>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(GroupPayrunData result) {
                groupPayrunData = result;
                initializeForm();
                show();
            }
        });
    }

    protected void registerEventHandlers() {
        typeListBox.setChangeEvent(() -> {
            enableGroupOrProject(TYPE_GROUP.equals(typeListBox.getSelectedItem()), TYPE_LOCATION.equals(typeListBox.getSelectedItem()));
            onChangeMonth();
        });

        approver.getSuggestBox().addSelectionHandler(e -> onApproverLookupSelected());

        employeeTable.setRemoveRowListener(() -> {
            if (grid.getRowCount() > 1) {
                deleteTableItem(grid.getCurrentRow());
                grid.getModel().removeRow(grid.getCurrentRow());
            } else {
                WfmWindow.alert(wfmStrings.youCanNotRemoveOneLineItem());
            }
        });
    }

    private void onChangeMonth() {
        if (((TYPE_GROUP.equals(typeListBox.getSelectedItem()) && payrollBatchLookUp.getSelectedItem() != null) ||
                (TYPE_PROJECT.equals(typeListBox.getSelectedItem()) && projectLookUp.getSelectedItem() != null) ||
                (TYPE_LOCATION.equals(typeListBox.getSelectedItem()) && locationLookUp.getSelectedItem() != null)) &&
                month.getSelectedId() != null && year.getSelectedId() != null) {
            tableStart = 0;
            tableCurrent = 0;
            totalTableItems = 0;
            changedItemMap.clear();
            deletedItemMap.clear();
            loadTableData();
        }
    }

    protected void initializeForm() {
        initTopPanel();
        initSearchPanel();

        initEmployeeTable();
        initPaginationWidgets();

        registerEventHandlers();
        loadPaymentMethods();
        disableFields();
        initButtons();
    }

    private void initTopPanel() {
        typeListBox = new DataListBox();
        typeListBox.setWithoutNullLabel(true);
        typeListBox.setItems(new SelectItem[]{TYPE_GROUP, TYPE_PROJECT, TYPE_LOCATION});
        typeListBox.setSelected(TYPE_GROUP);


        fillMonthData();
        fillYears();
        frequency = new DataListBox();
        frequency.setWithoutNullLabel(true);
        if (Utils.isArabicCompany()) {
            frequency.setItems(PayrollClientUtils.getPayFrequencies(Utils.isArabicCompany()));
        } else {
            frequency.setItems(Frequency.asSelectItem(false));
        }
        frequency.setSelected(1);

        payrollBatchLookUp = new PayrollBatchLookUp();
        payrollBatchLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onChangeMonth());

        projectLookUp = new ProjectLookUp(null);
        projectLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onChangeMonth());

        locationLookUp = new PayrollLocationLookUp();
        locationLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onChangeMonth());

        lookupBox = new FormGroup();
        enableGroupOrProject(true, false);

        approver = new EmployeeByPermissionLookUp();
        approver.setPermissionCode(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP);

        approver2 = new EmployeeByPermissionLookUp();
        approver2.setPermissionCode(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP);

        processDate = new DatePicker(true);
        processDate.setDate(new Date());

        currencyWidget = new CurrencyWidget(false);
        currencyWidget.setEnabled(false);
        currencyWidget.setDatePicker(processDate, groupPayrunData != null && groupPayrunData.isEnabledMultiCurrency());

        paymentMethodListBox = new DataListBox();

        sendNotification = new KpiSwitcher();
        sendNotification.setValue(groupPayrunData.isSendNotification());
        sendNotification.setEnabled(false);
        addTopPanel();
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
                loadTableData();
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
            loadTableData();
        });
        Div searchDiv = new Div("simpleGwt-ComboBox");
        searchDiv.add(tableSearchBox);
        searchDiv.add(btnSearch);
        addField(SEARCH, new FormGroup("&nbsp;", searchDiv));
    }

    protected void addTopPanel() {
        /*FormGroup sendNotificationGroup = new FormGroup(wfmStrings.sendEmailNotification(), sendNotification);

        GRow gridRow = new GRow();
        gridRow.add(new GColumn(GColumnEnum.COL_8, new FormGroup(wfmStrings.approver(), approver, true)));
        gridRow.add(new GColumn(GColumnEnum.COL_4, sendNotificationGroup));*/
        addField(PAYROLL_STARTER.APPROVER, new FormGroup(wfmStrings.approver(), approver, true));

        addField(PAYROLL_STARTER.SETTINGS_TYPE, new FormGroup(wfmStrings.type(), typeListBox));
        addField(PAYROLL_STARTER.PAYROLL_BATCH, lookupBox);

        addField(PAYROLL_STARTER.PROCESS_DATE, new FormGroup(wfmStrings.processDate(), processDate));
        addField(PAYROLL_STARTER.FREQUENCY, new FormGroup(wfmStrings.frequency(), frequency));
        addField(PAYROLL_STARTER.PAYMENT_METHOD, new FormGroup(wfmStrings.paymentMethod(), paymentMethodListBox));
        addField(PAYROLL_STARTER.PERIOD, new FormGroup(wfmStrings.period(), new InputGroup(month, year)));
    }

    protected ColumnConfig[] getColumns() {
        List<ColumnConfig> columns = new ArrayList<>();

        if (columnsMap.containsKey(PayrollContants.EMPLOYEE)) {
            columns.add(columnsMap.get(PayrollContants.EMPLOYEE));
        }
        if (columnsMap.containsKey(PayrollContants.BASIC_SALARY)) {
            columns.add(columnsMap.get(PayrollContants.BASIC_SALARY));
        }
        if (columnsMap.containsKey(PayrollContants.ALLOWANCE)) {
            columns.add(columnsMap.get(PayrollContants.ALLOWANCE));
        }
        if (columnsMap.containsKey(PayrollContants.PENSION)) {
            columns.add(columnsMap.get(PayrollContants.PENSION));
        }
        if (columnsMap.containsKey(PayrollContants.EMPLOYER_CONTRIBUTION)) {
            columns.add(columnsMap.get(PayrollContants.EMPLOYER_CONTRIBUTION));
        }
        if (columnsMap.containsKey(PayrollContants.DEDUCTION)) {
            columns.add(columnsMap.get(PayrollContants.DEDUCTION));
        }
        if (columnsMap.containsKey(PayrollContants.TAX)) {
            columns.add(columnsMap.get(PayrollContants.TAX));
        }
        if (Utils.isEnableAccountingModule()) {
            if (columnsMap.containsKey(PayrollContants.EXPENSE)) {
                columns.add(columnsMap.get(PayrollContants.EXPENSE));
            }
        }
        if (columnsMap.containsKey(PayrollContants.TOTAL_SALARY)) {
            columns.add(columnsMap.get(PayrollContants.TOTAL_SALARY));
        }
        if (columnsMap.containsKey(PayrollContants.STATUS)) {
            columns.add(columnsMap.get(PayrollContants.STATUS));
        }
        return columns.toArray(new ColumnConfig[]{});
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

    protected Widget drawPaginationPanel() {
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
            loadTableData();
        });

        nextLink.addClickHandler((event) -> {
            Integer selectedLimit = Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20);
            int totalPages = totalTableItems / selectedLimit + (totalTableItems % selectedLimit > 0 ? 1 : 0);
            int currentPage = tableStart / selectedLimit + 1;

            if (currentPage >= totalPages) {
                return;
            }
            tableStart += selectedLimit;
            loadTableData();
        });

        tableLimitListBox.addValueChangeHandler(event -> {
            tableStart = 0;
            tableCurrent = 0;
            totalTableItems = 0;
            loadTableData();
        });
        return row;
    }

    protected void disableFields() {

    }

    protected void loadPaymentMethods() {
        AllInOneService.App.get().getPaymentMethodList(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(final Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(final SelectItem[] result) {
                super.onSuccess(result);
                paymentMethodListBox.setItems(result);
            }
        });
    }

    protected void loadTableData() {
        LoadingPanel.loading(true);
        PayslipFilter fp = getFilterParameter();

        payrollService.getPayslipItems(fp, new AbstractAsyncCallback<PayrolTableItemListResult>() {
            @Override
            public void onFailure(Throwable caught) {
                super.failure(caught);
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(PayrolTableItemListResult result) {
                super.onSuccess(result);
                setPaginationData(result);
                setTableData(result);
                LoadingPanel.loading(false);
            }
        });
    }

    void setPaginationData(PayrolTableItemListResult result) {
        if (result.getCurrency() != null) {
            currencyWidget.setCurrency(result.getCurrency().getId());
        }
        if (result == null || result.getList() == null) {
            return;
        }
        totalTableItems = Optional.ofNullable(result.getTotal()).orElse(0);

        int pageSize = Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20);
        int position = Optional.ofNullable(tableStart).orElse(0);

        tableCurrent = position / pageSize + 1;

        tableCurrentBox.setValue(tableCurrent.toString());
        tablePagingResult.setText((position + 1) + " - " + ((position + pageSize) < totalTableItems ? (position + pageSize) : totalTableItems) + " " + wfmStrings.of() + " " + totalTableItems);
    }

    protected void setTableData(PayrolTableItemListResult result) {
        itemMap.clear();
        employeeTable.removeAllRows();
        for (SinglePayrunItem item : result.getList()) {
            employeeTable.addRow(getWidgets(item));
        }
    }

    protected Object[] getWidgets(SinglePayrunItem item) {
        List<Widget> widgets = new ArrayList<>();
        itemMap.put(item.getEmployeeID(), item);

        LinkCellWidget employeeCell = new LinkCellWidget(item.getEmployee(), () -> showPaymentModal(item));
        employeeCell.setItem(new SelectItem(item.getEmployeeID(), item.getEmployee()));
        widgets.add(employeeCell);

        CustomCellLabel basicSalary = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
        basicSalary.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        if (item != null && item.getBasicSalary() != null) {
            basicSalary.setText(PayrollClientUtils.format(item.getBasicSalary()));
        }
        widgets.add(basicSalary);

        if (columnsMap.containsKey(PayrollContants.ALLOWANCE)) {
            CustomCellLabel allowanceCell = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
            allowanceCell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            if (item != null && item.getAllowance() != null) {
                allowanceCell.setText(PayrollClientUtils.format(item.getAllowance()));
            }
            widgets.add(allowanceCell);
        }

        if (columnsMap.containsKey(PayrollContants.PENSION)) {
            CustomCellLabel pension = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
            pension.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            if (item != null && item.getPensionAmount() != null) {
                pension.setText(PayrollClientUtils.format(item.getPensionAmount()));
            }
            widgets.add(pension);
        }

        if (columnsMap.containsKey(PayrollContants.EMPLOYER_CONTRIBUTION)) {
            CustomCellLabel employerContribution = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
            employerContribution.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            if (item != null && item.getEmployerContribution() != null) {
                employerContribution.setText(PayrollClientUtils.format(item.getEmployerContribution()));
            }
            widgets.add(employerContribution);
        }

        if (columnsMap.containsKey(PayrollContants.DEDUCTION)) {
            CustomCellLabel deductionCell = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
            deductionCell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            if (item != null && item.getDeduction() != null) {
                deductionCell.setText(PayrollClientUtils.format(item.getDeduction()));
            }
            widgets.add(deductionCell);
        }
        if (columnsMap.containsKey(PayrollContants.TAX)) {
            CustomCellLabel taxCell = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
            taxCell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            if (item != null && item.getTax() != null) {
                taxCell.setText(PayrollClientUtils.format(item.getTax()));
            }
            widgets.add(taxCell);
        }
        if (Utils.isEnableAccountingModule() && columnsMap.containsKey(PayrollContants.EXPENSE)) {
            CustomCellLabel expenseCell = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
            expenseCell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            if (item != null && item.getEmployeeExpenses() != null && item.getEmployeeExpenses().getPaymentAmount() != null) {
                expenseCell.setText(PayrollClientUtils.format(item.getEmployeeExpenses().getPaymentAmount()));
            }
            widgets.add(expenseCell);
        }
        CustomCellLabel totalSalary = new CustomCellLabel(PayrollClientUtils.format(BigDecimal.ZERO));
        totalSalary.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        if (item != null && item.getTotal() != null) {
            totalSalary.setText(PayrollClientUtils.format(item.getTotal()));
        }
        widgets.add(totalSalary);

        return widgets.toArray(new Object[]{});
    }

    private PayslipFilter getFilterParameter() {
        if (filterParameter == null) {
            filterParameter = new PayslipFilter();
        }
        filterParameter.setStart(Optional.ofNullable(tableStart).orElse(0));
        filterParameter.setLimit(Optional.ofNullable(tableLimitListBox.getSelectedId()).orElse(20));
        filterParameter.setSearchKey(tableSearchBox.getText());

        if (TYPE_GROUP.equals(typeListBox.getSelectedItem()) && payrollBatchLookUp.getSelectedItem() != null) {
            filterParameter.setPayrollBatchID(payrollBatchLookUp.getSelectedItem().getId());
            filterParameter.setProjectId(null);
            filterParameter.setLocationId(null);
        } else if (TYPE_PROJECT.equals(typeListBox.getSelectedItem()) && projectLookUp.getSelectedItem() != null) {
            filterParameter.setProjectId(projectLookUp.getSelectedItemID());
            filterParameter.setPayrollBatchID(null);
            filterParameter.setLocationId(null);
        } else if (TYPE_LOCATION.equals(typeListBox.getSelectedItem()) && locationLookUp.getSelectedItem() != null) {
            filterParameter.setLocationId(locationLookUp.getSelectedItemID());
            filterParameter.setPayrollBatchID(null);
            filterParameter.setProjectId(null);
        }
        Integer currentYear = year.getSelectedId();
        Integer currentMonth = month.getSelectedId();
        if (currentMonth == null || currentYear == null) {
            return filterParameter;
        }
        int monthDayCount = CalendarUtil.getMonthDaysCount(currentMonth, currentYear);

        filterParameter.setDaysOfMonth(monthDayCount);
        filterParameter.setFromDate(new DateNonConvertable(new Date(currentYear - 1900, currentMonth, 1)));
        filterParameter.setToDate(new DateNonConvertable(new Date(currentYear - 1900, currentMonth, monthDayCount)));
        filterParameter.setPeriodChecker(currentMonth + "," + currentYear);
        filterParameter.setYear(currentYear);
        filterParameter.setMonth(currentMonth);
        filterParameter.setEnabledMultiCurrency(groupPayrunData.isEnabledMultiCurrency());
        filterParameter.setCalculatePension(Optional.ofNullable(groupPayrunData.getCalculatePension()).orElse(false));

        filterParameter.setAvoidEmployees(new ArrayList<>(deletedItemMap.keySet()));
        return filterParameter;
    }

    protected void setColumns() {
        columnsMap = new HashMap<>();
        columnsMap.put(PayrollContants.EMPLOYEE, new ColumnConfig(LinkableCell.class, PayrollContants.EMPLOYEE, wfmStrings.employee(), 180, true, Constants.LEFT_ALIGN_CELL));
        columnsMap.put(PayrollContants.BASIC_SALARY, new ColumnConfig(CustomCell.class, PayrollContants.BASIC_SALARY, wfmStrings.basicSalary(), 80, true, Constants.RIGHT_ALIGN_CELL));
        columnsMap.put(PayrollContants.ALLOWANCE, new ColumnConfig(CustomCell.class, PayrollContants.ALLOWANCE, wfmStrings.allowance(), 80, true, Constants.RIGHT_ALIGN_CELL));
        if (groupPayrunData != null && groupPayrunData.getPensionType() != null && groupPayrunData.getPensionValue() != null && groupPayrunData.getPensionValue().compareTo(BigDecimal.ZERO) > 0) {
            columnsMap.put(PayrollContants.PENSION, new ColumnConfig(CustomCell.class, PayrollContants.PENSION, payrollStrings.pension(), 100, true, Constants.RIGHT_ALIGN_CELL));
        }
        columnsMap.put(PayrollContants.EMPLOYER_CONTRIBUTION, new ColumnConfig(CustomCell.class, PayrollContants.EMPLOYER_CONTRIBUTION, wfmStrings.employerContribution(), 100, true, Constants.RIGHT_ALIGN_CELL));
        columnsMap.put(PayrollContants.DEDUCTION, new ColumnConfig(CustomCell.class, PayrollContants.DEDUCTION, wfmStrings.deduction(), 80, true, Constants.RIGHT_ALIGN_CELL));
        columnsMap.put(PayrollContants.TAX, new ColumnConfig(CustomCell.class, PayrollContants.TAX, wfmStrings.tax(), 80, true, Constants.RIGHT_ALIGN_CELL));
        if (Utils.isEnableAccountingModule()) {
            columnsMap.put(PayrollContants.EXPENSE, new ColumnConfig(CustomCell.class, PayrollContants.EXPENSE, Property.get(Constants.EXPENSES_CLAIM, wfmStrings.expense()), 80, true, Constants.RIGHT_ALIGN_CELL));
        }
        columnsMap.put(PayrollContants.TOTAL_SALARY, new ColumnConfig(CustomCell.class, PayrollContants.TOTAL_SALARY, wfmStrings.total(), 120, true, Constants.RIGHT_ALIGN_CELL));
    }

    protected void initEmployeeTable() {
        setColumns();
        employeeTable = new EditableTable(getColumns());
        grid = employeeTable.getGrid();

        Div div = new Div();
        div.setStyleName("scroll-box--x");
        div.add(employeeTable);

        addField(PAYROLL_STARTER.EMPLOYEE_PAYSLIP_TABLE, div);
    }

    @Override
    protected List<Widget> getFooterLeftSideWidgets() {
        List<Widget> widgets = new ArrayList<>();

        Div filterDiv = new Div("frame__info-paging");
        filterDiv.add(drawPaginationPanel());
        widgets.add(filterDiv);
        return widgets;
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightWidgets = new ArrayList<>();

        buttonsPanel = new Div();
        rightWidgets.add(buttonsPanel);

        submitForApprovalButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitForApprovalButton.addClickHandler(event -> {
            submitForApprovalButton.setEnabled(false);
            if (Utils.getUserID().equals(approver.getSelectedItemID())) {
                save(Constants.PAYRUN_STATUS_APPROVED);
            } else {
                save(Constants.PAYRUN_STATUS_SUBMITTED);
            }
        });
        Div submitWrapper = new Div();
        submitWrapper.add(submitForApprovalButton);
        rightWidgets.add(submitWrapper);

        return rightWidgets;
    }

    protected void initButtons() {
        saveAsDraftButton = new WfmButton2(wfmStrings.saveAsDraft(), BTN_DEFAULT_OUTLINE);
        saveAsDraftButton.addClickHandler(event -> {
            saveAsDraftButton.setEnabled(false);
            save(Constants.PAYRUN_STATUS_DRAFT);
        });
        buttonsPanel.add(saveAsDraftButton);
    }

    @Override
    protected void addButtons() {
    }

    protected void enableGroupOrProject(boolean group, boolean location) {
        if (group && !location) {
            lookupBox.setLabel(payrollStrings.payrollGroup(), true);
            lookupBox.setContent(payrollBatchLookUp);
        } else if (!group && !location) {
            lookupBox.setLabel(wfmStrings.project());
            lookupBox.setContent(projectLookUp);
        } else if (!group && location) {
            lookupBox.setLabel(wfmStrings.location());
            lookupBox.setContent(locationLookUp);
        }
    }

    protected void saveTableItem(Integer rowId, Boolean dateChange, SinglePayrunItem singlePayrunItem) {
        changedItemMap.put(singlePayrunItem.getEmployeeID(), singlePayrunItem);
        employeeTable.addRow(rowId, getWidgets(singlePayrunItem));
    }

    protected void deleteTableItem(Integer rowId) {
        LinkCellWidget employee = (LinkCellWidget) employeeTable.getColumnById(rowId, GroupPayrunContants.EMPLOYEE);
        deletedItemMap.put(employee.getItem().getId(), null);
    }

    protected void save(String status) {
        if (saveInProgress) return;
        if (!isValidFormData(status)) {
            submitForApprovalButton.setEnabled(true);
            saveAsDraftButton.setEnabled(true);
            return;
        }
        saveInProgress = true;
        GroupPayrunData item = getTransferObject(status);
        PayslipFilter filter = getFilterParameter();
        filter.setCalculatePension(true);
        LoadingPanel.loading(true);

        payrollService.createPayslipTable(item, filter, new AbstractAsyncCallback<SaveResultTO<Integer>>() {
            @Override
            public void onFailure(Throwable caught) {
                saveInProgress = false;
                LoadingPanel.loading(false);
                submitForApprovalButton.setEnabled(true);
                saveAsDraftButton.setEnabled(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(SaveResultTO<Integer> result) {
                saveInProgress = false;
                LoadingPanel.loading(false);
                if (result == null) {
                    Info.show(wfmStrings.errorOccurredSavingChanges(), Info.Type.WARNING);
                    submitForApprovalButton.setEnabled(true);
                    saveAsDraftButton.setEnabled(true);
                    return;
                }
                if (result.getResult() != null) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYSLIP_SAVED, null, GroupPayrunAddView.this);
                    closeTab();
                } else if (result.getMessage() != null) {
                    submitForApprovalButton.setEnabled(true);
                    saveAsDraftButton.setEnabled(true);
                    Info.show(result.getMessage(), Info.Type.WARNING);
                }

            }
        });
    }

    private GroupPayrunData getTransferObject(String status) {
        status = Optional.ofNullable(status).orElse(Constants.PAYRUN_STATUS_DRAFT);
        GroupPayrunData item = new GroupPayrunData();

        item.setObjectID(id);
        item.setMonth(month.getSelectedItem().getName());
        item.setMonthID(month.getSelectedId());
        item.setYear(year.getSelectedId());
        item.setApprover(approver.getSelectedItem());
        if (groupPayrunData.isDoubleApprovedEnabled() && approver2 != null) {
            item.setApprover2(approver2.getSelectedItem());
        }
        if (processDate.getDate() != null) {
            item.setProcessDate(new DateNonConvertable(processDate.getDate()));
        }
        item.setFrequency(frequency.getSelectedId());
        item.setPayMethod(paymentMethodListBox.getSelectedItem());
        item.setCreator(new SelectItem(Utils.getUserID(), ""));
        item.setProcessDate(new DateNonConvertable(processDate.getDate()));
        item.setStatus(status);

        if (TYPE_GROUP.equals(typeListBox.getSelectedItem())) {
            item.setPayrollBatchItem(payrollBatchLookUp.getSelectedItem());
            item.setProjectItem(null);
            item.setLocationItem(null);
        } else if (TYPE_PROJECT.equals(typeListBox.getSelectedItem())) {
            item.setProjectItem(projectLookUp.getSelectedItem());
            item.setPayrollBatchItem(null);
            item.setLocationItem(null);
        } else if (TYPE_LOCATION.equals(typeListBox.getSelectedItem())) {
            item.setLocationItem(locationLookUp.getSelectedItem());
            item.setPayrollBatchItem(null);
            item.setProjectItem(null);
        }
        if (Constants.PAYRUN_STATUS_APPROVED.equals(status)) {
            item.setApproveDate(new DateNonConvertable());
            item.setSendNotification(sendNotification.getValue());
        }
        item.setCurrency(currencyWidget.getCurrency());
        item.setExchangeRate(currencyWidget.getExchangeRate());

        item.setChangedItems(changedItemMap);
        item.setDeletedItems(deletedItemMap);
        return item;
    }

    private boolean isValidFormData(String status) {
        int errors = 0;

        if ((TYPE_GROUP.equals(typeListBox.getSelectedItem()) && !Validation.validateLookUpRequired(payrollBatchLookUp) ||
                (TYPE_PROJECT.equals(typeListBox.getSelectedItem()) && !Validation.validateLookUpRequired(projectLookUp)) ||
                (TYPE_LOCATION.equals(typeListBox.getSelectedItem()) && !Validation.validateLookUpRequired(locationLookUp)))) {
            errors++;
        }
        if (month.getSelectedItem() == null) {
            errors++;
        }
        if (year.getSelectedItem() == null) {
            errors++;
        }
        if (!Constants.PAYRUN_STATUS_DRAFT.equals(status)) {
            if (!Validation.validateLookUpRequired(approver)) {
                errors++;
            }
            if (frequency.getSelectedItem() == null) {
                errors++;
            }
            if (groupPayrunData.isDoubleApprovedEnabled() && approver2 != null && approver2.getSelectedItem() == null) {
                errors++;
            }
        }
        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }
        if (totalTableItems <= 0) {
            Info.show("No employees!", Info.Type.WARNING);
            return false;
        }


        if (Utils.isPayslipsLocked()) {
            Integer currentYear = year.getSelectedId();
            Integer currentMonth = month.getSelectedId();
            int monthDayCount = CalendarUtil.getMonthDaysCount(currentMonth, currentYear);
            DateNonConvertable toDate = new DateNonConvertable(new Date(currentYear - 1900, currentMonth, monthDayCount));

            if (DateUtils.getTransactionLockDate().after(toDate.getNonConvertedDate())) {
                Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.payslips(), Utils.getTransactionLockDate()), Info.Type.WARNING);
                return false;
            }
        }

        return true;
    }

    @Override
    public void getDataToFillFields() {
    }

    @Override
    public String getFormID() {
        return LayoutRPC.PAYROLL_PAYSLIP_TABLE_FORM;
    }

    @Override
    public String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    public String getWikiCode() {
        return null;
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

    private void fillMonthData() {
        Date date = DateUtil.getYearFirstDay(new Date());
        SelectItem[] monthItems = new SelectItem[12];
        for (int i = 0; i < 12; i++) {
            monthItems[i] = new SelectItem(i, DateTimeFormat.getFormat("MMMM").format(date));
            date = DateUtil.addMonths(date, 1);
        }
        month = new DataListBox();
        month.setWithoutNullLabel(true);
        month.setItems(monthItems);
        month.addValueChangeHandler(changeEvent -> onChangeMonth());
        month.setSelected(DateUtil.getMonth(new Date()));
        month.ensureDebugId("Group_Payrun_Add_Month");
    }

    private void fillYears() {
        Integer currentYear = DateUtil.getYear(new Date());
        List<SelectItem> yearsList = new ArrayList<>();

        for (int i = -2; i < 3; i++) {
            int year = currentYear + i;
            yearsList.add(new SelectItem(year, year + ""));
        }
        year = new DataListBox();
        year.setWithoutNullLabel(true);
        year.ensureDebugId("Group_Payrun_Add_Year");
        year.setItems(yearsList.toArray(new SelectItem[]{}));
        year.addValueChangeHandler(changeEvent -> onChangeMonth());
        this.year.setSelected(currentYear);
    }

    protected void onApproverLookupSelected() {
        if (Utils.getUserID().equals(approver.getSelectedItemID())) {
            sendNotification.setEnabled(true);
            if (submitForApprovalButton != null) {
                submitForApprovalButton.setText(wfmStrings.saveAndApprove());
            }
        } else {
            sendNotification.setEnabled(false);
            if (submitForApprovalButton != null) {
                submitForApprovalButton.setText(wfmStrings.submitForApproval());
            }
        }
    }

    private void showPaymentModal(SinglePayrunItem item) {
        if (payrunItemModal != null) {
            payrunItemModal.remove();
        }
        payrunItemModal = new PayrunItemModal(item, columnsMap);
        payrunItemModal.setGrid(grid);
        payrunItemModal.setSaveHandler((rowId, singlePayrunItem) -> saveTableItem(rowId, true, singlePayrunItem));
    }

    @Override
    public String getPropertyCode() {
        return PAYSLIP_TABLE_LIST;
    }
}
