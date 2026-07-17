package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientContact implements IsSerializable {

    private Integer objectID;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String jobType;
    private Integer clientId;
    private Integer countryId;
    private Integer stateId;
    private String zipCode;
    private Boolean active;
    private Boolean primaryContact;
    private Boolean deleted;
    private String position;
    private String refIndNumber;
    private boolean isClientContact;
    //    private Integer contactID;
    private Integer importFileID;
    private String password;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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

    public String getName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Boolean isActive() {
        return active != null ? active : false;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(Boolean primaryContact) {
        this.primaryContact = primaryContact;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isClientContact() {
        return isClientContact;
    }

    public void setClientContact(boolean clientContact) {
        isClientContact = clientContact;
    }

//    public Integer getContactID() {
//        return contactID;
//    }
//
//    public void setContactID(Integer contactID) {
//        this.contactID = contactID;
//    }

    public Integer getImportFileID() {
        return importFileID;
    }

    public void setImportFileID(Integer importFileID) {
        this.importFileID = importFileID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRefIndNumber() {
        return refIndNumber;
    }

    public void setRefIndNumber(String refIndNumber) {
        this.refIndNumber = refIndNumber;
    }
}
