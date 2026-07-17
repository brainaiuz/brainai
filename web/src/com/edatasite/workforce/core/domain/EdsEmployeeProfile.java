package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.util.*;

/**
 * User: izaynutdinov
 * Date: 28.04.2007
 * Time: 16:26:56
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employeeprofile")
public class EdsEmployeeProfile extends EdsObject {

    public static final String TITLE = "_TITLE";
    public static final String CAREER_LEVEL = "_CAREER_LEVEL";
    public static final String EXPERIENCE = "_EXPERIENCE";
    public static final String EDUCATION_LEVEL = "_EDUCATION_LEVEL";
    public static final String MANAGEMENT_EXPERIENCE = "_MANAGEMENT_EXPERIENCE";
    public static final String PROJECT_LEADERSHIP_EXPERIENCE = "_PROJECT_LEADERSHIP_EXPERIENCE";
    public static final String GENDER = "_GENDER";
    public static final String MARTIAL_STATUS = "_MARTIAL_STATUS";
    public static final String EMPLOYMENT_MODE = "_EMPLOYMENT_MODE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    private EdsEmployee employee;

    // Personal information
// Personal information
    @Column(name = "gender")
    private String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "martialStatusId")
    private EdsReference martialStatus;

    @Column(name = "employeeCode")
    private String employeeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employmentModeId")
    private EdsReference employmentMode;

    @Column(name = "termsOfContract")
    private Integer termsOfContract;

    @Column(name = "termsOfCMonthOrYear")
    private Integer termsOfCMonthOrYear;// 1 -- Month; 2 -- Years; DEFAULT -- null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salaryGradeId")
    private EdsGrade salaryGrade;

//    @JoinColumn(name = "positionId")
//    private Integer positionId;

    @Column(columnDefinition = " boolean DEFAULT false")
    private Boolean isSetupProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportsTo")
    private EdsEmployee reportsTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private EdsCrmContact contact;
// Contact info

    // Career info
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "careerLevelId")
    private EdsReference careerLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experienceId")
    private EdsReference experience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "educationLevelId")
    private EdsReference educationLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managementExperienceId")
    private EdsReference managementExperience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectLeadershipExperienceId")
    private EdsReference projectLeadershipExperience;

//    @Column(name = "availableforcoomembers")
//    private Boolean availableForCooMembers = true;

    @Column(name = "salaryAmount")
    private Double salaryAmount;

    @Column(name = "employmentHistory", length = 1500)
    private String empHistory;

    @Column(name = "intnumber")
    private Integer intNumber;

    @Column(name = "saveNumberFormula")
    private String savedNumberFormula;

    @Column(name = "visaExpirationDate")
    private Date visaExpirationDate;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "employeeProfile")
    private List<EdsEmployeeProfileVisaExpirationReminder> visaExpirationReminders = new ArrayList<>();

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "passportNumber")
    private String passportNumber;

    @Column(name = "passportIssueDate")
    private Date passportIssueDate;

    @Column(name = "passportExpiryDate")
    private Date passportExpiryDate;

    @Column(name = "medicalInsuranceExDate")
    private Date medicalInsuranceExDate;

    @Column(name = "visaNumber")
    private String visaNumber;

    @Column(name = "visaIssueDate")
    private Date visaIssueDate;

    @Column(name = "insuranceNumber")
    private String insuranceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    @ForeignKey(name = "none")
    private EdsCountry country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeedegree_id")
    private EdsReference employeeDegree;

// Career info

// Job related

    @Transient
    private Set<EdsProfileSkill> skills = new HashSet<>();

    public EdsEmployeeProfile() {
    }

// Job related

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsEmployee getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(EdsEmployee reportsTo) {
        if (!ServerUtils.equalsEdsObject(this.reportsTo, reportsTo) && getEmployee() != null) {
            getEmployee().addHistoryChange("Supervisor", this.reportsTo != null ? this.reportsTo.getFullName() : null, reportsTo != null ? reportsTo.getFullName() : null);
        }
        this.reportsTo = reportsTo;
    }

    public EdsGrade getSalaryGrade() {
        return salaryGrade;
    }

    public void setSalaryGrade(EdsGrade salaryGrade) {
        if (!ServerUtils.equalsEdsObject(this.salaryGrade, salaryGrade) && getEmployee() != null) {
            getEmployee().addHistoryChange("Salary Grade", this.salaryGrade != null ? this.salaryGrade.getGradeCode() : null, salaryGrade != null ? salaryGrade.getName() : null);
        }
        this.salaryGrade = salaryGrade;
    }

    public Integer getTermsOfContract() {
        return termsOfContract;
    }

    public void setTermsOfContract(Integer termsOfContract) {
        this.termsOfContract = termsOfContract;
    }

    public Integer getTermsOfCMonthOrYear() {
        return termsOfCMonthOrYear;
    }

    public void setTermsOfCMonthOrYear(Integer termsOfCMonthOrYear) {
        this.termsOfCMonthOrYear = termsOfCMonthOrYear;
    }

    public EdsReference getEmploymentMode() {
        return employmentMode;
    }

    public void setEmploymentMode(EdsReference employmentMode) {
        if (!ServerUtils.equalsEdsObject(this.employmentMode, employmentMode) && getEmployee() != null) {
            getEmployee().addHistoryChange("Employment Mode", this.employmentMode != null ? this.employmentMode.getName() : null, employmentMode != null ? employmentMode.getName() : null);
        }
        this.employmentMode = employmentMode;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        if (!ServerUtils.equalsString(this.employeeCode, employeeCode) && getEmployee() != null) {
            getEmployee().addHistoryChange("Employee Code", this.employeeCode, employeeCode);
        }
        this.employeeCode = employeeCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if (!ServerUtils.equalsString(this.gender, gender) && getEmployee() != null) {
            getEmployee().addHistoryChange("Gender", this.gender, gender);
        }
        this.gender = gender;
    }

    public EdsReference getMartialStatus() {
        return martialStatus;
    }

    public void setMartialStatus(EdsReference martialStatus) {
        if (!ServerUtils.equalsEdsObject(this.martialStatus, martialStatus) && getEmployee() != null) {
            getEmployee().addHistoryChange("Marital Status", this.martialStatus != null ? this.martialStatus.getName() : null, martialStatus != null ? martialStatus.getName() : null);
        }
        this.martialStatus = martialStatus;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public EdsReference getCareerLevel() {
        return careerLevel;
    }

    public void setCareerLevel(EdsReference careerLevel) {
        this.careerLevel = careerLevel;
    }

    public EdsReference getExperience() {
        return experience;
    }

    public void setExperience(EdsReference experience) {
        this.experience = experience;
    }

    public EdsReference getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(EdsReference educationLevel) {
        this.educationLevel = educationLevel;
    }

    public EdsReference getManagementExperience() {
        return managementExperience;
    }

    public void setManagementExperience(EdsReference managementExperience) {
        this.managementExperience = managementExperience;
    }

    public EdsReference getProjectLeadershipExperience() {
        return projectLeadershipExperience;
    }

    public void setProjectLeadershipExperience(EdsReference projectLeadershipExperience) {
        this.projectLeadershipExperience = projectLeadershipExperience;
    }

    public Set<EdsProfileSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<EdsProfileSkill> skills) {
        this.skills = skills;
    }

//    public Integer getPositionId() {
//        return positionId;
//    }
//
//    public void setPositionId(Integer positionId) {
//        this.positionId = positionId;
//    }

//    public Boolean isAvailableForCooMembers() {
//        return availableForCooMembers;
//    }
//
//    public void setAvailableForCooMembers(Boolean availableForCooMembers) {
//        this.availableForCooMembers = availableForCooMembers;
//    }

    public EdsCrmContact getContact() {
        return contact;
    }

    public void setContact(EdsCrmContact contact) {
        this.contact = contact;
    }

    public Boolean isSetupProfile() {
        return isSetupProfile;
    }

    public void setSetupProfile(Boolean setupProfile) {
        this.isSetupProfile = setupProfile;
    }

    public Double getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(Double salaryAmount) {
        if (!ServerUtils.equalsDoubleCustom(this.salaryAmount, salaryAmount) && getEmployee() != null) {
            getEmployee().addHistoryChange("Basic Salary", this.salaryAmount, salaryAmount);
        }
        this.salaryAmount = salaryAmount;
    }

    public String getEmpHistory() {
        return empHistory;
    }

    public void setEmpHistory(String empHistory) {
        this.empHistory = empHistory;
    }


    public String getSavedNumberFormula() {
        return savedNumberFormula;
    }

    public void setSavedNumberFormula(String savedNumberFormula) {
        this.savedNumberFormula = savedNumberFormula;
    }

    public Date getVisaExpirationDate() {
        return visaExpirationDate;
    }

    public void setVisaExpirationDate(Date visaExpirationDate) {
        if (!ServerUtils.equalsDate(this.visaExpirationDate, visaExpirationDate) && getEmployee() != null) {
            getEmployee().addHistoryChange("Visa expiry date", this.visaExpirationDate, visaExpirationDate);
        }

        this.visaExpirationDate = visaExpirationDate;
    }

    public List<EdsEmployeeProfileVisaExpirationReminder> getVisaExpirationReminders() {
        return visaExpirationReminders;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        if (!ServerUtils.equalsString(this.getNationality(), nationality) && getEmployee() != null) {
            getEmployee().addHistoryChange("Nationality", this.nationality, nationality);
        }
        this.nationality = nationality;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        if (!ServerUtils.equalsString(this.passportNumber, passportNumber) && getEmployee() != null) {
            getEmployee().addHistoryChange("Passport Number", this.passportNumber, passportNumber);
        }

        this.passportNumber = passportNumber;
    }

    public Date getPassportIssueDate() {
        return passportIssueDate;
    }

    public void setPassportIssueDate(Date passportIssueDate) {
        if (!ServerUtils.equalsDate(this.passportIssueDate, passportIssueDate) && getEmployee() != null) {
            getEmployee().addHistoryChange("Passport Issue Date", this.passportIssueDate, passportIssueDate);
        }
        this.passportIssueDate = passportIssueDate;
    }

    public Date getPassportExpiryDate() {
        return passportExpiryDate;
    }

    public void setPassportExpiryDate(Date passportExpiryDate) {
        if (!ServerUtils.equalsDate(this.passportExpiryDate, passportExpiryDate) && getEmployee() != null) {
            getEmployee().addHistoryChange("Passport Expiry Date", this.passportExpiryDate, passportExpiryDate);
        }

        this.passportExpiryDate = passportExpiryDate;
    }

    public String getVisaNumber() {
        return visaNumber;
    }

    public void setVisaNumber(String visaNumber) {
        if (!ServerUtils.equalsString(this.visaNumber, visaNumber) && getEmployee() != null) {
            getEmployee().addHistoryChange("Visa Number", this.visaNumber, visaNumber);
        }

        this.visaNumber = visaNumber;
    }

    public Date getVisaIssueDate() {
        return visaIssueDate;
    }

    public void setVisaIssueDate(Date visaIssueDate) {
        if (!ServerUtils.equalsDate(this.visaIssueDate, visaIssueDate) && getEmployee() != null) {
            getEmployee().addHistoryChange("Visa Issue Date", this.visaIssueDate, visaIssueDate);
        }
        this.visaIssueDate = visaIssueDate;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        if (!ServerUtils.equalsString(this.insuranceNumber, insuranceNumber) && getEmployee() != null) {
            getEmployee().addHistoryChange("Insurance Number", this.insuranceNumber, insuranceNumber);
        }
        this.insuranceNumber = insuranceNumber;
    }

    public EdsCountry getCountry() {
        return country;
    }

    public void setCountry(EdsCountry country) {
        if (!ServerUtils.equalsEdsObject(this.country, country) && getEmployee() != null) {
            getEmployee().addHistoryChange("Passport Issue By", this.country != null ? this.country.getName() : "", country != null ? country.getName() : "");
        }
        this.country = country;
    }

    public ArrayList<CalendarEventReminder> getVisaExpirationDateReminders() {
        ArrayList<CalendarEventReminder> reminders = new ArrayList<>();
        List<EdsEmployeeProfileVisaExpirationReminder> expirationReminders = getVisaExpirationReminders();
        if (expirationReminders != null && expirationReminders.size() > 0) {
            CalendarEventReminder reminder;
            for (EdsEmployeeProfileVisaExpirationReminder expirationReminder : expirationReminders) {
                reminder = new CalendarEventReminder();
                reminder.setReminderTimes(expirationReminder.getMinutes());
                reminders.add(reminder);
            }
        }
        return reminders;
    }

    public void setVisaExpirationReminders(List<EdsEmployeeProfileVisaExpirationReminder> visaExpirationReminders) {
        this.visaExpirationReminders = visaExpirationReminders;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public Date getMedicalInsuranceExDate() {
        return medicalInsuranceExDate;
    }

    public void setMedicalInsuranceExDate(Date medicalInsuranceExDate) {
        if (!ServerUtils.equalsDate(this.medicalInsuranceExDate, medicalInsuranceExDate) && getEmployee() != null) {
            getEmployee().addHistoryChange("Insurance Expiry Date", this.medicalInsuranceExDate, medicalInsuranceExDate);
        }
        this.medicalInsuranceExDate = medicalInsuranceExDate;
    }

    public EdsReference getEmployeeDegree() {
        return employeeDegree;
    }

    public void setEmployeeDegree(EdsReference employeeDegree) {
        this.employeeDegree = employeeDegree;
    }

    public ProfileItem getRPC(ProfileItem... items) {
        ProfileItem item = new ProfileItem();
        if (items != null && items.length > 0 && items[0] != null) {
            item = items[0];
        }
        if (getContact() != null) {
            ListingFilterParameter f = new ListingFilterParameter(false);
            f.setHRMS(true);
            item = (ProfileItem) getContact().getRPC(f, item);
            item.setContactID(getContact().getObjectID());
        }
        item.setObjectId(getObjectID());
        return item;
    }
}
