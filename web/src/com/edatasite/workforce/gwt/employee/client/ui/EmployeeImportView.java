package com.edatasite.workforce.gwt.employee.client.ui;

import com.edatasite.workforce.gwt.contact.client.ui.AddContactView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.reference.PhoneReference;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.importfile.client.ImportAbstractView;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_PAYROLL_DEDUCTION_CATEGORIES;

/**
 * Created by Dilshod Madrahimov on 8/18/15 5:47 PM
 */
public class EmployeeImportView extends ImportAbstractView implements Colapse {
    public static final String PARAM_LIST_BOX = "PARAM_LIST_BOX";
    private static final String RELATION_LIST_BOX = "RELATION_LIST_BOX";
    private static final String PRIMARY_RADIO_BUTTON = "PRIMARY_RADIO_BUTTON";
    private final String test_code_ID_name = "employee_";
    private static final Integer EMAILS = 1;
    private static final Integer PHONES = 2;


    public EmployeeImportView(Integer objectID/*, Integer maxNoAccessCount*/) {
        super("hrmsemployeeadd", wfmStrings.importEmployees());
        this.objectId = objectID;
//        this.maxNoAccessCount = maxNoAccessCount;
    }

    //    private Integer maxNoAccessCount;
    private DataListBox salutation;
    private DataListBox firstName;
    private DataListBox lastName;
    private DataListBox middleName;
    private DataListBox dateOfBirth;
    private DataListBox gender;
    private DataListBox nationality;
    private DataListBox maritalStatus;
    private DataListBox spokenLanguages;
    private DataListBox competencies;

//    private DataListBox email;
//    private DataListBox phone;
    private MultiTable emails;
    private MultiTable phones;
    private DataListBox imAddress;
    private DataListBox webAddress;

    private DataListBox addressName;
    private DataListBox addressLine;
    private DataListBox addressLine2;
    private DataListBox addressType;
    private DataListBox country;
    private DataListBox state;
    private DataListBox city;
    private DataListBox postCode;

    private DataListBox employeeCode;
    private DataListBox departmentName;
    private DataListBox position;
    private DataListBox location;
    private DataListBox supervisor;
    private DataListBox wageRate;
    private DataListBox clientChargeRate;
    private DataListBox hireDate;
    private DataListBox resignationDate;
    private DataListBox employmentContractTerms;
    private DataListBox employmentMode;
    private DataListBox salaryGrade;
    private DataListBox basicSalary;
    private DataListBox openingBlanceDays, probationPeriodDays;
    private DataListBox qualification;

    private DataListBox bankName;
    private DataListBox accountNumber;
    private DataListBox accountName;
    private DataListBox bankAddress;
    private DataListBox swiftBICCode;
    private DataListBox sortCode;
    private DataListBox iBANNumber;
    private DataListBox agentID;

    private DataListBox passportNumber;
    private DataListBox passportIssueBy;
    private DataListBox passportIssueDate;
    private DataListBox passportExpiryDate;
    private DataListBox insuranceNumber;
    private DataListBox insuranceExpiryDate;
    private DataListBox visaNumber;
    private DataListBox visaIssueDate;
    private DataListBox visaExpiryDate;

    private DataListBox wpsNumber;

    private DataListBox employeeType;

    private EditableTable paymentsDeductions;

    private SelectItem[] columnItems;

    public void initialize() {
        LoadingPanel.loading(true);
        createAndSetWidth();
        super.initialize();
    }

    private void createAndSetWidth() {
        salutation = new DataListBox();
        salutation.addStyleName(DEFAULT_WIDTH);
        firstName = new DataListBox();
        firstName.addStyleName(DEFAULT_WIDTH);
        lastName = new DataListBox();
        lastName.addStyleName(DEFAULT_WIDTH);
        middleName = new DataListBox();
        middleName.addStyleName(DEFAULT_WIDTH);
        dateOfBirth = new DataListBox();
        dateOfBirth.addStyleName(DEFAULT_WIDTH);
        gender = new DataListBox();
        gender.addStyleName(DEFAULT_WIDTH);
        nationality = new DataListBox();
        nationality.addStyleName(DEFAULT_WIDTH);
        maritalStatus = new DataListBox();
        maritalStatus.addStyleName(DEFAULT_WIDTH);
        spokenLanguages = new DataListBox();
        spokenLanguages.addStyleName(DEFAULT_WIDTH);
        competencies = new DataListBox();
        competencies.addStyleName("file--EmployeeImportView");

        emails = new MultiTable(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getMultiWidgets(EMAILS);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });
        emails.getElement().setId("emails");
        phones = new MultiTable(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getMultiWidgets(PHONES);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });
        phones.getElement().setId("phones");
//        email = new DataListBox();
//        email.addStyleName(DEFAULT_WIDTH);
//        phone = new DataListBox();
//        phone.addStyleName(DEFAULT_WIDTH);
        imAddress = new DataListBox();
        imAddress.addStyleName(DEFAULT_WIDTH);
        webAddress = new DataListBox();
        webAddress.addStyleName(DEFAULT_WIDTH);

        addressName = new DataListBox();
        addressName.addStyleName(DEFAULT_WIDTH);
        addressLine = new DataListBox();
        addressLine.addStyleName(DEFAULT_WIDTH);
        addressLine2 = new DataListBox();
        addressLine2.addStyleName(DEFAULT_WIDTH);
        addressType = new DataListBox();
        addressType.addStyleName(DEFAULT_WIDTH);
        country = new DataListBox();
        country.addStyleName(DEFAULT_WIDTH);
        state = new DataListBox();
        state.addStyleName(DEFAULT_WIDTH);
        city = new DataListBox();
        city.addStyleName(DEFAULT_WIDTH);
        postCode = new DataListBox();
        postCode.addStyleName(DEFAULT_WIDTH);

        employeeCode = new DataListBox();
        employeeCode.addStyleName(DEFAULT_WIDTH);
        departmentName = new DataListBox();
        departmentName.addStyleName(DEFAULT_WIDTH);
        position = new DataListBox();
        position.addStyleName(DEFAULT_WIDTH);
        location = new DataListBox();
        location.addStyleName(DEFAULT_WIDTH);
        supervisor = new DataListBox();
        supervisor.addStyleName(DEFAULT_WIDTH);
        wageRate = new DataListBox();
        wageRate.addStyleName(DEFAULT_WIDTH);
        clientChargeRate = new DataListBox();
        clientChargeRate.addStyleName(DEFAULT_WIDTH);
        hireDate = new DataListBox();
        hireDate.addStyleName(DEFAULT_WIDTH);
        resignationDate = new DataListBox();
        resignationDate.addStyleName(DEFAULT_WIDTH);
        employmentContractTerms = new DataListBox();
        employmentContractTerms.addStyleName(DEFAULT_WIDTH);
        employmentMode = new DataListBox();
        employmentMode.addStyleName(DEFAULT_WIDTH);
        salaryGrade = new DataListBox();
        salaryGrade.addStyleName(DEFAULT_WIDTH);
        basicSalary = new DataListBox();
        basicSalary.addStyleName(DEFAULT_WIDTH);
        openingBlanceDays = new DataListBox();
        openingBlanceDays.addStyleName(DEFAULT_WIDTH);
        probationPeriodDays = new DataListBox();
        probationPeriodDays.addStyleName(DEFAULT_WIDTH);
        qualification = new DataListBox();
        qualification.addStyleName(DEFAULT_WIDTH);

        bankName = new DataListBox();
        bankName.addStyleName(DEFAULT_WIDTH);
        accountNumber = new DataListBox();
        accountNumber.addStyleName(DEFAULT_WIDTH);
        accountName = new DataListBox();
        accountName.addStyleName(DEFAULT_WIDTH);
        bankAddress = new DataListBox();
        bankAddress.addStyleName(DEFAULT_WIDTH);
        swiftBICCode = new DataListBox();
        swiftBICCode.addStyleName(DEFAULT_WIDTH);
        sortCode = new DataListBox();
        sortCode.addStyleName(DEFAULT_WIDTH);
        iBANNumber = new DataListBox();
        iBANNumber.addStyleName(DEFAULT_WIDTH);
        agentID = new DataListBox();
        agentID.addStyleName(DEFAULT_WIDTH);

        passportNumber = new DataListBox();
        passportNumber.addStyleName(DEFAULT_WIDTH);
        passportIssueBy = new DataListBox();
        passportIssueBy.addStyleName(DEFAULT_WIDTH);
        passportIssueDate = new DataListBox();
        passportIssueDate.addStyleName(DEFAULT_WIDTH);
        passportExpiryDate = new DataListBox();
        passportExpiryDate.addStyleName(DEFAULT_WIDTH);
        insuranceNumber = new DataListBox();
        insuranceNumber.addStyleName(DEFAULT_WIDTH);
        insuranceExpiryDate = new DataListBox();
        insuranceExpiryDate.addStyleName(DEFAULT_WIDTH);
        visaNumber = new DataListBox();
        visaNumber.addStyleName(DEFAULT_WIDTH);
        visaIssueDate = new DataListBox();
        visaIssueDate.addStyleName(DEFAULT_WIDTH);
        visaExpiryDate = new DataListBox();
        visaExpiryDate.addStyleName(DEFAULT_WIDTH);

        wpsNumber = new DataListBox();
        wpsNumber.addStyleName(DEFAULT_WIDTH);

        employeeType = new DataListBox();
        employeeType.addStyleName(DEFAULT_WIDTH);

        paymentsDeductions = new EditableTable(getPaymentDeductionTableColumns(wfmStrings.paymentsAndDeductions()));
        paymentsDeductions.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                addItem();
            }

            @Override
            public void removeRow() {

            }
        });
        paymentsDeductions.setRemoveRowListener(() -> {
            if (paymentsDeductions.getRowCount() <= 1) {
                WfmWindow.alert(wfmStrings.youCanNotRemoveOneLineItem());
            } else {
                paymentsDeductions.getGrid().getModel().removeRow(paymentsDeductions.getGrid().getCurrentRow());
            }
        });
        addItem();

    }

    private void addItem() {
        final CategoryLookUp categoryLookUp = new CategoryLookUp(() -> true);
//        categoryLookUp.getSuggestBox().getElement().setAttribute("style", "width:200px !important");

        categoryLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onCategorySelected(categoryLookUp));

        categoryLookUp.getSuggestBox().addKeyUpHandler(event -> onCategorySelected(categoryLookUp));

        DataListBox columns = new DataListBox();
        columns.setItems(columnItems);
        paymentsDeductions.addRow(new Widget[]{categoryLookUp, columns});
    }

    private void onCategorySelected(CategoryLookUp categoryLookUp) {
        int selectedCategoryCount = 0;
        PaymentDeductionSelectItem selectedCategory = categoryLookUp.getSelectedData();
        if (selectedCategory != null) {
            for (int i = 0; i < paymentsDeductions.getGrid().getRowCount(); i++) {
                PaymentDeductionSelectItem selectedItem = ((CategoryLookUp) paymentsDeductions.getColumnById(i, "category")).getSelectedData();
                if (selectedItem != null && selectedItem.getCode() != null && selectedCategory.getCode() != null && selectedItem.getCode().equals(selectedCategory.getCode())) {
                    selectedCategoryCount++;
                }
            }
            if (selectedCategoryCount >= 2) {
                categoryLookUp.clear();
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, selectedCategory.getName() +  wfmStrings.isAlreadySelected());
                messageBox.open();
            }
        }
    }

    private ColumnConfig[] getPaymentDeductionTableColumns(String title) {
        ColumnConfig[] columns = new ColumnConfig[2];
        columns[0] = new ColumnConfig(LookUpCell.class, "category", title, 220, true, "left-align-Cell");
        columns[1] = new ColumnConfig(CustomCell.class, "column", wfmStrings.columns() + " / " + wfmStrings.percentage(), 120, true, "right-align-Cell");
        return columns;
    }

    @Override
    public void drawForm() {
        super.drawForm();
        addTitleField(EMPLOYEE_INFORMATION, wfmStrings.employeeInformation());
        addField(SALUTATION, salutation, getTitle("Salutation"));
        addField(CustomFormConstants.FIRST_NAME, firstName, getTitle(wfmStrings.firstName(), true));
        addField(CustomFormConstants.LAST_NAME, lastName, getTitle(wfmStrings.lastName(), true));
        addField(MIDDLE_NAME, middleName, getTitle(wfmStrings.middleName()));
        addField(BIRTH_DAY, dateOfBirth, getTitle(wfmStrings.dateOfBirth()));
        addField(GENDER, gender, getTitle(wfmStrings.gender()));
        addField(NATIONALITY, nationality, getTitle(wfmStrings.nationality()));
        addField(MARTIAL_STATUS, maritalStatus, getTitle(wfmStrings.maritalStatus()));
        addField(LANGUAGE, spokenLanguages, getTitle(wfmStrings.spokenLanguages()));
        addField(WPS_NO, wpsNumber, getTitle(wfmStrings.wpsNumber()));
        addTitleField(CONTACT_INFORMATION, Property.get(Constants.Contacts, wfmStrings.contactDetails(), wfmStrings.contact()));
        addField(CustomFormConstants.EMAIL, emails, getTitle(wfmStrings.email()));
        addField(CustomFormConstants.PHONE, phones, getTitle(wfmStrings.phone()));
//        addField(CustomFormConstants.EMAIL, email, getTitle(wfmStrings.email()));
//        addField(CustomFormConstants.PHONE, phone, getTitle(wfmStrings.phone()));
        addField(IM_ADDRESS, imAddress, getTitle(wfmStrings.imAddress()));
        addField(WEB_ADDRESS, webAddress, getTitle(wfmStrings.webAddress()));

        addTitleField(ADDRESS_INFORMATION, wfmStrings.additionalInformation());
        addField(ADDRESS_NAME, addressName, getTitle(wfmStrings.addressName()));
        addField(ADDRESS, addressLine, getTitle(wfmStrings.addressLine1()));
        addField(ADDRESS_2, addressLine2, getTitle(wfmStrings.addressLine2()));
        addField(ADDRESS_TYPE, addressType, getTitle(wfmStrings.addressType()));
        addField(COUNTRY, country, getTitle(wfmStrings.country()));
        addField(STATE, state, getTitle(wfmStrings.state()));
        addField(CITY, city, getTitle(wfmStrings.city()));
        addField(POST_CODE, postCode, getTitle(wfmStrings.postCode()));

        addTitleField(EMPLOYMENT_INFORMATION, wfmStrings.employmentInformation());
        addField(EMPLOYEE_CODE, employeeCode, getTitle(wfmStrings.employeeCode()));
        addField(CustomFormConstants.DEPARTMENT, departmentName, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
        addField(POSITION, position, getTitle(wfmStrings.position()));
        addField(LOCATION_FIELD, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())));
        addField(SUPERVISOR, supervisor, getTitle(wfmStrings.supervisor()));
        addField(WAGE_RATE, wageRate, getTitle(wfmStrings.wageRate()));
        addField(CLIENT_CHARGE_RATE, clientChargeRate, getTitle(wfmStrings.clientChargeRate()));
        addField(HIRE_DATE, hireDate, getTitle(wfmStrings.hireDate()));
        addField(RESIGNATION_DATE, resignationDate, getTitle(wfmStrings.resignationDate()));
        addField(EMPLOYMENT_CONTACT_TERMS, employmentContractTerms, getTitle(wfmStrings.employmentContractTerms()));
        addField(EMPLOYMENT_MODE, employmentMode, getTitle(wfmStrings.employmentMode()));
        addField(SALARY_GRADE, salaryGrade, getTitle(wfmStrings.salaryGrade()));
        addField(SALARY_AMOUNT, basicSalary, getTitle(wfmStrings.basicSalary()));
        addField(QUALIFICATION, qualification, getTitle(wfmStrings.qualification()));
        addField(COMPETENCIES, competencies, getTitle(wfmStrings.competencies()));
        addField(OPENING_BALANCE, openingBlanceDays, getTitle(wfmStrings.openingBalanceForAnnualLeave()));
        addField(PROBATION_DAYS, probationPeriodDays, getTitle(wfmStrings.probationPeriodDays()));

        addTitleField(BANK_ACCOUNT_INFORMATION, Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccountInformation(), wfmStrings.bankAccount()));
        addField(BANK_NAME, bankName, getTitle(wfmStrings.bankName()));
        addField(ACCOUNT_NUMBER, accountNumber, getTitle(wfmStrings.accountNumber()));
        addField(ACCOUNT_NAME, accountName, getTitle(wfmStrings.accountName()));
        addField(BANK_ADDRESS, bankAddress, getTitle(wfmStrings.bankAddress()));
        addField(SWIFT_CODE, swiftBICCode, getTitle(wfmStrings.swiftCode()));
        addField(SORT_CODE, sortCode, getTitle(wfmStrings.sortCode()));
        addField(IBAN_CODE, iBANNumber, getTitle(wfmStrings.ibanCode()));
        addField(AGENT_ID, agentID, getTitle(wfmStrings.agentID()));
//        if (maxNoAccessCount > 0) {
        addTitleField(ACCOUNT_INFORMATION, wfmStrings.basicInfo());
        addField(HAS_ACCESS, employeeType, getTitle(wfmStrings.employeeType()));
//        }


        addTitleField(PERSONAL_IDENTITY_INFORMATION, wfmStrings.personalIdentityInformation());
        addField(PASSPORT_NUMBER, passportNumber, getTitle(wfmStrings.passportNumber()));
        addField(PASSPORT_ISSUE, passportIssueBy, getTitle(wfmStrings.passportIssueBy()));
        addField(PASSPORT_ISSUE_DATE, passportIssueDate, getTitle(wfmStrings.passportIssueDate()));
        addField(PASSPORT_EXPIRY_DATE, passportExpiryDate, getTitle(wfmStrings.passportExpireDate()));
        addField(INSURANCE_NUMBER, insuranceNumber, getTitle(wfmStrings.insuranseNumber()));
        addField(INSURANCE_EXPIRY_DATE, insuranceExpiryDate, getTitle(wfmStrings.insuranceExpiryDate()));
        addField(VISA_NUMBER, visaNumber, getTitle(wfmStrings.visaNumber()));
        addField(VISA_ISSUE_DATE, visaIssueDate, getTitle(wfmStrings.visaIssueDate()));
        addField(VISA_EXPIRATION_DATE, visaExpiryDate, getTitle(wfmStrings.visaExpirationDate()));
        if (Utils.hasPermission(HRMS_PAYROLL_DEDUCTION_CATEGORIES)) {
            addTitleField(CustomFormConstants.PAYMENT_DEDUCTION_INFORMATION, wfmStrings.paymentDeductionCategoryTable());
            addField(PAYMENT_DEDUCTION, paymentsDeductions, wfmStrings.allowance());
        }
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.Employee;
    }

    @Override
    public void setItems(SelectItem[] items) {
        columnItems = items;
        salutation.setItems(items, "Salutation");
        firstName.setItems(items, wfmStrings.firstName());
        lastName.setItems(items, wfmStrings.lastName());
        middleName.setItems(items, wfmStrings.middleName());
        dateOfBirth.setItems(items, wfmStrings.dateOfBirth());
        gender.setItems(items, wfmStrings.gender());
        nationality.setItems(items, wfmStrings.nationality());
        maritalStatus.setItems(items, wfmStrings.maritalStatus());
        spokenLanguages.setItems(items, wfmStrings.spokenLanguages());
        competencies.setItems(items, wfmStrings.competencies());

        emails.removeAllRows();
        emails.addWidgets(getMultiWidgets(EMAILS));
        phones.removeAllRows();
        phones.addWidgets(getMultiWidgets(PHONES));
//        email.setItems(items, wfmStrings.email());
//        phone.setItems(items, wfmStrings.phone());
        imAddress.setItems(items, wfmStrings.imAddress());
        webAddress.setItems(items, wfmStrings.webAddress());

        addressName.setItems(items, wfmStrings.addressName());
        addressLine.setItems(items, wfmStrings.addressLine1());
        addressLine2.setItems(items, wfmStrings.addressLine2());
        addressType.setItems(items, wfmStrings.addressType());
        country.setItems(items, wfmStrings.country());
        state.setItems(items, wfmStrings.state());
        city.setItems(items, wfmStrings.city());
        postCode.setItems(items, wfmStrings.postCode());

        employeeCode.setItems(items, wfmStrings.employeeCode());
        departmentName.setItems(items, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
        position.setItems(items, wfmStrings.position());
        location.setItems(items, Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()));
        supervisor.setItems(items, wfmStrings.supervisor());
        wageRate.setItems(items, wfmStrings.wageRate());
        clientChargeRate.setItems(items, wfmStrings.clientChargeRate());
        hireDate.setItems(items, wfmStrings.hireDate());
        resignationDate.setItems(items, wfmStrings.resignationDate());
        employmentContractTerms.setItems(items, wfmStrings.employmentContractTerms());
        employmentMode.setItems(items, wfmStrings.employmentMode());
        salaryGrade.setItems(items, wfmStrings.salaryGrade());
        qualification.setItems(items, wfmStrings.qualification());
        basicSalary.setItems(items, wfmStrings.basicSalary());
        openingBlanceDays.setItems(items, wfmStrings.openingBalanceForAnnualLeave());
        probationPeriodDays.setItems(items, wfmStrings.probationPeriodDays());

        bankName.setItems(items, wfmStrings.bankName());
        accountNumber.setItems(items, wfmStrings.accountNumber());
        accountName.setItems(items, wfmStrings.accountName());
        bankAddress.setItems(items, wfmStrings.bankAddress());
        swiftBICCode.setItems(items, wfmStrings.swiftCode());
        sortCode.setItems(items, wfmStrings.sortCode());
        iBANNumber.setItems(items, wfmStrings.ibanCode());
        agentID.setItems(items, wfmStrings.agentID());

        passportNumber.setItems(items, wfmStrings.passportNumber());
        passportIssueBy.setItems(items, wfmStrings.passportIssueBy());
        passportIssueDate.setItems(items, wfmStrings.passportIssueDate());
        passportExpiryDate.setItems(items, wfmStrings.passportExpireDate());
        insuranceNumber.setItems(items, wfmStrings.insuranseNumber());
        insuranceExpiryDate.setItems(items, wfmStrings.insuranceExpiryDate());
        visaNumber.setItems(items, wfmStrings.visaNumber());
        visaIssueDate.setItems(items, wfmStrings.visaIssueDate());
        visaExpiryDate.setItems(items, wfmStrings.visaExpirationDate());
        wpsNumber.setItems(items, wfmStrings.wpsNumber());
        employeeType.setItems(items, wfmStrings.employeeType());

        setAllowanceDeductionColumnItems();

        if (tbValues != null) {
            for (DataListBox dataListBox : tbValues) {
                if (dataListBox != null) {
                    dataListBox.setItems(items);
                }
            }
        }
        LoadingPanel.loading(false);
    }

    private void setAllowanceDeductionColumnItems() {
        if (paymentsDeductions != null && paymentsDeductions.getRowCount() > 0) {
            for (int i = 0; i < paymentsDeductions.getRowCount(); i++) {
                DataListBox columns = (DataListBox) paymentsDeductions.getColumnById(i, "column");
                columns.setItems(columnItems);
            }
        }
    }

    @Override
    protected ImportFile getImportFile() {
        ImportFile importFile = createColumns(getRPC());
        importFile.setFileID(objectId);
        return importFile;
    }

    private ImportFile createColumns(EmployeeListItem item) {
        ImportFile importFile = new ImportFile();
        importFile.addColumn(ImportField.EmployeeField.EMPLOYEE_CODE, item.getEmployeeCode() != null ? item.getEmployeeCode() : -1);
        importFile.addColumn(ImportField.EmployeeField.SALUTATION, item.getSalutation() != null ? item.getSalutation() : -1);
        importFile.addColumn(ImportField.EmployeeField.FIRST_NAME, item.getFirstNameId() != null ? item.getFirstNameId() : -1);
        importFile.addColumn(ImportField.EmployeeField.LAST_NAME, item.getLastNameId() != null ? item.getLastNameId() : -1);
        importFile.addColumn(ImportField.EmployeeField.MIDDLE_NAME, item.getMiddleNameId() != null ? item.getMiddleNameId() : -1);
        importFile.addColumn(ImportField.EmployeeField.DATE_OF_BIRTH, item.getDateOfBirthId() != null ? item.getDateOfBirthId() : -1);
        importFile.addColumn(ImportField.EmployeeField.GENGER, item.getGenderId() != null ? item.getGenderId() : -1);
        importFile.addColumn(ImportField.EmployeeField.NATIONALITY, item.getNationality() != null ? item.getNationality() : -1);
        importFile.addColumn(ImportField.EmployeeField.MARTIAL_STATUS, item.getMaritalStatusId() != null ? item.getMaritalStatusId() : -1);
        importFile.addColumn(ImportField.EmployeeField.SPOKEN_LANGUAGES, item.getSpokenLanguagesId() != null ? item.getSpokenLanguagesId() : -1);

        if (item.getHomeEmail().size() > 0) {
            importFile.addExtraColumn(false, ImportField.ContactField.FIELD_EMAILS, ImportField.ContactField.FIELD_HOME_EMAILS, item.getHomeEmail().toArray(new String[]{}));
        }
        if (item.getWorkEmail().size() > 0) {
            importFile.addExtraColumn(false, ImportField.ContactField.FIELD_EMAILS, ImportField.ContactField.FIELD_WORK_EMAILS, item.getWorkEmail().toArray(new String[]{}));
        }
        if (item.getOtherEmail().size() > 0) {
            importFile.addExtraColumn(false, ImportField.ContactField.FIELD_EMAILS, ImportField.ContactField.FIELD_OTHER_EMAILS, item.getOtherEmail().toArray(new String[]{}));
        }
        //add Phones
        if (item.getHomePhone().size() > 0) {
            importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_HOME_PHONES, item.getHomePhone().toArray(new String[]{}));
        }
        if (item.getWorkPhone().size() > 0) {
            importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_WORK_PHONES, item.getWorkPhone().toArray(new String[]{}));
        }
        if (item.getMobile().size() > 0) {
            importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_MOBILE_PHONES, item.getMobile().toArray(new String[]{}));
        }
        if (item.getHomeFax().size() > 0) {
            importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_HOMEFAX_PHONES, item.getHomeFax().toArray(new String[]{}));
        }
        if (item.getWorkFax().size() > 0) {
            importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_WORKFAX_PHONES, item.getWorkFax().toArray(new String[]{}));
        }
        if (item.getPager().size() > 0) {
            importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_PAGER_PHONES, item.getPager().toArray(new String[]{}));
        }
        if (item.getOtherPhone().size() > 0) {
            importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_OTHER_PHONES, item.getOtherPhone().toArray(new String[]{}));
        }
        if (item.getExtension().size() > 0) {
            importFile.addExtraColumn(false, ImportField.ContactField.FIELD_PHONES, ImportField.ContactField.FIELD_EXTENSION, item.getExtension().toArray(new String[]{}));
        }
//        importFile.addColumn(ImportField.EmployeeField.EMAIL, item.getEmailId() != null ? item.getEmailId() : -1);
//        importFile.addColumn(ImportField.EmployeeField.PHONE_NUMBER, item.getPhoneNumberId() != null ? item.getPhoneNumberId() : -1);
        importFile.addColumn(ImportField.EmployeeField.IM_ADDRESS, item.getImAddressId() != null ? item.getImAddressId() : -1);
        importFile.addColumn(ImportField.EmployeeField.WEB_ADDRESS, item.getWebAddressId() != null ? item.getWebAddressId() : -1);

        importFile.addColumn(ImportField.EmployeeField.ADDRESS_NAME, item.getAddressNameId() != null ? item.getAddressNameId() : -1);
        importFile.addColumn(ImportField.EmployeeField.ADDRESS_LINE, item.getAddressLine() != null ? item.getAddressLine() : -1);
        importFile.addColumn(ImportField.EmployeeField.ADDRESS_LINE2, item.getAddressLine2() != null ? item.getAddressLine2() : -1);
        importFile.addColumn(ImportField.EmployeeField.ADDRESS_TYPE, item.getAddressType() != null ? item.getAddressType() : -1);
        importFile.addColumn(ImportField.EmployeeField.COUNTRY, item.getCountry() != null ? item.getCountry() : -1);
        importFile.addColumn(ImportField.EmployeeField.CITY, item.getCity() != null ? item.getCity() : -1);
        importFile.addColumn(ImportField.EmployeeField.STATE, item.getState() != null ? item.getState() : -1);
        importFile.addColumn(ImportField.EmployeeField.POST_CODE, item.getPostCode() != null ? item.getPostCode() : -1);

        importFile.addColumn(ImportField.EmployeeField.DEPARTMENT_NAME, item.getDepartmentId() != null ? item.getDepartmentId() : -1);
        importFile.addColumn(ImportField.EmployeeField.POSITION, item.getPositionId() != null ? item.getPositionId() : -1);
        importFile.addColumn(ImportField.EmployeeField.LOCATION, item.getLocationId() != null ? item.getLocationId() : -1);
        importFile.addColumn(ImportField.EmployeeField.SUPER_VISER, item.getSupervisor() != null ? item.getSupervisor() : -1);
        importFile.addColumn(ImportField.EmployeeField.WAGE_RATE, item.getWageRateId() != null ? item.getWageRateId() : -1);
        importFile.addColumn(ImportField.EmployeeField.CLIENT_CHARGE_RATE, item.getClientChargeRateId() != null ? item.getClientChargeRateId() : -1);
        importFile.addColumn(ImportField.EmployeeField.HIRE_DATE, item.getHireDate() != null ? item.getHireDate() : -1);
        importFile.addColumn(ImportField.EmployeeField.RESIGNATION_DATE, item.getResignationDate() != null ? item.getResignationDate() : -1);
        importFile.addColumn(ImportField.EmployeeField.EMPLOYMENT_CONTACT_TERMS, item.getEmploymentContractTermsID() != null ? item.getEmploymentContractTermsID() : -1);
        importFile.addColumn(ImportField.EmployeeField.EMPLOYMENT_MODE, item.getEmploymentModeId() != null ? item.getEmploymentModeId() : -1);
        importFile.addColumn(ImportField.EmployeeField.SALARY_GRADE, item.getSalaryGradeID() != null ? item.getSalaryGradeID() : -1);
        importFile.addColumn(ImportField.EmployeeField.BASIC_SALARY, item.getBasicSalary() != null ? item.getBasicSalary() : -1);
        importFile.addColumn(ImportField.EmployeeField.OPENING_BALANCE_DAYS, item.getOpeningBalanceDays() != null ? item.getOpeningBalanceDays() : -1);
        importFile.addColumn(ImportField.EmployeeField.PROBATION_PERIOD_DAYS, item.getProbationPeriodDays() != null ? item.getProbationPeriodDays() : -1);
        importFile.addColumn(ImportField.EmployeeField.QUALIFICATION, item.getQualificationId() != null ? item.getQualificationId() : -1);
        importFile.addColumn(ImportField.EmployeeField.COMPETENCIES, item.getCompetenciesId() != null ? item.getCompetenciesId() : -1);

        importFile.addColumn(ImportField.EmployeeField.BANK_NAME, item.getBankName() != null ? item.getBankName() : -1);
        importFile.addColumn(ImportField.EmployeeField.ACCOUNT_NUMBER, item.getAccountNumber() != null ? item.getAccountNumber() : -1);
        importFile.addColumn(ImportField.EmployeeField.ACCOUNT_NAME, item.getAccountName() != null ? item.getAccountName() : -1);
        importFile.addColumn(ImportField.EmployeeField.BANK_ADDRESS, item.getBankAddress() != null ? item.getBankAddress() : -1);
        importFile.addColumn(ImportField.EmployeeField.SWIFT_BCIC_CODE, item.getSwiftBICCode() != null ? item.getSwiftBICCode() : -1);
        importFile.addColumn(ImportField.EmployeeField.SORT_CODE, item.getSortCode() != null ? item.getSortCode() : -1);
        importFile.addColumn(ImportField.EmployeeField.IBAN_NUMBER, item.getiBANNumber() != null ? item.getiBANNumber() : -1);
        importFile.addColumn(ImportField.EmployeeField.AGENT_ID, item.getAgentId() != null ? item.getAgentId() : -1);

        importFile.addColumn(ImportField.EmployeeField.PASSPORT_NUMBER, item.getPassportNumber() != null ? item.getPassportNumber() : -1);
        importFile.addColumn(ImportField.EmployeeField.PASSPORT_ISSUE_BY, item.getPassportIssueById() != null ? item.getPassportIssueById() : -1);
        importFile.addColumn(ImportField.EmployeeField.PASSPORT_ISSUE_DATE, item.getPassportIssueDate() != null ? item.getPassportIssueDate() : -1);
        importFile.addColumn(ImportField.EmployeeField.PASSPORT_EXPIRY_DATE, item.getPassportExpiryDate() != null ? item.getPassportExpiryDate() : -1);
        importFile.addColumn(ImportField.EmployeeField.INSURANCE_NUMBER, item.getInsuranceNumber() != null ? item.getInsuranceNumber() : -1);
        importFile.addColumn(ImportField.EmployeeField.INSURANCE_EXPIRY_DATE, item.getInsuranceExpiryDateId() != null ? item.getInsuranceExpiryDateId() : -1);
        importFile.addColumn(ImportField.EmployeeField.VISA_NUMBER, item.getVisaNumber() != null ? item.getVisaNumber() : -1);
        importFile.addColumn(ImportField.EmployeeField.VISA_ISSUE_DATE, item.getVisaIssueDate() != null ? item.getVisaIssueDate() : -1);
        importFile.addColumn(ImportField.EmployeeField.VISA_EXPIRY_DATE, item.getVisaExpiryDate() != null ? item.getVisaExpiryDate() : -1);
        importFile.addColumn(ImportField.EmployeeField.WPS_NO, item.getWpsNumber() != null ? item.getWpsNumber() : -1);
        importFile.addColumn(ImportField.EmployeeField.HAS_ACCESS, item.getHasAccressId() != null ? item.getHasAccressId() : -1);
        importFile.addColumn(ImportField.EmployeeField.USER_ROLE, item.getRoleId() != null ? item.getRoleId() : -1);
        if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
            int s = ImportField.EmployeeField.FIELD_CUSTOM_FIELD_START_NUMBER;
            for (CompanyCustomFieldItem customField : item.getCustomFields()) {

                if (customField != null && customField.getFieldStringValue() != null && !"".equals(customField.getFieldStringValue()) && customField.getFieldStringValue().matches(Constants.REGEX_INTEGER)) {
                    Integer columnID = Integer.parseInt(customField.getFieldStringValue());
                    importFile.addExtraColumn(false,
                            s++,
                            columnID,
                            customField.getDataType(),
                            customField.getColumnCode(),
                            customField.getCustomFieldSettingID() != null ? customField.getCustomFieldSettingID().toString() : "-1",
                            customField.getUiType(),
                            customField.getPredefinedValues() != null ? String.join("-:-", customField.getPredefinedValues()) : null,
                            customField.getFieldName());
                } else {
                    importFile.addExtraColumn(false, s++, null);
                }
            }
        }
        if (item.getCategoryColumns() != null) {
            importFile.setCategoryColumns(importFile.toJson(item.getCategoryColumns()));
        }
        return importFile;
    }

    private EmployeeListItem getRPC() {
        EmployeeListItem item = new EmployeeListItem();
        item.setObjectID(objectId);
        item.setSalutation(getSelectedItem(salutation));
        item.setFirstNameId(getSelectedItem(firstName));
        item.setLastNameId(getSelectedItem(lastName));
        item.setMiddleNameId(getSelectedItem(middleName));
        item.setDateOfBirthId(getSelectedItem(dateOfBirth));
        item.setGenderId(getSelectedItem(gender));
        item.setNationality(getSelectedItem(nationality));
        item.setMaritalStatusId(getSelectedItem(maritalStatus));
        item.setSpokenLanguagesId(getSelectedItem(spokenLanguages));
        item.setCompetenciesId(getSelectedItem(competencies));

        setMultiResults(item, EMAILS);
        setMultiResults(item, PHONES);
//        item.setEmailId(getSelectedItem(email));
//        item.setPhoneNumberId(getSelectedItem(phone));
        item.setImAddressId(getSelectedItem(imAddress));
        item.setWebAddressId(getSelectedItem(webAddress));

        item.setAddressNameId(getSelectedItem(addressName));
        item.setAddressLine(getSelectedItem(addressLine));
        item.setAddressLine2(getSelectedItem(addressLine2));
        item.setAddressType(getSelectedItem(addressType));
        item.setCountry(getSelectedItem(country));
        item.setState(getSelectedItem(state));
        item.setCity(getSelectedItem(city));
        item.setPostCode(getSelectedItem(postCode));

        item.setEmployeeCode(getSelectedItem(employeeCode));
        item.setDepartmentId(getSelectedItem(departmentName));
        item.setPositionId(getSelectedItem(position));
        item.setLocationId(getSelectedItem(location));
        item.setSupervisor(getSelectedItem(supervisor));
        item.setWageRateId(getSelectedItem(wageRate));
        item.setClientChargeRateId(getSelectedItem(clientChargeRate));
        item.setHireDate(getSelectedItem(hireDate));
        item.setResignationDate(getSelectedItem(resignationDate));
        item.setEmploymentContractTermsID(getSelectedItem(employmentContractTerms));
        item.setEmploymentModeId(getSelectedItem(employmentMode));
        item.setSalaryGradeID(getSelectedItem(salaryGrade));
        item.setBasicSalary(getSelectedItem(basicSalary));
        item.setOpeningBalanceDays(getSelectedItem(openingBlanceDays));
        item.setProbationPeriodDays(getSelectedItem(probationPeriodDays));
        item.setQualificationId(getSelectedItem(qualification));

        item.setBankName(getSelectedItem(bankName));
        item.setAccountNumber(getSelectedItem(accountNumber));
        item.setAccountName(getSelectedItem(accountName));
        item.setBankAddress(getSelectedItem(bankAddress));
        item.setSwiftBICCode(getSelectedItem(swiftBICCode));
        item.setSortCode(getSelectedItem(sortCode));
        item.setiBANNumber(getSelectedItem(iBANNumber));
        item.setAgentId(getSelectedItem(agentID));

        item.setPassportNumber(getSelectedItem(passportNumber));
        item.setPassportIssueById(getSelectedItem(passportIssueBy));
        item.setPassportIssueDate(getSelectedItem(passportIssueDate));
        item.setPassportExpiryDate(getSelectedItem(passportExpiryDate));
        item.setInsuranceNumber(getSelectedItem(insuranceNumber));
        item.setInsuranceExpiryDateId(getSelectedItem(insuranceExpiryDate));
        item.setVisaNumber(getSelectedItem(visaNumber));
        item.setVisaIssueDate(getSelectedItem(visaIssueDate));
        item.setVisaExpiryDate(getSelectedItem(visaExpiryDate));
        item.setWpsNumber(getSelectedItem(wpsNumber));

        item.setHasAccressId(getSelectedItem(employeeType));


        if (tbValues != null && tbValues.length > 0) {
            ArrayList<CompanyCustomFieldItem> resultItemList = new ArrayList<>();
            for (int i = 0; i < tbValues.length; i++) {
                CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                resultItem.setObjectId(companyCustomFieldItems.get(i).getObjectId());
                resultItem.setDataType(companyCustomFieldItems.get(i).getDataType());
                resultItem.setUiType(companyCustomFieldItems.get(i).getUiType());
                resultItem.setColumnCode(companyCustomFieldItems.get(i).getColumnCode());
                resultItem.setCustomFieldSettingID(companyCustomFieldItems.get(i).getCustomFieldSettingID());
                resultItem.setPredefinedValues(companyCustomFieldItems.get(i).getPredefinedValues());
                resultItem.setFieldName(companyCustomFieldItems.get(i).getFieldName());

                if (tbValues[i].getSelectedItem() != null) {
                    resultItem.setFieldStringValue(tbValues[i].getSelectedItem().getId().toString());
                }
                resultItemList.add(resultItem);
            }
            item.setCustomFields(resultItemList);
        }
        item.setCategoryColumns(getCategoryColumns());
        return item;
    }

    private HashMap<Integer, Integer> getCategoryColumns() {
        HashMap<Integer, Integer> categoryColumn = null;
        if (paymentsDeductions != null && paymentsDeductions.getRowCount() > 0) {
            categoryColumn = new HashMap<>();
            for (int i = 0; i < paymentsDeductions.getRowCount(); i++) {
                DataListBox column = (DataListBox) paymentsDeductions.getColumnById(i, "column");
                CategoryLookUp category = (CategoryLookUp) paymentsDeductions.getColumnById(i, "category");
                Integer categoryId = category != null && category.getSelectedItem() != null ? category.getSelectedItem().getId() : null;
                Integer columnId = getSelectedItem(column);
                if (columnId != null && categoryId != null) {
                    categoryColumn.put(columnId, categoryId);
                }
            }
        }

        return categoryColumn;
    }

    @Override
    public boolean validate() {
        employeeType.removeStyleName(Constants.ERROR_FORM_STYLE);
        if (!employeeType.isSomethingSelected()) {
            employeeType.addStyleName(Constants.ERROR_FORM_STYLE);
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }
    private WidgetsMap getMultiWidgets(Integer type) {
        WidgetsMap widgetsMap = new WidgetsMap();
        final DataListBox paramListBox = new DataListBox();
        paramListBox.getElement().setId("param_list_box");
        paramListBox.addStyleName(DEFAULT_WIDTH);
        DataListBox relationListBox = new DataListBox();
        relationListBox.getElement().setId("relation_list_box");
        relationListBox.setWidth("130px");
        relationListBox.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        if (type.equals(EMAILS)) {
            relationListBox.setItems(AddContactView.getAddress());
        } else if (type.equals(PHONES)) {
            relationListBox.setItems(getPhoneNumber());
        }
        if (items != null) {
            if (type.equals(EMAILS)) {
                paramListBox.setItems(items, wfmStrings.email());
            } else if (type.equals(PHONES)) {
                paramListBox.setItems(items, wfmStrings.phone());
            }
        }
        widgetsMap.addWidgetToMap(PARAM_LIST_BOX, paramListBox);
        widgetsMap.addWidgetToMap(RELATION_LIST_BOX, relationListBox);
        widgetsMap.addWidgets(paramListBox, relationListBox);
        return widgetsMap;
    }

    private void setMultiResults(final EmployeeListItem item, Integer type) {
        ArrayList<HashMap<String, Widget>> list = null;
        if (EMAILS.equals(type)) {
            list = emails.getWidgets();
        } else if (PHONES.equals(type)) {
            list = phones.getWidgets();
        }
        if (list != null) {
            for (HashMap<String, Widget> row : list) {
                if (row != null) {
                    DataListBox param = (DataListBox) row.get(PARAM_LIST_BOX);
                    DataListBox relation = (DataListBox) row.get(RELATION_LIST_BOX);
                    if (EMAILS.equals(type)) {
                        setEmail(item, param, relation);
                    } else if (PHONES.equals(type)) {
                        setPhone(item, param, relation);
                    }
                }
            }
        }
    }


    private void setEmail(final EmployeeListItem item, DataListBox box, DataListBox type) {
        if (box.getSelectedItem() != null) {
            if (type.getSelectedItem() != null) {
                if (type.getSelectedId().equals(AddressReference.HOME.getId())) {
                    item.getHomeEmail().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(AddressReference.WORK.getId())) {
                    item.getWorkEmail().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(AddressReference.OTHER.getId())) {
                    item.getOtherEmail().add(getSelectedItemAsString(box));
                }
            } else {
                item.getHomeEmail().add(getSelectedItemAsString(box));
            }
        }
    }

    private void setPhone(final EmployeeListItem item, DataListBox box, DataListBox type) {
        if (box.getSelectedItem() != null) {
            if (type.getSelectedItem() != null) {
                if (type.getSelectedId().equals(PhoneReference.HOME.getId())) {
                    item.getHomePhone().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(PhoneReference.WORK.getId())) {
                    item.getWorkPhone().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(PhoneReference.MOBILE.getId())) {
                    item.getMobile().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(PhoneReference.HOMEFAX.getId())) {
                    item.getHomeFax().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(PhoneReference.WORKFAX.getId())) {
                    item.getWorkFax().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(PhoneReference.PAGER.getId())) {
                    item.getPager().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(PhoneReference.OTHER.getId())) {
                    item.getOtherPhone().add(getSelectedItemAsString(box));
                } else if (type.getSelectedId().equals(PhoneReference.EXTENSION.getId())) {
                    item.getExtension().add(getSelectedItemAsString(box));
                }
            } else {
                item.getHomePhone().add(getSelectedItemAsString(box));
            }
        }
    }

    public static SelectItem[] getPhoneNumber() {
        SelectItem[] items = new SelectItem[5];
        items[0] = new SelectItem(PhoneReference.WORK.getId(), wfmStrings.contactwork());
        items[1] = new SelectItem(PhoneReference.HOME.getId(), wfmStrings.contacthome());
        items[2] = new SelectItem(PhoneReference.MOBILE.getId(), wfmStrings.mobile());
//        items[3] = new SelectItem(PhoneReference.HOMEFAX.getId(), wfmStrings.homeFax());
//        items[4] = new SelectItem(PhoneReference.WORKFAX.getId(), wfmStrings.workFax());
//        items[3] = new SelectItem(PhoneReference.PAGER.getId(), wfmStrings.pager());
        items[3] = new SelectItem(PhoneReference.OTHER.getId(), wfmStrings.other());
        items[4] = new SelectItem(PhoneReference.EXTENSION.getId(), wfmStrings.extension());
        return items;
    }
    @Override
    protected ImportTypeEnum getType() {
        return ImportTypeEnum.EMPLOYEE;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.HRMS_EMPLOYEE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.IMPORT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }
}
