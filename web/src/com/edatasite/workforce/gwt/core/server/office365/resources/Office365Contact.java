package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by umakarimov on 10/5/15.
 */
public class Office365Contact extends Office365BaseItem {
    private String id;

    private String title;
    private String initials;
    private String surname;
    private String givenName;
    private String middleName;
    private String displayName;
    private String nickName;

    private Date birthday;
    private String generation;
    private String fileAs;
    private String personalNotes;

    private String mobilePhone;
    private ArrayList<String> imAddresses;
    private ArrayList<Office365EmailAddress> emailAddresses;

    private ArrayList<String> homePhones;
    private Office365PhysicalAddress homeAddress;

    private String businessHomePage;
    private ArrayList<String> businessPhones;
    private Office365PhysicalAddress businessAddress;

    private Office365PhysicalAddress otherAddress;

    private String profession;
    private String officeLocation;
    private String companyName;
    private String department;
    private String jobTitle;
    private String manager;
    private String assistantName;

    private String parentFolderId;
    private ArrayList<String> categories;

    private String changeKey;
    private Date dateTimeCreated;
    private Date dateTimeLastModified;

    public Office365Contact() {
    }


    /**
     * @param data
     * @see https://msdn.microsoft.com/office/office365/APi/complex-types-for-mail-contacts-calendar#RESTAPIResourcesContact
     */
    public Office365Contact(JSONObject data) {
        this.id = this.getString(data, "id");
        this.title = this.getString(data, "title");
        this.initials = this.getString(data, "initials");
        this.surname = this.getString(data, "surname");
        this.givenName = this.getString(data, "givenName");
        this.middleName = this.getString(data, "middleName");
        this.displayName = this.getString(data, "displayName");
        this.nickName = this.getString(data, "nickName");
        this.birthday = this.getDate(this.getString(data, "birthday"));
        this.generation = this.getString(data, "generation");
        this.fileAs = this.getString(data, "fileAs");
        this.personalNotes = this.getString(data, "personalNotes");
        this.mobilePhone = this.getString(data, "mobilePhone");
        this.imAddresses = this.getArrayList(data, "imAddresses", stringMapper);
        if (this.getArrayList(data, "emailAddresses", stringMapper).size() > 0) {
            this.emailAddresses = this.getArrayList(data, "emailAddresses", new FieldMapper<Office365EmailAddress>() {
                @Override
                public Office365EmailAddress map(Object item) {
                    Office365EmailAddress emailAddress = new Office365EmailAddress();
                    emailAddress.setName(getString((JSONObject) item, "name"));
                    emailAddress.setAddress(getString((JSONObject) item, "address"));
                    return emailAddress;
                }
            });
        }
        this.homePhones = this.getArrayList(data, "homePhones", stringMapper);
        this.homeAddress = new Office365PhysicalAddress((JSONObject) data.get("homeAddress"));
        this.businessHomePage = this.getString(data, "businessHomePage");
        this.businessPhones = this.getArrayList(data, "businessPhones", stringMapper);
        this.businessAddress = new Office365PhysicalAddress((JSONObject) data.get("businessAddress"));
        /*ArrayList<Office365PhysicalAddress> otherAddress = new ArrayList<>();
        otherAddress.add(new Office365PhysicalAddress((JSONObject) data.get("otherAddress")));*/
        this.otherAddress = new Office365PhysicalAddress((JSONObject) data.get("otherAddress"));
        this.profession = this.getString(data, "profession");
        this.officeLocation = this.getString(data, "officeLocation");
        this.companyName = this.getString(data, "companyName");
        this.department = this.getString(data, "department");
        this.jobTitle = this.getString(data, "jobTitle");
        this.manager = this.getString(data, "manager");
        this.assistantName = this.getString(data, "assistantName");
        this.parentFolderId = this.getString(data, "parentFolderId");
        this.categories = this.getArrayList(data, "categories", stringMapper);
        this.changeKey = this.getString(data, "changeKey");
        this.dateTimeCreated = this.getDate(this.getString(data, "createdDateTime"));
        this.dateTimeLastModified = this.getDate(this.getString(data, "lastModifiedDateTime"));
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("id", this.id);
        json.put("title", this.title);
        json.put("initials", this.initials);
        json.put("surname", this.surname);
        json.put("givenName", this.givenName);
        json.put("middleName", this.middleName);
        json.put("displayName", this.displayName);
        json.put("nickName", this.nickName);
        json.put("birthday", this.formatDate(this.birthday));
        json.put("generation", this.generation);
        json.put("fileAs", this.fileAs);
        json.put("mobilePhone", this.mobilePhone);
        json.put("imAddresses", this.getJSONArray(getImAddressesForExport()));
        json.put("emailAddresses", this.getJSONArray(getEmailAddressesForExport()));
        json.put("homePhones", this.getJSONArray(getHomePhonesForExport()));
        if (this.homeAddress != null && !"".equals(this.homeAddress)) {
            json.put("homeAddress", this.homeAddress.toJSON());
        }
        json.put("businessHomePage", this.businessHomePage);
        if (businessPhones != null && businessPhones.size() > 0) {
            json.put("businessPhones", this.getJSONArray(this.businessPhones));
        }
        if (this.businessAddress != null && !"".equals(this.businessAddress)) {
            json.put("businessAddress", this.businessAddress.toJSON());
        }
        if (otherAddress != null) {
//            json.put("otherAddress", this.getJSONArray(this.otherAddress));
            json.put("otherAddress", this.otherAddress.toJSON());
        }
        json.put("profession", this.profession);
        json.put("officeLocation", this.officeLocation);
        json.put("companyName", this.companyName);
        json.put("department", this.department);
        json.put("jobTitle", this.jobTitle);
        json.put("personalNotes", this.personalNotes);
        json.put("manager", this.manager);
        json.put("assistantName", this.assistantName);
        json.put("parentFolderId", this.parentFolderId);
        if (categories != null && categories.size() > 0) {
            json.put("categories", this.getJSONArray(this.categories));
        }
        json.put("changeKey", this.changeKey);
        json.put("createdDateTime", this.formatDate(this.dateTimeCreated));
        json.put("lastModifiedDateTime", this.formatDate(this.dateTimeLastModified));

        return json;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getFileAs() {
        return fileAs;
    }

    public void setFileAs(String fileAs) {
        this.fileAs = fileAs;
    }

    public String getPersonalNotes() {
        return personalNotes;
    }

    public void setPersonalNotes(String personalNotes) {
        this.personalNotes = personalNotes;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public ArrayList<String> getImAddresses() {
        return imAddresses;
    }

    public ArrayList<String> getImAddressesForExport() {
        if (imAddresses != null && imAddresses.size() > 0) {
            return imAddresses;
        } else {
            /*ArrayList list = new ArrayList();
            String s = "n/a";
            list.add(s);
            return list;*/
            return new ArrayList<>();
        }
    }

    public void setImAddresses(ArrayList<String> imAddresses) {
        this.imAddresses = imAddresses;
    }

    public ArrayList<Office365EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    public ArrayList<Office365EmailAddress> getEmailAddressesForExport() {
        if (emailAddresses != null && emailAddresses.size() > 0) {
            return emailAddresses;
        } else {
            /*ArrayList<Office365EmailAddress> list = new ArrayList<>();

            Commented by Anvar Akramov: Why below code is needed, during the sync we are copying below emails to Office365
            Office365EmailAddress emailAddress = new Office365EmailAddress();
            emailAddress.setName("test@workforcetrack.com");
            emailAddress.setAddress("test@workforcetrack.com");
            list.add(emailAddress);
            return list;*/
            return new ArrayList<>();
        }
    }

    public void setEmailAddresses(ArrayList<Office365EmailAddress> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public ArrayList<String> getHomePhones() {
        return homePhones;
    }

    public ArrayList<String> getHomePhonesForExport() {
        if (homePhones != null && !homePhones.isEmpty()) {
            return homePhones;
        } else {
            /*ArrayList list = new ArrayList();
            String s = "n/a";
            list.add(s);
            return list;*/
            return new ArrayList();
        }
    }

    public void setHomePhones(ArrayList<String> homePhones) {
        this.homePhones = homePhones;
    }

    public Office365PhysicalAddress getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Office365PhysicalAddress homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getBusinessHomePage() {
        return businessHomePage;
    }

    public void setBusinessHomePage(String businessHomePage) {
        this.businessHomePage = businessHomePage;
    }

    public ArrayList<String> getBusinessPhones() {
        return businessPhones;
    }

    public void setBusinessPhones(ArrayList<String> businessPhones) {
        this.businessPhones = businessPhones;
    }

    public Office365PhysicalAddress getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(Office365PhysicalAddress businessAddress) {
        this.businessAddress = businessAddress;
    }

    public Office365PhysicalAddress getOtherAddress() {
        return otherAddress;
    }

    public void setOtherAddress(Office365PhysicalAddress otherAddress) {
        this.otherAddress = otherAddress;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public void setAssistantName(String assistantName) {
        this.assistantName = assistantName;
    }

    public String getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(String parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getChangeKey() {
        return changeKey;
    }

    public void setChangeKey(String changeKey) {
        this.changeKey = changeKey;
    }

    public Date getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(Date dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }

    public Date getDateTimeLastModified() {
        return dateTimeLastModified;
    }

    public void setDateTimeLastModified(Date dateTimeLastModified) {
        this.dateTimeLastModified = dateTimeLastModified;
    }
}
