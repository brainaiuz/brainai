package com.edatasite.workforce.gwt.newemployee.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ExperienceTableItems;
import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SpokenLanguageItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserBankAccountData;
import com.edatasite.workforce.gwt.core.client.rpc.UserGrant;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewEmployee implements IsSerializable, UserGrant {

    private Integer objectID;
    private Integer employeeTemplateID;
    private String quickbookEmployeeID;
    private String quickbookEditSequence;
    private Long driverNumber;
    private String fname;
    private String mname;
    private String lname;
    private String email;
    private String homeAddress;
    private String cityTown;
    private Integer country;
    private Integer region;
    private String postCode;
    private String hphone;
    private String mphone;
    private String wphone;
    private DateNonConvertable startDate;
    private DateNonConvertable endDate;
    private DateNonConvertable birthDate;
    private Integer department;
    private String position;
    private Integer positionId;
    private Double wageRate;
    private Double clientChargeRate;
    private Integer role;
    private String roleName;
    private Integer[] roleId;
    private String createdFrom = "";
    private Integer[] projects;
    private Integer projectID;
    private Integer existingContactID;
    private boolean isOneOff = false;
    private int permission;
    private boolean check;
    private Integer locationId;
    private String password;
    private String gender;
    private Integer companyID;
    private DateNonConvertable prevEndDate;
    private DateNonConvertable startDateForOnlyPayroll;

    private Boolean active;

    private String organizationName;
    private Integer photoID;
    private String photoURL;
    private RegistrationTypeEnum registrationType;
    private String socialUserName;
    private Boolean hasAccess = Boolean.TRUE;
    private Boolean essUser = Boolean.FALSE;
    private NumberData numberData;
    private String empCode;
    private DateNonConvertable resignationDate;
    //this for import employee from quickbook
    /*private Date creationTime;
    private Date lastUpdatedTime;*/
    private ArrayList<PaymentDeductionObject> payments;
    private ArrayList<PaymentDeductionObject> deductions;
    private ArrayList<PaymentDeductionObject> taxes;
    private ArrayList<PaymentDeductionObject> loans;
    private ArrayList<PaymentDeductionObject> employerContributions;
    private ArrayList<Integer> deletedCategories;
    private ArrayList<Integer> inactiveCategories;
    private String otherName;
    private String title;
    private String driverID;
    private String nationality;
    private Integer martialStatusId;
    private SelectItem[] spokenLanguages;
    private Integer qualificationID;
    private Integer statusId;
    private Integer reportsToId;
    private Integer termsOfContract;
    private Integer termsOfCMonthORYear;
    private Integer empModeId;
    private Integer salaryGradeId;
    private Double salaryAmount;
    private Integer jobTitleId;
    private String jobTitle;
    private DateNonConvertable visaExpirationDate;
    private DateNonConvertable fireDate;
    private DateNonConvertable hireDate;
    private DateNonConvertable passportIssueDate;
    private DateNonConvertable passportExpiryDate;
    private DateNonConvertable medicalInsuranceExpireDate;
    private DateNonConvertable visaIssueDate;
    private ArrayList<CalendarEventReminder> visaExpirationDateReminder;
    private boolean applyPositionLeaveForEmployee;
    private UserBankAccountData bankAccountData;
    private ArrayList<SelectItem> coursesItems;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private String passportNumber;
    private String visaNumber;
    private String insuranceNumber;
    private String paymentMethod;
    private Integer passportIssueID;
    private Integer placementId;
    private ContactListItem contactListItem;
    private FileItem[] attachments;
    private boolean isAddSingleEmployee;
    private boolean fromEmployeeImport;
    private ArrayList<KpiTreeInfo> employeeCompetencies;
    private SelectItem approver;
    private SelectItem sender;
    private String status;
    private String rejectionNote;
    private boolean isFromMultiEmployee;
    private boolean isFromCandidate;
    private CurrencyItem salaryCurrency;
    private SelectItem payMethod;
    private SelectItem citizenship;
    private HashMap<String, String> payrollSettings = new HashMap<>();
    private Double openingBalanceDays;
    private Double probationDays;
    private Integer importFileID;
    private ArrayList<SpokenLanguageItem> spokingLanguages;
    private ReferenceItem employeeDegree;
    private HashMap<String, ArrayList<CustomTableRpc>> customTableItems = new HashMap<>();
    private SelectItem timeslot;

    private String salaryMode;
    private ExperienceTableItems[] experienceTableItems;
    private List<String> fingerprintDeviceUuids = new ArrayList<>();
    private Integer[] fingerprintDeviceId;

    public NewEmployee() {
    }

    public NewEmployee(Integer objectID, Integer department, String mainRoleName, boolean check) {
        this.objectID = objectID;
        this.department = department;
        this.roleName = mainRoleName;
        this.check = check;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public boolean isOneOff() {
        return isOneOff;
    }

    public void setOneOff(boolean oneOff) {
        isOneOff = oneOff;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHphone() {
        return hphone;
    }

    public void setHphone(String hphone) {
        this.hphone = hphone;
    }

    public String getMphone() {
        return mphone;
    }

    public void setMphone(String mphone) {
        this.mphone = mphone;
    }

    public String getWphone() {
        return wphone;
    }

    public void setWphone(String wphone) {
        this.wphone = wphone;
    }

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer[] getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer[] roleId) {
        this.roleId = roleId;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Integer[] getProjects() {
        return projects;
    }

    public void setProjects(Integer[] projects) {
        this.projects = projects;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getExistingContactID() {
        return existingContactID;
    }

    public void setExistingContactID(Integer existingContactID) {
        this.existingContactID = existingContactID;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getEmployeeTemplateID() {
        return employeeTemplateID;
    }

    public void setEmployeeTemplateID(Integer employeeTemplateID) {
        this.employeeTemplateID = employeeTemplateID;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getCityTown() {
        return cityTown;
    }

    public void setCityTown(String cityTown) {
        this.cityTown = cityTown;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Double getWageRate() {
        return wageRate != null ? wageRate : Double.valueOf(0.0);
    }

    public void setWageRate(Double wageRate) {
        this.wageRate = wageRate;
    }

    public Double getClientChargeRate() {
        return clientChargeRate != null ? clientChargeRate : Double.valueOf(0.0);
    }

    public void setClientChargeRate(Double clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
    }

    public DateNonConvertable getEndDate() {
        return endDate;
    }

    public void setEndDate(DateNonConvertable endDate) {
        this.endDate = endDate;
    }

    public String getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
    }

    public DateNonConvertable getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(DateNonConvertable birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getQuickbookEmployeeID() {
        return quickbookEmployeeID;
    }

    public void setQuickbookEmployeeID(String quickbookEmployeeID) {
        this.quickbookEmployeeID = quickbookEmployeeID;
    }

    public String getQuickbookEditSequence() {
        return quickbookEditSequence;
    }

    public void setQuickbookEditSequence(String quickbookEditSequence) {
        this.quickbookEditSequence = quickbookEditSequence;
    }

    /*    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }*/

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public DateNonConvertable getPrevEndDate() {
        return prevEndDate;
    }

    public void setPrevEndDate(DateNonConvertable prevEndDate) {
        this.prevEndDate = prevEndDate;
    }

    public DateNonConvertable getStartDateForOnlyPayroll() {
        return startDateForOnlyPayroll;
    }

    public void setStartDateForOnlyPayroll(DateNonConvertable startDateForOnlyPayroll) {
        this.startDateForOnlyPayroll = startDateForOnlyPayroll;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getFullName() {
        return getFname() + " " + getLname();
    }

    public Integer getPhotoID() {
        return photoID;
    }

    public void setPhotoID(Integer photoID) {
        this.photoID = photoID;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean hasAccess() {
        return hasAccess != null ? hasAccess : Boolean.TRUE;
    }

    public void setHasAccess(Boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public Boolean isEssUser() {
        return essUser;
    }

    public void setEssUser(Boolean essUser) {
        this.essUser = essUser;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public DateNonConvertable getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(DateNonConvertable resignationDate) {
        this.resignationDate = resignationDate;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public ArrayList<PaymentDeductionObject> getPayments() {
        if (payments == null) {
            payments = new ArrayList<>();
        }
        return payments;
    }

    public void setPayments(ArrayList<PaymentDeductionObject> payments) {
        this.payments = payments;
    }

    public ArrayList<PaymentDeductionObject> getDeductions() {
        if (deductions == null) {
            deductions = new ArrayList<>();
        }
        return deductions;
    }

    public void setDeductions(ArrayList<PaymentDeductionObject> deductions) {
        this.deductions = deductions;
    }

    public ArrayList<PaymentDeductionObject> getTaxes() {
        if (taxes == null) {
            taxes = new ArrayList<>();
        }
        return taxes;
    }

    public void setTaxes(ArrayList<PaymentDeductionObject> taxes) {
        this.taxes = taxes;
    }

    public ArrayList<PaymentDeductionObject> getLoans() {
        if (loans == null) {
            loans = new ArrayList<>();
        }
        return loans;
    }

    public void setLoans(ArrayList<PaymentDeductionObject> loans) {
        this.loans = loans;
    }

    public Long getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(Long driverNumber) {
        this.driverNumber = driverNumber;
    }

    public ArrayList<Integer> getDeletedCategories() {
        return deletedCategories;
    }

    public void setDeletedCategories(ArrayList<Integer> deletedCategories) {
        this.deletedCategories = deletedCategories;
    }

    public ArrayList<Integer> getInactiveCategories() {
        return inactiveCategories;
    }

    public void setInactiveCategories(ArrayList<Integer> inactiveCategories) {
        this.inactiveCategories = inactiveCategories;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNationality() {
        return nationality;
    }

    public void setMartialStatusId(Integer martialStatusId) {
        this.martialStatusId = martialStatusId;
    }

    public Integer getMartialStatusId() {
        return martialStatusId;
    }

    public void setSpokenLanguages(SelectItem[] spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public SelectItem[] getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setQualificationID(Integer qualificationID) {
        this.qualificationID = qualificationID;
    }

    public Integer getQualificationID() {
        return qualificationID;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setFireDate(DateNonConvertable fireDate) {
        this.fireDate = fireDate;
    }

    public DateNonConvertable getFireDate() {
        return fireDate;
    }

    public void setHireDate(DateNonConvertable hireDate) {
        this.hireDate = hireDate;
    }

    public DateNonConvertable getHireDate() {
        return hireDate;
    }

    public Integer getReportsToId() {
        return reportsToId;
    }

    public void setReportsToId(Integer reportsToId) {
        this.reportsToId = reportsToId;
    }

    public void setTermsOfContract(Integer termsOfContract) {
        this.termsOfContract = termsOfContract;
    }

    public Integer getTermsOfContract() {
        return termsOfContract;
    }

    public void setTermsOfCMonthORYear(Integer termsOfCMonthORYear) {
        this.termsOfCMonthORYear = termsOfCMonthORYear;
    }

    public Integer getTermsOfCMonthORYear() {
        return termsOfCMonthORYear;
    }

    public Integer getEmpModeId() {
        return empModeId;
    }

    public void setEmpModeId(Integer empModeId) {
        this.empModeId = empModeId;
    }

    public void setSalaryGradeId(Integer salaryGradeId) {
        this.salaryGradeId = salaryGradeId;
    }

    public Integer getSalaryGradeId() {
        return salaryGradeId;
    }

    public void setSalaryAmount(Double salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public Double getSalaryAmount() {
        return salaryAmount;
    }

    public void setJobTitleId(Integer jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public Integer getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setVisaExpirationDate(DateNonConvertable visaExpirationDate) {
        this.visaExpirationDate = visaExpirationDate;
    }

    public DateNonConvertable getVisaExpirationDate() {
        return visaExpirationDate;
    }

    public void setVisaExpirationDateReminder(ArrayList<CalendarEventReminder> visaExpirationDateReminder) {
        this.visaExpirationDateReminder = visaExpirationDateReminder;
    }

    public ArrayList<CalendarEventReminder> getVisaExpirationDateReminder() {
        return visaExpirationDateReminder;
    }

    public void setApplyPositionLeaveForEmployee(boolean applyPositionLeaveForEmployee) {
        this.applyPositionLeaveForEmployee = applyPositionLeaveForEmployee;
    }

    public boolean isApplyPositionLeaveForEmployee() {
        return applyPositionLeaveForEmployee;
    }

    public void setBankAccountData(UserBankAccountData bankAccountData) {
        this.bankAccountData = bankAccountData;
    }

    public UserBankAccountData getBankAccountData() {
        return bankAccountData;
    }

    public void setCoursesItems(ArrayList<SelectItem> coursesItems) {
        this.coursesItems = coursesItems;
    }

    public ArrayList<SelectItem> getCoursesItems() {
        return coursesItems;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportIssueDate(DateNonConvertable passportIssueDate) {
        this.passportIssueDate = passportIssueDate;
    }

    public DateNonConvertable getPassportIssueDate() {
        return passportIssueDate;
    }

    public void setPassportExpiryDate(DateNonConvertable passportExpiryDate) {
        this.passportExpiryDate = passportExpiryDate;
    }

    public DateNonConvertable getPassportExpiryDate() {
        return passportExpiryDate;
    }

    public void setMedicalInsuranceExpireDate(DateNonConvertable medicalInsuranceExpireDate) {
        this.medicalInsuranceExpireDate = medicalInsuranceExpireDate;
    }

    public DateNonConvertable getMedicalInsuranceExpireDate() {
        return medicalInsuranceExpireDate;
    }

    public void setVisaNumber(String visaNumber) {
        this.visaNumber = visaNumber;
    }

    public String getVisaNumber() {
        return visaNumber;
    }

    public void setVisaIssueDate(DateNonConvertable visaIssueDate) {
        this.visaIssueDate = visaIssueDate;
    }

    public DateNonConvertable getVisaIssueDate() {
        return visaIssueDate;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPassportIssueID(Integer passportIssueID) {
        this.passportIssueID = passportIssueID;
    }

    public Integer getPassportIssueID() {
        return passportIssueID;
    }


    public Integer getPlacementId() {
        return this.placementId;
    }

    public void setPlacementId(final Integer placementId) {
        this.placementId = placementId;
    }

    public void setContactListItem(ContactListItem contactListItem) {
        this.contactListItem = contactListItem;
    }

    public ContactListItem getContactListItem() {
        return contactListItem;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public boolean isAddSingleEmployee() {
        return isAddSingleEmployee;
    }

    public void setAddSingleEmployee(boolean isAddSingleEmployee) {
        this.isAddSingleEmployee = isAddSingleEmployee;
    }

    public boolean isFromEmployeeImport() {
        return fromEmployeeImport;
    }

    public void setFromEmployeeImport(boolean fromEmployeeImport) {
        this.fromEmployeeImport = fromEmployeeImport;
    }

    public void setEmployeeCompetencies(ArrayList<KpiTreeInfo> employeeCompetencies) {
        this.employeeCompetencies = employeeCompetencies;
    }

    public ArrayList<KpiTreeInfo> getEmployeeCompetencies() {
        return employeeCompetencies;
    }

    public SelectItem getApprover() {
        return approver;
    }

    public void setApprover(SelectItem approver) {
        this.approver = approver;
    }

    public SelectItem getSender() {
        return sender;
    }

    public void setSender(SelectItem sender) {
        this.sender = sender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectionNote() {
        return rejectionNote;
    }

    public void setRejectionNote(String rejectionNote) {
        this.rejectionNote = rejectionNote;
    }

    public boolean isFromMultiEmployee() {
        return isFromMultiEmployee;
    }

    public void setIsFromMultiEmployee(boolean isFromMultiEmployee) {
        this.isFromMultiEmployee = isFromMultiEmployee;
    }

    public boolean isFromCandidate() {
        return isFromCandidate;
    }

    public void setIsFromCandidate(boolean isFromCandidate) {
        this.isFromCandidate = isFromCandidate;
    }

    public CurrencyItem getSalaryCurrency() {
        return salaryCurrency;
    }

    public void setSalaryCurrency(CurrencyItem salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }

    public SelectItem getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(SelectItem payMethod) {
        this.payMethod = payMethod;
    }

    public HashMap<String, String> getPayrollSettings() {
        return payrollSettings;
    }

    public void setPayrollSettings(HashMap<String, String> payrollSettings) {
        this.payrollSettings = payrollSettings;
    }

    public SelectItem getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(SelectItem citizenship) {
        this.citizenship = citizenship;
    }

    public RegistrationTypeEnum getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationTypeEnum registrationType) {
        this.registrationType = registrationType;
    }

    public String getSocialUserName() {
        return socialUserName;
    }

    public void setSocialUserName(String socialUserName) {
        this.socialUserName = socialUserName;
    }

    public Double getOpeningBalanceDays() {
        return openingBalanceDays;
    }

    public void setOpeningBalanceDays(Double openingBalanceDays) {
        this.openingBalanceDays = openingBalanceDays;
    }

    public Double getProbationDays() {
        return probationDays;
    }

    public void setProbationDays(Double probationDays) {
        this.probationDays = probationDays;
    }


    public Integer getImportFileID() {
        return importFileID;
    }

    public void setImportFileID(Integer importFileID) {
        this.importFileID = importFileID;
    }

    public ArrayList<PaymentDeductionObject> getEmployerContributions() {
        if (employerContributions == null) {
            employerContributions = new ArrayList<>();
        }
        return employerContributions;
    }

    public void setEmployerContributions(ArrayList<PaymentDeductionObject> employerContributions) {
        this.employerContributions = employerContributions;
    }

    public ArrayList<SpokenLanguageItem> getSpokingLanguages() {
        return spokingLanguages;
    }

    public void setSpokingLanguages(ArrayList<SpokenLanguageItem> spokingLanguages) {
        this.spokingLanguages = spokingLanguages;
    }

    public ReferenceItem getEmployeeDegree() {
        return employeeDegree;
    }

    public void setEmployeeDegree(ReferenceItem employeeDegree) {
        this.employeeDegree = employeeDegree;
    }

    public HashMap<String, ArrayList<CustomTableRpc>> getCustomTableItems() {
        return customTableItems;
    }

    public void setCustomTableItems(HashMap<String, ArrayList<CustomTableRpc>> customTableItems) {
        this.customTableItems = customTableItems;
    }

    public SelectItem getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(SelectItem timeslot) {
        this.timeslot = timeslot;
    }

    public ExperienceTableItems[] getExperienceTableItems() {
        return experienceTableItems;
    }

    public void setExperienceTableItems(ExperienceTableItems[] experienceTableItems) {
        this.experienceTableItems = experienceTableItems;
    }

    public String getSalaryMode() {
        return salaryMode;
    }

    public void setSalaryMode(String salaryMode) {
        this.salaryMode = salaryMode;
    }

    public List<String> getFingerprintDeviceUuids() {
        return fingerprintDeviceUuids;
    }

    public void setFingerprintDeviceUuids(List<String> fingerprintDeviceUuids) {
        this.fingerprintDeviceUuids = fingerprintDeviceUuids;
    }

    public void setFingerprintDeviceId(Integer[] fingerprintDeviceId) {
        this.fingerprintDeviceId = fingerprintDeviceId;
    }

    public Integer[] getFingerprintDeviceId() {
        return fingerprintDeviceId;
    }
}
