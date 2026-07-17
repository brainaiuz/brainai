package com.edatasite.workforce.gwt.core.client.rpc.employee;

import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * User: Admin
 * Date: 07.01.2008
 * Time: 15:54:13
 */

public class EmployeeListItem extends BaseListItem implements ListingCustomFields, IsSerializable {

    public static final String ACTION = "action";
    public static final String EMPLOYEE_NUMBER = "employeeNumber";
    public static final String EMPLOYEE_NAME = "EMPLOYEE_NAME";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String MIDDLE_NAME = "middleName";
    public static final String EMAIL = "email";
    public static final String COUNTRY = "country";
    public static final String STATE = "STATE";
    public static final String CITY = "CITY";
    public static final String POST_CODE = "POST_CODE";
    public static final String STREET = "STREET";
    public static final String STREET2 = "STREET2";
    public static final String JOB_TITLE = "JOB_TITLE";
    public static final String BIRH_DATE = "birthDate";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String STATUS = "status";
    public static final String ROLE = "role";
    public static final String POSITION = "position";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String LAST_UPDATE = "lastUpdate";
    public static final String LOCATION = "location";
    public static final String SUPERVISOR = "supervisor";
    public static final String QUALIFICATION = "qualification";
    public static final String MOBILE = "mobilePhone";
    public static final String DEPARTMENT = "department";
    public static final String SALARY_AMOUNT = "salaryAmount";
    public static final String WAGE_RATE = "wageRate";
    public static final String CLIENT_CHARGE_RATE = "clientChargeRate";
    public static final String DRIVER_ID = "driverNumber";
    public static final String PASSPORT_NUMBER = "passportNumberField";
    public static final String PASSPORT_ISSUE_DATE = "passportIssueDateField";
    public static final String PASSPORT_ISSUE_BY = "passportIssueNameField";
    public static final String PASSPORT_EXPIRE_DATE = "passportExpiryDateField";
    public static final String INSURANCE_NUMBER = "insuranceNumberField";
    public static final String INSURANCE_EXPIRY_DATE = "insurancExpireDate";
    public static final String VISA_NUMBER = "visaNumberField";
    public static final String VISA_ISSUE_DATE = "visaIssueDateField";
    public static final String VISA_EXPIRATION_DATE = "visaExpiryDateField";
    public static final String AGENT_ID = "agetnID";
    public static final String BANK_NAME = "bankName";
    public static final String ACCOUNT_NUMBER = "accountNumber";
    public static final String ACCOUNT_NAME = "accountName";
    public static final String BANK_ADDRESS = "bankAddress";
    public static final String POSITION_TYPE = "positionType";
    public static final String BANK_ACCOUNT = "bankAccount";
    public static final String SWIFT_CODE = "swiftCode";
    public static final String SORT_CODE = "sortCode";
    public static final String IBAN_CODE = "ibanCode";
    public static final String GENDER_NAME = "genderName";
    public static final String HIRE_DATE = "HIRE_DATE";
    public static final String PAYMENTS_TOTAL = "paymentsTotal";
    public static final String DEDUCTIONS_TOTAL = "deductionsTotal";
    public static final String LOANS_TOTAL = "loansTotal";
    public static final String CURRENCY = "currency";
    public static final String HASACCESS = "hasAccess";
    public static final String OPENING_BALANCE_DAYS = "opening_balance_day";
    public static final String PROBATION_DAYS = "probation_day";
    public static final String WPS_NUMBER = "wpsNumber";
    public static final String TOTAL_SALARY = "totalSalary";
    public static final String USERNAME = "username";
    public static final String TIMESLOT = "timeslot";

    private static final String WORK_EMAIL = "workEmail";
    private static final String HOME_EMAIL = "homeEmail";
    private static final String OTHER_EMAIL = "otherEmail";
    private static final String HOME_PHONE = "homePhone";
    private static final String WORK_PHONE = "workPhone";
    private static final String FAX = "fax";
    private static final String HOME_FAX = "homeFax";
    private static final String WORK_FAX = "workFax";
    private static final String EXTENSION = "extension";
    private static final String PAGER = "pager";
    private static final String OTHER_PHONE = "otherPhone";
    private static final String MOBILE1 = "mobile";
    private static final String WHATS_APP = "whatsApp";
    private static final String TELEGRAM = "telegram";
    private static final String VIBER = "viber";

    private Integer employeeTemplateID;
    private String employeeNumber;
    private Integer employeeCode;
    private Integer salutation;
    private Integer addressType;
    private Integer addressLine;
    private Integer addressLine2;
    private Integer city;
    private Integer country;
    private Integer state;
    private Integer postCode;
    private Integer basicSalary;
    private Integer openingBalanceDays, probationPeriodDays;
    private Integer transportation;
    private Integer housing;
    private Integer hireDate;
    private Integer resignationDate;
    private Integer nationality;
    private Integer passportNumber;
    private Integer passportIssueDate;
    private Integer passportExpiryDate;
    private Integer visaNumber;
    private Integer visaIssueDate;
    private Integer visaExpiryDate;
    private Integer insuranceNumber;
    private Integer accountNumber;
    private String accountNumberString;
    private Integer accountName;
    private HashMap<String, ArrayList<String>> stringListMap = new HashMap<>();
    private String accountNameString;
    private Integer bankAddress;
    private String bankAddressString;
    private Integer swiftBICCode;
    private String swiftBICCodeString;
    private Integer sortCode;
    private String sortCodeString;
    private Integer iBANNumber;
    private String iBANNumberString;
    private Integer supervisor;
    private SelectItem supervisorItem;
    private Integer bankName;
    private String bankNameString;
    private String fullName;
    private String firstName;
    private Integer firstNameId;
    private String middleName;
    private Integer middleNameId;
    private String lastName;
    private Integer lastNameId;
    private String email;
    private Integer emailId;
    private String position;
    private Integer positionId;
    private String role;
    private String roleCode;
    private Integer roleId;
    private DateNonConvertable birthDate;
    private DateNonConvertable startDate;
    private DateNonConvertable enddate;
    private Boolean active;
    private String department;
    private Integer departmentId;
    private String status;
    private String statusCode;
    private String landing;
    private String phoneNumber;
    private Integer phoneNumberId;
    private String lastUpdate;
    private String location;
    private Integer locationId;
    private Boolean creator;
    private String mobilePhone;
    private Double latitude;
    private Double longitude;
    private HashMap<String, Object> customFieldsMap;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private BigDecimal salaryAmount;
    private String driverNumber;
    //    private boolean currentEmployee = false;
    // new fields
    private String passportNumberField;
    private DateNonConvertable passportIssueDateField;
    private SelectItem[] passportIssueItemsField;
    private Integer passportIssueIDField;
    private String passportIssueNameField;
    private DateNonConvertable passportExpiryDateField;
    private DateNonConvertable medicalExpiryDateField;
    private String insuranceNumberField;
    private DateNonConvertable insuranceExpiryDate;
    private String visaNumberField;
    private DateNonConvertable visaIssueDateField;
    private DateNonConvertable visaExpiryDateField;
    private Integer dateOfBirthId;
    private Integer imAddressId;
    private Integer webAddressId;
    private Integer addressNameId;
    private Integer wageRateId;
    private Double wageRate;
    private Integer clientChargeRateId;
    private Double clientChargeRate;
    private Integer agentId;
    private Integer passportIssueById;
    private Integer insuranceExpiryDateId;
    private Integer genderId;
    private Integer maritalStatusId;
    private Integer spokenLanguagesId;
    private Integer competenciesId;
    private Integer employmentContractTermsID;
    private Integer employmentModeId;
    private Integer salaryGradeID;
    private Integer qualificationId;
    private String qualificationName;
    private Integer hasAccressId;
    private String agentName;
    private String skills;
    private Address primaryAddress;  //need for getting contact's single address
    private String genderName;
    private BigDecimal paymentsTotal;
    private BigDecimal deductionsTotal;
    private BigDecimal loansTotal;
    private CurrencyItem currency;
    private Integer wpsNumber;
    private String wpsNumberString;
    private HashMap<Integer, Integer> categoryColumns;
    private ArrayList<Integer> payrolBatchIDs;
    private String bankAccount;
    private Double openingBalanceDay;
    private BigDecimal totalSalary;
    private Integer contactID;
    private String contactName;
    private String asteriskUsername;

    private SelectItem[] emails;
    private SelectItem[] phoneNumbers;
    private Double probationDay;
    private SelectItem timeslot;
    private Date deptStartDate;
    private SelectItem positionType;

    private ArrayList<String> getStringList(String key) {
        stringListMap.computeIfAbsent(key, k -> new ArrayList<>());
        return stringListMap.get(key);
    }

    private void addStringList(String key, ArrayList<String> value) {
        stringListMap.put(key, value);
    }

    public SelectItem[] getEmails() {
        return emails;
    }

    public void setEmails(SelectItem[] emails) {
        this.emails = emails;
    }

    public SelectItem[] getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(SelectItem[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public EmployeeListItem() {
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }


    public ArrayList<String> getExtension() {
        return getStringList(EXTENSION);
    }

    public void setExtension(ArrayList<String> extension) {
        addStringList(EXTENSION, extension);
    }

    public ArrayList<String> getHomeFax() {
        return getStringList(HOME_FAX);
    }

    public void setHomeFax(ArrayList<String> homeFax) {
        addStringList(HOME_FAX, homeFax);
    }

    public void setHomeFax(String homeFax) {
        getHomeFax().add(homeFax);
    }

    public ArrayList<String> getWorkFax() {
        return getStringList(WORK_FAX);
    }

    public void setWorkFax(ArrayList<String> workFax) {
        addStringList(WORK_FAX, workFax);
    }

    public ArrayList<String> getHomeEmail() {
        return getStringList(HOME_EMAIL);
    }

    public void setHomeEmail(ArrayList<String> homeEmail) {
        addStringList(HOME_EMAIL, homeEmail);
    }

    public void setHomeEmail(String homeEmail) {
        getHomeEmail().add(homeEmail);
    }

    public ArrayList<String> getWorkEmail() {
        return getStringList(WORK_EMAIL);
    }

    public void setWorkEmail(ArrayList<String> workEmail) {
        addStringList(WORK_EMAIL, workEmail);
    }

    public ArrayList<String> getOtherEmail() {
        return getStringList(OTHER_EMAIL);
    }

    public void setOtherEmail(ArrayList<String> otherEmail) {
        addStringList(OTHER_EMAIL, otherEmail);
    }

    public ArrayList<String> getWorkPhone() {
        return getStringList(WORK_PHONE);
    }

    public void setWorkPhone(ArrayList<String> workPhone) {
        addStringList(WORK_PHONE, workPhone);
    }

    public void setWorkPhone(String workPhone) {
        getWorkPhone().add(workPhone);
    }

    public ArrayList<String> getHomePhone() {
        return getStringList(HOME_PHONE);
    }

    public void setHomePhone(ArrayList<String> homePhone) {
        addStringList(HOME_PHONE, homePhone);
    }

    public void setHomePhone(String homePhone) {
        getHomePhone().add(homePhone);
    }

    public ArrayList<String> getMobile() {
        return getStringList(MOBILE1);
    }

    public void setMobile(ArrayList<String> mobile) {
        addStringList(MOBILE1, mobile);
    }

    public void setMobile(String mobile) {
        getMobile().add(mobile);
    }

    public ArrayList<String> getPager() {
        return getStringList(PAGER);
    }

    public void setPager(ArrayList<String> pager) {
        addStringList(PAGER, pager);
    }

    public ArrayList<String> getOtherPhone() {
        return getStringList(OTHER_PHONE);
    }

    public void setOtherPhone(ArrayList<String> otherPhone) {
        addStringList(OTHER_PHONE, otherPhone);
    }

    public void setOtherPhone(String otherPhone) {
        getOtherPhone().add(otherPhone);
    }

    public ArrayList<String> getFax() {
        return getStringList(FAX);

    }

    public void setFax(ArrayList<String> fax) {
        addStringList(FAX, fax);
    }

    public void setFax(String fax) {
        getWhatsApp().add(fax);
    }

    public ArrayList<String> getWhatsApp() {
        return getStringList(WHATS_APP);
    }

    public void setWhatsApp(ArrayList<String> whatsApp) {
        addStringList(WHATS_APP, whatsApp);
    }

    public void setWhatsApp(String whatsApp) {
        getWhatsApp().add(whatsApp);
    }

    public ArrayList<String> getTelegram() {
        return getStringList(TELEGRAM);
    }

    public void setTelegram(ArrayList<String> telegram) {
        addStringList(TELEGRAM, telegram);
    }

    public void setTelegram(String telegram) {
        getTelegram().add(telegram);
    }


    public ArrayList<String> getViber() {
        return getStringList(VIBER);
    }

    public void setViber(ArrayList<String> viber) {
        addStringList(VIBER, viber);
    }

    public void setViber(String viber) {
        getViber().add(viber);
    }


    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public DateNonConvertable getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(DateNonConvertable birthDate) {
        this.birthDate = birthDate;
    }

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public DateNonConvertable getEnddate() {
        return enddate;
    }

    public void setEnddate(DateNonConvertable enddate) {
        this.enddate = enddate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getLanding() {
        return landing;
    }

    public void setLanding(String landing) {
        this.landing = landing;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getCreator() {
        return creator;
    }

    public void setCreator(Boolean creator) {
        this.creator = creator;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

//    public boolean isCurrentEmployee() {
//        return currentEmployee;
//    }
//
//    public void setCurrentEmployee(boolean is) {
//        this.currentEmployee = is;
//    }

    public HashMap<String, Object> getCustomFieldsMap() {
        if (customFieldsMap == null) {
            customFieldsMap = new HashMap<>();
        }
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFields) {
        this.customFieldsMap = customFields;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    @Override
    public Integer getRelationID() {
        return null;
    }

    @Override
    public String getRelationType() {
        return null;
    }

    @Override
    public String getRelationName() {
        return null;
    }

    public BigDecimal getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(BigDecimal salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public Integer getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(Integer employeeCode) {
        this.employeeCode = employeeCode;
    }

    public Integer getSalutation() {
        return salutation;
    }

    public void setSalutation(Integer salutation) {
        this.salutation = salutation;
    }

    public Integer getAddressType() {
        return addressType;
    }

    public void setAddressType(Integer addressType) {
        this.addressType = addressType;
    }

    public Integer getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(Integer addressLine) {
        this.addressLine = addressLine;
    }

    public Integer getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(Integer addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPostCode() {
        return postCode;
    }

    public void setPostCode(Integer postCode) {
        this.postCode = postCode;
    }

    public Integer getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Integer basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Integer getOpeningBalanceDays() {
        return openingBalanceDays;
    }

    public void setOpeningBalanceDays(Integer openingBalanceDays) {
        this.openingBalanceDays = openingBalanceDays;
    }

    public Integer getProbationPeriodDays() {
        return probationPeriodDays;
    }

    public void setProbationPeriodDays(Integer probationPeriodDays) {
        this.probationPeriodDays = probationPeriodDays;
    }

    public Integer getTransportation() {
        return transportation;
    }

    public void setTransportation(Integer transportation) {
        this.transportation = transportation;
    }

    public Integer getHousing() {
        return housing;
    }

    public void setHousing(Integer housing) {
        this.housing = housing;
    }

    public Integer getHireDate() {
        return hireDate;
    }

    public void setHireDate(Integer hireDate) {
        this.hireDate = hireDate;
    }

    public Integer getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(Integer resignationDate) {
        this.resignationDate = resignationDate;
    }

    public Integer getNationality() {
        return nationality;
    }

    public void setNationality(Integer nationality) {
        this.nationality = nationality;
    }

    public Integer getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(Integer passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Integer getPassportIssueDate() {
        return passportIssueDate;
    }

    public void setPassportIssueDate(Integer passportIssueDate) {
        this.passportIssueDate = passportIssueDate;
    }

    public Integer getPassportExpiryDate() {
        return passportExpiryDate;
    }

    public void setPassportExpiryDate(Integer passportExpiryDate) {
        this.passportExpiryDate = passportExpiryDate;
    }

    public Integer getVisaNumber() {
        return visaNumber;
    }

    public void setVisaNumber(Integer visaNumber) {
        this.visaNumber = visaNumber;
    }

    public Integer getVisaIssueDate() {
        return visaIssueDate;
    }

    public void setVisaIssueDate(Integer visaIssueDate) {
        this.visaIssueDate = visaIssueDate;
    }

    public Integer getVisaExpiryDate() {
        return visaExpiryDate;
    }

    public void setVisaExpiryDate(Integer visaExpiryDate) {
        this.visaExpiryDate = visaExpiryDate;
    }

    public Integer getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(Integer insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumberString() {
        return accountNumberString;
    }

    public void setAccountNumberString(String accountNumberString) {
        this.accountNumberString = accountNumberString;
    }

    public Integer getAccountName() {
        return accountName;
    }

    public void setAccountName(Integer accountName) {
        this.accountName = accountName;
    }

    public String getAccountNameString() {
        return accountNameString;
    }

    public void setAccountNameString(String accountNameString) {
        this.accountNameString = accountNameString;
    }

    public Integer getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(Integer bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankAddressString() {
        return bankAddressString;
    }

    public void setBankAddressString(String bankAddressString) {
        this.bankAddressString = bankAddressString;
    }

    public Integer getSwiftBICCode() {
        return swiftBICCode;
    }

    public void setSwiftBICCode(Integer swiftBICCode) {
        this.swiftBICCode = swiftBICCode;
    }

    public String getSwiftBICCodeString() {
        return swiftBICCodeString;
    }

    public void setSwiftBICCodeString(String swiftBICCodeString) {
        this.swiftBICCodeString = swiftBICCodeString;
    }

    public Integer getSortCode() {
        return sortCode;
    }

    public void setSortCode(Integer sortCode) {
        this.sortCode = sortCode;
    }

    public String getSortCodeString() {
        return sortCodeString;
    }

    public void setSortCodeString(String sortCodeString) {
        this.sortCodeString = sortCodeString;
    }

    public Integer getiBANNumber() {
        return iBANNumber;
    }

    public void setiBANNumber(Integer iBANNumber) {
        this.iBANNumber = iBANNumber;
    }

    public String getiBANNumberString() {
        return iBANNumberString;
    }

    public void setiBANNumberString(String iBANNumberString) {
        this.iBANNumberString = iBANNumberString;
    }

    public Integer getFirstNameId() {
        return firstNameId;
    }

    public void setFirstNameId(Integer firstNameId) {
        this.firstNameId = firstNameId;
    }

    public Integer getMiddleNameId() {
        return middleNameId;
    }

    public void setMiddleNameId(Integer middleNameId) {
        this.middleNameId = middleNameId;
    }

    public Integer getLastNameId() {
        return lastNameId;
    }

    public void setLastNameId(Integer lastNameId) {
        this.lastNameId = lastNameId;
    }

    public Integer getEmailId() {
        return emailId;
    }

    public void setEmailId(Integer emailId) {
        this.emailId = emailId;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPhoneNumberId() {
        return phoneNumberId;
    }

    public void setPhoneNumberId(Integer phoneNumberId) {
        this.phoneNumberId = phoneNumberId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Integer supervisor) {
        this.supervisor = supervisor;
    }

    public Integer getBankName() {
        return bankName;
    }

    public void setBankName(Integer bankName) {
        this.bankName = bankName;
    }

    public String getBankNameString() {
        return bankNameString;
    }

    public void setBankNameString(String bankNameString) {
        this.bankNameString = bankNameString;
    }

    public Integer getEmployeeTemplateID() {
        return employeeTemplateID;
    }

    public void setEmployeeTemplateID(Integer employeeTemplateID) {
        this.employeeTemplateID = employeeTemplateID;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public String getPassportNumberField() {
        return passportNumberField;
    }

    public void setPassportNumberField(String passportNumberField) {
        this.passportNumberField = passportNumberField;
    }

    public DateNonConvertable getPassportIssueDateField() {
        return passportIssueDateField;
    }

    public void setPassportIssueDateField(DateNonConvertable passportIssueDateField) {
        this.passportIssueDateField = passportIssueDateField;
    }

    public SelectItem[] getPassportIssueItemsField() {
        return passportIssueItemsField;
    }

    public void setPassportIssueItemsField(SelectItem[] passportIssueItemsField) {
        this.passportIssueItemsField = passportIssueItemsField;
    }

    public Integer getPassportIssueIDField() {
        return passportIssueIDField;
    }

    public void setPassportIssueIDField(Integer passportIssueIDField) {
        this.passportIssueIDField = passportIssueIDField;
    }

    public String getPassportIssueNameField() {
        return passportIssueNameField;
    }

    public void setPassportIssueNameField(String passportIssueNameField) {
        this.passportIssueNameField = passportIssueNameField;
    }

    public DateNonConvertable getPassportExpiryDateField() {
        return passportExpiryDateField;
    }

    public void setPassportExpiryDateField(DateNonConvertable passportExpiryDateField) {
        this.passportExpiryDateField = passportExpiryDateField;
    }

    public String getVisaNumberField() {
        return visaNumberField;
    }

    public void setVisaNumberField(String visaNumberField) {
        this.visaNumberField = visaNumberField;
    }

    public DateNonConvertable getVisaIssueDateField() {
        return visaIssueDateField;
    }

    public void setVisaIssueDateField(DateNonConvertable visaIssueDateField) {
        this.visaIssueDateField = visaIssueDateField;
    }

    public String getInsuranceNumberField() {
        return insuranceNumberField;
    }

    public void setInsuranceNumberField(String insuranceNumberField) {
        this.insuranceNumberField = insuranceNumberField;
    }

    public DateNonConvertable getInsuranceExpiryDate() {
        return insuranceExpiryDate;
    }

    public void setInsuranceExpiryDate(DateNonConvertable insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    public DateNonConvertable getVisaExpiryDateField() {
        return visaExpiryDateField;
    }

    public void setVisaExpiryDateField(DateNonConvertable visaExpiryDateField) {
        this.visaExpiryDateField = visaExpiryDateField;
    }

    public DateNonConvertable getMedicalExpiryDateField() {
        return medicalExpiryDateField;
    }

    public void setMedicalExpiryDateField(DateNonConvertable medicalExpiryDateField) {
        this.medicalExpiryDateField = medicalExpiryDateField;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
        if (customFields != null && customFields.size() > 0) {
            for (CompanyCustomFieldItem customField : customFields) {
                if ((customField.getFieldStringValue() != null && !"".equals(customField.getFieldStringValue())) || customField.getFieldDateNonConvertedValue() != null) {
                    if (customField.getColumnCode() != null) {
                        Object value = null;
                        if ((customField.getDataType().equals(CompanyCustomFieldItem.TEXT) || customField.getDataType().equals(CompanyCustomFieldItem.NUMBER)) && customField.getFieldStringValue() != null) {
                            try {
                                value = customField.getDataType().equals(CompanyCustomFieldItem.TEXT) ? customField.getFieldStringValue() : Double.valueOf(customField.getFieldStringValue());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (customField.getDataType().equals(CompanyCustomFieldItem.DATE) && customField.getFieldDateNonConvertedValue() != null) {
                                value = customField.getFieldDateNonConvertedValue().getNonConvertedDate();
                            }
                        }
                        if (value != null) {
                            getCustomFieldsMap().put(customField.getColumnCode(), value);
                        }
                    }
                }
            }
        }
    }

    public void setDateOfBirthId(Integer dateOfBirthId) {
        this.dateOfBirthId = dateOfBirthId;
    }

    public Integer getDateOfBirthId() {
        return dateOfBirthId;
    }

    public void setImAddressId(Integer imAddressId) {
        this.imAddressId = imAddressId;
    }

    public Integer getImAddressId() {
        return imAddressId;
    }

    public void setWebAddressId(Integer webAddressId) {
        this.webAddressId = webAddressId;
    }

    public Integer getWebAddressId() {
        return webAddressId;
    }

    public void setAddressNameId(Integer addressNameId) {
        this.addressNameId = addressNameId;
    }

    public Integer getAddressNameId() {
        return addressNameId;
    }

    public void setWageRateId(Integer wageRateId) {
        this.wageRateId = wageRateId;
    }

    public Integer getWageRateId() {
        return wageRateId;
    }

    public void setClientChargeRateId(Integer clientChargeRateId) {
        this.clientChargeRateId = clientChargeRateId;
    }

    public Integer getClientChargeRateId() {
        return clientChargeRateId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setPassportIssueById(Integer passportIssueById) {
        this.passportIssueById = passportIssueById;
    }

    public Integer getPassportIssueById() {
        return passportIssueById;
    }

    public void setInsuranceExpiryDateId(Integer insuranceExpiryDateId) {
        this.insuranceExpiryDateId = insuranceExpiryDateId;
    }

    public Integer getInsuranceExpiryDateId() {
        return insuranceExpiryDateId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public void setMaritalStatusId(Integer maritalStatusId) {
        this.maritalStatusId = maritalStatusId;
    }

    public Integer getMaritalStatusId() {
        return maritalStatusId;
    }

    public void setSpokenLanguagesId(Integer spokenLanguagesId) {
        this.spokenLanguagesId = spokenLanguagesId;
    }

    public Integer getSpokenLanguagesId() {
        return spokenLanguagesId;
    }

    public void setEmploymentModeId(Integer employmentModeId) {
        this.employmentModeId = employmentModeId;
    }

    public Integer getEmploymentModeId() {
        return employmentModeId;
    }

    public void setQualificationId(Integer qualificationId) {
        this.qualificationId = qualificationId;
    }

    public Integer getQualificationId() {
        return qualificationId;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }

    public void setHasAccressId(Integer hasAccressId) {
        this.hasAccressId = hasAccressId;
    }

    public Integer getHasAccressId() {
        return hasAccressId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Double getClientChargeRate() {
        return clientChargeRate;
    }

    public void setClientChargeRate(Double clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
    }

    public Double getWageRate() {
        return wageRate;
    }

    public void setWageRate(Double wageRate) {
        this.wageRate = wageRate;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getPrimaryAddress() {
        return getPrimaryAddress(false);
    }

    public Address getPrimaryAddress(boolean doNotReturnNull) {
        if (primaryAddress == null && doNotReturnNull) {
            return new Address();
        }
        return primaryAddress;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getGenderName() {
        return genderName;
    }

    public SelectItem getSupervisorItem() {
        return supervisorItem;
    }

    public void setSupervisorItem(SelectItem supervisorItem) {
        this.supervisorItem = supervisorItem;
    }

    public BigDecimal getPaymentsTotal() {
        return paymentsTotal;
    }

    public void setPaymentsTotal(BigDecimal paymentsTotal) {
        this.paymentsTotal = paymentsTotal;
    }

    public BigDecimal getDeductionsTotal() {
        return deductionsTotal;
    }

    public void setDeductionsTotal(BigDecimal deductionsTotal) {
        this.deductionsTotal = deductionsTotal;
    }

    public BigDecimal getLoansTotal() {
        return loansTotal;
    }

    public void setLoansTotal(BigDecimal loansTotal) {
        this.loansTotal = loansTotal;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public Integer getWpsNumber() {
        return wpsNumber;
    }

    public void setWpsNumber(Integer wpsNumber) {
        this.wpsNumber = wpsNumber;
    }

    public String getWpsNumberString() {
        return wpsNumberString;
    }

    public void setWpsNumberString(String wpsNumberString) {
        this.wpsNumberString = wpsNumberString;
    }

    public HashMap<Integer, Integer> getCategoryColumns() {
        return categoryColumns;
    }

    public void setCategoryColumns(HashMap<Integer, Integer> categoryColumns) {
        this.categoryColumns = categoryColumns;
    }

    public Integer getEmploymentContractTermsID() {
        return employmentContractTermsID;
    }

    public void setEmploymentContractTermsID(Integer employmentContractTermsID) {
        this.employmentContractTermsID = employmentContractTermsID;
    }

    public Integer getSalaryGradeID() {
        return salaryGradeID;
    }

    public void setSalaryGradeID(Integer salaryGradeID) {
        this.salaryGradeID = salaryGradeID;
    }

    public ArrayList<Integer> getPayrolBatchIDs() {
        if (payrolBatchIDs == null) {
            payrolBatchIDs = new ArrayList<>();
        }
        return payrolBatchIDs;
    }

    public void setPayrolBatchIDs(ArrayList<Integer> payrolBatchIDs) {
        this.payrolBatchIDs = payrolBatchIDs;
    }

    public Double getOpeningBalanceDay() {
        return openingBalanceDay;
    }

    public void setOpeningBalanceDay(Double openingBalanceDay) {
        this.openingBalanceDay = openingBalanceDay;
    }

    public Double getProbationDay() {
        return probationDay;
    }

    public void setProbationDay(Double probationDay) {
        this.probationDay = probationDay;
    }

    public Integer getCompetenciesId() {
        return competenciesId;
    }

    public void setCompetenciesId(Integer competenciesId) {
        this.competenciesId = competenciesId;
    }

    public BigDecimal getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
    }

    public Integer getContactID() {
        return this.contactID;
    }

    public void setContactID(final Integer contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(final String contactName) {
        this.contactName = contactName;
    }

    public String getAsteriskUsername() {
        return asteriskUsername;
    }

    public void setAsteriskUsername(String asteriskUsername) {
        this.asteriskUsername = asteriskUsername;
    }

    public void setEmails(ArrayList<String>... emails) {
        this.setHomeEmail(emails != null && emails.length > 0 ? emails[0] : new ArrayList<>());
        this.setWorkEmail(emails != null && emails.length > 0 ? emails[0] : new ArrayList<>());
        this.setOtherEmail(emails != null && emails.length > 0 ? emails[0] : new ArrayList<>());
    }

    public void setPhones(ArrayList<String>... phones) {
        this.setHomePhone(phones != null && phones.length > 0 ? phones[0] : new ArrayList<>());
        this.setWorkPhone(phones != null && phones.length > 0 ? phones[0] : new ArrayList<>());
        this.setMobile(phones != null && phones.length > 0 ? phones[0] : new ArrayList<>());
        this.setFax(phones != null && phones.length > 0 ? phones[0] : new ArrayList<>());
        this.setWhatsApp(phones != null && phones.length > 0 ? phones[0] : new ArrayList<>());
        this.setTelegram(phones != null && phones.length > 0 ? phones[0] : new ArrayList<>());
        this.setViber(phones != null && phones.length > 0 ? phones[0] : new ArrayList<>());
    }

    public void addParam(Integer param, Integer relation, String value, String... code) {
        if (param != null && relation != null && value != null && !"".equals(value.trim())) {
            if (param.equals(Constants.CONTACT_PHONES)) {
                addPhone(relation, value);
            } else if (param.equals(Constants.CONTACT_EMAILS)) {
                addEmail(relation, value);
            }
        }
    }

    public void addEmail(Integer relation, String value) {
        if (relation == AddressReference.HOME.getId()) {
            getHomeEmail().add(value);
        } else {
            if (relation == AddressReference.WORK.getId()) {
                getWorkEmail().add(value);
            } else {
                if (relation == AddressReference.OTHER.getId()) {
                    getOtherEmail().add(value);
                }
            }
        }
    }

    public void addPhone(Integer relation, String value) {
        switch (relation) {
            case Constants.G_WORK:
                getWorkPhone().add(value);
                break;
            case Constants.G_HOME:
                getHomePhone().add(value);
                break;
            case Constants.G_MOBILE:
                getMobile().add(value);
                break;
            case Constants.G_FAX:
                getFax().add(value);
                break;
            case Constants.G_WHATS_APP:
                getWhatsApp().add(value);
                break;
            case Constants.G_TELEGRAM:
                getTelegram().add(value);
                break;
            case Constants.G_VIBER:
                getViber().add(value);
                break;
            case Constants.G_HOME_FAX:
                getHomeFax().add(value);
                break;
            case Constants.G_WORK_FAX:
                getWorkFax().add(value);
                break;
            case Constants.G_PAGER:
                getPager().add(value);
                break;
            case Constants.G_OTHER:
                getOtherPhone().add(value);
                break;
            case Constants.G_EXTENSION:
                getExtension().add(value);
                break;
        }
    }

    public HashSet<String> getAllPhones() {
        HashSet<String> allPhones = new HashSet<>();
        allPhones.addAll(getHomePhone());
        allPhones.addAll(getWorkPhone());
        allPhones.addAll(getMobile());
        allPhones.addAll(getHomeFax());
        allPhones.addAll(getWorkFax());
        allPhones.addAll(getPager());
        allPhones.addAll(getOtherPhone());
        allPhones.addAll(getWhatsApp());
        allPhones.addAll(getTelegram());
        allPhones.addAll(getViber());
        allPhones.addAll(getFax());
        return allPhones;
    }

    public SelectItem getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(SelectItem timeslot) {
        this.timeslot = timeslot;
    }

    public Date getDeptStartDate() {
        return deptStartDate;
    }

    public void setDeptStartDate(Date deptStartDate) {
        this.deptStartDate = deptStartDate;
    }

    public SelectItem getPositionType() {
        return positionType;
    }

    public void setPositionType(SelectItem positionType) {
        this.positionType = positionType;
    }
}
