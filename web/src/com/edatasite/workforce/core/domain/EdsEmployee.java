package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsUserBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsEmployeeCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsEmployeeCustomItemTable;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.hmrc.EdsEmployeeExperienceItemTable;
import com.edatasite.workforce.core.domain.lucene.Indexable;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollBatch;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollGlobalSettings;
import com.edatasite.workforce.core.domain.projectcost.EdsResource;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeSolrItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeRepresenter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.SpokenLanguagesManager;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;


@Entity
@TypeDefs(@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class))
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employee",
        indexes = {
                @Index(columnList = "employeeDepartmentId", name = "employee_employeeDepartmentId_idx"),
                @Index(columnList = "profileId", name = "employee_profileId_idx")
        })
public class EdsEmployee extends EdsUser implements Indexable, ObjectHistory {

    private Integer employeeDepartmentId;

    @Column(name = "activationDate")
    private Date activationDate;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "annualallowance")
    private Integer annualAllowance;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Transient
    private String trainingNeeds;

    @Transient
    private EdsTimeTrack status;

    @Transient
    private EdsReference grade;

    @Transient
    private Boolean indexed = false;

    @Transient
    private List<EdsJobFunction> jobFunctions = new ArrayList<>();

    @Column(name = "wageRate")
    private Double wageRate;

    @Column(name = "clientChargeRate")
    private Double clientChargeRate;

    @Column(name = "quickbook_employee_id")
    private String quickbookEmployeeID; //(Quick Book Emplooyee ID)

    @Column(name = "driver_number")
    private Long driverNumber; //Taxi driver unique number

    @Column(name = "quickbook_edit_sequence")
    private String quickbookEditSequence;

    @Column(name = "external_guid")
    private String externalGUID;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;


    @Column(name = "prevEndDate")
    private Date prevEndDate;

    @Column(name = "startDateForOnlyPayroll")
    private Date startDateForOnlyPayroll;

    @Column(name = "leaveDurationHour")
    private Double leaveDurationHour;

    @Column(name = "leaveDurationDay")
    private Double leaveDurationDay;

    @Column(name = "openingbalancedays")
    private Double openingBalanceDays;

    @Column(name = "probationperiods")
    private Double probationDays;

    @Column(name = "importFileID")
    private Integer importFileID;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "course_instructor",
            joinColumns = {@JoinColumn(name = "instructor_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")})
    private List<EdsCourse> courses = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "warehouse_salesperson",
            joinColumns = {@JoinColumn(name = "salesperson_id")},
            inverseJoinColumns = {@JoinColumn(name = "warehouse_id")})
    private List<EdsWarehouse> warehouses;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsEmployeeCustomFields customFields;

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EdsEmployeeCustomItemTable> itemTables = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "employeeID")
    @Where(clause = "(deleted = 'false' or deleted is null) and (isRecurring = 'true' OR paymentType = 'ADDITIONAL')")
    private List<EdsPaymentDeduction> categories = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "emp_batch",
            joinColumns = {@JoinColumn(name = "emp_id")},
            inverseJoinColumns = {@JoinColumn(name = "batch_id")})
    private List<EdsPayrollBatch> payrollBatches;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "pds_employee",
            joinColumns = {@JoinColumn(name = "employee_id")},
            inverseJoinColumns = {@JoinColumn(name = "pds_id")})
    private List<EdsPayrollGlobalSettings> payrollGlobalSettings;

    @ManyToMany(mappedBy = "backupEmployees")
    private Set<EdsSickRequest> leaveRequestOfBackupEmployee = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @ForeignKey(name = "none")
    private EdsCurrency salaryCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizenship")
    @ForeignKey(name = "none")
    EdsCountry citizenship;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placement_id")
    private EdsPlacement placement;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<EdsLabourPeriod> periodList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "resourceId")
    private EdsResource resource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeDepartmentId", updatable = false, insertable = false)
    private EdsEmployeeDepartment employeeDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "positionid")
    private EdsPosition position;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslotid")
    private EdsTimeSlot timeSlot;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeID")
    private Set<EdsAnnualLeaveAllowance> annualLeaveAllowances = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "employeeID")
    private final Set<EdsEmployeeBenefitAllowance> benefitAllowance = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payMethodId")
    private EdsPaymentMethod payMethod;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "profileId")
    private EdsEmployeeProfile profile;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "employee", orphanRemoval = true)
    @Where(clause = "deleted = 'false' or deleted is null")
    private List<EdsEmployeeSkills> employeeskills;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "employeeId")
    @OrderBy("changeDate ASC")
    private List<EmployeeWageClientRateHistory> wageClientRatesHistory = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "managerId")
    private Set<EdsProject> managedProjects = new TreeSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qualification")
    private EdsReference qualification;  //employee qualification

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;


    @Column(name = "rejection_reasonId")
    private Integer rejectionReason;

    @OneToMany(mappedBy = "edsEmployee", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EdsEmployeeExperienceItemTable> experienceItemTables = new HashSet<>();

    @Column(name = "salaryMode")
    private String salaryMode;

    @Type(type = "jsonb")
    @Column(name = "fingerprint_device_uuids", columnDefinition = "jsonb")
    private List<String> fingerprintDeviceUuids = new ArrayList<>();

    public List<EdsLabourPeriod> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<EdsLabourPeriod> periodList) {
        this.periodList = periodList;
    }

    public Double getWageRate() {
        return wageRate == null ? wageRate = 0.0 : wageRate;
    }

    public void setWageRate(Double wageRate) {
        if (!ServerUtils.equalsDoubleCustom(this.wageRate, wageRate)) {
            addHistoryChange("Wage Rate (hourly)", this.wageRate, wageRate);
        }
        this.wageRate = wageRate;
    }

    public List<EdsPayrollGlobalSettings> getPayrollGlobalSettings() {
        return payrollGlobalSettings;
    }

    public void setPayrollGlobalSettings(List<EdsPayrollGlobalSettings> payrollGlobalSettings) {
        this.payrollGlobalSettings = payrollGlobalSettings;
    }

    public Double getClientChargeRate() {
        return clientChargeRate == null ? clientChargeRate = 0.0 : clientChargeRate;
    }

    public void setClientChargeRate(Double clientChargeRate) {
        if (!ServerUtils.equalsDoubleCustom(this.clientChargeRate, clientChargeRate)) {
            addHistoryChange("Client Charge Rate (hourly)", this.clientChargeRate, clientChargeRate);
        }
        this.clientChargeRate = clientChargeRate;
    }

    public String getTrainingNeeds() {
        return trainingNeeds;
    }

    public void setTrainingNeeds(String trainingNeeds) {
        this.trainingNeeds = trainingNeeds;
    }

    public Integer getAnnualAllowance() {
        return annualAllowance;
    }

    public void setAnnualAllowance(Integer annualAllowance) {
        this.annualAllowance = annualAllowance;
    }

    public Set<EdsAnnualLeaveAllowance> getAnnualLeaveAllowances() {
        return annualLeaveAllowances;
    }

    public void setAnnualLeaveAllowances(Set<EdsAnnualLeaveAllowance> annualLeaveAllowances) {
        this.annualLeaveAllowances = annualLeaveAllowances;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        if (!ServerUtils.equalsString(this.paymentMethod, paymentMethod)) {
            addChange(CustomFormConstants.PAYMENT_METHOD);
        }
        this.paymentMethod = paymentMethod;
    }

    public EdsPaymentMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(EdsPaymentMethod payMethod) {
        this.payMethod = payMethod;
    }

    public EdsPosition getPosition() {
        return position;
    }

    public void setPosition(EdsPosition position) {
        if (!ServerUtils.equalsEdsObject(this.position, position)) {
            addHistoryChange("Position", this.position != null ? this.position.getName() : null, position != null ? position.getName() : null);
        }
        this.position = position;
    }

    public Integer getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(Integer rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public EdsTimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(EdsTimeSlot timeSlot) {
        if (!ServerUtils.equalsEdsObject(this.timeSlot, timeSlot)) {
            addHistoryChange("Timeslot", this.timeSlot != null ? this.timeSlot.getName() : null, timeSlot != null ? timeSlot.getName() : null);
        }
        this.timeSlot = timeSlot;
    }

    public String getMobileOrFirst() {
        if (getContact() != null) {
            List<EdsCrmContactItemParams> phones = getContact().getItemParams(EdsCrmContactItemParams.PHONE);
            for (EdsCrmContactItemParams phone : phones) {
                if (phone != null && phone.getRelation() != null && phone.getRelation() == EdsCrmContactItemParams.MOBILE) {
                    return phone.getValue();
                }
            }
            for (EdsCrmContactItemParams phone : phones) {
                if (phone != null && phone.getRelation() != null) {
                    return phone.getValue();
                }
            }
        }
        return null;
    }

    public String getParamByRelation(int... relation) {
        if (getContact() != null) {
            List<EdsCrmContactItemParams> phones = getContact().getItemParams(EdsCrmContactItemParams.PHONE);
            if (phones != null && phones.size() > 0) {
                EdsCrmContactItemParams phone = EdsCrmContactItemParams.getFirstItemParam(phones, false, relation);
                if (phone != null) {
                    return phone.getValue();
                }
            }
        }
        return null;
    }

    public String getHomePhoneFirst() {
        return getParamByRelation(EdsCrmContactItemParams.HOME);
    }

    public String getMobilePhoneFirst() {
        return getParamByRelation(EdsCrmContactItemParams.MOBILE);
    }

    public String getWorkPhoneFirst() {
        return getParamByRelation(EdsCrmContactItemParams.WORK);
    }

    public String getTgUserName() {
        return getParamByRelation(EdsCrmContactItemParams.TG_USERNAME);
    }

    public String getPrimaryPhone() {
        return getContact() != null ? getContact().getPrimaryPhone() : null;
    }

    public String getFirstAvailablePhone() {
        if (getMobilePhoneFirst() != null && !getMobilePhoneFirst().isEmpty()) {
            return getMobilePhoneFirst();
        }
        if (getWorkPhoneFirst() != null && !getWorkPhoneFirst().isEmpty()) {
            return getWorkPhoneFirst();
        }
        if (getPrimaryPhone() != null && !getPrimaryPhone().isEmpty()) {
            return getPrimaryPhone();
        }
        if (getHomePhoneFirst() != null && !getHomePhoneFirst().isEmpty()) {
            return getHomePhoneFirst();
        }
        return null;
    }

    public String getPrimaryTelegram() {
        return getContact() != null ? getContact().getPrimaryTelegram() : null;
    }

    public Date getBirthDay() {
        return getProfile() != null && getProfile().getContact() != null ? getProfile().getContact().getDateOfBirth() : null;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (!ServerUtils.equalsDate(this.startDate, startDate)) {
            addHistoryChange("Hire Date", this.startDate, startDate);
        }
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (!ServerUtils.equalsDate(this.endDate, endDate)) {
            addHistoryChange("Resignation Date", this.endDate, endDate);
        }
        this.endDate = endDate;
    }

    public EdsTimeTrack getStatus() {
        return status;
    }

    public void setStatus(EdsTimeTrack status) {
        this.status = status;
    }

    public List<EdsJobFunction> getJobFunctions() {
        return jobFunctions;
    }

    public void setJobFunctions(List<EdsJobFunction> jobFunctions) {
        this.jobFunctions = jobFunctions;
    }

    public EdsReference getGrade() {
        return grade;
    }

    public void setGrade(EdsReference grade) {
        this.grade = grade;
    }

    public EdsEmployeeProfile getProfile() {
        return profile;
    }

    public void setProfile(EdsEmployeeProfile profile) {
        this.profile = profile;
    }

    public Set<EdsProject> getManagedProjects() {
        return managedProjects;
    }

    public void setManagedProjects(Set<EdsProject> managedProjects) {
        this.managedProjects = managedProjects;
    }

    public Boolean getIndexed() {
        return indexed;
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
    }

    public EdsEmployeeDepartment getEmployeeTeam() {
        return employeeDepartment;
    }

    public void setTeam(EdsDepartment team) {
        employeeDepartment = new EdsEmployeeDepartment(team, this);
        employeeDepartment.setStartDate(getCompany().getCompanyDate());
        addChange(CustomFormConstants.DEPARTMENT);
    }

    public EdsDepartment getTeam() {
        return employeeDepartment == null ? null : employeeDepartment.getTeam();
    }

    public void setEmployeeTeam(EdsEmployeeDepartment employeeTeam) {
        String oldDepartmentName = this.employeeDepartment != null && this.employeeDepartment.getTeam() != null ? this.employeeDepartment.getTeam().getName() : null;
        String newDepartmentName = employeeTeam != null && employeeTeam.getTeam() != null ? employeeTeam.getTeam().getName() : null;

        if (!ServerUtils.equalsEdsObject(this.employeeDepartment, employeeTeam)) {
            addHistoryChange("Department", oldDepartmentName, newDepartmentName);
        }

        this.employeeDepartment = employeeTeam;
        this.employeeDepartmentId = employeeTeam != null ? employeeTeam.getObjectID() : null;
    }

    public EdsResource getResource() {
        return resource;
    }

    public void setResource(EdsResource resource) {
        this.resource = resource;
    }

    private Object getstatus() {
        return null;
    }

    public String getRolesAsString(Set<EdsRole> roles) {
        if (roles == null) {
            return "";
        }
        return roles.stream().map(EdsRole::getName).collect(Collectors.joining(", "));
    }


    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    @Override
    public Date getCreationDate() {
        return getCreationTime();
    }

    @Override
    public Date getModificationDate() {
        return getLastUpdateTime();
    }


    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean hasAccess(EdsUser user) {
        return user.getCompany().getObjectID().equals(getCompany().getObjectID());
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public List<EmployeeWageClientRateHistory> getWageClientRatesHistory() {
        return wageClientRatesHistory;
    }

    public void setWageClientRatesHistory(List<EmployeeWageClientRateHistory> wageClientRatesHistory) {
        this.wageClientRatesHistory = wageClientRatesHistory;
    }

    public List<EdsEmployeeSkills> getEmployeeskills() {
        return employeeskills == null ? employeeskills = new ArrayList<>() : employeeskills;
    }

    public void setEmployeeskills(List<EdsEmployeeSkills> employeeskills) {
        this.employeeskills = employeeskills;
    }

    public String getQuickbookEmployeeID() {
        return quickbookEmployeeID;
    }

    public void setQuickbookEmployeeID(String quickbookEmployeeID) {
        this.quickbookEmployeeID = quickbookEmployeeID;
    }

    public EdsEmployeeDepartment getEmployeeDepartment() {
        return employeeDepartment;
    }

    public void setEmployeeDepartment(EdsEmployeeDepartment employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
        this.employeeDepartmentId = employeeDepartment != null ? employeeDepartment.getObjectID() : null;
    }

    public String getQuickbookEditSequence() {
        return quickbookEditSequence;
    }

    public void setQuickbookEditSequence(String quickbookEditSequence) {
        this.quickbookEditSequence = quickbookEditSequence;
    }

    public String getExternalGUID() {
        return externalGUID;
    }

    public void setExternalGUID(String externalGUID) {
        this.externalGUID = externalGUID;
    }

    public EdsCrmContact getContact() {
        return profile != null ? profile.getContact() : null;
    }

    public EdsEmployeeCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsEmployeeCustomFields customFields) {
        this.customFields = customFields;
    }

    public Set<EdsEmployeeCustomItemTable> getItemTables() {
        return itemTables;
    }

    public void setItemTables(Set<EdsEmployeeCustomItemTable> itemTables) {
        this.itemTables = itemTables;
    }

    public void addItemTable(EdsEmployeeCustomItemTable itemTable) {
        itemTables.add(itemTable);
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

    public EdsReference getQualification() {
        return qualification;
    }

    public void setQualification(EdsReference qualification) {
        if (!ServerUtils.equalsEdsObject(this.qualification, qualification)) {
            addHistoryChange("Qualification", this.qualification != null ? this.qualification.getName() : null, qualification != null ? qualification.getName() : null);
        }
        this.qualification = qualification;
    }

    public List<EdsCourse> getCourses() {
        return courses;
    }

    public void setCourses(List<EdsCourse> courses) {
        this.courses = courses;
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

    public List<EdsPaymentDeduction> getCategories() {
        return categories;
    }

    public void setCategories(List<EdsPaymentDeduction> categories) {
        this.categories = categories;
    }

    public Long getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(Long driverNumber) {
        this.driverNumber = driverNumber;
    }

    public List<EdsPayrollBatch> getPayrollBatches() {
        return payrollBatches == null ? payrollBatches = new ArrayList<>() : payrollBatches;
    }

    public void setPayrollBatches(List<EdsPayrollBatch> payrollBatch) {
        this.payrollBatches = payrollBatch;
    }

    public void addPayrollBatch(EdsPayrollBatch payrollBatch) {
        getPayrollBatches().add(payrollBatch);
    }

    public EdsCurrency getSalaryCurrency() {
        return salaryCurrency;
    }

    public void setSalaryCurrency(EdsCurrency salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }

    public EdsCountry getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(EdsCountry citizenship) {
        this.citizenship = citizenship;
    }

    public Double getOpeningBalanceDays() {
        return openingBalanceDays;
    }

    public void setOpeningBalanceDays(Double openingBalanceDays) {
        this.openingBalanceDays = openingBalanceDays;
    }

    public Double getProbationDays() {
        return probationDays == null ? probationDays = 0d : probationDays;
    }

    public void setProbationDays(Double probationDays) {
        this.probationDays = probationDays;
    }

    public Set<EdsEmployeeBenefitAllowance> getBenefitAllowance() {
        return benefitAllowance == null ? new HashSet<>() : benefitAllowance;
    }

    @Override
    public void setFirstName(String firstName) {
        if (!ServerUtils.equalsString(this.getFirstName(), firstName)) {
            addChange(CustomFormConstants.FIRST_NAME);
        }
        super.setFirstName(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        if (!ServerUtils.equalsString(this.getLastName(), lastName)) {
            addChange(CustomFormConstants.LAST_NAME);
        }
        super.setLastName(lastName);
    }

    @Override
    public void setMiddleName(String middleName) {
        if (!ServerUtils.equalsString(this.getMiddleName(), middleName)) {
            addChange(CustomFormConstants.LAST_NAME);
        }
        super.setMiddleName(middleName);
    }

    public Double getLeaveDurationHour() {
        return leaveDurationHour != null ? leaveDurationHour : 0f;
    }

    public void setLeaveDurationHour(Double yearleaveDuration) {
        if (!ServerUtils.equalsDouble(this.getLeaveDurationHour(), yearleaveDuration)) {
            addChange(CustomFormConstants.YEAR_LEAVE_HOUR_DURATION);
        }
        this.leaveDurationHour = yearleaveDuration;
    }

    public Double getLeaveDurationDay() {
        return leaveDurationDay != null ? leaveDurationDay : 0f;
    }

    public void setLeaveDurationDay(Double allleaveDuration) {
        if (!ServerUtils.equalsDouble(this.getLeaveDurationDay(), allleaveDuration)) {
            addChange(CustomFormConstants.YEAR_LEAVE_DURATION);
        }
        this.leaveDurationDay = allleaveDuration;
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            if (fieldID.equals(CustomFormConstants.FIRST_NAME)) {
                setFirstName((String) value);
            } else if (fieldID.equals(CustomFormConstants.LAST_NAME)) {
                setLastName((String) value);
            } else if (fieldID.equals(CustomFormConstants.MIDDLE_NAME)) {
                setMiddleName((String) value);
            } else if (fieldID.equals(CustomFormConstants.HIRE_DATE)) {
                setStartDate((Date) value);
            } else if (fieldID.equals(CustomFormConstants.RESIGNATION_DATE)) {
                setEndDate((Date) value);
            } else if (fieldID.equals(CustomFormConstants.GENDER)) {
                getProfile().setGender((String) value);
            } else if (fieldID.equals(CustomFormConstants.NATIONALITY)) {
                getProfile().setNationality((String) value);
            } else if (fieldID.equals(CustomFormConstants.MARTIAL_STATUS)) {
                getProfile().setMartialStatus((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.EMPLOYEE_CODE)) {
                getProfile().setEmployeeCode((String) value);
            } else if (fieldID.equals(CustomFormConstants.DEPARTMENT)) {
                if (getEmployeeDepartment() != null) {
                    getEmployeeDepartment().setTeam((EdsDepartment) value);
                    if (!ServerUtils.equalsEdsObject(getEmployeeDepartment().getTeam(), (EdsDepartment) value)) {
                        addChange(CustomFormConstants.DEPARTMENT);
                    }
                } else {
                    setTeam((EdsDepartment) value);
                }
            } else if (fieldID.equals(CustomFormConstants.ACCOUNT_STATUS)) {
                setAccountStatus((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.SUPERVISOR)) {
                getProfile().setReportsTo((EdsEmployee) value);
            } else if (fieldID.equals(CustomFormConstants.LOCATION_FIELD)) {
                setLocation((EdsLocation) value);
            } else if (fieldID.equals(CustomFormConstants.POSITION)) {
                setPosition((EdsPosition) value);
            } else if (fieldID.equals(CustomFormConstants.WAGE_RATE)) {
                setWageRate((Double) value);
            } else if (fieldID.equals(CustomFormConstants.CLIENT_CHARGE_RATE)) {
                setClientChargeRate((Double) value);
            } else if (fieldID.equals(CustomFormConstants.EMPLOYMENT_MODE)) {
                getProfile().setEmploymentMode((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.SALARY_GRADE)) {
                getProfile().setSalaryGrade((EdsGrade) value);
            } else if (fieldID.equals(CustomFormConstants.SALARY_AMOUNT)) {
                getProfile().setSalaryAmount((Double) value);
            } else if (fieldID.equals(CustomFormConstants.PAYMENT_METHOD)) {
                setPaymentMethod((String) value);
            } else if (fieldID.equals(CustomFormConstants.QUALIFICATION)) {
                setQualification((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.PASSPORT_NUMBER)) {
                getProfile().setPassportNumber((String) value);
            } else if (fieldID.equals(CustomFormConstants.PASSPORT_ISSUE_DATE)) {
                getProfile().setPassportIssueDate((Date) value);
            } else if (fieldID.equals(CustomFormConstants.PASSPORT_EXPIRY_DATE)) {
                getProfile().setPassportExpiryDate((Date) value);
            } else if (fieldID.equals(CustomFormConstants.INSURANCE_NUMBER)) {
                getProfile().setInsuranceNumber((String) value);
            } else if (fieldID.equals(CustomFormConstants.INSURANCE_EXPIRY_DATE)) {
                getProfile().setMedicalInsuranceExDate((Date) value);
            } else if (fieldID.equals(CustomFormConstants.VISA_NUMBER)) {
                getProfile().setVisaNumber((String) value);
            } else if (fieldID.equals(CustomFormConstants.VISA_ISSUE_DATE)) {
                getProfile().setVisaIssueDate((Date) value);
            } else if (fieldID.equals(CustomFormConstants.VISA_EXPIRATION_DATE)) {
                getProfile().setVisaExpirationDate((Date) value);
            } else if (fieldID.equals(CustomFormConstants.YEAR_LEAVE_DURATION)) {
                setLeaveDurationDay((Double) value);
            } else if (fieldID.equals(CustomFormConstants.YEAR_LEAVE_HOUR_DURATION)) {
                setLeaveDurationHour((Double) value);
            } else if (field.isCustomField()) {
                Object ob = CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID);
                if (ob != null) {
                    if (ob instanceof String) {
                        String text = (String) ob;
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Number) {
                        String text = String.valueOf(((Double) ob).intValue());
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Date date) {
                        if (!date.equals(value)) {
                            addChange(fieldID);
                        }
                    }
                } else {
                    addChange(fieldID);
                }
                Map<String, Object> customFieldsMap = new HashMap<>();
                customFieldsMap.put(fieldID, value);
                CustomFieldsUtils.setDomenObjectFieldChange(getCustomFields(), customFieldsMap, fieldID);
            } else {
                for (EdsAnnualLeaveAllowance annualLeaveAllowance : getAnnualLeaveAllowances()) {
                    if (fieldID.equals(annualLeaveAllowance.getReason().getCode())) {
                        if ((value instanceof Double || value instanceof String)) {
                            annualLeaveAllowance.setAllowanceDays(Double.parseDouble(String.valueOf(value)));
                        }
                    }
                }
            }
        }
        super.setValueForField(field, value);
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.EMAIL)) {
            return getEmail();
        } else if (fieldID.equals(CustomFormConstants.FIRST_NAME)) {
            return getFirstName();
        } else if (fieldID.equals(CustomFormConstants.LAST_NAME)) {
            return getLastName();
        } else if (fieldID.equals(CustomFormConstants.MIDDLE_NAME)) {
            return getMiddleName();
        } else if (fieldID.equals(CustomFormConstants.HIRE_DATE)) {
            return getStartDate();
        } else if (fieldID.equals(CustomFormConstants.RESIGNATION_DATE)) {
            return getEndDate();
        } else if (fieldID.equals(CustomFormConstants.GENDER)) {
            return getProfile().getGender();
        } else if (fieldID.equals(CustomFormConstants.NATIONALITY)) {
            return getProfile().getNationality();
        } else if (fieldID.equals(CustomFormConstants.MARTIAL_STATUS)) {
            return getProfile().getMartialStatus();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNT_STATUS)) {
            return getAccountStatus();
        } else if (fieldID.equals(CustomFormConstants.EMPLOYEE_CODE)) {
            return getEmployee().getProfile() != null ? getEmployee().getProfile().getEmployeeCode() : null;
        } else if (fieldID.equals(CustomFormConstants.DEPARTMENT)) {
            return getEmployeeDepartment() != null ? getEmployeeDepartment().getTeam() : null;
        } else if (fieldID.equals(CustomFormConstants.LOCATION_FIELD)) {
            return getLocation();
        } else if (fieldID.equals(CustomFormConstants.POSITION)) {
            return getPosition();
        } else if (fieldID.equals(CustomFormConstants.SUPERVISOR)) {
            return getProfile().getReportsTo();
        } else if (fieldID.equals(CustomFormConstants.WAGE_RATE)) {
            return getWageRate();
        } else if (fieldID.equals(CustomFormConstants.CLIENT_CHARGE_RATE)) {
            return getClientChargeRate();
        } else if (fieldID.equals(CustomFormConstants.EMPLOYMENT_MODE)) {
            return getProfile().getEmploymentMode();
        } else if (fieldID.equals(CustomFormConstants.SALARY_GRADE)) {
            return getProfile().getSalaryGrade();
        } else if (fieldID.equals(CustomFormConstants.PAYMENT_METHOD)) {
            return getPaymentMethod();
        } else if (fieldID.equals(CustomFormConstants.SALARY_AMOUNT)) {
            return getProfile().getSalaryAmount();
        } else if (fieldID.equals(CustomFormConstants.QUALIFICATION)) {
            return getQualification();
        } else if (fieldID.equals(CustomFormConstants.PASSPORT_NUMBER)) {
            return getProfile().getPassportNumber();
        } else if (fieldID.equals(CustomFormConstants.PASSPORT_ISSUE_DATE)) {
            return getProfile().getPassportIssueDate();
        } else if (fieldID.equals(CustomFormConstants.PASSPORT_EXPIRY_DATE)) {
            return getProfile().getPassportExpiryDate();
        } else if (fieldID.equals(CustomFormConstants.INSURANCE_NUMBER)) {
            return getProfile().getInsuranceNumber();
        } else if (fieldID.equals(CustomFormConstants.INSURANCE_EXPIRY_DATE)) {
            return getProfile().getMedicalInsuranceExDate();
        } else if (fieldID.equals(CustomFormConstants.VISA_NUMBER)) {
            return getProfile().getVisaNumber();
        } else if (fieldID.equals(CustomFormConstants.VISA_ISSUE_DATE)) {
            return getProfile().getVisaIssueDate();
        } else if (fieldID.equals(CustomFormConstants.VISA_EXPIRATION_DATE)) {
            return getProfile().getVisaExpirationDate();
        } else if (fieldID.equals(CustomFormConstants.YEAR_LEAVE_DURATION)) {
            return getLeaveDurationDay();
        } else if (fieldID.equals(CustomFormConstants.YEAR_LEAVE_HOUR_DURATION)) {
            return getLeaveDurationHour();
        } else if (fieldID.equals(CustomFormConstants.CREATED_DATE)) {
            return getCreationDate();
        } else if (fieldID.equals(CustomFormConstants.UPDATED_DATE)) {
            return getModificationDate();
        } else if (fieldID.equals(CustomFormConstants.BIRTH_DAY)) {
            return getBirthDay();
        } else if (fieldID.equals(CustomForm.ACCOUNT_ROLES)) {
            return getRolesAsString(getRoles());
        } else if (fieldID.equals(CustomFormConstants.PROBATION_DAYS)) {
            return getProbationDays().toString();
        } else if (fieldID.equals(CustomFormConstants.PHONE)) {
            return getPrimaryPhone();
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getCustomFields() != null ? CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID) : "";
        } else {
            for (EdsAnnualLeaveAllowance annualLeaveAllowance : getAnnualLeaveAllowances()) {
                if (annualLeaveAllowance.getReason() != null && fieldID.equals(annualLeaveAllowance.getReason().getCode())) {
                    return annualLeaveAllowance.getAllowanceDays();
                }
            }
        }
        return super.getRealValue(fieldID);
    }

    public String getChangedField(String fieldId, Object changedValue) {
        if (fieldId != null) {
            if (CustomFormConstants.SALARY_AMOUNT.equals(fieldId)) {
                if (getProfile() != null) {
                    String salaryAmount = String.valueOf(getProfile().getSalaryAmount());
                    String changedAmount = String.valueOf(changedValue);
                    return !changedAmount.equals(salaryAmount) ? (CustomFormConstants.SALARY_AMOUNT + ",") : "";
                }
            } else if (CustomFormConstants.BIRTH_DAY.equals(fieldId)) {
                Date changedBirthDay = changedValue != null ? (Date) changedValue : null;
                return !ServerUtils.equalsDate(changedBirthDay, getBirthDay()) ? (CustomFormConstants.BIRTH_DAY + ",") : "";
            }
        }
        return "";
    }

    public SolrInputDocument indexToSolr(Integer companyID, EdsUserBankAccount userBankAccount, boolean isIntegerNumberEnabled) {
        SolrInputDocument doc = new SolrInputDocument();
        String compositID = companyID + "_" + getObjectID();
        doc.addField(SolrEmployeeRepresenter.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrEmployeeRepresenter.FIELD_COMPANY_ID, companyID);
        if (getProfile() != null) {
            doc.addField(SolrEmployeeRepresenter.FIELD_EMPLOYEE_NUMBER, getProfile().getEmployeeCode());
            String code = getProfile().getEmployeeCode();
            if (code != null && !"".equals(code) && isIntegerNumberEnabled) {
                doc.addField(SolrEmployeeRepresenter.FIELD_EMPLOYEE_INTEGER_NUMBER, Long.parseLong(code.replaceAll("[\\D]", "")));
            }
        }
        doc.addField(SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID, getObjectID());
        doc.addField(SolrEmployeeRepresenter.FIELD_EMPLOYEE_NAME, getFullName());
        doc.addField(SolrEmployeeRepresenter.FIELD_EMPLOYEE_FIRST_NAME, getFirstName());
        doc.addField(SolrEmployeeRepresenter.FIELD_EMPLOYEE_LAST_NAME, getLastName());
        doc.addField(SolrEmployeeRepresenter.FIELD_EMPLOYEE_MIDDLE_NAME, getMiddleName());
        doc.addField(SolrEmployeeRepresenter.FIELD_PHONE_NUMBER, getPrimaryPhone());
        if (getContact() != null && getContact().getPrimaryEmail() != null && !"".equals(getContact().getPrimaryEmail())) {
            if (getAccountStatus() != null && EMPLOYEE_STATUS_NO_ACCCESS.equals(getAccountStatus().getCode())) {
                doc.addField(SolrEmployeeRepresenter.FIELD_EMAIL, getContact().getPrimaryEmail());
            } else {
                doc.addField(SolrEmployeeRepresenter.FIELD_EMAIL, getEmail());
            }
        }

        if (getPosition() != null) {
            doc.addField(SolrEmployeeRepresenter.FIELD_POSITION_ID, getPosition().getObjectID());
            String positionaRealName = getPosition().getRealPositionName();
            doc.addField(SolrEmployeeRepresenter.FIELD_POSITION_NAME, positionaRealName);
            EdsReferenceLocale locale = getPosition().getLocale();
            doc.addField(SolrEmployeeRepresenter.FIELD_POSITION_NAME_UZ, locale != null && locale.getUzbek() != null ? locale.getUzbek() : positionaRealName);
            doc.addField(SolrEmployeeRepresenter.FIELD_POSITION_NAME_RU, locale != null && locale.getRussian() != null ? locale.getRussian() : positionaRealName);
            doc.addField(SolrEmployeeRepresenter.FIELD_POSITION_NAME_EN, locale != null && locale.getEnglish() != null ? locale.getEnglish() : positionaRealName);
            doc.addField(SolrEmployeeRepresenter.FIELD_POSITION_NAME_AR, locale != null && locale.getArabic() != null ? locale.getArabic() : positionaRealName);
            doc.addField(SolrEmployeeRepresenter.FIELD_POSITION_ID_NAME, getPosition().getObjectID() + SolrEmployeeRepresenter.SPLIT + positionaRealName);
        }
        if (getContact() != null) {
            doc.addField(SolrEmployeeRepresenter.FIELD_CONTACT_ID, getContact().getObjectID());
            doc.addField(SolrEmployeeRepresenter.FIELD_CONTACT_NAME, getContact().getName());
            doc.addField(SolrEmployeeRepresenter.FIELD_CONTACT_IN_NAME, getContact().getObjectID() + SolrEmployeeRepresenter.SPLIT + getContact().getName());
        }
        if (getTimeSlot() != null) {
            doc.addField(SolrEmployeeRepresenter.FIELD_TIMESLOT_ID, getTimeSlot().getObjectID());
            doc.addField(SolrEmployeeRepresenter.FIELD_TIMESLOT_NAME, getTimeSlot().getName());
            doc.addField(SolrEmployeeRepresenter.FIELD_TIMESLOT_ID_NAME, getTimeSlot().getObjectID() + SolrEmployeeRepresenter.SPLIT + getTimeSlot().getName());
        }
        if (getPosition() != null && getPosition().getType() != null) {
            doc.addField(SolrEmployeeRepresenter.FIELD_POSITION_TYPE_ID, getPosition().getType().getObjectID());
            doc.addField(SolrEmployeeRepresenter.FIELD_POSITION_TYPE_NAME, getPosition().getType().getName());
            String uz = getPosition().getType().getOriginalName();
            String ru = getPosition().getType().getOriginalName();
            String en = getPosition().getType().getOriginalName();
            String ar = getPosition().getType().getOriginalName();
            if (getPosition().getType().getLocale() != null) {
                uz = getPosition().getType().getLocale().getUzbek();
                ru = getPosition().getType().getLocale().getRussian();
                en = getPosition().getType().getLocale().getEnglish();
                ar = getPosition().getType().getLocale().getArabic();
            }
            doc.addField(SolrEmployeeRepresenter.FIELD_POSITION_TYPE_NAME_AR, ar);
            doc.addField(SolrEmployeeRepresenter.FIELD_POSITION_TYPE_NAME_EN, en);
            doc.addField(SolrEmployeeRepresenter.FIELD_POSITION_TYPE_NAME_RU, ru);
            doc.addField(SolrEmployeeRepresenter.FIELD_POSITION_TYPE_NAME_UZ, uz);

            doc.addField(SolrEmployeeRepresenter.FIELD_POSITION_TYPE_ID_NAME, getPosition().getType().getObjectID() + SolrEmployeeRepresenter.SPLIT + getPosition().getType().getName());
        }

        if (getRoles() != null) {
            StringBuilder roleAll = new StringBuilder();
            for (EdsRole role : getRolesSorted()) {
                doc.addField(SolrEmployeeRepresenter.FIELD_ROLE_ID, role.getObjectID());
                doc.addField(SolrEmployeeRepresenter.FIELD_ROLE_NAME, role.getName());
                doc.addField(SolrEmployeeRepresenter.FIELD_ROLE_CODE, role.getCode());
                doc.addField(SolrEmployeeRepresenter.FIELD_ROLE_ID_NAME, role.getObjectID() + SolrEmployeeRepresenter.SPLIT + role.getName());
                roleAll.append(role.getName());
            }
            doc.addField(SolrEmployeeRepresenter.FIELD_ROLE_NAME_ALL, roleAll.toString());
        }
        if (getAccountStatus() != null) {
            doc.addField(SolrEmployeeRepresenter.FIELD_STATUS_ID, getAccountStatus().getObjectID());
            doc.addField(SolrEmployeeRepresenter.FIELD_STATUS_NAME, getAccountStatus().getName());
            doc.addField(SolrEmployeeRepresenter.FIELD_STATUS_CODE, getAccountStatus().getCode());
            doc.addField(SolrEmployeeRepresenter.FIELD_STATUS_ID_NAME, getAccountStatus().getObjectID() + SolrEmployeeRepresenter.SPLIT + getAccountStatus().getName());
            doc.addField(SolrEmployeeRepresenter.FIELD_STATUS_ID_CODE, getAccountStatus().getObjectID() + SolrEmployeeRepresenter.SPLIT + getAccountStatus().getCode());
        }
        if (getLocation() != null) {
            doc.addField(SolrEmployeeRepresenter.FIELD_LOCATION_ID, getLocation().getObjectID());
            String locationRealName = getLocation().getLocationRealName();
            doc.addField(SolrEmployeeRepresenter.FIELD_LOCATION_NAME, locationRealName);
            EdsReferenceLocale locale = getLocation().getLocale();
            doc.addField(SolrEmployeeRepresenter.FIELD_LOCATION_NAME_AR, locale != null && locale.getArabic() != null ? locale.getArabic() : locationRealName);
            doc.addField(SolrEmployeeRepresenter.FIELD_LOCATION_NAME_EN, locale != null && locale.getEnglish() != null ? locale.getEnglish() : locationRealName);
            doc.addField(SolrEmployeeRepresenter.FIELD_LOCATION_NAME_UZ, locale != null && locale.getUzbek() != null ? locale.getUzbek() : locationRealName);
            doc.addField(SolrEmployeeRepresenter.FIELD_LOCATION_NAME_RU, locale != null && locale.getRussian() != null ? locale.getRussian() : locationRealName);
            doc.addField(SolrEmployeeRepresenter.FIELD_LOCATION_ID_NAME, getLocation().getObjectID() + SolrEmployeeRepresenter.SPLIT + getLocation().getLocationRealName());
            if (getLocation().getState() != null) {
                doc.addField(SolrEmployeeRepresenter.FIELD_LOCATION_STATE, getLocation().getState().getName());
            }
            doc.addField(SolrEmployeeRepresenter.FIELD_LOCATION_CITY, getLocation().getCity());
        }

        if (getEmployeeDepartment() != null && getEmployeeDepartment().getTeam() != null) {
            doc.addField(SolrEmployeeRepresenter.FIELD_DEPARTMENT_ID, getEmployeeDepartment().getTeam().getObjectID());
            String departmentRealName = getEmployeeDepartment().getTeam().getRealDepartmentName();
            doc.addField(SolrEmployeeRepresenter.FIELD_DEPARTMENT_NAME, departmentRealName);
            EdsReferenceLocale locale = getEmployeeDepartment().getTeam().getLocale();
            doc.addField(SolrEmployeeRepresenter.FIELD_DEPARTMENT_NAME_AR, locale != null && locale.getArabic() != null ? locale.getArabic() : departmentRealName);
            doc.addField(SolrEmployeeRepresenter.FIELD_DEPARTMENT_NAME_EN, locale != null && locale.getEnglish() != null ? locale.getEnglish() : departmentRealName);
            doc.addField(SolrEmployeeRepresenter.FIELD_DEPARTMENT_NAME_UZ, locale != null && locale.getUzbek() != null ? locale.getUzbek() : departmentRealName);
            doc.addField(SolrEmployeeRepresenter.FIELD_DEPARTMENT_NAME_RU, locale != null && locale.getRussian() != null ? locale.getRussian() : departmentRealName);

            doc.addField(SolrEmployeeRepresenter.FIELD_DEPARTMENT_ID_NAME, getEmployeeDepartment().getTeam().getObjectID() + SolrEmployeeRepresenter.SPLIT + getEmployeeDepartment().getTeam().getRealDepartmentName());
        }
        if (getQualification() != null) {
            doc.addField(SolrEmployeeRepresenter.FIELD_QUALIFICATION_ID, getQualification().getObjectID());
            doc.addField(SolrEmployeeRepresenter.FIELD_QUALIFICATION_NAME, getQualification().getName());
            doc.addField(SolrEmployeeRepresenter.FIELD_QUALIFICATION_ID_NAME, getQualification().getObjectID() + SolrEmployeeRepresenter.SPLIT + getQualification().getName());
        }
        if (getDriverNumber() != null) {
            doc.addField(SolrEmployeeRepresenter.FIELD_DRIVER_ID, getDriverNumber().toString());
        }
        if (getProfile() != null) {
            if (getProfile().getReportsTo() != null) {
                doc.addField(SolrEmployeeRepresenter.FIELD_SUPERVISOR_ID, getProfile().getReportsTo().getObjectID());
                doc.addField(SolrEmployeeRepresenter.FIELD_SUPERVISOR_NAME, getProfile().getReportsTo().getFullName());
                doc.addField(SolrEmployeeRepresenter.FIELD_SUPERVISOR_ID_NAME, getProfile().getReportsTo().getObjectID() + SolrEmployeeRepresenter.SPLIT + getProfile().getReportsTo().getFullName());
            }
            if (getProfile().getCountry() != null) {
                doc.addField(SolrEmployeeRepresenter.FIELD_PASSPORT_ISSUED_ID, getProfile().getCountry().getObjectID());
                doc.addField(SolrEmployeeRepresenter.FIELD_PASSPORT_ISSUED_BY, getProfile().getCountry().getName());
            }
            SpokenLanguagesManager languagesManager = StaticContextAccessor.getBean(SpokenLanguagesManager.class);
            List<EdsSpokenLanguages> languages = languagesManager.getListByRelation(getObjectID(), EdsSpokenLanguages.TYPE_EMPLOYEE);
            if (languages != null && languages.size() > 0) {
                for (EdsSpokenLanguages language : languages) {
                    EdsReference lang = language.getLanguage();
                    if (lang != null) {
                        doc.addField(SolrEmployeeRepresenter.FIELD_LANGUAGE_ID, lang.getObjectID());
                        doc.addField(SolrEmployeeRepresenter.FIELD_LANGUAGE_NAME, lang.getName());
                        doc.addField(SolrEmployeeRepresenter.FIELD_LANGUAGE_CODE, lang.getCode());
                        doc.addField(SolrEmployeeRepresenter.FIELD_LANGUAGE_ID_NAME, lang.getObjectID() + SolrEmployeeRepresenter.SPLIT + lang.getName());
                    }
                }
            }

            doc.addField(SolrEmployeeRepresenter.FIELD_PASSPORT_NUMBER, getProfile().getPassportNumber());
            doc.addField(SolrEmployeeRepresenter.FIELD_PASSPORT_ISSUE_DATE, getProfile().getPassportIssueDate());
            doc.addField(SolrEmployeeRepresenter.FIELD_PASSPORT_EXPIRE_DATE, getProfile().getPassportExpiryDate());
            doc.addField(SolrEmployeeRepresenter.FIELD_INSURANCE_NUMBER, getProfile().getInsuranceNumber());
            doc.addField(SolrEmployeeRepresenter.FIELD_INSURANCE_EXPIRY_DATE, getProfile().getMedicalInsuranceExDate());
            doc.addField(SolrEmployeeRepresenter.FIELD_VISA_NUMBER, getProfile().getVisaNumber());
            doc.addField(SolrEmployeeRepresenter.FIELD_VISA_ISSUE_DATE, getProfile().getVisaIssueDate());
            doc.addField(SolrEmployeeRepresenter.FIELD_VISA_EXPIRE_DATE, getProfile().getVisaExpirationDate());
            doc.addField(SolrEmployeeRepresenter.FIELD_GENDER_NAME, getProfile().getGender());
            doc.addField(SolrEmployeeRepresenter.FIELD_MARTIAL_STATUS_ID, getProfile().getMartialStatus() != null ? getProfile().getMartialStatus().getObjectID() : null);
            doc.addField(SolrEmployeeRepresenter.FIELD_SALARY_AMOUNT, getProfile().getSalaryAmount() != null ? getProfile().getSalaryAmount().doubleValue() : null);
        }
        if (getEmployeeskills().size() > 0) {
            for (EdsEmployeeSkills skill : getEmployeeskills()) {
                if (skill.getSkill() != null) {
                    doc.addField(SolrEmployeeRepresenter.FIELD_SKILL_ID, skill.getSkill().getObjectID());
                    doc.addField(SolrEmployeeRepresenter.FIELD_SKILL_NAME, skill.getSkill() != null ? skill.getSkill().getName() : "");
                    doc.addField(SolrEmployeeRepresenter.FIELD_SKILL_ID_NAME, skill.getSkill().getObjectID() + SolrEmployeeRepresenter.SPLIT + skill.getSkill().getName());
                }
            }
        }
        if (userBankAccount != null) {
            doc.addField(SolrEmployeeRepresenter.FIELD_AGENT_NAME, userBankAccount.getAgentID());
            doc.addField(SolrEmployeeRepresenter.FIELD_BANK_NAME, userBankAccount.getBankName());
            doc.addField(SolrEmployeeRepresenter.FIELD_ACCOUNT_NUMBER, userBankAccount.getAccountNumber());
            doc.addField(SolrEmployeeRepresenter.FIELD_ACCOUNT_NAME, userBankAccount.getAccountName());
            doc.addField(SolrEmployeeRepresenter.FIELD_BANK_ADDRESS, userBankAccount.getBankAddress());
            doc.addField(SolrEmployeeRepresenter.FIELD_SWIFT_CODE, userBankAccount.getSwiftCode());
            doc.addField(SolrEmployeeRepresenter.FIELD_SORT_CODE, userBankAccount.getSortCode());
            doc.addField(SolrEmployeeRepresenter.FIELD_IBAN_CODE, userBankAccount.getIbanCode());
        }
        doc.addField(SolrEmployeeRepresenter.FIELD_CREATED_DATE, getCreationDate());
        doc.addField(SolrEmployeeRepresenter.FIELD_LAST_UPDATE_DATE, getLastUpdateTime());
        doc.addField(SolrEmployeeRepresenter.FIELD_BIRTH_DATE, getBirthDay());
        doc.addField(SolrEmployeeRepresenter.FIELD_HIRE_DATE, getStartDate());
        doc.addField(SolrEmployeeRepresenter.FIELD_END_DATE, getEndDate());
        doc.addField(SolrEmployeeRepresenter.FIELD_WAGE_RATE, getWageRate());
        doc.addField(SolrEmployeeRepresenter.FIELD_CLIENT_CHARGE_RATE, getClientChargeRate());

        if (getSalaryCurrency() != null) {
            doc.addField(SolrEmployeeRepresenter.FIELD_CURRENCY_ID, getSalaryCurrency().getObjectID());
            doc.addField(SolrEmployeeRepresenter.FIELD_CURRENCY_NAME, getSalaryCurrency().getName());
            doc.addField(SolrEmployeeRepresenter.FIELD_CURRENCY_ID_NAME, getSalaryCurrency().getObjectID() + SolrEmployeeRepresenter.SPLIT + getSalaryCurrency().getName());
        }

        if (getContact() != null && getContact().getPrimaryAddressFromAll() != null) {
            Address primaryAddress = getContact().getPrimaryAddressFromAll();
            if (primaryAddress.getCountryId() != null) {
                doc.addField(SolrEmployeeRepresenter.FIELD_COUNTRY_ID, primaryAddress.getCountryId());
                doc.addField(SolrEmployeeRepresenter.FIELD_COUNTRY_NAME, primaryAddress.getCountry());
                doc.addField(SolrEmployeeRepresenter.FIELD_COUNTRY_CODE, primaryAddress.getCountryCode());
                doc.addField(SolrEmployeeRepresenter.FIELD_COUNTRY_ID_CODE, primaryAddress.getCountryId() + SolrEmployeeRepresenter.SPLIT + primaryAddress.getCountryCode());
                doc.addField(SolrEmployeeRepresenter.FIELD_COUNTRY_ID_CODE_NAME, primaryAddress.getCountryId() + SolrEmployeeRepresenter.SPLIT + primaryAddress.getCountryCode() + SolrEmployeeRepresenter.SPLIT + primaryAddress.getCountry());
            }
            if (primaryAddress.getStateId() != null) {
                doc.addField(SolrEmployeeRepresenter.FIELD_STATE_ID, primaryAddress.getStateId());
                doc.addField(SolrEmployeeRepresenter.FIELD_STATE_NAME, primaryAddress.getState());
                doc.addField(SolrEmployeeRepresenter.FIELD_STATE_ID_NAME, primaryAddress.getStateId() + SolrEmployeeRepresenter.SPLIT + primaryAddress.getState());
            }
            doc.addField(SolrEmployeeRepresenter.FIELD_CITY, primaryAddress.getCity());
            doc.addField(SolrEmployeeRepresenter.FIELD_STREET, primaryAddress.getAddress());
            doc.addField(SolrEmployeeRepresenter.FIELD_STREET2, primaryAddress.getAddressb());
            doc.addField(SolrEmployeeRepresenter.FIELD_POST_CODE, primaryAddress.getZipCode());
        }

        if (getPayrollBatches() != null && !getPayrollBatches().isEmpty()) {
            for (EdsPayrollBatch batch : getPayrollBatches()) {
                doc.addField(SolrEmployeeRepresenter.FIELD_PAYROLL_BATCH_ID, batch.getObjectID());
            }
        }
        doc.addField(SolrEmployeeRepresenter.FIELD_OPENING_BALANCE_DAYS, getOpeningBalanceDays());
        doc.addField(SolrEmployeeRepresenter.FIELD_PROBATION_DAYS, getProbationDays());


        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());
        return doc;
    }

    public EmployeeSolrItem getSolrRPC() {
        EmployeeSolrItem employeeSolrItem = new EmployeeSolrItem();

        employeeSolrItem.setObjectId(getObjectID());
        if (getProfile().getRPC() != null) {
            EdsEmployeeProfile profile = getProfile();
            String code = profile.getEmployeeCode();
            employeeSolrItem.setEmployeeNumber(code);
            if (profile.getReportsTo() != null) {
                EdsEmployee reports = profile.getReportsTo();
                employeeSolrItem.setSupervisor(new SelectItem(reports.getObjectID(), reports.getFullName()));
            }
            employeeSolrItem.setPassportNumber(profile.getPassportNumber());
            employeeSolrItem.setPassportIssueDate(profile.getPassportIssueDate());
            employeeSolrItem.setPassportExpireDate(profile.getPassportExpiryDate());

            employeeSolrItem.setInsuranceNumber(profile.getInsuranceNumber());
            employeeSolrItem.setVisaNumber(profile.getVisaNumber());
            employeeSolrItem.setVisaIssueDate(profile.getVisaIssueDate());
            employeeSolrItem.setVisaExpireDate(profile.getVisaExpirationDate());
            employeeSolrItem.setInsuranceExpiryDate(profile.getMedicalInsuranceExDate());
            employeeSolrItem.setGenderName(profile.getGender());
        }
        employeeSolrItem.setEmployeeName(getFullName());
        employeeSolrItem.setFirstName(getFirstName());
        employeeSolrItem.setLastName(getLastName());
        employeeSolrItem.setMiddleName(getMiddleName());
        employeeSolrItem.setPhoneNumber(getPrimaryPhone());
        if (getContact() != null && getContact().getPrimaryEmail() != null && !"".equals(getContact().getPrimaryEmail())) {
            if (getAccountStatus() != null && EMPLOYEE_STATUS_NO_ACCCESS.equals(getAccountStatus().getCode())) {
                employeeSolrItem.setEmail(getContact().getPrimaryEmail());
            } else {
                employeeSolrItem.setEmail(getEmail());
            }
        }
        if (getPosition() != null) {
            employeeSolrItem.setPosition(getPosition().getAsSelectItem());
            EdsReferenceLocale locale = getPosition().getLocale();
            if (locale != null) {
                employeeSolrItem.setPositionName(locale.toRPC());
            }
        }
        if (getPosition() != null && getPosition().getType() != null) {
            EdsReference positionType = getPosition().getType();
            employeeSolrItem.setPositionType(positionType.getAsSelectItem());
            if (positionType.getLocale() != null) {
                ReferenceLocale locale = positionType.getLocale().toRPC();
                employeeSolrItem.setPositionTypeLocale(locale);
            }
        }
        if (getRoles() != null) {
            getRoles().forEach(role -> {
                SelectItem employeeRole = new SelectItem(role.getObjectID(), role.getName());
                employeeRole.setCode(role.getCode());
                employeeSolrItem.getRole().add(employeeRole);
            });
        }
        if (getEmployeeskills() != null && getEmployeeskills().size() > 0) {
            for (EdsEmployeeSkills skill : getEmployeeskills()) {
                if (skill.getSkill() != null) {
                    employeeSolrItem.getSkill().add(skill.getSkill().getAsSelectItem());
                }
            }
        }
        if (getAccountStatus() != null) {
            SelectItem status = new SelectItem(getAccountStatus().getObjectID(), getAccountStatus().getName());
            status.setCode(getAccountStatus().getCode());
            employeeSolrItem.setStatus(status);
        }
        if (getLocation() != null) {
            employeeSolrItem.setLocation(getLocation().getAsSelectItem());
            if (getLocation().getState() != null) {
                employeeSolrItem.setLocationState(getLocation().getState().getName());
            }
            employeeSolrItem.setLocationCity(getLocation().getCity());
            EdsReferenceLocale locale = getLocation().getLocale();
            if (locale != null) {
                employeeSolrItem.setLocationName(locale.toRPC());
            }
        }
        if (getEmployeeDepartment() != null && getEmployeeDepartment().getTeam() != null) {
            EdsDepartment department = getEmployeeDepartment().getTeam();
            employeeSolrItem.setDepartment(department.getAsSelectItem());
            EdsReferenceLocale locale = department.getLocale();
            if (locale != null) {
                employeeSolrItem.setDepartmentName(locale.toRPC());
            }

        }
        if (getQualification() != null) {
            employeeSolrItem.setQualification(getQualification().getAsSelectItem());
        }
        if (getContact() != null) {
            employeeSolrItem.setContact(getContact().getAsSelectItem());
        }
        if (getTimeSlot() != null) {
            employeeSolrItem.setTimeslot(getTimeSlot().getAsSelectItem());
        }
        if (getQualification() != null) {
            employeeSolrItem.setQualification(getQualification().getAsSelectItem());
        }

        if (getDriverNumber() != null) {
            employeeSolrItem.setDriverId(getDriverNumber().toString());
        }
        employeeSolrItem.setWageRate(getWageRate());
        employeeSolrItem.setClientChargeRate(getClientChargeRate());
        if (getContact() != null && getContact().getPrimaryAddressFromAll() != null) {
            Address primaryAddress = getContact().getPrimaryAddressFromAll();
            if (primaryAddress.getCountryId() != null) {
                SelectItem country = new SelectItem(primaryAddress.getCountryId(), primaryAddress.getCountry());
                country.setCode(primaryAddress.getCountryCode());
                employeeSolrItem.setCountry(country);
            }
            if (primaryAddress.getStateId() != null) {
                employeeSolrItem.setState(new SelectItem(primaryAddress.getStateId(), primaryAddress.getState()));
            }
            employeeSolrItem.setCity(primaryAddress.getCity());
            employeeSolrItem.setStreet(primaryAddress.getAddress());
            employeeSolrItem.setStreet2(primaryAddress.getAddressb());
            employeeSolrItem.setPostCode(primaryAddress.getZipCode());
        }
        employeeSolrItem.setCreatedDate(getCreationDate());
        employeeSolrItem.setLastUpdate(getLastUpdateTime());
        employeeSolrItem.setBirthDate(getBirthDay());
        employeeSolrItem.setHireDate(getStartDate());
        employeeSolrItem.setEndDate(getEndDate());
        if (getSalaryCurrency() != null) {
            EdsCurrency currency = getSalaryCurrency();
            employeeSolrItem.setCurrency(currency.getAsSelectItem());
        }
        if (getPayrollBatches() != null && getPayrollBatches().size() > 0) {
            getPayrollBatches().forEach(batch -> {
                employeeSolrItem.getPayrollBatchId().add(batch.getObjectID());
            });
        }
        employeeSolrItem.setOpeningBalanceDays(getOpeningBalanceDays());
        employeeSolrItem.setProbationDays(getProbationDays());

        return employeeSolrItem;
    }

    public void setWorkflowValues(String key, Object value) {
        if (key != null && value != null) {
            if (key.equals(CustomFormConstants.FIRST_NAME) && value instanceof String) {
                setFirstName((String) value);
            } else if (key.equals(CustomFormConstants.LAST_NAME) && value instanceof String) {
                setLastName((String) value);
            } else if (key.equals(CustomFormConstants.SALARY_AMOUNT) && (value instanceof Double || value instanceof String)) {
                Double sal = null;
                if (value instanceof Double) {
                    sal = (Double) value;
                } else if (value instanceof String) {
                    try {
                        sal = Double.parseDouble((String) value);
                    } catch (NumberFormatException e) {
                    }
                }
                getProfile().setSalaryAmount(sal);
            } else if (key.equals(CustomFormConstants.PASSPORT_NUMBER) && value instanceof String) {
                getProfile().setPassportNumber((String) value);
            } else if (key.equals(CustomFormConstants.PASSPORT_ISSUE_DATE) && value instanceof Date) {
                getProfile().setPassportIssueDate((Date) value);
            } else if (key.equals(CustomFormConstants.PASSPORT_EXPIRY_DATE) && value instanceof Date) {
                getProfile().setPassportExpiryDate((Date) value);
            } else if (key.equals(CustomFormConstants.INSURANCE_NUMBER) && value instanceof String) {
                getProfile().setInsuranceNumber((String) value);
            } else if (key.equals(CustomFormConstants.INSURANCE_EXPIRY_DATE) && value instanceof Date) {
                getProfile().setMedicalInsuranceExDate((Date) value);
            } else if (key.equals(CustomFormConstants.VISA_NUMBER) && value instanceof String) {
                getProfile().setVisaNumber((String) value);
            } else if (key.equals(CustomFormConstants.VISA_ISSUE_DATE) && value instanceof Date) {
                getProfile().setVisaIssueDate((Date) value);
            } else if (key.equals(CustomFormConstants.VISA_EXPIRATION_DATE) && value instanceof Date) {
                getProfile().setVisaExpirationDate((Date) value);
            } else if (key.contains("string_value") && value instanceof String) {
                Map<String, Object> customFieldsMap = new HashMap<>();
                customFieldsMap.put(key, value);
                CustomFieldsUtils.setDomenObjectFieldChange(getCustomFields(), customFieldsMap, key);
            } else if (key.contains("double_value") && value instanceof Double) {
                Map<String, Object> customFieldsMap = new HashMap<>();
                customFieldsMap.put(key, value);
                CustomFieldsUtils.setDomenObjectFieldChange(getCustomFields(), customFieldsMap, key);
            } else if (key.contains("date_value") && value instanceof Date) {
                Map<String, Object> customFieldsMap = new HashMap<>();
                customFieldsMap.put(key, value);
                CustomFieldsUtils.setDomenObjectFieldChange(getCustomFields(), customFieldsMap, key);
            }
        }
    }

    public Integer getImportFileID() {
        return importFileID;
    }

    public void setImportFileID(Integer importFileID) {
        this.importFileID = importFileID;
    }

    public Integer getEmployeeDepartmentId() {
        return employeeDepartmentId;
    }

    public void setEmployeeDepartmentId(Integer employeeDepartmentId) {
        this.employeeDepartmentId = employeeDepartmentId;
    }

    public Set<EdsSickRequest> getLeaveRequestOfBackupEmployee() {
        return leaveRequestOfBackupEmployee;
    }

    public void setLeaveRequestOfBackupEmployee(Set<EdsSickRequest> leaveRequestOfBackupEmployee) {
        this.leaveRequestOfBackupEmployee = leaveRequestOfBackupEmployee;
    }

    public EdsPlacement getPlacement() {
        return this.placement;
    }

    public void setPlacement(final EdsPlacement placement) {
        this.placement = placement;
    }

    public List<EdsWarehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<EdsWarehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public Set<EdsEmployeeExperienceItemTable> getExperienceItemTables() {
        return experienceItemTables;
    }

    public void setExperienceItemTables(Set<EdsEmployeeExperienceItemTable> experienceItemTables) {
        this.experienceItemTables = experienceItemTables;
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
}
