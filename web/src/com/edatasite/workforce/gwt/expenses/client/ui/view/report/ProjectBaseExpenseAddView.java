package com.edatasite.workforce.gwt.expenses.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.GroupByProjectEntry;
import com.edatasite.workforce.gwt.accounting.client.rpc.TotalCostHours;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AccountLookUpForExpense;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.expenses.client.ui.ExpenseConstants;
import com.edatasite.workforce.gwt.expenses.client.ui.ItemUploadForm;
import com.edatasite.workforce.gwt.expenses.client.ui.subtotal.Subtotal;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.ProjectBaseInvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Normurod on 10/22/2016.
 */
public class ProjectBaseExpenseAddView extends View implements Colapse, AccountingCustomFormConstants {

    private EmployeeLookUp employeeLookUp;
    private DatePicker startPeriod;
    private DatePicker endPeriod;
    private AccountLookUpForExpense accountLookUp;
    private DataListBox customerListBox;
    private FlowPanel pnlProjectTable;
    private KpiDataGrid<ExpenseProjectItem> dataGrid;
    private ListDataProvider<ExpenseProjectItem> dataProvider;
    private final HashSet<Integer> selectedProjectIds = new HashSet<>();

    private final HashMap<String, Widget> widgetsMap = new HashMap<>();
    private static final ProvidesKey<ExpenseProjectItem> KEY_PROVIDER = item -> item == null ? null : item.getId();

    private ExpenseAddEditView expenseView;
    private EditableTable itemsTable;

    private final int WIDGET_MAX_HEIGHT = 400;
    private final String projectBaseExpense = "projectBaseExpense";
    private final HashMap<Integer, SelectItem> projectItemMap = new HashMap<>();

    public ProjectBaseExpenseAddView() {
        super("projectBaseExpenseadd", "Project Base Expense");
    }

    @Override
    protected Widget onInitialize() {
        expenseView = new ExpenseAddEditView(new String[]{"add", ExpenseConstants.INTERNAL_INVOICE});
        expenseView.asyncOnInitialize(new AsyncCallback<Widget>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Widget widget) {

            }
        });

        employeeLookUp = new EmployeeLookUp(true, false, false, true);
        employeeLookUp.ensureDebugId(projectBaseExpense + "employeeLookUp");
        employeeLookUp.getSuggestBox().setWidth("200px");
        employeeLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
            if (employeeLookUp.getSelectedItemID() != null) {
                loadEmployeeClients(employeeLookUp.getSelectedItemID());
            }
            onChangePeriods();
        });

        if (!Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF)) {
            employeeLookUp.addItem(new SelectItem(Utils.getUserID(), ""));
            loadEmployeeClients(Utils.getUserID());
        }

        startPeriod = new DatePicker();
        startPeriod.ensureDebugId(projectBaseExpense + "startPeriod");
        startPeriod.addChangeHandler(changeEvent -> onChangePeriods());
        endPeriod = new DatePicker();
        endPeriod.ensureDebugId(projectBaseExpense + "endPeriod");
        endPeriod.addChangeHandler(changeEvent -> onChangePeriods());

        accountLookUp = new AccountLookUpForExpense(null);
        accountLookUp.ensureDebugId(projectBaseExpense + "accountLookUp");
        accountLookUp.getSuggestBox().setWidth("200px");

        customerListBox = new DataListBox();
        customerListBox.ensureDebugId(projectBaseExpense + "customerLookUp");
        customerListBox.setWidth("200px");
        customerListBox.addValueChangeHandler(changeEvent -> onChangePeriods());

        pnlProjectTable = new FlowPanel();

        initWidgetMap();
        ExpenseService.App.get().getProjectBaseExpenseFormLayout(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(String layout) {
                add(new WftHTMLPanel(layout, widgetsMap).getContainer());
            }
        });
        ExpenseService.App.get().getDefaultAccountForProjectBaseExpense(new AsyncCallback<SelectItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem selectItem) {
                if (selectItem != null) {
                    accountLookUp.addItem(selectItem);
                }
            }
        });

        WfmButton2 continueButton = new WfmButton2(wfmStrings.continueOnly());
        continueButton.addClickHandler(sender -> {
            if (!validate()) {
                return;
            }
            if (selectedProjectIds.isEmpty()) {
                Info.show("You must select one project to continue!", Info.Type.WARNING);
                return;
            }

            clear();

            LoadingPanel.loading(true);

            initializeData();
        });
        WfmButton2 closeButton = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        closeButton.addClickHandler(clickEvent -> closeTab());
        widgetsMap.put(SAVE_AS_DRAFT_BUTTON, continueButton);
        widgetsMap.put(CLOSE_BUTTON, closeButton);
        return null;
    }

    private void initializeData() {
        Integer[] ids = selectedProjectIds.toArray(new Integer[]{});

        expenseView.setProjectIDs(ids);

        Date dateTo = endPeriod.getDate();
        dateTo.setHours(23);
        dateTo.setMinutes(59);
        dateTo.setSeconds(59);
        DateNonConvertable from = new DateNonConvertable(startPeriod.getDate());
        DateNonConvertable to = new DateNonConvertable(dateTo);


        ProjectBaseInvoiceService.App.get().getGroupedByProjectFE(employeeLookUp.getSelectedItemID(), ids, from, to, new AbstractAsyncCallback<ArrayList<GroupByProjectEntry>>() {
            public void failure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void success(ArrayList<GroupByProjectEntry> map) {
                ProjectBaseData[] data = new ProjectBaseData[map.size()];
                int i = 0;
                for (GroupByProjectEntry entry : map) {
                    SelectItem key = entry.getKey();
                    TotalCostHours value = entry.getValue();
                    data[i] = new ProjectBaseData();

                    if (entry.getClient() != null) {
                        data[i].setClientName(entry.getClient().getName());
                    }
                    data[i].setProjectName(key.getName());
                    data[i].setProjectDescription(key.getDescription());
                    Double timespent = value.getTotalHours() != null ? value.getTotalHours() * 60 : 0d;
                    data[i].setTimespent(new BigDecimal(timespent).setScale(2, RoundingMode.HALF_UP).intValue());
                    data[i].setTimesheetEntryIdList(value.getEntryIds());
                    Double rate = (value.getTotalWageCost() != null && value.getTotalHours() != null && value.getTotalHours().compareTo(0d) != 0) ? value.getTotalWageCost().doubleValue() / value.getTotalHours().doubleValue() : 0;
                    data[i++].setWageRate(rate);

                }

                expenseView.getEmployeeLookUp().clear();
                expenseView.getEmployeeLookUp().addItem(employeeLookUp.getSelectedItem());
                expenseView.onEmployeeChange();

                if (ids.length == 1) {
                    expenseView.getProjectLookUp().addItem(projectItemMap.get(ids[0]));
                }
                drawInvoiceView(data);
            }
        });
    }

    private void drawInvoiceView(ProjectBaseData[] data) {
        itemsTable = expenseView.getItemsTable();
        expenseView.setPeriod(startPeriod.getDate(), endPeriod.getDate());

        fillItemsTable(data);
        add(expenseView);

        LoadingPanel.loading(false);
    }

    private void fillItemsTable(ProjectBaseData[] data) {
        itemsTable.removeAllRows();
        for (ProjectBaseData aData : data) {
            long st = new Date().getTime();
            itemsTable.addRow(getWidgets(aData));
            GWT.log("LOOP Log Time: " + (new Date().getTime() - st));
        }
    }

    private Widget[] getWidgets(ProjectBaseData data) {
        ArrayList<Widget> widgets = new ArrayList<>();

        AccountLookUpForExpense lookUpForExpense = new AccountLookUpForExpense(null);
        lookUpForExpense.getSuggestBox().setWidth("160px");

        if (accountLookUp.getSelectedItem() != null) {
            lookUpForExpense.addItem(accountLookUp.getSelectedItem());
        }
        widgets.add(lookUpForExpense);

        TextArea2 description = new TextArea2(350, false);
        description.setWidth("190px");

        StringBuilder desc = new StringBuilder();
        if (data.getClientName() != null && !data.getClientName().isEmpty()) {
            desc.append("Client: ").append(data.getClientName()).append(" -~\n");
        }
        if (Utils.isEnableBonnardCustomization()) {
            desc.append("Case: ").append(data.getProjectName());
        } else {
            desc.append("Project: ").append(data.getProjectName());
        }
        description.setText(desc.toString());
        description.setEntryIds(data.getTimesheetEntryIdList());
        widgets.add(description);

        String unitsDefaultText = AccountingUtils.getDefaultZero();
        String costDefaultText = AccountingUtils.getUnitPriceZero();
        String markupDefaultText = AccountingUtils.getZero();

        ExpenseAddEditView.UnitPriceTextBox units = expenseView.getUnitPriceTextBox();
        units.setText(data.getTimeSpentInHours());
        units.setEnabled(false);
        units.setWidth("70px");
        widgets.add(units);


        ExpenseAddEditView.ExtendedTextBox cost = expenseView.getExtendedTextBox();
        cost.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        cost.setWidth("70px");

        BigDecimal value = BigDecimal.ZERO;
        if (data.getWageRate() != null) {
            value = BigDecimal.valueOf(data.getWageRate()).setScale(2, RoundingMode.HALF_UP);
        }
        cost.setText(AccountingUtils.get().formatPrice(value));
        cost.setCostAmountInBase(value);
        Validation.addNumericKeyboardListener(cost, AccountingUtils.customUnitPriceScale, true);
        widgets.add(cost);

        ExpenseAddEditView.ExtendedTaxLookUp taxLookUp = null;//expenseView.getExtendedTaxLookUp();
        ExpenseAddEditView.ExtendedTaxLookUp doubleTaxLookUp = null;//expenseView.getExtendedTaxLookUp();
        if (expenseView.isCompanyExpense) {
            taxLookUp = expenseView.getExtendedTaxLookUp();
            taxLookUp.getSuggestBox().setWidth("100px");
            widgets.add(taxLookUp);
            if (expenseView.isDoubleTaxEnabled) {
                doubleTaxLookUp = expenseView.getExtendedTaxLookUp();
                doubleTaxLookUp.getSuggestBox().setWidth("100px");
                widgets.add(doubleTaxLookUp);
            }
        }
        String taxAmount = AccountingUtils.getZero();
        TextBox[] calculatingWidgets = {units, cost};

        Label subtotal = new Label(AccountingUtils.getZero());
        Label baseSubtotal = new Label(AccountingUtils.getZero());

        Subtotal subtotalCalculator = new Subtotal(calculatingWidgets, taxLookUp, doubleTaxLookUp, taxAmount, subtotal, null);
        subtotalCalculator.setTaxCalculationType(AccountingConstants.TAX_CALCULATION_EXCLUSIVE, false);

        subtotalCalculator.addPreparedListener(() -> expenseView.updateTotal());
        units.setSubtotalCalculator(subtotalCalculator);
        expenseView.addFocusListener(units, unitsDefaultText, subtotalCalculator, ExpenseAddEditView.UNITS);
        expenseView.addFocusListener(cost, costDefaultText, subtotalCalculator, ExpenseAddEditView.COST);

        widgets.add(new ItemUploadForm(Constants.F_EXP));

        widgets.add(subtotal);
        widgets.add(baseSubtotal);

        if (expenseView.isCompanyExpense) {
            CrmAccountLookUp customerLookUp = new CrmAccountLookUp(CrmAccountLookUp.CUSTOMER, true);
            customerLookUp.getSuggestBox().setWidth("110px");
            widgets.add(customerLookUp);

            TextBox markupAmount = new TextBox();
            markupAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            markupAmount.setText(markupDefaultText);
            Validation.addNumericKeyboardListener(markupAmount, AccountingUtils.calculationScale);
            Validation.checkToFocusTextBox(markupAmount, AccountingUtils.get().formatPrice(BigDecimal.ZERO));
            widgets.add(markupAmount);

            if (expenseView.isDepartmentRelationEnabled) {
                DepartmentLookUp departmentLookUp = new DepartmentLookUp();
                departmentLookUp.getSuggestBox().setWidth("110px");
                widgets.add(departmentLookUp);
            }
            if (Utils.isProjectInLineItemEnable()) {
                ProjectLookUp projectLookUp = new ProjectLookUp(Constants.EXPENSE_REPORT, customerLookUp);
                projectLookUp.getSuggestBox().setWidth("110px");
                widgets.add(projectLookUp);

                customerLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> projectLookUp.clear());
            }
            if (Utils.hasGenericAccess(GenericSettingsEnum.PO_IN_LINE_ITEM_ENABLE)) {
                PurchaseOrderLookUp poLookUp = new PurchaseOrderLookUp();
                poLookUp.getSuggestBox().setWidth("110px");
                widgets.add(poLookUp);
            }
        }

        return widgets.toArray(new Widget[]{});
    }

    private void initWidgetMap() {
        HTML employeeLabel = new HTML(wfmStrings.employee());
        employeeLabel.setStyleName(STYLE_LABEL);

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF)) {
            widgetsMap.put(LABEL_EMPLOYEE, employeeLabel);
            widgetsMap.put(INPUT_EMPLOYEE, employeeLookUp);
        }

        HTML startPeriodLabel = new HTML(wfmStrings.startDate());
        startPeriodLabel.setStyleName(STYLE_LABEL);
        widgetsMap.put(LABEL_PERIOD_START, startPeriodLabel);
        widgetsMap.put(INPUT_PERIOD_START, startPeriod);

        HTML endPeriodLabel = new HTML(wfmStrings.endDate());
        endPeriodLabel.setStyleName(STYLE_LABEL);
        widgetsMap.put(LABEL_PERIOD_END, endPeriodLabel);
        widgetsMap.put(INPUT_PERIOD_END, endPeriod);

        HTML customerLabel = new HTML(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        customerLabel.setStyleName(STYLE_LABEL);
        widgetsMap.put(LABEL_CUSTOMER, customerLabel);
        widgetsMap.put(INPUT_CUSTOMER, customerListBox);

        HTML accountLabel = new HTML(wfmStrings.account());
        accountLabel.setStyleName(STYLE_LABEL);
        widgetsMap.put(LABEL_ACCOUNT, accountLabel);
        widgetsMap.put(INPUT_ACCOUNT, accountLookUp);
        widgetsMap.put(INPUT_ITEM_TABLE, pnlProjectTable);
    }

    private void onChangePeriods() {
        if (startPeriod.getDate() != null && endPeriod.getDate() != null && employeeLookUp.getSelectedItemID() != null) {

            if (startPeriod.getDate().compareTo(endPeriod.getDate()) > 0) {
                Info.show("Date validation !", Info.Type.WARNING);
                return;
            }

            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setEmployeeId(employeeLookUp.getSelectedItemID());
            fp.setClientId(customerListBox.getSelectedId());
            fp.setStartDateNC(Utils.getStartDateNCForFilter(startPeriod.getDate()));
            fp.setEndDateNC(Utils.getEndDateNCForFilter(startPeriod.getDate()));

            LoadingPanel.loading(true);
            ExpenseService.App.get().getExpenseProjects(new DateNonConvertable(DateUtils.resetTime(startPeriod.getDate())), new DateNonConvertable(DateUtils.resetTime(endPeriod.getDate())), fp, new AsyncCallback<ArrayList<ExpenseProjectItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ArrayList<ExpenseProjectItem> expenseProjectItems) {
                    LoadingPanel.loading(false);
                    initProjectTable();
                    projectItemMap.clear();
                    if (expenseProjectItems != null && !expenseProjectItems.isEmpty()) {
                        int height = expenseProjectItems.size() * 40 + 50;

                        if (height > WIDGET_MAX_HEIGHT) {
                            height = WIDGET_MAX_HEIGHT;
                        }
                        dataGrid.setHeight(height + "px");

                        for (ExpenseProjectItem item : expenseProjectItems) {
                            projectItemMap.put(item.getId(), new SelectItem(item.getId(), item.getName()));
                        }
                    }
                    dataProvider.getList().clear();
                    dataProvider.getList().addAll(expenseProjectItems);
                    dataProvider.refresh();
                }
            });

        }
    }

    private void initColumns() {
        int index = 0;

        Header<Boolean> header = new Header(new CheckboxCell()) {
            @Override
            public Boolean getValue() {
                return Boolean.FALSE;
            }
        };
        header.setUpdater(value -> {
            List<ExpenseProjectItem> list = dataProvider.getList();
            for (ExpenseProjectItem item : list) {
                item.setSelected(value);

                if (value) {
                    selectedProjectIds.add(item.getId());
                } else {
                    selectedProjectIds.remove(item.getId());
                }
            }
            dataProvider.refresh();
        });
        dataGrid.addColumn(new Column<ExpenseProjectItem, Boolean>(new CheckboxCell()) {
            @Override
            public Boolean getValue(ExpenseProjectItem item) {
                return item.isSelected();
            }
        }, header);
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 50, Style.Unit.PX);

        dataGrid.addColumn(new TextColumn<ExpenseProjectItem>() {
            @Override
            public String getValue(ExpenseProjectItem object) {
                return object.getName();
            }
        }, Property.get(Constants.PROJECT, wfmStrings.project()));
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 300, Style.Unit.PX);

        if (customerListBox.getSelectedId() == null) {
            dataGrid.addColumn(new TextColumn<ExpenseProjectItem>() {
                @Override
                public String getValue(ExpenseProjectItem object) {
                    return object.getCustomer() != null ? object.getCustomer().getName() : "";
                }
            }, Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
            dataGrid.setColumnWidth(dataGrid.getColumn(index++), 250, Style.Unit.PX);
        }

        dataGrid.addColumn(new TextColumn<ExpenseProjectItem>() {
            @Override
            public String getValue(ExpenseProjectItem object) {
                return object.getLastExpenseReportedDate() != null ? com.edatasite.workforce.gwt.core.client.DateUtils.format(object.getLastExpenseReportedDate()) : "N/A";
            }
        }, wfmStrings.lastExpensedDate());
        dataGrid.setColumnWidth(dataGrid.getColumn(index++), 200, Style.Unit.PX);

        Column<ExpenseProjectItem, Boolean> checkbox = (Column<ExpenseProjectItem, Boolean>) dataGrid.getColumn(0);
        checkbox.setFieldUpdater((index1, item, value) -> {
            if (value) {
                selectedProjectIds.add(item.getId());
            } else {
                selectedProjectIds.remove(item.getId());
            }
        });
    }

    private void initProjectTable() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("900px");
        dataGrid.setHeight("200px");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataProvider.addDataDisplay(dataGrid);

        initColumns();
        pnlProjectTable.clear();
        pnlProjectTable.add(dataGrid);

    }

    private boolean validate() {
        int errors = 0;
        errors += !Validation.validateLookUpRequired(employeeLookUp) ? 1 : 0;
        errors += !Validation.validateDate(startPeriod) ? 1 : 0;
        errors += !Validation.validateDate(endPeriod) ? 1 : 0;

        return errors <= 0;
    }

    private void loadEmployeeClients(Integer employeeId) {
        ExpenseService.App.get().getEmployeeClients(employeeId, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                customerListBox.clear();
                customerListBox.setItems(selectItems);
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }

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
}
