package com.edatasite.workforce.rest.v3.release10.hrms.dto.employee;

import com.edatasite.workforce.gwt.core.client.enums.Gender;
import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.LanguagesDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EmployeeSaveDto {
    private Integer objectID;
    private Integer employeeTemplateID;
    private String quickbookEmployeeID;
    private String quickbookEditSequence;
    private Long driverNumber;
    private String firstname;
    private String middlename;
    private String lastname;
    private String email;
    private String homeAddress;
    private String cityTown;
    private Integer country;
    private Integer region;
    private String postCode;
    private ArrayList<String> homephone;
    private ArrayList<String> workphone;
    private String primaryphone;
    private Date startDate;
    private Date endDate;
    private Date birthDate;
    private Integer departmentId;
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
    private Gender gender;
    private Integer companyID;
    private Date prevEndDate;
    private Date startDateForOnlyPayroll;
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
    private Date resignationDate;
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
    private Date visaExpirationDate;
    private Date fireDate;
    private Date hireDate;
    private Date passportIssueDate;
    private Date passportExpiryDate;
    private Date medicalInsuranceExpireDate;
    private Date visaIssueDate;
    private boolean applyPositionLeaveForEmployee;
    private ArrayList<SelectItem> coursesItems;
    private List<? extends CustomFieldRequest> customFields;
    private String passportNumber;
    private String visaNumber;
    private String insuranceNumber;
    private String paymentMethod;
    private Integer passportIssueID;
    private Integer placementId;
    private FileItem[] attachments;
    private boolean isAddSingleEmployee;
    private boolean fromEmployeeImport;
    private IdNameTO approver;
    private IdNameTO sender;
    private String status;
    private String rejectionNote;
    private boolean isFromMultiEmployee;
    private boolean isFromCandidate;
    private CurrencyItem salaryCurrency;
    private IdNameTO payMethod;
    private IdNameTO citizenship;
    private HashMap<String, String> payrollSettings = new HashMap<>();
    private Double openingBalanceDays;
    private Double probationDays;
    private Integer importFileID;
    private ArrayList<LanguagesDto> spokingLanguages;
    private IdNameTO employeeDegree;
    private IdNameTO timeslot;
    private String salaryMode;

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

    public Long getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(Long driverNumber) {
        this.driverNumber = driverNumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public ArrayList<String> getHomephone() {
        return homephone;
    }

    public void setHomephone(ArrayList<String> homephone) {
        this.homephone = homephone;
    }

    public ArrayList<String> getWorkphone() {
        return workphone;
    }

    public void setWorkphone(ArrayList<String> workphone) {
        this.workphone = workphone;
    }

    public String getPrimaryphone() {
        return primaryphone;
    }

    public void setPrimaryphone(String primaryphone) {
        this.primaryphone = primaryphone;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
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

    public Double getWageRate() {
        return wageRate;
    }

    public void setWageRate(Double wageRate) {
        this.wageRate = wageRate;
    }

    public Double getClientChargeRate() {
        return clientChargeRate;
    }

    public void setClientChargeRate(Double clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
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

    public String getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
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

    public boolean isOneOff() {
        return isOneOff;
    }

    public void setOneOff(boolean oneOff) {
        isOneOff = oneOff;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Date getPrevEndDate() {
        return prevEndDate;
    }

    public void setPrevEndDate(Date prevEndDate) {
        this.prevEndDate = prevEndDate;
    }

    public Date getStartDateForOnlyPayroll() {
        return startDateForOnlyPayroll;
    }

    public void setStartDateForOnlyPayroll(Date startDateForOnlyPayroll) {
        this.startDateForOnlyPayroll = startDateForOnlyPayroll;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getPhotoID() {
        return photoID;
    }

    public void setPhotoID(Integer photoID) {
        this.photoID = photoID;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
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

    public Boolean getHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(Boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public Boolean getEssUser() {
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

    public Date getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(Date resignationDate) {
        this.resignationDate = resignationDate;
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

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Integer getMartialStatusId() {
        return martialStatusId;
    }

    public void setMartialStatusId(Integer martialStatusId) {
        this.martialStatusId = martialStatusId;
    }

    public SelectItem[] getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(SelectItem[] spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public Integer getQualificationID() {
        return qualificationID;
    }

    public void setQualificationID(Integer qualificationID) {
        this.qualificationID = qualificationID;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getReportsToId() {
        return reportsToId;
    }

    public void setReportsToId(Integer reportsToId) {
        this.reportsToId = reportsToId;
    }

    public Integer getTermsOfContract() {
        return termsOfContract;
    }

    public void setTermsOfContract(Integer termsOfContract) {
        this.termsOfContract = termsOfContract;
    }

    public Integer getTermsOfCMonthORYear() {
        return termsOfCMonthORYear;
    }

    public void setTermsOfCMonthORYear(Integer termsOfCMonthORYear) {
        this.termsOfCMonthORYear = termsOfCMonthORYear;
    }

    public Integer getEmpModeId() {
        return empModeId;
    }

    public void setEmpModeId(Integer empModeId) {
        this.empModeId = empModeId;
    }

    public Integer getSalaryGradeId() {
        return salaryGradeId;
    }

    public void setSalaryGradeId(Integer salaryGradeId) {
        this.salaryGradeId = salaryGradeId;
    }

    public Double getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(Double salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public Integer getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Integer jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Date getVisaExpirationDate() {
        return visaExpirationDate;
    }

    public void setVisaExpirationDate(Date visaExpirationDate) {
        this.visaExpirationDate = visaExpirationDate;
    }

    public Date getFireDate() {
        return fireDate;
    }

    public void setFireDate(Date fireDate) {
        this.fireDate = fireDate;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Date getPassportIssueDate() {
        return passportIssueDate;
    }

    public void setPassportIssueDate(Date passportIssueDate) {
        this.passportIssueDate = passportIssueDate;
    }

    public Date getPassportExpiryDate() {
        return passportExpiryDate;
    }

    public void setPassportExpiryDate(Date passportExpiryDate) {
        this.passportExpiryDate = passportExpiryDate;
    }

    public Date getMedicalInsuranceExpireDate() {
        return medicalInsuranceExpireDate;
    }

    public void setMedicalInsuranceExpireDate(Date medicalInsuranceExpireDate) {
        this.medicalInsuranceExpireDate = medicalInsuranceExpireDate;
    }

    public Date getVisaIssueDate() {
        return visaIssueDate;
    }

    public void setVisaIssueDate(Date visaIssueDate) {
        this.visaIssueDate = visaIssueDate;
    }

    public boolean isApplyPositionLeaveForEmployee() {
        return applyPositionLeaveForEmployee;
    }

    public void setApplyPositionLeaveForEmployee(boolean applyPositionLeaveForEmployee) {
        this.applyPositionLeaveForEmployee = applyPositionLeaveForEmployee;
    }

    public ArrayList<SelectItem> getCoursesItems() {
        return coursesItems;
    }

    public void setCoursesItems(ArrayList<SelectItem> coursesItems) {
        this.coursesItems = coursesItems;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getVisaNumber() {
        return visaNumber;
    }

    public void setVisaNumber(String visaNumber) {
        this.visaNumber = visaNumber;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getPassportIssueID() {
        return passportIssueID;
    }

    public void setPassportIssueID(Integer passportIssueID) {
        this.passportIssueID = passportIssueID;
    }

    public Integer getPlacementId() {
        return placementId;
    }

    public void setPlacementId(Integer placementId) {
        this.placementId = placementId;
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

    public void setAddSingleEmployee(boolean addSingleEmployee) {
        isAddSingleEmployee = addSingleEmployee;
    }

    public boolean isFromEmployeeImport() {
        return fromEmployeeImport;
    }

    public void setFromEmployeeImport(boolean fromEmployeeImport) {
        this.fromEmployeeImport = fromEmployeeImport;
    }

    public IdNameTO getApprover() {
        return approver;
    }

    public void setApprover(IdNameTO approver) {
        this.approver = approver;
    }

    public IdNameTO getSender() {
        return sender;
    }

    public void setSender(IdNameTO sender) {
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

    public void setFromMultiEmployee(boolean fromMultiEmployee) {
        isFromMultiEmployee = fromMultiEmployee;
    }

    public boolean isFromCandidate() {
        return isFromCandidate;
    }

    public void setFromCandidate(boolean fromCandidate) {
        isFromCandidate = fromCandidate;
    }

    public CurrencyItem getSalaryCurrency() {
        return salaryCurrency;
    }

    public void setSalaryCurrency(CurrencyItem salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }

    public IdNameTO getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(IdNameTO payMethod) {
        this.payMethod = payMethod;
    }

    public IdNameTO getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(IdNameTO citizenship) {
        this.citizenship = citizenship;
    }

    public HashMap<String, String> getPayrollSettings() {
        return payrollSettings;
    }

    public void setPayrollSettings(HashMap<String, String> payrollSettings) {
        this.payrollSettings = payrollSettings;
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

    public ArrayList<LanguagesDto> getSpokingLanguages() {
        return spokingLanguages;
    }

    public void setSpokingLanguages(ArrayList<LanguagesDto> spokingLanguages) {
        this.spokingLanguages = spokingLanguages;
    }

    public IdNameTO getEmployeeDegree() {
        return employeeDegree;
    }

    public void setEmployeeDegree(IdNameTO employeeDegree) {
        this.employeeDegree = employeeDegree;
    }

    public IdNameTO getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(IdNameTO timeslot) {
        this.timeslot = timeslot;
    }

    public String getSalaryMode() {
        return salaryMode;
    }

    public void setSalaryMode(String salaryMode) {
        this.salaryMode = salaryMode;
    }
}
