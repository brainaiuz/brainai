package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by umakarimov on 9/21/15.
 */
public class Office365User extends Office365BaseItem {
    private String objectId;

    private String surname;
    private String displayName;
    private String userPrincipalName;

    private String mail;
    private String mailNickname;
    private String mobile;
    private String telephoneNumber;
    private String facsimileTelephoneNumber;

    private ArrayList<String> otherMails = new ArrayList();

    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String streetAddress;
    private String usageLocation;

    private String jobTitle;
    private String department;

    private String userType;
    private String preferredLanguage;

    public Office365User() {
    }

    /**
     * @param data
     */
    public Office365User(JSONObject data) {
        this.objectId = this.getString(data, "id");

        this.surname = this.getString(data, "surname");
        this.displayName = this.getString(data, "displayName");
        this.userPrincipalName = this.getString(data, "userPrincipalName");

        this.mail = this.getString(data, "mail");
        this.mailNickname = this.getString(data, "mailNickname");
        this.mobile = this.getString(data, "mobile");
        this.telephoneNumber = this.getString(data, "telephoneNumber");
        this.facsimileTelephoneNumber = this.getString(data, "facsimileTelephoneNumber");

        this.city = this.getString(data, "city");
        this.state = this.getString(data, "state");
        this.country = this.getString(data, "country");
        this.postalCode = this.getString(data, "postalCode");
        this.streetAddress = this.getString(data, "streetAddress");
        this.usageLocation = this.getString(data, "usageLocation");

        this.jobTitle = this.getString(data, "jobTitle");
        this.department = this.getString(data, "department");

        this.userType = this.getString(data, "userType");
        this.preferredLanguage = this.getString(data, "preferredLanguage");

        this.otherMails = this.getArrayList(data, "otherMails", Office365BaseItem.stringMapper);
    }

    public Office365User(JSONObject data, boolean isSharepoint) {
        this.objectId = String.valueOf(((JSONObject) ((ArrayList) data.get("UserProfileProperties")).get(99)).get("Value"));

        this.surname = this.getString(data, "surname");
        this.displayName = this.getString(data, "DisplayName");
        this.userPrincipalName = this.getString(data, "userPrincipalName");

        this.mail = this.getString(data, "Email");
        this.mailNickname = this.getString(data, "mailNickname");
        this.mobile = this.getString(data, "mobile");
        this.telephoneNumber = this.getString(data, "telephoneNumber");
        this.facsimileTelephoneNumber = this.getString(data, "facsimileTelephoneNumber");

        this.city = this.getString(data, "city");
        this.state = this.getString(data, "state");
        this.country = this.getString(data, "country");
        this.postalCode = this.getString(data, "postalCode");
        this.streetAddress = this.getString(data, "streetAddress");
        this.usageLocation = this.getString(data, "usageLocation");

        this.jobTitle = this.getString(data, "jobTitle");
        this.department = this.getString(data, "department");

        this.userType = this.getString(data, "userType");
        this.preferredLanguage = this.getString(data, "preferredLanguage");

        this.otherMails = this.getArrayList(data, "otherMails", Office365BaseItem.stringMapper);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("objectId", this.getObjectId());
        json.put("surname", this.getSurname());
        json.put("displayName", this.getDisplayName());
        json.put("userPrincipalName", this.getUserPrincipalName());
        json.put("mail", this.getMail());
        json.put("mailNickname", this.getMailNickname());
        json.put("mobile", this.getMobile());
        json.put("telephoneNumber", this.getTelephoneNumber());
        json.put("facsimileTelephoneNumber", this.getFacsimileTelephoneNumber());
        json.put("city", this.getCity());
        json.put("state", this.getState());
        json.put("country", this.getCountry());
        json.put("postalCode", this.getPostalCode());
        json.put("streetAddress", this.getStreetAddress());
        json.put("usageLocation", this.getUsageLocation());
        json.put("jobTitle", this.getJobTitle());
        json.put("department", this.getDepartment());
        json.put("userType", this.getUserType());
        json.put("preferredLanguage", this.getPreferredLanguage());
        json.put("otherMails", this.getOtherMails());

        return json;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMailNickname() {
        return mailNickname;
    }

    public void setMailNickname(String mailNickname) {
        this.mailNickname = mailNickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getFacsimileTelephoneNumber() {
        return facsimileTelephoneNumber;
    }

    public void setFacsimileTelephoneNumber(String facsimileTelephoneNumber) {
        this.facsimileTelephoneNumber = facsimileTelephoneNumber;
    }

    public ArrayList<String> getOtherMails() {
        return otherMails;
    }

    public void setOtherMails(ArrayList<String> otherMails) {
        this.otherMails = otherMails;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getUsageLocation() {
        return usageLocation;
    }

    public void setUsageLocation(String usageLocation) {
        this.usageLocation = usageLocation;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }
}
