package com.edatasite.workforce.gwt.core.client.rpc.employee;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeSolrItem implements IsSerializable {

    private Integer objectId;
    private String employeeNumber;
    private String employeeName;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private SelectItem position;
    private List<SelectItem> role = new ArrayList<>();
    private List<SelectItem> language = new ArrayList<>();
    private List<SelectItem> skill = new ArrayList<>();
    private SelectItem status;
    private SelectItem location;
    private String locationState;
    private String locationCity;
    private SelectItem department;
    private String driverId;
    private String passportNumber;
    private String insuranceNumber;
    private String visaNumber;
    private Double wageRate;
    private Double clientChargeRate;
    private SelectItem country;
    private SelectItem state;
    private String street;
    private String street2;
    private String city;
    private String postCode;
    private Date createdDate;
    private Date lastUpdate;
    private Date birthDate;
    private Date hireDate;
    private Date endDate;
    private Date passportIssueDate;
    private Date passportExpireDate;
    private Date visaIssueDate;
    private Date visaExpireDate;
    private Date insuranceExpiryDate;
    private String genderName;
    private SelectItem currency;
    private SelectItem supervisor;
    private List<Integer> payrollBatchId = new ArrayList<>();
    private Double openingBalanceDays;
    private Double probationDays;
    private String middleName;
    private String roleAll;
    private String statusIdCode;
    private Double salaryAmount;
    private SelectItem qualification;
    private SelectItem timeslot;
    private SelectItem contact;
    private ReferenceLocale positionName;
    private ReferenceLocale locationName;
    private ReferenceLocale departmentName;
    private SelectItem positionType;
    private ReferenceLocale positionTypeLocale;
    private Integer martialStatusId;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public SelectItem getPosition() {
        return position;
    }

    public void setPosition(SelectItem position) {
        this.position = position;
    }

    public List<SelectItem> getRole() {
        return role;
    }

    public void setRole(List<SelectItem> role) {
        this.role = role;
    }

    public List<SelectItem> getLanguage() {
        return language;
    }

    public void setLanguage(List<SelectItem> language) {
        this.language = language;
    }

    public List<SelectItem> getSkill() {
        return skill;
    }

    public void setSkill(List<SelectItem> skill) {
        this.skill = skill;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
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

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
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

    public SelectItem getCountry() {
        return country;
    }

    public void setCountry(SelectItem country) {
        this.country = country;
    }

    public SelectItem getState() {
        return state;
    }

    public void setState(SelectItem state) {
        this.state = state;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public SelectItem getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SelectItem supervisor) {
        this.supervisor = supervisor;
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

    public SelectItem getQualification() {
        return qualification;
    }

    public void setQualification(SelectItem qualification) {
        this.qualification = qualification;
    }

    public SelectItem getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(SelectItem timeslot) {
        this.timeslot = timeslot;
    }

    public SelectItem getContact() {
        return contact;
    }

    public void setContact(SelectItem contact) {
        this.contact = contact;
    }

    public ReferenceLocale getPositionName() {
        return positionName;
    }

    public void setPositionName(ReferenceLocale positionName) {
        this.positionName = positionName;
    }

    public ReferenceLocale getLocationName() {
        return locationName;
    }

    public void setLocationName(ReferenceLocale locationName) {
        this.locationName = locationName;
    }

    public ReferenceLocale getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(ReferenceLocale departmentName) {
        this.departmentName = departmentName;
    }

    public SelectItem getPositionType() {
        return positionType;
    }

    public void setPositionType(SelectItem positionType) {
        this.positionType = positionType;
    }

    public ReferenceLocale getPositionTypeLocale() {
        return positionTypeLocale;
    }

    public void setPositionTypeLocale(ReferenceLocale positionTypeLocale) {
        this.positionTypeLocale = positionTypeLocale;
    }

    public Integer getMartialStatusId() {
        return martialStatusId;
    }

    public void setMartialStatusId(Integer martialStatusId) {
        this.martialStatusId = martialStatusId;
    }
}
