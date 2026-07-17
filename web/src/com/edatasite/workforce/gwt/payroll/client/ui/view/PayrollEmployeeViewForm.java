package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.EPPaymentType;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.Frequency;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.PayslipItemAmountWidget;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * User: Ilhombek
 * Date: 11/30/12
 * Time: 2:03 PM
 */
public class PayrollEmployeeViewForm extends PayrollEmployeeAddForm {

    private static final NumberFormat numberFormat = Utils.getCalculationNumberFormat();


    private Integer int_employeeID;

    private Integer employeeTemplateID;

    private String status;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private HTML html_first_name, html_last_name, html_email, html_birth_date, html_hire_date, contractStartDate, contractEndDate, mainrole, account_status, essUser, noAccess;
    private HTML html_employee_is_paid, html_job_title, html_salary, html_currency, html_salary_gross_net, html_normal_rate, html_over_time_rate, html_pay_period, html_payment_method, html_employee_bonus, html_employee_commission;
    private HTML html_start_date, html_employee_has_form, html_prev_end_date, html_office_reference_number, html_prev_tax_code,
            html_week_month_type, html_week_month_number, html_total_pay_to_date, html_total_tax_to_date, html_student_loan,
            html_have_another_job, html_family_status, html_gender, html_ni_NO, html_ni_category, html_current_tax_code,
            html_not_member_of_COOPS, html_pension_scheme, html_emp_code, html_wage_rate, html_fire_date, html_wps_no, html_salary_level, html_citizenship, html_salary_mode;


    private final String test_code_ID_name = "summary_starter_view_";


    public PayrollEmployeeViewForm(Integer int_employeeID) {
        super("summary", wfmStrings.summaryView(), "summary_starter_view_", int_employeeID);
        this.int_employeeID = int_employeeID;
    }

    public PayrollEmployeeViewForm(Integer employeeTemplateID, boolean fromTemplate, String status) {
        super("summary", wfmStrings.summaryView(), "summary_starter_view_", employeeTemplateID, fromTemplate);
        this.employeeTemplateID = employeeTemplateID;
        this.status = status;
    }

    @Override
    public String getIconStyle() {
        return "bgMark icon-employee";
    }

    @Override
    protected void addButtons() {
        boolean isBeforeLockDate = (Utils.isEmployeesLocked() && payrollSettings.getStartDate() != null && DateUtils.getTransactionLockDate().after(payrollSettings.getStartDate().getNonConvertedDate()));
        //edit button
        if (!isBeforeLockDate && (employeeTemplateID != null && !Utils.hasPermission(PAYROLL_EMPLOYEE_APPROVAL) && Utils.hasPermission(PermissionConstants.PAYROLL_EMPLOYEE_EDIT) && !status.equalsIgnoreCase(APPROVED))) {
            addButton(wfmStrings.edit(), BTN_PRIMARY, null, (test_code_ID_name + "edit_button"), event -> {
                closeTab();
                if (employeeTemplateID != null) {
                    SinksContainer s = SinksContainerFactory.entryPoint.onHistoryChanged("starter|edit/" + employeeTemplateID + "/fromTemplate", payrollSettings.getNumberData() != null ? payrollSettings.getNumberData().getNumberString() : payrollSettings.getEmployeeName(), payrollSettings.getEmployeeName());
                } else {
                    SinksContainer s = SinksContainerFactory.entryPoint.onHistoryChanged("starter|edit/" + int_employeeID + "/fromEmployeeList", payrollSettings.getNumberData() != null ? payrollSettings.getNumberData().getNumberString() : payrollSettings.getEmployeeName(), payrollSettings.getEmployeeName());
                }
            });
        } else if (!isBeforeLockDate && employeeTemplateID == null && Utils.hasPermission(PermissionConstants.PAYROLL_EMPLOYEE_EDIT)) {
            addButton(wfmStrings.edit(), BTN_PRIMARY, null, (test_code_ID_name + "edit_button"), event -> {
                closeTab();
                SinksContainer s = SinksContainerFactory.entryPoint.onHistoryChanged("starter|edit/" + int_employeeID + "/fromEmployeeList", payrollSettings.getNumberData() != null ? payrollSettings.getNumberData().getNumberString() : payrollSettings.getEmployeeName(), payrollSettings.getEmployeeName());
            });
        }

        if (!isBeforeLockDate && Utils.hasPermission(PAYROLL_EMPLOYEE_APPROVAL) && employeeTemplateID != null && status.equals("Submitted")) {
            addButton(wfmStrings.approve(), BTN_SUCCESS, null, test_code_ID_name + "approve_button", clickEvent -> updateStatus(APPROVED, null));

            addButton(wfmStrings.reject(), BTN_REJECT, null, test_code_ID_name + "reject_button", clickEvent -> {

                final WfmMessageBox notePopup = new WfmMessageBox(IconEnum.QUESTION, Action.OK);
                notePopup.setWidth("310px");
                notePopup.setTitle(wfmStrings.rejectionReason());
                notePopup.setTitle(wfmStrings.rejectionReason());
                notePopup.setMessage(wfmStrings.rejectionReason());
                final TextArea2 note = new TextArea2(1000);
                note.setStyleName("description-default-color");
                note.setWidth("300px");
                notePopup.setContent(note);
                notePopup.addCloseHandler(popupPanelCloseEvent -> updateStatus(REJECTED, note.getText()));
                notePopup.open();
            });
        }
        //close button
//        addButton(wfmStrings.close(), null, (test_code_ID_name + "close_button"), (ClickHandler) event -> closeTab());
    }

    private void updateStatus(String status, String rejectionNote) {
        PayrollService.App.get().updateEmployeeTemplateStatus(employeeTemplateID, status, rejectionNote, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                super.onFailure(throwable);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADD, result, PayrollEmployeeViewForm.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYROLL_EMPLOYEE_TEMPLATE_SAVED, result, PayrollEmployeeViewForm.this);
                closeTab();
            }
        });
    }

    @Override
    protected void getDataToFillFields() {
        super.getDataToFillFields();
    }

    @Override
    protected String getFormID() {
        return super.getFormID();
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return super.getWikiCode();
    }

    @Override
    protected void fillFormWithData() {
        //for employee details
        //employee first name
        html_first_name.setHTML(payrollSettings.getEmployeeFirstName() != null ? payrollSettings.getEmployeeFirstName() : "");
        //employee last name
        html_last_name.setHTML(payrollSettings.getEmployeeLastName() != null ? payrollSettings.getEmployeeLastName() : "");
        //employee email
        html_email.setHTML(payrollSettings.getEmployeeEmail() != null ? payrollSettings.getEmployeeEmail() : "");
        //employee hire date
        html_hire_date.setHTML(payrollSettings.getStartDate() != null ? DateUtils.format(payrollSettings.getStartDate()) : "");
        //employee birth date
        html_birth_date.setHTML(payrollSettings.getDob() != null ? DateUtils.format(payrollSettings.getDob()) : "");
        html_emp_code.setHTML(payrollSettings.getNumberData() != null ? payrollSettings.getNumberData().getNumberString() : "");
        html_salary_mode.setHTML(payrollSettings.getSalaryMode() != null && !payrollSettings.getSalaryMode().equals(TARIFF_GRID) ? payrollSettings.getSalaryMode().equals(FIXED_AMOUNT) ? wfmStrings.byFixedAmount() : wfmStrings.basedOnAttendanceReport() : wfmStrings.byTariffGrid());
        html_fire_date.setHTML(payrollSettings.getResignationDate() != null ? DateUtils.format(payrollSettings.getResignationDate()) : "");
//        if (payrollSettings.isEnabledMultiCurrency()) {
//            addField(CustomFormConstants.SALARY_CURRENCY, html_currency, payrollStrings.salaryCurrency());
//            html_currency.setHTML(payrollSettings.getSalaryCurrency() != null ? payrollSettings.getSalaryCurrency().getName() : "");
//        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY) != null) {
            addField(CustomFormConstants.SALARY_CURRENCY, html_currency, getTitle(formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY).isChanged() ? formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY).getTitle() : payrollStrings.salaryCurrency(), formPropertyMap.get(CustomFormConstants.SALARY_CURRENCY).isRequired()));
            html_currency.setHTML(payrollSettings.getSalaryCurrency() != null ? payrollSettings.getSalaryCurrency().getName() : "");
        } else {
            addField(CustomFormConstants.SALARY_CURRENCY, html_currency, payrollStrings.salaryCurrency());
            html_currency.setHTML(payrollSettings.getSalaryCurrency() != null ? payrollSettings.getSalaryCurrency().getName() : "");
        }
        if (payrollSettings.getCitizenship() != null) {
            html_citizenship.setHTML(payrollSettings.getCitizenship().getName());
        }
        setPayrollSettings(payrollSettings.getPayrollSettings());

        if (payrollSettings.getPaymentCategories() != null && payrollSettings.getPaymentCategories().size() > 0) {
            for (PaymentDeductionObject object : payrollSettings.getPaymentCategories()) {
                addItem(object, PayrollConstants.CATEGORY_PAYMENT);
            }
        } else {
            for (int i = 0; i < DEFAULT_ROWS; i++) {
                addItem(null, PayrollConstants.CATEGORY_PAYMENT);
            }
        }

        if (payrollSettings.getDeductionCategories() != null && payrollSettings.getDeductionCategories().size() > 0) {
            for (PaymentDeductionObject object : payrollSettings.getDeductionCategories()) {
                addItem(object, PayrollConstants.CATEGORY_DEDUCTION);
            }
        } else {
            for (int i = 0; i < DEFAULT_ROWS; i++) {
                addItem(null, PayrollConstants.CATEGORY_DEDUCTION);
            }
        }

        if (payrollSettings.getTaxCategories() != null && payrollSettings.getTaxCategories().size() > 0) {
            for (PaymentDeductionObject object : payrollSettings.getTaxCategories()) {
                addItem(object, PayrollConstants.CATEGORY_TAX);
            }
        } else {
            for (int i = 0; i < DEFAULT_ROWS; i++) {
                addItem(null, PayrollConstants.CATEGORY_TAX);
            }
        }

        if (payrollSettings.getLoanCategories() != null && payrollSettings.getLoanCategories().size() > 0) {
            for (PaymentDeductionObject object : payrollSettings.getLoanCategories()) {
                addItem(object, PayrollConstants.CATEGORY_LOAN);
            }
        } else {
            for (int i = 0; i < DEFAULT_ROWS; i++) {
                addItem(null, PayrollConstants.CATEGORY_LOAN);
            }
        }

        if (payrollSettings.getEmployerContributions() != null && payrollSettings.getEmployerContributions().size() > 0) {
            for (PaymentDeductionObject object : payrollSettings.getEmployerContributions()) {
                addItem(object, PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);
            }
        } else {
            for (int i = 0; i < DEFAULT_ROWS; i++) {
                addItem(null, PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION);
            }
        }

        if (!payrollSettings.getEss() && (Utils.hasPermission(PermissionConstants.HRMS_SHOW_EMPLOYEE_ROLE_WIDGET)
                || Utils.hasPermission(PermissionConstants.PM_SHOW_EMPLOYEE_ROLE_WIDGET))) {
            addField(CustomFormConstants.ACCOUNT_ROLES, mainrole, getTitle(wfmStrings.roles()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNT_ROLES) != null) {
            addField(CustomFormConstants.ACCOUNT_ROLES, mainrole, getTitle(formPropertyMap.get(CustomFormConstants.ACCOUNT_ROLES).isChanged() ? formPropertyMap.get(CustomFormConstants.ACCOUNT_ROLES).getTitle() : wfmStrings.roles(), formPropertyMap.get(CustomFormConstants.ACCOUNT_ROLES).isRequired()));
        } else {
            addField(CustomFormConstants.ACCOUNT_ROLES, mainrole, getTitle(wfmStrings.roles()));
        }
        mainrole.setHTML(roles(payrollSettings.getRoleList(), payrollSettings.getRoleId()));
        account_status.setHTML(payrollSettings.getStatus());
        essUser.setHTML(payrollSettings.getEss() ? wfmStrings.yes() : wfmStrings.no());
        noAccess.setHTML(EMPLOYEE_STATUS_NO_ACCCESS.equals(payrollSettings.getStatusCode()) ? wfmStrings.yes() : wfmStrings.no());


    }

    private String roles(SelectItem[] items, Integer[] ids) {
        String role = "";

        if (items != null && items.length > 0) {
            if (ids != null) {
                for (Integer idS : ids) {
                    for (SelectItem selectItem : items) {
                        if (selectItem.getId().equals(idS)) {
                            if ("".equals(role)) {
                                role = role + selectItem.getName();
                            } else {
                                role = role + ", " + selectItem.getName();
                            }
                        }
                    }
                }
            }
        }
        return role;
    }


    @Override
    protected void registerFields() {
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //employee details
        //employee details -> first name
        html_first_name = new HTML();
        html_first_name.addStyleName(DEFAULT_WIDTH);
        html_first_name.ensureDebugId(test_code_ID_name + "first_name");
        //employee details -> last name
        html_last_name = new HTML();
        html_last_name.addStyleName(DEFAULT_WIDTH);
        html_last_name.ensureDebugId(test_code_ID_name + "last_name");
        //employee details -> email
        html_email = new HTML();
        html_email.addStyleName(DEFAULT_WIDTH);
        html_email.ensureDebugId(test_code_ID_name + "email");
        //employee details -> hire date
        html_hire_date = new HTML();
        html_hire_date.addStyleName(DEFAULT_WIDTH);
        html_hire_date.ensureDebugId(test_code_ID_name + "hire_date");
        //employee details -> date of birth
        html_birth_date = new HTML();
        html_birth_date.addStyleName(DEFAULT_WIDTH);
        html_birth_date.ensureDebugId(test_code_ID_name + "date_of_birth");
        //employee details -> contract start date
        contractStartDate = new HTML();
        contractStartDate.addStyleName(DEFAULT_WIDTH);
        contractStartDate.ensureDebugId(test_code_ID_name + "contract_start_date");
        //employee details -> contract end date
        contractEndDate = new HTML();
        contractEndDate.addStyleName(DEFAULT_WIDTH);
        contractEndDate.ensureDebugId(test_code_ID_name + "contract_end_date");

        //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //payment settings
        //employee payment settings -> rates (employee is paid)
        html_employee_is_paid = new HTML();
        html_employee_is_paid.addStyleName(DEFAULT_WIDTH);
        html_employee_is_paid.ensureDebugId(test_code_ID_name + "employee_is_paid");
        //employee payment settings -> salary
        html_job_title = new HTML();
        html_job_title.addStyleName(DEFAULT_WIDTH);
        html_job_title.ensureDebugId(test_code_ID_name + "jobtitle");
        html_salary = new HTML();
        html_salary.addStyleName(DEFAULT_WIDTH);
        html_salary.ensureDebugId(test_code_ID_name + "salary");

        html_currency = new HTML();
        html_currency.addStyleName(DEFAULT_WIDTH);
        html_currency.ensureDebugId(test_code_ID_name + "currency");

        html_citizenship = new HTML();
        html_citizenship.addStyleName(DEFAULT_WIDTH);
        html_citizenship.ensureDebugId(test_code_ID_name + "html_citizenship");

        //employee payment settings -> salary gross/net
        html_salary_gross_net = new HTML();
        html_salary_gross_net.addStyleName(DEFAULT_WIDTH);
        html_salary_gross_net.ensureDebugId(test_code_ID_name + "salary_gross_net");
        //employee payment settings -> normal rate
        html_normal_rate = new HTML();
        html_normal_rate.addStyleName(DEFAULT_WIDTH);
        html_normal_rate.ensureDebugId(test_code_ID_name + "normal_rate");
        //employee payment settings -> over time rate
        html_over_time_rate = new HTML();
        html_over_time_rate.addStyleName(DEFAULT_WIDTH);
        html_over_time_rate.ensureDebugId(test_code_ID_name + "over_time_rate");
        //employee payment settings -> pay period
        html_pay_period = new HTML();
        html_pay_period.addStyleName(DEFAULT_WIDTH);
        html_pay_period.ensureDebugId(test_code_ID_name + "pay_period");
        //employee payment settings -> pay method
        html_payment_method = new HTML();
        html_payment_method.addStyleName(DEFAULT_WIDTH);
        html_payment_method.ensureDebugId(test_code_ID_name + "payment_method");
        //employee payment settings -> Salary Level
        html_salary_level = new HTML();
        html_salary_level.addStyleName(DEFAULT_WIDTH);
        html_salary_level.ensureDebugId(test_code_ID_name + "salary_level");
        //payslip employee bonus settings
        html_employee_bonus = new HTML();
        html_employee_bonus.addStyleName(DEFAULT_WIDTH);
        html_employee_bonus.ensureDebugId(test_code_ID_name + "employee_bonus");
        //additional payment employee commission settings
        html_employee_commission = new HTML();
        html_employee_commission.addStyleName(DEFAULT_WIDTH);
        html_employee_commission.ensureDebugId(test_code_ID_name + "employee_commission");

        salary_table = new InputGroup();
        salaryPaymentCategoryHTML = new HTML("");

        Div spcWrapper = new Div();
        spcWrapper.setClass("input-group-prepend");
        spcWrapper.add(InputGroup.wrapIntoGroupText(salaryPaymentCategoryHTML));
        salary_table.add(spcWrapper);
        Div salaryWrapper = InputGroup.wrapIntoGroupContent(html_salary); //https://prnt.sc/r90v8j
        salaryWrapper.addStyleName("form-control");
        salary_table.add(salaryWrapper);
        html_salary.setVisible(true);

        //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //payroll settings
        //employee payroll settings -> start date
        html_start_date = new HTML();
        html_start_date.addStyleName(DEFAULT_WIDTH);
        html_start_date.ensureDebugId(test_code_ID_name + "start_date");
        //employee payroll settings -> employee has form
        html_employee_has_form = new HTML();
        html_employee_has_form.addStyleName(DEFAULT_WIDTH);
        html_employee_has_form.ensureDebugId(test_code_ID_name + "employee_has_form");
        //employee payroll settings -> previous end date
        html_prev_end_date = new HTML();
        html_prev_end_date.addStyleName(DEFAULT_WIDTH);
        html_prev_end_date.ensureDebugId(test_code_ID_name + "previous_end_date");
        //employee payroll settings -> office/reference number
        html_office_reference_number = new HTML();
        html_office_reference_number.addStyleName(DEFAULT_WIDTH);
        html_office_reference_number.ensureDebugId(test_code_ID_name + "office_reference_number");
        //employee payroll settings -> previous tax code
        html_prev_tax_code = new HTML();
        html_prev_tax_code.setWidth("150px");
        html_prev_tax_code.getElement().getStyle().setMarginLeft(2, Style.Unit.PX);
        html_prev_tax_code.ensureDebugId(test_code_ID_name + "previous_tax_code");
        //employee payment settings -> previous wk1_month1
        check_prev_wk1_mth1 = new KpiCheckBox(" Wk1/Mnth1");
        check_prev_wk1_mth1.setWordWrap(false);
        check_prev_wk1_mth1.setEnabled(false);
        check_prev_wk1_mth1.setVisible(false);
        check_prev_wk1_mth1.setWidth("100px");
        check_prev_wk1_mth1.ensureDebugId(test_code_ID_name + "checkbox_previous_wk1_mth1");
        //employee payment settings -> previous tax code VIA previous wk1_month1 table
        FlexTable prev_tax_code_via_prev_wk1_mth1_table = new FlexTable();
        prev_tax_code_via_prev_wk1_mth1_table.addStyleName(DEFAULT_WIDTH);
        prev_tax_code_via_prev_wk1_mth1_table.setWidget(0, 0, html_prev_tax_code);
        prev_tax_code_via_prev_wk1_mth1_table.setWidget(0, 1, check_prev_wk1_mth1);
        //employee payroll settings -> week/month type
        html_week_month_type = new HTML();
        html_week_month_type.addStyleName(DEFAULT_WIDTH);
        html_week_month_type.ensureDebugId(test_code_ID_name + "week_month_type");
        //employee payroll settings -> week/month number
        html_week_month_number = new HTML();
        html_week_month_number.addStyleName(DEFAULT_WIDTH);
        html_week_month_number.ensureDebugId(test_code_ID_name + "week_month_number");
        //employee payroll settings -> total pay to date
        html_total_pay_to_date = new HTML();
        html_total_pay_to_date.addStyleName(DEFAULT_WIDTH);
        html_total_pay_to_date.ensureDebugId(test_code_ID_name + "total_pay_to_date");
        //employee payroll settings -> total tax to date
        html_total_tax_to_date = new HTML();
        html_total_tax_to_date.addStyleName(DEFAULT_WIDTH);
        html_total_tax_to_date.ensureDebugId(test_code_ID_name + "total_tax_to_date");
        //employee payroll settings -> deduct student loan
        html_student_loan = new HTML();
        html_student_loan.addStyleName(DEFAULT_WIDTH);
        html_student_loan.ensureDebugId(test_code_ID_name + "deduct_student_loan");
        //employee payroll settings -> Does your employee have another job where they already pay NICs
        html_have_another_job = new HTML();
        html_have_another_job.addStyleName(DEFAULT_WIDTH);
        html_have_another_job.ensureDebugId(test_code_ID_name + "have_another_job");
        //employee payroll settings -> family status
        html_family_status = new HTML();
        html_family_status.addStyleName(DEFAULT_WIDTH);
        html_family_status.ensureDebugId(test_code_ID_name + "family_status");

        //employee code
        html_emp_code = new HTML();
        html_emp_code.addStyleName(DEFAULT_WIDTH);
        html_emp_code.ensureDebugId(test_code_ID_name + "emp_code");

        //wage rate
        html_wage_rate = new HTML();
        html_wage_rate.addStyleName(DEFAULT_WIDTH);
        html_wage_rate.ensureDebugId(test_code_ID_name + "wage_rate");


        //fire date
        html_fire_date = new HTML();
        html_fire_date.addStyleName(DEFAULT_WIDTH);
        html_fire_date.ensureDebugId(test_code_ID_name + "fire_date");

        //html_wps_no
        html_wps_no = new HTML();
        html_wps_no.addStyleName(DEFAULT_WIDTH);
        html_wps_no.ensureDebugId(test_code_ID_name + "wpsno");

        //salary mode
        html_salary_mode = new HTML();
        html_salary_mode.addStyleName(DEFAULT_WIDTH);
        html_salary_mode.ensureDebugId(test_code_ID_name + "salary_mode");

        //employee payroll settings -> gender (male or female)
        html_gender = new HTML();
        html_gender.addStyleName(DEFAULT_WIDTH);
        html_gender.ensureDebugId(test_code_ID_name + "gender_male_female");
        //employee payroll settings -> NI No.
        html_ni_NO = new HTML();
        html_ni_NO.addStyleName(DEFAULT_WIDTH);
        html_ni_NO.ensureDebugId(test_code_ID_name + "ni_NO");
        //employee payroll settings -> NI category
        html_ni_category = new HTML();
        html_ni_category.setWidth("150px");
        html_ni_category.ensureDebugId(test_code_ID_name + "ni_category");
        //employee payroll settings -> current tax code
        html_current_tax_code = new HTML();
        html_current_tax_code.addStyleName(DEFAULT_WIDTH);
        html_current_tax_code.ensureDebugId(test_code_ID_name + "current_tax_code");
        //employee payment settings -> current wk1_month1
        check_wk1_mth1 = new KpiCheckBox(" Wk1/Mnth1");
        check_wk1_mth1.setWordWrap(false);
        check_wk1_mth1.setEnabled(false);
        check_wk1_mth1.setVisible(false);
        check_wk1_mth1.setWidth("100px");
        check_wk1_mth1.ensureDebugId(test_code_ID_name + "checkbox_wk1_mth1");
        //employee payment settings -> current tax code VIA current wk1_month1 table
        FlexTable current_tax_code_via_prev_wk1_mth1_table = new FlexTable();
        current_tax_code_via_prev_wk1_mth1_table.addStyleName(DEFAULT_WIDTH);
        current_tax_code_via_prev_wk1_mth1_table.setWidget(0, 0, html_current_tax_code);
        current_tax_code_via_prev_wk1_mth1_table.setWidget(0, 1, check_wk1_mth1);

        //employee payroll settings -> Not a member of COOPS
        html_not_member_of_COOPS = new HTML();
        html_not_member_of_COOPS.addStyleName(DEFAULT_WIDTH);
        html_not_member_of_COOPS.ensureDebugId(test_code_ID_name + "not_a_member_of_COOPS");
        //employee payroll settings -> pension scheme
        html_pension_scheme = new HTML();
        html_pension_scheme.addStyleName(DEFAULT_WIDTH);
        html_pension_scheme.ensureDebugId(test_code_ID_name + "pension_scheme");


        //employee payment settings -> job details
        job_details = new DataListBox();
        job_details.addListItem(type_a);
        job_details.addListItem(type_b);
        job_details.addListItem(type_c);
        job_details.setEnabled(false);
        check_d = new KpiCheckBox();
        check_d.setEnabled(false);
        check_d.ensureDebugId(test_code_ID_name + "job_details_d");
        //employee payment settings -> job details table
        HorizontalPanelDiv job_details_div = new HorizontalPanelDiv();
        job_details_div.add(job_details);
        job_details_div.add(check_d);
        job_details_div.setSpacing(5);


        //Account information section
        mainrole = new HTML();
        mainrole.addStyleName(DEFAULT_WIDTH);
        mainrole.ensureDebugId(test_code_ID_name + "roles");

        account_status = new HTML();
        account_status.addStyleName(DEFAULT_WIDTH);
        account_status.ensureDebugId(test_code_ID_name + "status");

        essUser = new HTML();
        essUser.addStyleName(DEFAULT_WIDTH);
        essUser.ensureDebugId(test_code_ID_name + "essUser");

        noAccess = new HTML();
        noAccess.addStyleName(DEFAULT_WIDTH);
        noAccess.ensureDebugId(test_code_ID_name + "noAccess");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        paymentsTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.payments(), false, true, false), false);
        deductionsTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.deductions(), false, true, true), false);
        taxTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.taxes(), false, true, true), false);
        loansTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.loans(), true, true, false), false);
        overtimeSettingsTable = new EditableTable(getOvertimeSettingsTableColumns(), false);
        employerContributionTable = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.employerContribution(), false, true, true), false);
        drawOverTimeSettingsTable(payrollStrings.regularOvertimeRate(), null, null, null, true, null);
        drawOverTimeSettingsTable(wfmStrings.weekendOvertimeRate(), null, null, null, true, null);
        drawOverTimeSettingsTable(wfmStrings.holidayOvertimeRate(), null, null, null, true, null);

        //add field items
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //employee details -> 1
        addTitleField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_DETAILS, wfmStrings.generalDetails());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME) != null) {
            addField(CustomFormConstants.FIRST_NAME, html_first_name, getTitle(formPropertyMap.get(CustomFormConstants.FIRST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.firstName(), formPropertyMap.get(CustomFormConstants.FIRST_NAME).isRequired()));
        } else {
            addField(CustomFormConstants.FIRST_NAME, html_first_name, getTitle(wfmStrings.firstName()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LAST_NAME) != null) {
            addField(CustomFormConstants.LAST_NAME, html_last_name, getTitle(formPropertyMap.get(CustomFormConstants.LAST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.LAST_NAME).getTitle() : wfmStrings.lastName(), formPropertyMap.get(CustomFormConstants.LAST_NAME).isRequired()));
        } else {
            addField(CustomFormConstants.LAST_NAME, html_last_name, getTitle(wfmStrings.lastName()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL) != null) {
            addField(CustomFormConstants.EMAIL, html_email, getTitle(formPropertyMap.get(CustomFormConstants.EMAIL).isChanged() ? formPropertyMap.get(CustomFormConstants.EMAIL).getTitle() : wfmStrings.email(), formPropertyMap.get(CustomFormConstants.EMAIL).isRequired()));
        } else {
            addField(CustomFormConstants.EMAIL, html_email, getTitle(wfmStrings.email()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.HIRE_DATE) != null) {
            addField(CustomFormConstants.HIRE_DATE, html_hire_date, getTitle(formPropertyMap.get(CustomFormConstants.HIRE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.HIRE_DATE).getTitle() : wfmStrings.hireDate(), formPropertyMap.get(CustomFormConstants.HIRE_DATE).isRequired()));
        } else {
            addField(CustomFormConstants.HIRE_DATE, html_hire_date, getTitle(wfmStrings.hireDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BIRTH_DAY) != null) {
            addField(CustomFormConstants.BIRTH_DAY, html_birth_date, getTitle(formPropertyMap.get(CustomFormConstants.BIRTH_DAY).isChanged() ? formPropertyMap.get(CustomFormConstants.BIRTH_DAY).getTitle() : wfmStrings.dateOfBirth(), formPropertyMap.get(CustomFormConstants.BIRTH_DAY).isRequired()));
        } else {
            addField(CustomFormConstants.BIRTH_DAY, html_birth_date, getTitle(wfmStrings.dateOfBirth()));
        }

        if (Utils.isUAECompany()) {
            addField(CustomFormConstants.CONTRACT_START_DATE, contractStartDate, getTitle(wfmStrings.contractStart()));
            addField(CustomFormConstants.CONTRACT_END_DATE, contractEndDate, getTitle(wfmStrings.contractEnd()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE) != null) {
            addField(CustomFormConstants.CONTRACT_START_DATE, contractStartDate, getTitle(formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE).getTitle() : wfmStrings.contractStart(), formPropertyMap.get(CustomFormConstants.CONTRACT_START_DATE).isRequired()));
        } else {
            addField(CustomFormConstants.CONTRACT_START_DATE, contractStartDate, getTitle(wfmStrings.contractStart()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE) != null) {
            addField(CustomFormConstants.CONTRACT_END_DATE, contractEndDate, getTitle(formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE).getTitle() : wfmStrings.contractEnd(), formPropertyMap.get(CustomFormConstants.CONTRACT_END_DATE).isRequired()));
        } else {
            addField(CustomFormConstants.CONTRACT_END_DATE, contractEndDate, getTitle(wfmStrings.contractEnd()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_METHOD) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_METHOD, html_salary_level, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_METHOD).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_METHOD).getTitle() : wfmStrings.salaryLevel(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_METHOD).isRequired()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_METHOD, html_salary_level, getTitle(wfmStrings.salaryLevel()));
        }

        //payment settings -> 2
        addTitleField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_SETTINGS, wfmStrings.paymentSettings());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID, html_employee_is_paid, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID).getTitle() : wfmStrings.employeeIsPaid(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID).isRequired()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID, html_employee_is_paid, getTitle(wfmStrings.employeeIsPaid()));
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.ATS_PAYROLL_CUSTOMIZATION)) {
            addField(CustomFormConstants.JOB_TITLE, html_job_title, getTitle(wfmStrings.jobTitle()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE) != null) {
            addField(CustomFormConstants.JOB_TITLE, salary_table, getTitle(formPropertyMap.get(CustomFormConstants.JOB_TITLE).isChanged() ? formPropertyMap.get(CustomFormConstants.JOB_TITLE).getTitle() : wfmStrings.jobTitle(), formPropertyMap.get(CustomFormConstants.JOB_TITLE).isRequired()));
        } else {
            addField(CustomFormConstants.JOB_TITLE, html_job_title, getTitle(wfmStrings.jobTitle()));
        }

        if (Utils.hasPermission(PermissionConstants.PAYROLL_EMPLOYEE_BASIC_SALARY)) {
            addField(CustomFormConstants.SALARY_GRADE, salary_table, wfmStrings.paymentCategory());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALARY_GRADE) != null) {
            addField(CustomFormConstants.SALARY_GRADE, salary_table, getTitle(formPropertyMap.get(CustomFormConstants.SALARY_GRADE).isChanged() ? formPropertyMap.get(CustomFormConstants.SALARY_GRADE).getTitle() : wfmStrings.paymentCategory(), formPropertyMap.get(CustomFormConstants.SALARY_GRADE).isRequired()));
        } else {
            addField(CustomFormConstants.SALARY_GRADE, salary_table, wfmStrings.paymentCategory());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CITIZENSHIP) != null) {
            addField(CustomFormConstants.CITIZENSHIP, html_citizenship, getTitle(formPropertyMap.get(CustomFormConstants.CITIZENSHIP).isChanged() ? formPropertyMap.get(CustomFormConstants.CITIZENSHIP).getTitle() : payrollStrings.citizenship(), formPropertyMap.get(CustomFormConstants.CITIZENSHIP).isRequired()));
        } else {
            addField(CustomFormConstants.CITIZENSHIP, html_citizenship, payrollStrings.citizenship());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.NORMAL_RATE) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.NORMAL_RATE, html_normal_rate, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.NORMAL_RATE).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.NORMAL_RATE).getTitle() : wfmStrings.normalRate(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.NORMAL_RATE).isRequired()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.NORMAL_RATE, html_normal_rate, getTitle(wfmStrings.normalRate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.OVER_TIME_RATE) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.OVER_TIME_RATE, html_over_time_rate, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.OVER_TIME_RATE).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.OVER_TIME_RATE).getTitle() : wfmStrings.overtimeRate(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.OVER_TIME_RATE).isRequired()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.OVER_TIME_RATE, html_over_time_rate, getTitle(wfmStrings.overtimeRate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD, html_pay_period, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD).getTitle() : wfmStrings.payPeriod(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD).isRequired()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD, html_pay_period, getTitle(wfmStrings.payPeriod()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD, html_payment_method, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD).getTitle() : wfmStrings.paymentMethod(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD).isRequired()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.PAY_METHOD, html_payment_method, getTitle(wfmStrings.paymentMethod()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS, html_employee_bonus, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS).getTitle() : wfmStrings.paymentMethod(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS).isRequired()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_BONUS, html_employee_bonus, getTitle(wfmStrings.bonus()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION, html_employee_commission, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION).getTitle() : wfmStrings.commission(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION).isRequired()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_COMMISSION, html_employee_commission, getTitle(wfmStrings.commission()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE) != null) {
            addField(CustomFormConstants.EMPLOYEE_CODE, html_emp_code, getTitle(formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE).getTitle() : wfmStrings.employeeCode(), formPropertyMap.get(CustomFormConstants.EMPLOYEE_CODE).isRequired()));
        } else {
            addField(CustomFormConstants.EMPLOYEE_CODE, html_emp_code, getTitle(wfmStrings.employeeCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE) != null) {
            addField(CustomFormConstants.RESIGNATION_DATE, html_fire_date, getTitle(formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).getTitle() : wfmStrings.resignationDate(), formPropertyMap.get(CustomFormConstants.RESIGNATION_DATE).isRequired()));
        } else {
            addField(CustomFormConstants.RESIGNATION_DATE, html_fire_date, getTitle(wfmStrings.resignationDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WPS_NUMBER) != null) {
            addField(CustomFormConstants.WPS_NUMBER, html_wps_no, getTitle(formPropertyMap.get(CustomFormConstants.WPS_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.WPS_NUMBER).getTitle() : wfmStrings.wpsNumber(), formPropertyMap.get(CustomFormConstants.WPS_NUMBER).isRequired()));
        } else {
            addField(CustomFormConstants.WPS_NUMBER, html_wps_no, getTitle(!"".equals(Utils.getPersonalID()) ? Utils.getPersonalID() : wfmStrings.wpsNumber()));
        }

        addTitleField(CustomFormConstants.PAYROLL_STARTER.PAYROLL_SETTINGS, wfmStrings.payrollSettings());
        addTitleField(PAYROLL_STARTER.EMPLOYEE_OVERTIME_SETTINGS, wfmStrings.overTimeSettings());
        addField(PAYROLL_STARTER.OVERTIME_SETTINGS_TABLE, overtimeSettingsTable, null);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS, html_family_status, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS).getTitle() : wfmStrings.maritalStatus(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS).isRequired()));
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS, html_family_status, getTitle(wfmStrings.maritalStatus()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.GENDER) != null) {
            addField(CustomFormConstants.GENDER, html_gender, getTitle(formPropertyMap.get(CustomFormConstants.GENDER).isChanged() ? formPropertyMap.get(CustomFormConstants.GENDER).getTitle() : wfmStrings.maritalStatus(), formPropertyMap.get(CustomFormConstants.GENDER).isRequired()));
        } else {
            addField(CustomFormConstants.GENDER, html_gender, getTitle(wfmStrings.gender()));
        }
        job_details_item = addCustomField(CustomFormConstants.PAYROLL_STARTER.JOB_DETAILS, job_details_div, getTitle(wfmStrings.jobDetails()));

        if (Utils.hasRole(ADMIN) || Utils.hasRole(DR)) {
            addTitleField(CustomFormConstants.ACCOUNT_INFORMATION, wfmStrings.basicInfo());
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS) != null) {
                addField(CustomFormConstants.ACCOUNT_STATUS, account_status, getTitle(formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS).getTitle() : wfmStrings.accountStatus(), formPropertyMap.get(CustomFormConstants.ACCOUNT_STATUS).isRequired()));
            } else {
                addField(CustomFormConstants.ACCOUNT_STATUS, account_status, getTitle(wfmStrings.accountStatus()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ESS_USER) != null) {
                addField(CustomFormConstants.ESS_USER, essUser, getTitle(formPropertyMap.get(CustomFormConstants.ESS_USER).isChanged() ? formPropertyMap.get(CustomFormConstants.ESS_USER).getTitle() : wfmStrings.essUser(), formPropertyMap.get(CustomFormConstants.ESS_USER).isRequired()));
            } else {
                addField(CustomFormConstants.ESS_USER, essUser, getTitle(wfmStrings.essUser()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NO_ACCESS) != null) {
                addField(CustomFormConstants.NO_ACCESS, noAccess, getTitle(formPropertyMap.get(CustomFormConstants.NO_ACCESS).isChanged() ? formPropertyMap.get(CustomFormConstants.NO_ACCESS).getTitle() : wfmStrings.noAccess(), formPropertyMap.get(CustomFormConstants.NO_ACCESS).isRequired()));
            } else {
                addField(CustomFormConstants.NO_ACCESS, noAccess, getTitle(wfmStrings.noAccess()));
            }

            if (Utils.hasGenericAccess(GenericSettingsEnum.LOCALE_PAYROLL)) {
                if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALARY_MODE) != null) {
                    addField(CustomFormConstants.SALARY_MODE, html_salary_mode, getTitle(formPropertyMap.get(CustomFormConstants.SALARY_MODE).isChanged() ?
                                    formPropertyMap.get(CustomFormConstants.SALARY_MODE).getTitle() : wfmStrings.salaryMode(),
                            formPropertyMap.get(CustomFormConstants.SALARY_MODE).isRequired()));
                } else {
                    addField(CustomFormConstants.SALARY_MODE, html_salary_mode, getTitle(wfmStrings.salaryMode()));
                }
            }
        }

        visibleNormalRateFields(false);
        visibleOverTimeRateFields(false);
        visibleJobDetailsFields(false);
        show();
        drawPaymentDeductionCategoryTable();
    }

    @Override
    protected void initialize2() {
        super.initialize2();
    }

    @Override
    protected Widget onInitialize() {
        return super.onInitialize();
    }

    @Override
    protected void setPayrollSettings(HashMap<String, String> payrollSettingsMap) {
        String pensionSchemes = null;
        if (payrollSettingsMap != null && payrollSettingsMap.size() == 0) {

            html_salary.setHTML(numberFormat.format(BigDecimal.ZERO));
            html_current_tax_code.setHTML("647L");
            html_total_pay_to_date.setHTML(numberFormat.format(BigDecimal.ZERO));
            html_total_tax_to_date.setHTML(numberFormat.format(BigDecimal.ZERO));
        }

        //employee payroll settings -> start date
        if (startDateForOnlyPayroll != null) {
            html_start_date.setHTML(DateUtils.format(startDateForOnlyPayroll));
        }
        //employee payroll settings -> employee has form
        if (payrollSettingsMap.get(FORMS) != null) {
            if (payrollSettingsMap.get(FORMS).equals("P46")) {
                visibleJobDetailsFields(true);
                visibleP45DetailsFields(false);
                html_employee_has_form.setHTML("P46");
            } else {
                visibleJobDetailsFields(false);
                visibleP45DetailsFields(true);
                html_employee_has_form.setText("P45");
            }
        }
        //employee payroll settings -> previous end date
        if (prevEndDate != null) {
            html_prev_end_date.setHTML(DateUtils.format(prevEndDate));
        }
        if (payrollSettingsMap.get(Constants.CONTRACT_START_DATE) != null) {
            contractStartDate.setHTML(payrollSettingsMap.get(Constants.CONTRACT_START_DATE));
        }
        if (payrollSettingsMap.get(Constants.CONTRACT_END_DATE) != null) {
            contractEndDate.setHTML(payrollSettingsMap.get(Constants.CONTRACT_END_DATE));
        }
        //employee payroll settings -> office number
        if (payrollSettingsMap.get(OFFICE_NUMBER) != null) {
            html_office_reference_number.setHTML(payrollSettingsMap.get(OFFICE_NUMBER));
        }
        //employee payroll settings -> pay. reference number
        if (payrollSettingsMap.get(PAYE_REF_NUMBER) != null) {
            html_office_reference_number.setHTML((html_office_reference_number.getHTML().length() > 0 ? (html_office_reference_number.getHTML() + "/") : "") + payrollSettingsMap.get(PAYE_REF_NUMBER));
        }
        //employee payroll settings -> previous tax code
        if (payrollSettingsMap.get(PREV_TAX_CODE) != null) {
            html_prev_tax_code.setHTML(payrollSettingsMap.get(PREV_TAX_CODE));
        }
        if (payrollSettingsMap.get(PREV_WK1MTH1) != null) {
            if ("true".equals(payrollSettingsMap.get(PREV_WK1MTH1))) {
                check_prev_wk1_mth1.setValue(true);
//                check_prev_wk1_mth1.setVisible(true);
            } else if ("false".equals(payrollSettingsMap.get(PREV_WK1MTH1))) {
                check_prev_wk1_mth1.setValue(false);
//                check_prev_wk1_mth1.setVisible(false);
            }
        }
        //employee payroll settings -> week/month type via number
        if (payrollSettingsMap.get(WEEK_MONTH_TYPE) != null && payrollSettingsMap.get(WEEK_MONTH_NUMBER) != null) {
            html_week_month_type.setHTML(Frequency.getByID(Integer.valueOf(payrollSettingsMap.get(WEEK_MONTH_TYPE))).getName());
            html_week_month_number.setHTML(getPaymentPeriods(Integer.valueOf(payrollSettingsMap.get(WEEK_MONTH_NUMBER)), Frequency.getByID(Integer.valueOf(payrollSettingsMap.get(WEEK_MONTH_TYPE))).getCycleName()));
        }
        //employee payroll settings -> total pay to date
        if (payrollSettingsMap.get(TOTAL_PAY_TO_DATE) != null) {
            html_total_pay_to_date.setHTML(numberFormat.format(parseToBigDecimal(payrollSettingsMap.get(TOTAL_PAY_TO_DATE))));
        }
        //employee payroll settings -> total tax to date
        if (payrollSettingsMap.get(TOTAL_TAX_TO_DATE) != null) {
            html_total_tax_to_date.setHTML(numberFormat.format(parseToBigDecimal(payrollSettingsMap.get(TOTAL_TAX_TO_DATE))));
        }
        //employee payroll settings -> student loan
        if (payrollSettingsMap.get(STUDENT_LOAN) != null) {
            html_student_loan.setHTML(payrollSettingsMap.get(STUDENT_LOAN));
        }
        //employee payroll settings -> Does your employee have another job where they already pay NICs
        if (payrollSettingsMap.get(HAVE_ANOTHER_JOB) != null) {
            html_have_another_job.setHTML(payrollSettingsMap.get(HAVE_ANOTHER_JOB));
        }
        //employee payroll settings -> family status
        if (payrollSettingsMap.get(FAMILY_STATUS) != null) {
            html_family_status.setHTML(payrollSettingsMap.get(FAMILY_STATUS));
        }
        //employee payroll settings -> gender (male/female)
        if (payrollSettingsMap.get(SEX) != null) {
            html_gender.setHTML(payrollSettingsMap.get(SEX));
        }
        //employee payroll settings -> NI number
        if (payrollSettingsMap.get(NI_NUMBER) != null) {
            html_ni_NO.setHTML(payrollSettingsMap.get(NI_NUMBER));
        }
        //employee payroll settings -> NI category
        if (payrollSettingsMap.get(NI_TABLE_LETTER) != null) {
            html_ni_category.setHTML(payrollSettingsMap.get(NI_TABLE_LETTER));
        }
        //employee payroll settings -> current tax code
        if (payrollSettingsMap.get(TAX_CODE) != null) {
            html_current_tax_code.setHTML(payrollSettingsMap.get(TAX_CODE));
        }
        //employee payroll settings -> not member of COOPS
        if (payrollSettingsMap.get(NOT_MEMBER_OF_COOPS) != null) {
            html_not_member_of_COOPS.setHTML("true".equals(payrollSettingsMap.get(NOT_MEMBER_OF_COOPS)) ? wfmStrings.enabled() : wfmStrings.disabled());
        }
        //employee payroll settings -> pension scheme
        if (payrollSettingsMap.get(PENSION_SCHEME) != null) {
            pensionSchemes = payrollSettingsMap.get(PENSION_SCHEME);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //payment settings

        //employee payment settings -> salary
        if (payrollSettings.getSalaryCategory() != null) {
            salaryPaymentCategoryHTML.setHTML(payrollSettings.getSalaryCategory().getName());
        }
        if (payrollSettingsMap.get(SALARY) != null) {
            html_salary.setHTML(numberFormat.format(parseToBigDecimal(payrollSettingsMap.get(SALARY))));
        } else {
            html_salary.setHTML(numberFormat.format(BigDecimal.ZERO));
        }
        if (payrollSettingsMap.get(JOB_TITLE_TEXT) != null) {
            html_job_title.setHTML(payrollSettingsMap.get(JOB_TITLE_TEXT));
        }
        //employee payment settings -> over time rate
        if (payrollSettingsMap.get(OVERTIME_RATE) != null) {
            html_over_time_rate.setHTML(numberFormat.format(parseToBigDecimal(payrollSettingsMap.get(OVERTIME_RATE))));
        }
        if (payrollSettingsMap.get(RATE_TYPE) != null) {
            html_salary.setVisible(true);
            if (payrollSettingsMap.get(RATE_TYPE).equals(FIXED_RATE)) {
                html_employee_is_paid.setHTML(wfmStrings.fixedRate());
            } else if (payrollSettingsMap.get(RATE_TYPE).equals(TIMESHEET_ONLY_RATE)) {
                html_employee_is_paid.setHTML(wfmStrings.paymentTypeTimesheetOnly());
                html_salary.setVisible(false);
            } else if (payrollSettingsMap.get(RATE_TYPE).equals(FIXED_OVERTIME_RATE)) {
                html_employee_is_paid.setHTML(wfmStrings.fixedRateWithOvertime());
            } else if (payrollSettingsMap.get(RATE_TYPE).equals(FIXED_HRMS_OVERTIME_RATE)) {
                html_employee_is_paid.setHTML(wfmStrings.fixedHourlyRate());
            } else if (payrollSettingsMap.get(RATE_TYPE).equals(HRMS_HOURS_RATE)) {
                html_employee_is_paid.setHTML(payrollStrings.hrmsHoursRate());
            } else if (payrollSettingsMap.get(RATE_TYPE).equals(TIMESHEET_RATE)) {
                html_employee_is_paid.setHTML(Property.get(Constants.TIMESHEET, payrollStrings.timeSheetHoursRate(), wfmStrings.timesheet()));
            } else if (payrollSettingsMap.get(RATE_TYPE).equals(FIXED_TIMESHEET_OVERTIME_RATE)) {
                html_employee_is_paid.setHTML(Property.get(Constants.TIMESHEET, payrollStrings.basedOnMonthlyTimesheet(), wfmStrings.timesheet()));
            } else if (payrollSettingsMap.get(RATE_TYPE).equals(FIXED_ATTENDANCE_REPORT_OVERTIME_RATE)) {
                html_employee_is_paid.setHTML(wfmStrings.basedOnAttendanceReport());
            }
        }
        if (payrollSettingsMap.get(NORMAL_RATE) != null) {
            html_normal_rate.setHTML(numberFormat.format(parseToBigDecimal(payrollSettingsMap.get(NORMAL_RATE))));
        }
        if (payrollSettingsMap.get(SALARY_GROSS_NET) != null) {
            html_salary_gross_net.setHTML(payrollSettingsMap.get(SALARY_GROSS_NET));
        }
        if (payrollSettingsMap.get(PAY_FREQUENCY) != null) {
            html_pay_period.setHTML(payrollSettingsMap.get(PAY_FREQUENCY));
        }
        if (payrollSettingsMap.get(PAY_METHOD) != null) {
            html_payment_method.setHTML(payrollSettingsMap.get(PAY_METHOD));
        }
        if (payrollSettingsMap.get(PAYMENT_METHOD) != null) {
            html_salary_level.setHTML(payrollSettingsMap.get(PAYMENT_METHOD));
        }
        if (payrollSettingsMap.get(EMPLOYEE_BONUS_TYPE) != null && payrollSettingsMap.get(EMPLOYEE_BONUS_VALUE) != null && !"".equals(payrollSettingsMap.get(EMPLOYEE_BONUS_TYPE)) && !"".equals(payrollSettingsMap.get(EMPLOYEE_BONUS_VALUE))) {
            BigDecimal bonusAmount = new BigDecimal(payrollSettingsMap.get(EMPLOYEE_BONUS_VALUE));
            html_employee_bonus.setHTML(PERCENTAGE.equals(payrollSettingsMap.get(EMPLOYEE_BONUS_TYPE)) ? PayrollClientUtils.numberFormat(bonusAmount) + "%" : PayrollClientUtils.format(bonusAmount));
        }
        if (payrollSettingsMap.get(EMPLOYEE_COMMISSION) != null) {
            html_employee_commission.setHTML(numberFormat.format(parseToBigDecimal(payrollSettingsMap.get(EMPLOYEE_COMMISSION))));
        }
        if (payrollSettingsMap.get(PAY_METHOD) != null) {
//                selectByName(payrollSettings.get(PAY_METHOD), html_payment_method);
        }

        if (payrollSettingsMap.get(WAGE_RATE) != null) {
            html_wage_rate.setHTML(payrollSettingsMap.get(WAGE_RATE));
        }
        SelectItem overtimeCategory;
        if (payrollSettingsMap.get(REGULAR_OVERTIME_RATE) != null) {
            overtimeCategory = payrollSettingsMap.get(REGULAR_OVERTIME_CATEGORY_ID) != null ? new SelectItem(Integer.valueOf(payrollSettingsMap.get(REGULAR_OVERTIME_CATEGORY_ID)), payrollSettingsMap.get(REGULAR_OVERTIME_CATEGORY_NAME)) : null;
            setOvertimeSettingsTableData(overtimeCategory, payrollSettingsMap.get(REGULAR_OVERTIME_RATE_TYPE), payrollSettingsMap.get(REGULAR_OVERTIME_RATE), 0);
        } else {
            setOvertimeSettingsTableData(null, null, null, 0);
        }
        if (payrollSettingsMap.get(WEEKEND_OVERTIME_RATE) != null) {
            overtimeCategory = payrollSettingsMap.get(WEEKEND_OVERTIME_CATEGORY_ID) != null ? new SelectItem(Integer.valueOf(payrollSettingsMap.get(WEEKEND_OVERTIME_CATEGORY_ID)), payrollSettingsMap.get(WEEKEND_OVERTIME_CATEGORY_NAME)) : null;
            setOvertimeSettingsTableData(overtimeCategory, payrollSettingsMap.get(WEEKEND_OVERTIME_RATE_TYPE), payrollSettingsMap.get(WEEKEND_OVERTIME_RATE), 1);
        } else {
            setOvertimeSettingsTableData(null, null, null, 1);
        }
        if (payrollSettingsMap.get(HOLIDAY_OVERTIME_RATE) != null) {
            overtimeCategory = payrollSettingsMap.get(HOLIDAY_OVERTIME_CATEGORY_ID) != null ? new SelectItem(Integer.valueOf(payrollSettingsMap.get(HOLIDAY_OVERTIME_CATEGORY_ID)), payrollSettingsMap.get(HOLIDAY_OVERTIME_CATEGORY_NAME)) : null;
            setOvertimeSettingsTableData(overtimeCategory, payrollSettingsMap.get(HOLIDAY_OVERTIME_RATE_TYPE), payrollSettingsMap.get(HOLIDAY_OVERTIME_RATE), 2);
        } else {
            setOvertimeSettingsTableData(null, null, null, 2);
        }

        if (payrollSettingsMap.get(CustomFormConstants.WPS_NUMBER) != null) {
            html_wps_no.setHTML(payrollSettingsMap.get(CustomFormConstants.WPS_NUMBER));
        }

        //employee payroll settings -> job details
        if (payrollSettingsMap.get(JOB_TYPE) != null || payrollSettingsMap.get(JOB_TYPE_D) != null) {
            visibleJobDetailsFields(true);

            if (payrollSettingsMap.get(JOB_TYPE) != null) {
                if ("A".equals(payrollSettingsMap.get(JOB_TYPE))) {
                    job_details.setSelected(type_a);
                } else if ("B".equals(payrollSettingsMap.get(JOB_TYPE))) {
                    job_details.setSelected(type_b);
                } else if ("C".equals(payrollSettingsMap.get(JOB_TYPE))) {
                    job_details.setSelected(type_c);
                }
            }
            if (payrollSettingsMap.get(JOB_TYPE_D) != null) {
                if ("true".equals(payrollSettingsMap.get(JOB_TYPE_D))) {
                    check_d.setValue(true);
                }
            }
        }


        //employee payroll settings -> pension schemes set
        final String finalPensionSchemes = pensionSchemes;
        PayrollService.App.get().getPensionSchemes(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(SelectItem[] result) {
                String[] ids = null;
                if (finalPensionSchemes != null && !"".equals(finalPensionSchemes)) {
                    ids = finalPensionSchemes.split(",");
                }
                StringBuilder pens = new StringBuilder();
                if (ids != null) {
                    for (String id : ids) {
                        for (SelectItem aResult : result) {
                            if (aResult.getId().toString().equals(id)) {
                                pens.append(aResult.getName()).append("<br>");
                            }
                        }
                    }
                }
                html_pension_scheme.setHTML(pens.toString());
            }
        });
    }

    public void setOvertimeSettingsTableData(SelectItem overtimeCategory, String overtimeType, String amount, Integer index) {
        CategoryLookUp categoryLookUp = (CategoryLookUp) overtimeSettingsTable.getColumnById(index, "paymentCategory");
        PayslipItemAmountWidget amountWidget = (PayslipItemAmountWidget) overtimeSettingsTable.getColumnById(index, "amount");
        DataListBox typeWidget = (DataListBox) overtimeSettingsTable.getColumnById(index, "type");
        CustomCell amountWidgetCell = (CustomCell) overtimeSettingsTable.getColumnCellWidgetById(index, "amount");
        CustomCell typeWidgetCell = (CustomCell) overtimeSettingsTable.getColumnCellWidgetById(index, "type");
        amountWidget.getAmountTextBox().setEnabled(false);
        typeWidget.setEnabled(false);

        if (overtimeType != null && !"".equals(overtimeType)) {
            if (FIXED.equals(overtimeType)) {
                typeWidget.setSelected(0);
            } else {
                typeWidget.setSelected(1);
            }
        }
        if (overtimeCategory != null) {
            categoryLookUp.setSelected(overtimeCategory);
        }

        if (amount != null && !"".equals(amount)) {
            amountWidget.setAmount(PayrollClientUtils.parseToBigDecimal(amount));
        }

        categoryLookUp.setEnabled(false);
        amountWidgetCell.InActive();
        typeWidgetCell.InActive();
    }


    public void addItem(PaymentDeductionObject paymentDeduction, String type) {
        EditableTextBox category = new EditableTextBox();
        category.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getCategoryItem() != null) {
            category.setText(paymentDeduction.getCategoryItem().getName());
        }

        EditableTextBox paymentType = new EditableTextBox();
        paymentType.setEnabled(false);
        if (paymentDeduction != null) {
            paymentType.setText(paymentDeduction.getPaymentType() != null ? paymentDeduction.getPaymentType().getTitle() : EPPaymentType.RECURRING.getTitle());
        }

        EditableTextBox payType = new EditableTextBox();
        payType.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getType() != null) {
            if (paymentDeduction.getType() == 0) {
                payType.setText(wfmStrings.fixed());
            } else if (paymentDeduction.getType() == 1) {
                payType.setText(wfmStrings.basicOfPersentage());
            } else {
                payType.setText(wfmStrings.ofBasicAllowances());
            }
        } else {
            payType.setText(wfmStrings.fixed());
        }

        EditableTextBox amount = new EditableTextBox();
        amount.setEnabled(false);
        if (paymentDeduction != null && (paymentDeduction.getType() == null || paymentDeduction.getType() == 0)) {
            amount.setText(PayrollClientUtils.format(paymentDeduction.getPaymentAmount()));
        } else if (paymentDeduction != null && (paymentDeduction.getType() == 1 || paymentDeduction.getType() == 2)) {
            amount.setText(PayrollClientUtils.numberFormat(paymentDeduction.getPercentage()) + "%");
        } else {
            amount.setText(PayrollClientUtils.format(BigDecimal.ZERO));
        }

        EditableTextBox startDate = new EditableTextBox();
        startDate.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getStarttDate() != null && paymentDeduction.getStarttDate().getDate() != null) {
            startDate.setText(DateUtils.format(paymentDeduction.getStarttDate()));
        }


        EditableTextBox totalAmount = new EditableTextBox();
        Validation.addNumericKeyboardListener(totalAmount);
        totalAmount.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getTotalAmount() != null) {
            totalAmount.setText(PayrollClientUtils.format(paymentDeduction.getTotalAmount()));
        }


        /*EditableTextBox remainingAmount = new EditableTextBox();
        remainingAmount.setEnabled(false);
        if (paymentDeduction != null && paymentDeduction.getRemainingAmount() != null) {
            remainingAmount.setText(PayrollClientUtils.format(paymentDeduction.getRemainingAmount()));
        }*/

        EditableTable table = getTable(type);
        if (PayrollConstants.CATEGORY_LOAN.equals(type)) {
            table.addRow(new Widget[]{category, payType, amount, totalAmount, startDate/*, remainingAmount*/});
        } else {
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_PAYSLIP_PAYMENT_TYPE)) {
                table.addRow(new Widget[]{category, paymentType, payType, amount});
            } else {
                table.addRow(new Widget[]{category, payType, amount});
            }
        }
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
}
