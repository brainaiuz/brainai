package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.EPPaymentType;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkedLinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollBatchLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.LinkedTypeWidget;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.PayslipItemAmountWidget;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollGlobalSettingsData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.MultiSelectionModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/10/14
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollGlobalSettingsView extends CustomForm implements PayrollConstants, Constants, Colapse {

    protected static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    public static NumberFormat extendedNumberFormat = NumberFormat.getFormat(",##0.00");

    private KpiCellTree employeesTree;
    private EditableTable paymentsTable;
    private EditableTable deductionsTable;
    private EditableTable taxTable;
    private EditableTable employerContributionTable;
    private EditableTable overtimeSettingsTable;
    private Integer objectID;
    private TextBox name;
    private KpiRadioButton byPosition;
    private KpiRadioButton byDepartment;
    private KpiRadioButton byLocation;
    private PayrollBatchLookUp batchLookUp;
    private KpiRadioButton radio_fixed_rate;
    private KpiRadioButton radio_fixed_overtime_rate;
    private KpiRadioButton radio_based_on_monthly_timesheet;
    private KpiRadioButton radio_fixed_via_hourly_rate;
    private KpiRadioButton radio_by_timesheet_only;
    private KpiRadioButton radio_based_on_attendance_report;
    private TextBox box_salary;
    private CategoryLookUp basicSalaryCategory;
    private DataListBox currencyListBox;
    private ListingFilterParameter lfp;
    private boolean enabledMultiCurrency;
    private FormGroup currency;


    public PayrollGlobalSettingsView() {
        super("addGlobalSettings", payrollStrings.bulkUpdate());
    }

    public PayrollGlobalSettingsView(Integer objectID) {
        super("editGlobalSettings", payrollStrings.bulkUpdate());
        this.objectID = objectID;
    }


    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        init();
        return null;
    }

    private void init() {
        lfp = new ListingFilterParameter();
        radio_fixed_rate = new KpiRadioButton("payType", wfmStrings.fixedRate());
        radio_fixed_overtime_rate = new KpiRadioButton("payType", wfmStrings.fixedRateWithOvertime());
        radio_fixed_via_hourly_rate = new KpiRadioButton("payType", wfmStrings.fixedHourlyRate());
        radio_based_on_monthly_timesheet = new KpiRadioButton("payType", Property.get(Constants.TIMESHEET, payrollStrings.basedOnMonthlyTimesheet(), wfmStrings.timesheet()));
        radio_by_timesheet_only = new KpiRadioButton("payType", wfmStrings.paymentTypeTimesheetOnly());
        radio_based_on_attendance_report = new KpiRadioButton("payType", wfmStrings.basedOnAttendanceReport());

        VerticalPanel radioRatePanel = new VerticalPanel();
        radioRatePanel.add(radio_fixed_rate);
        radioRatePanel.add(radio_by_timesheet_only);
        if (Utils.isMonthlyTimeSheetEnable()) {
            radioRatePanel.add(radio_fixed_overtime_rate);
        } else {
            radioRatePanel.add(radio_fixed_via_hourly_rate);
        }
        radioRatePanel.add(radio_based_on_monthly_timesheet);
        radioRatePanel.add(radio_based_on_attendance_report);
        byDepartment = new KpiRadioButton("type", payrollStrings.byDepartment());
        byPosition = new KpiRadioButton("type", payrollStrings.byPosition());
        byLocation = new KpiRadioButton("type", payrollStrings.byLocation());
        byDepartment.setValue(true);

        ValueChangeHandler<Boolean> changeHandler = booleanValueChangeEvent -> onChangeSettingsType();
        name = new TextBox();
        name.setName("name");
        name.addStyleName(DEFAULT_WIDTH);

        byDepartment.addValueChangeHandler(changeHandler);
        byPosition.addValueChangeHandler(changeHandler);
        byLocation.addValueChangeHandler(changeHandler);

        batchLookUp = new PayrollBatchLookUp();
        batchLookUp.ensureDebugId("batch_lookup");
        batchLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> onChangeSettingsType());

        HorizontalPanel typePanel = new HorizontalPanel();
        typePanel.add(byDepartment);
        typePanel.add(byPosition);
        typePanel.add(byLocation);

        employeesTree = new KpiCellTree();
        employeesTree.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getName();
                    }
                };
                employee.setSortable(true);
                sortHandler.setComparator(employee, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                selectedDataGrid.addColumn(employee, wfmStrings.employee());
                selectedDataGrid.setColumnWidth(employee, 50, com.google.gwt.dom.client.Style.Unit.PCT);

                final Column<KpiTreeInfo, String> action = new Column<KpiTreeInfo, String>(new SimpleLinkCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return wfmStrings.delete();
                    }
                };
                action.setFieldUpdater((index, object, value) -> {
                    List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                    contacts.remove(object);
                    object.setSelected(false);
                    selectionModel.setSelected(object, false);
                });
                selectedDataGrid.addColumn(action, wfmStrings.action());
                selectedDataGrid.setColumnWidth(action, 20, com.google.gwt.dom.client.Style.Unit.PCT);

            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        box_salary = new TextBox();
        box_salary.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        box_salary.addStyleName(DEFAULT_WIDTH);
        box_salary.setText(PayrollClientUtils.format(BigDecimal.ZERO));

        basicSalaryCategory = new CategoryLookUp(PayrollConstants.CATEGORY_PAYMENT);
        basicSalaryCategory.addStyleName("MyClass");

        InputGroup salaryInput = new InputGroup(InputGroup.wrapIntoGroupContent(basicSalaryCategory), InputGroup.wrapIntoGroupContent(box_salary));

        currencyListBox = new DataListBox();
        currencyListBox.addStyleName(DEFAULT_WIDTH);
        currencyListBox.ensureDebugId("currencyListBox");
        currencyListBox.setStyleName("currencyList");
        currency = new FormGroup(payrollStrings.salaryCurrency(), currencyListBox);

        overtimeSettingsTable = new EditableTable(getOvertimeSettingsTableColumns());
        drawOverTimeSettingsTable(payrollStrings.regularOvertimeRate(), null, null, true, null);
        drawOverTimeSettingsTable(wfmStrings.weekendOvertimeRate(), null, null, true, null);
        drawOverTimeSettingsTable(wfmStrings.holidayOvertimeRate(), null, null, true, null);

        paymentsTable = new EditableTable(getColumns(PayrollConstants.CATEGORY_PAYMENT));
        addItem(null, PayrollConstants.CATEGORY_PAYMENT);
        paymentsTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                addItem(null, PayrollConstants.CATEGORY_PAYMENT);
            }

            @Override
            public void removeRow() {

            }
        });

        deductionsTable = new EditableTable(getColumns(PayrollConstants.CATEGORY_DEDUCTION));
        addItem(null, PayrollConstants.CATEGORY_DEDUCTION);
        deductionsTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                addItem(null, PayrollConstants.CATEGORY_DEDUCTION);
            }

            @Override
            public void removeRow() {

            }
        });

        taxTable = new EditableTable(getColumns(PayrollConstants.CATEGORY_TAX));
        addItem(null, PayrollConstants.CATEGORY_TAX);
        taxTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                addItem(null, PayrollConstants.CATEGORY_TAX);
            }

            @Override
            public void removeRow() {

            }
        });

        employerContributionTable = new EditableTable(getColumns(PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION));
        addItem(null, PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);
        employerContributionTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                addItem(null, PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);
            }

            @Override
            public void removeRow() {

            }
        });

        addTitleField(PAYROLL_STARTER.ALLOWANCE_DEDUCTION_SETTINGS, wfmStrings.settings());
        addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name(), true));
        addField(PAYROLL_STARTER.SETTINGS_TYPE, typePanel, wfmStrings.type());
        addField(PAYROLL_STARTER.PAYROLL_BATCH, batchLookUp, payrollStrings.payrollGroup());
        addField(CustomFormConstants.EMPLOYEES, employeesTree, getTitle(wfmStrings.assignedEmployees()));
        addTitleField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_SETTINGS, wfmStrings.paymentSettings());
        addField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID, radioRatePanel, getTitle(wfmStrings.employeeIsPaid()));
        addField(CustomFormConstants.SALARY_GRADE, salaryInput, getTitle(wfmStrings.basicSalary()));
        addTitleField(CustomFormConstants.OVERTIME_SETTINGS, wfmStrings.overTimeSettings());
        addField(PAYROLL_STARTER.OVERTIME_SETTINGS_TABLE, overtimeSettingsTable, null);
        addTitleField(CustomFormConstants.PAYMENT_DEDUCTION_INFORMATION, wfmStrings.paymentDeductionCategoryTable());
        addField(CustomFormConstants.PAYMENT_TABLE, paymentsTable, null);
        addField(CustomFormConstants.DEDUCTION_TABLE, deductionsTable, null);
        addField(CustomFormConstants.TAX_TABLE, taxTable, null);
        addField(CustomFormConstants.EMPLOYER_CONTRIBUTION, employerContributionTable, null);
        show();
    }

    private void onChangeSettingsType() {
        if (byPosition.getValue()) {
            lfp.setType(1);
        }
        if (byLocation.getValue()) {
            lfp.setType(3);
        } else {
            lfp.setType(2);
        }
        lfp.setPayrollBatchID(batchLookUp.getSelectedItemID());
        PayrollService.App.get().getEmployeesForPaymentDeductionSettings(lfp, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                employeesTree.setItems(result);
            }
        });
    }

    private boolean validation() {
        clearErrorStyle();
        int error = 0;
        if (employeesTree.getSelectedIds() == null || employeesTree.getSelectedIds().length <= 0) {
            error += markAsError(employeesTree, true);
        }
        if (error > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void save() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        PayrollGlobalSettingsData settingsData = new PayrollGlobalSettingsData();
        settingsData.setObjectID(objectID);
        if (byPosition.getValue()) {
            settingsData.setSettingsType(1);
        } else {
            settingsData.setSettingsType(2);
        }
        settingsData.setName(name.getText());
        settingsData.setBatchItem(batchLookUp.getSelectedItem());
        settingsData.setSelectedEmployeeIds(employeesTree.getSelectedIds());
        if (radio_fixed_rate.getValue()) {
            settingsData.setRateType(Constants.FIXED_RATE);
        } else if (radio_fixed_overtime_rate.getValue()) {
            settingsData.setRateType(Constants.FIXED_OVERTIME_RATE);
        } else if (radio_fixed_via_hourly_rate.getValue()) {
            settingsData.setRateType(Constants.FIXED_HRMS_OVERTIME_RATE);
        } else if (radio_based_on_monthly_timesheet.getValue()) {
            settingsData.setRateType(Constants.FIXED_TIMESHEET_OVERTIME_RATE);
        } else if (radio_by_timesheet_only.getValue()) {
            settingsData.setRateType(TIMESHEET_ONLY_RATE);
        } else if (radio_based_on_attendance_report.getValue()) {
            settingsData.setRateType(FIXED_ATTENDANCE_REPORT_OVERTIME_RATE);
        }
        if (basicSalaryCategory.getSelectedData() != null) {
            settingsData.setSalaryCategory(basicSalaryCategory.getSelectedData());
            settingsData.setSalary(PayrollClientUtils.parseToBigDecimal(box_salary.getText()));
        }

        PayslipItemAmountWidget regularOvertimeAmount = (PayslipItemAmountWidget) overtimeSettingsTable.getColumnById(0, "amount");
        DataListBox regularOvertimeRateType = (DataListBox) overtimeSettingsTable.getColumnById(0, "type");
        if (regularOvertimeAmount.getAmount() != null && regularOvertimeAmount.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            settingsData.setRegularOvertimeRate(regularOvertimeAmount.getAmount());
            settingsData.setRegularOvertimeRateType(regularOvertimeRateType.getSelectedId(true) == 0 ? FIXED : PERCENTAGE);
        }
        PayslipItemAmountWidget weekendOvertimeAmount = (PayslipItemAmountWidget) overtimeSettingsTable.getColumnById(1, "amount");
        DataListBox weekendOvertimeRateType = (DataListBox) overtimeSettingsTable.getColumnById(1, "type");
        if (weekendOvertimeAmount.getAmount() != null && weekendOvertimeAmount.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            settingsData.setWeekendOvertimeRate(weekendOvertimeAmount.getAmount());
            settingsData.setWeekendOvertimeRateType(weekendOvertimeRateType.getSelectedId(true) == 0 ? FIXED : PERCENTAGE);
        }
        PayslipItemAmountWidget holidayOvertimeAmount = (PayslipItemAmountWidget) overtimeSettingsTable.getColumnById(2, "amount");
        DataListBox holidayOvertimeRateType = (DataListBox) overtimeSettingsTable.getColumnById(2, "type");
        if (holidayOvertimeAmount.getAmount() != null && holidayOvertimeAmount.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            settingsData.setHolidayOvertimeRate(holidayOvertimeAmount.getAmount());
            settingsData.setHolidayOvertimeRateType(holidayOvertimeRateType.getSelectedId(true) == 0 ? FIXED : PERCENTAGE);
        }

        if (enabledMultiCurrency && currencyListBox.getSelectedItem() != null) {
            settingsData.setCurrency((CurrencyItem) currencyListBox.getSelectedItem());
        }

        settingsData.getPayments().addAll(getTableItems(PayrollConstants.CATEGORY_PAYMENT));
        settingsData.getDeductions().addAll(getTableItems(PayrollConstants.CATEGORY_DEDUCTION));
        settingsData.getTaxes().addAll(getTableItems(PayrollConstants.CATEGORY_TAX));
        settingsData.getEmployerContributions().addAll(getTableItems(PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION));

        LoadingPanel.loading(true);
        PayrollService.App.get().savePaymentDeductionSettings(settingsData, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_DEDUCTION_ADD, result, PayrollGlobalSettingsView.this);
                closeTab();
            }
        });

    }

    private boolean validate() {
        clearErrorStyle();
        int errors = 0;
        errors += markAsError(CustomFormConstants.NAME, name, !Validation.validateTextBoxRequired(name));

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    public void addItem(PaymentDeductionObject paymentDeduction, final String from) {
        EditableTable table = getTable(from);
        DataListBox epPaymentType = new DataListBox();
        epPaymentType.setWithoutNullLabel(true);
        epPaymentType.setItems(new SelectItem[]{
                new SelectItem(EPPaymentType.RECURRING.getId(), EPPaymentType.RECURRING.getTitle()),
                new SelectItem(EPPaymentType.ADDITIONAL.getId(), EPPaymentType.ADDITIONAL.getTitle())
        });
        epPaymentType.setSelected(paymentDeduction != null && paymentDeduction.getPaymentType() != null ? paymentDeduction.getPaymentType().getAsSelectItem() : epPaymentType.getItems()[1]);
        Command cmdEPPaymentType = () -> {
            EPPaymentType epType = EPPaymentType.findById(epPaymentType.getSelectedItem().getId());
            Widget typeWidget = table.getColumnById(table.getGrid().getCurrentRow(), "type");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) table.getColumnById(table.getGrid().getCurrentRow(), "amount");

            if (typeWidget instanceof LinkedTypeWidget) {
                if (EPPaymentType.ADDITIONAL.equals(epType)) {
                    ((LinkedTypeWidget) typeWidget).setSelected(0);
                    amountWidget.showPercentage(false);
                    Command cmd = ((LinkedTypeWidget) typeWidget).getChangeHandler();
                    if (cmd != null) {
                        cmd.execute();
                    }
                }
                ((LinkedTypeWidget) typeWidget).setEnabled(!EPPaymentType.ADDITIONAL.equals(epType));
                ((LinkedTypeWidget) typeWidget).showOrRemoveLink();
                LinkedLinkableCell cell = (LinkedLinkableCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "type");
                cell.InActive();
            } else {
                if (EPPaymentType.ADDITIONAL.equals(epType)) {
                    ((DataListBox) typeWidget).setSelected(0);
                    amountWidget.showPercentage(false);
                }
                ((DataListBox) typeWidget).setEnabled(!EPPaymentType.ADDITIONAL.equals(epType));

                CustomCell cell = (CustomCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "type");
                cell.InActive();
            }
            CustomCell amountWidgetCell = (CustomCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "amount");
            amountWidgetCell.InActive();
        };
        epPaymentType.addValueChangeHandler(ch -> {
            cmdEPPaymentType.execute();
        });

        final DataListBox type = new DataListBox();
        final LinkedTypeWidget linkedType = new LinkedTypeWidget();
        final CategoryLookUp categoryLookUp = new CategoryLookUp(from, () -> true);
//        categoryLookUp.getSuggestBox().getElement().setAttribute("style", "width:200px !important");
        if (paymentDeduction != null && paymentDeduction.getCategoryItem() != null) {
            categoryLookUp.addCategoryItem(paymentDeduction.getCategoryItem());
        }
        categoryLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onCategorySelected(categoryLookUp, from));
        categoryLookUp.getSuggestBox().addKeyUpHandler(event -> onCategorySelected(categoryLookUp, from));

        if (PayrollConstants.CATEGORY_DEDUCTION.equals(from) || PayrollConstants.CATEGORY_TAX.equals(from) || PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION.equals(from)) {
            linkedType.setEnabled(true);
            linkedType.setChangeHandler(() -> {
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) table.getColumnById(table.getGrid().getCurrentRow(), "amount");
                CustomCell amountWidgetCell = (CustomCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "amount");
                LinkedLinkableCell typeWidgetCell = (LinkedLinkableCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "type");
                linkedType.showOrRemoveLink();
                amountWidget.showPercentage(linkedType.getSelectedId() != 0);

                amountWidgetCell.InActive();
                typeWidgetCell.InActive();
//                onCalculateBasicTotalSalary();
            });
            linkedType.setCopyFromBoxHandler(() -> {
                linkedType.clearCategoriesTable();
                for (int i = 0; i < paymentsTable.getRowCount(); i++) {
                    CategoryLookUp categoryLookUp1 = (CategoryLookUp) paymentsTable.getColumnById(i, "category");
                    if (categoryLookUp1.getSelectedData() != null) {
                        PaymentDeductionObject object = new PaymentDeductionObject();
                        object.setCategoryItem(categoryLookUp1.getSelectedData());
                        linkedType.addItem(object, true);
                    }
                }
            });
            if (paymentDeduction != null && paymentDeduction.getType() != null) {
                linkedType.setSelected(paymentDeduction.getType());
                if (paymentDeduction.getLinkedCategories() != null && paymentDeduction.getLinkedCategories().size() > 0) {
                    linkedType.setLinkedItems(paymentDeduction.getLinkedCategories());
                } else if (paymentDeduction.isFromAllAllowances()) {
                    linkedType.clearCategoriesTable();
                    linkedType.setValue(paymentDeduction.isFromAllAllowances());
                    for (int i = 0; i < paymentsTable.getRowCount(); i++) {
                        CategoryLookUp category = (CategoryLookUp) paymentsTable.getColumnById(i, "category");
                        if (category.getSelectedData() != null) {
                            PaymentDeductionObject object = new PaymentDeductionObject();
                            object.setCategoryItem(category.getSelectedData());
                            linkedType.addItem(object, true);
                        }
                    }
                }
            } else {
                linkedType.setSelected(0);
            }
        } else {
            type.setWithoutNullLabel(true);
            type.setItems(new SelectItem[]{
                    new SelectItem(0, wfmStrings.fixed() + " "),
                    new SelectItem(1, "% of Basic ")
            });
            if (paymentDeduction != null && paymentDeduction.getType() != null) {
                type.setSelected(paymentDeduction.getType());
            } else {
                type.setSelected(0);
            }
            type.addValueChangeHandler(changeEvent -> {
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) table.getColumnById(table.getGrid().getCurrentRow(), "amount");
                CustomCell amountWidgetCell = (CustomCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "amount");
                CustomCell typeWidgetCell = (CustomCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "type");
                amountWidget.showPercentage(type.getSelectedId() != 0);
                amountWidgetCell.InActive();
                typeWidgetCell.InActive();
            });
        }

        final PayslipItemAmountWidget amountWidget = new PayslipItemAmountWidget();
        amountWidget.setWidth("118px");
        if (paymentDeduction != null) {
            amountWidget.setAmount(paymentDeduction.getType() == null || paymentDeduction.getType() == 0 ? paymentDeduction.getPaymentAmount() : paymentDeduction.getPercentage());

            amountWidget.showPercentage(paymentDeduction.getType() != null && paymentDeduction.getType() != 0);
            amountWidget.setItemID(paymentDeduction.getId());
            amountWidget.setUsed(paymentDeduction.isUsed());
        }


        EditableTextBox totalAmount = new EditableTextBox();
        if (paymentDeduction != null && paymentDeduction.getTotalAmount() != null) {
            totalAmount.setText(PayrollClientUtils.format(paymentDeduction.getTotalAmount()));
        }
        EditableTextBox remainingAmount = new EditableTextBox();
        remainingAmount.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getRemainingAmount() != null) {
            remainingAmount.setText(PayrollClientUtils.format(paymentDeduction.getRemainingAmount()));
        }

        Widget typeWidget = PayrollConstants.CATEGORY_DEDUCTION.equals(from) || PayrollConstants.CATEGORY_TAX.equals(from) || PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION.equals(from) ? linkedType : type;
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_PAYSLIP_PAYMENT_TYPE)) {
            table.addRow(new Widget[]{categoryLookUp, epPaymentType, typeWidget, amountWidget});
            cmdEPPaymentType.execute();
        } else {
            table.addRow(new Widget[]{categoryLookUp, typeWidget, amountWidget});
        }
    }

    private void onCategorySelected(CategoryLookUp categoryLookUp, String from) {
        EditableTable table = getTable(from);
        int selectedCategoryCount = 0;
        PaymentDeductionSelectItem selectedCategory = categoryLookUp.getSelectedData();
        if (selectedCategory != null) {
            for (int i = 0; i < table.getGrid().getRowCount(); i++) {
                PaymentDeductionSelectItem selectedItem = ((CategoryLookUp) table.getColumnById(i, "category")).getSelectedData();
                if (selectedItem != null && selectedItem.getCode() != null && selectedCategory.getCode() != null && selectedItem.getCode().equals(selectedCategory.getCode())) {
                    selectedCategoryCount++;
                }
            }
            if (selectedCategoryCount >= 2) {
                categoryLookUp.clear();
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, selectedCategory.getName() + wfmStrings.isAlreadySelected());
                messageBox.open();
            }
        }
    }

    public ColumnConfig[] getOvertimeSettingsTableColumns() {
        ColumnConfig[] columns = new ColumnConfig[3];
        columns[0] = new ColumnConfig(CustomCell.class, "overtimeRate", wfmStrings.overtimeRate(), 220, true, "left-align-Cell");
        columns[1] = new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 220, true, "center-align-Cell");
        columns[2] = new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount() + " / " + wfmStrings.percentage(), 120, true, "right-align-Cell");
        return columns;
    }

    private ColumnConfig[] getColumns(final String from) {
        ArrayList<ColumnConfig> columns = new ArrayList<>();
        columns.add(new ColumnConfig(LookUpCell.class, "category", getCategory(from), 220, true, "left-align-Cell"));
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_PAYSLIP_PAYMENT_TYPE)) {
            columns.add(new ColumnConfig(CustomCell.class, "paymentType", wfmStrings.paymentType(), 80, true, "left-align-Cell"));
        }
        Class aClass = PayrollConstants.CATEGORY_DEDUCTION.equals(from) || PayrollConstants.CATEGORY_TAX.equals(from) || PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION.equals(from) ? LinkedLinkableCell.class : CustomCell.class;
        columns.add(new ColumnConfig(aClass, "type", wfmStrings.type(), 200, true, "left-align-Cell"));
        columns.add(new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount() + " / " + wfmStrings.percentage(), 120, true, "right-align-Cell"));
        return columns.toArray(new ColumnConfig[]{});
    }

    private List<PaymentDeductionObject> getTableItems(final String from) {
        EditableTable table = getTable(from);
        List<PaymentDeductionObject> list = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            CategoryLookUp categoryLookUp = (CategoryLookUp) table.getColumnById(i, "category");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) table.getColumnById(i, "amount");
            Widget typeWidget = table.getColumnById(i, "type");
            DataListBox paymentType = (DataListBox) table.getColumnById(i, "paymentType");
            if (categoryLookUp.getSelectedItem() != null && amountWidget.getAmount() != null) {
                PaymentDeductionObject object = new PaymentDeductionObject();
                object.setCategoryItem(categoryLookUp.getSelectedData());

                if (typeWidget instanceof LinkedTypeWidget) {
                    LinkedTypeWidget type = (LinkedTypeWidget) table.getColumnById(i, "type");
                    object.setType(type.getSelectedId());
                    object.setFromAllAllowances(type.isFromAllAllowances());
                    if (!object.isFromAllAllowances()) {
                        object.setLinkedCategories(type.getLinkedCategories());
                    }
                } else {
                    DataListBox type = (DataListBox) table.getColumnById(i, "type");
                    object.setType(type.getSelectedId());
                }
                if (object.getType() == 0) {
                    object.setPaymentAmount(amountWidget.getAmount());
                } else {
                    object.setPercentage(amountWidget.getAmount());
                }

                if (paymentType != null && paymentType.getSelectedId() != null) {
                    object.setPaymentType(EPPaymentType.findById(paymentType.getSelectedId()));
                }
                object.setId(amountWidget.getItemID());
                list.add(object);
            }
        }
        return list;
    }

    private void fillTable(List<PaymentDeductionObject> items, final String from) {
        EditableTable table = getTable(from);
        if (items != null && items.size() > 0) {
            table.removeAllRows();
            for (PaymentDeductionObject item : items) {
                addItem(item, from);
            }
        } else {
            addItem(null, from);
        }
    }

    private EditableTable getTable(final String from) {
        switch (from) {
            case PayrollConstants.CATEGORY_PAYMENT:
                return paymentsTable;
            case PayrollConstants.CATEGORY_DEDUCTION:
                return deductionsTable;
            case PayrollConstants.CATEGORY_TAX:
                return taxTable;
            case PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION:
                return employerContributionTable;
        }
        return null;
    }

    private String getCategory(final String from) {
        switch (from) {
            case PayrollConstants.CATEGORY_PAYMENT:
                return wfmStrings.paymentCategory();
            case PayrollConstants.CATEGORY_DEDUCTION:
                return payrollStrings.deductionCategory();
            case PayrollConstants.CATEGORY_TAX:
                return payrollStrings.taxCategory();
            case PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION:
                return wfmStrings.employerContribution();
        }
        return null;
    }

    public void drawOverTimeSettingsTable(String overtimeName, String overtimeType, String amount, boolean newItem, Integer index) {

        if (newItem) {
            EditableTextBox overTimeNameBox = new EditableTextBox();
            overTimeNameBox.setEnabled(false);
            overTimeNameBox.setText(overtimeName);

            final DataListBox type = new DataListBox();
            type.setWithoutNullLabel(true);
            type.setItems(new SelectItem[]{
                    new SelectItem(0, wfmStrings.fixed() + " "),
                    new SelectItem(1, "% of Basic")
            });
            type.addValueChangeHandler(changeEvent -> {
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) overtimeSettingsTable.getColumnById(overtimeSettingsTable.getGrid().getCurrentRow(), "amount");
                CustomCell amountWidgetCell = (CustomCell) overtimeSettingsTable.getColumnCellWidgetById(overtimeSettingsTable.getGrid().getCurrentRow(), "amount");
                CustomCell typeWidgetCell = (CustomCell) overtimeSettingsTable.getColumnCellWidgetById(overtimeSettingsTable.getGrid().getCurrentRow(), "type");
                amountWidget.showPercentage(type.getSelectedId() != 0);
                amountWidgetCell.InActive();
                typeWidgetCell.InActive();
            });
            if (overtimeType != null && !"".equals(overtimeType)) {
                if (Constants.FIXED.equals(overtimeType)) {
                    type.setSelected(0);
                } else {
                    type.setSelected(1);
                }
            } else {
                type.setSelected(0);
            }

            PayslipItemAmountWidget amountCell = new PayslipItemAmountWidget();

            if (amount != null && !"".equals(amount)) {
                amountCell.setAmount(PayrollClientUtils.parseToBigDecimal(amount));
            }
            overtimeSettingsTable.addRow(new Widget[]{overTimeNameBox, type, amountCell});
        } else {
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) overtimeSettingsTable.getColumnById(index, "amount");
            DataListBox typeWidget = (DataListBox) overtimeSettingsTable.getColumnById(index, "type");
            CustomCell amountWidgetCell = (CustomCell) overtimeSettingsTable.getColumnCellWidgetById(index, "amount");
            CustomCell typeWidgetCell = (CustomCell) overtimeSettingsTable.getColumnCellWidgetById(index, "type");

            if (overtimeType != null && !"".equals(overtimeType)) {
                if (Constants.FIXED.equals(overtimeType)) {
                    typeWidget.setSelected(0);
                } else {
                    typeWidget.setSelected(1);
                }
            } else {
                typeWidget.setSelected(0);
            }


            if (amount != null && !"".equals(amount)) {
                amountWidget.setAmount(PayrollClientUtils.parseToBigDecimal(amount));
            }

            amountWidgetCell.InActive();
            typeWidgetCell.InActive();
        }
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            if (validation()) {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.OkCancel, true);
                messageBox.setTitle(wfmStrings.confirmationMessage());
                messageBox.setMessage(payrollStrings.globalSettingsAddConfirmationMessage());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onCancel() {
                        messageBox.close();
                    }

                    @Override
                    public void onSubmit() {
                        save();
                    }
                });
                messageBox.open();
            }
        });

    }

    @Override
    protected void getDataToFillFields() {
        if (objectID != null) {
            lfp.setObjectId(objectID);
            LoadingPanel.loading(true);
            PayrollService.App.get().getPaymentDeductionSettingsData(objectID, new AsyncCallback<PayrollGlobalSettingsData>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(PayrollGlobalSettingsData result) {
                    if (result != null) {
                        switch (result.getSettingsType()) {
                            case 1:
                                byPosition.setValue(true);
                                break;
                            case 2:
                                byDepartment.setValue(true);
                                break;
                            case 3:
                                byLocation.setValue(true);
                                break;
                            default:
                                byDepartment.setValue(true);
                                break;
                        }

                        if (result.getName() != null) {
                            name.setText(result.getName());
                        }
                        if (result.getBatchItem() != null) {
                            batchLookUp.setSelected(result.getBatchItem());
                        }

                        if (result.getRateType() != null && !"".equals(result.getRateType())) {
                            switch (result.getRateType()) {
                                case Constants.FIXED_RATE:
                                    radio_fixed_rate.setValue(true);
                                    break;
                                case Constants.FIXED_OVERTIME_RATE:
                                    radio_fixed_overtime_rate.setValue(true);
                                    break;
                                case Constants.FIXED_TIMESHEET_OVERTIME_RATE:
                                    radio_based_on_monthly_timesheet.setValue(true);
                                    break;
                                case Constants.TIMESHEET_ONLY_RATE:
                                    radio_by_timesheet_only.setValue(true);
                                    break;
                                case FIXED_ATTENDANCE_REPORT_OVERTIME_RATE:
                                    radio_based_on_attendance_report.setValue(true);
                                    break;
                                default:
                                    radio_fixed_via_hourly_rate.setValue(true);
                                    break;
                            }
                        }
                        if (result.getSalaryCategory() != null) {
                            basicSalaryCategory.addCategoryItem(result.getSalaryCategory());
                        }
                        if (result.getSalary() != null) {
                            box_salary.setText(PayrollClientUtils.format(result.getSalary()));
                        }
                        enabledMultiCurrency = result.isEnabledMultiCurrency();
                        if (enabledMultiCurrency) {
                            addField(CustomFormConstants.CURRENCY, currency);
                            currencyListBox.setItems(result.getCurrencies());
                            currencyListBox.setSelected(result.getCurrency());
                        }
                        if (result.getRegularOvertimeRate() != null) {
                            drawOverTimeSettingsTable(null, result.getRegularOvertimeRateType(), PayrollClientUtils.format(result.getRegularOvertimeRate()), false, 0);
                        }
                        if (result.getWeekendOvertimeRate() != null) {
                            drawOverTimeSettingsTable(null, result.getWeekendOvertimeRateType(), PayrollClientUtils.format(result.getWeekendOvertimeRate()), false, 1);
                        }
                        if (result.getHolidayOvertimeRate() != null) {
                            drawOverTimeSettingsTable(null, result.getHolidayOvertimeRateType(), PayrollClientUtils.format(result.getHolidayOvertimeRate()), false, 2);
                        }
                        fillTable(result.getPayments(), PayrollConstants.CATEGORY_PAYMENT);
                        fillTable(result.getDeductions(), PayrollConstants.CATEGORY_DEDUCTION);
                        fillTable(result.getTaxes(), PayrollConstants.CATEGORY_TAX);
                        fillTable(result.getEmployerContributions(), PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);
                    }
                    onChangeSettingsType();
                    LoadingPanel.loading(false);
                }
            });
        } else {
            PayrollService.App.get().getCompanyPayrollSettings(Constants.MULTI_CURRENCY_FOR_PAYROLL, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(String s) {
                    enabledMultiCurrency = "true".equals(s);
                    if (enabledMultiCurrency) {
                        addField(CustomFormConstants.CURRENCY, currency);
                        CurrencyService.App.get().getCurrencies(true, new AsyncCallback<CurrencyItem[]>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                            }

                            @Override
                            public void onSuccess(CurrencyItem[] currencyItems) {
                                currencyListBox.setItems(currencyItems);
                            }
                        });
                    }
                }
            });
            onChangeSettingsType();
        }

    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_ALLOWANCE_DEDUCTION_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
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
