package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Dilshod Madrahimov on 03.02.2016.
 * * This class for Timesheet API Syc from Excel plugin
 */
public class EmployeeTO implements IsSerializable {
    Integer id;
    String name;
    String number;
    String firstName;
    String lastName;
    String middleName;
    SelectItemTO title;
    String primaryEmail;
    String primaryPhone;
    Long dateOfBirth;
    SelectItemTO gender;
    SelectItemTO paymentMethod;
    SelectItemTO role;
    SelectItemTO status;
    Long hireDate;
    Long fireDate;
    Boolean hasAccess;
    SelectItemTO department;
    SelectItemTO position;
    String nationality;
    SelectItemTO martialStatus;
    ArrayList<CheckListItemTO> spokenLanguages;
    ContactTO contact;
    Double wageRate;
    SelectItemTO employmentMode;
    Double salaryAmount;
    SelectItemTO location;

    AttendanceTO attendance;

    public EmployeeTO() {

    }

    public EmployeeTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public EmployeeTO(EmployeeListItem item) {
        this.id = item.getObjectID();
        this.name = item.getFirstName() + " " + item.getLastName();
        this.number = item.getEmployeeNumber();
    }

    public EmployeeTO(KpiTreeInfo item) {
        this.name = item.getName();
        this.id = item.getEmployeeId();

        if (item.getDepartmentId() != null || item.getDepartmentName() != null) {
            this.department = new SelectItemTO(item.getDepartmentId(), item.getDepartmentName());
        }

        if (item.getPositionId() != null || item.getPositionName() != null) {
            this.position = new SelectItemTO(item.getPositionId(), item.getPositionName());
        }
    }

    public EmployeeTO(ProfileItem item) {
        this.id = item.getEmployeeId();
        this.name = item.getName();
        this.firstName = item.getFirstName();
        this.lastName = item.getLastName();
        this.middleName = item.getMiddleName();
        this.number = item.getEmpCode();
        if (item.getPrimaryEmail() != null) {
            this.primaryEmail = item.getPrimaryEmail();
        }
        if (item.getPrimaryPhone() != null) {
            this.primaryPhone = item.getPrimaryPhone();
        }
        if (item.getStatusId() != null) {
            this.status = new SelectItemTO(item.getStatusId(), item.getStatus(), item.getStatusCode(), "");
        }
        if (item.getDob() != null && item.getDob().getDate() != null) {
            this.dateOfBirth = item.getDob().getDate().getTime();
        }
        if (item.getPositionId() != null) {
            this.position = new SelectItemTO(item.getPositionId(), item.getPosition());
        }
        if (item.getMartialStatusId() != null) {
            this.martialStatus = new SelectItemTO(item.getMartialStatusId(), item.getMartialStatus());
        }
        if (item.getGender() != null) {
            this.gender = new SelectItemTO(item.getGender());
        }
        if (item.getPaymentMethod() != null) {
            this.paymentMethod = new SelectItemTO(item.getPaymentMethod());
        }
        if (item.getSpokenLanguages() != null && item.getSpokenLanguages().length > 0) {
            ArrayList<CheckListItemTO> checkListItemTO = new ArrayList<>();
            for (SelectItem languageItem : item.getSpokenLanguages()) {
                checkListItemTO.add(new CheckListItemTO(languageItem.getId(), languageItem.getName(), null, null, true));
            }
            this.spokenLanguages = checkListItemTO;
        }
        if (item.getEmpModeId() != null) {
            this.employmentMode = new SelectItemTO(item.getEmpModeId(), item.getEmpMode(), item.getEmpCode(), "");
        }
        if (item.getHireDate() != null && item.getHireDate().getDate() != null) {
            this.hireDate = item.getHireDate().getDate().getTime();
        }
        if (item.getFireDate() != null && item.getFireDate().getDate() != null) {
            this.fireDate = item.getFireDate().getDate().getTime();
        }
        if (item.getSalaryGrade() != null && item.getFireDate().getDate() != null) {
            this.fireDate = item.getFireDate().getDate().getTime();
        }
        this.salaryAmount = item.getSalaryAmount();
        if (item.getDepartmentID() != null) {
            this.department = new SelectItemTO(item.getDepartmentID(), item.getDepartment());
        }
        if (item.getLocaleId() != null) {
            this.location = new SelectItemTO(item.getLocaleId(), item.getLocationName());
        }


    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public SelectItemTO getPosition() {
        return position;
    }

    public void setPosition(SelectItemTO position) {
        this.position = position;
    }

    public AttendanceTO getAttendance() {
        return attendance;
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


    public SelectItemTO getRole() {
        return role;
    }

    public void setRole(SelectItemTO role) {
        this.role = role;
    }

    public SelectItemTO getStatus() {
        return status;
    }

    public void setStatus(SelectItemTO status) {
        this.status = status;
    }

    public Long getHireDate() {
        return hireDate;
    }

    public void setHireDate(Long hireDate) {
        this.hireDate = hireDate;
    }

    public Long getFireDate() {
        return fireDate;
    }

    public void setFireDate(Long fireDate) {
        this.fireDate = fireDate;
    }

    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(Boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public SelectItemTO getGender() {
        return gender;
    }

    public void setGender(SelectItemTO gender) {
        this.gender = gender;
    }

    public SelectItemTO getDepartment() {
        return department;
    }

    public void setDepartment(SelectItemTO department) {
        this.department = department;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public ContactTO getContact() {
        return contact;
    }

    public void setContact(ContactTO contact) {
        this.contact = contact;
    }

    public SelectItemTO getMartialStatus() {
        return martialStatus;
    }

    public void setMartialStatus(SelectItemTO martialStatus) {
        this.martialStatus = martialStatus;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public ArrayList<CheckListItemTO> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(ArrayList<CheckListItemTO> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public Double getWageRate() {
        return wageRate;
    }

    public void setWageRate(Double wageRate) {
        this.wageRate = wageRate;
    }

    public void setAttendance(AttendanceTO attendance) {
        this.attendance = attendance;
    }

    public SelectItemTO getTitle() {
        return title;
    }

    public void setTitle(SelectItemTO title) {
        this.title = title;
    }

    public SelectItemTO getEmploymentMode() {
        return employmentMode;
    }

    public void setEmploymentMode(SelectItemTO employmentMode) {
        this.employmentMode = employmentMode;
    }

    public Double getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(Double salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public SelectItemTO getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(SelectItemTO paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public SelectItemTO getLocation() {
        return location;
    }

    public void setLocation(SelectItemTO location) {
        this.location = location;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }
}