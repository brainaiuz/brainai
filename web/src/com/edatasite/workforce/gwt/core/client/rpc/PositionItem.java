package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.AnnualLeaveItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PositionItem extends SelectItem implements IsSerializable, ListingCustomFields {

    public static String LOCATION = "location";
    public static String TYPE = "type";
    public static String CREATED_BY = "createdBy";
    public static String CREATED_DATE = "createdDate";
    public static String MODIFIED_BY = "modifiedBy";
    public static String MODIFIED_DATE = "modifiedDate";
    public static String DEPARTMENT = "department";
    public static String ACTION = "action";
    public static String POSITION_TITLE = "position title";
    public static String POSITION_CODE = "code";
    public static String DESCRIPTION = "description";
    public static String STATUS = "status";
    public static String EMPLOYEE_COUNT = "employee_count";
    public static String POSITION_COUNT = "position_count";
    public static String WAGE_RATE = "Wage Rate (hourly)";
    public static String CLIENT_CHARGE_RATE = "Client Charge Rate (hourly)";
    HashMap<Integer, AnnualLeaveItem> leaveitems = new HashMap<>();
    HashMap<Integer, AnnualLeaveItem> benefititems = new HashMap<>();
    private String position;
    private Boolean isDeleted;
    private NumberData numberData;
    private Integer codeId, objectID, positionId, locationId, departmentId, vacantCountId, typeId, intNumber, salaryGrade, coefficentId;
    private String prefix, positionCode,externalGUID;
    private String createdBy;
    private String modifiedBy;
    private String salaryBasis;
    private Date modifiedDate,established;
    private Date createdDate;
    private Date available,endDate;
    private SelectItem status,fullPartTime;
    private Integer employeeCount,headCount,objectId;

    private Integer requestedCount;
    private SelectItem creatorLocation;
    private SelectItem creatorDepartment;
    private String vacantCount;
    private SelectItem department,reportsTo;
    private String workingConditions,primaryResponse;
    private String jobEvalPoints,jobPurpose,expirence;
    private String skills,qualification,personalAttr,streetAddress;
    private Float budgetedHours,budgetedPay,annualCost;
    private SelectItem bhperiod,bpperiod,jobfamily;
    private Boolean deleted;
    private Integer employeeId;
    private SelectItem[] posStatus;
    private ArrayList<KpiTreeInfo> members;
    private EmployeeListItem[] employeesData;
    private SelectItem[] templates;
    private SelectItem[] timeTypes;
    private SelectItem[] jobFamilies;
    private SelectItem[] teams;
    private SelectItem[] weekMonth;
    private BigDecimal wageRate,clientChargeRate;
    private BigDecimal minSalary, midSalary, maxSalary;
    private Boolean applyLeaveForEmployees, applyBenefitForEmployees;
    private ReferenceLocale localeItem;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;
    private SelectItem location;
    private String count;
    private ReferenceLocale departmentLocale;
    private ReferenceLocale positionLocale;
    private SelectItem type;
    private HashMap<String, String> descriptionLocalize;
    private HashMap<String, String> jobRequirementLocalize;
    private HashMap<String, String> responsibilitiesLocalize;
    private String jobRequirements;
    private String positionDescription;
    private String responsibility;
    private HashMap<String, String> measuringEmployeePerformanceLocalize;
    private HashMap<String, String> personalQualitiesLocalize;
    private HashMap<String, String> knowledgeLocalize;
    private String measuringEmployeePerformance;
    private String personalQualities;
    private String knowledge;
    private Double coefficent;

    private SelectItem positionName;

    private Integer positionRefId;


    public PositionItem() {
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public static void setLOCATION(String LOCATION) {
        PositionItem.LOCATION = LOCATION;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public ReferenceLocale getLocaleItem() {
        return localeItem;
    }

    public void setLocaleItem(ReferenceLocale localeItem) {
        this.localeItem = localeItem;
    }

    public PositionItem(Integer id, String name) {
        super(id, name);
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer positionId) {
        this.objectID = positionId;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }
    public String getPosition() {
        return position;
    }

    public SelectItem getFullPartTime() {
        return fullPartTime;
    }

    public void setFullPartTime(SelectItem fullPartTime) {
        this.fullPartTime = fullPartTime;
    }

    public Integer getSalaryGrade() {
        return salaryGrade;
    }

    public void setSalaryGrade(Integer salaryGrade) {
        this.salaryGrade = salaryGrade;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Date getEstablished() {
        return established;
    }

    public void setEstablished(Date established) {
        this.established = established;
    }

    public Date getAvailable() {
        return available;
    }

    public void setAvailable(Date available) {
        this.available = available;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public String getVacantCount() {
        return vacantCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    public SelectItem getJobfamily() {
        return jobfamily;
    }

    public void setJobfamily(SelectItem jobfamily) {
        this.jobfamily = jobfamily;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public SelectItem getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(SelectItem reportsTo) {
        this.reportsTo = reportsTo;
    }

    public String getJobEvalPoints() {
        return jobEvalPoints;
    }

    public void setJobEvalPoints(String jobEvalPoints) {
        this.jobEvalPoints = jobEvalPoints;
    }

    public String getJobPurpose() {
        return jobPurpose;
    }

    public void setJobPurpose(String jobPurpose) {
        this.jobPurpose = jobPurpose;
    }

    public String getPrimaryResponsibilities() {
        return primaryResponse;
    }

    public void setPrimaryResponsibilities(String primaryRespons) {
        this.primaryResponse = primaryRespons;
    }

    public String getWorkingConditions() {
        return workingConditions;
    }

    public void setWorkingConditions(String workingCond) {
        this.workingConditions = workingCond;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public String getExpirence() {
        return expirence;
    }

    public void setExpirence(String text) {
        this.expirence = text;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String text) {
        this.skills = text;
    }

    public String getPersonalAttributes() {
        return personalAttr;
    }

    public void setPersonalAttributes(String text) {
        this.personalAttr = text;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPrimaryResponse() {
        return primaryResponse;
    }

    public void setPrimaryResponse(String primaryResponse) {
        this.primaryResponse = primaryResponse;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Float getBudgetedHours() {
        return budgetedHours;
    }

    public void setBudgetedHours(Float budgetedHours) {
        this.budgetedHours = budgetedHours;
    }

    public Float getBudgetedPay() {
        return budgetedPay;
    }

    public void setBudgetedPay(Float budgetedPay) {
        this.budgetedPay = budgetedPay;
    }

    public SelectItem getBHPeriod() {
        return bhperiod;
    }

    public void setBHPeriod(SelectItem bhperiod) {
        this.bhperiod = bhperiod;
    }

    public SelectItem getBPPeriod() {
        return bpperiod;
    }

    public void setBPPeriod(SelectItem bpperiod) {
        this.bpperiod = bpperiod;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getPersonalAttr() {
        return personalAttr;
    }

    public void setPersonalAttr(String personalAttr) {
        this.personalAttr = personalAttr;
    }

    public Float getAnnualCost() {
        return annualCost;
    }

    public void setAnnualCost(Float annualCost) {
        this.annualCost = annualCost;
    }

    public SelectItem[] getPosStatus() {
        return posStatus;
    }

    public void setPosStatus(SelectItem[] posStatus) {
        this.posStatus = posStatus;
    }

    public ArrayList<KpiTreeInfo> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<KpiTreeInfo> members) {
        this.members = members;
    }

    public EmployeeListItem[] getEmployeesData() {
        return employeesData;
    }

    public void setEmployeesData(EmployeeListItem[] employeesData) {
        this.employeesData = employeesData;
    }

    public SelectItem[] getTimeTypes() {
        return timeTypes;
    }

    public void setTimeTypes(SelectItem[] timeTypes) {
        this.timeTypes = timeTypes;
    }

    public SelectItem[] getJobFamilies() {
        return jobFamilies;
    }

    public void setJobFamilies(SelectItem[] jobFamilies) {
        this.jobFamilies = jobFamilies;
    }

    public SelectItem[] getTeams() {
        return teams;
    }

    public void setTeams(SelectItem[] teams) {
        this.teams = teams;
    }

    public SelectItem[] getWeekMonth() {
        return weekMonth;
    }

    public void setWeekMonth(SelectItem[] weekMonth) {
        this.weekMonth = weekMonth;
    }

    public HashMap<Integer, AnnualLeaveItem> getLeaveitems() {
        return leaveitems;
    }

    public void setLeaveitems(HashMap<Integer, AnnualLeaveItem> leaveitems) {
        this.leaveitems = leaveitems;
    }

    public Boolean getApplyLeaveForEmployees() {
        return applyLeaveForEmployees;
    }

    public void setApplyLeaveForEmployees(Boolean applyLeaveForEmployees) {
        this.applyLeaveForEmployees = applyLeaveForEmployees;
    }

    public Boolean getApplyBenefitForEmployees() {
        return applyBenefitForEmployees;
    }

    public void setApplyBenefitForEmployees(Boolean applyBenefitForEmployees) {
        this.applyBenefitForEmployees = applyBenefitForEmployees;
    }

    public BigDecimal getWageRate() {
        return wageRate;
    }

    public void setWageRate(BigDecimal wageRate) {
        this.wageRate = wageRate;
    }

    public BigDecimal getClientChargeRate() {
        return clientChargeRate;
    }

    public void setClientChargeRate(BigDecimal clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMidSalary() {
        return midSalary;
    }

    public void setMidSalary(BigDecimal midSalary) {
        this.midSalary = midSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public HashMap<Integer, AnnualLeaveItem> getBenefititems() {
        return benefititems;
    }

    public void setBenefititems(HashMap<Integer, AnnualLeaveItem> benefititems) {
        this.benefititems = benefititems;
    }


    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public HashMap<String, Object> getCustomFieldValuesItems() {
        return customFieldValues;
    }

    public void setCustomFieldValuesItems(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }


    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFieldValues.get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFieldValues.put(columnCodeKey, cellValue);
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public String getExternalGUID() {
        return externalGUID;
    }

    public void setExternalGUID(String externalGUID) {
        this.externalGUID = externalGUID;
    }

    public Integer getHeadCount() {
        if(headCount==null){
            headCount=0;
        }
        return headCount;
    }

    public void setHeadCount(Integer headCount) {
        if(headCount==null){
            headCount=0;
        }
        this.headCount = headCount;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem locationId) {
        this.location = locationId;
    }

    public String getCount() {
        if(count==null){
            count="0";
        }
        return count;
    }

    public void setCount(String count) {
        if(count==null){
            count="0";
        }

        this.count = count;
    }


    public Integer getRequestedCount() {
        return requestedCount;
    }

    public void setRequestedCount(Integer requestedCount) {
        this.requestedCount = requestedCount;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public ReferenceLocale getDepartmentLocale() {
        return departmentLocale;
    }

    public void setDepartmentLocale(ReferenceLocale departmentLocale) {
        this.departmentLocale = departmentLocale;
    }

    public ReferenceLocale getPositionLocale() {
        return positionLocale;
    }

    public void setPositionLocale(ReferenceLocale positionLocale) {
        this.positionLocale = positionLocale;
    }

    public Integer getCodeId() {
        return this.codeId;
    }

    public void setCodeId(final Integer codeId) {
        this.codeId = codeId;
    }

    public Integer getPositionId() {
        return this.positionId;
    }

    public void setPositionId(final Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getLocationId() {
        return this.locationId;
    }

    public void setLocationId(final Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getDepartmentId() {
        return this.departmentId;
    }

    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getVacantCountId() {
        return this.vacantCountId;
    }

    public void setVacantCountId(final Integer vacantCountId) {
        this.vacantCountId = vacantCountId;
    }

    public Integer getTypeId() {
        return this.typeId;
    }

    public void setTypeId(final Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getCoefficentId() {
        return this.coefficentId;
    }

    public void setCoefficentId(final Integer coefficentId) {
        this.coefficentId = coefficentId;
    }

    public SelectItem getCreatorLocation() {
        return creatorLocation;
    }
    public void setCreatorLocation(SelectItem creatorLocation) {
        this.creatorLocation = creatorLocation;
    }

    public SelectItem getCreatorDepartment() {
        return creatorDepartment;
    }

    public void setCreatorDepartment(SelectItem creatorDepartment) {
        this.creatorDepartment = creatorDepartment;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public Integer getIntNumber() {
        return this.intNumber;
    }

    public void setIntNumber(final Integer intNumber) {
        this.intNumber = intNumber;
    }

    public SelectItem getType() {
        return type;
    }

    public void setType(SelectItem type) {
        this.type = type;
    }

    public HashMap<String, String> getDescriptionLocalize() {
        return descriptionLocalize;
    }

    public void setDescriptionLocalize(HashMap<String, String> descriptionLocalize) {
        this.descriptionLocalize = descriptionLocalize;
    }

    public HashMap<String, String> getJobRequirementLocalize() {
        return jobRequirementLocalize;
    }

    public void setJobRequirementLocalize(HashMap<String, String> jobRequirementLocalize) {
        this.jobRequirementLocalize = jobRequirementLocalize;
    }

    public HashMap<String, String> getResponsibilitiesLocalize() {
        return responsibilitiesLocalize;
    }

    public void setResponsibilitiesLocalize(HashMap<String, String> responsibilitiesLocalize) {
        this.responsibilitiesLocalize = responsibilitiesLocalize;
    }

    public String getJobRequirements() {
        return jobRequirements;
    }

    public void setJobRequirements(String jobRequirements) {
        this.jobRequirements = jobRequirements;
    }

    public String getPositionDescription() {
        return positionDescription;
    }

    public void setPositionDescription(String positionDescription) {
        this.positionDescription = positionDescription;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public Double getCoefficent() {
        return coefficent;
    }

    public void setCoefficent(Double coefficent) {
        this.coefficent = coefficent;
    }

    public SelectItem getPositionName() {
        return positionName;
    }

    public void setPositionName(SelectItem positionName) {
        this.positionName = positionName;
    }

    public Integer getPositionRefId() {
        return positionRefId;
    }

    public void setPositionRefId(Integer positionRefId) {
        this.positionRefId = positionRefId;
    }

    public String getSalaryBasis() {
        return salaryBasis;
    }

    public void setSalaryBasis(String salaryBasis) {
        this.salaryBasis = salaryBasis;
    }

    public HashMap<String, String> getMeasuringEmployeePerformanceLocalize() {
        return measuringEmployeePerformanceLocalize;
    }

    public void setMeasuringEmployeePerformanceLocalize(HashMap<String, String> measuringEmployeePerformanceLocalize) {
        this.measuringEmployeePerformanceLocalize = measuringEmployeePerformanceLocalize;
    }

    public HashMap<String, String> getPersonalQualitiesLocalize() {
        return personalQualitiesLocalize;
    }

    public void setPersonalQualitiesLocalize(HashMap<String, String> personalQualitiesLocalize) {
        this.personalQualitiesLocalize = personalQualitiesLocalize;
    }

    public HashMap<String, String> getKnowledgeLocalize() {
        return knowledgeLocalize;
    }

    public void setKnowledgeLocalize(HashMap<String, String> knowledgeLocalize) {
        this.knowledgeLocalize = knowledgeLocalize;
    }

    public String getMeasuringEmployeePerformance() {
        return measuringEmployeePerformance;
    }

    public void setMeasuringEmployeePerformance(String measuringEmployeePerformance) {
        this.measuringEmployeePerformance = measuringEmployeePerformance;
    }

    public String getPersonalQualities() {
        return personalQualities;
    }

    public void setPersonalQualities(String personalQualities) {
        this.personalQualities = personalQualities;
    }
}
