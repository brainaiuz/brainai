package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsPositionCustomFields;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.rpc.PositionSolrItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPositionRepresenter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "position")
public class EdsPosition extends EdsTraceable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "externalGUID")
    private String externalGUID;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "positionNameId")
    private EdsReference positionName;

    @Type(type = "text")
    @Column(name = "description")
    private String description;

    private Double averageRate;

    private Double averageClientchargeRate;

    @Column(name = "isDeleted")
    private Boolean deleted = false;

    @Column(name = "code")
    private String code;

    @Column(name = "intNumber")
    private Integer intNumber;

    @Column(name = "numberData")
    private String numberData;

    @Column(name = "regulartemp")
    private Integer regularTemp;

    @Column(name = "fullparttime")
    private Integer fullPartTime;

    @Column(name = "salarygrade")
    private Integer salaryGrade;

    @Column(name = "established")
    private Date established;

    @Column(name = "available")
    private Date available;

    @Column(name = "enddate")
    private Date enddate;

    @Column(name = "status")
    private Integer status;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "status", updatable = false, insertable = false)
    private EdsReference statusEnt;

    @Column(name = "positionapprover")
    private Integer positionApprover;

    @Column(name = "numberplannedincumbents")
    private Integer numberPlannedIncumbents;

    @Column(name = "department")
    private Integer department;

    @Column(name = "jobFamily")
    private Integer jobFamily;

    @Column(name = "reportsto")
    private Integer reportsTo;

    @Column(name = "regularregion")
    private Integer regularRegion;

    @Column(name = "jobEvaluationPoints")
    private String jobEvalPoints;

    @Column(name = "jobPurpose")
    private String jobPurpose;

    @Type(type = "text")
    @Column(name = "primaryResponse")
    private String primaryResponse;

    @Type(type = "text")
    @Column(name = "workingConditions")
    private String workingConditions;


    @Type(type = "text")
    @Column(name = "expirence")
    private String expirence;

    @Type(type = "text")
    @Column(name = "skills")
    private String skills;

    @Type(type = "text")
    @Column(name = "qualification")
    private String qualification;

    @Type(type = "text")
    @Column(name = "personalAttributes")
    private String personalAttr;

    @Column(name = "streetaddress")
    private String streetAddress;

    @Column(name = "budgetedhours")
    private Float budgetedHours;

    @Column(name = "bhperiod")
    private Integer bhperiod;

    @Column(name = "bpperiod")
    private Integer bpperiod;

    @Column(name = "budgetedpay")
    private Float budgetedPay;

    @Column(name = "annualcost")
    private Float annualCost;

    @Column(precision = 11, scale = 5)
    private BigDecimal wageRate;

    @Column(precision = 11, scale = 5)
    private BigDecimal clientChargeRate;

    @Column(precision = 11, scale = 5)
    private BigDecimal minSalary;

    @Column(precision = 11, scale = 5)
    private BigDecimal midSalary;

    @Column(precision = 11, scale = 5)
    private BigDecimal maxSalary;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localeId")
    private EdsReferenceLocale locale;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsPositionCustomFields customFields;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "positionID")
    private Set<EdsPositionBenefitAllowance> benefitAllowances;


    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "department", updatable = false, insertable = false)
    private EdsDepartment departmentObject;

    @Column(name = "locationId")
    private Integer locationId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "locationId", updatable = false, insertable = false)
    private EdsLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    private EdsReference type;

    private String counter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @Column(name = "creationTime")
    private Date creationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @Type(type = "text")
    private String responsibility;

    @Type(type = "text")
    @Column(name = "responsibility_localization")
    private String responsibilityLocalize;

    @Type(type = "text")
    @Column(name = "jobrequirements")
    private String jobrequirements;

    @Type(type = "text")
    @Column(name = "job_requirements_localization")
    private String jobRequirementsLocalize;

    @Type(type = "text")
    private String detailingDescription;

    @Type(type = "text")
    @Column(name = "description_localization")
    private String descriptionLocalize;


    @Type(type = "text")
    @Column(name = "measuring_employee_performance")
    private String measuringEmployeePerformance;

    @Type(type = "text")
    @Column(name = "measuring_employee_performance_localization")
    private String measuringEmployeePerformanceLocalize;


    @Type(type = "text")
    @Column(name = "personal_qualities")
    private String personalQualities;

    @Type(type = "text")
    @Column(name = "personal_qualities_localization")
    private String personalQualitiesLocalize;


    @Type(type = "text")
    @Column(name = "knowledge")
    private String knowledge;

    @Type(type = "text")
    @Column(name = "knowledge_localization")
    private String knowledgeLocalize;


    private Double coefficient;

    @Column(name = "salaryBasis")
    private String salaryBasis;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        if (getLocale() != null) {
            String lang = ServerUtils.getUserLocale().getLanguage();
            if (StringUtils.isNotBlank(getLocale().getLocaleByCode(lang))) {
                return getLocale().getLocaleByCode(lang);
            }
        }
        return name;
    }

    public String getOriginalName() {
        return name;
    }

    public String getRealPositionName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Double getAverageRate() {
        return averageRate != null ? averageRate : 0.0;
    }

    public void setAverageRate(Double averageRate) {
        this.averageRate = averageRate;
    }

    public Double getAverageClientchargeRate() {
        return averageClientchargeRate != null ? averageClientchargeRate : 0.0;
    }

    public void setAverageClientchargeRate(Double averageClientchargeRate) {
        this.averageClientchargeRate = averageClientchargeRate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getFullPartTime() {
        return fullPartTime;
    }

    public void setFullPartTime(Integer fullparttime) {
        this.fullPartTime = fullparttime;
    }

    public Integer getSalaryGrade() {
        return salaryGrade;
    }

    public void setSalaryGrade(Integer salarygrade) {
        this.salaryGrade = salarygrade;
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

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public EdsReference getStatus() {
        return statusEnt;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setStatusEnt(EdsReference statusEnt) {
        this.statusEnt = statusEnt;
    }

    public Integer getPositionApprover() {
        return positionApprover;
    }

    public void setPositionApprover(Integer positionapprover) {
        this.positionApprover = positionapprover;
    }

    public Integer getNumberplannedincumbents() {
        return numberPlannedIncumbents;
    }

    public void setNumberPlannedIncumbents(Integer numberplannedincumbents) {
        this.numberPlannedIncumbents = numberplannedincumbents;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public Integer getJobFamily() {
        return jobFamily;
    }

    public void setJobFamily(Integer jobFamily) {
        this.jobFamily = jobFamily;
    }

    public Integer getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(Integer reportsTo) {
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

    public String getPrimaryResponse() {
        return primaryResponse;
    }

    public void setPrimaryResponse(String primaryResponse) {
        this.primaryResponse = primaryResponse;
    }

    public String getWorkingConditions() {
        return workingConditions;
    }

    public void setWorkingConditions(String workingConditions) {
        this.workingConditions = workingConditions;
    }

    public String getExpirence() {
        return expirence;
    }

    public void setExpirence(String expirence) {
        this.expirence = expirence;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getPersonalAttr() {
        return personalAttr;
    }

    public void setPersonalAttr(String personalAttr) {
        this.personalAttr = personalAttr;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public Float getBudgetedHours() {
        return budgetedHours;
    }

    public void setBudgetedHours(Float budgetedHours) {
        this.budgetedHours = budgetedHours;
    }

    public Integer getBHPeriod() {
        return bhperiod;
    }

    public void setBHPeriod(Integer bhperiod) {
        this.bhperiod = bhperiod;
    }

    public Integer getBPPeriod() {
        return bpperiod;
    }

    public void setBPPeriod(Integer bpperiod) {
        this.bpperiod = bpperiod;
    }

    public Float getBudgetedPay() {
        return budgetedPay;
    }

    public void setBudgetedPay(Float budgetedPay) {
        this.budgetedPay = budgetedPay;
    }

    public Float getAnnualCost() {
        return annualCost;
    }

    public void setAnnualCost(Float annualCost) {
        this.annualCost = annualCost;
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

    public Set<EdsPositionBenefitAllowance> getBenefitAllowances() {
        if (benefitAllowances == null) {
            benefitAllowances = new HashSet<>();
        }
        return benefitAllowances;
    }

    public void setBenefitAllowances(Set<EdsPositionBenefitAllowance> benefitAllowances) {
        this.benefitAllowances = benefitAllowances;
    }

    public EdsPositionCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsPositionCustomFields customFields) {
        this.customFields = customFields;
    }

    public PositionItem getRPC() {
        PositionItem item = new PositionItem();
        if (getDepartmentObject() != null) {
            item.setDepartment(new SelectItem(getDepartmentObject().getObjectID(), getDepartmentObject().getName()));
            item.setDepartmentLocale(getDepartmentObject().getLocale() != null ? getDepartmentObject().getLocale().toRPC() : null);
        }
        if (getLocation() != null) {
            item.setLocation(new SelectItem(getLocation().getObjectID(), getLocation().getName()));
        }
        if (getLocale() != null) {
            item.setPositionLocale(getLocale().toRPC());
        }

        item.setObjectID(getObjectID());
        item.setName(getName());
        item.setPositionCode(getCode());
        item.setWageRate(getWageRate());
        item.setClientChargeRate(getClientChargeRate());
        item.setCount(getCount());
        return item;
    }

    public PositionSolrItem getSolrRPC() {
        PositionSolrItem item = new PositionSolrItem();

        item.setObjectId(getObjectID());
        item.setName(getName());
        item.setNumber(getNumberData());
        item.setVacantCount(Integer.valueOf(getCount()));
        if (getCreator() != null) {
            item.setCreatedDate(getCreationTime());
            item.setCreatedBy(getCreator().getAsSelectItem());
        }

        item.setModifiedDate(getLastUpdateTime());
        if (getUpdater() != null) {
            item.setModifiedBy(getUpdater().getAsSelectItem());
        }

        if (getLocale() != null) {
            item.setNameLocale(getLocale().toRPC());
        }

        if (getDepartmentObject() != null) {
            item.setDepartment(getDepartmentObject().getAsSelectItem());
            if (getDepartmentObject().getLocale() != null) {
                item.setDepartmentLocale(getDepartmentObject().getLocale().toRPC());
            }
        }

        if (getStatus() != null) {
            EdsReference status = getStatus();
            item.setStatus(status.getAsSelectItem());
            if (status.getLocale() != null) {
                item.setStatusLocale(status.getLocale().toRPC());
            }
        }

        if (getType() != null) {
            EdsReference type = getType();
            item.setType(type.getAsSelectItem());
            if (type.getLocale() != null) {
                item.setTypeLocale(type.getLocale().toRPC());
            }
        }

        if (getLocation() != null) {
            item.setLocation(getLocation().getAsSelectItem());
        }

        return item;
    }

    public EdsReferenceLocale getLocale() {
        return locale;
    }

    public void setLocale(EdsReferenceLocale locale) {
        this.locale = locale;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getNumberData() {
        return numberData;
    }

    public void setNumberData(String numberData) {
        this.numberData = numberData;
    }

    public String getExternalGUID() {
        return externalGUID;
    }

    public void setExternalGUID(String externalGUID) {
        this.externalGUID = externalGUID;
    }

    public EdsDepartment getDepartmentObject() {
        return departmentObject;
    }

    public void setDepartmentObject(EdsDepartment departmentObject) {
        this.departmentObject = departmentObject;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        this.location = location;
    }

    public String getCount() {
        return counter;
    }

    public void setCount(String count) {
        this.counter = count;
    }

    public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
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

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getResponsibilityLocalize() {
        return responsibilityLocalize;
    }

    public void setResponsibilityLocalize(String responsibilityLocalize) {
        this.responsibilityLocalize = responsibilityLocalize;
    }

    public String getJobrequirements() {
        return jobrequirements;
    }

    public void setJobrequirements(String jobrequirements) {
        this.jobrequirements = jobrequirements;
    }

    public String getJobRequirementsLocalize() {
        return jobRequirementsLocalize;
    }

    public void setJobRequirementsLocalize(String jobRequirementsLocalize) {
        this.jobRequirementsLocalize = jobRequirementsLocalize;
    }

    public String getDetailingDescription() {
        return detailingDescription;
    }

    public void setDetailingDescription(String detailingDescription) {
        this.detailingDescription = detailingDescription;
    }

    public String getDescriptionLocalize() {
        return descriptionLocalize;
    }

    public void setDescriptionLocalize(String descriptionLocalize) {
        this.descriptionLocalize = descriptionLocalize;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public String getKnowledgeLocalize() {
        return knowledgeLocalize;
    }

    public void setKnowledgeLocalize(String knowledgeLocalize) {
        this.knowledgeLocalize = knowledgeLocalize;
    }

    public String getPersonalQualitiesLocalize() {
        return personalQualitiesLocalize;
    }

    public void setPersonalQualitiesLocalize(String personalQualitiesLocalize) {
        this.personalQualitiesLocalize = personalQualitiesLocalize;
    }

    public String getPersonalQualities() {
        return personalQualities;
    }

    public void setPersonalQualities(String personalQualities) {
        this.personalQualities = personalQualities;
    }

    public String getMeasuringEmployeePerformanceLocalize() {
        return measuringEmployeePerformanceLocalize;
    }

    public void setMeasuringEmployeePerformanceLocalize(String measuringEmployeePerformanceLocalize) {
        this.measuringEmployeePerformanceLocalize = measuringEmployeePerformanceLocalize;
    }

    public String getMeasuringEmployeePerformance() {
        return measuringEmployeePerformance;
    }

    public void setMeasuringEmployeePerformance(String measuringEmployeePerformance) {
        this.measuringEmployeePerformance = measuringEmployeePerformance;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public EdsReference getPositionName() {
        return positionName;
    }

    public void setPositionName(EdsReference positionName) {
        this.positionName = positionName;
    }

    public String getSalaryBasis() {
        return salaryBasis;
    }

    public void setSalaryBasis(String salaryBasis) {
        this.salaryBasis = salaryBasis;
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID.equals(CustomFormConstants.DESCRIPTION)) {
            return getDescription();
        } else if (fieldID.equals(CustomFormConstants.POSITIONS.POSITION_CODE)) {
            return getNumberData();
        } else if (fieldID.equals(CustomFormConstants.POSITIONS.POSITION_TITLE)) {
            return getName();
        } else if (fieldID.equals(CustomFormConstants.POSITIONS.STATUS)) {
            return getStatus();
        } else if (fieldID.equals(CustomFormConstants.POSITIONS.JOB_FAMILY_PANEL)) {
            return getJobFamily();
        } else if (fieldID.equals(CustomFormConstants.POSITIONS.CLIENT_CHARGE_RATE)) {
            return getClientChargeRate();
        } else if (fieldID.equals(CustomFormConstants.POSITIONS.WAGE_RATE)) {
            return getWageRate();
        }
        return super.getRealValue(fieldID);
    }

    public SolrInputDocument wrapToSolrDocument(Integer companyID, ArrayList<Integer> members) {
        String compositeId = companyID + "_" + getObjectID();
        SolrInputDocument doc = new SolrInputDocument();

        doc.addField(SolrPositionRepresenter.FIELD_COMPOSITE_ID, compositeId);
        doc.addField(SolrPositionRepresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrPositionRepresenter.FIELD_POSITION_ID, getObjectID());
        doc.addField(SolrPositionRepresenter.FIELD_NAME, getName());
        doc.addField(SolrPositionRepresenter.FIELD_NUMBER, getNumberData());
        doc.addField(SolrPositionRepresenter.FIELD_MODIFIED_DATE, getLastUpdateTime());
        doc.addField(SolrPositionRepresenter.FIELD_CREATED_DATE, getCreationTime());
        doc.addField(SolrPositionRepresenter.FIELD_EMPLOYEE_COUNT, getCount());
        doc.addField(SolrPositionRepresenter.FIELD_VACANT_COUNT, members != null ? members.size() : 0);

        String en = getName();
        String ru = getName();
        String ar = getName();
        String uz = getName();
        if (getPositionName() != null && getPositionName().getLocale() != null) {
            en = getPositionName().getLocale().getEnglish() != null ? getPositionName().getLocale().getEnglish() : getName();
            ru = getPositionName().getLocale().getRussian() != null ? getPositionName().getLocale().getRussian() : getName();
            ar = getPositionName().getLocale().getArabic() != null ? getPositionName().getLocale().getArabic() : getName();
            uz = getPositionName().getLocale().getUzbek() != null ? getPositionName().getLocale().getUzbek() : getName();
        } else if (getLocale() != null) {
            en = getLocale().getEnglish() != null ? getLocale().getEnglish() : getName();
            ru = getLocale().getRussian() != null ? getLocale().getRussian() : getName();
            ar = getLocale().getArabic() != null ? getLocale().getArabic() : getName();
            uz = getLocale().getUzbek() != null ? getLocale().getUzbek() : getName();
        }
        doc.addField(SolrPositionRepresenter.FIELD_NAME_EN, en);
        doc.addField(SolrPositionRepresenter.FIELD_NAME_RU, ru);
        doc.addField(SolrPositionRepresenter.FIELD_NAME_AR, ar);
        doc.addField(SolrPositionRepresenter.FIELD_NAME_UZ, uz);

        if (getStatus() != null) {
            doc.addField(SolrPositionRepresenter.FIELD_STATUS_ID, getStatus().getObjectID());
            doc.addField(SolrPositionRepresenter.FIELD_STATUS_NAME, getStatus().getName());
            doc.addField(SolrPositionRepresenter.FIELD_STATUS_CODE, getStatus().getCode());
            doc.addField(SolrPositionRepresenter.FIELD_STATUS_ID_NAME, getStatus().getObjectID() + SolrPositionRepresenter.SPLIT + getStatus().getName());
            if (getStatus().getLocale() != null) {
                doc.addField(SolrPositionRepresenter.FIELD_STATUS_EN, getStatus().getLocale().getEnglish() != null ? getStatus().getLocale().getEnglish() : getStatus().getName());
                doc.addField(SolrPositionRepresenter.FIELD_STATUS_RU, getStatus().getLocale().getRussian() != null ? getStatus().getLocale().getRussian() : getStatus().getName());
                doc.addField(SolrPositionRepresenter.FIELD_STATUS_UZ, getStatus().getLocale().getUzbek() != null ? getStatus().getLocale().getUzbek() : getStatus().getName());
                doc.addField(SolrPositionRepresenter.FIELD_STATUS_AR, getStatus().getLocale().getArabic() != null ? getStatus().getLocale().getArabic() : getStatus().getName());
            }

        }

        if (getLocation() != null) {
            doc.addField(SolrPositionRepresenter.FIELD_LOCATION_ID, getLocation().getObjectID());
            String locationFormattedName = getLocation().getCode() != null ? getLocation().getCode() + "->" + getLocation().getName() : getLocation().getName();
            doc.addField(SolrPositionRepresenter.FIELD_LOCATION_NAME, locationFormattedName);
            doc.addField(SolrPositionRepresenter.FIELD_LOCATION_ID_NAME, getLocation().getObjectID() + SolrPositionRepresenter.SPLIT + locationFormattedName);
        }

        if (getDepartmentObject() != null) {
            doc.addField(SolrPositionRepresenter.FIELD_DEPARTMENT_ID, getDepartmentObject().getObjectID());
            doc.addField(SolrPositionRepresenter.FIELD_DEPARTMENT_NAME, getDepartmentObject().getName());
            doc.addField(SolrPositionRepresenter.FIELD_DEPARTMENT_ID_NAME, getDepartmentObject().getObjectID() + SolrPositionRepresenter.SPLIT + getDepartmentObject().getNumberData() + "->" + getDepartmentObject().getName());

            String departmentEn = getDepartmentObject().getName();
            String departmentRu = getDepartmentObject().getName();
            String departmentAr = getDepartmentObject().getName();
            String departmentUz = getDepartmentObject().getName();

            if (getDepartmentObject().getDepartmentName() != null && getDepartmentObject().getDepartmentName().getLocale() != null) {
                EdsReferenceLocale departmentLocale = getDepartmentObject().getDepartmentName().getLocale();
                departmentEn = departmentLocale.getEnglish() != null ? departmentLocale.getEnglish() : getDepartmentObject().getName();
                departmentRu = departmentLocale.getRussian() != null ? departmentLocale.getRussian() : getDepartmentObject().getName();
                departmentAr = departmentLocale.getArabic() != null ? departmentLocale.getArabic() : getDepartmentObject().getName();
                departmentUz = departmentLocale.getUzbek() != null ? departmentLocale.getUzbek() : getDepartmentObject().getName();

            } else if (getDepartmentObject().getLocale() != null) {
                departmentEn = getDepartmentObject().getLocale().getEnglish() != null ? getDepartmentObject().getLocale().getEnglish() : getDepartmentObject().getName();
                departmentRu = getDepartmentObject().getLocale().getRussian() != null ? getDepartmentObject().getLocale().getRussian() : getDepartmentObject().getName();
                departmentAr = getDepartmentObject().getLocale().getArabic() != null ? getDepartmentObject().getLocale().getArabic() : getDepartmentObject().getName();
                departmentUz = getDepartmentObject().getLocale().getUzbek() != null ? getDepartmentObject().getLocale().getUzbek() : getDepartmentObject().getName();
            }

            doc.addField(SolrPositionRepresenter.FIELD_DEPARTMENT_NAME_EN, departmentEn);
            doc.addField(SolrPositionRepresenter.FIELD_DEPARTMENT_NAME_RU, departmentRu);
            doc.addField(SolrPositionRepresenter.FIELD_DEPARTMENT_NAME_AR, departmentAr);
            doc.addField(SolrPositionRepresenter.FIELD_DEPARTMENT_NAME_UZ, departmentUz);
        }

        if (getCreator() != null) {
            doc.addField(SolrPositionRepresenter.FIELD_CREATED_BY_ID, getCreator().getObjectID());
            doc.addField(SolrPositionRepresenter.FIELD_CREATED_BY_NAME, getCreator().getFullName());
            doc.addField(SolrPositionRepresenter.FIELD_CREATED_BY_ID_NAME, getCreator().getObjectID() + SolrPositionRepresenter.SPLIT + getCreator().getFullName());
        }

        if (getUpdater() != null) {
            doc.addField(SolrPositionRepresenter.FIELD_MODIFIED_BY_ID, getUpdater().getObjectID());
            doc.addField(SolrPositionRepresenter.FIELD_MODIFIED_BY_NAME, getUpdater().getFullName());
            doc.addField(SolrPositionRepresenter.FIELD_MODIFIED_BY_ID_NAME, getUpdater().getObjectID() + SolrPositionRepresenter.SPLIT + getUpdater().getFullName());
        }

        if (getType() != null) {
            doc.addField(SolrPositionRepresenter.FIELD_TYPE_ID, getType().getObjectID());
            doc.addField(SolrPositionRepresenter.FIELD_TYPE_NAME, getType().getName());
            doc.addField(SolrPositionRepresenter.FIELD_TYPE_CODE, getType().getCode());
            doc.addField(SolrPositionRepresenter.FIELD_TYPE_ID_NAME, getType().getObjectID() + SolrPositionRepresenter.SPLIT + getType().getName());

            String typeEn = getType().getName();
            String typeRu = getType().getName();
            String typeAr = getType().getName();
            String typeUz = getType().getName();
            if (getType().getLocale() != null) {
                typeEn = getType().getLocale().getEnglish() != null ? getType().getLocale().getEnglish() : getType().getName();
                typeRu = getType().getLocale().getRussian() != null ? getType().getLocale().getRussian() : getType().getName();
                typeAr = getType().getLocale().getArabic() != null ? getType().getLocale().getArabic() : getType().getName();
                typeUz = getType().getLocale().getUzbek() != null ? getType().getLocale().getUzbek() : getType().getName();
            }
            doc.addField(SolrPositionRepresenter.FIELD_TYPE_NAME_EN, typeEn);
            doc.addField(SolrPositionRepresenter.FIELD_TYPE_NAME_RU, typeRu);
            doc.addField(SolrPositionRepresenter.FIELD_TYPE_NAME_AR, typeAr);
            doc.addField(SolrPositionRepresenter.FIELD_TYPE_NAME_UZ, typeUz);
        }

        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());
        return doc;
    }
}
