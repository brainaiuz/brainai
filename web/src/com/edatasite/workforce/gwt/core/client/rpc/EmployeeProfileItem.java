package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Abror Abdukadirov
 * Date: 18.01.2018 12:29
 */
public class EmployeeProfileItem implements IsSerializable {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String position;
    private String employeeImageUrl;
    private String email;
    private String phone;
    private DateNonConvertable dob;
    private DateNonConvertable hireDate;
    private Address homeAddress;
    private boolean isClientContact;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmployeeImageUrl() {
        return employeeImageUrl;
    }

    public void setEmployeeImageUrl(String employeeImageUrl) {
        this.employeeImageUrl = employeeImageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public DateNonConvertable getDob() {
        return dob;
    }

    public void setDob(DateNonConvertable dob) {
        this.dob = dob;
    }

    public DateNonConvertable getHireDate() {
        return hireDate;
    }

    public void setHireDate(DateNonConvertable hireDate) {
        this.hireDate = hireDate;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public boolean isClientContact() {
        return isClientContact;
    }

    public void setClientContact(boolean clientContact) {
        isClientContact = clientContact;
    }
}
