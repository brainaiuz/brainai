package com.edatasite.workforce.gwt.newemployee.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeProjectsListItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 11.01.2008
 * Time: 13:52:45
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeViewItem implements IsSerializable {

    private Integer ObjectID;
    private String firstName;
    private String lastName;
    private String employeeCode;
    private String middleName;
    private String company;
    private Integer companyID;
    private String timeSlot;
    private String homeAddress;
    private String cityTown;
    private String country;
    private String region;
    private String postCode;
    private String homePhone;
    private String mobilePhone;
    private String workPhone;
    private DateNonConvertable endDate;
    private Integer annualAllowance;
    private String trainingNeeds;
    private String grade;
    private String position;
    private String role;
    private DateNonConvertable startDate;
    private String userName;
    private String email;
    private String locationName;
    private EmployeeProjectsListItem[] projects;
    private EmployeeManagedDepartment departments;
    private String gender;
    private String status;
    private String supervisor;
    private String supervisorCode;
    private String locationId;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public DateNonConvertable getEndDate() {
        return endDate;
    }

    public void setEndDate(DateNonConvertable endDate) {
        this.endDate = endDate;
    }

    public Integer getAnnualAllowance() {
        return annualAllowance;
    }

    public void setAnnualAllowance(Integer annualAllowance) {
        this.annualAllowance = annualAllowance;
    }

    public String getTrainingNeeds() {
        return trainingNeeds;
    }

    public void setTrainingNeeds(String trainingNeeds) {
        this.trainingNeeds = trainingNeeds;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getObjectID() {
        return ObjectID;
    }

    public void setObjectID(Integer objectID) {
        ObjectID = objectID;
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

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public EmployeeProjectsListItem[] getProjects() {
        return projects;
    }

    public void setProjects(EmployeeProjectsListItem[] projects) {
        this.projects = projects;
    }

    public EmployeeManagedDepartment getDepartments() {
        return departments;
    }

    public void setDepartments(EmployeeManagedDepartment departments) {
        this.departments = departments;
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

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getSupervisorCode() {
        return supervisorCode;
    }

    public void setSupervisorCode(String supervisorCode) {
        this.supervisorCode = supervisorCode;
    }
}