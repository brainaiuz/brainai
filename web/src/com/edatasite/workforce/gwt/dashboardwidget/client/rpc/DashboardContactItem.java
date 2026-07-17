package com.edatasite.workforce.gwt.dashboardwidget.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * User: Abror Abdukadirov
 * Date: 02.05.2018 16:19
 */
public class DashboardContactItem implements IsSerializable {

    private Integer objectId;
    private String firstName;
    private String lastName;
    private String primaryEmail;
    private String primaryPhone;
    private String imageUrl;
    private Boolean isFavourited;
    private Boolean emailOptOut;
    private Integer crmAccountId;

    private String companyName;
    private String jobTitle;
    private ArrayList<MyUpdateItem> lastEvents = new ArrayList<>();
    private SelectItem[] categories;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
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

    public String getContactName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else {
            if (firstName != null && !"".equals(firstName)) {
                return firstName;
            }
            if (lastName != null && !"".equals(lastName)) {
                return lastName;
            }
        }
        return "";
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean isFavourited() {
        return isFavourited != null ? isFavourited : false;
    }

    public void setFavourited(Boolean favourited) {
        isFavourited = favourited;
    }

    public Boolean getEmailOptOut() {
        return emailOptOut != null ? emailOptOut : false;
    }

    public void setEmailOptOut(Boolean emailOptOut) {
        this.emailOptOut = emailOptOut;
    }

    public Integer getCrmAccountId() {
        return crmAccountId;
    }

    public void setCrmAccountId(Integer crmAccountId) {
        this.crmAccountId = crmAccountId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public ArrayList<MyUpdateItem> getLastEvents() {
        if (lastEvents == null) {
            lastEvents = new ArrayList<>();
        }
        return lastEvents;
    }

    public void setLastEvents(ArrayList<MyUpdateItem> lastEvents) {
        this.lastEvents = lastEvents;
    }

    public SelectItem[] getCategories() {
        return categories;
    }

    public void setCategories(SelectItem[] categories) {
        this.categories = categories;
    }
}
