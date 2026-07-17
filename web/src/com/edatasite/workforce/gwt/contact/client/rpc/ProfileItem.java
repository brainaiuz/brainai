package com.edatasite.workforce.gwt.contact.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SpokenLanguageItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserBankAccountData;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ProfileItem extends ContactListItem {
    private String driverID;
    private HashMap<Integer, AnnualLeaveItem> leaveitems;
    private HashMap<Integer, AnnualLeaveItem> employeeBenefits;
    private Date applyWageRateFrom;
    private Date applyClientChargeRateFrom;
    private HashMap<String, String> payrollSettings;
    private ArrayList<PaymentDeductionObject> payments;
    private ArrayList<PaymentDeductionObject> deductions;
    private ArrayList<PaymentDeductionObject> taxes;
    private ArrayList<PaymentDeductionObject> loans;
    private ArrayList<PaymentDeductionObject> employerContributions;
    private ArrayList<Integer> deletedCategories;
    private ArrayList<Integer> inactiveCategories;

    private ArrayList<PaymentDeductionObject> paymentCategories;
    private ArrayList<PaymentDeductionObject> deductionCategories;
    private ArrayList<PaymentDeductionObject> loanCategories;
    private Integer importFileID;
    private ArrayList<SpokenLanguageItem> spokingLanguages;
    private SelectItem[] employeeDegrees;
    private ReferenceItem employeeDegree;
    private SelectItem[] templates;
    private boolean isFromPlacement;
    private ArrayList<RelationItem> relationItems;
    private List<String> fingerprintDeviceUuids = new ArrayList<>();

    public ProfileItem() {
        super();
        setContactType(ContactListItem.EMPLOYEE_CONTACT);
    }

    public static final String TITLE = "title";
    public static final String EMAIL = "email";

    private Integer objectId;
    private String position;
    private Integer positionId;
    private String reportsTo;
    private Integer reportsToId;

    private String careerLevel;
    private Integer careerLevelId;
    private String experience;
    private Integer experienceId;
    private String educationLevel;
    private Integer educationLevelId;
    private String managementExperience;
    private Integer managementExperienceId;
    private String projectLeadershipExperience;
    private Integer projectLeadershipExperienceId;
    private String skill;
    private String goal;

    //IM ADDRESS
    private SelectItem[] contactImAddress;

    private String employeeImageUrl;
    private DateNonConvertable dob;
    private String gender;
    private String paymentMethod;
    private String salaryMode;
    private SelectItem[] spokenLanguages;
    private SelectItem[] languages;
    private LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> competencies;
    private ArrayList<KpiTreeInfo> employeeCompetencies;
    private String empCode;
    private String status;
    private String statusCode;
    private SelectItem[] statusList;
    private Integer statusId;
    private String empMode;
    private SelectItem[] empModeList;
    private Integer empModeId;
    private DateNonConvertable hireDate;
    private String hireDateString;
    private DateNonConvertable fireDate;
    private Integer termsOfContract;
    private Integer termsOfCMonthORYear;
    private String tOCInYears;
    private String salaryGrade;
    private SelectItem[] salaryGradeList;
    private Integer salaryGradeId;
    private Double salaryAmount;
    private String department;
    private boolean same;
    private Integer employeeId;
    private Integer role;
    private String roleName;
    private Integer[] roleId;
    private SelectItem[] roleList;
    private Integer[] fingerprintDeviceId;
    private SelectItem[] fingerprintDeviceList;
    private SelectItem location;
    private String locationName;
    private Integer locationId;
    private UserBankAccountData bankAccountData;//from profile.client.rpc.ProfileItem
    private Integer localeId;//from profile.client.rpc.ProfileItem
    //    private ProfileImItem[] ims;//from profile.client.rpc.ProfileItem
    private ArrayList<CompanyCustomFieldItem> customFields;
    private LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> coursesMap;
    private ArrayList<SelectItem> coursesItems;
    private FileItem[] attachments;
    private String empHistory;
    private DateNonConvertable visaExpirationDate;
    private ArrayList<CalendarEventReminder> visaExpirationDateReminder;

    private SelectItem[] qualifications;
    private Integer qualificationID;
    private String qualificationCode;
    private String qualificationName;
    private String nationality;
    private String passportNumber;
    private DateNonConvertable passportIssueDate;
    private String passportIssueBy;
    private SelectItem passportIssueItem;
    private DateNonConvertable passportExpiryDate;
    private DateNonConvertable medicalInsuranceExpireDate;
    private String visaNumber;
    private DateNonConvertable visaIssueDate;
    private String insuranceNumber;
    // --------------------------- for COO Connect Workspace ---------------------------
    private String from;
    private boolean availableForCooMembers;
    private Integer companyPhotoId;
    private boolean applyPositionLeaveForEmployee = false;
    private Boolean noAccess;
    private Boolean ess;
    private Integer[] userLimit;
    private DateNonConvertable currentYear;
    private Double openingBalanceDays;
    private Double probationDays;
    private SelectItem[] timeslots;
    private SelectItem timeslot;
    private SelectItem defaultTimeslot;
    private ArrayList<RelationItem> convertedRelations;
    private String fromName;
    private String startDate;
    private String endDate;
    private Double allowance;
    private Double leftLeaveDays;
    private Long pinfl;
    private String employeeFullName;
    private Integer pinflID;
    private Integer employeeFullNameID;
    private Integer departmentID;
    private Integer hireDateID;
    private Integer startDateID;
    private Integer endDateID;
    private Integer allowanceID;
    private Integer leftDaysID;
    private String rejectionReason;

    private ExperienceTableItems[] experienceTableItems;

    public SelectItem[] getRoleList() {
        return roleList;
    }

    public void setRoleList(SelectItem[] roleList) {
        this.roleList = roleList;
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

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public boolean isSame() {
        return same;
    }

    public void setSame(boolean same) {
        this.same = same;
    }

    public SelectItem[] getStatusList() {
        return statusList;
    }

    public void setStatusList(SelectItem[] statusList) {
        this.statusList = statusList;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public SelectItem[] getEmpModeList() {
        return empModeList;
    }

    public void setEmpModeList(SelectItem[] empModeList) {
        this.empModeList = empModeList;
    }

    public Integer getEmpModeId() {
        return empModeId;
    }

    public void setEmpModeId(Integer empModeId) {
        this.empModeId = empModeId;
    }

    public Integer getReportsToId() {
        return reportsToId;
    }

    public void setReportsToId(Integer reportsToId) {
        this.reportsToId = reportsToId;
    }

    public SelectItem[] getSalaryGradeList() {
        return salaryGradeList;
    }

    public void setSalaryGradeList(SelectItem[] salaryGradeList) {
        this.salaryGradeList = salaryGradeList;
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

    public DateNonConvertable getVisaExpirationDate() {
        return visaExpirationDate;
    }

    public void setVisaExpirationDate(DateNonConvertable visaExpirationDate) {
        this.visaExpirationDate = visaExpirationDate;
    }

    public ArrayList<CalendarEventReminder> getVisaExpirationDateReminder() {
        return visaExpirationDateReminder;
    }

    public void setVisaExpirationDateReminder(ArrayList<CalendarEventReminder> visaExpirationDateReminder) {
        this.visaExpirationDateReminder = visaExpirationDateReminder;
    }

    public SelectItem[] getQualifications() {
        return qualifications;
    }

    public void setQualifications(SelectItem[] qualifications) {
        this.qualifications = qualifications;
    }

    public Integer getQualificationID() {
        return qualificationID;
    }

    public void setQualificationID(Integer qualificationID) {
        this.qualificationID = qualificationID;
    }

    public String getQualificationCode() {
        return qualificationCode;
    }

    public void setQualificationCode(String qualificationCode) {
        this.qualificationCode = qualificationCode;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }

    public String getEmpHistory() {
        return empHistory;
    }

    public void setEmpHistory(String empHistory) {
        this.empHistory = empHistory;
    }

    public DateNonConvertable getDob() {
        return dob;
    }

    public void setDob(DateNonConvertable dob) {
        this.dob = dob;
    }

    public SelectItem[] getContactImAddress() {
        return contactImAddress;
    }

    public void setContactImAddress(SelectItem[] contactImAddress) {
        this.contactImAddress = contactImAddress;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public SelectItem[] getSpokenLanguages() {
        return spokenLanguages;
    }

    public String getSpokenLanguagesAsString() {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        if (spokenLanguages != null && spokenLanguages.length > 0) {
            for (SelectItem item : spokenLanguages) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    builder.append(", ");
                }

                builder.append(item.getName());
            }

            return builder.toString();
        }
        return null;
    }

    public void setSpokenLanguages(SelectItem[] spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public SelectItem[] getLanguages() {
        return languages;
    }

    public void setLanguages(SelectItem[] languages) {
        this.languages = languages;
    }

    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getCompetencies() {
        return competencies;
    }

    public void setCompetencies(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> competencies) {
        this.competencies = competencies;
    }

    public ArrayList<KpiTreeInfo> getEmployeeCompetencies() {
        return employeeCompetencies;
    }

    public void setEmployeeCompetencies(ArrayList<KpiTreeInfo> employeeCompetencies) {
        this.employeeCompetencies = employeeCompetencies;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getEmpMode() {
        return empMode;
    }

    public void setEmpMode(String empMode) {
        this.empMode = empMode;
    }

    public DateNonConvertable getHireDate() {
        return hireDate;
    }

    public void setHireDate(DateNonConvertable hireDate) {
        this.hireDate = hireDate;
    }

    public String getHireDateString() {
        return hireDateString;
    }

    public void setHireDateString(String hireDateString) {
        this.hireDateString = hireDateString;
    }

    public DateNonConvertable getFireDate() {
        return fireDate;
    }

    public void setFireDate(DateNonConvertable fireDate) {
        this.fireDate = fireDate;
    }

    public String getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(String reportsTo) {
        this.reportsTo = reportsTo;
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

    public String getTOCInYears() {
        return tOCInYears;
    }

    public void setTOCInYears(String tOCInYears) {
        this.tOCInYears = tOCInYears;
    }

    public String getSalaryGrade() {
        return salaryGrade;
    }

    public void setSalaryGrade(String salaryGrade) {
        this.salaryGrade = salaryGrade;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getObjectId() {
        return this.objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getCareerLevel() {
        return this.careerLevel;
    }

    public void setCareerLevel(String careerLevel) {
        this.careerLevel = careerLevel;
    }

    public String getExperience() {
        return this.experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getEducationLevel() {
        return this.educationLevel;
    }

    public void setEducationLevel(String education) {
        this.educationLevel = education;
    }

    public String getManagementExperience() {
        return this.managementExperience;
    }

    public void setManagementExperience(String managementExperience) {
        this.managementExperience = managementExperience;
    }

    public String getProjectLeadershipExperience() {
        return this.projectLeadershipExperience;
    }

    public void setProjectLeadershipExperience(String projectLeadershipExperience) {
        this.projectLeadershipExperience = projectLeadershipExperience;
    }

    public Integer getCareerLevelId() {
        return this.careerLevelId;
    }

    public void setCareerLevelId(Integer careerLevelId) {
        this.careerLevelId = careerLevelId;
    }

    public Integer getExperienceId() {
        return this.experienceId;
    }

    public void setExperienceId(Integer experienceId) {
        this.experienceId = experienceId;
    }

    public Integer getEducationLevelId() {
        return this.educationLevelId;
    }

    public void setEducationLevelId(Integer educationId) {
        this.educationLevelId = educationId;
    }

    public Integer getManagementExperienceId() {
        return this.managementExperienceId;
    }

    public void setManagementExperienceId(Integer managementExperienceId) {
        this.managementExperienceId = managementExperienceId;
    }

    public Integer getProjectLeadershipExperienceId() {
        return this.projectLeadershipExperienceId;
    }

    public void setProjectLeadershipExperienceId(Integer projectLeadershipExperienceId) {
        this.projectLeadershipExperienceId = projectLeadershipExperienceId;
    }

    public String getSkill() {
        return this.skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getGoal() {
        return this.goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getEmployeeImageUrl() {
        return employeeImageUrl;
    }

    public void setEmployeeImageUrl(String employeeImageUrl) {
        this.employeeImageUrl = employeeImageUrl;
    }

    public boolean isFromPlacement() {
        return isFromPlacement;
    }

    public void setFromPlacement(boolean fromPlacement) {
        isFromPlacement = fromPlacement;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public String gettOCInYears() {
        return tOCInYears;
    }

    public void settOCInYears(String tOCInYears) {
        this.tOCInYears = tOCInYears;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isAvailableForCooMembers() {
        return availableForCooMembers;
    }

    public void setAvailableForCooMembers(boolean availableForCooMembers) {
        this.availableForCooMembers = availableForCooMembers;
    }

    public UserBankAccountData getBankAccountData() {
        return bankAccountData;
    }

    public void setBankAccountData(UserBankAccountData bankAccountData) {
        this.bankAccountData = bankAccountData;
    }

    public Integer getLocaleId() {
        return localeId;
    }

    public void setLocaleId(Integer localeId) {
        this.localeId = localeId;
    }

//    public ProfileImItem[] getIms() {
//        return ims;
//    }
//

    //    public void setIms(ProfileImItem[] ims) {
//        this.ims = ims;
//    }
//
    public String getEmail() {
        return getPrimaryEmail();
    }


    public Integer getContactID() {
        return super.getObjectId();
    }

    public void setContactID(Integer contactID) {
        super.setObjectId(contactID);
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getCoursesMap() {
        return coursesMap;
    }

    public void setCoursesMap(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> coursesMap) {
        this.coursesMap = coursesMap;
    }

    public ArrayList<SelectItem> getCoursesItems() {
        return coursesItems;
    }

    public void setCoursesItems(ArrayList<SelectItem> coursesItems) {
        this.coursesItems = coursesItems;
    }

    public Integer getCompanyPhotoId() {
        return companyPhotoId;
    }

    public void setCompanyPhotoId(Integer companyPhotoId) {
        this.companyPhotoId = companyPhotoId;
    }

//    public String getNationality() {
//        return nationality;
//    }
//
//    public void setNationality(String nationality) {
//        this.nationality = nationality;
//    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public DateNonConvertable getPassportIssueDate() {
        return passportIssueDate;
    }

    public void setPassportIssueDate(DateNonConvertable passportIssueDate) {
        this.passportIssueDate = passportIssueDate;
    }

    public String getPassportIssueBy() {
        return passportIssueBy;
    }

    public void setPassportIssueBy(String passportIssueBy) {
        this.passportIssueBy = passportIssueBy;
    }

    public DateNonConvertable getPassportExpiryDate() {
        return passportExpiryDate;
    }

    public void setPassportExpiryDate(DateNonConvertable passportExpiryDate) {
        this.passportExpiryDate = passportExpiryDate;
    }

    public String getVisaNumber() {
        return visaNumber;
    }

    public void setVisaNumber(String visaNumber) {
        this.visaNumber = visaNumber;
    }

    public DateNonConvertable getVisaIssueDate() {
        return visaIssueDate;
    }

    public void setVisaIssueDate(DateNonConvertable visaIssueDate) {
        this.visaIssueDate = visaIssueDate;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getDriverID() {
        return driverID;
    }

    public SelectItem getPassportIssueItem() {
        return passportIssueItem;
    }

    public void setPassportIssueItem(SelectItem passportIssueItem) {
        this.passportIssueItem = passportIssueItem;
    }

    public DateNonConvertable getMedicalInsuranceExpireDate() {
        return medicalInsuranceExpireDate;
    }

    public void setMedicalInsuranceExpireDate(DateNonConvertable medicalInsuranceExpireDate) {
        this.medicalInsuranceExpireDate = medicalInsuranceExpireDate;
    }

    public HashMap<Integer, AnnualLeaveItem> getLeaveitems() {
        return leaveitems;
    }

    public void setLeaveitems(HashMap<Integer, AnnualLeaveItem> leaveitems) {
        this.leaveitems = leaveitems;
    }

    public boolean isApplyPositionLeaveForEmployee() {
        return applyPositionLeaveForEmployee;
    }

    public void setApplyPositionLeaveForEmployee(boolean applyPositionLeaveForEmployee) {
        this.applyPositionLeaveForEmployee = applyPositionLeaveForEmployee;
    }

    public HashMap<Integer, AnnualLeaveItem> getEmployeeBenefits() {
        return employeeBenefits;
    }

    public void setEmployeeBenefits(HashMap<Integer, AnnualLeaveItem> employeeBenefits) {
        this.employeeBenefits = employeeBenefits;
    }

    public Boolean getNoAccess() {
        if (noAccess == null) {
            noAccess = Boolean.FALSE;
        }
        return noAccess;
    }

    public void setNoAccess(Boolean noAccess) {
        this.noAccess = noAccess;
    }

    public void setApplyWageRateFrom(Date applyWageRateFrom) {
        this.applyWageRateFrom = applyWageRateFrom;
    }

    public Date getApplyWageRateFrom() {
        return applyWageRateFrom;
    }

    public void setApplyClientChargeRateFrom(Date applyClientChargeRateFrom) {
        this.applyClientChargeRateFrom = applyClientChargeRateFrom;
    }

    public Date getApplyClientChargeRateFrom() {
        return applyClientChargeRateFrom;
    }

    public Boolean getEss() {
        if (ess == null) {
            return Boolean.FALSE;
        }
        return ess;
    }

    public void setEss(Boolean ess) {
        this.ess = ess;
    }

    public HashMap<String, String> getPayrollSettings() {
        if (payrollSettings == null) {
            return new HashMap<>();
        }
        return payrollSettings;
    }

    public void setPayrollSettings(HashMap<String, String> payrollSettings) {
        this.payrollSettings = payrollSettings;
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
        return loans;
    }

    public void setLoans(ArrayList<PaymentDeductionObject> loans) {
        this.loans = loans;
    }

    public ArrayList<PaymentDeductionObject> getPaymentCategories() {
        if (paymentCategories == null) {
            paymentCategories = new ArrayList<>();
        }
        return paymentCategories;
    }

    public void setPaymentCategories(ArrayList<PaymentDeductionObject> paymentCategories) {
        this.paymentCategories = paymentCategories;
    }

    public ArrayList<PaymentDeductionObject> getDeductionCategories() {
        if (deductionCategories == null) {
            deductionCategories = new ArrayList<>();
        }
        return deductionCategories;
    }

    public void setDeductionCategories(ArrayList<PaymentDeductionObject> deductionCategories) {
        this.deductionCategories = deductionCategories;
    }
    public ArrayList<PaymentDeductionObject> getLoanCategories() {
        if (loanCategories == null) {
            loanCategories = new ArrayList<>();
        }
        return loanCategories;
    }

    public void setLoanCategories(ArrayList<PaymentDeductionObject> loanCategories) {
        this.loanCategories = loanCategories;
    }

    public Integer[] getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(Integer[] userLimit) {
        this.userLimit = userLimit;
    }

    public DateNonConvertable getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(DateNonConvertable currentYear) {
        this.currentYear = currentYear;
    }

    public Double getOpeningBalanceDays() {
        return openingBalanceDays;
    }

    public void setOpeningBalanceDays(Double openingBalanceDays) {
        this.openingBalanceDays = openingBalanceDays;
    }

    public ArrayList<RelationItem> getRelationItems() {
        return relationItems;
    }

    public void setRelationItems(ArrayList<RelationItem> relationItems) {
        this.relationItems = relationItems;
    }

    public Double getProbationDays() {
        return probationDays;
    }

    public void setProbationDays(Double probationDays) {
        this.probationDays = probationDays;
    }

    public void setImportFileID(Integer importFileID) {
        this.importFileID = importFileID;
    }

    public Integer getImportFileID() {
        return importFileID;
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

    public void setSpokingLanguages(ArrayList<SpokenLanguageItem> spokenLanguages) {
        this.spokingLanguages = spokenLanguages;
    }

    public SelectItem[] getemployeeDegrees() {
        return employeeDegrees;
    }

    public void setemployeeDegrees(SelectItem[] employeeDegrees) {
        this.employeeDegrees = employeeDegrees;
    }

    public ReferenceItem getemployeeDegree() {
        return employeeDegree;
    }

    public void setemployeeDegree(ReferenceItem employeeDegree) {
        this.employeeDegree = employeeDegree;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public SelectItem[] getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(SelectItem[] timeslots) {
        this.timeslots = timeslots;
    }

    public SelectItem getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(SelectItem timeslot) {
        this.timeslot = timeslot;
    }

    public SelectItem getDefaultTimeslot() {
        return defaultTimeslot;
    }

    public void setDefaultTimeslot(SelectItem defaultTimeslot) {
        this.defaultTimeslot = defaultTimeslot;
    }

    public ArrayList<RelationItem> getConvertedRelations() {
        return this.convertedRelations;
    }

    public void setConvertedRelations(final ArrayList<RelationItem> convertedRelations) {
        this.convertedRelations = convertedRelations;
    }

    public String getFromName() {
        return this.fromName;
    }

    public void setFromName(final String fromName) {
        this.fromName = fromName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Double getAllowance() {
        return allowance;
    }

    public void setAllowance(Double allowance) {
        this.allowance = allowance;
    }

    public Double getLeftLeaveDays() {
        return leftLeaveDays;
    }

    public void setLeftLeaveDays(Double leftLeaveDays) {
        this.leftLeaveDays = leftLeaveDays;
    }

    public Long getPinfl() {
        return pinfl;
    }

    public void setPinfl(Long pinfl) {
        this.pinfl = pinfl;
    }

    public String getEmployeeFullName() {
        return employeeFullName;
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName = employeeFullName;
    }

    public Integer getPinflID() {
        return pinflID;
    }

    public void setPinflID(Integer pinflID) {
        this.pinflID = pinflID;
    }

    public Integer getEmployeeFullNameID() {
        return employeeFullNameID;
    }

    public void setEmployeeFullNameID(Integer employeeFullNameID) {
        this.employeeFullNameID = employeeFullNameID;
    }

    @Override
    public Integer getDepartmentID() {
        return departmentID;
    }

    @Override
    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public Integer getHireDateID() {
        return hireDateID;
    }

    public void setHireDateID(Integer hireDateID) {
        this.hireDateID = hireDateID;
    }

    public Integer getStartDateID() {
        return startDateID;
    }

    public void setStartDateID(Integer startDateID) {
        this.startDateID = startDateID;
    }

    public Integer getEndDateID() {
        return endDateID;
    }

    public void setEndDateID(Integer endDateID) {
        this.endDateID = endDateID;
    }

    public Integer getAllowanceID() {
        return allowanceID;
    }

    public void setAllowanceID(Integer allowanceID) {
        this.allowanceID = allowanceID;
    }

    public Integer getLeftDaysID() {
        return leftDaysID;
    }

    public void setLeftDaysID(Integer leftDaysID) {
        this.leftDaysID = leftDaysID;
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

    public Integer[] getFingerprintDeviceId() {
        return fingerprintDeviceId;
    }

    public void setFingerprintDeviceId(Integer[] fingerprintDeviceId) {
        this.fingerprintDeviceId = fingerprintDeviceId;
    }

    public SelectItem[] getFingerprintDeviceList() {
        return fingerprintDeviceList;
    }

    public void setFingerprintDeviceList(SelectItem[] fingerprintDeviceList) {
        this.fingerprintDeviceList = fingerprintDeviceList;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
