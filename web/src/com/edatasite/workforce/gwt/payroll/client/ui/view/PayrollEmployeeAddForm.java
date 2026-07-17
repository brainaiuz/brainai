package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.ui.DOBWidget;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.EPPaymentType;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.CategoryRate;
import com.edatasite.workforce.gwt.core.client.ui.CheckboxSelector;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.WfmPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomList;
import com.edatasite.workforce.gwt.core.client.ui.customlist.Design;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkedLinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeByPermissionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.LinkedTypeWidget;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.PayslipItemAmountWidget;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollSettings;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollValidationException;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollValidator;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.edatasite.workforce.gwt.webforms.client.forms.Form;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.LOCALE_PAYROLL;

/**
 * User: Ilhombek
 * Date: 11/30/12
 * Time: 2:01 PM
 */
public class PayrollEmployeeAddForm extends CustomForm2 implements Constants, PayrollConstants {

    public static final int DEFAULT_ROWS = 5;
    protected static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    private static final NumberFormat numberFormat = Utils.getCalculationNumberFormat();
    private static final String YES = "YES";
    private static final String NO = "NO";
    protected static boolean isUKCompany = false;

    private static final SelectItem radio_fixed_rate = new SelectItem(1, wfmStrings.fixedRate());// Generic payment type: Fixed Rate (Salary)
    private static final SelectItem radio_by_timesheet_only = new SelectItem(2, wfmStrings.paymentTypeTimesheetOnly());//Generic payment type: By timesheet hours only
    private static final SelectItem radio_fixed_via_hourly_rate = new SelectItem(3, wfmStrings.fixedHourlyRate());//For non Al-Fursan companies
    private static final SelectItem radio_fixed_overtime_rate = new SelectItem(4, wfmStrings.fixedRateWithOvertime());// Al-Fursan only (custom option)
    private static final SelectItem radio_based_on_monthly_timesheet = new SelectItem(5, Property.get(Constants.TIMESHEET, payrollStrings.basedOnMonthlyTimesheet(), wfmStrings.timesheet()));//Al-Fursan only (custom option)
    private static final SelectItem radio_based_on_attendance_report = new SelectItem(6, wfmStrings.basedOnAttendanceReport());
    protected final DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd MMM yyyy");
    protected final SelectItem type_a = new SelectItem(1, payrollStrings.thisIsMyFirstJobSince());
    protected final SelectItem type_b = new SelectItem(1, payrollStrings.thisIsNowMyOnlyJob());
    protected final SelectItem type_c = new SelectItem(1, payrollStrings.haveAnotherJobState());
    private final SelectItem radio_gender_male = new SelectItem(1, wfmStrings.male());
    private final SelectItem radio_gender_female = new SelectItem(2, wfmStrings.female());
    private final SelectItem minSalaryMethod = new SelectItem(1, wfmStrings.minSalary());
    private final SelectItem midSalaryMethod = new SelectItem(2, wfmStrings.midSalary());
    private final SelectItem maxSalaryMethod = new SelectItem(3, wfmStrings.maxSalary());
    public Date prevEndDate;
    public Date startDateForOnlyPayroll;
    public EditableTable paymentsTable;
    public EditableTable deductionsTable;
    public EditableTable taxTable;
    public EditableTable employerContributionTable;
    public EditableTable loansTable;
    public EditableTable overtimeSettingsTable;
    protected PayrollSettings payrollSettings;
    protected boolean canApprove = Utils.hasPermission(PAYROLL_EMPLOYEE_APPROVAL); //default: ADMIN, DR
    protected Integer int_employeeID;
    protected TextBox box_normal_rate;
    protected TextBox box_over_time_rate;
    protected InputGroup salary_table;
    protected HTML salaryPaymentCategoryHTML;
    protected Field normal_rate_item;
    protected Field over_time_rate_item;
    //payroll settings
    protected DataListBox job_details;
    protected Field job_details_item;
    protected KpiCheckBox check_prev_wk1_mth1;
    protected KpiCheckBox check_d;
    protected KpiCheckBox check_wk1_mth1;
    private Integer employeeTemplateID;
    private final int maxEmp = 0;
    //employee details
    private TextBox box_email;
    private PayrollEmployeeLookUp existingEmployeeLookUp;
    private TextBox box_first_name;
    private TextBox box_last_name;
    private DOBWidget birth_date;
    private DatePicker hire_date;
    private DatePicker contractStartDate;
    private DatePicker contractEndDate;
    private KpiModal selectEmployeesPopup;
    private DataListBox currencyListBox;
    private DataListBox citizenship;
    private final int pensionage = 60;
    private NewEmployee newEmployee;
    //payment settings
    private DataListBox paymentType;
    private DataListBox jobTitle;
    private TextBox box_salary;
    private TextBox box_commission;
    private DataListBox box_pay_period;
    private DataListBox box_pay_method;
    private BonusWidget bonusWidget;
    private DataListBox box_forms;
    private DatePicker previous_end_date;
    private TextBox box_office_number;
    private TextBox box_reference_number;
    private TextBox box_prev_tax_code;
    private DataListBox box_week_month_type;
    private DataListBox box_week_month_number;
    private TextBox box_total_pay_to_date;
    private TextBox box_total_tax_to_date;
    private TextBox box_tax_code;
    private Numbering empCode;
    private DataListBox salaryMode;
    private TextBox wpsNo;
    private TextBox wageRate;
    private DatePicker fireDate;
    private TextBox agentID;
    private DatePicker start_date;
    private DataListBox box_student_loan;
    private DataListBox box_have_another_job;
    private DataListBox box_family_status;
    private DataListBox gender_table;
    private DataListBox box_ni_category;
    private SimpleLink link_calculate_NI;
    private KpiCheckBox check_not_member_of_COOPS;
    private CustomList pension_scheme;
    public static NumberFormat extendedNumberFormat = NumberFormat.getFormat(",##0.00");
    private MaterialLink saveAndCloseButton;
    private WfmButton2 updateButton;
    private final ArrayList<Integer> deletedCategories = new ArrayList<>();
    private final ArrayList<Integer> inactiveCategories = new ArrayList<>();
    private CategoryLookUp basicSalaryCategory;
    private EmployeeByPermissionLookUp approver;
    private Form payment_method_item;
    private DataListBox payment_methods;
    private DataListBox status;
    protected KpiSwitcher noAccess, essUser;
    private CheckboxSelector mainRole;
    private MaterialLink saveAndNewButton;
    private TextBox salaryTotalAmount;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private String test_code_ID_name = "add_starter_view_";
    private final SelectItem salaryMode0 = new SelectItem(0, wfmStrings.byTariffGrid(), TARIFF_GRID);
    private final SelectItem salaryMode1 = new SelectItem(1, wfmStrings.byFixedAmount(), FIXED_AMOUNT);
    private final SelectItem salaryMode2 = new SelectItem(2, wfmStrings.basedOnAttendanceReport(), BY_ATTENDANCE_REPORT);

    public PayrollEmployeeAddForm() {
        super("add", wfmStrings.addEmployees());
    }

    public PayrollEmployeeAddForm(String name, String description, String test_code_ID_name, Integer int_employeeID) {
        super(name, description);
        this.test_code_ID_name = test_code_ID_name;
        this.int_employeeID = int_employeeID;
    }

    public PayrollEmployeeAddForm(String name, String description, String test_code_ID_name, Integer employeeTemplateID, boolean fromTemplates) {
        super(name, description);
        this.test_code_ID_name = test_code_ID_name;
        this.employeeTemplateID = employeeTemplateID;
    }

    private static void selectByName(String value, DataListBox dataListBox) {
        SelectItem[] items = dataListBox.getItems();
        for (SelectItem item : items) {
            if (value.equals(item.getName())) {
                dataListBox.setSelected(item.getId());
            }
        }
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Employee, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                formPropertyMap = result.getFormPropertyMap();
                PayrollEmployeeAddForm.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    public String getIconStyle() {
        return "bgMark icon-employee";
    }

    @Override
    protected void addButtons() {
        if (int_employeeID == null) {
            //save and close button
            saveAndCloseButton = new MaterialLink(canApprove ? wfmStrings.save() : wfmStrings.submitForApproval());
            MaterialSplitButton splitButton = new MaterialSplitButton(saveAndCloseButton);
            saveAndCloseButton.addClickHandler(event -> {
                save(true, false);
            });
//            saveAndCloseButton = addButton(canApprove ? wfmStrings.saveClose() : wfmStrings.submitForApproval(), null, (test_code_ID_name + "save_and_close_button"), (ClickHandler) event -> save(true, false));
            if (canApprove) {
//                saveAndNewButton = addButton(wfmStrings.saveAdd(), null, (test_code_ID_name + "save_and_new_button"), (ClickHandler) event -> save(false, false));
                saveAndNewButton = new MaterialLink(wfmStrings.saveAndNew());
                saveAndNewButton.addClickHandler(event -> {
                    save(false, false);
                });
                splitButton.addItem(saveAndNewButton);
            }
            addButton(splitButton);
        } else {
            //update button
            updateButton = addButton(canApprove ? wfmStrings.save() : wfmStrings.submitForApproval(), null, (test_code_ID_name + "update_button"), event -> save(true, false));
        }
    }

    @Override
    protected void getDataToFillFields() {
        //for employee details

        if (employeeTemplateID != null) {
            PayrollService.App.get().getEmployeeTemplate(employeeTemplateID, new AbstractAsyncCallback<PayrollSettings>() {
                @Override
                public void failure(Throwable caught) {
                    super.onFailure(caught);
                }

                @Override
                public void success(PayrollSettings result) {
                    payrollSettings = result;
                    fillFormWithData();
                }
            });
        } else {
            PayrollService.App.get().getEmployeeDetailsAndPayrollSettings(int_employeeID, new Date(), new AbstractAsyncCallback<PayrollSettings>() {
                @Override
                public void failure(Throwable caught) {
                }

                @Override
                public void success(PayrollSettings result) {
                    payrollSettings = result;
                    prevEndDate = result.getPrevEndDate() != null ? result.getPrevEndDate().getNonConvertedDate() : null;
                    startDateForOnlyPayroll = result.getStartDateForOnlyPayroll() != null ? result.getStartDateForOnlyPayroll().getNonConvertedDate() : null;
                    fillFormWithData();
                }
            });
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_STARTER_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    protected void fillFormWithData() {
        if (payrollSettings.getStatusList() != null) {
            status.setItems(payrollSettings.getStatusList());
        }
        if (payrollSettings.getStatusId() != null) {
            status.setSelected(payrollSettings.getStatusId());
            status.setEnabled(false);
        }
        if (payrollSettings.getStatus() != null) {
            status.setTitle(payrollSettings.getStatus());
        }
        if (payrollSettings.getRoleList() != null) {
            if (payrollSettings.getRoleId() != null) {
                mainRole.addItems(payrollSettings.getRoleList(), payrollSettings.getRoleId());
            } else {
                mainRole.addItems(payrollSettings.getRoleList());
            }
        }
        if (int_employeeID != null && EMPLOYEE_STATUS_NO_ACCCESS.equals(payrollSettings.getStatusCode())) {
            noAccess.setValue(true);
            mainRole.setVisible(false);
        } else {
            noAccess.setValue(payrollSettings.getNoAccess());
        }

        int maxEssUser = payrollSettings.getUserLimit() != null ? payrollSettings.getUserLimit()[Constants.ESS] : 0;
        if (maxEssUser > 0 || payrollSettings.getEss()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ESS_USER) != null) {
                addField(CustomFormConstants.ESS_USER, essUser, getTitle(formPropertyMap.get(CustomFormConstants.ESS_USER).isChanged() ? formPropertyMap.get(CustomFormConstants.ESS_USER).getTitle() : wfmStrings.essUser(), formPropertyMap.get(CustomFormConstants.ESS_USER).isRequired()));
                essUser.setEnabled(!formPropertyMap.get(CustomFormConstants.ESS_USER).isDisabled());
            } else {
                addField(CustomFormConstants.ESS_USER, essUser, getTitle(wfmStrings.essUser()));
            }
        }


        if (payrollSettings.getEss()) {
            essUser.setValue(true);
            status.setEnabled(false);
            mainRole.setVisible(false);
        }


        //for employee details
        //employee first name
        if (payrollSettings.getEmployeeFirstName() != null) {
            box_first_name.setText(payrollSettings.getEmployeeFirstName());
        }
        //employee last name
        if (payrollSettings.getEmployeeLastName() != null) {
            box_last_name.setText(payrollSettings.getEmployeeLastName());
        }
        //employee email
        if (payrollSettings.getEmployeeEmail() != null) {
            box_email.setText(payrollSettings.getEmployeeEmail());
        }
        //employee hire date
        if (payrollSettings.getStartDate() != null) {
            hire_date.setDate(payrollSettings.getStartDate().getNonConvertedDate());
        }
        //employee birth date
        if (payrollSettings.getDob() != null) {
            birth_date.setSelected(payrollSettings.getDob().getNonConvertedDate().getDate(), payrollSettings.getDob().getNonConvertedDate().getMonth(), payrollSettings.getDob().getNonConvertedDate().getYear());
        } else {
            birth_date.setSelected(0);
        }
        //employment code
        if (payrollSettings.getNumberData() != null) {
            empCode.setNumberData(payrollSettings.getNumberData());
        }
        if (payrollSettings.getResignationDate() != null) {
            fireDate.setDate(payrollSettings.getResignationDate().getNonConvertedDate());
        }
        if (payrollSettings.getSalaryCategory() != null) {
            basicSalaryCategory.addCategoryItem(payrollSettings.getSalaryCategory());
        }
        if (payrollSettings.isEnabledMultiCurrency() && payrollSettings.getCurrencies() != null) {
            currencyListBox.setItems(payrollSettings.getCurrencies());
            currencyListBox.setSelected(payrollSettings.getSalaryCurrency());
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY) != null) {
                addField(CustomFormConstants.SALARY_CURRENCY, currencyListBox, getTitle(formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY).isChanged() ? formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY).getTitle() : payrollStrings.salaryCurrency(), formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY).isRequired()));
                currencyListBox.setEnabled(!formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY).isDisabled());
            } else {
                addField(CustomFormConstants.SALARY_CURRENCY, currencyListBox, payrollStrings.salaryCurrency());
            }
        }

        citizenship.setItems(payrollSettings.getCountries());
        if (payrollSettings.getCitizenship() != null) {
            citizenship.setSelected(payrollSettings.getCitizenship());
        }
        //SET PAYROLL SETTINGS
        setPayrollSettings(payrollSettings.getPayrollSettings());

        if (payrollSettings.getPaymentCategories() != null && payrollSettings.getPaymentCategories().size() > 0) {
            for (PaymentDeductionObject paymentDeductionObject : payrollSettings.getPaymentCategories()) {
                addItem(paymentDeductionObject, PayrollConstants.CATEGORY_PAYMENT);
            }
        } else {
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_PAYMENT);
        }
        if (payrollSettings.getDeductionCategories() != null && payrollSettings.getDeductionCategories().size() > 0) {
            for (PaymentDeductionObject paymentDeductionObject : payrollSettings.getDeductionCategories()) {
                addItem(paymentDeductionObject, PayrollConstants.CATEGORY_DEDUCTION);
            }
        } else {
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_DEDUCTION);
        }
        if (payrollSettings.getTaxCategories() != null && payrollSettings.getTaxCategories().size() > 0) {
            for (PaymentDeductionObject paymentDeductionObject : payrollSettings.getTaxCategories()) {
                addItem(paymentDeductionObject, PayrollConstants.CATEGORY_TAX);
            }
        } else {
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_TAX);
        }
        if (payrollSettings.getLoanCategories() != null && payrollSettings.getLoanCategories().size() > 0) {
            for (PaymentDeductionObject paymentDeductionObject : payrollSettings.getLoanCategories()) {
                addItem(paymentDeductionObject, PayrollConstants.CATEGORY_LOAN);
            }
        } else {
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_LOAN);
        }
        if (payrollSettings.getEmployerContributions() != null && payrollSettings.getEmployerContributions().size() > 0) {
            for (PaymentDeductionObject object : payrollSettings.getEmployerContributions()) {
                addItem(object, PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);
            }
        } else {
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);
        }
        if (payrollSettings.getPayMethods() != null && payrollSettings.getPayMethods().length > 0) {
            box_pay_method.setItems(payrollSettings.getPayMethods());

            if (payrollSettings.getPayMethod() != null) {
                box_pay_method.setSelected(payrollSettings.getPayMethod().getId());
            }
        }
        if (payrollSettings.getPaymentMethod() != null && !"".equals(payrollSettings.getPaymentMethod())) {
            if (payrollSettings.getPaymentMethod().equals(wfmStrings.minSalary())) {
                payment_methods.setSelected(minSalaryMethod);
            } else if (payrollSettings.getPaymentMethod().equals(wfmStrings.midSalary())) {
                payment_methods.setSelected(midSalaryMethod);
            } else if (payrollSettings.getPaymentMethod().equals(wfmStrings.maxSalary())) {
                payment_methods.setSelected(maxSalaryMethod);
            }
        }
        if (payrollSettings.getSalaryMode() != null) {
            salaryMode.setSelectedByDescription(payrollSettings.getSalaryMode());
            box_salary.setEnabled(!payrollSettings.getSalaryMode().equals(TARIFF_GRID));
        } else {
            if (Utils.hasGenericAccess(LOCALE_PAYROLL)) {
                salaryMode.setSelected(salaryMode0);
                box_salary.setEnabled(false);
            } else {
                salaryMode.setSelected(salaryMode1);
                box_salary.setEnabled(true);
            }
        }
        calculateBasicTotalSalary();
        if (int_employeeID == null) {
            setDefaultValuesByFormProperty();
        }
    }

    private void setDefaultValuesByFormProperty() {
        if (payrollSettings.isEnabledMultiCurrency() && payrollSettings.getCurrencies() != null && formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY) != null && formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY).getDefaultValue() != null) {
            currencyListBox.setSelected(new CurrencyItem(formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY).getSelectedId(), formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME) != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME).getDefaultValue() != null) {
            box_first_name.setText(formPropertyMap.get(CustomFormConstants.FIRST_NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LAST_NAME) != null && formPropertyMap.get(CustomFormConstants.LAST_NAME).getDefaultValue() != null) {
            box_last_name.setText(formPropertyMap.get(CustomFormConstants.LAST_NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL) != null && formPropertyMap.get(CustomFormConstants.EMAIL).getDefaultValue() != null) {
            box_email.setText(formPropertyMap.get(CustomFormConstants.EMAIL).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.HIRE_DATE) != null && formPropertyMap.get(CustomFormConstants.HIRE_DATE).getDefaultValue() != null && hire_date != null) {
            if (!"".equals(formPropertyMap.get(CustomFormConstants.HIRE_DATE).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(CustomFormConstants.HIRE_DATE).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.HIRE_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.HIRE_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.HIRE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.HIRE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                hire_date.setDate(currentDate);
            } else {
                hire_date.setDate(new Date((formPropertyMap.get(CustomFormConstants.HIRE_DATE).getDefaultValue())));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CITIZENSHIP) != null && formPropertyMap.get(CustomFormConstants.CITIZENSHIP).getDefaultValue() != null) {
            citizenship.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CITIZENSHIP).getSelectedId(), formPropertyMap.get(CustomFormConstants.CITIZENSHIP).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.APPROVER) != null && formPropertyMap.get(CustomFormConstants.APPROVER).getDefaultValue() != null) {
            approver.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.APPROVER).getSelectedId(), formPropertyMap.get(CustomFormConstants.APPROVER).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE) != null && formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE).getDefaultValue() != null && contractStartDate != null) {
            contractStartDate.setDate(new Date((formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE).getDefaultValue())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE) != null && formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE).getDefaultValue() != null && contractEndDate != null) {
            contractEndDate.setDate(new Date((formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE).getDefaultValue())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID).getDefaultValue() != null) {
            paymentType.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID).getSelectedId(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD).getDefaultValue() != null) {
            box_pay_period.setSelected(new SelectItem(formPropertyMap.get(PAYROLL_STARTER.PAY_PERIOD).getSelectedId(), formPropertyMap.get(PAYROLL_STARTER.PAY_PERIOD).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD).getDefaultValue() != null) {
            box_pay_method.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD).getSelectedId(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS).getDefaultValue() != null) {
            bonusWidget.type.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS).getSelectedId(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION).getDefaultValue() != null) {
            box_commission.setText(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION).getDefaultValue());
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.ATS_PAYROLL_CUSTOMIZATION)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE) != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE).getDefaultValue() != null) {
                jobTitle.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.JOB_TITLE).getSelectedId(), formPropertyMap.get(CustomFormConstants.JOB_TITLE).getDefaultValue()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TOTAL_SALARY) != null && formPropertyMap.get(CustomFormConstants.TOTAL_SALARY).getDefaultValue() != null) {
            salaryTotalAmount.setText(formPropertyMap.get(CustomFormConstants.TOTAL_SALARY).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS).getDefaultValue() != null) {
            box_family_status.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS).getSelectedId(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.GENDER) != null && formPropertyMap.get(CustomFormConstants.GENDER).getDefaultValue() != null) {
            gender_table.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.GENDER).getSelectedId(), formPropertyMap.get(CustomFormConstants.GENDER).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WPS_NUMBER) != null && formPropertyMap.get(CustomFormConstants.WPS_NUMBER).getDefaultValue() != null) {
            wpsNo.setText(formPropertyMap.get(CustomFormConstants.WPS_NUMBER).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE) != null && formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).getDefaultValue() != null && fireDate != null) {
            fireDate.setDate(new Date((formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).getDefaultValue())));
        }
        if (Utils.isMonthlyTimeSheetEnable()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD) != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).getDefaultValue() != null) {
                payment_methods.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).getSelectedId(), formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).getDefaultValue()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS) != null && formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS).getDefaultValue() != null) {
            status.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS).getSelectedId(), formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS).getDefaultValue()));
        }
    }


    protected void registerFields() {
        //employee details
        //employee details -> first name
        box_first_name = new TextBox();
        box_first_name.addStyleName(DEFAULT_WIDTH);
        box_first_name.ensureDebugId(test_code_ID_name + "first_name");
        //employee details -> last name
        box_last_name = new TextBox();
        box_last_name.addStyleName(DEFAULT_WIDTH);
        box_last_name.ensureDebugId(test_code_ID_name + "last_name");
        //employee details -> email
        box_email = new TextBox();
        box_email.addStyleName(DEFAULT_WIDTH);
        box_email.ensureDebugId(test_code_ID_name + "email");
        //employee details -> hire date
        hire_date = new DatePicker();
        hire_date.addStyleName(DEFAULT_WIDTH);
        hire_date.ensureDebugId(test_code_ID_name + "hire_date");
        //employee details -> date of birth
        birth_date = new DOBWidget();
//        birth_date.setWidth("58px", "107px", "75px");
//        birth_date.setSpacing(5);
        birth_date.ensureDebugId(test_code_ID_name + "date_of_birth");
        //contract start date
        contractStartDate = new DatePicker();
        contractStartDate.addStyleName(DEFAULT_WIDTH);
        contractStartDate.ensureDebugId(test_code_ID_name + "contract_start_date");
        //contract end date
        contractEndDate = new DatePicker();
        contractEndDate.addStyleName(DEFAULT_WIDTH);
        contractEndDate.ensureDebugId(test_code_ID_name + "contract_start_date");
//        drawEmployeesPopup();

        currencyListBox = new DataListBox();
        currencyListBox.addStyleName(DEFAULT_WIDTH);
        currencyListBox.ensureDebugId("currencyListBox");

        citizenship = new DataListBox();
        citizenship.addStyleName(DEFAULT_WIDTH);
        citizenship.ensureDebugId(test_code_ID_name + "citizenship");

        //employee payment settings -> job title
        if (Utils.hasGenericAccess(GenericSettingsEnum.ATS_PAYROLL_CUSTOMIZATION)) {
            jobTitle = new DataListBox();

            PayrollService.App.get().getJobTitles(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable caught) {
                    Info.show(caught.toString(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(SelectItem[] result) {
                    jobTitle.setItems(result);
                    jobTitle.addValueChangeHandler(event -> {
                        if (jobTitle.getSelectedItem() != null)
                            box_salary.setText(jobTitle.getSelectedItem().getDescription());
                    });
                }
            });
        }

        //employee payment settings -> salary
        box_salary = new TextBox();
        box_salary.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        box_salary.setText(numberFormat.format(BigDecimal.ZERO));
        Validation.addNumericKeyboardListener(box_salary);
        box_salary.ensureDebugId(test_code_ID_name + "salary");
        box_salary.addKeyDownHandler(c -> onCalculateBasicTotalSalary());
        box_salary.addKeyUpHandler(c -> onCalculateBasicTotalSalary());
        box_salary.addValueChangeHandler(c -> onCalculateBasicTotalSalary());
        basicSalaryCategory = new CategoryLookUp(PayrollConstants.CATEGORY_PAYMENT);
        approver = new EmployeeByPermissionLookUp();
        approver.setPermissionCode(PAYROLL_EMPLOYEE_APPROVAL);
        //employee commission for additional payment
        box_commission = new TextBox();
        box_commission.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        box_commission.addStyleName(DEFAULT_WIDTH);
        box_commission.setText(numberFormat.format(BigDecimal.ZERO));
        Validation.addNumericKeyboardListener(box_commission);
        box_commission.ensureDebugId(test_code_ID_name + "commission");
        //employee payment settings -> normal rate
        box_normal_rate = new TextBox();
        box_normal_rate.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        box_normal_rate.addStyleName(DEFAULT_WIDTH);
        box_normal_rate.setText(numberFormat.format(BigDecimal.ZERO));
        Validation.addNumericKeyboardListener(box_normal_rate);
        box_normal_rate.ensureDebugId(test_code_ID_name + "normal_rate");
        //employee payment settings -> over time rate
        box_over_time_rate = new TextBox();
        box_over_time_rate.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        box_over_time_rate.addStyleName(DEFAULT_WIDTH);
        box_over_time_rate.setText(numberFormat.format(BigDecimal.ZERO));
        Validation.addNumericKeyboardListener(box_over_time_rate);
        box_over_time_rate.ensureDebugId(test_code_ID_name + "over_time_rate");
        //employee payment settings -> pay period
        box_pay_period = new DataListBox();
        box_pay_period.addStyleName(DEFAULT_WIDTH);
        box_pay_period.setItems(PayrollClientUtils.getPayFrequencies(Utils.isArabicCompany()));
        box_pay_period.ensureDebugId(test_code_ID_name + "pay_period");
        //employee payment settings -> pay method
        box_pay_method = new DataListBox();
        box_pay_method.addStyleName(DEFAULT_WIDTH);
        box_pay_method.ensureDebugId(test_code_ID_name + "pay_method");

        //employee payment settings
        paymentType = new DataListBox();
        paymentType.addStyleName(DEFAULT_WIDTH);
        paymentType.setWithoutNullLabel(true);
        //Generic Payment Types
        paymentType.addListItem(radio_fixed_rate);
        paymentType.addListItem(radio_by_timesheet_only);
        paymentType.setSelected(radio_fixed_rate);

        if (Utils.isMonthlyTimeSheetEnable()) {
            //if settings called MONTHLY_TIMESHEET is true this was done for Al-Fursan
            paymentType.addListItem(radio_fixed_overtime_rate);
        } else {
            //For all other non Al-Fursan companies
//            paymentType.addListItem(radio_fixed_via_hourly_rate);
        }
        paymentType.addListItem(radio_based_on_monthly_timesheet);
//        paymentType.addListItem(radio_based_on_attendance_report);
        paymentType.setSelected(radio_fixed_rate);
        registerRateTypeChangeListener();


        //employee payslip bonus settings
        bonusWidget = new BonusWidget();

        salary_table = new InputGroup(InputGroup.wrapIntoGroupContent(basicSalaryCategory), InputGroup.wrapIntoGroupContent(box_salary));

        //payroll settings
        //employee payment settings -> start date
        start_date = new DatePicker();
        start_date.addStyleName(DEFAULT_WIDTH);
        start_date.ensureDebugId(test_code_ID_name + "start_date");
//        start_date.setDate(DateUtil.resetTime(new Date()));
        //employee payment settings -> employee has form
        box_forms = new DataListBox();
        box_forms.addStyleName(DEFAULT_WIDTH);
        box_forms.setItems(FORMS_LIST);
        box_forms.ensureDebugId(test_code_ID_name + "employee_has_form");
        box_forms.addValueChangeHandler(event -> {
            //
            if (box_forms.isSomethingSelected()) {
                if (P46.equals(box_forms.getSelectedItem().getDescription())) {
                    visibleJobDetailsFields(true);
                    visibleP45DetailsFields(false);
                } else {
                    visibleJobDetailsFields(false);
                    visibleP45DetailsFields(true);
                }
            } else {
                visibleJobDetailsFields(false);
                visibleP45DetailsFields(false);
            }
        });
        //employee payment settings -> previous end date (date left previous employement)
        previous_end_date = new DatePicker();
        previous_end_date.addStyleName(DEFAULT_WIDTH);
        previous_end_date.ensureDebugId(test_code_ID_name + "previous_end_date");
        //employee payment settings -> office number
        box_office_number = new TextBox();
        box_office_number.setWidth("40px");
//        box_office_number.getElement().getStyle().setMarginRight(2, Style.Unit.PX);
        box_office_number.ensureDebugId(test_code_ID_name + "office_number");
        //employee payment settings -> reference number
        box_reference_number = new TextBox();
        box_reference_number.setWidth("200px");
        box_reference_number.ensureDebugId(test_code_ID_name + "reference_number");
        //employee payment settings -> office number VIA reference number table
        FlexTable office_via_reference_number_table = new FlexTable();
        office_via_reference_number_table.addStyleName(DEFAULT_WIDTH);
        office_via_reference_number_table.setWidget(0, 0, box_office_number);
        office_via_reference_number_table.setWidget(0, 1, box_reference_number);
        //employee payment settings -> previous tax code
        box_prev_tax_code = new TextBox();
        box_prev_tax_code.setWidth("150px");
        box_prev_tax_code.ensureDebugId(test_code_ID_name + "previous_tax_code");
        //employee payment settings -> previous wk1_month1
        check_prev_wk1_mth1 = new KpiCheckBox(" Wk1/Mnth1");
        check_prev_wk1_mth1.setWordWrap(false);
        check_prev_wk1_mth1.setWidth("100px");
        check_prev_wk1_mth1.ensureDebugId(test_code_ID_name + "checkbox_previous_wk1_mth1");
        //employee payment settings -> previous tax code VIA previous wk1_month1 table
        FlexTable prev_tax_code_via_prev_wk1_mth1_table = new FlexTable();
        prev_tax_code_via_prev_wk1_mth1_table.addStyleName(DEFAULT_WIDTH);
        prev_tax_code_via_prev_wk1_mth1_table.setWidget(0, 0, box_prev_tax_code);
        prev_tax_code_via_prev_wk1_mth1_table.setWidget(0, 1, check_prev_wk1_mth1);
        prev_tax_code_via_prev_wk1_mth1_table.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        prev_tax_code_via_prev_wk1_mth1_table.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        //employee payment settings -> week/month type
        box_week_month_type = new DataListBox();
        box_week_month_type.addStyleName(DEFAULT_WIDTH);
        box_week_month_type.setItems(PayrollClientUtils.getPayFrequencies(Utils.isArabicCompany()));
        box_week_month_type.ensureDebugId(test_code_ID_name + "week_month_type");
        box_week_month_type.addValueChangeHandler(event -> {
            if (box_week_month_type.isSomethingSelected()) {
                setPaymentPeriods(box_week_month_type.getSelectedItem().getName());
            } else {
                box_week_month_number.clear();
            }
        });
        //employee payment settings -> week/month number
        box_week_month_number = new DataListBox();
        box_week_month_number.addStyleName(DEFAULT_WIDTH);
        box_week_month_number.ensureDebugId(test_code_ID_name + "week_month_number");
        //employee payment settings -> total pay to date
        box_total_pay_to_date = new TextBox();
        box_total_pay_to_date.addStyleName(DEFAULT_WIDTH);
        box_total_pay_to_date.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        box_total_pay_to_date.setText(numberFormat.format(BigDecimal.ZERO));
        Validation.addNumericKeyboardListener(box_total_pay_to_date);
        box_total_pay_to_date.ensureDebugId(test_code_ID_name + "total_pay_to_date");
        //employee payment settings -> total tax to date
        box_total_tax_to_date = new TextBox();
        box_total_tax_to_date.addStyleName(DEFAULT_WIDTH);
        box_total_tax_to_date.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        box_total_tax_to_date.setText(numberFormat.format(BigDecimal.ZERO));
        Validation.addNumericKeyboardListener(box_total_tax_to_date);
        box_total_tax_to_date.ensureDebugId(test_code_ID_name + "total_tax_to_date");
        //employee payment settings -> yes/no list items
        SelectItem[] YES_NO_LIST_ITEMS = new SelectItem[]{
                new SelectItem(0, wfmStrings.yes(), YES),
                new SelectItem(1, wfmStrings.no(), NO)
        };
        //employee payment settings -> deduct student load
        box_student_loan = new DataListBox();
        box_student_loan.addStyleName(DEFAULT_WIDTH);
        box_student_loan.setItems(YES_NO_LIST_ITEMS);
        box_student_loan.setSelected(1);
        box_student_loan.ensureDebugId(test_code_ID_name + "deduct_student_load");
        //employee payment settings -> Does your employee have another job where they already pay NICs
        box_have_another_job = new DataListBox();
        box_have_another_job.addStyleName(DEFAULT_WIDTH);
        box_have_another_job.setItems(YES_NO_LIST_ITEMS);
        box_have_another_job.ensureDebugId(test_code_ID_name + "have_another_job");
        //employee payment settings -> family status
        box_family_status = new DataListBox();
        box_family_status.addStyleName(DEFAULT_WIDTH);
        box_family_status.setItems(FAMILY_STATUS_LIST);
        box_family_status.ensureDebugId(test_code_ID_name + "family_status");

        //employee payment settings -> gender table
        gender_table = new DataListBox();
        gender_table.addStyleName(DEFAULT_WIDTH);
        gender_table.setWithoutNullLabel(true);
        gender_table.addListItem(radio_gender_male);
        gender_table.addListItem(radio_gender_female);
        gender_table.setSelected(radio_gender_male);
        //employee payment settings -> NI category
        box_ni_category = new DataListBox();
        box_ni_category.addStyleName(DEFAULT_WIDTH);
        box_ni_category.setAllowFirstItem(true);
        box_ni_category.setItems(TABLE_LETTER_LIST);
        box_ni_category.ensureDebugId(test_code_ID_name + "ni_category");
        box_ni_category.addValueChangeHandler(event -> {
            //register ni category logic
            calculateNICategory();
        });

        //employee payment settings -> calculate NI link
        link_calculate_NI = new SimpleLink(wfmStrings.calculate());
        link_calculate_NI.setWordWrap(false);
        link_calculate_NI.addStyleName("addField");
        link_calculate_NI.ensureDebugId(test_code_ID_name + "ni_calculate_link");
        link_calculate_NI.addClickHandler(event -> {
            //register calculate NI letter logic
            calculateNILetter();
        });
        VerticalPanel ni_category_panel = new VerticalPanel();
        ni_category_panel.add(box_ni_category);
        ni_category_panel.add(link_calculate_NI);
        ni_category_panel.setCellHorizontalAlignment(link_calculate_NI, HasHorizontalAlignment.ALIGN_RIGHT);
        FlowPanel ni_category_FlowPanel = new FlowPanel();
        ni_category_FlowPanel.addStyleName("addFieldSet");
        ni_category_FlowPanel.add(ni_category_panel);

        //employee payment settings -> current tax code
        box_tax_code = new TextBox();
        box_tax_code.setWidth("150px");
        box_tax_code.setText("647L");
        box_tax_code.ensureDebugId(test_code_ID_name + "current_tax_code");
        //employee payment settings -> current wk1_month1
        check_wk1_mth1 = new KpiCheckBox(" Wk1/Mnth1");
        check_wk1_mth1.setWordWrap(false);
        check_wk1_mth1.setWidth("100px");
        check_wk1_mth1.ensureDebugId(test_code_ID_name + "checkbox_wk1_mth1");
        //employee payment settings -> current tax code VIA current wk1_month1 table
        FlexTable current_tax_code_via_prev_wk1_mth1_table = new FlexTable();
        current_tax_code_via_prev_wk1_mth1_table.addStyleName(DEFAULT_WIDTH);
        current_tax_code_via_prev_wk1_mth1_table.setWidget(0, 0, box_tax_code);
        current_tax_code_via_prev_wk1_mth1_table.setWidget(0, 1, check_wk1_mth1);
        current_tax_code_via_prev_wk1_mth1_table.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        current_tax_code_via_prev_wk1_mth1_table.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

        //employee payment settings -> Not a member of COOPS
        check_not_member_of_COOPS = new KpiCheckBox();
        check_not_member_of_COOPS.setName("true");//
        check_not_member_of_COOPS.ensureDebugId(test_code_ID_name + "not_a_member_of_COOPS");
        //employee payment settings -> pension scheme
        pension_scheme = new CustomList(Design.CHECK);
        pension_scheme.setWidth("200px");
        pension_scheme.setHeight(150);
        pension_scheme.ensureDebugId(test_code_ID_name + "pension_scheme");


        //employee payment settings -> job details
        check_d = new KpiCheckBox();
        check_d.ensureDebugId(test_code_ID_name + "job_details_d");
        //employee payment settings -> job details table
        job_details = new DataListBox();
        job_details.addStyleName(DEFAULT_WIDTH);
        job_details.addListItem(type_a);
        job_details.addListItem(type_b);
        job_details.addListItem(type_c);

//        registerJobTypeListener();

//        job_details_table.setWidget(3, 0, check_d);

        visibleJobDetailsFields(false);


        empCode = new Numbering();
//        empCode.addStyleName(DEFAULT_WIDTH);
        empCode.ensureDebugId(test_code_ID_name + "employee_code");

        wpsNo = new TextBox();
        wpsNo.addStyleName(DEFAULT_WIDTH);
        wpsNo.ensureDebugId(test_code_ID_name + "wps_no");

        wageRate = new TextBox();
        wageRate.addStyleName(DEFAULT_WIDTH);
        wageRate.ensureDebugId(test_code_ID_name + "wage_rate");

        Validation.addNumericKeyboardListener(wageRate);
        wageRate.addKeyPressHandler(keyPressEvent -> wageRate.removeStyleName(Constants.ERROR_FORM_STYLE));


        //fire(resignation) date
        fireDate = new DatePicker();
        fireDate.addStyleName(DEFAULT_WIDTH);
        fireDate.ensureDebugId(test_code_ID_name + "resignation_date");

        agentID = new TextBox();
        agentID.addStyleName(DEFAULT_WIDTH);

        paymentsTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.payments(), false, false, false));
        deductionsTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.deductions(), false, false, true));
        taxTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.taxes(), false, false, true));
        employerContributionTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.employerContribution(), false, false, true));
        loansTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.loans(), true, false, false));
        overtimeSettingsTable = new EditableTable(getOvertimeSettingsTableColumns(), false);
        drawOverTimeSettingsTable(payrollStrings.regularOvertimeRate(), null, null, null, true, null);
        drawOverTimeSettingsTable(wfmStrings.weekendOvertimeRate(), null, null, null, true, null);
        drawOverTimeSettingsTable(wfmStrings.holidayOvertimeRate(), null, null, null, true, null);

        initTable(PayrollConstants.CATEGORY_PAYMENT);
        initTable(PayrollConstants.CATEGORY_DEDUCTION);
        initTable(PayrollConstants.CATEGORY_TAX);
        initTable(PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);
        initTable(PayrollConstants.CATEGORY_LOAN);

        if (int_employeeID == null && employeeTemplateID != null) {
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_PAYMENT);
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_DEDUCTION);
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_TAX);
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_LOAN);
            addPaymentDeductionEmptyRows(0, PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);
        }

        payment_methods = new DataListBox();
        payment_methods.addStyleName(DEFAULT_WIDTH);
        payment_methods.addListItem(minSalaryMethod);
        payment_methods.addListItem(midSalaryMethod);
        payment_methods.addListItem(maxSalaryMethod);

        salaryMode = new DataListBox();
        salaryMode.addStyleName(DEFAULT_WIDTH);
        if (Utils.hasGenericAccess(LOCALE_PAYROLL)) {
            salaryMode.addListItem(salaryMode0);
        }
        salaryMode.addListItem(salaryMode1);
        salaryMode.addListItem(salaryMode2);
        salaryMode.addValueChangeHandler(event -> box_salary.setEnabled(!event.getValue().getName().equals(wfmStrings.byTariffGrid())));

        salaryTotalAmount = new TextBox();
        salaryTotalAmount.ensureDebugId("employee_totalSalary");
        salaryTotalAmount.addStyleName(DEFAULT_WIDTH);
        salaryTotalAmount.setEnabled(false);
        Validation.addNumericKeyboardListener(salaryTotalAmount, 2);

        //add field items
        //employee details -> 1
        addTitleField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_DETAILS, wfmStrings.employeeInformation());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME) != null) {
            addField(CustomFormConstants.FIRST_NAME, box_first_name, getTitle(formPropertyMap.get(CustomFormConstants.FIRST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.firstName(), formPropertyMap.get(CustomFormConstants.FIRST_NAME).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.FIRST_NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.FIRST_NAME).isInformation()) {
                new KpiToolTip(box_first_name, formPropertyMap.get(CustomFormConstants.FIRST_NAME).getInformationText());
            }

            box_first_name.setEnabled(!formPropertyMap.get(CustomFormConstants.FIRST_NAME).isDisabled());
        } else {
            addField(CustomFormConstants.FIRST_NAME, box_first_name, getTitle(wfmStrings.firstName(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LAST_NAME) != null) {
            addField(CustomFormConstants.LAST_NAME, box_last_name, getTitle(formPropertyMap.get(CustomFormConstants.LAST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.LAST_NAME).getTitle() : wfmStrings.lastName(), formPropertyMap.get(CustomFormConstants.LAST_NAME).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.LAST_NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.FIRST_NAME).isInformation()) {
                new KpiToolTip(box_last_name, formPropertyMap.get(CustomFormConstants.FIRST_NAME).getInformationText());
            }

            box_last_name.setEnabled(!formPropertyMap.get(CustomFormConstants.LAST_NAME).isDisabled());
        } else {
            addField(CustomFormConstants.LAST_NAME, box_last_name, getTitle(wfmStrings.lastName(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL) != null) {
            addField(CustomFormConstants.EMAIL, box_email, getTitle(formPropertyMap.get(CustomFormConstants.EMAIL).isChanged() ? formPropertyMap.get(CustomFormConstants.EMAIL).getTitle() : wfmStrings.email(), formPropertyMap.get(CustomFormConstants.EMAIL).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.EMAIL).isInformation());
            if (formPropertyMap.get(CustomFormConstants.EMAIL).isInformation()) {
                new KpiToolTip(box_email, formPropertyMap.get(CustomFormConstants.EMAIL).getInformationText());
            }

            box_email.setEnabled(!formPropertyMap.get(CustomFormConstants.EMAIL).isDisabled());
        } else {
            addField(CustomFormConstants.EMAIL, box_email, getTitle(wfmStrings.email(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.HIRE_DATE) != null) {
            addField(CustomFormConstants.HIRE_DATE, hire_date, getTitle(formPropertyMap.get(CustomFormConstants.HIRE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.HIRE_DATE).getTitle() : wfmStrings.hireDate(), formPropertyMap.get(CustomFormConstants.HIRE_DATE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.HIRE_DATE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.HIRE_DATE).isInformation()) {
                new KpiToolTip(hire_date, formPropertyMap.get(CustomFormConstants.HIRE_DATE).getInformationText());
            }

            hire_date.setEnabled(!formPropertyMap.get(CustomFormConstants.HIRE_DATE).isDisabled());
        } else {
            addField(CustomFormConstants.HIRE_DATE, hire_date, getTitle(wfmStrings.hireDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BIRTH_DAY) != null) {
            addField(CustomFormConstants.BIRTH_DAY, birth_date, getTitle(formPropertyMap.get(CustomFormConstants.BIRTH_DAY).isChanged() ? formPropertyMap.get(CustomFormConstants.BIRTH_DAY).getTitle() : wfmStrings.dateOfBirth(), formPropertyMap.get(CustomFormConstants.BIRTH_DAY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.BIRTH_DAY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.BIRTH_DAY).isInformation()) {
                new KpiToolTip(birth_date, formPropertyMap.get(CustomFormConstants.BIRTH_DAY).getInformationText());
            }
        } else {
            addField(CustomFormConstants.BIRTH_DAY, birth_date, getTitle(wfmStrings.dateOfBirth()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CITIZENSHIP) != null) {
            addField(CustomFormConstants.CITIZENSHIP, citizenship, getTitle(formPropertyMap.get(CustomFormConstants.CITIZENSHIP).isChanged() ? formPropertyMap.get(CustomFormConstants.CITIZENSHIP).getTitle() : payrollStrings.citizenship(), formPropertyMap.get(CustomFormConstants.CITIZENSHIP).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.CITIZENSHIP).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CITIZENSHIP).isInformation()) {
                new KpiToolTip(citizenship, formPropertyMap.get(CustomFormConstants.CITIZENSHIP).getInformationText());
            }

            citizenship.setEnabled(!formPropertyMap.get(CustomFormConstants.CITIZENSHIP).isDisabled());
        } else {
            addField(CustomFormConstants.CITIZENSHIP, citizenship, payrollStrings.citizenship());
        }
        if (!(canApprove || Utils.adminOrDirector())) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.APPROVER) != null) {
                addField(CustomFormConstants.APPROVER, approver, getTitle(formPropertyMap.get(CustomFormConstants.APPROVER).isChanged() ? formPropertyMap.get(CustomFormConstants.APPROVER).getTitle() : wfmStrings.approver(), formPropertyMap.get(CustomFormConstants.APPROVER).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.APPROVER).isInformation());
                if (formPropertyMap.get(CustomFormConstants.APPROVER).isInformation()) {
                    new KpiToolTip(approver, formPropertyMap.get(CustomFormConstants.APPROVER).getInformationText());
                }

                approver.setEnabled(!formPropertyMap.get(CustomFormConstants.APPROVER).isDisabled());
            } else {
                addField(CustomFormConstants.APPROVER, approver, getTitle(wfmStrings.approver()));
            }
        }

        if (Utils.isUAECompany()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE) != null) {
                addField(CustomFormConstants.CONTRACT_START_DATE, contractStartDate, getTitle(formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE).getTitle() : wfmStrings.contractStart(), formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE).isInformation());
                if (formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE).isInformation()) {
                    new KpiToolTip(contractStartDate, formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE).getInformationText());
                }

                contractStartDate.setEnabled(!formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE).isDisabled());
            } else {
                addField(CustomFormConstants.CONTRACT_START_DATE, contractStartDate, getTitle(wfmStrings.contractStart()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE) != null) {
                addField(CustomFormConstants.CONTRACT_END_DATE, contractEndDate, getTitle(formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE).getTitle() : wfmStrings.contractEnd(), formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE).isInformation());
                if (formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE).isInformation()) {
                    new KpiToolTip(contractEndDate, formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE).getInformationText());
                }

                contractEndDate.setEnabled(!formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE).isDisabled());
            } else {
                addField(CustomFormConstants.CONTRACT_END_DATE, contractEndDate, getTitle(wfmStrings.contractEnd()));
            }
        }

        //payment settings -> 2
        addTitleField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_SETTINGS, wfmStrings.paymentSettings());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID, paymentType, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID).getTitle() : wfmStrings.employeeIsPaid(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID).isInformation()) {
                new KpiToolTip(paymentType, formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID).getInformationText());
            }

            paymentType.setEnabled(!formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID).isDisabled());
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID, paymentType, getTitle(wfmStrings.employeeIsPaid()));
        }
        if (Utils.hasPermission(PermissionConstants.PAYROLL_EMPLOYEE_BASIC_SALARY)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALARY_GRADE) != null) {
                addField(CustomFormConstants.SALARY_GRADE, salary_table, getTitle(formPropertyMap.get(CustomFormConstants.SALARY_GRADE).isChanged() ? formPropertyMap.get(CustomFormConstants.SALARY_GRADE).getTitle() : wfmStrings.paymentCategory(), formPropertyMap.get(CustomFormConstants.SALARY_GRADE).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.SALARY_GRADE).isInformation());
                if (formPropertyMap.get(CustomFormConstants.SALARY_GRADE).isInformation()) {
                    new KpiToolTip(salary_table, formPropertyMap.get(CustomFormConstants.SALARY_GRADE).getInformationText());
                }

                salary_table.setEnabled(!formPropertyMap.get(CustomFormConstants.SALARY_GRADE).isDisabled());
                basicSalaryCategory.setEnabled(!formPropertyMap.get(CustomFormConstants.SALARY_GRADE).isDisabled());
                box_salary.setEnabled(!formPropertyMap.get(CustomFormConstants.SALARY_GRADE).isDisabled());
            } else {
                addField(CustomFormConstants.SALARY_GRADE, salary_table, getTitle(wfmStrings.paymentCategory(), true));
            }
        }

        normal_rate_item = addCustomField(CustomFormConstants.PAYROLL_STARTER.NORMAL_RATE, box_normal_rate, getTitle(wfmStrings.normalRate(), true));
        over_time_rate_item = addCustomField(CustomFormConstants.PAYROLL_STARTER.OVER_TIME_RATE, box_over_time_rate, getTitle(wfmStrings.overtimeRate(), true));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD, box_pay_period, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD).getTitle() : wfmStrings.payPeriod(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD).isInformation()) {
                new KpiToolTip(box_pay_period, formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD).getInformationText());
            }

            box_pay_period.setEnabled(!formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD).isDisabled());
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD, box_pay_period, getTitle(wfmStrings.payPeriod(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD, box_pay_method, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD).getTitle() : wfmStrings.paymentMethod(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD).isInformation()) {
                new KpiToolTip(box_pay_method, formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD).getInformationText());
            }
            box_pay_method.setEnabled(!formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD).isDisabled());
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD, box_pay_method, getTitle(wfmStrings.paymentMethod()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS, bonusWidget, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS).getTitle() : wfmStrings.bonus(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS).isRequired()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS, bonusWidget, getTitle(wfmStrings.bonus()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION, box_commission, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION).getTitle() : wfmStrings.commission(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION).isRequired()));
            box_commission.setEnabled(!formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION).isDisabled());
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION, box_commission, getTitle(wfmStrings.commission()));
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.ATS_PAYROLL_CUSTOMIZATION)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE) != null) {
                addField(CustomFormConstants.JOB_TITLE, jobTitle, getTitle(formPropertyMap.get(CustomFormConstants.JOB_TITLE).isChanged() ? formPropertyMap.get(CustomFormConstants.JOB_TITLE).getTitle() : wfmStrings.jobTitle(), formPropertyMap.get(CustomFormConstants.JOB_TITLE).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.JOB_TITLE).isInformation());
                if (formPropertyMap.get(CustomFormConstants.JOB_TITLE).isInformation()) {
                    new KpiToolTip(jobTitle, formPropertyMap.get(CustomFormConstants.JOB_TITLE).getInformationText());
                }

                jobTitle.setEnabled(!formPropertyMap.get(CustomFormConstants.JOB_TITLE).isDisabled());
            } else {
                addField(CustomFormConstants.JOB_TITLE, jobTitle, wfmStrings.jobTitle());
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TOTAL_SALARY) != null) {
            addField(CustomFormConstants.TOTAL_SALARY, salaryTotalAmount, getTitle(formPropertyMap.get(CustomFormConstants.TOTAL_SALARY).isChanged() ? formPropertyMap.get(CustomFormConstants.TOTAL_SALARY).getTitle() : wfmStrings.totalSalary(), formPropertyMap.get(CustomFormConstants.TOTAL_SALARY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.TOTAL_SALARY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TOTAL_SALARY).isInformation()) {
                new KpiToolTip(salaryTotalAmount, formPropertyMap.get(CustomFormConstants.TOTAL_SALARY).getInformationText());
            }

            salaryTotalAmount.setEnabled(!formPropertyMap.get(CustomFormConstants.TOTAL_SALARY).isDisabled());
        } else {
            addField(CustomFormConstants.TOTAL_SALARY, salaryTotalAmount, wfmStrings.totalSalary());
        }
        //payroll settings -> 3
        addTitleField(CustomFormConstants.PAYROLL_STARTER.PAYROLL_SETTINGS, wfmStrings.payrollSettings());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS, box_family_status, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS).getTitle() : wfmStrings.maritalStatus(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS).isInformation()) {
                new KpiToolTip(box_family_status, formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS).getInformationText());
            }

            box_family_status.setEnabled(!formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS).isDisabled());
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS, box_family_status, getTitle(wfmStrings.maritalStatus()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.GENDER) != null) {
            addField(CustomFormConstants.GENDER, gender_table, getTitle(formPropertyMap.get(CustomFormConstants.GENDER).isChanged() ? formPropertyMap.get(CustomFormConstants.GENDER).getTitle() : wfmStrings.gender(), formPropertyMap.get(CustomFormConstants.GENDER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.GENDER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.GENDER).isInformation()) {
                new KpiToolTip(gender_table, formPropertyMap.get(CustomFormConstants.GENDER).getInformationText());
            }

            gender_table.setEnabled(!formPropertyMap.get(CustomFormConstants.GENDER).isDisabled());
        } else {
            addField(CustomFormConstants.GENDER, gender_table, getTitle(wfmStrings.gender()));
        }

        if (!Utils.isUKCompany()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE) != null) {
                addField(CustomFormConstants.EMPLOYEE_CODE, empCode, getTitle(formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE).getTitle() : wfmStrings.employeeCode(), formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE).isInformation());
                if (formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE).isInformation()) {
                    new KpiToolTip(empCode, formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE).getInformationText());
                }

                empCode.setEnabled(!formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE).isDisabled());
            } else {
                addField(CustomFormConstants.EMPLOYEE_CODE, empCode, getTitle(wfmStrings.employeeCode()));
            }
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WPS_NUMBER) != null) {
                addField(CustomFormConstants.WPS_NUMBER, wpsNo, getTitle(formPropertyMap.get(CustomFormConstants.WPS_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.WPS_NUMBER).getTitle() : wfmStrings.wpsNumber(), formPropertyMap.get(CustomFormConstants.WPS_NUMBER).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.WPS_NUMBER).isInformation());
                if (formPropertyMap.get(CustomFormConstants.WPS_NUMBER).isInformation()) {
                    new KpiToolTip(wpsNo, formPropertyMap.get(CustomFormConstants.WPS_NUMBER).getInformationText());
                }

                wpsNo.setEnabled(!formPropertyMap.get(CustomFormConstants.WPS_NUMBER).isDisabled());
            } else {
                addField(CustomFormConstants.WPS_NUMBER, wpsNo, getTitle(!"".equals(Utils.getPersonalID()) ? Utils.getPersonalID() : wfmStrings.wpsNumber()));
            }
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE) != null) {
                addField(CustomFormConstants.RESIGNATION_DATE, fireDate, getTitle(formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).getTitle() : wfmStrings.resignationDate(), formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).isInformation());
                if (formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).isInformation()) {
                    new KpiToolTip(fireDate, formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).getInformationText());
                }

                fireDate.setEnabled(!formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).isDisabled());
            } else {
                addField(CustomFormConstants.RESIGNATION_DATE, fireDate, getTitle(wfmStrings.resignationDate()));
            }
        }

        addTitleField(PAYROLL_STARTER.EMPLOYEE_OVERTIME_SETTINGS, wfmStrings.overTimeSettings());
        addField(PAYROLL_STARTER.OVERTIME_SETTINGS_TABLE, overtimeSettingsTable, null);
        if (Utils.isMonthlyTimeSheetEnable()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD) != null) {
                addField(CustomFormConstants.PAYMENT_METHOD, payment_methods, getTitle(formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).getTitle() : wfmStrings.salaryLevel(), formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).isRequired()));
                payment_methods.setEnabled(!formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).isDisabled());
            } else {
                addField(CustomFormConstants.PAYMENT_METHOD, payment_methods, getTitle(wfmStrings.salaryLevel()));
            }
        }

        if (Utils.hasGenericAccess(LOCALE_PAYROLL)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALARY_MODE) != null) {
                addField(CustomFormConstants.SALARY_MODE, salaryMode, getTitle(formPropertyMap.get(CustomFormConstants.SALARY_MODE).isChanged() ?
                                formPropertyMap.get(CustomFormConstants.SALARY_MODE).getTitle() : wfmStrings.salaryMode(),
                        formPropertyMap.get(CustomFormConstants.SALARY_MODE).isRequired()));
                salaryMode.setEnabled(!formPropertyMap.get(CustomFormConstants.SALARY_MODE).isDisabled());
            } else {
                addField(CustomFormConstants.SALARY_MODE, salaryMode, getTitle(wfmStrings.salaryMode()));
            }
        }

        status = new DataListBox();
        status.ensureDebugId(test_code_ID_name + "accountStatus");
        status.addStyleName(DEFAULT_WIDTH);
        status.setAllowFirstItem(true);
        noAccess = new KpiSwitcher();
        noAccess.ensureDebugId(test_code_ID_name + "no_access");
        noAccess.setStyleName(DEFAULT_WIDTH);
        essUser = new KpiSwitcher();
        essUser.ensureDebugId(test_code_ID_name + "ess_user");
        essUser.setStyleName(DEFAULT_WIDTH);
        noAccess.addValueChangeHandler(valueChangeEvent -> {
            if (valueChangeEvent.getValue()) {
                essUser.setValue(false);
                payrollSettings.setStatusCode(EMPLOYEE_STATUS_NO_ACCCESS);
                mainRole.setVisible(false);
            } else {
                payrollSettings.setStatusCode(EMPLOYEE_STATUS_ACTIVE);
                mainRole.setVisible(true);
            }
        });
        essUser.addValueChangeHandler(valueChangeEvent -> {
            if (valueChangeEvent.getValue()) {
                noAccess.setValue(false);
                mainRole.setVisible(false);
            } else {
                mainRole.setVisible(true);
            }
            payrollSettings.setStatusCode(EMPLOYEE_STATUS_ACTIVE);
        });
        mainRole = new CheckboxSelector();
        mainRole.ensureDebugId("account_information-role");
        mainRole.addStyleName("cellBadgetWidget-prevent-absolute");
        if (Utils.hasPermission(PermissionConstants.HRMS_SHOW_EMPLOYEE_ROLE_WIDGET)
                || Utils.isSettings() || Utils.hasPermission(PermissionConstants.PM_SHOW_EMPLOYEE_ROLE_WIDGET)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNT_ROLES) != null) {
                addField(CustomFormConstants.ACCOUNT_ROLES, mainRole, getTitle(formPropertyMap.get(CustomFormConstants.ACCOUNT_ROLES).isChanged() ? formPropertyMap.get(CustomFormConstants.ACCOUNT_ROLES).getTitle() : wfmStrings.roles(), formPropertyMap.get(CustomFormConstants.ACCOUNT_ROLES).isRequired()));
            } else {
                addField(CustomFormConstants.ACCOUNT_ROLES, mainRole, getTitle(wfmStrings.roles()));
            }
            mainRole.addStyleName(test_code_ID_name + "account_roles");
        }
        noAccess.setValue(false);
        mainRole.setVisible(true);

        addTitleField(CustomFormConstants.PAYROLL_STARTER.ACCOUNT_INFORMATION, wfmStrings.basicInfo());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS) != null) {
            addField(CustomFormConstants.ACCOUNT_STATUS, status, getTitle(formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS).getTitle() : wfmStrings.accountStatus(), formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS).isRequired()));
            status.setEnabled(!formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS).isDisabled());
        } else {
            addField(CustomFormConstants.ACCOUNT_STATUS, status, getTitle(wfmStrings.accountStatus()));
        }
        //account roles
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NO_ACCESS) != null) {
            addField(CustomFormConstants.NO_ACCESS, noAccess, getTitle(formPropertyMap.get(CustomFormConstants.NO_ACCESS).isChanged() ? formPropertyMap.get(CustomFormConstants.NO_ACCESS).getTitle() : wfmStrings.noAccess(), formPropertyMap.get(CustomFormConstants.NO_ACCESS).isRequired()));
            noAccess.setEnabled(!formPropertyMap.get(CustomFormConstants.NO_ACCESS).isDisabled());
        } else {
            addField(CustomFormConstants.NO_ACCESS, noAccess, getTitle(wfmStrings.noAccess()));
        }


        visibleSalaryBoxFields(true);
        visibleNormalRateFields(false);
        visibleOverTimeRateFields(false);
        drawPaymentDeductionCategoryTable();

        show();
    }

    protected EditableTable getTable(final String from) {
        switch (from) {
            case PayrollConstants.CATEGORY_PAYMENT:
                return paymentsTable;
            case PayrollConstants.CATEGORY_DEDUCTION:
                return deductionsTable;
            case PayrollConstants.CATEGORY_TAX:
                return taxTable;
            case PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION:
                return employerContributionTable;
            default:
                return loansTable;
        }
    }

    private void initTable(final String from) {
        EditableTable table = getTable(from);
        table.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                addItem(null, from);
            }

            @Override
            public void removeRow() {

            }
        });

        table.setRemoveRowListener(() -> {
            if (table.getRowCount() > 1) {
                final PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) table.getColumnById(table.getGrid().getCurrentRow(), "amount");
                if (amountWidget.getItemID() != null & amountWidget.isUsed()) {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setWidth("400px");
                    messageBox.setTitle(wfmStrings.confirmation());
                    messageBox.setMessage("Please note that you cannot delete the Category which has been used in Payslips.<br/><br/>If you will not use this Recurring Category, you can make it inactive so that it does not appear in Recurring Payment/Deduction categories list. Do you want to make it Inactive?");
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            inactiveCategories.add(amountWidget.getItemID());
                            table.getGrid().getModel().removeRow(table.getGrid().getCurrentRow());
                            onCalculateBasicTotalSalary();
                        }
                    });
                    messageBox.open();
                } else if (amountWidget.getItemID() != null) {
                    deletedCategories.add(amountWidget.getItemID());
                    table.getGrid().getModel().removeRow(table.getGrid().getCurrentRow());
                    onCalculateBasicTotalSalary();
                } else {
                    table.getGrid().getModel().removeRow(table.getGrid().getCurrentRow());
                    onCalculateBasicTotalSalary();
                }
            } else {
                WfmWindow.alert(wfmStrings.youCanNotRemoveOneLineItem());
            }
        });
    }

    public void drawOverTimeSettingsTable(String overtimeName, SelectItem overtimeCategory, String overtimeType, String amount, boolean newItem, Integer index) {

        if (newItem) {

            final CategoryLookUp categoryLookUp = new CategoryLookUp(PayrollConstants.CATEGORY_PAYMENT);
//            categoryLookUp.getSuggestBox().getElement().setAttribute("style", "width:200px !important");

            EditableTextBox overTimeNameBox = new EditableTextBox();
            overTimeNameBox.setEnabled(false);
            overTimeNameBox.setText(overtimeName);

            final DataListBox type = new DataListBox();
            type.setWithoutNullLabel(true);
            type.setItems(new SelectItem[]{
                    new SelectItem(0, wfmStrings.fixed()),
                    new SelectItem(1, wfmStrings.basicOfPersentage())
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
                if (FIXED.equals(overtimeType)) {
                    type.setSelected(0);
                } else {
                    type.setSelected(1);
                }
            } else {
                type.setSelected(0);
            }

            if (overtimeCategory != null) {
                categoryLookUp.setSelected(overtimeCategory);
            }

            PayslipItemAmountWidget amountCell = new PayslipItemAmountWidget();

            if (amount != null && !"".equals(amount)) {
                amountCell.setAmount(PayrollClientUtils.parseToBigDecimal(amount));
            }
            overtimeSettingsTable.addRow(new Widget[]{categoryLookUp, overTimeNameBox, type, amountCell});
        } else {
            CategoryLookUp categoryLookUp = (CategoryLookUp) overtimeSettingsTable.getColumnById(index, "paymentCategory");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) overtimeSettingsTable.getColumnById(index, "amount");
            DataListBox typeWidget = (DataListBox) overtimeSettingsTable.getColumnById(index, "type");
            CustomCell amountWidgetCell = (CustomCell) overtimeSettingsTable.getColumnCellWidgetById(index, "amount");
            CustomCell typeWidgetCell = (CustomCell) overtimeSettingsTable.getColumnCellWidgetById(index, "type");

            if (overtimeType != null && !"".equals(overtimeType)) {
                if (FIXED.equals(overtimeType)) {
                    typeWidget.setSelected(0);
                } else {
                    typeWidget.setSelected(1);
                }
            } else {
                typeWidget.setSelected(0);
            }

            if (overtimeCategory != null) {
                categoryLookUp.setSelected(overtimeCategory);
            }
            if (amount != null && !"".equals(amount)) {
                amountWidget.setAmount(PayrollClientUtils.parseToBigDecimal(amount));
            }

            amountWidgetCell.InActive();
            typeWidgetCell.InActive();
        }
    }

    public void drawPaymentDeductionCategoryTable() {
        addTitleField(CustomFormConstants.PAYMENT_DEDUCTION_INFORMATION, wfmStrings.paymentDeductionCategoryTable());
        addField(CustomFormConstants.PAYMENT_TABLE, paymentsTable, null);
        addField(CustomFormConstants.DEDUCTION_TABLE, deductionsTable, null);
        addField(CustomFormConstants.TAX_TABLE, taxTable, null);
        addField(CustomFormConstants.EMPLOYER_CONTRIBUTION, employerContributionTable, null);
        addField(CustomFormConstants.LOAN_TABLE, loansTable, null);
    }

    public ColumnConfig[] getPaymentDeductionTableColumns(String title, boolean isLoan, boolean fromSummary, boolean isLinkedType) {
        ArrayList<ColumnConfig> columns = new ArrayList<>();
        if (isLoan) {
            columns.add(new ColumnConfig(fromSummary ? CustomCell.class : LookUpCell.class, "category", title, 120, true, "left-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 80, true, "left-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount() + " / " + wfmStrings.percentage(), 70, true, "right-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "totalAmount", wfmStrings.totalAmount(), 70, true, "right-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "startDate", wfmStrings.startDate(), 110, true, "left-align-Cell"));
        } else if (isLinkedType) {
            columns.add(new ColumnConfig(fromSummary ? CustomCell.class : LookUpCell.class, "category", title, 120, true, "left-align-Cell"));
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_PAYSLIP_PAYMENT_TYPE)) {
                columns.add(new ColumnConfig(CustomCell.class, "paymentType", wfmStrings.paymentType(), 80, true, "left-align-Cell"));
            }
            columns.add(new ColumnConfig(fromSummary ? CustomCell.class : LinkedLinkableCell.class, "type", wfmStrings.type(), 80, true, "left-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount() + " / " + wfmStrings.percentage(), 80, true, "right-align-Cell"));
        } else {
            columns.add(new ColumnConfig(fromSummary ? CustomCell.class : LookUpCell.class, "category", title, 120, true, "left-align-Cell"));
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_PAYSLIP_PAYMENT_TYPE)) {
                columns.add(new ColumnConfig(CustomCell.class, "paymentType", wfmStrings.paymentType(), 80, true, "left-align-Cell"));
            }
            columns.add(new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 80, true, "left-align-Cell"));
            columns.add(new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount() + " / " + wfmStrings.percentage(), 80, true, "right-align-Cell"));
        }
        return columns.toArray(new ColumnConfig[0]);
    }

    public ColumnConfig[] getOvertimeSettingsTableColumns() {
        ColumnConfig[] columns = new ColumnConfig[4];
        columns[0] = new ColumnConfig(LookUpCell.class, "paymentCategory", wfmStrings.paymentCategory(), 120, false, "left-align-Cell");
        columns[1] = new ColumnConfig(CustomCell.class, "overtimeRate", wfmStrings.overtimeRate(), 120, true, "left-align-Cell");
        columns[2] = new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 80, true, "left-align-Cell");
        columns[3] = new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount() + " / " + wfmStrings.percentage(), 80, true, "right-align-Cell");
        return columns;
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
        final LinkedTypeWidget deductType = new LinkedTypeWidget();
        final CategoryLookUp categoryLookUp = new CategoryLookUp(from, () -> true);
//        categoryLookUp.getSuggestBox().getElement().setAttribute("style", "width:200px !important");
        if (paymentDeduction != null && paymentDeduction.getCategoryItem() != null) {
            categoryLookUp.addCategoryItem(paymentDeduction.getCategoryItem());
        }
        categoryLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onCategorySelected(categoryLookUp, from));
        categoryLookUp.getSuggestBox().addKeyUpHandler(event -> onCategorySelected(categoryLookUp, from));

        if (PayrollConstants.CATEGORY_DEDUCTION.equals(from) || PayrollConstants.CATEGORY_TAX.equals(from) || PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION.equals(from)) {
            deductType.setChangeHandler(() -> {
                PayslipItemAmountWidget amountWidget;
                CustomCell amountWidgetCell;
                LinkedLinkableCell typeWidgetCell;
                amountWidget = (PayslipItemAmountWidget) table.getColumnById(table.getGrid().getCurrentRow(), "amount");
                amountWidgetCell = (CustomCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "amount");
                typeWidgetCell = (LinkedLinkableCell) table.getColumnCellWidgetById(table.getGrid().getCurrentRow(), "type");
                deductType.showOrRemoveLink();
                amountWidget.showPercentage(deductType.getSelectedId() != 0);

                amountWidgetCell.InActive();
                typeWidgetCell.InActive();
                onCalculateBasicTotalSalary();
            });
            deductType.setCopyFromBoxHandler(() -> {
                deductType.clearCategoriesTable();
                for (int i = 0; i < paymentsTable.getRowCount(); i++) {
                    CategoryLookUp categoryLookUp1 = (CategoryLookUp) paymentsTable.getColumnById(i, "category");
                    if (categoryLookUp1.getSelectedData() != null) {
                        PaymentDeductionObject object = new PaymentDeductionObject();
                        object.setCategoryItem(categoryLookUp1.getSelectedData());
                        deductType.addItem(object, true);
                    }
                }
            });
            if (paymentDeduction != null && paymentDeduction.getType() != null) {
                deductType.setSelected(paymentDeduction.getType());
                if (paymentDeduction.getLinkedCategories() != null && paymentDeduction.getLinkedCategories().size() > 0) {
                    deductType.setLinkedItems(paymentDeduction.getLinkedCategories());
                } else if (paymentDeduction.isFromAllAllowances()) {
                    deductType.clearCategoriesTable();
                    deductType.setValue(paymentDeduction.isFromAllAllowances());
                    for (int i = 0; i < paymentsTable.getRowCount(); i++) {
                        CategoryLookUp category = (CategoryLookUp) paymentsTable.getColumnById(i, "category");
                        if (category.getSelectedData() != null) {
                            PaymentDeductionObject object = new PaymentDeductionObject();
                            object.setCategoryItem(category.getSelectedData());
                            deductType.addItem(object, true);
                        }
                    }
                }
            } else {
                deductType.setSelected(0);
            }
        } else {
            type.setWithoutNullLabel(true);
            type.setItems(new SelectItem[]{
                    new SelectItem(0, wfmStrings.fixed()),
                    new SelectItem(1, wfmStrings.basicOfPersentage())
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
                onCalculateBasicTotalSalary();
            });
        }
        Command calculate = () -> {
            if (PayrollConstants.CATEGORY_PAYMENT.equals(from) || PayrollConstants.CATEGORY_DEDUCTION.equals(from) || PayrollConstants.CATEGORY_TAX.equals(from)) {
                onCalculateBasicTotalSalary();
            }
        };

        final PayslipItemAmountWidget amountWidget = new PayslipItemAmountWidget();
        amountWidget.getAmountTextBox().addKeyPressHandler(keyPressEvent -> calculate.execute());
        amountWidget.getAmountTextBox().addKeyUpHandler(keyPressEvent -> calculate.execute());
        amountWidget.getAmountTextBox().addChangeHandler(keyPressEvent -> calculate.execute());
        if (paymentDeduction != null) {
            amountWidget.setAmount(paymentDeduction.getType() == null || paymentDeduction.getType() == 0 ? paymentDeduction.getPaymentAmount() : paymentDeduction.getPercentage());

            amountWidget.showPercentage(paymentDeduction.getType() != null && paymentDeduction.getType() != 0);
            amountWidget.setItemID(paymentDeduction.getId());
            amountWidget.setPsdId(paymentDeduction.getPsdId());
            amountWidget.setUsed(paymentDeduction.isUsed());
        }

        CustomDatePicker startDate = new CustomDatePicker();
        if (paymentDeduction != null && paymentDeduction.getStarttDate() != null && paymentDeduction.getStarttDate().getDate() != null) {
            startDate.setDate(paymentDeduction.getStarttDate().getNonConvertedDate());
        }
        EditableTextBox totalAmount = new EditableTextBox();
        Validation.addNumericKeyboardListener(totalAmount);
        if (paymentDeduction != null && paymentDeduction.getTotalAmount() != null) {
            totalAmount.setText(PayrollClientUtils.format(paymentDeduction.getTotalAmount()));
        }

        Widget typeWidget = PayrollConstants.CATEGORY_DEDUCTION.equals(from) || PayrollConstants.CATEGORY_TAX.equals(from) || PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION.equals(from) ? deductType : type;
        if (PayrollConstants.CATEGORY_LOAN.equals(from)) {
            table.addRow(new Widget[]{categoryLookUp, typeWidget, amountWidget, totalAmount, startDate/*, remainingAmount*/});
        } else {
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_PAYSLIP_PAYMENT_TYPE)) {
                table.addRow(new Widget[]{categoryLookUp, epPaymentType, typeWidget, amountWidget});
                cmdEPPaymentType.execute();
            } else {
                table.addRow(new Widget[]{categoryLookUp, typeWidget, amountWidget});
            }
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
            if (PayrollConstants.CATEGORY_PAYMENT.equals(from)) {
                for (int i = 0; i < overtimeSettingsTable.getGrid().getRowCount(); i++) {
                    PaymentDeductionSelectItem selectedItem = ((CategoryLookUp) overtimeSettingsTable.getColumnById(i, "category")).getSelectedData();
                    if (selectedItem != null && selectedItem.getCode() != null && selectedCategory.getCode() != null && selectedItem.getCode().equals(selectedCategory.getCode())) {
                        selectedCategoryCount++;
                    }
                }
            }

            if (selectedCategoryCount >= 2) {
                categoryLookUp.clear();
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, selectedCategory.getName() + wfmStrings.isAlreadySelected());
                messageBox.open();
            }
        }
    }

    private void addPaymentDeductionEmptyRows(int length, String type) {
        while (length < DEFAULT_ROWS) {
            addItem(null, type);
            length++;
        }
    }

    protected String getPaymentPeriods(Integer wmNumbers, String weekMonthType) {
        if (weekMonthType.startsWith("M")) {
            return "M" + wmNumbers;
        } else if (weekMonthType.startsWith("W")) {
            return "W" + wmNumbers;
        } else if (weekMonthType.startsWith("Y")) {
            return "W" + wmNumbers;
        } else if (weekMonthType.startsWith("A")) {
            return "W" + wmNumbers;
        }
        return "";
    }

    protected void initialize2() {
        //exceeded users limit
        if (saveAndNewButton != null) {
            saveAndNewButton.setVisible(false);
        }
        if (saveAndCloseButton != null) {
            saveAndCloseButton.setVisible(false);
        }
        if (updateButton != null) {
            updateButton.setVisible(false);
        }

        HTML usersLimitExceeded = new HTML(wfmStrings.usersLimitExceeded());
        SimpleLink currentSubscriptionLink = new SimpleLink(wfmStrings.currentSubscriptionSection());
        currentSubscriptionLink.ensureDebugId(test_code_ID_name + "current_subscription_link");
        currentSubscriptionLink.addClickHandler(event -> Utils.redirect(GWT.getHostPageBaseURL() + "Myaccount.html#settings|CurrentUsagePlanView"));
        HorizontalPanel linkPanel = new HorizontalPanel();
        linkPanel.add(usersLimitExceeded);
        linkPanel.add(currentSubscriptionLink);
        WfmPanel helpPanel = new WfmPanel(WfmPanel.Styles.GRAY);
        helpPanel.setWidget(linkPanel);
        helpPanel.setWidth("850px");
        //add field items
        //employee payroll settings -> 1
        addField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_PAYROLL_SETTINGS, helpPanel, null);

        show();
    }

    @Override
    protected void initPredefinedValues() {
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    protected void setPayrollSettings(HashMap<String, String> payrollSettings) {

        String pensionSchemes = null;
        if (payrollSettings != null && payrollSettings.size() == 0) {

            box_salary.setText(numberFormat.format(BigDecimal.ZERO));
            box_tax_code.setText("647L");
            box_total_pay_to_date.setText(numberFormat.format(BigDecimal.ZERO));
            box_total_tax_to_date.setText(numberFormat.format(BigDecimal.ZERO));
            //for edit view
            check_not_member_of_COOPS.setEnabled("true".equals(check_not_member_of_COOPS.getName()));
        }

        if (payrollSettings != null) {
            SelectItem overtimeCategory;
            if (payrollSettings.get(SEX) != null) {
                if (payrollSettings.get(SEX).equals("Male")) {
                    gender_table.setSelected(radio_gender_male);
                } else {
                    gender_table.setSelected(radio_gender_female);
                }
            }
            if (payrollSettings.get(FORMS) != null) {
                selectByName(payrollSettings.get(FORMS), box_forms);
                if (payrollSettings.get(FORMS).equals("P46")) {
                    visibleJobDetailsFields(true);
                    visibleP45DetailsFields(false);
                } else {
                    visibleJobDetailsFields(false);
                    visibleP45DetailsFields(true);
                }
            }
            if (prevEndDate != null) {
                previous_end_date.setDate(prevEndDate);
            }
            if (startDateForOnlyPayroll != null) {
                start_date.setDate(startDateForOnlyPayroll);
            }

            if (payrollSettings.get(CustomFormConstants.CONTRACT_START_DATE) != null) {
                contractStartDate.setDate(dateFormat.parse(payrollSettings.get(CustomFormConstants.CONTRACT_START_DATE)));
            }

            if (payrollSettings.get(CustomFormConstants.CONTRACT_END_DATE) != null) {
                contractEndDate.setDate(dateFormat.parse(payrollSettings.get(CustomFormConstants.CONTRACT_END_DATE)));
            }

            if (payrollSettings.get(OFFICE_NUMBER) != null) {
                box_office_number.setText(payrollSettings.get(OFFICE_NUMBER));
            }
            if (payrollSettings.get(PAYE_REF_NUMBER) != null) {
                box_reference_number.setText(payrollSettings.get(PAYE_REF_NUMBER));
            }
            if (payrollSettings.get(PREV_TAX_CODE) != null) {
                box_prev_tax_code.setText(payrollSettings.get(PREV_TAX_CODE));
            }
            if (payrollSettings.get(PREV_WK1MTH1) != null) {
                if ("true".equals(payrollSettings.get(PREV_WK1MTH1))) {
                    check_prev_wk1_mth1.setValue(true);
                }
            }
            if (payrollSettings.get(WEEK_MONTH_TYPE) != null) {
                box_week_month_type.setSelected(Integer.valueOf(payrollSettings.get(WEEK_MONTH_TYPE)));
                setPaymentPeriods(box_week_month_type.getSelectedItem().getName());
            }
            if (payrollSettings.get(WEEK_MONTH_NUMBER) != null) {
                box_week_month_number.setSelected(Integer.valueOf(payrollSettings.get(WEEK_MONTH_NUMBER)));
            }
            if (payrollSettings.get(TOTAL_PAY_TO_DATE) != null) {
                box_total_pay_to_date.setText(payrollSettings.get(TOTAL_PAY_TO_DATE));
            }
            if (payrollSettings.get(TOTAL_TAX_TO_DATE) != null) {
                box_total_tax_to_date.setText(payrollSettings.get(TOTAL_TAX_TO_DATE));
            }
            if (payrollSettings.get(HAVE_ANOTHER_JOB) != null) {
                selectByName(payrollSettings.get(HAVE_ANOTHER_JOB), box_have_another_job);
            }
            if (payrollSettings.get(FAMILY_STATUS) != null) {
                selectByName(payrollSettings.get(FAMILY_STATUS), box_family_status);
            }

            if (payrollSettings.get(WAGE_RATE) != null) {
                wageRate.setText(payrollSettings.get(WAGE_RATE));
            }
            if (payrollSettings.get(REGULAR_OVERTIME_RATE) != null) {
                overtimeCategory = payrollSettings.get(REGULAR_OVERTIME_CATEGORY_ID) != null ? new SelectItem(Integer.valueOf(payrollSettings.get(REGULAR_OVERTIME_CATEGORY_ID)), payrollSettings.get(REGULAR_OVERTIME_CATEGORY_NAME)) : null;
                drawOverTimeSettingsTable(null, overtimeCategory, payrollSettings.get(REGULAR_OVERTIME_RATE_TYPE), payrollSettings.get(REGULAR_OVERTIME_RATE), false, 0);
            }

            if (payrollSettings.get(WEEKEND_OVERTIME_RATE) != null) {
                overtimeCategory = payrollSettings.get(WEEKEND_OVERTIME_CATEGORY_ID) != null ? new SelectItem(Integer.valueOf(payrollSettings.get(WEEKEND_OVERTIME_CATEGORY_ID)), payrollSettings.get(WEEKEND_OVERTIME_CATEGORY_NAME)) : null;
                drawOverTimeSettingsTable(null, overtimeCategory, payrollSettings.get(WEEKEND_OVERTIME_RATE_TYPE), payrollSettings.get(WEEKEND_OVERTIME_RATE), false, 1);
            }

            if (payrollSettings.get(HOLIDAY_OVERTIME_RATE) != null) {
                overtimeCategory = payrollSettings.get(HOLIDAY_OVERTIME_CATEGORY_ID) != null ? new SelectItem(Integer.valueOf(payrollSettings.get(HOLIDAY_OVERTIME_CATEGORY_ID)), payrollSettings.get(HOLIDAY_OVERTIME_CATEGORY_NAME)) : null;
                drawOverTimeSettingsTable(null, overtimeCategory, payrollSettings.get(HOLIDAY_OVERTIME_RATE_TYPE), payrollSettings.get(HOLIDAY_OVERTIME_RATE), false, 2);
            }

            if (payrollSettings.get(CustomFormConstants.WPS_NUMBER) != null) {
                wpsNo.setText(payrollSettings.get(CustomFormConstants.WPS_NUMBER));
            }
            if (payrollSettings.get(NI_TABLE_LETTER) != null) {
                selectByName(payrollSettings.get(NI_TABLE_LETTER), box_ni_category);
            }
            if (payrollSettings.get(TAX_CODE) != null) {
                box_tax_code.setText(payrollSettings.get(TAX_CODE));
            }
            if (payrollSettings.get(WK1MNTH1) != null) {
                check_wk1_mth1.setValue("true".equals(payrollSettings.get(WK1MNTH1)));
            }
            if (payrollSettings.get(NOT_MEMBER_OF_COOPS) != null) {
                if ("true".equals(payrollSettings.get(NOT_MEMBER_OF_COOPS))) {
                    check_not_member_of_COOPS.setValue(true);
//                        pension_scheme.setEnabled(false);
                }
            }
            if (payrollSettings.get(PENSION_SCHEME) != null) {
                pensionSchemes = payrollSettings.get(PENSION_SCHEME);
            }
            if (payrollSettings.get(STUDENT_LOAN) != null) {
                selectByName(payrollSettings.get(STUDENT_LOAN), box_student_loan);
            }
            if (payrollSettings.get(SALARY) != null) {
                BigDecimal salaryRate = parseToBigDecimal(payrollSettings.get(SALARY));
                box_salary.setText(numberFormat.format(salaryRate));
            } else {
                box_salary.setText(numberFormat.format(BigDecimal.ZERO));
            }
            if (payrollSettings.get(CustomFormConstants.JOB_TITLE) != null && jobTitle != null) {
                jobTitle.setSelected(Integer.valueOf(payrollSettings.get(CustomFormConstants.JOB_TITLE)));
            }
            if (payrollSettings.get(OVERTIME_RATE) != null) {
                BigDecimal overTimeRate = parseToBigDecimal(payrollSettings.get(OVERTIME_RATE));
                box_over_time_rate.setText(numberFormat.format(overTimeRate));
            }
            if (payrollSettings.get(RATE_TYPE) != null) {
                if (FIXED_RATE.equals(payrollSettings.get(RATE_TYPE))) {
                    paymentType.setSelected(radio_fixed_rate);
                    applyRateTypeChanges(false, true, false, false, false, false, false);
                } else if (TIMESHEET_ONLY_RATE.equals(payrollSettings.get(RATE_TYPE))) {
                    paymentType.setSelected(radio_by_timesheet_only);
                    applyRateTypeChanges(false, false, false, false, false, false, true);
                } else if (FIXED_OVERTIME_RATE.equals(payrollSettings.get(RATE_TYPE))) {
                    paymentType.setSelected(radio_fixed_overtime_rate);
                    applyRateTypeChanges(false, true, false, false, false, false, false);
                } else if (FIXED_HRMS_OVERTIME_RATE.equals(payrollSettings.get(RATE_TYPE))) {
                    paymentType.setSelected(radio_fixed_via_hourly_rate);
                    applyRateTypeChanges(false, true, false, false, false, false, false);
                } else if (FIXED_TIMESHEET_OVERTIME_RATE.equals(payrollSettings.get(RATE_TYPE))) {
                    paymentType.setSelected(radio_based_on_monthly_timesheet);
                    applyRateTypeChanges(false, false, true, false, false, false, false);
                } else if (FIXED_ATTENDANCE_REPORT_OVERTIME_RATE.equals(payrollSettings.get(RATE_TYPE))) {
                    paymentType.setSelected(radio_based_on_attendance_report);
                    applyRateTypeChanges(false, false, true, false, false, false, false);
                }
            }
            if (payrollSettings.get(NORMAL_RATE) != null) {
                BigDecimal normalRate = parseToBigDecimal(payrollSettings.get(NORMAL_RATE));
                box_normal_rate.setText(numberFormat.format(normalRate));
            }
            if (payrollSettings.get(PAY_FREQUENCY) != null) {
                selectByName(payrollSettings.get(PAY_FREQUENCY), box_pay_period);
            }
            if (payrollSettings.get(JOB_TYPE) != null) {
                if ("A".equals(payrollSettings.get(JOB_TYPE))) {
                    job_details.setSelected(type_a);
                } else if ("B".equals(payrollSettings.get(JOB_TYPE))) {
                    job_details.setSelected(type_b);
                } else if ("C".equals(payrollSettings.get(JOB_TYPE))) {
                    job_details.setSelected(type_c);
                }
            }
            if (payrollSettings.get(JOB_TYPE_D) != null) {
                if ("true".equals(payrollSettings.get(JOB_TYPE_D))) {
                    check_d.setValue(true);
                }
            }
            if (payrollSettings.get(EMPLOYEE_BONUS_TYPE) != null) {
                bonusWidget.setData(payrollSettings.get(EMPLOYEE_BONUS_TYPE), payrollSettings.get(EMPLOYEE_BONUS_VALUE));
            }
            if (payrollSettings.get(EMPLOYEE_COMMISSION) != null) {
                BigDecimal commission = parseToBigDecimal(payrollSettings.get(EMPLOYEE_COMMISSION));
                box_commission.setText(numberFormat.format(commission));
            }
        }
        //employee payroll settings -> pension schemes set
        getPensionSchemasList(pensionSchemes);
    }

    protected void visibleJobDetailsFields(boolean visible) {
        if (job_details_item != null) {
            job_details_item.setVisible(visible);
        }
    }

    protected void visibleNormalRateFields(boolean visible) {
        if (normal_rate_item != null) {
            normal_rate_item.setVisible(visible);
        }
    }

    protected void visibleP45DetailsFields(boolean visible) {

    }

    protected void visibleOverTimeRateFields(boolean visible) {
        if (over_time_rate_item != null) {
            over_time_rate_item.setVisible(visible);
        }
    }

    protected void visibleSalaryBoxFields(boolean visible) {
        if (box_salary != null) {
            box_salary.setVisible(visible);
        }
    }

    private void calculateNICategory() {
        if (box_forms.isSomethingSelected()) {
            boolean isMarried = false;
            boolean isFemale = false;
            if (box_family_status.getSelectedItem() != null && MARRIED.equals(box_family_status.getSelectedItem().getDescription())) {
                isMarried = true;
            }
            if (radio_gender_female.equals(gender_table.getSelectedItem())) {
                isFemale = true;
            }
            WfmMessageBox confirmationDialog;
            if (P46.equals(box_forms.getSelectedItem().getDescription())) {
                if (box_ni_category.getSelectedItem() != null && box_ni_category.getSelectedItem().getId() != null) {
                    if (LETTER_A.equals(box_ni_category.getSelectedItem().getDescription())) {//select -> 'A'
                        box_tax_code.setText("647L");
                        check_wk1_mth1.setValue(false);
                    } else if (LETTER_B.equals(box_ni_category.getSelectedItem().getDescription())) {//select -> 'B'
                        box_tax_code.setText("647L");
                        check_wk1_mth1.setValue(true);
                        if (!isFemale && !isMarried || isFemale && !isMarried || !isFemale) {
                            confirmationDialog = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, payrollStrings.thisNICategoryApplies());
                            confirmationDialog.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onCancel() {
                                    box_ni_category.setSelected(box_ni_category.getPreviousSelectedItem());
                                }

                                @Override
                                public void onSubmit() {
                                    gender_table.setSelected(radio_gender_female);
                                    box_family_status.setSelected(1);
                                }
                            });
                            confirmationDialog.center();
                        }
                    } else if (LETTER_E.equals(box_ni_category.getSelectedItem().getDescription())) {//select -> 'E'
                        if (!isFemale && !isMarried || isFemale && !isMarried || !isFemale) {
                            confirmationDialog = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, payrollStrings.thisNICategoryApplies());
                            confirmationDialog.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onCancel() {
                                    box_ni_category.setSelected(box_ni_category.getPreviousSelectedItem());
                                }

                                @Override
                                public void onSubmit() {
                                    gender_table.setSelected(radio_gender_female);
                                    box_family_status.setSelected(1);
                                }
                            });
                            confirmationDialog.center();
                        }
                    } else if (LETTER_G.equals(box_ni_category.getSelectedItem().getDescription())) {//select -> 'G'
                        if (!isFemale && !isMarried || isFemale && !isMarried || !isFemale) {
                            confirmationDialog = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, payrollStrings.thisNICategoryApplies());
                            confirmationDialog.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onCancel() {
                                    box_ni_category.setSelected(box_ni_category.getPreviousSelectedItem());
                                }

                                @Override
                                public void onSubmit() {
                                    gender_table.setSelected(radio_gender_female);
                                    box_family_status.setSelected(1);
                                }
                            });
                            confirmationDialog.center();
                        }
                    } else if (LETTER_C.equals(box_ni_category.getSelectedItem().getDescription())) {//select -> 'C'
                        box_tax_code.setText("BR");
                        check_wk1_mth1.setValue(true);
                    } else {
                        box_tax_code.setText("647L");
                        check_wk1_mth1.setValue(false);
                    }
                    if (LETTER_D.equals(box_ni_category.getSelectedItem().getDescription())) {//select -> 'D'
                        box_student_loan.setSelected(0);
                    } else {
                        box_student_loan.setSelected(1);
                    }
                }
            } else {
                box_tax_code.setText("647L");
                check_wk1_mth1.setValue(false);
                box_student_loan.setSelected(1);
                if (box_ni_category.getSelectedItem() != null && (LETTER_B.equals(box_ni_category.getSelectedItem().getDescription()) ||
                        LETTER_E.equals(box_ni_category.getSelectedItem().getDescription()) ||
                        LETTER_G.equals(box_ni_category.getSelectedItem().getDescription()))) {
                    if (!isFemale && !isMarried || isFemale && !isMarried || !isFemale) {
                        confirmationDialog = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, payrollStrings.thisNICategoryApplies());
                        confirmationDialog.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onCancel() {
                                box_ni_category.setSelected(box_ni_category.getPreviousSelectedItem());
                            }

                            @Override
                            public void onSubmit() {
                                gender_table.setSelected(radio_gender_female);
                                box_family_status.setSelected(1);
                            }
                        });
                        confirmationDialog.center();
                    }
                }
            }
        }
    }

    private void calculateNILetter() {
        if (birth_date.getConvertableDOBDate() == null) {
            markAsError(birth_date, birth_date.box_validate());
            return;
        }

        DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");
        int currentYear = Integer.parseInt(yearFormat.format(new Date()));
        int yearOfBirth = Integer.parseInt(yearFormat.format(birth_date.getConvertableDOBDate().getNonConvertedDate()));
        int age = currentYear - yearOfBirth;
        int errors = 0;
        if (age <= pensionage) {

            if (pension_scheme.getItems() != null && pension_scheme.getCheckedItemCount() > 0) {
                errors += markAsError(box_have_another_job, !Validation.validateListBoxRequired(box_have_another_job, new HTML(), wfmStrings.pleaseSelectYesNo()));

                if (box_have_another_job.getSelectedItem() != null && box_have_another_job.getSelectedItem().getId() != null) {
                    errors += markAsError(box_have_another_job, !Validation.validateListBoxRequired(box_family_status, new HTML(), wfmStrings.pleaseSelectFamilyStatus()));
                }
            }
        }
        if (errors > 0) {
            return;
        }

        HashMap<Integer, Boolean> params = new HashMap<>();

        Boolean f = Boolean.FALSE;
        Boolean t = Boolean.TRUE;

        params.put(PENSION_AGE, age > pensionage ? t : f);
        params.put(COOPS, f);
        params.put(COSR, f);
        params.put(COMP, f);
        if (box_have_another_job.getSelectedItem() != null && YES.equals(box_have_another_job.getSelectedItem().getDescription())) {
            params.put(ANOTHER_JOB, t);
            params.put(ANOTHER_JOB2, t);
            params.put(ANOTHER_JOB3, t);
        } else {
            params.put(ANOTHER_JOB, f);
            params.put(ANOTHER_JOB2, f);
            params.put(ANOTHER_JOB3, f);
        }
        if (box_family_status.getSelectedItem() != null && MARRIED.equals(box_family_status.getSelectedItem().getDescription())) {
            params.put(MARRIED_WIDOW, t);
            params.put(MARRIED_WIDOW2, t);
            params.put(MARRIED_WIDOW3, t);
        } else {
            params.put(MARRIED_WIDOW, f);
            params.put(MARRIED_WIDOW2, f);
            params.put(MARRIED_WIDOW3, f);
        }

        PayrollService.App.get().calculateNITableLetter(params, new AbstractAsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.show(caught.toString(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(String result) {
                selectByName(result, box_ni_category);
            }
        });
    }

//    private void drawEmployeesPopup() {
//        //select employees popup
//        selectEmployeesPopup = new KpiModal();
//        selectEmployeesPopup.setTitle(wfmStrings.selectEmployee());
//        selectEmployeesPopup.setWidth("300px");
//        existingEmployeeLookUp = new PayrollEmployeeLookUp(false);
//        existingEmployeeLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
//            if (suggestionSelectionEvent.getSelectedItem() != null) {
//                int_employeeID = existingEmployeeLookUp.getSelectedItemID();
//            }
//        });
//        //
//        Button okButton = new Button(wfmStrings.ok(), (ClickHandler) event -> {
//            //
//            selectEmployeesPopup.close();
//            if (existingEmployeeLookUp.getSelectedItem() != null) {
//                registerFields();
//                initForm();
//            }
//            existingEmployeeLookUp.clear();
//        });
//
//        selectEmployeesPopup.add(existingEmployeeLookUp);
//        selectEmployeesPopup.addButton(okButton);
//    }

    private void getPensionSchemasList(final String selectedPensionSchemes) {
        PayrollService.App.get().getPensionSchemes(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(SelectItem[] result) {
                String[] ids = null;
                if (selectedPensionSchemes != null && !"".equals(selectedPensionSchemes)) {
                    ids = selectedPensionSchemes.split(",");
                }
                if (pension_scheme != null) {
                    pension_scheme.removeItems();

                    for (SelectItem aResult : result) {
                        pension_scheme.add(aResult);

                        try {
                            if (ids != null) {
                                for (String id : ids) {
                                    for (int k = 0; k < pension_scheme.getItemCount(); k++) {
                                        if (id.equals(pension_scheme.getItem(k).getItem().getId().toString())) {
                                            pension_scheme.getItem(k).setCheck(true);
                                        }
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
    }

    protected void onShellOk(final boolean closeTabT, boolean gotoCalculator, Integer result) {
        Info.show(int_employeeID == null ? Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.newEmployee()) : Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.employee()));
        int_employeeID = result;
        if (closeTabT) {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged("starter|summary/" + int_employeeID + "/fromEmployeeList/view", payrollSettings.getNumberData() != null ? payrollSettings.getNumberData().getNumberString() : payrollSettings.getEmployeeName(), payrollSettings.getEmployeeName());
        } else {
            int_employeeID = null;
            reInit();
        }
    }

    private void reInit() {
        initForm();
    }

    private void registerRateTypeChangeListener() {
        if (paymentType == null) return;
        paymentType.addValueChangeHandler(c -> {
            if (radio_fixed_rate.equals(paymentType.getSelectedItem())) {
                applyRateTypeChanges(false, true, false, false, false, false, false);
            } else if (radio_by_timesheet_only.equals(paymentType.getSelectedItem())) {
                applyRateTypeChanges(false, false, false, false, false, false, true);
            } else if (radio_fixed_overtime_rate.equals(paymentType.getSelectedItem())) {
                applyRateTypeChanges(false, true, false, false, false, false, false);
            } else if (radio_fixed_via_hourly_rate.equals(paymentType.getSelectedItem())) {
                applyRateTypeChanges(false, true, false, false, false, false, false);
            } else if (radio_based_on_monthly_timesheet.equals(paymentType.getSelectedItem()) || radio_based_on_attendance_report.equals(paymentType.getSelectedItem())) {
                applyRateTypeChanges(false, false, true, false, false, false, false);
            }
        });
    }

    private void applyRateTypeChanges(boolean hourlyRateB, boolean fixedRateB, boolean fixedViaHlyB, boolean dailyRateB, boolean hrmsRate, boolean timesheetRate, boolean timesheetOnlyRate) {
        //register rate radio buttons listener logic
        if (hourlyRateB) {//hourly rate listener logic
            visibleSalaryBoxFields(false);
            visibleNormalRateFields(true);
            visibleOverTimeRateFields(true);
        } else if (fixedRateB) {//fixed rate listener logic
            visibleSalaryBoxFields(true);
            visibleNormalRateFields(false);
            visibleOverTimeRateFields(false);
        } else if (timesheetOnlyRate) {//timesheet hours only rate listener logic
            visibleSalaryBoxFields(false);
            visibleNormalRateFields(false);
            visibleOverTimeRateFields(false);
        } else if (fixedViaHlyB) {//fixed via hourly rate listener logic
            visibleSalaryBoxFields(true);
            visibleNormalRateFields(true);
            visibleOverTimeRateFields(true);
        } else if (dailyRateB) {//daily rate listener logic
            visibleSalaryBoxFields(false);
            visibleNormalRateFields(true);
            visibleOverTimeRateFields(true);
        } else if (hrmsRate) {//hrms in hours
            visibleSalaryBoxFields(false);
            visibleNormalRateFields(false);
            visibleOverTimeRateFields(false);
        } else if (timesheetRate) {//timesheet in hours
            visibleSalaryBoxFields(false);
            visibleNormalRateFields(false);
            visibleOverTimeRateFields(false);

        }
    }

    private void save(final boolean closeTabT, final boolean gotoCalculator) {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }

        final HashMap<String, String> payrollSettingsMap = setValues();

        if (Utils.hasPermission(PAYROLL_EMPLOYEE_APPROVAL) || Utils.adminOrDirector()) {
            LoadingPanel.loading(true);
            PayrollService.App.get().addNewStarter(newEmployee, payrollSettingsMap, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    enableButton(true);
                    Info.show(caught.getMessage(), Info.Type.WARNING);
                }

                @Override
                public void success(Integer result) {
                    LoadingPanel.loading(false);
                    enableButton(true);
                    onShellOk(closeTabT, gotoCalculator, result);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADD, result, PayrollEmployeeAddForm.this);
                }
            });
        } else {
            PayrollService.App.get().saveEmployeePayrollSettingsTemplate(newEmployee, payrollSettingsMap, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    enableButton(true);
                    Info.show(throwable.getMessage(), Info.Type.WARNING);
                }

                @Override
                public void success(Integer result) {
                    LoadingPanel.loading(false);
                    enableButton(true);
                    onShellOk(closeTabT, gotoCalculator, result);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYROLL_EMPLOYEE_TEMPLATE_SAVED, result, PayrollEmployeeAddForm.this);
                }
            });
        }
    }

    private void setPaymentPeriods(String week_month_type) {
        box_week_month_number.clear();
        box_week_month_number.setItems(PayrollClientUtils.getWeeksOrMonthForWeekMonthNumber(week_month_type));
    }

    private HashMap<String, String> setValues() {
        HashMap<String, String> payrollSettingsMap = new HashMap<>();

        newEmployee = new NewEmployee();
        //EMPLOYEE DETAILS
        //employee object ID
        if (int_employeeID == null) {
            newEmployee.setEndDate(previous_end_date.getDate() != null ? new DateNonConvertable(previous_end_date.getDate()) : null);
        } else {
            newEmployee.setObjectID(int_employeeID);
        }
        newEmployee.setEmployeeTemplateID(employeeTemplateID);
        newEmployee.setStartDate(hire_date.getDate() != null ? new DateNonConvertable((hire_date.getDate())) : null);
        //employee first name
        newEmployee.setFname(box_first_name.getText());
        //employee last name
        newEmployee.setLname(box_last_name.getText());
        //employee email
        newEmployee.setEmail(box_email.getText());

        if (empCode != null) {
            newEmployee.setNumberData(empCode.getNumberData(true));
            newEmployee.setEmpCode(empCode.getNumberData(true).getNumberString());
        }

        if (fireDate.getDate() != null) {
            newEmployee.setResignationDate(new DateNonConvertable((fireDate.getDate())));
        }

        //employee birth date
        if (birth_date.getConvertableDOBDate() != null) {
            newEmployee.setBirthDate(birth_date.getConvertableDOBDate());
        }
        //employee gender (male)
        if (radio_gender_male.equals(gender_table.getSelectedItem())) {
            newEmployee.setGender("Male");
            payrollSettingsMap.put(SEX, "Male");
        }
        //employee gender (female)
        if (radio_gender_female.equals(gender_table.getSelectedItem())) {
            newEmployee.setGender("Female");
            payrollSettingsMap.put(SEX, "Female");
        }
        if (approver != null && approver.getSelectedItem() != null) {
            newEmployee.setApprover(approver.getSelectedItem());
        }

        newEmployee.getPayments().addAll(getTableItems(PayrollConstants.CATEGORY_PAYMENT));
        newEmployee.getDeductions().addAll(getTableItems(PayrollConstants.CATEGORY_DEDUCTION));
        newEmployee.getTaxes().addAll(getTableItems(PayrollConstants.CATEGORY_TAX));
        newEmployee.getEmployerContributions().addAll(getTableItems(PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION));
        newEmployee.getLoans().addAll(getLoanTableItems());

        newEmployee.setDeletedCategories(deletedCategories);
        newEmployee.setInactiveCategories(inactiveCategories);
        if (!"".equals(previous_end_date.getText()) && previous_end_date.getDate() != null) {
            newEmployee.setPrevEndDate(new DateNonConvertable(previous_end_date.getDate()));
        }
        if (!"".equals(start_date.getText()) && start_date.getDate() != null) {
            newEmployee.setStartDateForOnlyPayroll(new DateNonConvertable(start_date.getDate()));
        }
        if (currencyListBox.getSelectedItem() != null) {
            newEmployee.setSalaryCurrency((CurrencyItem) currencyListBox.getSelectedItem());
        }
        if (citizenship.getSelectedItem() != null) {
            newEmployee.setCitizenship(citizenship.getSelectedItem());
        }
        if (box_pay_method.getSelectedItem() != null) {
            newEmployee.setPayMethod(box_pay_method.getSelectedItem());
        }

        if (minSalaryMethod.equals(payment_methods.getSelectedItem())) {
            newEmployee.setPaymentMethod(wfmStrings.minSalary());
        } else if (midSalaryMethod.equals(payment_methods.getSelectedItem())) {
            newEmployee.setPaymentMethod(wfmStrings.midSalary());
        } else if (maxSalaryMethod.equals(payment_methods.getSelectedItem())) {
            newEmployee.setPaymentMethod(wfmStrings.maxSalary());
        }
        newEmployee.setSalaryMode(salaryMode.getSelectedItem(true).getDescription());

        if (mainRole.getSelectItemIDs().size() > 0 && (!essUser.getValue() && !noAccess.getValue())) {
            newEmployee.setRoleId(mainRole.getSelectItemIDs().toArray(new Integer[]{}));
        }
        newEmployee.setRoleName(mainRole.getSelectItems().toString());
        newEmployee.setHasAccess(!noAccess.getValue());
        newEmployee.setEssUser(essUser.getValue());
        newEmployee.setStatusId(null);
        if (status.getSelectedItem() != null) {
            newEmployee.setStatusId(status.getSelectedItem().getId());
            newEmployee.setStatus(status.getSelectedItemText());
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //PAYROLL SETTINGS
        //employee payroll forms
        if (Utils.isUAECompany()) {
            if (contractStartDate.getDate() != null) {
                payrollSettingsMap.put(CustomFormConstants.CONTRACT_START_DATE, dateFormat.format(contractStartDate.getDate()));
            } else {
                payrollSettingsMap.put(CustomFormConstants.CONTRACT_START_DATE, EMPTY_VALUE);
            }
        }

        if (Utils.isUAECompany()) {
            if (contractEndDate.getDate() != null) {
                payrollSettingsMap.put(CustomFormConstants.CONTRACT_END_DATE, dateFormat.format(contractEndDate.getDate()));
            } else {
                payrollSettingsMap.put(CustomFormConstants.CONTRACT_END_DATE, EMPTY_VALUE);
            }
        }

        if (box_forms.getSelectedItem() != null) {
            payrollSettingsMap.put(FORMS, box_forms.getSelectedItem().getName());
        }
        //employee payroll office number
        if (box_office_number.getText() != null) {
            payrollSettingsMap.put(OFFICE_NUMBER, box_office_number.getText());
        }
        //employee payroll paye. ref number
        if (box_reference_number.getText() != null) {
            payrollSettingsMap.put(PAYE_REF_NUMBER, box_reference_number.getText());
        }
        //employee payroll prev. tax code
        if (box_prev_tax_code.getText() != null) {
            payrollSettingsMap.put(PREV_TAX_CODE, box_prev_tax_code.getText());
        }
        //employee payroll prev. wk1/mth1
        if (check_prev_wk1_mth1.getValue()) {
            payrollSettingsMap.put(PREV_WK1MTH1, "true");
        } else {
            payrollSettingsMap.put(PREV_WK1MTH1, "false");
        }
        //employee payroll week month type
        if (box_week_month_type.getSelectedItem() != null && box_week_month_type.getSelectedItem().getId() != null) {
            payrollSettingsMap.put(WEEK_MONTH_TYPE, box_week_month_type.getSelectedItem().getId().toString());
        }
        //employee payroll week month number
        if (box_week_month_number.getSelectedItem() != null && box_week_month_number.getSelectedItem().getId() != null) {
            payrollSettingsMap.put(WEEK_MONTH_NUMBER, box_week_month_number.getSelectedItem().getId().toString());
        }
        //employee payroll total pay to date
        if (box_total_pay_to_date.getText() != null && box_total_pay_to_date.getText().trim().length() > 0) {
            payrollSettingsMap.put(TOTAL_PAY_TO_DATE, parseToBigDecimal(box_total_pay_to_date.getText()).setScale(2, RoundingMode.HALF_UP).toString());
        }
        //employee payroll total tax to date
        if (box_total_tax_to_date.getText() != null && box_total_tax_to_date.getText().trim().length() > 0) {
            payrollSettingsMap.put(TOTAL_TAX_TO_DATE, parseToBigDecimal(box_total_tax_to_date.getText()).setScale(2, RoundingMode.HALF_UP).toString());
        }
        //employee payroll have another job
        if (box_have_another_job.getSelectedItem() != null) {
            payrollSettingsMap.put(HAVE_ANOTHER_JOB, box_have_another_job.getSelectedItem().getName());
        }
        //employee payroll family status
        if (box_family_status.getSelectedItem() != null) {
            payrollSettingsMap.put(FAMILY_STATUS, box_family_status.getSelectedItem().getName());
        }
        //employee payroll ni category letter
        if (box_ni_category.getSelectedItem() != null) {
            payrollSettingsMap.put(NI_TABLE_LETTER, box_ni_category.getSelectedItem().getName());
        }
        //employee payroll tax code
        if (box_tax_code.getText() != null && box_tax_code.getText().trim().length() > 0) {
            payrollSettingsMap.put(TAX_CODE, box_tax_code.getText());
        }
        //employee payroll wk1/mth1
        if (check_wk1_mth1.getValue()) {
            payrollSettingsMap.put(WK1MNTH1, "true");
        } else {
            payrollSettingsMap.put(WK1MNTH1, "false");
        }
        //employee payroll not member of coops
        payrollSettingsMap.put(NOT_MEMBER_OF_COOPS, check_not_member_of_COOPS.getValue() ? "true" : "false");
        //employee payroll pension scheme
        StringBuilder params = new StringBuilder();
        int k = 0;
        for (int i = 0; i < pension_scheme.getItems().size(); i++) {
            if (pension_scheme.getItem(i).getValue()) {
                params.append(k != 0 ? "," : "").append(pension_scheme.getItem(i).getItem().getId());
                k++;
            }
        }
        payrollSettingsMap.put(PENSION_SCHEME, params.toString());
        //employee payroll student loan
        if (box_student_loan.getSelectedItem() != null) {
            payrollSettingsMap.put(STUDENT_LOAN, box_student_loan.getSelectedItem().getName());
        }

        //PAYMENT SETTINGS
        //employee payment settings salary
        if (radio_fixed_rate.equals(paymentType.getSelectedItem())) {//FIXED RATE is true
            payrollSettingsMap.put(RATE_TYPE, FIXED_RATE);
            if (box_salary.getText() != null && box_salary.getText().trim().length() > 0) {
                payrollSettingsMap.put(SALARY, parseToBigDecimal(box_salary.getText()).toString());
            } else {
                payrollSettingsMap.put(SALARY, BigDecimal.ZERO.toString());
            }
        } else if (radio_by_timesheet_only.equals(paymentType.getSelectedItem())) { //TIMESHEET ONLY PAYMENT
            payrollSettingsMap.put(RATE_TYPE, TIMESHEET_ONLY_RATE);
        } else if (radio_fixed_overtime_rate.equals(paymentType.getSelectedItem())) {
            payrollSettingsMap.put(RATE_TYPE, FIXED_OVERTIME_RATE);
            if (box_salary.getText() != null && box_salary.getText().trim().length() > 0) {
                payrollSettingsMap.put(SALARY, parseToBigDecimal(box_salary.getText()).toString());
            } else {
                payrollSettingsMap.put(SALARY, BigDecimal.ZERO.toString());
            }
        } else if (radio_fixed_via_hourly_rate.equals(paymentType.getSelectedItem())) {
            payrollSettingsMap.put(RATE_TYPE, FIXED_HRMS_OVERTIME_RATE);
            if (box_salary.getText() != null && box_salary.getText().trim().length() > 0) {
                payrollSettingsMap.put(SALARY, parseToBigDecimal(box_salary.getText()).toString());
            } else {
                payrollSettingsMap.put(SALARY, BigDecimal.ZERO.toString());
            }
        } else if (radio_based_on_monthly_timesheet.equals(paymentType.getSelectedItem())) {
            payrollSettingsMap.put(RATE_TYPE, FIXED_TIMESHEET_OVERTIME_RATE);
            if (box_salary.getText() != null && box_salary.getText().trim().length() > 0) {
                payrollSettingsMap.put(SALARY, parseToBigDecimal(box_salary.getText()).toString());
            } else {
                payrollSettingsMap.put(SALARY, BigDecimal.ZERO.toString());
            }
        } else if (radio_based_on_attendance_report.equals(paymentType.getSelectedItem())) {
            payrollSettingsMap.put(RATE_TYPE, FIXED_ATTENDANCE_REPORT_OVERTIME_RATE);
            if (box_salary.getText() != null && box_salary.getText().trim().length() > 0) {
                payrollSettingsMap.put(SALARY, parseToBigDecimal(box_salary.getText()).toString());
            } else {
                payrollSettingsMap.put(SALARY, BigDecimal.ZERO.toString());
            }
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.ATS_PAYROLL_CUSTOMIZATION) && jobTitle.getSelectedItem() != null) {
            payrollSettingsMap.put(CustomFormConstants.JOB_TITLE, String.valueOf(jobTitle.getSelectedItem().getId()));
            payrollSettingsMap.put(JOB_TITLE_TEXT, String.valueOf(jobTitle.getSelectedItem().getName()));
        }
        //employee payslip bonus settings
        CategoryRate bonusRate = bonusWidget.getData();
        if (bonusRate != null) {
            if (bonusRate.getPercentage() != null) {
                payrollSettingsMap.put(EMPLOYEE_BONUS_TYPE, PERCENTAGE);
                payrollSettingsMap.put(EMPLOYEE_BONUS_VALUE, bonusRate.getPercentage().toString());
            } else if (bonusRate.getFixedAmount() != null) {
                payrollSettingsMap.put(EMPLOYEE_BONUS_TYPE, FIXED);
                payrollSettingsMap.put(EMPLOYEE_BONUS_VALUE, bonusRate.getFixedAmount().toString());
            }
        } else {
            payrollSettingsMap.put(EMPLOYEE_BONUS_TYPE, "");
            payrollSettingsMap.put(EMPLOYEE_BONUS_VALUE, "");
        }
        payrollSettingsMap.put(SALARY_CATEGORY, String.valueOf(basicSalaryCategory.getSelectedData().getId()));
        //employee payment settings salary gross net
        payrollSettingsMap.put(SALARY_GROSS_NET, PayrollConstants.SALARY_AMOUNT_LIST[0].getName());
        //employee payment settings pay frequency
        if (box_pay_period.getSelectedItem() != null) {
            payrollSettingsMap.put(PAY_FREQUENCY, box_pay_period.getSelectedItem().getName());
        }
        //employee payment settings pay method
        if (box_pay_method.getSelectedItem() != null) {
            payrollSettingsMap.put(PAY_METHOD, box_pay_method.getSelectedItem().getName());
        }
        //employee payment settings payment method
        if (minSalaryMethod.equals(payment_methods.getSelectedItem())) {
            payrollSettingsMap.put(PAYMENT_METHOD, wfmStrings.minSalary());
        } else if (midSalaryMethod.equals(payment_methods.getSelectedItem())) {
            payrollSettingsMap.put(PAYMENT_METHOD, wfmStrings.midSalary());
        } else if (maxSalaryMethod.equals(payment_methods.getSelectedItem())) {
            payrollSettingsMap.put(PAYMENT_METHOD, wfmStrings.maxSalary());
        }
        //Employee commission
        if (box_commission.getText() != null && box_commission.getText().trim().length() > 0) {
            payrollSettingsMap.put(EMPLOYEE_COMMISSION, parseToBigDecimal(box_commission.getText()).toString());
        } else {
            payrollSettingsMap.put(EMPLOYEE_COMMISSION, BigDecimal.ZERO.toString());
        }

        //employee payment settings p46 -> job type
        if (box_forms.isSomethingSelected()) {
            if (P46.equals(box_forms.getSelectedItem().getDescription())) {
                if (type_a.equals(job_details.getSelectedItem())) {
                    payrollSettingsMap.put(JOB_TYPE, "A");
                }
                if (type_b.equals(job_details.getSelectedItem())) {
                    payrollSettingsMap.put(JOB_TYPE, "B");
                }
                if (type_c.equals(job_details.getSelectedItem())) {
                    payrollSettingsMap.put(JOB_TYPE, "C");
                }
                if (check_d.getValue()) {
                    payrollSettingsMap.put(JOB_TYPE_D, "true");
                } else {
                    payrollSettingsMap.put(JOB_TYPE_D, "false");
                }
            }
        }
        if (wageRate != null && wageRate.getValue() != null) {
            payrollSettingsMap.put(WAGE_RATE, wageRate.getValue());
        }
        // employee overtime settings
        CategoryLookUp regularOvertimeCategory = (CategoryLookUp) overtimeSettingsTable.getColumnById(0, "paymentCategory");
        PayslipItemAmountWidget regularOvertimeAmount = (PayslipItemAmountWidget) overtimeSettingsTable.getColumnById(0, "amount");
        DataListBox regularOvertimeRateType = (DataListBox) overtimeSettingsTable.getColumnById(0, "type");
        if (regularOvertimeAmount.getAmount() != null && regularOvertimeAmount.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            payrollSettingsMap.put(REGULAR_OVERTIME_RATE, PayrollClientUtils.format(regularOvertimeAmount.getAmount()));
            payrollSettingsMap.put(REGULAR_OVERTIME_RATE_TYPE, regularOvertimeRateType.getSelectedId(true) == 0 ? FIXED : PERCENTAGE);
            if (regularOvertimeCategory.getSelectedItemID() != null) {
                payrollSettingsMap.put(REGULAR_OVERTIME_CATEGORY_ID, String.valueOf(regularOvertimeCategory.getSelectedItemID()));
                payrollSettingsMap.put(REGULAR_OVERTIME_CATEGORY_NAME, regularOvertimeCategory.getSelectedItem().getName());
            }
        }

        CategoryLookUp weekendOvertimeCategory = (CategoryLookUp) overtimeSettingsTable.getColumnById(1, "paymentCategory");
        PayslipItemAmountWidget weekendOvertimeAmount = (PayslipItemAmountWidget) overtimeSettingsTable.getColumnById(1, "amount");
        DataListBox weekendOvertimeRateType = (DataListBox) overtimeSettingsTable.getColumnById(1, "type");
        if (weekendOvertimeAmount.getAmount() != null && weekendOvertimeAmount.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            payrollSettingsMap.put(WEEKEND_OVERTIME_RATE, PayrollClientUtils.format(weekendOvertimeAmount.getAmount()));
            payrollSettingsMap.put(WEEKEND_OVERTIME_RATE_TYPE, weekendOvertimeRateType.getSelectedId(true) == 0 ? FIXED : PERCENTAGE);
            if (weekendOvertimeCategory.getSelectedItemID() != null) {
                payrollSettingsMap.put(WEEKEND_OVERTIME_CATEGORY_ID, String.valueOf(weekendOvertimeCategory.getSelectedItemID()));
                payrollSettingsMap.put(WEEKEND_OVERTIME_CATEGORY_NAME, weekendOvertimeCategory.getSelectedItem().getName());
            }
        }

        CategoryLookUp holidayOvertimeCategory = (CategoryLookUp) overtimeSettingsTable.getColumnById(2, "paymentCategory");
        PayslipItemAmountWidget holidayOvertimeAmount = (PayslipItemAmountWidget) overtimeSettingsTable.getColumnById(2, "amount");
        DataListBox holidayOvertimeRateType = (DataListBox) overtimeSettingsTable.getColumnById(2, "type");
        if (holidayOvertimeAmount.getAmount() != null && holidayOvertimeAmount.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            payrollSettingsMap.put(HOLIDAY_OVERTIME_RATE, PayrollClientUtils.format(holidayOvertimeAmount.getAmount()));
            payrollSettingsMap.put(HOLIDAY_OVERTIME_RATE_TYPE, holidayOvertimeRateType.getSelectedId(true) == 0 ? FIXED : PERCENTAGE);
            if (holidayOvertimeCategory.getSelectedItemID() != null) {
                payrollSettingsMap.put(HOLIDAY_OVERTIME_CATEGORY_ID, String.valueOf(holidayOvertimeCategory.getSelectedItemID()));
                payrollSettingsMap.put(HOLIDAY_OVERTIME_CATEGORY_NAME, holidayOvertimeCategory.getSelectedItem().getName());
            }
        }

        if (wpsNo != null) {
            payrollSettingsMap.put(CustomFormConstants.WPS_NUMBER, wpsNo.getValue());
        }
        return payrollSettingsMap;
    }

    private List<PaymentDeductionObject> getTableItems(final String from) {
        EditableTable table = getTable(from);
        List<PaymentDeductionObject> list = new ArrayList<>();
        if (table != null) {
            for (int i = 0; i < table.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) table.getColumnById(i, "category");
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) table.getColumnById(i, "amount");
                Widget typeWidget = table.getColumnById(i, "type");

                DataListBox paymentType = (DataListBox) table.getColumnById(i, "paymentType");

                if (categoryLookUp.getSelectedData() != null && amountWidget.getAmount() != null) {
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

                    object.setId(amountWidget.getItemID());
                    object.setPsdId(amountWidget.getPsdId());
                    if (paymentType != null && paymentType.getSelectedId() != null) {
                        object.setPaymentType(EPPaymentType.findById(paymentType.getSelectedId()));
                    }
                    list.add(object);
                }
            }
        }
        return list;
    }

    private List<PaymentDeductionObject> getLoanTableItems() {
        List<PaymentDeductionObject> list = new ArrayList<>();
        if (loansTable != null) {
            for (int i = 0; i < loansTable.getRowCount(); i++) {
                CategoryLookUp categoryLookUp = (CategoryLookUp) loansTable.getColumnById(i, "category");
                PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) loansTable.getColumnById(i, "amount");
                CustomDatePicker startDate = (CustomDatePicker) loansTable.getColumnById(i, "startDate");
                DataListBox type = (DataListBox) loansTable.getColumnById(i, "type");
                if (categoryLookUp.getSelectedData() != null && amountWidget.getAmount() != null && startDate.getDate() != null) {
                    EditableTextBox totalAmount = (EditableTextBox) loansTable.getColumnById(i, "totalAmount");
                    BigDecimal total = PayrollClientUtils.parseToBigDecimal(totalAmount.getText());
                    PaymentDeductionObject object = new PaymentDeductionObject();
                    object.setCategoryItem(categoryLookUp.getSelectedData());
                    object.setType(type.getSelectedId());
                    if (object.getType() == 0) {
                        object.setPaymentAmount(amountWidget.getAmount());
                    } else {
                        object.setPercentage(amountWidget.getAmount());
                        object.setPaymentAmount(total.multiply(object.getPercentage().divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP));
                    }
                    object.setStarttDate(new DateNonConvertable(startDate.getDate()));
                    object.setTotalAmount(total);
                    object.setId(amountWidget.getItemID());
                    list.add(object);
                }
            }
        }
        return list;
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();
        //employee first name validator
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME) != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.FIRST_NAME, box_first_name, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.FIRST_NAME).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.firstName(), box_first_name, formPropertyMap.get(CustomFormConstants.FIRST_NAME).getMinChar()));
        }
        //employee last name validator
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LAST_NAME) != null && formPropertyMap.get(CustomFormConstants.LAST_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.LAST_NAME, box_last_name, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.LAST_NAME).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.LAST_NAME).getTitle() : wfmStrings.lastName(), box_last_name, formPropertyMap.get(CustomFormConstants.LAST_NAME).getMinChar()));
        }
        //employee email validator
        if (!noAccess.getValue()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL) != null && formPropertyMap.get(CustomFormConstants.EMAIL).isRequired()) {
                errors += markAsError(CustomFormConstants.EMAIL, box_email, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.EMAIL).isChanged() ?
                        formPropertyMap.get(CustomFormConstants.EMAIL).getTitle() : wfmStrings.email(), box_email, formPropertyMap.get(CustomFormConstants.EMAIL).getMinChar()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(SALARY_GRADE) != null && formPropertyMap.get(SALARY_GRADE).isRequired()) {
            errors += markAsError(SALARY_GRADE, basicSalaryCategory, !Validation.validateLookUpRequired(basicSalaryCategory));
        }
        if (formPropertyMap != null && formPropertyMap.get(SALARY_GRADE) != null && formPropertyMap.get(SALARY_GRADE).isRequired()) {
            errors += markAsError(SALARY_GRADE, box_salary, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(SALARY_GRADE).isChanged() ?
                    formPropertyMap.get(SALARY_GRADE).getTitle() : wfmStrings.salaryGrade(), box_salary, formPropertyMap.get(SALARY_GRADE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(TAX_CODE) != null && formPropertyMap.get(TAX_CODE).isRequired()) {
            errors += markAsError(TAX_CODE, box_tax_code, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(TAX_CODE).isChanged() ?
                    formPropertyMap.get(TAX_CODE).getTitle() : wfmStrings.taxCode(), box_tax_code, formPropertyMap.get(TAX_CODE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.HIRE_DATE) != null && formPropertyMap.get(CustomFormConstants.HIRE_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.HIRE_DATE, hire_date, !Validation.validateDate(hire_date));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CITIZENSHIP) != null && formPropertyMap.get(CustomFormConstants.CITIZENSHIP).isRequired()) {
            errors += markAsError(CustomFormConstants.CITIZENSHIP, citizenship, !Validation.validateListBoxRequired(citizenship));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE) != null && formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.CONTRACT_START_DATE, contractStartDate, !Validation.validateDate(contractStartDate));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE) != null && formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.CONTRACT_END_DATE, contractEndDate, !Validation.validateDate(contractEndDate));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID).isRequired()) {
            errors += markAsError(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID, paymentType, !Validation.validateListBoxRequired(paymentType));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD).isRequired()) {
            errors += markAsError(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD, box_pay_period, !Validation.validateListBoxRequired(box_pay_period));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD).isRequired()) {
            errors += markAsError(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD, box_pay_method, !Validation.validateListBoxRequired(box_pay_method));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS).isRequired()) {
            errors += markAsError(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS, bonusWidget, !Validation.validateListBoxRequired(bonusWidget.type));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION).isRequired()) {
            errors += markAsError(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION, box_commission, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION).getTitle() : wfmStrings.commission(), box_commission, formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE) != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE).isRequired()) {
            errors += markAsError(CustomFormConstants.JOB_TITLE, jobTitle, !Validation.validateListBoxRequired(jobTitle));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TOTAL_SALARY) != null && formPropertyMap.get(CustomFormConstants.TOTAL_SALARY).isRequired()) {
            errors += markAsError(CustomFormConstants.TOTAL_SALARY, salaryTotalAmount, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.TOTAL_SALARY).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.TOTAL_SALARY).getTitle() : wfmStrings.totalSalary(), salaryTotalAmount, formPropertyMap.get(CustomFormConstants.TOTAL_SALARY).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS, box_family_status, !Validation.validateListBoxRequired(box_family_status));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.GENDER) != null && formPropertyMap.get(CustomFormConstants.GENDER).isRequired()) {
            errors += markAsError(CustomFormConstants.GENDER, gender_table, !Validation.validateListBoxRequired(gender_table));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE) != null && formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE).isRequired()) {
            errors += markAsError(CustomFormConstants.EMPLOYEE_CODE, empCode, !Validation.validateTextBoxRequired(empCode.getTxtNumber()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WPS_NUMBER) != null && formPropertyMap.get(CustomFormConstants.WPS_NUMBER).isRequired()) {
            errors += markAsError(CustomFormConstants.WPS_NUMBER, wpsNo, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.WPS_NUMBER).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.WPS_NUMBER).getTitle() : wfmStrings.wpsNumber(), wpsNo, formPropertyMap.get(CustomFormConstants.WPS_NUMBER).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE) != null && formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.RESIGNATION_DATE, fireDate, !Validation.validateDate(fireDate));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD) != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).isRequired()) {
            errors += markAsError(CustomFormConstants.PAYMENT_METHOD, payment_methods, !Validation.validateListBoxRequired(payment_methods));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS) != null && formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.ACCOUNT_STATUS, status, !Validation.validateListBoxRequired(status));
        }

        if (!(canApprove || Utils.adminOrDirector())) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.APPROVER) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.APPROVER).isRequired()) {
                errors += markAsError(PAYROLL_STARTER.APPROVER, approver, !Validation.validateLookUpRequired(approver));
            }
        }

        if (payrollSettings.isEnabledMultiCurrency()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY) != null && formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY).isRequired()) {
                errors += markAsError(CustomFormConstants.SALARY_CURRENCY, currencyListBox, !Validation.validateListBoxRequired(currencyListBox, new HTML(), payrollStrings.salaryCurrency()));
            }
        }


        try {
            if (!PayrollValidator.isValidFinalTaxCode(box_tax_code.getText())) {//Current Tax Code
            } else {
                box_tax_code.setTitle("");
                box_tax_code.setStyleName("");
            }
        } catch (PayrollValidationException e) {
            errors += markAsError(box_tax_code, true);
            box_tax_code.setTitle(e.getMessage());
            box_tax_code.addStyleName("x-form-invalid");
        }

        if (box_total_tax_to_date.getText().length() > box_total_pay_to_date.getText().length()) {
            errors += markAsError(box_total_pay_to_date, true);
            box_total_pay_to_date.setTitle(payrollStrings.totalTaxDateMustEqualOrLess());
        } else {
            box_total_pay_to_date.setTitle("");
        }

        for (int i = 0; i < paymentsTable.getRowCount(); i++) {
            paymentsTable.resetValidation(i);
            CategoryLookUp categoryLookUp = (CategoryLookUp) paymentsTable.getColumnById(i, "category");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
            if (amountWidget.getAmount() != null && amountWidget.getAmount().compareTo(BigDecimal.ZERO) != 0) {
                if (categoryLookUp.getSelectedData() == null) {
                    paymentsTable.notValid(i, "category");
                    errors++;
                }
            }
        }

        for (int i = 0; i < loansTable.getRowCount(); i++) {
            loansTable.resetValidation(i);
            CategoryLookUp categoryLookUp = (CategoryLookUp) loansTable.getColumnById(i, "category");
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) loansTable.getColumnById(i, "amount");
            if (categoryLookUp.getSelectedData() != null && amountWidget.getAmount() != null) {
                CustomDatePicker startDate = (CustomDatePicker) loansTable.getColumnById(i, "startDate");
                if (startDate.getDate() == null) {
                    loansTable.notValid(i, "startDate");
                    errors++;
                }
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.areYouSureThatEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        if (Utils.isEmployeesLocked() && hire_date.getDate() != null && DateUtils.getTransactionLockDate().after(hire_date.getDate())) {
            Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.employee(), Utils.getTransactionLockDate()), Info.Type.WARNING);
        }

        return true;
    }


    protected BigDecimal parseToBigDecimal(String text) {
        if (text != null && text.length() > 0) {
            return BigDecimal.valueOf(Utils.universalParse(numberFormat, text));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    private class BonusWidget extends HorizontalPanel {
        private final SelectItem PERCENTAGE_TYPE = new SelectItem(1, wfmStrings.percentage());
        private final SelectItem FIXED_TYPE = new SelectItem(2, wfmStrings.fixedRate());
        private final DataListBox type;
        private final TextBox amountTxtBox;

        private BonusWidget() {
            type = new DataListBox();
            type.addListItem(PERCENTAGE_TYPE);
            type.addListItem(FIXED_TYPE);
            amountTxtBox = new TextBox();
            amountTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            Validation.addNumericKeyboardListener(amountTxtBox, 2);

            MaterialPanel typeDiv = new MaterialPanel();
            MaterialPanel amountDiv = new MaterialPanel();

            typeDiv.setPaddingRight(5);
            typeDiv.add(type);

            amountDiv.add(amountTxtBox);

            add(typeDiv);
            add(amountDiv);
        }

        public CategoryRate getData() {
            if (amountTxtBox.getText() != null && !"".equals(amountTxtBox.getText().trim())) {
                if (PERCENTAGE_TYPE.equals(type.getSelectedItem())) {
                    CategoryRate categoryRate = new CategoryRate();
                    categoryRate.setPercentage(PayrollClientUtils.parseToBigDecimal(amountTxtBox.getText()));
                    return categoryRate;
                } else if (FIXED_TYPE.equals(type.getSelectedItem())) {
                    CategoryRate categoryRate = new CategoryRate();
                    categoryRate.setFixedAmount(PayrollClientUtils.parseToBigDecimal(amountTxtBox.getText()));
                    return categoryRate;
                }
            }
            return null;
        }

        public void setData(String bonusType, String bonusValue) {
            try {
                if (PERCENTAGE.equals(bonusType)) {
                    type.setSelected(PERCENTAGE_TYPE);
                    amountTxtBox.setText(PayrollClientUtils.numberFormat(parseToBigDecimal(bonusValue)));
                } else if (FIXED.equals(bonusType)) {
                    type.setSelected(FIXED_TYPE);
                    amountTxtBox.setText(PayrollClientUtils.format(parseToBigDecimal(bonusValue)));
                }
            } catch (Exception e) {

            }
        }
    }

    public class CustomDatePicker extends DatePicker implements CustomCellInterface {
        private DateTimeFormat dateFormatter = DateUtils.getFormat();
        private Date date;

        @Override
        public String getDisplayValue() {
            return getDate() != null ? DateUtils.format(getDate()) : "Please Select";
        }

        @Override
        public void setItemValue(Object value) {
            setDate((Date) value);
        }

        @Override
        public Date getDate() {
            if (getText() != null && !getText().isEmpty() && !"Please Select".equals(getText()) && !dateFormatter.getPattern().equals(getText())) {
                try {
                    if (dateFormatter == null) {
                        dateFormatter = DateUtils.getFormat();
                    }
                    date = dateFormatter.parse(getText());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                return date;
            }
            return null;
        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }
    }

    private void onCalculateBasicTotalSalary() {
        BigDecimal basicSalary = box_salary.getValue() != null && !box_salary.getValue().trim().equals("") ? BigDecimal.valueOf(extendedNumberFormat.parse(box_salary.getValue())) : BigDecimal.ZERO;
        BigDecimal grandTotalPayment = BigDecimal.ZERO;
        BigDecimal grandTotalDeduction = BigDecimal.ZERO;
        BigDecimal grandTotalTax = BigDecimal.ZERO;

        //Calc Payments
        for (int i = 0; i < paymentsTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) paymentsTable.getColumnById(i, "amount");
            DataListBox type = (DataListBox) paymentsTable.getColumnById(i, "type");
            if (type.getSelectedItem() == null || type.getSelectedItem().getId() == 0) {
                grandTotalPayment = grandTotalPayment.add(amountWidget.getAmount());
            } else {
                grandTotalPayment = grandTotalPayment.add((basicSalary.multiply(amountWidget.getAmount()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
            }
        }

        //Calc Deductions
        for (int i = 0; i < deductionsTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) deductionsTable.getColumnById(i, "amount");
            LinkedTypeWidget type = (LinkedTypeWidget) deductionsTable.getColumnById(i, "type");
            if (type.getSelectedId() == null || type.getSelectedId() == 0) {
                grandTotalDeduction = grandTotalDeduction.add(amountWidget.getAmount());
            } else if (type.getSelectedId() == 1) {
                grandTotalDeduction = grandTotalDeduction.add((basicSalary.multiply(amountWidget.getAmount()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
            } else if (type.getSelectedId() == 2) {
                if (type.getLinkedCategories() != null && !type.getLinkedCategories().isEmpty()) {
                    List<PaymentDeductionObject> linkedAllowance = new ArrayList<>();
                    List<Integer> linkedIds = new ArrayList<>();
                    for (PaymentDeductionObject linkedCategory : type.getLinkedCategories()) {
                        linkedIds.add(linkedCategory.getCategoryItem().getId());
                    }
                    for (PaymentDeductionObject payment : payrollSettings.getPaymentCategories()) {
                        if (linkedIds.contains(payment.getCategoryItem().getId())) {
                            linkedAllowance.add(payment);
                        }
                    }
                    BigDecimal linkedTotal = BigDecimal.ZERO;
                    for (PaymentDeductionObject allowance : linkedAllowance) {
                        if (allowance.getType() == 1) {
                            linkedTotal = linkedTotal.add((basicSalary.multiply(allowance.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
                        } else {
                            linkedTotal = linkedTotal.add(allowance.getPaymentAmount());
                        }
                    }
                    grandTotalDeduction = grandTotalDeduction.add(((basicSalary.add(linkedTotal)).multiply(amountWidget.getAmount()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
                }
            } else {//todo add allowances
                BigDecimal amount = basicSalary.multiply(amountWidget.getAmount()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                grandTotalDeduction = grandTotalDeduction.add(amount);
            }
        }

        for (int i = 0; i < taxTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) taxTable.getColumnById(i, "amount");
            LinkedTypeWidget type = (LinkedTypeWidget) taxTable.getColumnById(i, "type");
            if (type.getSelectedId() == null || type.getSelectedId() == 0) {
                grandTotalTax = grandTotalTax.add(amountWidget.getAmount());
            } else if (type.getSelectedId() == 1) {
                grandTotalTax = grandTotalTax.add((basicSalary.multiply(amountWidget.getAmount()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
            } else if (type.getSelectedId() == 2) {
                if (type.getLinkedCategories() != null && !type.getLinkedCategories().isEmpty()) {
                    List<PaymentDeductionObject> linkedAllowance = new ArrayList<>();
                    List<Integer> linkedIds = new ArrayList<>();
                    for (PaymentDeductionObject linkedCategory : type.getLinkedCategories()) {
                        linkedIds.add(linkedCategory.getCategoryItem().getId());
                    }
                    for (PaymentDeductionObject payment : payrollSettings.getPaymentCategories()) {
                        if (linkedIds.contains(payment.getCategoryItem().getId())) {
                            linkedAllowance.add(payment);
                        }
                    }
                    BigDecimal linkedTotal = BigDecimal.ZERO;
                    for (PaymentDeductionObject allowance : linkedAllowance) {
                        if (allowance.getType() == 1) {
                            linkedTotal = linkedTotal.add((basicSalary.multiply(allowance.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
                        } else {
                            linkedTotal = linkedTotal.add(allowance.getPaymentAmount());
                        }
                    }
                    grandTotalTax = grandTotalTax.add(((basicSalary.add(linkedTotal)).multiply(amountWidget.getAmount()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
                }
            } else {//todo add allowances
                BigDecimal amount = basicSalary.multiply(amountWidget.getAmount()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                grandTotalTax = grandTotalTax.add(amount);
            }
        }

        for (int i = 0; i < employerContributionTable.getRowCount(); i++) {
            PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) employerContributionTable.getColumnById(i, "amount");
            DataListBox type = (DataListBox) employerContributionTable.getColumnById(i, "type");
            if (type.getSelectedItem() == null || type.getSelectedItem().getId() == 0) {
                grandTotalPayment = grandTotalPayment.add(amountWidget.getAmount());
            } else {
                grandTotalPayment = grandTotalPayment.add((basicSalary.multiply(amountWidget.getAmount()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
            }
        }

        salaryTotalAmount.setValue(extendedNumberFormat.format(basicSalary.add(grandTotalPayment).subtract(grandTotalDeduction).subtract(grandTotalTax)));
    }

    private void calculateBasicTotalSalary() {
        BigDecimal basicSalary = BigDecimal.ZERO;
        if (box_salary.getValue() != null && !box_salary.getValue().trim().isEmpty()) {
            basicSalary = BigDecimal.valueOf(extendedNumberFormat.parse(box_salary.getValue()));
        }
        BigDecimal grandTotalPayment = BigDecimal.ZERO;
        BigDecimal grandTotalDeduction = BigDecimal.ZERO;

        if (payrollSettings.getPaymentCategories() != null && payrollSettings.getPaymentCategories().size() > 0) {
            for (PaymentDeductionObject paymentDeduction : payrollSettings.getPaymentCategories()) {
                if (paymentDeduction.getPaymentAmount() != null) {
                    if (paymentDeduction.getType() == null || paymentDeduction.getType() == 0) {
                        grandTotalPayment = grandTotalPayment.add(paymentDeduction.getPaymentAmount());
                    } else {
                        grandTotalPayment = grandTotalPayment.add((basicSalary.multiply(paymentDeduction.getPercentage()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
                    }
                }
            }
        }

        if (payrollSettings.getDeductionCategories() != null && payrollSettings.getDeductionCategories().size() > 0) {
            for (PaymentDeductionObject paymentDeduction : payrollSettings.getDeductionCategories()) {
                if (paymentDeduction.getPaymentAmount() != null) {
                    if (paymentDeduction.getType() == null || paymentDeduction.getType() == 0) {
                        grandTotalDeduction = grandTotalDeduction.add(paymentDeduction.getPaymentAmount());
                    } else if (paymentDeduction.getType() == 2) {
                        if (paymentDeduction.getLinkedCategories() != null && !paymentDeduction.getLinkedCategories().isEmpty()) {
                            List<PaymentDeductionObject> linkedAllowance = new ArrayList<>();
                            List<Integer> linkedIds = new ArrayList<>();
                            for (PaymentDeductionObject linkedCategory : paymentDeduction.getLinkedCategories()) {
                                linkedIds.add(linkedCategory.getCategoryItem().getId());
                            }
                            for (PaymentDeductionObject payment : payrollSettings.getPaymentCategories()) {
                                if (linkedIds.contains(payment.getCategoryItem().getId())) {
                                    linkedAllowance.add(payment);
                                }
                            }
                            BigDecimal linkedTotal = BigDecimal.ZERO;
                            for (PaymentDeductionObject allowance : linkedAllowance) {
                                if (allowance.getType() == 1) {
                                    linkedTotal = linkedTotal.add((basicSalary.multiply(allowance.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
                                } else {
                                    linkedTotal = linkedTotal.add(allowance.getPaymentAmount());
                                }
                            }
                            grandTotalDeduction = grandTotalDeduction.add(((basicSalary.add(linkedTotal)).multiply(paymentDeduction.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)));
                        }
                    } else {
                        grandTotalDeduction = grandTotalDeduction.add((basicSalary.multiply(paymentDeduction.getPercentage()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
                    }
                }
            }
        }

        salaryTotalAmount.setValue(extendedNumberFormat.format(basicSalary.add(grandTotalPayment).subtract(grandTotalDeduction)));
    }
}
