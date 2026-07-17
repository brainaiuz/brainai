package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:26.
 */
@SolrDocument(collection = "employeeCore")
public class EmployeeSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("employeeId")
    @Indexed(name = "employeeId", type = "pint", required = true)
    private Integer employeeId;

    @Field("employeeNumber")
    private String employeeNumber;

    @Field("employeeIntegerNumber")
    private Long employeeIntegerNumber;

    @Field("employeeName")
    private String employeeName;

    @Field("firstName")
    private String firstName;

    @Field("lastName")
    private String lastName;

    @Field("phoneNumber")
    private String phoneNumber;

    @Field("email")
    private String email;

    @Field("positionId")
    private Integer positionId;

    @Field("positionName")
    private String positionName;

    @Field("positionIdName")
    @Indexed(name = "positionIdName", type = "string", stored = false)
    private String positionIdName;

    @Field("roleId")
    @Indexed(name = "roleId", type = "pints")
    private List<Integer> roleId = new ArrayList<>();

    @Field("roleName")
    @Indexed(name = "roleName", type = "strings")
    private List<String> roleName = new ArrayList<>();

    @Field("roleCode")
    @Indexed(name = "roleCode", type = "strings")
    private List<String> roleCode = new ArrayList<>();

    @Field("roleIdName")
    @Indexed(name = "roleIdName", type = "strings", stored = false)
    private List<String> roleIdName = new ArrayList<>();

    @Field("languageId")
    @Indexed(name = "languageId", type = "pints")
    private List<Integer> languageId = new ArrayList<>();

    @Field("languageName")
    @Indexed(name = "languageName", type = "strings")
    private List<String> languageName = new ArrayList<>();

    @Field("languageCode")
    @Indexed(name = "languageCode", type = "strings")
    private List<String> languageCode = new ArrayList<>();

    @Field("languageIdName")
    @Indexed(name = "languageIdName", type = "strings", stored = false)
    private List<String> languageIdName = new ArrayList<>();

    @Field("skillId")
    @Indexed(name = "skillId", type = "pints")
    private List<Integer> skillId = new ArrayList<>();

    @Field("skillName")
    @Indexed(name = "skillName", type = "strings")
    private List<String> skillName = new ArrayList<>();

    @Field("skillIdName")
    @Indexed(name = "skillIdName", type = "strings", stored = false)
    private List<String> skillIdName = new ArrayList<>();

    @Field("statusId")
    private Integer statusId;

    @Field("statusName")
    private String statusName;

    @Field("statusCode")
    private String statusCode;

    @Field("statusIdName")
    @Indexed(name = "statusIdName", type = "string", stored = false)
    private String statusIdName;

    @Field("locationId")
    private Integer locationId;

    @Field("locationName")
    private String locationName;

    @Field("locationIdName")
    @Indexed(name = "locationIdName", type = "string", stored = false)
    private String locationIdName;

    @Field("locationState")
    @Indexed(name = "locationState", type = "string", stored = false)
    private String locationState;

    @Field("locationCity")
    @Indexed(name = "locationCity", type = "string", stored = false)
    private String locationCity;

    @Field("departmentId")
    private Integer departmentId;

    @Field("departmentName")
    private String departmentName;

    @Field("departmentIdName")
    @Indexed(name = "departmentIdName", type = "string", stored = false)
    private String departmentIdName;

    @Field("driverId")
    private String driverId;

    @Field("passportNumber")
    private String passportNumber;

    @Field("passportIssuedBy")
    private String passportIssuedBy;

    @Field("passportIssuedId")
    private Integer passportIssuedId;

    @Field("insuranceNumber")
    private String insuranceNumber;

    @Field("visaNumber")
    private String visaNumber;

    @Field("agentName")
    private String agentName;

    @Field("bankName")
    private String bankName;

    @Field("accountNumber")
    private String accountNumber;

    @Field("accountName")
    private String accountName;

    @Field("bankAddress")
    private String bankAddress;

    @Field("swiftCode")
    private String swiftCode;

    @Field("sortCode")
    private String sortCode;

    @Field("ibanCode")
    private String ibanCode;

    @Field("wageRate")
    private Double wageRate;

    @Field("clientChargeRate")
    private Double clientChargeRate;

    @Field("countryId")
    private Integer countryId;

    @Field("countryName")
    private String countryName;

    @Field("countryCode")
    private String countryCode;

    @Field("countryIdCode")
    @Indexed(name = "countryIdCode", type = "string", stored = false)
    private String countryIdCode;

    @Field("countryIdCodeName")
    @Indexed(name = "countryIdCodeName", type = "string", stored = false)
    private String countryIdCodeName;

    @Field("stateId")
    private Integer stateId;

    @Field("stateName")
    private String stateName;

    @Field("stateIdName")
    @Indexed(name = "stateIdName", type = "string", stored = false)
    private String stateIdName;

    @Field("street")
    private String street;

    @Field("street2")
    private String street2;

    @Field("city")
    private String city;

    @Field("postCode")
    private String postCode;

    @Field("createdDate")
    private Date createdDate;

    @Field("lastUpdate")
    private Date lastUpdate;

    @Field("birthDate")
    private Date birthDate;

    @Field("hireDate")
    private Date hireDate;

    @Field("endDate")
    private Date endDate;

    @Field("passportIssueDate")
    private Date passportIssueDate;

    @Field("passportExpireDate")
    private Date passportExpireDate;

    @Field("visaIssueDate")
    private Date visaIssueDate;

    @Field("visaExpireDate")
    private Date visaExpireDate;

    @Field("insuranceExpiryDate")
    private Date insuranceExpiryDate;

    @Field("genderName")
    private String genderName;

    @Field("currencyId")
    private Integer currencyId;

    @Field("currencyName")
    private String currencyName;

    @Field("currencyIdName")
    @Indexed(name = "currencyIdName", type = "string", stored = false)
    private String currencyIdName;

    @Field("supervisorId")
    private Integer supervisorId;

    @Field("supervisorName")
    private String supervisorName;

    @Field("supervisorIdName")
    @Indexed(name = "supervisorIdName", type = "string", stored = false)
    private String supervisorIdName;

    @Field("payrollBatchId")
    @Indexed(name = "payrollBatchId", type = "pints")
    private List<Integer> payrollBatchId = new ArrayList<>();

    @Field("openingBalanceDays")
    private Double openingBalanceDays;

    @Field("probationDays")
    private Double probationDays;

    @Field("middleName")
    private String middleName;

    @Field("roleAll")
    private String roleAll;

    @Field("statusIdCode")
    @Indexed(name = "statusIdCode", type = "string", stored = false)
    private String statusIdCode;

    @Field("salaryAmount")
    private Double salaryAmount;

    @Field("qualificationId")
    private Integer qualificationId;

    @Field("qualificationName")
    private String qualificationName;

    @Field("qualificationIdName")
    @Indexed(name = "qualificationIdName", type = "string", stored = false)
    private String qualificationIdName;

    @Field("timeslotId")
    private Integer timeslotId;

    @Field("timeslotName")
    private String timeslotName;

    @Field("timeslotIdName")
    @Indexed(name = "timeslotIdName", type = "string", stored = false)
    private String timeslotIdName;

    @Field("contactId")
    private Integer contactId;

    @Field("contactName")
    private String contactName;

    @Field("contactIdName")
    @Indexed(name = "contactIdName", type = "string", stored = false)
    private String contactIdName;

    @Field("positionNameUz")
    private String positionNameUz;

    @Field("positionNameRu")
    private String positionNameRu;

    @Field("positionNameEn")
    private String positionNameEn;

    @Field("positionNameAr")
    private String positionNameAr;

    @Field("locationNameUz")
    private String locationNameUz;

    @Field("locationNameRu")
    private String locationNameRu;

    @Field("locationNameEn")
    private String locationNameEn;

    @Field("locationNameAr")
    private String locationNameAr;

    @Field("departmentNameUz")
    private String departmentNameUz;

    @Field("departmentNameRu")
    private String departmentNameRu;

    @Field("departmentNameEn")
    private String departmentNameEn;

    @Field("departmentNameAr")
    private String departmentNameAr;

    @Field("positionTypeId")
    private Integer positionTypeId;

    @Field("positionTypeName")
    private String positionTypeName;

    @Field("positionTypeNameUz")
    private String positionTypeNameUz;

    @Field("positionTypeNameRu")
    private String positionTypeNameRu;

    @Field("positionTypeNameEn")
    private String positionTypeNameEn;

    @Field("positionTypeNameAr")
    private String positionTypeNameAr;

    @Field("positionTypeIdName")
    @Indexed(name = "positionTypeIdName", type = "string", stored = false)
    private String positionTypeIdName;

    @Field("martialStatusId")
    private Integer martialStatusId;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public Long getEmployeeIntegerNumber() {
        return employeeIntegerNumber;
    }

    public void setEmployeeIntegerNumber(Long employeeIntegerNumber) {
        this.employeeIntegerNumber = employeeIntegerNumber;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionIdName() {
        return positionIdName;
    }

    public void setPositionIdName(String positionIdName) {
        this.positionIdName = positionIdName;
    }

    public List<Integer> getRoleId() {
        return roleId;
    }

    public void setRoleId(List<Integer> roleId) {
        this.roleId = roleId;
    }

    public List<String> getRoleName() {
        return roleName;
    }

    public void setRoleName(List<String> roleName) {
        this.roleName = roleName;
    }

    public List<String> getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(List<String> roleCode) {
        this.roleCode = roleCode;
    }

    public List<String> getRoleIdName() {
        return roleIdName;
    }

    public void setRoleIdName(List<String> roleIdName) {
        this.roleIdName = roleIdName;
    }

    public List<Integer> getLanguageId() {
        return languageId;
    }

    public void setLanguageId(List<Integer> languageId) {
        this.languageId = languageId;
    }

    public List<String> getLanguageName() {
        return languageName;
    }

    public void setLanguageName(List<String> languageName) {
        this.languageName = languageName;
    }

    public List<String> getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(List<String> languageCode) {
        this.languageCode = languageCode;
    }

    public List<String> getLanguageIdName() {
        return languageIdName;
    }

    public void setLanguageIdName(List<String> languageIdName) {
        this.languageIdName = languageIdName;
    }

    public List<Integer> getSkillId() {
        return skillId;
    }

    public void setSkillId(List<Integer> skillId) {
        this.skillId = skillId;
    }

    public List<String> getSkillName() {
        return skillName;
    }

    public void setSkillName(List<String> skillName) {
        this.skillName = skillName;
    }

    public List<String> getSkillIdName() {
        return skillIdName;
    }

    public void setSkillIdName(List<String> skillIdName) {
        this.skillIdName = skillIdName;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusIdName() {
        return statusIdName;
    }

    public void setStatusIdName(String statusIdName) {
        this.statusIdName = statusIdName;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationIdName() {
        return locationIdName;
    }

    public void setLocationIdName(String locationIdName) {
        this.locationIdName = locationIdName;
    }

    public String getLocationState() {
        return locationState;
    }

    public void setLocationState(String locationState) {
        this.locationState = locationState;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentIdName() {
        return departmentIdName;
    }

    public void setDepartmentIdName(String departmentIdName) {
        this.departmentIdName = departmentIdName;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPassportIssuedBy() {
        return passportIssuedBy;
    }

    public void setPassportIssuedBy(String passportIssuedBy) {
        this.passportIssuedBy = passportIssuedBy;
    }

    public Integer getPassportIssuedId() {
        return passportIssuedId;
    }

    public void setPassportIssuedId(Integer passportIssuedId) {
        this.passportIssuedId = passportIssuedId;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public String getVisaNumber() {
        return visaNumber;
    }

    public void setVisaNumber(String visaNumber) {
        this.visaNumber = visaNumber;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getIbanCode() {
        return ibanCode;
    }

    public void setIbanCode(String ibanCode) {
        this.ibanCode = ibanCode;
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

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryIdCode() {
        return countryIdCode;
    }

    public void setCountryIdCode(String countryIdCode) {
        this.countryIdCode = countryIdCode;
    }

    public String getCountryIdCodeName() {
        return countryIdCodeName;
    }

    public void setCountryIdCodeName(String countryIdCodeName) {
        this.countryIdCodeName = countryIdCodeName;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateIdName() {
        return stateIdName;
    }

    public void setStateIdName(String stateIdName) {
        this.stateIdName = stateIdName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getPassportIssueDate() {
        return passportIssueDate;
    }

    public void setPassportIssueDate(Date passportIssueDate) {
        this.passportIssueDate = passportIssueDate;
    }

    public Date getPassportExpireDate() {
        return passportExpireDate;
    }

    public void setPassportExpireDate(Date passportExpireDate) {
        this.passportExpireDate = passportExpireDate;
    }

    public Date getVisaIssueDate() {
        return visaIssueDate;
    }

    public void setVisaIssueDate(Date visaIssueDate) {
        this.visaIssueDate = visaIssueDate;
    }

    public Date getVisaExpireDate() {
        return visaExpireDate;
    }

    public void setVisaExpireDate(Date visaExpireDate) {
        this.visaExpireDate = visaExpireDate;
    }

    public Date getInsuranceExpiryDate() {
        return insuranceExpiryDate;
    }

    public void setInsuranceExpiryDate(Date insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyIdName() {
        return currencyIdName;
    }

    public void setCurrencyIdName(String currencyIdName) {
        this.currencyIdName = currencyIdName;
    }

    public Integer getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Integer supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getSupervisorIdName() {
        return supervisorIdName;
    }

    public void setSupervisorIdName(String supervisorIdName) {
        this.supervisorIdName = supervisorIdName;
    }

    public List<Integer> getPayrollBatchId() {
        return payrollBatchId;
    }

    public void setPayrollBatchId(List<Integer> payrollBatchId) {
        this.payrollBatchId = payrollBatchId;
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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getRoleAll() {
        return roleAll;
    }

    public void setRoleAll(String roleAll) {
        this.roleAll = roleAll;
    }

    public String getStatusIdCode() {
        return statusIdCode;
    }

    public void setStatusIdCode(String statusIdCode) {
        this.statusIdCode = statusIdCode;
    }

    public Double getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(Double salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public Integer getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(Integer qualificationId) {
        this.qualificationId = qualificationId;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }

    public String getQualificationIdName() {
        return qualificationIdName;
    }

    public void setQualificationIdName(String qualificationIdName) {
        this.qualificationIdName = qualificationIdName;
    }

    public Integer getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(Integer timeslotId) {
        this.timeslotId = timeslotId;
    }

    public String getTimeslotName() {
        return timeslotName;
    }

    public void setTimeslotName(String timeslotName) {
        this.timeslotName = timeslotName;
    }

    public String getTimeslotIdName() {
        return timeslotIdName;
    }

    public void setTimeslotIdName(String timeslotIdName) {
        this.timeslotIdName = timeslotIdName;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactIdName() {
        return contactIdName;
    }

    public void setContactIdName(String contactIdName) {
        this.contactIdName = contactIdName;
    }

    public String getPositionNameUz() {
        return positionNameUz;
    }

    public void setPositionNameUz(String positionNameUz) {
        this.positionNameUz = positionNameUz;
    }

    public String getPositionNameRu() {
        return positionNameRu;
    }

    public void setPositionNameRu(String positionNameRu) {
        this.positionNameRu = positionNameRu;
    }

    public String getPositionNameEn() {
        return positionNameEn;
    }

    public void setPositionNameEn(String positionNameEn) {
        this.positionNameEn = positionNameEn;
    }

    public String getPositionNameAr() {
        return positionNameAr;
    }

    public void setPositionNameAr(String positionNameAr) {
        this.positionNameAr = positionNameAr;
    }

    public String getLocationNameUz() {
        return locationNameUz;
    }

    public void setLocationNameUz(String locationNameUz) {
        this.locationNameUz = locationNameUz;
    }

    public String getLocationNameRu() {
        return locationNameRu;
    }

    public void setLocationNameRu(String locationNameRu) {
        this.locationNameRu = locationNameRu;
    }

    public String getLocationNameEn() {
        return locationNameEn;
    }

    public void setLocationNameEn(String locationNameEn) {
        this.locationNameEn = locationNameEn;
    }

    public String getLocationNameAr() {
        return locationNameAr;
    }

    public void setLocationNameAr(String locationNameAr) {
        this.locationNameAr = locationNameAr;
    }

    public String getDepartmentNameUz() {
        return departmentNameUz;
    }

    public void setDepartmentNameUz(String departmentNameUz) {
        this.departmentNameUz = departmentNameUz;
    }

    public String getDepartmentNameRu() {
        return departmentNameRu;
    }

    public void setDepartmentNameRu(String departmentNameRu) {
        this.departmentNameRu = departmentNameRu;
    }

    public String getDepartmentNameEn() {
        return departmentNameEn;
    }

    public void setDepartmentNameEn(String departmentNameEn) {
        this.departmentNameEn = departmentNameEn;
    }

    public String getDepartmentNameAr() {
        return departmentNameAr;
    }

    public void setDepartmentNameAr(String departmentNameAr) {
        this.departmentNameAr = departmentNameAr;
    }

    public Integer getPositionTypeId() {
        return positionTypeId;
    }

    public void setPositionTypeId(Integer positionTypeId) {
        this.positionTypeId = positionTypeId;
    }

    public String getPositionTypeName() {
        return positionTypeName;
    }

    public void setPositionTypeName(String positionTypeName) {
        this.positionTypeName = positionTypeName;
    }

    public String getPositionTypeNameUz() {
        return positionTypeNameUz;
    }

    public void setPositionTypeNameUz(String positionTypeNameUz) {
        this.positionTypeNameUz = positionTypeNameUz;
    }

    public String getPositionTypeNameRu() {
        return positionTypeNameRu;
    }

    public void setPositionTypeNameRu(String positionTypeNameRu) {
        this.positionTypeNameRu = positionTypeNameRu;
    }

    public String getPositionTypeNameEn() {
        return positionTypeNameEn;
    }

    public void setPositionTypeNameEn(String positionTypeNameEn) {
        this.positionTypeNameEn = positionTypeNameEn;
    }

    public String getPositionTypeNameAr() {
        return positionTypeNameAr;
    }

    public void setPositionTypeNameAr(String positionTypeNameAr) {
        this.positionTypeNameAr = positionTypeNameAr;
    }

    public String getPositionTypeIdName() {
        return positionTypeIdName;
    }

    public void setPositionTypeIdName(String positionTypeIdName) {
        this.positionTypeIdName = positionTypeIdName;
    }

    public Integer getMartialStatusId() {
        return martialStatusId;
    }

    public void setMartialStatusId(Integer martialStatusId) {
        this.martialStatusId = martialStatusId;
    }
}
