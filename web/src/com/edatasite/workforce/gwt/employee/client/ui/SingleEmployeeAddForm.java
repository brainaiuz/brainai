package com.edatasite.workforce.gwt.employee.client.ui;

import com.edatasite.workforce.gwt.contact.client.ui.GeneralEmployeeEditForm;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.employee.client.ui.view.quickadd.VerificationBox;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.google.gwt.user.client.rpc.AsyncCallback;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_VERIFICATION;

/**
 * Created by Dilshod Madrahimov on 8/31/15 12:24 PM
 */
public class SingleEmployeeAddForm extends GeneralEmployeeEditForm {

    private final String fromView;

    public SingleEmployeeAddForm(String[] params) {
        super("singleemployeeadd", wfmStrings.addEmployee());
        this.fromView = params[1];
        this.isAddView = params[0].equals("add");
        if (params.length == 5 && params[2] == "CONVERT") {
            formType = String.valueOf(params[3]);
            convertedFormId = Integer.parseInt(params[4]);
        } else if (params.length > 2) {
            this.isPlacement = Boolean.parseBoolean(params[2]);
            this.placementId = Integer.parseInt(params[3]);
        }
    }

    @Override
    protected void addButtons() {

        saveAndCloseButton = addButton(wfmStrings.save(), BTN_PRIMARY, null, "employee_add_view_save_and_close_button", event -> save(true));
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE)) {
            saveButton = addButton(wfmStrings.saveAndNew(), BTN_PRIMARY, null, "employee_add_view_save_and_new_button", event -> save(false));
        }
    }

    @Override
    protected String getFormID() {

        fromSectionSingleEmployeeAdd = true;

        if (FROM_HRMS.equals(fromView)) {
            return LayoutRPC.HRMS_EMPLOYEE_FORM;
        }
        if (FROM_PM.equals(fromView)) {
            fromSectionPMEmployeeEdit = true;
            employeeAddFromPayroll = true;
            return LayoutRPC.PM_EMPLOYEE_FORM;
        }
        if (FROM_PAYROLL.equals(fromView)) {
            return LayoutRPC.PAYROLL_EMPLOYEE_FORM;
        }
        if (FROM_SETTINGS_PROFILE.equals(fromView)) {
            fromSectionProfileSettingsEdit = true;
            return LayoutRPC.SETTINGS_EMPLOYEE_FORM;
        }
        if (FROM_TC_INSTRUCTOR.equals(fromView)) {
            fromSectionTCInstructor = true;
            return LayoutRPC.INSTRUCTOR_FORM;
        }

        return null;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected void registerFields() {
        super.registerFields();

        initializeInstructorForm();

    }

    private void initializeInstructorForm() {
        if (Utils.isSettings()) {
            drawEmployeeInformation();
            drawEmploymentInformation();
            drawContactDetails();
            if (Utils.hasPermission(PermissionConstants.HRMS_SHOW_EMPLOYEE_ROLE_WIDGET)) {
                drawAccountInformation();
            }
        } else {
            //employee information
            drawEmployeeInformation();

            drawCustomFields();

            //contact details
            drawContactDetails();
            //address information
            drawAddressInformation();
            //employment information
            drawEmploymentInformation();

            if (FROM_HRMS.equals(fromView)) {
                //bank information
                if (Utils.hasPermission(PermissionConstants.SHOW_EMPLOYEE_BANK_DETAILS)) {
                    drawBankInformation();
                }
                //Personal Identity Information
                if (Utils.hasPermission(PermissionConstants.SHOW_EMPLOYEE_PERSONAL_INFORMATION)) {
                    drawPersonalIdentityInformation();
                }
                //attachments
                if (Utils.hasPermission(PermissionConstants.HRMS_SHOW_EMPLOYEE_ATTACHMENT)) {
                    drawAttachments();
                }
            }
            //account information
            if (Utils.hasPermission(PermissionConstants.HRMS_SHOW_EMPLOYEE_ROLE_WIDGET) || Utils.isSettings()) {
                drawAccountInformation();
            }
            drawPaymentDeductionCategoryTable();
        }
        if (Utils.hasGenericAccess(ENABLE_VERIFICATION)) {
            VerificationBox verificationBox = new VerificationBox();
        }

        show();
    }

    @Override
    protected void save(final boolean saveAndClose) {
        enableButton(false);
        if (!validation()) {
            enableButton(true);
            Info.show(wfmStrings.pleasefillintherequiredfields(), Info.Type.WARNING);
            return;
        } else {
            if (hireDatePicker != null && fireDatePicker != null && hireDatePicker.getDate() != null && fireDatePicker.getDate() != null) {
                if (hireDatePicker.getDate().after(fireDatePicker.getDate())) {
                    Info.show(wfmStrings.hireDateCannotBeAfterResignDate(), Info.Type.WARNING);
                    markAsError(hireDatePicker, true);
                    markAsError(fireDatePicker, true);
                    enableButton(true);
                    return;
                }
            }
        }
        setValues();

        LoadingPanel.loading(true);
        NewEmployee employee = wrap();
        NewEmployee[] employees = new NewEmployee[1];
        employees[0] = employee;
        checkPositionAvailability(employees, saveAndClose);


    }

    private NewEmployee wrap() {
        NewEmployee employee = new NewEmployee();
        employee.setAddSingleEmployee(true);
        employee.setFname(profileItem.getFirstName());
        employee.setLname(profileItem.getLastName());
        employee.setMname(profileItem.getMiddleName());
        employee.setOtherName(profileItem.getOtherName());
        employee.setTitle(profileItem.getTitle());
        employee.setEmail(profileItem.getEmail());
        employee.setDepartment(profileItem.getPmDepartmentID());
        employee.setRoleId(profileItem.getRoleId());
        employee.setStartDate(profileItem.getHireDate());
        employee.setEndDate(profileItem.getFireDate());
        employee.setHasAccess(profileItem.getNoAccess() == null || !profileItem.getNoAccess());
        employee.setBirthDate(profileItem.getDob());
        employee.setGender(profileItem.getGender());
        employee.setContactListItem(contactListItem);
        employee.setAttachments(profileItem.getAttachments());
        employee.setDriverID(profileItem.getDriverID());
        if (profileItem.getDriverID() != null && !"".equals(profileItem.getDriverID())) {
            employee.setDriverNumber(Long.valueOf(profileItem.getDriverID()));
        }
        employee.setNationality(profileItem.getNationality());
        employee.setMartialStatusId(profileItem.getMartialStatusId());
        employee.setSpokenLanguages(profileItem.getSpokenLanguages());
        employee.setEmployeeCompetencies(profileItem.getEmployeeCompetencies());
        employee.setCreatedFrom(profileItem.getFrom());
        employee.setNumberData(profileItem.getNumberData());
        employee.setEmpCode(profileItem.getEmpCode());
        employee.setDepartment(profileItem.getPmDepartmentID());
        employee.setWageRate(profileItem.getWageRate());
        employee.setClientChargeRate(profileItem.getClientChargeRate());
        employee.setQualificationID(profileItem.getQualificationID());
        employee.setStatusId(profileItem.getStatusId());
        employee.setReportsToId(profileItem.getReportsToId());
        employee.setTermsOfContract(profileItem.getTermsOfContract());
        employee.setTermsOfCMonthORYear(profileItem.getTermsOfCMonthORYear());
        employee.setEmpModeId(profileItem.getEmpModeId());
        employee.setSalaryGradeId(profileItem.getSalaryGradeId());
        employee.setSalaryAmount(profileItem.getSalaryAmount());
        employee.setJobTitleId(profileItem.getJobTitleId());
        employee.setJobTitle(profileItem.getJobTitle());
        employee.setVisaExpirationDate(profileItem.getVisaExpirationDate());
        employee.setVisaExpirationDateReminder(profileItem.getVisaExpirationDateReminder());
        employee.setPosition(profileItem.getPosition());
        employee.setPositionId(profileItem.getPositionId());
        employee.setLocationId(profileItem.getLocationId());
        employee.setApplyPositionLeaveForEmployee(profileItem.isApplyPositionLeaveForEmployee());
        employee.setBankAccountData(profileItem.getBankAccountData());
        employee.setCoursesItems(profileItem.getCoursesItems());
        employee.setPassportNumber(profileItem.getPassportNumber());
        employee.setPassportIssueDate(profileItem.getPassportIssueDate());
        employee.setPassportExpiryDate(profileItem.getPassportExpiryDate());
        employee.setMedicalInsuranceExpireDate(profileItem.getMedicalInsuranceExpireDate());
        employee.setVisaNumber(profileItem.getVisaNumber());
        employee.setVisaIssueDate(profileItem.getVisaIssueDate());
        employee.setInsuranceNumber(profileItem.getInsuranceNumber());
        if (profileItem.getPassportIssueItem() != null) {
            employee.setPassportIssueID(profileItem.getPassportIssueItem().getId());
        }
        employee.setCustomFields(profileItem.getCustomFields());
        employee.setCustomTableItems(profileItem.getCustomTableItems());
        employee.setPaymentMethod(profileItem.getPaymentMethod());
        employee.setEssUser(profileItem.getEss());
        employee.setPayrollSettings(profileItem.getPayrollSettings());
        employee.setPayments(profileItem.getPayments());
        employee.setDeductions(profileItem.getDeductions());
        employee.setLoans(profileItem.getLoans());
        employee.setEmployerContributions(profileItem.getEmployerContributions());
        employee.setInactiveCategories(profileItem.getInactiveCategories());
        employee.setDeletedCategories(profileItem.getDeletedCategories());
        employee.setOpeningBalanceDays(profileItem.getOpeningBalanceDays());
        employee.setProbationDays(profileItem.getProbationDays());
        employee.setPhotoID(profileItem.getPhotoId());
        employee.setSpokingLanguages(profileItem.getSpokingLanguages());
        employee.setEmployeeDegree(profileItem.getemployeeDegree());
        employee.setTimeslot(profileItem.getTimeslot());
        employee.setFingerprintDeviceId(profileItem.getFingerprintDeviceId());
        employee.setPlacementId(placementId);
        employee.setExperienceTableItems(profileItem.getExperienceTableItems());
        employee.setSalaryMode(profileItem.getSalaryMode());
        return employee;
    }

    private void checkPositionAvailability(NewEmployee[] employees, boolean saveAndClose) {
        if (profileItem.getPositionId() != null) {
            EmployeeService.App.get().checkPositionAvailability(profileItem.getPositionId(), new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    enableButton(true);
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Boolean result) {
                    if (result) {
                        createEmployee(employees, saveAndClose);
                    } else {
                        LoadingPanel.loading(false);
                        enableButton(true);
                        Info.warn(wfmStrings.position() + " " + wfmStrings.isAlreadySelected() + "");
                    }
                }
            });

        } else {
            createEmployee(employees, saveAndClose);
        }
    }

    private void createEmployee(NewEmployee[] employees, boolean saveAndClose) {
        EmployeeService.App.get().createEmployees(employees, new AbstractAsyncCallback<Integer[]>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong() + " " + wfmStrings.employee(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Integer[] result) {
                LoadingPanel.loading(false);
                switch (result[0]) {
                    case EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS:
                        Info.show(wfmStrings.employeeWithEmailAlreadyExist(), Info.Type.WARNING);
                        break;
                    case EMPLOYEE_WITH_THIS_EMAIL_HOST_DOES_NOT_EXIST:
                        Info.show(wfmStrings.invalidEmail(), Info.Type.WARNING);
                        break;
                    case EMPLOYEE_WITH_THIS_CODE_ALREADY_EXISTS:
                        Info.show(wfmStrings.youAlreadyHaveAIdWithThisNumber(), Info.Type.WARNING);
                        break;
                    case CAN_NOT_CREATE_EMPLOYEE:
                        Info.show(wfmStrings.canNotCreateAnEmployee(), Info.Type.WARNING);
                        break;
                    case ACTIVE_LIMIT_EXCEEDED:
                        Info.show(wfmStrings.usersLimitExceeded(), Info.Type.WARNING);
                        break;
                    case NO_ACCESS_LIMIT_EXCEEDED:
                        Info.show(wfmStrings.userLimitNoAccessExceeded(), Info.Type.WARNING);
                        break;
                    case ESS_LIMIT_EXCEEDED:
                        Info.show(wfmStrings.userLimitEssExceeded(), Info.Type.WARNING);
                        break;
                    case EMPLOYEE_WITH_THIS_PASSPORT_NUMBER_ALREADY_EXISTS:
                        Info.show(wfmStrings.youAlreadyHaveEmployeeWithThisPassportNumber(), Info.Type.WARNING);
                        break;
                    default:
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.employee()), Info.Type.INFO);
                        onShellOk(saveAndClose, result[0]);
                        if (!saveAndClose) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("singleemployee|add/add" + (fromView != null ? "/" + fromView : ""));
                        }
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADD, result[0], SingleEmployeeAddForm.this);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_BANK_UPDATE, null, SingleEmployeeAddForm.this);
                        if (placementId != null) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PLACEMENT_ADD_EDIT, result, SingleEmployeeAddForm.this);
                        }
                }
                enableButton(true);
            }
        });
    }
}
